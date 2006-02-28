/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;


public class ResultMetaData implements IResultMetaData
{
	// protected IResultMetaData resultMeta;
	protected String[] selectedColumns;
	protected HashMap columnsMap;
	protected DataExtractionHelper helper;
	
	ResultMetaData ( DataExtractionHelper helper, String[] selectedColumns )
	{
		this.selectedColumns = selectedColumns;
		this.helper = helper;
		populateColumnsMap( );
	}
	
	private void populateColumnsMap( )
	{
		if( helper == null )
			return ;
		if ( this.selectedColumns == null )
		{
			ArrayList aColMeta = helper.validatedColMetas;
			this.selectedColumns = new String[aColMeta.size( )];
			for( int i=0; i<aColMeta.size( ); i++ )
			{
				ExprMetaData meta = (ExprMetaData)aColMeta.get( i );
				this.selectedColumns[i] = meta.getName( );
			}
		}
		
		ArrayList validateColsMeta = helper.validatedColMetas;
		for( int i=0; i<selectedColumns.length; i++)
		{
			for( int j=0; j<validateColsMeta.size( ); j++)
			{
				ExprMetaData meta = (ExprMetaData) validateColsMeta.get( j );
				String colName = meta.getName( );

				if ( selectedColumns[i].equalsIgnoreCase( colName ) )
				{
					if ( columnsMap == null )
						columnsMap = new HashMap( );
					columnsMap.put( new Integer( i ), new Integer( j ) );
					break;
				}
			}
		}
	}
	
	private int getMappedIndex( int index )
	{
		if( columnsMap != null )
		{
			Integer mappedIndex = (Integer)columnsMap.get(new Integer( index ));
			if( mappedIndex != null)
				return mappedIndex.intValue();
		}
		return -1;
	}
	
	private void validateIndex( int index )
	{
		assert index >=0 && index < selectedColumns.length; 
	}
	public int getColumnCount( )
	{
		return selectedColumns.length;
	}

	private ExprMetaData getMeta( int index )
	{
		return (ExprMetaData)helper.validatedColMetas.get( index );
	}
	public String getColumnName( int index ) throws BirtException
	{
		validateIndex( index );
		return selectedColumns[index];
	}

	public String getColumnAlias( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta(mappedIndex).metaAlias;
		return null;
	}

	public int getColumnType( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta(mappedIndex).metaType;
		return DataType.UNKNOWN_TYPE;
	}

	public String getColumnTypeName( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta(mappedIndex).metaTypeName;
		return null;
	}

	public String getColumnNativeTypeName( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta( mappedIndex ).metaNativeTypeName;
		return null;
	}

	public String getColumnLabel( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta( mappedIndex ).metaLabel;
		return null;
	}

	public boolean isComputedColumn( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return getMeta( mappedIndex ).isMetaComputedColumn;
		return false;
	}
}
