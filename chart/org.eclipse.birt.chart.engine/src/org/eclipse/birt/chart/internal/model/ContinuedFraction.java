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

package org.eclipse.birt.chart.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.util.ChartUtil;

public class ContinuedFraction {

	private double decimal = 0;
	private List<Long> integerList = new ArrayList<Long>();

	public ContinuedFraction(double decimal) {
		// Correct double precision error here
		this.decimal = ValueFormatter.normalizeDouble(decimal).doubleValue();
		computeList();
	}

	public Fraction getExactFraction() {
		int lastIndex = integerList.size() - 1;
		return getFraction(lastIndex, new Fraction((integerList.get(lastIndex)).intValue(), 1));
	}

	public Fraction getFractionWithMaxDigits(int maxDigitsForDenominator) {
		int lastIndex = integerList.size();
		Fraction previousFraction = null;
		for (int i = 0; i < lastIndex; i++) {
			Fraction fraction = getFraction(i, new Fraction((integerList.get(i)).intValue(), 1));
			if (fraction.getDenominatorDigits() > maxDigitsForDenominator)
				return previousFraction;
			previousFraction = fraction;
		}
		return previousFraction;
	}

	private Fraction getFraction(int index, Fraction fraction) {
		if (index > 0) {
			return getFraction(index - 1, (fraction.invert()).add(integerList.get(index - 1)));
		}
		return fraction;
	}

	private void computeList() {
		int decimalDigits = 0;
		double decimalTemp = decimal;
		while (Math.abs(Math.ceil(decimalTemp) - decimalTemp) > Math.pow(10, decimalDigits - 8)) {
			decimalTemp *= 10.0;
			decimalDigits++;
		}
		long dividend = (long) Math.pow(10, decimalDigits);
		long start = (long) decimalTemp;
		if (ChartUtil.mathEqual(decimalTemp, Math.ceil(decimalTemp))) {
			start = (long) Math.ceil(decimalTemp);
		}

		long quotient = 0;
		long oldDividend = 0;
		do {
			quotient = start / dividend;
			integerList.add(Long.valueOf(quotient));
			oldDividend = dividend;
			dividend = start % dividend;
			start = oldDividend;

		} while (dividend != 0);
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (long l : integerList) {
			s.append(l).append(',');
		}
		if (integerList.size() > 0) {
			s.deleteCharAt(s.length() - 1);
		}
		return s.toString();
	}
}
