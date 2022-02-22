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

import java.io.IOException;
import java.net.URI;

import org.eclipse.birt.core.archive.compound.IArchiveFile;

/**
 * File system factory interface defines method to create <code>IFile</code>. It
 * allows to extend BIRT to support any file system.
 */

public interface IFileSystemFactory {

	/**
	 * ID for File System Factory extension.
	 */
	String EXTENSION_FILE_SYSTEM_FACTORY = IFileSystemFactory.class.getName();

	/**
	 * Creates file object according to file path.
	 *
	 * @param fileName file path
	 * @return file object
	 */
	IFile getFile(String fileName);

	/**
	 * Creates file object according to file URI.
	 *
	 * @param uri file URI
	 * @return file object
	 */
	IFile getFile(URI uri);

	/**
	 * Creates archive file with specific arguments.
	 *
	 * @param systemId     system id
	 * @param fileName     file name
	 * @param mode         file mode
	 * @param externalFile external file link
	 * @return archive file
	 * @throws IOException
	 */
	IArchiveFile createArchiveFile(String systemId, String fileName, String mode, IFile externalFile)
			throws IOException;
}
