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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.IActivityRecord;
import org.eclipse.birt.report.model.api.extension.IElementCommand;

/**
 * Application-level interface into the Model's command stack. Each design owns
 * a separate command stack. Only operations available to the application are
 * exposed; those internal to the model are not.
 * <p>
 * Although termed a "command stack", the implementation in BIRT is a bit more
 * complex. Every user gesture produces one or more changes. Each change is
 * recorded as an <em>activity record</em>. The set of activity records is
 * grouped into a <em>transaction</em>. The transaction represents the overall
 * application action.
 * 
 * <h3>Nested Transactions</h3>
 * 
 * The application can create <em>transactions</em> to group a collection of
 * operations that should be undone and redone as a unit. When performing a
 * series of such operations, the application must consider the case where one
 * of the operations fails. It can be difficult to ensure ahead of time that
 * that the entire sequence will succeed. It is often easier to simply try the
 * entire sequence, and discover failures when they occur. In this case, the
 * application needs a way to undo work already done when it encounters an
 * errors. Transactions provide this support.
 * <p>
 * The application starts an operation with a call to the
 * <code>{@link #startTrans(String)}</code> method, supplying a localized label
 * that can appear in the menu along with the "Undo" command. For example, "Undo
 * Align Left." The application then makes changes as usual. If the operation
 * succeeds, the application ends the transaction by calling
 * <code>{@link #commit}</code>. However, if the operation fails, and so the
 * whole sequence should be abandoned, the application calls the
 * <code>{@link #rollback}</code> method.
 * <p>
 * The application is often designed in a modular fashion; several modules may
 * contribute to an operation. Sometimes, the module is executed alone;
 * sometimes, in conjunction with others. For example, the module that changes
 * the x position might sometimes be called in response to moving one element,
 * but it may also be called as part of aligning several elements. To make the
 * code easier, each module can introduce its own transaction. This leads to
 * "nested" transactions. Each module calls <code>startTrans</code>, does its
 * work, and calls <code>commit</code>. The top-level commit commits the entire
 * transaction.
 * <p>
 * If an operation fails, the application can undo just the current nested
 * transaction. The <code>rollback</code> method undoes just the innermost
 * transaction. Or, if the application has a generic error handler, it can call
 * <code>rollbackAll</code> to undo the entire set of active transactions.
 * <p>
 * Sometimes, there is possibility that when a transaction fails, some certain
 * operation that has already done and succeeded is essential to be persistent
 * and not to be undone when the application call <code>rollback</code> or
 * <code>rollbackAll</code>. Considering that an Eclipse user select Eclipse ->
 * Search (Outer Dialog) -> Scope -> Choose -> Select Working Set (Inner Dialog)
 * to do some searching, he or she customizes the working set and succeeds, and
 * then he or she clicks "cancel" button to quit the searching, it is completely
 * possible to rollback the transaction while the customized working set is
 * still existent and impactful to the next searching working, which therefore
 * is called as "persistent transaction". In this condition, the application
 * calls <code>startPersistentTrans(String)</code> to do the customization of
 * the working set. So a persistent transaction means that, once the transaction
 * is committed, it will never be undone with all calls to rollback( ) or
 * rollbackAll( ) and the only way to make the transaction undone is just to
 * call undo( ).
 * 
 */

public interface CommandStack {

	/**
	 * Reports whether a command is available to undo.
	 * 
	 * @return <code>true</code> if a command is available to undo,
	 *         <code>false</code> if not.
	 */

	boolean canUndo();

	/**
	 * Returns an optional label for the next command to undo. The UI can display
	 * this label as part of the "Undo" menu command. The label should have been
	 * localized when set.
	 * 
	 * @return The command label. Returns <code>null</code> if either the command
	 *         has no label, of if there is no command to undo.
	 */

	String getUndoLabel();

	/**
	 * Undoes a command. Call this only if <code>canUndo( )</code> returns true.
	 */

	void undo();

	/**
	 * Reports whether a command is available to redo.
	 * 
	 * @return <code>true</code> if a command is available to redo,
	 *         <code>false</code> if not.
	 */

	boolean canRedo();

	/**
	 * Returns an optional label for the next command to redo. The UI can display
	 * this label as part of the "Redo" menu command. The label should have been
	 * localized when set.
	 * 
	 * @return The command label. Returns null if either the command has no label,
	 *         of if there is no command to redo.
	 */

	String getRedoLabel();

	/**
	 * Redoes a command. Call this only if <code>canRedo( )</code> returns true.
	 */

	void redo();

	/**
	 * Sets the size of the undo stack. A larger size keeps more history and allows
	 * the user to "unwind" a greater number of change; but at the cost of greater
	 * memory usage. Note that the size applies to top-level operations, not to the
	 * contents of composite commands.
	 * <p>
	 * If the new size is smaller than the existing size, then the method will
	 * remove any commands above the new limit. If the limit is set to zero, then no
	 * undo history is kept.
	 * 
	 * @param limit the new undo stack size
	 */

	void setStackLimit(int limit);

	/**
	 * Starts an application-level transaction.
	 * 
	 * @param string the localized label of the transaction
	 */

	void startTrans(String string);

	/**
	 * Commits an application-level transaction.
	 */

	void commit();

	/**
	 * Rolls back an application-level transaction. Rolls back just the inner-most
	 * transaction.
	 */

	void rollback();

	/**
	 * Rolls back all active transactions, leaving the design in the same state it
	 * was in when the top-most transaction started.
	 */

	void rollbackAll();

	/**
	 * Clears the record stack.
	 */

	void flush();

	/**
	 * Peeks at the top of the redo stack.
	 * 
	 * @return The record at the top of the redo stack, or null if there is no such
	 *         record.
	 */

	IActivityRecord getRedoRecord();

	/**
	 * Peeks at the top of the undo stack.
	 * 
	 * @return The record at the top of the undo stack, or null if there is no such
	 *         record.
	 */

	IActivityRecord getUndoRecord();

	/**
	 * Executes the specified record and flushes the redo stack.
	 * 
	 * @param record the ActivityRecord to execute
	 */

	void execute(IActivityRecord record);

	/**
	 * Executes the specified extended element command. The command must be ready to
	 * execute. As noted above, any required checks must have already been done.
	 * Flushes the redo stack.
	 * 
	 * @param command the ActivityRecord to execute
	 */

	public void execute(IElementCommand command);

	/**
	 * Registers a listener. A listener can be registered any number of times, but
	 * will receive each event only once.
	 * 
	 * @param obj the activity stack listener to register
	 */

	public void addListener(ActivityStackListener obj);

	/**
	 * Removes a listener. The listener is removed from the list of listeners. If
	 * the item is not in the list, then the request is silently ignored.
	 * 
	 * @param obj the activity stack listener to remove
	 */

	public void removeListener(ActivityStackListener obj);

	/**
	 * Starts one persistent transaction, which will never be rollbacked once the
	 * parent transaction is rollbacked.
	 * 
	 * @param label the localized label of the transaction
	 */

	void startPersistentTrans(String label);

	/**
	 * Removes all listeners on the ActivityStack.
	 */

	public void clearListeners();
}
