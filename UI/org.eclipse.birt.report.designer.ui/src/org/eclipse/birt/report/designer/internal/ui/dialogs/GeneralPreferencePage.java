/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Provides general preference page.
 */

public class GeneralPreferencePage extends BaseStylePreferencePage {

	private Object model;

	private AbstractThemeHandle theme;

	private Combo preName;

	private Text cusName;

	private Button preStyle;

	private Button cusStyle;

	private boolean initialized = false;

	private Label cusLabel;

	private Label preLabel;

	boolean isReportItemTheme = false;

	/**
	 * Default constructor.
	 * 
	 * @param model , the model of preference page.
	 */
	public GeneralPreferencePage(Object model) {
		this(model, null);
	}

	/**
	 * Constructor with theme.
	 * 
	 * @param model
	 * @param theme
	 */
	public GeneralPreferencePage(Object model, AbstractThemeHandle theme) {
		super(model);

		this.model = model;
		this.theme = theme;

		if (theme instanceof ReportItemThemeHandle)
			isReportItemTheme = true;
	}

	/**
	 * @see org.eclipse.jface.preference.
	 *      FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		createStyleNameControl();

		addField(new SeparatorFieldEditor(getFieldEditorParent()));

		BooleanFieldEditor shrink = new BooleanFieldEditor(
				StyleHandle.CAN_SHRINK_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.CAN_SHRINK_PROP).getDefn().getDisplayNameID()),
				getFieldEditorParent());
		addField(shrink);

		BooleanFieldEditor blank = new BooleanFieldEditor(
				StyleHandle.SHOW_IF_BLANK_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.SHOW_IF_BLANK_PROP).getDefn().getDisplayNameID()),
				getFieldEditorParent());
		addField(blank);
		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_GERNERAL_ID);

		Label note = new Label(getFieldEditorParent(), SWT.NONE);
		note.setText(Messages.getString("GeneralPreferencePage.Label.Note")); //$NON-NLS-1$
		note.setForeground(note.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.END;
		data.horizontalSpan = 2;
		note.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createStyleNameControl() {
		Composite nameComp = new Composite(getFieldEditorParent(), SWT.NULL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		nameComp.setLayoutData(data);
		nameComp.setLayout(new GridLayout(2, false));

		preStyle = new Button(nameComp, SWT.RADIO);
		preStyle.setText(" "); //$NON-NLS-1$
		int width = preStyle.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;

		preStyle.setText(Messages.getString("GeneralPreferencePage.label.predefinedStyle")); //$NON-NLS-1$
		preStyle.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (preStyle.getSelection() == false) {
					return;
				}
				setPredefinedStyle(true);
				preName.setFocus();
				if (preName.getSelectionIndex() == -1) {
					preName.select(0);
				}
				// selectedType = TYPE_PREDEFINED;
				checkPageValid();
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		preStyle.setLayoutData(data);

		preLabel = new Label(nameComp, SWT.NONE);
		preLabel.setText(Messages.getString("GeneralPreferencePage.Label.PreDefinedStyle")); //$NON-NLS-1$
		data = new GridData();
		data.horizontalIndent = width;
		preLabel.setLayoutData(data);

		preName = new Combo(nameComp, SWT.NULL | SWT.READ_ONLY);
		data = new GridData(GridData.FILL_HORIZONTAL);
		preName.setLayoutData(data);
		preName.setVisibleItemCount(30);
		if (isReportItemTheme) {
			preName.setItems(getPredefinedStyleNames(((ReportItemThemeHandle) theme).getType()));
		} else {
			preName.setItems(getPredefinedStyleNames(null));
		}
		if (preName.getItemCount() == 1) {// If only one item,set default selected.
			preName.select(0);
		}

		preName.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				checkPageValid();
			}
		});

		cusStyle = new Button(nameComp, SWT.RADIO);
		cusStyle.setText(Messages.getString("GeneralPreferencePage.label.customStyle")); //$NON-NLS-1$
		cusStyle.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (cusStyle.getSelection() == false) {
					return;
				}
				setPredefinedStyle(false);
				cusName.setFocus();
				// selectedType = TYPE_CUSTOM;
				checkPageValid();
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		cusStyle.setLayoutData(data);

		cusLabel = new Label(nameComp, SWT.NONE);
		cusLabel.setText(Messages.getString("GeneralPreferencePage.Label.CustomStyle")); //$NON-NLS-1$
		data = new GridData();
		data.horizontalIndent = width;
		cusLabel.setLayoutData(data);

		cusName = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		cusName.setLayoutData(data);
		cusName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkPageValid();

			}

		});
	}

	private String[] getPredefinedStyleNames(String type) {
		List preStyles = null;
		if (type == null) {
			preStyles = DEUtil.getMetaDataDictionary().getPredefinedStyles();
		} else {
			preStyles = DEUtil.getMetaDataDictionary().getPredefinedStyles(type);
		}
		if (preStyles == null) {
			return new String[] {};
		}
		String[] names = new String[preStyles.size()];
		for (int i = 0; i < preStyles.size(); i++) {
			names[i] = ((IPredefinedStyle) preStyles.get(i)).getName();
		}
		Arrays.sort(names);
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#initialize()
	 */
	protected void initialize() {
		if (model instanceof StyleHandle) {
			if (((StyleHandle) model).isPredefined() || isReportItemTheme) {
				preStyle.setSelection(true);
				setPredefinedStyle(true);
				preName.setText(((StyleHandle) model).getName());
			} else {
				cusStyle.setSelection(true);
				setPredefinedStyle(false);
				if (((StyleHandle) model).getName() != null) {
					cusName.setText(((StyleHandle) model).getName());
				}
			}
		}

		if (isReportItemTheme) {
			cusStyle.setEnabled(false);
			cusName.setEnabled(false);
		}
		super.initialize();
		initialized = true;
		checkPageValid();
	}

