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

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by StyleException.
 */

public class StyleExceptionTest extends BaseTestCase
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
				+ File.separator + "StyleExceptionError.out.txt" ) ); //$NON-NLS-1$

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

		StyleException error = new StyleException( table, "style1", StyleException.DESIGN_EXCEPTION_FORBIDDEN ); //$NON-NLS-1$
		print( error );

		error = new StyleException( table, "style1", StyleException.DESIGN_EXCEPTION_NOT_FOUND ); //$NON-NLS-1$
		print( error );

		writer.close( );

		assertTrue( compareTextFile(
				"StyleExceptionError.golden.txt", "StyleExceptionError.out.txt" ) ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void print( StyleException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}
	
	public void testModelException( )
	{
		ModelException e = new SemanticException( new Label( ), "test error code" ); //$NON-NLS-1$
		assertEquals( ModelException.PLUGIN_ID, e.getPluginId( ) );
	}

}