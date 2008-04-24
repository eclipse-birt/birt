/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ResultSetIndex
{

	public ResultSetIndex( )
	{
	}

	private static class QueryResultSets
	{

		Map<String, ResultSets> results = new HashMap<String, ResultSets>( );

		void addResultSet( String parent, int row, String rset )
		{
			ResultSets rsets = results.get( parent );
			if ( rsets == null )
			{
				rsets = new ResultSets( );
				results.put( parent, rsets );
			}
			rsets.addResultSet( row, rset );
		}

		String getResultSet( String parent, int row )
		{
			ResultSets rsets = results.get( parent );
			if ( rsets == null )
			{
				return null;
			}
			return rsets.getResultSet( row );
		}
	}

	private static class ResultSets
	{

		static Comparator<ResultSetEntry> comparator = new Comparator<ResultSetEntry>( ) {

			@Override
			public int compare( ResultSetEntry e1, ResultSetEntry e2 )
			{
				if ( e1.row == e2.row )
				{
					return 0;
				}
				if ( e1.row < e2.row )
				{
					return -1;
				}
				return 1;
			}
		};
		ResultSetEntry[] entries;
		Collection<ResultSetEntry> rsets = new ArrayList<ResultSetEntry>( );

		void addResultSet( int rowId, String rset )
		{
			if ( entries != null )
			{
				throw new IllegalStateException( );
			}
			rsets.add( new ResultSetEntry( rowId, rset ) );
		}

		String getResultSet( int rowId )
		{
			if ( entries == null )
			{
				entries = rsets.toArray( new ResultSetEntry[rsets.size( )] );
				Arrays.sort( entries, comparator );
			}
			int index = Arrays.binarySearch( entries, new ResultSetEntry(
					rowId, "" ), comparator );
			if ( index < 0 )
			{
				index = -( index + 1 ) - 1;
			}
			if ( index >= 0 && index < entries.length )
			{
				return entries[index].rset;
			}
			return null;
		}
	}

	private static class ResultSetEntry
	{

		int row;
		String rset;

		ResultSetEntry( int row, String rset )
		{
			this.row = row;
			this.rset = rset;
		}
	}

	private Map<String, QueryResultSets> queries = new HashMap<String, QueryResultSets>( );

	public void addResultSet( String query, String parent, int row, String rset )
	{
		QueryResultSets rsets = queries.get( query );
		if ( rsets == null )
		{
			rsets = new QueryResultSets( );
			queries.put( query, rsets );
		}
		rsets.addResultSet( parent, row, rset );
	}

	public String getResultSet( String query, String parent, int row )
	{
		QueryResultSets rsets = queries.get( query );
		if ( rsets != null )
		{
			String rset = rsets.getResultSet( parent, row );
			if ( rset == null )
			{
				if ( parent != null )
				{
					int charAt = parent.indexOf( "_" );
					if ( charAt != -1 )
					{
						String root = parent.substring( 0, charAt );
						return rsets.getResultSet( root, row );
					}
				}
			}
			return rset;
		}
		return null;
	}
}
