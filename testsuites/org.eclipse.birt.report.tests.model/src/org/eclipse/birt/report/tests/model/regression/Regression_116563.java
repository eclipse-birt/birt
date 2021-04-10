/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Element from library cannot appear after delete another same library
 * <p>
 * Step:
 * <ol>
 * <li>New a library and insert a label.
 * <li>Open the report and include the library twice named "libA" and "libB".
 * <li>Drop the label from libA and libB to layout.
 * <li>Priview and see two labels are appear.
 * <li>Delete libB in outline and Priview
 * </ol>
 * <p>
 * <b>Actual result:</b>
 * <p>
 * No error pop up, and all the label cannot appear.
 * <p>
 * <b>Expected result:</b>
 * <p>
 * Expected result: An Error message pop up because the library cannot delete
 * when some item in layout.
 * </p>
 * <b>Test description:</b>
 * <p>
 * Make sure that including the same library twice or over twice is not allowed.
 * </p>
 */
public class Regression_116563 extends BaseTestCase {

	private final static String LIBRARY = "regression_116563_lib.xml"; //$NON-NLS-1$
	private final static String REPORT = "regression_116563.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(LIBRARY, LIBRARY);
		copyResource_INPUT(REPORT, REPORT);

	}

	/**
	 * @throws SemanticException
	 * @throws DesignFileException
	 * 
	 */
	public void test_regression_116563() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		// designHandle.includeLibrary(
		// getClassFolder( ) + "/" + INPUT_FOLDER + "/" + LIBRARY,
		// "regression_116563_lib" ); //$NON-NLS-1$
		designHandle.includeLibrary(this.getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + LIBRARY,
				"regression_116563_lib"); //$NON-NLS-1$

		try {
			// should throw exception when add the same library the second time.

			// designHandle.includeLibrary( getClassFolder( ) + "/" + INPUT_FOLDER
			// + "/" + LIBRARY, "regression_116563_lib_2" ); //$NON-NLS-1$
			designHandle.includeLibrary(this.getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + LIBRARY, //$NON-NLS-2$
					"regression_116563_lib_2");
			fail();
		} catch (Exception e) {
			// success
		}
	}
}
