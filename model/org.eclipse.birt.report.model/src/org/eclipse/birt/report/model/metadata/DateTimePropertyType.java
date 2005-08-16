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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Date-time property type. Date-time property is stored as
 * <code>java.util.Date</code>
 *  
 */

public class DateTimePropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.dateTime"; //$NON-NLS-1$

	/**
	 * Fixed formatter for datetime expression in xml.
	 */

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH ); //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public DateTimePropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Validates the date time property value,the value is either a Java Date
	 * object, or a string with a date and/or time validated for the current
	 * locale.
	 * <p>
	 * Date-time property is stored as <code>java.util.Date</code>
	 * <p>
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateValue( Module module, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{

		if ( value == null )
			return null;
		if ( value instanceof Date )
			return value;
		if ( value instanceof String )
		{
			return validateInputString( module, defn, (String) value );
		}

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				DATE_TIME_TYPE );
	}

	/**
	 * Validates the XML representation of the date property value. Xml date
	 * time format should in the fixed pattern "yyyy-MM-dd HH:mm:ss".
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateXml( Module module, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		value = StringUtil.trimString( value );
		if ( value == null )
			return null;

		// fixed xml format.
		try
		{
			return formatter.parse( value );
		}
		catch ( ParseException e )
		{
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					getTypeCode( ) );
		}

	}

	/**
	 * Returns the display string for the Date object in the current locale.
	 * 
	 * @return display string for the date object in the current locale.
	 */

	public String toDisplayString( Module module, PropertyDefn defn,
			Object value )
	{
		if ( value == null )
			return null;

		assert value instanceof Date;

		// Convert to Locale-specific format.
		DateFormat formatter = DateFormat.getDateInstance( DateFormat.SHORT,
				ThreadResources.getLocale( ) );
		return formatter.format( (Date) value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return DATE_TIME_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return DATE_TIME_TYPE_NAME;
	}

	/**
	 * Validates the locale-dependent value for the date time type, validate the
	 * <code>value</code> in the locale-dependent way and convert the
	 * <code>value</code> into a Date object.
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateInputString( Module module, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		if ( StringUtil.isBlank( value ) )
			return null;

        // Parse the input in locale-dependent way.
        
        DateFormat formatter = DateFormat.getDateInstance( DateFormat.SHORT,
                ThreadResources.getLocale( ) );
        try
        {
            return formatter.parse( value );
        }
        catch ( ParseException e )
        {
            throw new PropertyValueException( value,
                    PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
                    DATE_TIME_TYPE );
        }
	}

	
    /**
     * Converts the Date object into a string presentation in a fixed xml format
     * "yyyy-MM-dd HH:mm:ss".
     */

	public String toString( Module module, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		assert value instanceof Date;

		return formatter.format( (Date) value );
	}

}