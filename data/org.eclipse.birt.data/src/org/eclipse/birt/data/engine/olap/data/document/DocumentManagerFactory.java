/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.document;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;

/**
 * A factory class used to create the instance of IDocumentManager.
 */

public class DocumentManagerFactory {

	private static final String tmpPath = System.getProperty("java.io.tmpdir");
	private static final String DEFAULT_CUB_MANAGER_NAME = "cub1";

	static {
		File tmp = new File(tmpPath);
		if (!FileSecurity.fileExist(tmp)) {
			FileSecurity.fileMakeDirs(tmp);
		}
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 */
	static public IDocumentManager createDirectoryDocumentManager(boolean deleteOldDocument) throws DataException {
		return new DirectoryDocumentManager(tmpPath, deleteOldDocument);
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager createFileDocumentManager() throws DataException, IOException {
		return FileDocumentManager.createManager(tmpPath, DEFAULT_CUB_MANAGER_NAME);
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager createFileDocumentManager(String tempDir) throws DataException, IOException {
		return FileDocumentManager.createManager(tempDir, DEFAULT_CUB_MANAGER_NAME);
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager loadFileDocumentManager() throws DataException, IOException {
		return FileDocumentManager.loadManager(tmpPath, DEFAULT_CUB_MANAGER_NAME);
	}

	/**
	 *
	 * @param docArchiveWriter
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager createRADocumentManager(IDocArchiveReader reader) throws DataException, IOException {
		return new RADocumentManager(reader);
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 */
	static public IDocumentManager createDirectoryDocumentManager(boolean deleteOldDocument, String dirName)
			throws DataException {
		return new DirectoryDocumentManager(dirName, deleteOldDocument);
	}

	/**
	 *
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager createFileDocumentManager(String dirName, String managerName)
			throws DataException, IOException {
		return FileDocumentManager.createManager(dirName, managerName);
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @param cacheSize
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager createFileDocumentManager(String dirName, String managerName, int cacheSize)
			throws DataException, IOException {
		return FileDocumentManager.createManager(dirName, managerName, cacheSize);
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static public IDocumentManager loadFileDocumentManager(String dirName, String managerName)
			throws DataException, IOException {
		return FileDocumentManager.loadManager(dirName, managerName);
	}
}
