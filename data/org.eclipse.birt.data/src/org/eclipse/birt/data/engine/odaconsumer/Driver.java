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
import java.sql.Types;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.manager.OdaDriver;
import org.eclipse.birt.data.oda.IDriver;
import org.eclipse.birt.data.oda.LogConfiguration;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.manifest.ManifestExplorer;
import org.eclipse.birt.data.oda.util.manifest.DataSetType;
import org.eclipse.birt.data.oda.util.manifest.DataTypeMapping;
import org.eclipse.birt.data.oda.util.manifest.ExtensionManifest;
import org.eclipse.birt.data.oda.util.manifest.TraceLogging;

/**
 * Each <code>Driver</code> maintains the state of a driver in the drivers 
 * home directory.  
 */
class Driver
{
	private String m_dataSourceDriverId;
	private ExtensionManifest m_extensionConfig;
    private IDriver m_driverHelper;

    // trace logging variables
	private static String sm_className = Driver.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );

	Driver( String dataSourceElementId )
	{
		String methodName = "Driver";		
		sm_logger.entering( sm_className, methodName, dataSourceElementId );

		m_dataSourceDriverId = dataSourceElementId;

		sm_logger.exiting( sm_className, methodName, this );
	}

	ExtensionManifest getExtensionConfig() throws DataException
	{
		String methodName = "getExtensionConfig";
		
		try
		{
			if( m_extensionConfig == null )
				m_extensionConfig = 
					ManifestExplorer.getInstance().getExtensionManifest( m_dataSourceDriverId );
			
			return m_extensionConfig;
		}
		catch( Exception ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
					"Cannot process data source extension configuration.", ex );
			throw new DataException( ResourceConstants.CANNOT_PROCESS_DRIVER_CONFIG, ex, 
				                     new Object[] { m_dataSourceDriverId } );
		}
	}
	
	// gets the consumer manager helper for this driver
	IDriver getDriverHelper() throws DataException
	{	
		String methodName = "getDriverHelper";
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

	// gets the specific native-to-oda type mapping for the specified data set type 
	// in this driver
	int getTypeMapping( String dataSetType, int nativeType ) throws DataException
	{
		String methodName = "getTypeMapping";
		DataSetType dsType = null;
		
		try
		{
			dsType = getExtensionConfig().getDataSetType( dataSetType );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
					"Cannot find data set element.", ex );
			throw new DataException( ex.getLocalizedMessage(), ex );
		}

		DataTypeMapping mapping = dsType.getDataTypeMapping( (short) nativeType );
		
		// no mapping found in data source extension configuration, return a default type
		if( mapping == null )	
			return Types.NULL;
		
		String odaType = mapping.getOdaScalarDataType();
		
		if( odaType.equals( "Date" ) )
			return Types.DATE;
		else if( odaType.equals( "Decimal" ) )
			return Types.DECIMAL;
		else if( odaType.equals( "Double" ) )
			return Types.DOUBLE;
		else if( odaType.equals( "Integer" ) )
			return Types.INTEGER;
		else if( odaType.equals( "String" ) )
			return Types.CHAR;
		else if( odaType.equals( "Time" ) )
			return Types.TIME;
		else if( odaType.equals( "Timestamp" ) )
			return Types.TIMESTAMP;
		else
		{
			// shouldn't be in here, the configuration should only have the 
			// types above
			sm_logger.logp( Level.WARNING, sm_className, methodName,
					"Invalid ODA data type {0} specified in data source extension mapping.", odaType );
			return Types.NULL;
		}
	}
	
    /**
     * Passes the trace logging configuration values to the
     * ODA driver and its consumer helper.
     */
	void setLogConfiguration() 
	{
		String methodName = "setLogConfiguration";
	            
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
     * Gets the driver's trace logging element in plug-in manifest.
     */ 
	private TraceLogging getLoggingElement()
    {
        String methodName = "getLoggingElement";
        
        ExtensionManifest config;
        try
        {
            config = getExtensionConfig();
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
            pluginId = getExtensionConfig().getNamespace();
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
        String methodName = "getDefaultLogDirectory( String logDir )";
        
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
        String methodName = "getDefaultLogDirectory";
        
        String defaultLogDir = null;
        ExtensionManifest driverManifest = getExtensionConfig();
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
                throw new DataException( ex.getLocalizedMessage(), ex );
            }
        }

        if( defaultLogDir == null )
        {
            sm_logger.logp( Level.WARNING, sm_className, methodName,
                    "Not able to determine ODA consumer default log directory." );
            throw new DataException( "" );    // no log directory
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
