/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public boolean canExecute() {
		if (source == null || target == null)
			return false;
		else
			return true;
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