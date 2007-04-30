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

package org.eclipse.birt.report.engine.api;

public class DataSetID
{

	DataSetID parent;
	long rowId;
	String queryName;

	String dataSetName;

	public DataSetID( DataSetID parent, long rowId, String queryName )
	{
		this.parent = parent;
		this.rowId = rowId;
		this.queryName = queryName;
	}
	public DataSetID getParentID( )
	{
		return parent;
	}
	public String getDataSetName( )
	{
		return dataSetName;
	}
	public String getQueryName( )
	{
		return queryName;
	}
	public long getRowID( )
	{
		return rowId;
	}
	public DataSetID( String dataSetName )
	{
		this.dataSetName = dataSetName;
	}

	public boolean equals( Object a )
	{
		if ( a instanceof DataSetID )
		{
			DataSetID aid = (DataSetID) a;
			if ( dataSetName != null )
			{
				return dataSetName.equals( aid.dataSetName );
			}
			if ( rowId == aid.rowId && queryName.equals( aid.queryName ) )
			{
				return parent.equals( aid.parent );
			}
		}
		return false;
	}

	void append( StringBuffer buffer )
	{
		if ( parent != null )
		{
			buffer.append( "{" );
			parent.append( buffer );
			buffer.append( "}." );
			buffer.append( rowId );
			buffer.append( "." );
			buffer.append( queryName );
		}
		else
		{
			buffer.append( dataSetName );
		}
	}

	public String toString( )
	{
		if ( dataSetName != null )
		{
			return dataSetName;
		}
		StringBuffer buffer = new StringBuffer( );
		append( buffer );
		return buffer.toString( );
	}

	static public DataSetID parse( String dataSetId )
	{
		return parse( dataSetId.toCharArray( ), 0, dataSetId.length( ) );
	}

	static public DataSetID parse( char[] buffer, int offset, int length )
	{
		int ptr = offset + length - 1;

		// the data ID is looks like:
		// { dataSet } . rowId . groupName or dataSet

		// search the last '.' to see if it is the simplest dataSetName
		while ( ptr >= offset && buffer[ptr] != '.' )
		{
			ptr--;
		}
		if ( ptr >= offset && buffer[ptr] == '.' )
		{
			// it is complex one: { dataSet } . rowId . groupName
			// get the group name first
			String queryName = new String( buffer, ptr + 1, offset + length
					- ptr - 1 );
			ptr--; // skip the current '.'
			length = ptr - offset + 1;
			// find the next '.' to get the rowId
			while ( ptr >= offset && buffer[ptr] != '.' )
			{
				ptr--;
			}
			if ( ptr >= offset && buffer[ptr] == '.' )
			{
				// get the rowId
				String strRowId = new String( buffer, ptr + 1, offset + length
						- ptr - 1 );
				long rowId = Long.parseLong( strRowId );
				ptr--; // skip the current '.'
				if ( ptr >= offset && buffer[ptr] == '}'
						&& buffer[offset] == '{' )
				{
					// skip the '{' and '}' to get the parent Id.
					ptr--;
					offset++;
					if ( ptr >= offset )
					{
						DataSetID parent = parse( buffer, offset, ptr - offset
								+ 1 );
						if ( parent != null )
						{
							return new DataSetID( parent, rowId, queryName );
						}
					}
				}
			}
		}
		return new DataSetID( new String( buffer, offset, length ) );
	}
}
