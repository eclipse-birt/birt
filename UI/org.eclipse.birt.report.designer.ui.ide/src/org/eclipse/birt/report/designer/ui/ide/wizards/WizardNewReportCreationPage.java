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

package org.eclipse.birt.report.designer.ui.ide.wizards;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardTemplateChoicePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * Creation page for Report Wizard without Advanced control
 * 
 */
public class WizardNewReportCreationPage extends WizardNewFileCreationPage {

	String fileExtension = IReportElementConstants.DESIGN_FILE_EXTENSION;

	/**
	 * The Constructor.
	 * 
	 * @param pageName
	 * @param selection
	 */
	public WizardNewReportCreationPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	/**
	 * The Constructor.
	 * 
	 * @param pageName
	 * @param selection
	 * @param fileType
	 */
	public WizardNewReportCreationPage(String pageName, IStructuredSelection selection, String fileType) {
		this(pageName, selection);
		super.setFileExtension(fileType);
		fileExtension = fileType;
	}

	/**
	 * (non-Javadoc) Method declared on IDialogPage.
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		if (fileExtension.equals(IReportElementConstants.TEMPLATE_FILE_EXTENSION)) {
			UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_TEMPLATE_WIZARD_ID);
		} else if (fileExtension.equals(IReportElementConstants.DESIGN_FILE_EXTENSION)) {
			UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_REPORT_WIZARD_ID);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls
	 * (org.eclipse.swt.widgets.Composite)
	 */
	protected void createAdvancedControls(Composite parent) {
		// does nothing here to remove the linked widget.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	protected IStatus validateLinkedResource() {
		// always return OK here.
		return new Status(IStatus.OK, ReportPlugin.getDefault().getBundle().getSymbolicName(), IStatus.OK, "", null); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		boolean rt = super.validatePage();

		if (rt) {
			if (isInValidFilePath()) {
				return false;
			}
			String fn = getFileName();

			if (!Platform.getOS().equals(Platform.OS_WIN32)) {
				IPath resourcePath;
				if (!fn.endsWith("." + fileExtension)) //$NON-NLS-1$
				{
					resourcePath = getContainerFullPath().append(getFileName() + "." + fileExtension); //$NON-NLS-1$
				} else
					resourcePath = getContainerFullPath().append(getFileName());

				if (resourcePath.lastSegment().equals("." + fileExtension)) {
					setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameEmpty")); //$NON-NLS-1$
					return false;
				}

				IWorkspace workspace = ResourcesPlugin.getWorkspace();

				if (workspace.getRoot().getFolder(resourcePath).exists()
						|| workspace.getRoot().getFile(resourcePath).exists()) {
					setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameExists")); //$NON-NLS-1$
					rt = false;
				}

			} else {
				IPath resourcePath;
				if (!fn.toLowerCase().endsWith(("." + fileExtension).toLowerCase())) //$NON-NLS-1$
				{

					resourcePath = getContainerFullPath().append(getFileName() + "." + fileExtension); //$NON-NLS-1$
				} else
					resourcePath = getContainerFullPath().append(getFileName());

				if (resourcePath.lastSegment().equals("." + fileExtension)) {
					setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameEmpty")); //$NON-NLS-1$
					return false;
				}
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				if (workspace.getRoot().getFolder(resourcePath).getLocation().toFile().exists()
						|| workspace.getRoot().getFile(resourcePath).getLocation().toFile().exists()) {
					setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameExists")); //$NON-NLS-1$
					rt = false;
				}
			}

			if (templateChoicePage != null) {
				templateChoicePage.setLTRDirection(ReportPlugin.getDefault().getLTRReportDirection(
						ResourcesPlugin.getWorkspace().getRoot().getProject(getContainerFullPath().lastSegment())));
			}
		}

		return rt;
	}

	private boolean isInValidFilePath() {
		String fn = getFileName();

		IPath resourcePath;
		if (!fn.endsWith("." + fileExtension)) //$NON-NLS-1$
		{
			resourcePath = getContainerFullPath().append(getFileName() + "." + fileExtension); //$NON-NLS-1$
		} else
			resourcePath = getContainerFullPath().append(getFileName());

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IStatus result = workspace.validatePath(resourcePath.removeFileExtension().toString(), IResource.FOLDER);

		if (!result.isOK()) {
			setErrorMessage(result.getMessage());
			return true;
		}

		return false;
	}

	/**
	 * Get File extension
	 * 
	 * @return The file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	public IWizardPage getNextPage() {
		IWizardPage page = super.getNextPage();
		if (page != null && page instanceof WizardReportSettingPage) {
			((WizardReportSettingPage) page).setContainerFullPath(getContainerFullPath());
		}
		return page;
	}

	/**
	 * Set file extension
	 * 
	 * @param fileExtension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	private WizardTemplateChoicePage templateChoicePage;

	public void setTemplateChoicePage(WizardTemplateChoicePage templateChoicePage) {
		this.templateChoicePage = templateChoicePage;
	}

}
