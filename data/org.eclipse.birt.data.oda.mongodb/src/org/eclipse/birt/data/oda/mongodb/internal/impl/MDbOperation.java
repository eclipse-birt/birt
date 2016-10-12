/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSet;
import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSetMetaData;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Base class for MongoDB findQuery and command operations. Delegated by
 * QueryModel to handle its operations.
 */
public class MDbOperation
{

	private QueryModel m_model;

	private MongoCollection<Document> m_queryCollection;
	private BasicDBObject m_fieldsObj;
	private BasicDBObject m_queryObj;
	private MDbResultSetMetaData m_rsMetaData;

	private static final MDbOperation sm_factory = new MDbOperation( );

	static MDbOperation createQueryOperation( QueryModel queryModel )
	{
		if ( queryModel == null || !queryModel.isValid( ) )
			throw new IllegalArgumentException( "null/invalid QueryModel" ); //$NON-NLS-1$

		return queryModel.getQueryProperties( ).hasValidCommandOperation( )
				? sm_factory.new CommandOperation( queryModel )
				: new MDbOperation( queryModel );
	}

	private MDbOperation( )
	{
	}

	private MDbOperation( QueryModel queryModel )
	{
		m_model = queryModel;
	}

	protected QueryModel getModel( )
	{
		return m_model;
	}

	protected void setResultSetMetaData( MDbResultSetMetaData rsmd )
	{
		m_rsMetaData = rsmd;
	}

	MDbResultSetMetaData getResultSetMetaData( )
	{
		return m_rsMetaData;
	}

	protected void resetPreparedState( )
	{
		m_queryCollection = null;
		m_fieldsObj = null;
		m_queryObj = null;
		m_rsMetaData = null;
	}

	protected void prepare( MongoCollection<Document> dbCollection )
			throws OdaException
	{
		resetPreparedState( );

		// get individual query components from data set properties
		QueryProperties queryProps = getModel( ).getQueryProperties( );

		// Apply read preferences
		ReadPreference readPref = queryProps.getTaggableReadPreference( );
		if ( readPref != null )
			dbCollection = dbCollection.withReadPreference( readPref );

		// handle findQueryExpr property
		BasicDBObject queryObj = queryProps.getFindQueryExprAsParsedObject( );

		if ( queryObj == null )
			queryObj = new BasicDBObject( );

		// specify fields to retrieve
		BasicDBObject fieldsObj = queryProps
				.getSelectedFieldsAsProjectionKeys( );

		try
		{
			// find search-limited rows to validate the queryObj and
			// obtain result set metadata on the specified dbCollection
			FindIterable<Document> findIterable = dbCollection.find( queryObj );
			findIterable = findIterable.projection( fieldsObj );
			// set data set query properties on DBCursor
			// to get result set metadata; no need to include sortExpr for
			// getting metadata
			applyPropertiesToCursor( findIterable, queryProps, true, false );

			m_rsMetaData = new MDbResultSetMetaData( findIterable,
					queryProps.getSelectedFieldNames( ),
					queryProps.isAutoFlattening( ) );

			// no exception; the find arguments on specified dbCollection are
			// valid
			m_queryCollection = dbCollection; // could be different, e.g.
												// mapReduce output collection
			m_fieldsObj = fieldsObj;
			m_queryObj = queryObj;
		}
		catch ( RuntimeException ex )
		{
			DriverUtil.getLogger( ).log( Level.SEVERE,
					"Encountered RuntimeException in QueryModel#prepareQuery(DBCollection).", //$NON-NLS-1$
					ex );
			throw new OdaException( ex );
		}
	}

	protected MDbResultSet execute( ) throws OdaException
	{
		if ( m_queryObj == null || m_queryCollection == null )
			throw new OdaException( Messages.mDbOp_invalidQueryExpr );

		try
		{
			FindIterable<Document> findIterable = m_queryCollection
					.find( m_queryObj );
			findIterable = findIterable.projection( m_fieldsObj );

			// no search limit applies here;
			// defer to MDbResultSet to set DBCursor#limit based on its maxRows
			applyPropertiesToCursor( findIterable,
					getModel( ).getQueryProperties( ),
					false,
					true );

			return new MDbResultSet( findIterable.iterator( ),
					getResultSetMetaData( ),
					getModel( ).getQueryProperties( ) );
		}
		catch ( RuntimeException ex )
		{
			DriverUtil.getLogger( ).log( Level.SEVERE,
					"Encountered RuntimeException: ", //$NON-NLS-1$
					ex );
			throw new OdaException( ex );
		}
	}

