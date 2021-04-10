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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.io.File;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.wizard.WizardDialog;

public class PublishLibraryToResourceFolderAction extends AbstractViewAction {

	public static final String ACTION_TEXT = Messages.getString("PublishLibraryToResourceFolderAction.Action.Text"); //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public PublishLibraryToResourceFolderAction(Object selectedObject) {
		super(selectedObject, ACTION_TEXT);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public PublishLibraryToResourceFolderAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	public boolean isEnable() {
		Object selectObj = getSelection();
		if (selectObj instanceof LibraryHandle) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {

		if (isEnable() == false) {
			return;
		}

		ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

		String filePath = module.getFileName();
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

		PublishLibraryWizard publishLibrary = new PublishLibraryWizard((LibraryHandle) module, fileName,
				ReportPlugin.getDefault().getResourceFolder());

		WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), publishLibrary);

		dialog.setPageSize(500, 250);
		dialog.open();

	}

}
