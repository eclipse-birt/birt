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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Calculator for type Boolean. Note that all operands are expected to be
 * converted to Boolean before invoking any operation. Use method
 * getTypedObject() to convert operands to the desired datatype. Nulls are
 * ignored in calculations.
 */

public class BooleanCalculator implements ICalculator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#add(java.lang.Object,
	 * java.lang.Object)
	 */
	public Number add(Object a, Object b) throws DataException {
		if (a == null && b == null)
			return null;
		if (a == null)
			return convertToNumber((Boolean) b);
		if (b == null)
			return convertToNumber((Boolean) a);
		return convertToNumber(((Boolean) a) || ((Boolean) b));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#divide(java.lang.Object,
	 * java.lang.Object)
	 */
	public Number divide(Object dividend, Object divisor) throws DataException {
		if (dividend == null)
			return null;
		if (divisor == null)
			return convertToNumber((Boolean) dividend);
		if (divisor == Boolean.FALSE)
			return null; // division by 0
		return convertToNumber(((Boolean) dividend) && ((Boolean) divisor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#multiply(java.lang.Object,
	 * java.lang.Object)
	 */
	public Number multiply(Object a, Object b) throws DataException {
		if (a == null && b == null)
			return null;
		if (a == null)
			return convertToNumber((Boolean) b);
		if (b == null)
			return convertToNumber((Boolean) a);
		return convertToNumber(((Boolean) a) && ((Boolean) b));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.script.math.ICalculator#safeDivide(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
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
	public Number subtract(Object a, Object b) throws DataException {
		if (a == null && b == null)
			return null;
		if (a == null)
			return convertToNumber((Boolean) b);
		if (b == null)
			return convertToNumber((Boolean) a);
		return convertToNumber(((Boolean) a) ^ ((Boolean) b));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.ICalculator#getTypedObject(java.
	 * lang.Object)
	 */
	public Object getTypedObject(Object obj) throws DataException {
		try {
			return DataTypeUtil.toBoolean(obj);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	private Number convertToNumber(Boolean a) {
		return a == Boolean.TRUE ? BigDecimal.ONE : BigDecimal.ZERO;
	}

}
