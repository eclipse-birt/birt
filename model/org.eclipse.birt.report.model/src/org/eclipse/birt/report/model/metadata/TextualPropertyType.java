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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;

/**
 * Base class for property types stored as literal strings.
 * 
 */

public abstract class TextualPropertyType extends PropertyType
{

	/**
	 * Constructor
	 * 
	 * @param displayNameID
	 *            display name id of the property type.
	 */

	TextualPropertyType( String displayNameID )
	{
		super( displayNameID );
	}

	/**
	 * Validates a generic string. An empty string will never be returned.
	 * 
	 * @return the value as a string
	 */

	public Object validateValue( Module module, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;
		if ( value instanceof String )
		{
			String validatedValue = StringUtil.trimString( (String) value );

			// It's impossible to be an empty string.

			assert validatedValue == null || validatedValue.length( ) > 0;

			return validatedValue;
		}
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				getTypeCode( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java.lang.Object)
	 */

	public String toString( Module module, PropertyDefn defn, Object value )
	{
		return (String) value;
	}

}
