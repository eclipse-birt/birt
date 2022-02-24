/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v2;

import java.io.IOException;
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
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.RenderTask;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
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
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.emitter.ContentDOMVisitor;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IReportContentLoader;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.toc.ITreeNode;

public class ReportContentLoaderV2 implements IReportContentLoader {

	protected static Logger logger = Logger.getLogger(IReportContentLoader.class.getName());

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected IContentEmitter emitter;
	protected ReportContentReaderV2 reader;
	protected ReportContentReaderV2 pageReader;
	protected PageHintReaderV2 hintReader;
	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;
	protected IContent dummyReportContent;
	/**
	 * the offset of current read object. The object has been read out, setted in
	 * loadContent();
	 */
	protected long currentOffset;

	protected Stack resultSets = new Stack();

	public ReportContentLoaderV2(ExecutionContext context) throws EngineException {
		this.context = context;
		dataEngine = context.getDataEngine();
		report = context.getReport();

		reportContent = (ReportContent) ContentFactory.createReportContent(report);
		reportContent.setExecutionContext(context);
		context.setReportContent(reportContent);

		dummyReportContent = reportContent.createLabelContent();
		dummyReportContent.setStyleClass(report.getRootStyleName());

		reportDoc = context.getReportDocument();
		dataEngine.prepare(report, context.getAppContext());

		IEngineTask engineTask = context.getEngineTask();
		if (engineTask instanceof RenderTask) {
			RenderTask renderTask = (RenderTask) engineTask;
			ITreeNode tocTree = renderTask.getRawTOCTree();
			reportContent.setTOCTree(tocTree);
		}
	}

