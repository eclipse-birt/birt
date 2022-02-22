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

	@Override
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
	@Override
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
	@Override
	protected String getDisplayLabel() {
		StringBuilder displayLabel = new StringBuilder().append(REDO_LABEL);
		if (!StringUtil.isBlank(stack.getRedoLabel())) {
			displayLabel.append(" ").append(stack.getRedoLabel()); //$NON-NLS-1$
		}
		return displayLabel.toString();

	}

}
