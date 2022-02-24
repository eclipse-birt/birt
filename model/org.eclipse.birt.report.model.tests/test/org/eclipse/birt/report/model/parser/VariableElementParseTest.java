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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Class to test VariableElement cases.
 */
public class VariableElementParseTest extends BaseTestCase {

	/**
	 * The extension element that uses variable element.
	 */

	private static final String FILE_NAME = "VariableElementParseTest.xml"; //$NON-NLS-1$
	private static final String DUPLICATE_NAME_FILE = "DuplicatedVariableNameTest.xml"; //$NON-NLS-1$
	private static final String VARIABLE_ELEMENT_IN_EXTENDED_ELEMENT = "VariableElementInExtendedElement.xml"; //$NON-NLS-1$

	/**
	 * Tests to get values for variable element.
	 * 
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign(FILE_NAME);

		ExtendedItemHandle action1 = (ExtendedItemHandle) designHandle.findElement("action1"); //$NON-NLS-1$

		List variables = action1.getListProperty("variables"); //$NON-NLS-1$
		VariableElementHandle var1 = (VariableElementHandle) variables.get(0);

		assertEquals("variable1", var1.getVariableName()); //$NON-NLS-1$
		assertEquals("expression for variable", var1.getValue()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.VARIABLE_TYPE_REPORT, var1.getType());

		var1.setVariableName("new variable1"); //$NON-NLS-1$
		var1.setValue("new expression for variable"); //$NON-NLS-1$
		var1.setType(DesignChoiceConstants.VARIABLE_TYPE_PAGE);

		save();

		assertTrue(compareFile("VariableElementParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testDuplicatedVariableName() throws Exception {
		// the variable elements have same name, the report design could not be
		// opened successfully.
		try {
			openDesign(DUPLICATE_NAME_FILE);
			fail();
		} catch (DesignFileException e) {
			assertEquals(DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR, e.getErrorCode());
		}

		// the the variable elements have same name, they are located in
		// extended item, the report design can be opened successfully..

		openDesign(VARIABLE_ELEMENT_IN_EXTENDED_ELEMENT);
		assertNotNull(designHandle);

	}

}
