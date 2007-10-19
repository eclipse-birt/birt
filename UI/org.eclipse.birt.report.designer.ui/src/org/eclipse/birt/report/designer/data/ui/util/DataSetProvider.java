/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

/**
 * 
 * Utility class to get meta data and data from data set
 *
 */
public final class DataSetProvider
{

	private static DataSetProvider instance = null;

	// column hash table
	private transient Hashtable htColumns = new Hashtable( 10 );
	private static Hashtable htDataSourceExtensions = new Hashtable( 10 );
	private transient Hashtable sessionTable = new Hashtable( 10 );

	// constant value
	private static final char RENAME_SEPARATOR = '_';
	private static String UNNAME_PREFIX = "UNNAMED"; //$NON-NLS-1$

	/**
	 * @return
	 */
	private static DataSetProvider newInstance( )
	{
		return new DataSetProvider( );
	}

	/**
	 * 
	 * @return
	 */
	public static DataSetProvider getCurrentInstance( )
	{
		if ( instance == null )
			instance = newInstance( );
		return instance;
	}

	/**
	 * get columns data by data set name
	 * @param dataSetName
	 * @param refresh
	 * @return
	 */
	public DataSetViewData[] getColumns( String dataSetName, boolean refresh )
	{
		ModuleHandle handle = Utility.getReportModuleHandle( );
		DataSetHandle dataSet = handle.findDataSet( dataSetName );
		if ( dataSet == null )
		{
			return new DataSetViewData[]{};
		}
		return getColumns( dataSet, refresh );
	}

	/**
	 * get column data by data set handle
	 * @param dataSet
	 * @param refresh
	 * @return
	 */
	public DataSetViewData[] getColumns( DataSetHandle dataSet, boolean refresh )
	{
		return getColumns( dataSet, refresh, true, false );
	}

	/**
	 * 
	 * @param dataSet
	 * @param refresh
	 * @param useColumnHints
	 *            Only applicable if the list is refreshed.
	 * @return
	 */
	public DataSetViewData[] getColumns( DataSetHandle dataSet,
			boolean refresh, boolean useColumnHints,
			boolean suppressErrorMessage )
	{
		if ( dataSet == null )
		{
			return new DataSetViewData[0];
		}
		DataSetViewData[] columns = null;
		try
		{
			DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSet.getModuleHandle( ) );
			DataRequestSession session = DataRequestSession.newSession( context );

			// Find the data set in the hashtable
			columns = (DataSetViewData[]) htColumns.get( dataSet );

			// If there are not cached get them from the column hints
			if ( columns == null || refresh )
			{
				columns = this.populateAllOutputColumns( dataSet, session );
				htColumns.put( dataSet, columns );
			}
			session.shutdown( );
		}
		catch ( BirtException e )
		{
			if ( !suppressErrorMessage )
			{
				ExceptionHandler.handle( e );
			}
			columns = null;
		}

