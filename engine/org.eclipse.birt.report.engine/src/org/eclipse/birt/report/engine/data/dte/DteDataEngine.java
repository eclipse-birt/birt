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

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DteException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IJSExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IReportQueryDefn;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefn;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.mozilla.javascript.Scriptable;


/**
 * implments IDataEngine interface, using birt's data transformation engine (DtE)
 * 
 * @version $Revision: #3 $ $Date: 2005/02/01 $
 */
public class DteDataEngine implements IDataEngine
{

	/** Used for registering the test expression */
	private static String TEST_EXPRESSION = "test_expression";
	/** Used for registering the first value expression of the rule */
	private static String VALUE1 = "value1";
	/** Used for registering the second value expression of the rule */
	private static String VALUE2 = "value2";
	/**
	 * The first element is uni-operator and the second needs to be added
	 * following the test expression
	 */

	private final static int[] uniOperator = new int[]{
			IConditionalExpression.OP_NULL, IConditionalExpression.OP_NOT_NULL,
			IConditionalExpression.OP_TRUE, IConditionalExpression.OP_FALSE};
	private final static String[] uniOperatorStr = new String[]{"== null",
			"!= null", "==true", "==false"};

	/**
	 * The first element is bi-operator and the second is placed between the two
	 * test expressions
	 */

	private final static int[] biOperator = new int[]{
			IConditionalExpression.OP_LT, IConditionalExpression.OP_LE,
			IConditionalExpression.OP_EQ, IConditionalExpression.OP_NE,
			IConditionalExpression.OP_GE, IConditionalExpression.OP_GT};
	private static String[] biOperatorStr = new String[]{"<", "<=", "==", "!=",
			">=", ">"};

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
	protected static Log logger = LogFactory.getLog( DteDataEngine.class );
	
