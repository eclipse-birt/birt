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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;

/**
 *  
 */
public abstract class JavaxImageIOWriter extends SwingRendererImpl implements
		IIOWriteWarningListener
{

	/**
	 *  
	 */
	private Image _img = null;

	/**
	 *  
	 */
	private Object _oOutputIdentifier = null;

	/**
	 *  
	 */
	private Bounds _bo = null;

	/**
	 *  
	 */
	private transient boolean _bImageExternallySpecified = false;

	/**
	 * 
	 * @return
	 */
	protected abstract String getFormat( );

	/**
	 * 
	 * @return
	 */
	protected abstract int getImageType( );

	/**
	 * 
	 * @param iwp
	 */
	protected void updateWriterParameters( ImageWriteParam iwp )
	{
		// OPTIONALLY IMPLEMENTED BY SUBCLASS
	}

	/**
	 * @return
	 */
	protected String getMimeType( )
	{
		// OPTIONALLY IMPLEMENTED BY SUBCLASS
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#before()
	 */
	public final void before( ) throws ChartException
	{
		super.before( );
		_bImageExternallySpecified = ( _img != null );

		// IF A CACHED IMAGE STRATEGY IS NOT USED, CREATE A NEW INSTANCE
		// EVERYTIME
		if ( !_bImageExternallySpecified )
		{
			if ( _bo == null ) // BOUNDS MUST BE SPECIFIED BEFORE RENDERING
							   // BEGINS
			{
				throw new ChartException( ChartException.RENDERING,
						"exception.no.bounds", //$NON-NLS-1$
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			// CREATE THE IMAGE INSTANCE
			_img = new BufferedImage( (int) _bo.getWidth( ),
					(int) _bo.getHeight( ),
					getImageType( ) );
		}
		super.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, _img.getGraphics( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	public final void after( ) throws ChartException
	{
		super.after( );

		// SEARCH FOR WRITER USING FORMAT
		Iterator it = null;
		String s = getFormat( );
		if ( s != null )
		{
			it = ImageIO.getImageWritersByFormatName( s );
			if ( !it.hasNext( ) )
			{
				it = null; // GET INTO NEXT CONSTRUCT; SEARCH BY MIME TYPE
			}
		}

		// SEARCH FOR WRITER USING MIME TYPE
		if ( it == null )
		{
			s = getMimeType( );
			if ( s == null )
			{
				throw new ChartException( ChartException.RENDERING,
						"exception.no.imagewriter.mimetype.and.format",//$NON-NLS-1$
						new Object[]{
								getMimeType( ),
								getFormat( ),
								getClass( ).getName( )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
			}
			it = ImageIO.getImageWritersByMIMEType( s );
			if ( !it.hasNext( ) )
			{
				throw new ChartException( ChartException.RENDERING,
						"exception.no.imagewriter.mimetype", //$NON-NLS-1$
						new Object[]{
							getMimeType( )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
			}
		}
		final ImageWriter iw = (ImageWriter) it.next( );
		DefaultLoggerImpl.instance( )
				.log( ILogger.INFORMATION,
						Messages.getString( "info.using.imagewriter", getLocale( ) ) + getFormat( ) + iw.getClass( ).getName( ) ); // i18n_CONCATENATIONS_REMOVED
																																   // //$NON-NLS-1$

		// WRITE TO SPECIFIC FILE FORMAT
		final Object o = ( _oOutputIdentifier instanceof String ) ? new File( (String) _oOutputIdentifier )
				: _oOutputIdentifier;
		try
		{
			final ImageOutputStream ios = ImageIO.createImageOutputStream( o );
			updateWriterParameters( iw.getDefaultWriteParam( ) ); // SET ANY
																  // OUTPUT
																  // FORMAT
																  // SPECIFIC
																  // PARAMETERS
																  // IF NEEDED
			iw.setOutput( ios );
			iw.write( (BufferedImage) _img );
			ios.close( );
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartException.RENDERING, ex );
		}

		// FLUSH AND RESTORE STATE OF INTERNALLY CREATED IMAGE
		if ( !_bImageExternallySpecified )
		{
			_img.flush( );
			_img = null;
		}

		// ALWAYS DISPOSE THE GRAPHICS CONTEXT THAT WAS CREATED FROM THE IMAGE
		_g2d.dispose( );
		_g2d = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setProperty( String sProperty, Object oValue )
	{
		super.setProperty( sProperty, oValue );
		if ( sProperty.equals( IDeviceRenderer.EXPECTED_BOUNDS ) )
		{
			_bo = (Bounds) oValue;
		}
		else if ( sProperty.equals( IDeviceRenderer.CACHED_IMAGE ) )
		{
			_img = (Image) oValue;
		}
		else if ( sProperty.equals( IDeviceRenderer.FILE_IDENTIFIER ) )
		{
			_oOutputIdentifier = oValue;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.imageio.event.IIOWriteWarningListener#warningOccurred(javax.imageio.ImageWriter,
	 *      int, java.lang.String)
	 */
	public void warningOccurred( ImageWriter source, int imageIndex,
			String warning )
	{
		DefaultLoggerImpl.instance( ).log( ILogger.WARNING, warning );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#presentException(java.lang.Exception)
	 */
	public void presentException( Exception cexp )
	{
		if ( _bo == null )
		{
			_bo = BoundsImpl.create( 0, 0, 400, 300 );
		}
		String sWrappedException = cexp.getClass( ).getName( );
		while ( cexp.getCause( ) != null )
		{
			cexp = (Exception) cexp.getCause( );
		}
		String sException = cexp.getClass( ).getName( );
		if ( sWrappedException.equals( sException ) )
		{
			sWrappedException = null;
		}
		String sMessage = cexp.getMessage( );
		StackTraceElement[] stea = cexp.getStackTrace( );
		Dimension d = new Dimension( (int) _bo.getWidth( ),
				(int) _bo.getHeight( ) );

		Font fo = new Font( "Monospaced", Font.BOLD, 14 ); //$NON-NLS-1$
		_g2d.setFont( fo );
		FontMetrics fm = _g2d.getFontMetrics( );
		_g2d.setColor( Color.WHITE );
		_g2d.fillRect( 20, 20, d.width - 40, d.height - 40 );
		_g2d.setColor( Color.BLACK );
		_g2d.drawRect( 20, 20, d.width - 40, d.height - 40 );
		_g2d.setClip( 20, 20, d.width - 40, d.height - 40 );
		int x = 25, y = 20 + fm.getHeight( );
		_g2d.drawString( Messages.getString( "exception.caption", getLocale( ) ), x, y ); //$NON-NLS-1$
		x += fm.stringWidth( Messages.getString( "exception.caption", getLocale( ) ) ) + 5; // i18n_CONCATENATIONS_REMOVED
																							// //$NON-NLS-1$
		_g2d.setColor( Color.RED );
		_g2d.drawString( sException, x, y );
		x = 25;
		y += fm.getHeight( );
		if ( sWrappedException != null )
		{
			_g2d.setColor( Color.BLACK );
			_g2d.drawString( Messages.getString( "wrapped.caption", getLocale( ) ), x, y ); //$NON-NLS-1$
			x += fm.stringWidth( Messages.getString( "wrapped.caption", getLocale( ) ) ) + 5; // i18n_CONCATENATIONS_REMOVED
																							  // //$NON-NLS-1$
			_g2d.setColor( Color.RED );
			_g2d.drawString( sWrappedException, x, y );
			x = 25;
			y += fm.getHeight( );
		}
		_g2d.setColor( Color.BLACK );
		y += 10;
		_g2d.drawString( Messages.getString( "message.caption", getLocale( ) ), x, y ); //$NON-NLS-1$
		x += fm.stringWidth( Messages.getString( "message.caption", getLocale( ) ) ) + 5; // i18n_CONCATENATIONS_REMOVED
																						  // //$NON-NLS-1$
		_g2d.setColor( Color.BLUE );
		_g2d.drawString( sMessage, x, y );
		x = 25;
		y += fm.getHeight( );
		_g2d.setColor( Color.BLACK );
		y += 10;
		_g2d.drawString( Messages.getString( "trace.caption", getLocale( ) ), x, y );x = 40;y += fm.getHeight( ); //$NON-NLS-1$
		_g2d.setColor( Color.GREEN.darker( ) );
		for ( int i = 0; i < stea.length; i++ )
		{
			_g2d.drawString( Messages.getString( "trace.detail",//$NON-NLS-1$
					new Object[]{
							stea[i].getClassName( ),
							stea[i].getMethodName( ),
							String.valueOf( stea[i].getLineNumber( ) )
					}, getLocale( ) ), x, y );
			x = 40;
			y += fm.getHeight( ); // i18n_CONCATENATIONS_REMOVED
		}

	}
}