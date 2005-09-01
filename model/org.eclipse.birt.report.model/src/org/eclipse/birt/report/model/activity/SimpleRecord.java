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
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Convenience class to automate routine records that work directly with a
 * design element. Execute, undo and redo call the {@link #perform perform( )}
 * method, making it easy to implement simple operations, especially when a
 * single record implements two different related operations (such as add and
 * delete).
 * <p>
 * Derived commands that must create a "memento" record the initial state should
 * do so in the constructor. This means that the constructor should gather all
 * the data needed to perform the record.
 * 
 */

public abstract class SimpleRecord extends AbstractElementRecord
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#execute()
	 */

	public void execute( )
	{
		perform( false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#undo()
	 */

	public void undo( )
	{
		perform( true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#redo()
	 */

	public void redo( )
	{
		perform( false );
	}

	/**
	 * Performs the actual operation.
	 * 
	 * @param undo
	 *            whether to undo (true) or execute/redo (false) the operation.
	 */

	protected abstract void perform( boolean undo );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#rollback()
	 */

	public void rollback( )
	{
		undo( );
		setState( ActivityRecord.UNDONE_STATE );
	}

	/**
	 * Performs tasks after the execution of the record.
	 * 
	 * @param transStack
	 */

	protected void performPostTasks( Stack transStack )
	{
		if ( holdTask( transStack ) )
			return;

		super.performPostTasks( transStack );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List getPostTasks( )
	{
		DesignElement element = getTarget( );
		assert element != null;
		if ( element.getRoot( ) == null )
			return Collections.EMPTY_LIST;

		IActivityTask task = new ValidationActivityTask( element.getRoot( ) );
		List retList = new ArrayList( );
		retList.addAll( super.getPostTasks( ) );
		retList.add( task );
		return retList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations(java.util.Stack)
	 */
	
	public void sendNotifcations( Stack transStack )
	{
		if ( !holdNotification( transStack ) )
			super.sendNotifcations( transStack );
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
	 * Returns <code>true</code> if need to hold the event at this time. We
	 * need to hold the event if it is sent inside a transaction that declared
	 * to filter notification events( <code>FilterEventsCompoundRecord</code>).
	 * 
	 * @param transStack
	 *            the transaction stack.
	 * @return <code>true</code> if need to hold the event at this time,
	 *         returns <code>false</code> otherwise.
	 */
	
	protected final boolean holdTask( Stack transStack )
	{
		if ( transStack != null && !transStack.isEmpty( ) )
		{
			CompoundRecord cr = (CompoundRecord) transStack.peek( );
			if ( cr instanceof SilentCompoundRecord )
				return true;
		}
	
		return false;
	}
}