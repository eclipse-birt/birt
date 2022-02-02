/* Copyright (c) 2004 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.ide.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class NewTemplateWizard extends NewReportWizard {

	private static final String WIZARDPAGE = Messages.getString("NewTemplateWizard.title.WizardPage"); //$NON-NLS-1$
	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewTemplateWizard.text.OpenFileForEditing"); //$NON-NLS-1$
	private static final String CREATING = Messages.getString("NewTemplateWizard.text.Creating"); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_FILE_NAME_PREFIX = Messages
			.getString("NewTemplateWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_DESCRIPTION = Messages
			.getString("NewTemplateWizard.pageDescription.createNewTemplate"); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_TITLE = Messages.getString("NewTemplateWizard.title.Template"); //$NON-NLS-1$

	private static final String SAVE_TEMPLATE_PROPERTIES_TITLE = Messages
			.getString("SaveReportAsWizard.SettingPage.title"); //$NON-NLS-1$
	private static final String SAVE_TEMPLATE_PROPERTIES_MESSAGES = Messages
			.getString("SaveReportAsWizard.SettingPage.Messages"); //$NON-NLS-1$

	public NewTemplateWizard() {
		super("." + IReportElementConstants.TEMPLATE_FILE_EXTENSION); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.ide.wizards.NewReportWizard#init(
	 * org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle(Messages.getString("NewTemplateWizard.title.New")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		newReportFileWizardPage = new WizardNewReportCreationPage(WIZARDPAGE, getSelection(),
				IReportElementConstants.TEMPLATE_FILE_EXTENSION);
		addPage(newReportFileWizardPage);

		resetUniqueCount();
		newReportFileWizardPage.setFileName(getUniqueReportName(NEW_TEMPLATE_FILE_NAME_PREFIX, getFileExtension()));
		newReportFileWizardPage.setContainerFullPath(getDefaultContainerPath());
		newReportFileWizardPage.setDescription(NEW_TEMPLATE_DESCRIPTION);
		newReportFileWizardPage.setTitle(NEW_TEMPLATE_TITLE);

		settingPage = new WizardReportSettingPage(null);
		settingPage.setTitle(SAVE_TEMPLATE_PROPERTIES_TITLE);
		settingPage.setPageDesc(SAVE_TEMPLATE_PROPERTIES_MESSAGES);

		addPage(settingPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return newReportFileWizardPage.isPageComplete() && settingPage.canFinish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.ide.wizards.NewReportWizard#performFinish
	 * ()
	 */
	public boolean performFinish() {
		final IPath containerName = newReportFileWizardPage.getContainerFullPath();
		String fn = newReportFileWizardPage.getFileName();
		final String fileName;
		if (!Platform.getOS().equals(Platform.WS_WIN32)) {
			if (!fn.endsWith(getFileExtension())) {
				fileName = fn + getFileExtension();
			} else {
				fileName = fn;
			}
		} else {
			if (!fn.toLowerCase(Locale.getDefault()).endsWith(getFileExtension())) {
				fileName = fn + getFileExtension();
			} else {
				fileName = fn;
			}
		}

		if (Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST) == null) {
			return true;
		}
		URL url = FileLocator.find(Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST),
				new Path("/templates/blank_report.rpttemplate"), null);//$NON-NLS-1$

		if (url == null) {
			return true;
		}
		final String templateFileName;
		try {
			templateFileName = FileLocator.resolve(url).getPath();
		} catch (IOException e1) {
			return false;
		}
		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, templateFileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			ExceptionUtil.handle(realException);
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing or
	 * just replace its contents, and open the editor on the newly created file.
	 * 
	 * @param cheatSheetId
	 * 
	 * @param containerName
	 * @param fileName
	 * @param showCheatSheet
	 * @param monitor
	 */

	private void doFinish(IPath containerName, String fileName, String sourceFileName, IProgressMonitor monitor)
			throws CoreException {
		// create a sample file
		monitor.beginTask(CREATING + fileName, 2);
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(containerName);
		IContainer container = null;
		if (resource == null || !resource.exists() || !(resource instanceof IContainer)) {
			// create folder if not exist
			IFolder folder = createFolderHandle(containerName);
			UIUtil.createFolder(folder, monitor);
			container = folder;
		} else {
			container = (IContainer) resource;
		}

		final IFile file = container.getFile(new Path(fileName));

		try {
			ReportDesignHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
					.createDesignFromTemplate(sourceFileName);
			if (ReportPlugin.getDefault().getEnableCommentPreference(file.getProject())) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP,
						ReportPlugin.getDefault().getCommentPreference(file.getProject()));
			}
			if (ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()) != null) {
				handle.setStringProperty(ModuleHandle.UNITS_PROP,
						ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()));
			}
			setReportSettings(handle);

			handle.setBidiOrientation(
					ReportPlugin.getDefault().getLTRReportDirection() ? DesignChoiceConstants.BIDI_DIRECTION_LTR
							: DesignChoiceConstants.BIDI_DIRECTION_RTL);

			handle.saveAs(file.getLocation().toOSString());
			handle.close();

		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		// to refresh this project, or file does not exist will be told, though
		// it's created.
		container.refreshLocal(IResource.DEPTH_INFINITE, monitor);

		monitor.worked(1);
		monitor.setTaskName(OPENING_FILE_FOR_EDITING);
		getShell().getDisplay().asyncExec(new Runnable() {

			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				IWorkbenchPage page = window.getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		monitor.worked(1);
	}

	/**
	 * Set report basic settings.
	 * 
	 * @param model
	 * @throws IOException
	 */
	void setReportSettings(Object model) throws IOException {
		ReportDesignHandle handle = (ReportDesignHandle) model;
		try {
			handle.setDisplayName(settingPage.getDisplayName());
			handle.setDescription(settingPage.getDescription());
			handle.setIconFile(settingPage.getPreviewImagePath());
			// add the create property
			UIUtil.addCreateBy(handle);
			if (handle.getProperty(ReportDesignHandle.IMAGE_DPI_PROP) == null) {
				UIUtil.setDPI(handle);
			}
		} catch (SemanticException e) {
		}
	}
}
