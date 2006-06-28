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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
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
	
	/**
	 * @param queryDefn
	 */
	RDSaveUtil( int mode, IBaseQueryDefinition queryDefn,
			StreamManager streamManager )
	{
		this.mode = mode;
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
		try
		{
			// save the information of result class and group information
			OutputStream streamForDataSet = null;
			OutputStream streamForResultClass = null;
			
			OutputStream streamForGroupInfo = null;		
			OutputStream streamForRowIndexInfo = null;			
			OutputStream streamForParentIndexInfo = null;
			
			boolean isSubQuery = streamManager.isSubquery( );
			if ( mode == DataEngineContext.MODE_GENERATION )
			{
				if ( isSubQuery == false )
				{
					streamForResultClass = streamManager.getOutStream( DataEngineContext.DATASET_META_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
					streamForDataSet = streamManager.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
				}
			}
			else if ( streamManager.isSecondRD( ) == true )
			{
				streamForRowIndexInfo = streamManager.getOutStream( DataEngineContext.ROW_INDEX_STREAM,
						StreamManager.ROOT_STREAM,
						StreamManager.SELF_SCOPE );
				if ( isSubQuery == true )
					streamForParentIndexInfo = streamManager.getOutStream( DataEngineContext.SUBQUERY_PARENTINDEX_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.SELF_SCOPE );
			}
			streamForGroupInfo = streamManager.getOutStream( DataEngineContext.GROUP_INFO_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			
			odiResult.doSave( new StreamWrapper( streamForResultClass,
					streamForDataSet,
					streamForGroupInfo,
					streamForRowIndexInfo,
					streamForParentIndexInfo ),
					isSubQuery );
			
			if ( streamForResultClass != null )
				streamForResultClass.close( );
			
			if ( streamForGroupInfo != null )
				streamForGroupInfo.close( );
			
			if ( streamForRowIndexInfo != null )
				streamForRowIndexInfo.close( );
			
			if ( streamForDataSet != null )
				streamForDataSet.close();

			if ( streamForParentIndexInfo != null )
				streamForParentIndexInfo.close( );
			
			// save the information of sub query information
			// notice, sub query name is used instead of sub query id
			if ( isSubQuery == true )
			{
				if ( streamManager.hasOutStream( DataEngineContext.SUBQUERY_INFO_STREAM,
						StreamManager.SUB_QUERY_ROOT_STREAM,
						StreamManager.SELF_SCOPE ) == false
						|| mode == DataEngineContext.MODE_UPDATE ) // TODO: enhance me
				{
					// save info related with sub query info
					OutputStream streamForSubQuery = streamManager.getOutStream( DataEngineContext.SUBQUERY_INFO_STREAM,
							StreamManager.SUB_QUERY_ROOT_STREAM,
							StreamManager.SELF_SCOPE );
					RDSubQueryUtil.doSave( streamForSubQuery,
							groupLevel,
							subQueryInfo );
					streamForSubQuery.close( );
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
		OutputStream outputStream = streamManager.getOutStream( DataEngineContext.QUERY_DEFN_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE );
		QueryDefnUtil.saveBaseQueryDefn( outputStream, queryDefn );

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
