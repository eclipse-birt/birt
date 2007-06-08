/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.util;

import java.util.List;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Utility class for Fill conversion.
 */

public class FillUtil
{

	/**
	 * Returns a darker color.
	 * 
	 * @param fill
	 * @return
	 */
	public static ColorDefinition getDarkerColor( Fill fill )
	{
		if ( fill instanceof ColorDefinition )
		{
			return ( (ColorDefinition) fill ).darker( );
		}
		if ( fill instanceof Gradient )
		{
			ColorDefinition cdStart = ( (Gradient) fill ).getStartColor( );
			ColorDefinition cdEnd = ( (Gradient) fill ).getEndColor( );
			return getSortedColors( false, cdStart, cdEnd ).darker( );
		}
		if ( fill instanceof Image )
		{
			// Gray color
			return ColorDefinitionImpl.create( 128, 128, 128 );
		}
		if ( fill instanceof MultipleFill )
		{
			List fills = ( (MultipleFill) fill ).getFills( );
			ColorDefinition cd0 = (ColorDefinition) fills.get( 0 );
			ColorDefinition cd1 = (ColorDefinition) fills.get( 1 );
			return getSortedColors( false, cd0, cd1 ).darker( );
		}
		return null;
	}

	/**
	 * Returns a darker fill.
	 * 
	 * @param fill
	 * @return
	 */
	public static Fill getDarkerFill( Fill fill )
	{
		if ( fill instanceof Image )
		{
			return fill;
		}
		return getDarkerColor( fill );
	}

	/**
	 * Returns a brighter color.
	 * 
	 * @param fill
	 * @return
	 */
	public static ColorDefinition getBrighterColor( Fill fill )
	{
		if ( fill instanceof ColorDefinition )
		{
			return ( (ColorDefinition) fill ).brighter( );
		}
		if ( fill instanceof Gradient )
		{
			ColorDefinition cdStart = ( (Gradient) fill ).getStartColor( );
			ColorDefinition cdEnd = ( (Gradient) fill ).getEndColor( );
			return getSortedColors( true, cdStart, cdEnd ).brighter( );
		}
		if ( fill instanceof Image )
		{
			// Gray color
			return ColorDefinitionImpl.create( 192, 192, 192 );
		}
		if ( fill instanceof MultipleFill )
		{
			List fills = ( (MultipleFill) fill ).getFills( );
			ColorDefinition cd0 = (ColorDefinition) fills.get( 0 );
			ColorDefinition cd1 = (ColorDefinition) fills.get( 1 );
			return getSortedColors( true, cd0, cd1 ).brighter( );
		}
		return null;
	}

	/**
	 * Returns a brighter fill.
	 * 
	 * @param fill
	 * @return
	 */
	public static Fill getBrighterFill( Fill fill )
	{
		if ( fill instanceof Image )
		{
			return fill;
		}
		return getBrighterColor( fill );
	}

	static ColorDefinition getSortedColors( boolean bBrighter,
			ColorDefinition cd1, ColorDefinition cd2 )
	{
		int result = ( cd1.getRed( ) + cd1.getGreen( ) + cd1.getBlue( ) )
				- ( ( cd2.getRed( ) + cd2.getGreen( ) + cd2.getBlue( ) ) );
		if ( bBrighter )
		{
			return result > 0 ? cd1 : cd2;
		}
		return result > 0 ? cd2 : cd1;
	}

	/**
	 * Converts Fill to Gradient if possible. If Fill is Gradient or Image type,
	 * just does nothing and returns.
	 * 
	 * @param fill
	 * @return Gradient Fill after conversion or original Image Fill
	 */
	public static Fill convertFillToGradient( Fill fill )
	{
		if ( fill instanceof ColorDefinition )
		{
			return createDefaultGradient( (ColorDefinition) fill );
		}
		if ( fill instanceof MultipleFill )
		{
			List fills = ( (MultipleFill) fill ).getFills( );
			return createDefaultGradient( (ColorDefinition) fills.get( 0 ) );
		}
		// Do nothing for Gradient of Image
		return fill;
	}

	/**
	 * Creates Gradient fill by default.
	 * 
	 * @param color
	 *            color to create Gradient
	 * @return
	 */
	public static Gradient createDefaultGradient( ColorDefinition color )
	{
		if ( color == null )
		{
			return null;
		}
		Gradient gradient = AttributeFactory.eINSTANCE.createGradient( );
		int currentLuminance = convertRGBToLuminance( color.getRed( ),
				color.getGreen( ),
				color.getBlue( ) );
		if ( currentLuminance < 200 )
		{
			gradient.setStartColor( color );
			ColorDefinition newColor = (ColorDefinition) EcoreUtil.copy( color );
			newColor.eAdapters( ).addAll( color.eAdapters( ) );

			int lumDiff = 240 - currentLuminance;
			newColor.setRed( getNewColor( lumDiff, newColor.getRed( ), 0.3 ) );
			newColor.setGreen( getNewColor( lumDiff, newColor.getGreen( ), 0.59 ) );
			newColor.setBlue( getNewColor( lumDiff, newColor.getBlue( ), 0.11 ) );
			gradient.setEndColor( newColor );
		}
		else
		{
			gradient.setEndColor( color );
			ColorDefinition newColor = (ColorDefinition) EcoreUtil.copy( color );
			newColor.eAdapters( ).addAll( color.eAdapters( ) );

			int lumDiff = -100;
			newColor.setRed( getNewColor( lumDiff, newColor.getRed( ), 0.3 ) );
			newColor.setGreen( getNewColor( lumDiff, newColor.getGreen( ), 0.59 ) );
			newColor.setBlue( getNewColor( lumDiff, newColor.getBlue( ), 0.11 ) );
			gradient.setStartColor( newColor );
		}
		return gradient;
	}

	private static int convertRGBToLuminance( int red, int green, int blue )
	{
		return (int) ( 0.3 * red + 0.59 * green + 0.11 * blue );
	}

	private static int getNewColor( int lumDiff, int oldColor,
			double coefficient )
	{
		int newColor = (int) ( lumDiff * coefficient ) + oldColor;
		return newColor < 255 ? newColor : 255;
	}
}
