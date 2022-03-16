/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class FolderArchive implements IDocArchiveWriter, IDocArchiveReader {
	private String folderName;
	private FolderArchiveReader reader;
	private FolderArchiveWriter writer;
	private boolean isOpen = false;

	/**
	 * @param absolute fileName the archive file name
	 */
	public FolderArchive(String folderName) throws IOException {
		if (folderName == null || folderName.length() == 0) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FOLDER_NAME_IS_NULL));
		}

		this.folderName = new File(folderName).getCanonicalPath();

		try {
			this.writer = new FolderArchiveWriter(folderName);
			this.reader = new FolderArchiveReader(folderName);
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	////////////////// Functions that are needed by IDocArchiveWriter
	////////////////// ///////////////

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	@Override
	public void initialize() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	@Override
	public String getName() {
		return folderName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java
	 * .lang.String)
	 */
	@Override
	public RAOutputStream createRandomAccessStream(String relativePath) throws IOException {
		return writer.createRandomAccessStream(relativePath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java
	 * .lang.String)
	 */
	@Override
	public RAOutputStream openRandomAccessStream(String relativePath) throws IOException {
		return writer.openRandomAccessStream(relativePath);
	}

	@Override
	public RAOutputStream createOutputStream(String relativePath) throws IOException {
		return createRandomAccessStream(relativePath);
	}

	@Override
	public RAOutputStream getOutputStream(String relativePath) throws IOException {
		return openRandomAccessStream(relativePath);
	}

	@Override
	public RAInputStream getInputStream(String relativePath) throws IOException {
		return reader.getInputStream(relativePath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveWriter#dropStream(java.lang.String)
	 */
	@Override
	public boolean dropStream(String relativePath) {
		return writer.dropStream(relativePath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String relativePath) {
		return writer.exists(relativePath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveWriter#setStreamSorter(org.eclipse.
	 * birt.core.archive.IStreamSorter)
	 */
	@Override
	public void setStreamSorter(IStreamSorter streamSorter) {
		writer.setStreamSorter(streamSorter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	@Override
	public void finish() throws IOException {
		close();
	}

	////////////////// Functions that are needed by IDocArchiveReader
	////////////////// ///////////////

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#open()
	 */
	@Override
	public void open() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveReader#getStream(java.lang.String)
	 */
	@Override
	public RAInputStream getStream(String relativePath) throws IOException {
		return reader.getStream(relativePath);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IDocArchiveReader#listStreams(java.lang.String)
	 */
	@Override
	public List<String> listStreams(String relativeStoragePath) throws IOException {
		return reader.listStreams(relativeStoragePath);
	}

	@Override
	public List<String> listAllStreams() throws IOException {
		return reader.listAllStreams();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#close()
	 */
	@Override
	public void close() throws IOException {
		if (isOpen) {
			isOpen = false;
			try {
				writer.finish();
			} finally {
				reader.close();
			}
		}
	}

	////////////////// Other FolderArchiveManager functions ///////////////

	public boolean isOpen() {
		return isOpen; // The archive will always be opened in the constructor. Do we need it?
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	@Override
	public Object lock(String stream) throws IOException {
		String path = ArchiveUtil.getFilePath(folderName, stream) + ".lck";
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance();
		return lockManager.lock(path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	@Override
	public void unlock(Object lock) {
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance();
		lockManager.unlock(lock);
	}

	@Override
	public IArchiveFile getArchiveFile() {
		throw new UnsupportedOperationException("getArchiveFile is not supported on this FolderAchiveWriter");
	}
}
