/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.File;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JDBCDriverInfoManager.JdbcDriverInfo;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public class JdbcDriverConfigUtil
{
	/** can not be instantiated */
	private JdbcDriverConfigUtil(){};
	
	/**
	 * @return driverFiles
	 */
	public static File[] getDriverFiles( )
	{
		File[] driverFiles = null;
		try
		{
			File driverDir = OdaJdbcDriver.getDriverDirectory( );
			if ( driverDir != null )
				driverFiles = driverDir.listFiles( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return driverFiles;
	}
    
	/**
	 * @param className
	 * @return urlFormat
	 */
    public static String getURLFormat(String className)
    {
       	JdbcDriverInfo jdbcDriverInfo = JDBCDriverInfoManager.getDriverInfo( className );
		if ( jdbcDriverInfo != null )
			return jdbcDriverInfo.getDriverUrlTemplate( );
		else
			return null;
    }
    
    /**
     * @param className
     * @return displayName
     */
    public static String getDisplayName( String className )
	{

		JdbcDriverInfo jdbcDriverInfo = JDBCDriverInfoManager.getDriverInfo( className );
		if ( jdbcDriverInfo != null )
			return jdbcDriverInfo.getDisplayName( );
		else
			return null;
	}
    
}
