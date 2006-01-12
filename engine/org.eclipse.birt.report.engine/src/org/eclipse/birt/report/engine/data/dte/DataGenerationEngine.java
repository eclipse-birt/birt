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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;

public class DataGenerationEngine extends AbstractDataEngine
{

	private IDocArchiveWriter writer;

	public DataGenerationEngine( ExecutionContext ctx, IDocArchiveWriter writer )
	{
		context = ctx;
		this.writer = writer;

		try
		{
			DataEngineContext dteContext = DataEngineContext.newInstance(
					DataEngineContext.MODE_GENERATION, ctx.getSharedScope( ),
					null, writer );
			dataEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( BirtException ex )
		{
			ex.printStackTrace( );
		}
	}

	public void prepare( Report report )
	{
		prepare( report, null );
	}

	protected void doPrepareQueryID( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IQueryDefinition queryDef = (IQueryDefinition) report.getQueries( )
					.get( i );
			try
			{
				IPreparedQuery preparedQuery = dataEngine.prepare( queryDef,
						appContext );
				mapQueryToPreparedQuery.put( queryDef, preparedQuery );

				QueryID qid = getQueryIDs( queryDef );
				queryExpressionIDs.add( qid );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ) );
				context.addException( be );
			}
		}
	}

	private QueryID getQueryIDs( IBaseQueryDefinition query )
	{
		QueryID queryID = new QueryID( );
		addIDToExpression( queryID.beforeExpressionIDs, query
				.getBeforeExpressions( ).iterator( ) );
		addIDToExpression( queryID.afterExpressionIDs, query
				.getAfterExpressions( ).iterator( ) );
		addIDToExpression( queryID.rowExpressionIDs, query.getRowExpressions( )
				.iterator( ) );

		Iterator subIter = query.getSubqueries( ).iterator( );
		while ( subIter.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) subIter.next( );
			QueryID qid = getQueryIDs( subquery );
			queryID.subqueryIDs.add( qid );
		}

		Iterator grpIter = query.getGroups( ).iterator( );
		while ( grpIter.hasNext( ) )
		{
			IGroupDefinition group = (IGroupDefinition) grpIter.next( );
			QueryID groupID = new QueryID( );
			addIDToExpression( groupID.beforeExpressionIDs, group
					.getBeforeExpressions( ).iterator( ) );
			addIDToExpression( groupID.afterExpressionIDs, group
					.getAfterExpressions( ).iterator( ) );
			addIDToExpression( groupID.rowExpressionIDs, group
					.getRowExpressions( ).iterator( ) );

			Iterator grpSubIter = group.getSubqueries( ).iterator( );
			while ( grpSubIter.hasNext( ) )
			{
				ISubqueryDefinition grpSubquery = (ISubqueryDefinition) grpSubIter
						.next( );
				QueryID qid = getQueryIDs( grpSubquery );
				groupID.subqueryIDs.add( qid );
			}
			queryID.groupIDs.add( groupID );
		}
		return queryID;
	}

	private void addIDToExpression( Collection idArray, Iterator iter )
	{
		while ( iter.hasNext( ) )
		{
			IBaseExpression expr = (IBaseExpression) iter.next( );
			idArray.add( expr.getID( ) );
		}
	}

	protected IResultSet doExecute( IBaseQueryDefinition query )
	{
		assert query instanceof IQueryDefinition;

		IPreparedQuery preparedQuery = (IPreparedQuery) mapQueryToPreparedQuery
				.get( query );
		if (preparedQuery == null)
		{
			return null;
		}
		String queryID = (String) queryIDMap.get( query );
		assert preparedQuery != null;
		Scriptable queryScope = context.getSharedScope( );

		IQueryResults queryResults = null;
		DteResultSet parentResult = (DteResultSet) getParentResultSet( );
		if ( parentResult != null )
		{
			queryResults = parentResult.getQueryResults( );
		}

		DteResultSet resultSet = null;
		try
		{

			if ( queryResults == null )
			{
				queryResults = preparedQuery.execute( queryScope );
				validateQueryResult( queryResults );
				resultSet = new DteResultSet( queryResults, this, context );
			}
			else
			{ // the query is NestedQuery
				String parentRSID = queryResults.getID( );
				String rowid = "" + parentResult.getCurrentPosition( );

				queryResults = preparedQuery.execute( queryResults, queryScope );
				validateQueryResult( queryResults );

				String childRSID = queryResults.getID( );
				Key key = new Key( parentRSID, rowid, childRSID );
				queryResultRelations.add( key );

				resultSet = new DteResultSet( queryResults, this, context );
			}

			queryResultStack.addLast( resultSet );
			LinkedList qidList = (LinkedList) mapQueryIDToResultSetIDs
					.get( queryID );
			if ( qidList == null )
			{
				qidList = new LinkedList( );
			}
			qidList.add( queryResults.getID( ) );
			mapQueryIDToResultSetIDs.put( queryID, qidList );
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}
		return resultSet;
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
		// store meta data of DtE
		storeDteMetaInfo( );
		dataEngine.shutdown( );
	}

	private void storeDteMetaInfo( )
	{
		try
		{
			DataOutputStream dos = new DataOutputStream( writer
					.createRandomAccessStream( DATA_META_STREAM ) );
			
			int size = mapQueryIDToResultSetIDs.size( );
			IOUtil.writeInt(dos, size );
			Set keySet = mapQueryIDToResultSetIDs.keySet( );
			Iterator keyIter = keySet.iterator( );
			while( keyIter.hasNext( ))
			{
				String queryId = (String)keyIter.next( );
				IOUtil.writeString( dos , queryId );
				LinkedList resultList = (LinkedList)mapQueryIDToResultSetIDs
									.get( queryId );
				writeStringList( dos, resultList );
			}
			
			size = queryResultRelations.size( );
			IOUtil.writeInt( dos, size );
			for( int i=0; i<size; i++ )
			{
				Key key = (Key)queryResultRelations.get( i );
				IOUtil.writeString( dos, key.parentRSID );
				IOUtil.writeString( dos, key.resultSetID );
				IOUtil.writeString( dos, key.rowid );
			}
			
			size = queryExpressionIDs.size( );
			IOUtil.writeInt( dos, size );
			for( int i=0; i<size; i++)
			{
				QueryID queryId = ( QueryID )queryExpressionIDs.get( i );
				writeQueryID( dos, queryId );
			}
			dos.close( );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
	}
	private void writeQueryID( DataOutputStream dos, QueryID queryId ) throws IOException
	{
		writeStringList( dos, queryId.beforeExpressionIDs );
		writeStringList( dos, queryId.afterExpressionIDs );
		writeStringList( dos, queryId.rowExpressionIDs );
		
		int size = queryId.groupIDs.size( );
		IOUtil.writeInt( dos, size );
		for( int i=0; i<size; i++ )
		{
			QueryID qid = (QueryID)queryId.groupIDs.get( i );
			writeQueryID( dos, qid );
		}
		
		size = queryId.subqueryIDs.size( );
		IOUtil.writeInt( dos, size );
		for( int i=0; i<size; i++ )
		{
			QueryID subQid = (QueryID)queryId.subqueryIDs.get( i );
			writeQueryID( dos, subQid );
		}
	}
	
	private void writeStringList( DataOutputStream dos, List list ) throws IOException
	{
		int size = list.size( );
		IOUtil.writeInt( dos, size );
		for( int i=0; i<size; i++ )
		{
			String str = (String)list.get( i );
			IOUtil.writeObject( dos, str );
		}
	}
}
