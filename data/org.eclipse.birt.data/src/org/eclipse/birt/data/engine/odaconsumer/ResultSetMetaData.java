/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.OdaException;

/**
 * ResultSetMetaData contains the result set metadata retrieved during 
 * runtime.
 */
public class ResultSetMetaData
{
	private IResultSetMetaData m_metadata;
	private String m_driverName;
	private String m_dataSetType;
	
	ResultSetMetaData( IResultSetMetaData metadata, String driverName,
					   String dataSetType )
	{
		m_metadata = metadata;
		m_driverName = driverName;
		m_dataSetType = dataSetType;
	}
	
	/**
	 * Returns the number of columns in the corresponding result set.
	 * @return	the number of columns in the result set.
	 * @throws DataException	if data source error occurs.
	 */
	public int getColumnCount( ) throws DataException
	{
		try
		{
			return m_metadata.getColumnCount( );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_COUNT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_COUNT, ex );
		}
	}
	
	/**
	 * Returns the column name at the specified column index.
	 * @param index	the column index.
	 * @return		the column name at the specified column index.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnName( int index ) throws DataException
	{
		try
		{
			return m_metadata.getColumnName( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NAME, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NAME, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	/**
	 * Returns the column label at the specified column index.
	 * @param index	the column index.
	 * @return		the column label at the specified column index.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnLabel( int index ) throws DataException
	{
		try
		{
			return m_metadata.getColumnLabel( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_LABEL, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_LABEL, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type at the specified column index.
	 * @param index	the column index.
	 * @return		the <code>java.sql.Types</code> type at the specified column index.
	 * @throws DataException	if data source error occurs.
	 */
	public int getColumnType( int index ) throws DataException
	{
		int nativeType = doGetColumnType( index );
		int odaType = 
			DriverManager.getInstance().getNativeToOdaMapping( m_driverName, 
															   m_dataSetType,
															   nativeType );
		if( odaType != Types.INTEGER &&
			odaType != Types.DOUBLE &&
			odaType != Types.CHAR &&
			odaType != Types.DECIMAL &&
			odaType != Types.DATE &&
			odaType != Types.TIME &&
			odaType != Types.TIMESTAMP )
			assert false;	// exception is now thrown by DriverManager
		
		return odaType;
	}
	
	/**
	 * Returns the native type name at the specified column index.
	 * @param index	the column index.
	 * @return		the native type name.
	 * @throws DataException	if data source error occurs.
	 */
	public String getColumnNativeTypeName( int index ) throws DataException
	{
		try
		{
			return m_metadata.getColumnTypeName( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NATIVE_TYPE_NAME, ex, 
									 new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_NATIVE_TYPE_NAME, ex, 
									 new Object[] { new Integer( index ) } );
		}
	}

	private int doGetColumnType( int index ) throws DataException
	{
		try
		{
			return m_metadata.getColumnType( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_TYPE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_COLUMN_TYPE, ex, 
			                         new Object[] { new Integer( index ) } );			
		}
	}
	
	Class getColumnTypeAsJavaClass( int index ) throws DataException
	{
		int odaType = getColumnType( index );
		return DataTypeUtil.toTypeClass( odaType );
	}
}
