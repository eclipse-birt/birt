/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class ExternalizedTextEditorDialog extends TrayDialog implements SelectionListener {

	private transient String sResult = ""; //$NON-NLS-1$

	private transient String sInputValue = ""; //$NON-NLS-1$

	private transient Button cbExternalize = null;

	private transient Combo cmbKeys = null;

	private transient Text txtValue = null;

	private transient Text txtCurrent = null;

	private transient List<String> keys = null;

	private transient IUIServiceProvider serviceprovider = null;

	private String defaultValue;

	/**
	 * @param parent
	 * @param style
	 */
	public ExternalizedTextEditorDialog(Shell parent, String sText, List<String> keys,
			IUIServiceProvider serviceprovider, String defaultValue) {
		super(parent);
		this.sResult = sText;
		this.sInputValue = getValueComponent(sText);
		this.keys = keys;
		this.serviceprovider = serviceprovider;
		this.defaultValue = defaultValue;
		setHelpAvailable(false);
	}

	public String getResult() {
		return sResult;
	}

	protected Control createContents(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_EXTERNALIZE_TEXT);
		getShell().setText(Messages.getString("ExternalizedTextEditorDialog.Title.ExternalizeText")); //$NON-NLS-1$
		return super.createContents(parent);
	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	protected Control createDialogArea(Composite parent) {
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 16;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		Composite cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);
		cmpContent.setLayoutData(new GridData(GridData.FILL_BOTH));

		cbExternalize = new Button(cmpContent, SWT.CHECK);
		GridData gdCBExternalize = new GridData(GridData.FILL_HORIZONTAL);
		gdCBExternalize.horizontalSpan = 2;
		cbExternalize.setLayoutData(gdCBExternalize);
		cbExternalize.setText(Messages.getString("ExternalizedTextEditorDialog.Lbl.ExternalizeText")); //$NON-NLS-1$
		cbExternalize.addSelectionListener(this);

		Label lblKey = new Label(cmpContent, SWT.NONE);
		GridData gdLBLKey = new GridData();
		lblKey.setLayoutData(gdLBLKey);
		lblKey.setText(Messages.getString("ExternalizedTextEditorDialog.Lbl.LookupKey")); //$NON-NLS-1$

		cmbKeys = new Combo(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBKeys = new GridData(GridData.FILL_HORIZONTAL);
		cmbKeys.setLayoutData(gdCMBKeys);
		cmbKeys.addSelectionListener(this);

		// Layout for Current Value composite
		GridLayout glCurrent = new GridLayout();
		glCurrent.horizontalSpacing = 5;
		glCurrent.verticalSpacing = 5;
		glCurrent.marginHeight = 0;
		glCurrent.marginWidth = 0;

		Composite cmpCurrent = new Composite(cmpContent, SWT.NONE);
		GridData gdCMPCurrent = new GridData(GridData.FILL_BOTH);
		gdCMPCurrent.horizontalSpan = 2;
		cmpCurrent.setLayoutData(gdCMPCurrent);
		cmpCurrent.setLayout(glCurrent);

		Label lblValue = new Label(cmpCurrent, SWT.NONE);
		GridData gdLBLValue = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		lblValue.setLayoutData(gdLBLValue);
		lblValue.setText(Messages.getString("ExternalizedTextEditorDialog.Lbl.DefaultValue")); //$NON-NLS-1$

		txtValue = new Text(cmpCurrent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gdTXTValue = new GridData(GridData.FILL_BOTH);
		gdTXTValue.widthHint = 280;
		gdTXTValue.heightHint = 40;
		txtValue.setLayoutData(gdTXTValue);

		// Layout for Current Value composite
		GridLayout glExtValue = new GridLayout();
		glExtValue.horizontalSpacing = 5;
		glExtValue.verticalSpacing = 5;
		glExtValue.marginHeight = 0;
		glExtValue.marginWidth = 0;

		Composite cmpExtValue = new Composite(cmpContent, SWT.NONE);
		GridData gdCMPExtValue = new GridData(GridData.FILL_BOTH);
		gdCMPExtValue.horizontalSpan = 2;
		cmpExtValue.setLayoutData(gdCMPExtValue);
		cmpExtValue.setLayout(glExtValue);

		Label lblExtValue = new Label(cmpExtValue, SWT.NONE);
		GridData gdLBLExtValue = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		lblExtValue.setLayoutData(gdLBLExtValue);
		lblExtValue.setText(Messages.getString("ExternalizedTextEditorDialog.Lbl.ExternalizedValue")); //$NON-NLS-1$

		txtCurrent = new Text(cmpExtValue,
				SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData gdTXTCurrent = new GridData(GridData.FILL_BOTH);
		gdTXTCurrent.widthHint = 280;
		gdTXTCurrent.heightHint = 40;
		txtCurrent.setLayoutData(gdTXTCurrent);

		// Layout for button composite
		GridLayout glButtons = new GridLayout();
		glButtons.numColumns = 2;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 0;
		glButtons.marginWidth = 0;
		glButtons.marginHeight = 0;

		populateList();

		return cmpContent;
	}

	private void populateList() {
		if (keys.isEmpty()) {
			cbExternalize.setSelection(false);
			cbExternalize.setEnabled(false);
			cmbKeys.setEnabled(false);
		} else {
			Collections.sort(keys);
			cmbKeys.setItems(keys.toArray(new String[keys.size()]));
			String str = getKeyComponent(sResult);
			if (str != null && str.length() != 0) {
				cbExternalize.setSelection(true);
				cmbKeys.setEnabled(true);
				if (!keys.contains(str)) {
					// Add non-existent key into list
					cmbKeys.add(str);
					// Select newly added entry
					cmbKeys.select(cmbKeys.getItemCount() - 1);
				} else {
					cmbKeys.setText(str);
				}
			} else {
				cbExternalize.setSelection(false);
				cmbKeys.setEnabled(false);
				cmbKeys.select(0);
			}
		}
		txtValue.setText(getDisplayValue());
		txtCurrent.setText(getCurrentPropertyValue());
	}

	private String getKeyComponent(String sText) {
		if (sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR) != -1) {
			return sText.substring(0, sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR));
		}
		return null;
	}

	private String getValueComponent(String sText) {
		String sKey = getKeyComponent(sText);
		if (sKey == null || "".equals(sKey) || serviceprovider == null) //$NON-NLS-1$
		{
			if (sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR) != -1) {
				return sText.substring(sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR)
						+ ExternalizedTextEditorComposite.SEPARATOR.length(), sText.length());
			}
			return sText;
		}
		String sValue = serviceprovider.getValue(sKey);
		if (sValue == null || "".equals(sValue)) //$NON-NLS-1$
		{
			sValue = Messages.getString("ExternalizedTextEditorDialog.Warn.KeyNotFound"); //$NON-NLS-1$
		}
		return sValue;
	}

	private String getCurrentPropertyValue() {
		if (!cbExternalize.getSelection() || sResult == null || "".equals(sResult)) //$NON-NLS-1$
		{
			return ""; //$NON-NLS-1$
		}
		return getValueComponent(sResult);
	}

	private String getDisplayValue() {
		if (cbExternalize.getSelection()) {
			if (defaultValue == null || defaultValue.length() == 0) {
				return new MessageFormat(Messages.getString("ExternalizedTextEditorDialog.Lbl.Value")) //$NON-NLS-1$
						.format(new Object[] { getKeyComponent(sResult) });
			}
			return defaultValue;
		}
		return getValueComponent(sResult);
	}

	/**
	 * @return "key=defaultValue"
	 */
	private String buildString() {
		StringBuffer sbText = new StringBuffer(""); //$NON-NLS-1$
		String sKey = cmbKeys.getText();
		if (cbExternalize.getSelection()) {
			sbText.append(sKey);
			sbText.append(ExternalizedTextEditorComposite.SEPARATOR);
		} else if (txtValue.getText().contains(ExternalizedTextEditorComposite.SEPARATOR)) {
			sbText.append(ExternalizedTextEditorComposite.SEPARATOR);
		}
		sbText.append(txtValue.getText());

		return sbText.toString();
	}

	protected void okPressed() {
		sResult = buildString();
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(cbExternalize)) {
			if (cbExternalize.getSelection()) {
				cmbKeys.setEnabled(true);
				if (cmbKeys.getItemCount() > 0) {
					sResult = buildString();
					txtValue.setText(getDisplayValue());
					txtCurrent.setText(getCurrentPropertyValue());
				}
			} else {
				cmbKeys.setEnabled(false);
				txtValue.setText(sInputValue);
				txtCurrent.setText(""); //$NON-NLS-1$
			}
		} else if (e.getSource().equals(cmbKeys)) {
			sResult = buildString();
			txtValue.setText(getDisplayValue());
			txtCurrent.setText(getCurrentPropertyValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}
}
