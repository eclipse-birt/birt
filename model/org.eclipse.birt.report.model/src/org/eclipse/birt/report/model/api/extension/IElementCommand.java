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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Represents a extended element command that is integrated with the BIRT
 * command stack. Note that BIRT commands cannot fail. BIRT assumes that all
 * semantic or other checking has been done before executing the command. (BIRT
 * itself divides the command mechanism into two layers: a command that does the
 * checking and can perform multiple operations, and an activity record that
 * records the low-level undo/redo information. This object is analogous to a
 * BIRT activity record.)
 */

public interface IElementCommand {

	/**
	 * Executes the record. Derived classes do the desired operation. All semantic
	 * and other checks must have already been done; the record operation must
	 * succeed.
	 */

	void execute();

	/**
	 * Undoes the record. Leaves the state of the IR identical to what it was before
	 * execute was called. Note that the operation must be designed so that it
	 * succeeds if the IR is in the correct state: the same state it was in after
	 * execute( ) was called. (If the IR is in any other state, then a programming
	 * error has occurred.)
	 */

	void undo();

	/**
	 * Redoes the record. Logically repeats the execute record. The state of the IR
	 * must be identical to that after undo( ) has executed. After the call, the
	 * state of the IR must be identical to that after execute( ) was called.
	 */

	void redo();

	/**
	 * Tells if this record can be undone. All records should be undoable in the
	 * production system. A record may temporarily not support undo during a
	 * development cycle.
	 *
	 * @return true if the record can be undone, false otherwise
	 */

	boolean canUndo();

	/**
	 * Tells if this record can be redone. All records should be redoable in the
	 * production system. A record may temporarily not support redo during a
	 * development cycle.
	 *
	 * @return true if redoable, false otherwise.
	 */

	boolean canRedo();

	/**
	 * Gets the label of this record. This label should be localized. The label is
	 * optional.
	 *
	 * @return the label of this record
	 */

	String getLabel();

	/**
	 * Gets the extendedItem handle from the extension user. This is for sending
	 * notifications.
	 *
	 * @return the ExtendedItem handle
	 */
	DesignElementHandle getElementHandle();
}
