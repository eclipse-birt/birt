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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.ReferenceableElement.BackRef;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SlotDefn;

/**
 * This class adds, deletes and moves content elements. Adding a content element
 * to a container is the only way to add a new element to the design. Similarly,
 * removing an element from its container is the only way to delete an element
 * from the design.
 * <p>
 * Note: be sure to use the move operation if your intent is to move an element
 * from one place to another. Do not use a drop followed by an add. The move
 * command verifies that the move can be done before starting the action. If you
 * instead do a drop followed by an add, you'll end up with the element deleted
 * if it cannot be added into its new location.
 *  
 */

public class ContentCommand extends AbstractElementCommand
{

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param design
	 *            the report design
	 * @param container
	 *            the container element
	 */

	public ContentCommand( ReportDesign design, DesignElement container )
	{
		super( design, container );
	}

	/**
	 * Adds a new element into a container and specifies the position in the
	 * container. Virtually all elements must reside in a container. Containers
	 * are identified by a container ID. The application creates the element
	 * object, then adds it to the container here. The undo of this operation
	 * effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @param newPos
	 *            the position index at which the content to be inserted. If
	 *            it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	public void add( DesignElement content, int slotID, int newPos )
			throws ContentException, NameException
	{
		assert newPos >= 0 || newPos == -1;
		doAdd( content, slotID, newPos );
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside
	 * in a container. Containers are identified by a container ID. The
	 * application creates the element object, then adds it to the container
	 * here. The undo of this operation effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	public void add( DesignElement content, int slotID )
			throws ContentException, NameException
	{
		doAdd( content, slotID, -1 );
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside
	 * in a container. (The term "container" here is generic, it is not the same
	 * as the Container element defined in the XML schema.) Containers are
	 * identified by a container ID. The application creates the element object,
	 * then adds it to the container here. The undo of this operation
	 * effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @param newPos
	 *            the position index at which the content to be inserted. If
	 *            it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	private void doAdd( DesignElement content, int slotID, int newPos )
			throws ContentException, NameException
	{
		// The element should not have any properties values or user
		// properties defined -- add these as separate steps.

		assert !content.hasUserProperties( );
		assert content.getExtendsElement( ) == null;
		assert content.getContainer( ) == null;

		// Ensure that the content can be put into the container.

		ElementDefn metaData = (ElementDefn) element.getDefn( );
		if ( !metaData.isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		SlotDefn slotInfo = (SlotDefn) metaData.getSlot( slotID );
		if ( slotInfo == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !slotInfo.canContain( content ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE );

		// This element is already the content of the element to add.

		if ( element.isContentOf( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE );

		// If this is a single-item slot, ensure that the slot is empty.

		if ( !slotInfo.isMultipleCardinality( )
				&& element.getSlot( slotID ).getCount( ) > 0 )
		{
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL );
		}

		if ( !element.canContain( design, slotID, content ) )
			throw new ContentException(
					element,
					slotID,
					content,
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT );

		// Ensure we can add the name.

		NameCommand nameCmd = new NameCommand( design, content );
		nameCmd.checkName( );

		// Add the item to the container.
		ContentRecord addRecord;
		if ( newPos == -1 )
		{
			addRecord = new ContentRecord( element, slotID, content, true );
		}
		else
		{
			addRecord = new ContentRecord( element, slotID, content, newPos );
		}

		// If we are using element IDs, allocate a new one.

		if ( MetaDataDictionary.getInstance( ).useID( ) )
		{
			content.setID( design.getNextID( ) );
			addRecord.setRoot( design );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( addRecord.getLabel( ) );
		stack.execute( addRecord );
		nameCmd.addElement( );
		stack.commit( );
	}

	/**
	 * Removes an item from its container. This is equivalent to deleting the
	 * element from the design. Because the element is being deleted, we must
	 * clean up all references to or from the element. References include:
	 * <p>
	 * <ul>
	 * <li>The elements that this content extends.
	 * <li>The elements that extend this content.
	 * <li>The style that this content uses.
	 * <li>The elements that use this style.
	 * <li>The elements that this content contains.
	 * <li>The name space that contains this content.
	 * </ul>
	 * 
	 * @param content
	 *            the element to remove
	 * @param slotID
	 *            the slot from which to remove the content
	 * @throws SemanticException
	 *             if this content cannot be removed from container.
	 */

