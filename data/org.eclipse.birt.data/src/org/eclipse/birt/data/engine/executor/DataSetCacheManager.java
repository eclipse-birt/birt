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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ResultIterator;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

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

	private static final String META_DATA_FILE = "metaData.data";

	private static final String END = "$end$";

	private static final String BEGIN = "$begin$";

	// whether cache is needed
	private int cacheOption;

	// how many rows needs to be always cached
	private int alwaysCacheRowCount;

	// data set id and its cache count
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection parameterHints;
	private Map appContext;

	// map manager instance
	private CacheMapManager cacheMapManager;
	private DataEngine dataEngine;

	// to mark whether "delta" has been merged
	private boolean needToMerge;

	/**
	 * Construction
	 */
	public DataSetCacheManager( String tempDir, DataEngine dataEngine )
	{
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheOption = DataEngineContext.CACHE_USE_DEFAULT;
		alwaysCacheRowCount = 0;

		cacheMapManager = new CacheMapManager( tempDir );
		this.dataEngine = dataEngine;
		needToMerge = true;
	}

	/**
	 * Enable cache on data set
	 * 
	 * @param cacheOption
	 */
	public void setCacheOption( int cacheOption )
	{
		this.cacheOption = cacheOption;
	}

	/**
	 * @return
	 */
	public int suspendCache( )
	{
		int lastCacheOption = this.cacheOption;
		this.setCacheOption( DataEngineContext.CACHE_USE_DISABLE );
		return lastCacheOption;
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
			IBaseDataSetDesign dataSetDesign, Collection parameterHints, Map appContext )
	{
		this.dataSourceDesign = dataSourceDesign;
		this.dataSetDesign = dataSetDesign;
		this.parameterHints = parameterHints;
		this.appContext = appContext;
		if ( needToMerge )
		{
			try
			{
				this.mergeDelta( );
			}
			catch ( BirtException e )
			{
			}
		}
	}

	/**
	 * @return
	 */
	public boolean doesSaveToCache( )
	{
		if ( needsToCache( dataSetDesign, cacheOption, alwaysCacheRowCount ) == false )
			return false;

		return cacheMapManager.doesSaveToCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints ) );
	}

	/**
	 * @return
	 */
	public boolean doesLikeToCache( )
	{
		return needsToCache( dataSetDesign, cacheOption, alwaysCacheRowCount );
	}

	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @param parameterBindings
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 */
	public boolean doesLoadFromCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign,
			Collection parameterHints, Map appContext, int cacheOption, int alwaysCacheRowCount )
	{
		if ( needsToCache( dataSetDesign, cacheOption, alwaysCacheRowCount ) == false )
			return false;

		// this.setFirstLoad( false );

		this.setDataSourceAndDataSet( dataSourceDesign,
				dataSetDesign,
				parameterHints,
				appContext);
		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				dataSetDesign,
				parameterHints ) );
	}

	/**
	 * @param dataSetDesign
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 */
	public boolean needsToCache( IBaseDataSetDesign dataSetDesign,
			int cacheOption, int alwaysCacheRowCount )
	{
		return DataSetCacheUtil.needsToCache( dataSetDesign,
				cacheOption,
				alwaysCacheRowCount );
	}

	/**
	 * @return
	 */
	public int getCacheRowCount( )
	{
		return DataSetCacheUtil.getCacheRowCount( cacheOption,
				alwaysCacheRowCount,
				this.dataSetDesign == null ? 0
						: this.dataSetDesign.getCacheRowCount( ) );
	}

	/**
	 * Clear cache
	 * 
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	public void clearCache( IBaseDataSourceDesign dataSourceDesign2,
			IBaseDataSetDesign dataSetDesign2 )
	{
		if ( dataSourceDesign2 == null || dataSetDesign2 == null )
			return;

		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( dataSourceDesign2,
				dataSetDesign2,
				null );
		cacheMapManager.clearCache( ds );
	}

	/**
	 * @return
	 */
	public String getSaveFolder( )
	{
		return cacheMapManager.getSaveFolder( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints ) );
	}

	/**
	 * @return
	 */
	public String getLoadFolder( )
	{
		return cacheMapManager.getLoadFolder( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints ) );
	}

	/**
	 * only for test
	 * 
	 * @return
	 */
	public boolean doesLoadFromCache( )
	{
		if ( needsToCache( dataSetDesign, cacheOption, alwaysCacheRowCount ) == false )
			return false;

		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints ) );
	}

	/**
	 * Notice, this method is only for test, it can not be called unless its use
	 * is for test.
	 */
	public void resetForTest( )
	{
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheOption = DataEngineContext.CACHE_USE_DEFAULT;
		alwaysCacheRowCount = 0;

		this.cacheMapManager.resetForTest( );
	}

	/**
	 * Return the cached result metadata. Please note that parameter hint will
	 * not change the returned metadata.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData getCachedResultMetadata(
			IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet )
			throws DataException
	{
		IResultClass resultClass = this.cacheMapManager.getCachedResultClass( DataSourceAndDataSet.newInstance( dataSource,
				dataSet,
				null ) );
		if ( resultClass != null )
			return new ResultMetaData( resultClass );
		else
			return null;
	}

	/**
	 * Do the action "merging" when the user provide a specific and valid
	 * configure file
	 * 
	 * @throws BirtException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void mergeDelta( ) throws DataException
	{
		try
		{
			String configFile = getCacheConfig( appContext );

			if ( null != configFile && ( !configFile.equals( "" ) ) )
			{
				String folder = getSaveFolder( );
				File dataFile = new File( folder + File.separator + "data.data" );
				// check whether the specific cache data file already exists in
				// local disk
				if ( dataFile.exists( ) )
				{

					File config = new File( configFile );
					FileReader fileReader = null;

					if ( config.exists( ) )
						fileReader = new FileReader( config );

					BufferedReader reader = new BufferedReader( fileReader );
					ArrayList list = readConfigFile( configFile, reader );
					File metaFile = new File( getLoadFolder( )
							+ File.separator + META_DATA_FILE );
					mergeDeltaToFile( dataFile, list, metaFile );

					CacheUtil.saveCurrentTime( getSaveFolder( ) );

					this.needToMerge = false;
				}
			}
		}
		catch ( Exception e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * 
	 * @param appContext2
	 * @return
	 */
	private String getCacheConfig( Map context )
	{
		return context.get( DataEngine.DATA_SET_CACHE_DELTA_FILE ) == null
				? null
				: String.valueOf( context.get( DataEngine.DATA_SET_CACHE_DELTA_FILE ) );
	}

	/**
	 * 
	 * @param dataFile
	 * @param list
	 * @param metaFile
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 * @throws ClassNotFoundException
	 */
	private void mergeDeltaToFile( File dataFile, ArrayList list, File metaFile )
			throws DataException, IOException, BirtException,
			ClassNotFoundException
	{

		MergeUtil merge = new MergeUtil( dataFile, metaFile );
		// read the objects from iterator to local file one by one
		IResultIterator iterator = getResultIterator( list );
		if ( iterator != null )
		{
			ResultObject ro;
			while ( iterator.next( ) )
			{
				ro = (ResultObject) ( ( (ResultIterator) iterator ).getOdiResult( ).getCurrentResult( ) );

				merge.saveObject( ro );
			}
		}
		merge.close( );
	}

	/**
	 * A private method to read all the query blocks to an arraylist one by one
	 * 
	 * @return an ArrayList that record each query block assigned by the user
	 * @throws IOException
	 */
	private ArrayList readConfigFile( String file, BufferedReader reader )
			throws IOException
	{
		ArrayList list = new ArrayList( );
		String line;
		StringBuffer block = new StringBuffer( "" );
		/* to mark the begining and the end of single query block */
		boolean begin = false;

		while ( ( line = reader.readLine( ) ) != null )
		{
			line.trim( );
			if ( line.startsWith( BEGIN ) && !begin )
			{
				begin = true;
				line = line.substring( 7 );
			}
			if ( line.endsWith( END ) && begin )
			{
				line = line.substring( 0, line.length( ) - 5 );
				block.append( line );
				list.add( this.parseQueryItem( block.toString( ) ) );
				block = new StringBuffer( "" );
				begin = false;
			}
			else if ( begin )
			{
				block.append( line );
			}
		}

		return list;
	}

	/**
	 * A private method to parse a single query string to an ArrayList
	 * 
	 * @return an ArrayList that record each small unit of information about a
	 *         single query
	 * @throws IOException
	 */
	private ArrayList parseQueryItem( String item )
	{
		// ArrayList structure = new ArrayList();
		ArrayList info = new ArrayList( );
		String[] split = item.split( ";" );
		String[] temp;
		for ( int i = 0; i < 3; i++ )
		{
			temp = split[i].split( "\"" );
			info.add( temp[1] );
		}
		String paraName, paraValue;
		Hashtable table = new Hashtable( );
		for ( int j = 3; j < split.length; j++ )
		{
			paraName = ( split[j].split( "=" ) )[0];
			paraValue = ( split[j].split( "\"" ) )[1];
			table.put( paraName, paraValue );
		}
		info.add( table );
		return info;
	}

	/**
	 * To get the ResultIterator from the configure file after executing query
	 * 
	 * @param list
	 * @param index
	 * @return the obtained ResultIterator
	 * @throws IOException
	 * @throws BirtException
	 * @throws ClassNotFoundException
	 */
	private IResultIterator getResultIterator( ArrayList list )
			throws IOException, BirtException, ClassNotFoundException
	{
		String sql = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			sql = getQualifiedSql( list, i, sql );
			if ( sql != null )
				break;
		}
		if ( sql == null )
			return null;
		if ( !( dataSetDesign instanceof OdaDataSetDesign ) )
			return null;

		sql = this.resetQueryText( sql );
		if ( sql == null || "".equals( sql ) )
		{
			return null;
		}

		QueryDefinition qd = new QueryDefinition( );
		qd.setDataSetName( dataSetDesign.getName( ) );
		String queryBack = ( (OdaDataSetDesign) dataSetDesign ).getQueryText( );
		( (OdaDataSetDesign) dataSetDesign ).setQueryText( sql );
		qd.setAutoBinding( true );
		IResultIterator iterator = this.dataEngine.prepare( qd )
				.execute( null )
				.getResultIterator( );

		( (OdaDataSetDesign) dataSetDesign ).setQueryText( queryBack );

		return iterator;
	}

	private String getQualifiedSql( ArrayList list, int index, String sql )
	{
		{
			ArrayList item = (ArrayList) list.get( index );
			if ( item == null || item.size( ) == 0 )
			{
				sql = null;
			}

			int count = 0;

			if ( ( (String) item.get( 0 ) ).equalsIgnoreCase( dataSourceDesign.getName( ) )
					&& ( (String) item.get( 1 ) ).equalsIgnoreCase( dataSetDesign.getName( ) ) )
			{
				Object[] para = this.parameterHints.toArray( );

				Hashtable table = (Hashtable) item.get( 3 );

				ParameterHint ph;

				for ( int i = 0; i < para.length; i++ )
				{
					ph = (ParameterHint) para[i];
					if ( ph.isInputMode( ) )
					{
						String paraName = ph.getName( );
						if ( table.containsKey( paraName )
								&& ( (String) table.get( paraName ) ).equalsIgnoreCase( ph.getDefaultInputValue( ) ) )
						{
							count++;
						}
						else
						{
							count = -1;
							sql = null;
						}
					}
				}

				if ( count == table.size( ) )
				{
					sql = (String) item.get( 2 );
				}
				else
					sql = null;

			}
		}
		return sql;
	}

	/**
	 * Reset the QueryText content
	 * 
	 * @param text
	 * @return the new String containing timestamp information
	 * @throws IOException
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	private String resetQueryText( String text ) throws IOException,
			DataException, ClassNotFoundException
	{
		String timestamp = CacheUtil.getLastTime( this.getSaveFolder( ) );
		if ( timestamp == null || timestamp.length( ) != 14 )
		{
			return null;
		}

		return text.replaceAll( "\\Q${DATE}$\\E", timestamp );
	}

	/**
	 * Util class to help merge delta data with the local existed data and
	 * update the revalent information in cache file.
	 */
	private class MergeUtil
	{

		private File dataFile;
		private IResultClass rsClass;
		private File metaFile;

		private FileOutputStream fos;
		private BufferedOutputStream bos;

		private ResultObjectUtil roUtil;

		private int rowCount;

		/**
		 * constructor of class MergeUtil
		 * 
		 * @param file
		 * @param rsClass
		 * @throws DataException
		 */
		private MergeUtil( File dataFile, File metaFile ) throws DataException
		{
			assert dataFile != null;
			assert metaFile != null;

			this.dataFile = dataFile;
			this.metaFile = metaFile;

			// this.metaFile.deleteOnExit( );
			this.init( );
		}

		/**
		 * Initialize the properties of a MergeUtil instance
		 * 
		 * @throws DataException
		 */
		private void init( ) throws DataException
		{
			try
			{
				FileInputStream fis = new FileInputStream( metaFile );
				BufferedInputStream bis = new BufferedInputStream( fis );

				rowCount = IOUtil.readInt( bis );
				rsClass = new ResultClass( bis );

				bis.close( );
				fis.close( );
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

		/**
		 * To save each ResultObject
		 * 
		 * @param resultObject
		 * @throws DataException
		 */
		private void saveObject( IResultObject resultObject )
				throws DataException
		{
			assert resultObject != null;

			if ( roUtil == null )
			{
				roUtil = ResultObjectUtil.newInstance( rsClass );
				try
				{
					fos = new FileOutputStream( dataFile, true );
					bos = new BufferedOutputStream( fos );
				}
				catch ( FileNotFoundException e )
				{
					throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
							e );
				}
			}

			try
			{
				rowCount++;
				roUtil.writeData( bos, resultObject );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}

		/**
		 * Write the new data to the local file and close the I/O operation
		 * 
		 * @throws DataException
		 */
		private void close( ) throws DataException
		{
			try
			{
				if ( bos != null )
				{
					bos.close( );
					fos.close( );
				}

				FileOutputStream fos1 = new FileOutputStream( metaFile );
				BufferedOutputStream bos1 = new BufferedOutputStream( fos1 );

				// save the count of data
				IOUtil.writeInt( bos1, this.rowCount );

				// save the meta data of result

				Map metaMap = new HashMap( );
				populateDataSetRowMapping( metaMap );
				( (ResultClass) rsClass ).doSave( bos1, metaMap );

				bos1.close( );
				fos1.close( );

				FileInputStream fis = new FileInputStream( metaFile );
				BufferedInputStream bis = new BufferedInputStream( fis );

				rowCount = IOUtil.readInt( bis );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}

		/**
		 * Populate the new rsClass object instance
		 * 
		 * @param metaMap
		 * @throws DataException
		 */
		private void populateDataSetRowMapping( Map metaMap )
				throws DataException
		{
			for ( int i = 0; i < rsClass.getFieldCount( ); i++ )
				metaMap.put( rsClass.getFieldName( i + 1 ),
						new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( rsClass.getFieldName( i + 1 ) ) ) );
		}

	}

}
