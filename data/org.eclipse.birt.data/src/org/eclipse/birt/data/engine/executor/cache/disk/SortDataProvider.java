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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Provide the service of reading/writing objects for DiskMergeSort. As for
 * DiskMergeSort, it only needs to know which position of data is to read and to
 * be written, this class will convert such a request to reading/writing objects
 * from/to acutal file. It makes the reading/writing objects transparent to
 * DiskMergeSort.
 */
class SortDataProvider {
	// data count of one unit
	private int dataCountOfUnit;

	// temp file
	private File tempDir;
	private File goalFile;

	// data file reader/writer
	private DataFileReader dfrArray[];
	private DataFileWriter dfw;

	// cache data
	private IResultObject[][] cachedResultObjects;
	private int[] indexOfCachedRowData;

	// temp file prefix
	private String tempDirStr;
	private final static String tempFilePrefix = "data";

	// sort hint
	public final static int SORT_ITSELF = 0;
	public final static int SORT_MERGE = 1;

	//
	private ResultObjectUtil resultObjectUtil;

	/**
	 * Construction
	 * 
	 * @param dataCountOfUnit
	 * @param tempDirStr
	 * @param goalFileStr
	 */
	SortDataProvider(int dataCountOfUnit, String tempDirStr, String goalFileStr, ResultObjectUtil resultObjectUtil) {
		this.dataCountOfUnit = dataCountOfUnit;
		this.resultObjectUtil = resultObjectUtil;

		tempDir = new File(tempDirStr);
		if (FileSecurity.fileExist(tempDir) == false)
			FileSecurity.fileMakeDirs(tempDir);
		this.tempDirStr = tempDirStr;

		goalFile = new File(goalFileStr);
	}

	/**
	 * Initialize class members value, this function must be called
	 * 
	 * @param countOfUnit
	 */
	void initForMerge(int dataCountOfTotal) {
		int countOfUnit = dataCountOfTotal / dataCountOfUnit;
		if (dataCountOfTotal % dataCountOfUnit != 0)
			countOfUnit++;

		dfrArray = new DataFileReader[countOfUnit];

		cachedResultObjects = new IResultObject[countOfUnit][];
		indexOfCachedRowData = new int[countOfUnit];
		for (int i = 0; i < countOfUnit; i++)
			indexOfCachedRowData[i] = -1;
	}

	/**
	 * Read specified position objects. One point needs to be noticed that in
	 * current algorithm implemenation, the data is read sequentially and then next
	 * begin will only have two possible values, 1:in the scope of last [begin, end)
	 * 2:(last end) +1.
	 * 
	 * @param begin
	 * @param end
	 * @param stopSign
	 * @return ResultObject[]
	 * @throws IOException,  file reader exception
	 * @throws DataException
	 */
	IResultObject[] readData(int begin, int end) throws IOException, DataException {
		if (begin == end)
			return new IResultObject[0];

		// get file reader
		int readerIndex = begin / dataCountOfUnit;
		if (indexOfCachedRowData[readerIndex] == -1) {
			File file = getTempFile(readerIndex);
			if (dfrArray[readerIndex] == null)
				dfrArray[readerIndex] = DataFileReader.newInstance(file, resultObjectUtil);
			else
				dfrArray[readerIndex].setReadFile(file);
		}

		int length = end - begin;
		IResultObject[] sortedData = null;

		int offsetOfBegin = begin - indexOfCachedRowData[readerIndex];
		int cacheLength = cachedResultObjects[readerIndex] != null ? cachedResultObjects[readerIndex].length : 0;
		if (indexOfCachedRowData[readerIndex] == -1 || offsetOfBegin >= cacheLength) {
			// no any loaded data
			cachedResultObjects[readerIndex] = dfrArray[readerIndex].read(length);
			sortedData = cachedResultObjects[readerIndex];
			indexOfCachedRowData[readerIndex] = begin;
		} else {
			int offsetOfEnd = end - indexOfCachedRowData[readerIndex];
			if (offsetOfEnd > cacheLength) {
				// some data is loaded, but others is not
				IResultObject[] tempCachedData = new IResultObject[length];
				int fromBeginCachedLength = indexOfCachedRowData[readerIndex] + cacheLength - begin;
				for (int i = 0; i < fromBeginCachedLength; i++)
					tempCachedData[i] = cachedResultObjects[readerIndex][offsetOfBegin + i];

				int nextReadLength = offsetOfEnd - cacheLength;
				IResultObject[] nextSortData = dfrArray[readerIndex].read(nextReadLength);
				for (int i = 0; i < nextReadLength; i++)
					tempCachedData[fromBeginCachedLength + i] = nextSortData[i];

				cachedResultObjects[readerIndex] = tempCachedData;
				indexOfCachedRowData[readerIndex] = begin;
				offsetOfBegin = begin - indexOfCachedRowData[readerIndex];
			}

			// get data from cache
			sortedData = new IResultObject[length];
			for (int i = 0; i < length; i++)
				sortedData[i] = cachedResultObjects[readerIndex][offsetOfBegin + i];
		}

		return sortedData;
	}

	/**
	 * Write specified position objects. One point needs to be noticed that in
	 * current algorithm implemenation, the data is written sequentially and then
	 * currPos value only has one possible values, that is the (currPos+length) of
	 * latest calling..
	 * 
	 * @param hint,          soring itself/merge sorting
	 * @param currPos,       current result object position
	 * @param resultObjects, the result objects array needs to be written
	 * @param count          how many data of array will be written
	 * @param stopSign
	 * @throws IOException,  file writer exception
	 * @throws DataException
	 */
	void writeData(int hint, int currPos, IResultObject[] resultObjects, int count) throws IOException, DataException {
		if (hint == SortDataProvider.SORT_ITSELF) {
			int writerIndex = currPos / dataCountOfUnit;
			File outputFile = getTempFile(writerIndex);
			if (dfw == null)
				dfw = DataFileWriter.newInstance(outputFile, resultObjectUtil);
			else if (currPos % dataCountOfUnit == 0)
				dfw.setWriteFile(outputFile);

			dfw.write(resultObjects, count);
			dfw.close();
		} else {
			if (currPos == 0)
				dfw.setWriteFile(goalFile);

			dfw.write(resultObjects, count);
		}
	}

	/**
	 * end event, and thnen this class can do clean up task.
	 */
	void end() {
		try {
			for (int i = 0; i < dfrArray.length; i++) {
				dfrArray[i].close();
				File tempFile = getTempFile(i);
				FileSecurity.fileDelete(tempFile);
			}
			FileSecurity.fileDelete(tempDir);
			dfw.close();
		} catch (Exception e) {
			// ignore it
		}

		tempDir = null;
		goalFile = null;

		dfrArray = null;
		dfw = null;

		indexOfCachedRowData = null;
		cachedResultObjects = null;
	}

	/**
	 * Get temp file for external sorting, template file is automatic generated
	 * according to passed result object index value.
	 * 
	 * @param index
	 * @return temp file
	 * @throws IOException
	 */
	private File getTempFile(int index) {
		return new File(tempDirStr, tempFilePrefix + "_" + index);
	}

}
