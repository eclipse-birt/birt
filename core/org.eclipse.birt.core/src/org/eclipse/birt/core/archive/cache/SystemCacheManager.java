/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
public class SystemCacheManager
{

	protected static Logger logger = Logger.getLogger( SystemCacheManager.class
			.getName( ) );

	protected boolean enableSystemCache;
	protected int maxCacheSize;
	protected int systemCacheSize;
	protected int usedCacheSize;

	protected Map<FileCacheManager, Map<Object, CacheEntry>> cachedFiles;
	protected CacheEntry firstEntry;
	protected CacheEntry lastEntry;

	public SystemCacheManager( )
	{
		this.maxCacheSize = 0;
		this.usedCacheSize = 0;
		this.systemCacheSize = 0;
		this.cachedFiles = new HashMap<FileCacheManager, Map<Object, CacheEntry>>( );
		this.firstEntry = new CacheEntry( );
		this.lastEntry = new CacheEntry( );
		this.firstEntry.next = this.lastEntry;
		this.lastEntry.prev = this.firstEntry;
	}

	public void clearCaches( FileCacheManager manager )
	{
		Map<Object, CacheEntry> caches = cachedFiles.remove( manager );
		if ( caches != null )
		{
			Collection<CacheEntry> entries = caches.values( );
			for ( CacheEntry entry : entries )
			{
				entry.prev.next = entry.next;
				entry.next.prev = entry.prev;
			}
			systemCacheSize -= caches.size( );
			caches.clear( );
		}
	}

	public void setMaxCacheSize( int size )
	{
		maxCacheSize = size;
	}

	public void increaseUsedCacheSize( int size )
	{
		usedCacheSize += size;
	}

	public int getUsedCacheSize( )
	{
		return usedCacheSize;
	}

	public void enableSystemCache( boolean enableSystemCache )
	{
		this.enableSystemCache = enableSystemCache;
		if ( !enableSystemCache )
		{
			systemCacheSize = 0;
			cachedFiles.clear( );
			this.firstEntry.next = this.lastEntry;
			this.lastEntry.prev = this.firstEntry;
		}
	}

	public Cacheable getCache( FileCacheManager file, Object key )
	{
		if ( !enableSystemCache )
		{
			return null;
		}
		Map<Object, CacheEntry> caches = cachedFiles.get( file );
		if ( caches != null )
		{
			CacheEntry entry = caches.remove( key );
			if ( entry != null )
			{
				entry.prev.next = entry.next;
				entry.next.prev = entry.prev;
				systemCacheSize--;
				return entry.value;
			}
		}
		return null;
	}

	private void removeCaches( int size )
	{
		for ( int i = 0; i < size; i++ )
		{
			CacheEntry removedEntry = lastEntry.prev;
			assert removedEntry != firstEntry;
			removedEntry.prev.next = removedEntry.next;
			removedEntry.next.prev = removedEntry.prev;
			Map<Object, CacheEntry> caches = cachedFiles
					.get( removedEntry.file );
			assert caches != null;
			caches.remove( removedEntry.value.getCacheKey( ) );
		}
		systemCacheSize -= size;
	}

	public void addCaches( FileCacheManager file, Cacheable[] caches )
	{
		if ( !enableSystemCache )
		{
			return;
		}
		// add to the free list
		int cacheSize = caches.length;
		int maxFreeCacheSize = maxCacheSize - usedCacheSize;
		if ( cacheSize > maxFreeCacheSize )
		{
			cacheSize = maxFreeCacheSize;
		}
		int removeCacheSize = cacheSize + systemCacheSize - maxFreeCacheSize;
		if ( removeCacheSize > 0 )
		{
			removeCaches( removeCacheSize );
		}

		Map<Object, CacheEntry> fileCaches = cachedFiles.get( file );
		if ( fileCaches == null )
		{
			fileCaches = new HashMap<Object, CacheEntry>( );
			cachedFiles.put( file, fileCaches );
		}

		int offset = caches.length - cacheSize;
		for ( int i = 0; i < cacheSize; i++ )
		{
			Cacheable cache = caches[offset + i];
			CacheEntry entry = new CacheEntry( file, cache );
			fileCaches.put( cache.getCacheKey( ), entry );
			entry.next = firstEntry.next;
			firstEntry.next.prev = entry;
			entry.prev = firstEntry;
			firstEntry.next = entry;
		}
		// insert it into the first entry
		systemCacheSize += cacheSize;
	}

	public void addCache( FileCacheManager catalog, Cacheable block )
	{
		addCaches( catalog, new Cacheable[]{block} );
	}

	private static class CacheEntry
	{

		FileCacheManager file;
		Cacheable value;
		CacheEntry prev;
		CacheEntry next;

		CacheEntry( )
		{
		}

		CacheEntry( FileCacheManager catalog, Cacheable value )
		{
			this.file = catalog;
			this.value = value;
		}
	}
}