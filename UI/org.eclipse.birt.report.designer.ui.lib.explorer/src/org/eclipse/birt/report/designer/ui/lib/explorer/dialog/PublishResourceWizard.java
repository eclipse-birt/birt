/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;

/**
 * PublishResourceWizard
 */
public class PublishResourceWizard extends Wizard {

	private static String windowTitle = Messages.getString("PublishResourceDialog.ShellText"); //$NON-NLS-1$
	private static String PAGE_TITLE = Messages.getString("PublishResourceDialog.TitleArea"); //$NON-NLS-1$
	private static String PAGE_DESC = Messages.getString("PublishResourceDialog.Message"); //$NON-NLS-1$

	private static String addLibraryTitle = Messages.getString("PublishResourceDialog.AddResource"); //$NON-NLS-1$

	private String filePath;
	private String fileName;
	private String folderName;

	private WizardResourceSettingPage page;

	public static final int HAVE_HANDLE = 1;
	public static final int HAVE_NO_HANDLE = 0;

	int type;

	/**
	 * The instance of <code>IRunnableWithProgress</code>, using the progress
	 * monitor for this progress dialog, to copy file.
	 */
	private IRunnableWithProgress copyFileRunnable = null;

	/**
	 * 
	 */
	public PublishResourceWizard(LibraryHandle handle, String fileName, String folderName) {
		setWindowTitle(windowTitle);
		this.fileName = fileName;
		this.folderName = folderName;
		this.filePath = handle.getFileName();
		type = HAVE_HANDLE;
	}

	/**
	 * 
	 */
	public PublishResourceWizard(String folderName) {
		setWindowTitle(addLibraryTitle);
		this.fileName = null;
		this.folderName = folderName;
		type = HAVE_NO_HANDLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new WizardResourceSettingPage(type);

		if (type == HAVE_HANDLE) {

			page.setTitle(PAGE_TITLE);
			page.setMessage(PAGE_DESC);

			page.setFileName(fileName);
			page.setfolderName(folderName);
		} else if (type == HAVE_NO_HANDLE) {
			page.setTitle(Messages.getString("PublishResourceDialog.AddText")); //$NON-NLS-1$
			page.setMessage(Messages.getString("PublishResourceDialog.AddMessage")); //$NON-NLS-1$
			page.setfolderName(folderName);
		}
		page.setType(type);
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		fileName = page.getFileName();
		folderName = page.getFolder();
		filePath = getSourceFile().getAbsolutePath();
		return publishiLibrary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return page.canFinish();
	}

	private boolean publishiLibrary() {
		// copy to library folder

		if (!(new File(filePath).exists())) {
			ExceptionUtil.openError(Messages.getString("PublishResourceAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishResourceAction.wizard.message.SourceFileNotExist")); //$NON-NLS-1$

			return false;
		}

		File targetFile = getTargetFile();

		if (targetFile == null) {
			ExceptionUtil.openError(Messages.getString("PublishResourceAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishResourceAction.wizard.notvalidfolder")); //$NON-NLS-1$

			return false;
		}

		if (new File(filePath).compareTo(targetFile) == 0) {
			ExceptionUtil.openError(Messages.getString("PublishResourceAction.wizard.errorTitle"), //$NON-NLS-1$
					Messages.getString("PublishResourceAction.wizard.message")); //$NON-NLS-1$
			return false;
		}

		int overwrite = Window.OK;
		try {
			if (targetFile.exists()) {
				String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
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
				doCopy(filePath, targetFile);

				IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

				if (synchronizer != null) {
					synchronizer.notifyResourceChanged(new ReportResourceChangeEvent(this,
							Path.fromOSString(targetFile.getAbsolutePath()), IReportResourceChangeEvent.NewResource));
				}
			}
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}

		return overwrite != 2;
	}

	/**
	 * Copies files in a monitor dialog.
	 * 
	 * @param filePath   the file path
	 * @param targetFile the target file
	 * @throws IOException if an I/O error occurs.
	 */
	private void doCopy(final String filePath, final File targetFile) throws IOException {
		if (copyFileRunnable == null) {
			copyFile(filePath, targetFile);
			return;
		}

		try {
			new ProgressMonitorDialog(UIUtil.getDefaultShell()).run(false, true, copyFileRunnable);
		} catch (InvocationTargetException e) {
			ExceptionUtil.handle(e);
		} catch (InterruptedException e) {
			ExceptionUtil.handle(e);
		}
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

	/**
	 * Returns the source file.
	 * 
	 * @return the source file.
	 */
	public File getSourceFile() {
		if (type == HAVE_NO_HANDLE) {
			return new File(page.getSourceFileName());
		}
		return new File(filePath);
	}

	/**
	 * Returns the target file.
	 * 
	 * @return the target file.
	 */
	public File getTargetFile() {
		File targetFolder = new File(folderName);

		if (targetFolder.exists() && (!targetFolder.isDirectory())) {
			return null;
		}

		if (!targetFolder.exists()) {
			if (!targetFolder.mkdirs()) {
				return null;
			}
		}

		return new File(targetFolder, fileName);
	}

	/**
	 * Set the specified instance of <code>IRunnableWithProgress</code> using the
	 * progress monitor for this progress dialog, to copy files.
	 * 
	 * @param runnable the specified instance of <code>IRunnableWithProgress</code>.
	 */
	public void setCopyFileRunnable(IRunnableWithProgress runnable) {
		this.copyFileRunnable = runnable;
	}
}
