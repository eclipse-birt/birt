/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.action;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.3 $ $Date: 2007/04/23 03:30:22 $
 */
public class EditCubeLevelAction extends AbstractElementAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditCubeLevelAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditCubeLevelAction(Object selectedObject) {
		super(selectedObject);
		setId(ID);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public EditCubeLevelAction(Object selectedObject, String text) {
		super(selectedObject, text);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	@Override
	protected boolean doAction() throws Exception {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit Level action >> Runs ..."); //$NON-NLS-1$
		}
		TabularLevelHandle level = (TabularLevelHandle) getSelection();
		CubeBuilder dialog = new CubeBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				(TabularCubeHandle) level.getContainer().getContainer().getContainer());

		dialog.showPage(CubeBuilder.GROUPPAGE);
		return (dialog.open() == IDialogConstants.OK_ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return ((LevelHandle) getSelection()).canEdit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#getTransactionLabel()
	 */
	@Override
	protected String getTransactionLabel() {
		return Messages.getFormattedString("cube.level.edit", //$NON-NLS-1$
				new String[] { ((LevelHandle) getSelection()).getName() });
	}
}
