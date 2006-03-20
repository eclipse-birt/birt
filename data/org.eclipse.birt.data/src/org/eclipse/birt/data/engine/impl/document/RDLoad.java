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

package org.eclipse.birt.data.engine.impl.document;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Load data from input stream and it simulates the behavior of the
 * IResultIterator of odiLayer.
 */
class RDLoad
{
	//	 
	private DataEngineContext context;
	private String queryResultID;

	// sub query info
	private String subQueryName;
	private String subQueryID;
	private RDSubQueryUtil subQueryUtil;

	//
	private RDGroupUtil rdGroupUtil;

	//
	private int rowCount;
	private int readIndex = 0;
	private int currPos = 0;

	private InputStream rowIs;
	private BufferedInputStream rowBis;
	private DataInputStream rowDis;
	
	private int[] rowLength;

	private int version;
	
	// expression value map
	private Map exprValueMap = new HashMap( );

	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param currParentIndex
	 * @throws DataException
	 */
	RDLoad( DataEngineContext context, String queryResultID,
			String subQueryName, int currParentIndex ) throws DataException
	{
		this.context = context;
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.version = VersionManager.getVersion( context );

		if ( subQueryName != null )
			this.subQueryID = subQueryName
					+ "/" + getSubQueryIndex( currParentIndex );
	}

	/**
	 * @return result meta data
	 * @throws DataException
	 */
	ResultMetaData loadResultMetaData( ) throws DataException
	{
		InputStream stream = context.getInputStream( queryResultID,
				subQueryName,
				DataEngineContext.RESULTCLASS_STREAM );
		IResultClass resultClass = new ResultClass( stream );
		try
		{
			stream.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Class" );
		}

		return new ResultMetaData( resultClass );
	}

	/**
	 * @return
	 * @throws DataException
	 */
	boolean next( ) throws DataException
	{
		if ( rowDis == null )
		{
			rowIs = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.EXPR_VALUE_STREAM );
			rowBis = new BufferedInputStream( rowIs );
			try
			{
				rowDis = new DataInputStream( rowBis );
				rowCount = IOUtil.readInt( rowDis );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_LOAD_ERROR,
						e,
						"result data" );
			}
			
			if ( this.version == VersionManager.VERSION_2_1 )
			{
				InputStream lenIs = context.getInputStream( queryResultID,
						subQueryID,
						DataEngineContext.ROWLENGTH_INFO_STREAM );
				BufferedInputStream lenBis = new BufferedInputStream( lenIs );
				DataInputStream lenDis = new DataInputStream( lenBis );
				this.rowLength = new int[rowCount];
				try
				{
					for ( int i = 0; i < rowCount; i++ )
						this.rowLength[i] = IOUtil.readInt( lenDis );
				}
				catch ( IOException e )
				{
					throw new DataException( ResourceConstants.RD_LOAD_ERROR,
							e,
							"row length" );
				}
				try
				{
					lenDis.close( );
					lenBis.close( );
					lenIs.close( );
				}
				catch ( IOException e )
				{
					// ignore it
				}
			}
			
