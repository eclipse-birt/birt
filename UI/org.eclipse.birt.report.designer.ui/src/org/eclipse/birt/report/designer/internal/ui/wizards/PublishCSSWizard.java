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

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;

/**
 * PublishCSSWizard
 */
public class PublishCSSWizard extends Wizard {

	// CSSHandle handle;

	private static String WINDOWS_TITLE = Messages.getString("PublishCSSDialog.ShellText"); //$NON-NLS-1$
	private static String PAGE_TITLE = Messages.getString("PublishCSSDialog.TitleArea"); //$NON-NLS-1$
	private static String PAGE_DESC = Messages.getString("PublishCSSDialog.Message"); //$NON-NLS-1$

	private String filePath;
	private String fileName;
	private String folderName;

	private WizardCSSSettingPage page;

	private String widonwTitle;
	private String pageTitle;
	private String pageDescription;

	public void setWizardTitle(String wizardTitle) {
		this.widonwTitle = wizardTitle;
		setWindowTitle(this.widonwTitle);
	}

	public void setPageTitle(String PageTitle) {
		this.pageTitle = PageTitle;
	}

	public void setPageDesc(String pageDesc) {
		this.pageDescription = pageDesc;
	}

	/**
	 * This constructor is used when the CSSHandle is known.
	 *
	 */
	// public PublishCSSWizard( LibraryHandle handle, String fileName,
	// String folderName )
	// {
	// setWindowTitle( windowTitle );
	// this.fileName = fileName;
	// this.folderName = folderName;
	// this.handle = handle;
	// this.filePath = handle.getFileName( );
	// }
	/**
	 *
	 */
	public PublishCSSWizard(String folderName) {
		this.fileName = null;
		this.folderName = folderName;
		// this.handle = null;

		setWizardTitle(WINDOWS_TITLE);
		setPageTitle(PAGE_TITLE);
		setPageDesc(PAGE_DESC);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new WizardCSSSettingPage(pageTitle, pageDescription);

		// page.setTitle( pageTitle );
		// page.setMessage( pageDescription );

		if (fileName != null) // should can be removed
		{
			page.setFileName(fileName);
		}
		page.setfolderName(folderName);

		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		fileName = page.getFileName();
		folderName = page.getFolder();
		// if ( handle == null )
		{
			filePath = page.getSourceFileName();
		}
		return publishiCSSFile();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return page.canFinish();
	}

	private boolean publishiCSSFile() {
		// copy to resource folder

		if (!(new File(filePath).exists())) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishCSSAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishCSSAction.wizard.message.SourceFileNotExist")); //$NON-NLS-1$
			return true;
		}

		File targetFolder = new File(folderName);
		if (targetFolder.exists() && (!targetFolder.isDirectory())) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishCSSAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishCSSAction.wizard.notvalidfolder")); //$NON-NLS-1$
			// $NON-NLS-1$
			return true;
		}
		boolean folderExists = targetFolder.exists();
		if (!folderExists) {
			// if creating dirs fails, it'll return false.
			folderExists = targetFolder.mkdirs();
		}
		if (!folderExists) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishCSSAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishCSSAction.wizard.msgDirErr")); //$NON-NLS-1$
			return false;
		}

		File targetFile = new File(targetFolder, fileName);
		if (new File(filePath).compareTo(targetFile) == 0) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishCSSAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishCSSAction.wizard.message")); //$NON-NLS-1$
			return false;
		}

		int overwrite = Window.OK;
		try {
			if (targetFile.exists()) {
				String[] buttons = { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
						IDialogConstants.CANCEL_LABEL };
				String question = Messages.getFormattedString("SaveAsDialog.overwriteQuestion", //$NON-NLS-1$
						new Object[] { targetFile.getAbsolutePath() });
				MessageDialog d = new MessageDialog(UIUtil.getDefaultShell(),
						Messages.getString("SaveAsDialog.Question"), //$NON-NLS-1$
						null, question, MessageDialog.QUESTION, buttons, 0);
				overwrite = d.open();
			}
			if (overwrite == Window.OK
					&& (targetFile.exists() || (!targetFile.exists() && targetFile.createNewFile()))) {
				copyFile(filePath, targetFile);

				IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

				if (synchronizer != null) {
					synchronizer.notifyResourceChanged(new ReportResourceChangeEvent(this,
							Path.fromOSString(targetFile.getAbsolutePath()), IReportResourceChangeEvent.NewResource));
				}
			}
		} catch (IOException e) {
			ExceptionHandler.handle(e);
		}

		return overwrite != 1;
	}

	private void copyFile(String in, File targetFile) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(targetFile);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}

}
