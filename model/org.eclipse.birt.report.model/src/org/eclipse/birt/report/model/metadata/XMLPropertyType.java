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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Module;

/**
 * XML Property value type.
 * 
 */

public class XMLPropertyType extends TextualPropertyType
{

	/**
	 * The no trim value.
	 */
	static final int NO_VALUE = 0;

	/**
	 * The value of the operation which will trim the input string.
	 */
	static final int NO_TRIM_VALUE = 1;

	/**
	 * The value of the operation which will trim the space.
	 */
	static final int TRIM_SPACE_VALUE = 2;

	/**
	 * The value of the operation which will normalizes the empty string to an
	 * null string.
	 */
	static final int TRIM_EMPTY_TO_NULL_VALUE = 4;

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.xml"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public XMLPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return XML_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return XML_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.TextualPropertyType#validateValue
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	public Object validateValue( Module module, PropertyDefn defn, Object value )
			throws PropertyValueException
	{
		if ( value == null )
			return null;
		if ( value instanceof String )
		{
			return trimString( (String) value, defn.getTrimOption( ) );
		}
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				getTypeCode( ) );
	}

	/**
	 * Trim a string according to the trim option.
	 * 
	 * @param value
	 *            the input value.
	 * @param trimOption
	 *            the trim option.
	 * @return the output value.
	 */
	private String trimString( String value, int trimOption )
	{
		if ( value == null )
			return null;

		if ( ( trimOption & TRIM_SPACE_VALUE ) != 0 )
			value = value.trim( );
		if ( ( trimOption & TRIM_EMPTY_TO_NULL_VALUE ) != 0 )
		{
			if ( value.length( ) == 0 )
				value = null;
		}
		return value;
	}

}
