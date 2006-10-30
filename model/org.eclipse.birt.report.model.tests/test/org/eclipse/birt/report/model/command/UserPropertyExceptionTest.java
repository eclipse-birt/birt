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

package org.eclipse.birt.report.model.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by UserPropertyException.
 */

public class UserPropertyExceptionTest extends BaseTestCase
{

	private PrintWriter writer;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		String outputPath = getClassFolder( ) + OUTPUT_FOLDER;
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}

		writer = new PrintWriter( new FileOutputStream( outputFolder
				+ File.separator + "UserPropertyExceptionError.out.txt" ) ); //$NON-NLS-1$

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

		String propName = "userProp1"; //$NON-NLS-1$
		
		UserPropertyException error = new UserPropertyException( table, "", //$NON-NLS-1$
				UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED );
		print( error );

		error = new UserPropertyException( table, propName, 
				UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_INVALID_DISPLAY_ID );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_CHOICE_NAME_REQUIRED );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED );
		print( error );

		error = new UserPropertyException( table, propName,
				UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE );
		print( error );

		writer.close( );

		assertTrue( compareTextFile(
				"UserPropertyExceptionError.golden.txt", "UserPropertyExceptionError.out.txt" ) ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void print( UserPropertyException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}