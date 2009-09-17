/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.build;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.tools.ant.BuildException;

/**
 * 
 *
 *  @author Rock Yu
 */

public class BuildUtil
{

	/**
	 * Get the plugin version for the plugin project. First find from
	 * "plugin.xml", then try to find the version number from "MANIFEST.MF"
	 * 
	 * @param pluginDir
	 *            directory of the plugin project.
	 * @return plugin version.
	 */

	public static String getPluginVersion( File pluginDir )
	{
		String version = null;

		// Find version from plugin.xml.

		File pluginXML = new File( pluginDir, "plugin.xml" ); //$NON-NLS-1$
		if ( pluginXML.exists( ) )
		{
			version = new PluginWrapper( pluginXML ).getPluginVersion( );
			if ( !StringUtil.isBlank( version ) )
				return version;
		}

		// Find version from manifest.

		File manifest = new File(
				new File( pluginDir, "META-INF" ), "MANIFEST.MF" ); //$NON-NLS-1$ //$NON-NLS-2$

		if ( manifest.exists( ) )
		{
			Properties props = new Properties( );
			try
			{
				props.load( new FileInputStream( manifest ) );
				return props.getProperty( "Bundle-Version" ); //$NON-NLS-1$
			}
			catch ( Exception e )
			{
				throw new BuildException( e );
			}
		}

		return null;
	}
}
