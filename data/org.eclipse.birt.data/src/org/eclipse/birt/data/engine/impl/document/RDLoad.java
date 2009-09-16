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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.QueryDefinitionUtil;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.util.ExprDataResultSet1;
import org.eclipse.birt.data.engine.impl.document.util.ExprDataResultSet2;
import org.eclipse.birt.data.engine.impl.document.util.ExprResultSet;
import org.eclipse.birt.data.engine.impl.document.util.ExprResultSet2;
import org.eclipse.birt.data.engine.impl.document.util.IExprDataResultSet;
import org.eclipse.birt.data.engine.impl.document.util.IExprResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.DataSetResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Load data from input stream.
 */
public class RDLoad
{
	private int version;
	
	//
	private StreamManager streamManager;
	private RDSubQueryUtil subQueryUtil;
	
	private String tempDir;
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param currParentIndex
	 * @throws DataException
	 */
	RDLoad( String tempDir, DataEngineContext context, QueryResultInfo queryResultInfo )
			throws DataException
	{
		this.tempDir = tempDir;
		subQueryUtil = new RDSubQueryUtil( context,
				QueryResultIDUtil.getRealStreamID( queryResultInfo.getRootQueryResultID( ),
						queryResultInfo.getSelfQueryResultID( ) ),
				queryResultInfo.getSubQueryName( ) );
		streamManager = new StreamManager( context,
				new QueryResultInfo( queryResultInfo.getRootQueryResultID( ),
						queryResultInfo.getParentQueryResultID( ),
						queryResultInfo.getSelfQueryResultID( ),
						queryResultInfo.getSubQueryName( ),
						queryResultInfo.getSubQueryName( ) == null
								? 0
								: getSubQueryIndex( queryResultInfo.getIndex( ) ) ) );
		
		this.version = streamManager.getVersion( );
	}
	
	/**
	 * @param currParentIndex
	 * @return
	 * @throws DataException
	 */
	int getSubQueryIndex( int currParentIndex ) throws DataException
	{
		return subQueryUtil.getSubQueryIndex( currParentIndex );
	}
	
	/**
	 * @return result meta data
	 * @throws DataException
	 */
	public ResultMetaData loadResultMetaData( ) throws DataException
	{
		return new ResultMetaData( loadResultClass( ) );
	}
	
	/**
	 * This is used for PRESENTATION, the data in report document as the
	 * CachedResultSet.
	 * 
	 * @return
	 * @throws DataException
	 */
	IExprResultSet loadExprResultSet( int rowIdStartingIndex, IBaseQueryDefinition qd ) throws DataException
	{
		if ( streamManager.isSecondRD( ) == true
				&& streamManager.isSubquery( ) == true )
			return new ExprResultSet2( tempDir, streamManager,
					version,
					streamManager.isSecondRD( ), rowIdStartingIndex );
		return new ExprResultSet( tempDir, streamManager,
				version,
				streamManager.isSecondRD( ),
				( streamManager.isSubquery( ) || this.version < VersionManager.VERSION_2_2_1_3 )
						? null : this.loadDataSetData( ), streamManager.isSubquery( ) ? rowIdStartingIndex:0,
								qd );
	}
	
