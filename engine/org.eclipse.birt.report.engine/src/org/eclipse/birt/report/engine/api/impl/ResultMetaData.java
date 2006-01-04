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

import java.util.HashMap;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;


public class ResultMetaData implements IResultMetaData
{
	protected IResultMetaData resultMeta;
	protected String[] selectedColumns;
	protected HashMap columnsMap;
	
	ResultMetaData( IResultMetaData metaData, String[] selectedColumns )
	{	
		assert selectedColumns != null;
		
		this.selectedColumns = selectedColumns;
		resultMeta = metaData;
		populateColumnsMap( );
	}
	
	private void populateColumnsMap( )
	{
		if( resultMeta == null )
			return ;
		
		for( int i=0; i<selectedColumns.length; i++)
		{
			for( int j=1; j<=resultMeta.getColumnCount(); j++)
			{
				try
				{
					String colName = resultMeta.getColumnName( j );
					if( colName.matches( "\"\\w+\"" ) )
					{
						colName = colName.replaceAll("\"", "\\\\\"");
					}
					
					String newColName =  "row[\"" + colName + "\"]";
					if( selectedColumns[i].equalsIgnoreCase( newColName ) )
					{
						if( columnsMap == null )
							columnsMap = new HashMap( );
						columnsMap.put( new Integer(i), new Integer(j));
						break;
					}
				}
				catch (BirtException be)
				{
					be.printStackTrace();
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
			return resultMeta.getColumnAlias( mappedIndex );
		return null;
	}

	public int getColumnType( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return resultMeta.getColumnType( mappedIndex );
		return DataType.UNKNOWN_TYPE;
	}

	public String getColumnTypeName( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return resultMeta.getColumnTypeName( mappedIndex );
		return null;
	}

	public String getColumnNativeTypeName( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return resultMeta.getColumnNativeTypeName( mappedIndex );
		return null;
	}

	public String getColumnLabel( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return resultMeta.getColumnLabel( mappedIndex );
		return null;
	}

	public boolean isComputedColumn( int index ) throws BirtException
	{
		validateIndex( index );
		int mappedIndex = getMappedIndex( index );
		if( mappedIndex != -1)
			return resultMeta.isComputedColumn( mappedIndex );
		return false;
	}
}
