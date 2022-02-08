/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * NPE when opening report template
 * <p>
 * I am getting following stacktrace when opening a report file (which worked ok
 * in BIRT 2.1) in BIRT 2.1.1 Report Design perspective:
 * ******************************** java.lang.NullPointerException
 * <p>
 * <b>Test description:</b>
 * <p>
 * Open the template file, make sure that no exception is throwed out.
 * <p>
 */
public class Regression_159858 extends BaseTestCase {

	private final static String REPORT = "Invoice_template.rptdesign"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_159858() throws DesignFileException {
		openDesign(REPORT);
	}
}
