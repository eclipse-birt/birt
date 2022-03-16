/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.prototype.excel;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.engine.emitter.prototype.excel");
		// $JUnit-BEGIN$

		/* in package: org.eclipse.birt.report.engine.emitter.prototype.excel */
		suite.addTestSuite(org.eclipse.birt.report.engine.emitter.prototype.excel.DateSymbolTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.emitter.prototype.excel.ExcelWriterTest.class);
		// $JUnit-END$
		return suite;
	}
}
