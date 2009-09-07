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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.CompatibilityUtil;


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
	private DataSessionContext sessionContext;

	/**
	 * 
	 * @param dataEngine
	 * @param modelAdaptor
	 * @param moduleHandle
	 */
	DataSetMetaDataHelper( DataEngine dataEngine, IModelAdapter modelAdaptor,
			DataSessionContext sessionContext )
	{
		this.dataEngine = dataEngine;
		this.modelAdaptor = modelAdaptor;
		this.sessionContext = sessionContext;
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
		IResultMetaData metaData = MetaDataPopulator.retrieveResultMetaData( dataSetHandle );

		if ( metaData == null )
		{
			QueryDefinition query = new QueryDefinition( );
			query.setDataSetName( dataSetHandle.getQualifiedName( ) );
			query.setMaxRows( 1 );
			query.setAutoBinding( true );
			
			metaData = new QueryExecutionHelper( dataEngine,
					modelAdaptor,
					sessionContext,
					false ).executeQuery( query ).getResultMetaData( );
			addResultSetColumn( dataSetHandle, metaData );
			if ( MetaDataPopulator.needsUseResultHint( dataSetHandle, metaData ) )
			{
				metaData = new QueryExecutionHelper( dataEngine,
						modelAdaptor,
						sessionContext,
						true ).executeQuery( query ).getResultMetaData( );
			}
		}
		
		if ( metaData!= null && !( dataSetHandle instanceof ScriptDataSetHandle ) )
			clearUnusedData( dataSetHandle, metaData );
		return metaData;
	}
	
	/**
	 * 
	 * @param meta
	 * @throws BirtException 
	 */
	private void addResultSetColumn( DataSetHandle dataSetHandle,
			IResultMetaData meta ) throws BirtException
	{
		if ( meta == null || !( dataSetHandle instanceof OdaDataSetHandle ) )
			return;

		List columnList = new ArrayList( );
		HashSet orgColumnNameSet = new HashSet( );
		HashSet uniqueColumnNameSet = new HashSet( );
		for ( int i = 1; i <= meta.getColumnCount( ); i++ )
		{
			OdaResultSetColumn rsColumn = new OdaResultSetColumn( );

			String uniqueName;
			if ( !meta.isComputedColumn( i ) )
			{
				uniqueName = MetaDataPopulator.getUniqueName( orgColumnNameSet,
						uniqueColumnNameSet,
						meta.getColumnName( i ),
						i - 1 );
				rsColumn.setColumnName( uniqueName );
				rsColumn.setDataType( DataAdapterUtil.adapterToModelDataType( meta.getColumnType( i ) ) );
				rsColumn.setNativeName( meta.getColumnName( i ) );
				rsColumn.setPosition( new Integer( i ) );

				columnList.add( rsColumn );

				uniqueColumnNameSet.add( uniqueName );
			}
		}

		// holdEvent
		CompatibilityUtil.addResultSetColumn( dataSetHandle, columnList );
	}
	
	/**
	 * 
	 * @param dataSetHandle
	 * @param metaData
	 * @throws BirtException 
	 */
    private final void clearUnusedData( DataSetHandle dataSetHandle,
			IResultMetaData metaData ) throws BirtException
	{
		clearUnusedColumnHints( dataSetHandle, metaData );
	}
    
    /**
	 * clear unused column hints
     * @throws BirtException 
	 * 
	 */
	private final void clearUnusedColumnHints( DataSetHandle dataSetHandle,
			IResultMetaData metaData ) throws BirtException
	{

		PropertyHandle handle = dataSetHandle.getPropertyHandle( DataSetHandle.COLUMN_HINTS_PROP );
		if ( handle != null && handle.getListValue( ) != null )
		{
			ArrayList list = handle.getListValue( );
			int count = list.size( );
			for ( int n = count - 1; n >= 0; n-- )
			{
				ColumnHint hint = (ColumnHint) list.get( n );
				String columnName = (String) hint.getProperty( handle.getModule( ),
						ColumnHint.COLUMN_NAME_MEMBER );
				boolean found = false;
				if ( !isEmpty( hint, handle.getModule( ).getModuleHandle( ) ) )
				{
					for ( int m = 0; m < metaData.getColumnCount( ) && !found; m++ )
					{
						found = columnName.equals( metaData.getColumnName( m + 1 ) );
					}
				}

				if ( !found )
				{
					try
					{
						// remove the item
						handle.removeItem( hint );
					}
					catch ( PropertyValueException e )
					{
					}
				}
			}
		}
	}
    
	/**
	 * 
	 * @param hint
	 * @param designHandle
	 * @return
	 */
	private boolean isEmpty( ColumnHint hint, ModuleHandle designHandle )
	{
		String alias = (String) hint.getProperty( designHandle.getModule( ),
				ColumnHint.ALIAS_MEMBER );
		String displayName = (String) hint.getProperty( designHandle.getModule( ),
				ColumnHint.DISPLAY_NAME_MEMBER );
		String displayNameKey = (String) hint.getProperty( designHandle.getModule( ),
				ColumnHint.DISPLAY_NAME_ID_MEMBER );
		String helpText = (String) hint.getProperty( designHandle.getModule( ),
				ColumnHint.HELP_TEXT_MEMBER );
		String analysis = (String) hint.getProperty( designHandle.getModule( ),
				ColumnHint.ANALYSIS_MEMBER );

		return ( ( alias == null || alias.trim( ).length( ) == 0 )
				&& ( displayName == null || displayName.trim( ).length( ) == 0 )
				&& ( displayNameKey == null || displayNameKey.trim( ).length( ) == 0 )
				&& ( helpText == null || helpText.trim( ).length( ) == 0 ) && ( analysis == null || analysis.trim( )
				.length( ) == 0 ) );
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
		return refreshMetaData( dataSetHandle, false );
	}

	/**
	 * 
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	IResultMetaData refreshMetaData( DataSetHandle dataSetHandle,
			boolean holdEvent ) throws BirtException
	{
		IResultMetaData rsMeta = null;
		try
		{
			rsMeta = this.getDataSetMetaData( dataSetHandle, false );
		}
		catch ( BirtException e1 )
		{
			//clear cache meta data
			if ( holdEvent || !dataSetHandle.canEdit( ) )
			{
				CompatibilityUtil.updateResultSetinCachedMetaData( dataSetHandle,
						new ArrayList( ) );
			}
			else
			{
				if ( dataSetHandle.getCachedMetaDataHandle( ) != null )
					dataSetHandle.getCachedMetaDataHandle( )
							.getResultSet( )
							.clearValue( );
				else
					dataSetHandle.setCachedMetaData( StructureFactory.createCachedMetaData( ) );
			}
			throw e1;
		}

		if ( needsSetCachedMetaData( dataSetHandle, rsMeta ) )
		{
			List columnList = new ArrayList( );
			if ( rsMeta != null && rsMeta.getColumnCount( ) != 0 )
			{
				for ( int i = 1; i <= rsMeta.getColumnCount( ); i++ )
				{
					ResultSetColumn rsc = StructureFactory.createResultSetColumn( );
					rsc.setColumnName( getColumnName( rsMeta, i ) );
					rsc.setDataType( DataAdapterUtil.adapterToModelDataType( rsMeta.getColumnType( i ) ) );
					rsc.setPosition( new Integer( i ) );

					columnList.add( rsc );
				}
			}

			if ( holdEvent || !dataSetHandle.canEdit( ) )
			{
				CompatibilityUtil.updateResultSetinCachedMetaData( dataSetHandle,
						columnList );
			}
			else
			{
				if ( dataSetHandle.getCachedMetaDataHandle( ) != null )
				{
					List resultSetColumnHandles = getResultSetColumnHandles( dataSetHandle.getCachedMetaDataHandle( ) );
					int i = 0;
					for ( ; i<columnList.size( ); i++ )
					{
						ResultSetColumn rsc = (ResultSetColumn)columnList.get( i );
						if ( i < resultSetColumnHandles.size( ) )
						{
							//update if needed, avoid writing "any" type to Model if old report contains "any" type
							ResultSetColumnHandle rsh = (ResultSetColumnHandle)resultSetColumnHandles.get( i );
							if ( !rsh.getColumnName( ).equals( rsc.getColumnName( ) ) )
							{
								rsh.setColumnName( rsc.getColumnName( ) );
							}
							if ( !rsh.getDataType( ).equals( rsc.getDataType( ) ))
							{
								rsh.setDataType( rsc.getDataType( ) );
							}
						}
						else
						{
							//some columns are to be added
							dataSetHandle.getCachedMetaDataHandle( ).getResultSet( ).addItem( rsc );
						}
					}
					if ( i < resultSetColumnHandles.size( ) )
					{
						//some columns are to be removed
						List toRemoved =  resultSetColumnHandles.subList( i, resultSetColumnHandles.size( ) );
						dataSetHandle.getCachedMetaDataHandle( ).getResultSet( ).removeItems( toRemoved );
					}
				}
				else
				{
					dataSetHandle.setCachedMetaData( StructureFactory.createCachedMetaData( ) );

					for ( int i = 0; i < columnList.size( ); i++ )
					{
						dataSetHandle.getCachedMetaDataHandle( )
								.getResultSet( )
								.addItem( (ResultSetColumn) columnList.get( i ) );
					}
				}

			}
		}
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

		List list = getResultSetColumnHandles( dataSetHandle.getCachedMetaDataHandle( ) );

		if ( list.size( ) != rsMeta.getColumnCount( ) )
			return true;

		for ( int i = 1; i <= rsMeta.getColumnCount( ); i++ )
		{
			ResultSetColumnHandle handle = (ResultSetColumnHandle) list.get( i - 1 );

			if ( handle.getColumnName( ) == null
					|| !handle.getColumnName( ).equals( getColumnName( rsMeta,
							i ) )
					|| !handle.getDataType( )
							.equals( DataAdapterUtil.adapterToModelDataType( rsMeta.getColumnType( i ) ) ) )
				return true;
		}

		return false;
	}
	
	private List getResultSetColumnHandles( CachedMetaDataHandle cmdh )
	{
		
		List list = new ArrayList( );
		for ( Iterator iter = cmdh
				.getResultSet( )
				.iterator( ); iter.hasNext( ); )
		{
			list.add( iter.next( ) );
		}
		return list;
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
}
