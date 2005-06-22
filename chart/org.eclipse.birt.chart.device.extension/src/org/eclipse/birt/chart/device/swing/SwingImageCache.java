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

import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;

/**
 *  
 */
public final class SwingImageCache
{

	/**
	 *  
	 */
	private final java.awt.Panel p = new java.awt.Panel( ); // NEEDED FOR IMAGE

	/**
	 *  
	 */
	private final Hashtable htCache;

	/**
	 *  
	 */
	private final IDisplayServer idsSWING;

	/**
	 *  
	 */
	SwingImageCache( IDisplayServer idsSWING )
	{
		this.idsSWING = idsSWING;
		htCache = new Hashtable( );
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws ImageLoadingException
	 */
	final Image loadImage( URL url ) throws ChartException
	{
		Image img = (Image) htCache.get( url );
		if ( img != null )
		{
			DefaultLoggerImpl.instance( ).log( ILogger.INFORMATION,
					Messages.getString( "info.using.swing.cached.image",//$NON-NLS-1$
							new Object[]{
								url
							}, idsSWING.getLocale( ) ) );
		}
		else
		{
			DefaultLoggerImpl.instance( ).log( ILogger.INFORMATION,
					Messages.getString( "info.loading.swing.image",//$NON-NLS-1$
							new Object[]{
								url
							}, idsSWING.getLocale( ) ) );
			img = ( new ImageIcon( url ) ).getImage( );
			try
			{
				final MediaTracker tracker = new MediaTracker( p );
				tracker.addImage( img, 0 );
				tracker.waitForAll( );

				if ( ( tracker.statusAll( true ) & MediaTracker.ERRORED ) != 0 )
				{
					StringBuffer sb = new StringBuffer( );
					Object[] oa = tracker.getErrorsAny( );
					sb.append( '[' );
					for ( int i = 0; i < oa.length; i++ )
					{
						sb.append( oa[i] );
						if ( i < oa.length - 1 )
						{
							sb.append( ", " ); //$NON-NLS-1$
						}
					}
					sb.append( ']' );
					throw new ChartException( ChartException.IMAGE_LOADING,
							"exception.media.tracker", //$NON-NLS-1$
							new Object[]{
								sb.toString( )
							},
							ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
									idsSWING.getLocale( ) ) );
				}
			}
			catch ( InterruptedException ex )
			{
				throw new ChartException( ChartException.IMAGE_LOADING, ex );
			}
			htCache.put( url, img );
		}
		return img;
	}

	/**
	 *  
	 */
	final void flush( )
	{
		if ( htCache.isEmpty( ) )
		{
			return;
		}
		Image img;
		final int n = htCache.size( );
		Enumeration eV = htCache.elements( );
		while ( eV.hasMoreElements( ) )
		{
			img = (Image) eV.nextElement( );
			img.flush( );
		}
		htCache.clear( );
		DefaultLoggerImpl.instance( ).log( ILogger.INFORMATION,
				Messages.getString( "info.flushed.swing.images",//$NON-NLS-1$
						new Object[]{
							new Integer( n )
						}, idsSWING.getLocale( ) ) );
	}

	/**
	 * 
	 * @return
	 */
	final Object getObserver( )
	{
		return p;
	}
}