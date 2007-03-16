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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.mozilla.javascript.Scriptable;

public abstract class AbstractDataEngine implements IDataEngine
{
	
	protected DataRequestSession dteSession;

	protected ExecutionContext context;

	protected HashMap queryIDMap = new HashMap( );

	//protected LinkedList rsets = new LinkedList( );

	protected String reportArchName = null;
	
	private ModelDteApiAdapter adapter = null;

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( IDataEngine.class
			.getName( ) );
	
	protected final static String VERSION_1 = "__version__1";

	public AbstractDataEngine( ExecutionContext context )
	{
		this.context = context;
		this.adapter = new ModelDteApiAdapter( context,
				context.getSharedScope( ) );
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
	
	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#defineDataSet(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public void defineDataSet( DataSetHandle dataSet )
	{
		try
		{
			adapter.defineDataSet( dataSet, dteSession );
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
	}
	
	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#prepare(org.eclipse.birt.report.engine.ir.Report, java.util.Map)
	 */
	public void prepare( Report report, Map appContext )
	{
		ReportDesignHandle rptHandle = report.getReportDesign( );

		// Handling data sets
		List dataSetList = rptHandle.getAllDataSets( );
		for ( int i = 0; i < dataSetList.size( ); i++ )
		{
			DataSetHandle dataset = (DataSetHandle) dataSetList.get( i );
			try
			{
				adapter.defineDataSet( dataset, dteSession );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ), be );
				context.addException( dataset, be );
			}
		}

		// build report queries
		new ReportQueryBuilder( report, context ).build( );

		doPrepareQuery( report, appContext );
	}

	/**
	 * 
	 * @param report
	 * @param appContext
	 */
	abstract protected void doPrepareQuery( Report report, Map appContext );

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	public IBaseResultSet execute( IDataQueryDefinition query )
	{
		return execute( null, query );
	}

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.report.engine.data.IResultSet, org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	public IBaseResultSet execute( IBaseResultSet parent, IDataQueryDefinition query )
	{
		if ( query instanceof ISubqueryDefinition )
		{
			if ( parent == null )
			{
				return null;
			}
			else if ( parent instanceof ICubeResultSet )
			{
				context.addException( new EngineException(
						"Incorrect parent resultSet for subQuery:"
								+ ( (ISubqueryDefinition) query ).getName( ) ) );
			}
			return doExecuteSubQuery( (QueryResultSet) parent, query );
		}
		else if ( query instanceof IQueryDefinition || query instanceof ICubeQueryDefinition )
		{
			return doExecuteQuery( parent, query );
		}
		
		return null;
	}
	
	abstract protected IBaseResultSet doExecuteQuery( IBaseResultSet parent, IDataQueryDefinition query );

	/**
	 * get the sub query result from the current query.
	 * 
	 * @param query
	 * @return
	 */
	protected IBaseResultSet doExecuteSubQuery( QueryResultSet parent, IDataQueryDefinition query )
	{
		// Extension Item may used to create the query stack, so we must do
		// error handling.
		assert query instanceof ISubqueryDefinition;

		QueryResultSet resultSet;
		try
		{
			ISubqueryDefinition subQuery = (ISubqueryDefinition) query;
			String subQueryName = subQuery.getName( );
			IResultIterator parentRI = parent.getResultIterator( );
			IResultIterator ri = parentRI.getSecondaryIterator( subQueryName,
					context.getSharedScope( ) );
			assert ri != null;
			resultSet = new QueryResultSet( parent, subQuery, ri  );
			return resultSet;
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			context.addException( e );
			return null;
		}
	}

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#close(org.eclipse.birt.report.engine.data.IResultSet)
	 */
	public void close( IBaseResultSet rs )
	{
	}

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#shutdown()
	 */
	public void shutdown( )
	{
		dteSession.shutdown( );
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

	public DataRequestSession getDTESession( )
	{
		return dteSession;
	}
	
	/**
	 * get the tempDir which be set in EngineConfig.
	 */
	protected String getTempDir( ExecutionContext context )
	{
		IReportEngine engine = context.getEngine( );
		if ( engine != null )
		{
			EngineConfig config = engine.getConfig( );
			if ( config != null )
			{
				return config.getTempDir( );				
			}
		}
		return null;
	}
	
	public ArrayList loadDteMetaInfo( DataInputStream dis )
	{
		ArrayList result = new ArrayList( );
		
		try
		{
			String version = IOUtil.readString( dis );
			boolean isFirst = true;
			StringBuffer buffer = new StringBuffer( );
			while ( true )
			{
				String pRsetId;
				String rowId;
				
				if ( isFirst && !VERSION_1.equals( version )  )
				{
					pRsetId = version;
				}
				else
				{
					pRsetId = IOUtil.readString( dis );
				}
				if ( VERSION_1.equals( version ) )
				{
					rowId = IOUtil.readString( dis );
				}
				else
				{
					rowId = String.valueOf( IOUtil.readLong( dis ) );
				}
				
				String queryId = IOUtil.readString( dis );
				String rsetId = IOUtil.readString( dis );				
				
				buffer.setLength( 0 );
				buffer.append( pRsetId );
				buffer.append( "." );
				buffer.append( rowId );
				buffer.append( "." );	
				buffer.append( queryId );				
				
				result.add( new String[]{ pRsetId, rowId, queryId, rsetId } );

				isFirst = false;
			}
		}
		catch ( EOFException eofe )
		{
			// we expect that there should be an EOFexception
		}
		catch ( IOException ioe )
		{
			context.addException( new EngineException(
					"Can't load the data in report document", ioe ) );
			logger.log( Level.SEVERE, ioe.getMessage( ), ioe );
		}
		return result;
	}

}