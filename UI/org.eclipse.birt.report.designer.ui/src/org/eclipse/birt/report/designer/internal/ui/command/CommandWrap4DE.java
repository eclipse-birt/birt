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
	public boolean canExecute() {
		return command == null ? false : true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return command == null ? false : command.canUndo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
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
	public String getLabel() {
		return command == null || command.getLabel() == null ? "" : command.getLabel().replaceAll("&", ""); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
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