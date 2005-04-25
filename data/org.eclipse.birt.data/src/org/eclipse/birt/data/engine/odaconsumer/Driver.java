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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.manager.OdaConnectionFactory;
import org.eclipse.birt.data.oda.IConnectionFactory;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.data.oda.util.driverconfig.DataSetType;
import org.eclipse.birt.data.oda.util.driverconfig.DataTypeMapping;
import org.eclipse.birt.data.oda.util.driverconfig.OdaDriverConfiguration;
import org.eclipse.birt.data.oda.util.driverconfig.TraceLogging;

/**
 * Each <code>Driver</code> maintains the state of a driver in the drivers 
 * home directory.  See <code>org.eclipse.birt.data.oda.util.driverconfig.ConfigManager</code> 
 * regarding the drivers home directory.
 */
class Driver
{
	private String m_driverName;
	private OdaDriverConfiguration m_driverConfig;
	
	Driver( String driverName )
	{
		m_driverName = driverName;
	}

	OdaDriverConfiguration getDriverConfig() throws DataException
	{
		try
		{
			if( m_driverConfig == null )
				m_driverConfig = 
					ConfigManager.getInstance().getDriverConfig( m_driverName );
			
			return m_driverConfig;
		}
		catch( Exception ex )
		{
			throw new DataException( ResourceConstants.CANNOT_PROCESS_DRIVER_CONFIG, ex, 
				                     new Object[] { m_driverName } );
		}
	}
	
	// gets the connection factory for this driver
	IConnectionFactory getConnectionFactory() throws DataException
	{	
		try
		{		
			return new OdaConnectionFactory( getDriverConfig() );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND, ex, 
                                     new Object[] { m_driverName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND, ex, 
                                     new Object[] { m_driverName } );
		}
	}

	// gets the specific native-to-oda type mapping for the specified data set type 
	// in this driver
	int getTypeMapping( String dataSetType, int nativeType ) throws DataException
	{
		DataSetType dsType = getDriverConfig().getDataSetType( dataSetType );
		DataTypeMapping mapping = dsType.getDataTypeMapping( (short) nativeType );
		
		// no mapping found in driver configuration, return a default type
		if( mapping == null )	
			return Types.CHAR;
		
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
			assert false;
			return Types.NULL;
		}
	}
	
	void setLogConfiguration( IConnectionFactory connFactory )
	{
	    assert( connFactory != null );
	    
	    OdaDriverConfiguration config;
	    try
	    {
	        config = getDriverConfig();
	        assert( config != null );
	    }
	    catch( DataException e )
	    {
	        // not able to set log configuratin
	        // TODO log warning
	        return;
	    }
	    
	    TraceLogging logConfig = config.getTraceLogging();
	    if ( logConfig == null )
	    {
	        // TODO log info
	        return;		// no log configuration, nothing to set
	    }
	    
	    // set default configuration values
	    String logFilenamePrefix = logConfig.getLogFileNamePrefix();
	    if ( logFilenamePrefix == null || logFilenamePrefix.length() == 0 )
	        logFilenamePrefix = m_driverName;
	    String logDest = logConfig.getLogDirectory();
	    // TODO set default log destination to that used by the ODA consumer application
	    
	    try
	    {
	        connFactory.setLogConfiguration( logConfig.getLogLevel(),
	                		logDest,
			                logFilenamePrefix,
			                logConfig.getLogFormatterClass() );
	    }
	    catch( OdaException e1 )
	    {
	        // TODO log warning
	    }
	}
}
