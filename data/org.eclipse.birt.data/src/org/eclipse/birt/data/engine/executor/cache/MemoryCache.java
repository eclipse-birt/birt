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

package org.eclipse.birt.data.engine.executor.cache;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Memory implementation of ResultSetCache
 */
public class MemoryCache implements ResultSetCache
{
	private int countOfResult;
	private int currResultIndex = -1;
	
	private IResultClass rsMeta;
	private IResultObject currResultObject;
	private IResultObject[] resultObjects;
	
	/**
	 * @param resultObjects
	 * @param comparator
	 */
	public MemoryCache( IResultObject[] resultObjects, IResultClass rsMeta,
			Comparator comparator )
	{
		this.resultObjects = resultObjects;
		this.rsMeta = rsMeta;
		this.countOfResult = resultObjects.length;

		if ( comparator != null )
			Arrays.sort( this.resultObjects, comparator );
	}

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
		if ( countOfResult == 0 )
			return false;
		
		if ( currResultIndex > countOfResult - 1 )
		{
			currResultObject = null;
		}
		else
		{			
			currResultIndex++;
			if ( currResultIndex == countOfResult )
				currResultObject = null;
			else
				currResultObject = resultObjects[currResultIndex];
		}

		return currResultObject != null;
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
		
		currResultIndex = destIndex;
	
		// currResultObject needs to be updated
		if ( currResultIndex == -1 || currResultIndex == countOfResult )
			currResultObject = null;
		else
			currResultObject = resultObjects[currResultIndex];
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
	public void reset( )
	{
		currResultIndex = -1;
		currResultObject = null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#close()
	 */
	public void close( )
	{
		reset( );
		resultObjects = null;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream(java.io.OutputStream)
	 */
	public void doSave( DataOutputStream outputStream,
			DataOutputStream rowLensStream,
			Map<String, StringTable> stringTable,
			Map<String, IIndexSerializer> index,
			List<IBinding> cacheRequestMap, int version,
			List<IAuxiliaryIndexCreator> auxiliaryIndexCreators,
			boolean saveInnerId )
			throws DataException
	{
		DataOutputStream dos = new DataOutputStream( outputStream );
		Set resultSetNameSet = ResultSetUtil.getRsColumnRequestMap( cacheRequestMap );
		try
		{
			// save data
			int rowCount = this.resultObjects.length;
			int colCount = getColumnCount( this.rsMeta );

			IOUtil.writeInt( dos, rowCount );
			long offset = 4;
			for ( int i = 0; i < rowCount; i++ )
			{
				IOUtil.writeLong( rowLensStream, offset );
				offset += ResultSetUtil.writeResultObject( dos,
						resultObjects[i],
						colCount,
						resultSetNameSet, stringTable, index, i, version, saveInnerId );
				if ( auxiliaryIndexCreators != null )
				{
					for ( IAuxiliaryIndexCreator creator : auxiliaryIndexCreators )
					{
						creator.save( resultObjects[i], i );
					}
				}
			}
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}

	private int getColumnCount( IResultClass meta )
			throws DataException
	{
		int count = meta.getFieldCount( );
		if ( meta != null )
		{
			for ( int i = 1; i <= meta.getFieldCount( ); i++ )
			{
				if ( meta.getFieldName( i ).equals( ExprMetaUtil.POS_NAME ) )
				{
					count--;
				}
			}
		}
		return count;
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
			int rowCount = originalRowCount + this.resultObjects.length;
			int colCount = this.rsMeta.getFieldCount( );
			
			IOUtil.writeInt( outputStream, rowCount );
			if( outputStream instanceof RAOutputStream )
				( ( RAOutputStream )outputStream ).seek( ( ( RAOutputStream )outputStream ).length( ) );
			if( rowLensStream instanceof RAOutputStream )
				( ( RAOutputStream )rowLensStream ).seek( ( ( RAOutputStream )rowLensStream ).length( ) );
			DataOutputStream dos = new DataOutputStream( outputStream );
			DataOutputStream rlos = new DataOutputStream( rowLensStream );
			
			long offset = 4;
			if( outputStream instanceof RAOutputStream )
				offset = ( ( RAOutputStream )outputStream ).length( );
			for ( int i = 0; i < rowCount - originalRowCount; i++ )
			{
				IOUtil.writeLong( rlos, offset );
				offset += ResultSetUtil.writeResultObject( dos,
						resultObjects[i],
						colCount,
						resultSetNameSet, stringTable, map, originalRowCount + i, version );
				if ( auxiliaryIndexCreators != null )
				{
					for ( IAuxiliaryIndexCreator creator : auxiliaryIndexCreators )
					{
						creator.save( resultObjects[i], originalRowCount + i );
					}
				}
			}
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