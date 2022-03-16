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

import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.ICloseListener;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Provide the service of reading/writing objects from one file It makes the
 * reading/writing objects transparent to DiskMergeSort.
 */
class RowFile implements IRowIterator, ICloseListener {
	private File tempFile = null;

	private ResultObjectUtil resultObjectUtil;

	private int readPos = 0;

	private int rowCount = 0;
	private IResultObject[] memoryRowCache = null;

	private DataFileReader dfr = null;
	private DataFileWriter dfw = null;

	/**
	 *
	 * @param file
	 * @param resultObjectUtil
	 * @param cacheSize
	 */
	RowFile(File file, ResultObjectUtil resultObjectUtil, int cacheSize) {
		assert file != null;

		this.tempFile = file;
		this.resultObjectUtil = resultObjectUtil;
		setCacheSize(cacheSize);
		DataEngineThreadLocal.getInstance().getCloseListener().add(this);
	}

	// -------------------------write-----------------------
	/**
	 * Set cache size and initialize cache.
	 *
	 * @param cacheSize
	 */
	private void setCacheSize(int cacheSize) {
		if (cacheSize >= 0) {
			memoryRowCache = new IResultObject[cacheSize];
		}
	}

	/**
	 *
	 * @param resultObject
	 * @throws IOException
	 * @throws DataException
	 */
	void write(IResultObject resultObject) throws IOException, DataException {
		IResultObject[] resultObjects = new IResultObject[1];
		resultObjects[0] = resultObject;
		writeRows(resultObjects, 1);
	}

	/**
	 * Write one object to file.
	 *
	 * @param resultObjects
	 * @param count
	 * @param stopSign
	 * @throws IOException
	 * @throws DataException
	 */
	void writeRows(IResultObject[] resultObjects, int count) throws IOException, DataException {
		int cacheFreeSize = memoryRowCache.length - rowCount;
		if (cacheFreeSize >= count) {
			writeRowsToCache(resultObjects, 0, count);
		} else if (cacheFreeSize > 0) {
			writeRowsToCache(resultObjects, 0, cacheFreeSize);
			writeRowsToFile(resultObjects, cacheFreeSize, count - cacheFreeSize);
		} else {
			writeRowsToFile(resultObjects, 0, count);
		}
	}

	/**
	 * Write objects to cache.
	 *
	 * @param resultObjects
	 * @param count
	 * @throws IOException
	 */
	private void writeRowsToCache(IResultObject[] resultObjects, int from, int count) throws IOException {
		System.arraycopy(resultObjects, from, memoryRowCache, rowCount, count);
		rowCount += count;
	}

	/**
	 * Write objects to file.
	 *
	 * @param resultObjects
	 * @param from
	 * @param count
	 * @throws IOException
	 * @throws DataException
	 */
	private void writeRowsToFile(IResultObject[] resultObjects, int from, int count) throws IOException, DataException {
		if (dfw == null) {
			createWriter();
		}
		dfw.write(getSubArray(resultObjects, from, count), count);
		rowCount += count;
	}

	/**
	 * Get subarray of a object array
	 *
	 * @param resultObjects
	 * @param count
	 * @throws IOException
	 */
	private IResultObject[] getSubArray(IResultObject[] resultObjects, int from, int count) {
		IResultObject[] subArray = new IResultObject[count];
		System.arraycopy(resultObjects, from, subArray, 0, count);
		return subArray;
	}

	/**
	 * Create a instance of DataFileWriter
	 *
	 */
	private void createWriter() {
		dfw = DataFileWriter.newInstance(tempFile, resultObjectUtil);
	}

	/**
	 * End write operation. This mothed must be called before fetching row object.
	 */
	void endWrite() {
		closeWriter();
	}

	/**
	 * Close current writer object
	 */
	private void closeWriter() {
		if (dfw != null) {
			dfw.close();
			dfw = null;
		}
	}

	// -------------------------read------------------------
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#first()
	 */
	@Override
	public void reset() {
		readPos = 0;
		createReader();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#next()
	 */
	@Override
	public IResultObject fetch() throws IOException, DataException {
		IResultObject resultObject = readRowFromCache();
		if (resultObject == null) {
			resultObject = readRowFromFile();
		}

		return resultObject;
	}

	/**
	 * Read one object from cache.
	 *
	 * @return
	 * @throws IOException
	 */
	private IResultObject readRowFromCache() throws IOException {
		if (readPos >= memoryRowCache.length) {
			return null;
		}
		return memoryRowCache[readPos++];
	}

	/**
	 * Read one object from file.
	 *
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IResultObject readRowFromFile() throws IOException, DataException {
		if (readPos >= rowCount) {
			return null;
		}
		if (dfr == null) {
			createReader();
		}
		readPos++;
		return (dfr.read(1))[0];
	}

	/**
	 * Create a instance of DataFileReader
	 *
	 */
	private void createReader() {
		if (dfr != null) {
			dfr.close();
		}

		dfr = DataFileReader.newInstance(tempFile, resultObjectUtil);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.cache.disk.IRowIterator#close()
	 */
	@Override
	public void close() {
		closeWriter();
		closeReader();

		if (tempFile != null) {
			FileSecurity.fileDelete(tempFile);
		}
		memoryRowCache = null;
	}

	/**
	 * Close current reader object
	 */
	private void closeReader() {
		if (dfr != null) {
			dfr.close();
			dfr = null;
		}
	}

}
