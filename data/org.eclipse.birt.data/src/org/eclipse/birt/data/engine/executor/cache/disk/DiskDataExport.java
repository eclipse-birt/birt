/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * An abstract class for data export to file, which has two sub classes. One is
 * the disk direct output class without any data opertaion, the other is with
 * exteranl data sorting.
 */
abstract class DiskDataExport {
	protected ResultObjectUtil resultObjectUtil;
	protected DataEngineSession session;

	/**
	 * According to the parameter of comparator to generate the instance, which is
	 * disk-based direct output instance or disk-based merge instance.
	 * 
	 * @param infoMap
	 * @param comparator
	 * @param rsMetaData
	 * @return a instance of DataBaseExport
	 */
	static DiskDataExport newInstance(Map infoMap, Comparator comparator, IResultClass rsMetaData,
			ResultObjectUtil resultObjectUtil, DataEngineSession session) {
		DiskDataExport dbExport;
		if (comparator != null)
			dbExport = new DiskSortExport2(infoMap, comparator, resultObjectUtil);
		else
			dbExport = new DiskDirectExport(infoMap, resultObjectUtil);

		dbExport.resultObjectUtil = resultObjectUtil;
		dbExport.session = session;
		return dbExport;
	}

	/**
	 * Export data which is stored in the resultObjects array to disk, which is the
	 * first step of export.
	 * 
	 * @param rs
	 * @param stopSign
	 * @throws IOException, file writer exception
	 */
	public abstract void exportStartDataToDisk(IResultObject[] resultObjects) throws IOException, DataException;

	/**
	 * Export data which is fetched form RowResultSet, which is the second step of
	 * export following the first step of exportStartDataToDisk.
	 * 
	 * @param resultObject
	 * @param rs
	 * @param maxRows
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	public abstract int exportRestDataToDisk(IResultObject resultObject, IRowResultSet rs, int maxRows)
			throws DataException, IOException;

	/**
	 * get a ObjectFileWithCache object for goal file
	 * 
	 * @return
	 */
	public abstract IRowIterator getRowIterator();

	/**
	 * 
	 * @return
	 */
	public abstract void close();

	/**
	 * A util method for sub class
	 * 
	 * @param resultObjects
	 * @param stopSign
	 * @return
	 * @throws IOException
	 */
	protected int innerExportStartData(IResultObject[] resultObjects) throws IOException, DataException {
		outputResultObjects(resultObjects, 0);
		return resultObjects.length;
	}

	/**
	 * A util method for sub class
	 * 
	 * @param resultObject
	 * @param rs
	 * @param dataCountOfUnit
	 * @param stopSign
	 * @throws DataException, fetch data exception
	 * @throws IOException,   file writer exception
	 * @return how much data is exported
	 */
	protected int innerExportRestData(IResultObject resultObject, IRowResultSet rs, int dataCountOfUnit, int maxRows)
			throws DataException, IOException {
		int columnCount = rs.getMetaData().getFieldCount();

		IResultObject[] rowDatas = new IResultObject[dataCountOfUnit];
		rowDatas[0] = resultObject;
		int currDataCount = 1;
		int dataIndex = 1;

		IResultObject odaObject = null;
		while ((odaObject = rs.next()) != null && !session.getStopSign().isStopped()) {
			if (maxRows > 0 && currDataCount > maxRows)
				throw new DataException(ResourceConstants.EXCEED_MAX_DATA_OBJECT_ROWS);

			Object[] ob = new Object[columnCount];
			for (int i = 0; i < columnCount; i++)
				ob[i] = odaObject.getFieldValue(i + 1);

			IResultObject rowData = resultObjectUtil.newResultObject(ob);
			if (currDataCount % dataCountOfUnit == 0) {
				int indexOfUnit = currDataCount / dataCountOfUnit - 1;
				if (indexOfUnit >= 0)
					outputResultObjects(rowDatas, indexOfUnit + 1);
				dataIndex = 0;
			}

			rowDatas[dataIndex++] = rowData;
			currDataCount++;
		}

		// process the last unit
		IResultObject[] rowDatas2 = rowDatas;
		int indexOfUnit = currDataCount / dataCountOfUnit - 1;
		if (currDataCount % dataCountOfUnit != 0) {
			indexOfUnit++;
			int length = currDataCount % dataCountOfUnit;
			rowDatas2 = new IResultObject[length];
			System.arraycopy(rowDatas, 0, rowDatas2, 0, length);
		}
		outputResultObjects(rowDatas2, indexOfUnit + 1);

		return currDataCount;
	}

	/**
	 * Output fetched data to file. When sort is needed, the data will be first
	 * sorted before it is exported.
	 * 
	 * @param resultObjects
	 * @param indexOfUnit
	 * @param stopSign
	 * @throws IOException, file writer exception
	 */
	protected abstract void outputResultObjects(IResultObject[] resultObjects, int indexOfUnit)
			throws IOException, DataException;

}
