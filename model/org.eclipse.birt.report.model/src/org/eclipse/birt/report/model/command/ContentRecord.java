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

import java.util.List;

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.validators.core.ValidationExecutor;

/**
 * Records adding a content into a container, or removing content from a
 * container. Removing a content from a container effectively deletes the
 * content from the report design .
 *  
 */

public class ContentRecord extends SimpleRecord
{

	/**
	 * The container element.
	 */

	protected DesignElement container = null;

	/**
	 * The content element.
	 */

	protected DesignElement content = null;

	/**
	 * The slot within the container.
	 */

	protected int slotID = 0;

	/**
	 * Whether to add or remove the element.
	 */

	protected boolean add = true;

	/**
	 * Memento for the old element position when deleting the element.
	 */

	protected int oldPosn = -1;

	/**
	 * The root element set when using element IDs.
	 */

	protected RootElement root = null;

	/**
	 * Constructs the record with container element, slot id, content element,
	 * and flag for adding or dropping.
	 * 
	 * @param containerObj
	 *            The container element.
	 * @param theSlot
	 *            The slotID in which to put the content.
	 * @param contentObj
	 *            The content object to add or remove.
	 * @param isAdd
	 *            Whether to add or remove the item.
	 */

	public ContentRecord( DesignElement containerObj, int theSlot,
			DesignElement contentObj, boolean isAdd )
	{
		init( containerObj, theSlot, contentObj, -1, isAdd );
	}

	/**
	 * Constructs the record for adding with container element, slot id, content
	 * element, and position in container.
	 * 
	 * @param containerObj
	 *            The container element.
	 * @param theSlot
	 *            The slotID in which to put the content.
	 * @param contentObj
	 *            The content object to add or remove.
	 * @param newPos
	 *            The position index where to insert the content.
	 */

	public ContentRecord( DesignElement containerObj, int theSlot,
			DesignElement contentObj, int newPos )
	{
		init( containerObj, theSlot, contentObj, newPos, true );
	}

	/**
	 * Initializes the record.
	 * 
	 * @param containerObj
	 *            the container element
	 * @param theSlot
	 *            the slotID in which to put the content
	 * @param contentObj
	 *            the content object to add or remove
	 * @param newPos
	 *            the position index at which the new content is to be inserted
	 * @param isAdd
	 *            whether to add or remove the item
	 */

	private void init( DesignElement containerObj, int theSlot,
			DesignElement contentObj, int newPos, boolean isAdd )
	{
		container = containerObj;
		slotID = theSlot;
		content = contentObj;
		add = isAdd;

		// Verify invariants.
		assert newPos >= -1;
		assert container != null;
		assert content != null;
		assert isAdd && content.getContainer( ) == null || !isAdd
				&& content.getContainer( ) != null;
		assert container.getDefn( ).getSlot( slotID ).canContain( content );

		ContainerSlot slot = container.getSlot( slotID );
		assert slot != null;
		if ( isAdd )
		{
			oldPosn = ( newPos == -1 || slot.getCount( ) < newPos ) ? slot
					.getCount( ) : newPos;
		}
		else
		{
			oldPosn = slot.findPosn( content );
			assert oldPosn != -1;
		}

		if ( add )
			label = ThreadResources
					.getMessage( MessageConstants.ADD_ELEMENT_MESSAGE );
		else
			label = ThreadResources
					.getMessage( MessageConstants.DROP_ELEMENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform()
	 */

	public DesignElement getTarget( )
	{
		return container;
	}

	/**
	 * Not used in this class.
	 * 
	 * @return null is always returned.
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		ContainerSlot slot = container.getSlot( slotID );
		if ( add && !undo || !add && undo )
		{
			slot.insert( content, oldPosn );

			// Cache the inverse relationship.

			content.setContainer( container, slotID );

			// Add the item to the element ID map if we are using
			// element IDs.

			if ( root != null )
				root.addElementID( content );
		}
		else
		{
			slot.remove( content );

			// Clear the inverse relationship.

			content.setContainer( null, DesignElement.NO_SLOT );

			// Remove the element from the ID map if we are using
			// IDs.

			if ( root != null )
				root.dropElementID( content );

			// Clear listeners on the element. Listeners are NOT required to
			// undo/redo.

			content.clearListeners( );
		}
	}

	/**
	 * Sends a content changed and possibly element deleted event. This record
	 * is unusual because it must send two events: one for the container, one
	 * for the content. If we are dropping the content, then it is effectively
	 * deleted, and we must tell the content that it has been deleted.
	 */

	protected void sendNotifcations( boolean transactionStarted )
	{
		// Send the content changed event to the container.

		NotificationEvent event = null;
		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
			event = new ContentEvent( container, content, slotID,
					ContentEvent.ADD );
		else
			event = new ContentEvent( container, slotID, ContentEvent.REMOVE );

		event.setInTransaction( transactionStarted );

		// Include the sender if this is the original execution.
		// The sender is not sent for undo, redo because such actions are
		// triggered by the activity stack, not dialog or editor.

		if ( state == DONE_STATE )
			event.setSender( sender );

		// Broadcast the event to the target.

		container.broadcast( event );

		// If the content was dropped, then send an element deleted
		// event to the content.

		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
		{
			if ( isSelector( content ) )
				content.broadcast( event, container.getRoot( ) );

			return;
		}

		event = new ElementDeletedEvent( content );
		if ( state == DONE_STATE )
			event.setSender( sender );
		content.broadcast( event, container.getRoot( ) );
	}

	private boolean isSelector( DesignElement content )
	{
		return MetaDataDictionary.getInstance( ).getPredefinedStyle(
				content.getName( ) ) != null;
	}

	/**
	 * Sets the root element if element IDs are in use. If set, the record will
	 * manage the ID map in the root element.
	 * 
	 * @param theRoot
	 *            The root element.
	 */

	public void setRoot( RootElement theRoot )
	{
		root = theRoot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.core.IValidatorProvider#getValidators()
	 */

	public List getValidators( )
	{
		SlotDefn slotDefn = (SlotDefn) container.getDefn( ).getSlot( slotID );

		List list = ValidationExecutor.getValidationNodes( container, slotDefn
				.getTriggerDefnSet( ), false );

		// Validate the content.

		ElementDefn contentDefn = (ElementDefn) content.getDefn( );
		list.addAll( ValidationExecutor.getValidationNodes( content,
				contentDefn.getTriggerDefnSet( ), false ) );

		return list;
	}
}