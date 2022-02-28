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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.model.api.activity.IActivityRecord;
import org.eclipse.gef.commands.Command;

/**
 * Provides a wrapper of DE command for GEF framework's call back needs GEF
 * command.
 */
public class CommandWrap4DE extends Command {

	private IActivityRecord command;

	/**
	 * Constructor for CommandWrap4DE.
	 */
	public CommandWrap4DE(IActivityRecord command) {
		this.command = command;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return command == null ? false : true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return command == null ? false : command.canUndo();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (command != null) {
			if (Policy.TRACING_COMMANDS) {
				System.out.println("GuiCommand >> Excute " + getLabel()); //$NON-NLS-1$
			}
			command.execute();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#getLabel()
	 */
	@Override
	public String getLabel() {
		return command == null || command.getLabel() == null ? "" : command.getLabel().replace("&", ""); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		if (command != null) {
			if (Policy.TRACING_COMMANDS) {
				System.out.println("GuiCommand >> Redo " + getLabel()); //$NON-NLS-1$
			}
			command.redo();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (command != null) {
			if (Policy.TRACING_COMMANDS) {
				System.out.println("GuiCommand >> Undo " + getLabel()); //$NON-NLS-1$
			}
			command.undo();
		}
	}

	public IActivityRecord unwrap() {
		return command;
	}

}
