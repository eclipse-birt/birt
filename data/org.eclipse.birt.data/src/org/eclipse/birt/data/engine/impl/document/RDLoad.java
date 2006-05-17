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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprDataResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Load data from input stream and it simulates the behavior of the
 * IResultIterator of odiLayer.
 */
public class RDLoad
{
	//
	private int rowCount;
	private int readIndex = 0;
	private int currPos = 0;

	private InputStream rowExprsIs;
	private DataInputStream rowExprsDis;
	
	private InputStream rowLenIs;
	private DataInputStream rowLenDis;
	
	private int currSkipIndex;	
	private int INT_LENGTH;
	
	private int version;
	
	// whether needed operation is done
	private boolean isPrepared;
	
	// expression value map
	private Map exprValueMap = new HashMap( );
	
	//
	private RDGroupUtil rdGroupUtil;
	//
	private RDSubQueryUtil subQueryUtil;
	//
	private LoadUtilHelper loadUtilHelper;
	//
	private StreamManager streamManager;
	
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
		this.version = VersionManager.getVersion( context );
		this.INT_LENGTH = IOUtil.INT_LENGTH;
		
		loadUtilHelper = new LoadUtilHelper( );
		subQueryUtil = new RDSubQueryUtil( context, queryResultID, subQueryName );
		streamManager = new StreamManager( context,
				queryResultID,
				subQueryName,
				subQueryName == null ? 0 : getSubQueryIndex( currParentIndex ) );
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	boolean next( ) throws DataException
	{
		if ( this.isPrepared == false )
		{
			this.prepare( );
			this.isPrepared = true;
		}
		
		boolean hasNext = currPos++ < rowCount;
		this.rdGroupUtil.next( hasNext );
		
		return hasNext;
	}
	
	/**
	 * @throws DataException
	 */
	private void prepare( ) throws DataException
	{
		if ( rowExprsDis == null )
		{
			rowExprsIs = streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM );
			rowExprsDis = new DataInputStream( rowExprsIs );
			try
			{	
				rowCount = IOUtil.readInt( rowExprsDis );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_LOAD_ERROR,
						e,
						"result data" );
			}
			
