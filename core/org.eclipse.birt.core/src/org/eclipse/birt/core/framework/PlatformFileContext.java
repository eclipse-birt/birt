/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.framework.IPlatformContext;

/**
 * An platform context that is based on file operations.
 * Since in web environment WAR deployment, absolute file path is not available. In this case, user should NOT use this class.
 * In this case, user should use PlatformServletContext or develop his own PlatformContext to make sure reousce operation are used. 
 */
public class PlatformFileContext implements IPlatformContext
{	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getFolderList( String homeFolder, String subFolder )
	 */
	public List getFolderList( String homeFolder, String subFolder ) 
	{
		List folderList = new ArrayList();
		
		File directory = null;
		if ( (subFolder != null) &&
			 (subFolder.length() >0) )
		{
			directory = new File( homeFolder, subFolder ); 
		}
		else
		{
			directory = new File( homeFolder );
		}
				
		// Get all folders in the directory
		File[] files = directory.listFiles( );	
		// Put all folders (absolute paths) into folderList
		for ( int i = 0; i < files.length; i++ )
		{
			if ( (files[i] != null) && 
					files[i].isDirectory() )
			{
				folderList.add( files[i].getAbsolutePath() );
			}
		}

		return folderList;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getInputStream( String folder, String fileName )
	 */
	public InputStream getInputStream( String folder, String fileName ) 
	{
		InputStream in = null;

		File file = new File( new File(folder), fileName );
		if ( file.exists() )
		{
			try 
			{
				in = new FileInputStream( file );
			} 
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return in;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.framework.IPlatformContext#getURL( String folder, String fileName )
	 */
	public URL getURL( String folder, String fileName ) 
	{		
		URL url = null;
	
		File file = new File( folder, fileName );
		if ( file.exists( ) )
		{
			try
			{
				url = file.toURL();
			}
			catch ( MalformedURLException e )
			{
				e.printStackTrace();
				assert false; //never occurs
			}
		}

		return url;
	}

}
