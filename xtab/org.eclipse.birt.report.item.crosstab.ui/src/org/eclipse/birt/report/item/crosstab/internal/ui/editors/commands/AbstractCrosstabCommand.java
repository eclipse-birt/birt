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
