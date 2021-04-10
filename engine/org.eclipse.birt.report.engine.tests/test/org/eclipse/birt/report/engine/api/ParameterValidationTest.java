
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import org.eclipse.birt.report.engine.EngineCase;

/**
 *
 */

public class ParameterValidationTest extends EngineCase {
	protected HashMap supportedMap = null;

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/param.rptdesign";
	static final String REPORT_DESIGN = "ReportEngineTest.rptdesign";

	public void setUp() {
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
	}

	public void testParameterValidation() {
		try {
			engine = createReportEngine();
			IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

			HTMLRenderOption options = new HTMLRenderOption();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);

			// "p1" is integer type
			task.setParameterValue("p1", "bbb");
			assertTrue(!task.validateParameters());
			assertEquals(task.getErrors().size(), 1);

			task.setParameterValue("p1", 2);
			assertTrue(task.validateParameters());
			assertEquals(task.getErrors().size(), 0);
			task.run();
			task.close();

			engine.destroy();
			engine = null;

		} catch (EngineException ex) {
			ex.printStackTrace();
		}
	}

	public void tearDown() {
		removeFile(REPORT_DESIGN);
	}

}