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

package org.eclipse.birt.report.designer.ui.samples.ide.action;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.samples.nls.Messages;
import org.eclipse.birt.report.designer.ui.samplesview.action.IOpenSampleReportAction;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.SampleIncludedSourceEntry;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;

public class IDEOpenSampleReportAction extends Action implements IOpenSampleReportAction, Listener {

	private static final String ACTION_TEXT = Messages.getString("SampleReportsView.Action.openSampleReport"); //$NON-NLS-1$

	private static final String SCRIPTING_CATEGORY = "Scripted Data Source"; //$NON-NLS-1$

	private static final String DRILL_TO_DETAILS_CATEGORY = "Drill to Details"; //$NON-NLS-1$

	private ReportExamples composite;

	private IProject reportProject;

	public IDEOpenSampleReportAction() {
		super(ACTION_TEXT);
		setToolTipText(Messages.getString("SampleReportsView.Action.openSampleReport.toolTipText.ide")); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ENABLE_IMPORT));
		setDisabledImageDescriptor(
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_DISABLE_IMPORT));
		setEnabled(false);
	}

	@Override
	public void setMainComposite(ReportExamples composite) {
		this.composite = composite;
		composite.addSelectedListener(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		TreeItem item = (TreeItem) composite.getSelectedElement();
		final Object selectedElement = item.getData();
		if (selectedElement == null || !(selectedElement instanceof ReportDesignHandle)) {
			return;
		}

		/*
		 * 1.Create a report project
		 */
		if (item.getParentItem().getText().equals(DRILL_TO_DETAILS_CATEGORY)) {
			reportProject = createProject(DRILL_TO_DETAILS_CATEGORY, false);

			if (reportProject == null) {
				return;
			}
			PlaceResources.copyDrillThroughReport(composite.getShell(), reportProject.getLocation().toOSString(),
					item.getText());
		}

		/*
		 * Create an Eclipse Java project if selecting scripted data source sample.
		 */
		else if (item.getParentItem().getText().equals(SCRIPTING_CATEGORY)) {
			reportProject = createProject(SCRIPTING_CATEGORY, true);
			if (reportProject != null) {
				createSourceAndOutputFolder(reportProject);
				try {
					setClasspath(reportProject);
				} catch (CoreException e) {
					ExceptionUtil.handle(e);
				}

				Enumeration enumeration = SampleIncludedSourceEntry.getJavaObjects();
				while (enumeration.hasMoreElements()) {
					URL javaObjectURL = (URL) enumeration.nextElement();
					String filename = javaObjectURL.getFile();
					String desFileName = filename.substring(filename.lastIndexOf('/') + 1);

					PlaceResources.copy(composite.getShell(), reportProject.getFolder("src") //$NON-NLS-1$
							.getLocation().toOSString(), desFileName, javaObjectURL);
				}
			}
		} else {
			reportProject = createProject(item.getText().substring(0, item.getText().lastIndexOf(".")), false); //$NON-NLS-1$
		}
		/*
		 * 2.Place the sample report into project folder
		 */
		if (reportProject != null) {
			PlaceResources.copy(composite.getShell(), reportProject.getLocation().toOSString(), item.getText(),
					((ReportDesignHandle) selectedElement).getFileName());

			PlaceResources.copyExcludedRptDesignes(composite.getShell(), reportProject.getLocation().toOSString(),
					((ReportDesignHandle) selectedElement).getFileName(), false);
		} else {
			return;
		}

		/*
		 * 3.Refresh the report project
		 */
		if (reportProject != null) {
			refreshReportProject(reportProject);
		}

		/*
		 * Copy the plug-in zip if selecting extending BIRT sample
		 */
		// Bugzilla 281827 (No need to open the dialog)
		// if ( item.getParentItem( ).getParentItem( ) != null
		// && item.getParentItem( ).getParentItem( ).getText( ).equals(
		// EXTENDING_CATEGORY ) )
		// {
		// PlaceExtendedPlugin( item.getParentItem( ).getText( ) );
		// }

		ISafeRunnable op = new ISafeRunnable() {

			@Override
			public void run() {
				String fileName = ((ReportDesignHandle) selectedElement).getFileName();
				doFinish(reportProject, fileName.substring(fileName.lastIndexOf('/') + 1));
			}

			@Override
			public void handleException(Throwable exception) {
				ExceptionUtil.handle(exception);
			}
		};
		SafeRunner.run(op);

	}

	private void doFinish(final IContainer locationPath, final String fileName) {

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				IWorkbenchPage page = window.getActivePage();
				try {
					// sanity checks
					if (page == null || locationPath == null) {
						throw new IllegalArgumentException();
					}

					// open the editor on the file
					IDE.openEditor(page, locationPath.getFile(new Path(fileName)), true);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});
	}

	// @SuppressWarnings("unchecked")
	// private void PlaceExtendedPlugin( String categoryName )
	// {
	// Enumeration enumeration = SampleIncludedSourceEntry.getExtendedPlugin(
	// categoryName );
	// URL pluginURL = (URL) enumeration.nextElement( );
	// String filename = pluginURL.getFile( );
	// String pluginName = filename.substring( filename.lastIndexOf( '/' ) + 1
	// );
	//
	// final FileDialog saveDialog = new FileDialog( composite.getShell( ),
	// SWT.SAVE );
	// saveDialog.setFilterExtensions( EXTENDING_PLUGIN_PATTERN );
	// saveDialog.setFileName( pluginName );
	// if ( saveDialog.open( ) == null )
	// return;
	//
	// PlaceResources.copy( composite.getShell( ),
	// saveDialog.getFilterPath( ),
	// saveDialog.getFileName( ),
	// pluginURL );
	// }

	private IProject createProject(String projectName, boolean isJavaProject) {
		ProjectNameDialog projectNameDlg = new ProjectNameDialog(UIUtil.getDefaultShell());
		projectNameDlg.setTitle(Messages.getString("IDEOpenSampleReportAction.ProjectNameDialog.Title.PrjectName"));
		projectNameDlg.setProjectName(projectName);

		if (projectNameDlg.open() == Window.CANCEL) {
			return null;
		}

		projectName = projectNameDlg.getProjectName();

		final IProject projectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (projectHandle.exists()) {
			String[] buttonLabels = { IDialogConstants.PROCEED_LABEL,
					Messages.getString("IDEOpenSampleReportAction.MessageDialog.ProjectExists.ButtonText"),
					IDialogConstants.CANCEL_LABEL };
			MessageDialog messageDlg = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("IDEOpenSampleReportAction.MessageDialog.ProjectExists.Title"), null,
					Messages.getFormattedString("IDEOpenSampleReportAction.MessageDialog.ProjectExists.Message",
							buttonLabels),
					MessageDialog.INFORMATION, buttonLabels, 0);
			messageDlg.open();
			if (messageDlg.getReturnCode() == 0) {
				// proceed
				return projectHandle;
			}

			if (messageDlg.getReturnCode() == 1) {
				// overwrite
				try {
					projectHandle.delete(true, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (messageDlg.getReturnCode() == 2) {
				// cancel
				return null;
			}

		}

		final IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription(projectHandle.getName());

		if (isJavaProject) {
			String[] natures = { JavaCore.NATURE_ID, "org.eclipse.birt.report.designer.ui.reportprojectnature", //$NON-NLS-1$
			};
			description.setNatureIds(natures);
			addJavaBuildSpec(description);
		} else {
			String[] natures = { "org.eclipse.birt.report.designer.ui.reportprojectnature", //$NON-NLS-1$
			};
			description.setNatureIds(natures);
		}

		// create the new project operation
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException {
				create(description, projectHandle, monitor);
			}
		};

		try {
			new ProgressMonitorDialog(composite.getShell()).run(false, true, op);
		} catch (InterruptedException e) {
			ExceptionUtil.handle(e);
			return null;
		} catch (InvocationTargetException e) {
			// ie.- one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			if (t instanceof CoreException) {
				if (((CoreException) t).getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
					MessageDialog.openError(composite.getShell(),
							Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
							Messages.getFormattedString("NewReportProjectWizard.caseVariantExistsError", //$NON-NLS-1$
									new String[] { projectHandle.getName() }) // ,
					);
				} else {
					ErrorDialog.openError(composite.getShell(),
							Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
							null, // no special message
							((CoreException) t).getStatus());
				}
			} else {
				// CoreExceptions are handled above, but unexpected runtime
				// exceptions and errors may still occur.
				ExceptionUtil.handle(e);

				MessageDialog.openError(composite.getShell(), Messages.getString("NewReportProjectWizard.errorMessage"), //$NON-NLS-1$
						Messages.getFormattedString("NewReportProjectWizard.internalError", //$NON-NLS-1$
								new Object[] { t.getMessage() }));
			}
			return null;
		}

		return projectHandle;
	}

	private void create(IProjectDescription description, IProject projectHandle, IProgressMonitor monitor)
			throws CoreException, OperationCanceledException {
		try {
			monitor.beginTask("", 2000);//$NON-NLS-1$
			projectHandle.create(description, new SubProgressMonitor(monitor, 1000));
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			projectHandle.open(new SubProgressMonitor(monitor, 1000));

		} finally {
			monitor.done();
		}
	}

	private void refreshReportProject(final IProject project) {
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException {
				project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			}
		};

		try {
			new ProgressMonitorDialog(composite.getShell()).run(false, true, op);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void addJavaBuildSpec(IProjectDescription description) {
		ICommand command = description.newCommand();
		command.setBuilderName(JavaCore.BUILDER_ID);
		description.setBuildSpec(new ICommand[] { command });
	}

	private void createSourceAndOutputFolder(IProject project) {

		IFolder srcFolder = project.getFolder("src"); //$NON-NLS-1$
		if (!srcFolder.exists()) {
			try {
				createFolder(srcFolder);
			} catch (CoreException e) {
				ExceptionUtil.handle(e);
			}
		}

		IFolder outputFolder = project.getFolder("bin"); //$NON-NLS-1$
		if (!outputFolder.exists()) {
			try {
				createFolder(outputFolder);
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

		IPath path = project.getFullPath().append("bin"); //$NON-NLS-1$
		javaProject.setOutputLocation(path, null);

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
		IClasspathEntry[] entries = new IClasspathEntry[1];
		IPath path = project.getFullPath().append("src"); //$NON-NLS-1$
		entries[0] = JavaCore.newSourceEntry(path);
		return entries;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget == null || !(event.widget instanceof TreeItem)) {
			setEnabled(false);
		}
		TreeItem item = (TreeItem) event.widget;
		if (item == null) {
			super.setEnabled(false);
			return;
		}
		Object selectedElement = item.getData();
		if (selectedElement == null) {
			super.setEnabled(false);
		} else {
			super.setEnabled(selectedElement instanceof ReportDesignHandle);
		}
	}

	static class ProjectNameDialog extends Dialog {

		Text text;
		String projectName = "";
		String title;

		protected ProjectNameDialog(Shell shell) {
			super(shell);
			// TODO Auto-generated constructor stub
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setProjectName(String name) {
			this.projectName = name;
		}

		public String getProjectName() {
			return projectName;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			getShell().setText(title);

			Composite parentComposite = (Composite) super.createDialogArea(parent);
			Composite composite = new Composite(parentComposite, SWT.NONE);
			GridData gd = new GridData();
			gd.widthHint = 320;
			composite.setLayoutData(gd);
			GridLayout layout = new GridLayout(2, false);
			composite.setLayout(layout);
			Label label = new Label(composite, SWT.NONE);
			label.setText(Messages.getString("IDEOpenSampleReportAction.ProjectNameDialog.Label.PrjectName"));
			text = new Text(composite, SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text.setText(projectName);
			return parentComposite;
		}

		@Override
		protected void okPressed() {
			this.projectName = text.getText().trim();
			super.okPressed();
		}

	}
}