	/*
	 * Applies data set query properties and hints on DBCursor.
	 */
	private void applyPropertiesToCursor( MongoIterable<Document> mongoIterable,
			QueryProperties queryProps, boolean includeMetaDataSearchLimit,
			boolean includeSortExpr )
	{
		if ( includeMetaDataSearchLimit )
		{
			Integer searchLimit = getModel( )
					.getEffectiveMDSearchLimit( queryProps );
			if ( searchLimit > 0 )
			{
				// Apply to FindIterable or MapReduceIterable
				if ( mongoIterable instanceof FindIterable )
				{
					FindIterable<Document> findIterable = (FindIterable<Document>) mongoIterable;
					findIterable.limit( searchLimit.intValue( ) );
				}
				else if ( mongoIterable instanceof MapReduceIterable )
				{
					MapReduceIterable<Document> mapReduceIterable = (MapReduceIterable<Document>) mongoIterable;
					mapReduceIterable.limit( searchLimit.intValue( ) );
				}
			}
		}

		applyPropertiesToCursor( mongoIterable, queryProps, includeSortExpr );
	}

	/**
	 * Applies data set query properties and hints on DBCursor, except for
	 * cursor limit.
	 * 
	 * @see #applyPropertiesToCursor(DBCursor,QueryProperties,boolean,boolean)
	 */
	static void applyPropertiesToCursor( MongoIterable<Document> mongoIterable,
			QueryProperties queryProps, boolean includeSortExpr )
	{
		BasicDBObject sortExprObj = null;
		if ( includeSortExpr ) // normally done only when executing a query to
								// get
								// full result set
		{
			try
			{
				sortExprObj = queryProps.getSortExprAsParsedObject( );
			}
			catch ( OdaException ex )
			{
				// log warning and ignore
				DriverUtil.getLogger( ).log( Level.WARNING,
						Messages.bind(
								"Unable to parse the user-defined Sort Expression: {0}", //$NON-NLS-1$
								queryProps.getSortExpr( ) ),
						ex );
			}

		}

		// Map it to correct iterable object
		FindIterable<Document> findIterable = null;
		AggregateIterable<Document> aggregateIterable = null;
		MapReduceIterable<Document> mapReduceIterable = null;
		if ( mongoIterable instanceof FindIterable )
		{
			findIterable = (FindIterable<Document>) mongoIterable;
		}
		else if ( mongoIterable instanceof AggregateIterable )
		{
			aggregateIterable = (AggregateIterable<Document>) mongoIterable;
		}
		else if ( mongoIterable instanceof MapReduceIterable )
		{
			mapReduceIterable = (MapReduceIterable<Document>) mongoIterable;
		}
		if ( findIterable == null
				&& aggregateIterable == null && mapReduceIterable == null )
		{
			// Unknown type, return
		}
		if ( findIterable != null )
		{
			if ( sortExprObj != null )
				findIterable.sort( sortExprObj );

			if ( queryProps.getBatchSize( ) > 0 )
				findIterable.batchSize( queryProps.getBatchSize( ) );

			if ( queryProps.getNumDocsToSkip( ) > 0 )
				findIterable.skip( queryProps.getNumDocsToSkip( ) );

			if ( queryProps.isPartialResultsOk( ) )
				findIterable.partial( true );
			// TODO: Remove hint from the UI
			// TODO: add Time out in the UI
			/*
			 * // hint is deprecated in 3.2 DBObject hintObj =
			 * queryProps.getIndexHintsAsParsedObject(); String hintValue =
			 * queryProps.getIndexHints(); if( hintObj != null )
			 * rowsCursor.hint( hintObj ); else // try to pass the hint string
			 * value as is { String hintValue = queryProps.getIndexHints(); if(
			 * ! hintValue.isEmpty() ) rowsCursor.hint( hintValue ); }
			 * findIterable.maxTime(Bytes.QUERYOPTION_NOTIMEOUT, arg1) if(
			 * queryProps.hasNoTimeOut() ) rowsCursor.addOption(
			 * Bytes.QUERYOPTION_NOTIMEOUT );
			 */
		}
		if ( aggregateIterable != null )
		{
			if ( queryProps.getBatchSize( ) > 0 )
				aggregateIterable.batchSize( queryProps.getBatchSize( ) );

		}
		if ( mapReduceIterable != null )
		{
			if ( sortExprObj != null )
				mapReduceIterable.sort( sortExprObj );

			if ( queryProps.getBatchSize( ) > 0 )
				mapReduceIterable.batchSize( queryProps.getBatchSize( ) );
		}

	}

