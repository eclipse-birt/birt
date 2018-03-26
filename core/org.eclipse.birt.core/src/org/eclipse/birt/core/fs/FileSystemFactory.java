/*******************************************************************************
 * Copyright (c) 2018 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.fs;

import java.io.File;
import java.net.URI;

import org.eclipse.birt.core.framework.Platform;

/**
 * Default implementation of File System Factory. It provides support for local
 * file. It also allows to support any file system, as long as extension point
 * <code>IFileSystemFactory.EXTENSION_FILE_SYSTEM_FACTORY</code> is found.
 */

public class FileSystemFactory implements IFileSystemFactory
{

	private static volatile IFileSystemFactory instance;

	/**
	 * Gets the instance of file system factory. If extension is not found,
	 * default implementation of local file system will be used.
	 * 
	 * @return instance of file system factory.
	 */
	public static IFileSystemFactory getInstance( )
	{
		if ( instance == null )
		{
			synchronized ( FileSystemFactory.class )
			{
				if ( instance == null )
				{
					Object factory = Platform.createFactoryObject(
							IFileSystemFactory.EXTENSION_FILE_SYSTEM_FACTORY );
					if ( factory instanceof IFileSystemFactory )
					{
						instance = (IFileSystemFactory) factory;
					}
					else
					{
						// Use default one if no extension found
						instance = new FileSystemFactory( );
					}
				}
			}
		}
		return instance;
	}

	@Override
	public IFile getFile( String fileName )
	{
		return new LocalFile( new File( fileName ) );
	}

	@Override
	public IFile getFile( URI uri )
	{
		return getFile( uri.toString( ) );
	}

}
