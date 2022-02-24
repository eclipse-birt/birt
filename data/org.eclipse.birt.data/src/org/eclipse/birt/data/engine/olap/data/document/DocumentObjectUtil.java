
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * 
 */

public class DocumentObjectUtil {
	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @param value
	 * @throws IOException
	 * @throws DataException
	 */
	public static void writeValue(IDocumentObject documentObject, int[] dataType, Object[] value)
			throws IOException, DataException {
		for (int i = 0; i < dataType.length; i++) {
			writeValue(documentObject, dataType[i], value[i]);
		}
	}

	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @param value
	 * @throws IOException
	 * @throws DataException
	 */
	public static void writeValue(IDocumentObject documentObject, int dataType, Object value)
			throws IOException, DataException {
		try {
			if (value == null) {
				documentObject.writeByte(0);
				return;
			} else {
				documentObject.writeByte(1);
			}
			switch (dataType) {
			case DataType.BOOLEAN_TYPE:
				documentObject.writeBoolean(DataTypeUtil.toBoolean(value).booleanValue());
				break;
			case DataType.INTEGER_TYPE:
				documentObject.writeInt(DataTypeUtil.toInteger(value).intValue());
				break;
			case DataType.DOUBLE_TYPE:
				documentObject.writeDouble(DataTypeUtil.toDouble(value).doubleValue());
				break;
			case DataType.STRING_TYPE:
				documentObject.writeString(DataTypeUtil.toString(value));
				break;
			case DataType.DATE_TYPE:
				documentObject.writeDate(DataTypeUtil.toDate(value));
				break;
			case DataType.BIGDECIMAL_TYPE:
				documentObject.writeBigDecimal(DataTypeUtil.toBigDecimal(value));
				break;
			case DataType.BLOB_TYPE:
				Bytes bytesValue = new Bytes((byte[]) value);
				documentObject.writeBytes(bytesValue);
				break;
			case DataType.BYTES_TYPE:
				documentObject.writeBytes((Bytes) value);
				break;
			case DataType.SQL_DATE_TYPE:
				if (value instanceof java.sql.Date)
					documentObject.writeDate((Date) value);
				else
					documentObject.writeDate(DataTypeUtil.toSqlDate(value));
				break;
			case DataType.SQL_TIME_TYPE:
				if (value instanceof java.sql.Time)
					documentObject.writeDate((Date) value);
				else
					documentObject.writeDate(DataTypeUtil.toSqlTime(value));
				break;
			case DataType.JAVA_OBJECT_TYPE:
				if (value != null && !(value instanceof Serializable)) {
					throw new DataException(ResourceConstants.NOT_SERIALIZABLE_CLASS, value.getClass().getName());
				}
				documentObject.writeObject(value);
				break;
			default:
				assert false;
				break;
			}
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @return
	 * @throws IOException
	 */
	public static Object[] readValue(IDocumentObject documentObject, int[] dataType) throws IOException {
		Object[] result = new Object[dataType.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = readValue(documentObject, dataType[i]);
		}
		return result;
	}

	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @return
	 * @throws IOException
	 */
	public static Object readValue(IDocumentObject documentObject, int dataType) throws IOException {
		byte nullSign = documentObject.readByte();
		if (nullSign == 0) {
			return null;
		}
		switch (dataType) {
		case DataType.BOOLEAN_TYPE:
			return Boolean.valueOf(documentObject.readBoolean());
		case DataType.INTEGER_TYPE:
			return Integer.valueOf(documentObject.readInt());
		case DataType.DOUBLE_TYPE:
			return new Double(documentObject.readDouble());
		case DataType.STRING_TYPE:
			return documentObject.readString();
		case DataType.DATE_TYPE:
			return documentObject.readDate();
		case DataType.BIGDECIMAL_TYPE:
			return documentObject.readBigDecimal();
		case DataType.BLOB_TYPE:
			return documentObject.readBytes().bytesValue();
		case DataType.BYTES_TYPE:
			return documentObject.readBytes();
		case DataType.SQL_DATE_TYPE:
			Date date = documentObject.readDate();
			if (date == null) {
				return null;
			}
			return new java.sql.Date(date.getTime());
		case DataType.SQL_TIME_TYPE:
			Date time = documentObject.readDate();
			if (time == null) {
				return null;
			}
			return new java.sql.Time(time.getTime());
		case DataType.JAVA_OBJECT_TYPE:
			return documentObject.readObject();
		default:
			assert false;
			return null;
		}
	}
}
