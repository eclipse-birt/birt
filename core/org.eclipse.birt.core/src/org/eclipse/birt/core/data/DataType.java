/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.core.data;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

/**
 * Defines constants for BIRT data types
 */
public abstract class DataType {
	public static final int UNKNOWN_TYPE = -1;
	public static final int ANY_TYPE = 0;
	public static final int BOOLEAN_TYPE = 1;
	public static final int INTEGER_TYPE = 2;
	public static final int DOUBLE_TYPE = 3;
	public static final int DECIMAL_TYPE = 4;
	public static final int STRING_TYPE = 5;
	public static final int DATE_TYPE = 6;
	public static final int BLOB_TYPE = 7;
	public static final int BINARY_TYPE = 8;
	public static final int SQL_DATE_TYPE = 9;
	public static final int SQL_TIME_TYPE = 10;
	public static final int JAVA_OBJECT_TYPE = 11;

	private static final String[] names = { "Any", "Boolean", "Integer", "Float", "Decimal", "String", "DateTime",
			"Blob", "Binary", "Date", "Time", "Java Object" };

	public static final String ANY_TYPE_NAME = names[0];
	public static final String BOOLEAN_TYPE_NAME = names[1];
	public static final String INTEGER_TYPE_NAME = names[2];
	public static final String DOUBLE_TYPE_NAME = names[3];
	public static final String DECIMAL_TYPE_NAME = names[4];
	public static final String STRING_TYPE_NAME = names[5];
	public static final String DATE_TYPE_NAME = names[6];
	public static final String BLOB_TYPE_NAME = names[7];
	public static final String BINARY_TYPE_NAME = names[8];
	public static final String SQL_DATE_TYPE_NAME = names[9];
	public static final String SQL_TIME_TYPE_NAME = names[10];
	public static final String OBJECT_TYPE_NAME = names[11];

	private static final Class[] classes = { AnyType.class, Boolean.class, Integer.class, Double.class,
			BigDecimal.class, String.class, Date.class, Blob.class, byte[].class, java.sql.Date.class,
			java.sql.Time.class, Object.class, };

	/**
	 * Gets the description of a data type.
	 * 
	 * @param typeCode Data type enumeration value
	 * @return Textual description of data type. "Unknown" if an undefined data type
	 *         is passed in.
	 */
	public static String getName(int typeCode) {
		if (typeCode < 0 || typeCode >= names.length) {
			return "Unknown";
		}
		return names[typeCode];
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
		return classes[typeCode];
	}

	/**
	 * 
	 * @param objClass
	 * @return
	 */
	public static boolean isValidDataType(Class objClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].equals(objClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Other type can be found in JDK, such as Integer and String, but AnyType have
	 * to be manually created to make it correspond to "Any" name.
	 */
	public static final class AnyType {
	};

}
