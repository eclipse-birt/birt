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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.DimensionValue;

/**
 * Utility class to do conversions between units.
 *  
 */
public class DimensionUtil
{

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
	 * Convert a measure from one units to another. The conversion is between
	 * absolute the units should be one of the absolute units(CM, IN, MM, PT).
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
			else if ( DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure  * POINTS_PER_PICA / POINTS_PER_INCH;
			else
				assert false;
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
			else if ( DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure  * POINTS_PER_PICA / POINTS_PER_CM;
			else
				assert false;
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
			else if ( DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure  * POINTS_PER_PICA * 10 / POINTS_PER_CM;
			else
				assert false;
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
			else if ( DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure * POINTS_PER_PICA;
			else
				assert false;
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
			else if ( DesignChoiceConstants.UNITS_PT.equalsIgnoreCase( fromUnits ) )
				targetMeasure = measure / POINTS_PER_PICA;
			else
				assert false;
		}
		else
			assert false;

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
	 *            the application unit of the dimension, if the dimension has not
	 *            specified a unit, the the application unit will be applied to it.
	 *            It must be one of the absolute unit.
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
	 *            the application unit of the dimension, if the dimension has not
	 *            specified a unit, the the application unit will be applied to it.
	 *            It must be one of the absolute unit.
	 * @param targetUnits
	 *            the desired unit.
	 * @return <code>DimensionValue</code> in the target unit.
	 * @throws PropertyValueException
	 *             if the dimension is not valid.
	 */

	public static DimensionValue convertTo( String dimension,
			String appUnit, String targetUnits )
			throws PropertyValueException

	{
		DimensionValue dim = DimensionValue.parse( dimension );
		return convertTo( dim, appUnit, targetUnits );
	}

	/**
	 * Return if the given unit is an absolute unit or not. The following units
	 * defined in <code>DesignChoiceConstants</code> are considered as
	 * absolute:
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
		return DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(unit);
	}
}

