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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Base class for property structures. Implements the two "boiler-plate" methods
 * from the IStructure interface.
 *  
 */

public abstract class Structure implements IStructure
{

	/**
	 * Gets a copy of this structure.
	 * 
	 * @return the copied structure.
	 *  
	 */

	public final IStructure copy( )
	{
		try
		{
			return (IStructure) clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}
		return null;
	}

	/**
	 * Gets the structure definition by the name of this structure.
	 * 
	 * @return structure definition.
	 */

	public IStructureDefn getDefn( )
	{
		return MetaDataDictionary.getInstance( )
				.getStructure( getStructName( ) );
	}

	/**
	 * Gets the object definition of this structure.
	 * 
	 * @return the structure definition returned.
	 *  
	 */

	public IObjectDefn getObjectDefn( )
	{
		return MetaDataDictionary.getInstance( )
				.getStructure( getStructName( ) );
	}

	/**
	 * Gets the value of the member. If the value has not been set, the default
	 * value for the member will be returned.
	 * 
	 * @param design
	 *            the report design
	 * @param memberName
	 *            the member name
	 * @return the member is defined on the structure and the value is set,
	 *         otherwise null.
	 */

	public final Object getProperty( ReportDesign design, String memberName )
	{
		PropertyDefn prop = (PropertyDefn) getDefn( ).getMember( memberName );
		if ( prop == null )
			return null;

		return getProperty( design, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#getProperty(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public Object getProperty( ReportDesign design, PropertyDefn propDefn )
	{
		assert propDefn != null;
		Object value = getLocalProperty( design, propDefn );
		if ( value == null )
			return propDefn.getDefault( );

		return value;
	}

	/**
	 * Gets the locale value of a property.
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @param propDefn
	 *            definition of the property to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 */

	public Object getLocalProperty( ReportDesign design, PropertyDefn propDefn )
	{
		if ( propDefn.isIntrinsic( ) )
			return getIntrinsicProperty( propDefn.getName( ) );
		return null;
	}

	/**
	 * Sets the value of the member.
	 * 
	 * @param propName
	 *            the member name to set
	 * @param value
	 *            the value to set
	 */

	public final void setProperty( String propName, Object value )
	{
		PropertyDefn prop = (PropertyDefn) getDefn( ).getMember( propName );
		if ( prop == null )
			return;

		setProperty( prop, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IPropertySet#setProperty(org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public void setProperty( PropertyDefn prop, Object value )
	{
		if ( prop.isIntrinsic( ) )
			setIntrinsicProperty( prop.getName( ), value );
	}

	/**
	 * Returns the value of a structure property represented as a member
	 * variable.
	 * 
	 * @param propName
	 *            name of the property
	 * @return the value of the property, or null if the property is not set
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		assert false;
		return null;
	}

	/**
	 * Sets the value of of a structure property represented as a member
	 * variable.
	 * 
	 * @param name
	 *            the name of the property to set
	 * @param value
	 *            the property value
	 */

	protected void setIntrinsicProperty( String name, Object value )
	{
		assert false;
	}

	/**
	 * Validate whether this structure is valid. The derived class should
	 * override this method if semantic check is needed. The each error is the
	 * instance of <code>PropertyValueException</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the element contains this structure
	 * @return the semantic error list
	 */

	public List validate( ReportDesign design, DesignElement element )
	{
		return new ArrayList( );
	}

	/**
	 * Gets the specific handle of this structure. This structure must be in the
	 * element's structure list. The structure handle is transient because the
	 * position in the structure list is kept. The position changes if any
	 * structure is added, or dropped. So this handle should not be kept.
	 * 
	 * @param valueHandle
	 *            the value handle of this structure list property this
	 *            structure is in
	 * @param index
	 *            the position of this structure in structure list
	 * @return the handle of this structure. If this structure is not in the
	 *         <code>valueHandle</code>,<code>null</code> is returned.
	 */

	public StructureHandle getHandle( SimpleValueHandle valueHandle, int index )
	{
		if ( valueHandle == null || valueHandle.getListValue( ) == null )
			return null;

		if ( index < 0 || index >= valueHandle.getListValue( ).size( ) )
			return null;

		return handle( valueHandle, index );
	}

	/**
	 * Creates the specific handle of this structure. This handle is always
	 * created.
	 * 
	 * @param valueHandle
	 *            the value handle of this structure list property this
	 *            structure is in
	 * @param index
	 *            the position of this structure in structure list
	 * @return the handle of this structure.
	 */

	protected abstract StructureHandle handle( SimpleValueHandle valueHandle,
			int index );

	/**
	 * Gets the specific handle of this structure. This structure must be in the
	 * element's structure list. The structure handle is transient because the
	 * position in the structure list is kept. The position changes if any
	 * structure is added, or dropped. So this handle should not be kept.
	 * 
	 * @param valueHandle
	 *            the value handle of this structure list property this
	 *            structure is in
	 * @return the handle of this structure. If this structure is not in the
	 *         <code>valueHandle</code>,<code>null</code> is returned.
	 */

	public StructureHandle getHandle( SimpleValueHandle valueHandle )
	{
		if ( valueHandle == null || valueHandle.getListValue( ) == null )
			return null;

		int posn = valueHandle.getListValue( ).indexOf( this );
		if ( posn == -1 )
			return null;

		return handle( valueHandle, posn );
	}

}