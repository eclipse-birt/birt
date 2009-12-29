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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class FileCacheManager
{

	private SystemCacheManager systemCache;
	private HashMap<Object, CacheEntry> lockedBlocks;
	private LinkedHashMap<Object, CacheEntry> freeBlocks;

	private int maxCacheSize;
	private CacheListener listener;

	public FileCacheManager( )
	{
		this( null, 0 );
	}

	public FileCacheManager( int maxCacheSize )
	{
		this( null, maxCacheSize );
	}

	public FileCacheManager( SystemCacheManager systemCache, int maxCacheSize )
	{
		this.systemCache = systemCache;
		this.maxCacheSize = maxCacheSize;
		this.lockedBlocks = new HashMap<Object, CacheEntry>( );
		this.freeBlocks = new LinkedHashMap<Object, CacheEntry>( );
	}

	public void setCacheListener( CacheListener listener )
	{
		this.listener = listener;
	}

	synchronized public void setMaxCacheSize( int maxCacheSize )
	{
		this.maxCacheSize = maxCacheSize;
		releaseFreeCaches( );
	}

	public int getUsedCacheSize( )
	{
		return lockedBlocks.size( ) + freeBlocks.size( );
	}

	synchronized public void setSystemCacheManager( SystemCacheManager manager )
	{
		if ( systemCache != manager )
		{
			if ( systemCache != null )
			{
				synchronized ( systemCache )
				{
					systemCache.increaseUsedCacheSize( -lockedBlocks.size( )
							- freeBlocks.size( ) );
					systemCache.clearCaches( this );
				}

			}
			if ( manager != null )
			{
				synchronized ( manager )
				{
					manager.increaseUsedCacheSize( lockedBlocks.size( )
							+ freeBlocks.size( ) );
				}
			}
			systemCache = manager;
		}
	}

	protected void releaseFreeCaches( )
	{
		if ( freeBlocks.isEmpty( ) )
		{
			return;
		}
		// release the free cache
		int releasedCacheSize = freeBlocks.size( ) + lockedBlocks.size( )
				- maxCacheSize;
		if ( releasedCacheSize > 0 )
		{
			releasedCacheSize = Math
					.min( releasedCacheSize, freeBlocks.size( ) );

			Cacheable[] caches = new Cacheable[releasedCacheSize];
			Iterator<CacheEntry> iter = freeBlocks.values( ).iterator( );
			for ( int i = 0; i < releasedCacheSize; i++ )
			{
				if ( iter.hasNext( ) )
				{
					caches[i] = ( iter.next( ) ).cache;
					iter.remove( );
				}
			}

			if ( listener != null )
			{
				for ( Cacheable cache : caches )
				{
					listener.onCacheRelease( cache );
				}
			}

			if ( systemCache != null )
			{
				synchronized ( systemCache )
				{
					systemCache.increaseUsedCacheSize( -releasedCacheSize );
					systemCache.addCaches( this, caches );
				}
			}
		}
	}

	synchronized public void clear( )
	{
		if ( systemCache != null )
		{
			synchronized ( systemCache )
			{
				systemCache.increaseUsedCacheSize( -lockedBlocks.size( )
						- freeBlocks.size( ) );
				systemCache.clearCaches( this );
			}
		}
		if ( listener != null )
		{
			for ( CacheEntry cacheEntry : lockedBlocks.values( ) )
			{
				listener.onCacheRelease( cacheEntry.cache );
			}
			for ( CacheEntry cacheEntry : freeBlocks.values( ) )
			{
				listener.onCacheRelease( cacheEntry.cache );

			}
		}
		lockedBlocks.clear( );
		freeBlocks.clear( );
	}

	synchronized public void touchAllCaches( CacheListener listener )
	{
		assert listener != null;
		for ( CacheEntry cacheEntry : lockedBlocks.values( ) )
		{
			listener.onCacheRelease( cacheEntry.cache );
		}
		for ( CacheEntry cacheEntry : freeBlocks.values( ) )
		{
			listener.onCacheRelease( cacheEntry.cache );
		}
	}

	public void releaseCache( Cacheable cache )
	{
		Object cacheKey = cache.getCacheKey( );
		CacheEntry cacheEntry = lockedBlocks.get( cacheKey );
		if ( cacheEntry != null )
		{
			cacheEntry.lock--;
			if ( cacheEntry.lock <= 0 )
			{
				lockedBlocks.remove( cacheKey );
				// we need free the blocks
				if ( maxCacheSize > 0 )
				{
					freeBlocks.put( cacheKey, cacheEntry );
					releaseFreeCaches( );
				}
				else if ( listener != null )
				{
					listener.onCacheRelease( cache );
				}
			}
		}
	}

	public Cacheable getCache( Object cacheKey )
	{
		CacheEntry cacheEntry = lockedBlocks.get( cacheKey );
		if ( cacheEntry == null )
		{
			cacheEntry = freeBlocks.remove( cacheKey );
			if ( cacheEntry != null )
			{
				cacheEntry.lock = 0;
				lockedBlocks.put( cacheKey, cacheEntry );
			}
			else if ( systemCache == null )
			{
				return null;
			}
			else
			{
				synchronized ( systemCache )
				{
					Cacheable cache = systemCache.getCache( this, cacheKey );
					if ( cache == null )
					{
						return null;
					}
					cacheEntry = new CacheEntry( cache );
					lockedBlocks.put( cacheKey, cacheEntry );
					systemCache.increaseUsedCacheSize( 1 );
					releaseFreeCaches( );
				}
			}
		}
		cacheEntry.lock++;
		return cacheEntry.cache;
	}

	public void addCache( Cacheable cache )
	{
		Object cacheKey = cache.getCacheKey( );
		CacheEntry cacheEntry = new CacheEntry( cache );
		cacheEntry.lock++;
		lockedBlocks.put( cacheKey, cacheEntry );
		if ( systemCache != null )
		{
			synchronized ( systemCache )
			{
				systemCache.increaseUsedCacheSize( 1 );
			}
		}
		releaseFreeCaches( );
	}

	private static class CacheEntry
	{

		int lock;
		Cacheable cache;

		CacheEntry( Cacheable cache )
		{
			this.cache = cache;
		}
	}
}
