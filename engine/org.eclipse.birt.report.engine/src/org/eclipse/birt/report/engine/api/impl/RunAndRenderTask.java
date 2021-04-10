/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ContextPageBreakHandler;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.layout.CompositeLayoutPageHandler;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.nLayout.LayoutEngine;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask {

	protected IReportLayoutEngine layoutEngine;

	/**
	 * @param engine   reference to the report engine
	 * @param runnable the runnable report design reference
	 */
	public RunAndRenderTask(ReportEngine engine, IReportRunnable runnable) {
		super(engine, runnable, IEngineTask.TASK_RUNANDRENDER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run() throws EngineException {
		if (progressMonitor != null) {
			progressMonitor.onProgress(IProgressMonitor.START_TASK, TASK_RUNANDRENDER);
		}
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			doRun();
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
			if (progressMonitor != null) {
				progressMonitor.onProgress(IProgressMonitor.END_TASK, TASK_RUNANDRENDER);
			}
		}
	}

	void doRun() throws EngineException {
		loadScripts();
		// register default parameters and validate
		doValidateParameters();

		setupRenderOption();
		initReportVariable();
		loadDesign();
		prepareDesign();
		startFactory();
		updateRtLFlag();
		startRender();
		try {
			IContentEmitter emitter = createContentEmitter();
			IReportExecutor executor = new ReportExecutor(executionContext);
			executor = new SuppressDuplciateReportExecutor(executor);
			executor = new LocalizedReportExecutor(executionContext, executor);
			executionContext.setExecutor(executor);
			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				LayoutEngine pdfLayoutEmitter = new LayoutEngine(emitter, renderOptions, executionContext, 0l);
				emitter = pdfLayoutEmitter;
			}
			initializeContentEmitter(emitter);

			// if we need do the paginate, do the paginate.
			String format = executionContext.getOutputFormat();

			boolean paginate = true;
			if (FORMAT_HTML.equalsIgnoreCase(format) || FORMAT_XHTML.equalsIgnoreCase(format)) // $NON-NLS-1$
			{
				HTMLRenderOption htmlOption = new HTMLRenderOption(executionContext.getRenderOption());
				paginate = htmlOption.getHtmlPagination();
			} else {
				RenderOption taskOption = new RenderOption(executionContext.getRenderOption());
				paginate = taskOption.getBooleanOption(IRenderOption.HTML_PAGINATION, true);
			}
			if (ExtensionManager.NO_PAGINATION.equals(pagination)) {
				paginate = false;
			}

			synchronized (this) {
				if (!executionContext.isCanceled()) {
					layoutEngine = createReportLayoutEngine(pagination, renderOptions);
				}
			}

			if (layoutEngine != null) {
				layoutEngine.setLocale(executionContext.getLocale());

				CompositeLayoutPageHandler layoutPageHandler = new CompositeLayoutPageHandler();
				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(executionContext);
				layoutPageHandler.addPageHandler(handle);
				layoutPageHandler.addPageHandler(new ContextPageBreakHandler(executionContext));
				if (!ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
					layoutPageHandler.addPageHandler(new LayoutPageHandler());
					layoutEngine.setPageHandler(layoutPageHandler);
				} else {
					((LayoutEngine) emitter).setPageHandler(layoutPageHandler);
				}

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(format);
				outputEmitters.addEmitter(emitter);
				outputEmitters.addEmitter(handle.getEmitter());

				IReportContent report = executor.execute();
				outputEmitters.start(report);
				layoutEngine.layout(executor, report, outputEmitters, paginate);
				layoutEngine.close();
				outputEmitters.end(report);
			}
			closeRender();
			executionContext.closeDataEngine();
			closeFactory();
		} catch (Throwable t) {
			handleFatalExceptions(t);
		}
	}

	public void cancel() {
		super.cancel();
		if (layoutEngine != null) {
			layoutEngine.cancel();
		}
	}

	public void setMaxRowsPerQuery(int maxRows) {
		executionContext.setMaxRowsPerQuery(maxRows);
	}

	private class LayoutPageHandler implements ILayoutPageHandler {

		public void onPage(long pageNumber, Object context) {
			if (pageHandler != null) {
				pageHandler.onPage((int) pageNumber, false, null);
			}
			executionContext.getProgressMonitor().onProgress(IProgressMonitor.END_PAGE, (int) pageNumber);
		}
	}
}