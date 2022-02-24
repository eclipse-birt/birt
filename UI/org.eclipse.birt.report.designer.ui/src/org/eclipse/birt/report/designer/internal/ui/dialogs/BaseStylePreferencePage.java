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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Provides default implementation for all Style preference pages
 */

public abstract class BaseStylePreferencePage extends FieldEditorPreferencePage {

	protected Logger logger = Logger.getLogger(BaseStylePreferencePage.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors ()
	 */
	protected void createFieldEditors() {
		noDefaultButton();
	}

	private Label descriptionLabel;

	private Control body;

	private boolean createDefaultButton = true;

	private Button defaultsButton;

	protected void noDefaultButton() {
		createDefaultButton = false;
	}

	public void createControl(Composite parent) {

		GridData gd;
		Composite content = new Composite(parent, SWT.NONE);
		setControl(content);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		content.setLayout(layout);
		// Apply the font on creation for backward compatibility
		applyDialogFont(content);

		// initialize the dialog units
		initializeDialogUnits(content);

		descriptionLabel = createDescriptionLabel(content);
		if (descriptionLabel != null) {
			descriptionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		body = createContents(content);
		if (body != null) {
			// null is not a valid return value but support graceful failure
			body.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		Composite buttonBar = new Composite(content, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		buttonBar.setLayout(layout);

		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);

		buttonBar.setLayoutData(gd);

		contributeButtons(buttonBar);

		if (createDefaultButton) {
			layout.numColumns = layout.numColumns + 1;
			String[] labels = JFaceResources.getStrings(new String[] { "defaults", "apply" }); //$NON-NLS-2$//$NON-NLS-1$
			int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
			defaultsButton = new Button(buttonBar, SWT.PUSH);
			defaultsButton.setText(labels[0]);
			Dialog.applyDialogFont(defaultsButton);
			GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			Point minButtonSize = defaultsButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			data.widthHint = Math.max(widthHint, minButtonSize.x);
			defaultsButton.setLayoutData(data);
			defaultsButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					performDefaults();
				}
			});

			applyDialogFont(buttonBar);
		} else {
			/*
			 * Check if there are any other buttons on the button bar. If not, throw away
			 * the button bar composite. Otherwise there is an unusually large button bar.
			 */
			if (buttonBar.getChildren().length < 1) {
				buttonBar.dispose();
			}
		}
	}

	protected Point doComputeSize() {
		if (descriptionLabel != null && body != null) {
			Point bodySize = body.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			GridData gd = (GridData) descriptionLabel.getLayoutData();
			gd.widthHint = bodySize.x;
		}
		return getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	}

	/**
	 * The constructor.
	 * 
	 * @param style
	 */
	protected BaseStylePreferencePage(Object model) {
		super(FieldEditorPreferencePage.GRID);
		setTitle(Messages.getString("BaseStylePreferencePage.displayname.Title")); //$NON-NLS-1$

		// Set the preference store for the preference page.
		IPreferenceStore store = new StylePreferenceStore(model);
		setPreferenceStore(store);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (getBuilder() != null) {
			getBuilder().refreshPagesStatus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	public boolean performOk() {
		IPreferenceStore ps = getPreferenceStore();

		if (ps instanceof StylePreferenceStore) {
			((StylePreferenceStore) ps).clearError();
		}

		boolean rt = super.performOk();

		if (ps instanceof StylePreferenceStore) {
			return !((StylePreferenceStore) ps).hasError();
		}

		return rt;
	}

	protected Button getDefaultsButton() {
		return defaultsButton;
	}

	private StyleBuilder builder;

	public StyleBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(StyleBuilder builder) {
		this.builder = builder;
	}

	public void setErrorMessage(String newMessage) {
		if (builder != null)
			builder.setErrorMessage(newMessage);
		else {
			super.setErrorMessage(newMessage);
			if (getContainer() != null) {
				getContainer().updateMessage();
			}
		}
	}

	protected abstract String[] getPreferenceNames();

	private boolean firstCheck = false;

	protected boolean hasLocaleProperty = false;

	public boolean hasLocaleProperties() {
		if (!firstCheck) {
			firstCheck = true;
			String[] fields = getPreferenceNames();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					if (getPreferenceStore() instanceof StylePreferenceStore) {
						StylePreferenceStore store = (StylePreferenceStore) getPreferenceStore();
						if (store.hasLocalValue(fields[i])) {
							hasLocaleProperty = true;
							return true;
						}
					}
				}
			}
		} else {
			if (fields != null) {
				for (int i = 0; i < fields.size(); i++) {
					FieldEditor editor = (FieldEditor) fields.get(i);
					if (editor instanceof AbstractFieldEditor) {
						if (((AbstractFieldEditor) editor).hasLocaleValue())
							return true;
					}
				}
				return false;
			}
		}
		return hasLocaleProperty;
	}

	private List fields;

	protected void addField(FieldEditor editor) {
		if (fields == null) {
			fields = new ArrayList();
		}
		fields.add(editor);

		super.addField(editor);
	}

}
