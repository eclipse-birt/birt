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
import java.io.IOException;

import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public class JdbcDriverConfigUtil
{

	private String driverName = "jdbc"; //$NON-NLS-1$

	/**
	 * 
	 * @param driverName
	 */
	public JdbcDriverConfigUtil( String driverName )
	{
		super( );
		this.driverName = driverName;
	}

	/**
	 * @return Returns the driverName.
	 */
	public String getDriverName( )
	{
		return driverName;
	}

	/**
	 * @param driverName
	 *            The driverName to set.
	 */
	public void setDriverName( String driverName )
	{
		this.driverName = driverName;
	}

	private File getDriverLocation( ) throws OdaException, IOException
	{
        ConfigManager configMgr = ConfigManager.getInstance( );
        return new File((configMgr.getDriverConfig(getDriverName())).getDriverLocation().getFile() + "/drivers");
	}
    

	/**
	 * @return
	 */
	public File[] getDriverFiles( )
	{
		try
		{
            return getDriverLocation().listFiles();
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
        
        return null;

	}
    
    public String getURLFormat(String className)
    {
        return null;
    }
}
