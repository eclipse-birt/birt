/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * TableOptionBindingDialog
 */
public class TableOptionBindingDialog extends TableOptionDialog {

	private DataSetBindingSelector selector;

	public TableOptionBindingDialog(Shell parentShell) {
		super(parentShell, true);
	}

	protected Control createDialogArea(Composite parent) {
		loadPreference();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);

		new Label(composite, SWT.NONE).setText(MSG_DATA_SET);

		dataSetCombo = new Combo(composite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		dataSetCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataSetCombo.setVisibleItemCount(30);
		String[] dataSets = ChoiceSetFactory.getDataSets();
		String[] newList = new String[dataSets.length + 1];
		System.arraycopy(dataSets, 0, newList, 1, dataSets.length);
		newList[0] = NONE;
		dataSetCombo.setItems(newList);
		dataSetCombo.select(0);

		autoChk = new Button(composite, SWT.CHECK);
		autoChk.setText(Messages.getString("TableOptionDialog.text.AutoSummary")); //$NON-NLS-1$
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.horizontalSpan = 2;
		gdata.verticalIndent = 10;
		gdata.horizontalIndent = 10;
		autoChk.setLayoutData(gdata);
		autoChk.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (dataSetCombo.getSelectionIndex() == 0) {
					rowEditor.setEnabled(!autoChk.getSelection());
				}
			}
		});

		final Composite contentPane = new Composite(composite, SWT.NONE);
		gdata = new GridData(GridData.FILL_BOTH);
		gdata.horizontalSpan = 2;
		gdata.minimumWidth = 400;
		gdata.minimumHeight = 250;
		contentPane.setLayoutData(gdata);
		contentPane.setLayout(new GridLayout());

		createStaticUI(contentPane);

		dataSetCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (dataSetCombo.getSelectionIndex() > 0) {
					createBindingUI(contentPane);
				} else {
					createStaticUI(contentPane);
				}

				contentPane.layout();
			};
		});

		UIUtil.bindHelp(parent, IHelpContextIds.TABLE_OPTION_DIALOG_ID);

		return composite;
	}

	private void createStaticUI(Composite parent) {
		disposeChildren(parent);

		new Label(parent, SWT.NONE).setText(MSG_TABLE_SIZE);
		Label sp = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		sp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite innerPane = new Composite(parent, SWT.NONE);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.horizontalSpan = 2;
		innerPane.setLayoutData(gdata);
		GridLayout glayout = new GridLayout(2, false);
		glayout.marginWidth = 10;
		glayout.marginHeight = 10;
		innerPane.setLayout(glayout);

		new Label(innerPane, SWT.NONE).setText(MSG_NUMBER_OF_COLUMNS);
		columnEditor = new Spinner(innerPane, SWT.BORDER);
		columnEditor.setMinimum(1);
		columnEditor.setMaximum(Integer.MAX_VALUE);
		columnEditor.setIncrement(1);
		columnEditor.setSelection(columnCount);
		columnEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(innerPane, SWT.NONE).setText(insertTable ? MSG_NUMBER_OF_TABLE_ROWS : MSG_NUMBER_OF_GRID_ROWS);
		rowEditor = new Spinner(innerPane, SWT.BORDER);
		rowEditor.setMinimum(1);
		rowEditor.setMaximum(Integer.MAX_VALUE);
		rowEditor.setIncrement(1);
		rowEditor.setSelection(rowCount);
		rowEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		chkbox = new Button(innerPane, SWT.CHECK);
		chkbox.setText(insertTable ? MSG_REMEMBER_DIMENSIONS_FOR_NEW_TABLES : MSG_REMEMBER_DIMENSIONS_FOR_NEW_GRIDS);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.horizontalSpan = 2;
		chkbox.setLayoutData(gdata);
	}

	private void createBindingUI(Composite parent) {
		disposeChildren(parent);

		selector = new DataSetBindingSelector(UIUtil.getDefaultShell(),
				Messages.getString("DataSetBindingSelectorPage.Title")); //$NON-NLS-1$

		String dsName = dataSetCombo.getItem(dataSetCombo.getSelectionIndex());
		boolean isDataSet = SessionHandleAdapter.getInstance().getReportDesignHandle().findDataSet(dsName) != null;

		selector.setDataSet(dsName, isDataSet);
		Control control = selector.createDialogArea(parent);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void disposeChildren(Composite parent) {
		Control[] cc = parent.getChildren();

		if (cc != null) {
			for (Control c : cc) {
				c.dispose();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		if (dataSetCombo.getSelectionIndex() == 0) {
			rowCount = rowEditor.getSelection();
			columnCount = columnEditor.getSelection();

			if (columnCount <= 0) {
				columnCount = DEFAULT_COLUMN_COUNT;
			}
			if (rowCount <= 0) {
				rowCount = insertTable ? DEFAULT_TABLE_ROW_COUNT : DEFAULT_ROW_COUNT;
			}

			setResult(new Object[] {
					new Object[] { Integer.valueOf(rowCount), Integer.valueOf(columnCount), autoChk.getSelection() },
					null });

			if (chkbox.getSelection()) {
				savePreference();
			}
		} else {
			selector.okPressed();

			setResult(
					new Object[] {
							new Object[] { Integer.valueOf(DEFAULT_TABLE_ROW_COUNT),
									Integer.valueOf(DEFAULT_COLUMN_COUNT), autoChk.getSelection() },
							selector.getResult() });
		}

		setReturnCode(OK);
		close();
	}

}
