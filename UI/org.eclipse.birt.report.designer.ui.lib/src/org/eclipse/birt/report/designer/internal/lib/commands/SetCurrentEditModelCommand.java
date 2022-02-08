/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.lib.commands;

import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.gef.commands.Command;

/**
 * Sets the Library editor current editor handle
 * 
 */
public class SetCurrentEditModelCommand extends Command {

	private Object currentModel;
	private String type = ""; //$NON-NLS-1$

	/**
	 * @param model
	 */
	public SetCurrentEditModelCommand(Object model) {
		this(model, LibraryHandleAdapter.CURRENTMODEL);
	}

	/**
	 * @param model
	 */
	public SetCurrentEditModelCommand(Object model, String type) {
		this.currentModel = model;
		setType(type);
	}

	/*
	 * The command donot refer the undo and redo, so it can be execute directly.
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		HandleAdapterFactory.getInstance().getLibraryHandleAdapter().setCurrentEditorModel(currentModel, getType());
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
