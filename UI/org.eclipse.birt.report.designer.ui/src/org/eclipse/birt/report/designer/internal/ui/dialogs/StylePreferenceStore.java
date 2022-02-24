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

import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.ibm.icu.util.ULocale;

/**
 * Provides get/set of model for field editors
 */

public class StylePreferenceStore implements IPreferenceStore {

	private Object model;

	private boolean hasError = false;

	/**
	 * The constructor.
	 * 
	 * @param model
	 */
	public StylePreferenceStore(Object model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener
	 * (org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent
	 * (java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String )
	 */
	public boolean getBoolean(String name) {
		return ((StyleHandle) model).getBooleanProperty(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.
	 * lang.String)
	 */
	public boolean getDefaultBoolean(String name) {
		Object obj = ((StyleHandle) model).getPropertyHandle(name).getDefn().getDefault();
		if (obj != null)
			return Boolean.valueOf(obj.toString());
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang
	 * .String)
	 */
	public double getDefaultDouble(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang
	 * .String)
	 */
	public float getDefaultFloat(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang
	 * .String)
	 */
	public int getDefaultInt(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang
	 * .String)
	 */
	public long getDefaultLong(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang
	 * .String)
	 */
	public String getDefaultString(String name) {
		Object obj = ((StyleHandle) model).getPropertyHandle(name).getDefn().getDefault();
		if (obj != null)
			return obj.toString();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
	 */
	public double getDouble(String name) {
		// TODO Auto-generated method stub
		return ((StyleHandle) model).getFloatProperty(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
	 */
	public float getFloat(String name) {
		return (float) ((StyleHandle) model).getFloatProperty(name);
	}

	private ULocale getFormatLocale(String property) {
		Object formatValue = ((StyleHandle) model).getProperty(property);
		if (formatValue instanceof FormatValue) {
			PropertyHandle propHandle = ((StyleHandle) model).getPropertyHandle(property);
			FormatValue formatValueToSet = (FormatValue) formatValue;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
			return formatHandle.getLocale();
		}
		return null;
	}

	/**
	 * Gets string format pattern.
	 */
	public String getStringFormat() {
		return ((StyleHandle) model).getStringFormat();
	}

	public ULocale getStringFormatLocale() {
		return getFormatLocale(IStyleModel.STRING_FORMAT_PROP);
	}

	/**
	 * Gets string format category.
	 */
	public String getStringFormatCategory() {
		return ((StyleHandle) model).getStringFormatCategory();
	}

	/**
	 * Gets Date time format pattern.
	 */
	public String getDateTimeFormat() {
		return ((StyleHandle) model).getDateTimeFormat();
	}

	public ULocale getDateTimeFormatLocale() {
		return getFormatLocale(IStyleModel.DATE_TIME_FORMAT_PROP);
	}

	/**
	 * Gets Date time format category.
	 */
	public String getDateTimeFormatCategory() {
		return ((StyleHandle) model).getDateTimeFormatCategory();
	}

	/**
	 * Gets number format pattern.
	 */
	public String getNumberFormat() {
		return ((StyleHandle) model).getNumberFormat();
	}

	/**
	 * Gets number format category.
	 */
	public String getNumberFormatCategory() {
		return ((StyleHandle) model).getNumberFormatCategory();
	}

	public ULocale getNumberFormatLocale() {
		return getFormatLocale(IStyleModel.NUMBER_FORMAT_PROP);
	}

	public void setFormatLocale(ULocale locale, String property) throws SemanticException {
		Object formatValue = ((StyleHandle) model).getProperty(property);
		if (formatValue instanceof FormatValue) {
			PropertyHandle propHandle = ((StyleHandle) model).getPropertyHandle(property);
			FormatValue formatValueToSet = (FormatValue) formatValue;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
			if (locale != null)
				formatHandle.setLocale(locale);
		}
	}

	/**
	 * Sets string format pattern.
	 */
	public void setStringFormat(String format) throws SemanticException {
		((StyleHandle) model).setStringFormat(format);
	}

	public void setStringFormatLocale(ULocale locale) throws SemanticException {
		setFormatLocale(locale, IStyleModel.STRING_FORMAT_PROP);
	}

	/**
	 * Sets string format category.
	 */
	public void setStringFormatCategory(String category) throws SemanticException {
		((StyleHandle) model).setStringFormatCategory(category);
	}

	/**
	 * Sets date time format pattern.
	 */
	public void setDateTimeFormat(String format) throws SemanticException {
		((StyleHandle) model).setDateTimeFormat(format);
	}

	public void setDateTimeFormatLocale(ULocale locale) throws SemanticException {
		setFormatLocale(locale, IStyleModel.DATE_TIME_FORMAT_PROP);
	}

	/**
	 * Sets date time format category.
	 */
	public void setDateTimeFormatCategory(String category) throws SemanticException {
		((StyleHandle) model).setDateTimeFormatCategory(category);
	}

	/**
	 * Sets number format pattern.
	 */
	public void setNumberFormat(String format) throws SemanticException {
		((StyleHandle) model).setNumberFormat(format);
	}

	public void setNumberFormatLocale(ULocale locale) throws SemanticException {
		setFormatLocale(locale, IStyleModel.NUMBER_FORMAT_PROP);
	}

	/**
	 * Sets number format category.
	 */
	public void setNumberFormatCategory(String category) throws SemanticException {
		((StyleHandle) model).setNumberFormatCategory(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
	 */
	public int getInt(String name) {
		return ((StyleHandle) model).getIntProperty(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
	 */
	public long getLong(String name) {
		// TODO
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
	 */
	public String getString(String name) {
		return ((StyleHandle) model).getStringProperty(name);
	}

	public PropertyHandle getPropertyHandle(String name) {
		return ((StyleHandle) model).getPropertyHandle(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
	 */
	public boolean isDefault(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
	 */
	public boolean needsSaving() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String,
	 * java.lang.String)
	 */
	public void putValue(String name, String value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener
	 * (org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * double)
	 */
	public void setDefault(String name, double value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * float)
	 */
	public void setDefault(String name, float value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * int)
	 */
	public void setDefault(String name, int value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * long)
	 */
	public void setDefault(String name, long value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * java.lang.String)
	 */
	public void setDefault(String name, String defaultObject) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String ,
	 * boolean)
	 */
	public void setDefault(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.
	 * String)
	 */
	public void setToDefault(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * double)
	 */
	public void setValue(String name, double value) {
		try {
			((StyleHandle) model).setProperty(name, new Double(value));
		} catch (SemanticException e) {
			hasError = true;
			WidgetUtil.processError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * float)
	 */
	public void setValue(String name, float value) {
		try {
			((StyleHandle) model).setProperty(name, new Double(value));
		} catch (SemanticException e) {
			hasError = true;
			WidgetUtil.processError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * int)
	 */
	public void setValue(String name, int value) {
		try {
			((StyleHandle) model).setProperty(name, Integer.valueOf(value));
		} catch (SemanticException e) {
			hasError = true;
			WidgetUtil.processError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * long)
	 */
	public void setValue(String name, long value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * java.lang.String)
	 */
	public void setValue(String name, String value) {
		try {
			((StyleHandle) model).setProperty(name, value);
		} catch (SemanticException e) {
			hasError = true;
			WidgetUtil.processError(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
	 * boolean)
	 */
	public void setValue(String name, boolean value) {
		try {
			((StyleHandle) model).setProperty(name, Boolean.valueOf(value));
		} catch (SemanticException e) {
			hasError = true;
			WidgetUtil.processError(e);
		}
	}

	/**
	 * Clears the error token.
	 */
	public void clearError() {
		hasError = false;
	}

	/**
	 * Checks if the previous operations cause error.
	 * 
	 * @return
	 */
	public boolean hasError() {
		return hasError;
	}

	public boolean hasLocalValue(String name) {
		PropertyHandle property = ((StyleHandle) model).getPropertyHandle(name);
		if (property != null)
			return property.isLocal();
		return false;
	}

	public StyleHandle getModel() {
		return ((StyleHandle) model);
	}
}
