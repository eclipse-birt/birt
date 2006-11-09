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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Abstract base class that represents a handle for the value to either a
 * property or a structure member.
 * 
 */

public abstract class SimpleValueHandle extends ValueHandle
{

	/**
	 * Constructs a handle with the given handle to an design element.
	 * 
	 * @param element
	 *            a handle to a report element
	 */

	public SimpleValueHandle( DesignElementHandle element )
	{
		super( element );
	}

	/**
	 * Gets the generic property definition. Its a property definition for an
	 * element or a member definition for a structure.
	 * 
	 * @return the value definition.
	 */

	public abstract IPropertyDefn getDefn( );

	/**
	 * Gets the value of the property as a generic object. Use the specialized
	 * methods to get the value as a particular type.
	 * 
	 * @return The value of the property as a generic object.
	 * @see #getStringValue( )
	 * @see #getIntValue( )
	 * @see #getFloatValue( )
	 * @see #getNumberValue( )
	 */

	public abstract Object getValue( );

	/**
	 * Gets the value as an integer.
	 * 
	 * @return The value as an integer. Returns 0 if the value cannot be
	 *         converted to an integer.
	 */

	public int getIntValue( )
	{
		return ( (PropertyDefn) getDefn( ) ).getIntValue( getModule( ),
				getValue( ) );
	}

	/**
	 * Gets the value as a string.
	 * 
	 * @return The value as a string.
	 */

	public String getStringValue( )
	{
		return ( (PropertyDefn) getDefn( ) ).getStringValue( getModule( ),
				getValue( ) );
	}

	/**
	 * Gets the value as a double.
	 * 
	 * @return The value as a double. Returns 0 if the value cannot be converted
	 *         to a double.
	 */

	public double getFloatValue( )
	{
		return ( (PropertyDefn) getDefn( ) ).getFloatValue( getModule( ),
				getValue( ) );
	}

	/**
	 * Gets the value as a number (BigDecimal).
	 * 
	 * @return The value as a number. Returns null if the value cannot be
	 *         converted to a number.
	 */

	public BigDecimal getNumberValue( )
	{
		return ( (PropertyDefn) getDefn( ) ).getNumberValue( getModule( ),
				getValue( ) );
	}

	/**
	 * Gets the value as a structure list.
	 * 
	 * @return The value as a list. Returns null if the value cannot be
	 *         converted to a list.
	 */

	public ArrayList getListValue( )
	{
		Object value = getValue( );
		if ( value instanceof ArrayList )
			return (ArrayList) value;
		return null;
	}

	/**
	 * gets the localized value of the property.
	 * 
	 * @return the localized value
	 */
	public String getDisplayValue( )
	{
		return ( (PropertyDefn) getDefn( ) ).getDisplayValue( getModule( ),
				getValue( ) );
	}

	/**
	 * Returns the the nth entry in a list property or member. Use this method
	 * for properties that contain a list of structures. The index must be valid
	 * for the list.
	 * 
	 * @param n
	 *            The list index.
	 * @return A handle to the structure at the given index.
	 */

	public StructureHandle getAt( int n )
	{
		if ( isList( ) )
		{
			MemberRef structRef = new CachedMemberRef( getReference( ), n );
			Structure struct = structRef.getStructure( getModule( ),
					getElement( ) );

			if ( struct != null )
				return struct.getHandle( this, n );
		}

		return null;
	}

	/**
	 * Returns the numeric code for the type of this property or member. The
	 * types are defined in the <code>PropertyType</code> class.
	 * 
	 * @return The property type code.
	 * @see org.eclipse.birt.report.model.metadata.PropertyType
	 */

	public int getTypeCode( )
	{
		return getDefn( ).getTypeCode( );
	}

	/**
	 * Returns an iterator over the values in a list property, or
	 * <code>null</code> if the property is not a list property. The iterator
	 * returns a <code>StructureHandle</code> for each entry in the list. For
	 * highlight rules, the iterator returns a list of
	 * <code>HighlightRuleHandle</code>s.
	 * 
	 * @return An iterator over the values in a list property.
	 * @see StructureHandle
	 * @see StructureIterator
	 */

	public Iterator iterator( )
	{
		if ( isList( ) )
		{
			return new StructureIterator( this );
		}

		return Collections.EMPTY_LIST.iterator( );
	}

