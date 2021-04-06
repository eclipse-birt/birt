/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Implements the merge sort algorithm.
 */
class MergeSortImpl {
	private int dataCountOfUnit;

	private MergeSortUtil mergeSortUtil;
	private MergeTempFileUtil tempFileUtil;
	private List tempRowFiles;

	// The number of temp files which are opend for merge sort at one time is
	// this value, max.
	private final static int maxOpenFile = 500;

	private DataEngineSession session;

	/**
	 * @param dataCountOfUnit
	 * @param mergeSortUtil
	 * @param tempFileUtil
	 * @param tempFiles
	 */
	MergeSortImpl(int dataCountOfUnit, MergeSortUtil mergeSortUtil, MergeTempFileUtil tempFileUtil, List tempRowFiles,
			DataEngineSession session) {
		this.dataCountOfUnit = dataCountOfUnit;
		this.mergeSortUtil = mergeSortUtil;
		this.tempFileUtil = tempFileUtil;
		this.tempRowFiles = tempRowFiles;
		this.session = session;
	}

	/**
	 * Merge sort on units
	 * 
	 * @param dataProvider
	 * @param startIndex
	 * @throws DataException
	 * @throws Exception
	 */
	IRowIterator mergeSortOnUnits() throws IOException, DataException {
		IRowIterator goalFile = null;

		int granularity = 0;
		boolean finish = false;
		do {
			tempFileUtil.newMergeLevel();
			granularity = getMergeGranularity();
			if (granularity == tempRowFiles.size()) {
				goalFile = new MergeSortRowFiles(getSubList(tempRowFiles, 0, tempRowFiles.size() - 1), mergeSortUtil);
				tempRowFiles.clear();
				finish = true;
			} else {
				levelMergeSort(granularity);
			}
		} while (!finish);

		return goalFile;
	}

	/**
	 * @return the granularity of merge unit
	 */
	private int getMergeGranularity() {
		return Math.min(dataCountOfUnit, Math.min(maxOpenFile, tempRowFiles.size()));
	}

	/**
	 * merge rows in temp files to new temp files. The number of new temp files is
	 * (number of old temp files) / granularity.
	 * 
	 * @param sourceFiles
	 * @param targetFile
	 * @throws IOException
	 * @throws DataException
	 */
	private void levelMergeSort(int granularity) throws IOException, DataException {
		int mergeCount = 0;
		List newTempList = new ArrayList();

		RowFile targetFile = null;
		do {
			targetFile = tempFileUtil.newTempFile(0);
			mergeRowFiles(getSubList(tempRowFiles, mergeCount * granularity, (mergeCount + 1) * granularity - 1),
					targetFile);
			newTempList.add(targetFile);
			mergeCount++;
			if (session.getStopSign().isStopped())
				break;
		} while (mergeCount * granularity <= tempRowFiles.size() - 1);

		tempRowFiles.clear();
		tempRowFiles = newTempList;
	}

	/**
	 * Get all the temperary row files.
	 * 
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 */
	private static RowFile[] getSubList(List list, int start, int end) {
		RowFile[] rowFiles = null;
		end = Math.min(end, list.size() - 1);
		rowFiles = new RowFile[end - start + 1];
		for (int i = 0; i < rowFiles.length; i++) {
			rowFiles[i] = (RowFile) (list.get(start + i));
		}
		return rowFiles;
	}

	/**
	 * merge rows in multi files to one file.
	 * 
	 * @param sourceFiles
	 * @param targetFile
	 * @throws IOException
	 * @throws DataException
	 */
	private void mergeRowFiles(RowFile[] sourceFiles, RowFile targetFile) throws IOException, DataException {
		MergeSortRowFiles mergeSortRowSet = new MergeSortRowFiles(sourceFiles, mergeSortUtil);
		IResultObject resultObject = mergeSortRowSet.fetch();
		while (resultObject != null) {
			targetFile.write(resultObject);
			resultObject = mergeSortRowSet.fetch();
		}
		mergeSortRowSet.close();
		targetFile.endWrite();
	}

}
