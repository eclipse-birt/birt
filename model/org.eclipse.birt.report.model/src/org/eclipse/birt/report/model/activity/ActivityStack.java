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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.IActivityRecord;
import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.core.Module;

/**
 * An implementation of a command stack, called an "activity stack" here. The
 * activity stack manages the execution, undo, and redo of {@link ActivityRecord
 * activity records}.
 * <p>
 * Executed records are pushed onto the undo stack. Records that are undone are
 * popped off the undo stack and pushed onto the redo stack. Records that are
 * redone are popped off the redo stack and pushed back onto the undo stack.
 * Thus, the most recently executed (or redone) record is at the top of the undo
 * stack, and the most recently redone record is at the top of the redo stack.
 * <p>
 * The record stack imposes a limit on the undo stack. When the undo stack grows
 * beyond this limit, the oldest record is flushed.
 *
 * <h3>Theory of Operation</h3>
 *
 * The record mechanism relies on accurate implementation of each record.
 * Records change the state of the model, and rely on accurate model state
 * transitions so that undo and redo can be done The following rules must hold:
 * <p>
 * <ul>
 * <li>The execution of a record transitions the model from one state, called
 * the <em>initial</em> state, to another state, called the <em>final</em>
 * state. Think of a model state as a snapshot of the model. Every change of
 * state is done by a record. If the model starts in state S0, then record C1
 * will have S0 as its initial state. ActivityRecord C1 moves the model into
 * state S1, the final state of record C1. A later record C2 will move the model
 * into a new state, S2, and so on.</li>
 *
 * <li>Both the initial and final states must be <em>valid</em> model states. A
 * valid state is one that satisfies all the semantic constraints of the model.
 * (For example that if an element has a value for property P, then P is a
 * property defined on that element.) Therefore, the record should assume that
 * the model starts out in a valid form, and must leave the model in a valid
 * form.</li>
 *
 * <li>While the record executes, is undone, or is redone, the model may
 * temporarily be in an invalid <em>transition</em> state. No application logic
 * should execute during the transition state. This state must be seen
 * <strong>only </strong> by the record itself, since only this record
 * understands this state.</li>
 * <li>The undo of a record must transition the model from the final state back
 * to the initial state.</li>
 *
 * <li>The redo of a record must again transition the model from the initial
 * state to the final state.</li>
 *
 * <li>Suppose the application issues three records in succession: C1, C2 and
 * C3. Then the final state of C1 is the initial state of C2. And the final
 * state of C2 is the initial state of C3.</li>
 *
 * <li>Whenever a new record is executed, the redo stack is flushed.</li>
 *
 * <li><strong>All </strong> changes to the model are done by records.</li>
 * </ul>
 * <p>
 * To understand why these rules are important, consider how records work.
 * Suppose we have a record that is to delete an element E. Obviously, for the
 * record to work, element E must exist and must be part of the model. This is
 * the initial state, and is a valid model state. When the record executes,
 * element E must be removed from the model and any dangling references must be
 * cleaned up. The result is a final state that is also a valid state.
 * <p>
 * In order for the above record to undo, the exact same final state must obtain
 * at the time we undo the record. That means, for example, that element E must
 * still be deleted, and no new element can appear in the model with the same
 * name as the deleted element. These constraints allow the record to reverse
 * its effect without fear of invalidating the design. The result is that the
 * model transitions back to the initial state.
 * <p>
 * This careful transition of states works only if records are executed, undone,
 * and redone in exactly the right order. Records must be undone in the order
 * that they were done. Suppose that they are not. In this case, when record C
 * is undone, it will find that the model is not in the expected final state for
 * record C, and thus there is no guarantee that doing the undo will leave the
 * model in either the initial state or even a valid state.
 * <p>
 * It is for this reason that executing a record must flush the redo stack. The
 * effect of the new record is to move the model to a state different than the
 * final state of the first record on the redo stack. Hence, that record can no
 * longer be redone and must be flushed.
 * <p>
 * Similarly, the record system works only if all changes to the model are done
 * through records. Suppose that some change is made without a record. The
 * result is that the model has changed from state Sn to state Sn+1. However,
 * the record at the top of the undo state still expects the model to be in its
 * final state: state Sn.
 *
 * <h3>Notifications</h3>
 *
 * The stack is responsible for sending out notification events as records
 * execute (do, undo or redo). Each record sends out a notification event at the
 * request of the stack. ActivityRecord in the stacks have a state that must
 * match the stack on which they appear. The state is used to compute
 * notification events for the record.
 * <p>
 * Notifications are sent as soon as a record is executed, even in the case of a
 * transaction. An alternative (more complex) implementation could provide an
 * additional hint to the UI that a transaction is in effect so that the UI can
 * batch up updates without repeating the same update unnecessarily. This
 * advanced mechanism would require some kind of "end update" event to be sent
 * to every listener that received a notification. Such a mechanism is beyond
 * the scope of this initial implementation, but can be added later if a proven
 * need arises.
 * <p>
 * Notifications are easiest to manage if each record affects a single element
 * called the target element. This is the element that will broadcast
 * notifications. The {@link AbstractElementRecord simple record}class automates
 * notifications for this case. More complex implementations are possible, but
 * the use of a transaction (or compound record) may be simpler in many cases.
 *
 * <h3>Scope of Change and Transactions</h3>
 *
 * Each record affects a single element. However, many application-level
 * operations affect multiple elements. For example, an element may be
 * referenced by multiple elements. If the application wants to delete this
 * element, it must first remove the references, then do the deletion. Removing
 * references affects other elements, and so must be done as separate records.
 * Still, from the user perspective, the entire operation is an atomic
 * operation, to be undo and redone together. The solution is to use a
 * <em>compound record</em>. The compound record is undone and redone as unit.
 * The detailed operations are automatically performed in the proper order.
 * <p>
 * Above we said that a record must leave the model in a valid state. This rule
 * applies only to top-level records. It is expected that the model may be in an
 * intermediate state during part of a compound record. For example, suppose we
 * want to delete an element E that is referenced by three other elements. We
 * would implement this as a compound record. The first three sub-records would
 * remove the references to E. Then, the final sub-record would delete E itself.
 * The initial and final states of the overall compound record are valid, but
 * the states between sub-records are not necessarily valid.
 * <p>
 * As a convenience for the application, the compound record implementation is
 * abstracted into the concept of a <em>transaction</em>. The application simply
 * calls startTrans( null ) to start a series of operations to be treated as
 * atomic, and calls commit( ) to complete the series. For convenience,
 * transactions can nest to any depth. Only the outermost transaction shows up
 * as a an undoable or redoable record in the UI.
 * <p>
 * It is important to understand when records execute within a transaction.
 * Records execute during the call to execute( ) on the record stack. That is,
 * whether or not a record is part of a transaction, it executes immediately
 * when added to the record stack. Note that this model differs from other
 * systems, such as GEF, that defers execution of nested records until the
 * compound record itself executes. However, the incremental execution model of
 * this implementation means that the application need not know whether a record
 * is part of a transaction or not, the effect on the model state is identical.
 * This model is important to allow code to work directly with the model,
 * knowing that the model reflects the series of records executed previously.
 *
 * <h3>Error Handling</h3>
 *
 * The record stack is a low-level operation primarily responsible for managing
 * record history. Records are defined to always work. That is, when a record is
 * told to execute, undo or redo, the record operation must succeed. If there
 * are issues that might prevent the execution from succeeding, they must be
 * checked by the application <em>before</em> issuing the record. If a record
 * returns true from canUndo( ), then it must successfully undo, assuming that
 * the state of the model is correct. Similarly, if a record returns true from
 * canRedo( ), then the redo must succeed, again assuming the model state is
 * valid. The only way the model state could be invalid is due to a programming
 * error, and so asserts should be used to verify that the state is valid.
 * Another way of saying this is that records <em>cannot</em> throw
 * application-level exceptions: they must succeed. Any exceptions thrown would
 * be system-level exceptions such as null- pointer violations, out-of-memory
 * errors and other generic problems.
 *
 * <h3>Persistence Support</h3>
 *
 * The record stack contains a dirty property. This property can be used to
 * determine when persisting the Records' changes is required. Upon
 * construction, the undo stack is empty, and is not dirty.
 *
 * <h3>Dirty State Support</h3>
 *
 * A design is considered <em>dirty</em> if the in-memory state differs from
 * that on disk. There are three primary considerations:
 * <p>
 * <ul>
 * <li>The user has made changes to the file since it was loaded or saved. The
 * file is considered dirty.</li>
 * <li>The user has made changes, then undone them so that the file is back to
 * the same state as on disk. In this case, the file is <em>not</em> dirty.</li>
 * <li>The user makes some changes, saves the file, then undoes the changes. The
 * file <em>is</em> considered dirty: the in-memory state represents an
 * "earlier" version of the file than what is stored on disk.</li>
 * </ul>
 * <p>
 * The activity stack is the primary mechanism for tracking dirty state. There
 * are three values we track:
 * <p>
 * <ul>
 * <li>A transaction counter. Each new transaction (including those that consist
 * of a single command) is assigned a monotonically increasing serial number.
 * <li>
 * <li>The current transaction number. This is the one at the top of the undo
 * stack. If the undo stack size is empty, then it is computed from the
 * transaction counter.
 * <li>
 * <li>The save state of the design. This is the current transaction number in
 * effect at the time the design was last saved. (By definition, the save state
 * and transaction counter are both 0 when a design is created or loaded.)</li>
 * </ul>
 * <p>
 * With the above, it is very easy to detect a dirty design: a design is dirty
 * if and only if its save state differs from the current transaction number.
 *
 * <h3>Notification</h3>
 *
 * Any class interested in the activity stack can implement the interface
 * <code>ActivityStackListener</code>. And {@link #addListener}and
 * {@link #removeListener}methods add and remove listeners. The listener class
 * will be notified when {@link #execute(IActivityRecord)},{@link #commit()},
 * {@link #undo()}, or {@link #redo()}is called. The event
 * <code>ActivityStackEvent</code> contains the cause of the activity stack
 * change.
 *
 */

