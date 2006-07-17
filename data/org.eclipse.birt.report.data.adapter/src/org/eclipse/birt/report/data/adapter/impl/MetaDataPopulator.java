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
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * Retrieve metaData from resultset property.
 *
 */
public class MetaDataPopulator
{
	// constant value
	private static final char RENAME_SEPARATOR = '_';

	private static String UNNAME_PREFIX = "UNNAMED"; //$NON-NLS-1$
	
	/**
	 * populate all output columns in viewer display. The output columns is
	 * retrieved from oda dataset handles's RESULT_SET_PROP and
	 * COMPUTED_COLUMNS_PROP.
	 * 
	 * @throws BirtException
	 */
	public static IResultMetaData retrieveResultMetaData(
			DataSetHandle dataSetHandle ) throws BirtException
	{
		List resultSetList = null;
		if ( dataSetHandle instanceof OdaDataSetHandle )
		{
			resultSetList = (List) dataSetHandle.getProperty( OdaDataSetHandle.RESULT_SET_PROP );
			dataSetHandle.getPropertyHandle( DataSetHandle.RESULT_SET_HINTS_PROP )
					.clearValue( );
		}
		else
		{
			return null;
		}

		List computedList = (List) dataSetHandle.getProperty( OdaDataSetHandle.COMPUTED_COLUMNS_PROP );

		List columnMeta = new ArrayList( );
		ResultSetColumnDefinition columnDef;
		int index = 0;
		// populate result set columns
		if ( resultSetList != null )
		{
			ResultSetColumn resultSetColumn;
			HashSet orgColumnNameSet = new HashSet( );
			HashSet uniqueColumnNameSet = new HashSet( );

			for ( int n = 0; n < resultSetList.size( ); n++ )
			{
				orgColumnNameSet.add( ( (ResultSetColumn) resultSetList.get( n ) ).getColumnName( ) );
			}

			for ( int i = 0; i < resultSetList.size( ); i++ )
			{

				resultSetColumn = (ResultSetColumn) resultSetList.get( i );
				String uniqueName = getUniqueName( orgColumnNameSet,
						uniqueColumnNameSet,
						resultSetColumn.getColumnName( ),
						i );
				columnDef = new ResultSetColumnDefinition( uniqueName );

				uniqueColumnNameSet.add( uniqueName );

				columnDef.setDataTypeName( resultSetColumn.getDataType( ) );
				columnDef.setDataType( ModelAdapter.adaptModelDataType( resultSetColumn.getDataType( ) ) );
				columnDef.setColumnPosition( resultSetColumn.getPosition( )
						.intValue( ) );

				if ( !uniqueName.equals( resultSetColumn.getColumnName( ) ) )
				{
					updateModelColumn( dataSetHandle, columnDef );
				}
				ColumnHintHandle columnHint = findColumnHint( dataSetHandle,
						uniqueName );
				if ( columnHint != null )
				{
					columnDef.setAlias( columnHint.getAlias( ) );
					columnDef.setLableName( columnHint.getDisplayName( ) );
				}
				columnDef.setComputedColumn( false );
				columnMeta.add( columnDef );
				index++;
			}

			// populate computed columns
			if ( computedList != null )
			{
				ComputedColumn computedColumn;
				Iterator computedColumnIterator = computedList.iterator( );
				while ( computedColumnIterator.hasNext( ) )
				{
					computedColumn = (ComputedColumn) computedColumnIterator.next( );
					columnDef = new ResultSetColumnDefinition( computedColumn.getName( ) );

					columnDef.setDataTypeName( computedColumn.getDataType( ) );
					columnDef.setDataType( ModelAdapter.adaptModelDataType( computedColumn.getDataType( ) ) );
					if ( findColumnHint( dataSetHandle,
							computedColumn.getName( ) ) != null )
					{
						ColumnHintHandle columnHint = findColumnHint( dataSetHandle,
								computedColumn.getName( ) );
						columnDef.setAlias( columnHint.getAlias( ) );
						columnDef.setLableName( columnHint.getDisplayName( ) );
					}
					columnDef.setComputedColumn( true );
					columnMeta.add( columnDef );
					index++;
				}
			}
			return new ResultMetaData2( columnMeta );
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
	private static String getUniqueName( HashSet orgColumnNameSet,
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
	 * find column hint according to the columnName
	 * 
	 * @param columnName
	 * @return
	 */
	private static ColumnHintHandle findColumnHint( DataSetHandle dataSetHandle, String columnName )
	{
		Iterator columnHintIter = dataSetHandle.columnHintsIterator( );

		if ( columnHintIter != null )
		{
			while ( columnHintIter.hasNext( ) )
			{
				ColumnHintHandle modelColumnHint = (ColumnHintHandle) columnHintIter.next( );
				if ( modelColumnHint.getColumnName( ).equals( columnName ) )
					return modelColumnHint;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param ds
	 * @param column
	 */
	private static void updateModelColumn( DataSetHandle ds, ResultSetColumnDefinition columnDef )
	{
		PropertyHandle resultSetColumns = ds.getPropertyHandle( DataSetHandle.RESULT_SET_HINTS_PROP );
		if ( resultSetColumns == null )
			return;
		// update result set columns
		Iterator iterator = resultSetColumns.iterator( );
		if( iterator == null )
			return;
		boolean found = false;
		while ( iterator.hasNext( ) )
		{
			ResultSetColumnHandle rsColumnHandle = (ResultSetColumnHandle) iterator.next( );
			assert rsColumnHandle.getPosition( ) != null;
			if ( rsColumnHandle.getPosition( ).intValue( ) == columnDef.getColumnPosition( ) )
			{
				if ( rsColumnHandle.getColumnName( ) != null
						&& !rsColumnHandle.getColumnName( )
								.equals( columnDef.getColumnName( ) ) )
				{
					rsColumnHandle.setColumnName( columnDef.getColumnName( ) );
				}
				found = true;
				break;
			}
		}
		if ( found == false )
		{
			addResultSetColumn( resultSetColumns, columnDef );
		}
	}

	/**
	 * @param resultSetColumnHandle
	 * @param column
	 */
	private static void addResultSetColumn( PropertyHandle resultSetColumnHandle,
			ResultSetColumnDefinition resultSetColumn )
	{
		ResultSetColumn rsColumn = new ResultSetColumn( );
		rsColumn.setColumnName( resultSetColumn.getColumnName( ) );
		rsColumn.setPosition( new Integer( resultSetColumn.getColumnPosition( ) ) );
		try
		{
			resultSetColumnHandle.addItem( rsColumn );
		}
		catch ( SemanticException e )
		{
		}
	}

}