	/**
	 * This is used for UPDATE, the data in report document as data source for
	 * transformation.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IExprDataResultSet loadExprDataResultSet( ) throws DataException
	{
		if ( version == VersionManager.VERSION_2_0 )
			throw new DataException( ResourceConstants.WRONG_VERSION );

		ExprMetaInfo[] exprMetas = loadExprMetaInfo( );

		// This is a special case, that the stream needs to be close at the code
		// of ExprDataResultSet
		IExprDataResultSet exprDataResultSet = null;
		if ( streamManager.isBasedOnSecondRD( ) == false )
			exprDataResultSet = new ExprDataResultSet1( streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE ),
					exprMetas,
					version,
					version < VersionManager.VERSION_2_2_1_3?null:this.loadDataSetData( ));
		else
			exprDataResultSet = new ExprDataResultSet2( tempDir,
					streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE ),
					streamManager.getInStream( DataEngineContext.EXPR_ROWLEN_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.BASE_SCOPE ),
					streamManager.getInStream( DataEngineContext.ROW_INDEX_STREAM,
							StreamManager.ROOT_STREAM,
							StreamManager.PARENT_SCOPE ),
					exprMetas, version, version < VersionManager.VERSION_2_2_1_3?null:this.loadDataSetData( ) );

		return exprDataResultSet;
	}

	/**
	 * @return
	 * @throws DataException
	 */
	public ExprMetaInfo[] loadExprMetaInfo( ) throws DataException
	{
		InputStream inputStream = streamManager.getInStream( DataEngineContext.EXPR_META_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		BufferedInputStream buffStream = new BufferedInputStream( inputStream );
		ExprMetaInfo[] exprMetas = ExprMetaUtil.loadExprMetaInfo( buffStream );
		try
		{
			buffStream.close( );
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
	private IResultClass loadResultClass( ) throws DataException
	{
		InputStream stream = streamManager.getInStream( DataEngineContext.DATASET_META_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		BufferedInputStream buffStream = new BufferedInputStream( stream );
		IResultClass resultClass = new ResultClass( buffStream );
		try
		{
			buffStream.close( );
			stream.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Result Class" );
		}

		return resultClass;
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public DataSetResultSet loadDataSetData( ) throws DataException
	{
		if( !streamManager.hasInStream( DataEngineContext.DATASET_DATA_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE ) )
			return null;
		RAInputStream stream = streamManager.getInStream( DataEngineContext.DATASET_DATA_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
	
		RAInputStream lensStream = null;
		if( version >= VersionManager.VERSION_2_2_1_3 )
			lensStream = streamManager.getInStream( DataEngineContext.DATASET_DATA_LEN_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		
		DataSetResultSet populator = new DataSetResultSet( stream, lensStream, 
				this.loadResultClass( ), version );

		return populator;
	}
	
	/**
	 * @param streamPos
	 * @param streamScope
	 * @return
	 * @throws DataException
	 */
	public IBaseQueryDefinition loadOriginalQueryDefn( int streamPos,
			int streamScope ) throws DataException
	{
		InputStream inputStream = streamManager.getInStream( DataEngineContext.ORIGINAL_QUERY_DEFN_STREAM,
				streamPos,
				streamScope );
		return loadQueryDefn( inputStream );
	}
	
	/**
	 * @param streamPos
	 * @param streamScope
	 * @return query definition
	 * @throws DataException
	 */
	public IBaseQueryDefinition loadQueryDefn( int streamPos, int streamScope )
			throws DataException
	{
		InputStream inputStream = streamManager.getInStream( DataEngineContext.QUERY_DEFN_STREAM,
				streamPos,
				streamScope );
		return loadQueryDefn( inputStream );
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws DataException
	 */
	private IBaseQueryDefinition loadQueryDefn( InputStream inputStream ) throws DataException
	{
		IBaseQueryDefinition queryDefn = QueryDefnIOUtil.loadQueryDefn( inputStream, version );
		try
		{
			inputStream.close( );
		}
		catch ( IOException e )
		{
			// ignore
		}

		return queryDefn;
	}
	
	/**
	 * @param streamPos
	 * @param streamScope
	 * @return filter definition
	 * @throws DataException
	 */
	public List loadFilterDefn( int streamPos, int streamScope )
			throws DataException
	{
		return loadQueryDefn( streamPos, streamScope ).getFilters( );
	}
	
	/**
	 * @param streamPos
	 * @param streamScope
	 * @return group definition
	 * @throws DataException
	 */
	public List loadGroupDefn( int streamPos, int streamScope )
			throws DataException
	{
		return loadQueryDefn( streamPos, streamScope ).getGroups( );
	}
	
	/**
	 * 
	 * @param streamPos
	 * @param streamScope
	 * @param subQueryName
	 * @return
	 * @throws DataException
	 */
	public ISubqueryDefinition loadSubQueryDefn( int streamPos,
			int streamScope, String subQueryName ) throws DataException
	{
		if ( subQueryName == null )
			return null;
		IBaseQueryDefinition baseQueryDefn = loadQueryDefn( streamPos,
				streamScope );
		return QueryDefinitionUtil.findSubQueryDefinition( subQueryName, baseQueryDefn );
	}
	
}
