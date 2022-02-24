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

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.EditAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.CascadingParametersDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Provider for Parameter Node
 */
public class ParameterNodeProvider extends DefaultNodeProvider {

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
				new EditAction(object, Messages.getString("ParameterNodeProvider.menu.text.edit"))); //$NON-NLS-1$
	}

	/**
	 * Gets the display name of the node.
	 * 
	 * @param model the model of the node
	 */
	public String getNodeDisplayName(Object model) {
		return DEUtil.getDisplayLabel(model, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performEdit(org.eclipse.birt.model.api.ReportElementHandle)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		if (handle instanceof ScalarParameterHandle) {
			ScalarParameterHandle param = (ScalarParameterHandle) handle;
			if (param.getContainer() instanceof CascadingParameterGroupHandle) {
				CascadingParametersDialog dialog = new CascadingParametersDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						Messages.getString("ParameterNodeProvider.dial.title.editCascading")); //$NON-NLS-1$

				dialog.setInput(param.getContainer());
				return dialog.open() == Dialog.OK;
			}
			ParameterDialog dialog = new ParameterDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					Messages.getString("ParameterNodeProvider.dial.title.editScalar")); //$NON-NLS-1$
			dialog.setInput(handle);
			return dialog.open() == Dialog.OK;

		}
		return true;
	}
}
