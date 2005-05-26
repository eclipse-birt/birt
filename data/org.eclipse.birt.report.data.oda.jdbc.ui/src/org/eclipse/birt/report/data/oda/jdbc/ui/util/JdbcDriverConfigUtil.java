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
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public class JdbcDriverConfigUtil
{
	/** can not be instantiated */
	private JdbcDriverConfigUtil(){};
	
	/**
	 * Gets a list of possible driver files under the oda.jdbc plugin's "drivers" directory
	 * Returned file list has been filtered by file type. Only JAR and ZIP files are expected
	 * @return driverFiles
	 */
	public static File[] getDriverFiles( )
	{
		try
		{
			return OdaJdbcDriver.getDriverFileList();
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return null;
	}
    
}
