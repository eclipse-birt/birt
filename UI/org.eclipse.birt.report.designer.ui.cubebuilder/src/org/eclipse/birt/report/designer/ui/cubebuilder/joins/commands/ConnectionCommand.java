/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

/**
 * The command for creating a new Connection between 2 objects selected This is
 * invoked when the user selects 2 columns and tries to create a join between
 * them
 */
public class ConnectionCommand extends Command {

	protected EditPart source;
	protected ColumnEditPart target;

	/**
	 *
	 */
	public ConnectionCommand() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (source == null || target == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return The Source ColumnEditPart
	 */
	public EditPart getSource() {
		return source;
	}

	/**
	 * @return The target ColumnEditPart
	 */
	public ColumnEditPart getTarget() {
		return target;
	}

	public void setSource(EditPart newSource) {
		source = newSource;
	}

	/**
	 * @param newTarget: The Target ColumnEditPart to be set
	 */
	public void setTarget(ColumnEditPart newTarget) {
		target = newTarget;
	}

}
