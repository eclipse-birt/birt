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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IConnectionFactory;
import org.eclipse.birt.data.oda.OdaException;

/**
 * ConnectionManager manages a set of data source connections.  Calling 
 * <code>getInstance</code> will return an instance of <code>ConnectionManager</code>.
 * When the method <code>getConnection</code> is called, the 
 * <code>ConnectionManager</code> will attempt to return an opened 
 * <code>Connection</code> supported by that driver.
 */
public class ConnectionManager
{
	private static ConnectionManager sm_instance = new ConnectionManager( );
	
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
		return sm_instance;
	}

	/**
	 * Returns an opened <code>Connection</code> that is supported by the specified 
	 * driver using the specified connection properties.
	 * @param driverName	name of the driver.
	 * @param connectionProperties	connection properties to open the underlying connection.
	 * @return	an opened <code>Connection</code> instance. 
	 * @throws DataException	if data source error occurs.
	 */
	public Connection openConnection( String driverName, 
									  Properties connectionProperties ) 
		throws DataException
	{
		try
		{
			IConnectionFactory factory = 
				DriverManager.getInstance().getConnectionFactory( driverName );
			String connectionName = 
				DriverManager.getInstance().getConnectionClassName( driverName );
			IConnection connection = factory.getConnection( connectionName );
			connection.open( connectionProperties );
			
			return ( new Connection( connection, driverName ) );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_OPEN_CONNECTION, ex, 
			                         new Object[] { driverName } );
		}
	}
}