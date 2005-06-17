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

import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents a reference to a structure. A structure reference is different
 * from a structure list property. A structure list <em>contains</em> a
 * structure. A structure reference simply <em>references</em> a structure
 * defined in the report design.
 * <p>
 * A structure reference can be in one of two states: resolved or unresolved. A
 * resolved reference points to an the "target" structure itself. An unresolved
 * reference gives only the name of the target structure, and the structure
 * itself may or may not exist.
 * <p>
 * Elements that contain properties of this type must provide code to perform
 * semantic checks on the reference property. This is done to avoid the need to
 * search the property list to find any properties that are of this type.
 * <p>
 * The reference value are stored as an <code>StructRefValue</code>
 * 
 * @see StructRefValue
 */

public class StructRefPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.structRef"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public StructRefPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return STRUCT_REF_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyType#getName()
	 */

	public String getName( )
	{
		return STRUCT_REF_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;

		StructureDefn targetDefn = (StructureDefn) defn.getStructDefn( );
		if ( value instanceof String )
		{
			String name = StringUtil.trimString( (String) value );
			return validateStringValue( design, targetDefn, name );
		}
		if ( value instanceof Structure )
		{
			Structure target = (Structure) value;
			return validateStructValue( design, targetDefn, target );
		}

		// Invalid property value.

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				PropertyType.ELEMENT_REF_TYPE );
	}

	/**
	 * Validates the structure name.
	 * 
	 * @param design
	 *            report design
	 * @param targetDefn
	 *            definition of target structure
	 * @param name
	 *            structure name
	 * @return the resolved structure reference value
	 * @throws PropertyValueException
	 *             if the type of target structure is not that target
	 *             definition, or the structure with the given name is not
	 *             found.
	 */

	private StructRefValue validateStringValue( ReportDesign design,
			StructureDefn targetDefn, String name )
			throws PropertyValueException
	{
		if ( StringUtil.isBlank( name ) )
			return null;

		Structure target = getStructure( design, targetDefn, name );

		// Element is unresolved.

		if ( target == null )
			return new StructRefValue( name );

		// Check type.

		if ( targetDefn != target.getDefn( ) )
			throw new PropertyValueException(
					target.getReferencableProperty( ),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE,
					PropertyType.STRUCT_REF_TYPE );

		// Resolved reference.

		return new StructRefValue( target );
	}

	private Structure getStructure( ReportDesign design,
			StructureDefn targetDefn, String name )
	{
		if ( StringUtil.isBlank( name ) || targetDefn == null )
			return null;

		List referencableProperties = design.getReferencablePropertyDefns( );
		assert !referencableProperties.isEmpty( );
		IElementPropertyDefn defn = null;
		for ( int i = 0; i < referencableProperties.size( ); i++ )
		{
			IElementPropertyDefn refDefn = (IElementPropertyDefn) referencableProperties
					.get( i );
			if ( targetDefn == refDefn.getStructDefn( ) )
			{
				defn = refDefn;
				break;
			}
		}

		if ( defn == null )
			return null;
		assert defn.getTypeCode( ) == PropertyType.STRUCT_TYPE;
		
		if ( defn.isList( ) )
		{
			List list = design.getListProperty( design, defn.getName( ) );
			if ( list == null )
				return null;
			for ( int i = 0; i < list.size( ); i++ )
			{
				Structure struct = (Structure) list.get( i );
				if ( name.equals( struct.getReferencableProperty( ) ) )
					return struct;
			}
		}
		else
		{
			Structure struct = (Structure) design.getProperty( design, defn
					.getName( ) );
			if ( name.equals( struct.getReferencableProperty( ) ) )
				return struct;
		}
		return null;
	}

	/**
	 * Validates the structure value.
	 * 
	 * @param design
	 *            report design
	 * @param targetDefn
	 *            definition of target structure
	 * @param target
	 *            target structure
	 * @return the resolved structure reference value
	 * @throws PropertyValueException
	 *             if the type of target structure is not that target
	 *             definition.
	 */

	private StructRefValue validateStructValue( ReportDesign design,
			StructureDefn targetDefn, Structure target )
			throws PropertyValueException
	{
		// Check type.

		if ( targetDefn != target.getDefn( ) )
			throw new PropertyValueException(
					target.getReferencableProperty( ),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE,
					PropertyType.STRUCT_REF_TYPE );

		// Resolved reference.

		return new StructRefValue( target );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#toString(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		if ( value instanceof String )
			return (String) value;

		return ( (StructRefValue) value ).getName( );
	}

	/**
	 * Resolves a structure reference. Look up the name in the report design. If
	 * the target is found, replace the structure name with the cached
	 * structure.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            the definition of the structure ref property
	 * @param ref
	 *            the structure reference
	 */

	public void resolve( ReportDesign design, PropertyDefn defn,
			StructRefValue ref )
	{
		if ( ref.isResolved( ) )
			return;
		StructureDefn targetDefn = (StructureDefn) defn.getStructDefn( );
		Structure target = getStructure( design, targetDefn, ref.getName( ) );
		if ( target != null )
			ref.resolve( target );
	}
}