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

import java.util.Hashtable;
import java.util.logging.Level;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.PropertySecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A runtime connection of a specific data source extension.
 */
public class Connection
{	
	private String m_dataSourceId;
	private IConnection m_connection;
	private Hashtable m_cachedDsMetaData;
	
    // trace logging variables
	private static String sm_className = Connection.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );
	
	Connection( IConnection connection, String dataSourceId ) 
		throws OdaException
	{
		String methodName = "Connection";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { connection, dataSourceId } );
		
		assert( connection != null && connection.isOpen( ) );
		m_dataSourceId = dataSourceId;
		m_connection = connection;
		
		sm_logger.exiting( sm_className, methodName, this );
	}
		
	/**
	 * Returns the maximum number of active queries of any data set types 
	 * that the driver can support per active connection.
	 * @return	the maximum number of any type of queries that can be supported 
	 * 			concurrently, or 0 if there is no limit or the limit is unknown.
	 * @throws DataException	if data source error occurs.
	 */
	public int getMaxQueries() throws DataException
	{
		String methodName = "getMaxQueries";		
		sm_logger.entering( sm_className, methodName );
				
		try
		{
			int ret = m_connection.getMaxQueries();
			
			sm_logger.exiting( sm_className, methodName, ret );	
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get max queries.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_MAX_QUERIES, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
							"Cannot get max queries.", ex );
			return 0;
		}
	}
	
	/**
	 * Returns the <code>DataSetCapabilities</code> based on the data set type.
	 * @param dataSetType	name of the data set type.
	 * @return	the <code>DataSetCapabilities</code> instance reflecting the specified 
	 * 			data set type.
	 * @throws DataException	if data source error occurs.
	 */
	public DataSetCapabilities getMetaData( String dataSetType ) throws DataException
	{
		String methodName = "getMetaData";		
		sm_logger.entering( sm_className, methodName, dataSetType );
		
		String cachedKey = ( dataSetType == null ) ?
		        			getDataSourceId( ) : dataSetType;
		
		DataSetCapabilities capabilities = 
			(DataSetCapabilities) getCachedDsMetaData( ).get( cachedKey );
		
		if( capabilities == null )
		{
			IDataSetMetaData dsMetaData = null;
			try
			{
				dsMetaData = m_connection.getMetaData( dataSetType );
			}
			catch( OdaException ex )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot get data set metadata.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_GET_DS_METADATA, ex, 
				                         new Object[] { dataSetType } );
			}
			catch( UnsupportedOperationException ex )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot get data set metadata.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_GET_DS_METADATA, ex, 
				                         new Object[] { dataSetType } );
			}
		
			capabilities = new DataSetCapabilities( dsMetaData );
			getCachedDsMetaData( ).put( cachedKey, capabilities );
		}
		
		sm_logger.exiting( sm_className, methodName, capabilities );
		
		return capabilities;
	}

	/**
	 * Creates a <code>PreparedStatement</code> object that needs to specify input 
	 * parameter values to execute.
	 * @param dataSetType	name of the data set type.
	 * @param query	the statement query text to be executed.
	 * @return	a <code>PreparedStatement</code> of the specified type with the specified 
	 * 			statement query.
	 * @throws DataException	if data source error occurs.
	 */
	public PreparedStatement prepareStatement( String query, 
											   String dataSetType )
		throws DataException
	{
		String methodName = "prepareStatement";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { query, dataSetType } );
		
		IQuery statement = prepareOdaQuery( query, dataSetType );
		PreparedStatement ret = ( new PreparedStatement( statement, dataSetType, this, 
														 query ) );
		
		sm_logger.exiting( sm_className, methodName, ret );		
		return ret;
	}
	
	/**
	 * Closes this <code>Connection</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public void close( ) throws DataException
	{
		String methodName = "close";		
		sm_logger.entering( sm_className, methodName );
		
		try
		{
			m_connection.close( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot close connection.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_CLOSE_CONNECTION, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"Cannot close connection.", ex );			
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	// cache the metadata since it's the same for the lifetime of this connection, 
	// and we'll lazily instantiate it since it may not be needed
	private Hashtable getCachedDsMetaData( )
	{
		if( m_cachedDsMetaData == null )
			m_cachedDsMetaData = PropertySecurity.createHashtable( );
		
		return m_cachedDsMetaData;
	}
	
	String getDataSourceId( )
	{
		return m_dataSourceId;
	}
	
	IQuery prepareOdaQuery( String query, String dataSetType ) 
		throws DataException
	{
		String methodName = "prepareOdaQuery";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, new Object[] { query, dataSetType } );
		
		try
		{
			assert( m_connection.isOpen( ) );
			IQuery statement = m_connection.newQuery( dataSetType );
			statement.prepare( query );
			
			sm_logger.exiting( sm_className, methodName, statement );
			
			return statement;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot prepare statement.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_PREPARE_STATEMENT, ex, 
			                         new Object[] { query, dataSetType } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot prepare statement.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_PREPARE_STATEMENT, ex, 
			                         new Object[] { query, dataSetType } );
		}
	}
}
