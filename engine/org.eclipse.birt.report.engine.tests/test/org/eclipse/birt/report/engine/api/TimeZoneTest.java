/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.birt.report.engine.EngineCase;

import com.ibm.icu.util.TimeZone;

public class TimeZoneTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/timeZoneTest.xml";
	static final String TEMP_RESULT = "tempResult.html";

	static final String GOLDENSTRING = "Nov 1, 2007, 9:38 AM";

	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void testRunAndRenderTask( )
	{
		try
		{
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			IRunAndRenderTask task = engine.createRunAndRenderTask( report );
			task.setLocale( com.ibm.icu.util.ULocale.US );
			task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
			IRenderOption option = new HTMLRenderOption( );
			option.setOutputFormat( "html" ); //$NON-NLS-1$
			option.setOutputFileName( TEMP_RESULT );
			// set the render options
			task.setRenderOption( option );
			task.run( );

			assertTrue( stringExist( TEMP_RESULT, GOLDENSTRING ) );
		}
		catch ( Exception ex )
		{
			assert false;
		}
	}

	public void testRenderTask( ) throws Exception
	{
		createReportDocument( );
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		IRenderTask task = engine.createRenderTask( reportDoc );
		task.setLocale( com.ibm.icu.util.ULocale.US );
		task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" ); //$NON-NLS-1$
		option.setOutputFileName( TEMP_RESULT );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );
		reportDoc.close( );
		assertTrue( stringExist( TEMP_RESULT, GOLDENSTRING ) );
	}

	private boolean stringExist( String src, String golden )
	{
		boolean result = false;
		try
		{
			InputStream srcInputStream = new FileInputStream( new File( src ) );
			assert ( srcInputStream != null );
			StringBuffer srcBuffer = new StringBuffer( );
			byte[] buffer = new byte[5120];
			int readCount = -1;
			while ( ( readCount = srcInputStream.read( buffer ) ) != -1 )
			{
				srcBuffer.append( new String( buffer, 0, readCount ) );
			}

			result = srcBuffer.toString( ).indexOf( golden ) != -1;
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			return false;
		}
		return result;
	}
}
