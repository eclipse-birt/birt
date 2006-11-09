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
import org.eclipse.birt.report.model.api.command.ContentReplaceEvent;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.validators.ValidationExecutor;

/**
 * Records replacing a content with a new element in the container.
 */

public class ContentReplaceRecord extends SimpleRecord
{

	/**
	 * The container element.
	 */

	protected DesignElement container = null;

	/**
	 * The new element.
	 */

	protected final DesignElement newElement;

	/**
	 * The old element.
	 */

	protected final DesignElement oldElement;

	/**
	 * The slot within the container.
	 */

	protected int slotID = 0;

	/**
	 * The module set when using element IDs.
	 */

	protected final Module module;

	/**
	 * The position where the old element or the new element exsits.
	 */

	protected int posn;

	/**
	 * The container slot where the record executes.
	 */

	protected ContainerSlot slot = null;

	/**
	 * Constructs the record with container element, slot id, old element, and
	 * new element.
	 * 
	 * @param module
	 *            the module in which this record executes
	 * @param containerObj
	 *            The container element.
	 * @param theSlot
	 *            The slotID in which to put the content.
	 * @param oldElement
	 *            the old elemnt to be replaced
	 * @param newElement
	 *            the new element to replace
	 * 
	 */

	public ContentReplaceRecord( Module module, DesignElement containerObj,
			int theSlot, DesignElement oldElement, DesignElement newElement )
	{
		this.module = module;
		this.oldElement = oldElement;
		this.newElement = newElement;
		this.slotID = theSlot;
		this.container = containerObj;
		assert module != null;

		this.slot = container.getSlot( theSlot );
		assert slot != null;
		this.posn = slot.findPosn( oldElement );
		assert posn != -1;

		this.label = ModelMessages
				.getMessage( MessageConstants.REPLACE_ELEMENT_MESSAGE );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		if ( undo )
		{
			replace( newElement, oldElement );
		}
		else
		{
			replace( oldElement, newElement );
		}
	}

	/**
	 * Does the replacement between the old element and new element. Delete old
	 * one and its id, add the new one and its id.
	 * 
	 * @param oldElement
	 *            the old element to delete
	 * @param newElement
	 *            the old element to insert
	 */

	private void replace( DesignElement oldElement, DesignElement newElement )
	{
		// remove old one

		slot.remove( oldElement );
		if ( oldElement.getRoot( ) != null )
			module.manageId( oldElement, false );
		oldElement.setContainer( null, IDesignElementModel.NO_SLOT );

		// add new one

		slot.insert( newElement, posn );
		newElement.setContainer( container, slotID );
		if ( newElement.getRoot( ) != null )
			module.manageId( newElement, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent( )
	{
		return null;
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
		SlotDefn slotDefn = (SlotDefn) container.getDefn( ).getSlot( slotID );

		List list = ValidationExecutor.getValidationNodes( container, slotDefn
				.getTriggerDefnSet( ), false );

		if ( state != UNDONE_STATE )
		{
			// Validate the new element.

			ElementDefn contentDefn = (ElementDefn) newElement.getDefn( );
			list.addAll( ValidationExecutor.getValidationNodes( newElement,
					contentDefn.getTriggerDefnSet( ), false ) );
		}
		else
		{
			// validate the old elment

			ElementDefn contentDefn = (ElementDefn) oldElement.getDefn( );
			list.addAll( ValidationExecutor.getValidationNodes( oldElement,
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

		if ( container instanceof TableItem || container instanceof TableGroup
				|| container instanceof TableRow )
		{

			ReportItem compoundElement = LayoutUtil
					.getCompoundContainer( container );
			if ( compoundElement != null )
			{
				retValue.add( new LayoutRecordTask( module, compoundElement ) );
			}
		}

		NotificationEvent event = null;

		// send the event to the container, the container is the "target"
		// element

		event = getContainerEvent( );

		retValue.add( new NotificationRecordTask( container, event ) );

		// If the content was added, then send an element added
		// event to the content.

		if ( state != UNDONE_STATE )
		{
			if ( isSelector( newElement ) )

				retValue.add( new NotificationRecordTask( newElement, event,
						container.getRoot( ) ) );
		}
		else
		{
			if ( isSelector( oldElement ) )
				retValue.add( new NotificationRecordTask( oldElement, event,
						container.getRoot( ) ) );
		}

		return retValue;
	}

	/**
	 * Gets the event sent to the container after the record executes.
	 * Subclasses can override this method to manage the event sent to the
	 * container.
	 * 
	 * @return the event sent to the container after the record executes
	 */

	protected NotificationEvent getContainerEvent( )
	{
		NotificationEvent event = null;

		// send the content replace event to the container, the container is the
		// "target" element

		if ( state != UNDONE_STATE )
			event = new ContentReplaceEvent( container, oldElement, newElement,
					slotID );
		else
			event = new ContentReplaceEvent( container, newElement, oldElement,
					slotID );

		if ( state == DONE_STATE )
			event.setSender( sender );

		return event;
	}

}