public class ActivityStack implements CommandStack {

	/**
	 * The default stack size limit.
	 */

	public static final int DEFAULT_STACK_LIMIT = 500;

	/**
	 * The undo stack. Entries are of type ActivityRecord.
	 */

	protected Stack<ActivityRecord> undoStack = new Stack<>();

	/**
	 * The redo stack. Entries are of type ActivityRecord.
	 */

	protected Stack<ActivityRecord> redoStack = new Stack<>();

	/**
	 * The active transaction stack. Entries are of type CompoundCommand.
	 */

	protected Stack<CompoundRecord> transStack = new Stack<>();

	/**
	 * The adapter for the specified compound records.
	 */

	protected TransactionAdapter adapter = null;

	/**
	 * The stack size limit. The limit applies to the undo stack. Since the redo
	 * stack holds items from the undo stack, by definition the redo stack can never
	 * hold more items than the undo stack once held.
	 */

	private int stackLimit = DEFAULT_STACK_LIMIT;

	/**
	 * The transaction counter. Incremented for each new transaction, including
	 * simple top-level commands.
	 */

	private int transCount = 0;

	/**
	 * Listeners are the objects that want to be notified of events. Contents are of
	 * type Listener. Created only when needed.
	 */

	protected ArrayList<ActivityStackListener> listeners = null;

