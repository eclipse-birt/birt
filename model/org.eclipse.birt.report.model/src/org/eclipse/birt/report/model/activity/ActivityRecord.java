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

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.activity.IActivityRecord;
import org.eclipse.birt.report.model.validators.IValidatorProvider;
import org.eclipse.birt.report.model.validators.ValidationNode;

/**
 * The base activity record provides the mechanism for performing a low-level
 * change to the model. See the
 * {@link org.eclipse.birt.report.model.activity.ActivityStack ActivityRecord
 * Stack} class for additional background information including a description of
 * the <em>initial</em> and <em>final</em> states for a record.
 * <p>
 * The activity stack (often called a "command stack") records changes made to
 * the model. We call it an activity stack because each UI "command" can give
 * rise to zero, one, or many low level changes. Each low-level change is
 * represented by an activity record. These records are much like the physical
 * change log records in a database transaction log (or journal) system.
 * {@link CompoundRecord compound records}make a series of low-level activity
 * records look like a single high-level command to the user.
 * <p>
 * This class is an abstract record. Most routine records should inherit from
 * the {@link AbstractElementRecord AbstractElementRecord class}.
 * 
 * <h3>Activity Record Life Cycle</h3>
 * 
 * The life cycle of a record is given by the following states as defined by
 * contents in this class.
 * <p>
 * <dl>
 * <dt><strong>Initial state </strong></dt>
 * <dd>The record has been created, but not yet executed.</dd>
 * 
 * <dt><strong>Done state </strong></dt>
 * <dd>The record has been executed, but not been undone.</dd>
 * 
 * <dt><strong>Undone state </strong></dt>
 * <dd>The record has executed and been undone. Or, the record has been redone
 * and undone.</dd>
 * 
 * <dt><strong>Redone state </strong></dt>
 * <dd>The record has executed, been undone, and has been redone. The (undo,
 * redo) cycle could have been repeated any number of times.</dd>
 * 
 * <dt><strong>Discard </strong></dt>
 * <dd>The record has been flushed from the record stack. It is no longer
 * eligible for undo or redo.</dd>
 * </dl>
 * 
 * <h3>Activity Record States</h3>
 * 
 * Records support the following valid state transitions.
 * <p>
 * <table>
 * <thead>
 * <th>Operation</th>
 * <th>From state</th>
 * <th>To state</th>
 * <th>Comment</th> </thead>
 * <tr>
 * <td><code>execute( )</code></td>
 * <td>Initial</td>
 * <td>Done</td>
 * <td>The record is executed.</td>
 * </tr>
 * <tr>
 * <td><code>undo( )</code></td>
 * <td>Done</td>
 * <td>Undone</td>
 * <td>The record is undone in response to an undo request from the user.</td>
 * </tr>
 * <tr>
 * <td><code>redo( )</code></td>
 * <td>Undone</td>
 * <td>Redone</td>
 * <td>The record is redone in response to a redo request from the user.</td>
 * </tr>
 * <tr>
 * <td><code>undo( )</code></td>
 * <td>Redone</td>
 * <td>Undone</td>
 * <td>The record is undone after having been redone.</td>
 * </tr>
 * <tr>
 * <td><code>discard( )</code></td>
 * <td>Any</td>
 * <td>Discard</td>
 * <td>The record is discarded from the record stack.</td>
 * </tr>
 * </table>
 * 
 * <h3>The Target Element and Notification</h3>
 * 
 * Model elements provide notification of changes. Notifications depend on the
 * specific action performed.
 * <p>
 * Most records have a <em>target element</em>. This is the element affected by
 * the record. A well-defined record affects exactly one element. If an
 * operation must affect multiple elements, then the application should create a
 * {@link CompoundRecord compound command}that will hold a series of records,
 * each of which operate on a single element.
 * <p>
 * The exception to the above rule is when a command changes only internally
 * cached information on elements other than the target. For example, suppose
 * the target element E refers to another element X. Suppose that X caches an
 * inverse relationship back to E. A command that changes this relationship
 * would have E as the target element. The change to element X would be done as
 * part of this command so as to leave the model in a consistent state after the
 * command.
 * <p>
 * Another operation is one in which the change must be broadcast to two
 * different elements. For example, when we drop an element from its container,
 * we effectively delete the element. In this case, we must send notifications
 * to both the container and the content.
 * <p>
 * Use the command state to determine the specific notification to send:
 * generally one kind for the do & redo operations, another for undo. The
 * {@link AbstractElementRecord simple command}class automates the process of
 * sending notifications for the target element.
 * <p>
 * Some UI components may find it useful to ignore notifications for operations
 * done by that UI component itself. For example, a graphic editor may provide a
 * move operation, an may do the move as part of the operation. It is not
 * efficient to repeat the move when notified that the position changed. To
 * handle this case, the UI can specify itself as the <em>sender</em> of the
 * notification. Then, when the UI receives the event, it can check the sender.
 * If the sender is itself, it ignores the update. If the sender is anything
 * else (including null), then the UI updates based on a change made elsewhere.
 * 
 * <h3>Saving Model State with Mementos</h3>
 * 
 * Records must often cache information in the form of a <em>memento</em>. The
 * memento gathers information needed to undo or redo the record. If a record
 * deletes an element E, then it must cache element E so that it can restore the
 * element. This means that a logical "delete" operation is implemented by
 * "detaching" the element from the model. In some cases, the required
 * information may be quite complex. In such cases, the record should create a
 * memento object that can gather up the information. The memento is passed to
 * the model when performing the operation, so that the model can record
 * information needed to reverse the command. This pattern keeps the model from
 * depending on the record, and keeps the command from having inappropriately
 * deep knowledge of the model.
 * 
 * <h3>Error Management</h3>
 * 
 * Note that none of the record methods throw an exception. As described in the
 * {@link org.eclipse.birt.report.model.activity.ActivityStack command stack},
 * records must be designed so that they do not fail. The application (generally
 * the command layer) is responsible for ensuring that the requested operation
 * is valid. This rule exists to keep the record mechanism simple. If a record
 * could fail, and was part of a composite record (transaction), then the
 * transaction would have to be rolled back to undo any partially completed
 * changes. Doing so is possible, but complex. It is better to perform all the
 * checks up front, then perform the records needed to make the changes.
 * <p>
 * Another way of saying this is that records are low-level operations that
 * simply perform physical updates. They are "dumb" in that they do not
 * understand, nor enforce (except via assertions) semantic rules.
 * 
 * <h3>Labels</h3>
 * 
 * User-level operations can have labels. Labels appear in the menu commands for
 * undo and redo. For example: "Undo Delete Text Item" or "Redo Move". In
 * general, there are multiple activity records for each user-level operation.
 * The user- level operations generally don't exactly map to activity records.
 * As a result, the application should specify a (localized) label when calling
 * the <code>startTrans( null )</code> method on the activity stack. That label
 * is then cached in the activity record. Each activity record provides a
 * default label, but that label is often not the best one for the overall
 * application operation.
 */

