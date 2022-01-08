/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.FolderArchive;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.emitter.ReportEmitterExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.internal.presentation.ReportDocumentInfo;
import org.eclipse.birt.report.engine.presentation.ReportDocumentBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask {

	private String documentName;
	private IDocArchiveWriter archiveWriter;
	private ReportDocumentWriter writer;
	private ReportDocumentBuilder documentBuilder;
	private IArchiveFile archive;

	/**
	 * @param engine   the report engine
	 * @param runnable the report runnable instance
	 */
	public RunTask(ReportEngine engine, IReportRunnable runnable) {
		super(engine, runnable, IEngineTask.TASK_RUN);
		executionContext.setFactoryMode(true);
		executionContext.setPresentationMode(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(java.lang.String)
	 */
	public void run(String reportDocName) throws EngineException {
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			if (reportDocName == null || reportDocName.length() == 0) {
				throw new EngineException(MessageConstants.REPORT_DOCNAME_NOT_SPECIFIED_ERROR); // $NON-NLS-1$
			}
			this.documentName = reportDocName;
			doRun();
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.IRunTask#run(org.eclipse.birt.core.archive
	 * .IDocumentArchive)
	 */
	public void run(IDocArchiveWriter archive) throws EngineException {
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			if (archive == null) {
				throw new EngineException(MessageConstants.REPORT_ARCHIVE_ERROR); // $NON-NLS-1$
			}
			this.archiveWriter = archive;
			doRun();
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
		}
	}

	private void openArchive() throws IOException {
		if (archive != null) {
			archiveWriter = new ArchiveWriter(archive);
			return;
		}
		File file = new File(documentName);
		if (file.exists()) {
			if (file.isDirectory()) {
				archiveWriter = new FolderArchiveWriter(documentName);
			} else {
				archiveWriter = new FileArchiveWriter(documentName);
			}
		} else {
			if (documentName.endsWith("\\") || documentName.endsWith("/")) {
				archiveWriter = new FolderArchiveWriter(documentName);
			} else {
				archiveWriter = new FileArchiveWriter(documentName);
			}
		}
	}

	private void openReportDocument() throws EngineException {
		try {
			if (archiveWriter == null) {
				openArchive();
			}
			String[] exts = executionContext.getEngineExtensions();
			writer = new ReportDocumentWriter(engine, archiveWriter, exts);
			executionContext.setReportDocWriter(writer);
			DocumentDataSource ds = executionContext.getDataSource();
			if (ds != null) {
				// avoid the auto-generated bookmark will be changed at generation time
				if (ds.getInstanceID() != null) {
					executionContext.setReportletBookmark(ds.getInstanceID().getComponentID(), ds.getBookmark());
				}
				if (ds.isReportletDocument()) {
					writer.saveReportletDocument(ds.getBookmark(), ds.getInstanceID());
				} else {
					writer.removeReportletDoucment();
				}
			}
		} catch (IOException ex) {
			throw new EngineException(MessageConstants.REPORT_ARCHIVE_OPEN_ERROR, ex);
		}
	}

	private void closeReportDocument() {
		writer.close();
		writer = null;
		archive = null;
		archiveWriter = null;
		documentName = null;
	}

	/**
	 * runs the report
	 * 
	 * @throws EngineException throws exception when there is a run error
	 */
	protected void doRun() throws EngineException {
		if (progressMonitor != null) {
			progressMonitor.onProgress(IProgressMonitor.START_TASK, TASK_RUN);
		}
		loadDataSource();
		loadScripts();
		doValidateParameters();
		initReportVariable();
		loadDesign();
		prepareDesign();
		// get design after prepare in case prepare create a new instance.
		ReportDesignHandle design = executionContext.getReportDesign();
		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(design.getLayoutPreference())) {
			executionContext.setFixedLayout(true);
			setupRenderOption();
			updateRtLFlag();
		}
		startFactory();
		openReportDocument();
		ArrayList<String> errList = new ArrayList<String>();
		ReportRunnable newRunnable = null;
		try {
			newRunnable = writer.saveDesign(executionContext.getRunnable(), executionContext.getOriginalRunnable());
			executionContext.updateRunnable(newRunnable);
			writer.saveReportIR(executionContext.getReport());
			writer.saveParamters(inputValues);

			executionContext.openDataEngine();

			synchronized (this) {
				if (!executionContext.isCanceled()) {
					documentBuilder = new ReportDocumentBuilder(executionContext, writer);
				}
			}

			if (documentBuilder != null) {
				if (pageHandler != null) {
					documentBuilder.setPageHandler(pageHandler);
				}

				IContentEmitter emitter = documentBuilder.getContentEmitter();
				IReportExecutor executor = new ReportExecutor(executionContext);
				// prepare the extension executor
				executor = createReportExtensionExecutor(executor);
				executor = new ReportEmitterExecutor(executor, emitter);
				executor = new SuppressDuplciateReportExecutor(executor);
				if (executionContext.isFixedLayout()) {
					executor = new LocalizedReportExecutor(executionContext, executor);
				}
				executionContext.setExecutor(executor);

				initializeContentEmitter(emitter);
				documentBuilder.build();
			}

			executionContext.closeDataEngine();
		} catch (Throwable t) {
			errList.add(t.getLocalizedMessage());
			handleFatalExceptions(t);
		} finally {
			if (newRunnable != null) {
				newRunnable.getReport().getModule().tidy();
			}

			documentBuilder = null;
			closeFactory();

			List<Exception> list = (List<Exception>) executionContext.getAllErrors();
			if (list != null) {
				for (Exception ex : list) {
					errList.add(ex.getLocalizedMessage());
				}
			}
			if (getStatus() == IEngineTask.STATUS_CANCELLED) {
				if (cancelReason == null) {
					cancelReason = new EngineResourceHandle(ULocale.forLocale(getLocale()))
							.getMessage(MessageConstants.TASK_CANCEL);
				}
				errList.add(cancelReason);
			}
			if (!errList.isEmpty()) {
				// status writer never throws out exception
				RunStatusWriter statusWriter = new RunStatusWriter(archiveWriter);
				statusWriter.writeRunTaskStatus(errList);
				statusWriter.close();
			} else {
				// TODO: need clear all related stream at the beginning of generation task
				if (archiveWriter.exists(ReportDocumentConstants.RUN_STATUS_STREAM)) {
					archiveWriter.dropStream(ReportDocumentConstants.RUN_STATUS_STREAM);
				}
			}

			writer.savePersistentObjects(executionContext.getGlobalBeans());
			writer.finish();

			// notify that the document has been finished
			if (pageHandler != null && !executionContext.isCanceled()) {
				int totalPage = (int) executionContext.getTotalPage();
				IReportDocumentInfo docInfo = new ReportDocumentInfo(executionContext, totalPage, true);
				pageHandler.onPage(totalPage, true, docInfo);
			}
			if (progressMonitor != null) {
				progressMonitor.onProgress(IProgressMonitor.END_TASK, TASK_RUN);
			}
			closeReportDocument();
		}
	}

	public void close() {
		super.close();
	}

	/**
	 * @deprecated
	 */
	public void run(FolderArchive fArchive) throws EngineException {
		try {
			changeStatusToRunning();
			setDataSource(fArchive);
			run((IDocArchiveWriter) fArchive);
		} finally {
			changeStatusToStopped();
		}
	}

	public void cancel() {
		super.cancel();
		if (documentBuilder != null) {
			documentBuilder.cancel();
		}
	}

	public void setMaxRowsPerQuery(int maxRows) {
		executionContext.setMaxRowsPerQuery(maxRows);
	}

	public void enableProgressiveViewing(boolean enabled) {
		executionContext.enableProgressiveViewing(enabled);
	}

	public void setReportDocument(IArchiveFile archive) {
		this.archive = archive;
	}

	public void setReportDocument(String name) {
		documentName = name;
	}

	public void run() throws EngineException {
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			doRun();
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
		}
	}
}
