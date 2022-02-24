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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.gef.commands.Command;

/**
 * The abstract command for all the cross tab command.
 */
public class AbstractCrosstabCommand extends Command {

	private DesignElementHandle handle;

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AbstractCrosstabCommand(DesignElementHandle handle) {
		assert handle != null;
		this.handle = handle;
	}

	/**
	 * Start the trans.
	 * 
	 * @param name
	 */
	protected void transStart(String name) {
		handle.getModuleHandle().getCommandStack().startTrans(name);
	}

	/**
	 * Commit the trans.
	 */
	protected void transEnd() {
		handle.getModuleHandle().getCommandStack().commit();
	}

	/**
	 * Roll back the trans.
	 */
	protected void rollBack() {
		handle.getModuleHandle().getCommandStack().rollback();
	}
}
