/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.AbstractDataEngine;
import org.eclipse.birt.report.engine.data.dte.CubeResultSet;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class QueryUtil
{

	/*
	 * Fetch all the result sets that the instanceID refers to.
	 */
	public static List<IBaseResultSet> getResultSet( ReportContent report,
			InstanceID instanceID )
	{
		Report design = report.getDesign( );
		ExecutionContext context = report.getExecutionContext( );
		try
		{
			ArrayList<QueryTask> plan = createPlan( design, instanceID );

			return executePlan( context, plan );
		}
		catch ( EngineException ex )
		{
			context.addException( ex );
		}
		return null;
	}

	/*
	 * create a plan which contains only table queries.
	 */
	static public ArrayList<QueryTask> createTablePlan( Report report,
			InstanceID instanceId ) throws EngineException
	{
		InstanceID iid = instanceId;
		IBaseQueryDefinition query = null;
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			ReportItemDesign design = (ReportItemDesign) report
					.getReportItemByID( id );
			IDataQueryDefinition dataQuery = design.getQuery( );

			if ( dataQuery != null )
			{
				ReportItemHandle handle = (ReportItemHandle) design.getHandle( );
				if ( !handle.allowExport( ) )
				{
					throw new EngineException(
							MessageConstants.RESULTSET_EXTRACT_ERROR );
				}
				if ( !( dataQuery instanceof IBaseQueryDefinition ) )
				{
					// it is a cube query, as we don't support it now, exit.
					return null;
				}
				query = (IBaseQueryDefinition) dataQuery;
				break;
			}
			iid = iid.getParentID( );
		}
		// At this point, query refers to the last query in the query chain.
		ArrayList<DataSetID> dsIDs = new ArrayList<DataSetID>( );
		ArrayList plan = new ArrayList( );
		while ( query != null )
		{
			while ( iid != null )
			{
				if ( iid.getDataID( ) != null )
				{
					DataID dataId = iid.getDataID( );
					DataSetID dsId = dataId.getDataSetID( );
					boolean found = false;
					Iterator<DataSetID> itr = dsIDs.iterator( );
					while ( itr.hasNext( ) )
					{
						if ( itr.next( ).equals( dsId ) )
						{
							found = true;
							break;
						}
					}
					if ( !found )
					{
						dsIDs.add( dsId );
						QueryTask task = new QueryTask( query, dsId,
								(int) dataId.getRowID( ), iid );
						plan.add( task );
						break;
					}
				}
				iid = iid.getParentID( );
			}
			if ( iid == null )
			{
				break;
			}
			query = query.getParentQuery( );
		}
		// At this point, query refers to the top most query
		QueryTask task = new QueryTask( query, null, -1, null );
		plan.add( task );
		return plan;
	}

	/*
	 * create a plan; this plan gets all data queries.
	 */
	static public ArrayList<QueryTask> createPlan( Report report,
			InstanceID instanceId )
	{
		ArrayList<IDataQueryDefinition> queries = new ArrayList<IDataQueryDefinition>( );
		InstanceID iid = instanceId;
		InstanceID dsIID = null;
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			ReportItemDesign design = (ReportItemDesign) report
					.getReportItemByID( id );
			if ( design != null )
			{
				IDataQueryDefinition query = design.getQuery( );
				if ( query != null )
				{
					queries.add( query );
					if ( dsIID == null )
					{
						dsIID = iid;
					}
				}
			}
			iid = iid.getParentID( );
		}
		if ( queries.size( ) == 0 )
			return null;
		ArrayList datasets = new ArrayList( );
		ArrayList plan = new ArrayList( );
		for ( IDataQueryDefinition query : queries )
		{
			while ( dsIID != null )
			{
				if ( dsIID.getDataID( ) != null )
				{
					DataID dataId = dsIID.getDataID( );
					DataSetID dsId = dataId.getDataSetID( );
					if ( !datasets.contains( dsId ) )
					{
						datasets.add( dsId );
						QueryTask task = null;
						if ( dataId.getCellID( ) != null )
						{
							task = new QueryTask( query, dsId, dataId
									.getCellID( ), dsIID );
						}
						else
						{
							task = new QueryTask( query, dsId, (int) dataId
									.getRowID( ), dsIID );
						}
						plan.add( task );
						break;
					}

				}
				dsIID = dsIID.getParentID( );
			}
			if ( dsIID == null )
			{
				break;
			}
		}
		QueryTask task = new QueryTask( queries.get( queries.size( ) - 1 ),
				null, -1, null );
		plan.add( task );
		return plan;
	}

	/*
	 * 
	 */
	static public List executePlan( ExecutionContext executionContext,
			ArrayList<QueryTask> plan ) throws EngineException
	{
		if ( plan == null || plan.size( ) == 0 )
		{
			return null;
		}
		List results = new ArrayList( );
		IBaseResultSet parent = null;
		try
		{
			for ( int current = plan.size( ) - 1; current >= 0; current-- )
			{
				if ( parent != null )
				{
					results.add( parent );
				}
				QueryTask task = plan.get( current );
				IDataQueryDefinition query = task.getQuery( );
				if ( task.getParent( ) == null )
				{
					// this is a top query
					String rset = getResultSetID( executionContext, null, "-1",
							query );
					IBaseQueryResults baseResults = executeQuery( null, query,
							rset, executionContext );
					if ( baseResults == null )
						return null;
					if ( baseResults instanceof IQueryResults )
					{
						parent = new QueryResultSet( executionContext
								.getDataEngine( ), executionContext,
								(IQueryDefinition) query,
								(IQueryResults) baseResults );
					}
					else if ( baseResults instanceof ICubeQueryResults )
					{
						parent = new CubeResultSet( executionContext
								.getDataEngine( ), executionContext,
								(ICubeQueryDefinition) query,
								(ICubeQueryResults) baseResults );
					}
					else
					{
						// should not go here
						return null;
					}
				}
				else
				{
					if ( parent == null )
					{
						throw new EngineException(
								MessageConstants.RESULTSET_EXTRACT_ERROR );
					}

					// skip parent to the proper position
					String parentId = null;
					if ( parent instanceof IQueryResultSet )
					{
						IResultIterator parentItr = ( (IQueryResultSet) parent )
								.getResultIterator( );
						parentItr.moveTo( task.getRowID( ) );
						parentId = ( (QueryResultSet) parent )
								.getQueryResultsID( );
					}
					else if ( parent instanceof ICubeResultSet )
					{
						( (ICubeResultSet) parent ).skipTo( task.getCellID( ) );
						parentId = ( (CubeResultSet) parent )
								.getQueryResultsID( );
					}

					if ( query instanceof ISubqueryDefinition )
					{
						IResultIterator parentItr = ( (QueryResultSet) parent )
								.getResultIterator( );
						String queryName = query.getName( );
						IResultIterator itr = parentItr
								.getSecondaryIterator( executionContext
										.getScriptContext( ), queryName );
						parent = new QueryResultSet( (QueryResultSet) parent,
								(ISubqueryDefinition) query, itr );
					}
					else
					{
						String rset = getResultSetID( executionContext,
								parentId, parent.getRawID( ), query  );
						IBaseQueryResults baseResults = executeQuery( parent
								.getQueryResults( ), query, rset,
								executionContext );
						if ( baseResults instanceof IQueryResults )
						{
							parent = new QueryResultSet( executionContext
									.getDataEngine( ), executionContext,
									parent, (IQueryDefinition) query,
									(IQueryResults) baseResults );
						}
						else if ( baseResults instanceof ICubeQueryResults )
						{
							if ( query instanceof ICubeQueryDefinition )
							{
								parent = new CubeResultSet( executionContext
										.getDataEngine( ), executionContext,
										parent, (ICubeQueryDefinition) query,
										(ICubeQueryResults) baseResults );
							}
							else if ( query instanceof ISubCubeQueryDefinition )
							{
								parent = new CubeResultSet( executionContext
										.getDataEngine( ), executionContext,
										parent,
										(ISubCubeQueryDefinition) query,
										(ICubeQueryResults) baseResults );
							}
						}
						else
						{
							// should not go here
							return null;
						}
					}
				}
			}
		}
		catch ( EngineException ex )
		{
			throw ex;
		}
		catch ( BirtException ex )
		{
			throw new EngineException( ex );
		}
		if ( parent != null && !results.contains( parent ) )
		{
			results.add( parent );
		}
		return results;
	}

	private static String getResultSetID( ExecutionContext context,
			String parent, String rowId, IDataQueryDefinition query )
	{
		IDataEngine engine = context.getDataEngine( );
		if ( engine instanceof AbstractDataEngine )
		{
			AbstractDataEngine dataEngine = (AbstractDataEngine) engine;
			String queryId = dataEngine.getQueryID( query );
			return dataEngine.getResultID( parent, rowId, queryId );
		}
		return null;
	}

	static public void processQueryExtensions( IDataQueryDefinition query,
			ExecutionContext executionContext ) throws EngineException
	{
		String[] extensions = executionContext.getEngineExtensions( );
		if ( extensions != null )
		{
			EngineExtensionManager manager = executionContext
					.getEngineExtensionManager( );
			for ( String extensionName : extensions )
			{
				IDataExtension extension = manager
						.getDataExtension( extensionName );
				if ( extension != null )
				{
					extension.prepareQuery( query );
				}
			}
		}
	}

	/**
	 * This method executes IQueryDefinition, ICubeQueryDefinition and
	 * ISubCubeQueryDefinition; ISubqueryDefinition is not included here.
	 */
	static public IBaseQueryResults executeQuery( IBaseQueryResults parent,
			IDataQueryDefinition query, String rset,
			ExecutionContext executionContext ) throws EngineException
	{
		try
		{
			DataRequestSession dataSession = executionContext.getDataEngine( )
					.getDTESession( );
			if ( dataSession == null )
				return null;
			Map appContext = executionContext.getAppContext( );
			ScriptContext scriptContext = executionContext.getScriptContext( );
			processQueryExtensions( query, executionContext );
			if ( query instanceof QueryDefinition )
			{
				QueryDefinition tmpQuery = (QueryDefinition) query;
				tmpQuery.setQueryResultsID( rset );
				IPreparedQuery pQuery = dataSession.prepare( tmpQuery,
						appContext );
				if ( pQuery == null )
					return null;
				return dataSession.execute(  pQuery, parent, scriptContext );
			}
			else if ( query instanceof ICubeQueryDefinition )
			{
				ICubeQueryDefinition cubeQuery = (ICubeQueryDefinition) query;
				cubeQuery.setQueryResultsID( rset );
				IPreparedCubeQuery pQuery = dataSession.prepare( cubeQuery,
						appContext );
				if ( pQuery == null )
					return null;
				return dataSession.execute(  pQuery, parent, scriptContext );
			}
			else if ( query instanceof ISubCubeQueryDefinition )
			{
				ISubCubeQueryDefinition cubeQuery = (ISubCubeQueryDefinition) query;
				IPreparedCubeQuery pQuery = dataSession.prepare( cubeQuery,
						appContext );
				if ( pQuery == null )
					return null;
				return dataSession.execute(  pQuery, parent, scriptContext );
			}
		}
		catch ( BirtException ex )
		{
			throw new EngineException( ex );
		}
		return null;
	}
}
