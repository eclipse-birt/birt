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

import java.util.Hashtable;
import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IConnectionMetaData;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.IStatement;
import org.eclipse.birt.data.oda.OdaException;

/**
 * A connection with a specific data source.
 */
public class Connection
{	
	private String m_driverName;
	private IConnection m_connection;
	private IConnectionMetaData m_cachedConnMetaData;
	private Hashtable m_cachedDsMetaData;
	
	Connection( IConnection connection, String driverName ) 
		throws OdaException
	{
		assert( connection != null && connection.isOpened( ) );
		m_driverName = driverName;
		m_connection = connection;
	}
	
	/**
	 * Returns the maximum number of active connections that can supported.
	 * @return	the maximum number of connections that can be opened concurrently, 
	 * 			or 0 if there is no limit or the limit is unknown.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getMaxConnections() throws OdaException
	{
		IConnectionMetaData connMetaData = getCachedConnMetaData();
		return connMetaData.getMaxConnections( );
	}
	
	/**
	 * Returns the maximum number of active statements of any data set types 
	 * that the driver can support per active connection.
	 * @return	the maximum number of any type of statements that can be supported 
	 * 			concurrently, or 0 if there is no limit or the limit is unknown.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getMaxStatements() throws OdaException
	{
		IConnectionMetaData connMetaData = getCachedConnMetaData();
		return connMetaData.getMaxStatements();
	}
	
	/**
	 * Returns the <code>DataSetCapabilities</code> based on the data set type.
	 * @param dataSetType	name of the data set type.
	 * @return	the <code>DataSetCapabilities</code> instance reflecting the specified 
	 * 			data set type.
	 * @throws OdaException	if data source error occurs.
	 */
	public DataSetCapabilities getMetaData( String dataSetType ) throws OdaException
	{
		DataSetCapabilities capabilities = 
			(DataSetCapabilities) getCachedDsMetaData( ).get( dataSetType );
		
		if( capabilities == null )
		{
			IDataSetMetaData dsMetaData = m_connection.getMetaData( dataSetType );
			capabilities = new DataSetCapabilities( dsMetaData );
			getCachedDsMetaData( ).put( dataSetType, capabilities );
		}
		
		return capabilities;
	}

	/**
	 * Creates a <code>PreparedStatement</code> object that needs to specify input 
	 * parameter values to execute.
	 * 
	 * @param dataSetType	name of the data set type.
	 * @param query	the statement query to be executed.
	 * @return	a <code>PreparedStatement</code> of the specified type with the specified 
	 * 			statement query.
	 * @throws OdaException	if data source error occurs.
	 */
	public PreparedStatement prepareStatement( String query, 
											   String dataSetType )
		throws OdaException
	{
		IStatement statement = prepareOdaStatement( query, dataSetType );
		return ( new PreparedStatement( statement, dataSetType, this, 
		                                query ) );
	}
	
	/**
	 * Closes this <code>Connection</code>.
	 * @throws OdaException	if data source error occurs.
	 */
	public void close( ) throws OdaException
	{
		m_connection.close( );
	}
	
	private IConnectionMetaData getCachedConnMetaData() throws OdaException
	{
		if( m_cachedConnMetaData == null )
			m_cachedConnMetaData = m_connection.getMetaData();
		
		return m_cachedConnMetaData;
	}
	
	// cache the metadata since it's the same for the lifetime of this connection, 
	// and we'll lazily instantiate it since it may not be needed
	private Hashtable getCachedDsMetaData( )
	{
		if( m_cachedDsMetaData == null )
			m_cachedDsMetaData = new Hashtable( );
		
		return m_cachedDsMetaData;
	}
	
	String getDriverName( )
	{
		return m_driverName;
	}
	
	IStatement prepareOdaStatement( String query, String dataSetType ) 
		throws OdaException
	{
		assert( m_connection.isOpened( ) );
		IStatement statement = m_connection.createStatement( dataSetType );
		statement.prepare( query );
		return statement;
	}
}
