/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bson.BSON;
import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb.internal.impl.DriverUtil;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.DocumentsMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.FieldMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IResultSetMetaData for the MongoDB ODA runtime
 * driver.
 */
public class MDbResultSetMetaData implements IResultSetMetaData {
	private Map<String, FieldMetaData> m_resultFieldsMD; // key is full name of result field
	private List<String> m_resultFieldFullNames; // convenient indexed access to field names
	private List<Integer> m_resultFieldDataTypes; // convenient cached access to field native data type
	private DocumentsMetaData m_docsMetaData;
	private boolean m_isAutoFlattening;

	public MDbResultSetMetaData(Iterable<Document> resultCursor, List<String> resultFieldNames,
			boolean isAutoFlattening) {
		// expects caller to have already applied all settings, such as searchLimit,
		// as needed on the resultCursor
		m_docsMetaData = MDbMetaData.getMetaData(resultCursor);
		init(resultFieldNames, isAutoFlattening);
	}

	public MDbResultSetMetaData(Iterable<Document> resultObjs, int searchLimit, List<String> resultFieldNames,
			boolean isAutoFlattening) {
		m_docsMetaData = MDbMetaData.getMetaData(resultObjs, searchLimit);
		init(resultFieldNames, isAutoFlattening);
	}

	private void init(List<String> resultFieldNames, boolean isAutoFlattening) {
		m_resultFieldsMD = mapResultFieldsMetaData(resultFieldNames, m_docsMetaData);
		m_isAutoFlattening = isAutoFlattening;
		if (m_isAutoFlattening)
			m_docsMetaData.setFlattenableFields(m_resultFieldsMD, true);
	}

	private static Map<String, FieldMetaData> mapResultFieldsMetaData(List<String> resultFieldNames,
			DocumentsMetaData fromDocMetaData) {
		if (resultFieldNames == null || resultFieldNames.isEmpty()) // no specific fields identified
		{
			// map all fields in metadata
			return MDbMetaData.flattenFieldsMetaData(fromDocMetaData, null);
		}

		// only get metadata for the specified list of fields
		return MDbMetaData.flattenFieldsMetaData(resultFieldNames, fromDocMetaData);
	}

	private List<String> getFieldFullNames() {
		if (m_resultFieldFullNames == null) {
			if (m_resultFieldsMD == null)
				return Collections.emptyList();
			m_resultFieldFullNames = new ArrayList<String>(m_resultFieldsMD.keySet());
		}
		return m_resultFieldFullNames;
	}

	private List<Integer> getFieldDataTypes() {
		if (m_resultFieldDataTypes == null) {
			if (m_resultFieldsMD == null)
				return Collections.emptyList();
			m_resultFieldDataTypes = new ArrayList<Integer>(m_resultFieldsMD.size());
			for (int i = 0; i < m_resultFieldsMD.size(); i++)
				m_resultFieldDataTypes.add(null); // initialize size
		}
		return m_resultFieldDataTypes;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount() throws OdaException {
		return m_resultFieldsMD.size();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName(int index) throws OdaException {
		int numColumns = getColumnCount();
		if (numColumns == 0 || index <= 0 || index > numColumns)
			return DriverUtil.EMPTY_STRING;

		return getFieldFullNames().get(index - 1); // 1-based position
	}

	int getColumnNumber(String columnName) {
		return getFieldFullNames().indexOf(columnName) + 1; // 1-based position
	}

	private FieldMetaData getColumnMetaData(int index) {
		String fieldName;
		try {
			fieldName = getColumnName(index);
		} catch (OdaException ex) {
			return null;
		}

		return getColumnMetaData(fieldName);
	}

	// For internal use only.
	public FieldMetaData getColumnMetaData(String columnName) {
		if (columnName == null || columnName.isEmpty())
			return null;
		return m_resultFieldsMD.get(columnName);
	}

	// For internal use only.
	public DocumentsMetaData getDocumentsMetaData() {
		return m_docsMetaData;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int index) throws OdaException {
		return getColumnName(index); // default
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType(int index) throws OdaException {
		if (index <= 0 || index > getFieldDataTypes().size())
			throw new OdaException(new IndexOutOfBoundsException());

		// get from cached value, if exists
		Integer nativeDataType = getFieldDataTypes().get(index - 1); // 1-based position
		if (nativeDataType != null)
			return nativeDataType;

		nativeDataType = doGetColumnType(index);
		if (nativeDataType != null)
			getFieldDataTypes().set(index - 1, nativeDataType); // save in cache
		return nativeDataType;
	}

	private int doGetColumnType(int index) throws OdaException {
		FieldMetaData columnMD = getColumnMetaData(index);
		if (columnMD == null) // unknown
			return BSON.STRING; // use default data type

		if (columnMD.hasDocumentDataType())
			return columnMD.getPreferredNativeDataType(m_isAutoFlattening);

		// a child field from a nested document
		if (columnMD.isDescendantOfArrayField()) {
			// If Flatten Nested Collections data set property == "false", i.e.
			// nested array's field values will be concatenated into a single String value
			// in a result set column,
			// flattening of nested array of array (with scalar values) is not supported
			// either, and
			// will be concatenated into a single String value as well.
			if (!m_isAutoFlattening || columnMD.isArrayOfScalarValues())
				return BSON.STRING;

			// flattening of nested collection is supported for only one such field in a
			// document,
			// and is tracked in containing DocumentsMetaData
			String arrayAncestorName = columnMD.getArrayAncestorName();
			if (arrayAncestorName != null && !isFlattenableNestedField(columnMD))
				return BSON.STRING;
		} else if (columnMD.isArrayOfScalarValues()) // top-level array of scalar values
		{
			// if no flattening, or already flattening another field,
			// this array of scalar values will be concatenated to a String value
			if (!m_isAutoFlattening)
				return BSON.STRING;
			String flattenableFieldName = m_docsMetaData.getFlattenableFieldName();
			if (flattenableFieldName != null && !flattenableFieldName.equals(columnMD.getFullName()))
				return BSON.STRING;
		}

		// return own native data type
		return columnMD.getPreferredNativeDataType(m_isAutoFlattening);
	}

	private boolean isFlattenableNestedField(FieldMetaData columnMD) {
		return MDbMetaData.isFlattenableNestedField(columnMD, m_docsMetaData);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnTypeName(
	 * int)
	 */
	public String getColumnTypeName(int index) throws OdaException {
		int nativeTypeCode = getColumnType(index);
		return MongoDBDriver.getNativeDataTypeName(nativeTypeCode);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#
	 * getColumnDisplayLength(int)
	 */
	public int getColumnDisplayLength(int index) throws OdaException {
		return -1; // unknown
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision(int index) throws OdaException {
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getScale(int)
	 */
	public int getScale(int index) throws OdaException {
		return -1;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable(int index) throws OdaException {
		// not all fields have data in each document in a collection, thus can be null
		return IResultSetMetaData.columnNullable;
	}

}
