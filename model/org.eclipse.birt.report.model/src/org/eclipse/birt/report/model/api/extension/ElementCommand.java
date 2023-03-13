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

/**
 * Extension adapter class for the IElementCommand. The subclasses must override
 * the execute/undo/redo methods to implement their own command. All the command
 * is undoable and redoable by default.
 */

abstract public class ElementCommand implements IElementCommand {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#execute()
	 */

	@Override
	abstract public void execute();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#undo()
	 */

	@Override
	abstract public void undo();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#redo()
	 */

	@Override
	abstract public void redo();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canUndo()
	 */

	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#canRedo()
	 */
	@Override
	public boolean canRedo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IElementCommand#getLabel()
	 */
	@Override
	public String getLabel() {
		return null;
	}

}
