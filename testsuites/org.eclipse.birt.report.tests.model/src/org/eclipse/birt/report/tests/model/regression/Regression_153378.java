/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Label text won't show up when the text key is not found
 * <p>
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>Add a label, set text to "aaa"
 * <li>Add a resource file "a" to the report, add a text key "k1" which is not
 * in the a.properties to the label
 * <li>Preview in Web Viewer
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * Label display "aaa" when the text key is not found
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Nothing in preview
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Add a label, set its key to the value which are in/not in .properties file.
 * Check the display text for label
 * 
 */
public class Regression_153378 extends BaseTestCase {

	private String filename = "Regression_153378.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_153378() throws DesignFileException, SemanticException {
		openDesign(filename);
		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		assertEquals("aaa", label.getDisplayText()); //$NON-NLS-1$

		label.setTextKey("k1"); //$NON-NLS-1$
		assertEquals("actuate", label.getDisplayText()); //$NON-NLS-1$

	}
}
