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

import org.eclipse.birt.report.model.api.command.CircularExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ExtendsException.
 */

public class ExtendsExceptionTest extends BaseTestCase
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
				+ File.separator + "ExtendsExceptionError.out.txt" ) ); //$NON-NLS-1$

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

		DesignElement parent = new GridItem( );
		parent.setName( "parentGrid" ); //$NON-NLS-1$

		String extendsName = "parentTable"; //$NON-NLS-1$

		ExtendsException error = new InvalidParentException( table,
				extendsName,
				InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND );
		print( error );

		error = new WrongTypeException( table, parent,
				WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE );
		print( error );

		error = new ExtendsForbiddenException( table, extendsName,
				ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND );
		print( error );

		error = new CircularExtendsException( table, extendsName,
				CircularExtendsException.DESIGN_EXCEPTION_SELF_EXTEND );
		print( error );

		error = new CircularExtendsException( table, parent,
				CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR );
		print( error );

		error = new InvalidParentException( table, extendsName,
				InvalidParentException.DESIGN_EXCEPTION_UNNAMED_PARENT );
		print( error );

		error = new ExtendsForbiddenException(
				table,
				extendsName,
				ExtendsForbiddenException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT );
		print( error );

		writer.close( );

		assertTrue( compareTextFile( "ExtendsExceptionError.golden.txt", //$NON-NLS-1$
				"ExtendsExceptionError.out.txt" ) ); //$NON-NLS-1$ 

	}

	private void print( ExtendsException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}