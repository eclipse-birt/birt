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

package org.eclipse.birt.chart.device.swt;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.chart.device.DisplayAdapter;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public final class SwtDisplayServer extends DisplayAdapter
{

	private Device _d = null;

	private double dScale = 1;

	private int iDpiResolution = 0;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swt" ); //$NON-NLS-1$

	/**
	 * Returns a new instance of an SWT Display Server
	 * 
	 * @param d
	 * @return
	 */
	public SwtDisplayServer( )
	{
		try
		{
			_d = Display.getDefault( );
		}
		catch ( Exception ex )
		{
			logger.log( ex );
			logger.log( ILogger.FATAL,
					Messages.getString( "SwtDisplayServer.exception.display.server", getULocale( ) ) ); //$NON-NLS-1$
		}
		logger.log( ILogger.INFORMATION,
				Messages.getString( "SwtDisplayServer.info.display.server", //$NON-NLS-1$
						new Object[]{
								SWT.getPlatform( ),
								new Integer( SWT.getVersion( ) )
						},
						getULocale( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#createFont(org.eclipse.birt.chart.model.attribute.FontDefinition)
	 */
	public Object createFont( FontDefinition fd )
	{
		int iStyle = 0;
		if ( fd.isBold( ) )
		{
			iStyle |= SWT.BOLD;
		}
		if ( fd.isItalic( ) )
		{
			iStyle |= SWT.ITALIC;
		}
		return new Font( _d, fd.getName( ), (int) Math.round( ( fd.getSize( ) )
				* dScale ), iStyle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getColor(org.eclipse.birt.chart.model.attribute.ColorDefinition)
	 */
	public Object getColor( ColorDefinition cd )
	{
		return new Color( _d, cd.getRed( ), cd.getGreen( ), cd.getBlue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getDpiResolution()
	 */
	public final int getDpiResolution( )
	{
		if ( iDpiResolution == 0 )
		{
			( (Display) _d ).syncExec( new Runnable( ) {

				public void run( )
				{
					iDpiResolution = _d.getDPI( ).x;
				}
			} );
		}
		return iDpiResolution;
	}

	public final void setDpiResolution( int dpi )
	{
		iDpiResolution = dpi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDisplayServer#loadImage(java.net.URL)
	 */
	public Object loadImage( URL url ) throws ChartException
	{
		try
		{
			final InputStream is = url.openStream( );
			final Image img = new Image( _d, is );
			is.close( );
			return img;
			// return new Image(_d, new FileInputStream(sUrl));
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartDeviceSwtActivator.ID,
					ChartException.IMAGE_LOADING,
					ex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getSize(java.lang.Object)
	 */
	public Size getSize( Object oImage )
	{
		final Image img = (Image) oImage;
		final Rectangle r = img.getBounds( );
		return SizeImpl.create( r.width, r.height );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getObserver()
	 */
	public Object getObserver( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getTextMetrics(org.eclipse.birt.chart.model.component.Label)
	 */
	public ITextMetrics getTextMetrics( Label la )
	{
		return new SwtTextMetrics( this, la );
	}

	final Device getDevice( )
	{
		return _d;
	}

	final double pointsToPixels( double dPoints )
	{
		return dPoints * getDpiResolution( ) / 72d;
	}

	final void setScale( double dScale )
	{
		this.dScale = dScale;
	}
}