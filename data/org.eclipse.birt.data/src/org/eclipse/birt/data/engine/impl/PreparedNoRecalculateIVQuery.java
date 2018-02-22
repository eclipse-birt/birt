/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.NoRecalculateIVQuery;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.NoRecalculateIVResultSet;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Prepared query for no recalculate IV query.
 */

public class PreparedNoRecalculateIVQuery extends PreparedIVQuerySourceQuery
{

	private String resultSetId;

	PreparedNoRecalculateIVQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext,
			IQueryContextVisitor visitor ) throws DataException
	{
		super( dataEngine, queryDefn, appContext, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.PreparedIVQuerySourceQuery#prepareQuery
	 * ()
	 */
	protected void prepareQuery( ) throws DataException
	{
		// Load previous query.
		try
		{
			this.queryResults = PreparedQueryUtil.newInstance( engine,
					(IQueryDefinition) queryDefn.getSourceQuery( ),
					this.appContext ).execute( null );
			( (NoRecalculateIVQuery) queryDefn ).setSourceQuery( null );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}

		if ( !hasBinding )
		{
			IBinding[] bindings = new IBinding[0];
			if ( queryResults != null
					&& queryResults.getPreparedQuery( ) != null )
			{
				IQueryDefinition queryDefinition = queryResults.getPreparedQuery( )
						.getReportQueryDefn( );
				bindings = (IBinding[]) queryDefinition.getBindings( )
						.values( )
						.toArray( new IBinding[0] );
			}
			for ( int i = 0; i < bindings.length; i++ )
			{
				IBinding binding = bindings[i];
				if ( !this.queryDefn.getBindings( )
						.containsKey( binding.getBindingName( ) ) )
					this.queryDefn.addBinding( new Binding( binding.getBindingName( ),
							new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( binding.getBindingName( ) ),
									binding.getDataType( ) ) ) );
			}
		}
	}

	protected void initializeExecution( IBaseQueryResults outerResults,
			Scriptable scope ) throws DataException
	{
		String basedID = queryDefn.getQueryResultsID( );

		String _1partID = QueryResultIDUtil.get1PartID( basedID );
		if ( _1partID == null )
			resultSetId = basedID;
		else
			resultSetId = _1partID;
	}

	protected IQueryResults produceQueryResults(
			IBaseQueryResults outerResults, Scriptable scope )
			throws DataException
	{
		QueryResults queryResults = preparedQuery.doPrepare( outerResults,
				scope,
				newExecutor( ),
				this );
		queryResults.setID( resultSetId );
		return queryResults;
	}

	protected QueryExecutor newExecutor( )
	{
		return new NoUpdateAggrFilterIVQuerySourceExecutor( engine.getSession( )
				.getSharedScope( ) );
	}

	protected class NoUpdateAggrFilterIVQuerySourceExecutor extends IVQuerySourceExecutor
	{
		NoUpdateAggrFilterIVQuerySourceExecutor( Scriptable sharedScope )
		{
			super( sharedScope );
			ignoreDataSetFilter = true;
		}

		protected IResultIterator executeOdiQuery( IEventHandler eventHandler )
				throws DataException
		{
			try
			{
				org.eclipse.birt.data.engine.impl.document.ResultIterator sourceData = 
						(org.eclipse.birt.data.engine.impl.document.ResultIterator) queryResults.getResultIterator( );
				
				IDataSetPopulator querySourcePopulator = new IVQuerySourcePopulator( sourceData,
						getResultClass( ),
						query,
						queryDefn.getStartingRow( ) );
				return new NoRecalculateIVResultSet( query,
						resultClass,
						querySourcePopulator,
						eventHandler,
						engine.getSession( ),
						sourceData.getGroupInfos( ) );
				
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}
		}
	}

}
