/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.executor.transform;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.executor.cache.SmartCacheRequest;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A Result Set that directly fetch data from ODA w/o using any cache.
 * @author Work
 *
 */
public class SimpleResultSet implements IResultIterator
{

	private ResultSet resultSet;
	private RowResultSet rowResultSet;
	private StopSign stopSign;
	private IResultObject currResultObj;
	private IEventHandler handler;
	private int initialRowCount, rowCount;
	private StreamWrapper streamsWrapper;
	private OutputStream dataSetStream;
	private DataOutputStream dataSetLenStream;
	private long offset = 4;
	private long rowCountOffset = 0;
	
	/**
	 * 
	 * @param dataSourceQuery
	 * @param resultSet
	 * @param resultClass
	 * @param stopSign
	 * @throws DataException
	 */
	public SimpleResultSet( DataSourceQuery dataSourceQuery,
			ResultSet resultSet, IResultClass resultClass,
			IEventHandler handler, StopSign stopSign ) throws DataException
	{
		this.rowResultSet = new RowResultSet( new SmartCacheRequest( dataSourceQuery.getMaxRows( ),
				dataSourceQuery.getFetchEvents( ),
				new OdiAdapter( resultSet ),
				resultClass,
				false ) );
		this.currResultObj = this.rowResultSet.next( stopSign );
		this.initialRowCount = ( this.currResultObj != null ) ? -1 : 0;
		this.rowCount = ( this.currResultObj != null ) ? 1 : 0;
		this.resultSet = resultSet;
		this.stopSign = stopSign;
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#close()
	 */
	public void close( ) throws DataException
	{
		if ( this.resultSet != null )
		{
			this.resultSet.close( );
			this.resultSet = null;
		}
		if ( this.dataSetStream != null )
		{
			try
			{

				if ( dataSetStream instanceof RAOutputStream )
				{
					( (RAOutputStream) dataSetStream ).seek( rowCountOffset );
					IOUtil.writeInt( dataSetStream, rowCount );
				}
				OutputStream exprValueStream = this.streamsWrapper.getStreamManager( )
						.getOutStream( DataEngineContext.EXPR_VALUE_STREAM,
								StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE );
				if ( exprValueStream instanceof RAOutputStream )
				{
					( (RAOutputStream) exprValueStream ).seek( 0 );
					IOUtil.writeInt( exprValueStream, rowCount );
				}

				dataSetStream.close( );
			}
			catch ( Exception e )
			{
				
			}
			dataSetStream = null;
		}
		if ( this.dataSetLenStream != null )
		{
			try
			{
				dataSetLenStream.close( );
			}
			catch ( Exception e )
			{
			}
			dataSetLenStream = null;
		}
	}

	public void finalize()
	{
		try
		{
			this.close();
		}
		catch ( DataException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#doSave(org.eclipse.birt.data.engine.impl.document.StreamWrapper, boolean)
	 */
	public void doSave( StreamWrapper streamsWrapper, boolean isSubQuery )
			throws DataException
	{
		this.streamsWrapper = streamsWrapper;

		if ( streamsWrapper.getStreamForGroupInfo( ) != null )
		{
			try
			{
				IOUtil.writeInt( streamsWrapper.getStreamForGroupInfo( ), 0 );
			}
			catch ( IOException e )
			{
			}
		}

		if ( streamsWrapper.getStreamForResultClass( ) != null )
		{
			( (ResultClass) getResultClass( ) ).doSave( streamsWrapper.getStreamForResultClass( ),
					this.handler.getAllColumnBindings( ) );
		}
		try
		{
			streamsWrapper.getStreamForResultClass( ).close( );
			dataSetStream = this.streamsWrapper.getStreamManager( )
					.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
			dataSetLenStream = streamsWrapper.getStreamForDataSetRowLens( );
			if ( dataSetStream instanceof RAOutputStream )
				rowCountOffset = ( (RAOutputStream) dataSetStream ).getOffset( );
			IOUtil.writeInt( dataSetStream, this.initialRowCount );
		}
		catch ( IOException e )
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#first(int)
	 */
	public void first( int groupingLevel ) throws DataException
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getAggrValue(java.lang.String)
	 */
	public Object getAggrValue( String aggrName ) throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentGroupIndex(int)
	 */
	public int getCurrentGroupIndex( int groupLevel ) throws DataException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentResult()
	 */
	public IResultObject getCurrentResult( ) throws DataException
	{
		return this.currResultObj;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentResultIndex()
	 */
	public int getCurrentResultIndex( ) throws DataException
	{
		return this.rowResultSet.getIndex( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		if ( this.rowResultSet.hasNext( ) )
		{
			return 1;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getExecutorHelper()
	 */
	public IExecutorHelper getExecutorHelper( )
	{
		return this.handler.getExecutorHelper( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getGroupStartAndEndIndex(int)
	 */
	public int[] getGroupStartAndEndIndex( int groupLevel )
			throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getResultClass()
	 */
	public IResultClass getResultClass( ) throws DataException
	{
		// TODO Auto-generated method stub
		return this.rowResultSet.getMetaData( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getResultSetCache()
	 */
	public ResultSetCache getResultSetCache( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getRowCount()
	 */
	public int getRowCount( ) throws DataException
	{
		return this.initialRowCount;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		if ( this.rowResultSet.getIndex( ) == 0 )
			return 0;
		else
			return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#last(int)
	 */
	public void last( int groupingLevel ) throws DataException
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#next()
	 */
	public boolean next( ) throws DataException
	{
		if ( this.streamsWrapper != null && currResultObj != null )
		{
			try
			{
				if ( dataSetStream != null )
				{
					int colCount = this.currResultObj.getResultClass( )
							.getFieldCount( );
					Set resultSetNameSet = ResultSetUtil.getRsColumnRequestMap( handler.getAllColumnBindings( ) );
					IOUtil.writeLong( dataSetLenStream, offset );

					offset += ResultSetUtil.writeResultObject( new DataOutputStream( dataSetStream ),
							currResultObj,
							colCount,
							resultSetNameSet );
				}
			}
			catch ( IOException e )
			{
			}
		}
		this.currResultObj = this.rowResultSet.next( stopSign );

		if ( this.currResultObj != null )
			rowCount++;
		return this.currResultObj != null;
	}

}
