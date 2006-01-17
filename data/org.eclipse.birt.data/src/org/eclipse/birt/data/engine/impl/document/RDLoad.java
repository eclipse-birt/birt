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
public class RDLoad
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
	private int readPos = 0;
	private int currPos = 0;

	private InputStream is;
	private BufferedInputStream bis;
	private DataInputStream dis;

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
		if ( dis == null )
		{
			is = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.EXPR_VALUE_STREAM );
			bis = new BufferedInputStream( is );
			try
			{
				dis = new DataInputStream( bis );
				rowCount = IOUtil.readInt( dis );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_LOAD_ERROR,
						e,
						"result data" );
			}

			InputStream stream = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.GROUP_INFO_STREAM );
			BufferedInputStream bis2 = new BufferedInputStream( stream );
			rdGroupUtil = new RDGroupUtil( bis2, new CacheProviderImpl( this ) );
			try
			{
				bis2.close( );
				stream.close( );
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
			if ( readPos < currPos )
			{
				int exprCount;
				int gapRows = currPos - readPos - 1;
				for ( int j = 0; j < gapRows; j++ )
				{
					exprCount = IOUtil.readInt( dis );
					for ( int i = 0; i < exprCount; i++ )
					{
						IOUtil.readString( dis );
						IOUtil.readObject( dis );
					}
				}

				exprValueMap.clear( );
				exprCount = IOUtil.readInt( dis );
				for ( int i = 0; i < exprCount; i++ )
				{
					String exprID = IOUtil.readString( dis );
					Object exprValue = IOUtil.readObject( dis );
					exprValueMap.put( exprID, exprValue );
				}
			}
			readPos = currPos;
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
			if ( dis != null )
			{
				dis.close( );
				bis.close( );
				is.close();
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
