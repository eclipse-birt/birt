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
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.OdaException;

/**
 * <code>ResultSet</code> maintains an incremental pointer to rows in the  
 * result set.
 */
public class ResultSet
{
	private IResultSet m_resultSet;
	private IResultClass m_resultClass;		// cached result class
	
	ResultSet( IResultSet resultSet, IResultClass resultClass )
	{
		assert( resultSet != null && resultClass != null );
		m_resultSet = resultSet;
		m_resultClass = resultClass;
	}
	
	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the 
	 * result set for this <code>ResultSet</code>.
	 * @return	this <code>ResultSet</code>'s metadata
	 * @throws OdaException	if data source error occurs.
	 */
	public IResultClass getMetaData() throws OdaException
	{
		return m_resultClass;
	}
	
	/**
	 * Specifies the maximum number of <code>IResultObjects</code> that can be 
	 * fetched from this <code>ResultSet</code>.
	 * @param max	the maximum number of <code>IResultObjects</code> that can be 
	 *				fetched; 0 means no limit.
	 * @throws OdaException	if data source error occurs.
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		m_resultSet.setMaxRows( max );
	}
	
	/**
	 * Returns the IResultObject representing the next row in the result set.
	 * @return 	the IResultObject representing the next row; null if there are 
	 * 			no more rows available or if max rows limit has been reached.
	 * @throws OdaException	if data source error occurs.
	 */
	public IResultObject fetch( ) throws OdaException
	{
		if( ! m_resultSet.next( ) )
			return null;

		int columnCount = m_resultClass.getFieldCount();
		int[] driverPositions = 
			( (ResultClass) m_resultClass ).getFieldDriverPositions();
		assert( columnCount == driverPositions.length );
		
		Object[] fields = new Object[ columnCount ];
		
		for( int i = 1; i <= columnCount; i++ )
		{
			try
			{
				if ( m_resultClass.isCustomField( i ) == true )
					continue;
			}
			catch ( DataException e )
			{
				throw new OdaException( e.getMessage() );
			}
			
			Class dataType = null;
			try
			{
				dataType = m_resultClass.getFieldValueClass( i );
			}
			catch( DataException ex )
			{
				// ignore since this means we passed in a wrong index 
				// that we were not suppose to 
				assert false;
			}
			
			int driverPosition = driverPositions[i - 1];
			Object colValue = null;
			
			if( dataType == Integer.class )
			{
				int j = m_resultSet.getInt( driverPosition );
				if( ! m_resultSet.wasNull() )
					colValue = new Integer( j ); 
			}
			else if( dataType == Double.class )
			{
				double d = m_resultSet.getDouble( driverPosition );
				if( ! m_resultSet.wasNull() )
					colValue = new Double( d );
			}
			else if( dataType == String.class )
				colValue = m_resultSet.getString( driverPosition );
			else if( dataType == BigDecimal.class )
				colValue = m_resultSet.getBigDecimal( driverPosition );
			else if( dataType == Date.class )
				colValue = m_resultSet.getDate( driverPosition );
			else if( dataType == Time.class )
				colValue = m_resultSet.getTime( driverPosition );
			else if( dataType == Timestamp.class )
				colValue = m_resultSet.getTimestamp( driverPosition );
			else 
				assert false;
			
			if( m_resultSet.wasNull( ) )
				colValue = null;
			
			fields[i - 1] = colValue;
		}
		
		return new ResultObject( m_resultClass, fields );
	}

	/**
	 * Returns the current row's 1-based index position.
	 * @return	current row's 1-based index position.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getRowPosition( ) throws OdaException
	{
		return m_resultSet.getRow( );
	}
	
	/**
	 * Closes this <code>ResultSet</code>.
	 * @throws OdaException	if data source error occurs.
	 */
	public void close( ) throws OdaException
	{
		m_resultSet.close( );
	}
}
