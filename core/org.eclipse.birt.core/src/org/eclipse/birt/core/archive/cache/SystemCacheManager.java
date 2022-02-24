/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.core.archive.cache;

import java.util.logging.Logger;

/**
 * The cache is divided into four levels:
 * 
 * System Cache: It is the File Cache: the cache shared by a single archive
 * file. Once the file is closed, the cached data is release. The user can set
 * the max cache used by a single file. Stream Cache: Each opened stream locks
 * at most 4 blocks, 1 data block, 3 FAT block.
 * 
 */
public class SystemCacheManager {

	protected static Logger logger = Logger.getLogger(SystemCacheManager.class.getName());

	protected int maxCacheSize;
	protected int usedCacheSize;
	protected CacheList caches;
	protected boolean enableSystemCache;

	public SystemCacheManager() {
		this(0);
	}

	public SystemCacheManager(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		this.usedCacheSize = 0;
		this.caches = new CacheList();
	}

	public void setMaxCacheSize(int size) {
		maxCacheSize = size;
	}

	void increaseUsedCacheSize(int size) {
		usedCacheSize += size;
	}

	public int getUsedCacheSize() {
		return usedCacheSize;
	}

	void removeCaches(FileCacheManager manager) {
		Cacheable cache = caches.first();
		while (cache != null) {
			Cacheable next = cache.getNext();
			if (cache.manager == manager) {
				caches.remove(cache);
				manager.caches.remove(cache.getCacheKey());
				usedCacheSize--;
			}
			cache = next;
		}
	}

	void removeCache(Cacheable cache) {
		caches.remove(cache);
	}

	void addCaches(Cacheable[] caches) {
		if (maxCacheSize == 0) {
			// remove the cache directly
			for (Cacheable cache : caches) {
				cache.getReferenceCount().set(-2);
				cache.manager.caches.remove(cache.getCacheKey());
			}
		} else {
			for (Cacheable cache : caches) {
				cache.getReferenceCount().set(-1);
				this.caches.add(cache);
			}
			adjustSystemCaches();
		}
	}

	void addCache(Cacheable cache) {
		if (maxCacheSize == 0) {
			// remove the cache directly
			cache.getReferenceCount().set(-2);
			cache.manager.caches.remove(cache.getCacheKey());
		} else {
			cache.getReferenceCount().set(-1);
			caches.add(cache);
			adjustSystemCaches();
		}
	}

	private void adjustSystemCaches() {
		int releaseCacheSize = caches.size() - maxCacheSize;
		if (releaseCacheSize > 0) {
			for (int i = 0; i < releaseCacheSize; i++) {
				Cacheable removed = caches.remove();
				if (removed.getReferenceCount().compareAndSet(-1, -2)) {
					removed.manager.caches.remove(removed.getCacheKey());
					usedCacheSize--;
				}
			}
		}
	}
}
