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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage;
import org.eclipse.birt.report.designer.ui.wizards.NewLibraryWizard;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * Add INewLibraryCreationPage adaptable to NewLibraryWizard.
 */

public class NewLibraryWizardAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		NewLibraryWizard wizard = (NewLibraryWizard) adaptableObject;

		return new NewLibraryCreationPage("", wizard.getSelection()); //$NON-NLS-1$
	}

	public Class[] getAdapterList() {
		return new Class[] { INewLibraryCreationPage.class };
	}

}

class NewLibraryCreationPage extends WizardNewFileCreationPage implements INewLibraryCreationPage {

	private String fileExtension = IReportElementConstants.LIBRARY_FILE_EXTENSION;
	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewLibraryWizard.text.OpenFileForEditing"); //$NON-NLS-1$
	private static final String CREATING = Messages.getString("NewLibraryWizard.text.Creating"); //$NON-NLS-1$

	// private static final String NEW_REPORT_FILE_NAME_PREFIX =
	// Messages.getString(
	// "NewLibraryWizard.displayName.NewReportFileNamePrefix" ); //$NON-NLS-1$
	// private static final String NEW_REPORT_FILE_EXTENSION =
	// Messages.getString( "NewLibraryWizard.displayName.NewReportFileExtension"
	// ); //$NON-NLS-1$
	// private static final String NEW_REPORT_FILE_NAME =
	// NEW_REPORT_FILE_NAME_PREFIX
	// + NEW_REPORT_FILE_EXTENSION;
	// private static final String CREATE_A_NEW_REPORT = Messages.getString(
	// "NewLibraryWizard.text.CreateReport" ); //$NON-NLS-1$
	// private static final String REPORT = Messages.getString(
	// "NewLibraryWizard.title.Report" ); //$NON-NLS-1$
	// private static final String WIZARDPAGE = Messages.getString(
	// "NewLibraryWizard.title.WizardPage" ); //$NON-NLS-1$
	// private static final String NEW = Messages.getString(
	// "NewLibraryWizard.title.New" ); //$NON-NLS-1$

	/**
	 * (non-Javadoc) Method declared on IDialogPage.
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_LIBRARY_WIZARD_ID);
	}

	public NewLibraryCreationPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		super.setFileExtension(fileExtension);
	}

	protected void createAdvancedControls(Composite parent) {
	}

	protected IStatus validateLinkedResource() {
		// always return OK here.
		return new Status(IStatus.OK, ReportPlugin.getDefault().getBundle().getSymbolicName(), IStatus.OK, "", null); //$NON-NLS-1$
	}

	public boolean performFinish() {
		final IPath containerName = getContainerFullPath();
		String fn = getFileName();
		final String fileName;
		if (!Platform.getOS().equals(Platform.WS_WIN32)) {
			if (!fn.endsWith("." + fileExtension)) //$NON-NLS-1$
			{
				fileName = fn + "." + fileExtension; //$NON-NLS-1$
			} else {
				fileName = fn;
			}
		} else {
			if (!fn.toLowerCase(Locale.getDefault()).endsWith("." + fileExtension)) //$NON-NLS-1$
			{
				fileName = fn + "." + fileExtension; //$NON-NLS-1$
			} else {
				fileName = fn;
			}
		}

		if (Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST) == null) {
			return true;
		}

		final String libraryFileName = UIUtil.getDefaultLibraryTemplate();
		if (libraryFileName == null) {
			return false;
		}
		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, libraryFileName, monitor);
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
			ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
					.createLibraryFromTemplate(sourceFileName);

			if (ReportPlugin.getDefault().getEnableCommentPreference(file.getProject())) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP,
						ReportPlugin.getDefault().getCommentPreference(file.getProject()));
			}

			if (ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()) != null) {
				handle.setStringProperty(ModuleHandle.UNITS_PROP,
						ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()));
			}

			if (inPredifinedTemplateFolder(sourceFileName)) {

				String description = handle.getDescription();
				if (description != null && description.trim().length() > 0) {
					handle.setDescription(Messages.getString(description));
				}

			}
			// add the create property
			UIUtil.addCreateBy(handle);
			handle.saveAs(file.getLocation().toOSString());
			handle.close();

		} catch (Exception e) {
			// Do nothing now
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
					// page.openEditor( new FileEditorInput( file ),
					// LibraryReportEditor.EDITOR_ID,
					// true );
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		monitor.worked(1);

		fireLibraryChanged(fileName);
	}

	private void fireLibraryChanged(String fileName) {
		SessionHandleAdapter.getInstance().getSessionHandle().fireResourceChange(new LibraryChangeEvent(fileName));
	}

	protected IFolder createFolderHandle(IPath folderPath) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		return workspaceRoot.getFolder(folderPath);
	}

	protected boolean inPredifinedTemplateFolder(String sourceFileName) {
		String predifinedDir = UIUtil.getFragmentDirectory();
		File predifinedFile = new File(predifinedDir);
		File sourceFile = new File(sourceFileName);
		if (sourceFile.getAbsolutePath().startsWith(predifinedFile.getAbsolutePath())) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage#
	 * updatePerspective(org.eclipse.core.runtime.IConfigurationElement)
	 */
	public void updatePerspective(IConfigurationElement configElement) {
		BasicNewProjectResourceWizard.updatePerspective(configElement);
	}

	protected boolean validatePage() {
		boolean rt = super.validatePage();

		if (rt) {
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

		}

		return rt;
	}

}
