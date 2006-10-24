/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.helper.OdaDriver;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Each <code>Driver</code> maintains the state of a driver in the drivers 
 * home directory.  
 */
class Driver
{
	private String m_dataSourceDriverId;
	private ExtensionManifest m_driverConfig;
	private ExtensionManifest m_adapterConfig;
    private IDriver m_driverHelper;

    private static final String BIRT_DATASOURCE_EXTENSION_POINT = "org.eclipse.birt.data.oda.dataSource";
    private static final String ODA_ADAPTER_PLUGIN_ID = "org.eclipse.birt.data.oda.adapter.dtp";
    
    // trace logging variables
	private static final String sm_className = Driver.class.getName();
	private static final String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );

	Driver( String dataSourceElementId )
	{
		final String methodName = "Driver";		
		sm_logger.entering( sm_className, methodName, dataSourceElementId );

		m_dataSourceDriverId = dataSourceElementId;

		sm_logger.exiting( sm_className, methodName, this );
	}

	/**
	 * Returns the manifest that should be passed to 
	 * the ODA consumer helper to handle, 
	 * either a DTP ODA driver or the DTP-to-BIRT adapter.
	 */ 
	ExtensionManifest getExtensionConfig() throws DataException
	{
		if ( m_adapterConfig != null )
	        return m_adapterConfig;

		// get DTP ODA driver manifest, or
		// do lazy initialization of member variable(s)
		ExtensionManifest driverManifest = getDriverExtensionConfig();
	    assert( driverManifest != null );	// otherwise, DataException should have been thrown
		
	    if ( m_adapterConfig != null )	// check if adapter is now initialized
	        return m_adapterConfig;

	    return driverManifest;	// manifest of a DTP ODA driver
	}
	
	/**
	 * Returns the manifest of a DTP ODA driver, or that of
	 * a BIRT ODA driver.
	 * @throws DataException
	 */	 
	ExtensionManifest getDriverExtensionConfig() throws DataException
	{
		if ( m_driverConfig != null )
	        return m_driverConfig;
		
		// do lazy initialization;
	    // find the driver extension config and initializes member variables
	    findDataSourceExtensionConfig();
	    
	    assert( m_driverConfig != null );	// otherwise, DataException should have been thrown
	    return m_driverConfig;
	}

	// gets the consumer manager helper for this driver
	IDriver getDriverHelper() throws DataException
	{	
		final String methodName = "getDriverHelper";
		if( m_driverHelper != null )
            return m_driverHelper;
        
		try
		{		
            m_driverHelper = new OdaDriver( getExtensionConfig() );
            return m_driverHelper;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
					"Cannot get ODA data source driver helper.", ex );
			throw new DataException( ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND, ex, 
                                     new Object[] { m_dataSourceDriverId } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
					"Cannot get ODA data source driver factory.", ex );
			throw new DataException( ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND, ex, 
                                     new Object[] { m_dataSourceDriverId } );
		}
	}

	/*
	 * Sets the member variable(s) with value of the DTP ODA driver manifest, or 
	 * the manifests of a BIRT ODA driver and its adapter
	 */
	private void findDataSourceExtensionConfig() throws DataException
	{
	    final String methodName = "findDataSourceExtensionConfig";

	    // reset member variables
	    m_driverConfig = null;
        m_adapterConfig = null;

	    // first, try finding extension for org.eclipse.datatools.connectivity.oda.dataSource 
        m_driverConfig = 
	        doGetDriverManifest( m_dataSourceDriverId, true /*useDtpExtPoint*/,
	                			 false /*throwsIfNotFound*/ );

	    if( m_driverConfig != null )	// found as an DTP ODA driver
	        return;		// done
	    
    	// next, try org.eclipse.birt.data.oda.dataSource
	    m_driverConfig = 
	        doGetDriverManifest( m_dataSourceDriverId, false /*useDtpExtPoint*/,
	                			 true /*throwsIfNotFound*/ );

	    assert( m_driverConfig != null );	// otherwise, DataException should have been thrown

	    // now get the DTP-BIRT ODA adapter's manifest
	    try
		{
	        m_adapterConfig = 
		        doGetDriverManifest( ODA_ADAPTER_PLUGIN_ID, true /*useDtpExtPoint*/,
		                			 true /*throwsIfNotFound*/ );
		}
    	catch( Exception adapterEx )	// wraps all runtime exceptions
		{
    	    m_driverConfig = null;	// cannot use BIRT driver manifest
    		throwAdapterException( methodName, adapterEx );
		}
	}

	/*
	 * Finds and returns a driver manifest of either the DTP extension point,
	 * or the BIRT one.
	 * This methods takes care of catching all exceptions, and
	 * in turn throws a DataException only. 
	 * The throwsIfNotFound flag, when set to true, throws
	 * a DataException if given driver manifest is not found; 
	 * if the flag is set to false, returns null instead. 
	 */
	private ExtensionManifest doGetDriverManifest( String dataSourceDriverId, 
	        										boolean useDtpExtPoint,
	        										boolean throwsIfNotFound ) 
		throws DataException
	{
	    final String methodName = "doGetDriverManifest";
	 
	    ManifestExplorer explorer = ManifestExplorer.getInstance();
	    try
	    {
			if( useDtpExtPoint )
			    return explorer.getExtensionManifest( dataSourceDriverId );

			// look under the birt oda data source extension point instead
			ExtensionManifest birtManifest = 
			    explorer.getExtensionManifest( dataSourceDriverId, 
			            					   BIRT_DATASOURCE_EXTENSION_POINT );

			if( birtManifest == null && throwsIfNotFound )	// not found
			    throw new IllegalArgumentException( dataSourceDriverId );
			return birtManifest;
	    }
	    catch( Exception ex )
		{
		    // dataSourceDriverId is not found as a DTP ODA driver
			if( useDtpExtPoint && 
			    ex instanceof IllegalArgumentException )
			{
			    if( ! throwsIfNotFound )
			        return null;	// not an error
			}
			
			// throws a DataException for driver configuration problem
	    	return throwConfigException( methodName, dataSourceDriverId, ex );
		}
	}
	
	private void throwAdapterException( String methodName, Throwable cause ) 
		throws DataException
	{
		sm_logger.logp( Level.SEVERE, sm_className, methodName,
			"Cannot load DTP-to-BIRT ODA adapter." );
		
		throw new DataException( ResourceConstants.CANNOT_LOAD_ODA_ADAPTER, cause,
			new Object[] { m_dataSourceDriverId, ODA_ADAPTER_PLUGIN_ID } );
	}
	
	private ExtensionManifest throwConfigException( String methodName, 
	        		String dataSourceDriverId, Throwable cause ) 
		throws DataException
	{
		sm_logger.logp( Level.SEVERE, sm_className, methodName,
			"Cannot process data source extension configuration.", cause );
		
		throw new DataException( ResourceConstants.CANNOT_PROCESS_DRIVER_CONFIG, cause, 
			new Object[] { dataSourceDriverId } );	
	}

}
