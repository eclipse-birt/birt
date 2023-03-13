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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardTemplateChoicePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.OpenCheatSheetAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * An implementation of <code>INewWizard</code>. Creates a new blank report
 * file.
 */

public class NewReportWizard extends Wizard implements INewWizard, IExecutableExtension {

	// private static final String REPORT_WIZARD = Messages.getString(
	// "NewReportWizard.title.ReportWizard" ); //$NON-NLS-1$
	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewReportWizard.text.OpenFileForEditing"); //$NON-NLS-1$
	// private static final String DOES_NOT_EXIST = Messages.getString(
	// "NewReportWizard.text.DoesNotExist" ); //$NON-NLS-1$
	// private static final String CONTAINER = Messages.getString(
	// "NewReportWizard.text.Container" ); //$NON-NLS-1$
	private static final String CREATING = Messages.getString("NewReportWizard.text.Creating"); //$NON-NLS-1$
	private static final String NEW_REPORT_FILE_NAME_PREFIX = Messages
			.getString("NewReportWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$
	private static final String NEW_REPORT_FILE_EXTENSION = Messages
			.getString("NewReportWizard.displayName.NewReportFileExtension"); //$NON-NLS-1$
	// private static final String NEW_REPORT_FILE_NAME =
	// NEW_REPORT_FILE_NAME_PREFIX;
	private static final String SELECT_A_REPORT_TEMPLATE = Messages.getString("NewReportWizard.text.SelectTemplate"); //$NON-NLS-1$
	private static final String CREATE_A_NEW_REPORT = Messages.getString("NewReportWizard.text.CreateReport"); //$NON-NLS-1$
	String REPORT = Messages.getString("NewReportWizard.title.Report"); //$NON-NLS-1$
	private static final String TEMPLATECHOICEPAGE = Messages.getString("NewReportWizard.title.Template"); //$NON-NLS-1$
	private static final String WIZARDPAGE = Messages.getString("NewReportWizard.title.WizardPage"); //$NON-NLS-1$
	private static final String NEW = Messages.getString("NewReportWizard.title.New"); //$NON-NLS-1$
	// private static final String CHOOSE_FROM_TEMPLATE = Messages.getString(
	// "NewReportWizard.title.Choose" ); //$NON-NLS-1$

	/** Holds selected project resource for run method access */
	private IStructuredSelection selection;

	WizardNewReportCreationPage newReportFileWizardPage;

	WizardReportSettingPage settingPage;

	private WizardTemplateChoicePage templateChoicePage;

	private int UNIQUE_COUNTER = 0;

	private String fileExtension = IReportElementConstants.DESIGN_FILE_EXTENSION;

	// private WizardChoicePage choicePage;
	// private WizardCustomTemplatePage customTemplatePage;

	public NewReportWizard() {
		super();
	}

	public NewReportWizard(String fileType) {
		super();
		this.fileExtension = fileType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final IPath containerName = newReportFileWizardPage.getContainerFullPath();
		String fn = newReportFileWizardPage.getFileName();
		final String fileName;
		if (!Platform.getOS().equals(Platform.WS_WIN32)) {
			if (!fn.endsWith("." + fileExtension)) //$NON-NLS-1$
			{
				fileName = fn + "." + fileExtension; //$NON-NLS-1$
			} else {
				fileName = fn;
			}
		} else if (!fn.toLowerCase(Locale.getDefault()).endsWith("." + fileExtension)) //$NON-NLS-1$
		{
			fileName = fn + "." + fileExtension; //$NON-NLS-1$
		} else {
			fileName = fn;
		}

		String cheatSheetIdFromPage;//$NON-NLS-1$
		boolean showCheatSheetFromPage;

		final ReportDesignHandle selTemplate = templateChoicePage.getTemplate();
		final String templateName = selTemplate.getFileName();

		cheatSheetIdFromPage = templateChoicePage.getTemplate().getCheatSheet();
		if (cheatSheetIdFromPage == null) {
			cheatSheetIdFromPage = ""; //$NON-NLS-1$
		}
		showCheatSheetFromPage = templateChoicePage.getShowCheatSheet();

		final String cheatSheetId = cheatSheetIdFromPage;
		final boolean showCheatSheet = showCheatSheetFromPage;
		final boolean isUseDefaultLibray = templateChoicePage.isUseDefaultLibrary();
		final LibraryHandle library = templateChoicePage.getDefaultLibraryHandle();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, templateName, resolveRemoteStream(templateName, selTemplate),
							cheatSheetId, showCheatSheet, isUseDefaultLibray, library, monitor);
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

	private InputStream resolveRemoteStream(String templateName, ReportDesignHandle handle) {
		if (templateName == null || handle == null) {
			return null;
		}

		File f = new File(templateName);

		if (!f.exists()) {
			try {
				new URL(templateName);
			} catch (Exception e) {
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					handle.serialize(out);

					byte[] bytes = out.toByteArray();
					out.close();

					return new ByteArrayInputStream(bytes);
				} catch (IOException ie) {
					ExceptionHandler.handle(ie, true);
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// check existing open project
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projects[] = root.getProjects();
		boolean foundOpenProject = false;
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].isOpen()) {
				foundOpenProject = true;
				break;
			}
		}
		if (!foundOpenProject) {
			MessageDialog.openError(getShell(), Messages.getString("NewReportWizard.title.Error"), //$NON-NLS-1$
					Messages.getString("NewReportWizard.error.NoProject")); //$NON-NLS-1$

			// abort wizard. There is no clean way to do it.
			/**
			 * Remove the exception here 'cause It's safe since the wizard won't create any
			 * file without an open project.
			 */
			// throw new RuntimeException( );
		}
		// OK
		this.selection = selection;
		setWindowTitle(NEW);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#getDefaultPageImage()
	 */
	@Override
	public Image getDefaultPageImage() {
		return ReportPlugin.getImage("/icons/wizban/create_report_wizard.gif"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		newReportFileWizardPage = new WizardNewReportCreationPage(WIZARDPAGE, selection, fileExtension);
		addPage(newReportFileWizardPage);

		templateChoicePage = new WizardTemplateChoicePage(TEMPLATECHOICEPAGE);
		addPage(templateChoicePage);

		// set titles
		newReportFileWizardPage.setTitle(REPORT);
		newReportFileWizardPage.setDescription(CREATE_A_NEW_REPORT);
		templateChoicePage.setTitle(REPORT);
		templateChoicePage.setDescription(SELECT_A_REPORT_TEMPLATE);

		resetUniqueCount();
		newReportFileWizardPage
				.setFileName(getUniqueReportName(NEW_REPORT_FILE_NAME_PREFIX, NEW_REPORT_FILE_EXTENSION));
		newReportFileWizardPage.setContainerFullPath(getDefaultContainerPath());
		newReportFileWizardPage.setTemplateChoicePage(templateChoicePage);
	}

	/**
	 * Set unique count to zero
	 *
	 */
	void resetUniqueCount() {
		UNIQUE_COUNTER = 0;
	}

	/**
	 * Get the path of default container
	 *
	 */
	IPath getDefaultContainerPath() {
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

		return null;
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

	String getUniqueReportName(String prefix, String ext) {
		int counter = getCounter(prefix, ext);
		return counter == 0 ? prefix + ext
				: prefix + "_" //$NON-NLS-1$
						+ counter + ext;
	}

	int getCounter(String prefix, String ext) {
		IProject[] pjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		resetUniqueCount();

		boolean goon = true;

		while (goon) {
			goon = false;

			for (int i = 0; i < pjs.length; i++) {
				if (pjs[i].isAccessible()) {
					if (!validDuplicate(prefix, ext, UNIQUE_COUNTER, pjs[i])) {
						UNIQUE_COUNTER++;

						goon = true;

						break;
					}
				}
			}
		}

		return UNIQUE_COUNTER;

	}

	private static final List<Boolean> tmpList = new ArrayList<>();
	private IConfigurationElement configElement;

	/**
	 * Judge whether the name new report has been used
	 *
	 * @param prefix
	 * @param ext
	 * @param count
	 * @param res
	 * @return
	 */
	boolean validDuplicate(String prefix, String ext, int count, IResource res) {
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

					@Override
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

	private void doFinish(IPath containerName, String fileName, final String templateFileName,
			final InputStream templateStream, String cheatSheetId, boolean showCheatSheet, boolean isUseDefaultLibrary,
			LibraryHandle library, IProgressMonitor monitor) throws CoreException {
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
		final String cheatId = cheatSheetId;
		final boolean showCheat = showCheatSheet;

		try {
			ReportDesignHandle handle;

			if (templateStream == null) {
				handle = SessionHandleAdapter.getInstance().getSessionHandle()
						.createDesignFromTemplate(templateFileName);
			} else {
				handle = SessionHandleAdapter.getInstance().getSessionHandle()
						.createDesignFromTemplate(templateFileName, templateStream);
			}

			if (ReportPlugin.getDefault().getEnableCommentPreference(file.getProject())) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP,
						ReportPlugin.getDefault().getCommentPreference(file.getProject()));
			}

			if (ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()) != null) {
				handle.setStringProperty(ModuleHandle.UNITS_PROP,
						ReportPlugin.getDefault().getDefaultUnitPreference(file.getProject()));
			}

			if (isPredifinedTemplate(templateFileName)) {
				handle.setDisplayName(null);
				handle.setDescription(null);
			}
			// bidi_hcg start
			// save value of bidiLayoutOrientation property

			String bidiOrientation;
			if (templateChoicePage.isLTRDirection()) {
				bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_LTR;
			} else {
				bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_RTL;
			}

			handle.setBidiOrientation(bidiOrientation);

			// add the create property
			UIUtil.addCreateBy(handle);
			if (handle.getProperty(ReportDesignHandle.IMAGE_DPI_PROP) == null) {
				UIUtil.setDPI(handle);
			}

			// Support the default library
			if (isUseDefaultLibrary) {
				UIUtil.includeLibrary(handle, DEUtil.DEFAULT_LIBRARY, true);
				DEUtil.setDefaultTheme(handle);
			}
			// bidi_hcg end
			handle.saveAs(file.getLocation().toOSString());
			handle.close();
		} catch (Exception e) {
			ExceptionHandler.handle(e, true);
		}

		// to refresh this project, or file does not exist will be told, though
		// it's created.
		file.refreshLocal(IResource.DEPTH_INFINITE, monitor);

		monitor.worked(1);
		monitor.setTaskName(OPENING_FILE_FOR_EDITING);
		getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();

				try {
					IDE.openEditor(page, file, true);

					BasicNewProjectResourceWizard.updatePerspective(configElement);
					if (showCheat && !cheatId.equals("")) //$NON-NLS-1$
					{
						Object oldData = Display.getCurrent().getActiveShell().getData();

						if (oldData instanceof TrayDialog) {
							Display.getCurrent().getActiveShell().setData(null);
						}

						new OpenCheatSheetAction(cheatId).run();

						// Display.getCurrent( )
						// .getActiveShell( )
						// .setData( oldData );
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		monitor.worked(1);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		// Temporary remark the choice page for that feature is not supported in
		// R1
		// if ( choicePage.isBlank( ) )
		// {
		// return newReportFileWizardPage.isPageComplete( );
		// }
		// else if ( choicePage.isCustom( ) )
		// {
		// return customTemplatePage.isPageComplete( )
		// && newReportFileWizardPage.isPageComplete( );
		// }
		// else
		// {
		return templateChoicePage.isPageComplete() && newReportFileWizardPage.isPageComplete();
		// }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		this.configElement = config;
	}

	/**
	 * Report design file extension
	 *
	 * @return file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Set file extension for report design
	 *
	 * @param fileExtension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Get the selection
	 *
	 * @return The selection
	 */
	public IStructuredSelection getSelection() {
		return selection;
	}

	/**
	 * Get configuration element
	 *
	 * @return
	 */
	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	protected boolean isPredifinedTemplate(String sourceFileName) {
		if (sourceFileName == null || sourceFileName.trim().length() == 0) {
			return false;
		}

		String predifinedDir = UIUtil.getFragmentDirectory();
		assert predifinedDir != null;
		File predifinedFile = new File(predifinedDir);
		File sourceFile = new File(sourceFileName);
		if (sourceFile.getAbsolutePath().startsWith(predifinedFile.getAbsolutePath())) {
			return true;
		}
		return false;
	}
}
