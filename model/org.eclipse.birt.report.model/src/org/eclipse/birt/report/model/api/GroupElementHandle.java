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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * This class provides services to deal with a group of elements. It is mostly
 * useful for some multiple-selection cases, elements of the same type(or share
 * the same base type) can be handled as a whole. User can perform some
 * operations on the collection of elements.
 * <p>
 * For our ease-of-use purpose, we support multiple selections across type.
 * Given a collection of elements, user can ask for their common
 * properties(including user property definitions). Also, return a list of
 * values that are identical for all items. Finally, return an indication of
 * whether all elements are of the same type.
 * <p>
 * This class also supports a collection of elements contains some objects that
 * are not a <code>DesignElementHandle</code> or its subclass. For this case,
 * <code>getCommonProperties</code> always returns an empty list.
 * <p>
 * For BIRT UI usage, the attributes view will go blank if the providing
 * elements are not of the same type, the property sheet will show the common
 * properties(including user property definitions).
 * <p>
 * This handle is mutable, it can be kept. The query results changed as the
 * given elements themselves changed.
 * <p>
 * Note that the Model special handling of the case where all elements are the
 * same type: in this case, by definition, all BIRT-defined properties are the
 * same. (User-defined properties may differ.)
 */

public class GroupElementHandle implements IGroupElementModel
{

	/**
	 * The design provides overall information about the design, especially the
	 * command stack.
	 */

	protected final ReportDesign design;

	/**
	 * List of handles to design elements.
	 */

	protected List elements = null;

	/**
	 * Constructs a handle to deal with a list of report elements. The contents
	 * of the given list should be design element handles.
	 * 
	 * @param designHandle
	 *            the report design
	 * @param elements
	 *            a list of handles of design elements. If a item is not
	 *            <code>DesignElementHandle</code>, it is ignored.
	 * @see DesignElementHandle
	 */

	public GroupElementHandle( ReportDesignHandle designHandle, List elements )
	{
		this.design = designHandle.getDesign( );
		assert elements != null;

		this.elements = elements;
	}

	/**
	 * Returns the list that contains the group of design elements. Contents of
	 * it is <code>DesignElementHandle</code>
	 * 
	 * @return the list that contains the group of design elements.
	 */

	public List getElements( )
	{
		return this.elements;
	}

	/**
	 * Finds common properties from two set of property definitions. Two
	 * property definitions are considered identical if they are same instances
	 * or they are equal.
	 * 
	 * @param list1
	 *            storing a list of <code>PropertyDefn</code>
	 * @param list2
	 *            storing a list of <code>PropertyDefn</code>
	 * @return A set containing all the common properties from the two lists.
	 */

