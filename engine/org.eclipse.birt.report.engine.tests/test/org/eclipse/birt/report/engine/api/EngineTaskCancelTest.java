/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;

/**
 * 
 */

public class EngineTaskCancelTest extends EngineCase {

	static final String PAGE_HANDLER_CANCEL = "org/eclipse/birt/report/engine/api/six_pages_design.xml";
	static final String REPORT_DESIGN = "design.rptdesign";
	static final String REPORT_DOCUMENT = "./reportdocument.folder/";
	static final String ERROR_MESSAGE = "cancel error message";
	static final String SCRIPT_CANCEL = "org/eclipse/birt/report/engine/api/ScriptCancel.xml";

	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testScriptCancel() throws Exception {
		copyResource(SCRIPT_CANCEL, REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
		IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
		IRunTask runTask = engine.createRunTask(runnable);
		ScriptCancelHanlder handler = new ScriptCancelHanlder();
		runTask.setStatusHandler(handler);
		runTask.run(REPORT_DOCUMENT);
		runTask.close();
		assertTrue(handler.status);
		assertEquals(IEngineTask.STATUS_CANCELLED, runTask.getStatus());
		removeFile(REPORT_DESIGN);

		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		List<String> errorList = document.getDocumentErrors();
		int numberoferrormessage = 0;
		for (String error : errorList) {
			if (ERROR_MESSAGE.equals(error)) {
				numberoferrormessage++;
			}
		}
		assertEquals(1, numberoferrormessage);

		removeFile(REPORT_DOCUMENT);

	}

	public void testCancelInPageHandler() throws Exception {
		copyResource(PAGE_HANDLER_CANCEL, REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
		IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
		IRunTask runTask = engine.createRunTask(runnable);
		runTask.setPageHandler(new CancelPageHandler(runTask));
		runTask.run(REPORT_DOCUMENT);
		runTask.close();
		assertEquals(IEngineTask.STATUS_CANCELLED, runTask.getStatus());
		IReportDocument reportDoc = engine.openReportDocument(REPORT_DOCUMENT);
		assertTrue(reportDoc.getPageCount() < 6);
		reportDoc.close();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
	}

	private class ScriptCancelHanlder implements IStatusHandler {

		public boolean status = false;

		public void initialize() {
		}

		public void showStatus(String s) {
			status = true;
			assertTrue(s != null && s.length() > 0);
		}

		public void finish() {
		}

	}

	private class CancelPageHandler implements IPageHandler {

		IEngineTask task;

		CancelPageHandler(IEngineTask task) {
			this.task = task;
		}

		public void onPage(int pageNumber, boolean checkpoint, IReportDocumentInfo doc) {
			if (pageNumber == 2) {
				task.cancel();
			}
		}
	}
}
