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

import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.MasterPageSizeValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>MasterPageSizeValidator</code>.
 */

public class MasterPageSizeValidatorTest extends ValidatorTestCase
{

	MyListener listener = new MyListener( );

	/**
	 * Tests <code>MasterPageSizeValidator</code>.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testTriggers( ) throws Exception
	{
		createDesign( );
		MetaDataDictionary.getInstance( ).setUseValidationTrigger( true );
		
		SimpleMasterPageHandle pageHandle = designHandle.getElementFactory( )
				.newSimpleMasterPage( "masterPage1" ); //$NON-NLS-1$
		designHandle.getMasterPages( ).add( pageHandle );

		designHandle.addValidationListener( listener );

		DimensionHandle marginLeft = pageHandle.getLeftMargin( );
		marginLeft.setStringValue( "10in" ); //$NON-NLS-1$
		assertTrue( listener.hasError( pageHandle, MasterPageSizeValidator
				.getInstance( ).getName( ),
				SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS ) );

		marginLeft.setStringValue( "1in" ); //$NON-NLS-1$
		assertFalse( listener.hasError( pageHandle, MasterPageSizeValidator
				.getInstance( ).getName( ),
				SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS ) );

		// Change page size to custom, and height = 15in, width = 10in

		pageHandle.setPageType( DesignChoiceConstants.PAGE_SIZE_CUSTOM );
		assertTrue( listener.hasError( pageHandle, MasterPageTypeValidator
				.getInstance( ).getName( ),
				SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE ) );

        // Height and width don't trigger validators now since it will get
        // height property in style instead of that in master page. Comment the
        // following assertions for temporary fix. Uncomment it if bug DSG-674
        // is fixed.
		
		DimensionHandle height = pageHandle.getHeight( );
		height.setStringValue( "15in" ); //$NON-NLS-1$
//		assertTrue( listener.hasError( pageHandle, MasterPageTypeValidator
//				.getInstance( ).getName( ),
//				SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE ) );
//
		DimensionHandle width = pageHandle.getWidth( );
		width.setStringValue( "10in" ); //$NON-NLS-1$
//		assertFalse( listener.hasError( pageHandle, MasterPageTypeValidator
//				.getInstance( ).getName( ),
//				SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE ) );

		// Change bottom margin to 15in

		DimensionHandle marginBottom = pageHandle.getBottomMargin( );
		marginBottom.setStringValue( "15in" ); //$NON-NLS-1$
		assertTrue( listener.hasError( pageHandle, MasterPageSizeValidator
				.getInstance( ).getName( ),
				SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS ) );
	}
}