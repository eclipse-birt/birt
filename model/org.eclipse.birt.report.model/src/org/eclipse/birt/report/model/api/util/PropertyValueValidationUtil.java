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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Utility class to validate the property value.
 * 
 */

public class PropertyValueValidationUtil
{

	/**
	 * Validates the values of the item members.
	 * 
	 * @param propDefn
	 *            the property definition
	 * @param item
	 *            the structure to validate
	 * @param element
	 *            the element
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	private static Object validateStructure( DesignElementHandle element,
			ElementPropertyDefn propDefn, Object value )
			throws SemanticException
	{

		if ( !( value instanceof IStructure ) )
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.STRUCT_TYPE );

		IStructure item = (IStructure) value;
		if ( item.getDefn( ) != propDefn.getStructDefn( ) )
			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.STRUCT_TYPE );

		return doValidateStructure( element, propDefn.getStructDefn( ), item );
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param structDefn
	 *            the structure property definition
	 * @param item
	 *            the structure to validate
	 * @param element
	 *            the element
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	private static IStructure doValidateStructure( DesignElementHandle element,
			IStructureDefn structDefn, IStructure item )
			throws SemanticException
	{

		for ( Iterator iter = structDefn.propertiesIterator( ); iter.hasNext( ); )
		{
			PropertyDefn memberDefn = (PropertyDefn) iter.next( );
			item.setProperty( memberDefn, memberDefn.validateValue( element
					.getModule( ), item.getLocalProperty( element.getModule( ),
					memberDefn ) ) );
		}

		if ( item instanceof Structure )
		{
			List errorList = ( (Structure) item ).validate(
					element.getModule( ), element.getElement( ) );
			if ( errorList.size( ) > 0 )
			{
				throw (SemanticException) errorList.get( 0 );
			}
		}

		return item;
	}

	/**
	 * Validates a structure list.
	 * 
	 * @param propDefn
	 *            the property definition
	 * @param item
	 *            the structure to validate
	 * @param element
	 *            the element
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	private static Object validateList( DesignElementHandle element,
			ElementPropertyDefn propDefn, Object value )
			throws SemanticException
	{
		if ( !( value instanceof List ) )
			return null;

		assert propDefn.isList( );

		List retList = new ArrayList( );
		List list = (List) value;
		IStructureDefn structDefn = propDefn.getStructDefn( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			IStructure item = (IStructure) list.get( i );
			if ( item.getDefn( ) != structDefn )
				throw new PropertyValueException( element.getElement( ),
						propDefn, item,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE );

			retList.add( doValidateStructure( element, structDefn, item ) );
		}

		return retList;
	}

	/**
	 * Validates a value to be stored for the given property.
	 * 
	 * @param element
	 *            the element to store the property value
	 * @param propName
	 *            the property name
	 * @param propValue
	 *            the value to check
	 * @return the translated value to be stored
	 * @throws SemanticException
	 *             if <code>propValue</code> is invalid.
	 */

	public static Object validateProperty( DesignElementHandle element,
			String propName, Object propValue ) throws SemanticException
	{

		ElementPropertyDefn propDefn = (ElementPropertyDefn) element
				.getPropertyDefn( propName );

		if ( propDefn == null )
			throw new PropertyNameException( element.getElement( ), propName );

		Object retValue = null;

		switch ( propDefn.getTypeCode( ) )
		{
			case IPropertyType.EXTENDS_TYPE :
				throw new PropertyValueException( propValue,
						PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
						propDefn.getTypeCode( ) );			
			case IPropertyType.STRUCT_TYPE :
				if ( propDefn.isList( ) )
					retValue = validateList( element, propDefn, propValue );
				else
					retValue = validateStructure( element, propDefn, propValue );
				break;
			default :
				retValue = propDefn.validateValue( element.getModule( ),
						propValue );
		}

		return retValue;
	}
}
