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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;

public class JdbcDriverConfigUtil
{
	/** can not be instantiated */
	private JdbcDriverConfigUtil(){};

	//reset deleted Jar files map
	static
	{
		Utility.setPreferenceStoredMap( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY,
				new HashMap( ) );
	}
	
	/**
	 * Gets a list of possible driver files under the oda.jdbc plugin's "drivers" directory
	 * Returned file list has been filtered by file type. Only JAR and ZIP files are expected
	 * @return driverFiles
	 */
	public static List getDriverFiles( )
	{
		try
		{
			//can not use filefilter,since the input is not a directory
			List fileList = OdaJdbcDriver.getDriverFileList( );
			Map deletedJars = Utility.getPreferenceStoredMap( JdbcPlugin.DELETED_JAR_MAP_PREFERENCE_KEY );
			List filteredFileList = new java.util.ArrayList( );
			for ( int i = 0; i < fileList.size(); i++ )
			{
				File f = (File) fileList.get( i );
				if ( !deletedJars.containsKey( f.getName( ) ) )
				{
					filteredFileList.add( f );
				}
			}
			return filteredFileList;
		}
		catch ( OdaException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}
		return null;
	}
}
