/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.aggregation.calculator;

import java.math.BigDecimal;
import java.math.MathContext;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Calculator primarily for type BigDecimal. Note that all operands are expected
 * to be converted to BigDecimal before invoking any operation. Use method
 * getTypedObject() to convert operands to the desired datatype. Nulls are
 * ignored in calculations. NaN and Infinity are NOT supported: method
 * DataTypeUtil.toBigDecimal() used by the calculator converts NaN and Infinity
 * values to null, so check for those conditions in the respective aggregate
 * function if those have to be recognized.
 */

public class BigDecimalCalculator implements ICalculator {
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.math.ICalculator#add(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Number add(Object a, Object b) throws DataException {
		if (a == null && b == null) {
			return null;
		}
		if (a == null) {
			return (BigDecimal) b;
		}
		if (b == null) {
			return (BigDecimal) a;
		}
		return ((BigDecimal) a).add((BigDecimal) b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.math.ICalculator#divide(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Number divide(Object dividend, Object divisor) throws DataException {
		if (dividend == null) {
			return null;
		}
		if (divisor == null) {
			return (BigDecimal) dividend;
		}
		return ((BigDecimal) dividend).divide((BigDecimal) divisor, MathContext.DECIMAL128);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.math.ICalculator#multiply(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Number multiply(Object a, Object b) throws DataException {
		if (a == null && b == null) {
			return null;
		}
		if (a == null) {
			return (BigDecimal) b;
		}
		if (b == null) {
			return (BigDecimal) a;
		}
		return ((BigDecimal) a).multiply((BigDecimal) b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.math.ICalculator#safeDivide(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Number safeDivide(Object dividend, Object divisor, Number ifZero) throws DataException {
		try {
			return divide(dividend, divisor);
		} catch (ArithmeticException e) {
			return ifZero;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.math.ICalculator#subtract(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Number subtract(Object a, Object b) throws DataException {
		if (a == null && b == null) {
			return null;
		}
		if (a == null) {
			return BigDecimal.ZERO.subtract((BigDecimal) b);
		}
		if (b == null) {
			return (BigDecimal) a;
		}
		return ((BigDecimal) a).subtract((BigDecimal) b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.ICalculator#getTypedObject(java.
	 * lang.Object)
	 */
	@Override
	public Object getTypedObject(Object obj) throws DataException {
		try {
			return DataTypeUtil.toBigDecimal(obj);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
