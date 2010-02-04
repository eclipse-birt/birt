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

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Manages and register color resource.
 */

public final class ColorPalette
{

	private static ColorPalette instance = null;
	private List colorLib = new ArrayList( );
	private List colorAvailable = new ArrayList( );
	private Color currentColor;
	private HashMap<String, Color> hmColorUsed = new HashMap<String, Color>( );

	private ColorPalette( )
	{
		initColorLibrary( );
		restore( );
	}

	public synchronized static ColorPalette getInstance( )
	{
		if ( instance == null )
		{
			instance = new ColorPalette( );
		}
		return instance;
	}

	private void initColorLibrary( )
	{
		colorLib.add( new RGB( 170, 200, 255 ) );
		colorLib.add( new RGB( 255, 255, 128 ) );
		colorLib.add( new RGB( 128, 255, 128 ) );
		colorLib.add( new RGB( 128, 255, 255 ) );
		colorLib.add( new RGB( 255, 128, 255 ) );
		colorLib.add( new RGB( 255, 128, 64 ) );
		colorLib.add( new RGB( 0, 255, 128 ) );
		colorLib.add( new RGB( 200, 156, 156 ) );
		colorLib.add( new RGB( 128, 128, 255 ) );
		colorLib.add( new RGB( 210, 210, 210 ) );
		colorLib.add( new RGB( 184, 184, 114 ) );
		colorLib.add( new RGB( 128, 128, 128 ) );
	}

	/**
	 * This map stores color name - Color pairs, used to quickly lookup a Color
	 * of a predefined color.
	 * 
	 * @param rgb
	 *            RGB value of color
	 */
	private Color getColor( RGB rgb )
	{
		if ( rgb == null )
		{
			return null;
		}

		String key = rgb.toString( );
		Color color = JFaceResources.getColorRegistry( ).get( key );
		if ( color == null )
		{
			JFaceResources.getColorRegistry( ).put( key, rgb );
			color = JFaceResources.getColorRegistry( ).get( key );
		}
		return color;
	}

	/**
	 * Gets new color from palette.
	 * 
	 * @return new color
	 */
	public Color getNewColor( )
	{
		Color color = getColor( );
		currentColor = color;
		if ( color != null )
		{
			colorAvailable.remove( color.getRGB( ) );
		}
		return color;
	}

	private Color getColor( )
	{
		RGB rgb = colorAvailable.isEmpty( ) ? null
				: (RGB) colorAvailable.get( 0 );
		if ( rgb == null )
		{
			return null;
		}
		return getColor( rgb );
	}

	/**
	 * Returns the color obtained last time
	 * 
	 * @return the color
	 */
	public Color getCurrentColor( )
	{
		if ( currentColor == null )
		{
			currentColor = getColor( );
		}
		return currentColor;
	}

	/**
	 * Registers the expression with a color. Duplicate expression will be
	 * ignored.
	 * 
	 * @param expression
	 *            registered expression
	 */
	public void putColor( String expression )
	{
		if ( expression != null && expression.length( ) > 0 )
		{
			expression = expression.toUpperCase( );
			if ( !hmColorUsed.containsKey( expression ) )
			{
				hmColorUsed.put( expression, getNewColor( ) );
				colorAvailable.remove( getCurrentColor( ).getRGB( ) );
			}
			else
			{
				// Set binding color be the current
				currentColor = getColor( expression );
			}
		}
	}

	public void retrieveColor( String expression )
	{
		if ( expression != null && expression.length( ) > 0 )
		{
			expression = expression.toUpperCase( );
			if ( hmColorUsed.containsKey( expression ) )
			{
				Color oldColor = (Color) hmColorUsed.get( expression );
				colorAvailable.add( oldColor.getRGB( ) );
				hmColorUsed.remove( expression );
			}
		}
	}

	/**
	 * Fetches the color by registered expression
	 * 
	 * @param expression
	 *            registered expression
	 * @return the registered color or null if not found
	 */
	public Color getColor( String expression )
	{
		if ( expression != null && expression.length( ) > 0 )
		{
			expression = expression.toUpperCase( );
			return (Color) hmColorUsed.get( expression );
		}
		return null;
	}

	/**
	 * Restores the current to the initial state.
	 * 
	 */
	public void restore( )
	{
		colorAvailable.clear( );
		colorAvailable.addAll( colorLib );
		currentColor = null;
		hmColorUsed.clear( );
	}

	public void updateKeys(Collection<String> keys)
	{
		Set<String> newKeys = new HashSet<String>( );
		for (String key:keys)
		{
			newKeys.add( key.toUpperCase( ) );
		}
		
		Set<String> oldKeys = hmColorUsed.keySet( );

		Set<String> keysToRemove = new HashSet<String>( oldKeys );
		keysToRemove.removeAll( newKeys );

		for ( String key : keysToRemove )
		{
			retrieveColor( key );
		}
		
		Set<String> keysToAdd = newKeys;
		keysToAdd.removeAll( oldKeys );

		for ( String key : keysToAdd )
		{
			putColor( key );
		}
	}

}