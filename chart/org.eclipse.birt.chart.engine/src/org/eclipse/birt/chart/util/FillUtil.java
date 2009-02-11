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
import org.eclipse.emf.common.util.EList;
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
	 * @return darker color
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
			List<Fill> fills = ( (MultipleFill) fill ).getFills( );
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
	 * @return darker color or image
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
	 * @return brighter color
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
			List<Fill> fills = ( (MultipleFill) fill ).getFills( );
			ColorDefinition cd0 = (ColorDefinition) fills.get( 0 );
			ColorDefinition cd1 = (ColorDefinition) fills.get( 1 );
			return getSortedColors( true, cd0, cd1 ).brighter( );
		}
		return null;
	}

	public static Fill changeBrightness( Fill fill, double brightness )
	{
		if ( fill instanceof ColorDefinition )
		{
			ColorDefinition new_fill = ColorDefinitionImpl.copyInstance( (ColorDefinition) fill );
			new_fill.eAdapters( ).addAll( fill.eAdapters( ) );
			applyBrightness( new_fill, brightness );
			return new_fill;
		}
		else
		{
			return fill;
		}
	}

	/**
	 * Returns a brighter fill.
	 * 
	 * @param fill
	 * @return brighter color or image
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
	 * Converts Fill to Gradient if possible, and changes gradient angle
	 * according to chart direction. If Fill is Image type, just does nothing
	 * and returns.
	 * 
	 * @param fill
	 * @param bTransposed
	 * @return Gradient Fill after conversion or original Image Fill
	 */
	public static Fill convertFillToGradient( Fill fill, boolean bTransposed )
	{
		Gradient grad = null;
		if ( fill instanceof ColorDefinition )
		{
			grad = createDefaultGradient( (ColorDefinition) fill );
		}
		else if ( fill instanceof MultipleFill )
		{
			List<Fill> fills = ( (MultipleFill) fill ).getFills( );
			grad = createDefaultGradient( (ColorDefinition) fills.get( 0 ) );
		}
		else if ( fill instanceof Gradient )
		{
			grad = (Gradient) fill;
		}

		if ( grad != null )
		{
			// Change direction if it's transposed
			if ( bTransposed && !grad.isSetDirection( ) )
			{
				grad.setDirection( 90 );
			}
			return grad;
		}
		// Do nothing for Image
		return fill;
	}

	private static void applyBrightness( ColorDefinition cdf, double brightness )
	{
		cdf.set( (int) ( cdf.getRed( ) * brightness ),
				(int) ( cdf.getGreen( ) * brightness ),
				(int) ( cdf.getBlue( ) * brightness ),
				cdf.getTransparency( ) );
	}

	public static Fill convertFillToGradient3D( Fill fill, boolean bTransposed )
	{
		if ( fill instanceof ColorDefinition )
		{
			ColorDefinition color = (ColorDefinition) fill;
			if ( color == null )
			{
				return null;
			}
			Gradient gradient = AttributeFactory.eINSTANCE.createGradient( );
			ColorDefinition newStartColor = (ColorDefinition) changeBrightness( fill,
					0.95 );
			gradient.setStartColor( newStartColor );

			ColorDefinition newColor = (ColorDefinition) changeBrightness( fill,
					0.65 );
			gradient.setEndColor( newColor );

			return gradient;
		}
		else
		{
			return convertFillToGradient( fill, bTransposed );
		}

	}

	/**
	 * Creates Gradient fill by default.
	 * 
	 * @param color
	 *            color to create Gradient
	 * @return default gradient
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
			ColorDefinition newStartColor = (ColorDefinition) EcoreUtil.copy( color );
			newStartColor.eAdapters( ).addAll( color.eAdapters( ) );
			gradient.setStartColor( newStartColor );

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
			ColorDefinition newEndColor = (ColorDefinition) EcoreUtil.copy( color );
			newEndColor.eAdapters( ).addAll( color.eAdapters( ) );
			gradient.setEndColor( newEndColor );

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
		if ( newColor < 0 )
		{
			return 0;
		}
		return newColor < 255 ? newColor : 255;
	}

	/**
	 * The purpose of the Method is to make faster copy of Fill for rendering,
	 * in the moment only copying of ColorDefinition is improved, which is the
	 * most commonest case.
	 * 
	 * @param src
	 * @return fill copy
	 */
	public static Fill copyOf( Fill src )
	{
		if ( src instanceof ColorDefinition )
		{
			return ColorDefinitionImpl.copyInstance( (ColorDefinition) src );
		}
		else
		{
			return (Fill) EcoreUtil.copy( src );
		}
	}

	/**
	 * Returns the fill from palette. If the index is less than the palette
	 * colors size, simply return the fill. If else, first return brighter fill,
	 * then darker fill. The color fetching logic is like this: In the first
	 * round, use the color from palette directly. In the second round, use the
	 * brighter color of respective one in the first round. In the third round,
	 * use the darker color of respective one in the first round. In the forth
	 * round, use the brighter color of respective one in the second round. In
	 * the fifth round, use the darker color of respective one in the third
	 * round. ...
	 * 
	 * @param elPalette
	 * @param index
	 * @since 2.5
	 * @return fill from palette
	 */
	public static Fill getPaletteFill( EList<Fill> elPalette, int index )
	{
		final int iPaletteSize = elPalette.size( );
		Fill fill = elPalette.get( index % iPaletteSize );
		if ( index < iPaletteSize )
		{
			return copyOf( elPalette.get( index % iPaletteSize ) );
		}
		int d = index / iPaletteSize;
		if ( d % 2 == 1 )
		{
			Fill brighterFill = getBrighterFill( fill );
			while ( d / 2 > 0 )
			{
				d -= 2;
				brighterFill = getBrighterFill( brighterFill );
			}
			return brighterFill;
		}
		Fill darkerFill = getDarkerFill( fill );
		while ( ( d - 1 ) / 2 > 0 )
		{
			d -= 2;
			darkerFill = getDarkerFill( darkerFill );
		}
		return darkerFill;
	}

}