			InputStream groupIs = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM );
			rdGroupUtil = new RDGroupUtil( groupIs,
					new CacheProviderImpl( this ) );			
			try
			{
				groupIs.close( );
			}
			catch ( IOException e )
			{
				// ignore it
			}
			
			this.isPrepared = true;
		}
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
				this.initCurrentRow( );
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
	 * @param name
	 * @return
	 * @throws DataException
	 */
	Object getValue( String name ) throws DataException
	{
		try
		{
			if ( readIndex < currPos )
			{
				this.skipTo( currPos - 1 );
				this.initCurrentRow( );
			}
			readIndex = currPos;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Data" );
		}

		if ( exprValueMap.containsKey( name ) == false )
			throw new DataException( ResourceConstants.RD_EXPR_INVALID_ERROR );

		return exprValueMap.get( name );
	}
	
	/**
	 * @param rowIndex
	 */
	void moveTo( int rowIndex ) throws DataException
	{
		if ( this.isPrepared == false )
		{
			this.prepare( );
			this.isPrepared = true;
		}
		
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
	
	/**
	 * @param absoluteIndex
	 * @throws IOException
	 * @throws DataException 
	 */
	private void skipTo( int absoluteIndex ) throws IOException, DataException
	{
		if ( readIndex == absoluteIndex )
			return;

		if ( version == VersionManager.VERSION_2_0 )
		{
			int exprCount;
			int gapRows = absoluteIndex - readIndex;
			for ( int j = 0; j < gapRows; j++ )
			{
				exprCount = IOUtil.readInt( rowExprsDis );
				for ( int i = 0; i < exprCount; i++ )
				{
					IOUtil.readString( rowExprsDis );
					IOUtil.readObject( rowExprsDis );
				}
			}
		}
		else
		{
			initSkipData( );

			int gapRow = readIndex - currSkipIndex;
			if ( gapRow > 0 )
				this.rowLenDis.skipBytes( gapRow * INT_LENGTH );
			int rowOffsetRead = IOUtil.readInt( rowLenDis );
			currSkipIndex = readIndex + 1;
			
			gapRow = absoluteIndex - currSkipIndex;
			if ( gapRow > 0 )
				this.rowLenDis.skipBytes( gapRow * INT_LENGTH );
			int rowOffsetAbsolute = IOUtil.readInt( rowLenDis );
			currSkipIndex = absoluteIndex + 1;
			
			int skipBytesLen = rowOffsetAbsolute - rowOffsetRead;

			if ( skipBytesLen > 0 )
				this.rowExprsDis.skipBytes( skipBytesLen );

			readIndex = absoluteIndex;
		}
	}
	
	/**
	 * @throws DataException
	 */
	private void initSkipData( ) throws DataException
	{
		if ( this.rowLenDis != null )
			return;
		
		rowLenIs = streamManager.getInStream( DataEngineContext.ROWLENGTH_INFO_STREAM );
		rowLenDis = new DataInputStream( rowLenIs );
	}
	
	/**
	 * @throws IOException
	 */
	private void initCurrentRow( ) throws IOException
	{
		exprValueMap.clear( );
		if ( version == VersionManager.VERSION_2_0 )
		{
			int exprCount = IOUtil.readInt( rowExprsDis );
			for ( int i = 0; i < exprCount; i++ )
			{
				String exprID = IOUtil.readString( rowExprsDis );
				Object exprValue = IOUtil.readObject( rowExprsDis );
				exprValueMap.put( exprID, exprValue );
			}
		}
		else
		{
			while ( true )
			{
				if ( RDIOUtil.getSeperator( rowExprsDis ) == RDIOUtil.ColumnSeparator )
				{
					String exprID = IOUtil.readString( rowExprsDis );
					Object exprValue = IOUtil.readObject( rowExprsDis );
					exprValueMap.put( exprID, exprValue );
				}
				else
				{
					break;
				}
			}
		}
	}
	
	/**
	 * @return
	 */
	int getCurrentId( )
	{
		return this.getCurrentIndex( );
	}
	
	/**
	 * @return
	 */
	int getCurrentIndex( )
	{
		return this.currPos - 1;
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
			if ( rowExprsDis != null )
			{
				rowExprsDis.close( );
				rowExprsIs.close( );
				
				if ( rowLenDis != null )
				{
					rowLenDis.close( );
					rowLenIs.close( );
				}
			}
		}
		catch ( IOException e )
		{
			// ignore throw new DataException( "error in close" );
		}
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public RDGroupUtil loadGroupUtil( ) throws DataException
	{
		if ( this.rdGroupUtil == null )
			rdGroupUtil = loadUtilHelper.loadGroupUtil( );
		
		return this.rdGroupUtil;
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData loadResultMetaData( ) throws DataException
	{
		return loadUtilHelper.loadResultMetaData( );
	}

	/**
	 * @return
	 * @throws DataException
	 */
	public ExprDataResultSet loadExprDataResultSet( ) throws DataException
	{
		return loadUtilHelper.loadExprDataResultSet( );
	}

	/**
	 * @param currParentIndex
	 * @return
	 * @throws DataException
	 */
	public int getSubQueryIndex( int currParentIndex ) throws DataException
	{
		return subQueryUtil.getSubQueryIndex( currParentIndex );
	}

	/**
	 *
	 */
	private class LoadUtilHelper
	{
		/**
		 * @return result meta data
		 * @throws DataException
		 */
		ResultMetaData loadResultMetaData( ) throws DataException
		{
			InputStream stream = streamManager.getSubInStream( DataEngineContext.RESULTCLASS_STREAM );
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
		public ExprDataResultSet loadExprDataResultSet( ) throws DataException
		{
			if ( version == VersionManager.VERSION_2_0 )
				throw new DataException( "Not supported in earlier version" );
			
			ExprMetaInfo[] exprMetas = loadExprMetadata( );

			List newProjectedColumns = new ArrayList( );
			for ( int i = 0; i < exprMetas.length; i++ )
			{
				String name = exprMetas[i].getName( );
				Class clazz = DataType.getClass( exprMetas[i].getDataType( ) );
				ResultFieldMetadata metaData = new ResultFieldMetadata( 0,
						name,
						name,
						clazz,
						clazz == null ? null : clazz.toString( ),
						i == exprMetas.length - 1 ? true : false );
				newProjectedColumns.add( metaData );
			}
			
			InputStream inputStream = streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM );
			ExprDataResultSet exprDataResultSet = new ExprDataResultSet( inputStream,
					exprMetas,
					new ResultClass( newProjectedColumns ) );
					
			return exprDataResultSet;
		}
		
		/**
		 * @throws DataException
		 */
		private ExprMetaInfo[] loadExprMetadata( ) throws DataException
		{
			InputStream inputStream = streamManager.getInStream( DataEngineContext.EXPR_META_STREAM );
			ExprMetaInfo[] exprMetas = ExprMetaUtil.loadExprMetaInfo( inputStream );
			
			try
			{
				inputStream.close( );
			}
			catch ( IOException e )
			{
				// ignore
			}
			
			return exprMetas;
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public RDGroupUtil loadGroupUtil( ) throws DataException
		{
			InputStream stream = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM );
			RDGroupUtil rdGroupUtil = new RDGroupUtil( stream );
			try
			{
				stream.close( );
			}
			catch ( IOException e )
			{
				// ignore it
			}

			return rdGroupUtil;
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
			return rdLoader.rowCount;
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
