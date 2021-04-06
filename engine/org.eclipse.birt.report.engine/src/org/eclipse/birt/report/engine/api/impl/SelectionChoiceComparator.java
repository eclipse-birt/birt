/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import java.util.Comparator;

import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;

import com.ibm.icu.util.ULocale;

public class SelectionChoiceComparator implements Comparator {
	/*
	 * here, use a boolean value to express the direction of the sort "true" means
	 * ASC and false means DESC the default value is true
	 */
	private boolean sortDirection = true;

	protected boolean sortDisplayValue;
	protected String format = null;
	protected ULocale locale = null;

	public SelectionChoiceComparator(boolean sortDisplayValue, String format, ULocale locale) {
		this.sortDisplayValue = sortDisplayValue;
		this.format = format;
		this.locale = locale;
		if (null == this.locale) {
			this.locale = ULocale.getDefault();
		}
	}

	public SelectionChoiceComparator(boolean sortDisplayValue, String format, boolean sortDirection, ULocale locale) {
		this.sortDirection = sortDirection;
		this.sortDisplayValue = sortDisplayValue;
		this.format = format;
		this.locale = locale;
		if (null == this.locale) {
			this.locale = ULocale.getDefault();
		}
	}

	public int compare(Object o1, Object o2) {
		int compareResult = -1;/* default value */
		if ((o1 instanceof IParameterSelectionChoice) && (o2 instanceof IParameterSelectionChoice)) {
			Object value1;
			Object value2;
			if (sortDisplayValue) {
				value1 = ((IParameterSelectionChoice) o1).getLabel();
				value2 = ((IParameterSelectionChoice) o2).getLabel();
				if (null == value1) {
					value1 = getDisplayValue(((IParameterSelectionChoice) o1).getValue());
				}
				if (null == value2) {
					value2 = getDisplayValue(((IParameterSelectionChoice) o2).getValue());
				}

				compareResult = compareValues(value1, value2);
				if (compareResult != 0)
					return compareResult;
			}

			value1 = ((IParameterSelectionChoice) o1).getValue();
			value2 = ((IParameterSelectionChoice) o2).getValue();

			return compareValues(value1, value2);
		}

		return resultByDirection(compareResult);
	}

	private int compareValues(Object value1, Object value2) {
		int compareResult = -1;
		if (value1 == value2) {
			return 0;
		} else if (value1 == null) {
			return -1;
		} else if (value2 == null) {
			return 1;
		}

		if ((value1 instanceof Boolean) && (value2 instanceof Boolean)) {
			if (((Boolean) value1).booleanValue() ^ ((Boolean) value1).booleanValue()) {
				compareResult = 0;
			} else {
				compareResult = ((Boolean) value1).booleanValue() ? 1 : -1;
			}

			return resultByDirection(compareResult);
		}

		if (value1 instanceof Comparable || value2 instanceof Comparable) {
			if (value1 instanceof Comparable) {
				compareResult = ((Comparable) value1).compareTo(value2);
			} else {
				compareResult = -((Comparable) value2).compareTo(value1);
			}
			return resultByDirection(compareResult);
		}

		return resultByDirection(compareResult);
	}

	private int resultByDirection(int compareResult) {
		if (sortDirection) {
			return compareResult;
		}
		return -compareResult;
	}

	/**
	 * convert value to display value
	 * 
	 * @param value
	 */
	private String getDisplayValue(Object value) {
		if (null == value) {
			return null;
		}

		ReportParameterConverter converter = new ReportParameterConverter(format, locale);
		return converter.format(value);
	}
}