	/**
	 * The host module where this activity stack resides.
	 */

	protected Module module = null;

	/**
	 * Default constructor.
	 */

	public ActivityStack(Module module) {
		adapter = new TransactionAdapter(this);
		this.module = module;
	}

	/**
	 * Executes the specified extended element command. The command must be ready to
	 * execute. As noted above, any required checks must have already been done.
	 * Flushes the redo stack.
	 *
	 * @param command the ActivityRecord to execute
	 */

	@Override
	public void execute(IElementCommand command) {
		ExtensionActivityRecord record = new ExtensionActivityRecord(command);

		// The record must exist and must be in the initial state.

		execute(record);
	}

	/**
	 * Executes the specified record. The record must be ready to execute. As noted
	 * above, any required checks must have already been done. Flushes the redo
	 * stack.
	 *
	 * @param executeRecord the ActivityRecord to execute
	 */

	@Override
	public void execute(IActivityRecord executeRecord) {
		ActivityRecord record = (ActivityRecord) executeRecord;

		// The record must exist and must be in the initial state.

		assert record != null;
		assert record.getState() == ActivityRecord.INITIAL_STATE;

		// Execute the record and push it onto the undo stack.

		record.execute();
		record.setState(ActivityRecord.DONE_STATE);

		// if module is in the caching state and any record is executed, then
		// the cache must be disabled
		clearCachedValues();

		assert !(record instanceof CompoundRecord);

		record.performPostTasks(transStack);

		// Add the record to the undo stack if it is a singleton, or
		// to the current transaction if one is in effect.

		if (transStack.isEmpty()) {
			// Flush the redo stack.

			destroyRecords(redoStack);

			record.setTransNo(++transCount);
			undoStack.push(record);
			trimUndoStack();

			sendNotifcations(new ActivityStackEvent(this, ActivityStackEvent.DONE));

		} else {
			CompoundRecord trans = transStack.lastElement();
			trans.append(record);
		}

	}

