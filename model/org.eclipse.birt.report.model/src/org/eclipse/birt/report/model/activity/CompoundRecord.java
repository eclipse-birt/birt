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
import java.util.Iterator;
import java.util.List;

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
 * {@link ActivityStack#startTrans( String )}method.
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
	 * @see ActivityStack#execute(ActivityRecord)
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
	 * @see ActivityStack#undo()
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
			record.sendNotifcations( true );
		}
	}

	/**
	 * Redoes the composite record. This implementation redoes each sub-record
	 * in the order they were originally executed.
	 * 
	 * @see ActivityRecord#redo()
	 * @see ActivityStack#redo()
	 */

	public void redo( )
	{
		for ( int i = 0; i < recordList.size( ); i++ )
		{
			ActivityRecord record = (ActivityRecord) recordList.get( i );
			assert record.getState( ) == ActivityRecord.UNDONE_STATE;
			record.redo( );
			record.setState( ActivityRecord.REDONE_STATE );
			record.sendNotifcations( true );
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
	 * {@link ActivityStack#execute(ActivityRecord) ActivityStack.execute( )}.
	 * 
	 * @param record
	 *            the record to be added.
	 * @see ActivityStack#execute(ActivityRecord)
	 */

	protected void append( ActivityRecord record )
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

	/**
	 * This is a null operation in a composite record. Instead, notifications
	 * must have been sent when executing, undoing or redoing each sub-record.
	 * 
	 * @see ActivityRecord#sendNotifcations( boolean transactionStarted )
	 */

	protected void sendNotifcations( boolean transactionStarted )
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
}