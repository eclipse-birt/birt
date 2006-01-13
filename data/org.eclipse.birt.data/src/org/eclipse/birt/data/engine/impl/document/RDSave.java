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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * 
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
	private OutputStream outputStream;
	private BufferedOutputStream bos;
	private DataOutputStream dos;
	
	//
	private Map exprValueMap = new HashMap( );
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	public RDSave( DataEngineContext context, String queryResultID,
			int rowCount, String subQueryName, int subQueryIndex )
			throws DataException
	{
		this.context = context;
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.rowCount = rowCount;
		
		if ( subQueryName != null )
			this.subQueryID = subQueryName + "/" + subQueryIndex;
		
		this.lastRowIndex = 0;
	}
	
	/**
	 * init save environment
	 */
	private void initSave( ) throws DataException
	{
		if ( dos == null )
		{
			outputStream = context.getOutputStream( queryResultID,
					subQueryID,
					DataEngineContext.EXPR_VALUE_STREAM );
			bos = new BufferedOutputStream( outputStream );
			dos = new DataOutputStream( bos );

			try
			{
				IOUtil.writeInt( dos, rowCount );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
			}
		}
	}
	
	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 */
	public void saveExprValue( int currIndex, String exprID, Object exprValue )
			throws DataException
	{
		initSave( );

		if ( currIndex != lastRowIndex )
		{
			try
			{
				saveExprOfOneRow( lastRowIndex );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Data" );
			}

			exprValueMap.clear( );
			lastRowIndex = currIndex;
		}

		exprValueMap.put( exprID, exprValue );
	}

	/**
	 * Notify save needs to be finished
	 */
	public void saveFinish( ) throws DataException
	{
		initSave( );
		
		try
		{
			saveExprOfOneRow( this.lastRowIndex );
			dos.close( );
			bos.close( );
			outputStream.close( );
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
	private void saveExprOfOneRow( int rowIndex ) throws IOException
	{
		Set keySet = exprValueMap.keySet( );
		String[] exprIDs = (String[]) keySet.toArray( new String[0] );
		
		int size = exprIDs.length;
		if ( size == 0 )
			return;

		IOUtil.writeInt( dos, size );
		for ( int i = 0; i < size; i++ )
		{
			String exprID = exprIDs[i];
			Object exprValue = exprValueMap.get( exprID );

			IOUtil.writeString( dos, exprID );
			IOUtil.writeObject( dos, exprValue );
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
				if ( isSubQuery == false )
				{
					streamForResultClass = context.getOutputStream( queryResultID,
							subQueryID,
							DataEngineContext.RESULTCLASS_STREAM );
				}

				odiResult.doSave( streamForResultClass,
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
	
}
