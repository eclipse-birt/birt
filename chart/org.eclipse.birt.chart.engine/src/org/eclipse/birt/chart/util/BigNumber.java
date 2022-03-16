/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.util;

import com.ibm.icu.math.BigDecimal;

/**
 * The class extends Number to represent a big number, which is used by chart to
 * store big decimal in data set.
 * <p>
 * In order to reuse current framework of chart engine to compute axes scale and
 * chart layout/rendering, each big decimal will be represented as [Big Decimal]
 * = [Double value] X [divisor], the BigNumber stores [Double Value] and
 * [divisor] and original big decimal value. All data sets in same axis will
 * have same [divisor], then the [Double value] of each value will be used to
 * compute axis scale and chart layout.
 * <p>
 * But the formatter of axis label and data points still use big decimal as
 * standard reference.
 *
 * @since 2.6
 */

public class BigNumber extends Number implements Comparable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The original big decimal value.
	 */
	private BigDecimal value;

	/**
	 * The divisor of big decimal.
	 */
	private BigDecimal divisor;

	/**
	 * The double part which is used to compute axis scale and chart layout by chart
	 * engine.
	 */
	private double doublePart;

	/**
	 * Constructs a raw instance of BigNumber, which is not set divisor.
	 *
	 * @param value
	 */
	public BigNumber(String value) {
		this(new BigDecimal(value));
	}

	/**
	 * Constructs a raw instance of BigNumber, which is not set divisor.
	 *
	 * @param value
	 */
	public BigNumber(BigDecimal value) {
		this(value, null);
	}

	/**
	 * Constructs an instance of BigNumber with divisor.
	 *
	 * @param value
	 * @param divisor
	 */
	public BigNumber(BigDecimal value, BigDecimal divisor) {
		this.value = value;
		if (divisor != null) {
			setDivisor(divisor);
		}
	}

	/**
	 * Sets divisor.
	 *
	 * @param divisor
	 */
	public void setDivisor(BigDecimal divisor) {
		this.divisor = divisor;
		doublePart = value.divide(divisor, NumberUtil.DEFAULT_MATHCONTEXT).doubleValue();
		if (Double.isNaN(doublePart) || Double.isInfinite(doublePart)) {
			doublePart = 0d;
		}
	}

	/**
	 * Returns original big decimal value.
	 *
	 * @return
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * Returns double part value.
	 *
	 * @return
	 */
	public double getDouble() {
		return doublePart;
	}

	/**
	 * Returns divrsor.
	 *
	 * @return
	 */
	public BigDecimal getDivisor() {
		return divisor;
	}

	@Override
	public int intValue() {
		return value.intValue();
	}

	@Override
	public long longValue() {
		return value.longValue();
	}

	@Override
	public float floatValue() {
		return value.floatValue();
	}

	@Override
	public double doubleValue() {
		return doublePart;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		return value.compareTo(((BigNumber) o).getValue());
	}

	/**
	 * Returns the minimum between specified value and this.
	 *
	 * @param o
	 * @return
	 */
	public BigNumber min(BigNumber o) {
		return compareTo(o) > 0 ? o : this;
	}

	/**
	 * Returns the maximum between specified value and this.
	 *
	 * @param o
	 * @return
	 */
	public BigNumber max(BigNumber o) {
		return compareTo(o) < 0 ? o : this;
	}

	/**
	 * Returns result of this adding specified value.
	 *
	 * @param num
	 * @return
	 */
	public BigNumber add(BigNumber num) {
		return new BigNumber(value.add(num.getValue(), NumberUtil.DEFAULT_MATHCONTEXT), divisor);
	}

	/**
	 * Returns result of this subtracting specified value.
	 *
	 * @param num
	 * @return
	 */
	public BigNumber subtract(BigNumber num) {
		return new BigNumber(value.subtract(num.getValue(), NumberUtil.DEFAULT_MATHCONTEXT), divisor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value.toString();
	}
}
