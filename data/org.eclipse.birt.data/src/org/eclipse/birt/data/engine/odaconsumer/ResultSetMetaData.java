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

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
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
	 * @throws OdaException	if data source error occurs.
	 */
	public int getColumnCount( ) throws OdaException
	{
		return m_metadata.getColumnCount( );
	}
	
	/**
	 * Returns the column name at the specified column index.
	 * @param index	the column index.
	 * @return		the column name at the specified column index.
	 * @throws OdaException	if data source error occurs.
	 */
	public String getColumnName( int index ) throws OdaException
	{
		return m_metadata.getColumnName( index );
	}
	
	/**
	 * Returns the column label at the specified column index.
	 * @param index	the column index.
	 * @return		the column label at the specified column index.
	 * @throws OdaException	if data source error occurs.
	 */
	public String getColumnLabel( int index ) throws OdaException
	{
		return m_metadata.getColumnLabel( index );
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type at the specified column index.
	 * @param index	the column index.
	 * @return		the <code>java.sql.Types</code> type at the specified column index.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getColumnType( int index ) throws OdaException
	{
		int nativeType = m_metadata.getColumnType( index );
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
	
	Class getColumnTypeAsJavaClass( int index ) throws OdaException
	{
		int odaType = getColumnType( index );
		Class fieldClass = null;
		switch( odaType )
		{
			case Types.INTEGER:
				fieldClass = Integer.class;
				break;
			
			case Types.DOUBLE:
				fieldClass = Double.class;
				break;
				
			case Types.CHAR:
				fieldClass = String.class;
				break;
				
			case Types.DECIMAL:
				fieldClass = BigDecimal.class;
				break;
				
			case Types.DATE:
				fieldClass = Date.class;
				break;
				
			case Types.TIME:
				fieldClass = Time.class;
				break;

			case Types.TIMESTAMP:
				fieldClass = Timestamp.class;
				break;
		}
		
		return fieldClass;
	}
}
