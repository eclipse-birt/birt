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

import java.math.BigInteger;

import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.model.data.BigNumberDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.math.MathContext;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.ULocale;

/**
 * This class defines constants and static methods for the number.
 *
 * @since 2.6
 */

public class NumberUtil {

	/**
	 * The default divisor of big number.
	 */
	public static final BigDecimal DEFAULT_DIVISOR = new BigDecimal("1E304"); //$NON-NLS-1$

	/**
	 * The default multiplier of big number.
	 */
	public static final BigDecimal DEFAULT_MULTIPLIER = new BigDecimal("1E-300"); //$NON-NLS-1$

	/**
	 * This value is used for the computation of big number.
	 */
	public static final BigDecimal DOUBLE_MIN = DEFAULT_MULTIPLIER;

	/**
	 * This vlaue is used for the computation of big number.
	 */
	public static final BigDecimal DOUBLE_MAX = DEFAULT_DIVISOR;

	/**
	 * Default math context of the computation of big nubmer.
	 */
	public static final MathContext DEFAULT_MATHCONTEXT = MathContext.DEFAULT;

	/**
	 * Default suffix of big number format.
	 */
	public static final String BIG_DECIMAL_FORMAT_SUFFIX = "E0"; //$NON-NLS-1$

	/**
	 * Returns a default format for big number.
	 *
	 * @param locale
	 * @return
	 */
	public static DecimalFormat getDefaultBigDecimalFormat(ULocale locale) {
		return new DecimalFormat("0.##E0", new DecimalFormatSymbols(locale)); //$NON-NLS-1$
	}

	/**
	 * Checks if specified object is a big number.
	 *
	 * @param value
	 * @return
	 */
	public static boolean isBigNumber(Object value) {
		return (value instanceof BigNumber);
	}

	/**
	 * Checks if specified object is a big decimal.
	 *
	 * @param value
	 * @return
	 */
	public static boolean isBigDecimal(Object value) {
		return value instanceof BigDecimal || value instanceof java.math.BigDecimal;
	}

	/**
	 * Checks if specified object is instance of ibm's BigDecimal.
	 *
	 * @param value
	 * @return
	 */
	public static boolean isIBMBigDecimal(Object value) {
		return value instanceof BigDecimal;
	}

	/**
	 * Checks if specified object is instance of java.math.BigDecimal.
	 *
	 * @param value
	 * @return
	 */
	public static boolean isJavaMathBigDecimal(Object value) {
		return value instanceof java.math.BigDecimal;
	}

	/**
	 * This method compares two number objects.
	 *
	 * @param na first number object.
	 * @param nb second number object.
	 * @return 1 if 1st is greater than 2nd, -1 if 1st is less than 2nd, 0 if 1st
	 *         equals wit 2nd. Null is valid in here.
	 */
	@SuppressWarnings("unchecked")
	public static int compareNumber(Number na, Number nb) {
		if (na == null && nb == null) {
			return 0;
		}
		if (na == null) {
			return -1;
		}
		if (nb == null) {
			return 1;
		}
		if (na instanceof Comparable<?> && nb instanceof Comparable<?>) {
			return ((Comparable<Number>) na).compareTo(nb);
		}
		return Double.valueOf(na.doubleValue()).compareTo(nb.doubleValue());
	}

	/**
	 * This method transform number value into Double or BigDecimal. If specified
	 * number is Double, Float, Integer, Long, Short, Unsigned Int, Unsigned Long,
	 * Byte, Char. It will be transformed as Double. If specified number is
	 * BigDecimal or BigInteger. It will be transformed as BigDecimal.
	 *
	 * @param n
	 * @return
	 */
	public static Number transformNumber(Object n) {
		if (n == null) {
			return null;
		} else if (n instanceof Double) {
			return (Double) n;
		} else if (n instanceof BigDecimal) {
			return (BigDecimal) n;
		} else if (n instanceof java.math.BigDecimal) {
			return new BigDecimal(((java.math.BigDecimal) n).toString());
		} else if (n instanceof BigInteger) {
			return new BigDecimal((BigInteger) n);
		}

		return Methods.asDouble(n);
	}

