/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package testutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.eclipse.birt.core.script.ScriptContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.rules.TestName;

import static org.junit.Assert.*;

/**
 * Common base class for all Dte test cases
 */
abstract public class BaseTestCase {

	@Rule public TestName testName = new TestName();

	protected PrintStream testOut;
	
	/** Top level Javascript scope */
	protected Context jsContext;
	protected Scriptable jsScope;
	protected ScriptContext scriptContext;

	//private static final String TEST_FOLDER = "test";
	private static final String OUTPUT_FOLDER = "DtETestTempDataoutput";
	private static final String INPUT_FOLDER = "input";
	private static final String GOLDEN_FOLDER = "golden";
	
	{
		deleteFile( getOutputFolder() );
	}
	
	//private String classFolder;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void baseSetUp() throws Exception
	{
		// Create test output file
		// We must make sure this folder will be created successfully
		// before we do next job.		
		openOutputFolder();
		openOutputFile();
		
		// Create top-level Javascript scope
		jsContext = Context.enter( );
		jsScope = new ImporterTopLevel(jsContext);
		scriptContext = new ScriptContext().newContext(jsScope);
		
		// Add JS functions testPrint and testPrintln for scripts to write
		// to output file
		jsScope.put("_testCase", jsScope, this );
		jsContext.evaluateString( jsScope,
				"function testPrint(str) { _testCase.testPrint(str); }; " +
				"function testPrintln(str) { _testCase.testPrintln(str); }; "
				, "BaseTestCase.setUp", 1, null );
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
    public void baseTearDown() throws Exception
	{
		Context.exit( );
		closeOutputFile();
	}
	
	/** return test case name */
	public String getTestName( )
	{
		return this.testName.getMethodName( );
	}
	
	/** return input folder */
	protected InputStream getInputFolder( String dataFileName )
	{
		return this.getClass().getResourceAsStream( INPUT_FOLDER + File.separator + dataFileName );
	}
	
	/** return output folder */
	protected static File getOutputFolder()
	{
		return new File( new File(System.getProperty("java.io.tmpdir")),
				OUTPUT_FOLDER );
	}

	protected static String getTempDir()
	{
		return getOutputFolder( ).getAbsolutePath( ) + File.separator + "DataEngineSessionTemp" + File.separator;
	}
	/** return golder folder *//*
	private File getGoldenFolder( )
	{
		return new File( getBaseFolder( ), GOLDEN_FOLDER );
	}*/
	
	/** open output folder */
	private void openOutputFolder( )
	{
		File outputFolder = getOutputFolder( );
		if ( outputFolder.exists( ) == false )
		{
			outputFolder.mkdir( );
		}
	}

	/** Opens defalt test output file. File name is ClassName.TestName.txt */
	protected void openOutputFile( ) throws IOException
	{
		File outputFile = new File( getOutputFolder( ),
				getOutputFileName( ) );
		testOut = new PrintStream( new FileOutputStream( outputFile, false ) );
	}
	
	/** close ouput file stream */
	protected void closeOutputFile( ) throws IOException
	{
		if ( testOut != null )
		{
			testOut.close( );
			testOut = null;
		}
	}
	
	/** return default output file name*/
	private String getOutputFileName() 
	{
		String className = this.getClass().getName();
		int lastDotIdx = className.lastIndexOf( '.' );
		if  ( lastDotIdx >= 0 )
			className = className.substring(lastDotIdx + 1);
		return className + "." + this.getTestName() + ".txt";
	}
	
	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path where the test java source file locates.
	 */
/*	private File getBaseFolder( )
	{
		if ( classFolder == null )
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
						pathBase = pathBase.substring( 0,
								pathBase.length( ) - 4 );
					if ( pathBase.endsWith( "bin" ) ) //$NON-NLS-1$
						pathBase = pathBase.substring( 0,
								pathBase.length( ) - 3 );
				}
			}

			pathBase = pathBase + TEST_FOLDER + "/";
			classFolder = pathBase.substring( 1 );
		}

		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = classFolder + className.replace( '.', '/' );

		return new File( className );
	}
	
*/	/**
	 * Asserts that output file matches the golden file. Default file name for
	 * current test case is used for both files
	 */
	protected void checkOutputFile( ) throws IOException
	{
		if ( testOut != null )
			testOut.flush();
		
		String name = getOutputFileName();
		checkOutputFile( name, name );
	}
	
	/**
	 * Asserts that output file matches the golden file.
	 */
	private void checkOutputFile( String goldenFileName, String outputFileName )
		throws IOException
	{
		InputStream golden = this.getClass().getResourceAsStream( GOLDEN_FOLDER + File.separator + goldenFileName );
		File outputFile = new File( getOutputFolder(),  outputFileName);
		assertTrue( compareTextFile( golden, outputFile ));
	}

	/**
	 * compare two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the 1st file name to be compared.
	 * @param outputFileName
	 *            the 2nd file name to be compared.
	 * @return True if two text file is same line by line
	 */
	private boolean compareTextFile( InputStream golden, File outputFile )
			throws IOException
	{
		boolean same = true;
	
		InputStreamReader readerA = new InputStreamReader( golden );
		FileReader readerB = new FileReader( outputFile );
		BufferedReader	lineReaderA = new BufferedReader( readerA );
		BufferedReader	lineReaderB = new BufferedReader( readerB );
	
		String strA = lineReaderA.readLine( ).trim( );
		String strB = lineReaderB.readLine( ).trim( );
		while ( strA != null && strB != null )
		{
			same = strA.trim( ).equals( strB.trim( ) );
			if ( !same )
			{
				break;
			}
	
			strA = lineReaderA.readLine( );
			strB = lineReaderB.readLine( );
		}
		same = strA == null && strB == null;
		golden.close();	
		readerA.close( );
		readerB.close( );
		lineReaderA.close( );
		lineReaderB.close( );
	
		return same;
	}
	
	/** print to console and stream */
    public void testPrint( String str )
	{
		System.out.print(str);
		if ( testOut != null )
			testOut.print( str );
	}
	
	/** println to console and stream */
    public void testPrintln( String str )
	{
		System.out.println(str);
		if ( testOut != null )
			testOut.println( str );
	}
	
	public static void deleteFile ( File f )
	{
		if ( f.isDirectory() )
		{
			String[] children = f.list();
			for ( String child : children )
			{
				deleteFile( new File( f, child ));
			}
		}
		f.delete();
		f.deleteOnExit();
	}
	
}
