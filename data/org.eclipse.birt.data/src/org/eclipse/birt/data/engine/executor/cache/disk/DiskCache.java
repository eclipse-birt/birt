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
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.cache.SimpleCachedObject;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;

/**
 * Disk cache implementation of ResultSetCache
 */
public class DiskCache implements ResultSetCache
{	
	// current result data
	private int currResultIndex = -1;
	private IResultObject currResultObject;
	
	// the count of result
	private int countOfResult;
	
	// how many rows can be accomondated
	private int MemoryCacheRowCount;

	// goal file of this session
	private String goalFileStr;	
	private String sessionRootDirStr;
	
	// temporary root folder shared by all sessions
	private String tempRootDirStr;
	
	// disk result set
	protected DiskCacheResultSet diskBasedResultSet;
	
	// metadata
	protected IResultClass rsMeta;
	
	// log instance
	private static Logger logger = Logger.getLogger( DiskCache.class.getName( ) );
	
	protected DataEngineSession session;
	
	private boolean needCache;
	private BufferedStructureArray cache;
	/**
	 * The MemoryCacheRowCount indicates the upper limitation of how many rows
	 * can be loaded into memory. Note this value is included as well. Look at
	 * the start three parameters of the parameter list, the first is the result
	 * object array which length is MemoryCacheRowCount, and the second is one
	 * result object which follows the object array according to the position
	 * sequence of data source. The last is the RowResultSet, and it might have
	 * or not have more result object.
	 * 
	 * @param resultObjects
	 * @param nextResultObject
	 * @param rowResultSet
	 * @param rsMeta
	 * @param comparator
	 * @param MemoryCacheRowCount
	 * @param stopSign
	 * @throws DataException
	 */
	public DiskCache( IResultObject[] resultObjects, IResultObject resultObject,
			IRowResultSet rowResultSet, IResultClass rsMeta,
			Comparator comparator, int MemoryCacheRowCount,int maxRows, DataEngineSession session )
			throws DataException
	{
		//this.rsMeta = rsMeta;
		this.MemoryCacheRowCount = MemoryCacheRowCount;
		this.rsMeta = rsMeta;
		this.session = session;
		this.diskBasedResultSet = new DiskCacheResultSet( getInfoMap( ), session );
		
		try
		{
			logger.info( "Start processStartResultObjects" );
			diskBasedResultSet.processStartResultObjects( resultObjects,
					comparator );
			
			logger.info( "Start processRestResultObjects" );
			diskBasedResultSet.processRestResultObjects( resultObject,
					rowResultSet, maxRows == -1? -1 : maxRows - resultObjects.length );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.WRITE_TEMPFILE_ERROR, e );
		}
		countOfResult = diskBasedResultSet.getCount( );
		
