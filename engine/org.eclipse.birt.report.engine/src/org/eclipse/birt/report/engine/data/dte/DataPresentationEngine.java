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

package org.eclipse.birt.report.engine.data.dte;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;

public class DataPresentationEngine extends AbstractDataEngine
{

	private IDocArchiveReader reader;

	public DataPresentationEngine( ExecutionContext ctx,
			IDocArchiveReader reader )
	{
		context = ctx;
		this.reader = reader;
		// fd = getRandomAccessFile();

		loadDteMetaInfo( );

		try
		{
			DataEngineContext dteContext = DataEngineContext.newInstance(
					DataEngineContext.MODE_PRESENTATION, ctx.getSharedScope( ),
					reader, null );
			dataEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
		}
	}

	public void prepare( Report report, Map appContext )
	{
		// build report queries
		new ReportQueryBuilder( ).build( report, context );

		doPrepareQueryID( report, appContext );

	}

	private void loadDteMetaInfo( )
	{
		try
		{
			DataInputStream dis = new DataInputStream( reader
					.getStream( DATA_META_STREAM ) );
			mapQueryIDToResultSetIDs = new HashMap( );
			int size = IOUtil.readInt( dis );
			for ( int i = 0; i < size; i++ )
			{
				String queryId = IOUtil.readString( dis );
				LinkedList ridList = new LinkedList( );
				readStringList( dis, ridList );
				mapQueryIDToResultSetIDs.put( queryId, ridList );
			}

			size = IOUtil.readInt( dis );
			queryResultRelations = new ArrayList( );
			for ( int i = 0; i < size; i++ )
			{
				String parentRSID = IOUtil.readString( dis );
				String rowid = IOUtil.readString( dis );
				String qid = IOUtil.readString( dis );
				String resultSetID = IOUtil.readString( dis );
				Key key = new Key( parentRSID, rowid, qid, resultSetID );
				queryResultRelations.add( key );
			}

			size = IOUtil.readInt( dis );
			queryExpressionIDs = new ArrayList( );
			for ( int i = 0; i < size; i++ )
			{
				QueryID queryId = new QueryID( );
				readQueryID( dis, queryId );
				queryExpressionIDs.add( queryId );
			}
			dis.close( );
		}
		catch ( IOException ioe )
		{
			context.addException( new EngineException(
					"Can't load the data in report document", ioe ) );
			logger.log( Level.SEVERE, ioe.getMessage( ), ioe );
		}
	}

	private void readQueryID( DataInputStream dis, QueryID queryId )
			throws IOException
	{
		readStringList( dis, queryId.beforeExpressionIDs );
		readStringList( dis, queryId.afterExpressionIDs );
		readStringList( dis, queryId.rowExpressionIDs );

		int size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			QueryID qid = new QueryID( );
			readQueryID( dis, qid );
			queryId.groupIDs.add( qid );
		}

		size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			QueryID subQid = new QueryID( );
			readQueryID( dis, subQid );
			queryId.subqueryIDs.add( subQid );
		}
	}

	private void readStringList( DataInputStream dis, List list )
			throws IOException
	{
		int size = IOUtil.readInt( dis );
		for ( int i = 0; i < size; i++ )
		{
			String str = IOUtil.readString( dis );
			list.add( str );
		}
	}

	protected void doPrepareQueryID( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		if ( queryExpressionIDs.size( ) != report.getQueries( ).size( ) )
		{
			EngineException ex = new EngineException(
					"Data in report document is not couple with report design" );
			context.addException( ex );
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
			return;
		}
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IQueryDefinition queryDef = (IQueryDefinition) report.getQueries( )
					.get( i );
			QueryID queryID = (QueryID) queryExpressionIDs.get( i );
			setQueryIDs( queryDef, queryID );
		}
	}

	private void setQueryIDs( IBaseQueryDefinition query, QueryID queryID )
	{
		setExpressionID( queryID.beforeExpressionIDs, query
				.getBeforeExpressions( ) );
		setExpressionID( queryID.afterExpressionIDs, query
				.getAfterExpressions( ) );
		setExpressionID( queryID.rowExpressionIDs, query.getRowExpressions( ) );

		Iterator subIter = query.getSubqueries( ).iterator( );
		Iterator subIDIter = queryID.subqueryIDs.iterator( );
		while ( subIter.hasNext( ) && subIDIter.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) subIter.next( );
			QueryID subID = (QueryID) subIDIter.next( );
			setQueryIDs( subquery, subID );
		}

		Iterator grpIter = query.getGroups( ).iterator( );
		Iterator grpIDIter = queryID.groupIDs.iterator( );
		while ( grpIter.hasNext( ) && grpIDIter.hasNext( ) )
		{
			IGroupDefinition group = (IGroupDefinition) grpIter.next( );

			QueryID groupID = (QueryID) grpIDIter.next( );
			setExpressionID( groupID.beforeExpressionIDs, group
					.getBeforeExpressions( ) );
			setExpressionID( groupID.afterExpressionIDs, group
					.getAfterExpressions( ) );
			setExpressionID( groupID.rowExpressionIDs, group
					.getRowExpressions( ) );

			Iterator grpSubIter = group.getSubqueries( ).iterator( );
			Iterator grpIDSubIter = groupID.subqueryIDs.iterator( );
			while ( grpSubIter.hasNext( ) && grpIDSubIter.hasNext( ) )
			{
				ISubqueryDefinition grpSubquery = (ISubqueryDefinition) grpSubIter
						.next( );
				QueryID grpSubID = (QueryID) grpIDSubIter.next( );
				setQueryIDs( grpSubquery, grpSubID );
			}
		}
	}

	private void setExpressionID( Collection idArray, Collection exprArray )
	{
		if ( idArray.size( ) != exprArray.size( ) )
		{
			EngineException ex = new EngineException(
					"Data in report document is not couple with report design" );
			context.addException( ex );
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
			return;
		}
		Iterator idIter = idArray.iterator( );
		Iterator exprIter = exprArray.iterator( );
		while ( idIter.hasNext( ) )
		{
			String id = (String) idIter.next( );
			IBaseExpression expr = (IBaseExpression) exprIter.next( );
			expr.setID( id );
		}
	}

	protected IResultSet doExecute( IBaseQueryDefinition query )
	{
		assert query instanceof IQueryDefinition;

		String queryID = (String) queryIDMap.get( query );

		try
		{
			IQueryResults queryResults = null;
			DteResultSet parentResult = (DteResultSet) getParentResultSet( );
			if ( parentResult != null )
			{
				queryResults = parentResult.getQueryResults( );
			}
			DteResultSet resultSet = null;
			String resultSetID = null;

			if ( queryResults == null )
			{
				List resultSetIDs = (List) mapQueryIDToResultSetIDs
						.get( queryID );
				if ( resultSetIDs != null && !resultSetIDs.isEmpty( ) )
				{
					resultSetID = (String) resultSetIDs.get( 0 );
				}
			}
			else
			{
				String rowid = Long.toString( parentResult
						.getCurrentPosition( ) );

				resultSetID = getResultID( queryResults.getID( ), rowid,
						queryID );
			}
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}
			queryResults = dataEngine.getQueryResults( resultSetID );
			validateQueryResult( queryResults );
			resultSet = new DteResultSet( queryResults, this, context );

			queryResultStack.addLast( resultSet );

			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
			return null;
		}
	}

	public void close( )
	{
		if ( queryResultStack.size( ) > 0 )
		{
			queryResultStack.removeLast( );
		}
	}

	public void shutdown( )
	{
		assert ( queryResultStack.size( ) == 0 );
		dataEngine.shutdown( );
	}
}
