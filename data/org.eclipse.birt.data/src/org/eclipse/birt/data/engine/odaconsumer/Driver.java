/*******************************************************************************
* Copyright (c) 2004, 2005 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.data.engine.odaconsumer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Types;
import java.util.Hashtable;
import org.eclipse.birt.data.oda.IConnectionFactory;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.data.oda.util.driverconfig.DataSetType;
import org.eclipse.birt.data.oda.util.driverconfig.DataSetTypes;
import org.eclipse.birt.data.oda.util.driverconfig.DataTypeMapping;
import org.eclipse.birt.data.oda.util.driverconfig.DataTypeMappings;
import org.eclipse.birt.data.oda.util.driverconfig.DriverLibraries;
import org.eclipse.birt.data.oda.util.driverconfig.LibrariesForOS;
import org.eclipse.birt.data.oda.util.driverconfig.OpenDataAccessConfig;
import org.eclipse.birt.data.oda.util.driverconfig.RunTimeInterface;
import org.eclipse.birt.data.oda.util.driverconfig.types.OSTypeType;
import org.eclipse.birt.data.oda.util.driverconfig.types.OdaScalarDataTypeType;

/**
 * Each <code>Driver</code> maintains the state of a driver in the drivers 
 * home directory.  See <code>org.eclipse.birt.data.oda.util.driverconfig.ConfigManager</code> 
 * regarding the drivers home directory.
 */
class Driver
{
	private String m_driverName;
	private OpenDataAccessConfig m_driverConfig;
	private URLClassLoader m_classLoader;
	private Hashtable m_DSTypeMappings;
	
	Driver( String driverName )
	{
		m_driverName = driverName;
	}

