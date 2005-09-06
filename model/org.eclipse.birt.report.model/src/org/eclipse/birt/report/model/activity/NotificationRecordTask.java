/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.Stack;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;

/**
 * The task to send notifications to corresponding element after the execution
 * of records.
 * 
 */

public class NotificationRecordTask extends RecordTask
{

	/**
	 * Root element.
	 */

	private Module root = null;

	/**
	 * The wrappered notification event.
	 */

	private NotificationEvent event = null;

	/**
	 * A flag used when we need to filter a list of events. Indicating whether
	 * or not a event needs to be filtered.
	 * 
	 */

	private boolean filtered = false;

	/**
	 * Constructs a new event wrapper,
	 * <code>target.broadcast( event, root )</code> will be called when the
	 * notification event is sent.
	 * 
	 * @param target
	 *            Target element where the event will be sent to.
	 * @param event
	 *            The wrappered notification event.
	 */

	public NotificationRecordTask( DesignElement target, NotificationEvent event )
	{
		super( target );

		this.event = event;
		this.root = target.getRoot( );
	}

	/**
	 * Constructs a new Notification instance. Sometimes the target element is
	 * dropped from the design tree when the notification is sent. So original
	 * root element should be specified explicitly.
	 * <code>target.broadcast( event, root )</code> will be called when the
	 * notification event is sent.
	 * 
	 * @param target
	 *            Target element where the event will be sent to.
	 * @param event
	 *            The wrappered notification event.
	 * @param rootElement
	 *            the root node of the design tree.
	 */

	public NotificationRecordTask( DesignElement target,
			NotificationEvent event, Module rootElement )
	{
		super( target );

		this.event = event;
		this.root = rootElement;
	}

	/**
	 * Constructs a new event wrapper, <code>target.broadcast( event )</code>
	 * will be called when the notification is sent.
	 * 
	 * @param target
	 *            Target element where the event will be sent to.
	 * @param event
	 *            The wrappered notification event.
	 */

	public NotificationRecordTask( ReferencableStructure target,
			NotificationEvent event )
	{
		super( target );
		this.event = event;
	}

	/**
	 * Sets the filter flag, indicating whether or not the event will be
	 * filtered.
	 * 
	 * @param filtered
	 *            a filter flag, indicating whether or not the event will be
	 *            filtered.
	 */

	public void setFiltered( boolean filtered )
	{
		this.filtered = filtered;
	}

	/**
	 * Returns the flag indication whether or not the event will be filtered.
	 * 
	 * @return The flag indication whether or not the event will be filtered.
	 */

	public boolean isFiltered( )
	{
		return filtered;
	}

	/**
	 * Returns the wrappered notification event.
	 * 
	 * @return the wrappered notification event.
	 */

	public NotificationEvent getEvent( )
	{
		return event;
	}

	/**
	 * Returns <code>true</code> if need to hold the event at this time. We
	 * need to hold the event if it is sent inside a transaction that declared
	 * to filter notification events( <code>FilterEventsCompoundRecord</code>).
	 * 
	 * @param transStack
	 *            the transaction stack.
	 * @return <code>true</code> if need to hold the event at this time,
	 *         returns <code>false</code> otherwise.
	 */

	protected final boolean holdNotification( Stack transStack )
	{
		if ( transStack != null && !transStack.isEmpty( ) )
		{
			CompoundRecord cr = (CompoundRecord) transStack.peek( );
			if ( cr instanceof FilterEventsCompoundRecord )
				return true;
		}

		return false;
	}

	/**
	 * Fire event to the target.
	 */

	public void doTask( ActivityRecord record, Stack transStack )
	{
		if ( holdNotification( transStack ) )
			return;

		if ( getTarget( ) instanceof DesignElement )
		{
			// case 1:
			// StyleHandle label = factory.newStyle( label );
			// label.setProperty();
			// root == null, should not broadcast events.

			// case 2:
			// dataSetHandle.setDataSource( dataSourceHandle );
			// dataSourceHandle.drop();
			//		
			// Datasource not on the tree, its root is explicitly specified.
			// event should be broadcast.

			DesignElement elementTarget = (DesignElement) getTarget( );
			Module theRoot = this.root == null
					? elementTarget.getRoot( )
					: this.root;

			if ( getTarget( ) instanceof Module || theRoot != null )
				elementTarget.broadcast( event, theRoot );

			if ( event instanceof ElementDeletedEvent )
			{
				elementTarget.clearListeners( );
			}

		}
		else if ( getTarget( ) instanceof ReferencableStructure )
		{
			( (ReferencableStructure) getTarget( ) ).broadcast( event );
		}
		else
			assert false;
	}
}
