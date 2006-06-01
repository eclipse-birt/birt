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
	private StreamManager streamManager;	
	protected SaveUtilHelper saveUtilHelper;
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	RDSave( DataEngineContext context, IBaseQueryDefinition queryDefn,
			int rowCount, QueryResultInfo queryResultInfo )
			throws DataException
	{
		this.context = context;
		this.rowCount = rowCount;
		
		this.saveUtilHelper = new SaveUtilHelper( queryDefn );
		this.streamManager = new StreamManager( context, queryResultInfo );
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
	private void saveForIV( ) throws DataException
	{
		// save expression metadata and transformation info
		if ( context.getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			this.saveUtilHelper.saveExprMetadata( );
		}
		else
		{
			this.saveUtilHelper.cleanChildOfRoot( );
		}

		this.saveUtilHelper.saveFilterInfo( );
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
	protected class SaveUtilHelper
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
				OutputStream streamForDataSet = null;
				OutputStream streamForResultClass = null;
				
				OutputStream streamForGroupInfo = null;
				
				OutputStream streamForRowIndexInfo = null;
				
				boolean isSubQuery = streamManager.isSubquery( );
				if ( streamManager.isSecondRD( ) == false )
				{
					if ( context.getMode( ) == DataEngineContext.MODE_GENERATION )
					{
						if ( isSubQuery == false )
							streamForResultClass = streamManager.getOutStream( DataEngineContext.RESULTCLASS_STREAM );
						streamForDataSet = streamManager.getOutStream( DataEngineContext.DATASET_DATA_STREAM );
					}
				}
				else
				{
					streamForRowIndexInfo = streamManager.getOutStream( DataEngineContext.ROW_INDEX_STREAM );
				}
				streamForGroupInfo = streamManager.getOutStream( DataEngineContext.GROUP_INFO_STREAM );
				
				odiResult.doSave( new StreamWrapper( streamForResultClass,
						streamForDataSet,
						streamForGroupInfo,
						streamForRowIndexInfo ),
						isSubQuery,
						RDSave.this.exprNameSet );
				
				if ( streamForResultClass != null )
					streamForResultClass.close( );
				
				if ( streamForGroupInfo != null )
					streamForGroupInfo.close( );
				
				if ( streamForRowIndexInfo != null )
					streamForRowIndexInfo.close( );
				
				if ( streamForDataSet != null )
					streamForDataSet.close();

				
				// save the information of sub query information
				// notice, sub query name is used instead of sub query id
				if ( isSubQuery == true
						&& streamManager.hasOutSubStream( DataEngineContext.SUBQUERY_INFO_STREAM ) == false )
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
		
		/**
		 * @throws DataException
		 */
		protected void saveFilterInfo( ) throws DataException
		{
			OutputStream outputStream = streamManager.getOutStream( DataEngineContext.FILTER_INFO_STREAM );
			FilterDefnUtil.saveFilterDefn( outputStream, queryDefn.getFilters( ) );

			try
			{
				outputStream.close( );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
			}
		}
		
		/**
		 * @throws DataException
		 * @throws IOException
		 */
		protected void appendSelfToRoot( ) throws DataException
		{
			QueryResultIDManager.appendChildToRoot( streamManager,
					queryDefn.getFilters( ) );
		}
		
		/**
		 * @throws DataException
		 */
		protected void cleanChildOfRoot( ) throws DataException
		{
			QueryResultIDManager.cleanChildOfRoot( streamManager );
		}
	}	
	
}
