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

import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // trace logging variables
	private static String sm_className = Driver.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static Logger sm_logger = Logger.getLogger( sm_loggerName );

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
	
	// gets the connection factory for this driver
	IDriver getConnectionFactory() throws DataException
	{	
		String methodName = "getConnectionFactory";
		
		try
		{		
			return new OdaDriver( getExtensionConfig() );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
					"Cannot get ODA data source driver factory.", ex );
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
	
	void setLogConfiguration( IDriver odaDriver )
	{
		String methodName = "setLogConfiguration";
	    assert( odaDriver != null );
	    
	    ExtensionManifest config;
	    try
	    {
	        config = getExtensionConfig();
	        assert( config != null );
	    }
	    catch( DataException ex )
	    {
	        // not able to set log configuration
			sm_logger.logp( Level.WARNING, sm_className, methodName,
					"Cannot set data source extension's log configuration.", ex );
	        return;
	    }
	    
	    // use the data source extension's data source element id 
	    String dataSourceId = m_dataSourceDriverId;
	    
	    TraceLogging logConfig = config.getTraceLogging();
	    if ( logConfig == null )
	    {
			sm_logger.logp( Level.INFO, sm_className, methodName,
					"ODA driver does not have trace logging defined." );
	        return;		// no log configuration, nothing to set
	    }
	    
	    // set default configuration values
	    String logFilenamePrefix = logConfig.getLogFileNamePrefix();
	    if ( logFilenamePrefix == null || logFilenamePrefix.length() == 0 )
	        logFilenamePrefix = m_dataSourceDriverId;
	    String logDest = logConfig.getLogDirectory();
	    // TODO post release 1 - set default log destination to that used by the ODA consumer application
	    
	    LogConfiguration logConfigSpec =
	        new LogConfiguration( dataSourceId, 
		            		logConfig.getLogLevel(),
		            		logDest,
			                logFilenamePrefix,
			                logConfig.getLogFormatterClass() );
	    try
	    {
	        odaDriver.setLogConfiguration( logConfigSpec );
	    }
	    catch( OdaException e1 )
	    {
			sm_logger.logp( Level.WARNING, sm_className, methodName,
					"Cannot set ODA data source log configuration.", e1 );
	    }
	}
}
