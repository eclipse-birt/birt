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

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class FileCacheManager {

	/**
	 * the cache used by this file.
	 */
	protected ConcurrentHashMap<Object, Cacheable> caches;
	/**
	 * the size of locked cache
	 */
	private int lockedCacheSize;
	/**
	 * the free caches
	 */
	protected CacheList freeCaches;
	/**
	 * the maximum cache should be used in locked and free list
	 */
	private int maxCacheSize;

	private CacheListener listener;

	public FileCacheManager() {
		this(0);
	}

	public FileCacheManager(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		this.lockedCacheSize = 0;
		this.caches = new ConcurrentHashMap<Object, Cacheable>(2);
		this.freeCaches = new CacheList();
	}

	public void setCacheListener(CacheListener listener) {
		this.listener = listener;
	}

	synchronized public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		adjustFreeCaches();
	}

	public int getUsedCacheSize() {
		return lockedCacheSize + freeCaches.size();
	}

	public int getTotalUsedCacheSize() {
		return caches.size();
	}

	protected void adjustFreeCaches() {
		// release the free cache
		if (freeCaches.size() <= 0) {
			return;
		}
		int releasedCacheSize = (lockedCacheSize + freeCaches.size()) - maxCacheSize;
		releasedCacheSize = releasedCacheSize > freeCaches.size() ? freeCaches.size() : releasedCacheSize;
		if (releasedCacheSize <= 0) {
			return;
		}
		for (int i = 0; i < releasedCacheSize; i++) {
			Cacheable freeCache = freeCaches.remove();
			if (listener != null) {
				listener.onCacheRelease(freeCache);
			}
			caches.remove(freeCache.getCacheKey());
		}
	}

	synchronized public void clear() {
		caches.clear();
		lockedCacheSize = 0;
		freeCaches.clear();
	}

	synchronized public void touchAllCaches() {
		if (listener != null) {
			touchAllCaches(this.listener);
		}
	}

	synchronized public void touchAllCaches(CacheListener listener) {
//		System.out.println("--------------start flush------------");
		assert listener != null;
		Cacheable[] entries = caches.values().toArray(new Cacheable[caches.size()]);
		Arrays.sort(entries, new Comparator<Cacheable>() {

			public int compare(Cacheable cache0, Cacheable cache1) {
				if (cache0 == null) {
					if (cache1 == null) {
						return 0;
					}
					return -1;
				}
				if (cache1 == null) {
					return -1;
				}
				Comparable k0 = cache0.getCacheKey();
				Comparable k1 = cache1.getCacheKey();
				return k0.compareTo(k1);
			}
		});
		for (Cacheable cache : entries) {
			if (cache != null) {
				listener.onCacheRelease(cache);
			}
		}
//		System.out.println("--------------end flush------------");
	}

	/**
	 * return the cache object to the system. The object should be added into the
	 * system or it is got from the system.
	 * 
	 * @param cache the cache object.
	 */
	synchronized public void releaseCache(Cacheable cache) {
		assert (cache.getReferenceCount().get() > 0);
		int referenceCount = cache.getReferenceCount().decrementAndGet();
		if (referenceCount > 0) {
			// there still some one locked the cache object, return directly
			return;
		}
		// the lock count must be zero
		assert (referenceCount == 0);
		lockedCacheSize--;
		if (maxCacheSize > 0) {
			// return it to the free list
			freeCaches.add(cache);
			adjustFreeCaches();
		} else {
			if (listener != null) {
				listener.onCacheRelease(cache);
			}
			caches.remove(cache.getCacheKey());
		}
	}

	/**
	 * get the cache from the cache system
	 * 
	 * @param cacheKey
	 * 
	 * @return the cached object
	 */
	synchronized public Cacheable getCache(Object cacheKey) {
		Cacheable cache = caches.get(cacheKey);
		if (cache == null) {
			return null;
		}
		int referenceCount = cache.getReferenceCount().incrementAndGet();
		if (referenceCount > 1) {
			return cache;
		}
		if (referenceCount == 1) {
			freeCaches.remove(cache);
			lockedCacheSize++;
			return cache;
		}
		if (referenceCount == 0) {
			lockedCacheSize++;
			cache.getReferenceCount().set(1);
			return cache;
		}
		return null;
	}

	/**
	 * add a cache object into the cache system.
	 * 
	 * @param cache the cache object to be added.
	 */
	synchronized public void addCache(Cacheable cache) {
		cache.getReferenceCount().set(1);
		Object cacheKey = cache.getCacheKey();

		Cacheable oldCache = caches.get(cacheKey);
		if (oldCache != null) {
			int referenceCount = oldCache.getReferenceCount().get();
			if (referenceCount >= 1) {
				throw new IllegalStateException("Reference count is not zero");
			}
			if (referenceCount == 0) {
				// the cache exist in the free cache
				freeCaches.remove(oldCache);
			}
		}
		caches.put(cacheKey, cache);
		lockedCacheSize++;
		if (maxCacheSize > 0) {
			adjustFreeCaches();
		}
	}
}
