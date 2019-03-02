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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarFile;


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
	
	public static boolean checkAboutFile(File jarname)
	{
		
		try{
		    
			JarFile a = new JarFile(jarname);
			
		    if(a.getEntry("about.html")!=null){
				//System.out.println("license exists");
				return true;
			}else{
				//System.out.println("license is missing");
				return false;
			}
		}catch(IOException e)
		{
			System.out.println(e.getMessage() + e.getStackTrace());
			System.out.println("JAR doesn't exist");
		}
		return false;
	}
	
	public static File[] getJarList(String dir){
		
		File[] flist;
		File file = new File(dir);
		flist = file.listFiles(new JarFileFilter());
		return flist;
		
	}
	
	static class JarFileFilter implements FileFilter {

		  public boolean accept(File pathname) {

		    if (pathname.getName().endsWith(".jar"))
		      return true;
		    
		    return false;
		  }

	public static void main(String[] args){
		//BuildUtil checker = new BuildUtil();
		String checkdir = "E:/test/2_6_0/temp/birt-updatesite-2_6_0-20100601/plugins";
		File[] list = BuildUtil.getJarList(checkdir);
		for(int i=0;i<list.length;i++){
			System.out.println("Checking " + list[i].getName());
			if(BuildUtil.checkAboutFile(list[i]))
				;//System.out.println("pass");
			else
				System.out.println("File: " + list[i].getName() + "............................missing about.html");
		}
	}
	
	}
}
