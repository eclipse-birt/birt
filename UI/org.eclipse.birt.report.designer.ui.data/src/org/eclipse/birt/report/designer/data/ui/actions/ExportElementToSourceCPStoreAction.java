/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.actions;

import org.eclipse.birt.report.designer.data.ui.datasource.ExportDataSourceDialog;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class ExportElementToSourceCPStoreAction extends AbstractViewAction {
	private static final String DISPLAY_TEXT = Messages.getString("ExportToCPSouceAction.action.text"); //$NON-NLS-1$
	private final static String BUTTON_OK = Messages.getString("datasource.exportToCP.errorBox.button.OK"); //$NON-NLS-1$

	public ExportElementToSourceCPStoreAction(Object selectedObject) {
		super(selectedObject, DISPLAY_TEXT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled() {
		// will implement it later.
		Object selection = getSelection();
		if (selection instanceof StructuredSelection) {
			if (((StructuredSelection) selection).size() > 1) {
				return false;
			}
			selection = ((StructuredSelection) selection).getFirstElement();
		}
		if (selection instanceof OdaDataSourceHandle) {
			return !isSampleDB((OdaDataSourceHandle) selection);
		}
		return false;
	}

	private boolean isSampleDB(OdaDataSourceHandle handle) {
		if ("org.eclipse.birt.report.data.oda.jdbc".equals(handle.getExtensionID())) {
			Object driverClass = handle.getProperty("odaDriverClass");
			if (driverClass != null
					&& "org.eclipse.birt.report.data.oda.sampledb.Driver".equals(driverClass.toString()))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Object selection = getSelection();
		if (selection instanceof StructuredSelection) {
			selection = ((StructuredSelection) selection).getFirstElement();
		}
		if (selection instanceof OdaDataSourceHandle) {
			try {
				DesignSessionRequest designSessionRequest = DTPUtil.getInstance()
						.createDesignSessionRequest((OdaDataSourceHandle) selection);

				if (designSessionRequest.getDataSourceDesign().hasLinkToProfile()) {
					MessageDialog errorDialog = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("datasource.exprotToCP.title"), null,
							Messages.getFormattedString("datasource.exportToCP.error.alreadyExported",
									new Object[] { ((OdaDataSourceHandle) selection).getName() }),
							MessageDialog.ERROR, new String[] { BUTTON_OK }, 0);

					errorDialog.open();
					return;
				}
				ExportDataSourceDialog dialog = new ExportDataSourceDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						Messages.getString("datasource.exprotToCP.title"), (DataSourceHandle) selection);

				if (dialog.open() == Dialog.OK) {
					OdaDesignSession session = DataSourceDesignSession.convertDesignToLinkedProfile(
							designSessionRequest, dialog.getProfileName(), dialog.isExternalToCP(),
							dialog.doesCreateProfileStore(), PlatformUI.getWorkbench().getDisplay().getActiveShell());
					DTPUtil.getInstance().updateDataSourceHandle(session.getResponse(),
							session.getRequestDataSourceDesign(), (OdaDataSourceHandle) selection);
				}
			} catch (Exception ex) {
				ExceptionHandler.handle(ex);
			}
		}
	}
}
