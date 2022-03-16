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

/**
 * This record that cannot be undoable/redoable after the top level record
 * commits.
 *
 */

class NonUndoableCompoundRecord extends CompoundRecord {

	/**
	 * Constructor.
	 *
	 * @param text      the localized label text
	 * @param outerMost indicates if it is the outer most filter event transaction.
	 */

	public NonUndoableCompoundRecord(String text) {
		super(text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#canRedo()
	 */

	@Override
	public boolean canRedo() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#canUndo()
	 */

	@Override
	public boolean canUndo() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#redo()
	 */

	@Override
	public void redo() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#undo()
	 */

	@Override
	public void undo() {

	}

}
