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

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ScalarParameterDefn;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>ReportEngine test</b>
 * <p>
 * This case tests methods in ReportEngine API.
 * 
 */
public class ReportEngineTest extends EngineCase
{

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( "report_engine.rptdesign", "report_engine.rptdesign" );
		copyResource_INPUT ( "parameter.rptdesign" , "parameter.rptdesign" );
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	/**
	 * @param name
	 */
	public ReportEngineTest( String name )
	{
		super( name );
	}

	/**
	 * Test suite
	 * 
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( ReportEngineTest.class );
	}

	/**
	 * Test getConfig() method
	 * 
	 */
	public void testGetConfig( )
	{
		EngineConfig config = new EngineConfig( );
		config.setTempDir( "tempdir" );
		ReportEngine engine = new ReportEngine( config );
		EngineConfig configGet = engine.getConfig( );
		assertEquals( "getConfig() fail", config.getTempDir( ), configGet
				.getTempDir( ) );
	}

	/**
	 * Test openReportDesign(string)
	 * 
	 * @throws EngineException
	 * 
	 */
	public void testOpenReportDesign( ) throws EngineException
	{
		EngineConfig config = new EngineConfig( );
		IReportRunnable reportRunner;
		config.setTempDir( "tempdir" );
		ReportEngine engine = new ReportEngine( config );
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE.getString("CASE_INPUT");
		 * input += System.getProperty("file.separator") ; String
		 * designName=input+"report_engine.rptdesign";
		 */
		String designName = this.genInputFile( "report_engine.rptdesign" );

		try
		{
			reportRunner = engine.openReportDesign( designName );
			designName = "file:" + designName;
			designName = designName.replace( '/', '\\' );
			String reportName = reportRunner.getReportName( ).replace( '/',
					'\\' );
			assertEquals( "openReportDesign(String) fail", designName.substring( designName.indexOf( "org" ), designName.length( ) ),
					reportName.substring( reportName.indexOf( "org" ), reportName.length( ) ) );
			assertNotNull( "openReportDesign(String) fail", reportRunner
					.getImage( "23.gif" ) );
		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
			fail( "openReportDesign(String) fail" );
		}
		engine.destroy( );

	}

	/**
	 * Test openReportDesign(inputStream)
	 * 
	 */
	public void testOpenReportDesign1( )
	{
		EngineConfig config = new EngineConfig( );
		IReportRunnable reportRunner;
		config.setTempDir( "tempdir" );
		ReportEngine engine = new ReportEngine( config );
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE.getString("CASE_INPUT");
		 * input += System.getProperty("file.separator") ; String
		 * designName=input+"report_engine.rptdesign";
		 */

		String designName = this.genInputFile( "report_engine.rptdesign" );

		try
		{
			File file = new File( designName );
			FileInputStream fis = new FileInputStream( file );
			reportRunner = engine.openReportDesign( fis );
			assertEquals( "openReportDesign(InputStream) fail", "<stream>",
					reportRunner.getReportName( ) );
			assertNotNull( "openReportDesign(InputStream) fail", reportRunner
					.getImage( "23.gif" ) );
		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
		}
		catch ( FileNotFoundException fe )
		{
			fe.printStackTrace( );
		}
		engine.destroy( );

	}

	/**
	 * Test createGetParameterDefinitionTask()
	 */
	public void testCreateGetParameterDefinitionTask( )
	{
		EngineConfig config = new EngineConfig( );
		IReportRunnable reportRunner;
		ReportEngine engine = new ReportEngine( config );
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE.getString("CASE_INPUT");
		 * input += System.getProperty("file.separator") ; String
		 * designName=input+"parameter.rptdesign";
		 */
		String designName = this.genInputFile( "parameter.rptdesign" );

		try
		{
			reportRunner = engine.openReportDesign( designName );
			IGetParameterDefinitionTask getParamTask = engine
					.createGetParameterDefinitionTask( reportRunner );
			getParamTask.evaluateDefaults( );
			IParameterDefnBase paramDefn = getParamTask.getParameterDefn( "p1" );
			System.err.println( paramDefn.getTypeName( ) );
			System.err.println( paramDefn instanceof ScalarParameterDefn );
			assertEquals( "creatGetParameterDefinitionTask() fail", "\"abc\"",
					getParamTask.getDefaultValue( paramDefn ) );
		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
		}

	}
	
	public void testGetEmitterInfos( )
	{
		EngineConfig config = new EngineConfig( );
		ReportEngine engine = new ReportEngine( config );
		EmitterInfo[] emitters = engine.getEmitterInfo( );
		boolean found = false;
		for ( int i = 0; i < emitters.length; i++ )
		{
			if ( emitters[i] != null
					&& emitters[i]
							.getID( )
							.equalsIgnoreCase(
									"org.eclipse.birt.report.tests.engine.emitter.html" ) )
			{
				found = true;
				assertEquals( "emitter_html", emitters[i].getFormat( ) );
				assertEquals( "resource/test_emitter.gif", emitters[i]
						.getIcon( ) );
				assertEquals( "text/html", emitters[i].getMimeType( ) );
				assertEquals(
						"org.eclipse.birt.report.tests.engine.emitter.html",
						emitters[i].getNamespace( ) );
				assertEquals( "paper-size-pagination", emitters[i]
						.getPagination( ) );
				break;
			}
		}
		assertTrue( found );
	}
}
