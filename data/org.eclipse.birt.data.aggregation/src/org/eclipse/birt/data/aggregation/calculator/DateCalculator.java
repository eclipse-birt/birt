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
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Calculator for type Date. Note that all operands are expected to be converted
 * to Date before invoking any operation. Use method getTypedObject() to convert
 * operands to the desired datatype. Operations are performed via
 * BigDecimalCalculator facilities, which performs operations using Long date
 * representation.
 */

public class DateCalculator extends BigDecimalCalculator {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#add(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public Number add(Object a, Object b) throws DataException {
		return (Number) getTypedObject(super.add(a, b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#divide(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Number divide(Object dividend, Object divisor) throws DataException {
		return (Number) getTypedObject(super.divide(dividend, divisor));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#multiply(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Number multiply(Object a, Object b) throws DataException {
		return (Number) getTypedObject(super.multiply(a, b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#safeDivide
	 * (java.lang.Object, java.lang.Object, java.lang.Number)
	 */
	@Override
	public Number safeDivide(Object dividend, Object divisor, Number ifZero) throws DataException {
		return (Number) getTypedObject(super.safeDivide(dividend, divisor, ifZero));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#subtract(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Number subtract(Object a, Object b) throws DataException {
		return (Number) getTypedObject(super.subtract(a, b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.calculator.NumberCalculator#
	 * getTypedObject(java.lang.Object)
	 */
	@Override
	public Object getTypedObject(Object obj) throws DataException {
		try {
			BigDecimal ret = (BigDecimal) super.getTypedObject(obj);
			return new Date(ret.setScale(0, BigDecimal.ROUND_HALF_UP).longValueExact());
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
