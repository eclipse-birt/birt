/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.ide.explorer;

import java.io.File;
import java.util.Objects;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISources;

/**
 *
 */

public class PublishLibraryExplorerHandler extends AbstractHandler {

	protected IFile selectedFile;

	@Override
	public void setEnabled(Object evaluationContext) {

		this.selectedFile = null;

		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object object = context.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
			if (object instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) object;
				if (selection.size() == 1 && selection.getFirstElement() instanceof IFile) {
					this.selectedFile = (IFile) selection.getFirstElement();
				}
			}
		}

		if (this.selectedFile != null) {
			if (Objects.equals(this.selectedFile.getFileExtension(), IReportElementConstants.LIBRARY_FILE_EXTENSION)) {
				String url = this.selectedFile.getLocation().toOSString();
				try {
					ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(url);
					if (handle != null) {
						handle.close();
						this.setBaseEnabled(true);
						return;
					}
				} catch (DesignFileException e) {
					/* No need to handle this exception here this */
				}
			}
		}

		this.setBaseEnabled(false);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (this.selectedFile != null) {
			String url = this.selectedFile.getLocation().toOSString();
			try {
				LibraryHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(url);

				String filePath = handle.getFileName();
				String fileName = null;
				if (filePath != null && filePath.length() != 0) {
					fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				}

				PublishLibraryWizard publishLibrary = new PublishLibraryWizard(handle, fileName,
						ReportPlugin.getDefault().getResourceFolder());

				WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), publishLibrary);

				dialog.setPageSize(500, 250);
				dialog.open();
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				throw new ExecutionException("Error executing command", e);
			}
		}

		return null;
	}
}
