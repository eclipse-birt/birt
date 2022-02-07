/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.aggregate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.core.data.DataType;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * The empty IAggregateFunction adapter.
 */
public abstract class AggregateFunctionAdapter implements IAggregateFunction {

	private int iDataType = UNKNOWN;

	private ULocale lcl = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#accumulate(java.lang.
	 * Object)
	 */
	public void accumulate(Object oValue) throws IllegalArgumentException {
		detectTypeChange(oValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getAggregatedValue()
	 */
	public Object getAggregatedValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#initialize()
	 */
	public void initialize() {
		iDataType = UNKNOWN;
	}

	/**
	 * Internally detects if the accumulated data's data type has changed in the
	 * aggregate function.
	 * 
	 * @param oValue
	 */
	private final void detectTypeChange(Object oValue) throws IllegalArgumentException {
		if (iDataType == UNKNOWN) {
			if (oValue == null) {
				return;
			} else if (oValue instanceof Number) {
				iDataType = NUMBER;
			} else if (oValue instanceof BigDecimal) {
				iDataType = BIGDECIMAL;
			} else if (oValue instanceof Date) {
				iDataType = DATE;
			} else if (oValue instanceof Calendar) {
				iDataType = CALENDAR;
			} else if (oValue instanceof String) {
				iDataType = TEXT;
			} else {
				iDataType = CUSTOM;
			}
		} else {
			final int iExistingType = iDataType;
			if (oValue == null) {
				// Set unknown type for null
				iDataType = UNKNOWN;
				return;
			} else if (oValue instanceof Number) {
				iDataType = NUMBER;
			} else if (oValue instanceof BigDecimal) {
				iDataType = BIGDECIMAL;
			} else if (oValue instanceof Date) {
				iDataType = DATE;
			} else if (oValue instanceof Calendar) {
				iDataType = CALENDAR;
			} else if (oValue instanceof String) {
				iDataType = TEXT;
			} else {
				iDataType = CUSTOM;
			}
			if (iExistingType != iDataType) {
				throw new IllegalArgumentException(
						MessageFormat.format(Messages.getResourceBundle(lcl).getString("exception.mixed.data.types"), //$NON-NLS-1$
								new Object[] { getClass().getName() }));
			}
		}
	}

	/**
	 * Sets a locale associated with this aggregate function instance
	 * 
	 * @param lcl A locale associated with this aggregate function instance
	 */
	public final void setLocale(ULocale lcl) {
		this.lcl = lcl;
	}

	/**
	 * Returns the locale associated with this aggregate function instance to be
	 * used by this aggregate function
	 * 
	 * @return The locale associated with this aggregate function instance
	 */
	protected final ULocale getLocale() {
		return (lcl == null) ? ULocale.getDefault() : lcl;
	}

	/**
	 * 
	 * @return
	 */
	protected final int getDataType() {
		return iDataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#getDisplayParameters()
	 */
	public String[] getDisplayParameters() {
		return new String[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getParametersCount()
	 */
	public int getParametersCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getType()
	 */
	public int getType() {
		return SUMMARY_AGGR;
	}

	public int getBIRTDataType() {
		return DataType.UNKNOWN_TYPE;
	}
}
