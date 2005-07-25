/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * An platform context that is based on resource operations instead of file operations.
 * Since in web environment WAR deployment, absolute file path is not available, user must use resource operations instead of file operations.
 * In this case, user should use this PlatformContext or develop his own PlatformContext to make sure no file operations are used. 
 */
public class PlatformServletContext implements IPlatformContext
{
	public ServletContext context = null;	// the ServletContext.  
	public String urlLeadingString = null;	// The URLLeadingString (e.g. http://localhost:7001/birt).
	private static final String directorySeparator = "/"; //$NON-NLS-1$ // Refer to getResourcePaths in http://java.sun.com/j2ee/sdk_1.3/techdocs/api/javax/servlet/ServletContext.html#getResourcePaths(java.lang.String)
	
	static protected Logger log = Logger.getLogger(PlatformServletContext.class.getName());

	public PlatformServletContext( ServletContext context, String urlLeadingString )
	{
		this.context = context;
		this.urlLeadingString = urlLeadingString;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getFolderList( String homeFolder, String subFolder )
	 */
	public List getFolderList( String homeFolder, String subFolder )
	{
		List folderList = new ArrayList();
		
		String folderString = homeFolder;
		if ( (subFolder != null) && 
			 (subFolder.length() > 0) )
		{
		     if (folderString.endsWith(directorySeparator))
		     {
		     	folderString += directorySeparator;
		     }
			 folderString +=  subFolder;
		}
		
		Set files = context.getResourcePaths( folderString );		
		if ( (files != null) &&
			 (files.size() > 0) )
		{
			for ( Iterator it = files.iterator(); it.hasNext(); )
			{
				Object obj = it.next();
				if ( !(obj instanceof String) )
					continue;
				
				String pluginPath = (String)obj;
				if ( hasChildren(pluginPath) )		// Simulate File.isDirectory()
				{
					folderList.add( pluginPath );
				}
			}
		}

		return folderList;
	}
	
	/**
	 * Simulate File.isDirectory()
	 * @param path - the relative path to check
	 * @return whether the element specified by path has children or not.
	 */
	private boolean hasChildren( String path )
	{
		Set children = context.getResourcePaths( path );
		return ( (children != null) &&
				 (children.size() > 0) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getInputStream( String folder, String fileName )
	 */
	public InputStream getInputStream( String folder, String fileName ) 
	{
		InputStream in = null;		

		String file =  folder + fileName;
		in = context.getResourceAsStream( file );
	
		return in;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getURL( String folder, String fileName )
	 */
	public URL getURL( String folder, String fileName ) 
	{		
		URL url = null;

		try 
		{
			String realPath = context.getRealPath(folder + fileName);
			if (realPath == null)
			{
				url = context.getResource(folder + fileName);
				log.log(Level.FINE, "getResource({0}, {1}) returns {2}", new Object[]{ folder , fileName, realPath});
			}
			else
			{
				url = new File(realPath).toURL();
				log.log(Level.FINE, "getRealPath({0}, {1}) returns {2}", new Object[]{folder , fileName, realPath});
			}
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url;
	}

}
