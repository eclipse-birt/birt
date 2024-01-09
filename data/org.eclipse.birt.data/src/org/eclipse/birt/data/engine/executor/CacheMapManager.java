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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Manage the cache map
 */
public class CacheMapManager {
	/**
	 * Please notice that we must use static variable here for the sharing of cached
	 * data set would be cross data set session.
	 */
	private static Map<DataSourceAndDataSet, IDataSetCacheObject> JVMLevelCacheMap = Collections
			.synchronizedMap(new HashMap<>());
	private static Map<DataSourceAndDataSet, Integer> lockedDataSetCacheMap = Collections
			.synchronizedMap(new HashMap<>());

	private Map<DataSourceAndDataSet, IDataSetCacheObject> cacheMap;
	// use this field temporarily keep the data set object need to be saved in
	// cache. After the data set result has been cached, saved data set object
	// into cachedMap
	private Map<DataSourceAndDataSet, IDataSetCacheObject> tempDataSetCacheMap;

	private boolean useJVMLevelCache;

	// ensure that JVMLevelCache will be clear when JVM shutdown
	static {
		new ShutdownHook(JVMLevelCacheMap);
	}

	/**
	 * construction
	 */
	CacheMapManager(boolean useJVMLevelCache) {
		this.useJVMLevelCache = useJVMLevelCache;
		if (useJVMLevelCache) {
			cacheMap = JVMLevelCacheMap;
		} else {
			cacheMap = new HashMap<>();
		}
		tempDataSetCacheMap = new HashMap<>();
	}

	/**
	 * @param appContext
	 * @param collection
	 * @param baseDataSetDesign
	 * @param baseDataSourceDesign
	 * @return
	 * @throws DataException
	 */
	boolean doesSaveToCache(DataSourceAndDataSet dsAndDs, DataSetCacheConfig dscc) throws DataException {
		synchronized (cacheMap) {
			IDataSetCacheObject cacheObject = cacheMap.get(dsAndDs);
			if (cacheObject != null) {
				return cacheObject.needUpdateCache(dscc.getCacheCapability());
			}
			if (!tempDataSetCacheMap.containsKey(dsAndDs)) {
				IDataSetCacheObject dsco = dscc.createDataSetCacheObject();
				tempDataSetCacheMap.put(dsAndDs, dsco);
			}
			return true;
		}
	}

	/**
	 * @param dsAndDs
	 * @return
	 */
	boolean doesLoadFromCache(DataSourceAndDataSet dsAndDs, int requiredCapability) {
		synchronized (cacheMap) {
			IDataSetCacheObject cacheObject = cacheMap.get(dsAndDs);
			if (cacheObject != null) {
				boolean reusable = cacheObject.isCachedDataReusable(requiredCapability);
				if (!reusable) {
					if (useJVMLevelCache) {
						synchronized (lockedDataSetCacheMap) {
							if (lockedDataSetCacheMap.containsKey(dsAndDs)) {
								try {
									// waiting for 60s
									lockedDataSetCacheMap.wait(60000);
								} catch (InterruptedException e) {
								}
								lockedDataSetCacheMap.remove(dsAndDs);
							}
						}
					}
					cacheObject.release();
					tempDataSetCacheMap.remove(dsAndDs);
					cacheMap.remove(dsAndDs);
				} else if (this.useJVMLevelCache) {
					if (!lockedDataSetCacheMap.containsKey(dsAndDs)) {
						lockedDataSetCacheMap.put(dsAndDs, 0);
					}
				}
				return reusable;
			}
			return false;
		}
	}

	/**
	 * @return
	 */
	IDataSetCacheObject getSavedCacheObject(DataSourceAndDataSet dsAndDs) {
		synchronized (cacheMap) {
			return tempDataSetCacheMap.get(dsAndDs);
		}
	}

	void saveFinishOnCache(DataSourceAndDataSet dsAndDs, IDataSetCacheObject dsco) {
		synchronized (cacheMap) {
			cacheMap.put(dsAndDs, dsco);
		}
	}

	/**
	 */
	void loadStart(DataSourceAndDataSet dsAndDs) throws DataException {
		if (this.useJVMLevelCache) {
			synchronized (lockedDataSetCacheMap) {
				if (lockedDataSetCacheMap.containsKey(dsAndDs)) {
					Integer count = lockedDataSetCacheMap.get(dsAndDs);
					lockedDataSetCacheMap.put(dsAndDs, count + 1);
				}
			}
		}
	}

