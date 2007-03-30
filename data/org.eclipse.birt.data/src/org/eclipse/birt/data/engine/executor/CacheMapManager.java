/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Manage the cache map
 */
class CacheMapManager
{
	// map of cache relationship
	//private Map cacheMap;
		
	// folder util instance
	private FolderUtil folderUtil;
	private String tempDir;
	
	private static Integer cacheCounter1 = new Integer(0);
	private static Integer cacheCounter2 = new Integer(0);
	private static Map cacheMap = new HashMap();
	/**
	 * construction
	 */
	CacheMapManager( String tempDir )
	{
		
		this.folderUtil = new FolderUtil( );
		this.tempDir = tempDir;
	}
	
	/**
	 * @return
	 */
	boolean doesSaveToCache( DataSourceAndDataSet dsAndDs, int mode )
	{
		Object cacheObject = null;
		
		synchronized ( cacheMap )
		{
			cacheObject = cacheMap.get( dsAndDs );
		}
		
		if ( cacheObject != null  )
		{
			return needSaveToCache( cacheObject );
		}
		else
		{
			synchronized ( cacheMap )
			{
				cacheObject = (String) cacheMap.get( dsAndDs );
				if ( cacheObject != null )
						
				{
					return needSaveToCache( cacheObject );					
				}
				else
				{
					cacheMap.put( dsAndDs, mode == DataEngineContext.CACHE_MODE_IN_MEMORY?
							(IDataSetCacheObject )new MemoryDataSetCacheObject():
							(IDataSetCacheObject )new DiskDataSetCacheObject(folderUtil.createSessionTempDir( )) );
					return true;
				}
			}
		}
	}

