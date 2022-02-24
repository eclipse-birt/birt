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

package org.eclipse.birt.report.designer.ui.internal.rcp.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardTemplateChoicePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.OpenCheatSheetAction;

/**
 * An implementation of <code>INewWizard</code>. Creates a new blank report
 * file.
 */

public class NewReportWizard extends Wizard implements INewWizard, IExecutableExtension {

	private static final String NEW = Messages.getString("NewReportWizard.title.New"); //$NON-NLS-1$

	private static final String REPORT = Messages.getString("NewReportWizard.title.Report"); //$NON-NLS-1$

	private static final String WIZARDPAGE = Messages.getString("NewReportWizard.title.WizardPage"); //$NON-NLS-1$

	private static final String TEMPLATECHOICEPAGE = Messages.getString("NewReportWizard.title.Template"); //$NON-NLS-1$

	private static final String CREATE_A_NEW_REPORT = Messages.getString("NewReportWizard.text.CreateReport"); //$NON-NLS-1$

	private static final String SELECT_A_REPORT_TEMPLATE = Messages.getString("NewReportWizard.text.SelectTemplate"); //$NON-NLS-1$

	private static final String CREATING = Messages.getString("NewReportWizard.text.Creating"); //$NON-NLS-1$

	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewReportWizard.text.OpenFileForEditing"); //$NON-NLS-1$

