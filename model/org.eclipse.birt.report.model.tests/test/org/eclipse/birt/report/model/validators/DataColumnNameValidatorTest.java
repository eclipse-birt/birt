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

public class DataColumnNameValidatorTest extends BaseTestCase
{

	/**
	 * Test validate method.
	 * 
	 * @throws Exception
	 */

	public void testValidate( ) throws Exception
	{
		openDesign( "DataColumnNameValidator.xml" ); //$NON-NLS-1$
		DataItemHandle datawithBind = (DataItemHandle) designHandle
				.getElementByID( 146l );
		DataItemHandle datawithoutBind = (DataItemHandle) designHandle
				.getElementByID( 110l );

		List result = DataColumnNameValidator.getInstance( ).validate(
				designHandle.getModule( ), datawithBind.getElement( ) );
		assertEquals( 0, result.size( ) );

		result = DataColumnNameValidator.getInstance( ).validate(
				designHandle.getModule( ), datawithoutBind.getElement( ) );
		assertEquals( 0, result.size( ) );

	}
}
