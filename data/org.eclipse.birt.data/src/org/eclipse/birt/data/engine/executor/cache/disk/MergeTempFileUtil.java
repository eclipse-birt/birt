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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;

/**
 * Util to create/delete temp file for merge sort.
 */
class MergeTempFileUtil {
	// temp dir
	private String tempDirStr;

	// result object util
	private ResultObjectUtil resultObjectUtil;

	// record current level and index of temp file. They are used to produce
	// file name for temp file.
	private int curLevel = 0;
	private int curIndex = 0;

	// the prefix of temp file
	private final static String tempFilePrefix = "data";

	/**
	 * @param tempDirStr
	 * @param resultObjectUtil
	 */
	MergeTempFileUtil(String tempDirStr, ResultObjectUtil resultObjectUtil) {
		this.tempDirStr = tempDirStr;
		this.resultObjectUtil = resultObjectUtil;

		File tempDir = new File(tempDirStr);
		if (FileSecurity.fileExist(tempDir) == false)
			FileSecurity.fileMakeDirs(tempDir);
	}

	/**
	 * Get temp file for external sorting, template file is automatic generated
	 * according to passed result row index value.
	 * 
	 * @param index
	 * @return temp file
	 * @throws IOException
	 */
	RowFile newTempFile(int cacheSize) {
		return new RowFile(new File(tempDirStr, tempFilePrefix + "_" + curLevel + "_" + curIndex++), resultObjectUtil,
				cacheSize);
	}

	/**
	 * Start new merge level.
	 * 
	 */
	void newMergeLevel() {
		curLevel++;
		curIndex = 0;
	}

	/**
	 * Delete the temperary dir.
	 * 
	 */
	void clearTempDir() {
		File tempDir = new File(tempDirStr);
		if (FileSecurity.fileExist(tempDir))
			FileSecurity.fileDelete(tempDir);
	}

}
