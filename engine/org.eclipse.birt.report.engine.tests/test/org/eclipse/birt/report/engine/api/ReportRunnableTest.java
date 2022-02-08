/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * 
 */

public class ReportRunnableTest extends EngineCase {

	protected IReportRunnable runnable = null;

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/ReportRunnableTest.rptdesign";
	static final String REPORT_DESIGN = "ReportRunnableTest.rptdesign";

	public void setUp() throws Exception {
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);

		engine = createReportEngine();
		runnable = engine.openReportDesign(REPORT_DESIGN);
	}

	public void tearDown() {
		// shut down the engine.
		if (engine != null) {
			engine.shutdown();
		}
		removeFile(REPORT_DESIGN);
	}

	/**
	 * API test on IReportRunnable.getImage( ) method
	 */
	public void testGetImage() {
		final String IMAGE_NAME = "img.jpg";
		try {
			IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
			IImage img = runnable.getImage(IMAGE_NAME);
			assertTrue(img != null);
			assertEquals(runnable, img.getReportRunnable());
			assertTrue(IMAGE_NAME.equals(img.getID()));
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	/**
	 * API test on IReportRunnable.setDesignHandle() and
	 * IReportRunnable.getDesignHandle()
	 *
	 */
	public void testGetDesignHandle() {
		IReportRunnable runnableA = null;
		DesignElementHandle designHandleA = null;

		try {
			runnableA = engine.openReportDesign(REPORT_DESIGN);
			designHandleA = runnableA.getDesignHandle();
			assertTrue(designHandleA != null);

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	public void testRerenderTask() throws Exception {
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		// create an IRunTask
		IRunTask runTask = engine.createRunTask(report);
		// execute the report to create the report document.
		runTask.run(REPORT_DOCUMENT);
		// close the task, release the resource.
		runTask.close();

		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument(REPORT_DOCUMENT);
		IReportRunnable pRunnable = reportDoc.getPreparedRunnable();
		IRenderTask task = engine.createRenderTask(reportDoc, pRunnable);

		IRenderOption option = new HTMLRenderOption();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		option.setOutputStream(out);
		// set the render options
		task.setRenderOption(option);

		// render report by page
		task.render();
		String outputString = out.toString();
		assertTrue(outputString.indexOf("after onPrepare") > 0);
		task.close();
		reportDoc.close();
	}
}
