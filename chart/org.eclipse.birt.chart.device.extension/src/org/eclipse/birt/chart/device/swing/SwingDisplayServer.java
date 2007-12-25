/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.awt.GraphicsDevice;
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

	private int userResolution;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swing" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 */
	public SwingDisplayServer( )
	{
	
		logger.log( ILogger.INFORMATION,
				Messages.getString( "SwingDisplayServer.info.display.server", //$NON-NLS-1$ 
						new Object[]{
								System.getProperty( "java.vendor" ), System.getProperty( "java.version" )}, //$NON-NLS-1$ //$NON-NLS-2$
						getULocale( ) ) );
		_simc = new SwingImageCache( this );
	}
	
	public void dispose( )
	{
		if ( _bi != null )
		{
			// This means we have created our own _g2d, so we need to dispose it
			this._g2d.dispose();
			this._g2d = null;
			this._bi = null;
		}
		super.dispose( );
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
		// Although the fonts is set in points, we need to apply the dpi ratio manually
		// java always assumes 72dpi for fonts, see this link:
		// http://java.sun.com/products/java-media/2D/reference/faqs/index.html#Q_Why_does_eg_a_10_pt_font_in_Ja
		
		m.put( TextAttribute.SIZE, new Float( fd.getSize( ) * getDpiResolution() / 72d) );
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
		return getGraphicsContext().getFontMetrics( (Font) createFont( fd ) );
	}

	protected int computeScreenDpi()
	{
		if ( GraphicsEnvironment.isHeadless( ) )
		{
			// RETURN OS SPECIFIC DEFAULTS
			return super.getDpiResolution( );
		}
		else
		{
			return Toolkit.getDefaultToolkit( )
					.getScreenResolution( );
		}
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
			switch ( getGraphicsContext( ).getDeviceConfiguration( )
					.getDevice( )
					.getType( ) )
			{
				case GraphicsDevice.TYPE_RASTER_SCREEN :
					// This is the only reliable dpi for the display, the one in
					// g2d.getTransform()
					// will be 72 dpi for the display, even when the OS has a
					// different dpi set.
					iDpiResolution = computeScreenDpi();
					break;
				case GraphicsDevice.TYPE_PRINTER :
					// In that case the g2d already contains a transform with the right dpi of the printer
					// so we set the dpi to 72, since there is no adjustment needed
					iDpiResolution = 72;
					break;
				case GraphicsDevice.TYPE_IMAGE_BUFFER :
					if ( userResolution == 0 )
					{
						// Use value set by user, if none, use screen resolution
						iDpiResolution = computeScreenDpi( );
					}
					else
					{
						iDpiResolution = userResolution;
					}
					break;
			}
			// set the fractionalmetrics to ON only for high resolution
			if (iDpiResolution >= 192 )
			{
				_g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
						RenderingHints.VALUE_FRACTIONALMETRICS_ON );
			}
			else
			{
				_g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
						RenderingHints.VALUE_FRACTIONALMETRICS_OFF );
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
		userResolution = dpi;
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
		return new SwingTextMetrics( this, la, getGraphicsContext() );
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


	
	public void setGraphicsContext( Object g2d )
	{
		// User g2d will replace the one instantiated by the display server if any
		if ( g2d != this._g2d && this._bi != null)
		{
			this._g2d.dispose();
			// set image as null to indicate it's an external graphic context.
			this._bi = null;
		}
		this._g2d = (Graphics2D)g2d;
		setAntialiasProperties( _g2d );
	}
	
	// For internal use only
	private Graphics2D getGraphicsContext()
	{
		if ( _g2d  == null )
		{
			// The user _g2d hasn't been set yet.
			// We create our own _g2d here for computations, and it will be disposed later.

			_bi = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB );
			_g2d = (Graphics2D) _bi.getGraphics( );
			
			setAntialiasProperties( _g2d );

		}
		
		return _g2d;
	}
	
	private void setAntialiasProperties( Graphics2D g2d )
	{
		g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON );
	}
	
}