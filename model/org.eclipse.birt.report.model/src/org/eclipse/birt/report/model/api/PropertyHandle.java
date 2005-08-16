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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * A handle for working with a top-level property of an element.
 * 
 * @see org.eclipse.birt.report.model.metadata.PropertyDefn
 * @see org.eclipse.birt.report.model.metadata.PropertyType
 */

public class PropertyHandle extends SimpleValueHandle
{

	/**
	 * Definition of the property.
	 */

	protected ElementPropertyDefn propDefn;

	/**
	 * Constructs the handle for a top-level property with the given element
	 * handle and property name.
	 * 
	 * @param element
	 *            a handle to a report element
	 * @param propName
	 *            the name of the property
	 */

	public PropertyHandle( DesignElementHandle element, String propName )
	{
		super( element );
		propDefn = element.getElement( ).getPropertyDefn( propName );
	}

	/**
	 * Constructs the handle for a top-level property with the given element
	 * handle and the definition of the property.
	 * 
	 * @param element
	 *            a handle to a report element
	 * @param prop
	 *            the definition of the property.
	 */

	public PropertyHandle( DesignElementHandle element, ElementPropertyDefn prop )
	{
		super( element );
		propDefn = prop;
	}

	// Implementation of abstract method defined in base class.

	public IElementPropertyDefn getPropertyDefn( )
	{
		return propDefn;
	}

	// Implementation of abstract method defined in base class.

	public IPropertyDefn getDefn( )
	{
		return propDefn;
	}

	// Implementation of abstract method defined in base class.

	public Object getValue( )
	{
		Object value = getElement( ).getProperty( getModule( ), propDefn );

		if ( value instanceof ElementRefValue )
			value = ( (ElementRefValue) value ).getName( );
		if ( value instanceof StructRefValue )
			value = ( (StructRefValue) value ).getName( );
		return value;
	}

	// Implementation of abstract method defined in base class.

	public void setValue( Object value ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.setProperty( propDefn, value );
	}

	// Implementation of abstract method defined in base class.

	public MemberRef getReference( )
	{
		return new CachedMemberRef( propDefn );
	}

	/**
	 * Determines whether this property value is set for this element. It is set
	 * if it is defined on this element property or any of its parents, or in
	 * the element's private style property. It is considered unset if it is set
	 * on a shared style.
	 * 
	 * @return <code>true</code> if the value is set, <code>false</code> if
	 *         it is not set
	 */

	public boolean isSet( )
	{
		FactoryPropertyHandle handle = new FactoryPropertyHandle(
				elementHandle, propDefn );
		return handle.isSet( );
	}

	/**
	 * Determines whether this property value is set locally for this element.
	 * It is set if and only if it is defined on this element local property.
	 * 
	 * @return <code>true</code> if the local value is set, otherwise
	 *         <code>false</code>.
	 * 
	 */

	public boolean isLocal( )
	{
		Object value = getElement( ).getLocalProperty( getModule( ), propDefn );
		return ( value != null );
	}

	/**
	 * Returns true if the two property handle has the same element and the same
	 * property.
	 * 
	 * @param propertyHandle
	 *            the property handle
	 * @return true if the two property handles are same.
	 */
	public boolean equals( Object propertyHandle )
	{
		if ( !( propertyHandle instanceof PropertyHandle ) )
			return false;

		DesignElement element = ( (PropertyHandle) propertyHandle )
				.getElement( );
		IPropertyDefn propDefn = ( (PropertyHandle) propertyHandle ).getDefn( );

		return ( element == getElement( ) ) && ( propDefn == getDefn( ) );

	}

	/**
	 * returns the element reference value list if the property is element
	 * referenceable type.
	 * 
	 * @return list of the reference element value.
	 */

	public List getReferenceableElementList( )
	{
		if ( propDefn.getTypeCode( ) != PropertyType.ELEMENT_REF_TYPE )
			return Collections.EMPTY_LIST;

		List list = new ArrayList( );

		ElementDefn elementDefn = (ElementDefn) propDefn.getTargetElementType( );
		assert elementDefn != null;

		ModuleHandle moduleHandle = ( (ModuleHandle) getModule( )
				.getHandle( getModule( ) ) );

		if ( ReportDesignConstants.DATA_SET_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getDataSets( ).getContents( );

		else if ( ReportDesignConstants.DATA_SOURCE_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getDataSources( ).getContents( );

		else if ( ReportDesignConstants.STYLE_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getStyles( ).getContents( );

		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isReadOnly()
	 */

	public boolean isReadOnly( )
	{
		IElementDefn elementDefn = getElementHandle( ).getDefn( );
		return elementDefn.isPropertyReadOnly( propDefn.getName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isVisible()
	 */

	public boolean isVisible( )
	{
		IElementDefn elementDefn = getElementHandle( ).getDefn( );
		return elementDefn.isPropertyVisible( propDefn.getName( ) );
	}
}