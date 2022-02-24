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

/**
 * Fraction class to represent a Fraction
 */
public class Fraction {

	private long numerator = 0;

	private long denominator = 1;

	public Fraction(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * @return the denominator
	 */
	public long getDenominator() {
		return denominator;
	}

	/**
	 * @param denominator the denominator to set
	 */
	public void setDenominator(long denominator) {
		this.denominator = denominator;
	}

	/**
	 * @return the numerator
	 */
	public long getNumerator() {
		return numerator;
	}

	/**
	 * @param numerator the numerator to set
	 */
	public void setNumerator(long numerator) {
		this.numerator = numerator;
	}

	public Fraction invert() {
		if (denominator == 0)
			assert false;

		long temp = numerator;
		numerator = denominator;
		denominator = temp;
		return this;
	}

	public long getDenominatorDigits() {
		long digitLeft = Math.abs(denominator);
		int digits = 0;
		while (digitLeft > 0) {
			digitLeft /= 10;
			digits++;
		}
		return digits;
	}

	/**
	 * Add an integer to the fraction
	 */
	public Fraction add(Long number) {
		numerator = numerator + denominator * number.longValue();
		return this;
	}

	public String toString() {
		return toString("/"); //$NON-NLS-1$
	}

	public String toString(String separator) {
		if (denominator != 1) {
			StringBuffer buffer = new StringBuffer();
			// both negative: no sign
			if ((numerator * denominator) < 0)
				buffer.append("-"); //$NON-NLS-1$
			buffer.append(Math.abs(numerator));
			buffer.append(separator);
			buffer.append(Math.abs(denominator));
			return buffer.toString();
		} else
			return String.valueOf(numerator);
	}

}
