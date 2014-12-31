/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.utils;

import java.util.ArrayList;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * The JDBCDriverInfoManager manage the information of jdbc drivers read from the 
 * org.eclipse.birt.report.data.oda.jdbc.driverInfo extensions. The
 * <code>JDBCDriverInfoManager</code> instance can be retrieved using the
 * <code>getInstance()</code> method.
 */

public class JDBCDriverInfoManager
{
	private JDBCDriverInformation[] jdbcDriverInfo = null;	
	private static JDBCDriverInfoManager instance = null;
	
	// prevents construction
	private JDBCDriverInfoManager( )
	{
	}

	public synchronized static JDBCDriverInfoManager getInstance( )
	{
		if( instance== null )
			instance = new JDBCDriverInfoManager( );
		return instance;
	}
	
	/**
	 * 
	 * @param driverClassName
	 * @return
	 */
	public JDBCDriverInformation getDriversInfo( String driverClassName )
	{
		JDBCDriverInformation[] infos = getDriversInfo( );
    	for( JDBCDriverInformation info: infos )
    	{
    		if( driverClassName.equals( info.getDriverClassName( )) )
			{
    			return info;
  			}
    	}
    	return null;
	}
	
	/**
	 * Returns a list of JDBC drivers discovered in the driverInfo extensions,
	 * as an array of JDBCDriverInformation objects
	 */
	public JDBCDriverInformation[] getDriversInfo( )
	{
		if( jdbcDriverInfo!= null )
			return jdbcDriverInfo;
		
		synchronized( this )
		{
			if( jdbcDriverInfo!= null )
				return jdbcDriverInfo;			

			IExtensionRegistry extReg = Platform.getExtensionRegistry();
			IExtensionPoint extPoint = 
				extReg.getExtensionPoint( OdaJdbcDriver.Constants.DRIVER_INFO_EXTENSION );
			
			if ( extPoint == null )
				return new JDBCDriverInformation[0];
			
			IExtension[] exts = extPoint.getExtensions();
			if ( exts == null )
				return new JDBCDriverInformation[0];
			
			ArrayList<JDBCDriverInformation> drivers = new ArrayList<JDBCDriverInformation>( );
			
			for ( int e = 0; e < exts.length; e++)
			{
				IConfigurationElement[] configElems = exts[e].getConfigurationElements(); 
				if ( configElems == null )
					continue;
				
				for ( int i = 0; i < configElems.length; i++ )
				{
					if ( configElems[i].getName().equals( 
							OdaJdbcDriver.Constants.DRIVER_INFO_ELEM_JDBCDRIVER) )
					{
						drivers.add( newJdbcDriverInfo( configElems[i] ) );
					}
				}
			}

			jdbcDriverInfo = (JDBCDriverInformation[])drivers.toArray( new JDBCDriverInformation[0]);
		}
		return jdbcDriverInfo;
	}

	/**
	 * Creates a new JDBCDriverInformation instance based on a driverInfo extension element
	 * @param driverClass
	 * @return
	 */
	private static JDBCDriverInformation newJdbcDriverInfo(
			IConfigurationElement configElement )
	{
		JDBCDriverInformation driverInfo = JDBCDriverInformation.newInstance( 
				configElement.getAttribute( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_DRIVERCLASS ) );
		driverInfo.setDisplayName( 
				configElement.getAttribute( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_NAME ) );
		driverInfo.setUrlFormat( 
				configElement.getAttribute( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_URLTEMPL ) );
		driverInfo.setHide( configElement.getAttribute( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_HIDE ) );
		driverInfo.populateProperties( configElement );
		return driverInfo;
	}
	
}