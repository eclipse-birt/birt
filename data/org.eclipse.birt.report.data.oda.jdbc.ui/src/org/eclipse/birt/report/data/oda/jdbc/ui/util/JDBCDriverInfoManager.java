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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.oda.OdaException;

/**
 * The JDBCDriverInfoManager manage the information of jdbc drivers. The
 * <code>JDBCDriverInfoManager</code> instance can be retrieved using the
 * <code>getInstance()</code> method.
 */

public class JDBCDriverInfoManager
{
	private static Map driverInfoMap = new HashMap( );
	private static Object nullObject = new Object( );

	/** single instance */
	private JDBCDriverInfoManager( )
	{
	}

	/**
	 * Return the information for the specified jdbc driver
	 * Since cache mechanism is used, plugin registry is not
	 * allowed to be updated in running time.
	 * @param driverClass
	 *            The jdbc driver class name
	 * @return
	 * @throws OdaException
	 */
	static JdbcDriverInfo getDriverInfo( String driverClass )
	{
		if ( driverClass == null )
			return null;
		
		JdbcDriverInfo driverInfo = null;
		
		Object ob = driverInfoMap.get( driverClass );
		if ( ob != null )
		{
			if ( ob == nullObject )
			{
				driverInfo = null;
			}
			else
			{
				driverInfo = (JdbcDriverInfo) ob;
			}
		}
		else
		{
			driverInfo = getJdbcDriverInfo( driverClass );
			if ( driverInfo == null )
			{
				driverInfoMap.put( driverClass, nullObject );
			}
			else
			{
				driverInfoMap.put( driverClass, driverInfo );
			}
		}

		return driverInfo;
	}

	/**
	 * Get JdbcDriverInfo from plugin file
	 * @param driverClass
	 * @return
	 */
	private static JdbcDriverInfo getJdbcDriverInfo( String driverClass )
	{
		JdbcDriverInfo driverInfo = null;
		
		IExtension[] extensions = getDriverInfoExtensions( );		
		for ( int i = 0, n = extensions.length; i < n; i++ )
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements( );
			String configDriverName = null;
			for ( int j = 0; j < configElements.length; j++ )
			{
				IConfigurationElement configElement = configElements[j];

				if ( configElement.getName( ).equals( "jdbcDriver" ) )
					configDriverName = configElement.getAttribute( "driverClass" );

				if ( configDriverName != null
						&& configDriverName.equals( driverClass ) )
				{
					driverInfo = new JdbcDriverInfo( configElement );
					break;
				}
			}
		}

		return driverInfo;
	}

	/**
	 * Get IExtension of driverinfo 
	 * @return
	 */
	private static IExtension[] getDriverInfoExtensions( )
	{
		final String driverInfoExtName = "org.eclipse.birt.report.data.oda.jdbc.driverinfo";
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint( driverInfoExtName );
		return extensionPoint.getExtensions( );
	}

	/**
	 * This class encapsulates access to a driver's information.
	 */
	static class JdbcDriverInfo
	{
		private String driverClass;
		private String name;
		private String urlTemplate;

		JdbcDriverInfo( IConfigurationElement odaDriverConfigElement )
		{
			this.driverClass = odaDriverConfigElement.getAttribute( "driverClass" );
			this.name = odaDriverConfigElement.getAttribute( "name" );
			this.urlTemplate = odaDriverConfigElement.getAttribute( "urlTemplate" );
		}

		/**
		 * Returns the driver url template
		 * 
		 * @return the url template for the driver, null if the driver does not
		 *         have a url template.
		 */
		public String getDriverUrlTemplate( )
		{
			return urlTemplate;
		}

		/**
		 * Returns the driver class name
		 * 
		 * @return the class name for the driver.
		 */
		public String getDriverClassName( )
		{
			return driverClass;
		}

		/**
		 * Returns the driver display name
		 * 
		 * @return the display name for the driver, null if the driver does not
		 *         have a display name.
		 */
		public String getDisplayName( )
		{
			return name;
		}
	}

}