/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.CopyCellContentsHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CellHandle;

/**
 * Copy cell's contents action
 */
public class CopyCellContentsAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.command.copyCellContentsAction"; //$NON-NLS-1$

	/**
	 * Create a new copy action with given selection and default text
	 *
	 * @param selectedObject the selected object,which cannot be null
	 *
	 */
	public CopyCellContentsAction(Object selectedObject) {
		this(selectedObject, Messages.getString("CopyCellContentsAction.actionText")); //$NON-NLS-1$
		setId(ID);
	}

	/**
	 * Create a new copy action with given selection and text
	 *
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public CopyCellContentsAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Copy action >> Copy " + getSelection()); //$NON-NLS-1$
		}

		try {
			CommandUtils.executeCommand(CopyCellContentsHandler.ID);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public boolean isEnabled() {
		if (canCopy(getSelection())) {
			return super.isEnabled();
		}
		return false;
	}

	private boolean canCopy(Object selection) {
		if (selection instanceof CellHandle) {
			return ((CellHandle) selection).getContent().getCount() > 0;
		}
		return false;
	}

}
