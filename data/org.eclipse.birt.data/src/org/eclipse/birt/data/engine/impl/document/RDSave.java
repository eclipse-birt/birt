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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;

/**
 * Save expression value of every row into report document. The output format in
 * stream is, <expressCount, [expression id, expression value]*>.
 * 
 * When no value of one row is saved, the expressionCount will automatically be
 * ouptput as 0. It will happen when caller does not call getValue on this row
 * or call skipToEnd method.
 */
public class RDSave
{
	//	 
	private DataEngineContext context;
	private String queryResultID;
	
	// sub query info
	private String subQueryName;
	private String subQueryID;
	
	// row count
	private int rowCount;
	//
	private int lastRowIndex;

	//
	private OutputStream rowOs;
	private DataOutputStream rowDos;
	
	private OutputStream lenOs;
	private DataOutputStream lenDos;
	private byte[] zeroBytes;
	private int currentOffset;
	
	private IBaseQueryDefinition queryDefn;
	
	private Set nameSet = new HashSet( );
	
	public final static int columnSeparator = 1;
	public final static int rowSeparator = 2;
	public final static int endSeparator = 3;
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	public RDSave( DataEngineContext context, IBaseQueryDefinition queryDefn,
			String queryResultID, int rowCount, String subQueryName,
			int subQueryIndex )
			throws DataException
	{
		this.context = context;
		this.queryResultID = queryResultID;
		this.queryDefn = queryDefn;
		this.subQueryName = subQueryName;
		this.rowCount = rowCount;
		
		if ( subQueryName != null )
			this.subQueryID = subQueryName + "/" + subQueryIndex;
		
		this.lastRowIndex = 0;
	}
	
	/**
	 * init save environment
	 */
	private void initSave( boolean finish ) throws DataException
	{
		if ( rowDos == null )
		{
			VersionManager.setVersion( context, VersionManager.VERSION_2_1 );
			
			rowOs = context.getOutputStream( queryResultID,
					subQueryID,
					DataEngineContext.EXPR_VALUE_STREAM );
			rowDos = new DataOutputStream( rowOs );

			lenOs = context.getOutputStream( queryResultID,
					subQueryID,
					DataEngineContext.ROWLENGTH_INFO_STREAM );
			lenDos = new DataOutputStream( lenOs );
			
			try
			{
				int totalRowCount = 0;
				if ( finish == true )
					totalRowCount = rowCount;
				else
					totalRowCount = rowCount == 0 ? 1 : rowCount;
				
				// TODO: enhance me
				IOUtil.writeInt( rowDos, totalRowCount );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
			}
		}
	}
	
	private int currRowBytes = 0;
	private boolean rowStart = true;
	
	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 */
	public void saveExprValue( int currIndex, String exprID, Object exprValue )
			throws DataException
	{
		initSave( false );

		if ( currIndex != lastRowIndex )
		{
			try
			{
				if ( currIndex > 0 )
					this.currRowBytes += 4;
				
				saveEndOfCurrRow( lastRowIndex, currIndex );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Data" );
			}

			lastRowIndex = currIndex;
		}
		
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );
		
		try
		{
			if ( rowStart == true )
			{
				if ( currIndex > 0 )
					IOUtil.writeInt( rowDos, rowSeparator );
				
				IOUtil.writeInt( tempDos, columnSeparator );				
			}
			else
			{
				IOUtil.writeInt( tempDos, columnSeparator );
			}
			
			IOUtil.writeString( tempDos, exprID );
			IOUtil.writeObject( tempDos, exprValue );
			
			tempDos.flush( );
			tempBos.flush( );
			tempBaos.flush( );
			
			byte[] bytes = tempBaos.toByteArray( );
			currRowBytes += bytes.length;
			IOUtil.writeRawBytes( rowDos, bytes );

			tempBaos = null;
			tempBos = null;
			tempDos = null;
			
			rowStart = false;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
		
		nameSet.add( exprID );
	}