	/**
	 * Sets the cache status and clear the cached values.
	 */
	private void clearCachedValues() {
		// if module is in the caching state and any record is executed, then
		// the cache must be disabled
		if (module != null && module.isCached()) {
			module.setIsCached(false);
		}
	}

	/**
	 * Undoes the most recently executed (or redone) record. The record is popped
	 * from the undo stack to and pushed onto the redo stack. This method should
	 * only be called when {@link #canUndo()}returns <code>true</code>. Undo
	 * <em>cannot</em> be called while a transaction is active.
	 * <p>
	 * <em><strong>Note</strong>: It is possible to redefine undo( ) to reverse the
	 * action of a record within a transaction. We defer implementation of this
	 * feature until it the application has a demonstrated need for this
	 * feature.</em>
	 */

	@Override
	public void undo() {
		// Should only be called when there is a record to redo.

		assert canUndo();

		// Redo the record.

		ActivityRecord record = undoStack.pop();
		assert record.getState() == ActivityRecord.DONE_STATE || record.getState() == ActivityRecord.REDONE_STATE;
		record.undo();
		record.setState(ActivityRecord.UNDONE_STATE);

		// clear cached values
		clearCachedValues();

		// Push the record onto the redo stack.

		redoStack.push(record);

		record.performPostTasks(transStack);

		// listener, transaction, not go into transaction stack

		sendNotifcations(new ActivityStackEvent(this, ActivityStackEvent.UNDONE));
	}

	/**
	 * Calls redo on the ActivityRecord at the top of the redo stack, and pushes
	 * that record onto the undo stack. This method should only be called when
	 * {@link #canUndo()}returns <code>true</code>.
	 */

	@Override
	public void redo() {
		// Should only be called when there is a record to redo.

		assert canRedo();

		// Redo the record.

		ActivityRecord record = redoStack.pop();
		assert record.getState() == ActivityRecord.UNDONE_STATE;
		record.redo();
		record.setState(ActivityRecord.REDONE_STATE);

		// clear cached values
		clearCachedValues();

		// Push the record back onto the undo stack. No need to check
		// stack size here, it can't get any larger than it was when
		// we originally executed the record.

		undoStack.push(record);

		record.performPostTasks(transStack);

		// Send notifications.

		sendNotifcations(new ActivityStackEvent(this, ActivityStackEvent.REDONE));
	}

	/**
	 * Determines if the record stack has a record to undo. There is a record to
	 * undo if
	 * <nl>
	 * <li>no transaction is active,</li>
	 * <li>the undo stack is not empty, and
	 * <li>the top record can be undone.</li>
	 * </nl>
	 *
	 * @return <code>true</code> if {@link #undo()}can be called
	 */