	/**
	 * This method convert number to Double or BigDecimal types.
	 *
	 * @param n
	 * @return
	 */
	public static Number convertNumber(Object n) {
		if (n == null) {
			return null;
		} else if (n instanceof Double) {
			return (Double) n;
		} else if (n instanceof BigDecimal) {
			return (BigDecimal) n;
		} else if (n instanceof java.math.BigDecimal) {
			return (java.math.BigDecimal) n;
		} else if (n instanceof BigInteger) {
			return new BigDecimal((BigInteger) n);
		} else if (n instanceof NumberDataElement) {
			return ((NumberDataElement) n).getValue();
		} else if (n instanceof BigNumberDataElement) {
			return ((BigNumberDataElement) n).getValue();
		}

		return Methods.asDouble(n);
	}

	/**
	 * This method wraps number as big decimal.
	 *
	 * @param n
	 * @return
	 */
	public static BigDecimal asBigDecimal(Number n) {
		if (n == null) {
			return null;
		} else if (n instanceof BigNumber) {
			return ((BigNumber) n).getValue();
		} else if (n instanceof BigDecimal) {
			return (BigDecimal) n;
		} else if (n instanceof java.math.BigDecimal) {
			return new BigDecimal(((java.math.BigDecimal) n).toString());
		} else if (n instanceof BigInteger) {
			return new BigDecimal(n.toString());
		}

		return BigDecimal.valueOf(n.doubleValue());
	}

	/**
	 * Converts number as java.math.BigDecimal.
	 *
	 * @param n
	 * @return
	 */
	public static java.math.BigDecimal asJavaMathBigDecimal(Number n) {
		if (n == null) {
			return null;
		} else if (n instanceof BigNumber) {
			return new java.math.BigDecimal(((BigNumber) n).getValue().toString());
		} else if (n instanceof BigDecimal) {
			return new java.math.BigDecimal(n.toString());
		} else if (n instanceof java.math.BigDecimal) {
			return (java.math.BigDecimal) n;
		} else if (n instanceof BigInteger) {
			return new java.math.BigDecimal(n.toString());
		}

		return java.math.BigDecimal.valueOf(n.doubleValue());
	}

	/**
	 * This method wraps number as Double.
	 *
	 * @param n
	 * @return
	 */
	public static Double asDouble(Number n) {
		if (n == null) {
			return null;
		} else if (n instanceof Double) {
			return (Double) n;
		} else if (n instanceof BigNumber) {
			return new Double(((BigNumber) n).getValue().doubleValue());
		}

		return new Double(n.doubleValue());
	}

	/**
	 * This method wraps number as big number.
	 *
	 * @param n
	 * @param divisor
	 * @return
	 */
	public static BigNumber asBigNumber(Number n, BigDecimal divisor) {
		if (n == null) {
			return null;
		} else if (n instanceof Double) {
			if (((Double) n).isNaN()) {
				return new BigNumber(BigDecimal.ZERO, divisor);
			}
			return new BigNumber(BigDecimal.valueOf(((Double) n).doubleValue()), divisor);
		} else if (n instanceof BigDecimal) {
			return new BigNumber((BigDecimal) n, divisor);
		} else if (n instanceof java.math.BigDecimal) {
			return new BigNumber(asBigDecimal(n), divisor);
		} else if (n instanceof BigNumber) {
			((BigNumber) n).setDivisor(divisor);
			return (BigNumber) n;
		}

		return new BigNumber(BigDecimal.valueOf(n.doubleValue()), divisor);
	}

	/**
	 * This method removes invalid symbols from specified format pattern for big
	 * number.
	 *
	 * @param pattern
	 * @return
	 */
	public static String adjustBigNumberFormatPattern(String pattern) {
		if (pattern == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char a : pattern.toCharArray()) {
			switch (a) {
			case '#':
			case '.':
			case '0':
			case 'E':
				sb.append(a);
				break;
			}
		}
		return sb.toString();
	}
}
