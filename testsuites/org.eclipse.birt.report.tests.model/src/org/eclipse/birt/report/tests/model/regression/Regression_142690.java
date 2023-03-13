/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Drill-throught report path is not right generated. such as
 * ./../../../Eclipse/eclipse/null/D:/TestFiles/Design/BIRT/XX.rptdesign
 * </p>
 * Test description:
 * <p>
 * Test different relative path
 */
public class Regression_142690 extends BaseTestCase {

	/**
	 *
	 */
	public void test_regression_142690() {
		// Same root
		assertEquals("../lib/lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"E:/birt/sampleReports/reportdesigns/", //$NON-NLS-1$
				"/E:/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		// Different root
		assertEquals("E:/birt/sampleReports/lib/lib1.rptlibrary", URIUtil //$NON-NLS-1$
				.getRelativePath("D:/birt/sampleReports/reportdesigns/", //$NON-NLS-1$
						"E:/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		// Sub folder
		assertEquals("lib/lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"E://birt//sampleReports//reportdesigns//", //$NON-NLS-1$
				"E://birt//sampleReports//reportdesigns//lib//lib1.rptlibrary")); //$NON-NLS-1$

		// Path with space
		assertEquals("spaced directory name/aa/lib.xml", URIUtil.getRelativePath( //$NON-NLS-1$
				"D://", "D://spaced directory name//aa//lib.xml")); //$NON-NLS-1$//$NON-NLS-2$
	}
}