	@Override
	public boolean canUndo() {
		if (!transStack.isEmpty()) {
			return false;
		} else if (undoStack.size() == 0) {
			return false;
		} else {
			ActivityRecord record = undoStack.lastElement();
			return record.canUndo();
		}
	}

	/**
	 * Determines if the record stack has a record to redo. There is a record to
	 * redo if
	 * <nl>
	 * <li>no transaction is active,</li>
	 * <li>the redo stack is not empty, and</li>
	 * <li>the top record can be redone.</li>
	 * </nl>
	 *
	 * @return <code>true</code> if {@link #redo()}can be called.
	 */

	@Override
	public boolean canRedo() {
		if (!transStack.isEmpty()) {
			return false;
		} else if (redoStack.size() == 0) {
			return false;
		} else {
			ActivityRecord record = redoStack.lastElement();
			return record.canUndo();
		}
	}

	/**
	 * If the undo stack has grown too large, discard the oldest entries.
	 */

	protected void trimUndoStack() {
		while (undoStack.size() > stackLimit) {
			ActivityRecord cmd = undoStack.remove(0);
			assert cmd.getState() != ActivityRecord.DISCARD_STATE;
			cmd.destroy();
			cmd.setState(ActivityRecord.DISCARD_STATE);
		}
	}

	/**
	 * Clears the record stack.
	 */

	@Override
	public void flush() {
		destroyRecords(redoStack);
		destroyRecords(undoStack);
	}

	/**
	 * Private method to remove all records on a stack.
	 *
	 * @param stack
	 */

	private void destroyRecords(Stack<ActivityRecord> stack) {
		Iterator<ActivityRecord> iter = stack.iterator();
		while (iter.hasNext()) {
			ActivityRecord cmd = iter.next();
			assert cmd.getState() != ActivityRecord.DISCARD_STATE;
			cmd.destroy();
			cmd.setState(ActivityRecord.DISCARD_STATE);
		}
		stack.removeAllElements();
	}

	/**
	 * Sets the stack size limit. If the stack already exceeds the new limit then
	 * the excess records are flushed.
	 *
	 * @param limit The new stack size limit.
	 */

	@Override
	public void setStackLimit(int limit) {
		assert limit >= 0;
		stackLimit = limit;
		trimUndoStack();
	}

	/**
	 * Returns an array of the records in the order they were executed. This method
	 * is useful for debugging only, since the list contains no marker to note which
	 * records have been undone.
	 *
	 * @return An array containing all records in the order they were executed.
	 */

	public Object[] getRecords() {
		List<ActivityRecord> records = new ArrayList<>(undoStack);
		for (int i = redoStack.size() - 1; i >= 0; i--) {
			records.add(redoStack.get(i));
		}
		return records.toArray();
	}

	/**
	 * Peeks at the top of the redo stack.
	 *
	 * @return The record at the top of the redo stack, or null if there is no such
	 *         record.
	 */

	@Override
	public IActivityRecord getRedoRecord() {
		return redoStack.isEmpty() ? null : redoStack.peek();
	}

	/**
	 * Peeks at the top of the undo stack.
	 *
	 * @return The record at the top of the undo stack, or null if there is no such
	 *         record.
	 */

	@Override
	public IActivityRecord getUndoRecord() {
		return undoStack.isEmpty() ? null : undoStack.peek();
	}

	/**
	 * Returns the label of the record at the top of the undo stack. The label
	 * describes the next operation to be undone.
	 *
	 * @return The undo label, or null if there is no record to be undone.
	 */

	@Override
	public String getUndoLabel() {
		IActivityRecord cmd = getUndoRecord();
		if (cmd == null) {
			return null;
		}

		// Get the label. The label cannot be null when a record
		// is available to undo.

		String label = cmd.getLabel();
		assert label != null;
		return label;
	}

	/**
	 * Returns the label of the record at the top of the redo stack. The label
	 * describes the next operation to be redone.
	 *
	 * @return The redo label, or null if there is no record to be redone.
	 */

