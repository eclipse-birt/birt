/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.ExceptionHandler;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ImageHandleAdapter responds to model ImageHandle
 */
public class ImageHandleAdapter extends DesignElementHandleAdapter
{

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public ImageHandleAdapter( ImageHandle image, IModelAdapterHelper mark )
	{
		super( image, mark );
	}

	/**
	 * Gets the SWT image instance for given Image model
	 * 
	 * @return SWT image instance
	 */
	public Image getImage( )
	{
		String imageSource = getImageHandle( ).getSource( );

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equalsIgnoreCase( imageSource ) )
		{
			EmbeddedImage embeddedImage = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.findImage( getImageHandle( ).getImageName( ) );
			if ( embeddedImage != null )
			{
				return ImageManager.getImage( embeddedImage );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase( imageSource )
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase( imageSource ) )
		{
			String url = getImageHandle( ).getURI( );
			try
			{
				return ImageManager.getImage( new URL( url ) );
			}
			catch ( MalformedURLException e )
			{
				return null;
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase( imageSource ) )
		{
			//TODO: connection database to get the data
		}
		return null;
	}

	private ImageHandle getImageHandle( )
	{
		return (ImageHandle) getHandle( );
	}

	/**
	 * Gets size of image item. If the image size is 0, return null.
	 * 
	 * @return the size of image item.
	 */
	public Dimension getSize( )
	{
		DimensionHandle handle = getImageHandle( ).getWidth( );
		int px = (int) DEUtil.convertoToPixel( handle );

		handle = getImageHandle( ).getHeight( );
		int py = (int) DEUtil.convertoToPixel( handle );

		if ( px != 0 && py != 0 )
		{
			return new Dimension( px, py );
		}
		return null;
	}

	/**
	 * Gets size of image item. Always returns a non-null value.
	 * 
	 * @return
	 */
	public Dimension getRawSize( )
	{
		DimensionHandle handle = getImageHandle( ).getWidth( );
		int px = (int) DEUtil.convertoToPixel( handle );

		handle = getImageHandle( ).getHeight( );
		int py = (int) DEUtil.convertoToPixel( handle );

		return new Dimension( Math.max( px, 0 ), Math.max( py, 0 ) );
	}

	public void setSize( Dimension size )
	{
		String w = size.width + DesignChoiceConstants.UNITS_PX;
		String h = size.height + DesignChoiceConstants.UNITS_PX;

		try
		{
			getImageHandle( ).setWidth( w );
			getImageHandle( ).setHeight( h );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.getInstance( ).handleException( e );
		}
	}

}