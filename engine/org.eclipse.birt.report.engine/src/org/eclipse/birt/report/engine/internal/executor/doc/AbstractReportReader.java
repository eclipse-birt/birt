/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.RenderTask;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.ITreeNode;

public abstract class AbstractReportReader implements IReportExecutor {

	protected static Logger logger = Logger.getLogger(AbstractReportReader.class.getName());

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected CachedReportContentReaderV3 reader;
	protected PageHintReader hintReader;
	protected CachedReportContentReaderV3 pageReader;

	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;

	ReportItemReaderManager manager;

	public AbstractReportReader(ExecutionContext context) throws IOException, BirtException {
		assert context.getDesign() != null;
		assert context.getReportDocument() != null;

		this.context = context;

		report = context.getReport();

		reportContent = (ReportContent) ContentFactory.createReportContent(report);
		reportContent.setExecutionContext(context);
		context.setReportContent(reportContent);

		reportDoc = context.getReportDocument();

		IEngineTask engineTask = context.getEngineTask();
		if (engineTask instanceof RenderTask) {
			RenderTask renderTask = (RenderTask) engineTask;
			ITreeNode tocTree = renderTask.getRawTOCTree();
			reportContent.setTOCTree(tocTree);
		}

		long totalPage = reportDoc.getPageCount();
		context.setTotalPage(totalPage);
		reportContent.setTotalPage(totalPage);

		dataEngine = context.getDataEngine();
		dataEngine.prepare(report, context.getAppContext());

		manager = new ReportItemReaderManager(context);
		try {
			openReaders();
		} catch (IOException ex) {
			closeReaders();
			throw ex;
		}
	}

	public void close() {
		closeReaders();
	}

	protected void openReaders() throws IOException {
		IDocArchiveReader archive = reportDoc.getArchive();
		RAInputStream in = archive.getStream(ReportDocumentConstants.CONTENT_STREAM);
		reader = new CachedReportContentReaderV3(reportContent, in, context);

		// open the page hints stream and the page content stream
		hintReader = new PageHintReader(reportDoc);

		in = archive.getStream(ReportDocumentConstants.PAGE_STREAM);
		pageReader = new CachedReportContentReaderV3(reportContent, in, context);
	}

	protected void closeReaders() {
		if (reader != null) {
			reader.close();
			reader = null;
		}
		if (hintReader != null) {
			hintReader.close();
			hintReader = null;
		}
		if (pageReader != null) {
			pageReader.close();
			pageReader = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.executor.IReportExecutor#createPageExecutor(
	 * long, org.eclipse.birt.report.engine.ir.MasterPageDesign)
	 */
	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign)

	{
		try {
			IPageHint hint;
			long totalPage = hintReader.getTotalPage();
			if (pageNumber > totalPage) {
				hint = hintReader.getPageHint(1);
			} else {
				hint = hintReader.getPageHint(pageNumber);
				if (hint == null) {
					hint = hintReader.getPageHint(1);
				}
			}
			if (hint != null) {
				long offset = hint.getOffset();

				ReportItemReader pageExecutor = manager.createExecutor(null, offset);
				pageExecutor.reader = pageReader;

				return pageExecutor;
			}
		} catch (IOException ex) {
			context.addException(pageDesign, new EngineException(MessageConstants.PAGES_LOADING_ERROR, pageNumber, ex));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IReportExecutor#execute()
	 */
	public IReportContent execute() {
		return reportContent;
	}
}
