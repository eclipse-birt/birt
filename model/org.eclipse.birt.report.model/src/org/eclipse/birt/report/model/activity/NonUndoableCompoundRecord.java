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

	public boolean canRedo() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#canUndo()
	 */

	public boolean canUndo() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#redo()
	 */

	public void redo() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.CompoundRecord#undo()
	 */

	public void undo() {

	}

}
