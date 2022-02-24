
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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * An implementation of the <tt>IDocumentManager</tt> interface. This class
 * create document object in a disk directory. Each document object is saved in
 * a disk file.
 * 
 */

public class DirectoryDocumentManager implements IDocumentManager {
	private String documentDir = null;

	/**
	 * 
	 * @param documentDir
	 * @param deleteOld
	 * @throws DataException
	 */
	public DirectoryDocumentManager(String documentDir, boolean deleteOld) throws DataException {
		this.documentDir = documentDir;
		File dir = new File(documentDir);
		if (!FileSecurity.fileExist(dir) || !FileSecurity.fileIsDirectory(dir)) {
			if (!FileSecurity.fileMakeDirs(dir)) {
				throw new DataException(ResourceConstants.OLAPDIR_CREATE_FAIL, documentDir);
			}
		}
		if (deleteOld) {
			File[] oldFiles = FileSecurity.fileListFiles(dir);
			for (int i = 0; i < oldFiles.length; i++) {
				FileSecurity.fileDelete(oldFiles[i]);
			}
		}
	}

	public void close() throws IOException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#
	 * createDocumentObject(java.lang.String)
	 */
	public IDocumentObject createDocumentObject(String documentObjectName) throws IOException {
		File file = new File(documentDir + File.separatorChar + documentObjectName);
		if (FileSecurity.fileExist(file)) {
			return null;
		} else {
			if (!FileSecurity.createNewFile(file)) {
				return null;
			}
			return new DocumentObject(
					new BufferedRandomDataAccessObject(new SimpleRandomAccessObject(file, "rw"), 1024));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentManager#openDocumentObject(
	 * java.lang.String)
	 */
	public IDocumentObject openDocumentObject(String documentObjectName) throws IOException {
		File file = new File(documentDir + File.separatorChar + documentObjectName);
		if (!FileSecurity.fileExist(file)) {
			return null;
		}

		return new DocumentObject(new BufferedRandomDataAccessObject(new SimpleRandomAccessObject(file, "rw"), 1024));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentManager#exist(java.lang.
	 * String)
	 */
	public boolean exist(String documentObjectName) {
		File file = new File(documentDir + File.separatorChar + documentObjectName);
		return FileSecurity.fileExist(file);
	}

	public void flush() throws IOException {

	}

}
