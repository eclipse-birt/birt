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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;

public class ResultMetaData implements IResultMetaData
{

	protected IResultMetaData metaData;
	protected String[] selectedColumns;

	ResultMetaData( IResultMetaData metaData, String[] selectedColumns )
	{
		this.metaData = metaData;
		this.selectedColumns = selectedColumns;
	}

	ResultMetaData( IResultMetaData metaData )
	{
		this.metaData = metaData;
		this.selectedColumns = null;
	}

	public int getColumnCount( )
	{
		if ( selectedColumns != null )
		{
			return selectedColumns.length;
		}
		return metaData.getColumnCount( );
	}

	public String getColumnName( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnName( index  );
	}

	public String getColumnAlias( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnAlias( index );
	}

	public int getColumnType( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnType( index );
	}

	public String getColumnTypeName( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnTypeName( index );
	}

	public String getColumnNativeTypeName( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnNativeTypeName( index );
	}

	public String getColumnLabel( int index ) throws BirtException
	{
		index = getColumnIndex( index );
		return metaData.getColumnLabel( index );
	}

	public boolean isComputedColumn( int index ) throws BirtException
	{
		return false;
	}

	private int getColumnIndex( int index ) throws BirtException
	{
		if ( selectedColumns == null )
		{
			return index + 1;
		}
		String name = selectedColumns[index];
		for ( int i = 0; i < metaData.getColumnCount( ); i++ )
		{
			if ( name.equals( metaData.getColumnName( i ) ) )
			{
				return i + 1;
			}
		}
		return -1;
	}
}
