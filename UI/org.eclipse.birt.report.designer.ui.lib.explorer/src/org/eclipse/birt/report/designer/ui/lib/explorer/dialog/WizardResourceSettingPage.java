/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.dialog;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * 
 */

public class WizardResourceSettingPage extends WizardPage {

	private static String PAGE_DESC = Messages.getString("PublishResourceDialog.Message"); //$NON-NLS-1$
	private static String PAGE_DESC2 = Messages.getString("PublishResourceDialog.AddMessage"); //$NON-NLS-1$

	private static String LABEL_FILE_NAME = Messages.getString("PublishResourceDialog.Label.FileName"); //$NON-NLS-1$

	private static String LABEL_SOURCE_FILE_NAME = Messages.getString("PublishResourceDialog.Label.SourceFileName"); //$NON-NLS-1$

	private static String LABEL_FOLDER = Messages.getString("PublishResourceDialog.Label.Folder"); //$NON-NLS-1$

	private static String BUTTON_BROWSE = Messages.getString("PublishResourceDialog.Label.Browse"); //$NON-NLS-1$
	private static String BUTTON_BROWSE2 = Messages.getString("PublishResourceDialog.Label.Browse2"); //$NON-NLS-1$

	private static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.actions.PublishResourceAction"; //$NON-NLS-1$

	private String[] libraryExtensions = { Messages.getString("report.designer.ui.preferences.extensions") //$NON-NLS-1$
			+ ";*.css;*.CSS", //$NON-NLS-1$
			"*.*" //$NON-NLS-1$
	};

	private String dialogTitle = Messages.getString("AddResourceAction.Dialog.Tilte"); //$NON-NLS-1$

	private File selectedFolder;

	private Status pageStatus;

	private String fileName;
	private String folder;

	private Text nameText;
	private Text folderText;
	private Text sourceFileText;

