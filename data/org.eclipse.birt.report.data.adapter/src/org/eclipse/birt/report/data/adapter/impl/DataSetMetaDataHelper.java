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
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * One note is the relationship between resultSet, columnHints and
 * cachedMetaData. resultSet and columnHints are combined to be used as the
 * additional column definition based on the column metadata of data set.
 * cachedMetaData is retrieved from the data engine, and the time of its
 * generation is after the resultSet and columnHints is applied to the original
 * metadata. resultHint is processed before columnHints, and they corresponds to
 * the IColumnDefinition of data engine.
 * 
 * When refreshing the metadata of dataset handle, resultSet should not be added
 * into data set design since resultSet is based on old metadata, and then it is
 * no use for updated metadata. But it is a little different for script dataset.
 * Since the metadata of script dataset comes from defined resultSet, the
 * special case is resultSet needs to be added when it meets script data set.
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
			return getRealMetaData( dataSetHandle );
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
	private IResultMetaData getRealMetaData( DataSetHandle dataSetHandle )
			throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setDataSetName( dataSetHandle.getQualifiedName( ) );
		query.setMaxRows( 1 );
		
		boolean useResultHints = dataSetHandle instanceof ScriptDataSetHandle;
		return new QueryExecutionHelper( dataEngine,
				modelAdaptor,
				moduleHandle,
				useResultHints ).executeQuery( query ).getResultMetaData( );
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
		IResultMetaData rsMeta = null;
		BirtException e = null;

		try
		{
			rsMeta = this.getDataSetMetaData( dataSetHandle, false );
		}
		catch ( BirtException e1 )
		{
			e = e1;
		}

		if ( needsSetCachedMetaData( dataSetHandle, rsMeta ) )
		{
			dataSetHandle.setCachedMetaData( StructureFactory.createCachedMetaData( ) );
			if ( rsMeta != null && rsMeta.getColumnCount( ) != 0 )
			{
				for ( int i = 1; i <= rsMeta.getColumnCount( ); i++ )
				{
					ResultSetColumn rsc = StructureFactory.createResultSetColumn( );
					rsc.setColumnName( getColumnName( rsMeta, i ) );
					rsc.setDataType( toModelDataType( rsMeta.getColumnType( i ) ) );
					rsc.setPosition( new Integer( i ) );

					dataSetHandle.getCachedMetaDataHandle( )
							.getResultSet( )
							.addItem( rsc );
				}
			}
			
			if ( dataSetHandle instanceof ScriptDataSetHandle == false )
				dataSetHandle.getPropertyHandle( DataSetHandle.RESULT_SET_PROP )
						.clearValue( );
		}

		if ( e != null )
			throw e;

		return rsMeta;
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @param rsMeta
	 * @return
	 * @throws BirtException
	 */
	private boolean needsSetCachedMetaData( DataSetHandle dataSetHandle,
			IResultMetaData rsMeta ) throws BirtException
	{
		if ( dataSetHandle.getCachedMetaDataHandle( ) == null
				|| rsMeta == null || rsMeta.getColumnCount( ) == 0 )
			return true;

		List list = new ArrayList( );
		for ( Iterator iter = dataSetHandle.getCachedMetaDataHandle( )
				.getResultSet( )
				.iterator( ); iter.hasNext( ); )
		{
			list.add( iter.next( ) );
		}

		if ( list.size( ) != rsMeta.getColumnCount( ) )
			return true;

		for ( int i = 1; i <= rsMeta.getColumnCount( ); i++ )
		{
			ResultSetColumnHandle handle = (ResultSetColumnHandle) list.get( i - 1 );

			if ( !handle.getColumnName( ).equals( getColumnName( rsMeta, i ) )
					|| !handle.getDataType( )
							.equals( toModelDataType( rsMeta.getColumnType( i ) ) ) )
				return true;
		}

		return false;
	}
	
	/**
	 * 
	 * @param rsMeta
	 * @param index
	 * @return
	 * @throws BirtException
	 */
	private String getColumnName( IResultMetaData rsMeta, int index )
			throws BirtException
	{
		return ( rsMeta.getColumnAlias( index ) == null || rsMeta.getColumnAlias( index )
				.trim( )
				.length( ) == 0 ) ? rsMeta.getColumnName( index )
				: rsMeta.getColumnAlias( index );
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
