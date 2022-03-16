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
package org.eclipse.birt.report.designer.data.ui.datasource;

import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ExportDataSourceDialog extends StatusDialog {
	private DataSourceHandle dataSourceHandle;
	private boolean doesCreateStore = false, isExternalToCP = true;

	private Text nameText;
	private String fileName;

	protected ExportDataSourceDialog(Shell parentShell, String title) {
		super(parentShell);
		setHelpAvailable(false);
		setTitle(title);
	}

	public ExportDataSourceDialog(Shell parentShell, String title, DataSourceHandle selection) {
		this(parentShell, title);
		this.dataSourceHandle = selection;
		initProfileName();
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		validate();
		return control;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.None);

		GridLayout parentLayout = new GridLayout();
		parentLayout.marginLeft = parentLayout.marginTop = parentLayout.marginRight = 10;
		parentLayout.marginBottom = 5;
		composite.setLayout(parentLayout);
		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(data);

		Label message = new Label(composite, SWT.BOLD);
		message.setText(Messages.getFormattedString("datasource.exportToCP.message",
				new Object[] { this.dataSourceHandle.getQualifiedName() }));

		createSeparator(composite, 1);

		Composite content = new Composite(composite, SWT.None);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.numColumns = 2;
		content.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		content.setLayoutData(layoutData);

		createCheckboxArea(content);

		createSeparator(content, 2);

		createNameText(content);

		return parent;
	}

	private void createSeparator(Composite composite, int span) {
		Label separator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalIndent = 5;
		gd.horizontalSpan = span;
		separator.setLayoutData(gd);
	}

	private void createNameText(Composite content) {
		final Label nameLabel = new Label(content, SWT.CHECK);
		nameLabel.setText(Messages.getString("datasource.exportToCP.label.specifyName"));
		GridData labelData = new GridData();
		labelData.horizontalSpan = 1;
		labelData.verticalIndent = 5;
		nameLabel.setLayoutData(labelData);

		nameText = new Text(content, SWT.BORDER);
		GridData textGd = new GridData(GridData.FILL_HORIZONTAL);
		textGd.verticalIndent = 5;
		textGd.horizontalIndent = 10;
		nameText.setLayoutData(textGd);
		nameText.setText(this.fileName == null ? "" : this.fileName);
		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				fileName = nameText.getText().trim();
				validate();
			}
		});
	}

	private void createCheckboxArea(Composite content) {
		GridData btnData1 = new GridData();
		btnData1.horizontalSpan = 2;

		Button externalButton = new Button(content, SWT.CHECK);
		externalButton.setText(Messages.getString("datasource.exportToCP.externalCheckBox"));
		externalButton.setLayoutData(btnData1);
		externalButton.setSelection(true);
		externalButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				isExternalToCP = !isExternalToCP;
			}
		});

		GridData btnData2 = new GridData();
		btnData2.horizontalSpan = 2;

		Button button = new Button(content, SWT.CHECK);
		button.setText(Messages.getString("datasource.exportToCP.checkBox"));
		button.setLayoutData(btnData2);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				doesCreateStore = !doesCreateStore;
			}
		});
	}

	public boolean isExternalToCP() {
		return this.isExternalToCP;
	}

	public boolean doesCreateProfileStore() {
		return this.doesCreateStore;
	}

	public String getProfileName() {
		return fileName;
	}

	private void initProfileName() {
		this.fileName = this.dataSourceHandle.getQualifiedName();

		int count = 1;
		if (isDuplicatedName()) {
			fileName += "_" + count;
			while (isDuplicatedName()) {
				count++;
				fileName = fileName.substring(0, fileName.length() - 1) + count;
			}
		}
	}

	private void validate() {
		Status status;
		if (fileName == null || fileName.trim().length() == 0) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("datasource.exportToCP.error.emptyFileName"));
		} else if (containInvalidCharactor(fileName)) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("datasource.exportToCP.error.invalidFileName"));
		} else if (isDuplicatedName()) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("datasource.exportToCP.error.duplicatedFileName"));
		} else {
			status = getOKStatus();
		}
		updateStatus(status);
	}

	private boolean isDuplicatedName() {
		if ((!fileName.equals(this.dataSourceHandle.getName()) && Utility.checkDataSourceName(fileName))
				|| OdaProfileExplorer.isProfileNameUsed(fileName)) {
			return true;
		}

		return false;
	}

	/**
	 * whether name contains ".", "/", "\", "!", ";", "," characters
	 *
	 * @param name
	 * @return
	 */
	private boolean containInvalidCharactor(String name) {
		if (name == null) {
			return false;
		} else if (name.indexOf(".") > -1 || //$NON-NLS-1$
				name.indexOf("\\") > -1 || name.indexOf("/") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf("!") > -1 || name.indexOf(";") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf(",") > -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	protected Status getOKStatus() {
		return getMiscStatus(IStatus.OK, ""); //$NON-NLS-1$
	}

	/**
	 *
	 * @param severity
	 * @param message
	 * @return
	 */
	protected Status getMiscStatus(int severity, String message) {
		return new Status(severity, PlatformUI.PLUGIN_ID, severity, message, null);
	}

}
