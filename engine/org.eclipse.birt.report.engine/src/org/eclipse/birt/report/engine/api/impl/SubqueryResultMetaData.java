/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
import java.util.HashSet;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class SubqueryResultMetaData implements IResultMetaData
{

	ArrayList<MetaData> metas;

	public SubqueryResultMetaData( ISubqueryDefinition subquery, HashMap map )
			throws BirtException
	{
		metas = new ArrayList<MetaData>( );
		HashSet<String> names = new HashSet<String>( );
		IBaseQueryDefinition tmpQuery = subquery;
		while ( tmpQuery instanceof ISubqueryDefinition )
		{
			ResultMetaData metaData = (ResultMetaData) map.get( tmpQuery );
			int columnCount = metaData.getColumnCount( );
			for ( int index = 0; index < columnCount; index++ )
			{
				String columnName = metaData.getColumnName( index );
				if ( !names.contains( columnName ) )
				{
					MetaData meta = new MetaData( );
					meta.columnName = columnName;
					meta.columnAlias = metaData.getColumnAlias( index );
					meta.columnLabel = metaData.getColumnLabel( index );
					meta.columnType = metaData.getColumnType( index );
					meta.columnTypeName = metaData.getColumnTypeName( index );

					metas.add( meta );
					names.add( columnName );
				}
			}
			tmpQuery = tmpQuery.getParentQuery( );
		}
		// tmpQuery is a QueryDefinition now
		ResultMetaData metaData = (ResultMetaData) map.get( tmpQuery );
		int columnCount = metaData.getColumnCount( );
		for ( int index = 0; index < columnCount; index++ )
		{
			String columnName = metaData.getColumnName( index );
			if ( !names.contains( columnName ) )
			{
				MetaData meta = new MetaData( );
				meta.columnName = columnName;
				meta.columnAlias = metaData.getColumnAlias( index );
				meta.columnLabel = metaData.getColumnLabel( index );
				meta.columnType = metaData.getColumnType( index );
				meta.columnTypeName = metaData.getColumnTypeName( index );

				metas.add( meta );
				names.add( columnName );
			}
		}
	}

	public String getColumnAlias( int index ) throws BirtException
	{
		return metas.get( index ).columnAlias;
	}

	public int getColumnCount( )
	{
		return metas.size( );
	}

	public String getColumnLabel( int index ) throws BirtException
	{
		return metas.get( index ).columnLabel;
	}

	public String getColumnName( int index ) throws BirtException
	{
		return metas.get( index ).columnName;
	}

	public int getColumnType( int index ) throws BirtException
	{
		return metas.get( index ).columnType;
	}

	public String getColumnTypeName( int index ) throws BirtException
	{
		return metas.get( index ).columnTypeName;
	}

	private class MetaData
	{

		String columnName;
		String columnAlias;
		String columnLabel;
		int columnType;
		String columnTypeName;
	}
}
