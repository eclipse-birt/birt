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

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>HTMLRenderOption test</b>
 * <p>
 * This case tests methods in HTMLRenderOption API.
 * 
 */
public class HTMLRenderOptionTest extends EngineCase
{

	/**
	 * @param name
	 */
	public HTMLRenderOptionTest( String name )
	{
		super( name );
		// TODO Auto-generated constructor stub
	}

	/**
	 * Test suite()
	 * 
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( HTMLRenderOptionTest.class );
	}

	/**
	 * Test setEmbeddable(boolean embeddable) method Test getEmbeddable() method
	 */
	public void testGetEmbeddable( )
	{
		HTMLRenderOption option = new HTMLRenderOption( );
		boolean bEmbed = true, bEmbedGet;
		option.setEmbeddable( bEmbed );
		bEmbedGet = option.getEmbeddable( );
		assertEquals( "set/getEmbeddable() fail", bEmbed, bEmbedGet );
	}

	/**
	 * Test setUserAgent(java.lang.String userAgent) method Test getUserAgent()
	 * method
	 */
	public void testGetUserAgent( )
	{
		String agent = "agent", agentGet;
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setUserAgent( agent );
		agentGet = option.getUserAgent( );
		assertEquals( "set/getUserAgent() fail", agent, agentGet );
	}
}
