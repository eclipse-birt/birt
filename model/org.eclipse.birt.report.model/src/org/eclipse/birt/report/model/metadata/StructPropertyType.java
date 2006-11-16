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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents the property type for a list of objects (structures) or a simple
 * structure.
 * 
 */

public class StructPropertyType extends PropertyType
{

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger( StructPropertyType.class
			.getName( ) );
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.struct"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public StructPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return STRUCT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return STRUCT_TYPE_NAME;
	}

	/**
	 * Can not store objects of this list property type directly. Call to this
	 * method will always throw an exception.
	 * 
	 */

	public Object validateValue( Module module, PropertyDefn defn, Object value )
			throws PropertyValueException
	{

		if ( value == null )
		{
			logger.log( Level.WARNING, "The value of the structure is null" ); //$NON-NLS-1$
			return null;
		}
		
		// Now support empty list if structure property is list.

		if ( defn.isList( ) )
		{
			if( value instanceof List )
			{
				if( ((List)value).isEmpty( ) )
				{
					return value;
				}
			}
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					STRUCT_TYPE );
		}
		
		if ( value instanceof Structure )
		{
			logger.log( Level.INFO, "Validate the structure value for each of its member " ); //$NON-NLS-1$
			Iterator iter = ( (Structure) value ).getDefn( )
					.propertiesIterator( );
			while ( iter.hasNext( ) )
			{
				PropertyDefn memberDefn = (PropertyDefn) iter.next( );
				if ( !memberDefn.isList( ) )
				{
					Object propValue = ( (Structure) value ).getProperty(
							module, memberDefn );
					memberDefn.validateValue( module, propValue );
				}
			}

			return value;
		}

		// exception
		logger.log( Level.WARNING, "The value of this structure property is not a valid type" ); //$NON-NLS-1$
		return null;

	}

	/**
	 * Converts the structure list property type into an integer. If value is
	 * null, return 0, else return the size of the list value.
	 * 
	 * @return the integer value of the structure list property type.
	 */

	public int toInteger( Module module, Object value )
	{
		// Return the list size as the int value.

		if ( value == null )
			return 0;
		return ( (ArrayList) value ).size( );
	}

	/**
	 * Can not convert a list property type to a string. This method will always
	 * return null.
	 * 
	 */

	public String toString( Module module, PropertyDefn defn, Object value )
	{
		// Cannot convert to string

		return null;
	}

}
