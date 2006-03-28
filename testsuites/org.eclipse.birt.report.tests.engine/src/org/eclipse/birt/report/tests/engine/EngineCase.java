/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.ReportRunner;

public abstract class EngineCase extends TestCase
{

	private String caseName;

	protected static final String BUNDLE_NAME = "org.eclipse.birt.report.tests.engine.messages";//$NON-NLS-1$

	protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle( BUNDLE_NAME );

	protected static final String PLUGIN_NAME = "org.eclipse.birt.report.tests.engine"; //$NON-NLS-1$
	protected static final String PLUGINLOC = "/org.eclipse.birt.report.tests.engine/"; //$NON-NLS-1$

	protected static final String PLUGIN_PATH = System.getProperty( "user.dir" ) //$NON-NLS-1$
			+ "/plugins/" + PLUGINLOC.substring( PLUGINLOC.indexOf( "/" ) + 1 ) //$NON-NLS-1$//$NON-NLS-2$
			+ "bin/"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden"; //$NON-NLS-1$

	protected ReportEngine engine = null;

	public static void main( String[] args )
	{
		junit.awtui.TestRunner.run( EngineCase.class );
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		EngineConfig config = new EngineConfig( );
		IPlatformContext context = new PlatformFileContext( );
		config.setEngineContext( context );

		this.engine = new ReportEngine( config );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	/**
	 * Constructor.
	 */

	public EngineCase( )
	{
		super( null );
	}

	/**
	 * Constructor for DemoCase.
	 * 
	 * @param name
	 */
	public EngineCase( String name )
	{
		super( name );
	}

	protected void setCase( String caseName )
	{
		// set the case and emitter manager accroding to caseName.
		this.caseName = caseName;
	}

	protected void runCase( String args[] )
	{
		Vector runArgs = new Vector( );
		// invoke the report runner.
		String input = PLUGIN_PATH + System.getProperty( "file.separator" ) //$NON-NLS-1$
				+ RESOURCE_BUNDLE.getString( "CASE_INPUT" ); //$NON-NLS-1$
		input += System.getProperty( "file.separator" ) + caseName //$NON-NLS-1$
				+ ".rptdesign"; //$NON-NLS-1$
		System.out.println( "input is : " + input ); //$NON-NLS-1$

		// run report runner.

		if ( args != null )
		{
			for ( int i = 0; i < args.length; i++ )
			{
				runArgs.add( args[i] );
			}
		}
		runArgs.add( "-f" ); //$NON-NLS-1$
		runArgs.add( "test" ); //$NON-NLS-1$
		runArgs.add( input );

		args = (String[]) runArgs.toArray( new String[runArgs.size( )] );
		ReportRunner.main( args );
	}

	/*
	 * Add below three methods to test RunTask
	 */
	public void copyStream( String src, String tgt )
	{
		InputStream in = getClass( ).getClassLoader( )
				.getResourceAsStream( src );
		assertTrue( in != null );
		try
		{
			int size = in.available( );
			byte[] buffer = new byte[size];
			in.read( buffer );
			OutputStream out = new FileOutputStream( tgt );
			out.write( buffer );
			out.close( );
			in.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
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
				System.out.println( file.toString( ) + " can't be removed" );
			}
		}
	}

