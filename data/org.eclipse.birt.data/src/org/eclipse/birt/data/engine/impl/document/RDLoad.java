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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
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
	
	private InputStream lenIs;
	private BufferedInputStream lenBis;
	private DataInputStream lenDis;
	
	private int currSkipIndex;	
	private int INT_LENGTH;
	
	private int version;
	
	// whether needed operation is done
	private boolean isPrepared;
	
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
		this.INT_LENGTH = IOUtil.INT_LENGTH;
		
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
	 * @throws DataException
	 */
	private void prepare( ) throws DataException
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
			
			InputStream groupIs = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.GROUP_INFO_STREAM );
			BufferedInputStream groupBis = new BufferedInputStream( groupIs );
			rdGroupUtil = new RDGroupUtil( groupBis,
					new CacheProviderImpl( this ) );
			
			try
			{
				groupBis.close( );
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
	 * @return
	 * @throws DataException
	 */
	public ExprDataResultSet loadExprDataResultSet( ) throws DataException
	{
		if ( this.version == VersionManager.VERSION_2_0 )
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
		
		InputStream inputStream = context.getInputStream( queryResultID,
				subQueryID,
				DataEngineContext.EXPR_VALUE_STREAM );
		BufferedInputStream bis = new BufferedInputStream( inputStream );
		
		ExprDataResultSet exprDataResultSet = new ExprDataResultSet( bis,
				exprMetas,
				new ResultClass( newProjectedColumns ) );
				
		return exprDataResultSet;
	}
	
	/**
	 * @throws DataException
	 */
	private ExprMetaInfo[] loadExprMetadata( ) throws DataException
	{
		InputStream inputStream = context.getInputStream( queryResultID,
				subQueryID,
				DataEngineContext.EXPR_META_STREAM );
		BufferedInputStream bis = new BufferedInputStream( inputStream );

		ExprMetaInfo[] exprMetas = ExprMetaUtil.loadExprMetaInfo( bis );
		
		try
		{
			bis.close( );
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
		if ( this.rdGroupUtil == null )
		{
			InputStream stream = context.getInputStream( queryResultID,
					subQueryID,
					DataEngineContext.GROUP_INFO_STREAM );
			BufferedInputStream bis2 = new BufferedInputStream( stream );
			rdGroupUtil = new RDGroupUtil( bis2 );
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
		
		return this.rdGroupUtil;
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
			initSkipData( );

			int gapRow = readIndex - currSkipIndex;
			if ( gapRow > 0 )
				this.lenDis.skipBytes( gapRow * INT_LENGTH );
			int rowOffsetRead = IOUtil.readInt( lenDis );
			currSkipIndex = readIndex + 1;
			
			gapRow = absoluteIndex - currSkipIndex;
			if ( gapRow > 0 )
				this.lenDis.skipBytes( gapRow * INT_LENGTH );
			int rowOffsetAbsolute = IOUtil.readInt( lenDis );
			currSkipIndex = absoluteIndex + 1;
			
			int skipBytesLen = rowOffsetAbsolute - rowOffsetRead;

			if ( skipBytesLen > 0 )
				this.rowDis.skipBytes( skipBytesLen );

			readIndex = absoluteIndex;
		}
	}
	
	/**
	 * @throws DataException
	 */
	private void initSkipData( ) throws DataException
	{
		if ( this.lenDis != null )
			return;
		
		lenIs = context.getInputStream( queryResultID,
				subQueryID,
				DataEngineContext.ROWLENGTH_INFO_STREAM );
		lenBis = new BufferedInputStream( lenIs );
		lenDis = new DataInputStream( lenBis );
	}
	
	/**
	 * @throws IOException
	 */
	private void initCurrentRow( ) throws IOException
	{
		exprValueMap.clear( );
		if ( version == VersionManager.VERSION_2_0 )
		{
			int exprCount = IOUtil.readInt( rowDis );
			for ( int i = 0; i < exprCount; i++ )
			{
				String exprID = IOUtil.readString( rowDis );
				Object exprValue = IOUtil.readObject( rowDis );
				exprValueMap.put( exprID, exprValue );
			}
		}
		else
		{
			while ( true )
			{
				if ( getSeperator( rowDis ) == RDSave.columnSeparator )
				{
					String exprID = IOUtil.readString( rowDis );
					Object exprValue = IOUtil.readObject( rowDis );
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
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static int getSeperator( InputStream inputStream ) throws IOException
	{
		byte[] bytes = new byte[4];
		int len = inputStream.read( bytes );
		if ( len == -1 )
			return RDSave.endSeparator;
		else if ( len < 4 )
		{
			for ( int i = len; i < 4; i++ )
			{
				bytes[i] = (byte) inputStream.read( );
				if ( bytes[i] == -1 )
					throw new IOException( "there is an error in reading result set" );
			}
		}

		return IOUtil.getInt( bytes );
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
				rowIs.close( );
				
				if ( lenDis != null )
				{
					lenDis.close( );
					lenBis.close( );
					lenIs.close( );
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
