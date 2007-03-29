
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
import java.util.Iterator;

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
			String rootPath = new File("").toURL( ).toString( );
			String goldenReportName = rootPath + "/" + REPORT_DESIGN;
			
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
			
			Iterator it = supportedMap.keySet( ).iterator( );
			while ( it.hasNext( ) )
			{
				String key = (String) it.next( );
				boolean flag = true;
				for ( int index = 0; index < supportedFormats.length; index++ )
				{
					if ( supportedFormats[index].equals( key ) )
					{
						flag = false;
					}
				}
				assertFalse( flag );
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}
	
	/**
	 * API test on IReportEngine.getEmitterInfo( ) method
	 */
	public void testGetEmitterInfo( )
	{
		try
		{
			ReportEngine engine = new ReportEngine( new EngineConfig( ) );
			EmitterInfo[] infos = engine.getEmitterInfo( );
			for ( int index = 0, size = infos.length; index < size; index++ )
			{
				assertTrue( infos[index].getEmitter( ) != null );
				assertTrue( infos[index].getFormat( ) != null
						&& infos[index].getFormat( ).length( ) > 0 );
				/* icon may be null, so donot be test */
				assertTrue( infos[index].getID( ) != null
						&& infos[index].getID( ).length( ) > 0 );
				assertTrue( infos[index].getMimeType( ) != null
						&& infos[index].getMimeType( ).length( ) > 0 );
				assertTrue( infos[index].getNamespace( ) != null
						&& infos[index].getNamespace( ).length( ) > 0 );
				/*
				 * icon and pagination are not use-required
				 * so do not test it
				 */
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
			
			Iterator it = supportedMap.entrySet( ).iterator( );
			while ( it.hasNext( ) )
			{
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next( );
				String value = (String) entry.getValue( );
				boolean flag = true;
				for ( int index = 0; index < supportedFormats.length; index++ )
				{
					if ( engine.getMIMEType( supportedFormats[index] )
							.equals( value ) )
					{
						flag = false;
					}
				}
				assertFalse( flag );
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
		String[] goldenFormats = new String[]{"html", "pdf"};
		String[] goldenMIMEType = new String[]{"text/html", "application/pdf"};
		assertTrue( goldenFormats.length == goldenMIMEType.length );
		supportedMap = new HashMap( );
		for( int size = goldenFormats.length , index = 0 ; index < size ; index ++ )
		{
			supportedMap.put( goldenFormats[index], goldenMIMEType[index] );
		}
	}
}
