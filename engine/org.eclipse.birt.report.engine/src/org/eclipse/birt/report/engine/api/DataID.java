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

/**
 * the data id of the data used by an instance.
 */
public class DataID
{

	/**
	 * data set id.
	 */
	protected DataSetID dataSet;
	/**
	 * id of the row.
	 */
	protected long rowId;
	
	protected String cellId;

	/**
	 * creat the new data id instance.
	 * 
	 * @param dataSet
	 *            data set
	 * @param rowId
	 *            row id
	 */
	public DataID( DataSetID dataSet, long rowId )
	{
		this.dataSet = dataSet;
		this.rowId = rowId;
	}

	public DataID( DataSetID dataSet, String cellId )
	{
		this.dataSet = dataSet;
		this.cellId = cellId;
	}

	/**
	 * return the data set.
	 * 
	 * @return
	 */
	public DataSetID getDataSetID( )
	{
		return dataSet;
	}

	/**
	 * return the row id
	 * 
	 * @return
	 */
	public long getRowID( )
	{
		return rowId;
	}
	
	public String getCellID()
	{
		return cellId;
	}

	/**
	 * add the instance id to the string buffer.
	 * 
	 * It is a util class used by other internal packages.
	 * 
	 * @param buffer
	 */
	public void append( StringBuffer buffer )
	{
		if ( dataSet != null )
		{
			dataSet.append( buffer );
		}
		buffer.append( ":" );
		buffer.append( rowId );
	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );
		append( buffer );
		return buffer.toString( );
	}

	/**
	 * create a new data id instance from the string.
	 * 
	 * @param dataId
	 *            string represetantion of the data id
	 * @return data id instance.
	 */
	static DataID parse( String dataId )
	{
		return parse( dataId.toCharArray( ), 0, dataId.length( ) );
	}

	static DataID parse( char[] buffer, int offset, int length )
	{
		int ptr = offset + length - 1;
		while ( ptr >= offset && buffer[ptr] != ':' )
		{
			ptr--;
		}
		if ( ptr >= offset && buffer[ptr] == ':' )
		{
			// we found the row Id
			String strRowId = new String( buffer, ptr + 1, offset + length
					- ptr - 1 );
			ptr--; // skip the current ':'
			if ( ptr >= offset )
			{
				DataSetID dataSetId = DataSetID.parse( buffer, offset, ptr
						- offset + 1 );
				if ( dataSetId != null )
				{
					try
					{
						long rowId = Long.parseLong( strRowId );
						return new DataID( dataSetId, rowId );
					}
					catch ( Exception ex )
					{
						
					}
					return new DataID( dataSetId, strRowId );
				}
			}
		}
		return null;
	}
}
