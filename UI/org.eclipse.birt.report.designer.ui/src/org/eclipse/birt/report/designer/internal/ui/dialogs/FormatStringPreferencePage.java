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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ibm.icu.util.ULocale;

/**
 * A preference page for formatting string.
 */

public class FormatStringPreferencePage extends BaseStylePreferencePage implements IFormatChangeListener {

	private String name;
	private IFormatPage formatPage;

	/**
	 * Constructs a format string preference page.
	 * 
	 * @param model The model
	 */
	public FormatStringPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("FormatStringPreferencePage.formatString.title")); //$NON-NLS-1$
		setPreferenceName(StringFormatValue.FORMAT_VALUE_STRUCT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout() {
		((GridLayout) getFieldEditorParent().getLayout()).numColumns = 1;
	}

	/**
	 * Sets the preference name.
	 */
	private void setPreferenceName(String name) {
		this.name = name;
	}

	/**
	 * Gets the preference name.
	 */
	public String getPreferenceName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage
	 * #createFieldEditors()
	 */
	protected void createFieldEditors() {
		super.createFieldEditors();
		final Composite parent = getFieldEditorParent();
		formatPage = new FormatStringPage(parent, SWT.NULL);
		formatPage.addFormatChangeListener(this);
		((Composite) formatPage).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		initiateFormatPage();
		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_FORMATSTRING_ID);

	}

	private void initiateFormatPage() {
		String category = ((StylePreferenceStore) getPreferenceStore()).getStringFormatCategory();
		String pattern = ((StylePreferenceStore) getPreferenceStore()).getStringFormat();
		ULocale locale = ((StylePreferenceStore) getPreferenceStore()).getStringFormatLocale();

		formatPage.setInput(category, pattern, locale);
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage
	 * #performOk()
	 */
	public boolean performOk() {
		IPreferenceStore ps = getPreferenceStore();

		if (ps instanceof StylePreferenceStore) {
			((StylePreferenceStore) ps).clearError();
		}
		boolean rt = doStore();
		if (ps instanceof StylePreferenceStore) {
			return !((StylePreferenceStore) ps).hasError();
		}
		return rt;
	}

	/**
	 * Stores the result pattern string into Preference Store.
	 * 
	 * @return
	 */
	protected boolean doStore() {
		if (formatPage == null || !formatPage.isFormatModified() || !formatPage.isDirty()) {
			return true;
		}
		try {
			((StylePreferenceStore) getPreferenceStore()).setStringFormatCategory(formatPage.getCategory());
			((StylePreferenceStore) getPreferenceStore()).setStringFormat(formatPage.getPattern());
			((StylePreferenceStore) getPreferenceStore()).setStringFormatLocale(formatPage.getLocale());
			return true;
		} catch (SemanticException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}

	protected String[] getPreferenceNames() {
		return new String[] { IStyleModel.STRING_FORMAT_PROP };
	}

	private FormatChangeEvent event = null;

	public void formatChange(FormatChangeEvent event) {
		if (getBuilder() != null) {
			this.event = event;
			getBuilder().refreshPagesStatus();
		}
	}

	private boolean firstCheck = false;

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
			if (event != null) {
				hasLocaleProperty = true;
			}
		}
		return hasLocaleProperty;
	}
}