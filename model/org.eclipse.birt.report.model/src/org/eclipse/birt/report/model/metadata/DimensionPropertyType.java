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

package org.eclipse.birt.report.model.metadata;

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Dimension property type. Dimension property values have two parts:
 * <p>
 * <dl>
 * <dt><strong>Measure </strong></dt>
 * <dd>A numeric measurement expressed as a Java double.</dd>
 * 
 * <dt><strong>Optional units </strong></dt>
 * <dd>The units of the dimension expressed using one of the unit specifiers
 * defined in
 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants }:
 * <ul>
 * <li>in (inches)</li>
 * <li>mm (millimeters)</li>
 * <li>cm (centimeters)</li>
 * <li>pt (points)</li>
 * <li>pc (Picas)</li>
 * <li>px (pixels)</li>
 * <li>em (the height of the element's font )</li>
 * <li>ex (x-height)</li>
 * <li>% (percentage)
 * <li>
 * </ul>
 * </dd>
 * </dl>
 * <p>
 * For example: "10mm", "12.3in", "1.5cm", "20pt" or "0.1".
 * <p>
 * The units are optional. If omitted, the units are assumed to be those
 * specified on the report design itself. Default units are always absolute. For
 * example, a U.S. developer will mostly use inches, and so the design's default
 * units would be inches. Any report dimensions in inches can omit the unit
 * suffix: the model knows they are in inches because that is the default for
 * the report.
 * <p>
 * Dimension values are stored as <code>DimensionValue</code> objects. These
 * objects store the measure as a double and the units as an string value.
 * <p>
 * The user enters dimensions as strings with the optional unit suffix. The UI
 * displays dimensions as strings, again with the optional unit suffix.
 * <p>
 * Some parts of the application will find it easier to work with specific
 * physical units. For example, the layout editor may choose to work in units in
 * millimeters. In this case, the application specifies its
 * <em>application units</em> in the design session. If the application
 * requests the dimension property as a double, this class converts the property
 * into the desired application units, taking into consideration the unit suffix
 * (if any) for the property and the design's default units (if the property
 * value has no unit suffix.)
 * 
 * @see DimensionValue
 * @see org.eclipse.birt.report.model.api.util.DimensionUtil
 */

