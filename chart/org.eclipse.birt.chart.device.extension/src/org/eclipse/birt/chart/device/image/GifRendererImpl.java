/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;

/**
 * GifRendererImpl
 */
public final class GifRendererImpl extends JavaxImageIOWriter
{

	private static ILogger logger = Logger
			.getLogger( "org.eclipse.birt.chart.device.extension/image" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	protected final String getFormat( )
	{
		return "gif"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	public final String getMimeType( )
	{
		return "image/gif"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	protected final int getImageType( )
	{
		return BufferedImage.TYPE_INT_ARGB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	public final void after( ) throws ChartException
	{
		if ( isSupportedByJavaxImageIO() )
		{
			super.after();
		}
		else
		{
			logger.log( ILogger.INFORMATION, Messages.getString(
					"info.use.custom.image.writer", //$NON-NLS-1$
					new Object[]
					{ getFormat(), GifWriter.class.getName() }, getULocale() ) );

			// If not supported by JavaxImageIO, use our own.
			GifWriter gw = null;

			if ( _oOutputIdentifier instanceof OutputStream )
			{
				gw = new GifWriter( (OutputStream) _oOutputIdentifier );
				try
				{
					
					gw.write( _img, GifWriter.ORIGINAL_COLOR);
				}
				catch ( Exception ex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING, ex );
				}
			}
			else if ( _oOutputIdentifier instanceof String )
			{
				FileOutputStream fos = null;
				try
				{
					fos = new FileOutputStream( (String) _oOutputIdentifier );
					gw = new GifWriter( fos );
					gw.write( _img, GifWriter.ORIGINAL_COLOR);
					fos.close();
				}
				catch ( Exception ex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING, ex );
				}
			}
			else
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.unable.write.gif.identifier", //$NON-NLS-1$
						new Object[]
						{ _oOutputIdentifier }, ResourceBundle.getBundle(
								Messages.DEVICE_EXTENSION, getLocale() ) );
			}
		}
	}

}
