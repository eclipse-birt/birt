/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.List;
import java.util.Stack;

/**
 * The adapter to work on the specified compound record.
 * 
 */

class TransactionAdapter {

	/**
	 * The regular compound record.
	 */

	static final int DEFAULT_RECORD = 0;

	/**
	 * The record to filter events.
	 */

	static final int FILTER_RECORD = 1;

	/**
	 * The record to work on table/grid layout related.
	 */

	static final int LAYOUT_RECORD = 2;

	/**
	 * The persistent record that cannot be rollback.
	 */

	static final int PERSISTENT_RECORD = 3;

	/**
	 * The record that cannot be undo and rollback.
	 */

	static final int NONUNDOABLE_RECORD = 4;

	/**
	 * 
	 */

	private ActivityStack stack;

	/**
	 * 
	 */

	protected Stack<List<ActivityRecord>> needUndoPersistentRecords = new Stack<List<ActivityRecord>>();

	/**
	 * Constructor.
	 * 
	 * @param stack
	 */

	TransactionAdapter(ActivityStack stack) {
		this.stack = stack;
	}

	/**
	 * Commits the current transaction. There must be an active transaction. If
	 * nested transactions are active, this method will finish the inner- most
	 * transaction.
	 * 
	 * @param record
	 * 
	 * @see #startTrans(String)
	 */

	void handleCommit(ActivityRecord record) {
		Stack<CompoundRecord> transStack = stack.transStack;
		Stack<ActivityRecord> undoStack = stack.undoStack;

		if (transStack.empty()) {
			if (!(record instanceof NonUndoableCompoundRecord)) {
				undoStack.push(record);
				handlePersistentRecords(undoStack);
			}
		}
	}

	/**
	 * @param undoStack
	 */

	private void handlePersistentRecords(Stack<ActivityRecord> undoStack) {
		if (!needUndoPersistentRecords.isEmpty()) {
			List<ActivityRecord> needToUndoRecords = null;

			while (!needUndoPersistentRecords.isEmpty()) {
				needToUndoRecords = needUndoPersistentRecords.pop();
				for (int j = 0; j < needToUndoRecords.size(); j++) {
					(needToUndoRecords.get(j)).setTransNo(stack.increaseTransCount());
					undoStack.push(needToUndoRecords.get(j));
				}
			}

		}
		stack.trimUndoStack();
	}

	/**
	 * Undoes all actions done so far in the innermost transaction. Does not undo
	 * any parent transactions.
	 * 
	 * @param record the record that rolls back
	 */

	protected void handleRollback(CompoundRecord record) {
		Stack<CompoundRecord> transStack = stack.transStack;
		List<ActivityRecord> persistentRecord = record.getDonePersistentTrans();
		Stack<ActivityRecord> undoStack = stack.undoStack;

		if (persistentRecord.size() != 0) {
			if (!transStack.isEmpty()) {
				needUndoPersistentRecords.push(persistentRecord);
			} else {
				for (int i = 0; i < persistentRecord.size(); i++) {
					(persistentRecord.get(i)).setTransNo(stack.increaseTransCount());
					undoStack.push(persistentRecord.get(i));
				}

				stack.trimUndoStack();
			}
		} else if (transStack.isEmpty()) {
			handlePersistentRecords(undoStack);
		}
	}

	/**
	 * Returns a new compound record according to the the given record type. The
	 * record type can be one of followings:
	 * 
	 * <ul>
	 * <li>FILTER_RECORD
	 * <li>LAYOUT_RECORD
	 * <li>PERSISTENT_RECORD
	 * <li>NONUNDOABLE_RECORD
	 * <li>DEFAULT_RECORD
	 * </ul>
	 * 
	 * @param recordType the type of the compound record
	 * @param label      the transaction label
	 * @return the new compound record
	 */

	protected CompoundRecord createNewRecord(int recordType, String label) {
		Stack<CompoundRecord> transStack = stack.transStack;
		CompoundRecord retRecord = null;

		boolean outerMost = true;

		switch (recordType) {
		case FILTER_RECORD:

			if (!transStack.isEmpty()) {
				if (transStack.peek() instanceof LayoutCompoundRecord)
					return createNewRecord(LAYOUT_RECORD, label);
				else if (transStack.peek() instanceof NonUndoableCompoundRecord)
					return createNewRecord(NONUNDOABLE_RECORD, label);
			}

			if (!transStack.isEmpty() && transStack.peek() instanceof FilterEventsCompoundRecord)
				outerMost = false;

			retRecord = new FilterEventsCompoundRecord(label, outerMost);

			break;
		case LAYOUT_RECORD:

			if (!transStack.isEmpty() && transStack.peek() instanceof LayoutCompoundRecord)
				outerMost = false;

			retRecord = new LayoutCompoundRecord(label, outerMost);
			break;
		case PERSISTENT_RECORD:

			retRecord = new CompoundRecord(label, true);
			break;

		case NONUNDOABLE_RECORD:
			retRecord = new NonUndoableCompoundRecord(label);
			break;
		default:

			if (!transStack.isEmpty()) {
				if (transStack.peek() instanceof LayoutCompoundRecord)
					return createNewRecord(LAYOUT_RECORD, label);
				else if (transStack.peek() instanceof FilterEventsCompoundRecord)
					return createNewRecord(FILTER_RECORD, label);
				else if (transStack.peek() instanceof NonUndoableCompoundRecord)
					return createNewRecord(NONUNDOABLE_RECORD, label);
			}

			retRecord = new CompoundRecord(label);
			break;
		}

		return retRecord;
	}
}
