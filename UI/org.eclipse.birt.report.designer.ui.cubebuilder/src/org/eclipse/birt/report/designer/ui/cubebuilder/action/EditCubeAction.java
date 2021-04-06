/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.action;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.6 $ $Date: 2007/06/01 07:05:21 $
 */
public class EditCubeAction extends AbstractElementAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditCubeAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditCubeAction(String text) {
		super(text);
		setId(ID);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public EditCubeAction(Object selectedObject, String text) {
		super(selectedObject, text);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit cube action >> Runs ..."); //$NON-NLS-1$
		}
		TabularCubeHandle cubeHandle = null;
		if (getSelection() instanceof TabularCubeHandle)
			cubeHandle = (TabularCubeHandle) getSelection();
		else if (getSelection() instanceof PropertyHandle)
			cubeHandle = (TabularCubeHandle) ((PropertyHandle) getSelection()).getElementHandle();
		CubeBuilder dialog = new CubeBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), cubeHandle);
		if (getSelection() instanceof CubeHandle) {
			dialog.showPage(CubeBuilder.DATASETSELECTIONPAGE);
		} else if (getSelection() instanceof PropertyHandle) {
			dialog.showPage(CubeBuilder.GROUPPAGE);
		}
		return (dialog.open() == IDialogConstants.OK_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		if (getSelection() instanceof TabularCubeHandle)
			return ((TabularCubeHandle) getSelection()).canEdit();
		else if (getSelection() instanceof PropertyHandle
				&& ((PropertyHandle) getSelection()).getElementHandle() instanceof TabularCubeHandle)
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#getTransactionLabel()
	 */
	protected String getTransactionLabel() {
		if (getSelection() instanceof CubeHandle)
			return Messages.getFormattedString("cube.edit", new String[] { ((CubeHandle) getSelection()).getName() }); //$NON-NLS-1$
		return super.getTransactionLabel();
	}
}
