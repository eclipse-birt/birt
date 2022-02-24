/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStackListener;

/**
 * @author David Michonneau
 * 
 *         This class is a command stack for the GEF framework. It internally
 *         access the ActivityStack class of the DE. No commands are pushed to
 *         the command stack or ActivityStack here since the Design handles take
 *         care of that when executed. Commands are executed as transactions.
 */
public class WrapperCommandStack extends org.eclipse.gef.commands.CommandStack {

	private CommandStack ar;

	/**
	 * @deprecated use {@link #WrapperCommandStack(CommandStack)}
	 */
	public WrapperCommandStack() {
		this(SessionHandleAdapter.getInstance().getCommandStack());
	}

	public WrapperCommandStack(org.eclipse.birt.report.model.api.CommandStack ar) {
		this.ar = ar;
	}

	public boolean canUndo() {
		return ar.canUndo();
	}

	public boolean canRedo() {
		return ar.canRedo();
	}

	public void undo() {
		if (canUndo())
			ar.undo();
	}

	public void redo() {
		if (canRedo())
			ar.redo();
	}

	public void flush() {
		ar.flush();
	}

	public Command getRedoCommand() {
		return new CommandWrap4DE(ar.getRedoRecord());
	}

	public Command getUndoCommand() {
		return new CommandWrap4DE(ar.getUndoRecord());
	}

	public void execute(Command command) {
		if (command == null) {
			return;
		}

		if (command.getLabel() == null) {
			command.setLabel(""); //$NON-NLS-1$
		}
		ar.startTrans(command.getLabel());
		command.execute();
		ar.commit();

	}

	public void setUndoLimit(int undoLimit) {
		ar.setStackLimit(undoLimit);
	}

	public void addCommandStackListener(ActivityStackListener listener) {
		ar.addListener(listener);
	}

	public void removeCommandStackListener(ActivityStackListener listener) {
		ar.removeListener(listener);
	}

	/**
	 * @deprecated
	 * 
	 *             Do not use, use addCommandStackListener(ActivityStackListener)
	 *             instead
	 * 
	 * @see org.eclipse.gef.commands.CommandStack#addCommandStackListener(org.eclipse.gef.commands.CommandStackListener)
	 */
	public void addCommandStackListener(CommandStackListener listener) {
		// use addCommandStackListener(ActivityStackListener) instead
		// this method will called by GEF.
		// can't assert false.
		// see bugzilla 147687
		// assert false;
	}

	/**
	 * @deprecated
	 * 
	 *             Do not use, use removeCommandStackListener(ActivityStackListener)
	 *             instead
	 * 
	 * @see org.eclipse.gef.commands.CommandStack#removeCommandStackListener(org.eclipse.gef.commands.CommandStackListener)
	 */
	public void removeCommandStackListener(CommandStackListener listener) {
		// use removeCommandStackListener(ActivityStackListener) instead
		// assert false;
	}

	public void setActivityStack(CommandStack ar) {
		this.ar = ar;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		ar.flush();
		ar.clearListeners();
		ar = null;
	}
}