	@Override
	public String getRedoLabel() {
		IActivityRecord cmd = getRedoRecord();
		if (cmd == null) {
			return null;
		}

		// Get the label. The label cannot be null when a record
		// is available to redo.

		String label = cmd.getLabel();
		assert label != null;
		return label;
	}

	/**
	 * Starts a transaction. The application provides the message ID for a label to
	 * associate with the transaction.
	 *
	 * @param label localized label for the transaction
	 *
	 * @see #commit
	 */

	@Override
	public void startTrans(String label) {
		// Create a compound record to implement the transaction.

		transStack.push(adapter.createNewRecord(TransactionAdapter.DEFAULT_RECORD, label));

	}

	/**
	 * Starts a transaction. The application provides the message ID for a label to
	 * associate with the transaction.
	 *
	 * @param label   localized label for the transaction
	 * @param options the transaction options
	 *
	 * @see #commit
	 */

	public void startTrans(String label, TransactionOption options) {
		startTrans(label);

		CompoundRecord tmpRecord = transStack.peek();
		tmpRecord.setOptions(options);
	}

	/**
	 * Commits the current transaction. There must be an active transaction. If
	 * nested transactions are active, this method will finish the inner- most
	 * transaction.
	 *
	 * @see #startTrans()
	 * @see #startTrans(String)
	 * @see #rollback
	 */

	@Override
	public void commit() {
		assert (!transStack.empty());
		CompoundRecord transaction = transStack.pop();

		// If the compound record is empty, then we have a null
		// transaction. Just ignore this transaction, don't put it
		// onto the undo stack.

		if (transaction.isEmpty()) {
			transaction.destroy();
			return;
		}
		transaction.setState(ActivityRecord.DONE_STATE);

		// Handle the special case of a transaction with one item.

		ActivityRecord record = transaction;
		record.performPostTasks(transStack);

		if (transStack.empty()) {

			// Flush the redo stack.

			destroyRecords(redoStack);

			// This is the outermost transaction. Add it to the undo stack
			// and send out notifications.

			record.setTransNo(++transCount);

			adapter.handleCommit(record);

			sendNotifcations(new ActivityStackEvent(this, ActivityStackEvent.DONE));
		} else {
			// This is a nested transaction. Add it to the parent
			// transaction.

			CompoundRecord outer = transStack.lastElement();
			outer.append(record);

		}

	}

	/**
	 * Undoes all actions done so far in the innermost transaction. Does not undo
	 * any parent transactions.
	 */

	@Override
	public void rollback() {
		assert transStack.size() > 0;
		CompoundRecord trans = transStack.pop();

		// silent task do not perform tasks here since values on elements
		// are not changed.

		trans.rollback();
		trans.destroy();

		adapter.handleRollback(trans);

		// if the trans stack is empty now, then send the notifications

		if (transStack.empty()) {
			sendNotifcations(new ActivityStackEvent(this, ActivityStackEvent.ROLL_BACK));
		}
	}

	/**
	 * Undoes an entire uncommitted transaction. Provides the application a way to
	 * reverse a set of operations that must be abandoned. For example, the
	 * application may implement a user-level operation as a series of operations.
	 * If any of the operations fail, the application wants to undo all the changes
	 * made so far in that transaction.
	 */

	@Override
	public void rollbackAll() {
		while (!transStack.isEmpty()) {
			rollback();
		}
	}

	/**
	 * Returns the current transaction number. This is either the command just
	 * completed (the one on the top of the undo stack),or <code>0</code> if the
	 * undo stack is empty.
	 *
	 * @return the current transaction number
	 */

	public int getCurrentTransNo() {
		if (undoStack.isEmpty()) {
			return 0;
		}
		return (undoStack.lastElement()).getTransNo();
	}

	/**
	 * Registers a listener. A listener can be registered any number of times, but
	 * will receive each event only once.
	 * <p>
	 * Part of: Notification system.
	 *
	 * @param obj the activity stack listener to register
	 */

