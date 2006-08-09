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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.DefaultStatusHandler;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>EngineConfig test</b>
 * <p>
 * This case tests public methods in EngineConfig API.
 * 
 */

public class EngineConfigTest extends EngineCase
{

	public static Test suite( )
	{

		return new TestSuite( EngineConfigTest.class );
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public EngineConfigTest( String name )
	{
		super( name );

	}

	/**
	 * Store a EngineConfig instance
	 */
	protected EngineConfig engineConfig = new EngineConfig( );

	/**
	 * Test setEmitterConfiguration()/getEmitterConfiguration()
	 */
	public void testGetEmitterConfigs( )
	{
		HTMLEmitterConfig eConfig = new HTMLEmitterConfig( );
		engineConfig.setEmitterConfiguration( "pdf", eConfig );
		HTMLEmitterConfig eConfigNew = (HTMLEmitterConfig) engineConfig
				.getEmitterConfigs( ).get( "pdf" );
		assertEquals( "Not identical", eConfig, eConfigNew );

	}

	/**
	 * Test addScriptableJavaObject(java.lang.String jsName, java.lang.Object
	 * obj) method
	 * 
	 */
	public void testGetScriptObjects( )
	{
		engineConfig.addScriptableJavaObject( "jo1", new String( "jostring" ) );
		assertEquals( "Not identical", engineConfig.getScriptObjects( ).get(
				"jo1" ), "jostring" );

	}

	/**
	 * test setConfigurationVariable(java.lang.String name, java.lang.String
	 * value) method
	 * 
	 */
	public void testGetConfigMap( )
	{
		engineConfig.setConfigurationVariable( "config_var1", "config_value" );
		assertEquals( engineConfig.getConfigMap( ).get( "config_var1" ),
				"config_value" );
	}

	/**
	 * Test GetStatusHandler() method
	 * 
	 */
	public void testGetStatusHandler( )
	{
		assertNotNull( engineConfig.getStatusHandler( ) );
		DefaultStatusHandler handler = new DefaultStatusHandler( );
		engineConfig.setStatusHandler( handler );
		DefaultStatusHandler handlerNew = (DefaultStatusHandler) engineConfig
				.getStatusHandler( );
		assertEquals( "Not identical", handler, handlerNew );
	}

	/**
	 * Test GetTmpDir() method
	 * 
	 */
	public void testGetTmpDir( )
	{
		engineConfig.setTempDir( "temp directory" );
		assertEquals( "Not identical", "temp directory", engineConfig
				.getTempDir( ) );
	}

	/**
	 * Test SetEngineHome(java.lang.String birtHome) method
	 * 
	 */
	public void testSetEngineHome( )
	{
		engine.getConfig( ).setEngineHome( "c:/" );

		assertEquals( "Not identical", "c:/", engine.getConfig( ).getBIRTHome( ) );
	}

	/**
	 * Test setLogConfig(java.lang.String directoryName, java.util.logging.Level
	 * level) method
	 * 
	 */
	public void testSetLogConfig( )
	{
		/*
		 * fail("notfinishedyet");
		 * engineConfig.setLogConfig("directory",Level.ALL); assertEquals("Not
		 * identical",engineConfig.getConfigMap().get("LOG_DESTINATION"),"directory");
		 */
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
}
