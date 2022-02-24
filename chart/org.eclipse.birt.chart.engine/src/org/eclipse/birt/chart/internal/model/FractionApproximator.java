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

public class FractionApproximator {
	public static Fraction getExactFraction(double decimal) {
		ContinuedFraction cf = new ContinuedFraction(decimal);
		return cf.getExactFraction();
	}

	public static Fraction getFractionWithMaxDigits(double decimal, int maxDigitsForDenominator) {
		ContinuedFraction cf = new ContinuedFraction(decimal);
		return cf.getFractionWithMaxDigits(maxDigitsForDenominator);
	}

	public static Fraction getFractionWithNumerator(double decimal, long numerator) {
		if (decimal == 0) {
			return new Fraction(0, 1);
		}
		if (Math.abs(Math.round(decimal)) + 1 > Math.abs(numerator)) {
			// impossible case, reverts to exact fraction
			return getExactFraction(decimal);
		} else {
			return new Fraction(numerator, Math.round(numerator / decimal));
		}
	}

}
