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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;

/**
 *
 */
public class ParameterTest extends EngineCase {

	static final String TEST_FOLDER = "./utest/";

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/run_task_design.xml";
	static final String BLANK_REPORT_DOCUMENT_RESOURCE = "org/eclipse/birt/report/engine/api/BlankReport.rptdocument";
	static final String BLANK_REPORT_DOCUMENT = "utest/BlankReport.rptdocument";
	static final String VIEW_DOCUMENT = "./utest/view.rptdocument";
	static final String REPORT_DESIGN = "utest/design.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/reportdocument/";
	static final String REPORT_DOCUMENT_ZIP = "./utest/reportdocument.zip";

	static final String REPORT_DESIGN_RESOURCE1 = "org/eclipse/birt/report/engine/api/render_task_design.xml";
	static final String REPORT_DESIGN1 = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT1 = "./utest/reportdocument.folder/";

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeFile(TEST_FOLDER);
		removeFile(REPORT_DOCUMENT_ZIP);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		copyResource(BLANK_REPORT_DOCUMENT_RESOURCE, BLANK_REPORT_DOCUMENT);
	}

	@Override
	public void tearDown() throws Exception {
		removeFile(TEST_FOLDER);
		super.tearDown();
	}

	public void testRun() {
		try {
			IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
			IRunTask task = engine.createRunTask(report);
			task.setParameter("param1", 1, "11");
			assertEquals("11", task.getParameterDisplayText("param1"));
			task.setParameter("param2", new String[] { "1", "2", "3" }, new String[] { "a", "b", "c" });
			task.run(BLANK_REPORT_DOCUMENT);
			task.close();
			IReportDocument doc = engine.openReportDocument(BLANK_REPORT_DOCUMENT);
			IRenderTask rtask = engine.createRenderTask(doc);
			Object displayText = rtask.getParameterDisplayText("param2");
			Object values = rtask.getParameterValue("param2");
			assertTrue(displayText instanceof String[]);
			assertEquals(((String[]) displayText)[2], "c");
			assertTrue(((Object[]) values).length == 3);
			rtask.close();
			doc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

}