public abstract class ActivityRecord implements IActivityRecord, IValidatorProvider {

	// List of valid record states.

	/**
	 * Indicates that the record has not yet executed.
	 */

	public static final int INITIAL_STATE = 0;

	/**
	 * Indicates that the record has executed, but not been undone.
	 */

	public static final int DONE_STATE = 1;

	/**
	 * Indicates that the record has executed and been undone. The record could have
	 * been redone and undo any number of times.
	 */

	public static final int UNDONE_STATE = 2;

	/**
	 * Indicates that the record has executed, been undone, and has been redone. The
	 * (undo, redo) cycle could have been repeated any number of times.
	 */
	public static final int REDONE_STATE = 3;

	/**
	 * Indicates that the record has been discarded. It is no longer a candidate for
	 * undo or redo.
	 */

	public static final int DISCARD_STATE = 4;

	/**
	 * Record state. Used to verify the record life-cycle, and to send the correct
	 * notification events.
	 */

	protected int state = INITIAL_STATE;

	/**
	 * Optional hint that the UI can include along with a notification event. Sent
	 * only on the original execute operation.
	 */

	protected Object sender = null;

	/**
	 * Optional display label that the UI can display along with undo & redo menu
	 * options.
	 */

	protected String label;

	/**
	 * The transaction number of this command.
	 */

	private int transNo = 0;

	/**
	 * The status justifying whether the record is persistent when rollback.
	 */

	protected boolean isPersistent = false;

	/**
	 * Default constructor.
	 */

	public ActivityRecord() {
	}

	/**
	 * Destroys the record. Called when the record is discarded from the record
	 * stack. Derived records override this operation to release model resources
	 * that they may have cached.
	 */

	public void destroy() {
	}

	/**
	 * Gets the label of this record. This label should be localized.
	 * 
	 * @return the label of this record
	 */

	public final String getLabel() {
		return label;
	}

	/**
	 * Sets the label of this record. This label should be localized.
	 * 
	 * @param text the label to set
	 */

	public final void setLabel(String text) {
		label = text;
	}

