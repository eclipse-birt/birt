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

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.util.ArrayList;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * The JDBCDriverInfoManager manage the information of jdbc drivers read from the 
 * org.eclipse.birt.report.data.oda.jdbc.driverInfo extensions. The
 * <code>JDBCDriverInfoManager</code> instance can be retrieved using the
 * <code>getInstance()</code> method.
 */

public class JDBCDriverInfoManager
{
	// prevents construction
	private JDBCDriverInfoManager( )
	{
	}

	/**
	 * Returns a list of JDBC drivers discovered in the driverInfo extensions,
	 * as an array of JDBCDriverInformation objects
	 */
	static public JDBCDriverInformation[] getDrivers( )
	{
		ArrayList drivers = new ArrayList();
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().
				getConfigurationElementsFor( OdaJdbcDriver.Constants.DRIVER_INFO_EXTENSION ) ;
		if ( configElements != null )
		{
			for ( int e = 0; e < configElements.length; e++ )
			{
				if ( configElements[e].getName( ).equals( 
						OdaJdbcDriver.Constants.DRIVER_INFO_ELEM_JDBCDRIVER ) )
				{
					drivers.add( newJdbcDriverInfo( configElements[e] ) );
				}
			}
		}
		return (JDBCDriverInformation[])drivers.toArray( new JDBCDriverInformation[0]);
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
		driverInfo.setHide( configElement.getAttribute( "hide" ) );
		driverInfo.populateProperties( configElement );
		return driverInfo;
	}
	
}