	protected QueryProperties getEffectiveProperties( )
	{
		return getModel( ).getQueryProperties( );
	}

	/*
	 * Extends MDbOperation to handle MongoDB commands.
	 */
	public class CommandOperation extends MDbOperation
	{

		private Iterable<Document> m_cmdResultObjs;
		private boolean m_hasOutputCollection;

		private CommandOperation( QueryModel queryModel )
		{
			super( queryModel );
		}

		protected void resetPreparedState( )
		{
			m_cmdResultObjs = null;
			m_hasOutputCollection = false;
			super.resetPreparedState( );
		}

		protected void prepare( MongoCollection<Document> dbCollection )
				throws OdaException
		{
			resetPreparedState( );

			QueryProperties queryProps = getModel( ).getQueryProperties( );
			QueryModel.validateCommandSyntax( queryProps.getOperationType( ),
					queryProps.getOperationExpression( ) );

			// call the specified command
			Iterable<Document> cmdResults = null;
			if ( queryProps.hasAggregateCommand( ) )
				cmdResults = callAggregateCmd( dbCollection, queryProps );
			else if ( queryProps.hasRunCommand( ) )
				cmdResults = callDBCommand( getModel( ).getConnectedDB( ),
						queryProps );
			else if ( queryProps.hasMapReduceCommand( ) )
			{
				cmdResults = callMapReduceCmd( dbCollection, queryProps );
				if ( cmdResults != null )
				{

					// run query on the output collection
					// super.prepare( cmdResults );
					m_hasOutputCollection = true;
					// return;
				}
			}

			if ( cmdResults == null )
				return;

			setResultSetMetaData( new MDbResultSetMetaData( cmdResults,
					getModel( ).getEffectiveMDSearchLimit( queryProps ),
					queryProps.getSelectedFieldNames( ),
					queryProps.isAutoFlattening( ) ) );

			// no exception thus far, ok to cache the command result objects
			m_cmdResultObjs = cmdResults;
		}

		protected MDbResultSet execute( ) throws OdaException
		{
			if ( m_cmdResultObjs != null )
				return getCommandResults( );

			return super.execute( ); // default operation type
		}

		private MDbResultSet getCommandResults( ) throws OdaException
		{
			if ( m_cmdResultObjs == null )
				throw new OdaException( Messages.mDbOp_noCmdResults );

			return new MDbResultSet( m_cmdResultObjs.iterator( ),
					getResultSetMetaData( ),
					getModel( ).getQueryProperties( ) );
		}

		protected QueryProperties getEffectiveProperties( )
		{
			// Command operations do not apply the queryExpr and sortExpr,
			// unless a MapReduce op has output to a collection
			QueryProperties queryProps = super.getEffectiveProperties( );
			if ( m_hasOutputCollection )
				return queryProps;

			// remove n/a queryExpr and sortExpr, if exists, from effective
			// properties
			if ( queryProps.getFindQueryExpr( ).isEmpty( )
					&& queryProps.getSortExpr( ).isEmpty( ) )
				return queryProps; // nothing to remove

			QueryProperties effectiveProps = QueryProperties.copy( queryProps );
			effectiveProps.setFindQueryExpr( null );
			effectiveProps.setSortExpr( null );
			return effectiveProps;
		}
	}

