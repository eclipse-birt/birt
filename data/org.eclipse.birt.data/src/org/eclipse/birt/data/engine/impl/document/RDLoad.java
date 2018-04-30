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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.PLSUtil;
import org.eclipse.birt.data.engine.impl.QueryDefinitionUtil;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.util.EmptyExprResultSet;
import org.eclipse.birt.data.engine.impl.document.util.ExprDataResultSet1;
import org.eclipse.birt.data.engine.impl.document.util.ExprDataResultSet2;
import org.eclipse.birt.data.engine.impl.document.util.ExprResultSet;
import org.eclipse.birt.data.engine.impl.document.util.ExprResultSet2;
import org.eclipse.birt.data.engine.impl.document.util.IExprDataResultSet;
import org.eclipse.birt.data.engine.impl.document.util.IExprResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.DataSetResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;
import org.eclipse.birt.data.engine.impl.index.IOrderedIntSet;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.storage.DataSetStore;
import org.eclipse.birt.data.engine.storage.IDataSetReader;

/**
 * Load data from input stream.
 */
public class RDLoad
{
	private int version;
	
	//
	private StreamManager streamManager;
	private RDSubQueryUtil subQueryUtil;
	
	private QueryResultInfo queryResultInfo;
	private String tempDir;

	private DataEngineContext context;
	
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
		this.queryResultInfo = queryResultInfo;
		this.context = context;
		
