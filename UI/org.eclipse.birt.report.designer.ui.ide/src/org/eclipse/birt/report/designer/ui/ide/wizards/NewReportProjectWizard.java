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

package org.eclipse.birt.report.designer.ui.ide.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * BIRT Project Wizard. Implementation from BasicNewProjectResourceWizard
 * without references page and with report nature.
 * 
 */
public class NewReportProjectWizard extends BasicNewResourceWizard implements IExecutableExtension {

	private WizardNewProjectCreationPage mainPage;

	private boolean isJavaProject;
	private Text sourceText;
	private Text outputText;

	// cache of newly-created project
	private IProject newProject;

	/**
	 * The config element which declares this wizard.
	 */
	private IConfigurationElement configElement;

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();

		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage") { //$NON-NLS-1$

			public void createControl(Composite parent) {

				super.createControl(parent);
				UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_REPORT_PROJECT_ID);

				// Group group = new Group( (Composite) super.getControl( ),
				// SWT.NONE );
				// group.setText( Messages
				// .getString( "NewReportProjectWizard.projectSetting" ) );
				// //$NON-NLS-1$
				// GridLayout layout = new GridLayout( );
				// layout.numColumns = 2;
				// group.setLayout( layout );
				// group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL )
				// );
				//
				// Button javaButton = createButton( group );
				// javaButton.setText( Messages
				// .getString( "NewReportProjectWizard.javaProject" ) );
				// javaButton.addSelectionListener( new SelectionAdapter( )
				// {
				//
				// public void widgetSelected( SelectionEvent e )
				// {
				// isJavaProject = !isJavaProject;
				// sourceText.setEnabled( isJavaProject );
				// outputText.setEnabled( isJavaProject );
				// }
				// } );
				//
				// createLabel( group, Messages
				// .getString( "NewReportProjectWizard.src" ) ); //$NON-NLS-1$
				// sourceText = createText( group );
				//
				// IPreferenceStore store = PreferenceConstants
				// .getPreferenceStore( );
				// sourceText.setText( store
				// .getString( PreferenceConstants.SRCBIN_SRCNAME ) );
				// sourceText.setEnabled( isJavaProject );
				//
				// createLabel( group, Messages
				// .getString( "NewReportProjectWizard.bin" ) ); //$NON-NLS-1$
				// outputText = createText( group );
				// outputText.setText( store
				// .getString( PreferenceConstants.SRCBIN_BINNAME ) );
				// outputText.setEnabled( isJavaProject );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
			 */
			public boolean isPageComplete() {
				return validatePage() || super.isPageComplete();
			}

		};
		mainPage.setTitle(Messages.getString("NewReportProjectWizard.title")); //$NON-NLS-1$
		mainPage.setDescription(Messages.getString("NewReportProjectWizard.description")); //$NON-NLS-1$
		this.addPage(mainPage);

	}

	/**
	 * Creates a new project resource with the selected name.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish on
	 * the wizard; the enablement of the Finish button implies that all controls on
	 * the pages currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this wizard caches the new project once it has been successfully
	 * created; subsequent invocations of this method will answer the same project
	 * resource without attempting to create it again.
	 * </p>
	 * 
	 * @return the created project resource, or <code>null</code> if the project was
	 *         not created
	 */
	private IProject createNewProject() {
		if (newProject != null)
			return newProject;

		// get a project handle
		final IProject newProjectHandle = mainPage.getProjectHandle();

		// get a project descriptor
		IPath newPath = null;
		if (!mainPage.useDefaults())
			newPath = mainPage.getLocationPath();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
		description.setLocation(newPath);

		String[] natures = null;
		if (isJavaProject)
			natures = new String[] { ReportPlugin.NATURE_ID, JavaCore.NATURE_ID };
		else
			natures = new String[] { ReportPlugin.NATURE_ID };

		description.setNatureIds(natures);

		if (isJavaProject)
			addJavaBuildSpec(description);

		// create the new project operation
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			protected void execute(IProgressMonitor monitor) throws CoreException {
				createProject(description, newProjectHandle, monitor);
			}
		};

		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			// ie.- one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			if (t instanceof CoreException) {
				if (((CoreException) t).getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
					MessageDialog.openError(getShell(), Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
							Messages.getFormattedString("NewReportProjectWizard.caseVariantExistsError", //$NON-NLS-1$
									new String[] { newProjectHandle.getName() }) // ,
					);
				} else {
					ErrorDialog.openError(getShell(), Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
							null, // no special message
							((CoreException) t).getStatus());
				}
			} else {
				// CoreExceptions are handled above, but unexpected runtime
				// exceptions and errors may still occur.
				ExceptionUtil.handle(e);

				MessageDialog.openError(getShell(), Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
						Messages.getFormattedString("NewReportProjectWizard.internalError", //$NON-NLS-1$
								new Object[] { t.getMessage() }));
			}
			return null;
		}

		newProject = newProjectHandle;

		return newProject;
	}

	/**
	 * Creates a project resource given the project handle and description.
	 * 
	 * @param description   the project description to create a project resource for
	 * @param projectHandle the project handle to create a project resource for
	 * @param monitor       the progress monitor to show visual progress with
	 * 
	 * @exception CoreException              if the operation fails
	 * @exception OperationCanceledException if the operation is canceled
	 */
	void createProject(IProjectDescription description, IProject projectHandle, IProgressMonitor monitor)
			throws CoreException, OperationCanceledException {
		try {
			monitor.beginTask("", 2000);//$NON-NLS-1$

			projectHandle.create(description, new SubProgressMonitor(monitor, 1000));

			if (monitor.isCanceled())
				throw new OperationCanceledException();

			projectHandle.open(new SubProgressMonitor(monitor, 1000));

		} finally {
			monitor.done();
		}
	}

	/**
	 * Returns the newly created project.
	 * 
	 * @return the created project, or <code>null</code> if project not created
	 */
	public IProject getNewProject() {
		return newProject;
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.getString("NewReportProjectWizard.windowTitle")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getDefaultPageImage()
	 */
	public Image getDefaultPageImage() {
		return ReportPlugin.getImage("/icons/wizban/create_project_wizard.gif"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish() {
		createNewProject();

		if (newProject == null)
			return false;
		if (isJavaProject) {

			createSourceAndOutputFolder(newProject);

			try {
				setClasspath(newProject);
			} catch (JavaModelException e) {
				ExceptionUtil.handle(e);
				return false;
			} catch (CoreException e) {
				ExceptionUtil.handle(e);
				return false;
			}
		}

		updatePerspective();
		selectAndReveal(newProject);

		return true;
	}

	/**
	 * Stores the configuration element for the wizard. The config element will be
	 * used in <code>performFinish</code> to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		configElement = cfig;
	}

	/**
	 * Updates the perspective for the active page within the window.
	 */
	protected void updatePerspective() {
		BasicNewProjectResourceWizard.updatePerspective(configElement);
	}

	// private Button createButton( Composite container )
	// {
	// Button button = new Button( container, SWT.CHECK );
	// GridData gd = new GridData( );
	// gd.horizontalSpan = 2;
	// button.setLayoutData( gd );
	// return button;
	// }
	//
	// private Label createLabel( Composite container, String text )
	// {
	// Label label = new Label( container, SWT.NONE );
	// label.setText( text );
	// GridData gd = new GridData( );
	// gd.horizontalIndent = 22;
	// label.setLayoutData( gd );
	// return label;
	// }
	//
	// private Text createText( Composite container )
	// {
	// Text text = new Text( container, SWT.BORDER | SWT.SINGLE );
	// GridData gd = new GridData( GridData.FILL_HORIZONTAL );
	// gd.widthHint = 300;
	// text.setLayoutData( gd );
	// text.addModifyListener( new ModifyListener( )
	// {
	//
	// public void modifyText( ModifyEvent e )
	// {
	// // validatePage();
	// mainPage.isPageComplete( );
	// }
	// } );
	// return text;
	// }

	private void addJavaBuildSpec(IProjectDescription description) {
		ICommand command = description.newCommand();
		command.setBuilderName(JavaCore.BUILDER_ID);
		description.setBuildSpec(new ICommand[] { command });
	}

	private void createSourceAndOutputFolder(IProject project) {
		if (isJavaProject && sourceText.getText() != null && sourceText.getText().trim().length() > 0) {
			IFolder folder = project.getFolder(sourceText.getText());
			if (!folder.exists())
				try {
					createFolder(folder);
				} catch (CoreException e) {
					ExceptionUtil.handle(e);
				}
		}
		if (isJavaProject && outputText.getText() != null && outputText.getText().trim().length() > 0) {
			IFolder folder = project.getFolder(outputText.getText());
			if (!folder.exists())
				try {
					createFolder(folder);
				} catch (CoreException e) {
					ExceptionUtil.handle(e);
				}
		}
	}

	private void createFolder(IFolder folder) throws CoreException {
		if (!folder.exists()) {
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder) {
				createFolder((IFolder) parent);
			}
			folder.create(true, true, null);
		}
	}

	private void setClasspath(IProject project) throws JavaModelException, CoreException {
		IJavaProject javaProject = JavaCore.create(project);

		if (outputText.getText() != null && outputText.getText().trim().length() > 0) {
			IPath path = project.getFullPath().append(outputText.getText());
			javaProject.setOutputLocation(path, null);
		}

		IClasspathEntry[] entries = getClassPathEntries(project);
		javaProject.setRawClasspath(entries, null);
	}

	private IClasspathEntry[] getClassPathEntries(IProject project) {
		IClasspathEntry[] internalClassPathEntries = getInternalClassPathEntries(project);
		IClasspathEntry[] entries = new IClasspathEntry[internalClassPathEntries.length + 1];
		System.arraycopy(internalClassPathEntries, 0, entries, 0, internalClassPathEntries.length);
		entries[entries.length - 1] = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$
		return entries;
	}

	protected IClasspathEntry[] getInternalClassPathEntries(IProject project) {
		if (sourceText.getText() == null || sourceText.getText().trim().equals("")) //$NON-NLS-1$
		{
			return new IClasspathEntry[0];
		}
		IClasspathEntry[] entries = new IClasspathEntry[1];
		IPath path = project.getFullPath().append(sourceText.getText());
		entries[0] = JavaCore.newSourceEntry(path);
		return entries;
	}
}
