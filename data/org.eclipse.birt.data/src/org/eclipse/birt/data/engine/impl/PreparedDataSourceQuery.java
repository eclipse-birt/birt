/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * Base class for a top-level prepared query that has its own data source (either extended data source, or 
 * scripted data source)  
 */
abstract class PreparedDataSourceQuery extends PreparedQuery implements IPreparedQuery
{
	protected IBaseDataSetDesign dataSetDesign;
	
	/**
	 * Creates a new instance of the proper subclass based on the type of the
	 * query passed in.
	 * @param dataEngine
	 * @param queryDefn
	 * @return PreparedReportQuery
	 * @throws DataException
	 */
	static PreparedDataSourceQuery newInstance( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		assert dataEngine != null;
		assert queryDefn != null;
		
		IBaseDataSetDesign dset = dataEngine.getDataSetDesign( queryDefn.getDataSetName( ) );
		if ( dset == null )
			throw new DataException( ResourceConstants.UNDEFINED_DATA_SET,
					queryDefn.getDataSetName() );
			
		if ( dset instanceof IScriptDataSetDesign )
		{
			return new PreparedScriptDSQuery( dataEngine, queryDefn, dset );
		}
		if ( dset instanceof IOdaDataSetDesign )
		{
			return new PreparedExtendedDSQuery( dataEngine, queryDefn, dset );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASOURCE_TYPE );
		}
	}
	
	protected PreparedDataSourceQuery( DataEngineImpl dataEngine, IQueryDefinition queryDefn, 
			IBaseDataSetDesign dataSetDesign)
		throws DataException
	{
		super(dataEngine, queryDefn);
		this.dataSetDesign = dataSetDesign;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#getReportQuery()
	 */
	protected PreparedDataSourceQuery getDataSourceQuery()
	{
		return this;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
	 */
	public IQueryDefinition getReportQueryDefn()
	{
	    assert getQueryDefn() instanceof IQueryDefinition;
		return (IQueryDefinition) getQueryDefn();
	}
	
	/**
	 * Executes the prepared execution plan.  This returns
	 * a QueryResult object at a state ready to return its 
	 * current result iterator, or evaluate an aggregate expression.
	 * <p>
	 * This includes setup runtime state, and evaluation of
	 * any beforeOpen and afterOpen scripts on a data set. 
	 * @return The QueryResults object opened and ready to return
	 * 		the results of a report query. 
	 */
	public IQueryResults execute( Scriptable scope ) throws DataException
	{ 
    	return doExecute( null, scope );
	}

	/**
	 * Executes the prepared execution plan as an inner query 
	 * that appears within the scope of another report query. 
	 * The outer query must have been prepared and executed, and 
	 * its results given as a parameter to this method.
	 * @param outerResults	QueryResults for the executed outer query
	 * @return The QueryResults object opened and ready to return
	 * 		the results of a report query. 
	 */
	public IQueryResults execute( IQueryResults outerResults, Scriptable scope )
		throws DataException
	{ 
		return doExecute( outerResults, scope );
	}
	
	abstract class DSQueryExecutor extends PreparedQuery.Executor
	{
		protected DataSourceRuntime findDataSource( ) throws DataException
		{
			assert dataSetDesign != null;
			DataSourceRuntime dsRT = getDataEngine().getDataSourceRuntime( 
					dataSetDesign.getDataSourceName() );
			return dsRT;
		}
		
		protected DataSetRuntime newDataSetRuntime( ) throws DataException
		{
			return DataSetRuntime.newInstance( dataSetDesign, this );
		}
	}
}
