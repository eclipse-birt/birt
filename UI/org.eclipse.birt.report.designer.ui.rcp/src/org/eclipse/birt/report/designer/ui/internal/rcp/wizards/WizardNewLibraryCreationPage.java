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

package org.eclipse.birt.report.designer.ui.internal.rcp.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.NewReportPageSupport;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Wizard Page for RCP New Library Wizard.
 */

public class WizardNewLibraryCreationPage extends WizardPage implements INewLibraryCreationPage {

	private static final String MSG_DUPLICATE_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.duplicate.fileName"); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_LOCATION_DIRECTORY = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.locationDirectory"); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.name"); //$NON-NLS-1$

	private static final String CREATING = Messages.getString("NewReportWizard.text.Creating"); //$NON-NLS-1$

	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewReportWizard.text.OpenFileForEditing"); //$NON-NLS-1$

	private String fileExtension = IReportElementConstants.LIBRARY_FILE_EXTENSION;

	private static final String TEMPLATE_FILE = "/templates/blank_library.rpttemplate"; //$NON-NLS-1$

	private Listener locationModifyListener = new Listener() {

		public void handleEvent(Event e) {
			setPageComplete(validatePage());
		}
	};

	NewReportPageSupport pageSupport = null;

	/**
	 * The Constructor.
	 * 
	 * @param pageName
	 */
	public WizardNewLibraryCreationPage(String pageName) {
		super(pageName);
		pageSupport = new NewReportPageSupport();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		setControl(pageSupport.createComposite(parent));

		pageSupport.getFileNameField().addListener(SWT.Modify, locationModifyListener);
		pageSupport.getLocationPathField().addListener(SWT.Modify, locationModifyListener);

		setPageComplete(validatePage());
		setErrorMessage(null);
		setMessage(null);
		UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_LIBRARY_WIZARD_ID);
	}

	public void setVisible(boolean visible) {
		getControl().setVisible(visible);
		if (visible) {
			pageSupport.getFileNameField().setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	public boolean validatePage() {
		if (getFileName().equals(""))//$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_NAME);
			return false;
		}

		String location = getContainerFullPath().toOSString();

		if (location.equals("")) //$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_LOCATION_DIRECTORY);
			return false;
		}

		IPath path;

		if (!Platform.getOS().equals(Platform.OS_WIN32)) {
			if (!getFileName().endsWith(IReportEditorContants.LIBRARY_FILE_EXTENTION)) {
				path = getContainerFullPath().append(getFileName() + IReportEditorContants.LIBRARY_FILE_EXTENTION);
			} else {
				path = getContainerFullPath().append(getFileName());
			}
		} else {
			if (!getFileName().toLowerCase().endsWith(IReportEditorContants.LIBRARY_FILE_EXTENTION.toLowerCase())) {
				path = getContainerFullPath().append(getFileName() + IReportEditorContants.LIBRARY_FILE_EXTENTION);
			} else {
				path = getContainerFullPath().append(getFileName());
			}
		}

		if (path.lastSegment().equals("." + fileExtension)) {
			setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameEmpty")); //$NON-NLS-1$
			return false;
		}

		if (path.toFile().exists()) {
			setErrorMessage(MSG_DUPLICATE_FILE_NAME);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	public void setContainerFullPath(IPath initPath) {
		pageSupport.setInitialFileLocation(initPath.toOSString());
	}

	private static final String NEW_REPORT_FILE_NAME_PREFIX = Messages
			.getString("NewLibraryWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$

	public void setFileName(String initFileName) {
		pageSupport.setInitialFileName(getNewFileFullName(NEW_REPORT_FILE_NAME_PREFIX));
	}

	private String getNewFileFullName(String defaultName) {
		String path = getDefaultLocation();
		String name = defaultName + "." + fileExtension; //$NON-NLS-1$

		int count = 0;

		File file;

		file = new File(path, name);

		while (file.exists()) {
			count++;
			name = defaultName + "_" + count + "." + fileExtension; //$NON-NLS-1$ //$NON-NLS-2$
			file = null;
			file = new File(path, name);
		}

		file = null;

		return name;
	}

	private String getDefaultLocation() {
		IPath defaultPath = Platform.getLocation();
		return defaultPath.toOSString();
	}

	public String getFileName() {
		return pageSupport.getFileName();
	}

	public IPath getContainerFullPath() {
		return pageSupport.getFileLocationFullPath();
	}

	public boolean performFinish() {
		final IPath locPath = getContainerFullPath();
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
		URL url = FileLocator.find(Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST), new Path(TEMPLATE_FILE),
				null);

		if (url == null) {
			return true;
		}

		final String libraryFileName;
		try {
			libraryFileName = FileLocator.resolve(url).getPath();
		} catch (IOException e1) {
			return false;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				try {
					doFinish(locPath, fileName, libraryFileName, monitor);
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

	private void doFinish(IPath locationPath, String fileName, String sourceFileName, IProgressMonitor monitor) {
		// create a sample file
		monitor.beginTask(CREATING + fileName, 2);

		// final File file = new File( locationPath.toString( ), fileName );
		File container = null;
		try {
			container = new File(locationPath.toString());
			if (!container.exists()) {
				container.mkdirs();
			}

		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		if (container == null) {
			return;
		}
		final File file = new File(locationPath.toString(), fileName);

		try {
			ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
					.createLibraryFromTemplate(sourceFileName);

			if (ReportPlugin.getDefault().getEnableCommentPreference()) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP, ReportPlugin.getDefault().getCommentPreference());
			}

			if (ReportPlugin.getDefault().getDefaultUnitPreference() != null) {
				handle.setStringProperty(ModuleHandle.UNITS_PROP, ReportPlugin.getDefault().getDefaultUnitPreference());
			}

			if (inPredifinedTemplateFolder(sourceFileName)) {

				String description = handle.getDescription();
				if (description != null && description.trim().length() > 0) {
					handle.setDescription(Messages.getString(description));
				}

			}
			// add the create property
			UIUtil.addCreateBy(handle);
			handle.saveAs(file.getAbsolutePath());
			handle.close();

		} catch (Exception e) {
		}

		monitor.worked(1);
		monitor.setTaskName(OPENING_FILE_FOR_EDITING);
		getShell().getDisplay().asyncExec(new Runnable() {

			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				IWorkbenchPage page = window.getActivePage();
				try {
					page.openEditor(new ReportEditorInput(file), IReportEditorContants.LIBRARY_EDITOR_ID, true);
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
		// do nothing on updating perspective for RCP
	}
}
