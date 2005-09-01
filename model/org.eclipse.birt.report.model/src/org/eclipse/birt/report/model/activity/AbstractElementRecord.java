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

package org.eclipse.birt.report.model.activity;


import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.NotificationChain;

/**
 * This class is the base class for all simple activity records. A simple
 * activity record performs one operation. The <em>target element</em> is the
 * one that is changed. This class provides a standard implementation for
 * sending change notifications.
 *  
 */

public abstract class AbstractElementRecord extends ActivityRecord
{

	/**
	 * Constructor.
	 */

	public AbstractElementRecord( )
	{
	}

	/**
	 * Gets the element that is the target of the record. This is the element
	 * that has changed and that will broadcast notifications.
	 * 
	 * @return the target element.
	 */

	abstract public DesignElement getTarget( );

	/**
	 * Creates the notification event for this record. Derived classes use the
	 * command state to compute the correct event. Often, the undo event is the
	 * opposite of the execute & redo event. Check the command state to
	 * determine which notification to create.
	 * 
	 * @return the notification event.
	 */

	abstract public NotificationEvent getEvent( );

	/**
	 * Returns a chain of events relating to this record. This implementation
	 * uses <code>getEvent( )</code> to produce the notification, and the
	 * target of the event is returned by <code>getTarget( )
	 * 
	 * @return  a chain of events relating to this record
	 */

	protected NotificationChain getNotificationChain( )
	{
		// Get the target element. There should be one unless this is a
		// "null command" that does nothing.

		DesignElement target = getTarget( );

		// Create the event. The event is required if there is a target
		// element.

		NotificationEvent event = getEvent( );

		// Some record do not need a notification, e.g: BackRefRecord.
		
		if( event == null )
			return NotificationChain.EMPTY_CHAIN;
		
		assert target != null;
		
		// Include the sender if this is the original execution.
		// The sender is not sent for undo, redo because such actions are
		// triggered by the activity stack, not dialog or editor.

		if ( state == DONE_STATE )
			event.setSender( sender );

		return new NotificationChain( ).append( target, event );
	}
}