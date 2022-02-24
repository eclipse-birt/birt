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
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterGroupDialog;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Parameter group provider
 */
public class ParameterGroupNodeProvider extends DefaultNodeProvider {

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// Add new parameter action
		menu.add(new InsertAction(object, Messages.getString("ParameterGroupNodeProvider.Action.ParameterNew"), //$NON-NLS-1$
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT));

		super.createContextMenu(sourceViewer, object, menu);
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
				new EditAction(object, Messages.getString("ParameterGroupNodeProvider.Action.ParameterEdit"))); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object model) {
		ParameterGroupHandle paramGrpHandle = (ParameterGroupHandle) model;
		return this.getChildrenBySlotHandle(paramGrpHandle.getParameters());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.process.IOutlineProcess#
	 * getNoteDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object object) {
		String name = super.getNodeDisplayName(object);
		if (!MISSINGNAME.equals(name)) {
			return name;
		}
		return Messages.getString("ParameterGroupNodeProvider.Node.Group"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * createElement(java.lang.String)
	 */
	protected DesignElementHandle createElement(String type) throws Exception {
		DesignElementHandle handle = super.createElement(type);
		ParameterDialog dialog = new ParameterDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				Messages.getString("ParameterGroupNodeProvider.Dialogue.ParameterNew")); //$NON-NLS-1$
		dialog.setInput(handle);
		if (dialog.open() == Dialog.CANCEL) {
			return null;
		}
		return (DesignElementHandle) dialog.getResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performEdit(org.eclipse.birt.model.api.ReportElementHandle)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		ParameterGroupDialog dialog = new ParameterGroupDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				Messages.getString("ParameterGroupNodeProvider.Dialogue.ParameterEdit")); //$NON-NLS-1$
		dialog.setInput(handle);
		return dialog.open() == Dialog.OK;
	}
}
