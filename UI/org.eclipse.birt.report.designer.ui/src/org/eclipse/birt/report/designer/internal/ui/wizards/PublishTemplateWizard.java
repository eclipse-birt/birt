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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;

/**
 * PublishTemplateWizard
 */
public class PublishTemplateWizard extends Wizard {

	private static final String windowTitle = Messages.getString("PublishTemplateAction.wizard.title"); //$NON-NLS-1$
	private static final String PAGE_TITLE = Messages.getString("PublishTemplateAction.wizard.page.title"); //$NON-NLS-1$
	private static final String PAGE_DESC = Messages.getString("PublishTemplateAction.wizard.page.desc"); //$NON-NLS-1$

	private WizardReportSettingPage page;
	private ReportDesignHandle handle;

	private static final String[] IMAGE_TYPES = { ".bmp", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			".jpe", //$NON-NLS-1$
			".jfif", //$NON-NLS-1$
			".gif", //$NON-NLS-1$
			".png", //$NON-NLS-1$
			".tif", //$NON-NLS-1$
			".tiff", //$NON-NLS-1$
			".ico", //$NON-NLS-1$
			".svg" //$NON-NLS-1$
	};

	public PublishTemplateWizard(ReportDesignHandle handle) {
		setWindowTitle(windowTitle);
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new WizardReportSettingPage(handle);
		page.setTitle(PAGE_TITLE);
		page.setPageDesc(PAGE_DESC);
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// copy to template folder
		String templateFolderPath = ReportPlugin.getDefault().getTemplatePreference();

		String filePath = handle.getFileName();

		if (!(new File(filePath).exists())) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishTemplateAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishTemplateAction.wizard.message.SourceFileNotExist")); //$NON-NLS-1$
			return true;
		}

		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		File targetFolder = new File(templateFolderPath);
		// if ( !targetFolder.isDirectory( ) )
		// {
		// ExceptionHandler.openErrorMessageBox( Messages.getString(
		// "PublishTemplateAction.wizard.errorTitle" ), //$NON-NLS-1$
		// Messages.getString( "PublishTemplateAction.wizard.notvalidfolder" ) );
		// //$NON-NLS-1$
		// return true;
		// }

		boolean folderExists = targetFolder.exists();
		if (!folderExists) {
			folderExists = targetFolder.mkdirs();
		}

		if (!folderExists) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishTemplateAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishTemplateAction.wizard.msgDirErr")); //$NON-NLS-1$
			return true;
		}

		String targetFileName = fileName;
		if (ReportPlugin.getDefault().isReportDesignFile(fileName)) {
			int index = fileName.lastIndexOf("."); //$NON-NLS-1$
			targetFileName = fileName.substring(0, index) + ".rpttemplate"; //$NON-NLS-1$
		}
		File targetFile = new File(targetFolder, targetFileName);
		if (new File(filePath).compareTo(targetFile) == 0) {
			ExceptionHandler.openErrorMessageBox(Messages.getString("PublishTemplateAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishTemplateAction.wizard.message")); //$NON-NLS-1$
			return true;
		}

		int overwrite = Window.OK;
		try {
			if (targetFile.exists()) {
				String[] buttons = { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
						IDialogConstants.CANCEL_LABEL };
				String question = Messages.getFormattedString("SaveAsDialog.overwriteQuestion", //$NON-NLS-1$
						new Object[] { targetFile.getAbsolutePath() });
				MessageDialog d = new MessageDialog(getShell(), Messages.getString("SaveAsDialog.Question"), //$NON-NLS-1$
						null, question, MessageDialog.QUESTION, buttons, 0);
				overwrite = d.open();
			}
			if (overwrite == Window.OK
					&& (targetFile.exists() || (!targetFile.exists() && targetFile.createNewFile()))) {
				copyFile(filePath, targetFile);

				try {
					setDesignFile(targetFile.getAbsolutePath());
				} catch (DesignFileException | SemanticException | IOException e) {
					ExceptionHandler.handle(e);
					return false;
				}

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

	/**
	 *
	 * set ReportDesignHandle properties.
	 *
	 * @param fileName
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	private void setDesignFile(String fileName) throws DesignFileException, SemanticException, IOException {
		ReportDesignHandle newHandle = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(fileName);
		if (!page.getDisplayName().equals("")) { //$NON-NLS-1$
			newHandle.setDisplayName(page.getDisplayName());
		}

		newHandle.setProperty(ModuleHandle.DESCRIPTION_PROP, page.getDescription());

		if (!page.getPreviewImagePath().equals("")) //$NON-NLS-1$
		{
			newHandle.setIconFile(page.getPreviewImagePath());
			newHandle.deleteThumbnail();
		}
		// if ( !page.getCheetSheetPath( ).equals( "" ) ) //$NON-NLS-1$
		// handle.setCheetSheet( page.getCheetSheetPath( ) );

		newHandle.save();
		newHandle.close();
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return page.canFinish();
	}
}
