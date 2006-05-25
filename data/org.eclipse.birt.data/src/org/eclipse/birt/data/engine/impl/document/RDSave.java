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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Save expression value of every row into report document. The output format in
 * stream is, <expressCount, [expression id, expression value]*>.
 * 
 * When no value of one row is saved, the expressionCount will automatically be
 * ouptput as 0. It will happen when caller does not call getValue on this row
 * or call skipToEnd method.
 */
public class RDSave implements IRDSave
{
	//	 
	private DataEngineContext context;
	
	private int rowCount;
	
	//
	private OutputStream rowExprsOs;
	private OutputStream rowLenOs;
	
	//
	private Set exprNameSet = new HashSet( );
	
	//
	private RowSaveUtil rowSaveUtil;
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
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveExprValue(int,
	 *      java.lang.String, java.lang.Object)
	 */
	public void saveExprValue( int currIndex, String exprID, Object exprValue )
			throws DataException
	{
		if ( rowSaveUtil == null )
			this.initSaveRowUtil( );

		rowSaveUtil.saveExprValue( currIndex, exprID, exprValue );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveFinish(int)
	 */
	public void saveFinish( int currIndex ) throws DataException
	{
		if ( rowSaveUtil == null )
			this.initSaveRowUtil( );

		rowSaveUtil.saveFinish( currIndex );

		this.closeSaveRowUtil( );

		this.saveForIV( );
	}
	
	/**
	 * @throws DataException
	 */
	protected void saveForIV( ) throws DataException
	{
		// save expression metadata and transformation info
		this.saveUtilHelper.saveForIV( );
	}
	
	/**
	 * @throws DataException
	 */
	private void initSaveRowUtil( ) throws DataException
	{
		rowExprsOs = streamManager.getOutStream( DataEngineContext.EXPR_VALUE_STREAM );
		rowLenOs = streamManager.getOutStream( DataEngineContext.ROWLENGTH_INFO_STREAM );

		this.rowSaveUtil = new RowSaveUtil( rowCount,
				rowExprsOs,
				rowLenOs,
				this.exprNameSet );
	}

	/**
	 * @throws DataException
	 */
	private void closeSaveRowUtil( ) throws DataException
	{
		try
		{
			this.rowExprsOs.close( );
			this.rowLenOs.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_SAVE_ERROR,
					e,
					"Result Data" );
		}
	}
	
	/* 
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator, int, int[])
	 */
	public void saveResultIterator( IResultIterator odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		VersionManager.setVersion(context, VersionManager.VERSION_2_1);
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
		private void saveResultIterator( IResultIterator odiResult,
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
				
				OutputStream streamForExprValue = null;
				OutputStream streamForRowLen = null;
				if ( context.getMode( ) == DataEngineContext.MODE_UPDATE )
				{
					streamForExprValue = streamManager.getOutStream( DataEngineContext.EXPR_VALUE_STREAM );
					streamForRowLen = streamManager.getOutStream( DataEngineContext.ROWLENGTH_INFO_STREAM );
				}
								
				odiResult.doSave( new StreamWrapper( streamForExprValue,
						streamForRowLen,
						streamForResultClass,
						null,
						streamForGroupInfo ),
						isSubQuery,
						RDSave.this.exprNameSet );

				if ( streamForExprValue != null )
				{
					streamForExprValue.close( );
					streamForRowLen.close( );
				}
				
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