			InputStream groupIs = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.GROUP_INFO_STREAM );
			BufferedInputStream groupBis = new BufferedInputStream( groupIs );
			rdGroupUtil = new RDGroupUtil( groupBis, new CacheProviderImpl( this ) );
			try
			{
				groupBis.close( );
				groupIs.close( );
			}
			catch ( IOException e )
			{
				// ignore it
			}
		}

		boolean hasNext = currPos++ < rowCount;
		this.rdGroupUtil.next( hasNext );
		
		return hasNext;
	}

	/**
	 * @return
	 */
	int getCurrentIndex( )
	{
		return this.currPos - 1;
	}

	/**
	 * @return
	 */
	private int getCount( )
	{
		return this.rowCount;
	}

	/**
	 * @param expr
	 * @throws DataException 
	 */
	Object getValue( IBaseExpression expr ) throws DataException
	{
		try
		{
			if ( readIndex < currPos )
			{
				this.skipTo( currPos - 1 );
				this.loadCurrentRow( );
			}
			readIndex = currPos;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}

		if ( exprValueMap.containsKey( expr.getID( ) ) == false )
			throw new DataException( ResourceConstants.RD_EXPR_INVALID_ERROR );

		return exprValueMap.get( expr.getID( ) );
	}

	/**
	 * @param rowIndex
	 */
	void moveTo( int rowIndex ) throws DataException
	{
		if ( rowIndex < 0 || rowIndex >= this.rowCount )
			throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
					new Integer( rowIndex ) );
		else if ( rowIndex < this.getCurrentIndex( ) )
			throw new DataException( ResourceConstants.BACKWARD_SEEK_ERROR );
		else if ( rowIndex == this.getCurrentIndex( ) )
			return;

		this.currPos = rowIndex + 1;
	}
	
	/**
	 * @param absoluteIndex
	 * @throws IOException
	 */
	private void skipTo( int absoluteIndex ) throws IOException
	{
		if ( version == VersionManager.VERSION_2_0 )
		{
			int exprCount;
			int gapRows = absoluteIndex - readIndex;
			for ( int j = 0; j < gapRows; j++ )
			{
				exprCount = IOUtil.readInt( rowDis );
				for ( int i = 0; i < exprCount; i++ )
				{
					IOUtil.readString( rowDis );
					IOUtil.readObject( rowDis );
				}
			}
		}
		else
		{
			int skipBytesLen = 0;
			int gapRows = absoluteIndex - readIndex;
			for ( int j = 0; j < gapRows; j++ )
				skipBytesLen += rowLength[readIndex + j];

			if ( skipBytesLen > 0 )
				this.rowDis.skipBytes( skipBytesLen );

			readIndex = absoluteIndex;
		}
	}
	
	/**
	 * @throws IOException
	 */
	private void loadCurrentRow( ) throws IOException
	{
		exprValueMap.clear( );
		int exprCount = IOUtil.readInt( rowDis );
		for ( int i = 0; i < exprCount; i++ )
		{
			String exprID = IOUtil.readString( rowDis );
			Object exprValue = IOUtil.readObject( rowDis );
			exprValueMap.put( exprID, exprValue );
		}
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	int getSubQueryIndex( int currParentIndex ) throws DataException
	{
		subQueryUtil = new RDSubQueryUtil( this.context,
				this.queryResultID,
				this.subQueryName );
		return subQueryUtil.getSubQueryIndex( currParentIndex );
	}

	/**
	 * @param groupLevel
	 * @throws DataException
	 */
	void skipToEnd( int groupLevel ) throws DataException
	{
		this.rdGroupUtil.last( groupLevel );
	}

	/**
	 * @return
	 * @throws DataException
	 */
	int getStartingGroupLevel( ) throws DataException
	{
		return this.rdGroupUtil.getStartingGroupLevel( );
	}

	/**
	 * @return
	 * @throws DataException
	 */
	int getEndingGroupLevel( ) throws DataException
	{
		return this.rdGroupUtil.getEndingGroupLevel( );
	}

	/**
	 * @throws DataException
	 */
	public void close( ) throws DataException
	{
		try
		{
			if ( rowDis != null )
			{
				rowDis.close( );
				rowBis.close( );
				rowIs.close();
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
		private RDLoad rdLoader;

		/**
		 * @param loader
		 */
		public CacheProviderImpl( RDLoad loader )
		{
			assert loader != null;
			
			this.rdLoader = loader;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#getCount()
		 */
		public int getCount( )
		{
			return rdLoader.getCount( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#getCurrentIndex()
		 */
		public int getCurrentIndex( )
		{
			return rdLoader.getCurrentIndex( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.rd2.CacheProvider#moveTo(int)
		 */
		public void moveTo( int destIndex ) throws DataException
		{
			int currIndex = rdLoader.getCurrentIndex( );
			assert destIndex >= currIndex;

			int forwardSteps = destIndex - currIndex;
			for ( int i = 0; i < forwardSteps; i++ )
				rdLoader.next( );
		}
	}

}
