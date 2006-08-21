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
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

/**
 * Retrieve metaData from resultset property.
 *
 */
public class MetaDataPopulator
{
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
			for ( int i = 0; i < resultSetList.size( ); i++ )
			{

				resultSetColumn = (ResultSetColumn) resultSetList.get( i );

				columnDef = new ResultSetColumnDefinition( resultSetColumn.getColumnName( ) );

				columnDef.setDataTypeName( resultSetColumn.getDataType( ) );
				columnDef.setDataType( ModelAdapter.adaptModelDataType( resultSetColumn.getDataType( ) ) );
				columnDef.setColumnPosition( resultSetColumn.getPosition( )
						.intValue( ) );

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
}
