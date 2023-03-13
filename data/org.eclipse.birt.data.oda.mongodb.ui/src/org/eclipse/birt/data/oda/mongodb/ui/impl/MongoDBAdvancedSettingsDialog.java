/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver.ReadPreferenceChoice;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;
import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb.ui.util.IHelpConstants;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class MongoDBAdvancedSettingsDialog extends StatusDialog {

	private static String DIALOG_TITLE = Messages.getString("MongoDBAdvancedSettingsDialog.dialogTitle"); //$NON-NLS-1$

	private String batchSizeValue, docSearchLimitValue, maxSkipDocValue, indexExpr, queryPreferenceMode, tagSetValue;

	private Combo queryPreferenceModeCombo;
	private Text batchSizeText, docSearchLimitText, maxSkipDocText, tagSetText;
	private Button allowPartialResultsCheckbox, flattenNestedDocCheckbox;
	private boolean noTimeOut, allowsPartialResults, flattenNestedDocument;

	public MongoDBAdvancedSettingsDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();

		Point pt = getShell().computeSize(-1, -1);
		pt.x = Math.max(pt.x, 250);
		pt.y = Math.max(pt.y, 300);
		getShell().setSize(pt);
		getShell().setText(DIALOG_TITLE);

		validatePageStatus();

	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		layout.numColumns = 2;
		composite.setLayout(layout);

		createMainArea(composite);

		initDialogControls();

		UIHelper.setSystemHelp(composite, IHelpConstants.CONTEXT_ID_DIALOG_MONGODB_DATASET_ADVANCED_SETTING);

		return composite;

	}

	private void createMainArea(Composite parent) {
		Label maxResObjLabel = new Label(parent, SWT.NONE);
		maxResObjLabel.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Label.MaxResultObjects")); //$NON-NLS-1$
		maxResObjLabel.setToolTipText(Messages.getString("MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects")); //$NON-NLS-1$

		batchSizeText = new Text(parent, SWT.BORDER);
		batchSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		batchSizeText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				batchSizeValue = batchSizeText.getText().trim();
				validatePageStatus();

			}

		});

		Label maxSearchDocLabel = new Label(parent, SWT.NONE);
		maxSearchDocLabel.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch")); //$NON-NLS-1$
		maxSearchDocLabel
				.setToolTipText(Messages.getString("MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch")); //$NON-NLS-1$

		docSearchLimitText = new Text(parent, SWT.BORDER);
		docSearchLimitText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		docSearchLimitText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				docSearchLimitValue = docSearchLimitText.getText().trim();
				validatePageStatus();

			}

		});

		Label maxSkipDocLabel = new Label(parent, SWT.NONE);
		maxSkipDocLabel.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip")); //$NON-NLS-1$
		maxSkipDocLabel.setToolTipText(Messages.getString("MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip")); //$NON-NLS-1$

		maxSkipDocText = new Text(parent, SWT.BORDER);
		maxSkipDocText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxSkipDocText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				maxSkipDocValue = maxSkipDocText.getText().trim();
				validatePageStatus();

			}

		});

		createQueryPreferenceGroup(parent);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.verticalIndent = 8;

		allowPartialResultsCheckbox = new Button(parent, SWT.CHECK | SWT.WRAP);
		allowPartialResultsCheckbox
				.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults")); //$NON-NLS-1$
		allowPartialResultsCheckbox
				.setToolTipText(Messages.getString("MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		allowPartialResultsCheckbox.setLayoutData(gd);
		allowPartialResultsCheckbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				allowsPartialResults = allowPartialResultsCheckbox.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		flattenNestedDocCheckbox = new Button(parent, SWT.CHECK | SWT.WRAP);
		flattenNestedDocCheckbox
				.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments")); //$NON-NLS-1$
		flattenNestedDocCheckbox.setToolTipText(
				Messages.getString("MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		flattenNestedDocCheckbox.setLayoutData(gd);
		flattenNestedDocCheckbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				flattenNestedDocument = flattenNestedDocCheckbox.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void createQueryPreferenceGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference")); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, false));
		GridData groupGd = new GridData(GridData.FILL_HORIZONTAL);
		groupGd.horizontalSpan = 2;
		group.setLayoutData(groupGd);

		Label modeLabel = new Label(group, SWT.NONE);
		modeLabel.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode")); //$NON-NLS-1$
		modeLabel
				.setToolTipText(Messages.getString("MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode")); //$NON-NLS-1$

		queryPreferenceModeCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		queryPreferenceModeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		queryPreferenceModeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				queryPreferenceMode = queryPreferenceModeCombo.getText().trim();
				updateTagSetTextStatus();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		Label tagSetLabel = new Label(group, SWT.NONE);
		tagSetLabel.setText(Messages.getString("MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet")); //$NON-NLS-1$
		tagSetLabel.setToolTipText(
				Messages.getString("MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet")); //$NON-NLS-1$

		tagSetText = new Text(group, SWT.BORDER);
		tagSetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tagSetText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				tagSetValue = tagSetText.getText().trim();
			}

		});

		initQueryCursorPreferenceSelection();
	}

	private void initQueryCursorPreferenceSelection() {
		queryPreferenceModeCombo.add(ReadPreferenceChoice.PRIMARY.displayName());
		queryPreferenceModeCombo.add(ReadPreferenceChoice.PRIMARY_PREFERRED.displayName());
		queryPreferenceModeCombo.add(ReadPreferenceChoice.SECONDARY.displayName());
		queryPreferenceModeCombo.add(ReadPreferenceChoice.SECONDARY_PREFERRED.displayName());
		queryPreferenceModeCombo.add(ReadPreferenceChoice.NEAREST.displayName());

		queryPreferenceModeCombo.select(0);
	}

	private void updateTagSetTextStatus() {
		tagSetText.setEnabled(
				!ReadPreferenceChoice.PRIMARY.displayName().equals(queryPreferenceModeCombo.getText().trim()));
	}

	private void validatePageStatus() {
		Status status = null;

		if (!UIHelper.isEmptyString(batchSizeValue) && !UIHelper.isNumber(batchSizeValue)) {
			status = getMiscStatus(IStatus.ERROR,
					Messages.getString("MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects")); //$NON-NLS-1$
		} else if (!UIHelper.isEmptyString(docSearchLimitValue) && !UIHelper.isNumber(docSearchLimitValue)) {
			status = getMiscStatus(IStatus.ERROR,
					Messages.getString("MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch")); //$NON-NLS-1$
		} else if (!UIHelper.isEmptyString(maxSkipDocValue) && !UIHelper.isNumberOrZero(maxSkipDocValue)) {
			status = getMiscStatus(IStatus.ERROR,
					Messages.getString("MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip")); //$NON-NLS-1$
		} else {
			status = getOKStatus();
		}

		if (status != null) {
			updateStatus(status);
		}
	}

	private Status getMiscStatus(int severity, String message) {
		return new Status(severity, PlatformUI.PLUGIN_ID, severity, message, null);
	}

	private Status getOKStatus() {
		return getMiscStatus(IStatus.OK, ""); //$NON-NLS-1$
	}

	private void initDialogControls() {
		if (batchSizeValue != null) {
			batchSizeText.setText(batchSizeValue);
		}

		if (docSearchLimitValue != null) {
			docSearchLimitText.setText(docSearchLimitValue);
		}

		if (maxSkipDocValue != null) {
			maxSkipDocText.setText(maxSkipDocValue);
		}

		if (queryPreferenceMode != null) {
			queryPreferenceModeCombo.setText(queryPreferenceMode);
		}

		if (tagSetValue != null) {
			tagSetText.setText(tagSetValue);
		}
		updateTagSetTextStatus();

		if (docSearchLimitValue != null) {
			docSearchLimitText.setText(docSearchLimitValue);
		}

		allowPartialResultsCheckbox.setSelection(allowsPartialResults);
		flattenNestedDocCheckbox.setSelection(flattenNestedDocument);

	}

	protected void initQueryProps(QueryProperties queryProps) {
		this.docSearchLimitValue = ""; //$NON-NLS-1$
		this.batchSizeValue = ""; //$NON-NLS-1$
		this.maxSkipDocValue = ""; //$NON-NLS-1$

		if (queryProps.hasRuntimeMetaDataSearchLimit()) {
			this.docSearchLimitValue = String.valueOf(queryProps.getRuntimeMetaDataSearchLimit());
		}
		if (queryProps.hasBatchSize()) {
			this.batchSizeValue = String.valueOf(queryProps.getBatchSize());
		}
		if (queryProps.hasNumDocsToSkip()) {
			this.maxSkipDocValue = String.valueOf(queryProps.getNumDocsToSkip());
		}
		if (queryProps.getQueryReadPreference() != null) {
			this.queryPreferenceMode = String.valueOf(queryProps.getQueryReadPreference());
		} else {
			this.queryPreferenceMode = ReadPreferenceChoice.DEFAULT.displayName();
		}

		tagSetValue = queryProps.getQueryReadPreferenceTags();
		if (tagSetValue == null) {
			tagSetValue = ""; //$NON-NLS-1$
		}

		this.indexExpr = String.valueOf(queryProps.getIndexHints());
		this.noTimeOut = queryProps.hasNoTimeOut();
		this.flattenNestedDocument = queryProps.isAutoFlattening();
		this.allowsPartialResults = queryProps.isPartialResultsOk();
	}

	protected void updateQueryProperties(QueryProperties queryProps) {
		if (hasMaxDocumentToSkip()) {
			queryProps.setNumDocsToSkip(getMaxDocumentToSkip());
		} else {
			queryProps.setNumDocsToSkip(null);
		}

		if (hasBatchSize()) {
			queryProps.setBatchSize(getMaxBatchSize());
		} else {
			queryProps.setBatchSize(null);
		}

		if (hasDocumentSearchLimit()) {
			queryProps.setRuntimeMetaDataSearchLimit(getDocumentSearchLimit());
		} else {
			queryProps.setRuntimeMetaDataSearchLimit(null);
		}

		if (indexExpr != null) {
			queryProps.setIndexHints(indexExpr);
		}

		if (queryPreferenceMode != null) {
			queryProps.setQueryReadPreference(queryPreferenceMode);
		}

		if (tagSetValue != null) {
			queryProps.setQueryReadPreferenceTags(tagSetValue);
		}

		queryProps.setNoTimeOut(noTimeOut);
		queryProps.setPartialResultsOk(allowsPartialResults);
		queryProps.setAutoFlattening(flattenNestedDocument);

	}

	private boolean hasBatchSize() {
		return !UIHelper.isEmptyString(this.batchSizeValue);
	}

	protected Integer getMaxBatchSize() {
		return Integer.parseInt(this.batchSizeValue);
	}

	private boolean hasDocumentSearchLimit() {
		return !UIHelper.isEmptyString(this.docSearchLimitValue);
	}

	protected Integer getDocumentSearchLimit() {
		return Integer.parseInt(this.docSearchLimitValue);
	}

	private boolean hasMaxDocumentToSkip() {
		return !UIHelper.isEmptyString(this.maxSkipDocValue);
	}

	protected Integer getMaxDocumentToSkip() {
		return Integer.parseInt(this.maxSkipDocValue);
	}

}
