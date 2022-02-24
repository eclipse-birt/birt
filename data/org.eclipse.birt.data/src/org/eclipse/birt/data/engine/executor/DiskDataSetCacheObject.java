
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
package org.eclipse.birt.data.engine.executor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetCacheUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * The data set cache object which serve for disk based data set cache.
 */
public class DiskDataSetCacheObject implements IDataSetCacheObject {
	//
	private String cacheDir;
	private static Integer count = 0;

	// the most row count this cache can save
	private int cacheCapability;

	/**
	 * 
	 * @param tempFolder
	 * @param appContext
	 * @param parameterHints
	 * @param baseDataSetDesign
	 * @param baseDataSourceDesign
	 */
	public DiskDataSetCacheObject(String cacheDir, int cacheCapability) {
		assert cacheCapability > 0;
		if (cacheDir.endsWith(File.separator)) {
			this.cacheDir = cacheDir + "DataSetCacheObject_" + this.hashCode() + "_" + getCount();
		} else {
			this.cacheDir = cacheDir + File.separator + "DataSetCacheObject_" + this.hashCode() + "_" + getCount();
		}
		FileSecurity.fileMakeDirs(new File(this.cacheDir));

		this.cacheCapability = cacheCapability;
	}

	/**
	 * 
	 * @return
	 */
	private int getCount() {
		synchronized (count) {
			count = (count + 1) % 100000;
			return count.intValue();
		}
	}

	/**
	 * 
	 * @return
	 */
	public File getDataFile() {
		return new File(cacheDir + File.separator + "data.data");
	}

	/**
	 * 
	 * @return
	 */
	public File getMetaFile() {
		return new File(cacheDir + File.separator + "meta.data");
	}

	public boolean isCachedDataReusable(int requiredCapability) {
		assert requiredCapability > 0;
		// Only check if meta data file exists, because empty data file should
		// be still valid and cached.
		// Removed: FileSecurity.fileExist( getDataFile()) &&
		return FileSecurity.fileExist(getMetaFile()) && cacheCapability >= requiredCapability;
	}

	public boolean needUpdateCache(int requiredCapability) {
		return !isCachedDataReusable(requiredCapability);
	}

	public void release() {
		DataSetCacheUtil.deleteFile(cacheDir);
	}

	public IResultClass getResultClass() throws DataException {
		IResultClass rsClass;
		FileInputStream fis1 = null;
		BufferedInputStream bis1 = null;
		try {
			fis1 = FileSecurity.createFileInputStream(getMetaFile());
			bis1 = new BufferedInputStream(fis1);
			IOUtil.readInt(bis1);
			rsClass = new ResultClass(bis1, 0);
			bis1.close();
			fis1.close();

			return rsClass;
		} catch (FileNotFoundException e) {
			throw new DataException(ResourceConstants.DATASETCACHE_LOAD_ERROR, e);
		} catch (IOException e) {
			throw new DataException(ResourceConstants.DATASETCACHE_LOAD_ERROR, e);
		}
	}

	public String getCacheDir() {
		return cacheDir;
	}

}
