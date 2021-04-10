/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. This page is used to modify dataset preview preferences
 * only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */
public class DateSetPreferencePage extends PropertyAndPreferencePage {

	private IntegerFieldEditor maxRowEditor;
	private Button promptButton;
	private Button paramUpdatePromptButton;
	private Button updateButton;
	private Button ignoreButton;
	private Label alwaysLabel;

	/** default value of max number */
	public static final int DEFAULT_MAX_ROW = 500;
	public static final boolean DEFAULT_PROMPT_SELECTION = false;
	public static final boolean DEFAULT_REPORTPARAM_UPDATE_PROMP_SELECTION = false;

	private static final int MAX_MAX_ROW = Integer.MAX_VALUE;

	/** max Row preference name */
	public static final String USER_MAXROW = "user_maxrow"; //$NON-NLS-1$
	public static final String USER_MAX_NUM_OF_SCHEMA = "user_max_num_of_schema"; //$NON-NLS-1$
	public static final String USER_MAX_NUM_OF_TABLE_EACH_SCHEMA = "user_max_num_of_table_each_schema"; //$NON-NLS-1$

	public static final String PROMPT_ENABLE = "prompt_enable";//$NON-NLS-1$
	public static final String PROMPT_PARAM_UPDATE = "reportParam_update_enable";//$NON-NLS-1$
	public static final String PROMPT_PARAM_UPDATE_OPTION = "reportParam_update_enable_option";//$NON-NLS-1$
	public static final String PROMPT_PARAM_UPDATE_OPTION_UPDATE = "update";//$NON-NLS-1$
	public static final String PROMPT_PARAM_UPDATE_OPTION_IGNORE = "ignore";//$NON-NLS-1$

	private OptionsConfigurationBlock block = null;