	@Override
	public void addListener(ActivityStackListener obj) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		if (obj != null && !listeners.contains(obj)) {
			listeners.add(obj);
		}
	}

	/**
	 * Removes a listener. The listener is removed from the list of listeners. If
	 * the item is not in the list, then the request is silently ignored.
	 * <p>
	 * Part of: Notification system.
	 *
	 * @param obj the activity stack listener to remove
	 */

	@Override
	public void removeListener(ActivityStackListener obj) {
		if (listeners == null) {
			return;
		}
		int posn = listeners.indexOf(obj);
		if (posn != -1) {
			listeners.remove(posn);
		}
	}

	/**
	 * Sends the notifications. This method check the current record state, and fire
	 * event to corresponding method of listener.
	 *
	 * @param event activity stack event.
	 */

	public void sendNotifcations(ActivityStackEvent event) {
		// Send to all direct listeners.

		if (listeners != null) {
			List<ActivityStackListener> tmpList = new ArrayList<>(listeners);
			Iterator<ActivityStackListener> iter = tmpList.iterator();
			while (iter.hasNext()) {
				ActivityStackListener listener = iter.next();

				listener.stackChanged(event);
			}
		}

		// clear module namehelper
		if (module != null) {
			module.getNameHelper().clear();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.CommandStack#startPersistentTrans(java
	 * .lang.String)
	 */

	@Override
	public void startPersistentTrans(String label) {
		transStack.push(adapter.createNewRecord(TransactionAdapter.PERSISTENT_RECORD, label));
	}

	/**
	 * Starts a persistent transaction. Once the transaction is committed, it will
	 * never be undone with all calls to rollback( ) or rollbackAll( ). To make the
	 * transaction undone is just to call undo( ).
	 */

	public void startPersistentTrans() {
		startPersistentTrans(null);
	}

	/**
	 * Starts a silent transaction. All events in the transaction will not be sent
	 * out.
	 *
	 * @param label localized label for the transaction
	 */
	public void startSilentTrans(String label) {
		startSilentTrans(label, false);
	}

	/**
	 * Starts a silent transaction. All events in the transaction will not be sent
	 * out.
	 *
	 * @param filterAll status to filter all events or not
	 *
	 */

	public void startSilentTrans(boolean filterAll) {
		startSilentTrans(null, filterAll);
	}

	/**
	 * Starts a silent transaction. All events in the transaction will not be sent
	 * out.
	 *
	 * @param label     localized label for the transaction
	 * @param filterAll
	 */

	protected void startSilentTrans(String label, boolean filterAll) {
		LayoutCompoundRecord cmpRecord = (LayoutCompoundRecord) adapter
				.createNewRecord(TransactionAdapter.LAYOUT_RECORD, label);
		cmpRecord.setFilterAll(filterAll);

		transStack.push(cmpRecord);

	}

	/**
	 * Starts a filter events transaction, all the events within the transaction
	 * will be holden. They will be filtered and sent out once the transaction is
	 * committed.
	 *
	 * @param label localized label for the transaction
	 */

	public void startFilterEventTrans(String label) {
		transStack.push(adapter.createNewRecord(TransactionAdapter.FILTER_RECORD, label));
	}

	/**
	 * Starts a non-undo/redo compound record. This is primary to use in the simple
	 * api script environment.
	 *
	 * @param label localized label for the transaction
	 */

	public void startNonUndoableTrans(String label) {
		transStack.push(adapter.createNewRecord(TransactionAdapter.NONUNDOABLE_RECORD, label));
	}

	/**
	 * Removes all listeners on the ActivityStack.
	 */

	@Override
	public void clearListeners() {
		if (listeners != null) {
			listeners.clear();
		}
		listeners = null;
	}

	/**
	 * Increase the transaction count for the transaction stack.
	 *
	 * @return the increased transaction count
	 */

	protected int increaseTransCount() {
		return ++transCount;
	}

	/**
	 * Returns the last element from transaction stack
	 *
	 * @return
	 */
	public CompoundRecord getTopTransaction() {
		if (transStack.isEmpty()) {
			return null;
		}
		return this.transStack.lastElement();
	}
}