	public void remove( DesignElement content, int slotID )
			throws SemanticException
	{
		assert content != null;

		// Ensure that the content can be dropped from the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot slot = element.getSlot( slotID );
		if ( slot == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !slot.contains( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );
		if ( !slot.canDrop( content ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_DROP_FORBIDDEN );

		// if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if ( hasDescendents( content, slotID ) )
		{
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		}

		// Prepare the transaction.

		ContentRecord dropRecord = new ContentRecord( element, slotID, content,
				false );

		// Tell the record if we are using element IDs.

		if ( MetaDataDictionary.getInstance( ).useID( ) )
		{
			dropRecord.setRoot( design );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( dropRecord.getLabel( ) );

		try
		{
			// Remove contents.

			removeContents( content );

			// Clean up references to or from the element.

			dropUserProperties( content );
			if ( content.isStyle( ) )
				adjustStyleClients( (StyleElement) content );
			else if ( content.hasReferences( ) )
				adjustReferenceClients( (ReferenceableElement) content );
			adjustReferredClients( content );
			adjustDerived( content );

			// Drop the style...

			if ( content.getLocalStyle( ) != null )
			{
				StyleCommand styleCmd = new StyleCommand( design, content );
				styleCmd.setStyle( null );
			}

			// Drop the extends...

			if ( content.getExtendsElement( ) != null )
			{
				ExtendsCommand extendsCmd = new ExtendsCommand( design, content );
				extendsCmd.setExtendsName( null );
			}

			// Remove from name space...

			if ( content.getName( ) != null )
			{
				NameCommand nameCmd = new NameCommand( design, content );
				nameCmd.dropElement( );
			}
		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}

		// Remove the element itself.

		stack.execute( dropRecord );
		stack.commit( );

	}

	/**
	 * Changes derived elements to derive from the parent (if any) instead.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws ExtendsException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void adjustDerived( DesignElement obj ) throws ExtendsException
	{
		// Skip if this element does not have derived elements.

		Collection derived = obj.getDerived( );
		if ( derived.isEmpty( ) )
			return;

		DesignElement parent = obj.getExtendsElement( );
		Iterator iter = derived.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement child = (DesignElement) iter.next( );
			ExtendsCommand childCmd = new ExtendsCommand( design, child );
			childCmd.setExtendsElement( parent );
		}
	}

	/**
	 * Clears style of all clients of a style. This occurs when a style is
	 * deleted. There may be elements in the design that refer to the deleted
	 * style. To avoid "dangling references" to this style, the references must
	 * be cleared. The algorithm used is to simply delete the style reference.
	 * 
	 * @param style
	 *            the style to clean up
	 * @throws StyleException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void adjustStyleClients( StyleElement style ) throws StyleException
	{
		List clients = style.getClientList( );

		while ( !clients.isEmpty( ) )
		{
			BackRef ref = (BackRef) clients.get( 0 );
			DesignElement client = ref.element;
			StyleCommand clientCmd = new StyleCommand( design, client );
			clientCmd.setStyleElement( null );
		}
	}

	/**
	 * Clears references to an element that is to be deleted. The element to be
	 * deleted is one that has references in the form of element reference
	 * properties on other elements. These other elements, called "clients",
	 * each contain a property of type element reference and that property
	 * refers to this element. Each reference is recorded with a "back pointer"
	 * from the referenced element to the client. That back pointer has both a
	 * pointer to the client element, and the property within that element that
	 * holds the reference. We use this information to clear the property.
	 * 
	 * @param element
	 *            the element to be deleted
	 * @throws SemanticException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 * 
	 * @see #adjustReferredClients(DesignElement)
	 */

	private void adjustReferenceClients( ReferenceableElement element )
			throws SemanticException
	{
		List clients = element.getClientList( );
		while ( !clients.isEmpty( ) )
		{
			BackRef ref = (BackRef) clients.get( 0 );
			DesignElement client = ref.element;
			PropertyCommand cmd = new PropertyCommand( design, client );
			cmd.setProperty( ref.propName, null );
		}
	}

	/**
	 * Clears references of elements that are referred by the to-be-deleted
	 * element, except for extends and style element references. Unlike the
	 * method {@link #adjustReferenceClients(ReferenceableElement)}, this
	 * method removes references from those elements that are referred.
	 * 
	 * @param element
	 *            the element to be deleted
	 * @throws SemanticException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 *  
	 */

	private void adjustReferredClients( DesignElement element )
			throws SemanticException
	{
		List propDefns = element.getPropertyDefns( );

		for ( Iterator iter = propDefns.iterator( ); iter.hasNext( ); )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );

			// DO NOT consider extends and style property since this has been
			// handled in remove method.

			if ( DesignElement.EXTENDS_PROP.equalsIgnoreCase( propDefn
					.getName( ) )
					|| StyledElement.STYLE_PROP.equalsIgnoreCase( propDefn //$NON-NLS-1$
							.getName( ) ) )
				continue;

			if ( propDefn.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
			{
				Object value = element.getLocalProperty( null,
						(ElementPropertyDefn) propDefn );

				if ( value != null )
				{
					ElementRefValue refValue = (ElementRefValue) value;
					DesignElement referred = refValue.getElement( );

					if ( referred != null )
					{
						BackRefRecord record = new BackRefRecord(
								(ReferenceableElement) referred, element,
								propDefn.getName( ) );
						getActivityStack( ).execute( record );
					}
				}
			}
		}
	}

	/**
	 * Deletes user properties. This will also remove property values from this
	 * element and derived elements.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws UserPropertyException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void dropUserProperties( DesignElement obj )
			throws UserPropertyException
	{
		Collection props = element.getUserProperties( );
		if ( props != null )
		{
			UserPropertyCommand propCmd = new UserPropertyCommand( design, obj );
			Iterator iter = props.iterator( );
			while ( iter.hasNext( ) )
			{
				UserPropertyDefn prop = (UserPropertyDefn) iter.next( );
				propCmd.dropUserProperty( prop.getName( ) );
			}
		}
	}

	/**
	 * Removes all the contents of the element. Removes all contents from all
	 * slots. Does the drop recursively.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws SemanticException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void removeContents( DesignElement obj ) throws SemanticException
	{
		// Skip this step if the element is not a container.

		IElementDefn metaData = obj.getDefn( );
		if ( !metaData.isContainer( ) )
			return;

		int slotCount = metaData.getSlotCount( );
		for ( int slotID = 0; slotID < slotCount; slotID++ )
		{
			ContainerSlot slot = obj.getSlot( slotID );
			ContentCommand contentCmd = new ContentCommand( design, obj );
			while ( !slot.isEmpty( ) )
			{
				DesignElement content = slot.getContent( 0 );
				contentCmd.remove( content, slotID );
			}
		}
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different
	 * element (usual case). Use the other form of this method to move an
	 * element within the same slot.
	 * 
	 * @param content
	 *            the element to move.
	 * @param fromSlotID
	 *            the old slot location within the container item associated
	 *            with this command.
	 * @param to
	 *            the new container.
	 * @param toSlotID
	 *            the slot within the new container.
	 * @param newPos
	 *            the position in the target slot to which the content will be
	 *            moved. If it is -1 or greater than the size of the target
	 *            slot, the content will be appended at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be moved to new container.
	 */

	private void doMove( DesignElement content, int fromSlotID,
			DesignElement to, int toSlotID, int newPos )
			throws ContentException
	{
		assert content != null;
		assert to != null;
		assert newPos >= -1;

		// Do nothing if source and destination are the same.

		if ( element == to && fromSlotID == toSlotID )
			return;

		// Cannot put an element inside itself.

		if ( to.isContentOf( content ) )
			throw new ContentException( to, fromSlotID, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE );

		// Ensure that the content can be put into the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		if ( !to.getDefn( ).isContainer( ) )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot fromSlot = element.getSlot( fromSlotID );
		if ( fromSlot == null )
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		ContainerSlot toSlot = to.getSlot( toSlotID );
		if ( toSlot == null )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !fromSlot.contains( content ) )
			throw new ContentException( element, fromSlotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );
		if ( !fromSlot.canDrop( content ) )
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_DROP_FORBIDDEN );

		//  if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if ( hasDescendents( content, fromSlotID ) )
		{
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		}

		SlotDefn slotInfo = (SlotDefn) to.getDefn( ).getSlot( toSlotID );
		if ( !slotInfo.canContain( content ) )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE );
		if ( !slotInfo.isMultipleCardinality( ) && toSlot.getCount( ) > 0 )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL );

		if ( !to.canContain( design, toSlotID, content ) )
			throw new ContentException(
					to,
					toSlotID,
					content,
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT );

		ActivityStack stack = getActivityStack( );

		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ELEMENT_MESSAGE );
		stack.startTrans( label );

		ContentRecord record = new ContentRecord( element, fromSlotID, content,
				false );
		stack.execute( record );
		record = new ContentRecord( to, toSlotID, content, newPos );
		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different
	 * element (usual case). Use the other form of this method to move an
	 * element within the same slot.
	 * 
	 * @param content
	 *            The element to move.
	 * @param fromSlotID
	 *            Old slot location within the container item associated with
	 *            this command.
	 * @param to
	 *            The new container.
	 * @param toSlotID
	 *            the slot within the new container.
	 * @param newPos
	 *            the position in the target slot to which the content will be
	 *            moved. If it is greater than the size of the target slot, the
	 *            content will be appended at the end of the slot.
	 * @throws ContentException
	 */

	public void move( DesignElement content, int fromSlotID, DesignElement to,
			int toSlotID, int newPos ) throws ContentException
	{
		assert newPos >= 0;
		doMove( content, fromSlotID, to, toSlotID, newPos );
	}

	/**
	 * Moves an element from one slot to another. The destination slot can be in
	 * the same element (unusual) or a different element (usual case.) Use the
	 * other form of this method to move an element within the same slot.
	 * 
	 * @param content
	 *            The element to move.
	 * @param fromSlotID
	 *            Old slot location within the container item associated with
	 *            this command.
	 * @param to
	 *            The new container.
	 * @param toSlotID
	 *            The slot within the new container.
	 * @throws ContentException
	 */

	public void move( DesignElement content, int fromSlotID, DesignElement to,
			int toSlotID ) throws ContentException
	{
		doMove( content, fromSlotID, to, toSlotID, -1 );
	}

	/**
	 * Moves an element from one position to another within the same slot.
	 * <p>
	 * For example, if a slot has A, B, C elements in order, when move A element
	 * to <code>newPosn</code> with the value 2, the sequence becomes B, A, C.
	 * 
	 * 
	 * @param content
	 *            The element to move.
	 * @param slotID
	 *            The slot that contains the element.
	 * @param newPosn
	 *            The new position within the slot. Note that the range of
	 *            <code>newPos</code> is from 0 to the number of element in
	 *            the slot with the ID <code>slotID</code>.
	 * @throws ContentException
	 *             if the content cannot be moved to new container.
	 */

	public void movePosition( DesignElement content, int slotID, int newPosn )
			throws ContentException
	{
		assert content != null;

		// Ensure that the content can be put into the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot slot = element.getSlot( slotID );
		if ( slot == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !content.isContentOf( element ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );

		// Skip the step if the slotID has only single content.

		SlotDefn slotInfo = (SlotDefn) element.getDefn( ).getSlot( slotID );
		if ( !slotInfo.isMultipleCardinality( ) )
			return;

		if ( !canMovePosition( content, slotID, newPosn ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_MOVE_FORBIDDEN );

		// Move the new position so that it is in range.

		if ( newPosn < 0 )
			newPosn = 0;
		if ( newPosn > slot.getCount( ) - 1 )
			newPosn = slot.getCount( );

		// If the new position is the same as the old, then skip the operation.

		int posn = slot.findPosn( content );
		if ( ( posn == newPosn ) || ( posn + 1 == newPosn ) )
			return;

		// adjust the position when move a item from a position with a small
		// index to another position with a bigger index.

		if ( posn < newPosn )
			newPosn -= 1;

		// Do the move.

		MoveContentRecord record = new MoveContentRecord( element, slotID,
				content, newPosn );
		getActivityStack( ).execute( record );
	}

	/**
	 * Returns false if the content has ancestor behind the new position or has
	 * children before the new position.
	 * 
	 * @param content
	 *            the element to move position
	 * @param slotID
	 *            the slot that contains the element
	 * @param newPosn
	 *            the new position within the slot.
	 * @return true if the content can be moved within the slot, otherwise
	 *         false.
	 */

	private boolean canMovePosition( DesignElement content, int slotID,
			int newPosn )
	{
		if ( element instanceof ReportDesign
				&& slotID == ReportDesign.COMPONENT_SLOT )
		{
			List derived = content.getDerived( );
			ContainerSlot slot = element.getSlot( slotID );
			Iterator iter = derived.iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElement child = (DesignElement) iter.next( );
				if ( slot.contains( child ) )
				{
					// if content has child before the new position
					// then the move of new position is forbidden.

					if ( slot.findPosn( child ) <= newPosn )
						return false;
				}
			}
			DesignElement e = content.getExtendsElement( );
			while ( e != null )
			{
				// if the content has ancestor behind the new position
				// then the move of new position is forbidden.

				if ( slot.findPosn( e ) >= newPosn )
					return false;
				e = e.getExtendsElement( );
			}
		}
		return true;
	}

	/**
	 * Returns true if content in component slot of report design has
	 * descendants, otherwise false.
	 * 
	 * @param content
	 *            the content to handle
	 * @param fromSlotID
	 *            the slot that contains the element
	 * @return true if content has descendants, otherwise false.
	 */

	private boolean hasDescendents( DesignElement content, int fromSlotID )
	{
		return element instanceof ReportDesign
				&& fromSlotID == ReportDesign.COMPONENT_SLOT
				&& content.hasDerived( );
	}
}