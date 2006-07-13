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
import java.util.Arrays;
import java.util.Comparator;
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
	
	/**
	 * construction
	 */
	CacheMapManager( )
	{
		this.cacheMap = new HashMap( );
		this.folderUtil = new FolderUtil( );
	}
	
	/**
	 * @return
	 */
	synchronized boolean doesSaveToCache( DataSourceAndDataSet dsAndDs )
	{
		String cacheDirStr = (String) this.cacheMap.get( dsAndDs );
		if ( cacheDirStr != null && new File( cacheDirStr ).exists( ) == true )
		{
			return false;
		}
		else
		{
			this.cacheMap.put( dsAndDs, folderUtil.createSessionTempDir( ) );
			return true;
		}
	}
	
	/**
	 * @param dsAndDs
	 * @return
	 */
	synchronized boolean doesLoadFromCache( DataSourceAndDataSet dsAndDs )
	{
		String cacheDirStr = (String) this.cacheMap.get( dsAndDs );
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
	synchronized void clearCache( DataSourceAndDataSet dsAndDs )
	{
		Object cacheDir = cacheMap.get( dsAndDs );
		if ( cacheDir != null )
		{
			cacheMap.remove( dsAndDs );
			folderUtil.deleteDir( (String) cacheDir );
		}
	}
	
	/**
	 * Reset for test case
	 */
	synchronized void resetForTest( )
	{
		cacheMap = new HashMap( );
		folderUtil = new FolderUtil( );
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
			if ( tempRootDirStr != null )
				return;
			
			// system default temp dir is used
			String tempDirStr = System.getProperty( "java.io.tmpdir" );
			File tempDtEDir = new File( tempDirStr, "BirtDataCache" );
			if ( tempDtEDir.exists( ) == false )
			{
				tempDtEDir.mkdir( );
			}
			else
			{
				File[] sessionsFolder = tempDtEDir.listFiles( );
				for ( int i = 0; i < sessionsFolder.length; i++ )
				{
					File[] oneSessionFolder = sessionsFolder[i].listFiles( );
					for ( int j = 0; j < oneSessionFolder.length; j++ )
					{
						// temp files
						if ( oneSessionFolder[j].isDirectory( ) )
						{
							File[] oneSessionTempFiles = oneSessionFolder[j].listFiles( );
							for ( int k = 0; k < oneSessionTempFiles.length; k++ )
							{
								oneSessionTempFiles[k].delete( );
							}
							oneSessionFolder[j].delete( );
						}
						// goal file
						else
						{
							oneSessionFolder[j].delete( );
						}
					}
					sessionsFolder[i].delete( );
				}
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
			
			String sessionTempDirStr;
			final String prefix = "session_";

			// second create the seesion temp folder
			String[] filesName = new File( tempRootDirStr ).list( );
			Arrays.sort( filesName, new Comparator( ) {

				public int compare( Object o1, Object o2 )
				{
					String f1 = (String) o1;
					String f2 = (String) o2;

					int index1 = f1.indexOf( prefix );
					int index2 = f2.indexOf( prefix );

					if ( index1 < 0 || index2 < 0 )
						return 0;

					Integer i1 = Integer.valueOf( f1.substring( index1
							+ prefix.length( ) ) );
					Integer i2 = Integer.valueOf( f2.substring( index2
							+ prefix.length( ) ) );
					return i1.compareTo( i2 );
				}
			} );

			// find which extension should be used
			int maxIndex = -1;
			for ( int i = filesName.length - 1; i >= 0; i-- )
			{
				int index = filesName[i].indexOf( prefix );
				if ( index == 0 )
				{
					maxIndex = Integer.valueOf( filesName[i].substring( index
							+ prefix.length( ) ) ).intValue( );
					break;
				}
			}
			maxIndex++;

			sessionTempDirStr = tempRootDirStr
					+ File.separator + prefix + maxIndex;
			File file = new File( sessionTempDirStr );
			file.mkdir( );

			return sessionTempDirStr;
		}

		/**
		 * Delete folder
		 * 
		 * @param dirStr
		 */
		private void deleteDir( String dirStr )
		{
			File sessionsFolder = new File( dirStr );
			File[] sessionFiles = sessionsFolder.listFiles( );
			for ( int i = 0; i < sessionFiles.length; i++ )
				sessionFiles[i].delete( );
			sessionsFolder.delete( );
		}
	}
	
}
