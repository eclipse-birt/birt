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
import java.util.Collection;
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
 * Please notice the whole procedure: 1: first check whether data can be loaded
 * from cache 2.1: if yes, then data will be loaded and check whether data needs
 * to be saved into cache will be skiped. 2.2: if no, then data will be retrived
 * from data set and then whether saving into cache will be checked 2.2.1: if
 * yes, then data will be saved into cache 2.2.2: if no, then nothing will be
 * done
 * 
 * There are three possible value of cacheRowCount: 1: -1, cache all data set 2:
 * 0, don't cache 3: >0, cahe the specified value
 * 
 * Here whether data will be loaded from cache can be observed by external
 * caller, but about saving into cache is not.
 */
public class DataSetCacheManager
{
	// map of cache relationship
	private Map cacheMap;

	// data set id and its cache count
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection parameterBindings;
	
	private int cacheRowCount;

	//
	public final static int ALWAYS = 1;
	public final static int DISABLE = 2;
	public final static int DEFAULT = 3;
	
	// 
	private int cacheOption;
	private int alwaysCacheRowCount;

	// folder util instance
	private FolderUtil folderUtil;
		
	// instance
	private static DataSetCacheManager cacheManager;

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
		folderUtil = new FolderUtil( );
		
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheRowCount = 0;
		cacheOption = DEFAULT;
		alwaysCacheRowCount = 0;
	}

	/**
	 * Enable cache on data set
	 */
	public void setCacheOption( int cacheOption )
	{
		this.cacheOption = cacheOption;
	}

	/**
	 * @param rowCount
	 */
	public void setAlwaysCacheRowCount( int rowCount )
	{
		this.alwaysCacheRowCount = rowCount;
	}
	
	/**
	 * Remember before requesting any service, this function must be called in
	 * advance to make sure using current data source and data set.
	 * 
	 * @param dataSourceDesign
	 * @param datasetDesign
	 */
	public void setDataSourceAndDataSet(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign datasetDesign, Collection parameterBindings )
	{
		this.dataSourceDesign = dataSourceDesign;
		this.dataSetDesign = datasetDesign;
		if ( datasetDesign != null )
			this.cacheRowCount = datasetDesign.getCacheRowCount( );
		this.parameterBindings = parameterBindings;
	}

	/**
	 * @return
	 */
	public boolean doesSaveToCache( )
	{
		if ( basicCache( ) == false )
			return false;

		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings );
		String cacheDirStr = (String) this.cacheMap.get( ds );
		if ( cacheDirStr != null && new File( cacheDirStr ).exists( ) == true )
		{
			return false;
		}
		else
		{
			this.cacheMap.put( ds, this.getCacheDirStr( ) );
			return true;
		}
	}

	/**
	 * @return
	 */
	public boolean doesLoadFromCache( )
	{
		if ( basicCache( ) == false )
			return false;

		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings );
		String cacheDirStr = (String) this.cacheMap.get( ds );
		if ( cacheDirStr != null && new File( cacheDirStr ).exists( ) == true )
			return true;
		else
			return false;
	}
	
	/**
	 * @return
	 */
	private boolean basicCache( )
	{
		if ( dataSourceDesign == null || dataSetDesign == null )
			return false;

		if ( this.cacheOption == DISABLE )
		{
			return false;
		}
		else if ( this.cacheOption == ALWAYS )
		{
			if ( this.alwaysCacheRowCount == 0 )
				return false;
		}
		else if ( dataSetDesign.getCacheRowCount( ) == 0 )
		{
			return false;
		}

		return true;
	}
	
	/**
	 * @return
	 */
	public int getCacheRowCount( )
	{
		if ( this.cacheOption == ALWAYS )
		{
			if ( this.alwaysCacheRowCount <= 0 )
				return Integer.MAX_VALUE;
			else
				return this.alwaysCacheRowCount;
		}
		else if ( this.cacheOption == DISABLE )
		{
			return Integer.MAX_VALUE;
		}
		else
		{
			if ( cacheRowCount == -1 )
				return Integer.MAX_VALUE;
			else
				return cacheRowCount;
		}
	}

	/**
	 * clear cache
	 */
	public void clearCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign )
	{
		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( dataSourceDesign,
				dataSetDesign,
				null );
		Object cacheDir = cacheMap.get( ds );
		if ( cacheDir != null )
		{
			cacheMap.remove( ds );
			folderUtil.deleteDir( (String) cacheDir );
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
				this.dataSetDesign,
				this.parameterBindings ) );
	}

	/**
	 * @return
	 */
	public String getLoadFolder( )
	{
		return (String) cacheMap.get( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings ) );
	}

	/**
	 * @return temp directory string, this folder name is unique and then
	 *         different session will not influence each other, which can
	 *         support multi-thread
	 */
	private String getCacheDirStr( )
	{
		return folderUtil.createSessionTempDir( );
	}

	/**
	 * Notice, this method is only for test, it can not be called unless its use
	 * is for test.
	 */
	public void resetForTest( )
	{
		cacheMap = new HashMap( );
		folderUtil = new FolderUtil( );
		
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheRowCount = 0;
		cacheOption = DEFAULT;
		alwaysCacheRowCount = 0;
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
