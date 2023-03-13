
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
package org.eclipse.birt.report.data.adapter.group;

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

	protected DateTimeUtil dateTimeUtil;

	public DateGroupCalculator(Object intervalStart, double intervalRange, ULocale locale, TimeZone timeZone)
			throws BirtException {
		super(intervalStart, intervalRange);
		ULocale aLocale = locale == null ? ULocale.getDefault() : locale;
		TimeZone aZone = timeZone == null ? TimeZone.getDefault() : timeZone;

		Calendar c = Calendar.getInstance(aLocale);
		c.setTimeZone(aZone);
		c.clear();
		c.set(1970, 0, 1);
		this.defaultStart = c.getTime();
		this.dateTimeUtil = new DateTimeUtil(aLocale, aZone);
	}

	/**
	 *
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public DateGroupCalculator(Object intervalStart, double intervalRange) throws BirtException {
		super(intervalStart, intervalRange);
		if (intervalStart != null) {
			this.intervalStart = DataTypeUtil.toDate(intervalStart);
		}
	}

	/**
	 *
	 * @return
	 */
	protected int getDateIntervalRange() {
		return (int) Math.round(intervalRange);
	}
}
