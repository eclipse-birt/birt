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

package org.eclipse.birt.report.engine.emitter.html;

import java.io.File;
import java.util.HashMap;

import org.junit.Ignore;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;

/**
 * Unit test for Class HTMLReportEmitter.
 * 
 */
@Ignore
public class HTMLReportEmitterTest extends HTMLReportEmitterTestCase
{

	/**
	 * Golden file name
	 */
	private String goldenFileName = "html_report_gold.txt";

	public String getWorkSpace( )
	{
		return "./html-reportemitter-test/";
	}

	/**
	 * Test the methods in HTMLReportEmitter
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>startEmitter</li>
	 * <li>startBody</li>
	 * <li>endBody</li>
	 * <li>endEmitter</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */
	public void testEndEmitter( ) throws Exception
	{
		HTMLReportEmitter emitter = new HTMLReportEmitter( );
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( );
		emitterConfig.setActionHandler( new HTMLActionHandler( ) );
		emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		HashMap configs = new HashMap( );
		configs.put( "html", emitterConfig ); //$NON-NLS-1$

		HTMLRenderOption renderOption = new HTMLRenderOption( );
		new File( getWorkSpace( ) ).mkdir( );
		renderOption.setOutputFileName( getWorkSpace( )
				+ "/" + HTMLReportEmitter.REPORT_FILE ); //$NON-NLS-1$

		EngineEmitterServices services = new EngineEmitterServices( null,
				renderOption, configs );

		emitter.initialize( services );
		emitter.start( null );
		emitter.startPage( null );
		emitter.endPage( null );
		emitter.end( null );

		String content = loadReportContent( );
		String golden = loadGoldenContent( goldenFileName );
		assertEquals( golden, content );
	}
}