	/**
	 * 
	 * @param cacheObject
	 * @return
	 */
	private boolean needSaveToCache( Object cacheObject )
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			DiskDataSetCacheObject diskCacheObject = (DiskDataSetCacheObject)cacheObject;
			return !( diskCacheObject.getDataFile( ).exists( ) && diskCacheObject.getMetaFile( ).exists( ));
		}else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return ((MemoryDataSetCacheObject)cacheObject).needPopulateResult( );
		}
		return false;
	}
	
	/**
	 * @param dsAndDs
	 * @return
	 */
	boolean doesLoadFromCache( DataSourceAndDataSet dsAndDs )
	{
		Object cacheDirStr = null;
		synchronized ( cacheMap )
		{
			cacheDirStr = cacheMap.get( dsAndDs );
		}
		if ( cacheDirStr != null )
		{
			return !needSaveToCache( cacheDirStr );
		}
		return false;
	}
	
	/**
	 * @return
	 */
	IDataSetCacheObject getCacheObject( DataSourceAndDataSet dsAndDs )
	{
		return (IDataSetCacheObject) cacheMap.get( dsAndDs );
	}
	
	/**
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	void clearCache( DataSourceAndDataSet dsAndDs )
	{
		List cacheDir = new ArrayList( );
		synchronized ( cacheMap )
		{
			while ( getKey( dsAndDs ) != null )
				cacheDir.add( cacheMap.remove( getKey( dsAndDs ) ) );
		}
		for ( int i = 0; i < cacheDir.size( ); i++ )
		{
			if ( cacheDir.get( i ) instanceof DiskDataSetCacheObject )

				// assume the following statement is thread-safe
				folderUtil.deleteDir( ( (DiskDataSetCacheObject) cacheDir.get( i ) ).getTempDir( ) );
		}

	}
	
	/**
	 * Reset for test case
	 */
	void resetForTest( )
	{
		synchronized ( this )
		{
			cacheMap = new HashMap( );
			folderUtil = new FolderUtil( );
		}
	}
	
	/**
	 * Return the cached result metadata featured by the given
	 * DataSourceAndDataSet. Please note that the paramter would have no impact
	 * to DataSourceAndDataSet so that will be omited.
	 * 
	 * @param dsAndDs
	 * @return
	 * @throws DataException
	 */
	IResultClass getCachedResultClass( DataSourceAndDataSet dsAndDs )
			throws DataException
	{
		Object cacheObject = null;
		Object key = getKey( dsAndDs );
		if ( key != null )
		{
			cacheObject = cacheMap.get( key );
			// TODO

		}

		if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return ( (MemoryDataSetCacheObject) cacheObject ).getResultClass( );
		}
		else if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			IResultClass rsClass;
			FileInputStream fis1 = null;
			BufferedInputStream bis1 = null;
			try
			{
				fis1 = new FileInputStream( ( (DiskDataSetCacheObject) cacheObject ).getMetaFile( ) );
				bis1 = new BufferedInputStream( fis1 );
				IOUtil.readInt( bis1 );
				rsClass = new ResultClass( bis1 );
				bis1.close( );
				fis1.close( );

				return rsClass;
			}
			catch ( FileNotFoundException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}

		return null;
	}
	
	/**
	 * 
	 * @param dsAndDs
	 * @return
	 */
	private Object getKey ( DataSourceAndDataSet dsAndDs )
	{
		for ( Iterator it = cacheMap.keySet().iterator(); it.hasNext(); )
		{
			DataSourceAndDataSet temp = ( DataSourceAndDataSet )it.next();
			if( temp.isDataSourceDataSetEqual( dsAndDs, false ) )
			{
				return temp;
			}
			
		}
		return null;
	}
	/**
	 * Folder util class to manager temp folder
	 */
	private class FolderUtil
	{
		// temp root directory
		private String tempRootDirStr = null;

		/**
		 * @return temp root dir directory
		 */
		private void createTempRootDir( )
		{
			if ( tempRootDirStr != null
					&& new File( tempRootDirStr ).exists( ) == true )
				return;
					
			File tempDtEDir = null;
			// system default temp dir is used
			synchronized ( cacheCounter1 )
			{
				tempDtEDir = new File( tempDir, "BirtDataCache"
						+ System.currentTimeMillis( ) + cacheCounter1 );
				cacheCounter1 = new Integer( cacheCounter1.intValue( ) + 1 );
				int x = 0;
				while ( tempDtEDir.exists( ) )
				{
					x++;
					tempDtEDir = new File( tempDir, "BirtDataCache"
							+ System.currentTimeMillis( ) + cacheCounter1 + "_"
							+ x );
				}
				tempDtEDir.mkdir( );
				tempDtEDir.deleteOnExit( );
			}
			
			try
			{
				tempRootDirStr = tempDtEDir.getCanonicalPath( );
			}
			catch ( IOException e )
			{
				// normally this exception will never be thrown
			}
		}

		/**
		 * @return session temp dir
		 */
		private String createSessionTempDir( )
		{
			this.createTempRootDir();
			
			final String prefix = "session_";

			
			// system default temp dir is used
			synchronized ( cacheCounter2 )
			{
				String sessionTempDir = tempRootDirStr
						+ File.separator + prefix + System.currentTimeMillis( )
						+ cacheCounter2;
				cacheCounter2 = new Integer( cacheCounter2.intValue( ) + 1 );
				File sessionDirFile = new File( sessionTempDir );
				int x = 0;
				while ( sessionDirFile.exists( ) )
				{
					x++;
					sessionTempDir =  tempRootDirStr
					+ File.separator + prefix + System.currentTimeMillis( )
					+ cacheCounter2 + "_" + x;
					sessionDirFile = new File( sessionTempDir );
				}
				sessionDirFile.mkdir( );
				sessionDirFile.deleteOnExit( );
				return sessionTempDir;
			}
		}

		/**
		 * Delete folder
		 * 
		 * @param dirStr
		 */
		private void deleteDir( String dirStr )
		{
			File sessionsFolder = new File( dirStr );
			if ( !sessionsFolder.exists( ) )
				return;
			File[] sessionFiles = sessionsFolder.listFiles( );
			for ( int i = 0; i < sessionFiles.length; i++ )
				sessionFiles[i].delete( );
			sessionsFolder.delete( );
		}
	}
}
