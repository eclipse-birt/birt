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

package org.eclipse.birt.report.designer.data.ui.actions;

import org.eclipse.birt.report.designer.data.ui.datasource.AdvancedDataSourceEditor;
import org.eclipse.birt.report.designer.data.ui.datasource.DataSourceEditor;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */
public class EditDataSourceAction extends AbstractElementAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditDataSourceAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditDataSourceAction(Object selectedObject) {
		super(selectedObject);
		setId(ID);
	}

	/**
	 * Returns whether the EditDataSource Action is enabled
	 * 
	 * @param selectedObject
	 * @param text
	 */
	public EditDataSourceAction(Object selectedObject, String text) {
		super(selectedObject, text);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit data source action >> Runs ..."); //$NON-NLS-1$
		}
		DataSourceHandle handle = (DataSourceHandle) getSelection();
		DataSourceEditor dialog = new AdvancedDataSourceEditor(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				handle);

		return (dialog.open() == IDialogConstants.OK_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#getTransactionLabel()
	 */
	protected String getTransactionLabel() {
		return Messages.getFormattedString("datasource.edit", //$NON-NLS-1$
				new String[] { ((DataSourceHandle) getSelection()).getName() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		if (!(getSelection() instanceof ScriptDataSourceHandle) && ((DataSourceHandle) getSelection()).canEdit())
			return true;

		return false;
	}
}
