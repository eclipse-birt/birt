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

package org.eclipse.birt.report.model.api.activity;

/**
 * Application-level interface into the Model's activity record.
 *
 */

public interface IActivityRecord {

	/**
	 * Executes the record.
	 */

	void execute();

	/**
	 * Undoes the record. Leaves the state of the model identical to what it was
	 * before execute was called.
	 */

	void undo();

	/**
	 * Redoes the record. Logically repeats the execute record. The state of the
	 * model must be identical to that after undo( ) has executed.
	 */

	void redo();

	/**
	 * Tells if this record can be undone.
	 *
	 * @return true if the record can be undone, false otherwise
	 */

	boolean canUndo();

	/**
	 * Tells if this record can be redone.
	 *
	 * @return true if redoable, false otherwise.
	 */

	boolean canRedo();

	/**
	 * Gets the label of this record. This label should be localized.
	 *
	 * @return the label of this record
	 */

	String getLabel();
}
