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

package org.eclipse.birt.report.designer.internal.ui.editors.wizards;

import java.io.File;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Provides support for generating new report page
 */

public class NewReportPageSupport {

	private Composite topLevel;

	private static final String LABEL_SELECT_A_DIRECTORY = Messages
			.getString("WizardNewReportCreationPage.label.select.directory"); //$NON-NLS-1$

	private static final String LABEL_BROWSE = Messages.getString("WizardNewReportCreationPage.label.browse"); //$NON-NLS-1$

	private static final String LABEL_DIRECTORY = Messages.getString("WizardNewReportCreationPage.label.directory"); //$NON-NLS-1$

	private static final String LABEL_USE_DEFAULT = Messages.getString("WizardNewReportCreationPage.label.useDefault"); //$NON-NLS-1$

	private static final String LABEL_FILE_LOCATION = Messages
			.getString("WizardNewReportCreationPage.label.file.location"); //$NON-NLS-1$

	private static final String LABEL_FILE_NAME = Messages.getString("WizardNewReportCreationPage.label.file.name"); //$NON-NLS-1$

	private Text fileNameField;

	private Text locationPathField;

	private Label locationLabel;

	private Button browseButton;

	boolean useDefaults = true;

	private String initialFileName;

	private String defaultFileLocation;

	private String customLocationFieldValue = "";//$NON-NLS-1$

	public Composite createComposite(Composite parent) {
		topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setFont(parent.getFont());

		createFileNameGroup(topLevel);
		createFileLocationGroup(topLevel);
		return topLevel;
	}

	/**
	 * Creates the project name specification controls.
	 * 
	 * @param parent the parent composite
	 */
	private final void createFileNameGroup(Composite parent) {
		Composite nameGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		nameGroup.setLayout(layout);
		nameGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label fileLabel = new Label(nameGroup, SWT.NONE);
		fileLabel.setText(LABEL_FILE_NAME);
		fileLabel.setFont(parent.getFont());

		fileNameField = new Text(nameGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		getFileNameField().setLayoutData(data);
		getFileNameField().setFont(parent.getFont());

		initInput();
	}

	protected void initInput() {
		if (initialFileName != null) {
			getFileNameField().setText(initialFileName);
		} else {
			getFileNameField().setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * Creates the project location specification controls.
	 * 
	 * @param parent the parent composite
	 */
	private final void createFileLocationGroup(Composite parent) {
		Font font = parent.getFont();

		Group locationGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		locationGroup.setLayout(layout);
		locationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		locationGroup.setFont(font);
		locationGroup.setText(LABEL_FILE_LOCATION);

		final Button useDefaultsButton = new Button(locationGroup, SWT.CHECK | SWT.RIGHT);
		useDefaultsButton.setText(LABEL_USE_DEFAULT);
		useDefaultsButton.setSelection(useDefaults);
		useDefaultsButton.setFont(font);

		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 3;
		useDefaultsButton.setLayoutData(buttonData);

		createUserSpecifiedProjectLocationGroup(locationGroup, !useDefaults);

		SelectionListener listener = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				useDefaults = useDefaultsButton.getSelection();
				browseButton.setEnabled(!useDefaults);
				locationPathField.setEnabled(!useDefaults);
				locationLabel.setEnabled(!useDefaults);

				if (useDefaults) {
					customLocationFieldValue = locationPathField.getText();
					setLocationForSelection();
				} else {
					locationPathField.setText(customLocationFieldValue);
				}
			}
		};
		useDefaultsButton.addSelectionListener(listener);
	}

	/**
	 * Returns the value of the file name field with leading and trailing spaces
	 * removed.
	 * 
	 * @return the file name in the field
	 */
	public String getFileName() {
		if (getFileNameField() == null) {
			return initialFileName;
		}
		return getFileNameField().getText().trim();
	}

	/**
	 * Returns the value of the project location field with leading and trailing
	 * spaces removed.
	 * 
	 * @return the project location directory in the field
	 */
	public IPath getFileLocationFullPath() {
		if (locationPathField == null)
			return new Path(""); //$NON-NLS-1$
		return new Path(locationPathField.getText().trim());
	}

	/**
	 * Creates the project location specification controls.
	 * 
	 * @param group   the parent composite
	 * @param enabled the initial enabled state of the widgets created
	 */
	private void createUserSpecifiedProjectLocationGroup(Composite group, boolean enabled) {
		Font font = group.getFont();

		// location label
		locationLabel = new Label(group, SWT.NONE);
		locationLabel.setText(LABEL_DIRECTORY);
		locationLabel.setEnabled(enabled);
		locationLabel.setFont(font);

		// file location entry field
		locationPathField = new Text(group, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		locationPathField.setLayoutData(data);
		locationPathField.setEnabled(enabled);
		locationPathField.setFont(font);

		// browse button
		browseButton = new Button(group, SWT.PUSH);
		browseButton.setText(LABEL_BROWSE);
		browseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});

		browseButton.setEnabled(enabled);
		browseButton.setFont(font);
		setButtonLayoutData(browseButton);

		if (defaultFileLocation != null) {
			locationPathField.setText(defaultFileLocation);
		} else {
			locationPathField.setText("");//$NON-NLS-1$
		}
	}

	/**
	 * Open an appropriate directory browser
	 */
	private void handleLocationBrowseButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(locationPathField.getShell());
		dialog.setMessage(LABEL_SELECT_A_DIRECTORY);

		String dirName = getFileLocationFullPath().toOSString();
		if (!dirName.equals(""))//$NON-NLS-1$
		{
			File path = new File(dirName);
			if (path.exists()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}

		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			customLocationFieldValue = selectedDirectory;
			locationPathField.setText(customLocationFieldValue);
		}
	}

	/**
	 * Set the location to the default location if we are set to useDefaults.
	 */
	private void setLocationForSelection() {
		if (useDefaults) {
			locationPathField.setText(defaultFileLocation);
		}
	}

	/**
	 * Sets the initial file name that this page will use when created. The name is
	 * ignored if the createControl(Composite) method has already been called.
	 * Leading and trailing spaces in the name are ignored.
	 * 
	 * @param name initial file name for this page
	 */
	public void setInitialFileName(String name) {
		if (name == null) {
			initialFileName = null;
		} else {
			initialFileName = name.trim();
		}
	}

	public String getInitialFileName() {
		return initialFileName;
	}

	protected GridData setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		// TODO replace DialogPage's implementation
		// int widthHint =
		// convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = minSize.x;// Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
		return data;
	}

	public void setInitialFileLocation(String path) {
		defaultFileLocation = path;
	}

	public Text getFileNameField() {
		return fileNameField;
	}

	public Text getLocationPathField() {
		return locationPathField;
	}
}
