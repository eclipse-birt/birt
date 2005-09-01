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

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.util.EventFilter;
import org.eclipse.birt.report.model.util.NotificationChain;

/**
 * This compound record will do event filtering when it is executed, undone or
 * redone.
 * 
 */

public class FilterEventsCompoundRecord extends CompoundRecord
{

	/**
	 * Indicates if it is the outer most filter event transaction.
	 */

	private boolean isOutermostFilterTrans = false;

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger
			.getLogger( FilterEventsCompoundRecord.class.getName( ) );

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            the localized label text
	 * @param outerMost
	 *            indicates if it is the outer most filter event transaction.
	 */

	public FilterEventsCompoundRecord( String text, boolean outerMost )
	{
		super( text );
		this.isOutermostFilterTrans = outerMost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(java.util.Stack)
	 */

	public void sendNotifcations( Stack transStack )
	{
		// The current transaction is started as hold events. Checks all its
		// outer transactions, if any of them is started as hold events, the
		// notifications should still be held. Otherwise, all events
		// relating to this transaction is collected, filtered and then sent
		// out.

		if ( !isOutermostFilterTrans )
			return;

		// Collection all relating events, do fitering and then sent out.

		NotificationChain events = getNotificationChain( );
		NotificationChain filteredEvents = EventFilter.getInstance( ).filter(
				events );
		filteredEvents.fireEvents( );

		logger.log( Level.INFO, "FilterEventCompoundRecord[label=\"" //$NON-NLS-1$
				+ getLabel( ) + "\"]" + " Records filtered from [" //$NON-NLS-1$ //$NON-NLS-2$
				+ events.getEvents( ).size( ) + "] to [" //$NON-NLS-1$ //$NON-NLS-2$
				+ filteredEvents.getFilteredEvents( ).size( ) + "]" ); //$NON-NLS-1$
	}

	/**
	 * Undoes the composite record. This implementation undoes each of the
	 * sub-records in the reverse of the order that they were originally
	 * executed. Some notification events relating to the compound record will
	 * be filtered according to the <code>EventFilter</code>.
	 * 
	 * 
	 * @see ActivityRecord#undo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#undo()
	 */

	public void undo( )
	{
		for ( int i = getRecords( ).size( ) - 1; i >= 0; i-- )
		{
			ActivityRecord record = (ActivityRecord) getRecords( ).get( i );
			assert record.getState( ) == ActivityRecord.DONE_STATE
					|| record.getState( ) == ActivityRecord.REDONE_STATE;

			// Can not be a CompoundRecord.

			assert record.getClass( ) != CompoundRecord.class;
			record.undo( );
			record.performPostTasks( null );

			// Undo the record without sending notification.

			record.setState( ActivityRecord.UNDONE_STATE );
		}
	}

	/**
	 * Redoes the composite record. This implementation redoes each sub-record
	 * in the order they were originally executed. Some notification events
	 * relating to the compound record will be filtered according to the
	 * <code>EventFilter</code>.
	 * 
	 * @see ActivityRecord#redo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#redo()
	 */

	public void redo( )
	{
		for ( int i = 0; i < getRecords( ).size( ); i++ )
		{
			ActivityRecord record = (ActivityRecord) getRecords( ).get( i );
			assert record.getState( ) == ActivityRecord.UNDONE_STATE;

			// Can not be a CompoundRecord.

			assert record.getClass( ) != CompoundRecord.class;

			// Undo the record without sending notification.

			record.redo( );
			record.performPostTasks( null );
			record.setState( ActivityRecord.REDONE_STATE );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#rollback()
	 */

	public void rollback( )
	{
		for ( int i = getRecords( ).size( ) - 1; i >= 0; i-- )
		{
			ActivityRecord record = (ActivityRecord) getRecords( ).get( i );
			assert record.getClass( ) != CompoundRecord.class;

			if ( record.isPersistent( ) )
				continue;

			record.rollback( );
			record.performPostTasks( null );
		}
	}

	/**
	 * Checks whether this compound record is the outermost filter record.
	 * 
	 * @return <code>true</code> if this compound record is the outermost
	 *         filter record. Otherwise <code>false</code>.
	 */

	protected boolean isOutermostFilterTrans( )
	{
		return isOutermostFilterTrans;
	}
}
