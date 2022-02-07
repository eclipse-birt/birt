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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b> Format Number/Date Time/String, Hyperlink,
 * Visibillty, User Properties of date item from library can't be restore
 * <p>
 * Step:
 * <ol>
 * <li>New a library and add a data item.
 * <li>Publish the library to resource folder
 * <li>New a report design and use the library
 * <li>Drop the data item from library explorer into layout
 * <li>Modify the property of Format Number/Date Time/String, Hyperlink,
 * Visibillty, User Properties.
 * <li>Click the restore button.
 * </ol>
 * <p>
 * <b>Test description:</b>
 * <p>
 * Change the structure property (string format) of a extended data item, make
 * sure that the properties can be restored.
 * <p>
 */
public class Regression_146758 extends BaseTestCase {

	private final static String REPORT = "regression_146758.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_146758() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		DataItemHandle data = (DataItemHandle) designHandle.findElement("NewData"); //$NON-NLS-1$
		data.getPrivateStyle().setStringFormatCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE);

		ArrayList elements = new ArrayList();
		elements.add(data);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());

	}
}
