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
package org.eclipse.birt.data.engine.executor;

public class DataSetCacheConfig {
	private DataSetCacheMode cacheMode;

	/**
	 * >0: the max row count of cache <0: unlimited cache capability 0: invalid
	 * value, can't be this value
	 */
	private int countConfig;

	private boolean isIncremental;
	private String cacheDir;

	private DataSetCacheConfig(DataSetCacheMode cacheMode, int countConfig, boolean isIncremental, String cacheDir) {
		assert cacheMode != null && countConfig != 0;
		this.cacheMode = cacheMode;
		this.countConfig = countConfig;
		this.isIncremental = isIncremental;
		this.cacheDir = cacheDir;
	}

	public static DataSetCacheConfig getInstacne(DataSetCacheMode cacheMode, int countConfig, String cacheDir) {
		return getInstance(cacheMode, countConfig, false, cacheDir);
	}

	/**
	 * @param cacheMode
	 * @param countConfig   >0: a limited count cache; <0: unlimited capability
	 *                      cache; =0: do not use cache at all. see
	 *                      <code>setCacheOption( )</code> method in
	 *                      <code>org.eclipse.birt.data.engine.api.DataEngineContext</code>
	 * @param isIncremental
	 * @param cacheDir
	 * @return
	 */
	public static DataSetCacheConfig getInstance(DataSetCacheMode cacheMode, int countConfig, boolean isIncremental,
			String cacheDir) {
		if (cacheMode == null || countConfig == 0) {
			return null;
		}
		return new DataSetCacheConfig(cacheMode, countConfig, isIncremental, cacheDir);
	}

	/**
	 * @return
	 */
	int getCountConfig() {
		return countConfig;
	}

	int getCacheCapability() {
		return countConfig < 0 ? Integer.MAX_VALUE : countConfig;
	}

	String getCacheDir() {
		return cacheDir;
	}

	IDataSetCacheObject createDataSetCacheObject() {
		if (cacheMode == DataSetCacheMode.IN_MEMORY) {
			return new MemoryDataSetCacheObject(getCacheCapability());
		} else if (cacheMode == DataSetCacheMode.IN_DISK) {
			if (isIncremental) {
				return new IncreDataSetCacheObject(cacheDir);
			} else {
				return new DiskDataSetCacheObject(cacheDir, getCacheCapability());
			}
		}
		assert false;
		return null;
	}

	public enum DataSetCacheMode {
		IN_MEMORY, IN_DISK
	}
}
