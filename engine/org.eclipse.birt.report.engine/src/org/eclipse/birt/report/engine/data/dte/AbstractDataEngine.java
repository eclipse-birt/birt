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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public abstract class AbstractDataEngine implements IDataEngine
{

	protected DataEngine dteEngine;

	protected ExecutionContext context;

	protected HashMap queryIDMap = new HashMap( );

	protected LinkedList rsets = new LinkedList( );

	protected String reportArchName = null;

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( IDataEngine.class
			.getName( ) );

	/**
	 * prepare the queries defined in the report.
	 */
	public void prepare( Report report, Map appContext )
	{
		ReportDesignHandle rptHandle = report.getReportDesign( );

		ModelDteApiAdapter adaptor = new ModelDteApiAdapter( context, context
				.getSharedScope( ) );

		// Handling data sources
		List dataSourceList = rptHandle.getAllDataSources( );
		for ( int i = 0; i < dataSourceList.size( ); i++ )
		{
			DataSourceHandle dataSource = (DataSourceHandle) dataSourceList
					.get( i );
			try
			{
				dteEngine.defineDataSource( adaptor
						.createDataSourceDesign( dataSource ) );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ), be );
				context.addException( dataSource, be );
			}
		}

		// Handling data sets
		List dataSetList = rptHandle.getAllDataSets( );
		for ( int i = 0; i < dataSetList.size( ); i++ )
		{
			DataSetHandle dataset = (DataSetHandle) dataSetList.get( i );
			try
			{
				dteEngine
						.defineDataSet( adaptor.createDataSetDesign( dataset ) );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ), be );
				context.addException( dataset, be );
			}
		}

		// build report queries
		new ReportQueryBuilder( ).build( report, context );

		// 
		doPrepareQuery( report, appContext );
	}

	/**
	 * 
	 * @param report
	 * @param appContext
	 */
	abstract protected void doPrepareQuery( Report report, Map appContext );

	/**
	 * 
	 */
	public IResultSet execute( IBaseQueryDefinition query )
	{
		if ( query instanceof IQueryDefinition )
		{
			return doExecuteQuery( query );
		}
		else if ( query instanceof ISubqueryDefinition )
		{
			return doExecuteSubQuery( query );
		}
		return null;
	}

	abstract protected IResultSet doExecuteQuery( IBaseQueryDefinition query );

	/**
	 * get the sub query result from the current query.
	 * 
	 * @param query
	 * @return
	 */
	protected IResultSet doExecuteSubQuery( IBaseQueryDefinition query )
	{
		// Extension Item may used to create the query stack, so we must do
		// error handling.
		assert query instanceof ISubqueryDefinition;
		if ( rsets.isEmpty( ) )
			return null;

		DteResultSet resultSet;
		try
		{
			DteResultSet parent = (DteResultSet) rsets.getFirst( );
			ISubqueryDefinition subQuery = (ISubqueryDefinition) query;
			String subQueryName = subQuery.getName( );
			IResultIterator parentRI = parent.getResultIterator( );
			IResultIterator ri = parentRI.getSecondaryIterator( subQueryName,
					context.getSharedScope( ) );
			assert ri != null;
			resultSet = new DteResultSet( parent, subQueryName, ri, this,
					context );
			rsets.addFirst( resultSet );
			return resultSet;
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			context.addException( e );
			return null;
		}
	}

	public void close( IResultSet rs )
	{
		rsets.remove( rs );
	}

	public void shutdown( )
	{
		rsets.clear( );
		dteEngine.shutdown( );
	}

	/**
	 * @deprecated need to be deleted by LiangYu
	 * @return
	 */
	public Object evaluate( IBaseExpression expr )
	{
		if ( expr == null )
		{
			return null;
		}

		// Rhino handles evaluation
		if ( expr instanceof IScriptExpression )
		{
			return context.evaluate( ( (IScriptExpression) expr ).getText( ) );
		}
		if ( expr instanceof IConditionalExpression )
		{
			return context.evaluateCondExpr( (IConditionalExpression) expr );
		}

		// unsupported expression type
		assert ( false );
		return null;
	}

	public Object evaluate( String expr )
	{
		return context.evaluate( expr );
	}

	/**
	 * @deprecated need to be deleted by LiangYu
	 * @return
	 */
	public DataEngine getDataEngine( )
	{
		return dteEngine;
	}

	public DataEngine getDTEEngine( )
	{
		return dteEngine;
	}

	public IResultSet getResultSet( )
	{
		if ( !rsets.isEmpty( ) )
		{
			return (IResultSet) rsets.getFirst( );
		}
		return null;
	}

}
