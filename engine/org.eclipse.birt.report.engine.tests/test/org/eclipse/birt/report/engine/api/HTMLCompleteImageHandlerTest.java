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
	 * API test to test Multi-Types Image
	 */
	public void testMultiTypesImage( )
	{
		String blankURL = "";
		Image image = new Image( blankURL );
		RenderOptionBase option = new RenderOptionBase( );
		image.setRenderOption( option );
		int[] imageTypes = new int[]{Image.DESIGN_IMAGE, Image.REPORTDOC_IMAGE,
				Image.URL_IMAGE, Image.FILE_IMAGE, Image.CUSTOM_IMAGE,
				Image.INVALID_IMAGE};
		HTMLRenderContext context = new HTMLRenderContext( );
		HTMLCompleteImageHandler handler = new HTMLCompleteImageHandler( );
		for ( int size = imageTypes.length, index = 0; index < size; index++ )
		{
			String result = null;
			switch ( imageTypes[index] )
			{
				case Image.DESIGN_IMAGE :
					result = handler.onDesignImage( image, context );
					break;
				case Image.REPORTDOC_IMAGE :
					result = handler.onDocImage( image, context );
					break;
				case Image.URL_IMAGE :
					result = handler.onURLImage( image, context );
					break;
				case Image.FILE_IMAGE :
					result = handler.onFileImage( image, context );
					break;
				case Image.CUSTOM_IMAGE :
					result = handler.onCustomImage( image, context );
					break;
				case Image.INVALID_IMAGE :
					result = "";/* not implement */
					break;
			}
			if ( result != null && result.length( ) > 0 )
			{
				assertTrue( isFileAbsolute( shortAndTrimFilePath( result ) ) );
			}
		}
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
			final String ACTU_IMG_URL = "http://www.actuate.com/images/navimages/v8/logo.gif";
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
		resultPath = shortAndTrimFilePath( resultPath );
		File resultFile = new File( resultPath );
		assertTrue( resultFile.exists( ) );
		assertTrue( resultFile.length( ) > 0 );
	}

	private boolean isFileAbsolute( String path )
	{
		assert ( path != null );
		return ( new File( path ) ).isAbsolute( );
	}

	private String shortAndTrimFilePath( String path )
	{
		final String PATH_PREFIX = "file:/";
		assertTrue( path.startsWith( PATH_PREFIX ) );
		return path.substring( path.indexOf( PATH_PREFIX )
				+ PATH_PREFIX.length( ) );
	}
}
