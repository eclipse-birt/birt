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
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 * @version $Revision: 1.26 $ $Date: 2005/11/15 09:38:07 $
 */
public class DteDataEngine implements IDataEngine
{

	/**
	 * execution context
	 */
	protected ExecutionContext context;

	/**
	 * data engine
	 */
	protected DataEngine engine;

	/**
	 * Hashmap which map <code>ReportQuery</code> to
	 * <code>PreparedQuery</code>
	 */
	protected HashMap queryMap = new HashMap( );

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( DteDataEngine.class
			.getName( ) );

	/**
	 * resultset stack
	 */
	protected LinkedList rsStack = new LinkedList( );

	/**
	 * creates data engine, by first look into the directory specified by
	 * configuration variable odadriver, for oda configuration file. The oda
	 * configuration file is at $odadriver/drivers/driverType/odaconfig.xml.
	 * <p>
	 * 
	 * If the config variable is not set, search configuration file at
	 * ./drivers/driverType/odaconfig.xml.
	 * 
	 * @param context
	 */
	public DteDataEngine( ExecutionContext context )
	{
		this.context = context;

		try
		{
			DataEngineContext dteContext = DataEngineContext.newInstance(
					DataEngineContext.MODE_DIRECTPRESENT, context
							.getSharedScope( ), null );

			engine = DataEngine.newDataEngine( dteContext );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#prepare(org.eclipse.birt.report.engine.ir.Report)
	 */
	public void prepare( Report report )
	{
		prepare( report, null );
	}

	public void prepare( Report report, Map appContext )
	{
		assert ( report != null );

		// Handle data sources
		ReportDesignHandle handle = report.getReportDesign( );
		List dataSourceList = handle.getAllDataSources( );
		for ( int i = 0; i < dataSourceList.size( ); i++ )
		{
			DataSourceHandle dataSource = (DataSourceHandle) dataSourceList
					.get( i );

			try
			{
				engine.defineDataSource( ModelDteApiAdapter.getInstance( )
						.createDataSourceDesign( dataSource ) );
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( dataSource, e );
			}
		} // End of data source handling

		// Handle data sets
		List dataSetList = handle.getAllDataSets( );
		for ( int i = 0; i < dataSetList.size( ); i++ )
		{
			DataSetHandle dataset = (DataSetHandle) dataSetList.get( i );
			try
			{
				engine.defineDataSet( ModelDteApiAdapter.getInstance( )
						.createDataSetDesign( dataset ) );
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( dataset, e );
			}
		} // End of data set handling

		// build report queries
		new ReportQueryBuilder( ).build( report, context );

		// prepare report queries
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IQueryDefinition query = (IQueryDefinition) report.getQueries( )
					.get( i );
			try
			{
				IPreparedQuery preparedQuery = engine.prepare( query,
						appContext );
				queryMap.put( query, preparedQuery );

			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( e );
			}
		} // end of prepare
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.model.elements.ReportItemDesign)
	 */
	public IResultSet execute( IBaseQueryDefinition query )
	{
		if ( query == null )
			return null;

		if ( query instanceof IQueryDefinition )
		{
			Scriptable scope = context.getSharedScope( );
			IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
			assert ( pQuery != null );
			if ( pQuery != null )
			{
				try
				{
					IQueryResults queryResults = getParentQR( );
					if ( queryResults == null )
					{
						queryResults = pQuery.execute( scope );
					}
					else
					{
						queryResults = pQuery.execute( queryResults, scope );
					}
					IResultIterator ri = queryResults.getResultIterator( );
					assert ri != null;
					DteResultSet dRS = new DteResultSet( queryResults, this,
							context );
					rsStack.addLast( dRS );
					return dRS;
				}
				catch ( BirtException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
					context.addException( e );
					return null;
				}
			}
		}
		else if ( query instanceof ISubqueryDefinition )
		{
			// Extension Item may used to create the query stack, so we must do
			// error handling.
			if ( rsStack.isEmpty( ) )
			{
				return null;
			}
			assert ( rsStack.getLast( ) instanceof DteResultSet );

			try
			{
				IResultIterator ri = ( (DteResultSet) rsStack.getLast( ) )
						.getResultIterator( ).getSecondaryIterator(
								( (ISubqueryDefinition) query ).getName( ),
								context.getSharedScope( ) );
				assert ri != null;
				DteResultSet dRS = new DteResultSet( ri, this );
				rsStack.addLast( dRS );
				return dRS;
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( e );
				return null;
			}
		}
		assert ( false );
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#close()
	 */
	public void close( )
	{
		assert ( rsStack.size( ) > 0 );
		rsStack.removeLast( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#shutdown()
	 */
	public void shutdown( )
	{
		assert ( rsStack.size( ) == 0 );
		engine.shutdown( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#evaluate(org.eclipse.birt.data.engine.api.IExpression)
	 */
	public Object evaluate( IBaseExpression expr )
	{
		if ( expr == null )
		{
			return null;
		}

		if ( !rsStack.isEmpty( ) ) // DtE handles evaluation
		{
			try
			{
				Object value = ( (DteResultSet) rsStack.getLast( ) )
						.getResultIterator( ).getValue( expr );
				if ( value != null )
				{
					return context.jsToJava( value );
				}
				return null;
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( e );
				return null;
			}
			catch ( JavaScriptException ee )
			{
				logger.log( Level.SEVERE, ee.getMessage( ), ee );
				context.addException( new EngineException(
						MessageConstants.INVALID_EXPRESSION_ERROR, expr, ee ) );
				return null;
			}
		}

		// Rhino handles evaluation
		if ( expr instanceof IScriptExpression )
		{
			return context.evaluate( ( (IScriptExpression) expr ).getText( ) );
		}
		if ( expr instanceof IConditionalExpression )
		{
			return evaluateCondExpr( (IConditionalExpression) expr );
		}

		// unsupported expression type
		assert ( false );
		return null;
	}

	protected IQueryResults getParentQR( )
	{
		for ( int i = rsStack.size( ) - 1; i >= 0; i-- )
		{
			DteResultSet rs = (DteResultSet) rsStack.get( i );
			if ( rs.getQueryResults( ) != null )
			{
				return rs.getQueryResults( );
			}
		}
		return null;
	}

	/**
	 * evaluate conditional expression. A conditional expression can have an
	 * operator, one LHS expression, and up to two expressions on RHS, i.e.,
	 * 
	 * testExpr operator operand1 operand2 or testExpr between 1 20
	 * 
	 * Now only support comparison between the same data type
	 * 
	 * @param expr
	 *            the conditional expression to be evaluated
	 * @return a boolean value (as an Object)
	 */
	protected Object evaluateCondExpr( IConditionalExpression expr )
	{
		if ( expr == null )
		{
			EngineException e = new EngineException( "Failed to evaluate: null" );//$NON-NLS-1$
			context.addException( e );
			logger.log( Level.SEVERE, e.getMessage( ), e );
			return new Boolean( false );
		}

		int operator = expr.getOperator( );
		IScriptExpression testExpr = expr.getExpression( );
		IScriptExpression v1 = expr.getOperand1( );
		IScriptExpression v2 = expr.getOperand2( );

		if ( testExpr == null )
			return new Boolean( false );

		Object testExprValue = context.evaluate( testExpr.getText( ) );
		if ( IConditionalExpression.OP_NONE == operator )
		{
			return testExprValue;
		}
		Object vv1 = null;
		Object vv2 = null;
		if ( v1 != null )
		{
			vv1 = context.evaluate( v1.getText( ) );
		}
		if ( v2 != null )
		{
			vv2 = context.evaluate( v2.getText( ) );
		}

		try
		{
			return ScriptEvalUtil.evalConditionalExpr2( testExprValue, expr
					.getOperator( ), vv1, vv2 );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			context.addException( new EngineException(
					MessageConstants.INVALID_EXPRESSION_ERROR, expr, e ) );
			return new Boolean( false );
		}
	}

	public DataEngine getDataEngine( )
	{
		return engine;
	}

}