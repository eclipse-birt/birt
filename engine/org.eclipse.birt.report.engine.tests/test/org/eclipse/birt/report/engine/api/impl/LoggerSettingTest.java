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
package org.eclipse.birt.report.engine.api.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConfig;

import junit.framework.TestCase;

public class LoggerSettingTest extends TestCase {

	private ReportEngine createReportEngine(Level logLevel, String fileName) {
		EngineConfig engineConfig = new EngineConfig();
		engineConfig.setLogConfig(null, logLevel);
		engineConfig.setLogFile(fileName);
		return new ReportEngine(engineConfig);
	}

	private void verifyResult(Level level, int handlerNum) {
		// find the first logger in hierarchy with level set
		Logger bl = Logger.getLogger("org.eclipse.birt.report.engine.api.impl");
		while (bl != null && bl.getLevel() == null) {
			bl = bl.getParent();
		}
		assertNotNull(bl);

		if (level == null) {
			if (bl.getLevel() != null) {
				assertEquals(Level.INFO, bl.getLevel());
			}
			assertTrue(bl.getHandlers().length <= 1);
		} else {
			assertEquals(level, bl.getLevel());
			assertEquals(handlerNum, bl.getHandlers().length);
		}
	}

	public void test1() {
		verifyResult(null, 0);
		ReportEngine r1 = createReportEngine(Level.WARNING, null);
		verifyResult(Level.WARNING, 1);
		ReportEngine r2 = createReportEngine(Level.INFO, null);
		verifyResult(Level.INFO, 1);
		ReportEngine r3 = createReportEngine(Level.SEVERE, null);
		verifyResult(Level.SEVERE, 1);
		ReportEngine r4 = createReportEngine(null, null);
		verifyResult(Level.SEVERE, 1);
		r4.destroy();
		verifyResult(Level.SEVERE, 1);
		r2.destroy();
		verifyResult(Level.SEVERE, 1);
		r3.destroy();
		verifyResult(Level.WARNING, 1);
		r1.destroy();
		verifyResult(Level.WARNING, 0);
	}

	public void test2() {
		ReportEngine r1 = createReportEngine(Level.WARNING, null);
		verifyResult(Level.WARNING, 1);
		r1.changeLogLevel(Level.INFO);
		verifyResult(Level.INFO, 1);
	}
}
