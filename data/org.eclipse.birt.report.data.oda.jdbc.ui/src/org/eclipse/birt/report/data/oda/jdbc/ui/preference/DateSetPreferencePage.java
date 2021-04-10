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

package org.eclipse.birt.report.data.oda.jdbc.ui.preference;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. This page is used to modify dataset preview preferences
 * only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */
public class DateSetPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private IntegerFieldEditor maxDisplaySchemaEditor;
	private IntegerFieldEditor maxDisplayTableEditor;
	private IntegerFieldEditor timeOutLimitEditor;
	private Button schemasPrefetchConfigCheckbox;
	private Button enableCodeAssistCheckbox;

	/** default value of max schema number */
	public static final int DEFAULT_MAX_NUM_OF_SCHEMA = 20;

	/** default value of max table number each schema */
	public static final int DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA = 100;

	/** default value of timeout limit */
	public static final int DEFAULT_TIMEOUT_LIMIT = 60;

	private static final int MAX_MAX_ROW = Integer.MAX_VALUE;

	public static final String ENABLED = "YES";
	public static final String DISABLED = "NO";

	/** max Row preference name */
	public static final String USER_MAXROW = "user_maxrow"; //$NON-NLS-1$
	public static final String SCHEMAS_PREFETCH_CONFIG = "shemas_prefetch_config";
	public static final String USER_MAX_NUM_OF_SCHEMA = "user_max_num_of_schema";
	public static final String USER_MAX_NUM_OF_TABLE_EACH_SCHEMA = "user_max_num_of_table_each_schema";
	public static final String USER_TIMEOUT_LIMIT = "user_timeout_limit";
	public static final String ENABLE_CODE_ASSIST = "enable_code_assit";

	/*
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		data.horizontalSpan = 2;
		data.verticalSpan = 6;
		// mainComposite.setLayoutData( data );
		GridLayout layout = new GridLayout();
		mainComposite.setLayout(layout);

		final Group sqlDataSetGroup = new Group(mainComposite, SWT.NONE);

		sqlDataSetGroup.setLayout(layout);
		sqlDataSetGroup.setText(
				JdbcPlugin.getResourceString("designer.preview.preference.resultset.sqldatasetpage.group.title"));
		sqlDataSetGroup.setLayoutData(data);

		sqlDataSetGroup.setEnabled(true);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;

		Label invisibleRow = new Label(sqlDataSetGroup, SWT.NONE);
		invisibleRow.setLayoutData(data);

		schemasPrefetchConfigCheckbox = new Button(sqlDataSetGroup, SWT.CHECK);
		schemasPrefetchConfigCheckbox.setLayoutData(data);
		schemasPrefetchConfigCheckbox.setText(JdbcPlugin
				.getResourceString("designer.preview.preference.resultset.schemasPrefetchCheckbox.description"));

		enableCodeAssistCheckbox = new Button(sqlDataSetGroup, SWT.CHECK);
		enableCodeAssistCheckbox.setLayoutData(data);
		enableCodeAssistCheckbox.setText(
				JdbcPlugin.getResourceString("designer.preview.preference.resultset.enableCodeAssist.description"));

		initializeSchemaPrefetchConfig();
		initializeEnableCodeAssistConfig();

		// Set up the maximum number of schemas to be fetched in SQLDataSetPage.
		maxDisplaySchemaEditor = new IntegerFieldEditor(USER_MAX_NUM_OF_SCHEMA, "", sqlDataSetGroup);

		Label lab = maxDisplaySchemaEditor.getLabelControl(sqlDataSetGroup);
		lab.setText(JdbcPlugin.getResourceString("designer.preview.preference.resultset.maxNoOfSchema.description"));

		maxDisplaySchemaEditor.setPage(this);
		maxDisplaySchemaEditor.setTextLimit(Integer.toString(MAX_MAX_ROW).length());

		maxDisplaySchemaEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		maxDisplaySchemaEditor.setValidRange(0, MAX_MAX_ROW);

		maxDisplaySchemaEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(maxDisplaySchemaEditor.isValid());
			}
		});

		maxDisplaySchemaEditor.setErrorMessage(
				JdbcPlugin.getFormattedString("designer.preview.preference.resultset.maxNoOfSchema.errormessage",
						new Object[] { new Integer(MAX_MAX_ROW) }));

		String defaultMaxSchema = JdbcPlugin.getDefault().getPluginPreferences().getString(USER_MAX_NUM_OF_SCHEMA);
		if (defaultMaxSchema == null || defaultMaxSchema.trim().length() <= 0) {
			defaultMaxSchema = String.valueOf(DEFAULT_MAX_NUM_OF_SCHEMA);
		}
		maxDisplaySchemaEditor.setStringValue(defaultMaxSchema);

		// Set up the maximum number of tables in each schema
		maxDisplayTableEditor = new IntegerFieldEditor(USER_MAX_NUM_OF_TABLE_EACH_SCHEMA, "", sqlDataSetGroup);

		lab = maxDisplayTableEditor.getLabelControl(sqlDataSetGroup);
		lab.setText(JdbcPlugin.getResourceString("designer.preview.preference.resultset.maxNoOfTable.description"));

		maxDisplayTableEditor.setPage(this);
		maxDisplayTableEditor.setTextLimit(Integer.toString(MAX_MAX_ROW).length());

		maxDisplayTableEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		maxDisplayTableEditor.setValidRange(0, MAX_MAX_ROW);

		maxDisplayTableEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(maxDisplayTableEditor.isValid());
			}
		});

		maxDisplayTableEditor.setErrorMessage(
				JdbcPlugin.getFormattedString("designer.preview.preference.resultset.maxNoOfTable.errormessage",
						new Object[] { new Integer(MAX_MAX_ROW) }));

		String defaultMaxTable = JdbcPlugin.getDefault().getPluginPreferences()
				.getString(USER_MAX_NUM_OF_TABLE_EACH_SCHEMA);
		if (defaultMaxTable == null || defaultMaxTable.trim().length() <= 0) {
			defaultMaxTable = String.valueOf(DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA);
		}
		maxDisplayTableEditor.setStringValue(defaultMaxTable);

		// Set up the timeout
		timeOutLimitEditor = new IntegerFieldEditor(USER_MAX_NUM_OF_SCHEMA, "", sqlDataSetGroup);

		lab = timeOutLimitEditor.getLabelControl(sqlDataSetGroup);
		lab.setText(JdbcPlugin.getResourceString("designer.preview.preference.resultset.timeOutLimit.description"));

		timeOutLimitEditor.setPage(this);
		timeOutLimitEditor.setTextLimit(Integer.toString(MAX_MAX_ROW).length());

		timeOutLimitEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		timeOutLimitEditor.setValidRange(0, MAX_MAX_ROW);

		timeOutLimitEditor.setPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(timeOutLimitEditor.isValid());
			}
		});

		timeOutLimitEditor.setErrorMessage(
				JdbcPlugin.getFormattedString("designer.preview.preference.resultset.timeOutLimit.errormessage",
						new Object[] { new Integer(MAX_MAX_ROW) }));

		String defaultTimeOutLimit = JdbcPlugin.getDefault().getPluginPreferences().getString(USER_TIMEOUT_LIMIT);
		if (defaultTimeOutLimit == null || defaultTimeOutLimit.trim().length() <= 0) {
			defaultTimeOutLimit = String.valueOf(DEFAULT_TIMEOUT_LIMIT);
		}
		timeOutLimitEditor.setStringValue(defaultTimeOutLimit);

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_PREFERENCE_DATASET_JDBC);

		return mainComposite;
	}

	/**
	 * 
	 *
	 */
	private void initializeSchemaPrefetchConfig() {
		if (JdbcPlugin.getDefault().getPluginPreferences().contains(SCHEMAS_PREFETCH_CONFIG)) {
			String selection = JdbcPlugin.getDefault().getPluginPreferences().getString(SCHEMAS_PREFETCH_CONFIG);

			schemasPrefetchConfigCheckbox.setSelection(selection.equals(ENABLED) ? true : false);
		} else {
			JdbcPlugin.getDefault().getPluginPreferences().setValue(SCHEMAS_PREFETCH_CONFIG, ENABLED);
			schemasPrefetchConfigCheckbox.setSelection(true);
		}
	}

	/**
	 * 
	 *
	 */
	private void initializeEnableCodeAssistConfig() {
		if (JdbcPlugin.getDefault().getPluginPreferences().contains(ENABLE_CODE_ASSIST)) {
			String selection = JdbcPlugin.getDefault().getPluginPreferences().getString(ENABLE_CODE_ASSIST);

			enableCodeAssistCheckbox.setSelection(selection.equals(ENABLED) ? true : false);
		} else {
			JdbcPlugin.getDefault().getPluginPreferences().setValue(ENABLE_CODE_ASSIST, ENABLED);
			enableCodeAssistCheckbox.setSelection(true);
		}
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
		maxDisplaySchemaEditor.setStringValue(String.valueOf(DEFAULT_MAX_NUM_OF_SCHEMA));
		maxDisplayTableEditor.setStringValue(String.valueOf(DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA));
		enableCodeAssistCheckbox.setSelection(true);
		schemasPrefetchConfigCheckbox.setSelection(true);
		timeOutLimitEditor.setStringValue(String.valueOf(DEFAULT_TIMEOUT_LIMIT));
		super.performDefaults();
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		JdbcPlugin.getDefault().getPluginPreferences().setValue(SCHEMAS_PREFETCH_CONFIG,
				schemasPrefetchConfigCheckbox.getSelection() ? ENABLED : DISABLED);
		JdbcPlugin.getDefault().getPluginPreferences().setValue(ENABLE_CODE_ASSIST,
				enableCodeAssistCheckbox.getSelection() ? ENABLED : DISABLED);

		JdbcPlugin.getDefault().getPluginPreferences().setValue(USER_MAX_NUM_OF_SCHEMA,
				maxDisplaySchemaEditor.getStringValue());
		JdbcPlugin.getDefault().getPluginPreferences().setValue(USER_MAX_NUM_OF_TABLE_EACH_SCHEMA,
				maxDisplayTableEditor.getStringValue());
		JdbcPlugin.getDefault().getPluginPreferences().setValue(USER_TIMEOUT_LIMIT,
				timeOutLimitEditor.getStringValue());
		JdbcPlugin.getDefault().savePluginPreferences();

		return true;
	}

}