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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class FolderArchiveWriter implements IDocArchiveWriter {

	private static Logger logger = Logger.getLogger(FolderArchiveWriter.class.getName());
	private String folderName;
	private IStreamSorter streamSorter = null;
	private HashSet<RAFolderInputStream> inputStreams = new HashSet<>();
	private HashSet<RAFolderOutputStream> outputStreams = new HashSet<>();

	/**
	 * @param absolute fileName the archive file name
	 */
	public FolderArchiveWriter(String folderName) throws IOException {
		if (folderName == null || folderName.length() == 0) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FOLDER_NAME_IS_NULL));
		}

		File fd = new File(folderName);
		if (!fd.exists()) {
			fd.mkdirs();
		}
		this.folderName = fd.getCanonicalPath(); // make sure the file name is an
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	@Override
	public void initialize() {
		// Do nothing
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
		String path = getFilePath(relativePath);
		File fd = new File(path);

		ArchiveUtil.createParentFolder(fd);

		RAFolderOutputStream out = new RAFolderOutputStream(outputStreams, fd);
		return out;
	}

	@Override
	public RAOutputStream openRandomAccessStream(String relativePath) throws IOException {
		String path = getFilePath(relativePath);
		File fd = new File(path);

		ArchiveUtil.createParentFolder(fd);
		RAFolderOutputStream out = new RAFolderOutputStream(outputStreams, fd, true);
		return out;
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
		String path = getFilePath(relativePath);

		File file = new File(path);
		if (file.exists()) {
			RAFolderInputStream in = new RAFolderInputStream(inputStreams, file);
			return in;
		}
		throw new FileNotFoundException(relativePath);
	}

	/**
	 * Delete a stream from the archive and make sure the stream has been closed.
	 *
	 * @param relativePath - the relative path of the stream
	 * @return whether the delete operation was successful
	 * @throws IOException
	 */
	@Override
	public boolean dropStream(String relativePath) {
		String path = getFilePath(relativePath);
		File fd = new File(path);
		return removeFileAndFolder(fd);
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
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	@Override
	public boolean exists(String relativePath) {
		String path = getFilePath(relativePath);
		File fd = new File(path);
		return fd.exists();
	}

	@Override
	public void setStreamSorter(IStreamSorter streamSorter) {
		this.streamSorter = streamSorter;
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

	public void close() throws IOException {
		IOException exception = null;
		synchronized (outputStreams) {
			ArrayList<RAFolderOutputStream> outputs = new ArrayList<>(outputStreams);
			for (RAFolderOutputStream output : outputs) {
				try {
					output.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
					if (exception != null) {
						exception = ex;
					}
				}
			}
			outputStreams.clear();
		}
		synchronized (inputStreams) {
			ArrayList<RAFolderInputStream> inputs = new ArrayList<>(inputStreams);
			for (RAFolderInputStream input : inputs) {
				try {
					input.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
					if (exception != null) {
						exception = ex;
					}
				}
			}
			inputStreams.clear();
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * Convert the current folder archive to file archive. The original folder
	 * archive will NOT be removed.
	 *
	 * @param fileArchiveName
	 * @throws IOException
	 */
	public void toFileArchive(String fileArchiveName) throws IOException {
		ArchiveUtil.archive(folderName, streamSorter, fileArchiveName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	@Override
	public void flush() throws IOException {
		IOException ioex = null;
		synchronized (outputStreams) {

			for (RAOutputStream output : outputStreams) {
				try {
					output.flush();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
					if (ioex != null) {
						ioex = ex;
					}
				}

			}
		}
		if (ioex != null) {
			throw ioex;
		}
	}

	/**
	 * delete file or folder with its sub-folders and sub-files
	 *
	 * @param file file/folder which need to be deleted
	 * @return if files/folders can not be deleted, return false, or true
	 */
	private boolean removeFileAndFolder(File file) {
		assert (file != null);
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					removeFileAndFolder(children[i]);
				}
			}
		}
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	@Override
	public Object lock(String stream) throws IOException {
		String path = getFilePath(stream) + ".lck";
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

	/**
	 * return a list of strings which are the relative path of streams
	 */
	@Override
	public List<String> listStreams(String relativeStoragePath) throws IOException {
		ArrayList<String> streamList = new ArrayList<>();
		String storagePath = ArchiveUtil.getFullPath(folderName, relativeStoragePath);
		File dir = new File(storagePath);

		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isFile()) {
						String relativePath = ArchiveUtil.getEntryName(folderName, file.getPath());
						if (!ArchiveUtil.needSkip(relativePath)) {
							streamList.add(relativePath);
						}
					}
				}
			}
		}

		return streamList;
	}

	@Override
	public List<String> listAllStreams() throws IOException {
		ArrayList<File> list = new ArrayList<>();
		ArchiveUtil.listAllFiles(new File(folderName), list);

		ArrayList<String> streams = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			File file = list.get(i);
			String relativePath = ArchiveUtil.getEntryName(folderName, file.getPath());
			if (!ArchiveUtil.needSkip(relativePath)) {
				streams.add(relativePath);
			}
		}
		return streams;
	}

	@Override
	public IArchiveFile getArchiveFile() {
		throw new UnsupportedOperationException("getArchiveFile is not supported on this FolderAchiveWriter");
	}

	private String getFilePath(String entryName) {
		return ArchiveUtil.getFilePath(folderName, entryName);
	}
}
