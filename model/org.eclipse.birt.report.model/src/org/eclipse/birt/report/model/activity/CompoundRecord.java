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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.util.NotificationChain;

/**
 * A compound record represents an atomic operation made up of a series of other
 * records. Some user-visible operations require multiple low-level records to
 * implement. The compound record makes these operations appear as a single
 * atomic operation for the purpose of undo/redo.
 * <p>
 * A compound record can have an associated label that describes the high-level
 * operation. By default, the label is "Multiple Actions" or the localized
 * equivalent.
 * <p>
 * The application normally creates a compound record using the
 * {@link org.eclipse.birt.report.model.activity.ActivityStack#startTrans( String )}
 * method.
 * 
 * A compound record has a label. The system provides a default label. The
 * application can customize the label by passing the label to the
 * <code>ActivityStack.startTrans( )</code> method.
 */

public class CompoundRecord extends ActivityRecord
{

	/**
	 * The list of child records. Contents are of type ActivityRecord.
	 */

	private List recordList = new ArrayList( );

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            the localized label text
	 */

	public CompoundRecord( String text )
	{
		label = text;
	}

	/**
	 * Constructs a compound record.
	 * 
	 * @param text
	 *            the localized label text
	 * @param isPersistent
	 *            the status justifying whether the record is persistent when
	 *            rollback
	 */

	public CompoundRecord( String text, boolean isPersistent )
	{
		this.label = text;
		this.isPersistent = isPersistent;
	}

	/**
	 * Returns the list of records within this compound record.
	 * 
	 * @return the record list within the compound record.
	 */

	public List getRecords( )
	{
		return recordList;
	}

	/**
	 * Marks this compound record as complete. At this point, all records within
	 * the compound record must be in the Done state.
	 * 
	 * @see ActivityRecord#execute()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord)
	 */

	public void execute( )
	{
		assert checkState( ActivityRecord.DONE_STATE );
	}

	/**
	 * Undoes the composite record. This implementation undoes each of the
	 * sub-records in the reverse of the order that they were originally
	 * executed.
	 * 
	 * @see ActivityRecord#undo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#undo()
	 */

	public void undo( )
	{
		for ( int i = recordList.size( ) - 1; i >= 0; i-- )
		{
			ActivityRecord record = (ActivityRecord) recordList.get( i );
			assert record.getState( ) == ActivityRecord.DONE_STATE
					|| record.getState( ) == ActivityRecord.REDONE_STATE;
			record.undo( );
			record.setState( ActivityRecord.UNDONE_STATE );
			record.performPostTasks( null );
			record.sendNotifcations( null );
		}
	}

	/**
	 * Redoes the composite record. This implementation redoes each sub-record
	 * in the order they were originally executed.
	 * 
	 * @see ActivityRecord#redo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#redo()
	 */

	public void redo( )
	{
		for ( int i = 0; i < recordList.size( ); i++ )
		{
			ActivityRecord record = (ActivityRecord) recordList.get( i );
			assert record.getState( ) == ActivityRecord.UNDONE_STATE;
			record.redo( );
			record.setState( ActivityRecord.REDONE_STATE );
			record.performPostTasks( null );
			record.sendNotifcations( null );
		}
	}

	/**
	 * Checks the state of the contained records. Used in assert statements to
	 * validate the compound record state.
	 * 
	 * @param state
	 *            the state to check.
	 * @return True if all records are in that state, false otherwise.
	 */

	private boolean checkState( int state )
	{
		Iterator iter = recordList.iterator( );
		while ( iter.hasNext( ) )
		{
			ActivityRecord cmd = (ActivityRecord) iter.next( );
			if ( cmd.getState( ) != state )
				return false;
		}
		return true;
	}

	/**
	 * Reports if this record can be undone. A composite record can be undone
	 * only if each of the sub-records can be undone.
	 * 
	 * @see ActivityRecord#canUndo()
	 */

