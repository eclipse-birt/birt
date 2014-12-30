
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.dscache;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.core.security.PropertySecurity;
import org.eclipse.birt.data.engine.executor.DataSetCacheObjectWithDummyData;
import org.eclipse.birt.data.engine.executor.DiskDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.IDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.MemoryDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.ResultIterator;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

class CacheUtilFactory
{
	/**
	 * 
	 * @param cacheObject
	 * @param rs
	 * @return
	 */
	public static ISaveUtil createSaveUtil( IDataSetCacheObject cacheObject , IResultClass rs, DataEngineSession session )
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			return new DiskSaveUtil( (DiskDataSetCacheObject) cacheObject,
					rs, session  );
		} else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return new MemorySaveUtil( (MemoryDataSetCacheObject)cacheObject, rs );
		}
		else if ( cacheObject instanceof IncreDataSetCacheObject )
		{
			return new IncreCacheSaveUtil( (IncreDataSetCacheObject) cacheObject, rs, session );

		}
		return null;
	}
	
	/**
	 * 
	 * @param cacheObject
	 * @return
	 * @throws DataException 
	 */
	public static ILoadUtil createLoadUtil( IDataSetCacheObject cacheObject, DataEngineSession session ) throws DataException
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			return new DiskLoadUtil( (DiskDataSetCacheObject)cacheObject, session );
		}
		else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return new MemoryLoadUtil( (MemoryDataSetCacheObject) cacheObject );
		}
		else if ( cacheObject instanceof IncreDataSetCacheObject )
		{
			return new IncreCacheLoadUtil( (IncreDataSetCacheObject) cacheObject, session );
		}
		else if ( cacheObject instanceof DataSetCacheObjectWithDummyData )
		{
			return new DummyDataCacheLoadUtil( (DataSetCacheObjectWithDummyData) cacheObject, session );
		}
		return null;
	}
	
	/**
	 * 
	 * @param metaMap
	 * @throws DataException
	 */
	private static List<IBinding> populateDataSetRowMapping( IResultClass rsClass )
			throws DataException
	{
		List<IBinding> result = new ArrayList<IBinding>();
		for ( int i = 0; i < rsClass.getFieldCount( ); i++ )
		{
			IBinding binding = new Binding( rsClass.getFieldName( i + 1 ) );
			if ( rsClass.getFieldAlias( i + 1 ) != null )
				binding.setExpression( new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( rsClass.getFieldAlias( i + 1 ) ) ) );
			else
				binding.setExpression( new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( rsClass.getFieldName( i + 1 ) ) ) );
			result.add( binding );
		}
		return result;
	}
	
	
	/**
	 * Util class to save the original data retrieved from ODA driver into cache
	 * file.
	 */
	private static class DiskSaveUtil implements ISaveUtil
	{
		private File file;
		private File metaFile;
		private FileOutputStream fos;
		private BufferedOutputStream bos;
		
		private IResultClass rsClass;
		private ResultObjectUtil roUtil;
		
		private int rowCount;
		private String tempFolder;
		private DataEngineSession session;
		/**
		 * @param file
		 * @param rsClass
		 */
		public DiskSaveUtil( DiskDataSetCacheObject cacheObject, IResultClass rsClass, DataEngineSession session )
		{
			assert rsClass != null;

			this.file = cacheObject.getDataFile( );
//			FileSecurity.fileDeleteOnExit( this.file );
			this.metaFile = cacheObject.getMetaFile( );
//			FileSecurity.fileDeleteOnExit( this.metaFile );
			this.rsClass = rsClass;
			this.rowCount = 0;
			this.tempFolder = cacheObject.getCacheDir( );
			this.session = session;
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public void saveObject( IResultObject resultObject ) throws DataException
		{
			assert resultObject!=null;
			
			if ( roUtil == null )
			{				
				roUtil = ResultObjectUtil.newInstance( rsClass, session );
				try
				{
					fos = FileSecurity.createFileOutputStream( file );
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
				rowCount ++;
				roUtil.writeData( bos, resultObject );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{			
			try
			{
				if ( bos != null )
				{
					bos.close( );
					fos.close( );
				}
				
				FileOutputStream fos1 = FileSecurity.createFileOutputStream(  metaFile );
				BufferedOutputStream bos1 = new BufferedOutputStream( fos1 );

				// save the count of data
				IOUtil.writeInt( bos1, this.rowCount );
		
				( (ResultClass) rsClass ).doSave( bos1, populateDataSetRowMapping( rsClass ), 0 );

				bos1.close( );
				fos1.close( );
				
				// save the current time as the timestamp
				CacheUtil.saveCurrentTime( this.tempFolder );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}			
		}
	}
	
	/**
	 *
	 */
	private static class MemorySaveUtil implements ISaveUtil
	{
		private MemoryDataSetCacheObject cacheObject;
		
				/**
		 * @param file
		 * @param rsMeta
		 */
		public MemorySaveUtil( MemoryDataSetCacheObject cacheObject, IResultClass rs )
		{
			assert cacheObject != null;

			this.cacheObject = cacheObject;
			this.cacheObject.setResultClass( rs );
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public void saveObject( IResultObject resultObject ) throws DataException
		{
			assert resultObject!=null;
			
			this.cacheObject.populateResult( resultObject );
			
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{			
		}
	}
	/**
	 * Helper class to save result set to cache file.
	 *
	 */
	private static class IncreCacheSaveUtil implements ISaveUtil
	{
		private File file;
		private File metaFile;
		
		private BufferedOutputStream bos;
		
		private IResultClass rsMeta;
		private ResultObjectUtil roUtil;
		
		private int rowCount;
		private String tempDir;
		private DataEngineSession session;
		public IncreCacheSaveUtil( IncreDataSetCacheObject cacheObject, IResultClass rs, DataEngineSession session )
		{
			this.file = cacheObject.getDataFile( );
			this.metaFile = cacheObject.getMetaFile( );
			this.rsMeta = rs;
			this.rowCount = 0;
			this.tempDir = cacheObject.getCacheDir( );
			this.session = session;
		}

		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public void saveObject( IResultObject resultObject ) throws DataException
		{
			assert resultObject!=null;
			
			if ( roUtil == null )
			{				
				roUtil = ResultObjectUtil.newInstance( rsMeta, session );
				try
				{
					bos = new BufferedOutputStream( FileSecurity.createFileOutputStream( file,
							true ) );
				}
				catch ( Exception e )
				{
					throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
							e );
				}
			}
			
			try
			{
				rowCount ++;
				roUtil.writeData( bos, resultObject );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{			
			try
			{
				if ( bos != null )
				{
					bos.close( );
				}
				if ( FileSecurity.fileExist( metaFile ) )
				{
					FileInputStream fis1 = FileSecurity.createFileInputStream( metaFile );
					BufferedInputStream bis1 = new BufferedInputStream( fis1 );
					int oldCount = IOUtil.readInt( bis1 );
					rowCount += oldCount;
					bis1.close( );
					fis1.close( );
				}
				FileOutputStream fos1 = FileSecurity.createFileOutputStream( metaFile );
				BufferedOutputStream bos1 = new BufferedOutputStream( fos1 );

				// save the count of data
				IOUtil.writeInt( bos1, this.rowCount );
				// save the meta data of result
			
				( (ResultClass) rsMeta ).doSave( bos1, populateDataSetRowMapping( rsMeta ), 0 );

				bos1.close( );
				fos1.close( );
				
				// save the current time as the timestamp
				CacheUtil.saveCurrentTimestamp( this.tempDir );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}			
		}
	}
	/**
	 * Helper class to load result set from cache file. 
	 *
	 */
	private static class IncreCacheLoadUtil implements ILoadUtil
	{
		private File file;
		private File metaFile;
		
		private FileInputStream fis;
		private BufferedInputStream bis;
		
		private ResultObjectUtil roUtil;
		private IResultClass rsClass;
		
		private int rowCount;
		private int currIndex;
		
		private DataEngineSession session;

		public IncreCacheLoadUtil( IncreDataSetCacheObject cacheObject, DataEngineSession session )
		{
			assert cacheObject != null;
			this.file = cacheObject.getDataFile( );
			this.metaFile = cacheObject.getMetaFile( );
			this.rowCount = 0;
			this.currIndex = -1;
			this.session = session;
		}
		
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public IResultObject loadObject( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			try
			{
				if ( currIndex == rowCount - 1 )
					return null;
				currIndex++;
				return roUtil.readData( bis, null, 1 )[0];
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public IResultClass loadResultClass( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			return this.rsClass;
		}
		
		/**
		 * @throws DataException
		 */
		private void init( ) throws DataException
		{
			try
			{
				FileInputStream fis1 = FileSecurity.createFileInputStream( metaFile );
				BufferedInputStream bis1 = new BufferedInputStream( fis1 );

				rowCount = IOUtil.readInt( bis1 );
				rsClass = new ResultClass( bis1, 0 );

				bis1.close( );
				fis1.close( );

				if ( rowCount > 0 )
				{
					roUtil = ResultObjectUtil.newInstance( rsClass, session );
					fis = FileSecurity.createFileInputStream( file );
					bis = new BufferedInputStream( fis );
				}
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
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{
			if ( bis != null )
			{
				try
				{
					bis.close( );
					fis.close( );
				}
				catch ( IOException e )
				{
					throw new DataException( "", e );
				}
			}
		}
	}
	/**
	 * Util class to retrieve data from stored cache file.
	 */
	private static class DiskLoadUtil implements ILoadUtil
	{
		private static final String END = "$end$";
		private static final String BEGIN = "$begin$";
		
		private File file;
		private File metaFile;
		
		private FileInputStream fis;
		private BufferedInputStream bis;
		
		private ResultObjectUtil roUtil;
		private IResultClass rsClass;
		
		private DiskDataSetCacheObject cacheObject;
		private int rowCount;
		private int currIndex;
		private DataEngineSession session;
		/**
		 * @param session 
		 * @param file
		 * @param metaFile
		 * @throws DataException 
		 */
		public DiskLoadUtil( DiskDataSetCacheObject cacheObject, DataEngineSession session ) throws DataException
		{
			assert cacheObject != null;
			
			this.cacheObject = cacheObject;
			this.file = cacheObject.getDataFile( );
			this.metaFile = cacheObject.getMetaFile( );
			this.session = session;
			this.rowCount = 0;
			this.currIndex = -1;
			this.mergeDelta();
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public IResultObject loadObject( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			try
			{
				if ( currIndex == rowCount - 1 )
					return null;
				
				currIndex++;
				return roUtil.readData( bis, this.session.getEngineContext( ).getClassLoader( ), 1 )[0];
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public IResultClass loadResultClass( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			return this.rsClass;
		}
		
		/**
		 * @throws DataException
		 */
		private void init( ) throws DataException
		{
			try
			{
				FileInputStream fis1 = FileSecurity.createFileInputStream( metaFile );
				BufferedInputStream bis1 = new BufferedInputStream( fis1 );

				rowCount = IOUtil.readInt( bis1 );
				rsClass = new ResultClass( bis1, 0 );

				bis1.close( );
				fis1.close( );

				if ( rowCount > 0 )
				{
					roUtil = ResultObjectUtil.newInstance( rsClass, session );
					fis = FileSecurity.createFileInputStream( file );
					bis = new BufferedInputStream( fis );
				}
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
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{
			if ( bis != null )
			{
				try
				{
					bis.close( );
					fis.close( );
				}
				catch ( IOException e )
				{
					// ignore;
				}
			}
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
				String configFile = getCacheConfig( this.session.getDataSetCacheManager( ).getCurrentAppContext( ) );

				if ( null != configFile && ( !configFile.equals( "" ) ) )
				{
					File dataFile = this.file;
					// check whether the specific cache data file already exists in
					// local disk
					if ( FileSecurity.fileExist( dataFile ) )
					{

						File config = new File( configFile );
						FileReader fileReader = null;

						if ( FileSecurity.fileExist( config ) )
							fileReader = FileSecurity.createFileReader( config );

						BufferedReader reader = new BufferedReader( fileReader );
						ArrayList list = readConfigFile( configFile, reader );
						File metaFile = this.metaFile;
						mergeDeltaToFile( dataFile, list, metaFile );

						CacheUtil.saveCurrentTime( this.cacheObject.getCacheDir( ));
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
			if ( context == null )
				return null;
			
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

			MergeUtil merge = new MergeUtil( dataFile, metaFile, session );
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
				line = line.trim( );
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
			Hashtable table = PropertySecurity.createHashtable( );
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
			if ( !( this.session.getDataSetCacheManager( ).getCurrentDataSetDesign( ) instanceof OdaDataSetDesign ) )
				return null;

			sql = this.resetQueryText( sql );
			if ( sql == null || "".equals( sql ) )
			{
				return null;
			}

			QueryDefinition qd = new QueryDefinition( true );
			qd.setDataSetName( this.session.getDataSetCacheManager( ).getCurrentDataSetDesign( ).getName( ) );
			
			String queryBack = ( (OdaDataSetDesign) this.session.getDataSetCacheManager( ).getCurrentDataSetDesign( ) ).getQueryText( );
			//int savedCacheOption = this.session.getDataSetCacheManager( ).suspendCache( );
			IResultIterator iterator = this.session.getEngine( ).prepare( qd, new HashMap() )
					.execute( null )
					.getResultIterator( );
			( (OdaDataSetDesign) this.session.getDataSetCacheManager( ).getCurrentDataSetDesign( ) ).setQueryText( queryBack );
			//this.session.getDataSetCacheManager( ).setCacheOption( savedCacheOption );
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

				if ( ( (String) item.get( 0 ) ).equalsIgnoreCase( this.session.getDataSetCacheManager( ).getCurrentDataSourceDesign( ).getName( ) )
						&& ( (String) item.get( 1 ) ).equalsIgnoreCase( this.session.getDataSetCacheManager( ).getCurrentDataSetDesign( ).getName( ) ) )
				{
					Object[] para = this.session.getDataSetCacheManager( ).getCurrentParameterHints( ).toArray( );

					Hashtable table = (Hashtable) item.get( 3 );

					ParameterHint ph;

					for ( int i = 0; i < para.length; i++ )
					{
						ph = (ParameterHint) para[i];
						if ( ph.isInputMode( ) )
						{
							String paraName = ph.getName( );
							if ( table.containsKey( paraName )
									&& ( table.get( paraName ) ).equals( ph.getDefaultInputValue( ) ) )
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
			String timestamp = CacheUtil.getLastTime( this.cacheObject.getCacheDir() );
			if ( timestamp == null || timestamp.length( ) != 14 )
			{
				return null;
			}

			return text.replaceAll( "\\Q${DATE}$\\E", timestamp );
		}
	}
	
	/**
	 * Util class to retrieve data from stored cache file.
	 */
	private static class MemoryLoadUtil implements ILoadUtil
	{
		private int currIndex;
		
		private MemoryDataSetCacheObject cacheObject;
		
		/**
		 * @param file
		 * @param metaFile
		 */
		public MemoryLoadUtil( MemoryDataSetCacheObject cacheObject )
		{
			assert cacheObject != null;
			this.cacheObject = cacheObject;
			this.currIndex = -1;
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public IResultObject loadObject( ) throws DataException
		{			
			currIndex++;
			if ( currIndex >= this.cacheObject.getSize( ) )
				return null;
			return this.cacheObject.getResultObject( currIndex );
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public IResultClass loadResultClass( ) throws DataException
		{			
			return this.cacheObject.getResultClass( );
		}
	
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{
		}
	}

	/**
	 * Util class to help merge delta data with the local existed data and
	 * update the revalent information in cache file.
	 */
	private static class MergeUtil
	{

		private File dataFile;
		private IResultClass rsClass;
		private File metaFile;

		private FileOutputStream fos;
		private BufferedOutputStream bos;

		private ResultObjectUtil roUtil;

		private int rowCount;
		private DataEngineSession session;
		/**
		 * constructor of class MergeUtil
		 * 
		 * @param file
		 * @param rsMeta
		 * @throws DataException
		 */
		private MergeUtil( File dataFile, File metaFile, DataEngineSession session ) throws DataException
		{
			assert dataFile != null;
			assert metaFile != null;

			this.dataFile = dataFile;
			this.metaFile = metaFile;
			this.session = session;
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
				FileInputStream fis = FileSecurity.createFileInputStream( metaFile );
				BufferedInputStream bis = new BufferedInputStream( fis );

				rowCount = IOUtil.readInt( bis );
				rsClass = new ResultClass( bis, 0 );

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
				roUtil = ResultObjectUtil.newInstance( rsClass, session );
				try
				{
					fos = FileSecurity.createFileOutputStream( dataFile, true );
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

				FileOutputStream fos1 = FileSecurity.createFileOutputStream( metaFile );
				BufferedOutputStream bos1 = new BufferedOutputStream( fos1 );

				// save the count of data
				IOUtil.writeInt( bos1, this.rowCount );

				// save the meta data of result
				( (ResultClass) rsClass ).doSave( bos1, populateDataSetRowMapping( rsClass ), 0 );

				bos1.close( );
				fos1.close( );

				FileInputStream fis = FileSecurity.createFileInputStream( metaFile );
				BufferedInputStream bis = new BufferedInputStream( fis );

				rowCount = IOUtil.readInt( bis );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}
	}
}
