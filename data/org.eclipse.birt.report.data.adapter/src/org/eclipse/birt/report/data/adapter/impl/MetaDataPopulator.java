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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * Retrieve metaData from resultset property.
 *
 */
public class MetaDataPopulator {

	private static final char RENAME_SEPARATOR = '_';// $NON-NLS-1$
	private static final String UNNAME_PREFIX = "UNNAMED"; //$NON-NLS-1$

	/**
	 * populate all output columns in viewer display. The output columns is
	 * retrieved from oda dataset handles's RESULT_SET_PROP and
	 * COMPUTED_COLUMNS_PROP.
	 * 
	 * @throws BirtException
	 */
	public static IResultMetaData retrieveResultMetaData(DataSetHandle dataSetHandle) throws BirtException {
		List resultSetList = null;
		boolean removeDuplicated = false;
		if (dataSetHandle instanceof OdaDataSetHandle) {
			PropertyHandle handle = dataSetHandle.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP);
			resultSetList = handle.getListValue();
		} else if (dataSetHandle instanceof ScriptDataSetHandle) {
			PropertyHandle handle = dataSetHandle.getPropertyHandle(DataSetHandle.RESULT_SET_HINTS_PROP);
			resultSetList = handle.getListValue();
		} else {
			resultSetList = MetaDataUtil.getResultPropertyHandle(dataSetHandle);
			removeDuplicated = true;

		}

		List computedList = (List) dataSetHandle.getProperty(OdaDataSetHandle.COMPUTED_COLUMNS_PROP);

