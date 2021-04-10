/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentReader;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.internal.document.IReportContentLoader;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class ReportContentLoaderV1 implements IReportContentLoader {

	protected static Logger logger = Logger.getLogger(ReportContentLoaderV1.class.getName());

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected IContentEmitter emitter;
	protected ReportContentReaderV1 reader;
	protected ReportContentReaderV1 pageReader;
	protected PageHintReaderV1 hintReader;
	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;
	/**
	 * the offset of current read object. The object has been read out, setted in
	 * loadContent();
	 */
	protected long currentOffset;

	protected Stack resultSets = new Stack();

	public ReportContentLoaderV1(ExecutionContext context) throws EngineException {
		this.context = context;
		dataEngine = context.getDataEngine();
		report = context.getReport();

		reportContent = (ReportContent) ContentFactory.createReportContent(report);
		reportContent.setExecutionContext(context);
		context.setReportContent(reportContent);

		reportDoc = (ReportDocumentReader) context.getReportDocument();
		dataEngine.prepare(report, context.getAppContext());
	}

	protected void openReaders() {
		try {
			reader = new ReportContentReaderV1(reportContent, reportDoc, context.getApplicationClassLoader());
			reader.open(ReportDocumentConstants.CONTENT_STREAM);
			pageReader = new ReportContentReaderV1(reportContent, reportDoc, context.getApplicationClassLoader());
			pageReader.open(ReportDocumentConstants.PAGE_STREAM);
			hintReader = new PageHintReaderV1(reportDoc);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Failed to open the content reader", ex);
			closeReaders();
		}
	}

	protected void closeReaders() {
		if (reader != null) {
			reader.close();
			reader = null;
		}
		if (pageReader != null) {
			pageReader.close();
			pageReader = null;
		}
		if (hintReader != null) {
			hintReader.close();
			hintReader = null;
		}

	}

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPage(long pageNumber, int paginationType, IContentEmitter emitter) throws BirtException {
		boolean bodyOnly = paginationType == IReportContentLoader.NO_PAGE
				|| paginationType == IReportContentLoader.SINGLE_PAGE;
		emitter.start(reportContent);
		this.emitter = emitter;
		try {
			openReaders();
			excutePage(pageNumber, bodyOnly);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to load the page", ex);
		} finally {
			emitter.end(reportContent);
			closeReaders();
		}
	}

	public void loadReportlet(long offset, IContentEmitter emitter) throws BirtException {
		emitter.start(reportContent);
		this.emitter = emitter;
		try {
			openReaders();
			// we don't support this feature for V1
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to load the page", ex);
		} finally {
			emitter.end(reportContent);
			closeReaders();
		}
	}

	protected IPageHint loadPageHint(long pageNumber) {
		try {
			return hintReader.getPageHint(pageNumber);
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Failed to load page hint " + pageNumber, ex);
		}
		return null;
	}

	private void excutePage(long pageNumber, boolean bodyOnly) throws BirtException {
		IPageHint pageHint = loadPageHint(pageNumber);
		if (pageHint == null) {
			return;
		}
		IPageContent pageContent = null;
		if (!bodyOnly) {
			pageContent = loadPageContent(pageHint.getOffset());
			if (pageContent == null) {
				return;
			}

			Object generateBy = pageContent.getGenerateBy();
			if (generateBy instanceof SimpleMasterPageDesign) {
				SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) generateBy;
				if (!pageDesign.isShowHeaderOnFirst()) {
					if (pageNumber == 1) {
						pageContent.getHeader().clear();
					}
				}
				if (!pageDesign.isShowFooterOnLast()) {
					if (pageNumber == reportDoc.getPageCount()) {
						pageContent.getFooter().clear();
					}
				}
			}
			emitter.startPage(pageContent);
		}

		PageSection section = pageHint.getSection(0);
		long startOffset = section.startOffset;
		long endOffset = section.endOffset;
		if (startOffset != -1 && endOffset != -1) {
			if (reader != null) {
				reader.setOffset(startOffset);
				IContent content = loadContent(reader);
				while (content != null && currentOffset <= endOffset) {
					content = loadFullContent(content, reader, emitter);
				}
			}
		}

		if (!bodyOnly) {
			emitter.endPage(pageContent);
		}
	}

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPageRange(List pageList, int paginationType, IContentEmitter emitter) throws BirtException {
		boolean bodyOnly = paginationType == IReportContentLoader.NO_PAGE
				|| paginationType == IReportContentLoader.SINGLE_PAGE;
		emitter.start(reportContent);
		this.emitter = emitter;
		try {
			openReaders();
			for (int m = 0; m < pageList.size(); m++) {
				long[] ps = (long[]) pageList.get(m);
				for (long i = ps[0]; i <= ps[1]; i++) {
					excutePage(i, bodyOnly);
				}
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to load the page", ex); //$NON-NLS-1$
		} finally {
			emitter.end(reportContent);
			closeReaders();
		}
	}

	protected IPageContent loadPageContent(long offset) throws BirtException {

		if (pageReader != null) {

			pageReader.setOffset(offset);
			IPageContent pageContent = (IPageContent) loadContent(pageReader);
			if (pageContent == null) {
				return null;
			}

			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageContent.getGenerateBy();

			IContent content = loadContent(pageReader);
			for (int i = 0; i < masterPage.getHeaderCount(); i++) {
				pageContent.getHeader().add(content);
				content = loadFullContent(content, pageReader, new DOMBuildingEmitter());

			}
			for (int i = 0; i < masterPage.getFooterCount(); i++) {
				pageContent.getFooter().add(content);
				content = loadFullContent(content, pageReader, new DOMBuildingEmitter());
			}
			return pageContent;
		}
		return null;
	}

	private static class DOMBuildingEmitter extends ContentEmitterAdapter {

		private IContent parent;

		public DOMBuildingEmitter() {
		}

		public void startContent(IContent content) {
			if (parent != null) {
				parent.getChildren().add(content);
			}
			parent = content;
		}

		public void endContent(IContent content) {
			if (parent != null) {
				parent = (IContent) parent.getParent();
			}
		}
	}

	/**
	 * load all the children of the root from the reader and output them into
	 * emitter.
	 * 
	 * @param root    content to be loaded.
	 * @param reader  reader
	 * @param emitter output emitter.
	 * @return the next content after the root. NULL if at the end of stream.
	 */
	protected IContent loadFullContent(IContent root, ReportContentReaderV1 reader, IContentEmitter emitter)
			throws BirtException {
		Stack contents = new Stack();
		contents.push(root);

		startContent(root, emitter);

		IContent next = loadContent(reader);

		do {
			IContent parent = (IContent) contents.peek();
			if (isChildOf(next, parent)) {
				contents.push(next);
				next.setParent(parent);
				startContent(next, emitter);
				next = loadContent(reader);
			} else {
				endContent(parent, emitter);
				contents.pop();
			}
		} while (!contents.isEmpty());

		return next;
	}

	private boolean isChildOf(DesignElementHandle child, DesignElementHandle parent) {
		DesignElementHandle container = child.getContainer();
		if (container == parent) {
			return true;
		}
		if (container == null) {
			return false;
		}
		return isChildOf(container, parent);
	}

	private boolean isChildOf(IContent content, IContent parent) {
		if (content != null && parent != null) {
			DesignElementHandle handle = getDesignHandle(content);
			DesignElementHandle parentHandle = getDesignHandle(parent);
			if (handle != null && parentHandle != null) {
				return isChildOf(handle, parentHandle);
			} else {
				// we may create empty band for table (table body)
				if (parent instanceof ITableContent && content instanceof ITableBandContent) {
					return true;
				}
				// we may create row for empty band (table body)
				if (parent instanceof ITableBandContent && content instanceof IRowContent) {
					return isRowInBand((IRowContent) content, (ITableBandContent) parent);
				}
				// we may create a empty cell to fix the table layout.
				if (parent instanceof IRowContent && content instanceof ICellContent) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRowInBand(IRowContent row, ITableBandContent band) {
		Object generateBy = row.getGenerateBy();
		if (generateBy instanceof RowDesign) {
			RowDesign rowDesign = (RowDesign) generateBy;
			IElement parent = band.getParent();
			if (parent instanceof ITableContent) {
				ITableContent table = (ITableContent) parent;
				generateBy = table.getGenerateBy();
				if (generateBy instanceof ReportItemDesign) {
					DesignElementHandle rowHandle = rowDesign.getHandle();
					DesignElementHandle tableHandle = ((ReportItemDesign) generateBy).getHandle();
					if (rowHandle.getContainer() == tableHandle
							|| rowHandle.getContainer().getContainer() == tableHandle) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected IContent loadContent(ReportContentReaderV1 reader) {
		if (reader != null) {
			try {
				currentOffset = reader.getOffset();
				IContent content = reader.readContent();
				initializeContent(content);
				return content;
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "load content failed", ex);
			}
		}
		return null;
	}

	protected void initializeContent(IContent content) {
		// set up the report content
		content.setReportContent(reportContent);
		// set up the design object
		InstanceID id = content.getInstanceID();
		if (id != null) {
			long designId = id.getComponentID();
			if (designId != -1) {
				Object generateBy = findReportItem(designId);
				content.setGenerateBy(generateBy);
				if (generateBy instanceof TemplateDesign) {
					TemplateDesign design = (TemplateDesign) generateBy;
					if (content instanceof ILabelContent) {
						ILabelContent labelContent = (ILabelContent) content;
						labelContent.setLabelKey(design.getPromptTextKey());
						labelContent.setLabelText(design.getPromptText());
					}
				}
			}
		}
	}

	private DesignElementHandle getDesignHandle(IContent content) {
		Object generateBy = content.getGenerateBy();
		if (generateBy instanceof ReportElementDesign) {
			return ((ReportElementDesign) generateBy).getHandle();
		}
		return null;
	}

	private HashMap generateByIDs;

	/**
	 * find the report element by the design id. we need get the engine's IR from
	 * the design id, so we can't use the mode's getElementByID().
	 * 
	 * @param designId design id
	 * @return design object (engine)
	 */
	protected Object findReportItem(long designId) {
		if (generateByIDs == null) {
			generateByIDs = new HashMap();
			new GenerateIDMapVisitor(generateByIDs).visitReport(report);
		}
		return generateByIDs.get(Long.valueOf(designId));
	}

	/**
	 * generate a hash map used to find the element by element id.
	 * 
	 * It visits the report design, add the element id and design object into the
	 * hash map.
	 * 
	 */
	protected static class GenerateIDMapVisitor extends DefaultReportItemVisitorImpl {

		/**
		 * map used to store the id, design object.
		 */
		private HashMap IDMaps;

		/**
		 * create a visitor, the maps should be stroe into map.
		 * 
		 * @param map map used to store the id->design mapping.
		 */
		public GenerateIDMapVisitor(HashMap map) {
			IDMaps = map;
		}

		/**
		 * visit the report, store the id->design mapping.
		 * 
		 * It is the main entry of the class
		 * 
		 * @param report the visited report
		 */
		public void visitReport(Report report) {
			PageSetupDesign pageSetup = report.getPageSetup();
			for (int i = 0; i < pageSetup.getMasterPageCount(); i++) {
				MasterPageDesign masterPage = (MasterPageDesign) pageSetup.getMasterPage(i);
				IDMaps.put(Long.valueOf(masterPage.getID()), masterPage);
				if (masterPage instanceof SimpleMasterPageDesign) {
					SimpleMasterPageDesign simpleMasterPage = (SimpleMasterPageDesign) masterPage;
					for (int j = 0; j < simpleMasterPage.getHeaderCount(); j++) {
						simpleMasterPage.getHeader(j).accept(this, null);
					}
					for (int j = 0; j < simpleMasterPage.getFooterCount(); j++) {
						simpleMasterPage.getFooter(j).accept(this, null);
					}
				}
			}

			for (int i = 0; i < report.getContentCount(); i++) {
				report.getContent(i).accept(this, null);
			}

		}

		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			IDMaps.put(Long.valueOf(container.getID()), container);
			for (int i = 0; i < container.getItemCount(); i++) {
				container.getItem(i).accept(this, value);
			}
			return value;
		}

		public Object visitListingItem(ListItemDesign list, Object value) {
			IDMaps.put(Long.valueOf(list.getID()), list);
			if (list.getHeader() != null) {
				list.getHeader().accept(this, value);
			}
			for (int i = 0; i < list.getGroupCount(); i++) {
				list.getGroup(i).accept(this, value);
			}
			if (list.getDetail() != null) {
				list.getDetail().accept(this, value);
			}
			if (list.getFooter() != null) {
				list.getFooter().accept(this, value);
			}
			return value;
		}

		public Object visitGroup(GroupDesign group, Object value) {
			IDMaps.put(Long.valueOf(group.getID()), group);
			if (group.getHeader() != null) {
				group.getHeader().accept(this, value);
			}
			if (group.getFooter() != null) {
				group.getFooter().accept(this, value);
			}
			return value;
		}

		public Object visitBand(BandDesign band, Object value) {
			if (band != null) {
				IDMaps.put(Long.valueOf(band.getID()), band);
				for (int i = 0; i < band.getContentCount(); i++) {
					band.getContent(i).accept(this, null);
				}
			}
			return value;
		}

		public Object visitReportItem(ReportItemDesign item, Object value) {
			IDMaps.put(Long.valueOf(item.getID()), item);
			return value;
		}

		public Object visitGridItem(GridItemDesign grid, Object value) {
			IDMaps.put(Long.valueOf(grid.getID()), grid);
			for (int i = 0; i < grid.getRowCount(); i++) {
				grid.getRow(i).accept(this, value);
			}
			return value;
		}

		public Object visitRow(RowDesign row, Object value) {
			visitReportItem(row, value);
			for (int i = 0; i < row.getCellCount(); i++) {
				row.getCell(i).accept(this, value);
			}
			return value;
		}

		public Object visitCell(CellDesign cell, Object value) {
			visitReportItem(cell, value);
			for (int i = 0; i < cell.getContentCount(); i++) {
				cell.getContent(i).accept(this, value);
			}
			return value;
		}

	}

	protected void openQuery(IContent content) throws BirtException {
		Object generateBy = content.getGenerateBy();
		if (generateBy instanceof ReportItemDesign) {
			if (!(generateBy instanceof ExtendedItemDesign)) {
				ReportItemDesign design = (ReportItemDesign) generateBy;
				IDataQueryDefinition query = design.getQuery();
				if (query != null) {
					IBaseResultSet rset = dataEngine.execute(query);
					resultSets.push(rset);
				}
			}
		}
		InstanceID iid = content.getInstanceID();
		if (iid != null) {
			DataID dataId = iid.getDataID();
			while (dataId == null && iid.getParentID() != null) {
				iid = iid.getParentID();
				dataId = iid.getDataID();
			}
			if (dataId != null) {
				if (!resultSets.isEmpty()) {
					IQueryResultSet rset = (IQueryResultSet) resultSets.peek();
					if (rset != null) {
						long rowId = dataId.getRowID();
						if (rowId != -1 && rowId != rset.getRowIndex()) {
							rset.skipTo(rowId);
						}
					}
				}
			}
		}
		if (generateBy instanceof DataItemDesign && content instanceof DataContent) {
			DataItemDesign design = (DataItemDesign) generateBy;
			DataContent data = (DataContent) content;
			if (design.getMap() == null) {
				String column = design.getBindingColumn();
				String valueExpr = ExpressionUtil.createJSRowExpression(column);

				if (valueExpr != null) {
					Object value = context.evaluate(valueExpr);
					data.setValue(value);
				}
			}
		}
	}

	protected void checkDataSet(DataID dataId, IQueryResultSet rset) {
		DataSetID dsetId = rset.getID();
		DataSetID rsetId = dataId.getDataSetID();
		assert dsetId != null;
		assert rsetId != null;
		assert dsetId.toString().equals(rsetId.toString());
	}

	protected void closeQuery(IContent content) {
		Object generateBy = content.getGenerateBy();
		if (generateBy instanceof ReportItemDesign) {
			if (!(generateBy instanceof ExtendedItemDesign)) {
				ReportItemDesign design = (ReportItemDesign) generateBy;
				IDataQueryDefinition query = design.getQuery();
				if (query != null) {
					IQueryResultSet rset = (IQueryResultSet) resultSets.pop();
					if (rset != null) {
						rset.close();
					}
				}
			}
		}
	}

	/**
	 * output the content to emitter
	 * 
	 * @param content output content
	 */
	protected void startContent(IContent content, IContentEmitter emitter) throws BirtException {
		// open the query used by the content, locate the resource
		try {
			openQuery(content);
		} catch (BirtException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		outputStartVisitor.visit(content, emitter);
	}

	/**
	 * output the content to emitter.
	 * 
	 * @param content output content
	 */
	protected void endContent(IContent content, IContentEmitter emitter) throws BirtException {
		outputEndVisitor.visit(content, emitter);
		// close the query used by the content
		closeQuery(content);
	}

	protected IContentVisitor outputStartVisitor = new IContentVisitor() {

		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContent(content);
			return value;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startPage(page);
			return value;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContainer(container);
			return value;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTable(table);
			return value;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			emitter.startTableBand(tableBand);
			return value;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startRow(row);
			return value;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startCell(cell);
			return value;
		}

		public Object visitText(ITextContent text, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startText(text);
			return value;
		}

		public Object visitLabel(ILabelContent label, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startLabel(label);
			return value;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			if (autoText.getType() == IAutoTextContent.TOTAL_PAGE) {
				autoText.setText(String.valueOf(reportDoc.getPageCount()));
			}
			emitter.startAutoText(autoText);
			return value;
		}

		public Object visitData(IDataContent data, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startData(data);
			return value;
		}

		public Object visitImage(IImageContent image, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startImage(image);
			return value;
		}

		public Object visitForeign(IForeignContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startForeign(content);
			return value;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startList(list);
			return value;
		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListBand(listBand);
			return value;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startGroup(group);
			return value;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListGroup(group);
			return value;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableGroup(group);
			return value;
		}

	};

	protected IContentVisitor outputEndVisitor = new IContentVisitor() {

		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContent(content);
			return value;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endPage(page);
			return value;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContainer(container);
			return value;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTable(table);
			return value;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableBand(tableBand);
			return value;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endRow(row);
			return value;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endCell(cell);
			return value;
		}

		public Object visitText(ITextContent text, Object value) {
			return value;
		}

		public Object visitLabel(ILabelContent label, Object value) {
			return value;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			return value;
		}

		public Object visitData(IDataContent data, Object value) {
			return value;
		}

		public Object visitImage(IImageContent image, Object value) {
			return value;
		}

		public Object visitForeign(IForeignContent content, Object value) {
			return value;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endList(list);
			return value;
		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListBand(listBand);
			return value;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endGroup(group);
			return value;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListGroup(group);
			return value;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableGroup(group);
			return value;
		}
	};
}