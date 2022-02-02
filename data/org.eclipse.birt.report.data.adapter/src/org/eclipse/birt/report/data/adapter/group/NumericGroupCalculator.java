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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

/**
 * This calculator is used to calculate a numeric group key basing group
 * interval.
 */
class NumericGroupCalculator extends GroupCalculator {

	double doubleStartValue;
	private double firstValue = Double.MIN_VALUE;

	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public NumericGroupCalculator(Object intervalStart, double intervalRange) throws BirtException {
		super(intervalStart, intervalRange);
		if (intervalStart == null)
			doubleStartValue = 0;
		else
			doubleStartValue = (DataTypeUtil.toDouble(intervalStart)).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.group.GroupCalculator#calculate(java.lang.
	 * Object)
	 */
	public Object calculate(Object value) throws BirtException {
		double dValue = -1;
		if (value != null)
			dValue = (DataTypeUtil.toDouble(value)).doubleValue();

		if (dValue < doubleStartValue) {
			if (this.firstValue == Double.MIN_VALUE) {
				this.firstValue = dValue;
			}
			return new Double(this.firstValue);
		} else {
			return new Double(
					doubleStartValue + Math.floor((dValue - doubleStartValue) / intervalRange) * intervalRange);
		}
	}
}