	/**
	 * Notify save needs to be finished
	 */
	public void saveFinish( int currIndex ) throws DataException
	{
		initSave( true );
		
		try
		{
			saveEndOfCurrRow( lastRowIndex, currIndex );
			
			rowDos.close( );
			rowOs.close( );
			
			lenDos.close( );
			lenOs.close( );
			
			// save expression metadata and transformation info
			this.saveForIV( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
		
	/**
	 * @param rowIndex
	 * @throws IOException
	 */
	private void saveEndOfCurrRow( int lastRowIndex, int currIndex )
			throws IOException
	{
		IOUtil.writeInt( lenDos, currentOffset );
		currentOffset += currRowBytes;
		this.rowStart = true;
		this.currRowBytes = 0;
				
		saveNullRowsBetween( lastRowIndex, currIndex );
	}
	
	/**
	 * @param lastRowIndex
	 * @param currIndex
	 * @throws IOException
	 */
	private void saveNullRowsBetween( int lastRowIndex, int currIndex )
			throws IOException
	{
		initZeroBytes( );
		
		int gapRows = currIndex - lastRowIndex - 1;
		for ( int i = 0; i < gapRows; i++ )
		{
			IOUtil.writeRawBytes( rowDos, zeroBytes );
			IOUtil.writeInt( lenDos, currentOffset );
			currentOffset += zeroBytes.length;
		}
	}
	
	/**
	 * @throws IOException
	 */
	private void initZeroBytes( ) throws IOException
	{
		if ( this.zeroBytes == null )
		{
			ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
			BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
			DataOutputStream tempDos = new DataOutputStream( tempBos );
			
			IOUtil.writeInt( tempDos, rowSeparator );
			tempDos.flush( );

			this.zeroBytes = tempBaos.toByteArray( );

			tempDos.close( );
			tempBos.close( );
			tempBaos.close( );
		}
	}
	
	/**
	 * @param odiResult
	 * @throws DataException
	 */
	public void saveResultIterator( CachedResultSet odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		if ( context != null
				&& context.getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			try
			{
				boolean isSubQuery = subQueryName != null;

				OutputStream streamForResultClass = null;
				OutputStream streamForGroupInfo = context.getOutputStream( queryResultID,
						subQueryID,
						DataEngineContext.GROUP_INFO_STREAM );
				OutputStream streamForResultData = null;
				
				// TODO: temp logic
//				streamForResultData = context.getOutputStream( queryResultID,
//							subQueryID,
//							DataEngineContext.DATASET_DATA_STREAM );
				
				if ( isSubQuery == false )
				{
					streamForResultClass = context.getOutputStream( queryResultID,
							subQueryID,
							DataEngineContext.RESULTCLASS_STREAM );
				}

				odiResult.doSave( streamForResultClass,
						streamForResultData,
						streamForGroupInfo,
						isSubQuery );

				streamForGroupInfo.close( );
				if ( streamForResultClass != null )
					streamForResultClass.close( );

				// notice, sub query name is used instead of sub query id
				if ( subQueryName != null
						&& context.hasStream( queryResultID,
								subQueryName,
								DataEngineContext.SUBQUERY_INFO_STREAM ) == false )
				{

					// save info related with sub query info
					OutputStream stream4 = context.getOutputStream( queryResultID,
							subQueryName,
							DataEngineContext.SUBQUERY_INFO_STREAM );
					RDSubQueryUtil.doSave( stream4, groupLevel, subQueryInfo );
					try
					{
						stream4.close( );
					}
					catch ( IOException e )
					{
						throw new DataException( ResourceConstants.RD_SAVE_ERROR,
								e,
								"Sub Query" );
					}
				}
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Set" );
			}
		}
	}
	
	/**
	 * @throws DataException
	 */
	private void saveForIV( ) throws DataException
	{
		this.saveExprMetadata( );
	}
	
	/**
	 * @throws DataException
	 */
	private void saveExprMetadata( ) throws DataException
	{
		OutputStream outputStream = context.getOutputStream( queryResultID,
				subQueryID,
				DataEngineContext.EXPR_META_STREAM );

		ExprMetaUtil.saveExprMetaInfo( queryDefn, nameSet, outputStream );

		try
		{
			outputStream.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
		}
	}
	
}
