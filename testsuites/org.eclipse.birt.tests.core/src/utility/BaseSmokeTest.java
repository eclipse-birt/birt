
package utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

public abstract class BaseSmokeTest extends EngineCase
{

	private Map testStatus = new LinkedHashMap( );

	/**
	 * Working folder that containing the smoke test cases.
	 * 
	 * @return Working folder that containing the 'TestCases' folder containing
	 *         the smoke test collections.
	 */
	protected abstract String getWorkingFolder( );

	/**
	 * @throws Exception
	 * @throws Exception
	 */

	public final void testSmoke( ) throws Exception
	{
		String inputFolder = getWorkingFolder( ) + "/TestCases/input/"; //$NON-NLS-1$
		String outputFolder = getWorkingFolder( ) + "/TestCases/output/"; //$NON-NLS-1$

		File input = new File( inputFolder );
		if ( !input.isDirectory( ) || !input.exists( ) )
		{
			throw new Exception(
					"Input foler: " + inputFolder + " doesn't exist." ); //$NON-NLS-1$//$NON-NLS-2$
		}

		File[] reports = input.listFiles( new FilenameFilter( ) {

			public boolean accept( File dir, String name )
			{
				if ( name.endsWith( ".xml" ) ) //$NON-NLS-1$
					return true;
				return false;
			}
		} );

		for ( int i = 0; i < reports.length; i++ )
		{
			File report = reports[i];
			String html = report.getName( ).replaceAll( ".xml", ".html" ); //$NON-NLS-1$//$NON-NLS-2$

			try
			{
				runAndRender( inputFolder + report.getName( ), outputFolder
						+ html );
				compareHTML( html, html );

				// success

				testStatus.put( report.getName( ), null );
			}
			catch ( Exception e )
			{
				testStatus.put( report.getName( ), e.toString( ) );
			}
		}

		// reporting:

		DomWriter domwriter = new DomWriter( );
		domwriter.setOutput( new FileWriter( this.getBasePath( )
				+ "TESTS-SmokeTests.xml" ) ); //$NON-NLS-1$
		domwriter.setCanonical( true );

		// reporting.
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance( );
		DocumentBuilder builder = builderFactory.newDocumentBuilder( );
		Document doc = builder.newDocument( );

		Element testsuite = doc.createElement( "testsuite" ); //$NON-NLS-1$
		testsuite.setAttribute( "name", getName( ) ); //$NON-NLS-1$

		int failuresCount = 0;

		Iterator iter = this.testStatus.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			String testCaseName = (String) entry.getKey( );
			Object status = entry.getValue( );

			Element testcase = doc.createElement( "testcase" ); //$NON-NLS-1$
			testcase.setAttribute( "name", testCaseName ); //$NON-NLS-1$
			if ( null == status )
				testcase.setAttribute( "errors", null ); //$NON-NLS-1$
			else
			{
				testcase.setAttribute( "errors", status.toString( ) ); //$NON-NLS-1$
				++failuresCount;
			}

			testsuite.appendChild( testcase );
		}

		testsuite.setAttribute( "failures", String.valueOf( failuresCount ) ); //$NON-NLS-1$
		testsuite.setAttribute( "tests", String.valueOf( testStatus //$NON-NLS-1$
				.keySet( )
				.size( ) ) );

		domwriter.write( testsuite );
	}

	/**
	 * Returns base path of the plugin test project.
	 */

	protected String getBasePath( )
	{
		return new File( this
				.getClass( )
				.getProtectionDomain( )
				.getCodeSource( )
				.getLocation( )
				.getPath( ) ).getParent( )
				+ "/"; //$NON-NLS-1$
	}

	private void runAndRender( String inputFile, String outputFile )
			throws EngineException
	{
		IReportRunnable runnable = engine.openReportDesign( inputFile );
		IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );

		task.setLocale( Locale.ENGLISH );

		IRenderOption options = new HTMLRenderOption( );
		options.setOutputFileName( outputFile );
		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( "image" ); //$NON-NLS-1$
		HashMap appContext = new HashMap( );
		appContext.put(
				EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext );
		task.setAppContext( appContext );

		options.setOutputFormat( "html" ); //$NON-NLS-1$
		options
				.getOutputSetting( )
				.put( HTMLRenderOption.URL_ENCODING, "UTF-8" ); //$NON-NLS-1$

		task.setRenderOption( options );
		task.run( );
		task.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utility.EngineCase#compareHTML(java.lang.String, java.lang.String)
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
			golden = getClassFolder( ) + "/TestCases/golden/" + golden; //$NON-NLS-1$
			output = getClassFolder( ) + "/TestCases/output/" + output; //$NON-NLS-1$

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

}
