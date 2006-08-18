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

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.helper.OdaDriver;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;
import org.eclipse.datatools.connectivity.oda.util.manifest.TraceLogging;

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

    /**
     * Passes the trace logging configuration values to the
     * ODA driver and its consumer helper.
     */
	void setLogConfiguration() 
	{
	    final String methodName = "setLogConfiguration";
	            
        // get log configuration values
        LogConfiguration logConfigSpec = newLogSettings();
	    if( logConfigSpec == null )
	    {
			sm_logger.logp( Level.INFO, sm_className, methodName,
					"ODA driver does not have valid trace logging defined in the plug-in .options or plugin.xml file." );
	        return;		// no log configuration, nothing to set
	    }
	            
        // get the ODA consumer helper to set the log configuration
        IDriver driverHelper;
        try
        {
            driverHelper = getDriverHelper();
        }
        catch( DataException ex )
        {
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Cannot get ODA consumer manager's driver helper to set up logging.", ex );
            return;
        }

        try
	    {
	        driverHelper.setLogConfiguration( logConfigSpec );
	    }
	    catch( OdaException e1 )
	    {
            // log exception and continue
			sm_logger.logp( Level.WARNING, sm_className, methodName,
					"Cannot set ODA data source log configuration.", e1 );
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
	
    /*
     * Gets the driver's trace logging element in plug-in manifest.
     */ 
	private TraceLogging getLoggingElement()
    {
	    final String methodName = "getLoggingElement";
        
        ExtensionManifest config;
        try
        {
            config = getDriverExtensionConfig();
        }
        catch( DataException ex )
        {
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Cannot get ODA driver plug-in manifest.", ex );
            return null;
        }
        
        assert( config != null );
        return config.getTraceLogging();
    }
    
    /*
     * Gets the plug-in trace logging configuration from 
     * the .options file if set to debugging; otherwise,  
     * from the plugin.xml traceLogging element.
     */ 
	private LogConfiguration newLogSettings()
    {
        // get log configuration specified in plug-in .options file
        LogConfiguration logOptions = newTraceOptions();
        if( logOptions != null )
            return logOptions;  // done

        // no trace options are set for ODA driver;
        // get the driver's trace logging element in plug-in manifest        
        TraceLogging logManifest = getLoggingElement();
        if( logManifest == null )
            return null;    // none found
        
        // convert trace logging element to returned object
        return newLogSettings( logManifest );
    }
    
    /* 
     * Gets the plug-in trace logging configuration  
     * from the PDE .options file 
     */
    private LogConfiguration newTraceOptions()
    {
        String pluginId;
        try
        {
            pluginId = getDriverExtensionConfig().getNamespace();
        }
        catch( DataException e )
        {
            // try use the driver's data source element id
            pluginId = m_dataSourceDriverId;
        }
        
        String debugOption = pluginId + "/debug";
        String debugOptionValue = Platform.getDebugOption( debugOption );
        Boolean isDebug = Boolean.valueOf( debugOptionValue );
        if( isDebug == Boolean.FALSE )
            return null;    // not found or not debugging
        
        String logLevelOption = pluginId + "/traceLogging/logLevel";
        int logLevel = TraceLogging.toLogLevelNumber( 
                			Platform.getDebugOption( logLevelOption ) );
        
        String logFormatterOption = pluginId + "/traceLogging/logFormatterClass";
        String logFilePrefixOption = pluginId + "/traceLogging/logFileNamePrefix";
        String logDirOption = pluginId + "/traceLogging/logDirectory";
        
        // set default configuration values if not specified
        String logFilenamePrefix = Platform.getDebugOption( logFilePrefixOption );
        String logDest = Platform.getDebugOption( logDirOption );
        
        // if either log file attribute has value, ensure
        // both attributes have values, using default value as needed
        if( isNotEmpty( logFilenamePrefix ) || isNotEmpty( logDest ) )
        {
	        logFilenamePrefix = getDefaultLogFilenamePrefix( logFilenamePrefix );
	        logDest = getDefaultLogDirectory( logDest );
        }

        // instantiate object with log configuration values
        return new LogConfiguration( m_dataSourceDriverId, 
                            logLevel,
                            logDest,
                            logFilenamePrefix,
                            Platform.getDebugOption( logFormatterOption ) );
    }
    
    /*
     * Gets the plug-in trace logging configuration settings 
     * from the plugin.xml traceLogging element
     */
    private LogConfiguration newLogSettings( TraceLogging logManifest )
    {
        assert( logManifest != null );
        
        // set default configuration values if not specified
        String logFilenamePrefix = logManifest.getLogFileNamePrefix();
        String logDest = logManifest.getLogDirectory();
        
        // if either log file attribute has value, ensure
        // both attributes have values, using default value as needed
        if( isNotEmpty( logFilenamePrefix ) || isNotEmpty( logDest ) )
        {
	        logFilenamePrefix = getDefaultLogFilenamePrefix( logFilenamePrefix );
	        logDest = getDefaultLogDirectory( logDest );
        }

        // instantiate object with log configuration values
        return new LogConfiguration( m_dataSourceDriverId, 
                            logManifest.getLogLevel(),
                            logDest,
                            logFilenamePrefix,
                            logManifest.getLogFormatterClass() );
    }

    /*
     * Returns the default configuration value if 
     * the given log filename prefix is not specified
     */
    private String getDefaultLogFilenamePrefix( String prefix )
    {
        if( isNotEmpty( prefix ) )
            return prefix;	// already specified, use as is
        return m_dataSourceDriverId;
    }
    
    /*
     * Returns the default configuration value if 
     * the given log directory is not specified
     */
    private String getDefaultLogDirectory( String logDir )
    {
        final String methodName = "getDefaultLogDirectory( String logDir )";
        
        if( isNotEmpty( logDir ) )
            return logDir;	// already specified, use as is
        
        // set default log directory to that used by the ODA consumer application
        try
        {
            logDir = getDefaultLogDirectory();
        }
        catch( DataException ex )
        {
            // ignore error; leave null/empty log directory
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Not able to determine ODA driver's default log directory.", ex );
        }
        return logDir;		// may be null or empty
    }
    
    /*
     * Returns the default log directory of the driver
     */
    private String getDefaultLogDirectory() throws DataException
    {
        final String methodName = "getDefaultLogDirectory";
        
        String defaultLogDir = null;
        ExtensionManifest driverManifest = getDriverExtensionConfig();
        try
        {
            defaultLogDir = driverManifest.getDriverLocation().getPath();
        }
        catch( IOException ex )
        {
            // no driver directory info is available; ignore
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Not able to get ODA driver plugin directory.", ex );
        }

        if( defaultLogDir != null )
            return defaultLogDir;
        
        // use the data engine installation directory as driver log destination
                
        IBundle dataBundle = Platform.getBundle( "org.eclipse.birt.data" );
        if( dataBundle != null )
        {
            URL url = dataBundle.getEntry( "/" );
            try
            {
                if( url != null )
                    defaultLogDir = Platform.asLocalURL( url ).getPath();
            }
            catch( IOException ex )
            {
                sm_logger.logp( Level.WARNING, sm_className, methodName,
                        "Not able to get Data Engine plugin directory.", ex );
                throw new DataException( ex.getLocalizedMessage( ), ex.getCause( ) );
            }
        }

        if( defaultLogDir == null )
        {
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Not able to determine ODA consumer default log directory." );
            throw new DataException( ResourceConstants.CANNOT_FIND_LOG_DIRECTORY );    // no log directory
       }

        return defaultLogDir;
    }
 
    private boolean isNullOrEmpty( String value )
    {
        return ( value == null || value.length() == 0 );
    }
    
    
    private boolean isNotEmpty( String value )
    {
        return ! isNullOrEmpty( value );
    }

}
