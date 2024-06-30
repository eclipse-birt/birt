/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
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
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
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
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * RCP New Report Template Wizard.
 *
 */

public class NewReportTemplateWizard extends Wizard implements INewWizard, IExecutableExtension {

	private static final String NEW = Messages.getString("NewTemplateWizard.title.New"); //$NON-NLS-1$

	private static final String REPORT = Messages.getString("NewTemplateWizard.title.Template"); //$NON-NLS-1$

	private static final String WIZARDPAGE = Messages.getString("NewTemplateWizard.title.WizardPage"); //$NON-NLS-1$

	private static final String CREATE_A_NEW_REPORT = Messages.getString("NewTemplateWizard.text.CreateReport"); //$NON-NLS-1$

	private static final String CREATING = Messages.getString("NewTemplateWizard.text.Creating"); //$NON-NLS-1$

	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString("NewTemplateWizard.text.OpenFileForEditing"); //$NON-NLS-1$

	private static final String NEW_REPORT_FILE_NAME_PREFIX = Messages
			.getString("NewTemplateWizard.displayName.NewReportFileNamePrefix"); //$NON-NLS-1$

	private String fileExtension = "." + IReportElementConstants.TEMPLATE_FILE_EXTENSION; //$NON-NLS-1$

	private static final String TEMPLATE_FILE = "/templates/blank_report.rpttemplate"; //$NON-NLS-1$

	private static final String SAVE_TEMPLATE_PROPERTIES_MESSAGES = Messages
			.getString("SaveReportAsWizard.SettingPage.Messages"); //$NON-NLS-1$

	private WizardNewReportCreationPage newReportFileWizardPage;

	private WizardReportSettingPage settingPage;

	public NewReportTemplateWizard() {
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
		newReportFileWizardPage = new WizardNewReportCreationPage(WIZARDPAGE) {

			@Override
			public boolean validatePage() {
				return validatePage(fileExtension);
			}
		};

		addPage(newReportFileWizardPage);

		// set titles
		newReportFileWizardPage.setTitle(REPORT);
		newReportFileWizardPage.setDescription(CREATE_A_NEW_REPORT);

		// initialize new report file page.
		newReportFileWizardPage.setInitialFileName(getNewFileFullName(NEW_REPORT_FILE_NAME_PREFIX));
		newReportFileWizardPage.setInitialFileLocation(getDefaultLocation());

		settingPage = new WizardReportSettingPage(null);
		settingPage.setTitle(Messages.getFormattedString("SaveReportAsWizard.SettingPage.title", //$NON-NLS-1$
				new Object[] { Messages.getString("NewTemplateWizard.WizardPageTitle.Template") }));//$NON-NLS-1$
		settingPage.setPageDesc(SAVE_TEMPLATE_PROPERTIES_MESSAGES);

		addPage(settingPage);
	}

	/**
	 * Get the default location for the provided name.
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
	@Override
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
		} else if (!fn.toLowerCase(Locale.getDefault()).endsWith(fileExtension)) {
			fileName = fn + fileExtension;
		} else {
			fileName = fn;
		}

		if (Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST) == null) {
			return true;
		}
		URL url = FileLocator.find(Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST), new Path(TEMPLATE_FILE),
				null);

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

			@Override
			public void run(IProgressMonitor monitor) {
				try {
					doFinish(locPath, fileName, templateFileName, monitor);
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
	 * @param locationPath
	 * @param fileName
	 * @param stream
	 * @param monitor
	 */

	private void doFinish(IPath locationPath, String fileName, String templateFileName, IProgressMonitor monitor) {
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
				ExceptionUtil.openError(Messages.getString("PublishTemplateAction.wizard.errorTitle"), //$NON-NLS-1$
						Messages.getString("PublishTemplateAction.wizard.msgDirErr")); //$NON-NLS-1$
				return;
			}

		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		try {
			ReportDesignHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
					.createDesignFromTemplate(templateFileName);
			if (ReportPlugin.getDefault().getEnableCommentPreference()) {
				handle.setStringProperty(ModuleHandle.COMMENTS_PROP, ReportPlugin.getDefault().getCommentPreference());
			}
			setReportSettings(handle);

			handle.setBidiOrientation(
					ReportPlugin.getDefault().getLTRReportDirection() ? DesignChoiceConstants.BIDI_DIRECTION_LTR
							: DesignChoiceConstants.BIDI_DIRECTION_RTL);

			handle.saveAs(file.getAbsolutePath());
			handle.close();

		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		monitor.worked(1);
		monitor.setTaskName(OPENING_FILE_FOR_EDITING);
		getShell().getDisplay().asyncExec(new Runnable() {

			@Override
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
					page.openEditor(new ReportEditorInput(file), IReportEditorContants.TEMPLATE_EDITOR_ID, true);
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

	@Override
	public boolean canFinish() {
		return newReportFileWizardPage.isPageComplete() && settingPage.canFinish();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
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
			UIUtil.setDPI(handle);
		} catch (SemanticException e) {
		}
	}
}
