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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.ICloseable;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.aggregation.AggrDefnManager;
import org.eclipse.birt.data.engine.executor.aggregation.IProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.aggregation.ProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.executor.cache.SmartCacheRequest;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;

/**
 * A Result Set that directly fetch data from ODA w/o using any cache.
 * @author Work
 *
 */
public class SimpleResultSet implements IResultIterator
{

	private RowResultSet rowResultSet;
	private IResultObject currResultObj;
	private IEventHandler handler;
	private int initialRowCount, rowCount;
	private StreamWrapper streamsWrapper;
	private OutputStream dataSetStream;
	private DataOutputStream dataSetLenStream;
	private long offset = 4;
	private long rowCountOffset = 0;
	private Set resultSetNameSet = null;
	private IBaseQueryDefinition query;
	private IResultClass resultClass;
	private IGroupCalculator groupCalculator;
	private IProgressiveAggregationHelper aggrHelper;
	private boolean isClosed;
	private ICloseable closeable;
	
	/**
	 * 
	 * @param dataSourceQuery
	 * @param resultSet
	 * @param resultClass
	 * @param stopSign
	 * @throws DataException
	 */
	public SimpleResultSet( BaseQuery dataSourceQuery,
			final ResultSet resultSet, IResultClass resultClass,
			IEventHandler handler, GroupSpec[] groupSpecs, DataEngineSession session ) throws DataException
	{
		this.rowResultSet = new RowResultSet( new SmartCacheRequest( dataSourceQuery.getMaxRows( ),
				dataSourceQuery.getFetchEvents( ),
				new OdiAdapter( resultSet, resultClass ),
				resultClass,
				false ) );
		this.closeable = new ICloseable(){

			public void close( ) throws DataException
			{
				resultSet.close( );
				
			}};
		initialize( dataSourceQuery, resultClass, handler, groupSpecs, session );
				
	}

