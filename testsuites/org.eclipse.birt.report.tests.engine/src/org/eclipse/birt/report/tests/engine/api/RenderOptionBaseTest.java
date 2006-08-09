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

import java.io.ByteArrayOutputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>RenderOptionBase test</b>
 * <p>
 * This case tests methods in RenderOptionBase API.
 * 
 */
public class RenderOptionBaseTest extends EngineCase
{

	private TestRenderOptionBase optionBase = new TestRenderOptionBase( );

	/**
	 * @param name
	 */
	public RenderOptionBaseTest( String name )
	{
		super( name );
	}

	/**
	 * Test suite()
	 * 
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( RenderOptionBaseTest.class );
	}

	/**
	 * Test setOption(java.lang.String name, java.lang.Object value) method Test
	 * getOption() method
	 */
	public void testGetOption( )
	{
		String name = "newoption";
		Object value = new String( "option1" );
		optionBase.setOption( name, value );
		assertEquals( "set/getOption() fail", optionBase.getOption( name ),
				value );
	}

	/**
	 * Test setOutputFormat(java.lang.String format) method Test
	 * getOutputFormat() method
	 */
	public void testGetOutputFormat( )
	{
		String format = "html", formatGet = "";
		optionBase.setOutputFormat( format );
		formatGet = optionBase.getOutputFormat( );
		assertEquals( "set/getOutputFormat() fail", format, formatGet );
	}

	/**
	 * Test setOutputFileName(java.lang.String outputFileName) method
	 * 
	 */
	public void testSetOutputFileName( )
	{
		String name = "ofName", nameGet = "";
		optionBase.setOutputFileName( name );
		nameGet = (String) optionBase
				.getOption( TestRenderOptionBase.OUTPUT_FILE_NAME );
		assertEquals( "setOutputFileName() fail", name, nameGet );
	}

	/**
	 * Test setOutputStream(java.io.OutputStream ostream) method
	 * 
	 */
	public void testOutputStream( )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream( );
		optionBase.setOutputStream( bos );
		ByteArrayOutputStream bosGet = (ByteArrayOutputStream) optionBase
				.getOutputSetting( ).get( TestRenderOptionBase.OUTPUT_STREAM );
		assertEquals( "setOutputStream(java.io.OutputStream ostream) fail",
				bos, bosGet );

	}
}
