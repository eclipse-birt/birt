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

package org.eclipse.birt.core.script;

import java.math.BigDecimal;
import java.math.MathContext;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

/**
 *
 */

public class MathUtil {

	private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

	private enum Operator {
		ADD, SUBTRACT, MULTIPLY, DIVIDE
	}

	/**
	 * add operation.
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws BirtException
	 */
	public static final Number add(Object obj1, Object obj2) throws BirtException {
		return doOp(populateOprand(obj1), populateOprand(obj2), Operator.ADD);
	}

	/**
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws BirtException
	 */
	public static final Number subtract(Object obj1, Object obj2) throws BirtException {
		return doOp(populateOprand(obj1), populateOprand(obj2), Operator.SUBTRACT);
	}

	/**
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws BirtException
	 */
	public static final Number multiply(Object obj1, Object obj2) throws BirtException {
		return doOp(populateOprand(obj1), populateOprand(obj2), Operator.MULTIPLY);
	}

	/**
	 *
	 * @param dividend
	 * @param divisor
	 * @return
	 * @throws BirtException
	 */
	public static final Number divide(Object dividend, Object divisor) throws BirtException {
		return doOp(populateOprand(dividend), populateOprand(divisor), Operator.DIVIDE);

	}

	/**
	 *
	 * @param dividend
	 * @param divisor
	 * @param ifZero
	 * @return
	 * @throws BirtException
	 */
	public static final Object safeDivide(Object dividend, Object divisor, Object ifZero) throws BirtException {
		try {
			Object ret = divide(dividend, divisor);
			if (ret instanceof Double) {
				Double d = (Double) ret;
				return Double.isNaN(d) || Double.isInfinite(d) ? ifZero : ret;
			}
			return ret;
		} catch (ArithmeticException e) {
			return ifZero;
		}
	}

	/**
	 * @param number
	 * @return
	 * @throws BirtException
	 */
	public static final int compareTo0(Object number) throws BirtException {
		return compare(number, 0);
	}

	/**
	 * @param number
	 * @return
	 * @throws BirtException
	 */
	public static final int compare(Object number1, Object number2) throws BirtException {
		assert (number1 != null && number2 != null);

		if (number1 instanceof BigDecimal) {
			if (number2 instanceof BigDecimal) {
				return ((BigDecimal) number1).compareTo((BigDecimal) number2);
			} else {
				return ((BigDecimal) number1).compareTo(new BigDecimal(DataTypeUtil.toDouble(number2)));
			}
		} else {
			Double d1 = DataTypeUtil.toDouble(number1);
			if (number2 instanceof BigDecimal) {
				return (new BigDecimal(DataTypeUtil.toDouble(d1))).compareTo((BigDecimal) number2);
			} else {
				return d1.compareTo(DataTypeUtil.toDouble(number2));
			}
		}
	}

	/**
	 * @param number
	 * @return
	 * @throws BirtException
	 */
	public static final Number abs(Object number) throws BirtException {
		assert (number != null);

		if (number instanceof BigDecimal) {
			return ((BigDecimal) number).abs();
		} else {
			return Math.abs(DataTypeUtil.toDouble(number));
		}
	}

	/**
	 * @param number
	 * @return
	 * @throws BirtException
	 */
	public static final Number negate(Object number) throws BirtException {
		assert (number != null);

		if (number instanceof BigDecimal) {
			return ((BigDecimal) number).negate();
		} else {
			return DataTypeUtil.toDouble(number) * (-1);
		}
	}

	/**
	 * add operation.
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws BirtException
	 */
	public static final Number toNumber(Object obj1) throws BirtException {
		if (obj1 == null) {
			return null;
		}
		if (obj1 instanceof BigDecimal) {
			return (BigDecimal) obj1;
		}
		return DataTypeUtil.toDouble(obj1);
	}

	/**
	 *
	 * @param obj1
	 * @param obj2
	 * @param op
	 * @return
	 * @throws BirtException
	 */
	private static final Number doOp(Object obj1, Object obj2, Operator op) throws BirtException {
		if (obj1 instanceof BigDecimal) {
			return doOp((BigDecimal) obj1, obj2, op);
		}
		if (obj2 instanceof BigDecimal) {
			return doOp(obj1, (BigDecimal) obj2, op);
		}

		return doOp(DataTypeUtil.toDouble(obj1), DataTypeUtil.toDouble(obj2), op);
	}

	/**
	 *
	 * @param op1
	 * @param op2
	 * @param op
	 * @return
	 * @throws BirtException
	 */
	private static final Number doOp(BigDecimal op1, BigDecimal op2, Operator op) throws BirtException {
		if (op == Operator.ADD) {
			return op1.add(op2);
		}
		if (op == Operator.SUBTRACT) {
			return op1.subtract(op2);
		}
		if (op == Operator.MULTIPLY) {
			return op1.multiply(op2);
		}
		if (op == Operator.DIVIDE) {
			return op1.divide(op2, MATH_CONTEXT);
		}
		return null;
	}

	/**
	 *
	 * @param op1
	 * @param op2
	 * @param op
	 * @return
	 * @throws BirtException
	 */
	private static final Number doOp(Object op1, BigDecimal op2, Operator op) throws BirtException {
		return doOp(DataTypeUtil.toBigDecimal(op1), op2, op);
	}

	/**
	 *
	 * @param op1
	 * @param op2
	 * @param op
	 * @return
	 * @throws BirtException
	 */
	private static final Number doOp(BigDecimal op1, Object op2, Operator op) throws BirtException {
		return doOp(op1, DataTypeUtil.toBigDecimal(op2), op);
	}

	/**
	 * @param op
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static Number doOp(Double d1, Double d2, Operator op) {
		if (op == Operator.SUBTRACT) {
			return d1 - d2;
		}
		if (op == Operator.ADD) {
			return d1 + d2;
		}

		if (op == Operator.MULTIPLY) {
			return d1 * d2;
		}

		if (op == Operator.DIVIDE) {
			return d1 / d2;
		}

		return null;
	}

	/**
	 * @param obj
	 * @return
	 */
	private static Object populateOprand(Object obj) {
		return obj != null ? obj : new Double(0);
	}

}