	/**
	 */
	void loadFinishOnCache(DataSourceAndDataSet dsAndDs) throws DataException {
		if (this.useJVMLevelCache) {
			synchronized (lockedDataSetCacheMap) {
				if (lockedDataSetCacheMap.containsKey(dsAndDs)) {
					Integer count = lockedDataSetCacheMap.get(dsAndDs);
					if (count <= 1) {
						lockedDataSetCacheMap.remove(dsAndDs);
						lockedDataSetCacheMap.notifyAll();
					} else {
						lockedDataSetCacheMap.put(dsAndDs, count - 1);
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	IDataSetCacheObject getloadedCacheObject(DataSourceAndDataSet dsAndDs) {
		return cacheMap.get(dsAndDs);
	}

	/**
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	void clearCache(DataSourceAndDataSet dsAndDs) {
		List<IDataSetCacheObject> cacheObjects = new ArrayList<>();
		synchronized (cacheMap) {
			Object key = getKey(dsAndDs);
			while (key != null) {
				cacheObjects.add(cacheMap.remove(key));
				tempDataSetCacheMap.remove(key);
				key = getKey(dsAndDs);
			}
		}
		for (int i = 0; i < cacheObjects.size(); i++) {
			IDataSetCacheObject cacheObject = cacheObjects.get(i);
			cacheObject.release();
		}

	}

	/**
	 * Reset for test case
	 */
	void resetForTest() {
		synchronized (this) {
			cacheMap.clear();
			tempDataSetCacheMap.clear();
		}
	}

	/**
	 * Return the cached result metadata featured by the given DataSourceAndDataSet.
	 * Please note that the paramter would have no impact to DataSourceAndDataSet so
	 * that will be omited.
	 *
	 * @param dsAndDs
	 * @return
	 * @throws DataException
	 */
	IResultClass getCachedResultClass(DataSourceAndDataSet dsAndDs) throws DataException {
		IDataSetCacheObject cacheObject = null;
		Object key = getKey(dsAndDs);
		if (key != null) {
			cacheObject = cacheMap.get(key);
		}
		if (cacheObject != null) {
			return cacheObject.getResultClass();
		}
		return null;
	}

	/**
	 *
	 * @param dsAndDs
	 * @return
	 */
	private Object getKey(DataSourceAndDataSet dsAndDs) {
		synchronized (cacheMap) {
			for (Iterator<DataSourceAndDataSet> it = cacheMap.keySet().iterator(); it.hasNext();) {
				DataSourceAndDataSet temp = it.next();
				if (temp.isDataSourceDataSetEqual(dsAndDs, false)) {
					return temp;
				}
			}
			return null;
		}
	}

	public static void clearCache(Set<String> cacheIDs) {
		List<IDataSetCacheObject> removed = new ArrayList<>();

		Object[] keyArray = JVMLevelCacheMap.keySet().toArray(new DataSourceAndDataSet[] {});
		for (Object dsAndDs : keyArray) {
			if (cacheIDs.contains(((DataSourceAndDataSet) dsAndDs).getCacheScopeID())) {
				// here we do not use while clause to avoid thread suspending if
				// cached is not properly closed.
				synchronized (lockedDataSetCacheMap) {
					if (lockedDataSetCacheMap.containsKey(dsAndDs)) {
						try {
							// waiting for 60s
							lockedDataSetCacheMap.wait(60000);
						} catch (InterruptedException e) {
						}
						lockedDataSetCacheMap.remove(dsAndDs);
					}
				}
				IDataSetCacheObject cacheObj = JVMLevelCacheMap.remove(dsAndDs);
				if (cacheObj != null) {
					removed.add(cacheObj);
				}

			}
		}

		for (IDataSetCacheObject dataSetCacheObject : removed) {
			dataSetCacheObject.release();
		}
	}

	void clearCache() {
		List<IDataSetCacheObject> cacheObjects = new ArrayList<>();
		synchronized (cacheMap) {
			for (DataSourceAndDataSet dataSetAndSource : cacheMap.keySet().toArray(new DataSourceAndDataSet[0])) {
				cacheObjects.add(cacheMap.remove(dataSetAndSource));
				tempDataSetCacheMap.remove(dataSetAndSource);
			}
		}
		for (int i = 0; i < cacheObjects.size(); i++) {
			IDataSetCacheObject cacheObject = cacheObjects.get(i);
			cacheObject.release();
		}
	}
}

/**
 * Register shutdown hook on JVM exit to ensure that JVM cache will be cleared
 * correctly.
 *
 *
 */
class ShutdownHook implements Runnable {
	private Map<DataSourceAndDataSet, IDataSetCacheObject> cacheMap;

	ShutdownHook(Map<DataSourceAndDataSet, IDataSetCacheObject> jvmLevelCacheMap) {
		cacheMap = jvmLevelCacheMap;
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	@Override
	public void run() {
		List<IDataSetCacheObject> cacheObjects = new ArrayList<>();
		for (DataSourceAndDataSet dataSetAndSource : cacheMap.keySet().toArray(new DataSourceAndDataSet[0])) {
			cacheObjects.add(cacheMap.remove(dataSetAndSource));
		}
		for (int i = 0; i < cacheObjects.size(); i++) {
			IDataSetCacheObject cacheObject = cacheObjects.get(i);
			cacheObject.release();
		}
	}
}
