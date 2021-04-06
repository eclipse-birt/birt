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

package org.eclipse.birt.report.model.validators;

import java.util.List;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.validators.DataColumnNameValidator;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test <code>DataColumnNameValidator</code>
 * 
 */

public class DataColumnNameValidatorTest extends BaseTestCase {

	/**
	 * Test validate method.
	 * 
	 * @throws Exception
	 */

	public void testValidate() throws Exception {
		openDesign("DataColumnNameValidatorTest.xml"); //$NON-NLS-1$
		DataItemHandle datawithBind = (DataItemHandle) designHandle.getElementByID(146l);
		DataItemHandle datawithoutBind = (DataItemHandle) designHandle.getElementByID(110l);

		List result = DataColumnNameValidator.getInstance().validate(designHandle.getModule(),
				datawithBind.getElement());
		assertEquals(0, result.size());

		result = DataColumnNameValidator.getInstance().validate(designHandle.getModule(), datawithoutBind.getElement());
		assertEquals(0, result.size());

	}

	/**
	 * Tests validate column name in grid,if the data item locates in grid. see bug
	 * 244914
	 * 
	 * @throws Exception
	 */
	public void testValidateColumnNameInGrid() throws Exception {
		openDesign("DataColumnNameValidatorTest_2.xml"); //$NON-NLS-1$

		assertEquals(0, designHandle.getErrorList().size());

	}

	/**
	 * Tests the data in the inner table without binding and data-set.
	 * 
	 * @throws Exception
	 */
	public void testDataInInnerTable() throws Exception {
		openDesign("DataColumnNameValidatorTest_1.xml"); //$NON-NLS-1$
		DataItemHandle datawithBind = (DataItemHandle) designHandle.findElement("test_data"); //$NON-NLS-1$

		List result = DataColumnNameValidator.getInstance().validate(designHandle.getModule(),
				datawithBind.getElement());
		assertEquals(0, result.size());
	}

	/**
	 * Tests validate column name. If the column locates in template, the column
	 * name need not be checked.
	 * 
	 * @throws Exception
	 */
	public void testValidateColumnNameInTemplate() throws Exception {
		openDesign("ValidateColumnNameInTemplateTest.xml"); //$NON-NLS-1$

		assertEquals(0, designHandle.getErrorList().size());
	}

}
