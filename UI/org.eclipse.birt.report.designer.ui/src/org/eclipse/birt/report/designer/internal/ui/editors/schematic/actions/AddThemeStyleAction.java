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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.jface.action.Action;

/**
 * This is a AddStyleAction wrapper used in add style in Library Theme.
 */

public class AddThemeStyleAction extends Action {

	private AddStyleAction addStyleAction;
	private AbstractThemeHandle themeHandle;

	public AddThemeStyleAction(AbstractThemeHandle themeHandle, AddStyleAction addStyleAction) {
		this.themeHandle = themeHandle;
		this.addStyleAction = addStyleAction;
		setText(themeHandle.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		this.addStyleAction.setThemeHandle(themeHandle);
		this.addStyleAction.run();
		this.addStyleAction.setThemeHandle(null);
	}

}
