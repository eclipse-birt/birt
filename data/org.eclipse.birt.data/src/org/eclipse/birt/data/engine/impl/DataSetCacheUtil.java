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
package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig;
import org.eclipse.birt.data.engine.executor.DataSetCacheConfig.DataSetCacheMode;

/**
 * 
 */
public class DataSetCacheUtil {
	/**
	 * used to get DataSetCacheConfig from all boring options outside
	 * 
	 * @param appContext
	 * @param context
	 * @param session
	 * @param dataSetDesign
	 * @return null if no JVM level data set cache settings
	 * @throws DataException
	 */
	public static DataSetCacheConfig getJVMDataSetCacheConfig(Map appContext, DataEngineContext context,
			IBaseDataSetDesign dataSetDesign) throws DataException {
		String tempDir = context.getTmpdir();
		if (dataSetDesign != null && dataSetDesign instanceof IIncreCacheDataSetDesign) {
			return DataSetCacheConfig.getInstance(DataSetCacheMode.IN_DISK, -1, true, tempDir);
		}
		if (appContext != null) {
			Object option = appContext.get(DataEngine.MEMORY_DATA_SET_CACHE);
			if (option != null) {
				int rowLimit = getIntValueFromString(option);
				if (rowLimit >= 0) {
					return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_MEMORY, rowLimit, null);
				}
				// DtE level memory cache
				return null;
			}

			option = appContext.get(DataEngine.DATA_SET_CACHE_ROW_LIMIT);
			if (option != null) {
				int rowLimit = getIntValueFromString(option);
				return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_DISK, rowLimit, tempDir);
			}
		}

		int cacheOption = context.getCacheOption();
		if (cacheOption == DataEngineContext.CACHE_USE_ALWAYS) {
			return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_DISK, context.getCacheCount(), tempDir);
		} else if (cacheOption == DataEngineContext.CACHE_USE_DISABLE) {
			return null;
		} else if (appContext != null) {
			Object option = appContext.get(DataEngine.DATASET_CACHE_OPTION);
			if (option != null && option.toString().equals("true")) {
				int cacheCount = dataSetDesign.getCacheRowCount();
				if (cacheCount == 0) {
					cacheCount = context.getCacheCount();
				}
				return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_DISK, cacheCount, tempDir);
			}
		}
		return null;
	}

	/**
	 * @param queryExecutionHints
	 * @param dataSetDesign
	 * @param session
	 * @return null if no Dte level data set settings
	 * @throws DataException
	 */
	public static DataSetCacheConfig getDteDataSetCacheConfig(IEngineExecutionHints queryExecutionHints,
			IBaseDataSetDesign dataSetDesign, DataEngineSession session, Map appContext) throws DataException {
		if (queryExecutionHints == null || dataSetDesign == null) {
			return null;
		} else {
			if (queryExecutionHints.needCacheDataSet(dataSetDesign.getName())) {
				Object option = appContext.get(DataEngine.MEMORY_DATA_SET_CACHE);
				if (option != null) {
					int rowLimit = getIntValueFromString(option);
					if (rowLimit < 0) {
						return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_MEMORY, rowLimit, null);
					}
				}
				return DataSetCacheConfig.getInstacne(DataSetCacheMode.IN_DISK, -1, session.getTempDir());
			} else {
				return null;
			}
		}
	}

	/**
	 * 
	 * @param dir
	 */
	public static void deleteFile(String path) {
		if (path == null) {
			return;
		}
		deleteFile(new File(path));
	}

	/**
	 * 
	 * @param dir
	 */
	public static void deleteFile(File f) {
		if (f == null || !FileSecurity.fileExist(f)) {
			return;
		}
		if (FileSecurity.fileIsFile(f)) {
			safeDelete(f);
		} else {
			File[] childFiles = FileSecurity.fileListFiles(f);
			if (childFiles != null) {
				for (File child : childFiles) {
					deleteFile(child);
				}
			}
			safeDelete(f);
		}
	}

	/**
	 * 
	 * @param file
	 */
	private static void safeDelete(File file) {
		if (!FileSecurity.fileDelete(file)) {
			FileSecurity.fileDeleteOnExit(file);
		}
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	private static int getIntValueFromString(Object option) {
		return Integer.valueOf(option.toString()).intValue();
	}
}
