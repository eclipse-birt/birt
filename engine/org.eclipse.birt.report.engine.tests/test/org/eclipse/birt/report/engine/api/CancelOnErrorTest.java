/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;

public class CancelOnErrorTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/cancel-on-error.rptdesign";

	public void testCancel() {
		/*
		 * delete this UnitTest new UnitTest will be checkin to test task.cancel()
		 */
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);

		IReportEngine engine = createReportEngine();
		try {
			IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
			IRunTask task = engine.createRunTask(report);
			// task.setErrorHandlingOption( IEngineTask.CANCEL_ON_ERROR );
			task.run(REPORT_DOCUMENT);
			task.close();

			IReportDocument doc = engine.openReportDocument(REPORT_DOCUMENT);
			assertEquals(3, doc.getPageCount());
			doc.close();

			task = engine.createRunTask(report);
			task.setErrorHandlingOption(IEngineTask.CANCEL_ON_ERROR);
			task.run(REPORT_DOCUMENT);
			task.close();
			doc = engine.openReportDocument(REPORT_DOCUMENT);
			assertTrue(3 > doc.getPageCount());
			doc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		engine.shutdown();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
	}

}
