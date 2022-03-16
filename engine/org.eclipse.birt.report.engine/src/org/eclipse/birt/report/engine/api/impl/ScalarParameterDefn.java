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

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;

import com.ibm.icu.util.ULocale;

/**
 * Defines a scalar parameter
 */
public class ScalarParameterDefn extends ParameterDefn implements IScalarParameterDefn {
	protected boolean cancealValue;
	protected boolean allowNull;
	protected boolean allowBlank;
	protected String displayFormat;
	protected int controlType;
	protected int alignment;

	protected boolean fixedOrder;

	protected boolean allowNewValues;

	protected String defaultValue;

	// simple, multi-value, ad-hoc
	protected String scalarParameterType;

	protected int autoSuggestThreshold;

	protected Logger log = Logger.getLogger(ScalarParameterDefn.class.getName());

	/**
	 * @return
	 */
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param value
	 */
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IScalarParameterDefn#isValueConcealed()
	 */
	@Override
	public boolean isValueConcealed() {
		return cancealValue;
	}

	public void setValueConcealed(boolean valueConceal) {
		this.cancealValue = valueConceal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#allowNull()
	 */
	@Override
	public boolean allowNull() {
		return !isRequired();
	}

	/**
	 * @deprecated
	 * @param allowNull whether allow null value for the specific parameter
	 */
	@Deprecated
	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#allowBlank()
	 */
	@Override
	public boolean allowBlank() {
		return !isRequired();
	}

	/**
	 * @deprecated
	 * @param allowBlank
	 */
	@Deprecated
	public void setAllowBlank(boolean allowBlank) {
		this.allowBlank = allowBlank;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getFormat()
	 */
	@Override
	public String getDisplayFormat() {
		return displayFormat;
	}

	/**
	 * @param format
	 */
	public void setFormat(String format) {
		this.displayFormat = format;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getControlType()
	 */
	@Override
	public int getControlType() {
		return controlType;
	}

	/**
	 * @param controlType
	 */
	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getAlignment()
	 */
	@Override
	public int getAlignment() {
		return alignment;
	}

	/**
	 * @param align
	 */
	public void setAlignment(int align) {
		this.alignment = align;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefn#displayInFixedOrder()
	 */
	@Override
	public boolean displayInFixedOrder() {
		return fixedOrder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IParameterDefnBase#getParameterType()
	 */
	@Override
	public int getParameterType() {
		return parameterType;
	}

	/**
	 * @param fixedOrder The fixedOrder to set.
	 */
	public void setFixedOrder(boolean fixedOrder) {
		this.fixedOrder = fixedOrder;
	}

	/**
	 * @param parameterType The parameterType to set.
	 */
	@Override
	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object newObj = super.clone();
		ScalarParameterDefn para = (ScalarParameterDefn) newObj;
		ArrayList list = para.getSelectionList();
		if (list == null) {
			return para;
		}
		ArrayList newList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			ParameterSelectionChoice select = (ParameterSelectionChoice) list.get(i);
			newList.add(select.clone());
		}
		para.setSelectionList(newList);
		return para;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefn#allowNewValues()
	 */
	@Override
	public boolean allowNewValues() {
		return allowNewValues;
	}

	/**
	 * @param allowNewValues whether value not in the selection list is allowed for
	 *                       this parameter
	 */
	public void setAllowNewValues(boolean allowNewValues) {
		this.allowNewValues = allowNewValues;
	}

	/**
	 * creates the static selection list
	 */
	public void evaluateSelectionList() {
		// For now, supports static list only
		if (selectionListType == IScalarParameterDefn.SELECTION_LIST_STATIC) {
			boolean sortDisplayValue = true;
			for (int i = 0; i < selectionList.size(); i++) {
				ParameterSelectionChoice choice = (ParameterSelectionChoice) selectionList.get(i);
				choice.setLocale(locale);
				if (choice.getLabel() == null) {
					sortDisplayValue = false;
					break;
				}
			}

			// sort
			if (!fixedOrder) {
				Collections.sort(selectionList,
						new SelectionChoiceComparator(sortDisplayValue, displayFormat, ULocale.forLocale(locale)));
			}
		}
	}

	@Override
	public String getScalarParameterType() {
		return this.scalarParameterType;
	}

	@Override
	public void setScalarParameterType(String type) {
		this.scalarParameterType = type;
	}

	public void setAutoSuggestThreshold(int value) {
		autoSuggestThreshold = value;
	}

	@Override
	public int getAutoSuggestThreshold() {
		return autoSuggestThreshold;
	}
}