	/**
	 * Sets the value of the property or member to the given integer. It can
	 * also be used to set dimensions but dimensions are better set using a
	 * double.
	 * 
	 * @param value
	 *            The value to set.
	 * @throws SemanticException
	 *             If the property value cannot be converted from an integer, or
	 *             if the value of a choice is incorrect.
	 */

	public void setIntValue( int value ) throws SemanticException
	{
		setValue( new Integer( value ) );
	}

	/**
	 * Sets the value of the property or member to the given integer. Use this
	 * for properties such as expressions, labels, HTML, or XML. Also use it to
	 * set the value of a choice using the internal string name of the choice.
	 * Use it to set the value of a dimension when using specified units, such
	 * as "10pt".
	 * 
	 * @param value
	 *            The value to set.
	 * @throws SemanticException
	 *             If the value of a choice or other property is incorrect.
	 */

	public void setStringValue( String value ) throws SemanticException
	{
		setValue( value );
	}

	/**
	 * Sets the value of the property or member to the given double. Used
	 * primarily for dimension properties. The units of the dimension are
	 * assumed to be application units.
	 * 
	 * @param value
	 *            The value to set.
	 * @throws SemanticException
	 *             If the property value cannot be converted from a double.
	 */

	public void setFloatValue( double value ) throws SemanticException
	{
		setValue( new Double( value ) );
	}

	/**
	 * Sets the value of the property or member to the given number.
	 * 
	 * @param value
	 *            The value to set.
	 * @throws SemanticException
	 *             If the property value cannot be converted from a number.
	 */

	public void setNumberValue( BigDecimal value ) throws SemanticException
	{
		setValue( value );
	}

	/**
	 * Clears the value of the property or member.
	 * 
	 * @throws SemanticException
	 *             If the value cannot be cleared.
	 */

	public void clearValue( ) throws SemanticException
	{
		setValue( null );
	}

	/**
	 * Sets the value of a property or member to the object given. If the object
	 * is <code>null</code>, then the value is cleared.
	 * 
	 * @param value
	 *            The new value.
	 * @throws SemanticException
	 *             If the value is not valid for the property or member.
	 * @see #setIntValue
	 * @see #setStringValue
	 * @see #setFloatValue
	 * @see #setNumberValue
	 * @see #clearValue
	 */

	public abstract void setValue( Object value ) throws SemanticException;

	/**
	 * Removes an item from a list property or member. The handle must be
	 * working on a list property or member.
	 * 
	 * @param posn
	 *            The position of the item to remove.
	 * @throws PropertyValueException
	 *             If the property is not a list property.
	 * @throws IndexOutOfBoundsException
	 *             if the given <code>posn</code> is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( int posn ) throws PropertyValueException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.removeItem( getReference( ), posn );
	}

	/**
	 * Removes an item from a list property or member. The handle must be
	 * working on a list property or member.
	 * 
	 * @param item
	 *            the item to remove
	 * @throws PropertyValueException
	 *             If the property is not a list property, or if the given item
	 *             is not contained in the list.
	 */

