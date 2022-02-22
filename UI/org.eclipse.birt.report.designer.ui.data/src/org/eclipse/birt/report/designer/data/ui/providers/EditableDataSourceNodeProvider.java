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

package org.eclipse.birt.report.designer.data.ui.providers;

import org.eclipse.birt.report.designer.data.ui.actions.ExportElementToSourceCPStoreAction;
import org.eclipse.birt.report.designer.data.ui.datasource.AdvancedDataSourceEditor;
import org.eclipse.birt.report.designer.data.ui.datasource.DataSourceEditor;
import org.eclipse.birt.report.designer.data.ui.util.WizardUtil;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSourceNodeProvider;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with the data source node
 */
public class EditableDataSourceNodeProvider extends DataSourceNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (((DataSourceHandle) object).canEdit()) {
			WizardUtil.createEditDataSourceMenu(menu, object);
		}

		super.createContextMenu(sourceViewer, object, menu);

		ExportElementToSourceCPStoreAction exportSourceAction = new ExportElementToSourceCPStoreAction(object);
		if (exportSourceAction.isEnabled()) {
			menu.add(exportSourceAction);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #performEdit(org.eclipse.birt.model.api.ReportElementHandle)
	 */
	@Override
	protected boolean performEdit(ReportElementHandle handle) {
		if (!(handle instanceof ScriptDataSourceHandle) && handle.canEdit()) {
			DataSourceEditor dialog = new AdvancedDataSourceEditor(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(), (DataSourceHandle) handle);
			return dialog.open() == Dialog.OK;
		}
		return false;
	}

}
