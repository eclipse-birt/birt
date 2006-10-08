/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import junit.framework.TestCase;

/**
 * Base chart test case.
 */
public class ChartTestCase extends TestCase
{

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "/output/"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "/golden/"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		// make the output directory.

		File outputFolder = new File( this.getClassFolder( ) + OUTPUT_FOLDER );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}
	}

	/**
	 * Compares two byte arrays. Disallow <code>null</code> values.
	 * 
	 * @param bytes1
	 * @param bytes2
	 * @return <code>true</code> if <code>bytes1</code> and
	 *         <code>bytes2</code> have the same lengths and content. In all
	 *         other cases returns <code>false</code>.
	 */
	protected boolean compare( byte[] bytes1, byte[] bytes2 )
	{
		if ( bytes1.length != bytes2.length )
		{
			return false;
		}

		for ( int i = 0; i < bytes1.length; i++ )
		{
			if ( bytes1[i] != bytes2[i] )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Compare golden with output byte by byte.
	 * 
	 * @param golden
	 * @param output
	 * @return
	 * @throws Exception
	 */

	protected boolean compareBytes( String golden, String output )
			throws Exception
	{
		InputStream is1 = new FileInputStream( this.getClassFolder( )
				+ GOLDEN_FOLDER + golden );
		InputStream is2 = new FileInputStream( this.getClassFolder( )
				+ OUTPUT_FOLDER + output );

		return this.compare( is1, is2 );
	}

	/**
	 * computeFiles method compares two input streams and returns whether the
	 * contents of the input streams match.
	 * 
	 * @param left -
	 *            inputstream of the first resource
	 * @param right -
	 *            inputstream of the second resource
	 * @return true if the contents match; otherwise, false is returned.
	 * @throws Exception
	 *             thrown if io errors occur
	 */
	protected boolean compare( InputStream golden, InputStream output )
			throws Exception
	{
		int goldenChar = -1;
		while ( ( goldenChar = golden.read( ) ) != -1 )
		{
			if ( goldenChar != output.read( ) )
				return false;
		}

		return true;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder( )
	{
		String pathBase = null;

		ProtectionDomain domain = this.getClass( ).getProtectionDomain( );
		if ( domain != null )
		{
			CodeSource source = domain.getCodeSource( );
			if ( source != null )
			{
				URL url = source.getLocation( );
				pathBase = url.getPath( );

				if ( pathBase.endsWith( "bin/" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 4 );
				if ( pathBase.endsWith( "bin" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 3 );
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = pathBase + className.replace( '.', '/' );

		return className;
	}

	/**
	 * Locates the folder where the unit test java source file is saved, used
	 * in standalone test case.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder2( )
	{
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = TEST_FOLDER + className.replace( '.', '/' );

		return className;
	}
}