	private Status OKStatus = new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", null); //$NON-NLS-1$
	private Status ErrorStatus = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("WizardResourceSettingPage.ErrorMessage"), //$NON-NLS-1$
			null);

	private int type;

	public void setType(int type) {
		this.type = type;
	}

	public String getSourceFileName() {
		if (sourceFileText != null && type == PublishResourceWizard.HAVE_NO_HANDLE) {
			return sourceFileText.getText();
		}

		return null;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setfolderName(String folder) {
		this.folder = folder;
	}

	public String getFolder() {
		folder = folderText.getText();
		return folder;
	}

	public String getFileName() {
		fileName = nameText.getText();
		return fileName;
	}

	/**
	 * @param pageName
	 */
	public WizardResourceSettingPage(int type) {
		super(""); //$NON-NLS-1$
		if (type == PublishResourceWizard.HAVE_NO_HANDLE) {
			pageStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC2, null);
		} else {
			pageStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		}
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public WizardResourceSettingPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createControl(Composite parent) {

		UIUtil.bindHelp(parent, IHelpContextIds.ADD_RESOURCE_DIALOG_ID);

		Composite container = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		gd.heightHint = 350;
		container.setLayoutData(gd);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);

		if (type == PublishResourceWizard.HAVE_NO_HANDLE) {
			new Label(container, SWT.NONE).setText(LABEL_SOURCE_FILE_NAME);
			int style = SWT.BORDER | SWT.SINGLE;
			sourceFileText = createText(container, 1, style);
			sourceFileText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					checkStatus(type);
				}
			});

			Button chooseBtn = new Button(container, SWT.NONE);
			chooseBtn.setText(BUTTON_BROWSE2);
			chooseBtn.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					String sourceFileName = getFilePath();
					if (sourceFileName != null) {
						sourceFileText.setText(sourceFileName);
						nameText.setText(sourceFileName.substring(sourceFileName.lastIndexOf(File.separator) + 1));
						nameText.setFocus();
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

		}

		new Label(container, SWT.NONE).setText(LABEL_FILE_NAME);
		int style = SWT.BORDER | SWT.SINGLE;
		nameText = createText(container, 1, style);
		new Label(container, SWT.NONE);
		if (fileName != null) {
			nameText.setText(fileName);
		}

		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus(type);
			}
		});

		new Label(container, SWT.NONE).setText(LABEL_FOLDER);
		style = SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY;
		folderText = createText(container, 1, style);
		if (folder != null) {
			folderText.setText(folder);
		}
		folderText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus(type);
			}
		});

		Button chooseBtn = new Button(container, SWT.NONE);
		chooseBtn.setText(BUTTON_BROWSE);
		chooseBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog(false);

				dialog.setAllowMultiple(false);
				dialog.setTitle(Messages.getString("WizardResourceSettingPage.Resourcefile.Dialog.Title")); //$NON-NLS-1$
				dialog.setMessage(Messages.getString("WizardResourceSettingPage.Resourcefile.Dialog.Message")); //$NON-NLS-1$
				dialog.setValidator(new Validator());

				if (dialog.open() == dialog.OK) {
					Object[] selected = dialog.getResult();
					if (selected.length > 0) {
						ResourceEntry file = (ResourceEntry) selected[0];
						folderText.setText(new File(file.getURL().getPath()).getAbsolutePath());
					}

				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		nameText.forceFocus();
		setControl(container);

	}

	public void checkStatus(int type) {
		// Initialize a variable with the no error status
		Status status = null;
		if (type == PublishResourceWizard.HAVE_NO_HANDLE) {
			status = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC2, null);
		} else {
			status = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		}

		if (type == PublishResourceWizard.HAVE_NO_HANDLE && isTextEmpty(sourceFileText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("PublishResourceDialog.Message.SourceFileEmpty"), //$NON-NLS-1$
					null);
		} else if (type == PublishResourceWizard.HAVE_NO_HANDLE
				&& (new File(sourceFileText.getText()).isDirectory() || !new File(sourceFileText.getText()).exists())) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getFormattedString("AddResourceAction.Error.FileNotFound", //$NON-NLS-1$
							new String[] { sourceFileText.getText() }), null);
		} else if (isTextEmpty(nameText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("PublishResourceDialog.Message.FileNameEmpty"), //$NON-NLS-1$
					null);
		} else if (isTextEmpty(folderText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("PublishResourceDialog.Message.FolderEmpty"), //$NON-NLS-1$
					null);
		}

		pageStatus = status;

		// Show the most serious error
		applyToStatusLine(pageStatus);
		getWizard().getContainer().updateButtons();

	}

	/**
	 * Applies the status to the status line of a dialog page.
	 */
	private void applyToStatusLine(IStatus status) {
		String message = status.getMessage();
		if (message.length() == 0)
			message = PAGE_DESC;
		switch (status.getSeverity()) {
		case IStatus.OK:
			setErrorMessage(null);
			setMessage(message);
			break;
		case IStatus.ERROR:
			setErrorMessage(message);
			setMessage(message, IMessageProvider.ERROR);
			break;
		default:
			setErrorMessage(message);
			setMessage(null);
			break;
		}
	}

	private static boolean isTextEmpty(Text text) {
		String s = text.getText();
		if ((s != null) && (s.trim().length() > 0))
			return false;
		return true;
	}

	private Text createText(Composite container, int column, int style) {
		Text text;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = column;

		text = new Text(container, style);
		text.setLayoutData(gridData);
		return text;
	}

	/**
	 * @see IWizardPage#canFinish()
	 */
	public boolean canFinish() {
		return ((!isTextEmpty(nameText)) && (!isTextEmpty(folderText)) && pageStatus.isOK());
	}

	private class Validator implements ISelectionStatusValidator {

		public IStatus validate(Object[] selection) {
			int nSelected = selection.length;
			if (nSelected == 0 || nSelected > 1) {
				return ErrorStatus;
			}
			return OKStatus;
		}
	}

	private boolean isFileExists(String filePath) {
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		try {
			return new File(selectedFolder, fileName).exists();
		} catch (Exception e) {
		}
		return false;
	}

	private String getFilePath() {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		dialog.setFilterExtensions(libraryExtensions);
		dialog.setText(dialogTitle);
		String fileName = dialog.open();
		if (fileName != null) {
			if (!(new File(fileName).exists())) {
				ExceptionUtil.openError(Messages.getString("AddResourceAction.Error.Title"), //$NON-NLS-1$
						Messages.getFormattedString("AddResourceAction.Error.FileNotFound", new String[] { fileName })); //$NON-NLS-1$
				return getFilePath();
			}
			if (isFileExists(fileName)) {
				ExceptionUtil.openError(Messages.getString("AddResourceAction.Error.Title"), //$NON-NLS-1$
						Messages.getString("AddResourceAction.Error.Message")); //$NON-NLS-1$
				return getFilePath();
			}

			return fileName;
		} else {
			return null;
		}
	}

}
