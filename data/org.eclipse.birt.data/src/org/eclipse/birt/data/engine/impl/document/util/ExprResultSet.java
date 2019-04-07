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

package org.eclipse.birt.data.engine.impl.document.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.CacheProvider;
import org.eclipse.birt.data.engine.impl.document.IRDAggrUtil;
import org.eclipse.birt.data.engine.impl.document.IRDGroupUtil;
import org.eclipse.birt.data.engine.impl.document.ProgressiveViewingRDAggrUtil;
import org.eclipse.birt.data.engine.impl.document.RDAggrUtil;
import org.eclipse.birt.data.engine.impl.document.RDLoadUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;

/**
 * Read the data of expression and meantime provide the group related service.
 * It simulates the behavior of the IResultIterator of odiLayer. Used in the
 * presentation environment.
 */
public class ExprResultSet implements IExprResultSet
{	
	protected RAInputStream rowExprsRAIs;
	protected RAInputStream rowLenRAIs;
	protected RAInputStream rowInfoRAIs;

	protected int rowCount;
	protected int version;

	private boolean isBasedOnSecondRD;
	private int rowIdStartingIndex;
	
	protected IRDGroupUtil rdGroupUtil;
	protected IExprDataReader exprResultReader;

	protected StreamManager streamManager;
	
	protected IDataSetResultSet dataSetResultSet;
	
	protected String tempDir;
	
	private IRDAggrUtil aggrUtil = null;
	
	private IBaseQueryDefinition qd;
	
	private List<RAInputStream> aggrIndexStreams;
	private List<RAInputStream> aggrStreams;
	
