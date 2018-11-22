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

import java.net.URI;

/**
 * File system factory interface defines method to create <code>IFile</code>. It
 * allows to extend BIRT to support any file system.
 */

public interface IFileSystemFactory
{

	/**
	 * ID for File System Factory extension.
	 */
	static String EXTENSION_FILE_SYSTEM_FACTORY = IFileSystemFactory.class
			.getName( );

	/**
	 * Creates file object according to file path.
	 * 
	 * @param fileName
	 *            file path
	 * @return file object
	 */
	IFile getFile( String fileName );

	/**
	 * Creates file object according to file URI.
	 * 
	 * @param uri
	 *            file URI
	 * @return file object
	 */
	IFile getFile( URI uri );
}
