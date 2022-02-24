
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.group;

import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.DateTimeUtil;
import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * This calculator is used to calculate a datetime group key basing group
 * interval.
 */

abstract class DateGroupCalculator extends GroupCalculator {

	protected Date defaultStart;
	protected ULocale locale;
	protected TimeZone timeZone;
	protected DateTimeUtil dateTimeUtil;
	private int range;

	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public DateGroupCalculator(Object intervalStart, double intervalRange, ULocale locale, TimeZone timeZone)
			throws BirtException {
		super(intervalStart, intervalRange);
		range = (int) Math.round(intervalRange);
		range = (range == 0 ? 1 : range);
		if (intervalStart != null)
			this.intervalStart = DataTypeUtil.toDate(intervalStart);
		this.locale = locale == null ? ULocale.getDefault() : locale;
		this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
		Calendar c = Calendar.getInstance(this.locale);
		c.setTimeZone(this.timeZone);
		c.clear();
		c.set(1970, 0, 1);
		defaultStart = c.getTime();

		this.dateTimeUtil = new DateTimeUtil(this.locale, this.timeZone);
	}

	/**
	 * 
	 * @return
	 */
	protected int getDateIntervalRange() {
		return range;
	}

	protected Date getDate(Object value) throws BirtException {
		return DataTypeUtil.toDate(value);
	}
}
