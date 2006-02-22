/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.util.FileUtil;

/**
 * Default implementation for writing images in a form that is compatible with a
 * web browser's "HTML Complete" save option, i.e., writes images to a
 * predefined folder.
 */
public class HTMLCompleteImageHandler implements IHTMLImageHandler
{

	protected Logger log = Logger.getLogger( HTMLCompleteImageHandler.class
			.getName( ) );

	private static int count = 0;

	private static HashMap map = new HashMap( );

	/**
	 * dummy constructor
	 */
	public HTMLCompleteImageHandler( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onDesignImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onDesignImage( IImage image, Object context )
	{
		return handleImage( image, context, "design", true ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onDocImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onDocImage( IImage image, Object context )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onURLImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onURLImage( IImage image, Object context )
	{
		assert ( image != null );
		return image.getID( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onCustomImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onCustomImage( IImage image, Object context )
	{
		return handleImage( image, context, "custom", false ); //$NON-NLS-1$
	}

	/**
	 * creates a unique tempoary file to store an image
	 * 
	 * @param imageDir
	 *            directory to put image into
	 * @param prefix
	 *            file name prefix
	 * @param postfix
	 *            file name postfix
	 * @return a Java File Object
	 */
	protected File createUniqueFile( String imageDir, String prefix,
			String postfix )
	{
		assert prefix != null;
		if ( postfix == null )
		{
			postfix = "";
		}
		File file = null;
		do
		{
			count++;
			file = new File( imageDir + "/" + prefix + count + postfix ); //$NON-NLS-1$
		} while ( file.exists( ) );

		return new File( imageDir, prefix + count + postfix ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLImageHandler#onFileImage(org.eclipse.birt.report.engine.api2.IImage,
	 *      java.lang.Object)
	 */
	public String onFileImage( IImage image, Object context )
	{
		return handleImage( image, context, "file", true ); //$NON-NLS-1$
	}

	/**
	 * handles an image report item and returns an image URL
	 * 
	 * @param image
	 *            represents the image design information
	 * @param context
	 *            context information
	 * @param prefix
	 *            image prefix in URL
	 * @param needMap
	 *            whether image map is needed
	 * @return URL for the image
	 */
	protected String handleImage( IImage image, Object context, String prefix,
			boolean needMap )
	{
		String mapID = null;
		if ( needMap )
		{
			mapID = getImageMapID( image );
			if ( map.containsKey( mapID ) )
			{
				return (String) map.get( mapID );
			}
		}
		String ret = null;
		boolean returnRelativePath = true;
		if ( context != null && ( context instanceof HTMLRenderContext ) )
		{
			HTMLRenderContext myContext = (HTMLRenderContext) context;
			String imageURL = myContext.getBaseImageURL( );
			String imageDir = myContext.getImageDirectory( );
			String reportName = (String) image.getRenderOption( )
					.getOutputSetting( )
					.get( RenderOptionBase.OUTPUT_FILE_NAME );
			String reportBase = null;
			if ( reportName != null )
			{
				reportBase = new File( new File( reportName ).getAbsolutePath( ) )
						.getParent( );
			}
			else
			{
				reportBase = new File( "." ).getAbsolutePath( );
			}
			String imageAbsoluteDir = null;
			if ( imageDir == null )
			{
				imageAbsoluteDir = reportBase;
				imageURL = null;// return file path
				imageDir = "."; //$NON-NLS-1$
			}
			else
			{
				if ( !FileUtil.isRelativePath( imageDir ) )
				{
					returnRelativePath = false;
					imageAbsoluteDir = imageDir;
				}
				else
				{
					imageAbsoluteDir = reportBase + "/" + imageDir; //$NON-NLS-1$
				}
			}
			String fileName;
			File file;
			synchronized ( HTMLCompleteImageHandler.class )
			{
				file = createUniqueFile( imageAbsoluteDir, prefix, image
						.getExtension( ) );
				fileName = file.getName( );
				try
				{
					image.writeImage( file );
				}
				catch ( IOException e )
				{
					log.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
			if ( imageURL != null )
			{
				ret = imageURL + "/" + fileName; //$NON-NLS-1$
			}
			else
			{
				if ( returnRelativePath )
				{
					ret = imageDir + "/" + fileName; //$NON-NLS-1$
				}
				else
				{
					try
					{
						ret = file.toURL( ).toExternalForm( );
					}
					catch(Exception ex)
					{
						ret = file.getAbsolutePath( );
					}
				}
			}

			if ( needMap )
			{
				map.put( mapID, ret );
			}

		}
		else
		{
			ret = handleTempImage( image, prefix, needMap );
		}
		return ret;
	}

	protected String handleTempImage( IImage image, String prefix, boolean needMap )
	{
		try
		{

			File imageFile = File.createTempFile( prefix, ".img" );
			image.writeImage( imageFile );
			String fileName = imageFile.getAbsolutePath( ); //$NON-NLS-1$
			if ( needMap )
			{
				String mapID = getImageMapID( image );
				map.put( mapID, fileName );
			}
			return fileName;
		}
		catch ( IOException e )
		{
			log.log( Level.SEVERE, e.getMessage( ), e );
		}
		return "unknow.img";
	}

	/**
	 * returns the unique identifier for the image
	 * 
	 * @param image
	 *            the image object
	 * @return the image id
	 */
	protected String getImageMapID( IImage image )
	{
		if ( image.getReportRunnable( ) != null )
			return image.getReportRunnable( ).hashCode( ) + image.getID( );
		return image.getID( );
	}
}