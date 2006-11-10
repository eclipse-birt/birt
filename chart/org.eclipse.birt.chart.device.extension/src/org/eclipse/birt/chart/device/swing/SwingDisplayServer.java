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

package org.eclipse.birt.chart.device.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.device.DisplayAdapter;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;

/**
 * 
 */
public class SwingDisplayServer extends DisplayAdapter
{

	private transient BufferedImage _bi = null;

	private transient Graphics2D _g2d = null;

	private transient SwingImageCache _simc = null;

	/**
	 * dpi resolution
	 */
	private int iDpiResolution = 0;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swing" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @return
	 */
	public SwingDisplayServer( )
	{
		_bi = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB );
		_g2d = (Graphics2D) _bi.getGraphics( );
		_g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		_g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );
		_g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON );

		logger.log( ILogger.INFORMATION,
				Messages.getString( "SwingDisplayServer.info.display.server", //$NON-NLS-1$ 
						new Object[]{
								System.getProperty( "java.vendor" ), System.getProperty( "java.version" )}, //$NON-NLS-1$ //$NON-NLS-2$
						getULocale( ) ) );
		_simc = new SwingImageCache( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#createFont(org.eclipse.birt.chart.attribute.FontDefinition)
	 */
	public final Object createFont( FontDefinition fd )
	{
		final Map m = new HashMap( );
		m.put( TextAttribute.FAMILY, fd.getName( ) );
		m.put( TextAttribute.SIZE, new Float( pointsToPixels( fd.getSize( ) ) ) );
		if ( fd.isItalic( ) )
		{
			m.put( TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE );
		}
		if ( fd.isBold( ) )
		{
			m.put( TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD );
		}
		if ( fd.isUnderline( ) )
		{
			m.put( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON );
		}
		if ( fd.isStrikethrough( ) )
		{
			m.put( TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON );
		}
		return new Font( m );
	}

	/**
	 * Returns a color instance from given color definition
	 */
	public final Object getColor( ColorDefinition cd )
	{
		return new Color( cd.getRed( ),
				cd.getGreen( ),
				cd.getBlue( ),
				cd.getTransparency( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getMetrics(org.eclipse.birt.chart.attribute.FontDefinition,
	 *      java.lang.Object)
	 */
	public final Object getMetrics( FontDefinition fd )
	{
		return _g2d.getFontMetrics( (Font) createFont( fd ) );
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
			if ( GraphicsEnvironment.isHeadless( ) )
			{
				// RETURN OS SPECIFIC DEFAULTS
				iDpiResolution = super.getDpiResolution( );
			}
			else
			{
				iDpiResolution = Toolkit.getDefaultToolkit( )
						.getScreenResolution( );
			}
		}
		return iDpiResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.DisplayAdapter#setDpiResolution(int)
	 */
	public final void setDpiResolution( int dpi )
	{
		iDpiResolution = dpi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#loadImage(java.lang.String)
	 */
	public Object loadImage( URL url ) throws ChartException
	{
		return _simc.loadImage( url );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getSize(java.lang.Object)
	 */
	public final Size getSize( Object oImage )
	{
		final Image img = (Image) oImage;
		final ImageObserver io = (ImageObserver) _simc.getObserver( );
		return SizeImpl.create( img.getWidth( io ), img.getHeight( io ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.devices.IDisplayServer#getObserver()
	 */
	public final Object getObserver( )
	{
		return _simc.getObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDisplayServer#getTextMetrics(org.eclipse.birt.chart.model.component.Label)
	 */
	public ITextMetrics getTextMetrics( Label la )
	{
		return new SwingTextMetrics( this, la );
	}

	/**
	 * Returns the image cache
	 * 
	 * @return
	 */
	final SwingImageCache getImageCache( )
	{
		return _simc;
	}

	final double pointsToPixels( double dPoints )
	{
		return dPoints * getDpiResolution( ) / 72d;
	}
}