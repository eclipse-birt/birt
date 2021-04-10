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

package org.eclipse.birt.data.engine.impl.group;

import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * This calculator is used to calculate a second group key basing group
 * interval.
 */
class SecondGroupCalculator extends DateGroupCalculator {

	public SecondGroupCalculator(Object intervalStart, double intervalRange, ULocale locale, TimeZone timeZone)
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
	public Object calculate(Object value) throws BirtException {
		if (value == null) {
			return new Double(-1);
		}

		Date target = getDate(value);

		if (intervalStart == null) {
			return new Double(Math.floor(
					(double) this.dateTimeUtil.diffSecond(defaultStart, target) / (double) getDateIntervalRange()));
		} else {
			if (this.dateTimeUtil.diffSecond((Date) intervalStart, target) < 0) {
				return new Double(-1);
			} else {
				return new Double(Math.floor((double) this.dateTimeUtil.diffSecond((Date) intervalStart, target)
						/ (double) getDateIntervalRange()));
			}
		}
	}
}
