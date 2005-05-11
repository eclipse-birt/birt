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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates one list property of element. The property type should structure
 * list.
 * 
 * <h3>Rule</h3>
 * The rule is that
 * <ul>
 * <li>all structures in this list property should be valid.
 * <li>the value of the property with <code>NamePropertyType</code> should be
 * unique in the structure list.
 * </ul>
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to the property whose type is structure list
 * of one <code>DesignElement</code>.
 */

public class StructureListValidator extends AbstractPropertyValidator
{
	/**
	 * Name of this validator.
	 */
	
	public final static String NAME = "StructureListValidator"; //$NON-NLS-1$

	private static StructureListValidator instance = new StructureListValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static StructureListValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether a new structure can be added to structure list.
	 * 
	 * @param element
	 *            the element holding the structure list
	 * @param propDefn
	 *            definition of the list property
	 * @param list
	 *            the structure list
	 * @param toAdd
	 *            the structure to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAdding( DesignElementHandle element,
			IPropertyDefn propDefn, List list, IStructure toAdd )
	{
		// ElementPropertyDefn propDefn = element.getElement( ).getPropertyDefn(
		// propName );
		//
		// assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE
		// && propDefn.isList( );
		//
		// List list = (List) element.getElement( ).getLocalProperty(
		// element.getDesign( ), propDefn );

		return doCheckStructureList( element.getDesign( ),
				element.getElement( ), propDefn, list, toAdd );
	}

	/**
	 * Validates whether the list property specified by <code>propName</code>
	 * is invalid.
	 * @param design
	 *            the report design
	 * @param element
	 *            the master page to validate
	 * @param propName
	 *            the name of the list property to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( ReportDesign design, DesignElement element,
			String propName )
	{
		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );

		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE
				&& propDefn.isList( );

		List list = (List) element.getLocalProperty( design, propDefn );

		return doCheckStructureList( design, element, propDefn, list, null );

	}

	/**
	 * Checks all structures in the specific property whose type is structure
	 * list property type.
	 * @param design
	 *            the report design
	 * @param element
	 *            the design element to validate
	 * @param propDefn
	 *            the property definition of the list property
	 * @param list
	 *            the structure list to check
	 * @param toAdd
	 *            the structure to add. This parameter maybe is
	 *            <code>null</code>.
	 * 
	 * @return the error list
	 */

	private List doCheckStructureList( ReportDesign design,
			DesignElement element, IPropertyDefn propDefn, List list,
			IStructure toAdd )
	{
		boolean checkList = toAdd == null;

		List errorList = new ArrayList( );

		if ( list == null || list.size( ) == 0 )
			return errorList;

		assert propDefn != null;
		assert propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE;

		// Get the unique member whose value should be unique in the
		// structure list.
		// The type of unique member is name property type.
		// Note: The first unique member is considered.

		PropertyDefn uniqueMember = null;

		Iterator iter = propDefn.getStructDefn( ).getPropertyIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn memberDefn = (PropertyDefn) iter.next( );

			if ( memberDefn.getTypeCode( ) == PropertyType.NAME_TYPE )
			{
				uniqueMember = memberDefn;
				break;
			}
		}

		HashSet values = new HashSet( );

		// Check whether there two structure has the same value of
		// the unique member.

		for ( int i = 0; i < list.size( ); i++ )
		{
			Structure struct = (Structure) list.get( i );

			if ( checkList )
				errorList.addAll( struct.validate( design, element ) );

			if ( uniqueMember != null )
			{
				String value = (String) struct.getProperty( design,
						uniqueMember );
				if ( values.contains( value ) )
				{
					if ( checkList )
						errorList
								.add( new PropertyValueException(
										element,
										propDefn,
										value,
										PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS ) );
				}
				else
				{
					values.add( value );
				}
			}
		}

		// If the toAdd structure is added the structure list, check whether
		// there is a structure in the list has the same value of the unique
		// member.

		if ( uniqueMember != null && toAdd != null )
		{
			String value = (String) toAdd.getProperty( design, uniqueMember );
			if ( values.contains( value ) )
			{
				errorList.add( new PropertyValueException( element, propDefn
						.getName( ), value,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS ) );
			}
		}

		return errorList;
	}
}
