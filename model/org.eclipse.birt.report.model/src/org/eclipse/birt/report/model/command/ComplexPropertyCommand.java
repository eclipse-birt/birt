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
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Complex property command to handle all list related operations, such as,
 * addItem, removeItem.
 */

public class ComplexPropertyCommand extends AbstractPropertyCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the root of <code>obj</code>
	 * @param obj
	 *            the element to modify.
	 */

	public ComplexPropertyCommand( Module module, DesignElement obj )
	{
		super( module, obj );
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

	private void addItem( MemberRef ref, IStructure item )
			throws SemanticException
	{
		assert ref != null;
		checkAllowedOperation( );
		if ( item == null )
			return;

		Structure struct = (Structure) item;
		if ( struct.getContext( ) != null )
			struct = (Structure) struct.copy( );

		// for the new structure, establish the context for its nested
		// structures.

		setupStructureContext( struct );

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		if ( struct.isReferencable( ) )
			assert !( (ReferencableStructure) struct ).hasReferences( );

		checkListMemberRef( ref );
		checkItem( ref, struct );

		List list = ref.getList( module, element );
		PropertyDefn memberDefn = ref.getMemberDefn( );
		if ( memberDefn != null )
			element.checkStructureList( module, memberDefn, list, struct );
		else
			element.checkStructureList( module, propDefn, list, struct );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		if ( null == list )
		{
			list = new ArrayList( );
			MemberRecord memberRecord = new MemberRecord( module, element, ref,
					list );
			stack.execute( memberRecord );
		}

		PropertyListRecord record = constructStructureRecord( ref, struct, list
				.size( ) );

		record.setEventTarget( getEventTarget( ref.getPropDefn( ) ) );
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

	public void addItem( MemberRef ref, Object item ) throws SemanticException
	{
		if ( item instanceof IStructure )
		{
			addItem( ref, (IStructure) item );
			return;
		}

		assert ref != null;
		checkAllowedOperation( );
		if ( item == null )
			return;

		// this method is not called for structure list property

		assert !( item instanceof IStructure );
		PropertyDefn prop = ref.getPropDefn( );
		PropertyDefn memberDefn = ref.getMemberDefn( );
		assertExtendedElement( module, element, prop );

		if ( memberDefn != null )
			prop = memberDefn;

		// check the property type is list and do some validation about the item

		checkListProperty( prop );
		Object value = checkItem( prop, item );

		if ( element instanceof ContentElement )
		{
			if ( !( (ContentElement) element ).isLocal( ) )
			{
				ContentElementCommand attrCmd = new ContentElementCommand(
						module, element, ( (ContentElement) element )
								.getValueContainer( ) );

				attrCmd.addItem( ref, value );
				return;
			}
		}

		// check whether the value in the list is unique when the sub-type is
		// element reference value

		List list = ref.getList( module, element );
		if ( prop.getTypeCode( ) == IPropertyType.LIST_TYPE )
			element.checkSimpleList( module, prop, list, value );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );

		list = ref.getList( module, element );
		if ( null == list )
		{
			list = new ArrayList( );
			MemberRecord memberRecord = new MemberRecord( module, element, ref,
					list );
			stack.execute( memberRecord );
		}

		PropertyListRecord record = new PropertyListRecord( element, ref
				.getPropDefn( ), list, value, list.size( ) );

		record.setEventTarget( getEventTarget( ref.getPropDefn( ) ) );

		stack.execute( record );

		if ( value instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) value;
			if ( refValue.isResolved( ) )
			{
				ElementRefRecord refRecord = new ElementRefRecord( element,
						refValue.getTargetElement( ), prop.getName( ), true );
				stack.execute( refRecord );
			}
		}

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
		checkAllowedOperation( );
		if ( item == null )
			return;

		Structure struct = (Structure) item;
		if ( struct.getContext( ) != null )
			struct = (Structure) struct.copy( );

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );
		checkItem( ref, struct );

		List list = ref.getList( module, element );
		element.checkStructureList( module, ref.getPropDefn( ), list, struct );

		ActivityStack stack = getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.INSERT_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		if ( null == list )
		{
			list = new ArrayList( );
			MemberRecord memberRecord = new MemberRecord( module, element, ref,
					list );
			stack.execute( memberRecord );
		}

		if ( posn < 0 || posn > list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		PropertyListRecord record = constructStructureRecord( ref, struct, posn );

		record.setEventTarget( getEventTarget( ref.getPropDefn( ) ) );

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
	 * @throws SemanticException
	 *             if the item to remove is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( MemberRef ref, int posn ) throws SemanticException
	{
		assert ref != null;
		PropertyDefn propDefn = ref.getPropDefn( );

		checkAllowedOperation( );

		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		PropertyDefn memberDefn = ref.getMemberDefn( );
		List list = null;

		if ( memberDefn != null )
			propDefn = memberDefn;

		if ( propDefn.getTypeCode( ) == IPropertyType.LIST_TYPE )
		{
			// do not need to do checkListProperty( memberDefn );

			list = ref.getList( module, element );
		}
		else
		{
			checkListMemberRef( ref );
			list = ref.getList( module, element );
		}

		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( posn < 0 || posn >= list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		if ( element instanceof ContentElement )
		{
			if ( !( (ContentElement) element ).isLocal( ) )
			{
				ContentElementCommand attrCmd = new ContentElementCommand(
						module, element, ( (ContentElement) element )
								.getValueContainer( ) );

				attrCmd.removeItem( new CachedMemberRef( ref, posn ) );
				return;
			}
		}

		doRemoveItem( new CachedMemberRef( ref, posn ) );
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
		checkAllowedOperation( );
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );
		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		int posn = list.indexOf( structure );
		if ( posn == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		doRemoveItem( new CachedMemberRef( ref, posn ) );
	}

	/**
	 * Removes structure from structure list.
	 * 
	 * @param structRef
	 *            reference to the item to remove
	 */

	private void doRemoveItem( MemberRef memberRef )
	{
		String label = ModelMessages
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE );

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( label );

		makeLocalCompositeValue( memberRef );
		List list = memberRef.getList( module, element );
		assert list != null;

		Structure struct = memberRef.getStructure( module, element );
		if ( struct != null )
		{
			if ( struct.isReferencable( ) )
				adjustReferenceClients( (ReferencableStructure) struct );

			// handle the structure member refers to other elements.

			adjustReferenceClients( struct, memberRef );
		}

		Object item = list.get( memberRef.getIndex( ) );

		PropertyListRecord record = null;

		if ( struct != null )
			record = new PropertyListRecord( element, struct.getContext( ),
					item );
		else
		{
			record = new PropertyListRecord( element, memberRef.getPropDefn( ),
					list, item );
		}

		record.setEventTarget( getEventTarget( memberRef.getPropDefn( ) ) );
		stack.execute( record );

		if ( item instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) item;
			if ( refValue.isResolved( ) )
			{
				ElementRefRecord refRecord = new ElementRefRecord( element,
						refValue.getTargetElement( ), memberRef.getPropDefn( )
								.getName( ), false );
				stack.execute( refRecord );

			}
		}

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
		checkAllowedOperation( );
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );

		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		Structure struct = (Structure) newItem;

		if ( newItem != null )
		{
			if ( struct.getContext( ) != null )
				struct = (Structure) struct.copy( );

			checkItem( ref, struct );
			element.checkStructureList( module, ref.getPropDefn( ), list,
					struct );
		}

		ActivityStack stack = module.getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.REPLACE_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		assert list != null;

		int index = list.indexOf( oldItem );
		if ( index == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), oldItem,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		PropertyReplaceRecord record = new PropertyReplaceRecord( element, ref,
				list, index, struct );

		record.setEventTarget( getEventTarget( propDefn ) );
		stack.execute( record );

		if ( oldItem.isReferencable( ) )
			adjustReferenceClients( (ReferencableStructure) oldItem );

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
		checkAllowedOperation( );
		checkListMemberRef( ref );

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		if ( ref.refType == MemberRef.PROPERTY )
		{
			PropertyCommand cmd = new PropertyCommand( module, element );
			cmd.setProperty( ref.getPropDefn( ), null );
		}
		else
		{
			PropertyCommand cmd = new PropertyCommand( module, element );
			cmd.setMember( ref, null );
		}
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
	 * @param oldPosn
	 *            the old position of the item.
	 * @param newPosn
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

	public void moveItem( MemberRef ref, int oldPosn, int newPosn )
			throws PropertyValueException
	{
		assert ref != null;
		checkAllowedOperation( );
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		checkListMemberRef( ref );

		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		ActivityStack stack = getActivityStack( );
		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ITEM_MESSAGE );

		int adjustedNewPosn = checkAndAdjustPosition( oldPosn, newPosn, list
				.size( ) );
		if ( oldPosn == adjustedNewPosn )
			return;

		stack.startTrans( label );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		assert list != null;

		MoveListItemRecord record = new MoveListItemRecord( element, ref, list,
				oldPosn, adjustedNewPosn );
		record.setEventTarget( getEventTarget( ref.getPropDefn( ) ) );

		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param prop
	 *            the property definition to check whether it is list type
	 * @throws PropertyValueException
	 *             if the property definition is not a list type
	 */

	private void checkListProperty( PropertyDefn prop )
			throws PropertyValueException
	{
		if ( prop.getTypeCode( ) != IPropertyType.LIST_TYPE )
			throw new PropertyValueException( element, prop, null,
					PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
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

	protected void checkListMemberRef( MemberRef ref )
			throws PropertyValueException
	{
		if ( !ref.isListRef( ) )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
	}

	/**
	 * Check operation is allowed or not. Now if element is css style instance ,
	 * forbidden its operation.
	 * 
	 */

	private void checkAllowedOperation( )
	{
		if ( element != null && element instanceof CssStyle )
		{
			throw new IllegalOperationException(
					CssException.DESIGN_EXCEPTION_READONLY );
		}
	}

	private PropertyListRecord constructStructureRecord( MemberRef ref,
			Structure struct, int posn )
	{
		PropertyListRecord record = null;
		Object parentStruct = ref.getStructure( module, element );

		IPropertyDefn tmpPropDefn = ref.getMemberDefn( );
		if ( tmpPropDefn == null )
			tmpPropDefn = ref.getPropDefn( );

		StructureContext context = null;
		if ( parentStruct == null )
			context = new StructureContext( element, tmpPropDefn.getName( ) );
		else
			context = new StructureContext( (Structure) parentStruct,
					tmpPropDefn.getName( ) );

		record = new PropertyListRecord( element, context, struct, posn );

		return record;
	}

}
