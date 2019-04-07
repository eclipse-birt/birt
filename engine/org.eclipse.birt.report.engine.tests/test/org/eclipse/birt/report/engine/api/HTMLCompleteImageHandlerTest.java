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

import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.model.api.IResourceLocator;

/**
 * 
 */

public class HTMLCompleteImageHandlerTest extends EngineCase
{

	protected IReportEngine engine = null;
	protected IReportRunnable runnable = null;

	protected static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/HTMLCompleteImageHandlerTest.rptdesign";
	protected static final String REPORT_DESIGN = "HTMLCompleteImageHandlerTest.rptdesign";

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
	 * API test on HTMLCompleteImageHandler.onDocImage( ) method. This method is
	 * not implemented so far, so the default return value is *null*
	 */
	public void testOnDocImage( )
	{
		HTMLCompleteImageHandler handler = new HTMLCompleteImageHandler( );
		String result = handler.onDocImage( null, null );
		assertNull( result );
	}

	/**
	 * API test on HTMLCompleteImageHandler.onURLImage( ) method. This test get
	 * a connection for the web specified by the URL
	 */
	public void testOnURLImage( )
	{
		try
		{
			final String ACTU_IMG_URL = "https://mail.google.com/mail/help/images/logo1.gif";
			HTMLRenderContext context = new HTMLRenderContext( );
			context.setImageDirectory( "" );
			Image image = new Image( ACTU_IMG_URL );
			HTMLCompleteImageHandler handler = new HTMLCompleteImageHandler( );
			String urlString = handler.onURLImage( image, context );

			URL url = runnable.getDesignHandle( ).getModule( ).findResource(
					urlString, IResourceLocator.IMAGE );
			InputStream inputStream = url.openConnection( ).getInputStream( );
			int availableBytes = inputStream.available( );
			assert ( availableBytes > 0 );
		}
		catch ( java.net.ConnectException ce )
		{
			ce.printStackTrace( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	public void testOnFileImage( )
	{
		// todo
	}

	public void testoOnCustomImage( )
	{
		// todo
	}

	/**
	 * API test on HTMLCompleteImageHandler.onDesignImage( ) method
	 */
	public void testOnDesignImage( )
	{
		HTMLRenderContext context = new HTMLRenderContext( );
		context.setImageDirectory( "" );
		Image image = (Image) runnable.getImage( "img.jpg" );
		RenderOptionBase option = new RenderOptionBase( );
		image.setRenderOption( option );
		HTMLCompleteImageHandler imageHandler = new HTMLCompleteImageHandler( );
		String resultPath = imageHandler.onDesignImage( image, context );
		assertTrue( isFilePathLegal( resultPath ) );
	}
	
	private boolean isFilePathLegal( String filePath )
	{
		try
		{
			URL fileURL = new URL( filePath );
			return fileURL.openStream( ) != null;
		}
		catch ( Exception ex )
		{
			// DO NOTHING
		}
		return false;
	}
}
