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

import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.IActivityRecord;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents the activity stack which is read-only. Thats means any operation
 * like execute, and startTrans is forbidden.
 */

public class ReadOnlyActivityStack extends ActivityStack {

	/**
	 * library read-only message
	 */

	public final static String MESSAGE = "The module is read-only and operation is forbidden."; //$NON-NLS-1$

	/**
	 * Constructs a <code>ReadOnlyActivityStack</code>.
	 */

	public ReadOnlyActivityStack(Module module) {
		super(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.ActivityStack#execute(org.
	 * eclipse.birt.report.model.api.extension.IElementCommand)
	 */
	public void execute(IElementCommand command) {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.ActivityStack#getCurrentTransNo ()
	 */
	public int getCurrentTransNo() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.ActivityStack#getRecords()
	 */
	public Object[] getRecords() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.ActivityStack#sendNotifcations
	 * (org.eclipse.birt.report.model.api.activity.ActivityStackEvent)
	 */
	public void sendNotifcations(ActivityStackEvent event) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#canUndo()
	 */
	public boolean canUndo() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#getUndoLabel()
	 */
	public String getUndoLabel() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#undo()
	 */
	public void undo() {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#canRedo()
	 */
	public boolean canRedo() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#getRedoLabel()
	 */
	public String getRedoLabel() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#redo()
	 */
	public void redo() {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#setStackLimit(int)
	 */
	public void setStackLimit(int limit) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.CommandStack#startTrans(java.lang.String )
	 */
	public void startTrans(String string) {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#commit()
	 */
	public void commit() {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#rollback()
	 */
	public void rollback() {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#rollbackAll()
	 */
	public void rollbackAll() {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#flush()
	 */
	public void flush() {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#getRedoRecord()
	 */
	public IActivityRecord getRedoRecord() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#getUndoRecord()
	 */
	public IActivityRecord getUndoRecord() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#execute(org.eclipse.birt
	 * .report.model.api.activity.IActivityRecord)
	 */
	public void execute(IActivityRecord record) {
		throw new IllegalOperationException(MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.CommandStack#addListener(org.eclipse
	 * .birt.report.model.api.activity.ActivityStackListener)
	 */
	public void addListener(ActivityStackListener obj) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.CommandStack#removeListener(org.eclipse
	 * .birt.report.model.api.activity.ActivityStackListener)
	 */
	public void removeListener(ActivityStackListener obj) {
		// Do nothing.
	}

}
