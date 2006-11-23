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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import junit.framework.TestCase;

/**
 * Base chart test case.
 */
public class ChartTestCase extends TestCase
{

	protected static final String TEST_FOLDER = "src"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		// make the output directory.

		String tempDir = System.getProperty( "java.io.tmpdir" );
		if ( !tempDir.endsWith( File.separator ) )
			tempDir += File.separator;

		String outputPath = tempDir + getFullQualifiedClassName( ) + "/"
				+ OUTPUT_FOLDER;
		outputPath = outputPath.replace( '\\', '/' );

		File outputFolder = new File( outputPath );

		File parent = new File( outputPath ).getParentFile( );

		if ( parent != null )
		{
			parent.mkdirs( );
		}

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
		// InputStream is1 = new FileInputStream(
		// this.getFullQualifiedClassName( ) + "/"
		// + GOLDEN_FOLDER + "/" + golden );

		String className = getFullQualifiedClassName( );
		className = className.replace( '.', '/' );
		golden = className + "/" + GOLDEN_FOLDER + "/" + golden;

		// InputStream is1 = new FileInputStream( golden );

		InputStream is1 = getClass( ).getClassLoader( ).getResourceAsStream(
				golden );
		InputStream is2 = new FileInputStream( this.genOutputFile( output ) );

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
	 * Locates the folder where the unit test java source file is saved, used in
	 * standalone test case.
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

	/**
	 * Get the class name.
	 * 
	 * @return the class name
	 */
	protected String getFullQualifiedClassName( )
	{
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );

		return className;
	}

	/**
	 * Set the output path. And the path will set in java.io.tmpdir.
	 * 
	 * @return the the output path
	 */
	protected String genOutputFile( String output )
	{
		String tempDir = System.getProperty( "java.io.tmpdir" ); //$NON-NLS-1$
		if ( !tempDir.endsWith( File.separator ) )
			tempDir += File.separator;
		String outputFile = tempDir + getFullQualifiedClassName( ) //$NON-NLS-1$
				+ "/" + OUTPUT_FOLDER + "/" + output;
		return outputFile;
	}

	/**
	 * Make a copy of a given file to the target file.
	 * 
	 * @param src:
	 *            the file where to copy from
	 * @param tgt:
	 *            the target file to copy to
	 * @param folder:
	 *            the folder that the copied file in.
	 */
	protected void copyResource( String src, String tgt, String folder )
	{

		String className = getFullQualifiedClassName( );
		tgt = className + "/" + folder + "/" + tgt;
		className = className.replace( '.', '/' );

		src = className + "/" + folder + "/" + src;

		File parent = new File( tgt ).getParentFile( );

		if ( parent != null )
		{
			parent.mkdirs( );
		}

		InputStream in = getClass( ).getClassLoader( )
				.getResourceAsStream( src );
		assertTrue( in != null );
		try
		{
			FileOutputStream fos = new FileOutputStream( tgt );
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ( ( readCount = in.read( fileData ) ) != -1 )
			{
				fos.write( fileData, 0, readCount );
			}
			fos.close( );
			in.close( );

		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	protected void copyResource_INPUT( String input_resource, String input )
	{
		this.copyResource( input_resource, input, INPUT_FOLDER );
	}

	protected void copyResource_GOLDEN( String input_resource, String golden )
	{
		this.copyResource( input_resource, golden, GOLDEN_FOLDER );
	}

	protected void copyResource_SCRIPT( String input_resource, String script )
	{
		this.copyResource( input_resource, script, "input/scripts" );
	}

	public void removeFile( File file )
	{
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			for ( int i = 0; i < children.length; i++ )
			{
				removeFile( children[i] );
			}
		}
		if ( file.exists( ) )
		{
			if ( !file.delete( ) )
			{
				System.out.println( file.toString( ) + " can't be removed" ); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Remove a given file or directory recursively.
	 * 
	 * @param file
	 */

	public void removeFile( String file )
	{
		removeFile( new File( file ) );
	}

	public void removeResource( )
	{
		String className = getFullQualifiedClassName( );
		removeFile( className );
	}
}