	static AggregateIterable<Document> callAggregateCmd(
			MongoCollection<Document> mongoCollection,
			QueryProperties queryProps ) throws OdaException
	{
		if ( !queryProps.hasAggregateCommand( ) )
			return null;
		DBObject operationExprObj = queryProps
				.getOperationExprAsParsedObject( true );
		if ( operationExprObj == null )
			return null;

		// convert user-specified operation expression to operation pipeline
		List<Document> operationList = QueryProperties
				.getObjectsAsDocumentList( operationExprObj );
		// DBObject firstOp = QueryProperties.getFirstObjectSet(
		// operationExprObj );
		if ( operationList == null )
			return null; // no valid DBObject operation

		// DBObject[] addlOps = QueryProperties.getSecondaryObjectSets(
		// operationExprObj );

		// aggregation $limit and $skip operators applies to the number
		// of documents in the *input* pipeline, and thus cannot be used to
		// apply
		// the searchLimit and numSkipDocuments properties defined for data set

		// $match and $sort pipeline operators are built in an aggregate command

		// execute the aggregate command
		try
		{
			return mongoCollection.aggregate( operationList );
		}
		catch ( RuntimeException ex )
		{
			OdaException odaEx = new OdaException(
					Messages.mDbOp_aggrCmdFailed );
			odaEx.initCause( ex );
			throw odaEx;
		}
	}

	static MapReduceIterable<Document> callMapReduceCmd(
			MongoCollection<Document> mongoCollection,
			QueryProperties queryProps ) throws OdaException
	{

		if ( !queryProps.hasMapReduceCommand( ) )
			return null;
		DBObject command = queryProps.getOperationExprAsParsedObject( false );
		if ( command == null )
			return null;

		if ( !( command instanceof BasicDBObject ) )
		{
			throw new OdaException( Messages.bind(
					"Unexpected data type ({0}) in Selected Fields property value in MapReduce command",
					command.getClass( ).getSimpleName( ) ) );
		}
		String mapFunction = null;
		String reduceFunction = null;
		Object object = command.get( QueryModel.MAP_REDUCE_MAP_FUNCTION );
		if ( object instanceof String )
		{
			mapFunction = (String) object;
		}
		else
		{
			throw new OdaException(
					Messages.bind( "Unexpected data type ({0}) in {1} function",
							command.getClass( ).getSimpleName( ),
							QueryModel.MAP_REDUCE_MAP_FUNCTION ) );
		}
		object = command.get( QueryModel.MAP_REDUCE_REDUCE_FUNCTION );
		if ( object instanceof String )
		{
			reduceFunction = (String) object;
		}
		else
		{
			throw new OdaException(
					Messages.bind( Messages.driverUtil_invalidExpr,
							command.getClass( ).getSimpleName( )
									+ " in "
									+ QueryModel.MAP_REDUCE_REDUCE_FUNCTION ) );
		}

		// mapReduce command's optional "limit" parameter applies to the number
		// of documents in the *input* collection, and thus cannot be used to
		// apply
		// the searchLimit property defined for data set

		// execute the mapreduce command
		try
		{
			MapReduceIterable<Document> mapReduceIterable = mongoCollection
					.mapReduce( mapFunction, reduceFunction );

			object = command.get( "finalize" );
			String finalizeFunction = null;
			if ( object != null )
			{
				if ( object instanceof String )
				{
					finalizeFunction = (String) object;
				}
				else
				{
					throw new OdaException( Messages.bind(
							"Unexpected data type ({0}) in {1} function",
							command.getClass( ).getSimpleName( ),
							"finalize" ) );
				}
			}
			if ( finalizeFunction != null )
			{
				mapReduceIterable = mapReduceIterable
						.finalizeFunction( finalizeFunction );
			}
			return mapReduceIterable;
		}
		catch ( RuntimeException ex )
		{
			OdaException odaEx = new OdaException(
					Messages.bind( Messages.mDbOp_mapReduceCmdFailed,
							queryProps.getOperationExpression( ) ) );
			odaEx.initCause( ex );
			throw odaEx;
		}
	}

	static Iterable<Document> callDBCommand( MongoDatabase connectedDB,
			QueryProperties queryProps ) throws OdaException
	{
		if ( !queryProps.hasRunCommand( ) )
			return null;
		DBObject command = queryProps.getOperationExprAsParsedObject( false );
		if ( command == null )
			return null;

		try
		{
			Document documentCommand = QueryProperties
					.getDocument( (BasicDBObject) command );
			Document result = connectedDB.runCommand( documentCommand );
			List<Document> iterable = new ArrayList<Document>( );
			iterable.add( result );
			return iterable;
		}
		catch ( RuntimeException ex )
		{
			OdaException odaEx = new OdaException(
					Messages.bind( Messages.mDbOp_dbCmdFailed,
							queryProps.getOperationExpression( ) ) );
			odaEx.initCause( ex );
			throw odaEx;
		}
	}

}
