/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.core.archive.cache;

import org.junit.Test;

import junit.framework.TestCase;

public class FileCacheManagerTest extends TestCase {
	@Test
	public void testFileCacheManager() {
		FileCacheManager cacheManager = new FileCacheManager(5);
		CachedObject o1 = new CachedObject(cacheManager, 1);
		CachedObject o2 = new CachedObject(cacheManager, 2);
		CachedObject o3 = new CachedObject(cacheManager, 3);
		CachedObject o4 = new CachedObject(cacheManager, 4);
		CachedObject o5 = new CachedObject(cacheManager, 5);
		CachedObject o6 = new CachedObject(cacheManager, 6);

		cacheManager.addCache(o1);
		assertEquals(1, cacheManager.getUsedCacheSize());
		assertEquals(1, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o1.getReferenceCount().get());

		cacheManager.addCache(o2);
		assertEquals(2, cacheManager.getUsedCacheSize());
		assertEquals(2, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o2.getReferenceCount().get());

		cacheManager.addCache(o3);
		cacheManager.addCache(o4);
		cacheManager.addCache(o5);
		cacheManager.addCache(o6);
		assertEquals(6, cacheManager.getUsedCacheSize());
		assertEquals(6, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o2.getReferenceCount().get());

		Cacheable getO1 = cacheManager.getCache(1);
		assertEquals(o1, getO1);
		assertEquals(2, o1.getReferenceCount().get());
		// o1 should not be removed as the refcount = 2
		cacheManager.releaseCache(o1);
		assertEquals(6, cacheManager.getUsedCacheSize());
		assertEquals(6, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o1.getReferenceCount().get());
		// the refcount of o1 decreased to 1, so o1 can be remove now.
		cacheManager.releaseCache(o1);
		assertEquals(5, cacheManager.getUsedCacheSize());
		assertEquals(5, cacheManager.getTotalUsedCacheSize());
		assertEquals(0, o1.getReferenceCount().get());

		cacheManager.releaseCache(o3);
		// lockedsize + freeCaches = 4+1
		assertEquals(5, cacheManager.getUsedCacheSize());
		assertEquals(5, cacheManager.getTotalUsedCacheSize());
		assertEquals(0, o3.getReferenceCount().get());

		Cacheable getO3 = cacheManager.getCache(3);
		assertEquals(o3, getO3);
		// lockedsize + freeCaches = 5+0
		assertEquals(5, cacheManager.getUsedCacheSize());
		assertEquals(5, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o3.getReferenceCount().get());

		cacheManager.releaseCache(o2);
		// lockedsize + freeCaches = 4+1
		assertEquals(5, cacheManager.getUsedCacheSize());
		assertEquals(5, cacheManager.getTotalUsedCacheSize());
		assertEquals(0, o2.getReferenceCount().get());

		CachedObject oldO2 = o2;
		o2 = new CachedObject(cacheManager, 2);
		cacheManager.addCache(o2);
		// lockedsize + freeCaches = 5+0
		assertEquals(5, cacheManager.getUsedCacheSize());
		assertEquals(5, cacheManager.getTotalUsedCacheSize());
		assertEquals(1, o2.getReferenceCount().get());
		assertEquals(0, oldO2.getReferenceCount().get());
	}
}

class CachedObject extends Cacheable {

	CachedObject(FileCacheManager fcm, Integer key) {
		super(fcm, key);
	}
}
