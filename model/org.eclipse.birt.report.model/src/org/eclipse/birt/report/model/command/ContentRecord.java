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

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.validators.ValidationExecutor;

/**
 * Records adding a content into a container, or removing content from a
 * container. Removing a content from a container effectively deletes the
 * content from the report design .
 * 
 */

public class ContentRecord extends SimpleRecord
{

	/**
	 * Container information of this record.
	 */
	protected ContainerContext containerInfo = null;

	/**
	 * The content element.
	 */

	protected DesignElement content = null;

	/**
	 * Whether to add or remove the element.
	 */

	protected boolean add = true;

	/**
	 * Memento for the old element position when deleting the element.
	 */

	protected int oldPosn = -1;

	/**
	 * The module set when using element IDs.
	 */

	protected final Module module;

	/**
	 * Constructs the record with container element, slot id, content element,
	 * and flag for adding or dropping.
	 * 
	 * @param module
	 *            the module in which this record executes
	 * @param containerInfor
	 *            The container information
	 * @param contentObj
	 *            The content object to add or remove.
	 * @param isAdd
	 *            Whether to add or remove the item.
	 */

	public ContentRecord( Module module, ContainerContext containerInfor,
			DesignElement contentObj, boolean isAdd )
	{
		init( containerInfor, contentObj, -1, isAdd );
		this.module = module;
		assert module != null;
	}

	/**
	 * Constructs the record for adding with container element, slot id, content
	 * element, and position in container.
	 * 
	 * @param module
	 *            the module in which this record executes
	 * @param containerInfo
	 *            The container information
	 * @param contentObj
	 *            The content object to add or remove.
	 * @param newPos
	 *            The position index where to insert the content.
	 */

	public ContentRecord( Module module, ContainerContext containerInfo,
			DesignElement contentObj, int newPos )
	{
		init( containerInfo, contentObj, newPos, true );
		this.module = module;
		assert module != null;
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

	private void init( ContainerContext theContainerInfo,
			DesignElement contentObj, int newPos, boolean isAdd )
	{
		this.containerInfo = theContainerInfo;
		content = contentObj;
		add = isAdd;

		// Verify invariants.
		assert newPos >= -1;
		assert containerInfo != null;
		assert content != null;
		assert isAdd && content.getContainer( ) == null || !isAdd
				&& content.getContainer( ) != null;
		assert containerInfo.getContainerDefn( ).canContain( content );

		if ( isAdd )
		{
			int count = containerInfo.getContentCount( module );
			oldPosn = ( newPos == -1 || count < newPos ) ? count : newPos;
		}
		else
		{
			oldPosn = containerInfo.indexOf( module, content );
			assert oldPosn != -1;
		}

		if ( add )
			label = ModelMessages
					.getMessage( MessageConstants.ADD_ELEMENT_MESSAGE );
		else
			label = ModelMessages
					.getMessage( MessageConstants.DROP_ELEMENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.activity.SimpleRecord#perform()
	 */

	public DesignElement getTarget( )
	{
		return containerInfo.getElement( );
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
		if ( add && !undo || !add && undo )
		{
			containerInfo.add( module, content, oldPosn );

			// Add the item to the element ID map if we are using
			// element IDs.

			if ( content.getRoot( ) != null )
				module.manageId( content, true );
		}
		else
		{
			// Remove the element from the ID map if we are using
			// IDs.

			if ( content.getRoot( ) != null )
				module.manageId( content, false );
			
			containerInfo.remove( module, content );
		}
	}

	/**
	 * Indicate whether the given <code>content</code> is a CSS-selecter.
	 * 
	 * @param content
	 *            a given design element
	 * @return <code>true</code> if it is a predefined style.
	 */

	private boolean isSelector( DesignElement content )
	{
		if ( !( content instanceof StyleElement ) )
			return false;

		return MetaDataDictionary.getInstance( ).getPredefinedStyle(
				content.getName( ) ) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.core.IValidatorProvider#getValidators()
	 */

	public List getValidators( )
	{
		List list = ValidationExecutor.getValidationNodes( this.containerInfo
				.getElement( ), containerInfo.getTriggerSetForContainerDefn( ),
				false );

		// Validate the content.

		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
		{
			ElementDefn contentDefn = (ElementDefn) content.getDefn( );
			list.addAll( ValidationExecutor.getValidationNodes( content,
					contentDefn.getTriggerDefnSet( ), false ) );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		List retValue = new ArrayList( );
		retValue.addAll( super.getPostTasks( ) );

		DesignElement container = containerInfo.getElement( );
		if ( container instanceof TableItem || container instanceof GridItem
				|| container instanceof TableGroup
				|| container instanceof TableRow )
		{
			ReportItem compoundElement = LayoutUtil
					.getCompoundContainer( container );
			if ( compoundElement != null )
			{
				retValue.add( new LayoutRecordTask( module, compoundElement ) );
			}
		}

		// Send the content changed event to the container.

		NotificationEvent event = null;
		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
			event = new ContentEvent( containerInfo, content, ContentEvent.ADD );
		else
			event = new ContentEvent( containerInfo, content,
					ContentEvent.REMOVE );

		if ( state == DONE_STATE )
			event.setSender( sender );

		retValue.add( new NotificationRecordTask( container, event ) );

		// If the content was added, then send an element added
		// event to the content.

		if ( add && state != UNDONE_STATE || !add && state == UNDONE_STATE )
		{
			if ( isSelector( content ) )
				// content.broadcast( event, container.getRoot( ) );

				retValue.add( new NotificationRecordTask( content, event,
						container.getRoot( ) ) );

			return retValue;
		}

		// Broadcast to the content element of the deleted event if this content
		// is parameter or parameter group.

		if ( content instanceof Parameter || content instanceof ParameterGroup
				|| content instanceof SimpleDataSet
				|| content instanceof StyleElement )
		{
			event = new ElementDeletedEvent( container, content );
			if ( state == DONE_STATE )
				event.setSender( sender );

			retValue.add( new NotificationRecordTask( content, event, container
					.getRoot( ) ) );
		}
		else
			content.clearListeners( );

		return retValue;
	}
}