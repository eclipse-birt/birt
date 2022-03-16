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

package org.eclipse.birt.report.engine.regression;

import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;

public class Test451629 extends EngineCase {
	@Override
	protected void setUp() {
		EngineConfig config = new EngineConfig();
		engine = createReportEngine(config);
	}

	public void testRun() {
		IRunTask task = null;
		try {
			setUp();
			IReportRunnable runnable = this.engine
					.openReportDesign("test/org/eclipse/birt/report/engine/regression/451629.rptdesign");
			task = engine.createRunTask(runnable);

			task.run("utest/document");
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (task != null) {
				task.close();
			}
		}
		IReportDocument document = null;

		try {
			document = engine.openReportDocument("utest/document");

			List<String> errorList = document.getDocumentErrors();
			int numberoferrormessage = 0;
			for (String error : errorList) {
				if ("The task is cancelled.".equals(error)) {
					numberoferrormessage++;
				}
			}
			assertEquals(1, numberoferrormessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			document.close();
		}

	}

}
