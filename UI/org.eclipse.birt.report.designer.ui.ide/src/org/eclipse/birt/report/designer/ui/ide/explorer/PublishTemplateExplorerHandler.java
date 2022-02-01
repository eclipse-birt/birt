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

package org.eclipse.birt.report.designer.ui.ide.explorer;

import java.util.Objects;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishTemplateWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;

/**
 *
 */

public class PublishTemplateExplorerHandler extends AbstractHandler {

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
			if (Objects.equals(this.selectedFile.getFileExtension(), IReportElementConstants.DESIGN_FILE_EXTENSION)
					|| Objects.equals(this.selectedFile.getFileExtension(),
							IReportElementConstants.TEMPLATE_FILE_EXTENSION)) {
				String url = this.selectedFile.getLocation().toOSString();
				try {
					ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(url);
					if (handle != null) {
						handle.close();
						this.setBaseEnabled(true);
						return;
					}
				} catch (DesignFileException e) {
					/* No need to handle this exception here */

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
				ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(url);
				IEditorPart editor = org.eclipse.birt.report.designer.internal.ui.util.UIUtil.findOpenedEditor(url);

				if (editor != null && editor.isDirty()) {
					MessageDialog md = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("PublishTemplateAction.SaveBeforeGenerating.dialog.title"), //$NON-NLS-1$
							null,
							Messages.getFormattedString("PublishTemplateAction.SaveBeforeGenerating.dialog.message", //$NON-NLS-1$
									new Object[] { this.selectedFile.getName() }),
							MessageDialog.CONFIRM,
							new String[] {
									Messages.getString("PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes"), //$NON-NLS-1$
									Messages.getString("PublishTemplateAction.SaveBeforeGenerating.dialog.button.no") //$NON-NLS-1$
							}, 0);
					switch (md.open()) {
					case 0:
						editor.doSave(null);
						break;
					case 1:
					default:
					}
				}

				WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(),
						new PublishTemplateWizard((ReportDesignHandle) handle));
				dialog.setPageSize(500, 250);
				dialog.open();

				handle.close();
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				throw new ExecutionException("Error executing command", e);
			}
		}

		return null;
	}
}
