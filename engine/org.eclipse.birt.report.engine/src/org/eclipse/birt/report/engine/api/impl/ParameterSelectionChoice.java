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

package org.eclipse.birt.report.engine.api.impl;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;

import com.ibm.icu.util.ULocale;

/**
 * Wraps around a parameter selection choice
 */
public class ParameterSelectionChoice implements IParameterSelectionChoice, Cloneable {
	protected Locale locale;
	protected SelectionChoiceHandle handle;
	protected String label;
	protected String labelKey;

	protected Object value;

	protected Logger log = Logger.getLogger(ParameterSelectionChoice.class.getName());

	/**
	 * @param design the report design
	 */
	public ParameterSelectionChoice(SelectionChoiceHandle handle) {
		this.handle = handle;
	}

	/**
	 * @param locale the locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param lableKey the label key string
	 * @param label    the label string
	 */
	public void setLabel(String lableKey, String label) {
		this.label = label;
		this.labelKey = lableKey;
	}

	/**
	 * set parameter choice value. The string value is in English locale, and needs
	 * to be parsed back into object value based on the data type.
	 *
	 * @param value the string value for the object
	 * @param type  the parameter data type
	 */
	public void setValue(String value, int type) {
		try {
			switch (type) {
			case IScalarParameterDefn.TYPE_BOOLEAN:
				this.value = DataTypeUtil.toBoolean(value);
				break;
			case IScalarParameterDefn.TYPE_DATE_TIME:
				this.value = DataTypeUtil.toDate(value);
				break;
			case IScalarParameterDefn.TYPE_DECIMAL:
				this.value = DataTypeUtil.toBigDecimal(value);
				break;
			case IScalarParameterDefn.TYPE_FLOAT:
				this.value = DataTypeUtil.toDouble(value);
				break;
			case IScalarParameterDefn.TYPE_INTEGER:
				this.value = DataTypeUtil.toInteger(value);
				break;
			case IScalarParameterDefn.TYPE_DATE:
				this.value = DataTypeUtil.toSqlDate(value);
				break;
			case IScalarParameterDefn.TYPE_TIME:
				this.value = DataTypeUtil.toSqlTime(value);
				break;
			case IScalarParameterDefn.TYPE_STRING:
			default:
				this.value = DataTypeUtil.toString(value);
				break;
			}
		} catch (BirtException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.value = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IParameterSelectionChoice#getLabel()
	 */
	@Override
	public String getLabel() {
		if (labelKey == null) {
			return label;
		}

		String ret = handle.getExternalizedValue(SelectionChoice.LABEL_RESOURCE_KEY_MEMBER,
				SelectionChoice.LABEL_MEMBER, (locale == null) ? ULocale.getDefault() : ULocale.forLocale(locale));
		return (ret == null || ret.length() == 0) ? label : ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return returns the choice value
	 */
	@Override
	public Object getValue() {
		return value;
	}
}
