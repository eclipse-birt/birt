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
import java.util.Iterator;
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
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.mozilla.javascript.Scriptable;

public abstract class AbstractDataEngine implements IDataEngine
{

	protected DataEngine dteEngine;

	protected ExecutionContext context;

	protected HashMap queryIDMap = new HashMap( );

	//protected LinkedList rsets = new LinkedList( );

	protected String reportArchName = null;

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( IDataEngine.class
			.getName( ) );

	public AbstractDataEngine( ExecutionContext context )
	{
		this.context = context;
		try
		{
			Scriptable scope = context.getScope( );
			// register a js row object into the execution context, so
			// we can use row["colName"] to get the column values
			context.registerBean( "row", new NativeRowObject( scope, context ) );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't register row object", ex );
			ex.printStackTrace( );
		}
	}
	
	public void defineDataSet( DataSetHandle dataSet )
	{
		// Define data source and data set
		DataSourceHandle dataSource = dataSet.getDataSource( );
		ModelDteApiAdapter adaptor = new ModelDteApiAdapter( context, context
				.getSharedScope( ) );
		try
		{
			if ( dataSource != null )
			{
				doDefineDataSource( adaptor, dataSource );
			}
			doDefineDataSet( adaptor, dataSet );
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
	}

	protected void doDefineDataSource( ModelDteApiAdapter adaptor,
			DataSourceHandle dataSource ) throws BirtException
	{
		dteEngine
				.defineDataSource( adaptor.createDataSourceDesign( dataSource ) );
	}

	protected void doDefineDataSet( ModelDteApiAdapter adaptor,
			DataSetHandle dataSet ) throws BirtException
	{
		if ( dataSet instanceof JointDataSetHandle )
		{
			JointDataSetHandle jointDataSet = (JointDataSetHandle) dataSet;
			List dataSetNames = jointDataSet.getDataSetNames( );
			ModuleHandle report = dataSet.getModuleHandle( );
			Iterator iter = dataSetNames.iterator( );
			while ( iter.hasNext( ) )
			{
				String dataSetName = (String) iter.next( );
				DataSetHandle childDataSet = report.findDataSet( dataSetName );
				if ( childDataSet != null )
				{
					DataSourceHandle childDataSource = childDataSet
							.getDataSource( );
					if (childDataSource != null)
					{
						doDefineDataSource( adaptor, childDataSource );
					}
					doDefineDataSet( adaptor, childDataSet );
				}
			}

		}
		dteEngine.defineDataSet( adaptor.createDataSetDesign( dataSet ) );
	}
	
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
		return execute( null, query );
	}
	
	/**
	 * 
	 */
	public IResultSet execute( IResultSet parent, IBaseQueryDefinition query )
	{
		if ( query instanceof IQueryDefinition )
		{
			return doExecuteQuery( (DteResultSet) parent, (IQueryDefinition)query );
		}
		else if ( query instanceof ISubqueryDefinition )
		{
			return doExecuteSubQuery( (DteResultSet) parent, query );
		}
		return null;
	}
	

	abstract protected IResultSet doExecuteQuery( DteResultSet parent, IQueryDefinition query );

	/**
	 * get the sub query result from the current query.
	 * 
	 * @param query
	 * @return
	 */
	protected IResultSet doExecuteSubQuery( DteResultSet parent, IBaseQueryDefinition query )
	{
		// Extension Item may used to create the query stack, so we must do
		// error handling.
		assert query instanceof ISubqueryDefinition;

		DteResultSet resultSet;
		try
		{
			ISubqueryDefinition subQuery = (ISubqueryDefinition) query;
			String subQueryName = subQuery.getName( );
			IResultIterator parentRI = parent.getResultIterator( );
			IResultIterator ri = parentRI.getSecondaryIterator( subQueryName,
					context.getSharedScope( ) );
			assert ri != null;
			resultSet = new DteResultSet( parent, subQuery, ri  );
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
	}

	public void shutdown( )
	{
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
}