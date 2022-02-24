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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * One implemenation of DataBaseExport. This class will read data from data base
 * and export to file with sort operation done.
 */
class DiskSortExport2 extends DiskDataExport {
	private int dataCountOfUnit;
	private int dataCountOfTotal;

	private MergeTempFileUtil tempFileUtil;

	private List currRowFiles;

	// private SortDataProvider dataProvider;
	private MergeSortUtil mergeSortUtil;

	// buffer for merger sort. The lentgh of objectBuffer is dataCountOfUnit
	private IResultObject[] rowBuffer = null;

	// The positions from 0 to rowBufferPtr of buffer are free.
	private int inMemoryPos;

	// the goal file
	private IRowIterator goalRowIterator = null;

	/**
	 * @param dataProvider
	 */
	DiskSortExport2(Map infoMap, Comparator comparator, ResultObjectUtil resultObjectUtil) {
		dataCountOfUnit = Integer.parseInt((String) infoMap.get("dataCountOfUnit"));

		if (dataCountOfUnit < 2) {
			throw new IllegalArgumentException("the dataCountOfUnit of " + dataCountOfUnit + " is less than 2 "
					+ ", and then merge sort on file can not be done");
		}

		rowBuffer = new IResultObject[dataCountOfUnit];

		tempFileUtil = new MergeTempFileUtil((String) (infoMap.get("tempDir")), resultObjectUtil);

		mergeSortUtil = MergeSortUtil.getUtil(comparator);

		this.currRowFiles = new ArrayList();
		this.inMemoryPos = -1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.resultset.DataBaseExport#
	 * exportStartDataToDisk(org.eclipse.birt.data.engine.executor.ResultObject[])
	 */
	public void exportStartDataToDisk(IResultObject[] resultObjects) throws IOException {
		dataCountOfTotal = resultObjects.length;
		System.arraycopy(resultObjects, 0, rowBuffer, 0, resultObjects.length);
		inMemoryPos = this.dataCountOfUnit - 1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#
	 * exportRestDataToDisk(org.eclipse.birt.data.engine.odi.IResultObject,
	 * org.eclipse.birt.data.engine.executor.cache.RowResultSet)
	 */
	public int exportRestDataToDisk(IResultObject resultObject, IRowResultSet rs, int maxRows)
			throws DataException, IOException {
		// sort the raw data to unit
		int dataCountOfRest;
		try {
			dataCountOfRest = innerExportRestData(resultObject, rs, dataCountOfUnit, maxRows);
			dataCountOfTotal += dataCountOfRest;

			MergeSortImpl mergeSortImpl = new MergeSortImpl(this.dataCountOfUnit, this.mergeSortUtil, this.tempFileUtil,
					this.currRowFiles, session);
			this.goalRowIterator = mergeSortImpl.mergeSortOnUnits();
		} catch (IOException ie) {
			for (int i = 0; i < currRowFiles.size(); i++) {
				((RowFile) currRowFiles.get(i)).close();
			}
			throw ie;
		}

		return dataCountOfRest;
	}

	/*
	 * A util method for sub class
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#
	 * innerExportRestData(org.eclipse.birt.data.engine.odi.IResultObject,
	 * org.eclipse.birt.data.engine.executor.cache.IRowResultSet, int)
	 */
	protected int innerExportRestData(IResultObject resultObject, IRowResultSet rs, int dataCountOfUnit, int maxRows)
			throws DataException, IOException {
		addNewRow(resultObject);

		int columnCount = rs.getMetaData().getFieldCount();
		int currDataCount = 1;
		IResultObject odaObject = null;

		while ((odaObject = rs.next()) != null) {
			if (maxRows > 0 && currDataCount > maxRows)
				throw new DataException(ResourceConstants.EXCEED_MAX_DATA_OBJECT_ROWS);
			if (session.getStopSign().isStopped())
				return 0;
			Object[] ob = new Object[columnCount];
			for (int i = 0; i < columnCount; i++)
				ob[i] = odaObject.getFieldValue(i + 1);

			IResultObject rowData = resultObjectUtil.newResultObject(ob);
			addNewRow(rowData);

			currDataCount++;
		}

		processLastUnit();

		return currDataCount;
	}

	/**
	 * @param resultObject
	 * @throws IOException
	 * @throws DataException
	 */
	private void addNewRow(IResultObject resultObject) throws IOException, DataException {
		if (inMemoryPos == dataCountOfUnit - 1) {
			prepareNewTempRowFile(0);
			mergeSortUtil.sortSelf(rowBuffer);
			inMemoryPos = -1;
		}

		inMemoryPos++;
		getCurrTempFile(currRowFiles).write(rowBuffer[inMemoryPos]);

		rowBuffer[inMemoryPos] = resultObject;
	}

	/**
	 * @throws IOException
	 * @throws DataException
	 */
	private void processLastUnit() throws IOException, DataException {
		// Now all the rest rows exist in memory.
		rowBuffer = interchange(rowBuffer, inMemoryPos);
		mergeSortUtil.sortSelf(rowBuffer);

		int cacheSize = 0;
		if (currRowFiles.size() <= dataCountOfUnit)
			cacheSize = dataCountOfUnit - currRowFiles.size();
		prepareNewTempRowFile(cacheSize);

		// Output the rest rows
		inMemoryPos = -1;
		getCurrTempFile(currRowFiles).writeRows(rowBuffer, rowBuffer.length);
		getCurrTempFile(currRowFiles).endWrite();
	}

	/**
	 * To switch the place of rows in array by a postion.
	 * 
	 * @param objectArray
	 * @param position
	 * @return
	 */
	private static IResultObject[] interchange(IResultObject[] objectArray, int position) {
		IResultObject[] tempBuffer = new IResultObject[objectArray.length];
		System.arraycopy(objectArray, position + 1, tempBuffer, 0, objectArray.length - (position + 1));
		System.arraycopy(objectArray, 0, tempBuffer, objectArray.length - (position + 1), (position + 1));
		return tempBuffer;
	}

	/**
	 * End write operation of the last temporary file and create a new temporary
	 * file and initialize row buffer.
	 * 
	 * @param cacheSize
	 */
	private void prepareNewTempRowFile(int cacheSize) {
		if (currRowFiles.size() > 0) {
			RowFile lastRowFile = (RowFile) (currRowFiles.get(currRowFiles.size() - 1));
			lastRowFile.endWrite();
		}

		RowFile rowFile = tempFileUtil.newTempFile(cacheSize);
		currRowFiles.add(rowFile);
	}

	/**
	 * 
	 * @return
	 */
	private static RowFile getCurrTempFile(List files) {
		return (RowFile) (files.get(files.size() - 1));
	}

	/*
	 * @see
	 * org.eclipse.birt.sort4.DataBaseExport#outputRowsUnit(org.eclipse.birt.sort4.
	 * RowData[], int)
	 */
	protected void outputResultObjects(IResultObject[] resultObjects, int indexOfUnit) throws IOException {
	}

	/*
	 * get a iterator on the result rows
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.DataBaseExport#getRowIterator()
	 */
	public IRowIterator getRowIterator() {
		return goalRowIterator;
	}

	/*
	 * close the merge sort row
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#close()
	 */
	public void close() {
		tempFileUtil.clearTempDir();
	}

}
