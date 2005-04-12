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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents the integer property type. Integer property values are
 * stored as <code>java.lang.Integer</code> objects.
 */

public class IntegerPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.integer"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public IntegerPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Ensures that the value is a valid integer. Can be any of the following:
	 * <li>Integer object.</li>
	 * <li>Float or Double object. Truncate the decimal portion. Ensure that
	 * the value is within the integer range.</li>
	 * <li>BigDecimal object. Truncate the decimal portion. Ensure that the
	 * value is is within the integer range.</li>
     * <li>A Boolean object, <code>TRUE</code> will be converted into
     * Integer(1), <code>FALST</code> will be converted into Integer(0)
     * </li>
	 * <li>String that must evaluate to an integer in either of the two Java
	 * forms: decimal [1-9][0-9]* or hexadecimal format &[hH]xxxx.</li>
	 * <li>String that must evaluate to an HTML hexidecimal: #xxxxx.</li>.
	 * <p>
	 * 
	 * @return object of type Integer or null if value is null..
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
        if( value == null )
            return null;
        if ( value instanceof Integer )
			return value;
		if ( value instanceof Float )
			return new Integer( ( (Float) value ).intValue( ) );
		if ( value instanceof Double )
			return new Integer( ( (Double) value ).intValue( ) );
		if ( value instanceof String )
			return parseInteger( (String) value );
		if ( value instanceof BigDecimal )
			return new Integer( ( (BigDecimal) value ).intValue( ) );
		if ( value instanceof Boolean )
			return new Integer( ( (Boolean) value ).booleanValue( )
					? BooleanPropertyType.INT_TRUE
					: BooleanPropertyType.INT_FALSE );

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, INTEGER_TYPE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return INTEGER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return INTEGER_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java.lang.Object)
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		return value.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toInteger(java.lang.Object)
	 */

	public int toInteger( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0;

		return ( (Integer) value ).intValue( );
	}

	/**
	 * Returns a new <code>Integer</code> initialized to the value represented
	 * by the specified <code>String</code>.
	 * 
	 * @param value
	 *            the string representing an integer
	 * @return Returns the <code>Integer</code> represented by the string
	 *         argument
	 * @throws PropertyValueException
	 *             if the string can not be parsed to an integer
	 */
    
	protected Integer parseInteger( String value )
			throws PropertyValueException
	{
		try
		{
			return Integer.decode( value );
		}
		catch ( NumberFormatException e )
		{
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, INTEGER_TYPE );
		}
	}

}
