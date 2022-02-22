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
 * Global undo action for views
 */

public class GlobalUndoAction extends GlobalStackAction {

	private static String UNDO_LABEL = Messages.getString("label.undo"); //$NON-NLS-1$

	protected GlobalUndoAction(CommandStack stack) {
		super(GlobalActionFactory.UNDO, stack);
	}

	@Override
	public void run() {
		stack.undo();
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
		return stack.canUndo();
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
		StringBuilder displayLabel = new StringBuilder().append(UNDO_LABEL);
		if (!StringUtil.isBlank(stack.getUndoLabel())) {
			displayLabel.append(" ").append(stack.getUndoLabel()); //$NON-NLS-1$
		}
		return displayLabel.toString();
	}
}