	private List findInCommon( List list1, List list2 )
	{
		List retList = new ArrayList( );

		for ( Iterator iter = list1.iterator( ); iter.hasNext( ); )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );
			if ( list2.contains( propDefn ) )
				retList.add( propDefn );
		}

		return retList;
	}

	/**
	 * Indicates that if the given elements are of the same definition. Elements
	 * are considered of same type if their element definitions are identical.
	 * <p>
	 * If elements have different definitions. Even the same element type, the
	 * return value is <code>false</code>. For example, if the list contains
	 * an <code>OdaDataSource</code> and a <code>OdaDataSource</code>, this
	 * method returns <code>false</code>.
	 * 
	 * @return <code>true</code> if the given elements are of the same type;
	 *         return <code>false</code> if elements are of different element
	 *         types, or the given list is empty, or the list contains any
	 *         object that is not an instance of
	 *         <code>DesignElementHandle</code>.
	 */

	public boolean isSameType( )
	{
		if ( elements.size( ) == 0 )
			return false;

		IElementDefn baseDefn = null;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			Object item = elements.get( i );
			if ( !( item instanceof DesignElementHandle ) )
				return false;

			IElementDefn elemDefn = ( (DesignElementHandle) item ).getDefn( );

			if ( baseDefn == null )
				baseDefn = elemDefn;

			if ( elemDefn != baseDefn )
				return false;
		}

		return true;
	}

	/**
	 * Returns the common properties shared by the given group of
	 * elements(including user properties). Contents of the list is element
	 * property definitions. If elements do not share any common property,
	 * return an empty list.
	 * 
	 * @return the common properties shared by the given group of elements. If
	 *         elements do not share any common property, or the given list is
	 *         empty, or the list contains any item that is not an instance of
	 *         <code>DesignElementHandle</code>, return an empty list.
	 */

	public final List getCommonProperties( )
	{
		List commonProps = Collections.EMPTY_LIST;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			Object item = elements.get( i );

			if ( !( item instanceof DesignElementHandle ) )
				return Collections.EMPTY_LIST;

			List elemProps = ( (DesignElementHandle) item ).getElement( )
					.getPropertyDefns( );

			if ( i == 0 )
				commonProps = elemProps;
			else
				commonProps = findInCommon( commonProps, elemProps );

			if ( commonProps.isEmpty( ) )
				return Collections.EMPTY_LIST;
		}

		return commonProps;
	}

	/**
	 * Returns the report design.
	 * 
	 * @return the report design
	 */

	public ReportDesign getDesign( )
	{
		return design;
	}

	/**
	 * Returns the handle of report design.
	 * 
	 * @return the handle of report design
	 */

	public ReportDesignHandle getDesignHandle( )
	{
		return (ReportDesignHandle) design.getHandle( design );
	}

	/**
	 * Returns an iterator over the common properties. Contents of the iterator
	 * are handles to the common properties, type of them is
	 * <code>GroupPropertyHandle</code>. Note: remove is not support for the
	 * iterator.
	 * 
	 * @return an iterator over the common properties. Contents of the iterator
	 *         are handles to the common properties, type of them is
	 *         <code>GroupPropertyHandle</code>
	 */

	public Iterator propertyIterator( )
	{

		return new GroupPropertyIterator( getCommonProperties( ) );
	}

	/**
	 * Returns an iterator over the common properties that are visible. Contents
	 * of the iterator are handles to the common properties, type of them is
	 * <code>GroupPropertyHandle</code>. Note: remove is not support for the
	 * iterator.
	 * 
	 * @return an iterator over the common properties. Contents of the iterator
	 *         are handles to the common properties, type of them is
	 *         <code>GroupPropertyHandle</code>
	 */

	public Iterator visiblePropertyIterator( )
	{
		List list = getCommonProperties( );
		final List visibleList = new ArrayList( );

		for ( int i = 0; i < list.size( ); i++ )
		{
			IElementPropertyDefn propDefn = (IElementPropertyDefn) list.get( i );
			if ( isPropertyVisible( propDefn.getName( ) ) )
				visibleList.add( propDefn );
		}

		return new GroupPropertyIterator( visibleList );
	}

	/**
	 * Checks whether a property is visible in the property sheet. The visible
	 * property is visible in all <code>elements</code>.
	 * 
	 * @param propName
	 *            the property name
	 * @return <code>true</code> if it is visible. Otherwise
	 *         <code>false</code>.
	 */

	protected boolean isPropertyVisible( String propName )
	{
		boolean isVisible = true;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			IElementDefn elementDefn = ( (DesignElementHandle) elements.get( i ) )
					.getDefn( );
			if ( !elementDefn.isPropertyVisible( propName ) )
			{
				isVisible = false;
				break;
			}
		}

		return isVisible;
	}

	/**
	 * Checks whether a property is read-only in the property sheet. The visible
	 * property is read-only in all <code>elements</code>.
	 * 
	 * @param propName
	 *            the property name
	 * @return <code>true</code> if it is read-only. Otherwise
	 *         <code>false</code>.
	 */

	protected boolean isPropertyReadOnly( String propName )
	{
		boolean isReadOnly = false;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			IElementDefn elementDefn = ( (DesignElementHandle) elements.get( i ) )
					.getDefn( );
			if ( elementDefn.isPropertyReadOnly( propName ) )
			{
				isReadOnly = true;
				break;
			}
		}

		return isReadOnly;
	}

	/**
	 * If property is shared by the group of elements, return the corresponding
	 * <code>GroupPropertyHandle</code>, otherwise, return <code>null</code>.
	 * 
	 * @param propName
	 *            name of the property needs to be handled.
	 * @return If the property is a common property among the elements, return
	 *         the corresponding <code>GroupPropertyHandle</code>; Otherwise
	 *         return <code>null</code>.
	 */

	public final GroupPropertyHandle getPropertyHandle( String propName )
	{
		List commProps = this.getCommonProperties( );
		for ( int i = 0; i < commProps.size( ); i++ )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) commProps
					.get( i );
			if ( propDefn.getName( ).equalsIgnoreCase( propName ) )
			{
				return new GroupPropertyHandle( this, propDefn );
			}
		}

		return null;
	}

	/**
	 * If the given property is a common property, value will be returned as a
	 * string if all values within the group of elements are equal. If the
	 * property is not a common property, return <code>null</code>.
	 * 
	 * @param propName
	 *            name of the property.
	 * @return the value as a string if the property is a common property and
	 *         all the elements have the same value. Return null if the property
	 *         is not a common property or elements have different values for
	 *         this property.
	 * @see GroupPropertyHandle#getStringValue()
	 */

	public String getStringProperty( String propName )
	{
		GroupPropertyHandle propHandle = getPropertyHandle( propName );
		if ( propHandle == null )
			return null;

		return propHandle.getStringValue( );
	}

	/**
	 * Indicates whether the group of element share the same value for this
	 * property.
	 * <p>
	 * If all element has a <code>null</code> value for this property, it is
	 * considered that they share the same value.
	 * 
	 * @param propName
	 *            name of the property.
	 * @return <code>true</code> if the group of element share the same value.
	 *         Return <code>false</code> if the property is not a common
	 *         property or elements have different values for this property.
	 */

	public final boolean shareSameValue( String propName )
	{
		GroupPropertyHandle propHandle = getPropertyHandle( propName );
		if ( propHandle == null )
			return false;

		return propHandle.shareSameValue( );
	}

	/**
	 * Set the value of a property on the given collection of elements. If the
	 * property provided is not a common property then this method simply
	 * return; Otherwise, the value will be set on the group of elements.
	 * 
	 * @param propName
	 *            name of the property.
	 * @param value
	 *            value needs to set.
	 * @throws SemanticException
	 *             if the value is invalid for the property, or the property is
	 *             undefined on the elements.
	 * @see GroupPropertyHandle#setValue(Object)
	 */

	public void setProperty( String propName, Object value )
			throws SemanticException
	{
		GroupPropertyHandle propHandle = getPropertyHandle( propName );
		if ( propHandle == null )
			return;

		propHandle.setValue( value );
	}

	/**
	 * Clears the value of a property on the given collection of elements.
	 * Clearing a property removes any value set for the property on this
	 * element. After this, the element will now inherit the property from its
	 * parent element, style, or from the default value for the property.
	 * <p>
	 * If the property provided is not a common property then this method simply
	 * return, else, the value will be cleared on the group of elements.
	 * 
	 * @param propName
	 *            the name of the property to clear.
	 * @throws SemanticException
	 *             if the property is not defined on this element
	 */

	public void clearProperty( String propName ) throws SemanticException
	{
		setProperty( propName, null );
	}

	/**
	 * Set the value of a property to a string . If the property provided is not
	 * a common property then this method simply return; Else, the string value
	 * will be set on the group of element.
	 * 
	 * @param propName
	 *            name of the property.
	 * @param value
	 *            value needs to set.
	 * @throws SemanticException
	 *             if the value is invalid for the property, or the property is
	 *             undefined on the elements.
	 */

	public void setStringProperty( String propName, String value )
			throws SemanticException
	{
		setProperty( propName, value );
	}

	/**
	 * Checks whether the <code>element</code> is a member of
	 * <code>GroupElementHandle</code>.
	 * 
	 * @param element
	 *            the element to check
	 * @return <code>true</code> if the element is in the list, otherwise
	 *         <code>false</code>.
	 */

	protected boolean isInGroup( DesignElementHandle element )
	{
		return elements.contains( element );
	}

	/**
	 * An iterator over the properties defined for elements that in the group
	 * element.
	 */

	class GroupPropertyIterator implements Iterator
	{

		Iterator propIterator;

		GroupPropertyIterator( List list )
		{
			propIterator = list.iterator( );
		}

		public void remove( )
		{
			// not support.
		}

		public boolean hasNext( )
		{
			if ( propIterator == null )
				return false;

			return propIterator.hasNext( );
		}

		public Object next( )
		{
			if ( !propIterator.hasNext( ) )
				return null;

			return new GroupPropertyHandle( GroupElementHandle.this,
					(ElementPropertyDefn) propIterator.next( ) );
		}
	};
}