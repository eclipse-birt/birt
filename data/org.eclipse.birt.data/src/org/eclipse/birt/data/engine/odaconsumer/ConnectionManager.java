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

import java.util.Properties;
import java.util.logging.Level;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * ConnectionManager manages a set of data source connections.  Calling 
 * <code>getInstance</code> will return an instance of <code>ConnectionManager</code>.
 * When the method <code>getConnection</code> is called, the 
 * <code>ConnectionManager</code> will attempt to return an opened 
 * <code>Connection</code> instance of the data source extension 
 * supported by that driver.
 */
public class ConnectionManager
{
	private static ConnectionManager sm_instance = new ConnectionManager( );
	
    // trace logging variables
	private static String sm_className = ConnectionManager.class.getName();
	static String sm_packageName = "org.eclipse.birt.data.engine.odaconsumer";
	private static String sm_loggerName = sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );
	
	private ConnectionManager( )
	{
	}
	
	/**
	 * Returns a <code>ConnectionManager</code> instance for getting opened 
	 * <code>Connections</code>.
	 * @return	a <code>ConnectionManager</code> instance.
	 * @throws IllegalStateException	if the <code>ConnectionManager</code> subclass 
	 * 									specified in the properties file cannot be found.
	 */
	public static ConnectionManager getInstance( ) throws IllegalStateException
	{
		String methodName = "getInstance";		
		sm_logger.entering( sm_className, methodName );
		sm_logger.exiting( sm_className, methodName, sm_instance );
		
		return sm_instance;
	}

	/**
	 * Returns an opened <code>Connection</code> that is supported by the specified 
	 * data source extension using the specified connection properties.
	 * @param dataSourceElementId	id of the data source element defined
	 * 								in the data source extension.
	 * @param connectionProperties	connection properties to open the underlying connection.
	 * @return	an opened <code>Connection</code> instance. 
	 * @throws DataException	if data source error occurs.
	 */
	public Connection openConnection( String dataSourceElementId, 
									  Properties connectionProperties )
		throws DataException
	{
		String methodName = "openConnection";
		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { dataSourceElementId, connectionProperties } );
		
		try
		{
            DriverManager driverMgr = DriverManager.getInstance();

            // passes ODA framework's trace logging configuration settings 
            // to the ODA consumer helper and driver itself
            driverMgr.setDriverLogConfiguration( dataSourceElementId );

            // gets the driver's connection to open
            IDriver driverHelper = 
                driverMgr.getDriverHelper( dataSourceElementId );
			String dataSourceId = 
                driverMgr.getExtensionDataSourceId( dataSourceElementId );          
			IConnection connection = driverHelper.getConnection( dataSourceId );
			connection.open( connectionProperties );
			
			Connection ret = ( new Connection( connection, dataSourceElementId ) );
			
			sm_logger.exiting( sm_className, methodName, ret );	
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot open connection.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_OPEN_CONNECTION, ex, 
			                         new Object[] { dataSourceElementId } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot open connection.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_OPEN_CONNECTION, ex, 
			                         new Object[] { dataSourceElementId } );
		}
	}

	/**
	 * Returns the maximum number of active connections that the driver can support.
	 * @return	the maximum number of connections that can be opened concurrently, 
	 * 			or 0 if there is no limit or the limit is unknown.
	 * @throws DataException	if data source error occurs.
	 */
	public int getMaxConnections( String driverName ) throws DataException
	{
		String methodName = "getMaxConnections";
		sm_logger.entering( sm_className, methodName, driverName );

		int maxConnections = 0;  	// default to unknown limit
		try
		{
			IDriver driverHelper = 
				DriverManager.getInstance().getDriverHelper( driverName );
			if ( driverHelper != null )
			    maxConnections = driverHelper.getMaxConnections();
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"Cannot get max connections.", ex );
			maxConnections = 0;
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.INFO, sm_className, methodName, 
							"Cannot get max connections.", ex );
			maxConnections = 0;
		}

		sm_logger.exiting( sm_className, methodName, maxConnections );			
		return maxConnections;
	}

}