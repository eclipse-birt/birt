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
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.CacheProvider;
import org.eclipse.birt.data.engine.impl.document.RDGroupUtil;
import org.eclipse.birt.data.engine.impl.document.RDLoadUtil;
import org.eclipse.birt.data.engine.impl.document.StreamManager;
import org.eclipse.birt.data.engine.impl.document.VersionManager;

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
	private int version;

	private boolean isBasedOnSecondRD;
	
	protected RDGroupUtil rdGroupUtil;
	protected IExprDataReader exprResultReader;

	protected StreamManager streamManager;

	/**
	 * @param streamManager
	 * @param rdGroupUtil
	 * @throws DataException
	 */
	public ExprResultSet( StreamManager streamManager, int version,
			boolean isBasedOnSecondRD ) throws DataException
	{
		this.streamManager = streamManager;
		this.version = version;
		this.isBasedOnSecondRD = isBasedOnSecondRD;
		
		this.prepare( );
		
		this.rdGroupUtil.setCacheProvider( new CacheProviderImpl( this ) );
	}

	/**
	 * @throws DataException
	 */
	protected void prepare( ) throws DataException
	{
		this.rdGroupUtil = RDLoadUtil.loadGroupUtil( streamManager,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		
		if ( this.isBasedOnSecondRD == false )
		{
			rowExprsRAIs = streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			if ( version == VersionManager.VERSION_2_1 )
			{
				rowLenRAIs = streamManager.getInStream( DataEngineContext.EXPR_ROWLEN_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE );
			}
			
			this.exprResultReader = new ExprDataReader1( this.rowExprsRAIs,
					this.rowLenRAIs,
					this.version );
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
			this.exprResultReader = new ExprDataReader2( rowExprsRAIs,
					rowLenRAIs,
					rowInfoRAIs );
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
			throw new DataException( ResourceConstants.RD_EXPR_RESULT_SET_NOT_START);
		
		if ( exprValueMap.containsKey( name ) == false )
			throw new DataException( ResourceConstants.RD_EXPR_INVALID_ERROR );

		return exprValueMap.get( name );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws DataException
	{
		int currIndex = this.getCurrentIndex( );

		if ( rowIndex < 0 || rowIndex >= this.rowCount )
			throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
					new Integer( rowIndex ) );
		else if ( rowIndex < currIndex )
			throw new DataException( ResourceConstants.BACKWARD_SEEK_ERROR );
		else if ( rowIndex == currIndex )
			return;

		int gapValue = rowIndex - currIndex;
		for ( int i = 0; i < gapValue; i++ )
			this.next( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#getCurrentId()
	 */
	public int getCurrentId( )
	{
		return this.exprResultReader.getRowId( );
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
		}
		catch ( IOException e )
		{
			// ignore throw new DataException( "error in close" );
		}
	}
	
	/**
	 * Provider group info function for RDLoad
	 */
	private class CacheProviderImpl implements CacheProvider
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
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.document.util.IExprResultSet#isEmpty()
	 */
	public boolean isEmpty( )
	{
		return rowCount == 0 ? true : false;
	}

}
