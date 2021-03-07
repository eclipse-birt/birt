/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class ArchiveReader implements IDocArchiveReader {

	protected IArchiveFile archive;
	protected boolean shareArchive;

	public ArchiveReader(IArchiveFile archive) throws IOException {
		shareArchive = true;
		this.archive = archive;
	}

	public ArchiveReader(String archiveName) throws IOException {
		if (archiveName == null || archiveName.length() == 0) {
			throw new IllegalArgumentException(archiveName);
		}

		File fd = new File(archiveName);
		if (!fd.isFile() || !fd.exists()) {
			throw new FileNotFoundException(
					CoreMessages.getFormattedString(ResourceConstants.INVALID_ARCHIVE_NAME, archiveName));
		}

		archiveName = fd.getCanonicalPath(); // make sure the file name is an

		// absolute path

		shareArchive = false;
		archive = new ArchiveFile(archiveName, "r");
	}

	@Override
	public void close() throws IOException {
		if (!shareArchive) {
			archive.close();
		}

	}

	public IArchiveFile getArchive() {
		return archive;
	}

	@Override
	public boolean exists(String relativePath) {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR)) {
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		}
		return archive.exists(relativePath);
	}

	@Override
	public String getName() {
		return archive.getName();
	}

	@Override
	public RAInputStream getStream(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR)) {
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		}
		ArchiveEntry entry = archive.openEntry(relativePath);
		return new ArchiveEntryInputStream(entry);
	}

	@Override
	public RAInputStream getInputStream(String relativePath) throws IOException {
		return getStream(relativePath);
	}

	@Override
	public List<String> listAllStreams() throws IOException {
		ArrayList<String> list = new ArrayList<>(archive.listEntries("/"));
		return list;
	}

	@Override
	public List<String> listStreams(String namePattern) throws IOException {
		ArrayList<String> list = new ArrayList<>();
		List<String> archiveEntries = archive.listEntries(namePattern);
		for (String name : archiveEntries) {
			if (name.startsWith(namePattern) && !name.equalsIgnoreCase(namePattern)) {
				String diffString = ArchiveUtil.getRelativePath(namePattern, name);
				if (diffString.lastIndexOf(ArchiveUtil.UNIX_SEPERATOR) == 0) {
					list.add(name);
				}
			}
		}
		return list;
	}

	@Override
	public void open() throws IOException {
	}

	@Override
	public Object lock(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR)) {
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		}
		return archive.lockEntry(relativePath);
	}

	@Override
	public void unlock(Object locker) {
		try {
			archive.unlockEntry(locker);
		} catch (IOException ex) {
		}
	}
}
