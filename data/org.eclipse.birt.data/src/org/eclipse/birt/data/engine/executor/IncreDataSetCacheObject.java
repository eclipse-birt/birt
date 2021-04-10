/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.impl.DataSetCacheUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * This IncreDataSetCacheObject serves for incremental data set cache.
 */
public class IncreDataSetCacheObject implements IDataSetCacheObject {

	public static final String TIMESTAMP_DATA = "timestamp.data";
	public static final String META_DATA = "meta.data";
	public static final String DATA_DATA = "data.data";
	private static final char PATH_SEP = File.separatorChar;
	private String cacheDir;

	/**
	 * 
	 * @param cacheDir
	 * @throws NoSuchAlgorithmException
	 */
	public IncreDataSetCacheObject(String cacheDir) {
		this.cacheDir = cacheDir + PATH_SEP + "DataSetCacheObject_" + this.hashCode();
		FileSecurity.fileMakeDirs(new File(this.cacheDir));
	}

	/**
	 * 
	 * @return String persFolder
	 */
	public String getCacheDir() {
		return this.cacheDir;
	}

	/**
	 * 
	 * @return
	 */
	public File getDataFile() {
		return new File(cacheDir + PATH_SEP + DATA_DATA);
	}

	/**
	 * 
	 * @return
	 */
	public File getMetaFile() {
		return new File(cacheDir + PATH_SEP + META_DATA);
	}

	/**
	 * 
	 * @return
	 */
	public File getTimeStampFile() {
		return new File(cacheDir + PATH_SEP + TIMESTAMP_DATA);
	}

	public boolean isCachedDataReusable(int requiredMaxRowCount) {
		return true;
	}

	public boolean needUpdateCache(int requiredCapability) {
		return true;
	}

	public void release() {
		DataSetCacheUtil.deleteFile(cacheDir);
	}

	public IResultClass getResultClass() {
		return null;
	}
}
