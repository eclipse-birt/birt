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

import java.util.List;

import org.eclipse.birt.report.designer.data.ui.dataset.AdvancedDataSetEditor;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetEditor;
import org.eclipse.birt.report.designer.data.ui.util.WizardUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSourceSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetNodeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with dataset node
 */
public class EditableDataSetNodeProvider extends DataSetNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the menu.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {

		if (((DataSetHandle) object).canEdit()) {
			WizardUtil.createEditDataSetMenu(menu, object);
		}

		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		DataSetHandle dsHandle = (DataSetHandle) handle;
		if (!(dsHandle instanceof JointDataSetHandle || dsHandle instanceof DerivedDataSetHandle)
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
				(DataSetHandle) handle, false, false);

		return dialog.open() == Dialog.OK;
	}
}
