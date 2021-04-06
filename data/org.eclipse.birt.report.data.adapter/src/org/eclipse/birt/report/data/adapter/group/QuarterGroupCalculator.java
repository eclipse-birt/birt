/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.group;

import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * This calculator is used to calculate a quarter group key basing group
 * interval.
 */
class QuarterGroupCalculator extends DateGroupCalculator {

	public QuarterGroupCalculator(Object intervalStart, double intervalRange, ULocale locale, TimeZone timeZone)
			throws BirtException {
		super(intervalStart, intervalRange, locale, timeZone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.group.DateGroupCalculator#calculate(java.
	 * lang.Object)
	 */
	public Object calculate(Object value) {
		if (value == null) {
			return new Double(-1);
		}

		if (intervalStart == null) {
			return new Double(
					Math.floor(this.dateTimeUtil.diffQuarter(defaultStart, (Date) value) / getDateIntervalRange()));
		} else {
			if (this.dateTimeUtil.diffQuarter((Date) intervalStart, (Date) value) < 0) {
				return new Double(-1);
			} else {
				return new Double(Math.floor(
						this.dateTimeUtil.diffQuarter((Date) intervalStart, (Date) value) / getDateIntervalRange()));
			}
		}
	}
}