	private void setPredefinedStyle(boolean b) {
		preName.setEnabled(b);
		preLabel.setEnabled(b);
		cusName.setEnabled(!b);
		cusLabel.setEnabled(!b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	public boolean performOk() {
		if (storeName()) {
			return super.performOk();
		}
		cusName.setFocus();
		return false;
	}

	private boolean storeName() {
		IPreferenceStore ps = getPreferenceStore();

		((StylePreferenceStore) ps).clearError();

		if (!checkName(getName(), true)) {
			return false;
		}

		getPreferenceStore().setValue(StyleHandle.NAME_PROP, getName());

		return !((StylePreferenceStore) ps).hasError();
	}

	private boolean checkName(String name, boolean showError) {
		String trimName = name.trim();
		Iterator iterator = null;

		if (theme != null) {
			iterator = DEUtil.getStyles(theme);
		} else {
			iterator = DEUtil.getLocalStyles();
		}

		while (iterator.hasNext()) {
			SharedStyleHandle handle = (SharedStyleHandle) iterator.next();

			if ((handle.getName() != null) && handle.getName().equals(trimName) && (handle != model)) {
				if (showError) {
					ExceptionHandler.openErrorMessageBox(
							Messages.getString("GeneralPreferencePage.errorMsg.duplicate.styleName"), //$NON-NLS-1$
							Messages.getFormattedString("GeneralPreferencePage.label.styleNameDuplicate", //$NON-NLS-1$
									new String[] { name }));

				}
				return false;
			}
		}

		return true;
	}

	private String getName() {
		if (preStyle.getSelection()) {
			return preName.getText();
		}
		return cusName.getText();
	}

	protected boolean checkPageValid() {
		String name = null;
		if (preStyle.getSelection()) {
			name = preName.getText().trim();
		} else {
			name = cusName.getText().trim();
		}

		if (name == null || name.length() == 0) {
			setValid(false);
			if (initialized && (!isValid())) {
				String errorMessage = Messages.getString("GeneralPreferencePage.label.nameEmpty"); //$NON-NLS-1$
				// setMessage( errorMessage, PreferencePage.ERROR );
				setErrorMessage(errorMessage);
			}
		} else if (MetaDataDictionary.getInstance().getPredefinedStyle(name) != null && !preStyle.getSelection()) {
			setValid(false);
			if (initialized && (!isValid())) {
				String errorMessage = Messages.getFormattedString("GeneralPreferencePage.label.styleNamePredefind", //$NON-NLS-1$
						new String[] { name });
				// setMessage( errorMessage, PreferencePage.ERROR );
				setErrorMessage(errorMessage);
			}
		} else {
			setValid(checkName(name, false));
			if (initialized && (!isValid())) {
				String errorMessage = Messages.getFormattedString("GeneralPreferencePage.label.styleNameDuplicate", //$NON-NLS-1$
						new String[] { name });
				// setMessage( errorMessage, PreferencePage.ERROR );
				setErrorMessage(errorMessage);
			}
		}

		if (initialized && isValid()) {
			// setMessage( null, PreferencePage.NONE );
			setErrorMessage(null);
		}

		return isValid();
	}

	protected void checkState() {
		boolean result = isValid();
		if (result) {
			super.checkState();
		}
	}

	/*
	 * (non-Javadoc) Method declared on IDialog.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			if (preStyle.getSelection()) {
				preName.setFocus();
			} else {
				cusName.setFocus();
			}
		}
	}

	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.CAN_SHRINK_PROP, StyleHandle.SHOW_IF_BLANK_PROP, };
	}
}