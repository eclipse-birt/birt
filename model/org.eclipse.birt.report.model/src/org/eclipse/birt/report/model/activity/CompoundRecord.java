/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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

import org.eclipse.birt.report.model.api.activity.IEventFilter;
import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.validators.ValidationNode;

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
 * {@link org.eclipse.birt.report.model.activity.ActivityStack#startTrans(String )}
 * method.
 * 
 * A compound record has a label. The system provides a default label. The
 * application can customize the label by passing the label to the
 * <code>ActivityStack.startTrans( null )</code> method.
 */

public class CompoundRecord extends ActivityRecord {

	/**
	 * The list of child records. Contents are of type ActivityRecord.
	 */

	private List<ActivityRecord> recordList = new ArrayList<ActivityRecord>();

	/**
	 * Options set for this transaction.
	 */

	protected TransactionOption options = null;

	/**
	 * Constructor.
	 * 
	 * @param text the localized label text
	 */

	public CompoundRecord(String text) {
		label = text;
	}

	/**
	 * Constructs a compound record.
	 * 
	 * @param text         the localized label text
	 * @param isPersistent the status justifying whether the record is persistent
	 *                     when rollback
	 */

	public CompoundRecord(String text, boolean isPersistent) {
		this.label = text;
		this.isPersistent = isPersistent;
	}

	/**
	 * Returns the list of records within this compound record.
	 * 
	 * @return the record list within the compound record.
	 */

	public List<ActivityRecord> getRecords() {
		return recordList;
	}

	/**
	 * Marks this compound record as complete. At this point, all records within the
	 * compound record must be in the Done state.
	 * 
	 * @see ActivityRecord#execute()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord)
	 */

	public void execute() {
		assert checkState(ActivityRecord.DONE_STATE);
	}

	/**
	 * Undoes the composite record. This implementation undoes each of the
	 * sub-records in the reverse of the order that they were originally executed.
	 * 
	 * @see ActivityRecord#undo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#undo()
	 */

	public void undo() {
		// Since undo/redo has no information about the transaction stack,
		// establish a tricky way to restore the information about the
		// transaction stack. Because filter/layout compound record only
		// includes filter/layout compound record, records in
		// <code>recordList</code> only need to know the compound record they
		// reside in. That is, "this".

		Stack<CompoundRecord> stack = new Stack<CompoundRecord>();
		stack.push(this);

		for (int i = recordList.size() - 1; i >= 0; i--) {
			ActivityRecord record = recordList.get(i);
			assert record.getState() == ActivityRecord.DONE_STATE || record.getState() == ActivityRecord.REDONE_STATE;
			record.undo();
			record.setState(ActivityRecord.UNDONE_STATE);
			record.performPostTasks(stack);
		}
	}

	/**
	 * Redoes the composite record. This implementation redoes each sub-record in
	 * the order they were originally executed.
	 * 
	 * @see ActivityRecord#redo()
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#redo()
	 */

	public void redo() {
		// Since undo/redo has no information about the transaction stack,
		// establish a tricky way to restore the information about the
		// transaction stack. Because filter/layout compound record only
		// includes filter/layout compound record, records in
		// <code>recordList</code> only need to know the compound record they
		// reside in. That is, "this".

		Stack<CompoundRecord> stack = new Stack<CompoundRecord>();
		stack.push(this);

		for (int i = 0; i < recordList.size(); i++) {
			ActivityRecord record = recordList.get(i);
			assert record.getState() == ActivityRecord.UNDONE_STATE;
			record.redo();
			record.setState(ActivityRecord.REDONE_STATE);
			record.performPostTasks(stack);
		}
	}

	/**
	 * Checks the state of the contained records. Used in assert statements to
	 * validate the compound record state.
	 * 
	 * @param state the state to check.
	 * @return True if all records are in that state, false otherwise.
	 */

	private boolean checkState(int state) {
		Iterator<ActivityRecord> iter = recordList.iterator();
		while (iter.hasNext()) {
			ActivityRecord cmd = iter.next();
			if (cmd.getState() != state)
				return false;
		}
		return true;
	}

	/**
	 * Reports if this record can be undone. A composite record can be undone only
	 * if each of the sub-records can be undone.
	 * 
	 * @see ActivityRecord#canUndo()
	 */

	public boolean canUndo() {
		for (Iterator<ActivityRecord> records = recordList.listIterator(); records.hasNext();) {
			ActivityRecord record = records.next();
			if (!record.canUndo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Reports if this record can be redone. A composite record can be redone only
	 * if each of the sub-records can be redone.
	 * 
	 * @see ActivityRecord#canRedo()
	 */

	public boolean canRedo() {
		for (Iterator<ActivityRecord> records = recordList.listIterator(); records.hasNext();) {
			ActivityRecord record = records.next();
			if (!record.canRedo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Appends a record to the compound record. The record must have been executed
	 * already. The application should not call this method directly. Instead, the
	 * record should be executed via the usual call to
	 * {@link org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord)
	 * ActivityStack.execute( )}.
	 * 
	 * @param record the record to be added.
	 * @see org.eclipse.birt.report.model.activity.ActivityStack#execute(org.eclipse.birt.report.model.api.activity.IActivityRecord)
	 */

	public void append(ActivityRecord record) {
		assert record != null;
		assert record.getState() == ActivityRecord.DONE_STATE;
		recordList.add(record);

	}

	/**
	 * return the list contains persistent transactions which are all doen status.
	 * 
	 * @return List list contains the collected persistent records.
	 */

	public List<ActivityRecord> getDonePersistentTrans() {
		List<ActivityRecord> allDonePersistentRecords = new ArrayList<ActivityRecord>();
		ActivityRecord record = null;

		for (int i = 0; i < this.recordList.size(); i++) {
			record = recordList.get(i);

			if ((record.isPersistent) && (record.state == ActivityRecord.DONE_STATE))
				allDonePersistentRecords.add(record);

			else {
				if ((record instanceof CompoundRecord)) {
					allDonePersistentRecords.addAll(((CompoundRecord) record).getDonePersistentTrans());
				}
			}
		}

		return allDonePersistentRecords;
	}

	/**
	 * Checks if this compound record contains any nested records. If it contains no
	 * nested records, then this is a "null record" and can be discarded.
	 * 
	 * @return True if the compound record contains records, false otherwise.
	 */

	public boolean isEmpty() {
		return recordList.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getEventChain()
	 */

	/**
	 * Returns the number of records in this compound record.
	 * 
	 * @return the number of contained records
	 */

	public int getCount() {
		return recordList.size();
	}

	/**
	 * Removes the last record from the compound record.
	 * 
	 * @return the last record
	 */

	public ActivityRecord pop() {
		if (recordList.isEmpty())
			return null;
		return recordList.remove(recordList.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.IValidatable#getValidators()
	 */

	public List<ValidationNode> getValidators() {
		List<ValidationNode> list = new ArrayList<ValidationNode>();
		Iterator<ActivityRecord> iter = recordList.iterator();
		while (iter.hasNext()) {
			ActivityRecord record = iter.next();
			list.addAll(record.getValidators());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.activity.ActivityRecord#rollback(org.eclipse
	 * .birt.report.model.activity.ActivityStack)
	 */

	public void rollback() {
		// Since undo/redo has no information about the transaction stack,
		// establish a tricky way to restore the information about the
		// transaction stack. Because filter/layout compound record only
		// includes filter/layout compound record, records in
		// <code>recordList</code> only need to know the compound record they
		// reside in. That is, "this".

		Stack<CompoundRecord> stack = new Stack<CompoundRecord>();
		stack.push(this);

		for (int i = recordList.size() - 1; i >= 0; i--) {
			ActivityRecord record = recordList.get(i);
			if (!record.isPersistent()) {
				record.rollback();
				record.performPostTasks(stack);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		List<RecordTask> retList = new ArrayList<RecordTask>();

		for (int i = recordList.size() - 1; i >= 0; i--) {
			ActivityRecord record = recordList.get(i);
			if (record != null) {
				retList.addAll(record.getPostTasks());
			}
		}
		return retList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#performPostTasks
	 * (java.util.Stack)
	 */

	protected void performPostTasks(Stack<CompoundRecord> transStack) {
		List<RecordTask> simpleTasks = getPostTasks();
		List<RecordTask> validationTasks = new ArrayList<RecordTask>();
		for (int i = 0; i < simpleTasks.size(); i++) {
			RecordTask task = simpleTasks.get(i);
			if (task instanceof ValidationRecordTask)
				validationTasks.add(task);
		}

		// if this record is FilterEventsCompoundRecord and stack is empty, we
		// can do the filter operation by the event filter directly, not need
		// recusively call getFilterNotificationTask like the following 'else
		// if'; it is a special case

		if (this instanceof FilterEventsCompoundRecord && transStack.isEmpty()) {
			// filter events

			assert options != null;
			IEventFilter filter = options.getEventFilter();
			assert filter != null;
			doTasks(transStack, filter.filter(getNotificationTask(simpleTasks)));

		} else if (options != null && options.getSendTime() != TransactionOption.INSTANTANEOUS_SEND_TIME) {
			// filter notification tasks and do them second

			doTasks(transStack, getFilterNotificationTask());
		}

		else if (options == null) {
			// if options is not set, then hold the notification till
			// transaction stack is empty
			if (transStack == null || transStack.isEmpty())
				doTasks(transStack, getNotificationTask(simpleTasks));
		}

		// do the validation task in the end

		if (!validationTasks.isEmpty())
			(validationTasks.get(0)).doTask(this, transStack);
	}

	/**
	 * Gets the filtered notification tasks from all the post record task list.
	 * 
	 * @return the filtered notification tasks
	 */

	private List<RecordTask> getFilterNotificationTask() {
		List<RecordTask> events = new ArrayList<RecordTask>();
		for (int i = 0; i < recordList.size(); i++) {
			ActivityRecord record = recordList.get(i);
			if (record instanceof AbstractElementRecord)
				events.addAll(getNotificationTask(record.getPostTasks()));
			else if (record instanceof CompoundRecord) {
				// only collect those event will hold till the transaction stack
				// is empty; otherwise the notifications must already be send

				CompoundRecord cr = (CompoundRecord) record;

				if ((cr instanceof FilterEventsCompoundRecord) || (cr instanceof LayoutCompoundRecord)) {
					if (!((FilterEventsCompoundRecord) cr).isOutermostFilterTrans)
						events.addAll(cr.getFilterNotificationTask());
				} else {
					TransactionOption options = cr.getOptions();
					if (options != null && options.getSendTime() == TransactionOption.OUTMOST_TRANSACTION_SEND_TIME)
						events.addAll(cr.getFilterNotificationTask());
				}

			}
		}

		// filter all the collected events

		if (options != null) {
			IEventFilter filter = options.getEventFilter();
			if (filter != null)
				events = filter.filter(events);
		}
		return events;
	}

	/**
	 * Gets the <code>NotificationRecordTask</code> list from the given post task
	 * list.
	 * 
	 * @param tasks the post task list to retrieve
	 * @return the <code>NotificationRecordTask</code> list if exists, otherwise
	 *         <code>EMPTY_LIST</code>
	 */

	private List<RecordTask> getNotificationTask(List<RecordTask> tasks) {
		if (tasks == null || tasks.isEmpty())
			return Collections.emptyList();
		List<RecordTask> events = new ArrayList<RecordTask>();
		for (int i = 0; i < tasks.size(); i++) {
			RecordTask task = tasks.get(i);
			if (task instanceof NotificationRecordTask)
				events.add(task);
		}
		return events;
	}

	/**
	 * Does a list of record tasks.
	 * 
	 * @param transStack
	 * @param tasks
	 */

	protected void doTasks(Stack<CompoundRecord> transStack, List<? extends RecordTask> tasks) {
		if (tasks == null || tasks.isEmpty())
			return;

		for (int i = 0; i < tasks.size(); i++) {
			RecordTask task = tasks.get(i);
			task.doTask(this, transStack);
		}
	}

	/**
	 * Gets the transaction option set in this record.
	 * 
	 * @return the options the options set in this record
	 */

	public TransactionOption getOptions() {
		return options;
	}

	/**
	 * Sets the option in this record.
	 * 
	 * @param options the options to set
	 */

	public void setOptions(TransactionOption options) {
		this.options = options;
	}

}
