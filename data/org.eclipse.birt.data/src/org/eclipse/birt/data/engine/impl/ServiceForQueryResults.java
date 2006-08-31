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
package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
*
*/
public class ServiceForQueryResults implements IServiceForQueryResults
{
	private DataEngineContext 			context;
	private IPreparedQueryService       queryService;
	private IQueryExecutor				queryExecutor;

	private PreparedDataSourceQuery 	reportQuery;
	private IBaseQueryDefinition 		queryDefn;
	
	private ExprManager					exprManager;
	
	private Scriptable 					scope;
	private int 						nestedLevel;
	
	/**
	 * @param reportQuery
	 * @param query
	 */
	public ServiceForQueryResults( DataEngineContext context, Scriptable scope,
			int nestedLevel, PreparedDataSourceQuery reportQuery,
			IPreparedQueryService query, QueryExecutor queryExecutor,
			IBaseQueryDefinition queryDefn, ExprManager exprManager )
	{
		assert reportQuery != null && queryExecutor != null;

		this.context = context;
		this.scope = scope;
		this.nestedLevel = nestedLevel;
		this.reportQuery = reportQuery;
		this.queryService = query;
		this.queryExecutor = queryExecutor;
		this.queryDefn = queryDefn;
		this.exprManager = exprManager;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getContext()
	 */
	public DataEngineContext getContext( )
	{
		return this.context;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getScope()
	 */
	public Scriptable getScope( )
	{
		return this.scope;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getNestedLevel()
	 */
	public int getNestedLevel( )
	{
		return this.nestedLevel;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryDefn()
	 */
	public IBaseQueryDefinition getQueryDefn( )
	{
		return queryDefn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery( )
	{
		return this.reportQuery;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getGroupLevel()
	 */
	public int getGroupLevel( )
	{
		if ( queryService instanceof PreparedSubquery )
		{
			PreparedSubquery subQuery = (PreparedSubquery) queryService;
			return subQuery.getGroupLevel( );
		}
		else if ( queryService instanceof PreparedIVSubQuery )
		{
			PreparedIVSubQuery subQuery = (PreparedIVSubQuery) queryService;
			return subQuery.getGroupLevel( );
		}
		else
		{
			return 0;
		}
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
	 */
	public DataSetRuntime getDataSetRuntime( )
	{
		return queryExecutor.getDataSet( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntimeList()
	 */
	public DataSetRuntime[] getDataSetRuntimes( int count )
	{
		assert count >= 0;
		
		DataSetRuntime[] dsRuns = new DataSetRuntime[count];
		
		if ( count > 1 )
		{
			DataSetRuntime[] innerDsRuns = null;
			IQueryExecutor executor = queryExecutor;
			innerDsRuns = executor.getNestedDataSets( count - 1 );
			for ( int i = 0; i < count - 1; i++ )
				dsRuns[i] = innerDsRuns[i];
		}
		
		dsRuns[count - 1] = queryExecutor.getDataSet( );
		return dsRuns;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		return queryExecutor.getResultMetaData( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getResultIterator()
	 */
	public IResultIterator executeQuery( ) throws DataException
	{
		queryExecutor.execute( new EventHandler( ) );
		return queryExecutor.getOdiResultSet( );
	}
	
	/**
	 * The row object can have different meaning in the different context. In
	 * the phrase of data set process, the row refers to the data set row, but
	 * in the phrase of result set process, the row refers to the result set
	 * row. So in the first phrase, let the JSRowObject stands for the row, and
	 * in the second phrase, let the JSResultSetRowObject stands for the row.
	 * This event handler class will help to do such a switch.
	 */
	private class EventHandler implements IEventHandler
	{
		//
		private JSResultSetRow jsResultSetRow;
		private IExecutorHelper helper;
		
		/*
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#handleProcessEndOfDataSet
		 *      (org.eclipse.birt.data.engine.odi.IResultIterator)
		 */
		public void handleEndOfDataSetProcess( IResultIterator resultIterator )
		{
			jsResultSetRow = new JSResultSetRow( resultIterator,
					exprManager,
					queryExecutor.getQueryScope( ),
					helper );
			getDataSetRuntime( ).setJSResultSetRow( jsResultSetRow );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getValue(java.lang.String)
		 */
		public Object getValue( IResultObject rsObject, int index, String name )
				throws DataException
		{
			if ( jsResultSetRow == null )
				return rsObject.getFieldValue( index );

			return jsResultSetRow.getValue( rsObject, index, name );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#isRowID(java.lang.String)
		 */
		public boolean isRowID( int index, String name )
		{
			IBaseExpression baseExpr = exprManager.getExpr(name);
			if (baseExpr instanceof IScriptExpression) 
			{
				String exprText = ((IScriptExpression) baseExpr).getText();
				if (exprText == null)
					return false;
				else if (exprText.trim().equalsIgnoreCase("dataSetRow[0]")
						|| exprText.trim().equalsIgnoreCase(
								"dataSetRow._rowPosition"))

					return true;
				else
					return false;
			}
			return false;
		
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getBaseExpr(java.lang.String)
		 */
		public IBaseExpression getBaseExpr( String name )
		{
			if ( name == null )
				return null;
			return ServiceForQueryResults.this.exprManager.getExpr( name );
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getExecutorHelper()
		 */
		public IExecutorHelper getExecutorHelper( )
		{
			return this.helper;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#setExecutorHelper(org.eclipse.birt.data.engine.impl.IExecutorHelper)
		 */
		public void setExecutorHelper( IExecutorHelper helper )
		{
			this.helper = helper;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getColumnMappings()
		 */
		public Map getColumnMappings( )
		{
			Map result = new HashMap();
			List groupBindingColumns = exprManager.getBindingExprs( );
			for( int i = 0; i < groupBindingColumns.size( ); i++ )
			{
				GroupBindingColumn gbc = (GroupBindingColumn)groupBindingColumns.get( i );
				Iterator it = gbc.getColumnNames( ).iterator( );
				while ( it.hasNext( ) )
				{
					String name = it.next( ).toString( );
					result.put( name, gbc.getExpression( name ) );
				}
			}
			return result;
		}
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#execSubquery(org.eclipse.birt.data.engine.odi.IResultIterator,
	 *      java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execSubquery( IResultIterator iterator,
			String subQueryName, Scriptable subScope ) throws DataException
	{
		return queryService.execSubquery( iterator, subQueryName, subScope );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#close()
	 */
	public void close( )
	{
		if ( queryExecutor != null )
		{
			queryExecutor.close( );
			queryExecutor = null;
		}
	}

	// ----------------related with column binding-----------------

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#validateQueryColumBinding()
	 */
	public void validateQueryColumBinding( ) throws DataException
	{
		if ( getPreparedQuery( ) instanceof PreparedIVQuery )
			return;
		
		this.exprManager.validateColumnBinding( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getBaseExpression(java.lang.String)
	 */
	public IBaseExpression getBindingExpr( String exprName )
	{
		return this.exprManager.getExpr( exprName );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getAutoBindingExpr(java.lang.String)
	 */
	public IScriptExpression getAutoBindingExpr( String exprName )
	{
		return this.exprManager.getAutoBindingExpr( exprName );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getAllBindingExprs()
	 */
	public List getAllBindingExprs( )
	{
		return this.exprManager.getBindingExprs( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getAllAutoBindingExprs()
	 */
	public Map getAllAutoBindingExprs( )
	{
		return this.exprManager.getAutoBindingExprMap( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#initAutoBinding()
	 */
	public void initAutoBinding( ) throws DataException
	{
		if ( needAutoBinding( ) == false )
			return;

		Context cx = Context.enter( );

		IResultMetaData metaData = getResultMetaData( );
		int columnCount = metaData.getColumnCount( );
		for ( int i = 0; i < columnCount; i++ )
		{
			int colIndex = i + 1;
			try
			{
				String colName = metaData.getColumnAlias( colIndex );
				if ( colName == null )
					colName = metaData.getColumnName( colIndex );

				ScriptExpression baseExpr = new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( colName ),
						metaData.getColumnType( colIndex ) );
				CompiledExpression compiledExpr = ExpressionCompilerUtil.compile( baseExpr.getText( ),
						cx );
				baseExpr.setHandle( compiledExpr );
				this.exprManager.addAutoBindingExpr( colName, baseExpr );
			}
			catch ( BirtException e )
			{
				// impossible, ignore
			}
		}

		Context.exit( );
	}
	
	/**
	 * @return
	 */
	private boolean needAutoBinding( )
	{
		if ( this.queryDefn instanceof IQueryDefinition )
			return ( (IQueryDefinition) queryDefn ).needAutoBinding( );

		return false;
	}
	
}