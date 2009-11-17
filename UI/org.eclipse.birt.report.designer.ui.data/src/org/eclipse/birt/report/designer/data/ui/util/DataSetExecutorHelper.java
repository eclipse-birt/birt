/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;

/**
 * 
 * Utility class to execute query in data set editor
 *
 */
public final class DataSetExecutorHelper
{

	/**
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSetHandle,
			DataRequestSession session ) throws BirtException
	{
		return execute( dataSetHandle, true, true, -1, null, session );
	}
	
	/**
	 * execute query definition 
	 * @param dataSetHandle
	 * @param useColumnHints
	 * @param rowsToReturn
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSetHandle,
			boolean useColumnHints, boolean useFilters, int rowsToReturn,
			ExecutionContext context, DataRequestSession session )
			throws BirtException
	{

		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor( )
				.adaptDataSet( dataSetHandle );

		if ( !( dataSetHandle instanceof JointDataSetHandle || dataSetHandle instanceof DerivedDataSetHandle ) )
		{
			context.setReportContext( new ReportContextImpl( context ) );
			dataSetDesign = new ModelDteApiAdapter( context ).appendRuntimeInfoToDataSet( dataSetHandle,
					(BaseDataSetDesign) dataSetDesign );
		}

		if ( !useColumnHints )
		{
			dataSetDesign.getResultSetHints( ).clear( );
		}
		if ( !useFilters )
		{
			dataSetDesign.getFilters( ).clear( );
		}

		return DataSetProvider.getCurrentInstance( ).execute( dataSetHandle,
				dataSetDesign,
				rowsToReturn,
				context,
				session );
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @param queryDefn
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSetHandle,
			QueryDefinition queryDefn, boolean useColumnHints,
			boolean useFilters, DataRequestSession session )
			throws BirtException
	{
		return execute( dataSetHandle,
				queryDefn,
				useColumnHints,
				useFilters,
				false,
				null,
				session );
	}
	
	/**
	 * 
	 * @param dataSetHandle
	 * @param queryDefn
	 * @param useColumnHints
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSetHandle,
			IQueryDefinition queryDefn, boolean useColumnHints,
			boolean useFilters, boolean clearCache, ExecutionContext context,
			DataRequestSession session ) throws BirtException
	{
		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor( )
				.adaptDataSet( dataSetHandle );

		if ( !( dataSetHandle instanceof JointDataSetHandle || dataSetHandle instanceof DerivedDataSetHandle )
				&& context != null )
		{
			context.setReportContext( new ReportContextImpl( context ) );
			dataSetDesign = new ModelDteApiAdapter( context ).appendRuntimeInfoToDataSet( dataSetHandle,
					(BaseDataSetDesign) dataSetDesign );
		}
		if ( clearCache )
		{
			IBaseDataSourceDesign dataSourceDesign = session.getModelAdaptor( )
					.adaptDataSource( dataSetHandle.getDataSource( ) );
			session.clearCache( dataSourceDesign, dataSetDesign );
		}
		if ( !useColumnHints )
		{
			dataSetDesign.getResultSetHints( ).clear( );
		}
		if ( !useFilters )
		{
			dataSetDesign.getFilters( ).clear( );
		}

		return DataSetProvider.getCurrentInstance( ).execute( dataSetHandle,
				dataSetDesign,
				queryDefn,
				context,
				session );
	}
}
