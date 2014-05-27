/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * This class wraps functions of Data Engine to provide executing query for
 * chart live preview.
 * 
 * @since 2.5.2
 */

public class DteAdapter
{
	private ExecutionContext executionContext;
	
	/**
	 * Set related data set on specified session.
	 * 
	 * @param handle the handle which contains related data set.
	 * @param session data request session handle
	 * @param keepDataSetFilter 
	 * @param disAllowAggregation
	 * @throws AdapterException
	 * @throws BirtException
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public void defineDataSet( DataSetHandle handle,
			DataRequestSession session, boolean keepDataSetFilter,
			boolean disAllowAggregation ) throws AdapterException,
			BirtException
	{

		if ( handle == null )
		{
			return;
			// throw new AdapterException(
			// ResourceConstants.DATASETHANDLE_NULL_ERROR );
		}

		DataSourceHandle dataSourceHandle = handle.getDataSource( );
		if ( dataSourceHandle != null )
		{
			IBaseDataSourceDesign dsourceDesign = session.getModelAdaptor( )
					.adaptDataSource( dataSourceHandle );
			session.defineDataSource( dsourceDesign );
		}
		if ( handle instanceof JointDataSetHandle )
		{
			Iterator iter = ( (JointDataSetHandle) handle ).dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle dsHandle = (DataSetHandle) iter.next( );
				if ( dsHandle != null )
				{
					defineDataSet( dsHandle, session, true, false );
				}
			}

		}
		if ( handle instanceof DerivedDataSetHandle )
		{
			List inputDataSet = ( (DerivedDataSetHandle) handle ).getInputDataSets( );
			for ( int i = 0; i < inputDataSet.size( ); i++ )
			{
				defineDataSet( (DataSetHandle) inputDataSet.get( i ),
						session,
						keepDataSetFilter,
						disAllowAggregation );
			}
		}

		BaseDataSetDesign baseDS = session.getModelAdaptor( )
				.adaptDataSet( handle );
		
		if (baseDS == null )
		{
			return;
		}
		
		if ( !keepDataSetFilter )
		{
			if ( baseDS.getFilters( ) != null )
				baseDS.getFilters( ).clear( );
		}

		if ( disAllowAggregation )
		{
			List computedColumns = baseDS.getComputedColumns( );
			if ( computedColumns != null && computedColumns.size( ) != 0 )
			{
				for ( int i = 0; i < computedColumns.size( ); i++ )
				{
					IComputedColumn computedColumn = (IComputedColumn) computedColumns.get( i );
					if ( computedColumn.getAggregateFunction( ) != null )
					{
						computedColumns.set( i,
								new org.eclipse.birt.data.engine.api.querydefn.ComputedColumn( computedColumn.getName( ),
										"null" ) ); //$NON-NLS-1$
					}
				}
			}
		}

		if ( executionContext == null )
		{
			new ModelDteApiAdapter( ).defineDataSet( handle, session );
		}
		else
		{
			new ModelDteApiAdapter( executionContext ).defineDataSet( handle,
					session );
		}
	}
	
	/**
	 * Set row limit on session.
	 * 
	 * @param session data request session handle
	 * @param rowLimit the rows which will be retrieved.
	 * @param isCube specified if current is cube case.
	 */
	@SuppressWarnings("unchecked")
	public void setRowLimit( DataRequestSession session, int rowLimit, boolean isCube )
	{
		
		Map<String, Integer> appContext = session.getDataSessionContext( ).getAppContext( );
		if ( appContext == null )
		{
			appContext = new HashMap<String, Integer>( );
		}
		
		if ( !isCube )
		{
			appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT,
					Integer.valueOf( rowLimit ) );
		}
		else
		{
			appContext.put( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE,
					Integer.valueOf( rowLimit ) );
			appContext.put( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE,
					Integer.valueOf( rowLimit ) );
		}
		session.getDataSessionContext( ).setAppContext( appContext );
	}
	
	/**
	 * Remove row limit from app context of session.
	 * 
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	public void unsetRowLimit( DataRequestSession session )
	{
		Map<String, Integer> appContext = session.getDataSessionContext( )
				.getAppContext( );
		if ( appContext == null )
		{
			return;
		}

		appContext.remove( DataEngine.DATA_SET_CACHE_ROW_LIMIT );
		appContext.remove( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE );
		appContext.remove( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE );
	}
	
	/**
	 * Uses session to execute a query.
	 * 
	 * @param session
	 * @param queryDefn
	 * @return query result.
	 * @throws BirtException
	 */
	public IQueryResults executeQuery( DataRequestSession session,
			IQueryDefinition queryDefn ) throws BirtException
	{
		IPreparedQuery pq = session.prepare( queryDefn );
		return (IQueryResults) session.execute( pq,
				null,
				session.getDataSessionContext( )
						.getDataEngineContext( )
						.getScriptContext( ) );
	}

	/**
	 * Uses session to execute a cube query.
	 * 
	 * @param session
	 * @param queryDefn
	 * @return cube query results.
	 * @throws BirtException
	 */
	public ICubeQueryResults executeQuery(DataRequestSession session, ICubeQueryDefinition queryDefn ) throws BirtException
	{
		IPreparedCubeQuery pq = session.prepare( queryDefn );
		return (ICubeQueryResults) session.execute( pq, null, new ScriptContext( ) );
	}
	
	/**
	 * Populates data context into session.
	 *  
	 * @param handle
	 * @param session
	 * @throws BirtException
	 */
	public void populateApplicationContext( DataSetHandle handle,
			DataRequestSession session ) throws BirtException
	{
		// Not implemented here, just used for override.
	}
	
	public void setExecutionContext(ExecutionContext context )
	{
		this.executionContext = context;
	}
	
	/**
	 * Populates data context into session.
	 * 
	 * @param handle
	 * @param session
	 * @throws BirtException
	 */
	public void populateApplicationContext( CubeHandle handle,
			DataRequestSession session ) throws BirtException
	{
		// Not implemented here, just used for override.
	}
	
	/**
	 * Registers session.
	 * 
	 * @param handle
	 * @param session
	 * @throws BirtException
	 */
	public void registerSession( ReportElementHandle handle,
			DataRequestSession session ) throws BirtException
	{
		if ( handle instanceof DataSetHandle )
		{
			DataService.getInstance( ).registerSession( (DataSetHandle) handle,
					session );
		}
		else if ( handle instanceof CubeHandle )
		{
			DataService.getInstance( ).registerSession( (CubeHandle) handle,
					session );
		}
	}
	
	/**
	 * Unregister session.
	 * 
	 * @param session
	 * @throws BirtException
	 */
	public void unregisterSession( DataRequestSession session ) throws BirtException
	{
		if ( session != null )
		{
			DataService.getInstance( ).unRegisterSession( session );
		}
	}
}
