/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http)){}//www.eclipse.org/legal/epl-v10.html
 *
 * Contributors)){}
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * This class provide the function of compute the size of memory occupied by
 * object
 */
public class SizeOfUtil {
	private static int INTEGER_SIZE = 16;
	private static int DOUBLE_SIZE = 16;
	private static int BIGDECIMAL_SIZE = 200;
	private static int DATE_SIZE = 24;
	private static int TIME_SIZE = 24;
	private static int SQL_DATE_SIZE = 24;
	private static int TIMESTAMP_SIZE = 24;
	private static int STRING_OVERHEAD = 40;
	private static int STRING_SIZE = 40 + ((20 + 1) / 4) * 8; // We can assume String values to average 20 characters
																// each.;

	public static int POINTER_SIZE = 4;
	public static int PRIMITIVE_ARRAY_OVERHEAD = 12;
	public static int OBJECT_OVERHEAD = 8;
	public static int OBJECT_ARRAY_OVERHEAD = 12;

	// field count of result object
	private int fieldCount = 0;
	private boolean[] isfixedSize = null;
	private int[] fieldSize = null;

	static {
		if (System.getProperty("java.version").startsWith("1.5")) {
			BIGDECIMAL_SIZE = 200;
			TIMESTAMP_SIZE = 32;
		}

		Object JVMBit = System.getProperty("sun.arch.data.model");
		if (JVMBit != null) {
			try {
				if (DataTypeUtil.toInteger(JVMBit) == 64) {
					INTEGER_SIZE = 24;
					DOUBLE_SIZE = 24;
					BIGDECIMAL_SIZE = 216;
					DATE_SIZE = 32;
					TIME_SIZE = 32;
					SQL_DATE_SIZE = 32;
					TIMESTAMP_SIZE = 32;
					STRING_OVERHEAD = 56;
					STRING_SIZE = 56 + ((20 + 1) / 4) * 8; // We can assume String values to average 20 characters
															// each.;
					POINTER_SIZE = 8;
					PRIMITIVE_ARRAY_OVERHEAD = 2 * 8 + 4;
					OBJECT_ARRAY_OVERHEAD = 2 * 8 + 8;
				}
			} catch (BirtException e) {

			}
		}

	}

	/**
	 * 
	 * @param resultClass
	 * @throws DataException
	 */
	public SizeOfUtil(IResultClass resultClass) throws DataException {
		fieldCount = resultClass.getFieldCount();
		isfixedSize = new boolean[resultClass.getFieldCount()];
		fieldSize = new int[resultClass.getFieldCount()];

		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			if (isFixedSizeClass(resultClass.getFieldValueClass(i))) {
				fieldSize[i - 1] = sizeOf(resultClass.getFieldValueClass(i));
				isfixedSize[i - 1] = true;
			} else {
				isfixedSize[i - 1] = false;
			}
		}
	}

	/**
	 * Return whether a class is fixed size.
	 * 
	 * @param objectClass
	 * @return
	 */
	private static boolean isFixedSizeClass(Class objectClass) {
		return objectClass.equals(Integer.class) || objectClass.equals(Double.class)
				|| objectClass.equals(BigDecimal.class) || objectClass.equals(Date.class)
				|| objectClass.equals(java.sql.Date.class) || objectClass.equals(Time.class)
				|| objectClass.equals(Timestamp.class);
	}

	/**
	 * Return the size of memory occupied by fixed size class object.
	 * 
	 * @param objectClass
	 * @return
	 */
	private static int sizeOf(Class objectClass) {
		if (objectClass.equals(Integer.class)) {
			return SizeOfUtil.INTEGER_SIZE;
		} else if (objectClass.equals(Double.class)) {
			return SizeOfUtil.DOUBLE_SIZE;
		} else if (objectClass.equals(BigDecimal.class)) {
			return SizeOfUtil.BIGDECIMAL_SIZE;
		} else if (objectClass.equals(Date.class)) {
			return SizeOfUtil.DATE_SIZE;
		} else if (objectClass.equals(Time.class)) {
			return SizeOfUtil.TIME_SIZE;
		} else if (objectClass.equals(Timestamp.class)) {
			return SizeOfUtil.TIMESTAMP_SIZE;
		} else if (objectClass.equals(java.sql.Date.class)) {
			return SizeOfUtil.SQL_DATE_SIZE;
		} else if (objectClass.equals(String.class)) {
			return SizeOfUtil.STRING_SIZE;
		}
		// Normally followed lines will never be arrived.
		return 0;
	}

	/**
	 * Compute the size of memory occupied by result object
	 * 
	 * @param resultObject
	 * @return
	 * @throws DataException
	 */
	public int sizeOf(IResultObject resultObject) throws DataException {
		int returnValue = 0;
		for (int i = 1; i <= fieldCount; i++) {
			if (!isfixedSize[i - 1]) {
				if (resultObject.getFieldValue(i) != null)
					returnValue += sizeOf(resultObject.getFieldValue(i).getClass(), resultObject.getFieldValue(i));
			} else {
				if (resultObject.getFieldValue(i) != null) {
					returnValue += fieldSize[i - 1];
				}
			}
		}
		int fieldsSize = POINTER_SIZE * 2 + 8 + (4 + fieldCount * 4 - 1) / 8 * 8;
		returnValue += POINTER_SIZE * 2 + 8 + (4 + fieldsSize - 1) / 8 * 8;
		return returnValue;
	}

	public static int sizeOf(int dataType) {
		if (dataType == DataType.INTEGER_TYPE) {
			return SizeOfUtil.INTEGER_SIZE;
		} else if (dataType == DataType.DOUBLE_TYPE) {
			return SizeOfUtil.DOUBLE_SIZE;
		} else if (dataType == DataType.SQL_TIME_TYPE) {
			return SizeOfUtil.TIME_SIZE;
		} else if (dataType == DataType.DATE_TYPE) {
			return SizeOfUtil.TIMESTAMP_SIZE;
		} else if (dataType == DataType.SQL_DATE_TYPE) {
			return SizeOfUtil.SQL_DATE_SIZE;
		} else if (dataType == DataType.DECIMAL_TYPE) {
			return SizeOfUtil.BIGDECIMAL_SIZE;
		} else if (dataType == DataType.STRING_TYPE) {
			return SizeOfUtil.STRING_SIZE;
		}
		// Normally followed lines will never be arrived.
		return 0;
	}

	public static int getArraySize(int length) {
		if (length == 0)
			return 0;
		return POINTER_SIZE * 2 + 8 + (POINTER_SIZE + length * 4 - 1) / 8 * 8;
	}

	public static int getObjectSize(int[] dataType) {
		int size = 0;
		for (int i = 0; i < dataType.length; i++) {
			size += SizeOfUtil.sizeOf(dataType[i]);
		}
		size += SizeOfUtil.getArraySize(dataType.length);

		return size;
	}

	/**
	 * Return the size of memory occupied by variable size class object.
	 * 
	 * @param objectClass
	 * @param object
	 * @return
	 */
	public static int sizeOf(Class objectClass, Object object) {
		if (object == null) {
			return 0;
		} else if (objectClass.equals(String.class)) {
			int strLen = ((String) object).length();
			return STRING_OVERHEAD + ((strLen + 1) / 4) * 8;
		} else if (objectClass.equals(byte[].class)) {
			int byteLen = ((byte[]) object).length;
			return POINTER_SIZE * 2 + 8 + (4 + byteLen - 1) / 8 * 8;
		} else {
			return sizeOf(objectClass);
		}
	}

}