	protected void openReaders() {
		try {
			reader = new ReportContentReaderV2(reportContent, reportDoc, context.getApplicationClassLoader());
			reader.open(ReportDocumentConstants.CONTENT_STREAM);
			pageReader = new ReportContentReaderV2(reportContent, reportDoc, context.getApplicationClassLoader());
			pageReader.open(ReportDocumentConstants.PAGE_STREAM);
			hintReader = new PageHintReaderV2(reportDoc.getArchive());
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
	@Override
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

	@Override
	public void loadReportlet(long offset, IContentEmitter emitter) throws BirtException {
		emitter.start(reportContent);
		this.emitter = emitter;
		try {
			openReaders();
			excuteReportlet(offset);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to load the page", ex);
		} finally {
			emitter.end(reportContent);
			closeReaders();
		}

	}

	/**
	 * stack used to control the output contents.
	 */
	protected Stack contents = new Stack();

	protected IPageHint loadPageHint(long pageNumber) {
		try {
			return hintReader.getPageHint(pageNumber);
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Failed to load page hint " + pageNumber, ex);
		}
		return null;
	}

	/**
	 * load the page content and output to the emitter.
	 *
	 * @param pageNumber page number
	 * @param bodyOnly   only output the page body.
	 */
	private void excutePage(long pageNumber, boolean bodyOnly) throws BirtException {
		IPageHint pageHint = loadPageHint(pageNumber);
		if (pageHint == null) {
			return;
		}
		IPageContent pageContent = null;
		if (!bodyOnly) {
			long pageOffset = pageHint.getOffset();
			try {
				pageContent = loadPageContent(pageOffset);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Can not load the page content " + pageNumber, ex);
			}
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

		for (int i = 0; i < pageHint.getSectionCount(); i++) {
			PageSection section = pageHint.getSection(i);
			long start = section.startOffset;
			long end = section.endOffset;
			if (start != -1 && end != -1) {
				try {
					outputPageRegion(start, end);
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "Can not load the page content");
				}
			}
		}

		while (!contents.isEmpty()) {
			IContent content = (IContent) contents.pop();
			endContent(content, emitter);
		}

		if (!bodyOnly) {
			emitter.endPage(pageContent);
		}
	}

	/**
	 * load the page content and output to the emitter.
	 *
	 * @param pageNumber page number
	 * @param bodyOnly   only output the page body.
	 */
	private void excuteReportlet(long offset) throws BirtException {
		try {
			reader.setOffset(offset);

			// FIXME We needn't output the parent of the reportlet.

			// start all the parent of the content
			IContent root = reader.readContent();

			IContent parent = (IContent) root.getParent();
			if (parent != null) {
				outputParent(parent);
			}
			// output this content
			initializeContent(root);
			startContent(root, emitter);
			contents.push(root);

			parent = root;
			IContent next = reader.readContent();
			while (next != null) {
				if (next.getParent() == parent) {
					initializeContent(next);
					startContent(next, emitter);
					contents.push(next);

					parent = next;
					next = reader.readContent();
				} else {
					if (parent == root) {
						break;
					}
					endContent(parent, emitter);
					contents.pop();
					parent = (IContent) parent.getParent();
				}
			}

		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Can not load the page content", ex);
		}

		while (!contents.isEmpty()) {
			IContent content = (IContent) contents.pop();
			endContent(content, emitter);
		}
	}

	/**
	 * output the contents from start to end.
	 *
	 * @param start
	 * @param end
	 * @throws IOException
	 */
	private void outputPageRegion(long start, long end) throws IOException, BirtException {
		long offset = start;
		reader.setOffset(offset);
		while (offset <= end) {
			IContent content = reader.readContent();
			// first end all the content which is not
			// the ancestor of this contentutil the stack is empty
			while (!contents.isEmpty()) {
				IContent parent = (IContent) contents.peek();
				if (parent != content.getParent()) {
					endContent(parent, emitter);
					contents.pop();
					continue;
				}
				break;
			}
			// output all the ancestor of this content
			if (contents.isEmpty()) {
				long curOffset = reader.getOffset();
				IContent parent = (IContent) content.getParent();
				if (parent != null) {
					outputParent(parent);
				}
				reader.setOffset(curOffset);
			}
			// now the contents contains the parent of this content.
			initializeContent(content);
			startContent(content, emitter);
			contents.push(content);
			offset = reader.getOffset();
		}
	}

	/**
	 * output the parents of the content.
	 *
	 * @param content
	 */
	private void outputParent(IContent content) throws IOException, BirtException {
		IContent parent = (IContent) content.getParent();
		if (parent != null && parent != dummyReportContent) {
			outputParent(parent);
		}
		initializeContent(content);
		startContent(content, emitter);
		contents.push(content);
		if (content instanceof ITableContent) {
			ITableContent table = (ITableContent) content;
			if (table.isHeaderRepeat()) {
				ITableBandContent header = table.getHeader();
				if (header == null) {
					// try to load the header content
					long offset = getIndex(table);
					reader.setOffset(offset);
					// skip the table object
					reader.readContent();
					// read the header object
					IContent headerContent = reader.readContent();
					// read the contents in the header
					loadFullContent(headerContent, reader);
					// add the header into the table
					table.getChildren().add(headerContent);
					header = table.getHeader();
				}
				// output the table header
				if (header != null) {
					new ContentDOMVisitor().emit(header, emitter);
				}
			}
		}
		/*
		 * if (content instanceof IGroupContent) { IGroupContent group =
		 * (IGroupContent)content; IBandContent header = group.getHeader(); if (header
		 * == null) { reader.setOffset( group.getOffset() ); //skip to the table object?
		 * reader.readContent(); IContent headerContent = reader.readContent( );
		 * loadFullContent(headerContent, reader); group.setHeader(header); } if (header
		 * != null) { new ContentDOMVisitor().emit(header, emitter); } }
		 */
	}

	/**
	 * load the page from the content stream and output it to the emitter
	 *
	 * @param pageNumber
	 * @param emitter
	 */
	@Override
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

	/**
	 * load the page content from the page content stream.
	 *
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	protected IPageContent loadPageContent(long offset) throws IOException, BirtException {
		pageReader.setOffset(offset);
		IPageContent pageContent = (IPageContent) pageReader.readContent();
		initializeContent(pageContent);
		if (pageContent == null) {
			return null;
		}

		SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageContent.getGenerateBy();

		if (masterPage.getHeaderCount() > 0 || masterPage.getFooterCount() > 0) {
			IContent nextContent = null;
			IContent content = pageReader.readContent();
			for (int i = 0; i < masterPage.getHeaderCount(); i++) {
				nextContent = loadFullContent(content, pageReader);
				pageContent.getHeader().add(content);
				content = nextContent;
			}
			for (int i = 0; i < masterPage.getFooterCount(); i++) {
				nextContent = loadFullContent(content, pageReader);
				pageContent.getFooter().add(content);
				content = nextContent;
			}
		}
		return pageContent;
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
	protected IContent loadFullContent(IContent root, ReportContentReaderV2 reader) throws BirtException {
		IContentEmitter emitter = new DOMBuilderEmitter(root);
		IContent parent = root;

		initializeContent(root);
		try {
			openQuery(root);
			IContent next = reader.readContent();
			while (next != null) {
				if (next.getParent() == parent) {
					initializeContent(next);
					startContent(next, emitter);
					parent = next;
					next = reader.readContent();
				} else {
					if (parent == root) {
						closeQuery(root);
						return next;
					}
					endContent(parent, emitter);
					parent = (IContent) parent.getParent();
				}
			}
		} catch (IOException | BirtException ex) {

		}

		while (parent != root) {
			endContent(parent, emitter);
			parent = (IContent) parent.getParent();
		}
		closeQuery(root);

		return null;
	}

	protected void initializeContent(IContent content) {
		// set up the report content
		content.setReportContent(reportContent);

		IElement parent = content.getParent();
		if (parent == null) {
			content.setParent(dummyReportContent);
		}

		// set up the design object
		InstanceID id = content.getInstanceID();
		if (id != null) {
			long designId = id.getComponentID();
			if (designId != -1) {
				Object generateBy = findReportItem(designId);
				content.setGenerateBy(generateBy);
			}
		}
	}

	/**
	 * find the report element by the design id. we need get the engine's IR from
	 * the design id, so we can't use the mode's getElementByID().
	 *
	 * @param designId design id
	 * @return design object (engine)
	 */
	protected ReportElementDesign findReportItem(long designId) {
		return report.getReportItemByID(designId);
	}

	protected void openQuery(IContent content) throws BirtException {
		Object generateBy = content.getGenerateBy();
		// open the query associated with the current report item
		if (generateBy instanceof ReportItemDesign) {
			ReportItemDesign design = (ReportItemDesign) generateBy;
			IDataQueryDefinition query = design.getQuery();
			if (query != null) {
				InstanceID iid = content.getInstanceID();
				if (iid != null) {
					// To the current report item,
					// if the dataId exist and it's deteSet id is not null,
					// and we can find it has parent,
					// we'll try to skip to the current row of the parent
					// query.
					DataID dataId = iid.getDataID();
					if (dataId != null) {
						DataSetID dataSetId = dataId.getDataSetID();
						if (dataSetId != null) {
							DataSetID parentSetId = dataSetId.getParentID();
							long parentRowId = dataSetId.getRowID();
							if (parentSetId != null && parentRowId != -1) {
								// the parent exist.
								if (!resultSets.isEmpty()) {
									IQueryResultSet rset = (IQueryResultSet) resultSets.peek();
									if (rset != null) {
										// the parent query's result set is
										// not null, skip to the right row
										// according row id.
										if (parentRowId != rset.getRowIndex()) {
											rset.skipTo(parentRowId);
										}
									}
								}
							}
						}
					}
				}
				// execute query
				IQueryResultSet rset = (IQueryResultSet) dataEngine.execute(query);
				resultSets.push(rset);
			}
		}
		// locate the row position to the current position
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

		@Override
		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		@Override
		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContent(content);
			return value;
		}

		@Override
		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startPage(page);
			return value;
		}

		@Override
		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContainer(container);
			return value;
		}

