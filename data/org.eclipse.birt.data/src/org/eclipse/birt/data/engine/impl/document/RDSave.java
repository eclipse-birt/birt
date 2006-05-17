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

	//
	private OutputStream rowExprsOs;
	private DataOutputStream rowExprsDos;
	private byte[] zeroBytes;
	
	//
	private OutputStream rowLenOs;
	private DataOutputStream rowLenDos;
	private int currentOffset;
	private int currRowBytes = 0;

	// row count
	private int rowCount;
	private boolean rowStart = true;
	private int lastRowIndex;
	
	//
	private Set exprNameSet = new HashSet( );	
	
	//
	private SaveUtilHelper saveUtilHelper;
	private StreamManager streamManager;
	
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
		this.rowCount = rowCount;

		this.saveUtilHelper = new SaveUtilHelper( queryDefn );
		this.streamManager = new StreamManager( context,
				queryResultID,
				subQueryName,
				subQueryIndex );
		
		this.lastRowIndex = 0;
	}
	
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
			this.saveWhenEndOneRow( currIndex );
			lastRowIndex = currIndex;
		}
		
		saveWhenInOneRow( currIndex, exprID, exprValue );
	}

	/**
	 * @param currIndex
	 * @throws DataException
	 */
	private void saveWhenEndOneRow( int currIndex ) throws DataException
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
	}
	
	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 * @throws DataException
	 */
	private void saveWhenInOneRow( int currIndex, String exprID,
			Object exprValue ) throws DataException
	{
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream( );
		BufferedOutputStream tempBos = new BufferedOutputStream( tempBaos );
		DataOutputStream tempDos = new DataOutputStream( tempBos );

		try
		{
			if ( rowStart == true )
			{
				if ( currIndex > 0 )
					IOUtil.writeInt( rowExprsDos, RDIOUtil.RowSeparator );

				IOUtil.writeInt( tempDos, RDIOUtil.ColumnSeparator );
			}
			else
			{
				IOUtil.writeInt( tempDos, RDIOUtil.ColumnSeparator );
			}

			IOUtil.writeString( tempDos, exprID );
			IOUtil.writeObject( tempDos, exprValue );

			tempDos.flush( );
			tempBos.flush( );
			tempBaos.flush( );

			byte[] bytes = tempBaos.toByteArray( );
			currRowBytes += bytes.length;
			IOUtil.writeRawBytes( rowExprsDos, bytes );

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

		exprNameSet.add( exprID );
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
			
			rowExprsDos.close( );
			rowExprsOs.close( );
			
			rowLenDos.close( );
			rowLenOs.close( );
			
			// save expression metadata and transformation info
			saveUtilHelper.saveForIV( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
	
	/**
	 * init save environment
	 */
	private void initSave( boolean finish ) throws DataException
	{
		if ( rowExprsDos == null )
		{
			VersionManager.setVersion( context, VersionManager.VERSION_2_1 );
			
			rowExprsOs = streamManager.getOutStream( DataEngineContext.EXPR_VALUE_STREAM );
			rowExprsDos = new DataOutputStream( rowExprsOs );

			rowLenOs = streamManager.getOutStream( DataEngineContext.ROWLENGTH_INFO_STREAM );
			rowLenDos = new DataOutputStream( rowLenOs );
			
			try
			{
				int totalRowCount = 0;
				if ( finish == true )
					totalRowCount = rowCount;
				else
					totalRowCount = rowCount == 0 ? 1 : rowCount;
				
				// TODO: enhance me
				IOUtil.writeInt( rowExprsDos, totalRowCount );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
			}
		}
	}
	
	/**
	 * @param rowIndex
	 * @throws IOException
	 */
	private void saveEndOfCurrRow( int lastRowIndex, int currIndex )
			throws IOException
	{
		IOUtil.writeInt( rowLenDos, currentOffset );
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
			IOUtil.writeRawBytes( rowExprsDos, zeroBytes );
			IOUtil.writeInt( rowLenDos, currentOffset );
			currentOffset += zeroBytes.length;
		}
	}
	
	/**
	 * @throws IOException
	 */
	private void initZeroBytes( ) throws IOException
	{
		if ( this.zeroBytes == null )
			this.zeroBytes = RDIOUtil.getZeroBytes( );
	}
	
	/**
	 * Save below information into report document:
	 * 		result class
	 * 		group information
	 * 		subquery information
	 *  
	 * @param odiResult
	 * @throws DataException
	 */
	public void saveResultIterator( CachedResultSet odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		saveUtilHelper.saveResultIterator( odiResult, groupLevel, subQueryInfo ); 		
	}
	
	/**
	 * 
	 */
	private class SaveUtilHelper
	{
		private IBaseQueryDefinition queryDefn;
		
		/**
		 * @param queryDefn
		 */
		private SaveUtilHelper(IBaseQueryDefinition queryDefn)
		{
			this.queryDefn = queryDefn;
		}
		
		/**
		 * Save below information into report document:
		 * 		result class
		 * 		group information
		 * 		subquery information
		 *  
		 * @param odiResult
		 * @throws DataException
		 */
		private void saveResultIterator( CachedResultSet odiResult,
				int groupLevel, int[] subQueryInfo ) throws DataException
		{
			try
			{
				// save the information of result class and group information
				boolean isSubQuery = streamManager.isSubquery( );

				OutputStream streamForResultClass = null;
				if ( isSubQuery == false )
					streamForResultClass = streamManager.getOutStream( DataEngineContext.RESULTCLASS_STREAM );
				OutputStream streamForGroupInfo = streamManager.getOutStream( DataEngineContext.GROUP_INFO_STREAM );
				odiResult.doSave( streamForResultClass,
						null,
						streamForGroupInfo,
						isSubQuery );

				streamForGroupInfo.close( );
				if ( streamForResultClass != null )
					streamForResultClass.close( );

				// save the information of sub query information
				// notice, sub query name is used instead of sub query id
				if ( isSubQuery == true
						&& streamManager.hasSubStream( DataEngineContext.SUBQUERY_INFO_STREAM ) == false )
				{
					// save info related with sub query info
					OutputStream streamForSubQuery = streamManager.getSubOutStream( DataEngineContext.SUBQUERY_INFO_STREAM );
					RDSubQueryUtil.doSave( streamForSubQuery,
							groupLevel,
							subQueryInfo );
					streamForSubQuery.close( );
				}
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Set" );
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
			OutputStream outputStream = streamManager.getOutStream( DataEngineContext.EXPR_META_STREAM );
			ExprMetaUtil.saveExprMetaInfo( queryDefn, exprNameSet, outputStream );

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
	
}