public class DimensionPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.dimension"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public DimensionPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Validates the dimension property value, the value can be one of the
	 * followings:
	 * <ul>
	 * <li>Null, meaning to clear the property value.</li>
	 * <li>A string with optional units. The string is of the form n( *)[u]
	 * where n is a decimal number with the decimal separator specified by the
	 * current locale, ( *) is optional white space, and [u] is an optional
	 * units specifier. Units can be any of the recognized BIRT units: in, cm,
	 * mm, pt, pc, px, em, ex or % . If the unit specifier is omitted, use the
	 * default for the design.</li>
	 * <li>An <code>Integer</code>,<code>Float</code>,
	 * <code>Double</code> or <code>BigDecimal</code>. Assume that the
	 * units are specified by the design default.</li>
	 * </ul>
	 * 
	 * @return object is of type <code>DimensionValue</code> or null.
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;

		if ( value instanceof String )
		{
			DimensionValue dim = DimensionValue.parseInput( (String) value );
			if ( dim == null )
				return null;
			if ( !StringUtil.isBlank( dim.getUnits( ) ) )
				return dim;
			return new DimensionValue( dim.getMeasure( ), getDefaultUnit(
					design, defn ) );
		}
		if ( value instanceof DimensionValue )
		{
			if ( !StringUtil.isBlank( ( (DimensionValue) value ).getUnits( ) ) )
				return value;
			return new DimensionValue(
					( (DimensionValue) value ).getMeasure( ), getDefaultUnit(
							design, defn ) );
		}
		if ( value instanceof Integer )
			return fromDouble( design, defn, ( (Integer) value ).intValue( ) );
		if ( value instanceof Double )
			return fromDouble( design, defn, ( (Double) value ).doubleValue( ) );
		if ( value instanceof BigDecimal )
			return fromDouble( design, defn, ( (BigDecimal) value )
					.doubleValue( ) );

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				PropertyType.DIMENSION_TYPE );
	}

	/**
	 * Gets the default unit of the property. If the property defines a default
	 * unit , then return it, otherwise return the application unit defined on
	 * session.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            the property definition
	 * @return the default unit
	 */

	private String getDefaultUnit( ReportDesign design, PropertyDefn defn )
	{
		String unit = defn.getDefaultUnit( );
		if ( !StringUtil.isBlank( unit ) )
			return unit;
		assert design != null;
		return design.getSession( ).getUnits( );
	}

	/**
	 * Creates a <code>DimensionValue</code> given its measure. The unit is
	 * assumed to be in session unit.
	 * 
	 * @param design
	 *            the report design.
	 * @param defn
	 *            the definition of the property
	 * @param value
	 *            the double value of the measure.
	 * @return an <code>DimensionValue</code> Object in session unit.
	 *  
	 */

	private DimensionValue fromDouble( ReportDesign design, PropertyDefn defn,
			double value )
	{
		return new DimensionValue( value, getDefaultUnit( design, defn ) );
	}

	/**
	 * Validates the XML representation of the dimension property value. Parse
	 * it into a <code>DimensionValue</code>. The validation will use
	 * {@link DimensionValue#parse(String)}to parse the xml string.
	 * 
	 * @return a <code>DimensionValue</code> that holds the measure and unit
	 *         parsed from the xml value.
	 *  
	 */

	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{

		DimensionValue dim = DimensionValue.parse( value );

		if ( dim != null )
			validateUnits( design, defn, dim, true );

		return dim;

	}

	/**
	 * Validates the string value in the locale-dependent way. Possible valid
	 * property value is one of the following:
	 * <ul>
	 * <li>Null or blank string.</li>
	 * <li>A string with optional units. The string is of the form n( *)[u]
	 * where n is a decimal number with the decimal separator specified by the
	 * current locale, ( *) is optional white space, and [u] is an optional
	 * units specifier. Units can be any of the recognized BIRT units: in, cm,
	 * mm, pt, pc, px, em, ex or % . If the unit specifier is omitted, use the
	 * default for the design.</li>
	 * </ul>
	 * 
	 * @return object is of type <code>DimensionValue</code> or null.
	 *  
	 */

	public Object validateInputString( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		DimensionValue dim = DimensionValue.parseInput( value );

		if ( dim != null )
			validateUnits( design, defn, dim, false );

		return dim;
	}

	/**
	 * Converts the value into a locale-dependent string. The measure part of
	 * the <code>DimensionValue</code> will be formatted in the current
	 * locale, e.g: 12,000,000.123 for US locale while in German the value it
	 * will be 12.000.000,123. The unit will be attached after the measure.
	 */

	public String toDisplayString( ReportDesign design, PropertyDefn defn,
			Object value )
	{
		if ( value == null )
			return null;

		assert value instanceof DimensionValue;

		// Return the measure and unit of this DimensionValue in localized
		// format.
		return ( (DimensionValue) value ).toDisplayString( );
	}

	/**
	 * Validates the unit of the dimension value, checks to see if it is in the
	 * allowed units set.
	 * 
	 * @param defn
	 *            property definition.
	 * @param value
	 *            the dimension value of the dimension.
	 * @throws PropertyValueException
	 *             if unit is not allowed.
	 *  
	 */

	private void validateUnits( ReportDesign design, PropertyDefn defn,
			DimensionValue value, boolean isUnitRequired )
			throws PropertyValueException
	{
		assert value != null;
		String unit = value.getUnits( );
		double measure = value.getMeasure( );
		if ( Double.compare( 0.0, measure ) != 0 && isUnitRequired && StringUtil.isBlank( unit ) )
		{
			throw new PropertyValueException( null, defn, value,
					PropertyValueException.DESIGN_EXCEPTION_UNIT_REQUIRED );
		}
		if ( StringUtil.isBlank( value.getUnits( ) ) )
		{
			unit = getDefaultUnit( design, defn );
		}

		IChoiceSet units = defn.getAllowedChoices( );

		assert units != null;
		if ( !units.contains( unit ) )
		{
			// unit not allowed.

			throw new PropertyValueException( null, defn, value,
					PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return DIMENSION_TYPE_NAME;
	}

	/**
	 * Converts the dimension property value to a locale-independent string. The
	 * string will be converted into a format like "#.###", there is no group
	 * separator and remains at most 3 digits after the decimal separator. e.g:
	 * "12,000,000.12345cm" will be converted into "12000000.123"
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		assert value instanceof DimensionValue;
		return value.toString( );
	}

	/**
	 * Converts the dimension property value to double value. The dimension
	 * measure will be converted into the session unit.
	 * 
	 * @return double value of the dimension value in session unit. Return
	 *         <code>0.0</code> if <code>value</code> is null.
	 *  
	 */

	public double toDouble( ReportDesign design, Object value )
	{
		DimensionValue dim = (DimensionValue) value;
		if ( dim == null )
			return 0.0;

		return DimensionUtil.convertTo( dim.getMeasure( ), dim.getUnits( ),
				design.getSession( ).getUnits( ) ).getMeasure( );
	}
}