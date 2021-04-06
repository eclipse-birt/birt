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

package org.eclipse.birt.report.designer.data.ui.actions;

import java.util.List;

import org.eclipse.birt.report.designer.data.ui.dataset.AdvancedDataSetEditor;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetEditor;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSourceSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */
public class EditDataSetAction extends AbstractElementAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditDataSetAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditDataSetAction(Object selectedObject) {
		super(selectedObject);
		setId(ID);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public EditDataSetAction(Object selectedObject, String text) {
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
			System.out.println("Edit data set action >> Runs ..."); //$NON-NLS-1$
		}
		DataSetHandle dsHandle = (DataSetHandle) getSelection();
		if (!(dsHandle instanceof JointDataSetHandle) && !(dsHandle instanceof DerivedDataSetHandle)
				&& dsHandle.getDataSource() == null) {
			try {
				List dataSourceList = DEUtil.getDataSources();
				String[] names = new String[dataSourceList.size()];
				for (int i = 0; i < names.length; i++) {
					names[i] = ((DataSourceHandle) dataSourceList.get(i)).getName();
				}
				DataSourceSelectionDialog dataSorucedialog = new DataSourceSelectionDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						Messages.getString("dataSourceSelectionPage.title"), //$NON-NLS-1$
						names);
				if (dataSorucedialog.open() == Dialog.CANCEL)
					return false;
				dsHandle.setDataSource(dataSorucedialog.getResult().toString());
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				return false;
			}
		}
		DataSetEditor dialog = new AdvancedDataSetEditor(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				dsHandle, false, false);
		return (dialog.open() == IDialogConstants.OK_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return ((DataSetHandle) getSelection()).canEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#getTransactionLabel()
	 */
	protected String getTransactionLabel() {
		return Messages.getFormattedString("dataset.edit", new String[] { ((DataSetHandle) getSelection()).getName() }); //$NON-NLS-1$
	}
}
