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

package org.eclipse.birt.report.engine.executor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

/**
 * Captures the (report design to) report instance creation logic, by combining
 * the report design structure and the data. It acts as an entry point to allow
 * actual data to drive the contents that appears in a report, i.e., header
 * frames use the first data row, detail rows are repeated for each data row,
 * etc. The output of the executor, for now, is a specific output format, i.e.,
 * HTML, FO or PDF, with the help of the emitter extensions.
 * <p>
 *
 * The report instance creation logic is subject to further abstraction, because
 * it is needed in both report generation and report presentation. This is
 * because report document (not supported for now) does not store each report
 * item instance. As a result, the report item instances need to be created at
 * presentation time too. For now, report generation and presentation are merged
 * as we do not generate report documents. The report instance creation logic is
 * therefore run only once. When the generation and presentation phases are
 * separated, the output of an executor could not only be a specific report
 * output format, but also be a report document. Data would then come from
 * database in factory engine, and from report document in the presentation
 * engine.
 *
 */
public class ReportExecutor implements IReportExecutor {
	// the report execution context
	protected ExecutionContext context;

	// the manager used to manage the executors.
	protected ExecutorManager manager;

	protected Report report;

	protected ReportContent reportContent;

	protected long uniqueId;

	protected HashMap pages = new HashMap();

	protected ReportletItemExecutor reportletExecutor;

	/**
	 * constructor
	 *
	 * @param context the executor context
	 * @param emitter the report emitter
	 *
	 */
	public ReportExecutor(ExecutionContext context) {
		this.context = context;
		this.report = context.getReport();
		this.manager = new ExecutorManager(this);
		this.uniqueId = 0;
	}

	@Override
	public IReportContent execute() throws BirtException {
		reportContent = new ReportContent(report);
		reportContent.setExecutionContext(context);
		context.setReportContent(reportContent);

		try {
			TOCBuilder tocBuilder = new TOCBuilder(context);
			reportContent.setTOCTree(tocBuilder.getTOCTree());
			context.setTOCBuilder(tocBuilder);
		} catch (IOException ex) {
			context.addException(new EngineException(MessageConstants.FAILED_TO_CREATE_TOC_EXCEPTION, ex));
		}

		DocumentDataSource dataSource = context.getDataSource();
		if (dataSource != null) {
			long reportletId = dataSource.getElementID();
			if (reportletId != -1) {
				reportletExecutor = new ReportletItemExecutor(manager);
			}
		}

		// Prepare necessary data for this report
		Map appContext = context.getAppContext();

		context.getDataEngine().prepare(report, appContext);

		// the report variables has been registered before the initialize at
		// EngineTask.initReportVariable()

		if (reportletExecutor == null) {
			// create execution optimize policy
			context.optimizeExecution();
		}

		// prepare to execute the child
		currentItem = 0;

		if (reportletExecutor != null) {
			reportletExecutor.execute();
		}

		return reportContent;
	}

	@Override
	public void close() throws BirtException {
		TOCBuilder builder = context.getTOCBuilder();
		if (builder != null) {
			try {
				builder.close();
			} catch (IOException ex) {
				context.addException(new EngineException("failed to close TOC", ex));
			} finally {
				context.setTOCBuilder(null);
			}
		}

		if (reportletExecutor != null) {
			reportletExecutor.close();
			reportletExecutor = null;
		}
		uniqueId = 0;
	}

	int currentItem;

	@Override
	public IReportItemExecutor getNextChild() {
		if (reportletExecutor != null) {
			return reportletExecutor.getNextChild();
		}

		if (currentItem < report.getContentCount()) {
			ReportItemDesign design = report.getContent(currentItem++);
			ReportItemExecutor executor = manager.createExecutor(null, design);
			return executor;
		}
		return null;
	}

	@Override
	public boolean hasNextChild() {
		if (reportletExecutor != null) {
			return reportletExecutor.hasNextChild();
		}
		if (currentItem < report.getContentCount()) {
			return true;
		}
		return false;
	}

	public ExecutionContext getContext() {
		return this.context;
	}

	public ExecutorManager getManager() {
		return this.manager;
	}

	long generateUniqueID() {
		return uniqueId++;
	}

	@Override
	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException {
		// only execute once for the same page design
		IPageContent pageContent = (IPageContent) pages.get(pageDesign);
		if (pageContent == null) {

			IReportItemExecutor pageExecutor = new MasterPageExecutor(manager, pageNumber, pageDesign);
			pageContent = (IPageContent) pageExecutor.execute();
			IContentEmitter domEmitter = new DOMBuilderEmitter(pageContent);
			ReportExecutorUtil.executeAll(pageExecutor, domEmitter);
			pageExecutor.close();
			pages.put(pageDesign, pageContent);
		} else {
			pageContent.setPageNumber(pageNumber);
			context.setPageNumber(pageNumber);
		}
		return new DOMReportItemExecutor(pageContent, true);
	}
}
