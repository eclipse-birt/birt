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
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

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

		if ( value instanceof ReferenceValue )
			return ReferenceValueUtil.needTheNamespacePrefix(
					(ReferenceValue) value, getElementHandle( ).getModule( ) );

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
		// TODO: getModule() here should be getRoot()
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
		if ( propDefn.getTypeCode( ) != PropertyType.ELEMENT_REF_TYPE
				&& propDefn.getSubTypeCode( ) != PropertyType.ELEMENT_REF_TYPE )
			return Collections.EMPTY_LIST;

		List list = new ArrayList( );

		ElementDefn elementDefn = (ElementDefn) propDefn.getTargetElementType( );
		assert elementDefn != null;

		ModuleHandle moduleHandle = ( (ModuleHandle) getModule( ).getHandle(
				getModule( ) ) );

		if ( ReportDesignConstants.DATA_SET_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getVisibleDataSets( );

		else if ( ReportDesignConstants.DATA_SOURCE_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getVisibleDataSources( );

		else if ( ReportDesignConstants.STYLE_ELEMENT.equals( elementDefn
				.getName( ) ) )
			return ( (ReportDesignHandle) moduleHandle ).getAllStyles( );
		else if ( ReportDesignConstants.THEME_ITEM.equals( elementDefn
				.getName( ) ) )
			return moduleHandle.getVisibleThemes( IAccessControl.DIRECTLY_INCLUDED_LEVEL );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isReadOnly()
	 */

	public boolean isReadOnly( )
	{
		boolean isReadOnly = false;

		Module root = getElementHandle( ).getModule( );
		assert root != null;
		if ( root.isReadOnly( ) )
			isReadOnly = true;
		else
		{
			switch ( propDefn.getValueType( ) )
			{
				case PropertyDefn.SYSTEM_PROPERTY :
				case PropertyDefn.EXTENSION_PROPERTY :
				case PropertyDefn.ODA_PROPERTY :
					IElementDefn elementDefn = getElementHandle( ).getDefn( );
					if ( elementDefn.isPropertyReadOnly( propDefn.getName( ) ) )
						isReadOnly = true;
					break;
				case PropertyDefn.EXTENSION_MODEL_PROPERTY :
					if ( propDefn.isReadOnly( ) )
						isReadOnly = true;
					break;
			}
		}

		if ( isReadOnly )
			return true;

		return isReadOnlyInContext( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#isVisible()
	 */

	public boolean isVisible( )
	{
		boolean isVisible = true;
		switch ( propDefn.getValueType( ) )
		{
			case PropertyDefn.SYSTEM_PROPERTY :
			case PropertyDefn.EXTENSION_PROPERTY :
			case PropertyDefn.ODA_PROPERTY :
				IElementDefn elementDefn = getElementHandle( ).getDefn( );
				if ( !elementDefn.isPropertyVisible( propDefn.getName( ) ) )
					isVisible = false;
				break;
			case PropertyDefn.EXTENSION_MODEL_PROPERTY :
				if ( !propDefn.isVisible( ) )
					isVisible = false;
				break;
		}

		return isVisible;
	}

	/**
	 * Adds an item to the end of a list property. The handle must be working on
	 * a list property.
	 * 
	 * @param item
	 *            The new item to add.
	 * @throws SemanticException
	 *             If the property is not a list property, or if the the value
	 *             of the item is incorrect.
	 */

	public void addItem( Object item ) throws SemanticException
	{
		if ( item == null )
			return;
		if ( item instanceof IStructure )
			super.addItem( (IStructure) item );
		else
		{
			PropertyCommand cmd = new PropertyCommand( getModule( ),
					getElement( ) );
			cmd.addItem( propDefn, item );
		}
	}

	/**
	 * Removes an item from a list property. The handle must be working on a
	 * list property.
	 * 
	 * @param item
	 *            The new item to add.
	 * @throws SemanticException
	 *             If the property is not a list property, or if the the value
	 *             of the item does not exist in the element.
	 */

	public void removeItem( Object item ) throws SemanticException
	{
		if ( item == null )
			return;
		if ( item instanceof IStructure )
			super.removeItem( (IStructure) item );
		else
		{
			PropertyCommand cmd = new PropertyCommand( getModule( ),
					getElement( ) );
			cmd.removeItem( propDefn, item );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#removeItem(int)
	 */
	public void removeItem( int posn ) throws PropertyValueException
	{
		if ( propDefn.getTypeCode( ) == PropertyType.LIST_TYPE )
		{
			PropertyCommand cmd = new PropertyCommand( getModule( ) , getElement( ));
			cmd.removeItem( propDefn, posn );
		}
		else
			super.removeItem( posn );
	}	

	/**
	 * Returns whether the property value is read-only in the report context.
	 * 
	 * @return <code>true</code> if the value is read-only. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isReadOnlyInContext( )
	{
		DesignElementHandle element = getElementHandle( );
		if ( element instanceof MasterPageHandle )
		{
			MasterPage masterPage = (MasterPage) element.getElement( );
			if ( !masterPage.isCustomType( getModule( ) ) )
			{
				String propName = propDefn.getName( );
				if ( MasterPage.HEIGHT_PROP.equals( propName )
						|| MasterPage.WIDTH_PROP.equals( propName ) )
					return true;
			}
		}

		return false;
	}
}