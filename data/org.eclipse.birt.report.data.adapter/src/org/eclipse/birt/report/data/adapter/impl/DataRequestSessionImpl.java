/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Implementation of DataRequestSession
 */
public class DataRequestSessionImpl extends DataRequestSession
{

	//
	private DataEngine dataEngine;
	private IModelAdapter modelAdaptor;
	private DataSessionContext sessionContext;

	/**
	 * Constructs the data request session with the provided session context
	 * information.
	 * 
	 * @param context
	 * @throws BirtException
	 */
	public DataRequestSessionImpl( DataSessionContext context )
			throws BirtException
	{
		if ( context == null )
			throw new AdapterException( ResourceConstants.CONEXT_NULL_ERROR );

		dataEngine = DataEngine.newDataEngine( context.getDataEngineContext( ) );
		modelAdaptor = new ModelAdapter( context );
		sessionContext = context;

		// Comments out the following code. Now the definition of all data elements
		// will be defered until necessary.
		// If a report design handle provided, adapt all data sets and data
		// sources
		//adaptAllDataElements( );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#defineDataSource(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign)
	 */
	public void defineDataSource( IBaseDataSourceDesign design )
			throws BirtException
	{
		dataEngine.defineDataSource( design );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#defineDataSet(org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public void defineDataSet( IBaseDataSetDesign design ) throws BirtException
	{
		dataEngine.defineDataSet( design );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#getDataSetMetaData(java.lang.String,
	 *      boolean)
	 */
	public IResultMetaData getDataSetMetaData( String dataSetName,
			boolean useCache ) throws BirtException
	{
		return getDataSetMetaData( this.sessionContext.getModuleHandle( )
				.findDataSet( dataSetName ), useCache );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#getDataSetMetaData(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      boolean)
	 */
	public IResultMetaData getDataSetMetaData( DataSetHandle dataSetHandle,
			boolean useCache ) throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext ).getDataSetMetaData( dataSetHandle,
				useCache );
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      java.util.Iterator, java.util.Iterator, java.lang.String)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName ) throws BirtException
	{
		return getColumnValueSet( dataSet,
				inputParamBindings,
				columnBindings,
				boundColumnName,
				null );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, java.lang.String, int, int)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName, IRequestInfo requestInfo )
			throws BirtException
	{
		IQueryResults queryResults = getGroupingQueryResults( dataSet,
				inputParamBindings,
				columnBindings,
				boundColumnName );
		IResultIterator resultIt = queryResults.getResultIterator( );

		int maxRowCount = -1;
		if ( requestInfo != null )
		{
			resultIt.moveTo( requestInfo.getStartRow( ) );
			maxRowCount = requestInfo.getMaxRow( );
		}
		// Iterate through result, getting one column value per group, skipping
		// group detail rows
		ArrayList values = new ArrayList( );

		while ( resultIt.next( ) && maxRowCount != 0 )
		{
			Object value = resultIt.getValue( boundColumnName );
			values.add( value );
			resultIt.skipToEnd( 1 );
			maxRowCount--;
		}
		resultIt.close( );
		queryResults.close( );
		
		return values;
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#refreshMetaData(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public IResultMetaData refreshMetaData( DataSetHandle dataSetHandle )
			throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext ).refreshMetaData( dataSetHandle );
	}
	
	/*
	 * 
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#refreshMetaData(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      boolean)
	 */
	public IResultMetaData refreshMetaData( DataSetHandle dataSetHandle,
			boolean holdEvent ) throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext ).refreshMetaData( dataSetHandle, holdEvent );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#executeQuery(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Iterator, java.util.Iterator, java.util.Iterator)
	 */
	public IQueryResults executeQuery( QueryDefinition queryDefn,
			Iterator paramBindingIt, Iterator filterIt, Iterator bindingIt )
			throws BirtException
	{
		return new QueryExecutionHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext ).executeQuery( queryDefn,
				paramBindingIt,
				filterIt,
				bindingIt );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#getQueryResults(java.lang.String)
	 */
	public IQueryResults getQueryResults( String queryResultID )
			throws BirtException
	{
		return dataEngine.getQueryResults( queryResultID );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#clearCache(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign,
	 *      org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public void clearCache( IBaseDataSourceDesign dataSource,
			IBaseDataSetDesign dataSet ) throws BirtException
	{
		dataEngine.clearCache( dataSource, dataSet );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Map)
	 */
	public IPreparedQuery prepare( IQueryDefinition query, Map appContext )
			throws BirtException
	{
		if ( appContext == null )
			// Use session app context
			appContext = sessionContext.getAppContext();
		return dataEngine.prepare( query, appContext );
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition)
	 */
	public IPreparedQuery prepare(IQueryDefinition query) throws BirtException
	{
		// Use session app context
		return dataEngine.prepare( query, this.sessionContext.getAppContext() );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#closeDataSource(java.lang.String)
	 */
	public void closeDataSource( String dataSourceName ) throws BirtException
	{
		dataEngine.closeDataSource( dataSourceName );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#getModelAdaptor()
	 */
	public IModelAdapter getModelAdaptor( )
	{
		return modelAdaptor;
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#shutdown()
	 */
	public void shutdown( )
	{
		dataEngine.shutdown( );
		dataEngine = null;
	}

	/**
	 * get the distinct value of query
	 * @param dataSet
	 * @param inputParamBindings
	 * @param columnBindings
	 * @param boundColumnName
	 * @return
	 * @throws BirtException
	 */
	private IQueryResults getGroupingQueryResults( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName ) throws BirtException
	{
		assert dataSet != null;
		// TODO: this is the inefficient implementation
		// Need to enhance the implementation to verify that the column is bound
		// to a data set column

		// Run a query with the provided binding information. Group by bound
		// column so we can
		// retrieve distinct values using the grouping feature
		QueryDefinition query = new QueryDefinition( );
		query.setDataSetName( dataSet.getQualifiedName() );
		GroupDefinition group = new GroupDefinition( );
		group.setKeyColumn( boundColumnName );
		query.addGroup( group );
		query.setUsesDetails( false );

		ModuleHandle moduleHandle = sessionContext.getModuleHandle( );
		if ( moduleHandle == null )
			moduleHandle = dataSet.getModuleHandle( );

		QueryExecutionHelper execHelper = new QueryExecutionHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext );
		IQueryResults results = execHelper.executeQuery( query,
				inputParamBindings,
				null,
				columnBindings );
		return results;
	}
	
	/**
	 * Adapt all data sets and data sources defined in report design handle used
	 * to initialize this session
	 */
	/*private void adaptAllDataElements( ) throws BirtException
	{
		ModuleHandle reportDesign = sessionContext.getModuleHandle( );
		if ( reportDesign == null )
			return;

		List list = reportDesign.getAllDataSources( );
		if ( list != null )
		{
			Iterator it = list.iterator( );
			while ( it.hasNext( ) )
			{
				DataSourceHandle dataSource = (DataSourceHandle) it.next( );
				defineDataSource( getModelAdaptor( ).adaptDataSource( dataSource ) );
			}
		}

		list = reportDesign.getAllDataSets( );
		if ( list != null )
		{
			Iterator it = list.iterator( );
			while ( it.hasNext( ) )
			{
				DataSetHandle dataSet = (DataSetHandle) it.next( );
				defineDataSet( getModelAdaptor( ).adaptDataSet( dataSet ) );
			}
		}
	}
*/
}
