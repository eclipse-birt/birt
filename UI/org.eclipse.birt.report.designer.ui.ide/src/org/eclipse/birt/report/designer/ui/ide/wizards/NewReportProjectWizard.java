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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
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
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * BIRT Project Wizard. Implementation from BasicNewProjectResourceWizard without
 * references page and with report nature.
 * 
 */
public class NewReportProjectWizard extends BasicNewResourceWizard
implements
	IExecutableExtension {

	


		private WizardNewProjectCreationPage mainPage;


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

			mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");//$NON-NLS-1$
			mainPage.setTitle(Messages.getString("NewReportProjectWizard.title")); //$NON-NLS-1$
			mainPage.setDescription(Messages
					.getString("NewReportProjectWizard.description")); //$NON-NLS-1$
			this.addPage(mainPage);

		
		}
		/**
		 * Creates a new project resource with the selected name.
		 * <p>
		 * In normal usage, this method is invoked after the user has pressed Finish
		 * on the wizard; the enablement of the Finish button implies that all
		 * controls on the pages currently contain valid values.
		 * </p>
		 * <p>
		 * Note that this wizard caches the new project once it has been
		 * successfully created; subsequent invocations of this method will answer
		 * the same project resource without attempting to create it again.
		 * </p>
		 * 
		 * @return the created project resource, or <code>null</code> if the
		 *         project was not created
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
			final IProjectDescription description = workspace
					.newProjectDescription(newProjectHandle.getName());
			description.setLocation(newPath);

			description.setNatureIds(new String[]{"org.eclipse.birt.report.designer.ui.reportprojectnature"}); //$NON-NLS-1$
		
			// create the new project operation
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				protected void execute(IProgressMonitor monitor)
						throws CoreException {
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
						MessageDialog
								.openError(
										getShell(),
										Messages
												.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
										Messages
												.getFormattedString(
														"NewReportProjectWizard.caseVariantExistsError", new String[]{newProjectHandle.getName()}) //$NON-NLS-1$,
								);
					} else {
						ErrorDialog.openError(getShell(), Messages
								.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
								null, // no special message
								((CoreException) t).getStatus());
					}
				} else {
					// CoreExceptions are handled above, but unexpected runtime
					// exceptions and errors may still occur.
					ExceptionHandler.handle( e );
				    
					MessageDialog
							.openError(
									getShell(),
									Messages
											.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
									Messages
											.getFormattedString(
													"NewReportProjectWizard.internalError", new Object[]{t.getMessage()})); //$NON-NLS-1$
				}
				return null;
			}

			newProject = newProjectHandle;

			return newProject;
		}
		/**
		 * Creates a project resource given the project handle and description.
		 * 
		 * @param description
		 *            the project description to create a project resource for
		 * @param projectHandle
		 *            the project handle to create a project resource for
		 * @param monitor
		 *            the progress monitor to show visual progress with
		 * 
		 * @exception CoreException
		 *                if the operation fails
		 * @exception OperationCanceledException
		 *                if the operation is canceled
		 */
		void createProject(IProjectDescription description, IProject projectHandle,
				IProgressMonitor monitor) throws CoreException,
				OperationCanceledException {
			try {
				monitor.beginTask("", 2000);//$NON-NLS-1$

				projectHandle.create(description, new SubProgressMonitor(monitor,
						1000));

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
		 * @return the created project, or <code>null</code> if project not
		 *         created
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
		public Image getDefaultPageImage()
		{
			return ReportPlugin.getImage("/icons/wizban/create_project_wizard.gif"); //$NON-NLS-1$
		}
		
		/*
		 * (non-Javadoc) Method declared on IWizard.
		 */
		public boolean performFinish() {
			createNewProject();

			if (newProject == null)
				return false;

			updatePerspective();
			selectAndReveal(newProject);

			return true;
		}
		
		/**
		 * Stores the configuration element for the wizard. The config element will
		 * be used in <code>performFinish</code> to set the result perspective.
		 */
		public void setInitializationData(IConfigurationElement cfig,
				String propertyName, Object data) {
			configElement = cfig;
		}
		/**
		 * Updates the perspective for the active page within the window.
		 */
		protected void updatePerspective() {
			BasicNewProjectResourceWizard.updatePerspective(configElement);
		}
		
		
	}



