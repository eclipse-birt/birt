/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.ArrayList;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.EditAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;

/**
 * Group provider class - Populates the Group node menu items and associates to
 * the Action class. - Implements the getChildren method for this node type.
 * 
 * 
 */
public class GroupProvider extends DefaultNodeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#createMenu(
	 * java.lang.Object, org.eclipse.jface.action.IMenuManager)
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		EditAction editAction = new EditAction(object, Messages.getString("GroupProvider.action.text")); //$NON-NLS-1$
		if (DEUtil.getDataSetList((DesignElementHandle) object).isEmpty()) {
			editAction.setEnabled(false);
		}
		menu.add(editAction);
		super.createContextMenu(sourceViewer, object, menu);
	}

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param model the model
	 */
	public Object[] getChildren(Object model) {
		ArrayList list = new ArrayList();
		GroupHandle grpHandle = (GroupHandle) model;
		list.add(grpHandle.getHeader());
		list.add(grpHandle.getFooter());
		return list.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performEdit(org.eclipse.birt.model.api.ReportElementHandle)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		GroupDialog dialog = new GroupDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				GroupDialog.GROUP_DLG_TITLE_EDIT);
		dialog.setInput(handle);
		return (dialog.open() == Dialog.OK);
	}
}