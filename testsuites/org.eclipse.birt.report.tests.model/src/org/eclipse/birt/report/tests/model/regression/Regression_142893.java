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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * New library, assign resource file "r1.properties" which contains
 * key/value-pair "k1=v1"
 * <p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>New label in library, set text key of label to "k1"
 * <li>New report, assign resource file "r2.properties" which does not contain
 * key/value-pair "k1=v1"
 * <li>Drag and drop label from library to report body
 * <li>Preview
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * Report shows "v1"
 * </p>
 * <b>Actual result:</b>
 * <p>
 * Report shows nothing.
 * </p>
 * Test description:
 * <p>
 * Following the bug description, label display text should be "v1"
 * </p>
 */

public class Regression_142893 extends BaseTestCase {

	private String filename = "Regression_142893.xml"; //$NON-NLS-1$
	private String propname = "r_lib.properties";
	private String libname = "Regression_142893_lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(propname, propname);
		copyResource_INPUT(libname, libname);
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_142893() throws DesignFileException {
		openDesign(filename);

		LabelHandle label = (LabelHandle) designHandle.findElement("NewLabel"); //$NON-NLS-1$
		System.out.println(label.getDisplayText());
		assertEquals("v1", label.getDisplayText()); //$NON-NLS-1$

	}
}