	/**
	 * resultset  stack
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
	 * @param context
	 */
	public DteDataEngine( ExecutionContext context )
	{
		this.context = context;
		
		// TODO: Use Birt.Core config file support
		String odaDriver = System.getProperty( "odadriver" );
		
		File file;
		if ( odaDriver != null )
		{
			file = new File( odaDriver );
			if ( !file.exists( ) || !file.isDirectory( ) )
			{
				file = new File( "." );
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( "the direcotory:" + odaDriver
							+ " is not exist! use current path as it." );
				}
			}
		}
		else
		{
			file = new File( "." );
		}
		engine = DataEngine.newDataEngine( context.getScope( ), file );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#prepare(org.eclipse.birt.report.engine.ir.Report)
	 */
	public void prepare( Report report )
	{
		assert ( report != null );
		
		// Handle data sources
		ReportDesignHandle handle = report.getReportDesign( ).handle( );
		SlotHandle dataSourceSlot = handle.getDataSources( );
		for ( int i = 0; i < dataSourceSlot.getCount( ); i++ )
		{

			try
			{
				engine.defineDataSource( ModelDteApiAdapter.getInstance( )
						.createDataSourceDesign(
								(DataSourceHandle) dataSourceSlot.get( i ) ) );
			}
			catch ( EngineException e )
			{
				logger.error( e.getMessage( ), e );
			}
			catch ( DteException e )
			{
				logger.error( e.getMessage( ), e );
			}
		}	// End of data source handling

		// Handle data sets
		SlotHandle dataSetSlot = handle.getDataSets( );
		for ( int i = 0; i < dataSetSlot.getCount( ); i++ )
		{
			try
			{
				engine.defineDataSet( ModelDteApiAdapter.getInstance( )
						.createDataSetDesign(
								(DataSetHandle) dataSetSlot.get( i ) ) );
			}
			catch ( EngineException e )
			{
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( e.getMessage( ), e );
				}
			}
			catch ( DteException e )
			{
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( e.getMessage( ), e );
				}
			}
		} // End of data set handling

		// build report queries
		new ReportQueryBuilder( ).build( report, context );

		// prepare report queries
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IReportQueryDefn query = (IReportQueryDefn) report.getQueries( )
					.get( i );
			try
			{
				IPreparedQuery preparedQuery = engine.prepare( query );
				queryMap.put( query, preparedQuery );

			}
			catch ( DteException e )
			{
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( e.getMessage( ), e );
				}
			}
		}	// end of prepare 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.model.elements.ReportItemDesign)
	 */
	public IResultSet execute( ReportItemDesign item )
	{
		IBaseQueryDefn query = item.getQuery( );
		if ( query == null )
			return null;
		
		if ( query instanceof IReportQueryDefn )
		{
			Scriptable scope = context.getScope( );
			IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
			assert ( pQuery != null );
			if ( pQuery != null )
			{
				try
				{
					IQueryResults qr = getParentQR();
					if(qr == null)
					{
						qr = pQuery.execute( scope );
					}
					else
					{
						qr = pQuery.execute(qr, scope );
					}
					IResultIterator ri = qr.getResultIterator( );
					assert ri != null;
					DteResultSet dRS = new DteResultSet( qr, this );
					rsStack.addLast( dRS );
					return dRS;
				}
				catch ( DteException e )
				{
					if ( logger.isErrorEnabled( ) )
					{
						logger.error( e.getMessage( ), e );
					}
					return null;
				}
			}
		}
		else if ( query instanceof ISubqueryDefn )
		{
			assert ( rsStack.getLast() instanceof DteResultSet );

			try
			{
				IResultIterator ri = ( (DteResultSet) rsStack.getLast() ).getRs()
						.getSecondaryIterator( ( (ISubqueryDefn) query )
								.getName( ), context.getScope( ) );
				assert ri != null;
				DteResultSet dRS = new DteResultSet( ri, this );
				rsStack.addLast( dRS );
				return dRS;

			}
			catch ( DteException e )
			{
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( e.getMessage( ), e );
				}
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
		rsStack.removeLast();
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
		
		if ( !rsStack.isEmpty() )		// DtE handles evaluation
		{
			try
			{
				Object value = ( (DteResultSet) rsStack.getLast() ).getRs()
						.getValue( expr );
				if ( value != null )
				{
					return context.jsToJava( value );
				}
				else
				{
					return null;
				}
			}
			catch ( DteException e )
			{
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( e.getMessage( ), e );
				}
				return null;
			}
		}
		else	// Rhino handles evaluation
		{
			if ( expr instanceof IJSExpression )
			{
				return context.evaluate( ( (IJSExpression) expr ).getText( ) );
			}
			else if (expr instanceof IConditionalExpression)
			{
				return evaluateCondExpr((IConditionalExpression)expr);
			}
			
			//unsupported expression type
			assert(false);
		}
		return null;
	}
	
	protected IQueryResults getParentQR()
	{
		for(int i=rsStack.size()-1; i>=0; i--)
		{
			DteResultSet rs = (DteResultSet)rsStack.get(i);
			if(rs.getQr()!=null)
			{
				return rs.getQr();
			}
		}
		return null;
	}

	/**
	 * evaluate conditional expression. A conditional expression can have
	 * an operator, one LHS expression, and up to two expressions on RHS, i.e.,
	 * 
	 * testExpr operator operand1 operand2		or
	 * testExpr between 1 20
	 *  
	 * Now only support comparison between the same data type
	 * @param expr the conditional expression to be evaluated
	 * @return a boolean value (as an Object)
	 */
	protected Object evaluateCondExpr( IConditionalExpression expr )
	{
		if ( expr == null )
			return new Boolean(false);
		
		int operator = expr.getOperator( );
		IJSExpression testExpr = expr.getExpression( );
		IJSExpression v1 = expr.getOperand1( );
		IJSExpression v2 = expr.getOperand2( );

		if ( testExpr == null )
			return new Boolean(false);
		
		context.newScope( );
		Object testExprValue = context.evaluate( testExpr.getText( ) );
		if ( IConditionalExpression.OP_NONE == operator )
		{
			context.exitScope();
			return  testExprValue;
		}

		context.registerBean( TEST_EXPRESSION, testExprValue );
		for ( int i = 0; i < uniOperatorStr.length; i++ )
		{
			if ( uniOperator[i] == operator )
			{
				Object ret =  context.evaluate( TEST_EXPRESSION
						+ uniOperatorStr[i]  );
				context.exitScope();
				return ret;
			}
		}

		if ( v1 == null )
		{
			context.exitScope();
			return new Boolean(false);
		}
		Object vv1 = context.evaluate( v1.getText( ) );
		context.registerBean( VALUE1, vv1 );
		for ( int i = 0; i < biOperatorStr.length; i++ )
		{
			if ( biOperator[i] == operator )
			{
				Object ret =   context.evaluate( TEST_EXPRESSION
						+ biOperatorStr[i] + VALUE1  );
				context.exitScope();
				return ret;
			}
		}
		if ( IConditionalExpression.OP_LIKE == operator )
		{
			Object ret =  context.evaluate( "(" + TEST_EXPRESSION
					+ ").match(" + VALUE1 + ")!=null" );
			context.exitScope();
			return ret;
		}

		if(v2==null)
		{
			context.exitScope();
			return new Boolean(false);
		}
		Object vv2 = context.evaluate(v2.getText());
		context.registerBean( VALUE2, vv2);

		if ( IConditionalExpression.OP_BETWEEN == operator )
		{
			Object ret =  context.evaluate("(" + TEST_EXPRESSION + " >= " + VALUE1 + ") && ("
					+ TEST_EXPRESSION + " <= " + VALUE2 + ")");
			context.exitScope();
			return ret;
		}
		else if ( IConditionalExpression.OP_NOT_BETWEEN == operator )
		{
			Object ret = context.evaluate("(" + TEST_EXPRESSION + " < " + VALUE1 + ") || ("
					+ TEST_EXPRESSION + " > " + VALUE2 + ")");
			context.exitScope();
			return ret;
		}
		if ( logger.isErrorEnabled( ) )
		{
			logger.error( "Invalid operator: " + operator );
		}
		context.exitScope();
		return new Boolean(false);
	}


}