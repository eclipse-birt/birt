/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.util;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

/**
 *
 */

public class DataType {

	public static final int UNKNOWN_TYPE = org.eclipse.birt.core.data.DataType.UNKNOWN_TYPE;
	public static final int BOOLEAN_TYPE = org.eclipse.birt.core.data.DataType.BOOLEAN_TYPE;
	public static final int INTEGER_TYPE = org.eclipse.birt.core.data.DataType.INTEGER_TYPE;
	public static final int DOUBLE_TYPE = org.eclipse.birt.core.data.DataType.DOUBLE_TYPE;
	public static final int STRING_TYPE = org.eclipse.birt.core.data.DataType.STRING_TYPE;
	public static final int DATE_TYPE = org.eclipse.birt.core.data.DataType.DATE_TYPE;
	public static final int BIGDECIMAL_TYPE = org.eclipse.birt.core.data.DataType.DECIMAL_TYPE;
	public static final int BLOB_TYPE = org.eclipse.birt.core.data.DataType.BLOB_TYPE;
	public static final int BYTES_TYPE = 102;
	public static final int SQL_DATE_TYPE = org.eclipse.birt.core.data.DataType.SQL_DATE_TYPE;
	public static final int SQL_TIME_TYPE = org.eclipse.birt.core.data.DataType.SQL_TIME_TYPE;
	public static final int JAVA_OBJECT_TYPE = org.eclipse.birt.core.data.DataType.JAVA_OBJECT_TYPE;

	private static final String[] names = { "Boolean", "Integer", "Double", "String", "DateTime", "Decimal", "Blob",
			"Bytes", "Date", "Time", "Java Object" };

	private static final int[] typeCodes = { BOOLEAN_TYPE, INTEGER_TYPE, DOUBLE_TYPE, STRING_TYPE, DATE_TYPE,
			BIGDECIMAL_TYPE, BLOB_TYPE, BYTES_TYPE, SQL_DATE_TYPE, SQL_TIME_TYPE, JAVA_OBJECT_TYPE, };

	public static final String BOOLEAN_TYPE_NAME = names[0];
	public static final String INTEGER_TYPE_NAME = names[1];
	public static final String DOUBLE_TYPE_NAME = names[2];
	public static final String STRING_TYPE_NAME = names[3];
	public static final String DATE_TYPE_NAME = names[4];
	public static final String BIGDECIMAL_TYPE_NAME = names[5];
	public static final String BLOB_TYPE_NAME = names[6];
	public static final String BYTES_TYPE_NAME = names[7];
	public static final String SQL_DATE_TYPE_NAME = names[8];
	public static final String SQL_TIME_TYPE_NAMW = names[9];
	public static final String JAVA_OBJECT_TYPE_NAMW = names[10];

	private static final Class[] classes = { Boolean.class, Integer.class, Double.class, String.class, Date.class,
			BigDecimal.class, Blob.class, Bytes.class, java.sql.Date.class, java.sql.Time.class,
			java.lang.Object.class, };

	/**
	 * Gets the description of a data type.
	 *
	 * @param typeCode Data type enumeration value
	 * @return Textual description of data type. "Unknown" if an undefined data type
	 *         is passed in.
	 */
	public static String getName(int typeCode) {
		if (typeCode < 0 || typeCode >= typeCodes.length) {
			return "Unknown";
		}
		for (int i = 0; i < typeCodes.length; i++) {
			if (typeCodes[i] == typeCode) {
				return names[i];
			}
		}
		return null;
	}

	/**
	 * Gets the Java class used to represent the specified data type.
	 *
	 * @return Class for the specified data type. If data type is unknown or ANY,
	 *         returns null.
	 */
	public static Class getClass(int typeCode) {
		if (typeCode < 0 || typeCode >= classes.length) {
			return null;
		}
		for (int i = 0; i < typeCodes.length; i++) {
			if (typeCodes[i] == typeCode) {
				return classes[i];
			}
		}
		return null;
	}

	/**
	 *
	 * @param objClass
	 * @return
	 */
	public static int getDataType(Class objClass) {
		if (objClass.equals(java.sql.Timestamp.class)) {
			return typeCodes[4];
		}
		if (objClass.equals(byte[].class)) {
			return typeCodes[6];
		}
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].equals(objClass)) {
				return typeCodes[i];
			}
		}
		return UNKNOWN_TYPE;
	}

}
