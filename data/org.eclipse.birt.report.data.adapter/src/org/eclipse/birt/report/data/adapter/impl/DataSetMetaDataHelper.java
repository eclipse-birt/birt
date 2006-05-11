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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * 
 */
public class DataSetMetaDataHelper
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
	DataSetMetaDataHelper( DataEngine dataEngine, IModelAdapter modelAdaptor,
			ModuleHandle moduleHandle )
	{
		this.dataEngine = dataEngine;
		this.modelAdaptor = modelAdaptor;
		this.moduleHandle = moduleHandle;
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @param useCache
	 * @return
	 * @throws BirtException
	 */
	IResultMetaData getDataSetMetaData( DataSetHandle dataSetHandle,
			boolean useCache ) throws BirtException
	{
		if ( dataSetHandle == null )
		{
			throw new AdapterException( ResourceConstants.DATASETHANDLE_NULL_ERROR );
		}

		if ( useCache )
		{
			return getCachedMetaData( dataSetHandle.getCachedMetaDataHandle( ) );
		}
		else
		{
			return getRealMetaData( dataSetHandle.getName( ) );
		}
	}

	/**
	 * 
	 * @param cmdHandle
	 * @return
	 * @throws BirtException
	 */
	private IResultMetaData getCachedMetaData( CachedMetaDataHandle cmdHandle )
			throws BirtException
	{
		if ( cmdHandle == null )
			return null;

		Iterator it = cmdHandle.getResultSet( ).iterator( );
		List columnMeta = new ArrayList( );
		while ( it.hasNext( ) )
		{
			ResultSetColumnHandle rsColumn = (ResultSetColumnHandle) it.next( );
			IColumnDefinition cd = this.modelAdaptor.ColumnAdaptor( rsColumn );
			columnMeta.add( cd );
		}
		return new ResultMetaData( columnMeta );
	}

	/**
	 * @param dataSetName
	 * @return
	 * @throws BirtException
	 */
	private IResultMetaData getRealMetaData( String dataSetName )
			throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setDataSetName( dataSetName );
		query.setMaxRows( 1 );
		return new QueryExecutionHelper( dataEngine, modelAdaptor, moduleHandle ).executeQuery( query,
				null,
				null,
				null )
				.getResultMetaData( );
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	IResultMetaData refreshMetaData( DataSetHandle dataSetHandle )
			throws BirtException
	{
		dataSetHandle.setCachedMetaData( StructureFactory.createCachedMetaData( ) );

		IResultMetaData rsMeta = this.getDataSetMetaData( dataSetHandle, false );

		for ( int i = 1; i <= rsMeta.getColumnCount( ); i++ )
		{
			ResultSetColumn rsc = StructureFactory.createResultSetColumn( );
			rsc.setColumnName( ( rsMeta.getColumnAlias( i ) == null || rsMeta.getColumnAlias( i )
					.trim( )
					.length( ) == 0 ) ? rsMeta.getColumnName( i )
					: rsMeta.getColumnAlias( i ) );
			rsc.setDataType( toModelDataType( rsMeta.getColumnType( i ) ) );
			rsc.setPosition( new Integer( i ) );

			dataSetHandle.getCachedMetaDataHandle( )
					.getResultSet( )
					.addItem( rsc );
		}

		return rsMeta;
	}

	/**
	 * Map oda data type to model data type.
	 * 
	 * @param modelDataType
	 * @return
	 */
	private static String toModelDataType( int modelDataType )
	{
		if ( modelDataType == DataType.INTEGER_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
		else if ( modelDataType == DataType.STRING_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		else if ( modelDataType == DataType.DATE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		else if ( modelDataType == DataType.DECIMAL_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		else if ( modelDataType == DataType.DOUBLE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;

		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}
}
