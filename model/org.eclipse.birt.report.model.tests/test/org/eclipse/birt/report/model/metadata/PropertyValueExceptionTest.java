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

package org.eclipse.birt.report.model.metadata;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by PropertyValueException.
 */

public class PropertyValueExceptionTest extends BaseTestCase
{

	private PrintWriter writer;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		os = new ByteArrayOutputStream( );
		writer = new PrintWriter( os ); 
		resetMetadata();
	}

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages( ) throws Exception
	{
		DesignElement table = new TableItem( );
		table.setName( "customerTable" ); //$NON-NLS-1$

		String value = "badValue";//$NON-NLS-1$

		PropertyValueException error = new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				PropertyType.BOOLEAN_TYPE );
		print( error );

		error = new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND,
				PropertyType.CHOICE_TYPE );
		print( error );

		error = new PropertyValueException( table, TableItem.BOOKMARK_PROP,
				null, PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
		print( error );

		error = new PropertyValueException( table, TableItem.BOOKMARK_PROP,
				null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );
		print( error );

		error = new PropertyValueException( table, TableItem.VISIBILITY_PROP,
				new EmbeddedImage( ),
				PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE );
		print( error );

		error = new PropertyValueException( table.getName( ),
				PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE,
				PropertyType.ELEMENT_REF_TYPE );
		print( error );

		error = new PropertyValueException( table, TableItem.VISIBILITY_PROP,
				"var1", PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS ); //$NON-NLS-1$
		print( error );

		error = new PropertyValueException( table, TableItem.CAPTION_KEY_PROP,
				null, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED );
		print( error );

		error = new PropertyValueException( table, TableItem.VISIBILITY_PROP,
				value, PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED );
		print( error );

		error = new PropertyValueException( table, TableItem.VISIBILITY_PROP,
				value, PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED );
		print( error );

		error = new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED,
				PropertyType.CHOICE_TYPE );
		print( error );

		error = new PropertyValueException( null, table
				.getPropertyDefn( TableItem.HEIGHT_PROP ), new DimensionValue(
				-12.0d, DesignChoiceConstants.UNITS_CM ),
				PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE );
		print( error );

		error = new PropertyValueException( null, table
				.getPropertyDefn( TableItem.HEIGHT_PROP ), new DimensionValue(
				-12.0d, DesignChoiceConstants.UNITS_CM ),
				PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE );
		print( error );

		writer.close( );

		assertTrue( compareFile( "PropertyValueExceptionError.golden.txt" ) ); //$NON-NLS-1$

	}

	private void print( PropertyValueException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}