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
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.mozilla.javascript.Scriptable;

public class QueryUtil
{

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
	 * create a plan.
	 */
	static public ArrayList<QueryTask> createPlan( Report report,
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

	static public List executePlan( ExecutionContext executionContext,
			ArrayList<QueryTask> plan ) throws EngineException
	{
		if ( plan == null || plan.size( ) == 0 )
		{
			return null;
		}
		List results = new ArrayList( );
		QueryResultSet parent = null;
		try
		{
			for ( int current = plan.size( ) - 1; current >= 0; current-- )
			{
				if ( parent != null )
				{
					results.add( parent );
				}
				QueryTask task = plan.get( current );
				IBaseQueryDefinition query = task.query;
				if ( task.parent == null )
				{
					// this is a top query
					IQueryResults qryRS = executeQuery( null,
							(QueryDefinition) query, executionContext );
					if ( qryRS == null )
					{
						return null;
					}
					parent = new QueryResultSet( executionContext
							.getDataEngine( ), executionContext,
							(IQueryDefinition) query, qryRS );
				}
				else
				{
					if ( parent == null )
					{
						throw new EngineException(
								MessageConstants.RESULTSET_EXTRACT_ERROR );
					}
					IResultIterator parentItr = parent.getResultIterator( );
					parentItr.moveTo( task.rowId );
					if ( query instanceof IQueryDefinition )
					{
						// this is a nested query
						IQueryResults qryRS = executeQuery( null,
								(QueryDefinition) query, executionContext );
						if ( qryRS == null )
						{
							return null;
						}
						parent = new QueryResultSet( executionContext
								.getDataEngine( ), executionContext, parent,
								(IQueryDefinition) query, qryRS );
					}
					else if ( query instanceof ISubqueryDefinition )
					{
						// this is a sub query
						String queryName = query.getName( );
						Scriptable scope = executionContext.getSharedScope( );
						IResultIterator itr2 = parentItr.getSecondaryIterator(
								queryName, scope );
						parent = new QueryResultSet( parent,
								(ISubqueryDefinition) query, itr2 );
					}
					else
					{
						// should not enter here
						return null;
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
		if( parent != null && !results.contains( parent ) )
		{
			results.add( parent );
		}
		return results;
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

	static public IQueryResults executeQuery( String rset,
			QueryDefinition query, ExecutionContext executionContext )
			throws EngineException
	{
		try
		{
			( (QueryDefinition) query ).setQueryResultsID( rset );

			DataRequestSession dataSession = executionContext.getDataEngine( )
					.getDTESession( );
			Scriptable scope = executionContext.getSharedScope( );
			Map appContext = executionContext.getAppContext( );
			// prepare the query
			processQueryExtensions( query, executionContext );

			if ( dataSession == null )
			{
				return null;
			}
			IPreparedQuery pQuery = dataSession.prepare( query, appContext );
			if ( pQuery == null )
			{
				return null;
			}
			return pQuery.execute( scope );
		}
		catch ( BirtException ex )
		{
			throw new EngineException( ex );
		}
	}
}
