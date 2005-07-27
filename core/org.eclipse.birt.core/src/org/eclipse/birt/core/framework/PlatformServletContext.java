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
	public List getFileList( String homeFolder, String subFolder, boolean includingFiles, boolean relativeFileList )
	{
		List folderList = new ArrayList();
		
		String folderString = homeFolder;
		if ( (subFolder != null) && 
			 (subFolder.length() > 0) &&
			 !subFolder.equals("/") )
		{
			folderString = concatFolderString( folderString, subFolder );
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
				if ( includingFiles ||
					 hasChildren(pluginPath) )		// Simulate File.isDirectory()
				{
					String pathString = pluginPath;
					if ( relativeFileList )
					{
						// We assume the first part of pathString is homeFolder.
						pathString = pathString.substring( homeFolder.length() );
						if ( !pathString.startsWith(directorySeparator) )
							pathString = directorySeparator + pathString; 
					}
					folderList.add( pathString );
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

		String file =  concatFolderString( folder, fileName );
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
			String fullFileString = concatFolderString(folder, fileName);
			String realPath = context.getRealPath( fullFileString );		
			if (realPath == null)
			{
				/* We can not use context.getResource, because,
				 * although ServletContext.getResource can return a URL, in WAR deployment, 
				 * the URL (e.g. zip:D:/bea8.1/user_projects/domains/mydomain/myserver/upload/birt.war!/WEB-INF/plugins/org.eclipse.birt.report.engine.emitter.html/htmlEmitter.jar) 
				 * can not be recognized by URLClassLoader. So we decided to use the full URL path like 
				 * http://localhost:7001/birt/plugins/org.eclipse.birt.report.engine.emitter.html/htmlEmitter.jar instead,
				 * which can be used by URLClassLoader can load the class. 
				 * The first part (http://localhost:7001/birt) will be provided by viewer and passed into the engine. 
				 * The engine will append the second part to make it a full valid URL.
				 */				 
				url = new URL( urlLeadingString + fullFileString );
				
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

	private String concatFolderString( String parentString, String childString )
	{
		if ( parentString == null )
			return childString;
		else if ( childString == null )
			return parentString;
		
		if ( !parentString.endsWith(directorySeparator) )
		{
		 	parentString += directorySeparator;
		}
		if ( childString.startsWith(directorySeparator) )
		{
		 	childString = childString.substring(1);
		}
	     
	    return parentString + childString; 
	}
}