	/*
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PREFERENCE_BIRT_DATA_SET_EDITOR_ID);
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		data.horizontalSpan = 2;
		data.verticalSpan = 5;
		// mainComposite.setLayoutData( data );
		GridLayout layout = new GridLayout();
		mainComposite.setLayout(layout);

		Group previewPageGroup = new Group(mainComposite, SWT.NONE);

		previewPageGroup.setLayout(new GridLayout());

		previewPageGroup.setText(Messages.getString("designer.preview.preference.resultset.previewpage.group.title")); //$NON-NLS-1$
		previewPageGroup.setLayoutData(data);

		previewPageGroup.setEnabled(true);

		// Set up the maximum number of rows to be previewed in
		// ResultSetPreviewPage.

		maxRowEditor = new IntegerFieldEditor(USER_MAXROW, "", //$NON-NLS-1$
				previewPageGroup);

		Label lab2 = maxRowEditor.getLabelControl(previewPageGroup);
		lab2.setText(Messages.getString("designer.preview.preference.resultset.maxrow.description")); //$NON-NLS-1$

		maxRowEditor.setPage(this);
		maxRowEditor.setTextLimit(Integer.toString(MAX_MAX_ROW).length());
		maxRowEditor.setErrorMessage(
				Messages.getFormattedString("designer.preview.preference.resultset.maxrow.errormessage", //$NON-NLS-1$
						new Object[] { Integer.valueOf(MAX_MAX_ROW) }));
		maxRowEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		maxRowEditor.setValidRange(1, MAX_MAX_ROW);
		maxRowEditor.setPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(maxRowEditor.isValid());
			}
		});

		String defaultMaxRow = ReportPlugin.getDefault().getPluginPreferences().getString(USER_MAXROW);
		if (defaultMaxRow == null || defaultMaxRow.trim().length() <= 0) {
			defaultMaxRow = String.valueOf(DEFAULT_MAX_ROW);
		}
		maxRowEditor.setStringValue(defaultMaxRow);

		Group promptPageGroup = new Group(mainComposite, SWT.NONE);
		promptPageGroup.setLayout(new GridLayout());
		promptPageGroup.setText(Messages.getString("designer.preview.preference.updateParameter.confirmMessage"));
		promptButton = new Button(promptPageGroup, SWT.CHECK);

		boolean enable = ReportPlugin.getDefault().getPluginPreferences().getBoolean(PROMPT_ENABLE);
		promptButton.setSelection(enable);
		promptButton.setText(Messages.getString("designer.preview.preference.updateParameter.confirmButton"));

		paramUpdatePromptButton = new Button(promptPageGroup, SWT.CHECK);
		boolean enableParamUpdate = ReportPlugin.getDefault().getPluginPreferences().getBoolean(PROMPT_PARAM_UPDATE);
		paramUpdatePromptButton.setSelection(enableParamUpdate);
		paramUpdatePromptButton
				.setText(Messages.getString("designer.preview.preference.updateReportParameter.confirmButton"));

		paramUpdatePromptButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (paramUpdatePromptButton.getSelection()) {
					updateButton.setEnabled(true);
					ignoreButton.setEnabled(true);
					alwaysLabel.setEnabled(true);
				} else {
					updateButton.setEnabled(false);
					ignoreButton.setEnabled(false);
					alwaysLabel.setEnabled(false);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Composite com = new Composite(promptPageGroup, SWT.NONE);
		GridLayout gd = new GridLayout();
		gd.numColumns = 3;
		com.setLayout(gd);
		alwaysLabel = new Label(com, SWT.NONE);
		alwaysLabel
				.setText(Messages.getString("designer.preview.preference.updateReportParameter.confirmOption.always"));
		updateButton = new Button(com, SWT.RADIO);
		updateButton
				.setText(Messages.getString("designer.preview.preference.updateReportParameter.confirmOption.update"));
		ignoreButton = new Button(com, SWT.RADIO);
		ignoreButton
				.setText(Messages.getString("designer.preview.preference.updateReportParameter.confirmOption.ignore"));

		if (enableParamUpdate) {
			alwaysLabel.setEnabled(true);
			updateButton.setEnabled(true);
			ignoreButton.setEnabled(true);
		} else {
			updateButton.setEnabled(false);
			ignoreButton.setEnabled(false);
			alwaysLabel.setEnabled(false);
		}

		String option = ReportPlugin.getDefault().getPluginPreferences().getString(PROMPT_PARAM_UPDATE_OPTION);

		if (PROMPT_PARAM_UPDATE_OPTION_UPDATE.equals(option) || option.equals("")) {
			updateButton.setSelection(true);
			ignoreButton.setSelection(false);
		} else if (PROMPT_PARAM_UPDATE_OPTION_IGNORE.equals(option)) {
			ignoreButton.setSelection(true);
			updateButton.setSelection(false);
		}

		if (getConfigBlock() != null) {
			getConfigBlock().createContents(mainComposite);
		}

		return mainComposite;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		maxRowEditor.setStringValue(String.valueOf(DEFAULT_MAX_ROW));
		promptButton.setSelection(false);
		paramUpdatePromptButton.setSelection(false);
		alwaysLabel.setEnabled(false);
		updateButton.setEnabled(false);
		ignoreButton.setEnabled(false);
		updateButton.setSelection(true);
		ignoreButton.setSelection(false);

		if (getConfigBlock() != null) {
			getConfigBlock().performDefaults();
		}

		super.performDefaults();
	}

	protected void performApply() {
		super.performApply();
		if (getConfigBlock() != null) {
			getConfigBlock().performApply();
		}
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		ReportPlugin.getDefault().getPluginPreferences().setValue(USER_MAXROW, maxRowEditor.getStringValue());
		ReportPlugin.getDefault().getPluginPreferences().setValue(PROMPT_ENABLE, promptButton.getSelection());
		ReportPlugin.getDefault().getPluginPreferences().setValue(PROMPT_PARAM_UPDATE,
				paramUpdatePromptButton.getSelection());
		if (updateButton.getSelection())
			ReportPlugin.getDefault().getPluginPreferences().setValue(PROMPT_PARAM_UPDATE_OPTION,
					PROMPT_PARAM_UPDATE_OPTION_UPDATE);
		else if (ignoreButton.getSelection())
			ReportPlugin.getDefault().getPluginPreferences().setValue(PROMPT_PARAM_UPDATE_OPTION,
					PROMPT_PARAM_UPDATE_OPTION_IGNORE);

		ReportPlugin.getDefault().savePluginPreferences();

		if (getConfigBlock() != null) {
			getConfigBlock().performOk();
		}

		return true;
	}

	private OptionsConfigurationBlock getConfigBlock() {
		if (block == null) {
			if (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter() != null) {
				block = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter()
						.getDataModelConfigurationBlock(getNewStatusChangedListener(), null);
			}
		}

		return block;
	}

	protected Control createPreferenceContent(Composite composite) {
		return null;
	}

	protected boolean hasProjectSpecificOptions(IProject project) {
		return false;
	}

	protected String getPreferencePageID() {
		return null;
	}

	protected String getPropertyPageID() {
		return null;
	}

}