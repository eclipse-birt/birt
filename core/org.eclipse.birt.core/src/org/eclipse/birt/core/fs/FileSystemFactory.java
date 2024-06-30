/*******************************************************************************
 * Copyright (c) 2018 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.fs;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.framework.Platform;

/**
 * Default implementation of File System Factory. It provides support for local
 * file. It also allows to support any file system, as long as extension point
 * <code>IFileSystemFactory.EXTENSION_FILE_SYSTEM_FACTORY</code> is found.
 */

public class FileSystemFactory implements IFileSystemFactory {

	private static volatile IFileSystemFactory instance;

	/**
	 * Gets the instance of file system factory. If extension is not found, default
	 * implementation of local file system will be used.
	 *
	 * @return instance of file system factory.
	 */
	public static IFileSystemFactory getInstance() {
		if (instance == null) {
			synchronized (FileSystemFactory.class) {
				if (instance == null) {
					Object factory = Platform.createFactoryObject(IFileSystemFactory.EXTENSION_FILE_SYSTEM_FACTORY);
					if (factory instanceof IFileSystemFactory) {
						instance = (IFileSystemFactory) factory;
					} else {
						// Use default one if no extension found
						instance = new FileSystemFactory();
					}
				}
			}
		}
		return instance;
	}

	@Override
	public IFile getFile(String fileName) {
		return new LocalFile(new File(fileName));
	}

	@Override
	public IFile getFile(URI uri) {
		return new LocalFile(uri);
	}

	@Override
	public IArchiveFile createArchiveFile(String systemId, String fileName, String mode, IFile externalFile)
			throws IOException {
		return new ArchiveFile(systemId, fileName, mode);
	}
}
