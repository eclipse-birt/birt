/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * A transaction that do not send out notifications to elements. All events in
 * the transaction will be discarded. Meanwhile, all post tasks for a slient
 * transaction are held until the commit of this transaction.
 */

public class LayoutCompoundRecord extends FilterEventsCompoundRecord {

	/**
	 * Constructor.
	 * 
	 * @param text                   the localized label text
	 * @param isOutermostSilentTrans indicates if it is the outer most filter event
	 *                               transaction.
	 * @param filterAll              <code>true</code> if do not send any
	 *                               notification except Layout event.
	 */

	public LayoutCompoundRecord(String text, boolean isOutermostSilentTrans) {
		super(text, isOutermostSilentTrans);
	}

	/**
	 * Sets the filter all flag. If it is <code>true</code>, don't send out any
	 * event.
	 * 
	 * @param filterAll <code>true</code> means don't send out any event.
	 */

	protected void setFilterAll(boolean filterAll) {
		if (isOutermostFilterTrans && filterAll) {
			options = new TransactionOption();
			options.setEventfilter(new FullEventFilter());
			options.setSendTime(TransactionOption.OUTMOST_TRANSACTION_SEND_TIME);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#performPostTasks
	 * (java.util.Stack)
	 */

	protected void performPostTasks(Stack<CompoundRecord> transStack) {
		if (!isOutermostFilterTrans)
			return;

		// do the layout tasks

		List<RecordTask> layoutTasks = ModelUtil.filterLayoutTasks(getPostTasks());
		doTasks(transStack, layoutTasks);

		super.performPostTasks(transStack);
	}

	/**
	 * Undoes the composite record. This implementation undoes each of the
	 * sub-records in the reverse of the order that they were originally executed.
	 * Some notification events relating to the compound record will be filtered
	 * according to the <code>EventFilter</code>.
	 * 
	 * 
	 * @see ActivityRecord#undo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#undo()
	 */

	public void undo() {
		for (int i = getRecords().size() - 1; i >= 0; i--) {
			ActivityRecord record = getRecords().get(i);
			assert record.getState() == ActivityRecord.DONE_STATE || record.getState() == ActivityRecord.REDONE_STATE;

			// Can not be a CompoundRecord.

			assert record.getClass() != CompoundRecord.class;
			record.undo();

			// Undo the record without sending notification.

			record.setState(ActivityRecord.UNDONE_STATE);
		}
	}

	/**
	 * Redoes the composite record. This implementation redoes each sub-record in
	 * the order they were originally executed. Some notification events relating to
	 * the compound record will be filtered according to the
	 * <code>EventFilter</code>.
	 * 
	 * @see ActivityRecord#redo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#redo()
	 */

	public void redo() {
		for (int i = 0; i < getRecords().size(); i++) {
			ActivityRecord record = getRecords().get(i);
			assert record.getState() == ActivityRecord.UNDONE_STATE;

			// Can not be a CompoundRecord.

			assert record.getClass() != CompoundRecord.class;

			// Undo the record without sending notification.

			record.redo();
			record.setState(ActivityRecord.REDONE_STATE);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#rollback()
	 */

	public void rollback() {
		for (int i = getRecords().size() - 1; i >= 0; i--) {
			ActivityRecord record = getRecords().get(i);
			assert record.getClass() != CompoundRecord.class;

			if (record.isPersistent())
				continue;

			record.rollback();
		}
	}
}