	public SimpleResultSet( BaseQuery dataSourceQuery,
			IDataSetPopulator populator, IResultClass resultClass,
			IEventHandler handler, GroupSpec[] groupSpecs, DataEngineSession session ) throws DataException
	{
		this.rowResultSet = new RowResultSet( new SmartCacheRequest( dataSourceQuery.getMaxRows( ),
				dataSourceQuery.getFetchEvents( ),
				new OdiAdapter( populator ),
				resultClass,
				false ) );
		this.closeable = (populator instanceof ICloseable)?(ICloseable)populator:null; 
		initialize( dataSourceQuery, resultClass, handler, groupSpecs, session );
				
	}
	private void initialize( BaseQuery dataSourceQuery,
			IResultClass resultClass, IEventHandler handler,
			GroupSpec[] groupSpecs, DataEngineSession session ) throws DataException
	{
		this.query = dataSourceQuery.getQueryDefinition( );
		this.groupCalculator = groupSpecs.length > 0
				? new SimpleGroupCalculator( session,
						groupSpecs,
						this.rowResultSet.getMetaData( ) )
				: new DummyGroupCalculator( );
		this.currResultObj = this.rowResultSet.next( );
		this.groupCalculator.registerCurrentResultObject( this.currResultObj );
		this.groupCalculator.registerNextResultObject( this.rowResultSet );
		this.initialRowCount = ( this.currResultObj != null ) ? -1 : 0;
		this.rowCount = ( this.currResultObj != null ) ? 1 : 0;
		this.handler = handler;
		this.resultSetNameSet = ResultSetUtil.getRsColumnRequestMap( handler.getAllColumnBindings( ) );
		if( query instanceof IQueryDefinition && ((IQueryDefinition)query).needAutoBinding( ))
		{
			for( int i = 1; i <= resultClass.getFieldCount( ); i++ )
			{
				this.resultSetNameSet.add( resultClass.getFieldName( i ) );
				this.resultSetNameSet.add( resultClass.getFieldAlias( i ) );
			}
		}
		
		Scriptable scope = session.getSharedScope( );
		
		AggrDefnManager manager = new AggrDefnManager( this.handler.getAggrDefinitions( ));
		this.aggrHelper = groupSpecs.length > 0? new ProgressiveAggregationHelper( manager,
				session.getTempDir( ),
				scope,
				session.getEngineContext( )
						.getScriptContext( ) ): new DummyAggregationHelper();
		this.groupCalculator.setAggrHelper( this.aggrHelper );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#close()
	 */
	public void close( ) throws DataException
	{
		if( this.isClosed )
			return;
		if ( this.closeable != null )
		{
			this.closeable.close( );
			this.closeable = null;
		}
		this.groupCalculator.close( );
		
		if ( this.dataSetStream != null )
		{
			try
			{
				if ( dataSetStream instanceof RAOutputStream )
				{
					( (RAOutputStream) dataSetStream ).seek( rowCountOffset );
					IOUtil.writeInt( dataSetStream, rowCount );
				}
				
				if ( this.streamsWrapper.getStreamForIndex( this.getResultClass( ), handler.getAppContext( ) )!= null )
				{
					Map<String, IIndexSerializer> hashes = this.streamsWrapper.getStreamForIndex( this.getResultClass( ), handler.getAppContext( ) );
					for( IIndexSerializer hash : hashes.values( ))
					{
						hash.close( );
					}
				}
				Map<String, StringTable> stringTables = this.streamsWrapper.getOutputStringTable( this.getResultClass( ) );
				for( StringTable stringTable : stringTables.values( ))
				{
					stringTable.close( );
				}
				if ( this.streamsWrapper.getStreamManager( )
						.hasOutStream( DataEngineContext.EXPR_VALUE_STREAM,
								StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE ) )
				{
					OutputStream exprValueStream = this.streamsWrapper.getStreamManager( )
							.getOutStream( DataEngineContext.EXPR_VALUE_STREAM,
									StreamManager.ROOT_STREAM,
									StreamManager.SELF_SCOPE );
					if ( exprValueStream instanceof RAOutputStream )
					{
						( (RAOutputStream) exprValueStream ).seek( 0 );
						IOUtil.writeInt( exprValueStream, rowCount );
					}

					exprValueStream.close( );
				}
				dataSetStream.close( );
				dataSetStream = null;
			}
			catch ( Exception e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
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
		this.isClosed = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#addIncrement(org.eclipse.birt.data.engine.impl.document.StreamWrapper, int, boolean)
	 */
	public void incrementalUpdate(StreamWrapper streamsWrapper, int originalRowCount,
			boolean isSubQuery) throws DataException
	{
		this.streamsWrapper = streamsWrapper;

		try
		{
			dataSetStream = this.streamsWrapper.getStreamManager( )
						.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
			OutputStream dlenStream = this.streamsWrapper.getStreamManager( )
						.getOutStream( DataEngineContext.DATASET_DATA_LEN_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
			if ( dataSetStream instanceof RAOutputStream )
			{
				rowCountOffset = ( (RAOutputStream) dataSetStream ).getOffset( );
				( (RAOutputStream) dataSetStream ).seek( ( (RAOutputStream) dataSetStream ).length( ) );
				offset = ( (RAOutputStream) dataSetStream ).getOffset( );
			}
			if ( dlenStream instanceof RAOutputStream )
			{
				( (RAOutputStream) dlenStream ).seek( ( (RAOutputStream) dlenStream ).length( ) );
			}
			dataSetLenStream = new DataOutputStream( dlenStream );
			this.rowCount += originalRowCount;
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
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
		this.groupCalculator.doSave( streamsWrapper.getStreamManager( ) );
		try
		{
			if ( streamsWrapper.getStreamForResultClass( ) != null )
			{
				( (ResultClass) populateResultClass( getResultClass( ) ) ).doSave( streamsWrapper.getStreamForResultClass( ),
						( this.query instanceof IQueryDefinition && ( (IQueryDefinition) this.query ).needAutoBinding( ) )
								? null : this.handler.getAllColumnBindings( ),
						streamsWrapper.getStreamManager( ).getVersion( ) );
				streamsWrapper.getStreamForResultClass( ).close( );
			}
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
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}

	private IResultClass populateResultClass( IResultClass meta )
			throws DataException
	{
		if( resultClass == null )
		{
			List<ResultFieldMetadata> list = new ArrayList<ResultFieldMetadata>( );
			for ( int i = 1; i <= meta.getFieldCount( ); i++ )
			{
				if ( !meta.getFieldName( i ).equals( ExprMetaUtil.POS_NAME ) )
					list.add( meta.getFieldMetaData( i ) );
			}
			resultClass = new ResultClass( list );
		}
		return resultClass;
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
		return this.aggrHelper.getAggrValue( aggrName, this );
	}
	
	public IProgressiveAggregationHelper getAggrHelper( ) throws DataException
	{
		return this.aggrHelper;
	}

	public Integer[] getGroupIndex( ) throws DataException
	{
		//For the first group, the group have not been populated yet.
		if( this.groupCalculator.getStartingGroup( ) == 0 )
		{
			Integer[] result = new Integer[this.groupCalculator.getGroupInstanceIndex( ).length];
			Arrays.fill( result, 0 );
			return result;
		}
		
		Integer[] groupIndex = this.groupCalculator.getGroupInstanceIndex( );
        Integer[] copy = new Integer[ groupIndex.length];
        System.arraycopy( groupIndex, 0, copy, 0, copy.length );		
		return copy;
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
		return this.groupCalculator.getEndingGroup( );
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
		return this.groupCalculator.getStartingGroup( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#last(int)
	 */
	public void last( int groupingLevel ) throws DataException
	{
		if( this.getEndingGroupLevel( ) <= groupingLevel )
			return;
		else
		{
			while( this.next( ))
			{
				if( this.getEndingGroupLevel( ) <= groupingLevel )
					return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#next()
	 */
	public boolean next( ) throws DataException
	{
		if ( currResultObj == null ) 
			return false;
		if ( this.streamsWrapper != null && currResultObj != null )
		{
			try 
			{
				if ( dataSetStream != null )
				{
					int colCount = this.populateResultClass( this.currResultObj.getResultClass( ) )
							.getFieldCount( );
					IOUtil.writeLong( dataSetLenStream, offset );

					offset += ResultSetUtil.writeResultObject( new DataOutputStream( dataSetStream ),
							currResultObj,
							colCount,
							resultSetNameSet,
							streamsWrapper.getOutputStringTable( getResultClass( ) ),
							streamsWrapper.getStreamForIndex( getResultClass( ), handler.getAppContext( ) ),
							this.rowCount-1 );
				}
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
		try
		{
			this.groupCalculator.next( this.rowResultSet.getIndex( ));
			this.groupCalculator.registerPreviousResultObject( this.currResultObj );
			this.currResultObj = this.rowResultSet.next( );
			this.groupCalculator.registerCurrentResultObject( this.currResultObj );
			this.groupCalculator.registerNextResultObject( this.rowResultSet );
		}
		catch ( DataException e )
		{
			this.currResultObj = null;
			throw e;
		}

		if ( this.currResultObj != null )
			rowCount++;
		return this.currResultObj != null;
	}
	
	public boolean aggrValueAvailable( String aggrName, int index ) throws DataException
	{
		return this.groupCalculator.isAggrAtIndexAvailable( aggrName, index);
	}
	
	private class DummyAggregationHelper implements IProgressiveAggregationHelper
	{

		public void onRow( int startingGroupLevel, int endingGroupLevel,
				IResultObject ro, int currentRowIndex ) throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public void close( ) throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public Object getLatestAggrValue( String name ) throws DataException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Object getAggrValue( String name, IResultIterator ri )
				throws DataException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public List getAggrValues( String name ) throws DataException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasAggr( String name ) throws DataException
		{
			// TODO Auto-generated method stub
			return false;
		}

		public Set<String> getAggrNames( ) throws DataException
		{
			// TODO Auto-generated method stub
			return new HashSet<String>();
		}

		public IAggrInfo getAggrInfo( String aggrName ) throws DataException
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private class DummyGroupCalculator implements IGroupCalculator
	{

		public void registerPreviousResultObject( IResultObject previous )
		{
			// TODO Auto-generated method stub
			
		}

		public void registerCurrentResultObject( IResultObject current )
		{
			// TODO Auto-generated method stub
			
		}

		public void registerNextResultObject( RowResultSet rowResultSet )
				throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public void next( int rowId ) throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public int getStartingGroup( ) throws DataException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public int getEndingGroup( ) throws DataException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public void close( ) throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public void doSave( StreamManager manager ) throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public void setAggrHelper( IProgressiveAggregationHelper aggrHelper )
				throws DataException
		{
			// TODO Auto-generated method stub
			
		}

		public boolean isAggrAtIndexAvailable( String aggrName, int currentIndex )
				throws DataException
		{
			// TODO Auto-generated method stub
			return false;
		}

		public Integer[] getGroupInstanceIndex( )
		{
			// TODO Auto-generated method stub
			return new Integer[0];
		}
		
	}
}
