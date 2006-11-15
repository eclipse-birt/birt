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

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.model.api.DesignElementHandle;
/**
 * 
 */

public class ReportRunnableTest extends EngineCase
{

	protected IReportEngine engine = null;
	protected IReportRunnable runnable = null;

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/ReportRunnableTest.rptdesign";
	static final String REPORT_DESIGN = "ReportRunnableTest.rptdesign";

	public void setUp( ) throws Exception
	{
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );

		engine = createReportEngine( );
		runnable = engine.openReportDesign( REPORT_DESIGN );
	}

	public void tearDown( )
	{
		// shut down the engine.
		if ( engine != null )
		{
			engine.shutdown( );
		}
		removeFile( REPORT_DESIGN );
	}

	/**
	 * API test on IReportRunnable.getImage( ) method
	 */
	public void testGetImage( )
	{
		final String IMAGE_NAME = "img.jpg";
		try
		{
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IImage img = runnable.getImage( IMAGE_NAME );
			assertTrue( img != null );
			assertEquals( runnable, img.getReportRunnable( ) );
			assertTrue( IMAGE_NAME.equals( img.getID( ) ) );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}
	
	/**
	 * API test on IReportRunnable.setDesignHandle() and IReportRunnable.getDesignHandle()
	 *
	 */
	public void testGetDesignHandle( )
	{
		IReportRunnable runnableA = null;
		DesignElementHandle designHandleA = null;

		try
		{
			runnableA = engine.openReportDesign( REPORT_DESIGN );
			designHandleA = runnableA.getDesignHandle( );
			assertTrue( designHandleA != null );

		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}
}
