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

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IPropertyDefn;

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
		return getElement( ).getProperty( getDesign( ), propDefn );
	}

	// Implementation of abstract method defined in base class.

	public void setValue( Object value ) throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getDesign( ), getElement( ) );
		cmd.setProperty( propDefn, value );
	}

	// Implementation of abstract method defined in base class.

	public MemberRef getReference( )
	{
		return new MemberRef( propDefn );
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
		Object value = getElement( ).getLocalProperty( getDesign( ), propDefn );
		return ( value != null );
	}
}