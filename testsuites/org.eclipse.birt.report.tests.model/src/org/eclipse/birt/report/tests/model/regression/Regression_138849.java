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
 * Unsupported backward compatibility for resource file
 * <ol>
 * <li>Open the attached design file which has a resource file "a.properties"
 * <li>Put the attached properties file to the folder which design file exists
 * <li>Preview the report
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Put design file and resource file in the same directory, check label display
 * text
 * </p>
 */

public class Regression_138849 extends BaseTestCase {

	private String filename = "Regression_138849.xml"; //$NON-NLS-1$
	private String propname = "a.properties";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(propname, propname);
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_138849() throws DesignFileException {
		openDesign(filename);

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		System.out.println(label.getDisplayText());
		assertEquals("actuate", label.getDisplayText()); //$NON-NLS-1$

	}
}
