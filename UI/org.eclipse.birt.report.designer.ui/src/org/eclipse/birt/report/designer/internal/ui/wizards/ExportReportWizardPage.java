/*******************************************************************************
 * Copyright (c) 2004-2006 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Administrator
 * 
 */
public class ExportReportWizardPage extends WizardPage implements Listener {

	private static String LABEL_FILE_NAME = Messages.getString("ExportToLibraryAction.wizard.page.label.filename"); //$NON-NLS-1$
	private static String LABEL_FOLDER = Messages.getString("ExportToLibraryAction.wizard.page.label.folder"); //$NON-NLS-1$
	private static String BUTTON_BROWSER = Messages.getString("ExportToLibraryAction.wizard.page.button.browser"); //$NON-NLS-1$
	private static String PLUGIN_ID = "org.eclipse.birt.report.designer.internal.ui.wizards.ExportReportWizardPage"; //$NON-NLS-1$

	private static String PAGE_TITLE = Messages.getString("ExportToLibraryAction.wizard.page.title"); //$NON-NLS-1$
	private static String PAGE_DESC = Messages.getString("ExportToLibraryAction.wizard.page.desc"); //$NON-NLS-1$

	Status nameStatus;
	Status folderStatus;

	private Text nameText;
	private Text folderText;
	private Button browserButton;

	/**
	 * @param pageName
	 */
	public ExportReportWizardPage(String pageName) {
		super(pageName);
		this.setTitle(PAGE_TITLE);
		this.setMessage(PAGE_DESC);
		nameStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		folderStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ExportReportWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.setTitle(PAGE_TITLE);
		this.setMessage(PAGE_DESC);
		nameStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		folderStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createControl(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.EXPORT_TO_LIBRARY_WIZARD_ID);
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);

		new Label(container, SWT.NONE).setText(LABEL_FILE_NAME);
		nameText = createText(container, 1);
		nameText.addListener(SWT.Modify, this);

		new Label(container, SWT.NONE); // emprty

		new Label(container, SWT.NONE).setText(LABEL_FOLDER);
		folderText = createText(container, 1);
		folderText.setText(ReportPlugin.getDefault().getResourceFolder());
		folderText.addListener(SWT.Modify, this);

		browserButton = new Button(container, SWT.PUSH);
		browserButton.setText(BUTTON_BROWSER);
		browserButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(UIUtil.getDefaultShell());
				dialog.setMessage(Messages.getString("ExportToLibraryAction.wizard.page.dirdialog.message")); //$NON-NLS-1$

				String dirName = Platform.getLocation().toString();
				if (dirName != null && dirName.trim().length() > 0) {
					dialog.setFilterPath(dirName.trim());
				}

				String selectedDirectory = dialog.open();
				if (selectedDirectory != null)
					folderText.setText(selectedDirectory);

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		setControl(container);

	}

	/**
	 * Create text filed
	 * 
	 * @param container
	 * @param column
	 */
	private Text createText(Composite container, int column) {
		Text text;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = column;

		text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData(gridData);
		return text;
	}

	private static boolean isTextEmpty(Text text) {
		String s = text.getText();
		if ((s != null) && (s.trim().length() > 0))
			return false;
		return true;
	}

	public void handleEvent(Event event) {

		// Initialize a variable with the no error status
		Status status = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		if (isTextEmpty(nameText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("ExportToLibraryAction.wizard.page.nameempty"), //$NON-NLS-1$
					null);
		}
		nameStatus = status;

		status = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
		if (isTextEmpty(folderText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("ExportToLibraryAction.wizard.page.folderempty"), //$NON-NLS-1$
					null);
		} else
		// the folder is not empty
		{
			File file = new File(folderText.getText().trim());
			if (!file.exists()) {
				status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
						Messages.getString("ExportToLibraryAction.wizard.page.foldererror"), //$NON-NLS-1$
						null);
			}
		}
		folderStatus = status;

		applyToStatusLine(findMostSevere());
		getWizard().getContainer().updateButtons();
	}

	public boolean isPageComplete() {
		return (nameText.getText().trim().length() > 0) && (folderText.getText().trim().length() > 0)
				&& (nameStatus.getSeverity() == IStatus.OK) && (folderStatus.getSeverity() == IStatus.OK);
	}

	private IStatus findMostSevere() {
		if (nameStatus.matches(IStatus.ERROR))
			return nameStatus;
		if (folderStatus.matches(IStatus.ERROR))
			return folderStatus;
		else
			return nameStatus;
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
			setMessage(message, WizardPage.ERROR);
			break;
		case IStatus.WARNING:
			setErrorMessage(null);
			setMessage(message, WizardPage.WARNING);
			break;
		case IStatus.INFO:
			setErrorMessage(null);
			setMessage(message, WizardPage.INFORMATION);
			break;
		default:
			setErrorMessage(message);
			setMessage(null);
			break;
		}
	}

	public String getFullName() {
		String name = nameText.getText().trim();
		String folder = folderText.getText().trim();
		if (!folder.endsWith(File.separator)) {
			folder = folder + File.separator;
		}

		return folder + name;
	}

}
