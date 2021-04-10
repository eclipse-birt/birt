/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Read the transfered engine IR designs wrote in EngineIRWriter. The reading
 * sequence of report root: 1. Version. Version id stored in IR, to remember the
 * document changes. 2. Report Version. * in version 0 and 1, read: base path
 * unit 4. Report readDesign: 1. Read design type 2. Read report item design
 * according design type. 3. Read the current design's fields. 4. Read the
 * current design's children.
 * 
 * Version 1: remove read isBookmark of ActionDesign. Version 2: remove read
 * base path and unit of report. Version 3: add extended item's children.
 * Version 4: change the way of writing and reading the style.
 */
public class EngineIRReader implements IOConstants {

	protected boolean checkDesignVersion = false;

	protected DataInputStream dis;

	protected EngineIRReaderImpl reader;

	public EngineIRReader() {
	}

	public EngineIRReader(boolean checkDesignVersion) {
		this.checkDesignVersion = checkDesignVersion;
	}

	public Report read(InputStream in) throws IOException {
		DataInputStream dis = new DataInputStream(in);
		// read the version
		long version = IOUtil.readLong(dis);
		if (version >= ENGINE_IR_VERSION_7 || (version >= ENGINE_IR_VERSION_0 && version <= ENGINE_IR_VERSION_4)) {
			reader = new EngineIRReaderImpl(dis, checkDesignVersion);
			reader.version = version;
		} else {
			// V5, V6 is used in the 2.5.0 development, we won't support it anymore.
			throw new IOException("unsupported version:" + version); //$NON-NLS-1$
		}

		return reader.read();
	}

	public void link(Report report, ReportDesignHandle handle) {
		new ReportItemVisitor(handle, report).link();
	}

	protected static class ReportItemVisitor extends DefaultReportItemVisitorImpl {

		ReportDesignHandle handle;
		Report report;

		ReportItemVisitor(ReportDesignHandle handle, Report report) {
			this.handle = handle;
			this.report = report;
		}

		public void link() {
			report.setReportDesign(handle);
			// link the master pages
			PageSetupDesign pageSetup = report.getPageSetup();
			int masterPageCount = pageSetup.getMasterPageCount();
			for (int i = 0; i < masterPageCount; i++) {
				SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageSetup.getMasterPage(i);
				linkReportElement(masterPage);
				int count = masterPage.getHeaderCount();
				for (int j = 0; j < count; j++) {
					ReportItemDesign item = masterPage.getHeader(j);
					item.accept(this, null);
				}
				count = masterPage.getFooterCount();
				for (int j = 0; j < count; j++) {
					ReportItemDesign item = masterPage.getFooter(j);
					item.accept(this, null);
				}
			}
			// link the body contents
			int count = report.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = report.getContent(i);
				item.accept(this, null);
			}
			// link the report design
			linkReportDesign(report);
		}

		private void linkReportDesign(Report design) {
			setupScriptID(design);
		}

		private void setupScriptID(Report design) {
			ReportDesignHandle designHandle = design.getReportDesign();
			;
			Expression scriptExpr = design.getOnPageStart();
			if (null != scriptExpr && scriptExpr.getType() == Expression.SCRIPT) {
				String id = ModuleUtil
						.getScriptUID(designHandle.getPropertyHandle(ReportDesignHandle.ON_PAGE_START_METHOD));
				((Expression.Script) scriptExpr).setFileName(id);
			}
			scriptExpr = design.getOnPageEnd();
			if (null != scriptExpr && scriptExpr.getType() == Expression.SCRIPT) {
				String id = ModuleUtil
						.getScriptUID(designHandle.getPropertyHandle(ReportDesignHandle.ON_PAGE_END_METHOD));
				((Expression.Script) scriptExpr).setFileName(id);
			}
		}

		protected void linkReportElement(ReportElementDesign element) {
			long id = element.getID();
			DesignElementHandle elementHandle = handle.getElementByID(id);
			if (elementHandle instanceof ReportItemHandle) {
				DesignElementHandle currentView = ((ReportItemHandle) elementHandle).getCurrentView();
				if (currentView != null) {
					elementHandle = currentView;
				}
			}
			element.setHandle(elementHandle);
			setupScriptID(element);
			report.setReportItemInstanceID(id, element);
		}

