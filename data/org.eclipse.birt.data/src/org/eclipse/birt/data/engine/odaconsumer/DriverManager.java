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
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;

/**
 * <code>DriverManager</code> manages a set of data source drivers.  Calling 
 * <code>getInstance</code> will return the singleton instance of <code>DriverManager</code>.
 * 
 * When the method <code>getDriverConnectionFactory</code> is initiated by the 
 * <code>ConnectionManager</code>, the <code>DriverManager</code> will 
 * attempt to load the specified driver and return a <code>IDriver</code> 
 * instance of that driver.
 */
class DriverManager
{
	private static DriverManager sm_driverManager = new DriverManager();
	private Hashtable m_loadedDrivers;
	
	// trace logging variables
	private static String sm_className = DriverManager.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );

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
	 * Returns the <code>IDriver</code> based on driverName.
	 * @param dataSourceElementId	the name of the driver.
	 * @return	an <code>IDriver</code> instance.
	 */
	IDriver getDriverHelper( String dataSourceElementId )
		throws DataException
	{
		String methodName = "getDriverHelper";
		sm_logger.entering( sm_className, methodName, dataSourceElementId );

		Driver driver = getDriver( dataSourceElementId );
		IDriver ret = driver.getDriverHelper();
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}

	/**
	 * Returns the id of the type of ODA data source for use as an argument to 
	 * <code>IDriver.getConnection</code>.
	 * @param dataSourceElementId	the id of the data source element defined
	 * 								in a data source extension.
	 * @return	the extension data source type id for <code>IDriver.getConnection</code>, 
	 * 			or null if no explicit data source type was specified.
	 */
	String getExtensionDataSourceId( String dataSourceElementId ) 
		throws DataException
	{
		String methodName = "getExtensionDataSourceId";
		sm_logger.entering( sm_className, methodName, dataSourceElementId );

		Driver driver = getDriver( dataSourceElementId );
		ExtensionManifest config = driver.getDriverExtensionConfig();
		String ret = config.getDataSourceElementID();
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	void setDriverLogConfiguration( String dataSourceElementId )
	{
		String methodName = "setDriverLogConfiguration";
		if( sm_logger.isLoggingEnterExitLevel() )
		    sm_logger.entering( sm_className, methodName, 
		        				new Object[] { dataSourceElementId } );

	    assert( dataSourceElementId != null );
	    
		Driver driver = getDriver( dataSourceElementId );
		assert( driver != null );
        driver.setLogConfiguration();

		sm_logger.exiting( sm_className, methodName );
	}
		
	private Driver getDriver( String dataSourceElementId )
	{
		assert( dataSourceElementId != null && dataSourceElementId.length() != 0 );
		
		Driver driver = (Driver) getLoadedDrivers().get( dataSourceElementId );
		if( driver == null )
		{
			driver = new Driver( dataSourceElementId );
			getLoadedDrivers().put( dataSourceElementId, driver );
		}
		
		return driver;
	}
	
	Hashtable getLoadedDrivers()
	{
		if( m_loadedDrivers == null )
			m_loadedDrivers = new Hashtable();
		
		return m_loadedDrivers;
	}
}
