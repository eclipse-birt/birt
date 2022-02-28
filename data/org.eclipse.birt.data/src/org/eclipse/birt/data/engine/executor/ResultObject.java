/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.core.data.DataType.AnyType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * <code>ResultObject</code> contains the field values for a given row in the
 * result set.
 */
public class ResultObject implements IResultObject {
	private IResultClass resultClass;
	private Object[] fields;

	public ResultObject(IResultClass resultClass, Object[] fields) {
		// TODO externalize message text
		if (resultClass == null || fields == null) {
			throw new NullPointerException("ResultClass and/or fields" + " should not be null.");
		}

		assert (resultClass.getFieldCount() == fields.length);

		this.resultClass = resultClass;

		try {
			if (resultClass.hasAnyTYpe()) {
				for (int i = 1; i <= resultClass.getFieldCount(); i++) {
					if (resultClass.getFieldValueClass(i).getName().equals(AnyType.class.getName())) {
						if (fields[i - 1] != null) {
							((ResultClass) resultClass).getFieldMetaData(i).setDataType(fields[i - 1].getClass());
						}
					}
				}
			}
			if (resultClass.hasClobOrBlob()) {
				this.fields = convertClobAndBlob(fields, resultClass.getClobFieldIndexes(),
						resultClass.getBlobFieldIndexes());
			} else {
				this.fields = fields;
			}
		} catch (DataException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * Convert Clob type to string and Convert Blob type to byte[]
	 *
	 * @param fields
	 * @throws DataException
	 */
	private Object[] convertClobAndBlob(Object[] fields, int[] clobIndex, int[] blobIndex) throws DataException {

		// computed column has no information of field native type,
		// so a safe approach is by judging the value class.
		for (int i = 0; i < clobIndex.length; i++) {
			if (fields[clobIndex[i]] != null) {
				if (fields[clobIndex[i]] instanceof IClob) {
					fields[clobIndex[i]] = getClobValue((IClob) fields[clobIndex[i]]);
				}
			}
		}

		for (int i = 0; i < blobIndex.length; i++) {
			if (fields[blobIndex[i]] != null) {
				if (fields[blobIndex[i]] instanceof IBlob) {
					fields[blobIndex[i]] = getBlobValue((IBlob) fields[blobIndex[i]]);
				}
			}
		}

		return fields;
	}

	/**
	 * Retrieve the String value for Clob type
	 *
	 * @param clob
	 * @return String value of Clob type
	 * @throws DataException
	 */
	private String getClobValue(IClob clob) throws DataException {
		try {
			int len = (int) clob.length();
			return clob.getSubString(1, len);
		} catch (OdaException e) {
			throw new DataException(ResourceConstants.CLOB_OPEN_ERROR, e);
		}
	}

	/**
	 * Retrieve the byte array value for Blob type
	 *
	 * @param blob
	 * @return byte array value of Blob type
	 * @throws DataException
	 */
	private byte[] getBlobValue(IBlob blob) throws DataException {
		try {
			int len = (int) blob.length();
			return blob.getBytes(1, len); // index is 1-based
		} catch (OdaException e) {
			throw new DataException(ResourceConstants.BLOB_OPEN_ERROR, e);
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getResultClass()
	 */
	@Override
	public IResultClass getResultClass() {
		return resultClass;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(java.lang.
	 * String)
	 */
	@Override
	public Object getFieldValue(String fieldName) throws DataException {
		int fieldIndex = resultClass.getFieldIndex(fieldName);

		if (fieldIndex < 1) {
			throw new DataException(ResourceConstants.INVALID_FIELD_NAME, fieldName);
		}

		return getFieldValue(fieldIndex);
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(int)
	 */
	@Override
	public Object getFieldValue(int fieldIndex) throws DataException {
		return fields[fieldIndex - 1];
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(java.lang.
	 * String, java.lang.Object)
	 */
	@Override
	public void setCustomFieldValue(String fieldName, Object value) throws DataException {
		int idx = resultClass.getFieldIndex(fieldName);
		setCustomFieldValue(idx, value);
	}

	/*
	 * fieldIndex is 1-based
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(int,
	 * java.lang.Object)
	 */
	@Override
	public void setCustomFieldValue(int fieldIndex, Object value) throws DataException {
		if (resultClass.isCustomField(fieldIndex)) {
			fields[fieldIndex - 1] = value;
		} else {
			throw new DataException(ResourceConstants.INVALID_CUSTOM_FIELD_INDEX, Integer.valueOf(fieldIndex));
		}

		if (resultClass.getFieldValueClass(fieldIndex).getName().equals(AnyType.class.getName())) {
			if (value != null) {
				((ResultClass) resultClass).getFieldMetaData(fieldIndex).setDataType(value.getClass());
			}
		}
	}

	/**
	 *
	 * @param index
	 * @throws DataException
	 */
//	private void validateFieldIndex( int index ) throws DataException
//	{
//		if ( index < 1 || index > fields.length )
//			throw new DataException( ResourceConstants.INVALID_FIELD_INDEX,
//					new Integer( index ) );
//	}

	/*
	 * To help with debugging and tracing
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(fields.length * 10);
		for (int i = 0; i < fields.length; i++) {
			if (i > 0) {
				buf.append(',');
			}
			buf.append(fields[i] == null ? "null" : fields[i].toString());
		}
		return buf.toString();
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object ob) {
		if (!(ob instanceof IResultObject)) {
			return false;
		}

		IResultObject ob2 = (IResultObject) ob;

		int fieldCount = this.getResultClass().getFieldCount();
		if (fieldCount != ob2.getResultClass().getFieldCount()) {
			return false;
		}

		for (int i = 0; i < fieldCount; i++) {
			try {
				Object value1 = this.getFieldValue(i + 1);
				Object value2 = ob2.getFieldValue(i + 1);
				if (CompareUtil.compare(value1, value2) != 0) {
					return false;
				}
			} catch (DataException e) {
				return false;
			}
		}

		return true;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < fields.length; i++) {
			result = 37 * result + fields[i].hashCode();
		}
		return result;
	}
}