		@Override
		public Object visitTable(ITableContent table, Object value) throws BirtException {
			int colCount = table.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				IColumn col = table.getColumn(i);
				InstanceID id = col.getInstanceID();
				if (id != null) {
					long cid = id.getComponentID();
					ColumnDesign colDesign = (ColumnDesign) report.getReportItemByID(cid);
					col.setGenerateBy(colDesign);
				}
			}
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTable(table);
			return value;
		}

		@Override
		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			setupGroupBand(tableBand);
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableBand(tableBand);
			return value;
		}

		@Override
		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startRow(row);
			return value;
		}

		@Override
		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startCell(cell);
			return value;
		}

		@Override
		public Object visitText(ITextContent text, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startText(text);
			return value;
		}

		@Override
		public Object visitLabel(ILabelContent label, Object value) throws BirtException {
			if (label.getGenerateBy() instanceof TemplateDesign) {
				TemplateDesign design = (TemplateDesign) label.getGenerateBy();
				String promptKey = design.getPromptTextKey();
				String promptText = design.getPromptText();
				label.setLabelKey(promptKey);
				label.setLabelText(promptText);
			}
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startLabel(label);
			return value;
		}

		@Override
		public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			if (autoText.getType() == IAutoTextContent.TOTAL_PAGE) {
				autoText.setText(String.valueOf(reportDoc.getPageCount()));
			}
			emitter.startAutoText(autoText);
			return value;
		}

		@Override
		public Object visitData(IDataContent data, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			if (data.getGenerateBy() instanceof DataItemDesign) {
				DataItemDesign design = (DataItemDesign) data.getGenerateBy();
				if (design.getMap() == null) {
					String column = design.getBindingColumn();
					String valueExpr = ExpressionUtil.createJSRowExpression(column);
					;
					if (valueExpr != null) {
						try {
							Object dataValue = context.evaluate(valueExpr);
							data.setValue(dataValue);
						} catch (BirtException ex) {
							context.addException(ex);
						}
					}
				}
			}

			emitter.startData(data);
			return value;
		}

		@Override
		public Object visitImage(IImageContent image, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startImage(image);
			return value;
		}

		@Override
		public Object visitForeign(IForeignContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startForeign(content);
			return value;
		}

		@Override
		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startList(list);
			return value;
		}

		@Override
		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			setupGroupBand(listBand);
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListBand(listBand);
			return value;
		}

		protected void setupGroupBand(IBandContent bandContent) throws BirtException {
		}

		@Override
		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startGroup(group);
			return value;
		}

		@Override
		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListGroup(group);
			return value;
		}

		@Override
		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableGroup(group);
			return value;
		}

	};

	protected IContentVisitor outputEndVisitor = new IContentVisitor() {

		@Override
		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		@Override
		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContent(content);
			return value;
		}

		@Override
		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endPage(page);
			return value;
		}

		@Override
		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContainer(container);
			return value;
		}

		@Override
		public Object visitTable(ITableContent table, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTable(table);
			return value;
		}

		@Override
		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableBand(tableBand);
			return value;
		}

		@Override
		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endRow(row);
			return value;
		}

		@Override
		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endCell(cell);
			return value;
		}

		@Override
		public Object visitText(ITextContent text, Object value) {
			return value;
		}

		@Override
		public Object visitLabel(ILabelContent label, Object value) {
			return value;
		}

		@Override
		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			return value;
		}

		@Override
		public Object visitData(IDataContent data, Object value) {
			return value;
		}

		@Override
		public Object visitImage(IImageContent image, Object value) {
			return value;
		}

		@Override
		public Object visitForeign(IForeignContent content, Object value) {
			return value;
		}

		@Override
		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endList(list);
			return value;
		}

		@Override
		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListBand(listBand);
			return value;
		}

		@Override
		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endGroup(group);
			return value;
		}

		@Override
		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListGroup(group);
			return value;
		}

		@Override
		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableGroup(group);
			return value;
		}
	};

	long getIndex(IContent content) {
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		if (docExt != null) {
			return docExt.getIndex();
		}
		return -1;
	}
}
