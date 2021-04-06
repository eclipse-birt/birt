/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * A draft implementation to convert an object to the specified type
 */
public class DataTypeUtil {
	private static ULocale locale = ULocale.getDefault();

	public static Float toFloatFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			return Float.valueOf(NumberFormat.getInstance(locale).parse(s).floatValue());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static Character toCharFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.charAt(0);
		}
		throw new OdaException(Messages.getString("DataTypeUtil.FailToTransferType", //$NON-NLS-1$
				new Object[] { s, Character.class.getName() }));
	}

	public static Short toShortFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			return Short.valueOf(NumberFormat.getInstance(locale).parse(s).shortValue());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static Byte toByteFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			return Byte.valueOf(NumberFormat.getInstance(locale).parse(s).byteValue());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static Integer toIntegerFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			return Integer.valueOf(NumberFormat.getInstance(locale).parse(s).intValue());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static Double toDoubleFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			return Double.valueOf(NumberFormat.getInstance(locale).parse(s).doubleValue());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static BigDecimal toBigDecimalFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			Number n = NumberFormat.getInstance(locale).parse(s);
			return new BigDecimal(n.toString());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static java.sql.Date toDateFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			Date date = DateFormat.getInstance().parse(s);
			return new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static java.sql.Time toTimeFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			Date date = DateFormat.getInstance().parse(s);
			return new Time(date.getTime());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static java.sql.Timestamp toTimestampFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		try {
			Date date = DateFormat.getInstance().parse(s);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			throw new OdaException(e);
		}
	}

	public static IBlob toBlobFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		throw new OdaException("Failed to conver " + s + "(" + s.getClass() + ") to Blob"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	public static IClob toClobFromString(String s) throws OdaException {
		if (s == null) {
			return null;
		}
		throw new OdaException("Failed to conver " + s + "(" + s.getClass() + ") to Clob"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	public static Boolean toBooleanFromString(String s) throws OdaException {
		if (s == null) {
			return false;
		}
		return !"false".equalsIgnoreCase(s); //$NON-NLS-1$
	}

	public static int toInt(Object o) throws OdaException {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		return toIntegerFromString(o.toString()).intValue();
	}

	public static double toDouble(Object o) throws OdaException {
		if (o == null) {
			return 0;
		}
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		return toDoubleFromString(o.toString()).doubleValue();
	}

	public static BigDecimal toBigDecimal(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof BigDecimal) {
			return (BigDecimal) o;
		}
		return toBigDecimalFromString(o.toString());
	}

	public static java.sql.Date toDate(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof java.sql.Date) {
			return (java.sql.Date) o;
		}
		if (o instanceof Date) {
			return new java.sql.Date(((Date) o).getTime());
		}
		return toDateFromString(o.toString());
	}

	public static Time toTime(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof Time) {
			return (Time) o;
		}
		return toTimeFromString(o.toString());
	}

	public static Timestamp toTimestamp(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof Timestamp) {
			return (Timestamp) o;
		}
		return toTimestampFromString(o.toString());
	}

	public static IBlob toBlob(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof IBlob) {
			return (IBlob) o;
		}
		if (o instanceof java.sql.Blob) {
			return new Blob((java.sql.Blob) o);
		}
		return toBlobFromString(o.toString());
	}

	public static IClob toClob(Object o) throws OdaException {
		if (o == null) {
			return null;
		}
		if (o instanceof IClob) {
			return (IClob) o;
		}
		if (o instanceof java.sql.Clob) {
			return new Clob((java.sql.Clob) o);
		}
		return toClobFromString(o.toString());
	}

	public static boolean toBoolean(Object o) throws OdaException {
		if (o == null) {
			return false;
		}
		if (o instanceof Boolean) {
			return (Boolean) o;
		}
		if (o instanceof String) {
			return toBooleanFromString(o.toString());
		}
		if (o instanceof Integer) {
			return ((Integer) o).intValue() != 0;
		}
		return true;
	}

	public static String toString(Object o) {
		return o == null ? null : o.toString();
	}
}