	OpenDataAccessConfig getDriverConfig() throws OdaException
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
			// TODO externalize for i18n
			OdaException odaEx = 
				new OdaException( "Cannot process the (" + m_driverName +
								  ") driver's odaconfig.xml." );
			odaEx.initCause( ex );
			throw odaEx;
		}
	}
	
	// gets the connection factory for this driver using the driver's URLClassLoader
	IConnectionFactory getConnectionFactory() throws OdaException
	{
		RunTimeInterface runtime = 
			getDriverConfig().getRunTimeInterface();
		
		if( m_classLoader == null )
		{	
			DriverLibraries driverLibs = runtime.getDriverLibraries();
			LibrariesForOS libsForOS = findLibsForOS( driverLibs );
			URL[] urls = getURLs( libsForOS );
			m_classLoader = new DataAccessClassLoader( urls );
		}
		
		String initEntryPoint = runtime.getDriverInitEntryPoint();
		try
		{
			Class connectionFactoryCls = 
				Class.forName( initEntryPoint, true, m_classLoader );
			return (IConnectionFactory) connectionFactoryCls.newInstance();
		}
		catch( Exception ex )
		{
			// TODO externalize for i18n
			OdaException odaEx = 
				new OdaException( initEntryPoint + " cannot be found or " +
								  "cannot be instantiated." );
			odaEx.initCause( ex );
			throw odaEx;
		}
	}
	
	private LibrariesForOS findLibsForOS( DriverLibraries driverLibs ) throws OdaException
	{
		// find the current platform through the system property
		String systemOS = System.getProperty( "os.name" );
		systemOS = systemOS.toLowerCase();
		OSTypeType osType = null;
		if( systemOS.startsWith( "windows" ) )
			osType = OSTypeType.WINDOWS;
		else if( systemOS.startsWith( "sunos" ) )
			osType = OSTypeType.SOLARIS;
		else if( systemOS.startsWith( "aix" ) )
			osType = OSTypeType.AIX;
		else if( systemOS.startsWith( "hp-ux" ) )
			osType = OSTypeType.HP_UX;
		else if( systemOS.startsWith( "linux" ) )
			osType = OSTypeType.LINUX;
		else
		{
			// TODO any other supported platforms?
			// need to wait for Mihail's SPM to be done
		}
		
		LibrariesForOS libsForOS = null;
		LibrariesForOS[] libsForOSArray = driverLibs.getLibrariesForOS();
		for( int i = 0, n = libsForOSArray.length; i < n; i++ )
		{
			if( libsForOSArray[i].getOSType() == osType ||
				libsForOSArray[i].getOSType() == OSTypeType.ALL )
			{
				libsForOS = libsForOSArray[i];
				break;
			}
		}
		
		// TODO externalize for i18n
		if( libsForOS == null )
			throw new OdaException( "The driver (" + m_driverName + 
									") does not support " + systemOS + "." );
		return libsForOS;
	}

	private URL[] getURLs( LibrariesForOS libsForOS ) 
		throws OdaException
	{
		String[] filenames = libsForOS.getLibraryName();
		String location = libsForOS.getLocation();
		
		// use the optional location value when available
		File libPath = ( location != null ) ?
				  	   new File( location ) :
				  	   ConfigManager.getInstance().getDriverDefaultLibPath( m_driverName );

		int numOfLibs = filenames.length;
		URL[] urls = new URL[numOfLibs];
		for( int i = 0; i < numOfLibs; i++ )
		{
			File f = new File( libPath, filenames[i] );
			try
			{
				urls[i] = f.toURL();
			}
			catch( MalformedURLException ex )
			{
				// TODO externalize for i18n
				OdaException odaEx = 
					new OdaException( "Cannot generate URLs for the classloader." );
				odaEx.initCause( ex );
				throw odaEx;
			}
		}
		
		return urls;
	}

	// gets the specific native-to-oda type mapping for the specified data set type 
	// in this driver
	int getTypeMapping( String dataSetType, int nativeType ) throws OdaException
	{
		Hashtable typeMappingForDS = 
			(Hashtable) getDSTypeMappings().get( dataSetType );
		if( typeMappingForDS == null )
		{
			DataSetType dataSet = findDataSet( dataSetType );
			typeMappingForDS = cacheTypeMappings( dataSet );
			getDSTypeMappings().put( dataSetType, typeMappingForDS );
		}
		
		Integer i = (Integer) typeMappingForDS.get( new Integer( nativeType ) );
		// TODO externalize for i18n
		if( i == null )
			throw new OdaException( "Unsupported native type (" + nativeType +
									") for: " + m_driverName + "-" + dataSetType );
		
		return i.intValue();
	}
	
	// cache all of the type mappings for the specified data set into the 
	// hashtable
	private Hashtable cacheTypeMappings( DataSetType dataSet )
	{
		Hashtable typeMappingForDS = new Hashtable();
		DataTypeMappings mappings = dataSet.getDataTypeMappings();
		DataTypeMapping[] mappingsArray = mappings.getDataTypeMapping();
		for( int i = 0, n = mappingsArray.length; i < n; i++ )
		{
			DataTypeMapping mapping = mappingsArray[i];
			int nType = mapping.getNativeDataTypeCode();
			int scalarType = mapping.getOdaScalarDataType().getType();
			int odaType = Types.CHAR;
			switch( scalarType )
			{
				case OdaScalarDataTypeType.DATE_TYPE:
					odaType = Types.DATE;
					break;
				case OdaScalarDataTypeType.DECIMAL_TYPE:
					odaType = Types.DECIMAL;
					break;
				case OdaScalarDataTypeType.DOUBLE_TYPE:
					odaType = Types.DOUBLE;
					break;
				case OdaScalarDataTypeType.INTEGER_TYPE:
					odaType = Types.INTEGER;
					break;
				case OdaScalarDataTypeType.STRING_TYPE:
					odaType = Types.CHAR;
					break;
				case OdaScalarDataTypeType.TIME_TYPE:
					odaType = Types.TIME;
					break;
				case OdaScalarDataTypeType.TIMESTAMP_TYPE:
					odaType = Types.TIMESTAMP;
					break;
				default:
					// not possible for an unsupported oda scalar type because 
					// the odaconfig.xml would fail validation
					assert false;
			}
			typeMappingForDS.put( new Integer( nType ), 
								  new Integer( odaType ) );
		}
		return typeMappingForDS;
	}

	private DataSetType findDataSet( String dataSetType ) throws OdaException
	{
		// need to check the configuration to see if it has the data set type
		DataSetTypes dataSets = getDriverConfig().getDataSetTypes();
		DataSetType[] dataSetsArray = dataSets.getDataSetType();
		for( int i = 0, n = dataSetsArray.length; i < n; i++ )
		{
			DataSetType dataSet = dataSetsArray[i];
			if( dataSet.getName().equalsIgnoreCase( dataSetType ) )
				return dataSet;
		}

		// TODO externalize for i18n
		throw new OdaException( "The data set type (" + dataSetType + 
								") is not supported by this driver (" +
								m_driverName + ")." );
	}

	private Hashtable getDSTypeMappings()
	{
		if( m_DSTypeMappings == null )
			m_DSTypeMappings = new Hashtable();
		
		return m_DSTypeMappings;
	}
}
