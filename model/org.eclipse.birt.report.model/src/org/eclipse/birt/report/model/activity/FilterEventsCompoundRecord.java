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

import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.util.ModelUtil;

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

	protected void performPostTasks( Stack transStack )
	{
		// The current transaction is started as hold events. Checks all its
		// outer transactions, if any of them is started as hold events, the
		// notifications should still be held. Otherwise, all events
		// relating to this transaction is collected, filtered and then sent
		// out.

		if ( !isOutermostFilterTrans )
			return;

		// Collection all relating events, do fitering and then sent out.

		List tasks = ModelUtil
				.filterNotificationTasks( getPostTasks( ) );

		for ( int i = 0; i < tasks.size( ); i++ )
		{
			NotificationRecordTask subTask = (NotificationRecordTask) tasks
					.get( i );

			subTask.doTask( this, transStack );
		}

		super.performPostTasks( transStack );
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
