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

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.EditAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.dialogs.CascadingParametersDialog;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * CascadingParameterGroupNodeProvider.
 */

public class CascadingParameterGroupNodeProvider extends DefaultNodeProvider {

	public CascadingParameterGroupNodeProvider() {
		super();
	}

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
				new EditAction(object, Messages.getString("ParameterGroupNodeProvider.Action.ParameterEdit"))); //$NON-NLS-1$

	}

	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_ELEMENT_PARAMETER_GROUP;
//		if ( !DEUtil.isLinkedElement( (DesignElementHandle)model ) )
//		{
//			return IReportGraphicConstants.ICON_ELEMENT_PARAMETER_GROUP;
//		}
//		{
//			return IReportGraphicConstants.ICON_CASCADING_PARAMETER_GROUP_ELEMENT_LINK;
//		}
	}

	protected DesignElementHandle createElement(String type) throws Exception {
		return null;
	}

	public Object[] getChildren(Object model) {
		return getChildrenBySlotHandle(((CascadingParameterGroupHandle) model).getParameters());
	}

	protected boolean performEdit(ReportElementHandle handle) {
		CascadingParametersDialog dialog = new CascadingParametersDialog(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				Messages.getString("ParameterNodeProvider.dial.title.editCascading")); //$NON-NLS-1$

		dialog.setInput(handle);

		return dialog.open() == Dialog.OK;
	}
}