	public boolean canUndo( )
	{
		for ( Iterator records = recordList.listIterator( ); records.hasNext( ); )
		{
			ActivityRecord record = (ActivityRecord) records.next( );
			if ( !record.canUndo( ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Reports if this record can be redone. A composite record can be redone
	 * only if each of the sub-records can be redone.
	 * 
	 * @see ActivityRecord#canRedo()
	 */

	public boolean canRedo( )
	{
		for ( Iterator records = recordList.listIterator( ); records.hasNext( ); )
		{
			ActivityRecord record = (ActivityRecord) records.next( );
			if ( !record.canRedo( ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Appends a record to the compound record. The record must have been
	 * executed already. The application should not call this method directly.
	 * Instead, the record should be executed via the usual call to
	 * {@link org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord) ActivityStack.execute( )}.
	 * 
	 * @param record
	 *            the record to be added.
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord)
	 */

	public void append( ActivityRecord record )
	{
		assert record != null;
		assert record.getState( ) == ActivityRecord.DONE_STATE;
		recordList.add( record );
	}

	/**
	 * Checks if this compound record contains any nested records. If it
	 * contains no nested records, then this is a "null record" and can be
	 * discarded.
	 * 
	 * @return True if the compound record contains records, false otherwise.
	 */

	public boolean isEmpty( )
	{
		return recordList.isEmpty( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getEventChain()
	 */

	protected NotificationChain getNotificationChain( )
	{
		NotificationChain events = new NotificationChain( );
		for ( Iterator iter = recordList.iterator( ); iter.hasNext( ); )
		{
			ActivityRecord record = (ActivityRecord) iter.next( );
			NotificationChain recordEvents = record.getNotificationChain( );
			events.append( recordEvents );
		}

		return events;
	}

	/**
	 * Returns the number of records in this compound record.
	 * 
	 * @return the number of contained records
	 */

	public int getCount( )
	{
		return recordList.size( );
	}

	/**
	 * Removes the last record from the compound record.
	 * 
	 * @return the last record
	 */

	public ActivityRecord pop( )
	{
		if ( recordList.isEmpty( ) )
			return null;
		return (ActivityRecord) recordList.remove( recordList.size( ) - 1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.IValidatable#getValidators()
	 */

	public List getValidators( )
	{
		List list = new ArrayList( );
		Iterator iter = recordList.iterator( );
		while ( iter.hasNext( ) )
		{
			ActivityRecord record = (ActivityRecord) iter.next( );
			list.addAll( record.getValidators( ) );
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#rollback(org.eclipse.birt.report.model.activity.ActivityStack)
	 */

	public void rollback( )
	{
		for ( int i = recordList.size( ) - 1; i >= 0; i-- )
		{
			ActivityRecord record = (ActivityRecord) recordList.get( i );
			if ( !record.isPersistent( ) )
			{
				record.rollback( );
				record.performPostTasks( null );
				record.sendNotifcations( null );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		for ( int i = recordList.size( ) - 1; i >= 0; i-- )
		{
			ActivityRecord record = (ActivityRecord) recordList.get( i );
			if ( record instanceof CompoundRecord )
				continue;

			List simpleTasks = record.getPostTasks( );
			for ( int j = 0; j < simpleTasks.size( ); j++ )
			{
				IActivityTask task = (IActivityTask) simpleTasks.get( j );
				if ( !( task instanceof ValidationActivityTask ) )
					continue;

				List tasks = new ArrayList( );
				tasks.addAll( super.getPostTasks( ) );
				tasks.add( task );
				return tasks;
			}
		}

		return Collections.EMPTY_LIST;
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
	protected final boolean holdEvent( Stack transStack )
	{
		if ( transStack != null && !transStack.isEmpty( ) )
		{
			CompoundRecord cr = (CompoundRecord) transStack.peek( );
			if ( cr instanceof FilterEventsCompoundRecord )
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(java.util.Stack)
	 */

	public void sendNotifcations( Stack transStack )
	{
		// Ignore this operation. Notifications were sent when doing
		// the operations on the contained records.
		//
		// Indeed, doing the notifications here would result in the application
		// seeing notifications in a different state than if the records were
		// executed as atomic operations. The application expects to receive
		// notifications when the the model is in the <em>final</em> state for a
		// record, and this state holds only until the next record does an
		// operation. Deferring notifications will break this invariant, causing
		// endless problems in the UI.
	}
}