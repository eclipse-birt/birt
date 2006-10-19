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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the cache map
 */
class CacheMapManager
{
	// map of cache relationship
	private Map cacheMap;
		
	// folder util instance
	private FolderUtil folderUtil;
	private String tempDir;
	
	private static Integer cacheCounter1 = new Integer(0);
	private static Integer cacheCounter2 = new Integer(0);
	
	/**
	 * construction
	 */
	CacheMapManager( String tempDir )
	{
		this.cacheMap = new HashMap( );
		this.folderUtil = new FolderUtil( );
		this.tempDir = tempDir;
	}
	
	/**
	 * @return
	 */
	boolean doesSaveToCache( DataSourceAndDataSet dsAndDs )
	{
		String cacheDirStr = null;
		
		synchronized ( this.cacheMap )
		{
			cacheDirStr = (String) this.cacheMap.get( dsAndDs );
		}
		
		if ( cacheDirStr != null && new File( cacheDirStr ).exists( ) == true )
		{
			return false;
		}
		else
		{
			synchronized ( this.cacheMap )
			{
				cacheDirStr = (String) this.cacheMap.get( dsAndDs );
				if ( cacheDirStr != null
						&& new File( cacheDirStr ).exists( ) == true )
				{
					return false;					
				}
				else
				{
					this.cacheMap.put( dsAndDs,
							folderUtil.createSessionTempDir( ) );
					return true;
				}
			}
		}
	}
	
	/**
	 * @param dsAndDs
	 * @return
	 */
	boolean doesLoadFromCache( DataSourceAndDataSet dsAndDs )
	{
		String cacheDirStr = null;
		synchronized ( this.cacheMap )
		{
			cacheDirStr = (String) this.cacheMap.get( dsAndDs );
		}
		if ( cacheDirStr != null && new File( cacheDirStr ).exists( ) == true )
			return true;
		else
			return false;

	}
	
	/**
	 * @return
	 */
	String getSaveFolder( DataSourceAndDataSet dsAndDs )
	{
		return (String) cacheMap.get( dsAndDs );
	}
	
	/**
	 * @return
	 */
	String getLoadFolder( DataSourceAndDataSet dsAndDs )
	{
		return (String) cacheMap.get( dsAndDs );
	}
	
	/**
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	void clearCache( DataSourceAndDataSet dsAndDs )
	{
		Object cacheDir = null;
		synchronized ( this.cacheMap )
		{
			cacheDir = cacheMap.remove( dsAndDs );
		}
		if ( cacheDir != null )
		{
			// assume the following statement is thread-safe
			folderUtil.deleteDir( (String) cacheDir );
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
