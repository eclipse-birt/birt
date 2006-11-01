
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
import java.util.HashMap;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.ScalarParameterDefn;

/**
 * 
 */

public class ReportEngineTest extends EngineCase
{
	protected HashMap supportedMap = null;
	
	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/ReportEngineTest.rptdesign";
	static final String REPORT_DESIGN = "ReportEngineTest.rptdesign";
	
	public void setUp( )
	{
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		
		initSupportedMap( );
	}
	
	public void tearDown( )
	{
		removeFile( REPORT_DESIGN );
	}
	
	/**
	 * API test on IReportEngine.getConfig( ) method
	 */
	public void testGetConfig( )
	{
		EngineConfig config = new EngineConfig( );
		config.setTempDir( "tempdir" );
		ReportEngine engine = new ReportEngine( config );
		EngineConfig configGet = engine.getConfig( );
		assertEquals( config.getTempDir( ), configGet.getTempDir( ) );
		engine.shutdown( );
	}
	
	/**
	 * API test on IReportEngine.openReportDesign( ) method
	 */
	public void testOpenReportDesign( )
	{
		try
		{
			String rootPath = (new File("")).getAbsolutePath( ).replace( '\\','/' );
			String goldenReportName = "file:/" + rootPath + "/" + REPORT_DESIGN;
			
			ReportEngine engine = new ReportEngine( new EngineConfig());
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			assertTrue( runnable.getReportName( ).equals( goldenReportName ) );
			engine.shutdown( );
		}
		catch( Exception ex )
		{
			ex.printStackTrace( );
			fail();
		}
	}
	
	/**
	 * API test on IReportEngine.createGetParameterDefinitionTask( ) method
	 */
	public void testCreateGetParameterDefinitionTask( )
	{
		try
		{
			ReportEngine engine = new ReportEngine( new EngineConfig());
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IGetParameterDefinitionTask paramDefnTask = engine.createGetParameterDefinitionTask( runnable );
			IParameterDefnBase paramDefn = paramDefnTask.getParameterDefn( "param" );
			assertTrue( paramDefn instanceof ScalarParameterDefn );
			assertTrue( IScalarParameterDefn.TEXT_BOX == paramDefn.getParameterType( ) );
			assertTrue( "defaultValue".equals( paramDefnTask.getDefaultValue( paramDefn ) ));
		}
		catch( Exception ex )
		{
			ex.printStackTrace( );
			fail();
		}
	}
	
	/**
	 * API test on IReportEngine.getSupportedFormats( ) method
	 * test two default supported formats only - "html" and "pdf"
	 */
	public void testGetSupportedFormats( )
	{
		try
		{
			ReportEngine engine = new ReportEngine( new EngineConfig( ) );
			String[] supportedFormats = engine.getSupportedFormats( );
			for ( int length = supportedFormats.length, index = 0; index < length; index++ )
			{
				assertTrue( supportedMap.get( supportedFormats[index] ) != null );
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}
	
	/**
	 * API test on IReportEngine.getMIMEType( String ) method
	 * test two default supported formats only - "html" and "pdf"
	 */
	public void testGetMIMEType( )
	{
		try
		{
			ReportEngine engine = new ReportEngine( new EngineConfig( ) );
			String[] supportedFormats = engine.getSupportedFormats( );
			String result = null;
			String golden = null;
			for ( int length = supportedFormats.length, index = 0; index < length; index++ )
			{
				result = engine.getMIMEType( supportedFormats[index] );
				golden = (String) supportedMap.get( supportedFormats[index] );
				assertTrue( result.equals( golden ) );
			}
			engine.destroy( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}
	
	protected void initSupportedMap( )
	{
		String[] goldenFormats = new String[]{"gen", "html", "pdf", "test"};
		String[] goldenMIMEType = new String[]{"xml", "text/html", "application/pdf", "bugs"};
		assertTrue( goldenFormats.length == goldenMIMEType.length );
		supportedMap = new HashMap( goldenFormats.length );
		for( int size = goldenFormats.length , index = 0 ; index < size ; index ++ )
		{
			supportedMap.put( goldenFormats[index], goldenMIMEType[index] );
		}
	}
}