		if ( !isEmptyQueryResultID( this.queryResultInfo.getSelfQueryResultID( ) ) )
		{
			subQueryUtil = new RDSubQueryUtil( context,
					QueryResultIDUtil.getRealStreamID( queryResultInfo.getRootQueryResultID( ),
							queryResultInfo.getSelfQueryResultID( ) ),
							queryResultInfo.getSubQueryName( ) );
		}

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
		return subQueryUtil == null? 0 : subQueryUtil.getSubQueryIndex( currParentIndex );
	}
	
	/**
	 * @return result meta data
	 * @throws DataException
	 */
	public ResultMetaData loadResultMetaData( ) throws DataException
	{
		if ( isEmptyQueryResultID( this.queryResultInfo.getSelfQueryResultID( ) ) )
		{
			return new ResultMetaData( new ResultClass( new ArrayList( ) ) );
		}
		
		return new ResultMetaData( loadResultClass( ) );
	}
	
	/**
	 * This is used for PRESENTATION, the data in report document as the
	 * CachedResultSet.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IExprResultSet loadExprResultSet( int rowIdStartingIndex, IBaseQueryDefinition qd ) throws DataException
	{
		if ( streamManager.isSecondRD( ) == true
				&& streamManager.isSubquery( ) == true )
			return new ExprResultSet2( tempDir,
					streamManager,
					version,
					streamManager.isSecondRD( ),
					rowIdStartingIndex );
		if ( isEmptyQueryResultID( this.queryResultInfo.getSelfQueryResultID( ) ) )
		{
			return new EmptyExprResultSet( );
		}
		else
			return new ExprResultSet( tempDir,
					streamManager,
					version,
					streamManager.isSecondRD( ),
					( streamManager.isSubquery( ) || this.version < VersionManager.VERSION_2_2_1_3 )
							? null : this.loadDataSetData( null,
									null,
									new HashMap( ) ),
					streamManager.isSubquery( ) ? rowIdStartingIndex : 0,
					qd );
	}
	
	/**
	 * This is used for UPDATE, the data in report document as data source for
	 * transformation.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IExprDataResultSet loadExprDataResultSet( boolean isSummary ) throws DataException
	{
		if ( version == VersionManager.VERSION_2_0 )
			throw new DataException( ResourceConstants.WRONG_VERSION );

		ExprMetaInfo[] exprMetas = loadExprMetaInfo( );

		// This is a special case, that the stream needs to be close at the code
		// of ExprDataResultSet
		IExprDataResultSet exprDataResultSet = null;
		if ( streamManager.isBasedOnSecondRD( ) == false )
		{
			exprDataResultSet = new ExprDataResultSet1( streamManager.getInStream( DataEngineContext.EXPR_VALUE_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE ),
					exprMetas,
					version,
					( isSummary || version < VersionManager.VERSION_2_2_1_3 )
							? null : this.loadDataSetData( null,
									null,
									new HashMap( ) ) );
		}
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
					exprMetas, version, version < VersionManager.VERSION_2_2_1_3?null:this.loadDataSetData( null, null, new HashMap() ) );

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
	public IResultClass loadResultClass( ) throws DataException
	{
		return loadResultClass( false );
	}
	
	public IResultClass loadResultClass( boolean includeInnerID ) throws DataException
	{
		
		if( !streamManager.hasInStream( DataEngineContext.DATASET_META_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE ) )
			return null;
		
		InputStream stream = streamManager.getInStream( DataEngineContext.DATASET_META_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		BufferedInputStream buffStream = new BufferedInputStream( stream );
		IResultClass resultClass = new ResultClass( buffStream, version, includeInnerID );
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
	
	public IDataSetResultSet loadDataSetData( IOrderedIntSet preFilteredRowIds,
			Map<String, StringTable> stringTableMap, Map index )
			throws DataException
	{
		return loadDataSetData( preFilteredRowIds, stringTableMap, index, new HashMap() );
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public IDataSetResultSet loadDataSetData( IOrderedIntSet preFilteredRowIds,
			Map<String, StringTable> stringTableMap, Map index, Map appContext )
			throws DataException
	{
		return loadDataSetData( preFilteredRowIds,
				stringTableMap,
				index,
				true,
				appContext );
	}

	/**
	 * Load data set with assignment of inner id.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IDataSetResultSet loadDataSetData( IOrderedIntSet preFilteredRowIds,
			Map<String, StringTable> stringTableMap, Map index,
			boolean includeInnerID, Map appContext ) throws DataException
	{
		return loadDataSetData( null,
				preFilteredRowIds,
				stringTableMap,
				index,
				includeInnerID,
				appContext );
	}
	
	public IDataSetResultSet loadDataSetData( IResultClass targetResultClass,
			IOrderedIntSet preFilteredRowIds,
			Map<String, StringTable> stringTableMap, Map index,
			boolean includeInnerID, Map appContext ) throws DataException
	{
		boolean loadResultClass = false;
		if ( targetResultClass == null )
		{
			targetResultClass = this.loadResultClass( includeInnerID );
			loadResultClass = true;
		}

		// TODO Pass in filter rowIds as sorted list.
		IDataSetReader reader = DataSetStore.createReader( streamManager,
				targetResultClass,
				includeInnerID,
				appContext );
		if ( reader != null )
			return reader.load( preFilteredRowIds == null
					? null
					: preFilteredRowIds );

		if ( !streamManager.hasInStream( DataEngineContext.DATASET_DATA_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE ) )
			return null;
		
		RAInputStream stream = streamManager.getInStream( DataEngineContext.DATASET_DATA_STREAM,
				StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );

		RAInputStream lensStream = null;
		if ( version >= VersionManager.VERSION_2_2_1_3 )
			lensStream = streamManager.getInStream( DataEngineContext.DATASET_DATA_LEN_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE );

		int adjustedVersion = resolveVersionConflict( );
		
		if ( loadResultClass && includeInnerID )
		{
			List<ResultFieldMetadata> fields = new ArrayList<ResultFieldMetadata>( targetResultClass.getFieldCount( ) - 1 );
			for ( int i = 1; i <= targetResultClass.getFieldCount( ); i++ )
			{
				ResultFieldMetadata f = targetResultClass.getFieldMetaData( i );
				if ( f.getName( ).equals( ExprMetaUtil.POS_NAME ) )
					continue;
				fields.add( f );
			}

			targetResultClass = new ResultClass( fields );
		}
		else
		{
			targetResultClass = this.loadResultClass();
		}
		
		return new DataSetResultSet( stream,
				lensStream,
				targetResultClass,
				preFilteredRowIds,
				stringTableMap,
				index,
				adjustedVersion,
				includeInnerID,
				PLSUtil.isRowIdSaved( streamManager ) );
	}
	
	
	private int resolveVersionConflict( )
	{
		if ( version == VersionManager.VERSION_3_7_2_1
				&& ( "4.2.0.v20120611".equals( this.context.getBundleVersion( ) ) || "4.2.1.v20120820".equals( this.context.getBundleVersion( ) ) ) )
			return VersionManager.VERSION_4_2_1_2;
		else
			return version;
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
		String bundleVersion = this.context.getBundleVersion( );
		IBaseQueryDefinition queryDefn = QueryDefnIOUtil.loadQueryDefn( inputStream, version, bundleVersion );
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
	
	/**
	 * Read query result ID which result set is empty.
	 * 
	 * @param reader
	 * @return
	 * @throws DataException
	 */
	public boolean isEmptyQueryResultID( String queryResultID )
			throws DataException
	{
		if ( !this.context.hasInStream( "DataEngine",
				null,
				DataEngineContext.EMPTY_NESTED_QUERY_ID ) )
		{
			return false;
		}
		DataInputStream emptyQueryResultStream = new DataInputStream( context.getInputStream( "DataEngine",
				null,
				DataEngineContext.EMPTY_NESTED_QUERY_ID ) );
		Set emptyQueryResultID = new HashSet( );
		try
		{
			int count = IOUtil.readInt( emptyQueryResultStream );
			for ( int i = 0; i < count; i++ )
			{
				String temp = IOUtil.readString( emptyQueryResultStream );
				emptyQueryResultID.add( temp );
			}
			emptyQueryResultStream.close( );
			if ( emptyQueryResultID.contains( queryResultID ) )
				return true;
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return false;
	}

	/**
	 * @param streamPos
	 * @param streamScope
	 * @return query definition
	 * @throws DataException
	 */
	public IBaseQueryDefinition loadQueryDefn(int streamType, int streamPos, int streamScope,String subname )
			throws DataException
	{
		InputStream inputStream = streamManager.getInStream(streamType,
				streamPos,
				streamScope,subname );
		return loadQueryDefn( inputStream );
	}	
}