	private static final String NEW_REPORT_FILE_NAME_PREFIX = Messages
			.getString("NewReportWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$

	private String fileExtension = "." //$NON-NLS-1$
			+ IReportElementConstants.DESIGN_FILE_EXTENSION;

	private WizardNewReportCreationPage newReportFileWizardPage;

	private WizardTemplateChoicePage templateChoicePage;

	// /private WizardReportSettingPage settingPage;

	public NewReportWizard() {
		setWindowTitle(NEW);
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
		newReportFileWizardPage = new WizardNewReportCreationPage(WIZARDPAGE);

		addPage(newReportFileWizardPage);

		templateChoicePage = new WizardTemplateChoicePage(TEMPLATECHOICEPAGE);
		addPage(templateChoicePage);

		// set titles
		newReportFileWizardPage.setTitle(REPORT);
		newReportFileWizardPage.setDescription(CREATE_A_NEW_REPORT);
		templateChoicePage.setTitle(REPORT);
		templateChoicePage.setDescription(SELECT_A_REPORT_TEMPLATE);

		// settingPage = new WizardReportSettingPage( null );
		// settingPage.setTitle( Messages.getFormattedString(
		// "SaveReportAsWizard.SettingPage.title",//$NON-NLS-1$
		// new Object[]{
		// Messages.getString( "NewReportWizard.wizardPageTitle.report" )} )
		// );//$NON-NLS-1$
		// settingPage.setMessage( Messages.getString(
		// "SaveReportAsWizard.SettingPage.message" ) ); //$NON-NLS-1$
		//
		// addPage( settingPage );

		// initialize new report file page.
		newReportFileWizardPage.setInitialFileName(getNewFileFullName(NEW_REPORT_FILE_NAME_PREFIX));
		newReportFileWizardPage.setInitialFileLocation(getDefaultLocation());
	}

	/**
	 * Get the defualt location for the provided name.
	 * 
	 * @return the location
	 */
	private String getDefaultLocation() {
		IPath defaultPath = Platform.getLocation();
		return defaultPath.toOSString();
	}

	private String getNewFileFullName(String defaultName) {
		String path = getDefaultLocation();
		String name = defaultName + fileExtension;

		int count = 0;

		File file;

		file = new File(path, name);

		while (file.exists()) {
			count++;
			name = defaultName + "_" + count + fileExtension; //$NON-NLS-1$
			file = null;
			file = new File(path, name);
		}

		file = null;

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		final IPath locPath = newReportFileWizardPage.getFileLocationFullPath();
		String fn = newReportFileWizardPage.getFileName();

		final String fileName;
		if (!Platform.getOS().equals(Platform.WS_WIN32)) {
			if (!fn.endsWith(fileExtension)) {
				fileName = fn + fileExtension;
			} else {
				fileName = fn;
			}
		} else {
			if (!fn.toLowerCase(Locale.getDefault()).endsWith(fileExtension)) {
				fileName = fn + fileExtension;
			} else {
				fileName = fn;
			}
		}

		String cheatSheetIdFromPage = "";//$NON-NLS-1$
		boolean showCheatSheetFromPage = false;

		final ReportDesignHandle selTemplate = templateChoicePage.getTemplate();
		final String templateFileName = selTemplate.getFileName();

		cheatSheetIdFromPage = selTemplate.getCheatSheet();
		if (cheatSheetIdFromPage == null) {
			cheatSheetIdFromPage = ""; //$NON-NLS-1$
		}
		showCheatSheetFromPage = templateChoicePage.getShowCheatSheet();

		final String cheatSheetId = cheatSheetIdFromPage;
		final boolean showCheatSheet = showCheatSheetFromPage;
		final boolean isUseDefaultLibray = templateChoicePage.isUseDefaultLibrary();
		final LibraryHandle libraryName = templateChoicePage.getDefaultLibraryHandle();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				try {
					doFinish(locPath, fileName, templateFileName, resolveRemoteStream(templateFileName, selTemplate),
							cheatSheetId, showCheatSheet, isUseDefaultLibray, libraryName, monitor);
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

	/**
	 * The worker method. It will find the container, create the file if missing or
	 * just replace its contents, and open the editor on the newly created file.
	 * 
	 * @param cheatSheetId
	 * 
	 * @param locationPath
	 * @param fileName
	 * @param showCheatSheet
	 * @param monitor
	 */

	private void doFinish(IPath locationPath, String fileName, final String templateFileName,
			final InputStream templateStream, final String cheatSheetId, final boolean showCheatSheet,
			boolean isUseDefaultLibrary, LibraryHandle library, IProgressMonitor monitor) {
		// create a sample file
		monitor.beginTask(CREATING + fileName, 2);

		final File file = new File(locationPath.toString(), fileName);
		try {
			File container = new File(locationPath.toString());

			boolean conExists = container.exists();
			if (!conExists) {
				conExists = container.mkdirs();
			}
			if (!conExists) {
				ExceptionUtil.openError(Messages.getString("NewReportWizard.title.Error"), //$NON-NLS-1$
						Messages.getString("NewReportWizard.wizard.msgDirErr")); //$NON-NLS-1$
				return;
			}

			ReportDesignHandle handle;

			if (templateStream == null) {
				handle = SessionHandleAdapter.getInstance().getSessionHandle()
						.createDesignFromTemplate(templateFileName);
			} else {
				handle = SessionHandleAdapter.getInstance().getSessionHandle()
						.createDesignFromTemplate(templateFileName, templateStream);
			}

			if (ReportPlugin.getDefault().getEnableCommentPreference()) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP, ReportPlugin.getDefault().getCommentPreference());
			}
			if (ReportPlugin.getDefault().getDefaultUnitPreference() != null) {
				handle.setStringProperty(ModuleHandle.UNITS_PROP, ReportPlugin.getDefault().getDefaultUnitPreference());
			}
			if (isPredifinedTemplate(templateFileName)) {
				handle.setDisplayName(null);
				handle.setDescription(null);
			}

			// add the create property
			UIUtil.addCreateBy(handle);
			UIUtil.setDPI(handle);
			// bidi_hcg start
			// save value of bidiLayoutOrientation property

			String bidiOrientation;
			if (templateChoicePage.isLTRDirection())
				bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_LTR;
			else
				bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_RTL;

			handle.setBidiOrientation(bidiOrientation);

			// Support the default library
			if (isUseDefaultLibrary) {
				UIUtil.includeLibrary(handle, DEUtil.DEFAULT_LIBRARY, true);
			}
			// bidi_hcg end
			handle.saveAs(file.getAbsolutePath());
			handle.close();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		monitor.worked(1);
		monitor.setTaskName(OPENING_FILE_FOR_EDITING);
		getShell().getDisplay().asyncExec(new Runnable() {

			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

				IWorkbenchPage page = window.getActivePage();
				try {
					// sanity checks
					if (page == null) {
						throw new IllegalArgumentException();
					}

					// open the editor on the file
					page.openEditor(new ReportEditorInput(file), IReportEditorContants.DESIGN_EDITOR_ID, true);

					// setReportSettings( ( (RCPReportEditor) editorPart
					// ).getModel( ) );
					// editorPart.doSave( null );

					if (showCheatSheet && !cheatSheetId.equals("")) //$NON-NLS-1$
					{
						// this is to ensure the cheatshet is opened in the
						// view, not the dialog.
						Display.getCurrent().getActiveShell().setData(page);

						new OpenCheatSheetAction(cheatSheetId).run();
					}

					IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault()
							.getResourceSynchronizerService();

					if (synchronizer != null) {
						synchronizer.notifyResourceChanged(new ReportResourceChangeEvent(this,
								Path.fromOSString(file.getAbsolutePath()), IReportResourceChangeEvent.NewResource));
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		monitor.worked(1);

	} /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.wizard.IWizard#canFinish()
		 */

	public boolean canFinish() {
		return templateChoicePage.isPageComplete() && newReportFileWizardPage.isPageComplete();
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
	}

	private boolean isPredifinedTemplate(String sourceFileName) {
		String predifinedDir = UIUtil.getFragmentDirectory();
		File predifinedFile = new File(predifinedDir);
		File sourceFile = new File(sourceFileName);
		if (sourceFile.getAbsolutePath().startsWith(predifinedFile.getAbsolutePath())) {
			return true;
		}
		return false;
	}
}
