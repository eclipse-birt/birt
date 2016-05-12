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

package org.eclipse.birt.report.model.api.util;

import javax.print.attribute.ResolutionSyntax;

import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Utility class to do conversions between units.
 * 
 */
public class DimensionUtil
{

	private final static String ILLEGAL_UNIT = "must be one of the absolute units(CM, IN, MM, PT, PC)."; //$NON-NLS-1$

	/**
	 * Conversion factor from inches to cm.
	 */

	private static final double CM_PER_INCH = 2.54;

	/**
	 * Conversion factor from inches to points.
	 */

	private static final double POINTS_PER_INCH = 72;

	/**
	 * Conversion factor from cm to points.
	 */

	private static final double POINTS_PER_CM = POINTS_PER_INCH / CM_PER_INCH;

	/**
	 * Conversion factor from picas to points.
	 */

	private static final double POINTS_PER_PICA = 12;

	/**
	 * The default DPI value.
	 */
	private static final int DEFAULT_DPI = 96;

	/**
	 * Convert a measure from one units to another. The conversion is between
	 * absolute the units should be one of the absolute units(CM, IN, MM, PT,
	 * PC).
	 * 
	 * @param measure
	 *            the numeric measure of the dimension.
	 * @param fromUnits
	 *            unit of the measure, it must be one of the absolute unit.
	 * @param targetUnits
	 *            the desired units, it must be one of the absolute unit.
	 * 
	 * @return <code>DimensionValue</code> in the target unit.
	 */
	public static DimensionValue convertTo( double measure, String fromUnits,
			String targetUnits )
	{

		if ( targetUnits.equalsIgnoreCase( fromUnits ) )
			return new DimensionValue( measure, fromUnits );

		double targetMeasure = 0.0;

		if ( DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( targetUnits ) )
		{
			if ( DesignChoiceConstants.UNITS_CM.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / CM_PER_INCH;
			else if ( DesignChoiceConstants.UNITS_MM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / CM_PER_INCH / 10;
			else if ( DesignChoiceConstants.UNITS_PT
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / POINTS_PER_INCH;
			else if ( DesignChoiceConstants.UNITS_PC
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_PICA / POINTS_PER_INCH;
			else
				throw new IllegalArgumentException(
						"\"fromUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.UNITS_CM.equalsIgnoreCase( targetUnits ) )
		{
			if ( DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * CM_PER_INCH;
			else if ( DesignChoiceConstants.UNITS_MM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / 10;
			else if ( DesignChoiceConstants.UNITS_PT
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / POINTS_PER_CM;
			else if ( DesignChoiceConstants.UNITS_PC
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_PICA / POINTS_PER_CM;
			else
				throw new IllegalArgumentException(
						"\"fromUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.UNITS_MM.equalsIgnoreCase( targetUnits ) )
		{
			if ( DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * CM_PER_INCH * 10;
			else if ( DesignChoiceConstants.UNITS_CM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * 10;
			else if ( DesignChoiceConstants.UNITS_PT
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * 10 / POINTS_PER_CM;
			else if ( DesignChoiceConstants.UNITS_PC
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_PICA * 10 / POINTS_PER_CM;
			else
				throw new IllegalArgumentException(
						"\"fromUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.UNITS_PT.equalsIgnoreCase( targetUnits ) )
		{
			if ( DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_INCH;
			else if ( DesignChoiceConstants.UNITS_CM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_CM;
			else if ( DesignChoiceConstants.UNITS_MM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_CM / 10;
			else if ( DesignChoiceConstants.UNITS_PC
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_PICA;
			else if ( DesignChoiceConstants.UNITS_PX
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_INCH / DEFAULT_DPI;
			else
				throw new IllegalArgumentException(
						"\"fromUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( targetUnits ) )
		{
			if ( DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_INCH / POINTS_PER_PICA;
			else if ( DesignChoiceConstants.UNITS_CM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_CM / POINTS_PER_PICA;
			else if ( DesignChoiceConstants.UNITS_MM
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_CM / 10 / POINTS_PER_PICA;
			else if ( DesignChoiceConstants.UNITS_PT
					.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / POINTS_PER_PICA;
			else
				throw new IllegalArgumentException(
						"\"fromUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$
		}
		else
			throw new IllegalArgumentException(
					"\"targetUnits\"" + ILLEGAL_UNIT ); //$NON-NLS-1$

		return new DimensionValue( targetMeasure, targetUnits );
	}

	/**
	 * Convert a <code>DimensionValue</code> from one units to another, The
	 * conversion is between absolute the units should be one of the absolute
	 * units(CM, IN, MM, PT, PC).
	 * 
	 * @param dimension
	 *            the numeric measure of the dimension.
	 * @param appUnit
	 *            the application unit of the dimension, if the dimension has
	 *            not specified a unit, the the application unit will be applied
	 *            to it. It must be one of the absolute unit.
	 * @param targetUnits
	 *            the desired unit.
	 * @return <code>DimensionValue</code> in the target unit.
	 */

	public static DimensionValue convertTo( DimensionValue dimension,
			String appUnit, String targetUnits )
	{
		String fromUnit = dimension.getUnits( );
		if ( DimensionValue.DEFAULT_UNIT.equalsIgnoreCase( fromUnit ) )
			fromUnit = appUnit;

		return convertTo( dimension.getMeasure( ), fromUnit, targetUnits );
	}

	/**
	 * Convert a dimension from one units to another, the dimension like "12pt,
	 * 12cm" is composed of two parts: "measure" and "units". The conversion is
	 * between absolute the units should be one of the absolute units(CM, IN,
	 * MM, PT, PC).
	 * 
	 * @param dimension
	 *            a string representing a absolute dimension value like "12pt,
	 *            12pc...".
	 * @param appUnit
	 *            the application unit of the dimension, if the dimension has
	 *            not specified a unit, the the application unit will be applied
	 *            to it. It must be one of the absolute unit.
	 * @param targetUnits
	 *            the desired unit.
	 * @return <code>DimensionValue</code> in the target unit.
	 * @throws PropertyValueException
	 *             if the dimension is not valid.
	 */

	public static DimensionValue convertTo( String dimension, String appUnit,
			String targetUnits ) throws PropertyValueException

	{
		DimensionValue dim = DimensionValue.parse( dimension );
		if ( dim != null )
		{
			return convertTo( dim, appUnit, targetUnits );
		}
		throw new PropertyValueException( dimension,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				IPropertyType.DIMENSION_TYPE );
	}

	/**
	 * Return if the given unit is an absolute unit or not. The following units
	 * defined in <code>DesignChoiceConstants</code> are considered as absolute:
	 * <ul>
	 * <li>UNITS_IN
	 * <li>UNITS_CM
	 * <li>UNITS_MM
	 * <li>UNITS_PT
	 * <li>UNITS_PC
	 * </ul>
	 * 
	 * @param unit
	 *            a given unit.
	 * @return <code>true</code> if the unit is an absolute unit like cm, in,
	 *         mm, pt and pc. Return <code>false</code> if the unit is not an
	 *         absolute unit.( it can be an relative unit like "%", or even an
	 *         unrecognized unit. )
	 */

	public static final boolean isAbsoluteUnit( String unit )
	{
		return DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_CM.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_MM.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_PT.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( unit );
	}

	/**
	 * Return if the given unit is a relative unit or not. The following units
	 * defined in <code>DesignChoiceConstants</code> are considered as relative:
	 * <ul>
	 * <li>UNITS_EM
	 * <li>UNITS_EX
	 * <li>UNITS_PERCENTAGE
	 * <li>UNITS_PX
	 * </ul>
	 * 
	 * @param unit
	 *            a given unit.
	 * @return <code>true</code> if the unit is a relative unit like em, ex, %
	 *         and px. Return <code>false</code> if the unit is not a relative
	 *         unit.( it can be an absolute relative unit like "mm", or even an
	 *         unrecognized unit. )
	 */

	public static final boolean isRelativeUnit( String unit )
	{
		return DesignChoiceConstants.UNITS_EM.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_EX.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_PERCENTAGE
						.equalsIgnoreCase( unit )
				|| DesignChoiceConstants.UNITS_PX.equalsIgnoreCase( unit );
	}

	/**
	 * Returns whether the dimension string value is absolute font size
	 * constant. The absolute font size constants are defined in
	 * <code>DesignChoiceConstants</code> as followed.
	 * 
	 * <ul>
	 * <li><code>FONT_SIZE_XX_SMALL</code>
	 * <li><code>FONT_SIZE_X_SMALL</code>
	 * <li><code>FONT_SIZE_SMALL</code>
	 * <li><code>FONT_SIZE_MEDIUM</code>
	 * <li><code>FONT_SIZE_LARGE</code>
	 * <li><code>FONT_SIZE_X_LARGE</code>
	 * <li><code>FONT_SIZE_XX_LARGE</code>
	 * </ul>
	 * 
	 * @param value
	 *            dimension string value
	 * @return true if the given value is absolute font size constant.
	 */

	public static boolean isAbsoluteFontSize( String value )
	{
		return DesignChoiceConstants.FONT_SIZE_XX_SMALL
				.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_X_SMALL
						.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_SMALL
						.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_MEDIUM
						.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_LARGE
						.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_X_LARGE
						.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_XX_LARGE
						.equalsIgnoreCase( value );
	}

	/**
	 * Returns whether the dimension string value is relative font size
	 * constant. The relative font size constants are defined in
	 * <code>DesignChoiceConstants</code> as followed.
	 * 
	 * <ul>
	 * <li><code>FONT_SIZE_SMALLER</code>
	 * <li><code>FONT_SIZE_LARGER</code>
	 * </ul>
	 * 
	 * @param value
	 *            dimension string value
	 * @return true if the given value is relative font size constant.
	 */

	public static boolean isRelativeFontSize( String value )
	{
		return DesignChoiceConstants.FONT_SIZE_SMALLER.equalsIgnoreCase( value )
				|| DesignChoiceConstants.FONT_SIZE_LARGER
						.equalsIgnoreCase( value );
	}

	/**
	 * Convert a measure from one units to another. The target units must be one
	 * of the absolute units(CM, IN, MM, PT, PC). The input dimension value must
	 * be one of the following types:
	 * <ul>
	 * <li><code>String</code>. It must be a legal dimension value, such as '10
	 * em', '+3.5pt', '10%'.
	 * <li><code>DimensionValue</code>
	 * <li><code>DimensionHandle</code>
	 * </ul>
	 * 
	 * @param value
	 *            the input dimension value to be converted
	 * @param appUnits
	 *            the application units, used as default to convert from when
	 *            units part of the input value is empty or null
	 * @param targetUnits
	 *            the desired units, it must be one of the absolute unit.
	 * @param baseSize
	 *            the base size to convert value with relative units, such as
	 *            em, ex and %, this value must be computed in units of
	 *            <code>DesignChoiceConstants.UNITS_PT</code>.
	 * @param dpi
	 *            int value that represents the pixel per inch
	 * 
	 * @return double value in the target unit.
	 */

	public static double convertTo( Object value, String appUnits,
			String targetUnits, double baseSize, int dpi )
	{
		return convertTo( value, appUnits, targetUnits, baseSize,
				DesignChoiceConstants.UNITS_PT, dpi );
	}

	/**
	 * Convert a measure from one units to another. The application units,
	 * target units and base size units must be one of the absolute units(CM,
	 * IN, MM, PT, PC). The input dimension value must be one of the following
	 * types:
	 * <ul>
	 * <li><code>String</code>. It must be a legal dimension value, measure part
	 * and units part such as '10 em', '+3.5pt', '10%' or only measure part,
	 * such as 10.12, 45, +4.
	 * <li><code>DimensionValue</code>
	 * <li><code>DimensionHandle</code>
	 * </ul>
	 * 
	 * @param value
	 *            the input dimension value to be converted
	 * @param appUnits
	 *            the application units, used as the original units to convert
	 *            from when units part of the input value is empty or null.It
	 *            must be one of the absolute unit(CM, IN, MM, PT, PC).
	 * @param targetUnits
	 *            the desired units, it must be one of the absolute unit(CM, IN,
	 *            MM, PT, PC).
	 * @param baseSize
	 *            the base size to convert value with relative units, such as
	 *            em, ex and %
	 * @param baseSizeUnits
	 *            the units for the base size. It must be one of the absolute
	 *            units(CM, IN, MM, PT, PC). By default it is
	 *            <code>DesignChoiceConstants.UNITS_PT</code>
	 * @param dpi
	 *            int value that represents the pixel per inch
	 * 
	 * @return double value in the target unit.
	 */

	public static double convertTo( Object value, String appUnits,
			String targetUnits, double baseSize, String baseSizeUnits, int dpi )
	{
		if ( value == null )
			return 0.0;

		double measure = 0.0;
		String fromUnits = ""; //$NON-NLS-1$

		// get the measure and unit from the value

		if ( value instanceof String )
		{
			try
			{
				DimensionValue parsedValue = DimensionValue
						.parse( (String) value );
				// the value can not be null
				measure = parsedValue.getMeasure( );
				fromUnits = parsedValue.getUnits( );
			}
			catch ( PropertyValueException e )
			{
				// TODO: support the font-size choices?
				throw new IllegalArgumentException(
						"Given string is not well-formatted dimension!" ); //$NON-NLS-1$
			}
		}
		else if ( value instanceof DimensionValue )
		{
			DimensionValue parsedValue = (DimensionValue) value;
			measure = parsedValue.getMeasure( );
			fromUnits = parsedValue.getUnits( );
		}
		else if ( value instanceof DimensionHandle )
		{
			DimensionHandle dimensionHandle = (DimensionHandle) value;
			measure = dimensionHandle.getMeasure( );
			fromUnits = dimensionHandle.getUnits( );
			if ( StringUtil.isBlank( fromUnits ) )
				fromUnits = dimensionHandle.getDefaultUnit( );
		}
		// not supported value format
		else
			throw new IllegalArgumentException(
					"Given dimension value is a not supported format!" ); //$NON-NLS-1$

		// if units is null or empty, set it to application unit
		if ( StringUtil.isBlank( fromUnits ) )
			fromUnits = appUnits;

		// if baseSizeUnit is empty or null, set it to 'pt'
		if ( StringUtil.isBlank( baseSizeUnits ) )
			baseSizeUnits = DesignChoiceConstants.UNITS_PT;

		DimensionValue convertedValue = null;
		// do some prepare for the relative units
		if ( DesignChoiceConstants.UNITS_EM.equals( fromUnits ) )
		{
			convertedValue = DimensionUtil.convertTo( measure * baseSize,
					baseSizeUnits, targetUnits );
		}
		else if ( DesignChoiceConstants.UNITS_EX.equals( fromUnits ) )
		{
			convertedValue = DimensionUtil.convertTo( measure * baseSize / 3,
					baseSizeUnits, targetUnits );
		}
		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( fromUnits ) )
		{
			convertedValue = DimensionUtil.convertTo( measure * baseSize / 100,
					baseSizeUnits, targetUnits );
		}
		else if ( DesignChoiceConstants.UNITS_PX.equals( fromUnits ) )
		{
			convertedValue = convertTo( measure / dpi,
					DesignChoiceConstants.UNITS_IN, targetUnits );
		}
		else
			convertedValue = convertTo( measure, fromUnits, targetUnits );
		return convertedValue.getMeasure( );

	}

	/**
	 * Merges two dimension values with default dpi. If these two dimension
	 * values are not in the same unit, only dimension values in absolute units
	 * and pixels can be merged. The unit of the merged result will be according
	 * to the first dimension value except its unit is is pixel. If one of them
	 * is null, the other value will be returned.
	 * 
	 * @param dimension1
	 *            the first dimension value to merge
	 * @param dimension2
	 *            the second dimension value to merge
	 * @return the merged dimension value, or null if these two dimension value
	 *         cannot be merged or both of them are null.
	 */
	public static DimensionValue mergeDimension( DimensionValue dimension1,
			DimensionValue dimension2 )
	{
		return mergeDimension( dimension1, dimension2, -1 );
	}

	/**
	 * Merges two dimension values with the given dpi value. If these two
	 * dimension values are not in the same unit, only dimension values in
	 * absolute units and pixels can be merged. The unit of the merged result
	 * will be according to the first dimension value except its unit is
	 * pixel.If one of them is null, the other value will be returned.
	 * 
	 * @param dimension1
	 *            the first dimension value to merge
	 * @param dimension2
	 *            the second dimension value to merge
	 * @param dpi
	 *            the dpi value
	 * @return the merged dimension value, or null if these two dimension value
	 *         cannot be merged or both of them are null.
	 */
	public static DimensionValue mergeDimension( DimensionValue dimension1,
			DimensionValue dimension2, int dpi )
	{
		if ( dimension1 == null || dimension2 == null )
		{
			if ( dimension1 == null )
			{
				return dimension2;
			}
			return dimension1;
		}
		String unit = dimension1.getUnits( );
		String unit2 = dimension2.getUnits( );
		Double meature = null;
		if ( unit.equalsIgnoreCase( unit2 ) )
		{
			meature = dimension1.getMeasure( ) + dimension2.getMeasure( );
		}
		else if ( isAbsoluteUnit( unit ) )
		{
			if ( isAbsoluteUnit( unit2 ) )
			{
				meature = dimension1.getMeasure( )
						+ convertTo( dimension2, null, unit ).getMeasure( );
			}
			else if ( DesignChoiceConstants.UNITS_PX.equalsIgnoreCase( unit2 ) )
			{
				meature = dimension1.getMeasure( )
						+ convertTo( dimension2, null, unit, 0,
								validateDPI( dpi ) );
			}
		}
		else if ( DesignChoiceConstants.UNITS_PX.equalsIgnoreCase( unit )
				&& isAbsoluteUnit( unit2 ) )
		{
			meature = convertTo( dimension1, null, unit2, 0, validateDPI( dpi ) )
					+ dimension2.getMeasure( );
			unit = unit2;
		}
		if ( meature != null )
			return new DimensionValue( meature, unit );
		return null;
	}

	/**
	 * Validates the dpi value. If the value is invalid, try to use the JVM
	 * defined value and model defined default value.
	 * 
	 * @param dpi
	 *            the dpi value
	 * @return the validated dpi value
	 */
	private static int validateDPI( int dpi )
	{
		if ( dpi <= 0 )
			// Try to use JVM defined value if the dpi value is invalid.
			dpi = ResolutionSyntax.DPI;
		if ( dpi <= 0 )
			// Use the default value if the JVM defined is invalid.
			dpi = DEFAULT_DPI;
		return dpi;
	}
}