		// If the columns array is still null
		// just initialize it to an empty array
		if ( columns == null )
		{
			columns = new DataSetViewData[]{};
			updateModel( dataSet, columns );
			htColumns.put( dataSet, columns );
		}
		return columns;
	}
    
	/**
	 * populate all output columns in viewer display. The output columns is
	 * retrieved from oda dataset handles's RESULT_SET_PROP and
	 * COMPUTED_COLUMNS_PROP.
	 * 
	 * @throws BirtException
	 */
	public DataSetViewData[] populateAllOutputColumns(
			DataSetHandle dataSetHandle, DataRequestSession session ) throws BirtException
	{
		IResultMetaData metaData = session.getDataSetMetaData( dataSetHandle,
				false );

		DataSetViewData[] items = new DataSetViewData[metaData.getColumnCount( )];

		for ( int i = 0; i < metaData.getColumnCount( ); i++ )
		{
			items[i] = new DataSetViewData( );
			items[i].setName( metaData.getColumnName( i + 1 ) );
			items[i].setDataTypeName( metaData.getColumnTypeName( i + 1 ) );
			items[i].setAlias( metaData.getColumnAlias( i + 1 ) );
			items[i].setComputedColumn( metaData.isComputedColumn( i + 1 ) );
			items[i].setPosition( i + 1 );
			items[i].setDataType( metaData.getColumnType( i + 1 ) );
		}
		updateModel( dataSetHandle, items );
		return items;
	}
	
	/**
	 * get Cached metadata
	 * 
	 * @throws BirtException
	 */
	public DataSetViewData[] populateAllCachedMetaData(
			DataSetHandle dataSetHandle, DataRequestSession session )
			throws BirtException
	{
		IResultMetaData metaData = session.getDataSetMetaData( dataSetHandle,
				true );

		DataSetViewData[] items = new DataSetViewData[metaData.getColumnCount( )];

		for ( int i = 0; i < metaData.getColumnCount( ); i++ )
		{
			items[i] = new DataSetViewData( );
			items[i].setName( metaData.getColumnName( i + 1 ) );
			items[i].setDataTypeName( metaData.getColumnTypeName( i + 1 ) );
			items[i].setAlias( metaData.getColumnAlias( i + 1 ) );
			items[i].setComputedColumn( metaData.isComputedColumn( i + 1 ) );
			items[i].setPosition( i + 1 );
			items[i].setDataType( metaData.getColumnType( i + 1 ) );
		}
		return items;
	}
	
	/**
	 * update the columns of the DataSetHandle and put the new DataSetViewData[] into htColumns
	 * 
	 * @param dataSet
	 * @param dsItemModel
	 */
	public void updateColumnsOfDataSetHandle( DataSetHandle dataSet,
			DataSetViewData[] dsItemModel )
	{
		if ( dataSet == null || dsItemModel == null || dsItemModel.length == 0 )
			return;
		htColumns.put( dataSet, dsItemModel );
	}
	
	/**
	 * This function should be called very carefully. Presently it is only
	 * called in DataSetEditorDialog#performCancel.
	 * 
	 * @param dataSet
	 * @param itemModel
	 */
	public void setModelOfDataSetHandle( DataSetHandle dataSet,
			DataSetViewData[] dsItemModel )
	{
		if ( dataSet == null || dsItemModel == null )
			return;

		updateModel( dataSet, dsItemModel );
		cleanUnusedResultSetColumn( dataSet, dsItemModel );
		cleanUnusedComputedColumn( dataSet, dsItemModel );
		htColumns.put( dataSet, dsItemModel );
	}
	
	/**
	 * To rollback original datasetHandle, clean unused resultset columm
	 * 
	 * @param dataSetHandle
	 * @param dsItemModel
	 */
	private void cleanUnusedResultSetColumn( DataSetHandle dataSetHandle,
			DataSetViewData[] dsItemModel )
	{
		PropertyHandle handle = dataSetHandle.getPropertyHandle( DataSetHandle.RESULT_SET_PROP );
		if ( handle != null && handle.getListValue( ) != null )
		{
			ArrayList list = handle.getListValue( );
			int count = list.size( );
			for ( int n = count - 1; n >= 0; n-- )
			{
				ResultSetColumn rsColumn = (ResultSetColumn) list.get( n );
				String columnName = (String) rsColumn.getColumnName( );
				boolean found = false;

				for ( int m = 0; m < dsItemModel.length; m++ )
				{
					if ( columnName.equals( dsItemModel[m].getName( ) ) )
					{
						found = true;
						break;
					}
				}

				if ( !found )
				{
					try
					{
						// remove the item
						handle.removeItem( rsColumn );
					}
					catch ( PropertyValueException e )
					{
					}
				}
			}
		}
	}

	/**
	 * To rollback original datasetHandle, clean unused computed columm
	 * 
	 * @param dataSetHandle
	 * @param dsItemModel
	 */
	private void cleanUnusedComputedColumn( DataSetHandle dataSetHandle,
			DataSetViewData[] dsItemModel )
	{
		PropertyHandle handle = dataSetHandle.getPropertyHandle( DataSetHandle.COMPUTED_COLUMNS_PROP );
		if ( handle != null && handle.getListValue( ) != null )
		{
			ArrayList list = handle.getListValue( );
			int count = list.size( );
			for ( int n = count - 1; n >= 0; n-- )
			{
				ComputedColumn rsColumn = (ComputedColumn) list.get( n );
				String columnName = (String) rsColumn.getName( );
				boolean found = false;

				for ( int m = 0; m < dsItemModel.length; m++ )
				{
					if ( columnName.equals( dsItemModel[m].getName( ) ) )
					{
						found = true;
						break;
					}
				}

				if ( !found )
				{
					try
					{
						// remove the item
						handle.removeItem( rsColumn );
					}
					catch ( PropertyValueException e )
					{
					}
				}
			}
		}
	}

	/**
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSet, DataRequestSession session ) throws BirtException
	{
		return execute( dataSet, true, true, -1, session );
	}
	
	/**
	 * execute query definition 
	 * @param dataSet
	 * @param useColumnHints
	 * @param rowsToReturn
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSet,
			boolean useColumnHints, boolean useFilters, int rowsToReturn,
			DataRequestSession session ) throws BirtException
	{

	    populateAllOutputColumns( dataSet, session );

		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor( )
				.adaptDataSet( dataSet );

		if ( !useColumnHints )
		{
			dataSetDesign.getResultSetHints( ).clear( );
		}
		if ( !useFilters )
		{
			dataSetDesign.getFilters( ).clear( );
		}
		
		QueryDefinition queryDefn = getQueryDefinition( dataSetDesign,
				rowsToReturn );

		IQueryResults resultSet = executeQuery( session, queryDefn );
		saveResultToDataItems( dataSet, resultSet );

		return resultSet;
	}

	/**
	 * 
	 * @param dataSet
	 * @param queryDefn
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSet,
			QueryDefinition queryDefn, boolean useColumnHints,
			boolean useFilters, DataRequestSession session )
			throws BirtException
	{
		return this.execute( dataSet,
				queryDefn,
				useColumnHints,
				useFilters,
				false,
				session );
	}	
	
	/**
	 * 
	 * @param dataSet
	 * @param queryDefn
	 * @param useColumnHints
	 * @return
	 * @throws BirtException
	 */
	public IQueryResults execute( DataSetHandle dataSet,
			IQueryDefinition queryDefn, boolean useColumnHints,
			boolean useFilters, boolean clearCache,
			DataRequestSession session ) throws BirtException
	{

		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor( )
				.adaptDataSet( dataSet );
		if ( clearCache )
		{
			IBaseDataSourceDesign dataSourceDesign = session.getModelAdaptor( )
					.adaptDataSource( dataSet.getDataSource( ) );
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
		IQueryResults resultSet = executeQuery( session, queryDefn );
		saveResultToDataItems( dataSet, resultSet );

		return resultSet;
	}
	
	/**
	 * 
	 * @param dataSet
	 * @param resultSet
	 * @throws BirtException
	 */
	private void saveResultToDataItems( DataSetHandle dataSet,
			IQueryResults resultSet ) throws BirtException
	{

		// Get the metadata
		IResultMetaData metaData = resultSet.getResultMetaData( );
		// Put the columns into the hashtable
		int columnCount = 0;
		if ( metaData != null )
			columnCount = metaData.getColumnCount( );
		DataSetViewData[] columns = new DataSetViewData[columnCount];

		// check whether the column name has been changed,due to changes in
		// query text.
		// clear modle resultsetColumn,then execute again.

		// a Set of original column name
		HashSet orgColumnNameSet = new HashSet( );
		// a Set of new column name
		HashSet uniqueColumnNameSet = new HashSet( );
		for ( int n = 0; n < columns.length; n++ )
		{
			orgColumnNameSet.add( metaData.getColumnName( n + 1 ) );
		}

		for ( int n = 0; n < columns.length; n++ )
		{
			columns[n] = new DataSetViewData( );
			columns[n].setParent( dataSet );
			columns[n].setDataType( metaData.getColumnType( n + 1 ) );
			columns[n].setDataTypeName( metaData.getColumnTypeName( n + 1 ) );
			columns[n].setPosition( n + 1 );
			columns[n].setAlias( metaData.getColumnAlias( n + 1 ) );
			columns[n].setComputedColumn( metaData.isComputedColumn( n + 1 ) );
			String columnName = metaData.getColumnName( n + 1 );

			// give this column a unique name
			String uniqueColumnName = getUniqueName( orgColumnNameSet,
					uniqueColumnNameSet,
					columnName,
					n );

			// Update the column in UI layer
			columns[n].setDataSetColumnName( uniqueColumnName );
			uniqueColumnNameSet.add( uniqueColumnName );

			// Update the column in Model if necessary
			if ( !uniqueColumnName.equals( columnName ) )
				updateModelColumn( dataSet, columns[n] );
		}
		updateModel( dataSet, columns );
		htColumns.put( dataSet, columns );
	}
	
	/**
	 * 
	 * @param ds
	 * @param column
	 */
	private void updateModelColumn( DataSetHandle ds, DataSetViewData column )
	{
		PropertyHandle resultSetColumns = ds.getPropertyHandle( DataSetHandle.RESULT_SET_PROP );
		if ( resultSetColumns == null )
			return;
		// update result set columns
		Iterator iterator = resultSetColumns.iterator( );
		if ( iterator == null )
			return;
		while ( iterator.hasNext( ) )
		{
			ResultSetColumnHandle rsColumnHandle = (ResultSetColumnHandle) iterator.next( );
			assert rsColumnHandle.getPosition( ) != null;
			if ( rsColumnHandle.getPosition( ).intValue( ) == column.getPosition( ) )
			{
				if ( rsColumnHandle.getColumnName( ) != null
						&& !rsColumnHandle.getColumnName( )
								.equals( column.getDataSetColumnName( ) ) )
				{
					try
					{
						rsColumnHandle.setColumnName( column.getDataSetColumnName( ) );
					}
					catch ( SemanticException e )
					{
					}
				}
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param session
	 * @param queryDefn
	 * @return
	 * @throws BirtException
	 */
	private IQueryResults executeQuery( DataRequestSession session,
			IQueryDefinition queryDefn ) throws BirtException
	{
		IQueryResults resultSet = session.executeQuery( queryDefn,
				null,
				null,
				null );

		return resultSet;
	}

	/**
	 * 
	 * @param dataSetDesign
	 * @param rowsToReturn
	 * @return
	 */
	public final QueryDefinition getQueryDefinition(
			IBaseDataSetDesign dataSetDesign, int rowsToReturn )
	{
		if ( dataSetDesign != null )
		{
			QueryDefinition defn = new QueryDefinition( null );
			defn.setDataSetName( dataSetDesign.getName( ) );
			if ( rowsToReturn > 0 )
			{
				defn.setMaxRows( rowsToReturn );
			}
			List parameters = dataSetDesign.getParameters( );
			Iterator iter = parameters.iterator( );
			while ( iter.hasNext( ) )
			{
				ParameterDefinition paramDefn = (ParameterDefinition) iter.next( );
				if ( paramDefn.isInputMode( ) )
				{
					if ( paramDefn.getDefaultInputValue( ) != null )
					{
						InputParameterBinding binding = new InputParameterBinding( paramDefn.getName( ),
								new ScriptExpression( paramDefn.getDefaultInputValue( )
										.toString( ) ) );
						defn.addInputParamBinding( binding );
					}
				}
			}
			return defn;
		}
		return null;
	}

	/**
	 * @param dataSetDesign
	 * @param bindingParams
	 * @return
	 */
	public final QueryDefinition getQueryDefinition(
			IBaseDataSetDesign dataSetDesign, ParamBindingHandle[] bindingParams )
	{
		return getQueryDefinition( dataSetDesign, bindingParams, -1 );
	}
	
	/**
	 * @param dataSetDesign
	 * @param bindingParams
	 * @param i
	 * @return
	 */
	private QueryDefinition getQueryDefinition(
			IBaseDataSetDesign dataSetDesign,
			ParamBindingHandle[] bindingParams, int rowsToReturn )
	{
		if ( bindingParams == null || bindingParams.length == 0 )
		{
			return getQueryDefinition( dataSetDesign, rowsToReturn );
		}
		if ( dataSetDesign != null )
		{
			QueryDefinition defn = new QueryDefinition( null );
			defn.setDataSetName( dataSetDesign.getName( ) );
			if ( rowsToReturn > 0 )
			{
				defn.setMaxRows( rowsToReturn );
			}

			for ( int i = 0; i < bindingParams.length; i++ )
			{
				ParamBindingHandle param = bindingParams[i];
				InputParameterBinding binding = new InputParameterBinding( param.getParamName( ),
						new ScriptExpression( param.getExpression( ) ) );
				defn.addInputParamBinding( binding );
			}

			return defn;
		}
		return null;
	}

	/**
	 * 
	 * @param orgColumnNameSet
	 * @param newColumnNameSet
	 * @param columnName
	 * @param index
	 * @return
	 */
	private String getUniqueName( HashSet orgColumnNameSet,
			HashSet newColumnNameSet, String columnName, int index )
	{
		String newColumnName;
		if ( columnName == null
				|| columnName.trim( ).length( ) == 0
				|| newColumnNameSet.contains( columnName ) )
		{
			// name conflict or no name,give this column a unique name
			if ( columnName == null || columnName.trim( ).length( ) == 0 )
				newColumnName = UNNAME_PREFIX
						+ RENAME_SEPARATOR + String.valueOf( index + 1 );
			else
				newColumnName = columnName
						+ RENAME_SEPARATOR + String.valueOf( index + 1 );

			int i = 1;
			while ( orgColumnNameSet.contains( newColumnName )
					|| newColumnNameSet.contains( newColumnName ) )
			{
				newColumnName += String.valueOf( RENAME_SEPARATOR ) + i;
				i++;
			}
		}
		else
		{
			newColumnName = columnName;
		}
		return newColumnName;
	}

	/**
	 * @param ds
	 * @param columns
	 */
	public void updateModel( DataSetHandle ds, DataSetViewData[] columns )
	{
		// get the column hints
		PropertyHandle handle = ds.getPropertyHandle( DataSetHandle.COLUMN_HINTS_PROP );
		PropertyHandle resultSetColumnHandle = ds.getPropertyHandle( DataSetHandle.RESULT_SET_HINTS_PROP );

		Iterator iter = handle.iterator( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				ColumnHintHandle hint = (ColumnHintHandle) iter.next( );
				// Find this column in the list of columns passed and update the

				for ( int n = 0; n < columns.length; n++ )
				{
					// If the column name is not present then get the column
					// name from
					// the result set column definition if any
					String columnName = columns[n].getName( );
					if ( resultSetColumnHandle != null
							&& ( columnName == null || columnName.trim( )
									.length( ) == 0 ) )
					{
						Iterator resultIter = resultSetColumnHandle.iterator( );
						if ( resultIter != null )
						{
							while ( resultIter.hasNext( ) )
							{
								ResultSetColumnHandle column = (ResultSetColumnHandle) resultIter.next( );
								if ( column.getPosition( ).intValue( ) == n + 1 )
								{
									columnName = column.getColumnName( );
									break;
								}
							}
						}
						if ( columnName == null )
						{
							columnName = ""; //$NON-NLS-1$
						}
						columns[n].setName( columnName );
					}
					if ( columns[n].getName( ).equals( hint.getColumnName( ) ) )
					{
						columns[n].setDisplayName( hint.getDisplayName( ) );
						columns[n].setDisplayNameKey( hint.getDisplayNameKey( ) );
						columns[n].setAlias( hint.getAlias( ) );
						columns[n].setHelpText( hint.getHelpText( ) );
						break;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public IBaseDataSetDesign createDataSetDesign( DataSetHandle dataSet )
			throws BirtException
	{
		DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
				dataSet.getModuleHandle( ) );
		DataRequestSession session = DataRequestSession.newSession( context );

		return session.getModelAdaptor( ).adaptDataSet( dataSet );
	}

	/**
	 * 
	 * @param dataSource
	 * @return
	 * @throws BirtException
	 */
	public IBaseDataSourceDesign createDataSourceDesign(
			DataSourceHandle dataSource ) throws BirtException
	{
		DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
				dataSource.getModuleHandle( ) );
		DataRequestSession session = DataRequestSession.newSession( context );

		return session.getModelAdaptor( ).adaptDataSource( dataSource );
	}
	
	/**
	 * Get cached data set item model. If none is cached, return null;
	 * 
	 * @param ds
	 * @param columns
	 */
	public DataSetViewData[] getCachedDataSetItemModel( DataSetHandle ds )
	{
		DataSetViewData[] result = (DataSetViewData[]) this.htColumns.get( ds );
		if ( result == null )
		{

			DataRequestSession session;
			try
			{
				DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
						ds.getModuleHandle( ) );
				session = DataRequestSession.newSession( context );
				result = this.populateAllOutputColumns( ds, session );
				return result;
			}
			catch ( BirtException e )
			{
				result = new DataSetViewData[0];
			}
		}
		return result;
	}
	
	/**
	 * @param dataSetType
	 * @param dataSourceType
	 * @return
	 */
	public static IConfigurationElement findDataSetElement( String dataSetType,
			String dataSourceType )
	{
		// NOTE: multiple data source types can support the same data set type
		IConfigurationElement dataSourceElem = findDataSourceElement( dataSourceType );
		if ( dataSourceElem != null )
		{
			// Find data set declared in the same extension
			IExtension ext = dataSourceElem.getDeclaringExtension( );
			IConfigurationElement[] elements = ext.getConfigurationElements( );
			for ( int n = 0; n < elements.length; n++ )
			{
				if ( elements[n].getAttribute( "id" ).equals( dataSetType ) ) //$NON-NLS-1$
				{
					return elements[n];
				}
			}
		}
		return null;
	}
	
	/**
	 * @param dataSourceType
	 * @return
	 */
	public static IConfigurationElement findDataSourceElement(
			String dataSourceType )
	{
		assert ( dataSourceType != null );

		// Find it in the hashtable
		IConfigurationElement element = (IConfigurationElement) htDataSourceExtensions.get( dataSourceType );
		if ( element == null )
		{
			IConfigurationElement[] elements = Platform.getExtensionRegistry( )
					.getConfigurationElementsFor( "org.eclipse.birt.report.designer.ui.odadatasource" );
			for ( int n = 0; n < elements.length; n++ )
			{
				if ( elements[n].getAttribute( "id" ).equals( dataSourceType ) ) //$NON-NLS-1$
				{
					element = elements[n];
					htDataSourceExtensions.put( dataSourceType, element );
					break;
				}
			}
			elements = Platform.getExtensionRegistry( )
					.getConfigurationElementsFor( "org.eclipse.datatools.connectivity.oda.design.ui.dataSource" );
			for ( int n = 0; n < elements.length; n++ )
			{
				if ( elements[n].getAttribute( "id" ).equals( dataSourceType ) ) //$NON-NLS-1$
				{
					element = elements[n];
					htDataSourceExtensions.put( dataSourceType, element );
					break;
				}
			}
		}
		return element;
	}
	
	/**
	 * 
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	public final IBaseDataSetDesign getDataSetDesign( DataSetHandle dataSet,
			boolean useColumnHints, boolean useFilters ) throws BirtException
	{
		if ( dataSet != null )
		{
			DataRequestSession session = getDataRequestSession( dataSet );

			return getDataSetDesign( dataSet, useColumnHints, useFilters, session );
		}
		return null;
	}

	/**
	 * 
	 * @param session
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	private IBaseDataSetDesign getDataSetDesign( DataRequestSession session,DataSetHandle dataSet,
			boolean useColumnHints, boolean useFilters ) throws BirtException
	{
		if ( dataSet != null )
		{
			return getDataSetDesign( dataSet, useColumnHints, useFilters, session );
		}
		return null;
	}
	
	/**
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @param session
	 * @return
	 * @throws BirtException
	 */
	private IBaseDataSetDesign getDataSetDesign( DataSetHandle dataSet, boolean useColumnHints, boolean useFilters, DataRequestSession session ) throws BirtException
	{
		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor( )
				.adaptDataSet( dataSet );

		if ( !useColumnHints )
		{
			dataSetDesign.getResultSetHints( ).clear( );
		}
		if ( !useFilters )
		{
			dataSetDesign.getFilters( ).clear( );
		}
		if ( !( dataSet instanceof JointDataSetHandle ) )
		{
			IBaseDataSourceDesign dataSourceDesign = session.getModelAdaptor( )
					.adaptDataSource( dataSet.getDataSource( ) );
			session.defineDataSource( dataSourceDesign );

		}
		if ( dataSet instanceof JointDataSetHandle )
		{
			defineSourceDataSets( session, dataSet, dataSetDesign );
		}
		session.defineDataSet( dataSetDesign );
		return dataSetDesign;
	}
	

	/**
	 * @param dataSet
	 * @param dataSetDesign
	 * @throws BirtException
	 */
	private void defineSourceDataSets( DataRequestSession session, DataSetHandle dataSet,
			IBaseDataSetDesign dataSetDesign ) throws BirtException
	{
		List dataSets = dataSet.getModuleHandle( ).getAllDataSets( );
		for ( int i = 0; i < dataSets.size( ); i++ )
		{
			DataSetHandle dsHandle = (DataSetHandle) dataSets.get( i );
			if ( dsHandle.getName( ) != null )
			{
				if ( dsHandle.getName( )
						.equals( ( (IJointDataSetDesign) dataSetDesign ).getLeftDataSetDesignName( ) )
						|| dsHandle.getName( )
								.equals( ( (IJointDataSetDesign) dataSetDesign ).getRightDataSetDesignName( ) ) )
				{
					getDataSetDesign( session,dsHandle, true, true );
				}
			}
		}
	}

	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public DataRequestSession getDataRequestSession( DataSetHandle dataSet )
			throws BirtException
	{
		if ( sessionTable.get( dataSet.getName( ) ) != null )
			return (DataRequestSession) sessionTable.get( dataSet.getName( ) );
		else
		{
			DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSet.getModuleHandle( ) );
			DataRequestSession session = DataRequestSession.newSession( context );
			sessionTable.put( dataSet.getName( ), session );

			return session;
		}
	}
	
	/**
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public Collection getParametersFromDataSet( DataSetHandle dataSet )
			throws BirtException
	{
		return prepareQuery( dataSet ).getParameterMetaData( );
	}
	
	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public IPreparedQuery prepareQuery( DataSetHandle dataSet )
			throws BirtException
	{
		DataRequestSession session = getDataRequestSession( dataSet );

		IBaseDataSetDesign dataSetDesign = getDataSetDesign( dataSet,
				true,
				true );

		QueryDefinition queryDefn = getQueryDefinition( dataSetDesign, -1 );
		return session.prepare( queryDefn, null );
	}
	
	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public IPreparedQuery prepareQuery( DataSetHandle dataSet,
			IQueryDefinition query ) throws BirtException
	{
		DataRequestSession session = getDataRequestSession( dataSet );

		getDataSetDesign( dataSet, true, true );
		return session.prepare( query, null );
	}
	
	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public IPreparedQuery prepareQuery( DataSetHandle dataSet,
			IQueryDefinition query, boolean useColumnHints, boolean useFilters )
			throws BirtException
	{
		DataRequestSession session = getDataRequestSession( dataSet );

		getDataSetDesign( dataSet, useColumnHints, useFilters );
		return session.prepare( query, null );
	}
	
	/**
	 * Gets prepared query, given Data set, Parameter binding, and
	 * useColumnHints, useFilters information.
	 * 
	 * @param dataSet
	 *            Given DataSet providing SQL query and parameters.
	 * @param bindingParams
	 *            Given Parameter bindings providing binded parameters, null if
	 *            no binded parameters.
	 * @param useColumnHints
	 *            Using column hints flag.
	 * @param useFilters
	 *            Using filters flag.
	 * @return IPreparedQeury
	 * @throws BirtException
	 */
	public final IPreparedQuery prepareQuery( DataSetHandle dataSet,
			ParamBindingHandle[] bindingParams, boolean useColumnHints,
			boolean useFilters ) throws BirtException
	{
		DataRequestSession session = getDataRequestSession( dataSet );
		IBaseDataSetDesign dataSetDesign = getDataSetDesign( dataSet,
				useColumnHints,
				useFilters );

		return session.prepare( getQueryDefinition( dataSetDesign,
				bindingParams ), null );
	}

	/**
	 * @param parent
	 * @return
	 */
	public static ClassLoader getCustomScriptClassLoader( ClassLoader parent )
	{
		// For Bugzilla 106580: in order for Data Set Preview to locate POJO, we 
		// need to set current thread's context class loader to a custom loader 
		// which has the following path:
		// All workspace Java project's class path (this class path is already
		// has already calculated byorg.eclipse.birt.report.debug.ui plugin, and
		// set as system property "workspace.projectclasspath"
		String classPath = System.getProperty( "workspace.projectclasspath" );
		if ( classPath == null || classPath.length( ) == 0  )
			return parent;
		
		String[] classPathArray = classPath.split( File.pathSeparator, -1 );
		int count = classPathArray.length; 
		URL[] urls = new URL[count];
		for ( int i = 0; i < count; i++ )
		{
			File file = new File( classPathArray[i] );
			try
			{
				urls[i] = file.toURL( );
			} catch ( MalformedURLException e )
			{
				urls[i] = null;
			}
		}

		return new URLClassLoader( urls, parent);
	}
	
}