	public void removeItem( IStructure item ) throws PropertyValueException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.removeItem( getReference( ), item );
	}

	/**
	 * Removes all the items in the list from a list property or member. The
	 * handle must be working on a list property or member. Each one in the list
	 * is instance of <code>StructureHandle</code>
	 * 
	 * @param items
	 *            the item list to remove
	 * @throws PropertyValueException
	 *             If the property or the member is not a list type, or if any
	 *             item in the list is not found
	 */

	public void removeItems( List items ) throws PropertyValueException
	{
		if ( items == null )
			return;
		ActivityStack stack = getModule( ).getActivityStack( );
		List newItems = new ArrayList( );
		for ( int i = 0; i < items.size( ); i++ )
		{
			newItems.add( ( (StructureHandle) items.get( i ) ).getStructure( ) );
		}
		stack.startTrans( );
		try
		{
			for ( int i = 0; i < newItems.size( ); i++ )
			{
				IStructure item = (IStructure) newItems.get( i );
				removeItem( item );
			}
		}
		catch ( PropertyValueException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );
	}

	/**
	 * Replaces an old structure with a new one for the this property or member.
	 * The handle must be working on a list property or member.
	 * 
	 * @param oldItem
	 *            the old item to be replaced.
	 * @param newItem
	 *            the new item.
	 * 
	 * @throws SemanticException
	 *             if the property/member does not contain the list value or the
	 *             new structure is invalid or the old structure is not
	 *             contained in the list.
	 */

	public void replaceItem( IStructure oldItem, IStructure newItem )
			throws SemanticException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.replaceItem( getReference( ), oldItem, newItem );
	}

	/**
	 * Adds an item to the end of a list property or member. The handle must be
	 * working on a list property or member.
	 * 
	 * @param item
	 *            The new item to add.
	 * @return a handle to the newly added structure��return null if the item is
	 *         null.
	 * @throws SemanticException
	 *             If the property is not a list property, or if the the value
	 *             of the item is incorrect.
	 */

	public StructureHandle addItem( IStructure item ) throws SemanticException
	{
		if ( item == null )
			return null;

		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.addItem( getReference( ), item );

		return ( (Structure) item ).getHandle( this );
	}

	/**
	 * Inserts a new item into a list property or member at the given position.
	 * The handle must be working on a list property or member.
	 * 
	 * @param item
	 *            The new item to insert.
	 * @param posn
	 *            The insert position.
	 * @return a handle to the newly inserted structure, return null if the item
	 *         is null.
	 * @throws SemanticException
	 *             If the property is not a list property, or if the the value
	 *             of the item is incorrect.
	 * @throws IndexOutOfBoundsException
	 *             if the given <code>posn</code> is out of range
	 *             <code>(index &lt; 0 || index &gt; list.size())</code>.
	 */

	public StructureHandle insertItem( IStructure item, int posn )
			throws SemanticException
	{
		if ( item == null )
			return null;

		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.insertItem( this.getReference( ), item, posn );

		return ( (Structure) item ).getHandle( this );
	}

	/**
	 * Moves an item within a list property or member. The handle must be
	 * working on a list property or member.
	 * 
	 * @param from
	 *            The current position of the item to move
	 * @param to
	 *            The new position of the item to move.
	 * @throws PropertyValueException
	 *             If the property is not a list property.
	 * @throws IndexOutOfBoundsException
	 *             if the given from or to index is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void moveItem( int from, int to ) throws PropertyValueException
	{
		PropertyCommand cmd = new PropertyCommand( getModule( ), getElement( ) );
		cmd.moveItem( getReference( ), from, to );
	}

	/**
	 * Returns the array of choices that are defined for this property or
	 * member.
	 * 
	 * @return an array containing choices of this property. Return
	 *         <code>null</code>, if this property has no choice.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice
	 */

	public IChoice[] getChoices( )
	{
		IPropertyDefn propDefn = getDefn( );

		return propDefn.getChoices( ) == null ? null : propDefn.getChoices( )
				.getChoices( );
	}

	/**
	 * Indicate if this handle is working on a list property.
	 * 
	 * @return <code>true</code> if the handle is working on a list property,
	 *         otherwise return <code>false</code>
	 */

	protected boolean isList( )
	{
		return getDefn( ).getTypeCode( ) == IPropertyType.STRUCT_TYPE
				&& getDefn( ).isList( );
	}

	/**
	 * Gets the default unit of the property.
	 * 
	 * @return the default unit if the property is dimension type, otherwise
	 *         empty string
	 */

	public String getDefaultUnit( )
	{
		if ( getTypeCode( ) == IPropertyType.DIMENSION_TYPE )
		{
			PropertyDefn defn = (PropertyDefn) getDefn( );
			String unit = defn.getDefaultUnit( );
			if ( !StringUtil.isBlank( unit ) )
				return unit;
			unit = getModule( ).getUnits( );
			if ( !StringUtil.isBlank( unit ) )
				return unit;
			if ( getModule( ).getSession( ) != null )
				return getModule( ).getSession( ).getUnits( );
		}
		return DimensionValue.DEFAULT_UNIT;
	}

	/**
	 * Checks whether a value is visible in the property sheet.
	 * 
	 * @return <code>true</code> if it is visible. Otherwise
	 *         <code>false</code>.
	 */

	abstract public boolean isVisible( );

	/**
	 * Checks whether a value is read-only in the property sheet.
	 * 
	 * @return <code>true</code> if it is read-only. Otherwise
	 *         <code>false</code>.
	 */

	abstract public boolean isReadOnly( );
}