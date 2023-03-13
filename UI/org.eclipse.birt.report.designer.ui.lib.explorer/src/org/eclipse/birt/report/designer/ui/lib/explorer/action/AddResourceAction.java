/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.dialog.PublishResourceWizard;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * The action class for adding a resource in resource explorer.
 */
public class AddResourceAction extends ResourceAction {

	/**
	 * Constructs a action for adding resource.
	 *
	 * @param viewer the resource explorer page
	 */
	public AddResourceAction(LibraryExplorerTreeViewPage viewer) {
		super(Messages.getString("AddResourceAction.Text"), viewer); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled() {
		try {
			return canInsertIntoSelectedContainer();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void run() {
		File container;

		try {
			container = getSelectedContainer();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
			return;
		}

		if (container == null) {
			return;
		}

		final PublishResourceWizard publishLibrary = new PublishResourceWizard(container.getAbsolutePath());
		WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), publishLibrary) {

			@Override
			protected void okPressed() {
				publishLibrary.setCopyFileRunnable(
						createCopyFileRunnable(publishLibrary.getSourceFile(), publishLibrary.getTargetFile()));

				super.okPressed();
			}
		};

		dialog.setPageSize(500, 250);
		if (dialog.open() == Window.OK) {
			fireResourceChanged(publishLibrary.getTargetFile().getAbsolutePath());
		}
	}
}
