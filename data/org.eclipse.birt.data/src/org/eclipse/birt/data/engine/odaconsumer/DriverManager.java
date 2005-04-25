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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.oda.IConnectionFactory;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.driverconfig.ConnectionType;
import org.eclipse.birt.data.oda.util.driverconfig.OdaDriverConfiguration;
import org.eclipse.birt.data.oda.util.driverconfig.TraceLogging;

/**
 * <code>DriverManager</code> manages a set of data source drivers.  Calling 
 * <code>getInstance</code> will return an instance of <code>DriverManager</code>.
 * 
 * When the method <code>getConnectionFactory</code> is called by the 
 * <code>ConnectionManager</code>, the <code>DriverManager</code> will 
 * attempt to load the specified driver and return a <code>ConnectionFactory</code> 
 * supported by that driver.
 */
class DriverManager
{
	private static DriverManager sm_driverManager = new DriverManager();
	private Hashtable m_loadedDrivers;
	
	private DriverManager()
	{
	}
	
	/**
	 * Returns a <code>DriverManager</code> instance for loading drivers and 
	 * handling driver-related tasks.
	 * @return	a <code>DriverManager</code> instance.
	 * @throws IllegalStateException	if the <code>DriverManager</code> subclass 
	 * 									specified in the properties file cannot be found.
	 */
	static DriverManager getInstance() throws IllegalStateException
	{
		return sm_driverManager;
	}
	
	/**
	 * Returns the <code>IConnectionFactory</code> based on driverName.
	 * @param driverName	the name of the driver.
	 * @return	an <code>IConnectionFactory</code> instance.
	 */
	IConnectionFactory getConnectionFactory( String driverName )
		throws DataException
	{
		Driver driver = getDriver( driverName );
		return driver.getConnectionFactory();
	}

	/**
	 * Returns the connection class name to use as an argument for 
	 * <code>IConnectionFactory.getConnection</code>.
	 * @param driverName	the name of the driver.
	 * @return	the connection class name for <code>IConnectionFactory.getConnection</code>, 
	 * 			or null if no connection type or class name was specified.
	 */
	String getConnectionClassName( String driverName ) 
		throws DataException
	{
		Driver driver = getDriver( driverName );
		OdaDriverConfiguration config = driver.getDriverConfig();
		ConnectionType connectionType = config.getConnectionType();
		return ( connectionType == null ) ? null : connectionType.getClassName();
	}
	
	void setDriverLogConfiguration( String driverName, 
	        						IConnectionFactory connFactory )
	{
	    assert( driverName != null && connFactory != null );
	    
		Driver driver = getDriver( driverName );
		assert( driver != null );
        driver.setLogConfiguration( connFactory );
	}
	
	/**
	 * Returns the default ODA type code mapped to the native type code for the 
	 * specified driver and data set type.
	 * @param driverName	the name of the driver.
	 * @param dataSetType	the type of the data set.
	 * @param nativeType	the native type code.
	 * @return	the ODA type code.
	 */
	int getNativeToOdaMapping( String driverName, 
							   String dataSetType, 
							   int nativeType ) throws DataException
	{
		Driver driver = getDriver( driverName );
		return driver.getTypeMapping( dataSetType, nativeType );
	}
	
	private Driver getDriver( String driverName )
	{
		assert( driverName != null && driverName.length() != 0 );
		
		Driver driver = (Driver) getLoadedDrivers().get( driverName );
		if( driver == null )
		{
			driver = new Driver( driverName );
			getLoadedDrivers().put( driverName, driver );
		}
		
		return driver;
	}
	
	private Hashtable getLoadedDrivers()
	{
		if( m_loadedDrivers == null )
			m_loadedDrivers = new Hashtable();
		
		return m_loadedDrivers;
	}
}