		private void setupScriptID(ReportElementDesign element) {
			if (element instanceof ReportItemDesign) {
				ReportItemDesign item = (ReportItemDesign) element;
				DesignElementHandle elementHandle = item.getHandle();
				if (elementHandle != null) {
					Expression scriptExpr = item.getOnRender();
					if (null != scriptExpr && scriptExpr.getType() == Expression.SCRIPT) {
						String id = ModuleUtil
								.getScriptUID(elementHandle.getPropertyHandle(IReportItemModel.ON_RENDER_METHOD));
						((Expression.Script) scriptExpr).setFileName(id);
					}
					scriptExpr = item.getOnCreate();
					if (null != scriptExpr && scriptExpr.getType() == Expression.SCRIPT) {
						String id = ModuleUtil
								.getScriptUID(elementHandle.getPropertyHandle(IReportItemModel.ON_CREATE_METHOD));
						((Expression.Script) scriptExpr).setFileName(id);
					}
					scriptExpr = item.getOnPageBreak();
					if (null != scriptExpr && scriptExpr.getType() == Expression.SCRIPT) {
						String id = ModuleUtil
								.getScriptUID(elementHandle.getPropertyHandle(IReportItemModel.ON_PAGE_BREAK_METHOD));
						((Expression.Script) scriptExpr).setFileName(id);
					}
				}
			}
		}

		public Object visitBand(BandDesign band, Object value) {
			linkReportElement(band);
			int count = band.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = band.getContent(i);
				item.accept(this, value);
			}
			return value;
		}

		public Object visitCell(CellDesign cell, Object value) {
			linkReportElement(cell);
			int count = cell.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = cell.getContent(i);
				item.accept(this, value);
			}
			return value;
		}

		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			linkReportElement(container);
			int count = container.getItemCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = container.getItem(i);
				item.accept(this, value);
			}

			return value;
		}

		public Object visitGridItem(GridItemDesign grid, Object value) {
			linkReportElement(grid);

			int count = grid.getColumnCount();
			for (int i = 0; i < count; i++) {
				ColumnDesign column = grid.getColumn(i);
				linkReportElement(column);
			}

			count = grid.getRowCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = grid.getRow(i);
				item.accept(this, value);
			}

			return value;
		}

		public Object visitGroup(GroupDesign group, Object value) {
			linkReportElement(group);
			BandDesign header = group.getHeader();
			if (header != null) {
				header.accept(this, value);
			}
			BandDesign footer = group.getFooter();
			if (footer != null) {
				footer.accept(this, value);
			}
			return value;
		}

		public Object visitListing(ListingDesign listing, Object value) {
			linkReportElement(listing);
			BandDesign header = listing.getHeader();
			if (header != null) {
				header.accept(this, value);
			}
			int count = listing.getGroupCount();
			for (int i = 0; i < count; i++) {
				GroupDesign group = listing.getGroup(i);
				group.accept(this, value);
			}
			BandDesign detail = listing.getDetail();
			if (detail != null) {
				detail.accept(this, null);
			}
			BandDesign footer = listing.getFooter();
			if (footer != null) {
				footer.accept(this, value);
			}
			return value;

		}

		public Object visitTableItem(TableItemDesign table, Object value) {
			visitListing(table, value);

			int count = table.getColumnCount();
			for (int i = 0; i < count; i++) {
				ColumnDesign column = table.getColumn(i);
				linkReportElement(column);
			}
			return value;
		}

		public Object visitReportItem(ReportItemDesign item, Object value) {
			linkReportElement(item);
			return value;
		}

		public Object visitRow(RowDesign row, Object value) {
			linkReportElement(row);
			int count = row.getCellCount();
			for (int i = 0; i < count; i++) {
				CellDesign cell = row.getCell(i);
				cell.accept(this, value);
			}
			return value;
		}

		public Object visitExtendedItem(ExtendedItemDesign extendedItem, Object value) {
			linkReportElement(extendedItem);

			List children = extendedItem.getChildren();
			for (int i = 0; i < children.size(); i++) {
				((ReportItemDesign) children.get(i)).accept(this, null);
			}
			return value;
		}

		public Object visitImageItem(ImageItemDesign image, Object value) {
			linkReportElement(image);
			if (image.getImageSource() == ImageItemDesign.IMAGE_NAME && image.getImageName() == null) {
				ImageHandle imageHandle = (ImageHandle) image.getHandle();
				image.setImageName(Expression.newConstant(imageHandle.getImageName()));
			}
			return value;
		}
	}
}
