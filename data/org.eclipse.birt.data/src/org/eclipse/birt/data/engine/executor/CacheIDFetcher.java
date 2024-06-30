/*******************************************************************************
 * Copyright (c) 2004,2012 Actuate Corporation.
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.birt.data.engine.api.DataEngine;

public class CacheIDFetcher {
	private static CacheIDFetcher instance = null;
	private static long idleTime = 3600 * 1000;
	private Map<String, Long> activeCacheIDs;

	public static CacheIDFetcher getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (CacheIDFetcher.class) {
			if (instance != null) {
				return instance;
			}
			instance = new CacheIDFetcher();
			return instance;
		}
	}

	private CacheIDFetcher() {
		this.activeCacheIDs = new java.util.concurrent.ConcurrentHashMap<>();
		Timer timer = new Timer(true);
		TimerTask task = new CacheIDPurgeTimeTask();
		timer.schedule(task, 0, idleTime);
	}

	@SuppressWarnings("rawtypes")
	public String getCacheID(Map appContext) {
		try {
			if (appContext == null) {
				return null;
			}
			// Only apply to memory cache
			Object option = appContext.get(DataEngine.MEMORY_DATA_SET_CACHE);
			if (option == null) {
				return null;
			}
			Object o = appContext.get(DataEngine.QUERY_EXECUTION_SESSION_ID);
			if (o != null) {
				String cacheID = o.toString();
				this.activeCacheIDs.put(cacheID, System.currentTimeMillis());
				return cacheID;
			}
		} catch (Exception e) {
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public boolean enableSampleDataPreivew(Map appContext) {
		try {
			if (appContext == null) {
				return false;
			}
			// Only apply to memory cache
			Object option = appContext.get(DataEngine.MEMORY_DATA_SET_CACHE);
			if (option == null) {
				return false;
			}
			Object o = appContext.get(DataEngine.QUERY_EXECUTION_SESSION_ENABLE_SAMPLEDATAPREVIEW);
			if (o != null) {
				boolean enableSamplePreivew = Boolean.parseBoolean(o.toString());
				return enableSamplePreivew;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private class CacheIDPurgeTimeTask extends TimerTask {

		@Override
		public void run() {
			// Do not synchronize here.
			Set<String> inActiveCacheIDs = new HashSet<>();
			long currentTime = System.currentTimeMillis();
			String[] keyArray = activeCacheIDs.keySet().toArray(new String[] {});

			for (String cacheID : keyArray) {
				long lastAccessTime = activeCacheIDs.get(cacheID);
				if (currentTime - lastAccessTime > idleTime) {
					inActiveCacheIDs.add(cacheID);
				}
			}

			CacheMapManager.clearCache(inActiveCacheIDs);
			for (String cacheID : inActiveCacheIDs) {
				activeCacheIDs.remove(cacheID);
			}
		}

	}
}
