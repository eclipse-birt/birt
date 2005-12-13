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

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;

/**
 * Cache manager for ODA data set and Scripted data set. Since connection to
 * data set and retrieve data are expensive operations, it is unnecessary to do
 * them again and again when user just want to see the data in the same
 * configuration of data set. The basic idea is when user want to use cache, the
 * data will be saved into cache in the first time and it will be loaded in the
 * later time. Once the configuration of data set is changed, the data needs to
 * be retrieved again.
 * 
 * Please notice the whole procedure: 
 * 1: first check whether data can be loaded from cache 
 * 2.1: if yes, then data will be loaded and check whether data needs to be saved
 *    into cache will be skiped.
 * 2.2: if no, then data will be retrived from data set and then whether saving into
 *      cache will be checked
 * 2.2.1: if yes, then data will be saved into cache
 * 2.2.2: if no, then nothing will be done
 * 
 * There are three possible value of cacheRowCount:
 * 1: -1, cache all data set
 * 2: 0, don't cache
 * 3: >0, cahe the specified value
 * 
 * Here whether data will be loaded from cache can be observed by external caller, but
 * about saving into cache is not.
 */
public class DataSetCacheManager
{
	// map of cache relationship
	private Map cacheMap;

	// use cache for design time?
	private boolean useCache;

	// data set id and its cache count
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private int cacheRowCount;

	// instance
	private static DataSetCacheManager cacheManager;

	private FolderUtil folderUtil;

	/**
	 * @return unique instance
	 */
	public static DataSetCacheManager getInstance( )
	{
		if ( cacheManager == null )
			cacheManager = new DataSetCacheManager( );

		return cacheManager;
	}

	/**
	 * Construction
	 */
	private DataSetCacheManager( )
	{
		cacheMap = new HashMap( );
	}

	/**
	 * Enable cache on data set
	 */
	public void setCacheOption( boolean useCache )
	{
		this.useCache = useCache;
	}

	/**
	 * @param dataSourceDesign
	 */
	public void setDataSource( IBaseDataSourceDesign dataSourceDesign )
	{
		this.dataSourceDesign = dataSourceDesign;
	}

	/**
	 * @param datasetDesign
	 */
	public void setDataSet( IBaseDataSetDesign datasetDesign )
	{
		this.dataSetDesign = datasetDesign;
		if ( datasetDesign != null )
			this.cacheRowCount = datasetDesign.getCacheRowCount( );
	}

	/**
	 * @return
	 */
	public boolean doesSaveToCache( )
	{
		if ( useCache == false )
			return false;

		if ( dataSourceDesign != null
				&& dataSetDesign != null
				&& dataSetDesign.getCacheRowCount( ) != 0 )
		{
			DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( this.dataSourceDesign,
					this.dataSetDesign );
			if ( this.cacheMap.get( ds ) != null )
				return false;

			this.cacheMap.put( ds, this.getCacheDirStr( ) );
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	public boolean doesLoadFromCache( )
	{
		if ( useCache == false )
			return false;

		if ( dataSourceDesign != null
				&& dataSetDesign != null
				&& dataSetDesign.getCacheRowCount( ) != 0 )
			return this.cacheMap.get( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
					this.dataSetDesign ) ) != null;

		return false;
	}

	/**
	 * @return
	 */
	public int getCacheRowCount( )
	{
		if ( cacheRowCount == -1 )
			return Integer.MAX_VALUE;

		return cacheRowCount;
	}

	/**
	 * clear cache
	 */
	public void clearCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign )
	{
		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( dataSourceDesign,
				dataSetDesign );
		Object cacheDir = cacheMap.get( ds );
		if ( cacheDir != null )
		{
			cacheMap.remove( ds );
			getFolderUtil( ).deleteDir( (String) cacheDir );
		}
		this.dataSourceDesign = null;
		this.dataSetDesign = null;
		this.cacheRowCount = 0;
	}

	/**
	 * @return
	 */
	public String getSaveFolder( )
	{
		return (String) cacheMap.get( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign ) );
	}

	/**
	 * @return
	 */
	public String getLoadFolder( )
	{
		return (String) cacheMap.get( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign ) );
	}

	/**
	 * @return temp directory string, this folder name is unique and then
	 *         different session will not influence each other, which can
	 *         support multi-thread
	 */
	private String getCacheDirStr( )
	{
		getFolderUtil( ).createTempRootDir( );
		return getFolderUtil( ).createSessionTempDir( );
	}

	/**
	 * @return
	 */
	private FolderUtil getFolderUtil( )
	{
		if ( folderUtil == null )
			folderUtil = new FolderUtil( );
		return folderUtil;
	}

	/**
	 * Notice, this method is only for test, it can not be called unless its use
	 * is for test.
	 */
	public void resetForTest( )
	{
		cacheMap = new HashMap( );
		this.dataSourceDesign = null;
		this.dataSetDesign = null;
		this.cacheRowCount = 0;
	}

	/**
	 * Folder util class to manager temp folder
	 */
	private class FolderUtil
	{

		private String tempRootDirStr = null;

		/**
		 * @return temp root dir directory
		 */
		private void createTempRootDir( )
		{
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
