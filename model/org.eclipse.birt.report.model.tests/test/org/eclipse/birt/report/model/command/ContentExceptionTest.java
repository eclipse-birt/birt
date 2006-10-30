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

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ContentException.
 */

public class ContentExceptionTest extends BaseTestCase
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
				+ File.separator + "ContentExceptionError.out.txt" ) ); //$NON-NLS-1$

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

		DesignElement row = new TableRow( );

		ContentException error = new ContentException( table,
				TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_WRONG_TYPE );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_DROP_FORBIDDEN );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL );

		error = new ContentException( row, TableItem.COLUMN_SLOT, table,
				ContentException.DESIGN_EXCEPTION_RECURSIVE );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_HAS_NO_CONTAINER );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_MOVE_FORBIDDEN );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED );
		print( error );

		error = new ContentException( table, TableItem.COLUMN_SLOT,
				new TemplateReportItem( "test" ), //$NON-NLS-1$
				ContentException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT );
		print( error );

		// System.out.println(error.getLocalizedMessage());
		writer.close( );

		assertTrue( compareTextFile(
				"ContentExceptionError.golden.txt", "ContentExceptionError.out.txt" ) ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void print( ContentException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}
