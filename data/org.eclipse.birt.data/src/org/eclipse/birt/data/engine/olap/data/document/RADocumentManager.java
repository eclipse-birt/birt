
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

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;

/**
 * 
 */

public class RADocumentManager implements IDocumentManager {
	private IDocArchiveReader archiveReader;

	/**
	 * 
	 * @param archiveFile
	 * @throws IOException
	 */
	RADocumentManager(IDocArchiveReader reader) throws IOException {
		this.archiveReader = reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#close()
	 */
	public void close() throws IOException {
		// archiveReader.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#
	 * createDocumentObject(java.lang.String)
	 */
	public IDocumentObject createDocumentObject(String documentObjectName) throws IOException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#exist(java.
	 * lang.String)
	 */
	public boolean exist(String documentObjectName) {
		if (archiveReader == null)
			return false;
		return archiveReader.exists(documentObjectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#
	 * openDocumentObject(java.lang.String)
	 */
	public IDocumentObject openDocumentObject(String documentObjectName) throws IOException {
		if (archiveReader == null)
			return null;
		RAInputStream inputStream = archiveReader.getStream(documentObjectName);
		if (inputStream == null)
			return null;
		return new DocumentObject(new BufferedRandomDataAccessObject(new RAReader(inputStream), 8192));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#flush()
	 */
	public void flush() throws IOException {

	}

}
