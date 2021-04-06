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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Global redo action for views
 */

public class GlobalRedoAction extends GlobalStackAction {

	private static String REDO_LABEL = Messages.getString("label.redo"); //$NON-NLS-1$

	protected GlobalRedoAction(CommandStack stack) {
		super(GlobalActionFactory.REDO, stack);
	}

	public void run() {
		stack.redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalStackAction#
	 * calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return stack.canRedo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalStackAction#
	 * getDisplayLabel()
	 */
	protected String getDisplayLabel() {
		String displayLabel = REDO_LABEL;
		if (!StringUtil.isBlank(stack.getRedoLabel())) {
			displayLabel += " " + stack.getRedoLabel(); //$NON-NLS-1$
		}
		return displayLabel;

	}

}