	public void removeFile( String file )
	{
		removeFile( new File( file ) );
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder( )
	{
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = TEST_FOLDER + className.replace( '.', '/' );

		return className;
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param golden
	 *            the 1st file name to be compared.
	 * @param output
	 *            the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */

	protected boolean compareHTML( String golden, String output )
			throws Exception
	{
		FileReader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer( );

		try
		{
			golden = getClassFolder( ) + "/" + GOLDEN_FOLDER + "/" + golden; //$NON-NLS-1$//$NON-NLS-2$
			output = getClassFolder( ) + "/" + OUTPUT_FOLDER + "/" + output; //$NON-NLS-1$//$NON-NLS-2$

			readerA = new FileReader( golden );
			readerB = new FileReader( output );

			same = compareTextFile( readerA, readerB, output );
		}
		catch ( IOException e )
		{
			errorText.append( e.toString( ) );
			errorText.append( "\n" ); //$NON-NLS-1$
			e.printStackTrace( );
		}
		finally
		{
			try
			{
				readerA.close( );
				readerB.close( );
			}
			catch ( Exception e )
			{
				readerA = null;
				readerB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * Run and render the given design file into html file. If the input is
	 * "a.xml", output html file will be named "a.html" under folder "output".
	 * 
	 * @param input
	 * @throws EngineException
	 */

	protected void runAndRender_HTML( String input, String output )
			throws EngineException
	{
		runAndRender_HTML( input, output, null );
	}

	protected final void runAndRender_HTML( String input, String output,
			Map paramValues ) throws EngineException
	{
		String outputFile = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + output; //$NON-NLS-1$
		String inputFile = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + input; //$NON-NLS-1$

		String format = "html"; //$NON-NLS-1$
		String encoding = "UTF-8"; //$NON-NLS-1$

		IReportRunnable runnable = engine.openReportDesign( inputFile );
		IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
		if( paramValues != null )
		{
			Iterator keys = paramValues.keySet( ).iterator( );
			while( keys.hasNext( ) )
			{
				String key = (String)keys.next( );
				task.setParameterValue( key, paramValues.get( key ) );
			}
		}
		
		task.setLocale( Locale.ENGLISH );

		IRenderOption options = new HTMLRenderOption( );
		options.setOutputFileName( outputFile );

		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( "image" ); //$NON-NLS-1$
		HashMap appContext = new HashMap( );
		appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext );
		task.setAppContext( appContext );

		options.setOutputFormat( format );
		options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
				encoding );

		task.setRenderOption( options );
		task.run( );
		task.close( );
	}

	/**
	 * 
	 * @param doc
	 *            input rpt docuement file
	 * @param output
	 *            output file of the generation.
	 * @throws EngineException
	 */

	public void render_HTML( String doc, String output ) throws EngineException
	{
		String outputFile = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + output; //$NON-NLS-1$
		String inputFile = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + doc; //$NON-NLS-1$

		String format = "html"; //$NON-NLS-1$
		String encoding = "UTF-8"; //$NON-NLS-1$

		IReportDocument document = engine.openReportDocument( inputFile );
		IRenderTask task = engine.createRenderTask( document );
		task.setLocale( Locale.ENGLISH );

		IRenderOption options = new HTMLRenderOption( );
		options.setOutputFileName( outputFile );

		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( "image" );
		HashMap appContext = new HashMap( );
		appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext );
		task.setAppContext( appContext );
		options.setOutputFormat( format );
		options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
				encoding );

		task.setRenderOption( options );

		// TODO: changed to task.render when Engine has fix the render().
		task.render( "ALL" ); //$NON-NLS-1$
		task.close( );
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param golden
	 *            the reader for golden file
	 * @param output
	 *            the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception
	 *             if any exception
	 */

	private boolean compareTextFile( Reader golden, Reader output,
			String fileName ) throws Exception
	{
		StringBuffer errorText = new StringBuffer( );

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try
		{
			lineReaderA = new BufferedReader( golden );
			lineReaderB = new BufferedReader( output );

			String strA = lineReaderA.readLine( ).trim( );
			String strB = lineReaderB.readLine( ).trim( );
			while ( strA != null )
			{
				String filterA = this.filterLine( strA );
				String filterB = this.filterLine( strB );

				same = filterA.trim( ).equals( filterB.trim( ) );

				if ( !same )
				{
					StringBuffer message = new StringBuffer( );

					message.append( "line=" ); //$NON-NLS-1$
					message.append( lineNo );
					message.append( "(" ); //$NON-NLS-1$
					message.append( fileName );
					message.append( ")" ); //$NON-NLS-1$
					message.append( " is different:\n" );//$NON-NLS-1$
					message.append( " The line from golden file: " );//$NON-NLS-1$
					message.append( strA );
					message.append( "\n" );//$NON-NLS-1$
					message.append( " The line from result file: " );//$NON-NLS-1$
					message.append( strB );
					message.append( "\n" );//$NON-NLS-1$
					throw new Exception( message.toString( ) );
				}

				strA = lineReaderA.readLine( );
				strB = lineReaderB.readLine( );

				lineNo++;
			}

			same = ( strA == null ) && ( strB == null );
		}
		finally
		{
			try
			{
				lineReaderA.close( );
				lineReaderB.close( );
			}
			catch ( Exception e )
			{
				lineReaderA = null;
				lineReaderB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * Normalize some seeding values, lines that matches certain patterns will
	 * be repalced by a replacement.
	 */

	static String[][] FILTER_PATTERNS = {
			{
					"id[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\"", "id=\"AUTOGENBOOKMARK_000\""}, //$NON-NLS-1$ //$NON-NLS-2$
			{
					"name[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\"", "name=\"AUTOGENBOOKMARK_000\""}, //$NON-NLS-1$//$NON-NLS-2$

			{"iid[\\s]*=[\\s]*\"/.*(.*)\"", "iid=\"000\""}}; //$NON-NLS-1$//$NON-NLS-2$

	/**
	 * Replace the given string with a replacement if it matches a certain
	 * pattern.
	 * 
	 * @param str
	 * @return filtered string, the tokens that matches the patterns are
	 *         replaced with replacement.
	 */

	protected String filterLine( String str )
	{
		String result = str;

		for ( int i = 0; i < FILTER_PATTERNS.length; i++ )
		{
			String regExpr = FILTER_PATTERNS[i][0];
			String replacement = FILTER_PATTERNS[i][1];

			Pattern pattern = Pattern.compile( regExpr );
			Matcher matcher = pattern.matcher( result );
			result = matcher.replaceAll( replacement );
		}

		return result;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path where the test java source file locates.
	 */
	protected String getBaseFolder( )
	{

		String className = getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." );
		className = className.substring( 0, lastDotIndex );
		return PLUGIN_PATH + className.replace( '.', '/' );
	}

}
