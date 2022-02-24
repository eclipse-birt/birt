/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation .
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

package org.eclipse.birt.report.designer.ui.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * BIRT Project Wizard.
 * 
 */
public class NewLibraryWizard extends Wizard implements INewWizard, IExecutableExtension {

	// private static final String OPENING_FILE_FOR_EDITING =
	// Messages.getString( "NewLibraryWizard.text.OpenFileForEditing" );
	// //$NON-NLS-1$
	// private static final String CREATING = Messages.getString(
	// "NewLibraryWizard.text.Creating" ); //$NON-NLS-1$
	private static final String NEW_REPORT_FILE_NAME_PREFIX = Messages
			.getString("NewLibraryWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$
	private static final String NEW_REPORT_FILE_EXTENSION = Messages
			.getString("NewLibraryWizard.displayName.NewReportFileExtension"); //$NON-NLS-1$
	private static final String NEW_REPORT_FILE_NAME = NEW_REPORT_FILE_NAME_PREFIX + NEW_REPORT_FILE_EXTENSION;
	private static final String CREATE_A_NEW_REPORT = Messages.getString("NewLibraryWizard.text.CreateReport"); //$NON-NLS-1$
	private static final String REPORT = Messages.getString("NewLibraryWizard.title.Report"); //$NON-NLS-1$
	// private static final String WIZARDPAGE = Messages.getString(
	// "NewLibraryWizard.title.WizardPage" ); //$NON-NLS-1$
	private static final String NEW = Messages.getString("NewLibraryWizard.title.New"); //$NON-NLS-1$
	// private static final String CHOOSE_FROM_TEMPLATE = Messages.getString(
	// "NewReportWizard.title.Choose" ); //$NON-NLS-1$

	/** Holds selected project resource for run method access */
	private IStructuredSelection selection;

	private INewLibraryCreationPage newLibraryFileWizardPage;

	private int UNIQUE_COUNTER = 0;

	// private WizardChoicePage choicePage;
	// private WizardCustomTemplatePage customTemplatePage;

	public NewLibraryWizard() {
		setWindowTitle(NEW);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		boolean bool = newLibraryFileWizardPage.performFinish();
		if (bool == true) {
			newLibraryFileWizardPage.updatePerspective(getConfigElement());
		}
		return bool;
		// return newLibraryFileWizardPage.performFinish( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// check existing open project
		// IWorkspaceRoot root = ResourcesPlugin.getWorkspace( ).getRoot( );
		// IProject projects[] = root.getProjects( );
		// boolean foundOpenProject = false;
		// for ( int i = 0; i < projects.length; i++ )
		// {
		// if ( projects[i].isOpen( ) )
		// {
		// foundOpenProject = true;
		// break;
		// }
		// }
		// if ( !foundOpenProject )
		// {
		// MessageDialog.openError( getShell( ),
		// Messages.getString( "NewReportWizard.title.Error" ), //$NON-NLS-1$
		// Messages.getString( "NewReportWizard.error.NoProject" ) );
		// //$NON-NLS-1$
		//
		// // abort wizard. There is no clean way to do it.
		// /**
		// * Remove the exception here 'cause It's safe since the wizard won't
		// * create any file without an open project.
		// */
		// // throw new RuntimeException( );
		// }
		// OK
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#getDefaultPageImage()
	 */
	public Image getDefaultPageImage() {
		return ReportPlugin.getImage("/icons/wizban/create_report_wizard.gif"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		Object adapter = Platform.getAdapterManager().getAdapter(this, INewLibraryCreationPage.class);

		newLibraryFileWizardPage = (INewLibraryCreationPage) adapter;

		addPage(newLibraryFileWizardPage);

		// set titles
		newLibraryFileWizardPage.setTitle(REPORT);
		newLibraryFileWizardPage.setDescription(CREATE_A_NEW_REPORT);

		resetUniqueCount();
		newLibraryFileWizardPage.setFileName(getUniqueReportName());
		newLibraryFileWizardPage.setContainerFullPath(getDefaultContainerPath());
	}

	private void resetUniqueCount() {
		UNIQUE_COUNTER = 0;
	}

	protected IPath getDefaultContainerPath() {
		IWorkbenchWindow benchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPart part = benchWindow.getPartService().getActivePart();

		Object selection = null;
		if (part instanceof IEditorPart) {
			selection = ((IEditorPart) part).getEditorInput();
		} else {
			ISelection sel = benchWindow.getSelectionService().getSelection();
			if ((sel != null) && (sel instanceof IStructuredSelection)) {
				selection = ((IStructuredSelection) sel).getFirstElement();
			}
		}

		IContainer ct = getDefaultContainer(selection);

		if (ct == null) {
			IEditorPart editor = UIUtil.getActiveEditor(true);

			if (editor != null) {
				ct = getDefaultContainer(editor.getEditorInput());
			}
		}

		if (ct != null) {
			return ct.getFullPath();
		}

		return Platform.getLocation();
	}

	private IContainer getDefaultContainer(Object selection) {
		IContainer ct = null;
		if (selection instanceof IAdaptable) {
			IResource resource = (IResource) ((IAdaptable) selection).getAdapter(IResource.class);

			if (resource instanceof IContainer && resource.isAccessible()) {
				ct = (IContainer) resource;
			} else if (resource != null && resource.getParent() != null && resource.getParent().isAccessible()) {
				ct = resource.getParent();
			}
		}

		return ct;
	}

	private String getUniqueReportName() {
		IProject[] pjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (pjs.length != 0) {
			resetUniqueCount();

			boolean goon = true;

			while (goon) {
				goon = false;

				for (int i = 0; i < pjs.length; i++) {
					if (pjs[i].isAccessible()) {
						if (!validDuplicate(NEW_REPORT_FILE_NAME_PREFIX, NEW_REPORT_FILE_EXTENSION, UNIQUE_COUNTER,
								pjs[i])) {
							UNIQUE_COUNTER++;

							goon = true;

							break;
						}
					}
				}
			}

			if (UNIQUE_COUNTER == 0) {
				return NEW_REPORT_FILE_NAME;
			}
			return NEW_REPORT_FILE_NAME_PREFIX + "_" //$NON-NLS-1$
					+ UNIQUE_COUNTER + NEW_REPORT_FILE_EXTENSION;
		} else {
			String path = Platform.getLocation().toOSString();
			String name = NEW_REPORT_FILE_NAME_PREFIX + NEW_REPORT_FILE_EXTENSION;

			int count = 0;

			File file;

			file = new File(path, name);

			while (file.exists()) {
				count++;
				name = NEW_REPORT_FILE_NAME_PREFIX + "_" + count + NEW_REPORT_FILE_EXTENSION; //$NON-NLS-1$
				file = null;
				file = new File(path, name);
			}

			file = null;

			return name;
		}
	}

	private static final List tmpList = new ArrayList();
	private IConfigurationElement configElement;

	private boolean validDuplicate(String prefix, String ext, int count, IResource res) {
		if (res != null && res.isAccessible()) {
			final String name;
			if (count == 0) {
				name = prefix + ext;
			} else {
				name = prefix + "_" + count + ext; //$NON-NLS-1$
			}

			try {
				tmpList.clear();

				res.accept(new IResourceVisitor() {

					public boolean visit(IResource resource) throws CoreException {
						if (resource.getType() == IResource.FILE) {
							if (!Platform.getOS().equals(Platform.OS_WIN32)) {
								if (name.equals(((IFile) resource).getName())) {
									tmpList.add(Boolean.TRUE);
								}
							} else if (name.equalsIgnoreCase(((IFile) resource).getName())) {
								tmpList.add(Boolean.TRUE);
							}
						}

						return true;
					}
				}, IResource.DEPTH_INFINITE, true);

				if (tmpList.size() > 0) {
					return false;
				}
			} catch (CoreException e) {
				ExceptionUtil.handle(e);
			}
		}

		return true;
	}

	/**
	 * Creates a folder resource handle for the folder with the given workspace
	 * path. This method does not create the folder resource; this is the
	 * responsibility of <code>createFolder</code>.
	 * 
	 * @param folderPath the path of the folder resource to create a handle for
	 * @return the new folder resource handle
	 */
	protected IFolder createFolderHandle(IPath folderPath) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		return workspaceRoot.getFolder(folderPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.
	 * eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		this.configElement = config;
	}

	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}
}
