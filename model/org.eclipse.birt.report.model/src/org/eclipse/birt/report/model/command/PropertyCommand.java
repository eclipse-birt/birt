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

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.birt.report.model.extension.IReportItem;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Sets the value of a property. Works with both system and user properties.
 * Works with normal and intrinsic properties.
 *  
 */

public class PropertyCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param design
	 *            the report design
	 * @param obj
	 *            the element to modify.
	 */

	public PropertyCommand( ReportDesign design, DesignElement obj )
	{
		super( design, obj );
	}

	/**
	 * Sets the value of a property.
	 * 
	 * @param propName
	 *            the internal name of the property to set.
	 * @param value
	 *            the new property value.
	 * @throws SemanticException
	 *             if the property is not found.
	 */

	public void setProperty( String propName, Object value )
			throws SemanticException
	{
		propName = StringUtil.trimString( propName );

		// Ensure that the property is defined.

		ElementPropertyDefn prop = element.getPropertyDefn( propName );
		if ( prop == null )
			throw new PropertyNameException( element, propName );
		setProperty( prop, value );
	}

	/**
	 * Sets the value of a property. If the mask of a property is "lock", throws
	 * one exception.
	 * <p>
	 * If the mask of this property has been set to "lock", no value will be
	 * set. To set the value of a property, the mask value must be "hide" or
	 * "change".
	 * 
	 * @param prop
	 *            definition of the property to set
	 * @param value
	 *            the new property value.
	 * @throws SemanticException
	 *             if the value is invalid or the property mask is "lock".
	 */

	public void setProperty( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{
		String mask = element.getPropertyMask( design, prop.getName( ) );
		if ( DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK
				.equalsIgnoreCase( mask ) )
		{
			throw new PropertyValueException( element, prop, value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED );
		}

		value = validateValue( prop, value );

		// Set the property.

		if ( prop.isIntrinsic( ) )
		{
			setIntrinsicProperty( prop, value );
		}
		else
		{
			doSetProperty( prop, value );
		}
	}

	/**
	 * Private method to set property.
	 * 
	 * @param prop
	 *            the definition of the property to set.
	 * @param value
	 *            the new property value.
	 * @throws ExtendedElementException
	 *             if the extension property is invalid
	 */

	private void doSetProperty( ElementPropertyDefn prop, Object value )
			throws ExtendedElementException
	{
		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		Object oldValue = element.getLocalProperty( design, prop );
		if ( oldValue == null && value == null )
			return;
		if ( oldValue != null && value != null && oldValue.equals( value ) )
			return;

		// The values differ. Make the change.

		if ( element instanceof ExtendedItem )
		{
			if ( ( (ExtendedItem) element ).isExtensionModelProperty( prop.getName( ) )
					|| ( (ExtendedItem) element )
							.isExtensionXMLType( prop.getName( ) ) )
			{

				IReportItem extElement = ( (ExtendedItem) element )
						.getExtendedElement( );
				assert extElement != null;
				extElement.checkProperty( prop.getName( ), value );
				extElement.setProperty( prop.getName( ), value );
				this.sendNotifcations( element, prop.getName( ) );
				return;
			}
		}

		assertExtendedElement( design, element, prop );

		PropertyRecord record = new PropertyRecord( element, prop, value );
		getActivityStack( ).execute( record );

	}

	/**
	 * Sends the notifications of extended element. This implementation uses
	 * <code>getEvent( )</code> to produce the notification, and sends the
	 * event to the element returned by <code>getTarget( )</code>.
	 * 
	 * @param target
	 *            the target element of the event
	 * @param propName
	 *            the property name changed
	 */

	private void sendNotifcations( DesignElement target, String propName )
	{
		NotificationEvent event = null;
		assert element instanceof ExtendedItem;

		IReportItem extElement = ( (ExtendedItem) element ).getExtendedElement( );
		assert extElement != null;

		if ( extElement.refreshPropertyDefinition( ) )

			event = new ExtensionPropertyDefinitionEvent( element );

		else
			event = new PropertyEvent( element, propName );

		// Include the sender if this is the original execution.
		// The sender is not sent for undo, redo because such actions are
		// triggered by the activity stack, not dialog or editor.

//		if ( state == DONE_STATE )
//			event.setSender( sender );

		// Broadcast the event to the target.

		element.broadcast( event );
	}

	/**
	 * Justifies whether the extended element is created if the UI invokes some
	 * operations to change the extension properties.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the extended item that holds the extended element
	 * @param prop
	 *            the extension property definition to change
	 */

	private void assertExtendedElement( ReportDesign design,
			DesignElement element, PropertyDefn prop )
	{
		if ( element instanceof ExtendedItem )
		{
			if ( prop.isExtended( ) )
				assert ( (ExtendedItem) element ).getExtendedElement( ) != null;
		}
	}

	/**
	 * Private method to validate the value of a property.
	 * 
	 * @param prop
	 *            definition of the property to validate
	 * @param value
	 *            the value to validate
	 * @return the value to store for the property
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	private Object validateValue( ElementPropertyDefn prop, Object value )
			throws PropertyValueException
	{
		// clear the property doesn't needs validation.

		if ( value == null )
			return null;

		try
		{
			return prop.validateValue( design, value );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( prop.getName( ) );
			throw ex;
		}
	}

	/**
	 * Sets the value of an intrinsic property.
	 * 
	 * @param prop
	 *            definition of the property to set
	 * @param value
	 *            the property value to set.
	 * @throws SemanticException
	 *             if failed to set property.
	 */

	private void setIntrinsicProperty( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{
		String propName = prop.getName( );

		if ( DesignElement.NAME_PROP.equals( propName ) )
		{
			String name = (String) value;
			NameCommand cmd = new NameCommand( design, element );
			cmd.setName( name );
		}
		else if ( DesignElement.EXTENDS_PROP.equals( propName ) )
		{
			ExtendsCommand cmd = new ExtendsCommand( design, element );
			if ( value == null )
				cmd.setExtendsName( null );
			else
				cmd.setExtendsName( ( (ElementRefValue) value ).getName( ) );
		}
		else if ( StyledElement.STYLE_PROP.equals( propName ) )
		{
			StyleCommand cmd = new StyleCommand( design, element );
			if ( value == null )
				cmd.setStyle( null );
			else
				cmd.setStyle( ( (ElementRefValue) value ).getName( ) );
		}
		else if ( ReportDesign.UNITS_PROP.equals( propName ) )
		{
			doSetProperty( prop, value );
		}
		else if ( ExtendedItem.EXTENSION_PROP.equals( propName ) )
		{
			doSetProperty( prop, value );
		}
		else
		{
			// Other intrinsics properties will be added here.

			assert false;
		}
	}

	/**
	 * Clears the value of a property.
	 * 
	 * @param propName
	 *            the name of the property to clear.
	 * @throws SemanticException
	 *             if failed to clear property.
	 */

	public void clearProperty( String propName ) throws SemanticException
	{
		setProperty( propName, null );
	}

	/**
	 * Sets the value of the member of a structure.
	 * 
	 * @param ref
	 *            reference to the member to set
	 * @param value
	 *            new value of the member
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	public void setMember( MemberRef ref, Object value )
			throws PropertyValueException
	{
		PropertyDefn memberDefn = ref.getMemberDefn( );
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		assert memberDefn != null;
		value = memberDefn.validateValue( design, value );

		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		Object oldValue = ref.getValue( design, element );
		if ( oldValue == null && value == null )
			return;
		if ( oldValue != null && value != null && oldValue.equals( value ) )
			return;

		// The values differ. Make the change.

		ActivityStack stack = getActivityStack( );

		String label = ThreadResources
				.getMessage( MessageConstants.CHANGE_ITEM_MESSAGE );
		stack.startTrans( label );

		MemberRecord record = new MemberRecord( element, ref, value );
		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Adds an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance
	 * hierarchy, then a new list is created on this element, and the list
	 * contains the only the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * added to the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and
	 * the new element is then appended to the copy.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list into which to add the structure
	 * @param item
	 *            the structure to add to the list
	 * @throws SemanticException
	 *             if the item to add is invalid.
	 */

	public void addItem( MemberRef ref, IStructure item )
			throws SemanticException
	{
		assert ref != null;

		if ( item == null )
			return;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		checkListMemberRef( ref );
		checkItem( ref, item );

		List list = ref.getList( design, element );
		element.checkStructureList( design, ref.getPropDefn( ), list, item );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ThreadResources
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ) );

		list = getOrMakePropertyList( ref );

		MemberRef insertRef = new MemberRef( ref, list.size( ) );
		PropertyListRecord record = new PropertyListRecord( element, insertRef,
				list, item );
		stack.execute( record );

		stack.commit( );
	}

	/**
	 * Inserts an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance
	 * hierarchy, then a new list is created on this element, and the list
	 * contains the only the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * inserted into the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and
	 * the new element is then inserted into the copy.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list into which to insert the new item
	 * @param item
	 *            the item to insert
	 * @param posn
	 *            the position at which to insert the item
	 * @throws SemanticException
	 *             if the item to add is invalid.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt; list.size())</code>.
	 */

	public void insertItem( MemberRef ref, IStructure item, int posn )
			throws SemanticException
	{
		assert ref != null;

		if ( item == null )
			return;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		checkListMemberRef( ref );
		checkItem( ref, item );

		List list = ref.getList( design, element );
		element.checkStructureList( design, ref.getPropDefn( ), list, item );

		ActivityStack stack = getActivityStack( );

		stack.startTrans( ThreadResources
				.getMessage( MessageConstants.INSERT_ITEM_MESSAGE ) );

		list = getOrMakePropertyList( ref );
		assert list != null;

		if ( posn < 0 || posn > list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		MemberRef insertRef = new MemberRef( ref, posn );
		PropertyListRecord record = new PropertyListRecord( element, insertRef,
				list, item );

		stack.execute( record );

		stack.commit( );
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list in which to remove an item.
	 * @param posn
	 *            position of the item to be removed from the list.
	 * @throws PropertyValueException
	 *             if the item to remove is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( MemberRef ref, int posn )
			throws PropertyValueException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		checkListMemberRef( ref );

		List list = ref.getList( design, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( posn < 0 || posn >= list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		doRemoveItem( new MemberRef( ref, posn ) );
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            the structure list reference
	 * @param structure
	 *            the item to remove
	 * @throws PropertyValueException
	 *             if the item to remove is not found.
	 */

	public void removeItem( MemberRef ref, IStructure structure )
			throws PropertyValueException
	{
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		checkListMemberRef( ref );
		List list = ref.getList( design, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		int posn = list.indexOf( structure );
		if ( posn == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		doRemoveItem( new MemberRef( ref, posn ) );
	}

	/**
	 * Removes structure from structure list.
	 * 
	 * @param structRef
	 *            reference to the item to remove
	 */

	private void doRemoveItem( MemberRef structRef )
	{
		ActivityStack stack = design.getActivityStack( );

		String label = ThreadResources
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE );
		stack.startTrans( label );

		List list = getOrMakePropertyList( structRef );

		PropertyListRecord record = new PropertyListRecord( element, structRef,
				list );
		getActivityStack( ).execute( record );
		stack.commit( );

	}

	/**
	 * Replaces an item from a structure list with the new one.
	 * <ul>
	 * <li>The element must exist in the effective value for the list. This
	 * means the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * replaced</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first copied into this element. Then, the copy of the target item
	 * is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            The structure list reference.
	 * @param oldItem
	 *            the old item to be replaced
	 * @param newItem
	 *            the new item reference.
	 * @throws SemanticException
	 *             if the old item is not found or this property type is not
	 *             structure list.
	 */

	public void replaceItem( MemberRef ref, IStructure oldItem,
			IStructure newItem ) throws SemanticException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		checkListMemberRef( ref );

		List list = ref.getList( design, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( newItem != null )
		{
			checkItem( ref, newItem );
			element.checkStructureList( design, ref.getPropDefn( ), list,
					newItem );
		}

		ActivityStack stack = design.getActivityStack( );

		stack.startTrans( ThreadResources
				.getMessage( MessageConstants.REPLACE_ITEM_MESSAGE ) );
		list = getOrMakePropertyList( ref );

		int index = list.indexOf( oldItem );
		if ( index == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), oldItem, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		PropertyReplaceRecord record = new PropertyReplaceRecord( element, ref,
				list, index, newItem );

		stack.execute( record );

		stack.commit( );
	}

	/**
	 * Removes all contents of the list. This is different from simply clearing
	 * the property. Removing all the contents leaves the property set to an
	 * empty list.
	 * 
	 * @param ref
	 *            reference to the list to clear
	 * @throws SemanticException
	 *             if the property is not a structure list property
	 */

	public void removeAllItems( MemberRef ref ) throws SemanticException
	{
		checkListMemberRef( ref );

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( design, element, propDefn );

		if ( ref.refType == MemberRef.PROPERTY )
			setProperty( ref.getPropDefn( ), null );
		else
			setMember( ref, null );
	}

	/**
	 * Moves an item within a list from one position to a new position.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * moved.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is moved within the copy of the list.</li>
	 * </ul>
	 * 
	 * <p>
	 * For example, if a list has A, B, C structures in order, when move A
	 * strucutre to <code>newPosn</code> with the value 2, the sequence
	 * becomes B, A, C.
	 * 
	 * 
	 * @param ref
	 *            reference to the list in which to do the move the item.
	 * @param from
	 *            the old position of the item.
	 * @param to
	 *            new position of the item. Note that the range of
	 *            <code>to</code> is from 0 to the number of strucutres in the
	 *            list.
	 * 
	 * @throws PropertyValueException
	 *             if the property is not a structure list property, or the list
	 *             value is not set.
	 * @throws IndexOutOfBoundsException
	 *             if the given from or to index is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void moveItem( MemberRef ref, int from, int to )
			throws PropertyValueException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		checkListMemberRef( ref );

		List list = ref.getList( design, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		ActivityStack stack = getActivityStack( );
		String label = ThreadResources
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE );

		stack.startTrans( label );

		if ( from < 0 || from > list.size( ) )
			throw new IndexOutOfBoundsException(
					"From: " + from + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		if ( to < 0 || to > list.size( ) )
			throw new IndexOutOfBoundsException(
					"To: " + to + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		if ( ( from == to ) || ( from + 1 == to ) )
			return;

		// adjust the position when move a item from a position with a small
		// index to another position with a bigger index.

		if ( from < to )
			to -= 1;

		MoveListItemRecord record = new MoveListItemRecord( element, ref, list,
				from, to );

		stack.execute( record );
		stack.commit( );
	}

	/**
	 * If the given reference refers a top level list property and this element
	 * already has a list, it is returned. Otherwise, if a parent element has a
	 * value, it is copied into this element. If the reference refers a
	 * different list point the list value will be returned.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 * 
	 * @return the existing or new local value of the property, or a list value
	 *         for a member.
	 */

	private List getOrMakePropertyList( MemberRef ref )
	{
		assert ref != null;

		// Top level list ref.

		if ( ref.refType == MemberRef.PROPERTY && ref.getPropDefn( ).isList( ) )
		{
			ElementPropertyDefn propDefn = ref.getPropDefn( );

			// Top level list property can be inherited.

			ArrayList list = (ArrayList) element.getLocalProperty( design,
					propDefn );
			if ( list != null )
				return list;

			ArrayList inherited = (ArrayList) element.getProperty( design,
					propDefn );

			list = new ArrayList( );
			if ( inherited != null )
			{
				for ( int i = 0; i < inherited.size( ); i++ )
					list.add( ( (IStructure) inherited.get( i ) ).copy( ) );
			}

			PropertyRecord propRecord = new PropertyRecord( element, propDefn,
					list );
			getActivityStack( ).execute( propRecord );
			return list;
		}

		List list = ref.getList( design, element );
		if ( list != null )
			return list;

		list = new ArrayList( );
		MemberRecord memberRecord = new MemberRecord( element, ref, list );
		getActivityStack( ).execute( memberRecord );
		return list;
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param ref
	 *            reference to the list into which to add the structure
	 * @throws PropertyValueException
	 *             if the <code>ref</code> doesn't refer a list property or
	 *             member.
	 */

	private void checkListMemberRef( MemberRef ref )
			throws PropertyValueException
	{
		if ( !ref.isListRef( ) )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param ref
	 *            reference to a list.
	 * @param item
	 *            the item to check
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	private void checkItem( MemberRef ref, IStructure item )
			throws SemanticException
	{
		assert item != null;

		PropertyDefn propDefn = null;

		if ( ref.refType == MemberRef.PROPERTY )
		{
			propDefn = ref.getPropDefn( );
		}
		else
		{
			propDefn = ref.getMemberDefn( );
		}

		if ( item.getDefn( ) != propDefn.getStructDefn( ) )
		{
			throw new PropertyValueException( element, propDefn, item,
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE );
		}

		for ( Iterator iter = item.getDefn( ).getPropertyIterator( ); iter
				.hasNext( ); )
		{
			PropertyDefn memberDefn = (PropertyDefn) iter.next( );
			memberDefn.validateValue( design, item.getProperty( design,
					memberDefn ) );
		}

		if ( item instanceof Structure )
		{
			List errorList = ( (Structure) item ).validate( design, element );
			if ( errorList.size( ) > 0 )
			{
				throw (SemanticException) errorList.get( 0 );
			}
		}

	}

}