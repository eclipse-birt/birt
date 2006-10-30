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

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ContentException.
 */

public class CustomMsgExceptionTest extends BaseTestCase
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
				+ File.separator + "CustomMsgExceptionError.out.txt" ) ); //$NON-NLS-1$

	}

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages( ) throws Exception
	{

		SessionHandle session = DesignEngine.newSession( TEST_LOCALE );
		ReportDesign report = session.createDesign( ).getDesign( );

		CustomMsgException error = new CustomMsgException( report,
				CustomMsgException.DESIGN_EXCEPTION_RESOURCE_KEY_REQUIRED );
		print( error );

		error = new CustomMsgException( report,
				"ResourceKey.ReportDesign.Title", "en", //$NON-NLS-1$ //$NON-NLS-2$
				CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE );
		print( error );

		error = new CustomMsgException( report, null, "abc", //$NON-NLS-1$
				CustomMsgException.DESIGN_EXCEPTION_INVALID_LOCALE );
		print( error );

		error = new CustomMsgException( report,
				"ResourceKey.ReportDesign.Title", "en", //$NON-NLS-1$ //$NON-NLS-2$
				CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND );
		print( error );

		writer.close();
		
		assertTrue( compareTextFile( "CustomMsgExceptionError.golden.txt", //$NON-NLS-1$
				"CustomMsgExceptionError.out.txt" ) ); //$NON-NLS-1$

	}

	private void print( CustomMsgException error )
	{
		writer.write( error.getErrorCode( ) );
		for ( int i = error.getErrorCode( ).length( ); i < 60; i++ )
			writer.write( " " ); //$NON-NLS-1$
		writer.println( error.getMessage( ) );
	}

}