		List columnMeta = new ArrayList();
		ResultSetColumnDefinition columnDef;
		int count = 0;
		// populate result set columns
		if (resultSetList != null && !resultSetList.isEmpty()) {
			ResultSetColumn resultSetColumn;
			HashSet orgColumnNameSet = new HashSet();
			HashSet uniqueColumnNameSet = new HashSet();

			for (int n = 0; n < resultSetList.size(); n++) {
				orgColumnNameSet.add(((ResultSetColumn) resultSetList.get(n)).getColumnName());
			}

			for (int i = 0; i < resultSetList.size(); i++) {

				resultSetColumn = (ResultSetColumn) resultSetList.get(i);
				uniqueColumnNameSet.add(resultSetColumn.getColumnName());

				columnDef = new ResultSetColumnDefinition(resultSetColumn.getColumnName());
				columnDef.setDataTypeName(resultSetColumn.getDataType());
				columnDef.setDataType(DataAdapterUtil.adaptModelDataType(resultSetColumn.getDataType()));
				if (resultSetColumn.getPosition() != null)
					columnDef.setColumnPosition(resultSetColumn.getPosition().intValue());
				if (resultSetColumn.getNativeDataType() != null)
					columnDef.setNativeDataType(resultSetColumn.getNativeDataType().intValue());

				ColumnHintHandle columnHint = findColumnHint(dataSetHandle, resultSetColumn.getColumnName());
				if (columnHint != null) {
					columnDef.setAlias(columnHint.getAlias());
					columnDef.setLableName(columnHint.getDisplayName());
				}

				columnDef.setComputedColumn(false);
				columnMeta.add(columnDef);
			}
			count += resultSetList.size();

			// populate computed columns
			if (computedList != null) {
				for (int n = 0; n < computedList.size(); n++) {
					orgColumnNameSet.add(((ComputedColumn) computedList.get(n)).getName());
				}

				ComputedColumn computedColumn;

				for (int i = 0; i < computedList.size(); i++) {
					computedColumn = (ComputedColumn) computedList.get(i);

					String columnName = computedColumn.getName();
					if (removeDuplicated) {
						if (uniqueColumnNameSet.contains(columnName))
							continue;
					}
					String uniqueColumnName = getUniqueName(orgColumnNameSet, uniqueColumnNameSet, columnName,
							i + count);
					uniqueColumnNameSet.add(uniqueColumnName);

					if (!uniqueColumnName.equals(columnName)) {
						updateComputedColumn(dataSetHandle, uniqueColumnName, columnName);
					}

					columnDef = new ResultSetColumnDefinition(uniqueColumnName);

					columnDef.setDataTypeName(computedColumn.getDataType());
					columnDef.setDataType(org.eclipse.birt.report.data.adapter.api.DataAdapterUtil
							.adaptModelDataType(computedColumn.getDataType()));
					if (findColumnHint(dataSetHandle, uniqueColumnName) != null) {
						ColumnHintHandle columnHint = findColumnHint(dataSetHandle, uniqueColumnName);
						columnDef.setAlias(columnHint.getAlias());
						columnDef.setLableName(columnHint.getDisplayName());
					}
					columnDef.setComputedColumn(true);
					columnMeta.add(columnDef);
				}
			}
			return new ResultMetaData2(columnMeta);
		}
		return null;
	}

	/**
	 * find column hint according to the columnName
	 * 
	 * @param columnName
	 * @return
	 */
	private static ColumnHintHandle findColumnHint(DataSetHandle dataSetHandle, String columnName) {
		Iterator columnHintIter = dataSetHandle.columnHintsIterator();

		if (columnHintIter != null) {
			while (columnHintIter.hasNext()) {
				ColumnHintHandle modelColumnHint = (ColumnHintHandle) columnHintIter.next();
				if (modelColumnHint.getColumnName().equals(columnName))
					return modelColumnHint;
			}
		}
		return null;
	}

	/**
	 * Whether need to use resultHint, which stands for resultSetHint, columnHint or
	 * both
	 * 
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	public static boolean needsUseResultHint(DataSetHandle dataSetHandle, IResultMetaData metaData)
			throws BirtException {
		boolean hasResultSetHint = false;
		boolean hasColumnHint = false;
		PropertyHandle handle = dataSetHandle.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		if (handle != null)
			hasColumnHint = handle.iterator().hasNext();

		hasResultSetHint = populateResultsetHint(dataSetHandle, metaData);
		if (!hasResultSetHint) {
			hasResultSetHint = checkHandleType(dataSetHandle);
		}
		return hasResultSetHint || hasColumnHint;
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @param metaData
	 * @param columnCount
	 * @param hasResultSetHint
	 * @return
	 * @throws BirtException
	 */
	private static boolean populateResultsetHint(DataSetHandle dataSetHandle, IResultMetaData metaData)
			throws BirtException {
		boolean hasResultSetHint = false;
		int columnCount = 0;
		HashSet orgColumnNameSet = new HashSet();
		HashSet uniqueColumnNameSet = new HashSet();

		if (metaData != null) {
			columnCount = metaData.getColumnCount();
			for (int n = 0; n < columnCount; n++) {
				orgColumnNameSet.add(metaData.getColumnName(n + 1));
			}
		}
		for (int i = 0; i < columnCount; i++) {
			String columnName = metaData.getColumnName(i + 1);
			String uniqueColumnName = getUniqueName(orgColumnNameSet, uniqueColumnNameSet, columnName, i);
			uniqueColumnNameSet.add(uniqueColumnName);

			if (!uniqueColumnName.equals(columnName)) {
				updateModelColumn(dataSetHandle, uniqueColumnName, i + 1);

				if (hasResultSetHint != true)
					hasResultSetHint = true;
			}
		}
		return hasResultSetHint;
	}

	/**
	 * 
	 * @param orgColumnNameSet
	 * @param newColumnNameSet
	 * @param columnName
	 * @param index
	 * @return
	 */
	public static String getUniqueName(HashSet orgColumnNameSet, HashSet newColumnNameSet, String columnName,
			int index) {
		String newColumnName;
		if (columnName == null || columnName.trim().length() == 0 || newColumnNameSet.contains(columnName)) {
			// name conflict or no name,give this column a unique name
			if (columnName == null || columnName.trim().length() == 0)
				newColumnName = UNNAME_PREFIX + RENAME_SEPARATOR + String.valueOf(index + 1);
			else
				newColumnName = columnName + RENAME_SEPARATOR + String.valueOf(index + 1);

			int i = 1;
			while (orgColumnNameSet.contains(newColumnName) || newColumnNameSet.contains(newColumnName)) {
				newColumnName += String.valueOf(RENAME_SEPARATOR) + i;
				i++;
			}
		} else {
			newColumnName = columnName;
		}
		return newColumnName;
	}

	/**
	 * whether need to use result hint
	 * 
	 * @param dataSetHandle
	 * @return
	 */
	private static boolean checkHandleType(DataSetHandle dataSetHandle) {
		if (dataSetHandle instanceof ScriptDataSetHandle)
			return true;
		else if (dataSetHandle instanceof JointDataSetHandle) {
			Iterator iter = ((JointDataSetHandle) dataSetHandle).dataSetsIterator();
			while (iter.hasNext()) {
				DataSetHandle dsHandle = (DataSetHandle) iter.next();
				if (dsHandle != null && dsHandle instanceof ScriptDataSetHandle) {
					return true;
				} else if (dsHandle instanceof JointDataSetHandle) {
					if (checkHandleType(dsHandle))
						return true;
				}
			}
		} else if (dataSetHandle instanceof DerivedDataSetHandle) {
			List handleList = ((DerivedDataSetHandle) dataSetHandle).getInputDataSets();
			for (int i = 0; i < handleList.size(); i++) {
				if (checkHandleType((DataSetHandle) handleList.get(i)))
					return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param ds
	 * @param uniqueColumnName
	 * @param index
	 * @throws BirtException
	 */
	private static void updateModelColumn(DataSetHandle ds, String uniqueColumnName, int index) throws BirtException {
		PropertyHandle resultSetColumns = ds.getPropertyHandle(DataSetHandle.RESULT_SET_PROP);
		if (resultSetColumns == null)
			return;

		// update result set columns
		Iterator iterator = resultSetColumns.iterator();
		while (iterator.hasNext()) {
			ResultSetColumnHandle rsColumnHandle = (ResultSetColumnHandle) iterator.next();
			assert rsColumnHandle.getPosition() != null;
			if (rsColumnHandle.getPosition().intValue() == index) {
				if (rsColumnHandle.getColumnName() != null
						&& !rsColumnHandle.getColumnName().equals(uniqueColumnName)) {
					rsColumnHandle.setColumnName(uniqueColumnName);
				}
				break;
			}
		}
	}

	/**
	 * 
	 * @param ds
	 * @param uniqueColumnName
	 * @param index
	 * @throws BirtException
	 */
	private static void updateComputedColumn(DataSetHandle ds, String uniqueColumnName, String originalName)
			throws BirtException {
		PropertyHandle computedColumn = ds.getPropertyHandle(DataSetHandle.COMPUTED_COLUMNS_PROP);
		if (computedColumn == null)
			return;

		// update result set columns
		Iterator iterator = computedColumn.iterator();
		while (iterator.hasNext()) {
			ComputedColumnHandle compColumnHandle = (ComputedColumnHandle) iterator.next();
			if (compColumnHandle.getName() != null && compColumnHandle.getName().equals(originalName)) {
				compColumnHandle.setName(uniqueColumnName);
			}
		}
	}

}