	/**
	 * @param streamManager
	 * @param rdGroupUtil
	 * @throws DataException
	 */
	public ExprResultSet( String tempDir, StreamManager streamManager, int version,
			boolean isBasedOnSecondRD,  IDataSetResultSet dataSetResultSet, int rowIdStartingIndex,
			IBaseQueryDefinition qd ) throws DataException
	{
		this.tempDir = tempDir;
		this.streamManager = streamManager;
		this.version = version;
		this.isBasedOnSecondRD = isBasedOnSecondRD;
		this.dataSetResultSet = dataSetResultSet;
		this.rowIdStartingIndex = rowIdStartingIndex;
		this.qd = qd;
		this.prepare( );
		
		this.rdGroupUtil.setCacheProvider( new CacheProviderImpl( this ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getDataSetResultSet()
	 */
	public IDataSetResultSet getDataSetResultSet()
	{
		return this.dataSetResultSet;
	}
	
	/**
	 * @throws DataException
	 */
	protected void prepare( ) throws DataException
	{
		this.rdGroupUtil = RDLoadUtil.loadGroupUtil( tempDir, streamManager,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		if( version >=VersionManager.VERSION_2_5_1_0 )
		{
			aggrStreams = streamManager.getInStreams( DataEngineContext.AGGR_VALUE_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE ); 
			if ( streamManager.hasInStream( DataEngineContext.AGGR_INDEX_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE ) )
			{
				this.aggrUtil = new RDAggrUtil( streamManager, qd );
			}
			else if((!aggrStreams.isEmpty( )) || streamManager.hasInStream( DataEngineContext.COMBINED_AGGR_VALUE_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE ))
			{
				aggrIndexStreams = streamManager.getInStreams( DataEngineContext.AGGR_INDEX_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE ); 
				RAInputStream combinedAggrIndex = null;
				RAInputStream combinedAggrValue = null;
				if( streamManager.hasInStream( DataEngineContext.COMBINED_AGGR_INDEX_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE ))
				{
					combinedAggrIndex = streamManager.getInStream( DataEngineContext.COMBINED_AGGR_INDEX_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE );
				}
				
				if( streamManager.hasInStream( DataEngineContext.COMBINED_AGGR_VALUE_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE ))
				{
					combinedAggrValue = streamManager.getInStream( DataEngineContext.COMBINED_AGGR_VALUE_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE );
				}
				
				this.aggrUtil = new ProgressiveViewingRDAggrUtil( combinedAggrIndex, combinedAggrValue, aggrIndexStreams, aggrStreams );
			}
		}
		if ( this.isBasedOnSecondRD == false )
		{
			rowExprsRAIs = streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			if ( version > VersionManager.VERSION_2_0 )
			{
				rowLenRAIs = streamManager.getInStream( DataEngineContext.EXPR_ROWLEN_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE );
			}
			
			this.exprResultReader = new ExprDataReader1( this.rowExprsRAIs,
					this.rowLenRAIs,
					this.version,
					( this.qd instanceof IQueryDefinition && ( (IQueryDefinition) qd ).isSummaryQuery( ) )
							? null : this.dataSetResultSet );
			this.rowCount = exprResultReader.getCount( );
		}
		else
		{
			rowExprsRAIs = streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE );
			rowLenRAIs = streamManager.getInStream( DataEngineContext.EXPR_ROWLEN_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE );
			rowInfoRAIs = streamManager.getInStream( DataEngineContext.ROW_INDEX_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			this.exprResultReader = new ExprDataReader2( tempDir,
					rowExprsRAIs,
					rowLenRAIs,
					rowInfoRAIs, version, (this.qd instanceof IQueryDefinition && ( (IQueryDefinition) qd ).isSummaryQuery( ) )?null:this.dataSetResultSet );
			this.rowCount = this.exprResultReader.getCount( );			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#next()
	 */
	public boolean next( ) throws DataException
	{
		boolean hasNext = exprResultReader.next( );
		this.rdGroupUtil.next( hasNext );
		return hasNext;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getValue(java.lang.String)
	 */
	public Object getValue( String name ) throws DataException
	{
		Map exprValueMap = this.exprResultReader.getRowValue( );

		if ( exprValueMap == null )
			throw new DataException( ResourceConstants.RD_EXPR_RESULT_SET_NOT_START );

		if ( exprValueMap.containsKey( name ) == false )
		{
			if ( this.aggrUtil == null || !this.aggrUtil.contains( name ) )
				throw new DataException( ResourceConstants.RD_EXPR_INVALID_ERROR );
			else
			{
				return this.aggrUtil.getValue( name,
						this.aggrUtil.isRunningAggr( name )
								? this.getCurrentIndex( )
								: this.rdGroupUtil.getCurrentGroupIndex( this.aggrUtil.getGroupLevel( name ) ) );
			}
		}

		return exprValueMap.get( name );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws DataException
	{
		exprResultReader.moveTo( rowIndex );
		this.rdGroupUtil.move( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getCurrentId()
	 */
	public int getCurrentId( )
	{
		return this.rowIdStartingIndex + this.exprResultReader.getRowId( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getCurrentIndex()
	 */
	public int getCurrentIndex( )
	{
		return this.exprResultReader.getRowIndex( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		return this.rdGroupUtil.getStartingGroupLevel( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		return this.rdGroupUtil.getEndingGroupLevel( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#skipToEnd(int)
	 */
	public void skipToEnd( int groupLevel ) throws DataException
	{
		this.rdGroupUtil.last( groupLevel );
	}

	public int[] getGroupStartAndEndIndex( int groupIndex ) throws DataException
	{
		return this.rdGroupUtil.getGroupStartAndEndIndex( groupIndex );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#close()
	 */
	public void close( ) throws DataException
	{
		try
		{
			if ( exprResultReader != null )
			{
				exprResultReader.close( );
				exprResultReader = null;
			}
			if ( rowExprsRAIs != null )
			{
				rowExprsRAIs.close( );
				rowExprsRAIs = null;
			}
			if ( rowLenRAIs != null )
			{
				rowLenRAIs.close( );
				rowLenRAIs = null;
			}
			if ( rowInfoRAIs != null )
			{
				rowInfoRAIs.close( );
				rowInfoRAIs = null;
			}
			if( dataSetResultSet != null )
			{
				dataSetResultSet.close( );
				dataSetResultSet = null;
			}
			if( aggrUtil!= null )
			{
				aggrUtil.close( );
				aggrUtil = null;
			}
			if( rdGroupUtil != null )
			{
				rdGroupUtil.close( );
				rdGroupUtil = null;
			}
			if( aggrIndexStreams!= null )
			{
				for( int i=0; i< aggrIndexStreams.size( ); i++ )
				{
					aggrIndexStreams.get( i ).close( );
				}
			}
			if( aggrStreams!= null )
			{
				for( int i=0; i< aggrStreams.size( ); i++ )
				{
					aggrStreams.get( i ).close( );
				}
			}
		}
		catch ( IOException e )
		{
			// ignore throw new DataException( "error in close" );
		}
	}
	
	/**
	 * Provider group info function for RDLoad
	 */
	private static class CacheProviderImpl implements CacheProvider
	{

		// loader instance
		private ExprResultSet exprResultSet;

		/**
		 * @param loader
		 */
		public CacheProviderImpl( ExprResultSet exprResultSet )
		{
			this.exprResultSet = exprResultSet;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#getCount()
		 */
		public int getCount( )
		{
			return exprResultSet.rowCount;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#getCurrentIndex()
		 */
		public int getCurrentIndex( )
		{
			return exprResultSet.getCurrentIndex( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#moveTo(int)
		 */
		public void moveTo( int destIndex ) throws DataException
		{
			int currIndex = exprResultSet.getCurrentIndex( );
			assert destIndex >= currIndex;

			int forwardSteps = destIndex - currIndex;
			for ( int i = 0; i < forwardSteps; i++ )
				exprResultSet.next( );
		}
		
		public boolean next ( ) throws DataException
		{
			return this.exprResultSet.next( );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#isEmpty()
	 */
	public boolean isEmpty( )
	{
		return rowCount == 0 ? true : false;
	}

	public List[] getGroupInfos( ) throws DataException
	{
		return this.rdGroupUtil.getGroups( );
	}
}