	/**
	 * Executes the record. Derived classes do the desired operation. All semantic
	 * and other checks must have already been done; the record operation must
	 * succeed.
	 */

	abstract public void execute();

	/**
	 * Undoes the record. Leaves the state of the model identical to what it was
	 * before execute was called. Note that the operation must be designed so that
	 * it succeeds if the model is in the correct state: the same state it was in
	 * after execute( ) was called. (If the model is in any other state, then a
	 * programming error has occurred.)
	 */

	abstract public void undo();

	/**
	 * Redoes the record. Logically repeats the execute record. The state of the
	 * model must be identical to that after undo( ) has executed. After the call,
	 * the state of the model must be identical to that after execute( ) was called.
	 */

	abstract public void redo();

	/**
	 * Tells if this record can be undone. All records should be undoable in the
	 * production system. A record may temporarily not support undo during a
	 * development cycle.
	 * 
	 * @return true if the record can be undone, false otherwise
	 */

	public boolean canUndo() {
		return true;
	}

	/**
	 * Tells if this record can be redone. All records should be redoable in the
	 * production system. A record may temporarily not support redo during a
	 * development cycle.
	 * 
	 * @return true if redoable, false otherwise.
	 */

	public boolean canRedo() {
		return true;
	}

	/**
	 * Returns the state of the record. The possible value is:
	 * <p>
	 * <ul>
	 * <li><code>INITIAL_STATE</code>
	 * <li><code>DONE_STATE</code>
	 * <li><code>UNDONE_STATE</code>
	 * <li><code>REDONE_STATE</code>
	 * <li><code>DISCARD_STATE</code>
	 * </ul>
	 * 
	 * @return the record state.
	 */

	public int getState() {
		return state;
	}

	/**
	 * Sets the record state. This method can be called only by the record stack.
	 * The state transition must be legal.
	 * 
	 * @param newState the state to set
	 */

	public void setState(int newState) {
		// Verify that this is a legal state transition.

		assert state == INITIAL_STATE && newState == DONE_STATE || state == DONE_STATE && newState == UNDONE_STATE
				|| state == UNDONE_STATE && newState == REDONE_STATE
				|| state == REDONE_STATE && newState == UNDONE_STATE || newState == DISCARD_STATE;
		state = newState;
	}

	/**
	 * Returns the optional UI hint to be sent with the execute notification for
	 * this record.
	 * 
	 * @return the sender.
	 */

	public Object getSender() {
		return sender;
	}

	/**
	 * Sets the optional UI hint to be sent with the execute notification for this
	 * record.
	 * 
	 * @param obj the sender to set
	 */

	public void setSender(Object obj) {
		sender = obj;
	}

	/**
	 * Sets the transaction number for top-level commands.
	 * 
	 * @param n the transaction number to set
	 */

	public void setTransNo(int n) {
		transNo = n;
	}

	/**
	 * Returns the transaction number of this command.
	 * 
	 * @return the transaction number or 0 if this is not a top-level command
	 */

	public int getTransNo() {
		return transNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.validators.core.IValidatorProvider#
	 * getValidators()
	 */

	public List<ValidationNode> getValidators() {
		return Collections.emptyList();
	}

	/**
	 * Justifies whether the record is undoable or persistent when the application
	 * calls <code>rollback</code> or <code>rollbackAll</code>.
	 * 
	 * @return true if the record is persistent, otherwise false
	 */

	public boolean isPersistent() {
		return this.isPersistent;
	}

	/**
	 * Sets the persistent status of the record.
	 * 
	 * @param isPersistent <code>true</code> if the record is persistent. Otherwise
	 *                     <code>false</code>.
	 */

	public void setPersistent(boolean isPersistent) {
		this.isPersistent = isPersistent;
	}

	/**
	 * Rollbacks the record. If the record is persistent, then there will be no
	 * operation with the method. Otherwise the record is undone.
	 * 
	 */

	abstract public void rollback();

	/**
	 * Returns tasks that will be executed after the record and before sending
	 * notifications to elements.
	 * 
	 * @return a list containing tasks
	 */

	protected List<RecordTask> getPostTasks() {
		return Collections.emptyList();
	}

	/**
	 * Performs tasks after the execution of the record.
	 * 
	 * @param transStack the transaction stack.
	 */

	protected void performPostTasks(Stack<CompoundRecord> transStack) {
		List<RecordTask> tasks = getPostTasks();

		for (int i = 0; i < tasks.size(); i++) {
			RecordTask subTask = tasks.get(i);
			subTask.doTask(this, transStack);
		}
	}
}
