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

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>IncludedCssStyleSheetValidatorTest</code>.
 */

public class IncludedCssStyleSheetValidatorTest extends ValidatorTestCase
{

	/**
	 * Validates the input file name of the included Css Style Sheet.
	 * 
	 * @throws Exception
	 */
	public void testIncludedCssStyleSheetValidator( ) throws Exception
	{
		MetaDataDictionary.getInstance( ).setUseValidationTrigger( false );
		openDesign( "IncludedCssStyleSheetValidatorTest.xml" ); //$NON-NLS-1$

		List<ErrorDetail> list = design.getErrorList( );

		assertEquals( 1, list.size( ) );

	}

	/**
	 * Validates the input file name of the included Css Style Sheet which
	 * located in theme.
	 * 
	 * @throws Exception
	 */
	public void testIncludedCssStyleSheetValidatorInTheme( ) throws Exception
	{
		MetaDataDictionary.getInstance( ).setUseValidationTrigger( false );
		openLibrary( "IncludedCssStyleSheetValidatorInThemeTest.xml" ); //$NON-NLS-1$

		List<ErrorDetail> list = libraryHandle.getModule( ).getErrorList( );

		assertEquals( 1, list.size( ) );
	}
}