		logger.info( "End of process, and the count of data is "
				+ countOfResult );
	}
	
	protected DiskCache( ){ }
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentIndex()
	 */
	public int getCurrentIndex( ) throws DataException
	{
		return currResultIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentResult()
	 */
	public IResultObject getCurrentResult( ) throws DataException
	{
		return currResultObject;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#next()
	 */
	public boolean next( ) throws DataException
	{
		if ( currResultIndex > countOfResult - 1 )
		{
			currResultObject = null;
			return false;
		}
		else
		{
			currResultIndex++;
			if ( currResultIndex == countOfResult )
			{
				currResultObject = null;
				return false;
			}
		}
		try
		{
			if ( needCache )
			{
				if( currResultIndex < cache.size( ) )
				{
					SimpleCachedObject cachedRow = ( SimpleCachedObject )cache.get( currResultIndex );
					currResultObject = new ResultObject( rsMeta, cachedRow.getFieldValues( ) );
				}
				else
				{
					currResultObject = diskBasedResultSet.nextRow( );
					cache.add( new SimpleCachedObject( getAllFields( currResultObject ) ) );
				}
			}
			else
			{
				currResultObject = diskBasedResultSet.nextRow( );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.READ_TEMPFILE_ERROR, e );
		}
		
		return currResultObject != null;
	}
	
	private static Object[] getAllFields( IResultObject obj ) throws DataException
	{
		Object[] fields = new Object[obj.getResultClass( ).getFieldCount( )];
		for ( int i = 0; i < fields.length; i++ )
		{
			fields[i] = obj.getFieldValue( i + 1 );
		}
		return fields;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#fetch()
	 */
	public IResultObject fetch( ) throws DataException
	{
		next( );
		IResultObject resultObject = getCurrentResult( );
		return resultObject;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#moveTo(int)
	 */
	public void moveTo( int destIndex ) throws DataException
	{
		checkValid( destIndex );
		
		int advancedStep;
		if ( destIndex >= currResultIndex )
		{
			advancedStep = destIndex - currResultIndex;
			for ( int i = 0; i < advancedStep; i++ )
				next( );
		}
		else
		{
			if ( !needCache )
			{
				reset( );
				initCache( );
				advancedStep = destIndex + 1;
				for ( int i = 0; i < advancedStep; i++ )
					next( );
			}
			else
			{
				SimpleCachedObject cachedRow;
				try
				{
					cachedRow = (SimpleCachedObject) cache.get( destIndex );
					currResultObject = new ResultObject( rsMeta,
							cachedRow.getFieldValues( ) );
				}
				catch ( IOException e )
				{
					throw new DataException( ResourceConstants.READ_TEMPFILE_ERROR,
							e );
				}
			}
		}
		
		currResultIndex = destIndex;
		
		// currResultObject needs to be updated
		if ( currResultIndex == -1 || currResultIndex == countOfResult )
			currResultObject = null;
	}

	/**
	 * 
	 */
	private void initCache( )
	{
		needCache = true;
		DataEngineThreadLocal.getInstance( ).getPathManager( ).setTempPath( this.session.getTempDir( ) );
		cache = new BufferedStructureArray( SimpleCachedObject.getCreator( ), 0 );
	}

	/**
	 * Validate the value of destIndex
	 * 
	 * @param destIndex
	 * @throws DataException
	 */
	private void checkValid( int destIndex ) throws DataException
	{
		if ( destIndex < -1 || destIndex > countOfResult )
			throw new DataException( ResourceConstants.DESTINDEX_OUTOF_RANGE,
					new Object[]{
							Integer.valueOf( -1 ),
							Integer.valueOf( countOfResult )
					} );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCount()
	 */
	public int getCount( )
	{
		return countOfResult;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#reset()
	 */
	public void reset( ) throws DataException
	{		
		diskBasedResultSet.reset( );
		needCache = false;
		if( cache != null )
		{
			try
			{
				cache.close( );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.READ_TEMPFILE_ERROR, e );
			}
			cache = null;
		}
		currResultIndex = -1;
		currResultObject = null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#close()
	 */
	public void close( ) throws DataException
	{	
		if( cache != null )
		{
			try
			{
				cache.close( );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.READ_TEMPFILE_ERROR, e );
			}
			cache = null;
		}
		diskBasedResultSet.close( );
		
		File goalFile = new File( goalFileStr );
		FileSecurity.fileDelete( goalFile );
		File tempDir = new File( sessionRootDirStr );
		FileSecurity.fileDelete( tempDir );
		
		currResultIndex = -1;
		currResultObject = null;
	}
	
	/**
	 * @return infoMap, including below information
	 * 		tempDir, to generated temp file in DiskMergeSort
	 * 		goalFile, to generate the end result file
	 * 		dataCountOfUnit, to indicate how many rows can be loaded into memory
	 * @throws DataException 
	 */
	private Map getInfoMap( ) throws DataException
	{
		Map infoMap = new HashMap( );

		infoMap.put( "tempDir", getTempDirStr( ) );
		goalFileStr = getGoalFileStr( );
		infoMap.put( "goalFile", goalFileStr );
		infoMap.put( "dataCountOfUnit", "" + MemoryCacheRowCount );

		return infoMap;
	}
	
	/**
	 * @return temp directory string, this folder is used to store the temporary
	 *         result in merge sort
	 * @throws DataException 
	 */
	private String getTempDirStr( ) throws DataException
	{
		return getSessionTempDirStr( ) + File.separator + "temp";
	}
	
	/**
	 * @return goal file of the end result, 
	 * @throws DataException 
	 */
	private String getGoalFileStr( ) throws DataException
	{
		return getSessionTempDirStr( ) + File.separator + "goalFile";
	}	

	/**
	 * @return temp directory string, this folder name is unique and then
	 *         different session will not influence each other, which can
	 *         support multi-thread
	 * @throws DataException 
	 */
	private String getSessionTempDirStr( ) throws DataException
	{
		if ( sessionRootDirStr != null )
			return sessionRootDirStr;

		// first create the root temp directory
		if ( tempRootDirStr == null )
			tempRootDirStr = createTempRootDir( );

		sessionRootDirStr = CacheUtil.createSessionTempDir( tempRootDirStr );
		return sessionRootDirStr; 
	}
	
	/**
	 * @return temp root dir directory
	 * @throws DataException 
	 */
	private String createTempRootDir( ) throws DataException
	{
		if ( tempRootDirStr == null )
		{
			synchronized ( DiskCache.class )
			{
				if ( tempRootDirStr == null )
				{
					// tempDir is user specified temporary directory
					String tempDir = session.getTempDir( );
					tempRootDirStr = CacheUtil.createTempRootDir( tempDir );
				}
			}
		}
		return tempRootDirStr;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream(java.io.OutputStream)
	 */
	public void doSave( DataOutputStream outputStream,
			DataOutputStream rowLensStream,
			Map<String, StringTable> stringTable,
			Map<String, IIndexSerializer> map, List<IBinding> cacheRequestMap,
			int version, List<IAuxiliaryIndexCreator> auxiliaryIndexCreators,
			boolean saveRowId )
			throws DataException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );
		Set resultSetNameSet = ResultSetUtil.getRsColumnRequestMap( cacheRequestMap );
		try
		{
			// save data
			int rowCount = this.diskBasedResultSet.getCount( );
			int colCount = this.rsMeta.getFieldCount( );
			
			IOUtil.writeInt( dos, rowCount );
			
			int currIndex = this.currResultIndex;
			this.reset( );
			long offset = 4;
			for ( int i = 0; i < rowCount; i++ )
			{
				IResultObject ro = this.diskBasedResultSet.nextRow( );
				if( ro == null )
					return;
				IOUtil.writeLong( rowLensStream, offset );
				offset += ResultSetUtil.writeResultObject( dos, ro, colCount,
						resultSetNameSet, stringTable, map, i, version,
						saveRowId );
				if ( auxiliaryIndexCreators != null )
				{
					for ( IAuxiliaryIndexCreator creator : auxiliaryIndexCreators )
					{
						creator.save( ro, i );
					}
				}
			}
			
			this.reset( );
			this.moveTo( currIndex );

		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream(java.io.OutputStream)
	 */
	public void incrementalUpdate( OutputStream outputStream,
			OutputStream rowLensStream, int originalRowCount,
			Map<String, StringTable> stringTable,
			Map<String, IIndexSerializer> map, List<IBinding> cacheRequestMap,
			int version, List<IAuxiliaryIndexCreator> auxiliaryIndexCreators )
			throws DataException
	{
		Set resultSetNameSet = ResultSetUtil.getRsColumnRequestMap( cacheRequestMap );
		try
		{
			// save data
			int rowCount = originalRowCount + this.diskBasedResultSet.getCount( );
			int colCount = this.rsMeta.getFieldCount( );
			
			IOUtil.writeInt( outputStream, rowCount );
			if( outputStream instanceof RAOutputStream )
				( ( RAOutputStream )outputStream ).seek( ( ( RAOutputStream )outputStream ).length( ) );
			if( rowLensStream instanceof RAOutputStream )
				( ( RAOutputStream )rowLensStream ).seek( ( ( RAOutputStream )rowLensStream ).length( ) );
			DataOutputStream dos = new DataOutputStream( outputStream );
			DataOutputStream rlos = new DataOutputStream( rowLensStream );
			int currIndex = this.currResultIndex;
			this.reset( );
			long offset = 4;
			if( outputStream instanceof RAOutputStream )
				offset = ( ( RAOutputStream )outputStream ).length( );
			for ( int i = 0; i < rowCount - originalRowCount; i++ )
			{
				IOUtil.writeLong( rlos, offset );
				IResultObject ro = this.diskBasedResultSet.nextRow( );
				offset += ResultSetUtil.writeResultObject( dos,
						ro,
						colCount,
						resultSetNameSet, stringTable, map, i + originalRowCount, version );
				if ( auxiliaryIndexCreators != null )
				{
					for ( IAuxiliaryIndexCreator creator : auxiliaryIndexCreators )
					{
						creator.save( ro, i + originalRowCount );
					}
				}
			}
			
			this.reset( );
			this.moveTo( currIndex );

		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
	/**
	 * 
	 * @param rsMeta
	 * @throws DataException
	 */
	public void setResultClass( IResultClass rsMeta ) throws DataException
	{
		this.rsMeta = rsMeta;
	}
}
