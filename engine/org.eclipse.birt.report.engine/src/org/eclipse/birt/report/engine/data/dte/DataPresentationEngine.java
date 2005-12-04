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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
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
import org.eclipse.birt.report.engine.ir.ReportElementDesign;

public class DataPresentationEngine extends AbstractDataEngine
{
	private IDocArchiveReader reader;
	
	public DataPresentationEngine( ExecutionContext ctx, IDocArchiveReader reader )
	{
		context = ctx;
		this.reader = reader;
		// fd = getRandomAccessFile();

		loadDteMetaInfo( );

		try
		{
			DataEngineContext dteContext = DataEngineContext.newInstance(
					DataEngineContext.MODE_PRESENTATION, ctx.getSharedScope( ),
					reader, 
					null );
			dataEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( BirtException ex )
		{
			ex.printStackTrace( );
		}
	}

	private void loadDteMetaInfo( )
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream( reader.getStream( DATA_META_STREAM ) );
			mapQueryIDToResultSetIDs = (HashMap) ois.readObject( );
			queryResultRelations = (ArrayList) ois.readObject( );
			queryExpressionIDs = (ArrayList) ois.readObject( );
			ois.close( );
		}
		catch ( IOException ioe )
		{
			// FIXME handling exception
			ioe.printStackTrace( );
		}
		catch ( ClassNotFoundException cnfe )
		{
			cnfe.printStackTrace( );
		}
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
				QueryID queryID = (QueryID) queryExpressionIDs.get( i );
				setQueryIDs( queryDef, queryID );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ) );
				context.addException( be );
			}
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
			// FIXME ignore for now, we should throw BirtExpression here
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

		String queryID = String.valueOf( ((ReportElementDesign) queryIDMap.get( query )).getID( ) );
		
		try
		{
			IQueryResults queryResults = null;
			DteResultSet parentResult = (DteResultSet) getParentResultSet( );
			if ( parentResult != null )
			{
				queryResults = parentResult.getQueryResults( );
			}
			DteResultSet resultSet = null;
			String resultSetID;

			if ( queryResults == null )
			{
				resultSetID = (String) ( (LinkedList) mapQueryIDToResultSetIDs
						.get( queryID ) ).get( 0 );

			}
			else
			{
				String rowid = "" + parentResult.getCurrentPosition( );
				resultSetID = getResultID( queryResults.getID( ), rowid );
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
