/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.group;

import java.math.BigDecimal;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

public class DecimalGroupCalculator extends GroupCalculator {

	BigDecimal doubleStartValue;

	/**
	 *
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public DecimalGroupCalculator(Object intervalStart, double intervalRange) throws BirtException {
		super(intervalStart, intervalRange);
		intervalRange = (intervalRange == 0 ? 1 : intervalRange);
		this.intervalRange = intervalRange;
//		If the  value '-1',grouping interval doesn't work for negative categories .So I change the value to '-Double.MAX_VALUE'.
		if (intervalStart == null) {
			doubleStartValue = new BigDecimal(-Double.MAX_VALUE);
		} else {
			doubleStartValue = DataTypeUtil.toBigDecimal(intervalStart);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.group.GroupCalculator#calculate(java.lang.
	 * Object)
	 */
	@Override
	public Object calculate(Object value) throws BirtException {
		if (value == null) {
//			If the  value '-1',grouping interval doesn't work for negative categories .So I change the value to '-Double.MAX_VALUE'.
			return new BigDecimal(-Double.MAX_VALUE);
		}

		BigDecimal dValue = DataTypeUtil.toBigDecimal(value);

		if (dValue.compareTo(doubleStartValue) < 0) {
			return new Double(-1);
		} else {
			dValue = dValue.subtract(doubleStartValue);
			dValue = dValue.divide(new BigDecimal(intervalRange), 0, BigDecimal.ROUND_FLOOR);
			return dValue;

		}
	}
}
