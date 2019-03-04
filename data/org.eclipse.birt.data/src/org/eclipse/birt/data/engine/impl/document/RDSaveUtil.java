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
import java.util.Set;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefnDelegator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.SimpleResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Help to save information to report document.
 */
class RDSaveUtil
{
	private int mode;
	private IBaseQueryDefinition queryDefn;
	private StreamManager streamManager;
	private String bundleVersion;
	
	/**
	 * @param queryDefn
	 */
	RDSaveUtil( DataEngineContext context, IBaseQueryDefinition queryDefn,
			StreamManager streamManager )
	{
		this.mode = context.getMode( );
		this.bundleVersion = context.getBundleVersion( );
		this.queryDefn = queryDefn;
		this.streamManager = streamManager;
	}
	
	/**
	 * Save below information into report document:
	 * 		result class
	 * 		group information
	 * 		subquery information
	 *  
	 * @param odiResult
	 * @param groupLevel
	 * @param subQueryInfo
	 * @throws DataException
	 */
	void saveResultIterator( IResultIterator odiResult,
			int groupLevel, int[] subQueryInfo ) throws DataException
	{
		if ( mode == DataEngineContext.MODE_GENERATION )
			saveForGeneration( odiResult, groupLevel, subQueryInfo );
		else
			saveForUpdate( odiResult, groupLevel, subQueryInfo );
	}
	
	/**
	 * @param odiResult
	 * @param groupLevel
	 * @param subQueryInfo
	 * @throws DataException
	 */
	private void saveForGeneration( IResultIterator odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		try
		{
			// save the information of result class and group information
			OutputStream streamForResultClass = null;			
			OutputStream streamForGroupInfo = null;		
						
			boolean isSubQuery = streamManager.isSubquery( );
			if ( isSubQuery == false )
			{
				streamForResultClass = streamManager.getOutStream( DataEngineContext.DATASET_META_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE );
			}
			
			if( !(odiResult instanceof SimpleResultSet || odiResult instanceof ResultSetWrapper ) )
				streamForGroupInfo = streamManager.getOutStream( DataEngineContext.GROUP_INFO_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			
			odiResult.doSave( new StreamWrapper( streamManager,
					streamForResultClass,
					streamForGroupInfo,
					null,
					null ), isSubQuery );
			
			if( !(odiResult instanceof SimpleResultSet || odiResult instanceof ResultSetWrapper ) )
				streamForGroupInfo.close( );
			
			// save the information of sub query information
			// notice, sub query name is used instead of sub query id
			if ( isSubQuery == true )
			{
				if ( streamManager.hasOutStream( DataEngineContext.SUBQUERY_INFO_STREAM,
						StreamManager.SUB_QUERY_ROOT_STREAM,
						StreamManager.SELF_SCOPE ) == false )
				{
					// save info related with sub query info
					saveSubQueryInfo( groupLevel, subQueryInfo );
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
	
	/**
	 * @param odiResult
	 * @param groupLevel
	 * @param subQueryInfo
	 * @throws DataException
	 */
	private void saveForUpdate( IResultIterator odiResult, int groupLevel,
			int[] subQueryInfo ) throws DataException
	{
		try
		{
			OutputStream streamForGroupInfo = null;
			OutputStream streamForRowIndexInfo = null;
			OutputStream streamForParentIndexInfo = null;
			
			boolean isSubQuery = streamManager.isSubquery( );
			if ( streamManager.isSecondRD( ) == true )
			{
				streamForRowIndexInfo = streamManager.getOutStream( DataEngineContext.ROW_INDEX_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE );
				if ( isSubQuery == true )
					streamForParentIndexInfo = streamManager.getOutStream( DataEngineContext.SUBQUERY_PARENTINDEX_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
			}
			
			if( !(odiResult instanceof SimpleResultSet) )
				streamForGroupInfo = streamManager.getOutStream( DataEngineContext.GROUP_INFO_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			
			odiResult.doSave( new StreamWrapper( streamManager,
					null,
					streamForGroupInfo,
					streamForRowIndexInfo,
					streamForParentIndexInfo ),
					isSubQuery );
			if( !(odiResult instanceof SimpleResultSet) )			
				streamForGroupInfo.close( );
			
			if ( streamForRowIndexInfo != null )
				streamForRowIndexInfo.close( );
			
			if ( streamForParentIndexInfo != null )
				streamForParentIndexInfo.close( );
			
			// save the information of sub query information
			// notice, sub query name is used instead of sub query id
			if ( isSubQuery == true )
			{
				// TODO: enhance me
				if ( mode == DataEngineContext.MODE_UPDATE )
				{
					saveSubQueryInfo( groupLevel, subQueryInfo );
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
	
	/**
	 * @param groupLevel
	 * @param subQueryInfo
	 * @throws DataException
	 * @throws IOException
	 */
	private void saveSubQueryInfo( int groupLevel, int[] subQueryInfo )
			throws DataException, IOException
	{
		// save info related with sub query info
		OutputStream streamForSubQuery = streamManager.getOutStream( DataEngineContext.SUBQUERY_INFO_STREAM,
				StreamManager.SUB_QUERY_ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		RDSubQueryUtil.doSave( streamForSubQuery, groupLevel, subQueryInfo );
		streamForSubQuery.close( );
	}
	
	/**
	 * @param exprNameSet
	 * @throws DataException
	 */
	void saveExprMetadata( Set exprNameSet ) throws DataException
	{
		OutputStream outputStream = streamManager.getOutStream( DataEngineContext.EXPR_META_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
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
	void saveQueryDefn( ) throws DataException
	{
		if ( queryDefn instanceof QueryDefnDelegator )
		{
			queryDefn = ( (QueryDefnDelegator) queryDefn ).getBaseQuery( );
		}
		
//		if ( queryDefn instanceof QueryDefinition
//				&& ( (QueryDefinition) queryDefn ).getQueryResultsID( ) == null )
//		{
//			outputStream = streamManager.getOutStream( DataEngineContext.ORIGINAL_QUERY_DEFN_STREAM,
//					StreamManager.ROOT_STREAM,
//					StreamManager.SELF_SCOPE );
//			QueryDefnIOUtil.saveBaseQueryDefn( outputStream, queryDefn, streamManager.getVersion( ) );
//			try
//			{
//				outputStream.close( );
//			}
//			catch ( IOException e )
//			{
//				throw new DataException( ResourceConstants.RD_SAVE_ERROR, e );
//			}
//		}

		OutputStream outputStream = streamManager.getOutStream( DataEngineContext.QUERY_DEFN_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		QueryDefnIOUtil.saveBaseQueryDefn( outputStream, queryDefn, streamManager.getVersion( ), this.bundleVersion  );
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
	void saveChildQueryID( ) throws DataException
	{
		QueryResultIDManager.appendChildToRoot( streamManager,
				queryDefn.getFilters( ) );
	}
	
}
