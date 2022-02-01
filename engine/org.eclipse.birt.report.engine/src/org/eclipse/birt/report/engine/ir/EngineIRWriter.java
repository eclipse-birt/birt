/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Write the engine transfered IR report. The Writing sequence of report root:
 * 1. Version. Version id stored in IR, to remember the document changes. 2.
 * Report Version. * in version 0 and 1, write: base path unit 4. Report Write
 * design: 1. Write design type 2. Write report item design. 3. Write the
 * current design's fields( field type and field value ). 4. Write the current
 * design's children.
 * 
 * Version 1: remove write isBookmark of ActionDesign. Version 2: remove write
 * base path and unit of report. Version 3: add extended item's children.
 * Version 4: change the way of writing and reading the style.
 * 
 * <h4>Version 6
 * <h4>support the page script/page variable in the design and master page.
 * <h4>version 7</h4> From that version, we separate the expression with the
 * string.
 */
public class EngineIRWriter implements IOConstants {

	protected String scriptLanguage = Expression.SCRIPT_JAVASCRIPT;

	public void write(OutputStream out, Report design) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);

		// stream version number
		IOUtil.writeLong(dos, ENGINE_IR_VERSION_CURRENT);

		// design version number
		IOUtil.writeString(dos, DesignSchemaConstants.REPORT_VERSION);

		writeReport(dos, design);

		dos.flush();
	}

	private void writeReport(DataOutputStream dos, Report design) throws IOException {
		// report has total 8 segments now.
		IOUtil.writeShort(dos, (short) 9);

		IOUtil.writeShort(dos, FIELD_REPORT_VERSION);
		IOUtil.writeString(dos, design.getVersion());

		IOUtil.writeShort(dos, FIELD_REPORT_LOCALE);
		IOUtil.writeString(dos, design.getLocale());

		// write report styles and rootStyle
		IOUtil.writeShort(dos, FIELD_REPORT_STYLES);
		writeReportStyles(dos, design);

		// write named expressions
		IOUtil.writeShort(dos, FIELD_REPORT_USER_PROPERTIES);
		writeExprMap(dos, design.getUserProperties());

		// write the page variables
		IOUtil.writeShort(dos, FIELD_REPORT_VARIABLE);
		writeReportVariable(dos, design);

		// write the onPageStart
		IOUtil.writeShort(dos, FIELD_ON_PAGE_START);
		Expression onPageStart = design.getOnPageStart();
		writeExpression(dos, onPageStart);

		// write the on page end
		IOUtil.writeShort(dos, FIELD_ON_PAGE_END);
		Expression onPageEnd = design.getOnPageEnd();
		writeExpression(dos, onPageEnd);

		// write the master pages
		IOUtil.writeShort(dos, FIELD_REPORT_MASTER_PAGES);
		ReportItemWriter writer = new ReportItemWriter(dos);
		writeReportPageSetup(dos, writer, design);

		// write the report body
		IOUtil.writeShort(dos, FIELD_REPORT_BODY);
		writeReportBodyContent(dos, writer, design);
	}

	private void writeReportStyles(DataOutputStream dos, Report design) throws IOException {
		// style informations
		Map styles = design.getStyles();
		IOUtil.writeInt(dos, styles.size());
		Iterator iter = styles.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String styleName = (String) entry.getKey();
			IStyle style = (IStyle) entry.getValue();
			IOUtil.writeString(dos, styleName);
			style.write(dos);
		}
		String rootStyleName = design.getRootStyleName();
		IOUtil.writeString(dos, rootStyleName);
	}

	private void writeReportVariable(DataOutputStream dos, Report design) throws IOException {
		Collection<PageVariableDesign> vars = design.getPageVariables();
		IOUtil.writeInt(dos, vars.size());
		for (PageVariableDesign var : vars) {
			IOUtil.writeString(dos, var.getName());
			IOUtil.writeString(dos, var.getScope());
			writeExpression(dos, var.getDefaultValue());
		}
	}

	private void writeReportPageSetup(DataOutputStream dos, ReportItemWriter writer, Report design) throws IOException {
		// page setup
		PageSetupDesign pageSetup = design.getPageSetup();
		int masterPageCount = pageSetup.getMasterPageCount();
		IOUtil.writeInt(dos, masterPageCount);
		for (int i = 0; i < masterPageCount; i++) {
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageSetup.getMasterPage(i);
			writer.writeMasterPage(masterPage);
		}
	}

	private void writeReportBodyContent(DataOutputStream dos, ReportItemWriter writer, Report design)
			throws IOException {
		// write the body conent
		int count = design.getContentCount();
		IOUtil.writeInt(dos, count);
		for (int i = 0; i < count; i++) {
			// Write report item designs in report body.
			ReportItemDesign item = design.getContent(i);
			writer.write(item);
		}
	}

	private class ReportItemWriter extends DefaultReportItemVisitorImpl {

		DataOutputStream dos;
		ByteArrayOutputStream bout;
		DataOutputStream bdos;

		ReportItemWriter(DataOutputStream dos) {
			this.dos = dos;
			this.bout = new ByteArrayOutputStream();
			this.bdos = new DataOutputStream(bout);
		}

		public void write(ReportItemDesign design) throws IOException {

			Object exception = design.accept(this, null);
			if (exception != null) {
				throw (IOException) exception;
			}

		}

		public void writeMasterPage(SimpleMasterPageDesign masterPage) throws IOException {
			bout.reset();
			writeSimpleMasterPage(bdos, masterPage);
			bdos.flush();

			IOUtil.writeShort(dos, SIMPLE_MASTER_PAGE_DESIGN);
			IOUtil.writeBytes(dos, bout.toByteArray());

			int count = masterPage.getHeaderCount();
			IOUtil.writeInt(dos, count);
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = masterPage.getHeader(i);
				Object exception = item.accept(this, null);
				if (exception != null) {
					throw (IOException) exception;
				}
			}
			count = masterPage.getFooterCount();
			IOUtil.writeInt(dos, count);
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = masterPage.getFooter(i);
				Object exception = item.accept(this, null);
				if (exception != null) {
					throw (IOException) exception;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#
		 * visitAutoTextItem(org.eclipse.birt.report.engine.ir.AutoTextItemDesign,
		 * java.lang.Object)
		 */
		public Object visitAutoTextItem(AutoTextItemDesign autoText, Object value) {
			try {
				bout.reset();
				writeAutoText(bdos, autoText);
				bdos.flush();

				IOUtil.writeShort(dos, AUTO_TEXT_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitCell(CellDesign cell, Object value) {
			try {
				bout.reset();
				writeCell(bdos, cell);
				bdos.flush();
				IOUtil.writeShort(dos, CELL_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				int contentCount = cell.getContentCount();
				IOUtil.writeInt(dos, contentCount);
				for (int i = 0; i < contentCount; i++) {
					ReportItemDesign content = cell.getContent(i);
					value = content.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitDataItem(DataItemDesign data, Object value) {
			try {
				bout.reset();
				writeData(bdos, data);
				bdos.flush();
				IOUtil.writeShort(dos, DATA_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitExtendedItem(ExtendedItemDesign item, Object value) {
			try {
				bout.reset();
				writeExtended(bdos, item);
				bdos.flush();
				IOUtil.writeShort(dos, EXTENDED_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				List childrenDesigns = item.getChildren();
				IOUtil.writeInt(dos, childrenDesigns.size());
				for (int i = 0; i < childrenDesigns.size(); i++) {
					((ReportItemDesign) childrenDesigns.get(i)).accept(this, value);
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#
		 * visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign,
		 * java.lang.Object)
		 */
		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			try {
				bout.reset();
				writeFreeForm(bdos, container);
				bdos.flush();
				IOUtil.writeShort(dos, FREE_FORM_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
				int contentCount = container.getItemCount();
				IOUtil.writeInt(dos, contentCount);
				for (int i = 0; i < contentCount; i++) {
					ReportItemDesign content = container.getItem(i);
					value = content.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitGridItem(
		 * org.eclipse.birt.report.engine.ir.GridItemDesign, java.lang.Object)
		 */
		public Object visitGridItem(GridItemDesign grid, Object value) {
			try {
				bout.reset();
				writeGrid(bdos, grid);
				bdos.flush();
				IOUtil.writeShort(dos, GRID_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				int rowCount = grid.getRowCount();
				IOUtil.writeInt(dos, rowCount);
				for (int i = 0; i < rowCount; i++) {
					ReportItemDesign row = grid.getRow(i);
					value = row.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitImageItem(ImageItemDesign image, Object value) {
			try {
				bout.reset();
				writeImage(bdos, image);
				bdos.flush();
				IOUtil.writeShort(dos, IMAGE_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitLabelItem(LabelItemDesign label, Object value) {
			try {
				bout.reset();
				writeLabel(bdos, label);
				bdos.flush();
				IOUtil.writeShort(dos, LABEL_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitListBand(
		 * org.eclipse.birt.report.engine.ir.ListBandDesign, java.lang.Object)
		 */
		public Object visitListBand(ListBandDesign band, Object value) {
			try {
				bout.reset();
				writeListBand(bdos, band);
				bdos.flush();
				IOUtil.writeShort(dos, LIST_BAND_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				int count = band.getContentCount();
				IOUtil.writeInt(dos, count);
				for (int i = 0; i < count; i++) {
					ReportItemDesign content = band.getContent(i);
					value = content.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitListGroup(ListGroupDesign group, Object value) {
			try {
				bout.reset();
				writeListGroup(bdos, group);
				bdos.flush();
				IOUtil.writeShort(dos, LIST_GROUP_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				BandDesign header = group.getHeader();

				IOUtil.writeBool(dos, header != null);
				if (header != null) {
					value = header.accept(this, value);
					if (value != null) {
						return value;
					}
				}

				BandDesign footer = group.getFooter();
				IOUtil.writeBool(dos, footer != null);
				if (footer != null) {
					value = footer.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitListItem(ListItemDesign list, Object value) {
			try {
				bout.reset();
				writeList(bdos, list);
				bdos.flush();
				IOUtil.writeShort(dos, LIST_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				BandDesign header = list.getHeader();
				IOUtil.writeBool(dos, header != null);
				if (header != null) {
					value = header.accept(this, value);
					if (value != null) {
						return value;
					}
				}
				int count = list.getGroupCount();
				IOUtil.writeInt(dos, count);
				for (int i = 0; i < count; i++) {
					GroupDesign group = list.getGroup(i);
					value = group.accept(this, value);
					if (value != null) {
						return value;
					}
				}
				BandDesign detail = list.getDetail();
				IOUtil.writeBool(dos, detail != null);
				if (detail != null) {
					value = detail.accept(this, value);
					if (value != null) {
						return value;
					}
				}

				BandDesign footer = list.getFooter();
				IOUtil.writeBool(dos, footer != null);
				if (footer != null) {
					value = footer.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitDynamicTextItem(DynamicTextItemDesign multiLine, Object value) {
			try {
				bout.reset();
				writeDynamicText(bdos, multiLine);
				bdos.flush();
				IOUtil.writeShort(dos, MULTI_LINE_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		public Object visitRow(RowDesign row, Object value) {
			try {
				bout.reset();
				writeRow(bdos, row);
				bdos.flush();
				IOUtil.writeShort(dos, ROW_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				int count = row.getCellCount();
				IOUtil.writeInt(dos, count);
				for (int i = 0; i < count; i++) {
					CellDesign cell = row.getCell(i);
					value = cell.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTableBand
		 * (org.eclipse.birt.report.engine.ir.TableBandDesign, java.lang.Object)
		 */
		public Object visitTableBand(TableBandDesign band, Object value) {
			try {
				bout.reset();
				writeTableBand(bdos, band);
				bdos.flush();
				IOUtil.writeShort(dos, TABLE_BAND_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				int rowCount = band.getRowCount();
				IOUtil.writeInt(dos, rowCount);
				for (int i = 0; i < rowCount; i++) {
					ReportItemDesign row = band.getRow(i);
					value = row.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#
		 * visitTableGroup(org.eclipse.birt.report.engine.ir.TableGroupDesign,
		 * java.lang.Object)
		 */
		public Object visitTableGroup(TableGroupDesign group, Object value) {
			try {
				bout.reset();
				writeTableGroup(bdos, group);
				bdos.flush();
				IOUtil.writeShort(dos, TABLE_GROUP_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				BandDesign header = group.getHeader();
				IOUtil.writeBool(dos, header != null);
				if (header != null) {
					value = header.accept(this, value);
					if (value != null) {
						return value;
					}
				}

				BandDesign footer = group.getFooter();
				IOUtil.writeBool(dos, footer != null);
				if (footer != null) {
					value = footer.accept(this, value);
					if (value != null) {
						return value;
					}
				}
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTableItem
		 * (org.eclipse.birt.report.engine.ir.TableItemDesign, java.lang.Object)
		 */
		public Object visitTableItem(TableItemDesign table, Object value) {
			try {
				bout.reset();
				writeTable(bdos, table);
				bdos.flush();
				IOUtil.writeShort(dos, TABLE_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());

				BandDesign header = table.getHeader();
				IOUtil.writeBool(dos, header != null);
				if (header != null) {
					value = header.accept(this, value);
					if (value != null) {
						return value;
					}
				}

				int count = table.getGroupCount();
				IOUtil.writeInt(dos, count);
				for (int i = 0; i < count; i++) {
					GroupDesign group = table.getGroup(i);
					value = group.accept(this, value);
					if (value != null) {
						return value;
					}
				}
				BandDesign detail = table.getDetail();
				IOUtil.writeBool(dos, detail != null);
				if (detail != null) {
					value = detail.accept(this, value);
					if (value != null) {
						return value;
					}
				}
				BandDesign footer = table.getFooter();
				IOUtil.writeBool(dos, footer != null);
				if (footer != null) {
					value = footer.accept(this, value);
					if (value != null) {
						return value;
					}
				}

			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTemplate(
		 * org.eclipse.birt.report.engine.ir.TemplateDesign, java.lang.Object)
		 */
		public Object visitTemplate(TemplateDesign template, Object value) {
			try {
				bout.reset();
				writeTemplate(bdos, template);
				bdos.flush();
				IOUtil.writeShort(dos, TEMPLATE_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTextItem(
		 * org.eclipse.birt.report.engine.ir.TextItemDesign, java.lang.Object)
		 */
		public Object visitTextItem(TextItemDesign text, Object value) {
			try {
				bout.reset();
				writeText(bdos, text);
				bdos.flush();
				IOUtil.writeShort(dos, TEXT_DESIGN);
				IOUtil.writeBytes(dos, bout.toByteArray());
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

	}

	protected void writeReportElement(DataOutputStream out, ReportElementDesign design) throws IOException {
		long id = design.getID();
		if (id != -1) {
			IOUtil.writeShort(out, FIELD_ID);
			IOUtil.writeLong(out, id);
		}

		String name = design.getName();
		if (name != null) {
			IOUtil.writeShort(out, FIELD_NAME);
			IOUtil.writeString(out, name);
		}

		String ext = design.getExtends();
		if (ext != null) {
			IOUtil.writeShort(out, FIELD_EXTENDS);
			IOUtil.writeString(out, ext);
		}

		String javaClass = design.getJavaClass();
		if (javaClass != null) {
			IOUtil.writeShort(out, FIELD_JAVA_CLASS);
			IOUtil.writeString(out, javaClass);
		}

		Map<String, Expression> userProperties = design.getUserProperties();
		if (userProperties != null && !userProperties.isEmpty()) {
			IOUtil.writeShort(out, FIELD_USER_PROPERTIES);
			writeExprMap(out, userProperties);
		}
	}

	protected void writeStyledElement(DataOutputStream out, StyledElementDesign design) throws IOException {
		writeReportElement(out, design);

		String styleClass = design.getStyleName();
		if (styleClass != null) {
			IOUtil.writeShort(out, FIELD_STYLE_NAME);
			IOUtil.writeString(out, styleClass);
		}

		MapDesign map = design.getMap();
		if (map != null) {
			IOUtil.writeShort(out, FIELD_MAP);
			writeMap(out, map);
		}

		HighlightDesign highlight = design.getHighlight();
		;
		if (highlight != null) {
			IOUtil.writeShort(out, FIELD_HIGHLIGHT);
			writeHighlight(out, highlight);
		}
	}

	protected void writeReportItem(DataOutputStream out, ReportItemDesign design) throws IOException {
		writeStyledElement(out, design);

		DimensionType x = design.getX();
		if (x != null) {
			IOUtil.writeShort(out, FIELD_X);
			writeDimension(out, x);
		}

		DimensionType y = design.getY();
		if (y != null) {
			IOUtil.writeShort(out, FIELD_Y);
			writeDimension(out, y);
		}
		DimensionType height = design.getHeight();
		if (height != null) {
			IOUtil.writeShort(out, FIELD_HEIGHT);
			writeDimension(out, height);
		}
		DimensionType width = design.getWidth();
		if (width != null) {
			IOUtil.writeShort(out, FIELD_WIDTH);
			writeDimension(out, width);
		}

		Expression bookmark = design.getBookmark();
		if (bookmark != null) {
			IOUtil.writeShort(out, FIELD_BOOKMARK);
			writeExpression(out, bookmark);
		}
		Expression toc = design.getTOC();
		if (toc != null) {
			IOUtil.writeShort(out, FIELD_TOC);
			writeExpression(out, toc);
		}

		Expression onCreateScriptExpr = design.getOnCreate();
		if (onCreateScriptExpr != null) {
			IOUtil.writeShort(out, FIELD_ON_CREATE);
			writeExpression(out, onCreateScriptExpr);
		}

		Expression onRenderScriptExpr = design.getOnRender();
		if (onRenderScriptExpr != null) {
			IOUtil.writeShort(out, FIELD_ON_RENDER);
			writeExpression(out, onRenderScriptExpr);
		}
		Expression onPageBreakScriptExpr = design.getOnPageBreak();
		if (onPageBreakScriptExpr != null) {
			IOUtil.writeShort(out, FIELD_ON_PAGE_BREAK);
			writeExpression(out, onPageBreakScriptExpr);
		}

		VisibilityDesign visibility = design.getVisibility();
		if (visibility != null) {
			IOUtil.writeShort(out, FIELD_VISIBILITY);
			writeVisibility(out, visibility);
		}
		ActionDesign action = design.getAction();
		if (action != null) {
			IOUtil.writeShort(out, FIELD_ACTION_V1);
			writeAction(out, action);
		}
		boolean useCachedResult = design.useCachedResult();
		if (useCachedResult) {
			IOUtil.writeShort(out, FIELD_USE_CACHED_RESULT);
			IOUtil.writeBool(out, useCachedResult);
		}

		Expression altText = design.getAltText();
		String altTextKey = design.getAltTextKey();
		if (altText != null || altTextKey != null) {
			IOUtil.writeShort(out, FIELD_ALT_TEXT);
			IOUtil.writeString(out, altTextKey);
			writeExpression(out, altText);
		}
	}

	protected void writeMasterPage(DataOutputStream out, MasterPageDesign design) throws IOException {
		writeStyledElement(out, design);

		String pageType = design.getPageType();
		if (pageType != null) {
			IOUtil.writeShort(out, FIELD_PAGE_TYPE);
			IOUtil.writeString(out, pageType);
		}

		DimensionType width = design.getPageWidth();
		DimensionType height = design.getPageHeight();
		IOUtil.writeShort(out, FIELD_PAGE_SIZE);
		writeDimension(out, width);
		writeDimension(out, height);

		DimensionType top = design.getTopMargin();
		DimensionType left = design.getLeftMargin();
		DimensionType bottom = design.getBottomMargin();
		DimensionType right = design.getRightMargin();

		IOUtil.writeShort(out, FIELD_MARGIN);
		writeDimension(out, top);
		writeDimension(out, left);
		writeDimension(out, bottom);
		writeDimension(out, right);

		String orientation = design.getOrientation();
		if (orientation != null) {
			IOUtil.writeShort(out, FIELD_ORIENTATION);
			IOUtil.writeString(out, orientation);
		}
		String bodyStyleName = design.getBodyStyleName();
		if (bodyStyleName != null) {
			IOUtil.writeShort(out, FIELD_BODY_STYLE);
			IOUtil.writeString(out, bodyStyleName);
		}
	}

	protected void writeSimpleMasterPage(DataOutputStream out, SimpleMasterPageDesign design) throws IOException {
		writeMasterPage(out, design);

		boolean showHeaderOnFirst = design.isShowHeaderOnFirst();
		if (!showHeaderOnFirst) {
			IOUtil.writeShort(out, FIELD_SHOW_HEADER_ON_FIRST);
			IOUtil.writeBool(out, showHeaderOnFirst);
		}
		boolean showFooterOnLast = design.isShowFooterOnLast();
		if (!showFooterOnLast) {
			IOUtil.writeShort(out, FIELD_SHOW_FOOTER_ON_LAST);
			IOUtil.writeBool(out, showFooterOnLast);
		}
		boolean floatingFooter = design.isFloatingFooter();
		if (floatingFooter) {
			IOUtil.writeShort(out, FIELD_FLOATING_FOOTER);
			IOUtil.writeBool(out, floatingFooter);
		}
		DimensionType headerHeight = design.getHeaderHeight();
		if (headerHeight != null) {
			IOUtil.writeShort(out, FEILD_HEADER_HEIGHT);
			writeDimension(out, headerHeight);
		}
		DimensionType footerHeigh = design.getFooterHeight();
		if (footerHeigh != null) {
			IOUtil.writeShort(out, FEILD_FOOTER_HEIGHT);
			writeDimension(out, footerHeigh);
		}

	}

	protected void writeGraphicMasterPage(DataOutputStream out, GraphicMasterPageDesign design) throws IOException {
		writeMasterPage(out, design);

		int columns = design.getColumns();
		if (columns != -1) {
			IOUtil.writeShort(out, FIELD_COLUMNS);
			IOUtil.writeInt(out, columns);
		}

		DimensionType columnSpacing = design.getColumnSpacing();
		if (columnSpacing != null) {
			IOUtil.writeShort(out, FIELD_COLUMN_SPACING);
			writeDimension(out, columnSpacing);
		}
	}

	protected void writeListing(DataOutputStream out, ListingDesign listing) throws IOException {
		writeReportItem(out, listing);

		boolean repeatHeader = listing.isRepeatHeader();
		int pageBreakInterval = listing.getPageBreakInterval();

		if (repeatHeader == true) {
			IOUtil.writeShort(out, FIELD_REPEAT_HEADER);
			IOUtil.writeBool(out, repeatHeader);
		}
		if (pageBreakInterval != -1) {
			IOUtil.writeShort(out, FIELD_PAGE_BREAK_INTERVAL);
			IOUtil.writeInt(out, pageBreakInterval);
		}
	}

	protected void writeGroup(DataOutputStream out, GroupDesign group) throws IOException {
		writeReportItem(out, group);
		int groupLevel = group.getGroupLevel();
		String pageBreakBefore = group.getPageBreakBefore();
		String pageBreakAfter = group.getPageBreakAfter();
		String pageBreakInside = group.getPageBreakInside();
		boolean hideDetail = group.getHideDetail();
		boolean headerRepeat = group.isHeaderRepeat();

		if (groupLevel != -1) {
			IOUtil.writeShort(out, FIELD_GROUP_LEVEL);
			IOUtil.writeInt(out, groupLevel);
		}
		if (pageBreakBefore != null) {
			IOUtil.writeShort(out, FIELD_PAGE_BREAK_BEFORE);
			IOUtil.writeString(out, pageBreakBefore);
		}
		if (pageBreakAfter != null) {
			IOUtil.writeShort(out, FIELD_PAGE_BREAK_AFTER);
			IOUtil.writeString(out, pageBreakAfter);
		}
		if (pageBreakInside != null) {
			IOUtil.writeShort(out, FIELD_PAGE_BREAK_INSIDE);
			IOUtil.writeString(out, pageBreakInside);
		}

		if (headerRepeat == true) {
			IOUtil.writeShort(out, FIELD_HEADER_REPEAT);
			IOUtil.writeBool(out, headerRepeat);
		}
		if (hideDetail == true) {
			IOUtil.writeShort(out, FIELD_HIDE_DETAIL);
			IOUtil.writeBool(out, hideDetail);
		}

	}

	protected void writeBand(DataOutputStream out, BandDesign band) throws IOException {
		writeReportItem(out, band);
		int bandType = band.getBandType();
		if (bandType != -1) {
			IOUtil.writeShort(out, FIELD_BAND_TYPE);
			IOUtil.writeInt(out, bandType);
		}
	}

	protected void writeList(DataOutputStream out, ListItemDesign list) throws IOException {
		writeListing(out, list);

	}

	protected void writeListGroup(DataOutputStream out, ListGroupDesign group) throws IOException {
		writeGroup(out, group);
	}

	protected void writeListBand(DataOutputStream out, ListBandDesign listBand) throws IOException {
		writeBand(out, listBand);
	}

	protected void writeTable(DataOutputStream out, TableItemDesign table) throws IOException {
		writeListing(out, table);

		// write table caption
		String captionKey = table.getCaptionKey();
		String caption = table.getCaption();
		if (caption != null || captionKey != null) {
			IOUtil.writeShort(out, FIELD_CAPTION);
			IOUtil.writeString(out, captionKey);
			IOUtil.writeString(out, caption);
		}

		// write talbe summary
		String summary = table.getSummary();
		if (summary != null) {
			IOUtil.writeShort(out, FIELD_SUMMARY);
			IOUtil.writeString(out, summary);
		}

		// write columns
		int columnCount = table.getColumnCount();
		IOUtil.writeShort(out, FIELD_COLUMNS);
		IOUtil.writeInt(out, columnCount);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bo);
		for (int i = 0; i < columnCount; i++) {
			ColumnDesign column = table.getColumn(i);
			bo.reset();
			writeColumn(buffer, column);
			buffer.flush();
			IOUtil.writeBytes(out, bo.toByteArray());
		}
	}

	protected void writeTableGroup(DataOutputStream out, TableGroupDesign group) throws IOException {
		writeGroup(out, group);
	}

	protected void writeTableBand(DataOutputStream out, TableBandDesign listBand) throws IOException {
		writeBand(out, listBand);
	}

	protected void writeGrid(DataOutputStream out, GridItemDesign grid) throws IOException {
		writeReportItem(out, grid);

		// write caption
		String captionKey = grid.getCaptionKey();
		String caption = grid.getCaption();
		if (caption != null || captionKey != null) {
			IOUtil.writeShort(out, FIELD_CAPTION);
			IOUtil.writeString(out, captionKey);
			IOUtil.writeString(out, caption);
		}

		// write grid summary
		String summary = grid.getSummary();
		if (summary != null) {
			IOUtil.writeShort(out, FIELD_SUMMARY);
			IOUtil.writeString(out, summary);
		}

		// write columns
		int columnCount = grid.getColumnCount();
		IOUtil.writeShort(out, FIELD_COLUMNS);
		IOUtil.writeInt(out, columnCount);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bo);
		for (int i = 0; i < columnCount; i++) {
			ColumnDesign column = grid.getColumn(i);
			bo.reset();
			writeColumn(buffer, column);
			buffer.flush();
			IOUtil.writeBytes(out, bo.toByteArray());
		}
	}

	protected void writeColumn(DataOutputStream out, ColumnDesign column) throws IOException {
		writeStyledElement(out, column);
		boolean isColumnHeader = column.isColumnHeader();
		if (isColumnHeader == true) {
			IOUtil.writeShort(out, FIELD_IS_COLUMN_HEADER);
			IOUtil.writeBool(out, isColumnHeader);
		}
		DimensionType width = column.getWidth();
		if (width != null) {
			IOUtil.writeShort(out, FIELD_WIDTH);
			writeDimension(out, width);
		}
		boolean suppressDuplicate = column.getSuppressDuplicate();
		if (suppressDuplicate) {
			IOUtil.writeShort(out, FIELD_SUPPRESS_DUPLICATE);
			IOUtil.writeBool(out, suppressDuplicate);
		}
		VisibilityDesign visibility = column.getVisibility();
		if (visibility != null) {
			IOUtil.writeShort(out, FIELD_VISIBILITY);
			writeVisibility(out, visibility);
		}
		boolean hasDataItemsInDetail = column.hasDataItemsInDetail();
		if (hasDataItemsInDetail) {
			IOUtil.writeShort(out, FIELD_HAS_DATA_ITEMS_IN_DETAIL);
			IOUtil.writeBool(out, hasDataItemsInDetail);
		}
	}

	protected void writeRow(DataOutputStream out, RowDesign row) throws IOException {
		writeReportItem(out, row);
		boolean isStartOfGroup = row.isStartOfGroup();
		if (isStartOfGroup) {
			IOUtil.writeShort(out, FIELD_IS_START_OF_GROUP);
			IOUtil.writeBool(out, isStartOfGroup);
		}
		boolean repeatable = row.getRepeatable();
		if (!repeatable) {
			IOUtil.writeShort(out, FIELD_IS_REPEATABLE);
			IOUtil.writeBool(out, repeatable);
		}
	}

	protected void writeCell(DataOutputStream out, CellDesign cell) throws IOException {
		writeReportItem(out, cell);
		int column = cell.getColumn();
		if (column != -1) {
			IOUtil.writeShort(out, FIELD_COLUMN);
			IOUtil.writeInt(out, column);
		}
		int colSpan = cell.getColSpan();
		if (colSpan != 1) {
			IOUtil.writeShort(out, FIELD_COL_SPAN);
			IOUtil.writeInt(out, colSpan);
		}
		int rowSpan = cell.getRowSpan();
		if (rowSpan != 1) {
			IOUtil.writeShort(out, FIELD_ROW_SPAN);
			IOUtil.writeInt(out, rowSpan);
		}
		String drop = cell.getDrop();
		if (drop != null) {
			IOUtil.writeShort(out, FIELD_DROP);
			IOUtil.writeString(out, drop);
		}
		Expression headers = cell.getHeaders();
		if (headers != null) {
			IOUtil.writeShort(out, FIELD_HEADERS);
			writeExpression(out, headers);
		}
		String scope = cell.getScope();
		if (scope != null) {
			IOUtil.writeShort(out, FIELD_SCOPE);
			IOUtil.writeString(out, scope);
		}

		boolean displayGroupIcon = cell.getDisplayGroupIcon();
		if (displayGroupIcon) {
			IOUtil.writeShort(out, FIELD_DISPLAY_GROUP_ICON);
			IOUtil.writeBool(out, displayGroupIcon);
		}
		if (cell.hasDiagonalLine()) {
			int diagonalNumber = cell.getDiagonalNumber();
			if (diagonalNumber > 0) {
				IOUtil.writeShort(out, FIELD_DIAGONAL_NUMBER);
				IOUtil.writeInt(out, diagonalNumber);
				String diagonalStyle = cell.getDiagonalStyle();
				if (diagonalStyle != null) {
					IOUtil.writeShort(out, FIELD_DIAGONAL_STYLE);
					IOUtil.writeString(out, diagonalStyle);
				}
				DimensionType diagonalWidth = cell.getDiagonalWidth();
				if (diagonalWidth != null) {
					IOUtil.writeShort(out, FIELD_DIAGONAL_WIDTH);
					writeDimension(out, diagonalWidth);
				}
				String diagonalColor = cell.getDiagonalColor();
				if (diagonalColor != null) {
					IOUtil.writeShort(out, FIELD_DIAGONAL_COLOR);
					IOUtil.writeString(out, diagonalColor);
				}
			}
			int antidiagonalNumber = cell.getAntidiagonalNumber();
			if (antidiagonalNumber > 0) {
				IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_NUMBER);
				IOUtil.writeInt(out, antidiagonalNumber);
				String antidiagonalStyle = cell.getAntidiagonalStyle();
				if (antidiagonalStyle != null) {
					IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_STYLE);
					IOUtil.writeString(out, antidiagonalStyle);
				}
				DimensionType antidiagonalWidth = cell.getAntidiagonalWidth();
				if (antidiagonalWidth != null) {
					IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_WIDTH);
					writeDimension(out, antidiagonalWidth);
				}
				String antidiagonalColor = cell.getAntidiagonalColor();
				if (antidiagonalColor != null) {
					IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_COLOR);
					IOUtil.writeString(out, antidiagonalColor);
				}
			}
		}
	}

	protected void writeFreeForm(DataOutputStream out, FreeFormItemDesign freeForm) throws IOException {
		writeReportItem(out, freeForm);
	}

	protected void writeLabel(DataOutputStream out, LabelItemDesign label) throws IOException {
		writeReportItem(out, label);
		String text = label.getText();
		String textKey = label.getTextKey();
		String helpText = label.getHelpText();
		String helpTextKey = label.getHelpTextKey();

		if (text != null || textKey != null) {
			IOUtil.writeShort(out, FIELD_TEXT);
			IOUtil.writeString(out, textKey);
			IOUtil.writeString(out, text);
		}
		if (helpText != null || helpTextKey != null) {
			IOUtil.writeShort(out, FIELD_HELP_TEXT);
			IOUtil.writeString(out, helpTextKey);
			IOUtil.writeString(out, helpText);
		}
	}

	protected void writeExpression(DataOutputStream out, Expression expr) throws IOException {
		if (expr == null) {
			IOUtil.writeBool(out, true);
			return;
		}
		IOUtil.writeBool(out, false);
		int type = expr.getType();
		IOUtil.writeInt(out, type);
		switch (expr.getType()) {
		case Expression.CONSTANT:
			Expression.Constant cs = (Expression.Constant) expr;
			IOUtil.writeInt(out, cs.getValueType());
			IOUtil.writeString(out, cs.getScriptText());
			break;
		case Expression.SCRIPT:
			Expression.Script sc = (Expression.Script) expr;
			String language = sc.getLanguage();
			if (language == null || scriptLanguage.equals(language)) {
				IOUtil.writeShort(out, FIELD_EXPRESSION_WITHOUT_LANGUAGE);
			} else {
				IOUtil.writeShort(out, FIELD_EXPRESSION_WITH_LANGUAGE);
				IOUtil.writeString(out, language);
			}
			IOUtil.writeString(out, sc.getScriptText());
			break;
		default:
			throw new IOException(MessageConstants.UNSPPORTED_EXPRESSION_TYPE);
		}
	}

	protected void writeData(DataOutputStream out, DataItemDesign data) throws IOException {
		writeReportItem(out, data);

		// String value = data.getValue( );
		String bindingColumn = data.getBindingColumn();
		String helpText = data.getHelpText();
		String helpTextKey = data.getHelpTextKey();
		boolean suppressDuplicate = data.getSuppressDuplicate();
		if (bindingColumn != null) {
			IOUtil.writeShort(out, FIELD_BINDING_COLUMN);
			IOUtil.writeString(out, bindingColumn);
		}
		if (helpText != null || helpTextKey != null) {
			IOUtil.writeShort(out, FIELD_HELP_TEXT);
			IOUtil.writeString(out, helpTextKey);
			IOUtil.writeString(out, helpText);
		}
		if (suppressDuplicate) {
			IOUtil.writeShort(out, FIELD_SUPPRESS_DUPLICATE);
			IOUtil.writeBool(out, suppressDuplicate);
		}
	}

	protected void writeText(DataOutputStream out, TextItemDesign design) throws IOException {
		writeReportItem(out, design);
		String textType = design.getTextType();
		String textKey = design.getTextKey();
		String text = design.getText();
		if (textType != null) {
			IOUtil.writeShort(out, FIELD_TEXT_TYPE);
			IOUtil.writeString(out, textType);
		}
		if (text != null || textKey != null) {
			IOUtil.writeShort(out, FIELD_TEXT);
			IOUtil.writeString(out, textKey);
			IOUtil.writeString(out, text);
		}
		boolean hasExpr = design.hasExpression();
		if (hasExpr) {
			IOUtil.writeShort(out, FIELD_TEXT_HAS_EXPRESSION);
			IOUtil.writeBool(out, hasExpr);
		}
		boolean jTidy = design.isJTidy();
		if (!jTidy) {
			IOUtil.writeShort(out, FIELD_JTIDY);
			IOUtil.writeBool(out, jTidy);
		}
	}

	protected void writeDynamicText(DataOutputStream out, DynamicTextItemDesign design) throws IOException {
		writeReportItem(out, design);
		String contentType = design.getContentType();
		Expression content = design.getContent();
		if (contentType != null) {
			IOUtil.writeShort(out, FIELD_CONTENT_TYPE);
			IOUtil.writeString(out, contentType);
		}
		if (content != null) {
			IOUtil.writeShort(out, FIELD_CONTENT);
			writeExpression(out, content);
		}
		boolean jTidy = design.isJTidy();
		if (!jTidy) {
			IOUtil.writeShort(out, FIELD_JTIDY);
			IOUtil.writeBool(out, jTidy);
		}
	}

	protected void writeImage(DataOutputStream out, ImageItemDesign image) throws IOException {
		writeReportItem(out, image);
		IOUtil.writeShort(out, FIELD_IMAGE_SOURCE);
		int imageSource = image.getImageSource();
		IOUtil.writeInt(out, imageSource);
		switch (imageSource) {
		case ImageItemDesign.IMAGE_NAME:
			writeExpression(out, image.getImageName());
			break;
		case ImageItemDesign.IMAGE_FILE:
			writeExpression(out, image.getImageUri());
			break;
		case ImageItemDesign.IMAGE_URI:
			writeExpression(out, image.getImageUri());
			break;
		case ImageItemDesign.IMAGE_EXPRESSION:
			writeExpression(out, image.getImageExpression());
			writeExpression(out, image.getImageFormat());
			break;
		}

		boolean isFitToContainer = image.isFitToContainer();
		if (isFitToContainer) {
			IOUtil.writeShort(out, FIELD_FIT_TO_CONTAINER);
			IOUtil.writeBool(out, true);
		}

		boolean isProportionalScale = image.isProportionalScale();
		if (isProportionalScale) {
			IOUtil.writeShort(out, FIELD_PROPORTIONAL_SCALE);
			IOUtil.writeBool(out, isProportionalScale);
		}

		String helpText = image.getHelpText();
		String helpTextKey = image.getHelpTextKey();
		if (helpText != null || helpTextKey != null) {
			IOUtil.writeShort(out, FIELD_HELP_TEXT);
			IOUtil.writeString(out, helpTextKey);
			IOUtil.writeString(out, helpText);
		}
	}

	protected void writeExtended(DataOutputStream out, ExtendedItemDesign extended) throws IOException {
		writeReportItem(out, extended);
	}

	protected void writeAutoText(DataOutputStream out, AutoTextItemDesign design) throws IOException {
		writeReportItem(out, design);

		String type = design.getType();
		if (type != null) {
			IOUtil.writeShort(out, FIELD_TYPE);
			IOUtil.writeString(out, type);
		}

		String text = design.getText();
		String textKey = design.getTextKey();
		if (text != null || textKey != null) {
			IOUtil.writeShort(out, FIELD_TEXT);
			IOUtil.writeString(out, textKey);
			IOUtil.writeString(out, text);
		}
	}

	protected void writeTemplate(DataOutputStream out, TemplateDesign design) throws IOException {
		writeReportItem(out, design);

		String allowedType = design.getAllowedType();
		if (allowedType != null) {
			IOUtil.writeShort(out, FIELD_ALLOWED_TYPE);
			IOUtil.writeString(out, allowedType);
		}

		String promptText = design.getPromptText();
		String promptTextKey = design.getPromptTextKey();
		if (promptText != null || promptTextKey != null) {
			IOUtil.writeShort(out, FIELD_PROMPT_TEXT);
			IOUtil.writeString(out, promptTextKey);
			IOUtil.writeString(out, promptText);
		}
	}

	protected void writeDimension(DataOutputStream out, DimensionType dimension) throws IOException {
		IOUtil.writeBool(out, dimension != null);
		if (dimension != null) {
			dimension.writeObject(out);
		}
	}

	protected void writeVisibility(DataOutputStream out, VisibilityDesign visibility) throws IOException {
		int ruleCount = visibility.count();
		IOUtil.writeInt(out, ruleCount);
		for (int i = 0; i < ruleCount; i++) {
			VisibilityRuleDesign rule = visibility.getRule(i);
			IOUtil.writeString(out, rule.getFormat());
			writeExpression(out, rule.getExpression());
		}
	}

	protected void writeMap(DataOutputStream out, MapDesign map) throws IOException {
		int ruleCount = map.getRuleCount();
		IOUtil.writeInt(out, ruleCount);
		for (int i = 0; i < ruleCount; i++) {
			MapRuleDesign rule = map.getRule(i);
			writeRule(out, rule);
			IOUtil.writeString(out, rule.getDisplayText());
			IOUtil.writeString(out, rule.getDisplayKey());
		}
	}

	private void writeRule(DataOutputStream out, RuleDesign rule) throws IOException {
		writeExpression(out, rule.getTestExpression());
		IOUtil.writeString(out, rule.getOperator());
		boolean isValueList = rule.ifValueIsList();
		IOUtil.writeBool(out, isValueList);
		if (isValueList) {
			List<Expression> exprs = rule.getValue1List();
			IOUtil.writeInt(out, exprs.size());
			for (Expression expr : exprs) {
				writeExpression(out, expr);
			}
		} else {
			Expression expr1 = rule.getValue1();
			Expression expr2 = rule.getValue2();
			writeExpression(out, expr1);
			writeExpression(out, expr2);
		}
	}

	protected void writeHighlight(DataOutputStream out, HighlightDesign highlight) throws IOException {
		int ruleCount = highlight.getRuleCount();
		IOUtil.writeInt(out, ruleCount);
		for (int i = 0; i < ruleCount; i++) {
			HighlightRuleDesign rule = highlight.getRule(i);
			writeRule(out, rule);
			rule.getStyle().write(out);
		}
	}

	protected void writeAction(DataOutputStream out, ActionDesign action) throws IOException {
		int actionType = action.getActionType();
		IOUtil.writeInt(out, actionType);
		switch (actionType) {
		case ActionDesign.ACTION_BOOKMARK:
			Expression bookmark = action.getBookmark();
			writeExpression(out, bookmark);
			break;
		case ActionDesign.ACTION_DRILLTHROUGH:
			DrillThroughActionDesign drillThrough = action.getDrillThrough();
			writeDrillThrough(out, drillThrough);
			break;
		case ActionDesign.ACTION_HYPERLINK:
			Expression hyperlink = action.getHyperlink();
			writeExpression(out, hyperlink);
			break;
		}
		String targetWindow = action.getTargetWindow();
		IOUtil.writeString(out, targetWindow);
		String tooltip = action.getTooltip();
		IOUtil.writeString(out, tooltip);
	}

	protected void writeDrillThrough(DataOutputStream out, DrillThroughActionDesign drillThrough) throws IOException {
		Expression reportName = drillThrough.getReportName();
		String fileType = drillThrough.getTargetFileType();
		Map<String, List<Expression>> parameters = drillThrough.getParameters();
		Map search = drillThrough.getSearch();
		String format = drillThrough.getFormat();
		boolean bookmarkType = drillThrough.getBookmarkType();
		Expression bookmark = drillThrough.getBookmark();

		writeExpression(out, reportName);
		IOUtil.writeString(out, fileType);
		writeDrillThroughExprMap(out, parameters);
		IOUtil.writeMap(out, search);
		IOUtil.writeString(out, format);
		IOUtil.writeBool(out, bookmarkType);
		writeExpression(out, bookmark);
	}

	private void writeExprMap(DataOutputStream dos, Map<String, Expression> exprs) throws IOException {
		// named expression

		if (exprs == null) {
			IOUtil.writeInt(dos, 0);
		} else {
			IOUtil.writeInt(dos, exprs.size());
			for (Map.Entry<String, Expression> entry : exprs.entrySet()) {
				String name = entry.getKey();
				Expression expr = entry.getValue();
				IOUtil.writeString(dos, name);
				writeExpression(dos, expr);
			}
		}
	}

	private void writeDrillThroughExprMap(DataOutputStream dos, Map<String, List<Expression>> exprs)
			throws IOException {
		// named expression

		if (exprs == null) {
			IOUtil.writeInt(dos, 0);
		} else {
			IOUtil.writeInt(dos, exprs.size());
			for (Map.Entry<String, List<Expression>> entry : exprs.entrySet()) {
				String name = entry.getKey();
				IOUtil.writeString(dos, name);
				List<Expression> exprList = entry.getValue();
				IOUtil.writeInt(dos, exprList.size());
				for (Expression expr : exprList) {
					writeExpression(dos, expr);
				}
			}
		}
	}

}
