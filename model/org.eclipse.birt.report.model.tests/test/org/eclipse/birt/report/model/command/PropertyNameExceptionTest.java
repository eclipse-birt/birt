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

import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by PropertyNameException.
 */

public class PropertyNameExceptionTest extends BaseTestCase
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
				+ File.separator + "PropertyNameExceptionError.out.txt" ) ); //$NON-NLS-1$

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
        
        Structure customColor = new CustomColor();
        customColor.setProperty( CustomColor.NAME_MEMBER, "Color1" ); //$NON-NLS-1$

		PropertyNameException error = new PropertyNameException( table, GridItem.BOOKMARK_PROP ); //$NON-NLS-1$
		print( error );

        error = new PropertyNameException( table, customColor, CustomColor.COLOR_MEMBER );
        print( error );
        
		writer.close( );

		assertTrue( compareTextFile(
				"PropertyNameExceptionError.golden.txt", "PropertyNameExceptionError.out.txt" ) ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void print( PropertyNameException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}