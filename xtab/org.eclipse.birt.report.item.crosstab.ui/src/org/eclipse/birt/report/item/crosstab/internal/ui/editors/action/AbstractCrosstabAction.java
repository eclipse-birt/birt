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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.action.Action;

/**
 * Abstract class for the cross tab action.
 */
public abstract class AbstractCrosstabAction extends Action {

	private DesignElementHandle handle = null;

	/**
	 * Constructor
	 *
	 * @param handle the element handle
	 */
	public AbstractCrosstabAction(DesignElementHandle handle) {
		super();
		this.handle = handle;
	}

	/**
	 * Star the trans for the special name.
	 *
	 * @param name trans name
	 */
	public void transStar(String name) {
		CommandStack stack = handle.getModuleHandle().getCommandStack();
		// start trans
		stack.startTrans(name);
	}

	/**
	 * Ends a transaction on the current activity stack
	 */
	public void transEnd() {
		CommandStack stack = handle.getModuleHandle().getCommandStack();
		stack.commit();
	}

	/**
	 * Gets the handle
	 *
	 * @return
	 */
	public DesignElementHandle getHandle() {
		return handle;
	}

	/**
	 * Sets the handle
	 *
	 * @param handle
	 */
	public void setHandle(DesignElementHandle handle) {
		this.handle = handle;
	}

	/**
	 *
	 */
	protected void rollBack() {
		handle.getModuleHandle().getCommandStack().rollback();
	}

	@Override
	public boolean isEnabled() {
		return !DEUtil.isReferenceElement(handle);
	}

}
