/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;

/**
 * 
 */
class QueryExecutionHelper
{

	//
	private DataEngine dataEngine;
	private IModelAdapter modelAdaptor;
	private ModuleHandle moduleHandle;

	/**
	 * 
	 * @param dataEngine
	 * @param modelAdaptor
	 * @param moduleHandle
	 */
	QueryExecutionHelper( DataEngine dataEngine, IModelAdapter modelAdaptor,
			ModuleHandle moduleHandle )
	{
		this.dataEngine = dataEngine;
		this.modelAdaptor = modelAdaptor;
		this.moduleHandle = moduleHandle;
	}

	/**
	 * 
	 * @param queryDefn
	 * @param paramBindingIt
	 * @param filterIt
	 * @param bindingIt
	 * @return
	 * @throws BirtException
	 */
	IQueryResults executeQuery( QueryDefinition queryDefn,
			Iterator paramBindingIt, Iterator filterIt, Iterator bindingIt )
			throws BirtException
	{
		defineDataSourceDataSet( queryDefn );

		populateQueryDefn( queryDefn, paramBindingIt, filterIt, bindingIt );

		return dataEngine.prepare( queryDefn ).execute( null );
	}

	/**
	 * @param queryDefn
	 * @throws AdapterException
	 * @throws BirtException
	 */
	private void defineDataSourceDataSet( QueryDefinition queryDefn )
			throws AdapterException, BirtException
	{
		String dataSetName = queryDefn.getDataSetName( );

		List l = this.moduleHandle.getAllDataSets( );
		DataSetHandle handle = null;
		for ( int i = 0; i < l.size( ); i++ )
		{
			if ( ( (DataSetHandle) l.get( i ) ).getQualifiedName( ) != null
					&& ( (DataSetHandle) l.get( i ) ).getQualifiedName( )
							.equals( dataSetName ) )
			{
				handle = (DataSetHandle) l.get( i );
			}
		}

		defineDataSet( handle );
	}

	/**
	 * @param queryDefn
	 * @param paramBindingIt
	 * @param filterIt
	 * @param bindingIt
	 */
	private void populateQueryDefn( QueryDefinition queryDefn,
			Iterator paramBindingIt, Iterator filterIt, Iterator bindingIt )
	{
		while ( bindingIt != null && bindingIt.hasNext( ) )
		{
			IComputedColumn column = this.modelAdaptor.adaptComputedColumn( (ComputedColumnHandle) bindingIt.next( ) );
			ScriptExpression sxp = (ScriptExpression) column.getExpression( );
			sxp.setDataType( column.getDataType( ) );
			queryDefn.addResultSetExpression( column.getName( ), sxp );
		}

		List parameterBindings = convertParamterBindings( paramBindingIt );

		// add parameter binding
		if ( parameterBindings != null )
			queryDefn.getInputParamBindings( ).addAll( parameterBindings );

		// add filter
		List filters = convertFilters( filterIt );
		if ( filters != null )
			queryDefn.getFilters( ).addAll( filters );
	}

	/**
	 * 
	 * @param filterIt
	 * @return
	 */
	private List convertFilters( Iterator filterIt )
	{
		if ( filterIt == null )
			return null;

		List filters = new ArrayList( );
		while ( filterIt.hasNext( ) )
		{
			while ( filterIt.hasNext( ) )
			{
				IFilterDefinition filter = this.modelAdaptor.adaptFilter( (FilterConditionHandle) filterIt.next( ) );
				filters.add( filter );
			}
		}
		return filters;
	}

	/**
	 * 
	 * @param paramBindingIt
	 * @return
	 */
	private List convertParamterBindings( Iterator paramBindingIt )
	{
		if ( paramBindingIt == null )
			return null;

		List parameterBindings = new ArrayList( );
		while ( paramBindingIt.hasNext( ) )
		{
			ParamBindingHandle paramBinding = (ParamBindingHandle) paramBindingIt.next( );
			if ( paramBinding.getExpression( ) != null )
			{
				ScriptExpression paramValueExpr = new ScriptExpression( paramBinding.getExpression( ) );
				InputParameterBinding inputParamBinding = new InputParameterBinding( paramBinding.getParamName( ),
						paramValueExpr );
				parameterBindings.add( inputParamBinding );
			}
		}
		return parameterBindings;
	}

	/**
	 * @param dataSetName
	 * @throws AdapterException
	 * @throws BirtException
	 */
	private void defineDataSet( DataSetHandle handle ) throws AdapterException,
			BirtException
	{

		if ( handle == null )
			throw new AdapterException( ResourceConstants.DATASETHANDLE_NULL_ERROR );
		
		DataSourceHandle dataSourceHandle = handle.getDataSource( );
		if ( dataSourceHandle != null )
		{
			IBaseDataSourceDesign dsourceDesign = this.modelAdaptor.adaptDataSource( dataSourceHandle );
			dataEngine.defineDataSource( dsourceDesign );
		}
		if ( handle instanceof JointDataSetHandle )
		{
			defineSourceDataSets( (JointDataSetHandle) handle );
		}

		dataEngine.defineDataSet( this.modelAdaptor.adaptDataSet( handle ) );
	}

	/**
	 * @param dataSet
	 * @param dataSetDesign
	 * @throws BirtException
	 */
	private void defineSourceDataSets( JointDataSetHandle jointDataSetHandle )
			throws BirtException
	{
		List dataSets = jointDataSetHandle.getDataSetNames( );
		for( int i = 0; i < dataSets.size( ); i++ )
		{
			DataSetHandle dsHandle = jointDataSetHandle.getModuleHandle( ).findDataSet( dataSets.get( i ).toString( ) ); 
			if ( dsHandle != null )
			{
				defineDataSet( dsHandle );
			}
		}
	}

}
