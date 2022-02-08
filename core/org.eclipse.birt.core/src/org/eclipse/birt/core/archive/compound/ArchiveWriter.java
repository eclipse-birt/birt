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

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.IStreamSorter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;

public class ArchiveWriter implements IDocArchiveWriter {

	boolean shareArchive;
	IArchiveFile archive;

	public ArchiveWriter(String archiveName) throws IOException {
		archive = new ArchiveFile(archiveName, "rw");
		shareArchive = false;
	}

	public ArchiveWriter(IArchiveFile archive) throws IOException {
		this.archive = archive;
		shareArchive = true;
	}

	/**
	 * @deprecated use getArchiveFile instead
	 * @return
	 */
	public IArchiveFile getArchive() {
		return archive;
	}

	public IArchiveFile getArchiveFile() {
		return archive;
	}

	public RAOutputStream createRandomAccessStream(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry = archive.createEntry(relativePath);
		return new ArchiveEntryOutputStream(entry);
	}

	public RAOutputStream openRandomAccessStream(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry;
		if (archive.exists(relativePath)) {
			entry = archive.openEntry(relativePath);
		} else {
			entry = archive.createEntry(relativePath);
		}
		return new ArchiveEntryOutputStream(entry);
	}

	public RAOutputStream createOutputStream(String relativePath) throws IOException {
		return createRandomAccessStream(relativePath);
	}

	public RAOutputStream getOutputStream(String relativePath) throws IOException {
		return openRandomAccessStream(relativePath);
	}

	public RAInputStream getInputStream(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry = archive.openEntry(relativePath);
		return new ArchiveEntryInputStream(entry);
	}

	public boolean dropStream(String relativePath) {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		try {
			return archive.removeEntry(relativePath);
		} catch (IOException ex) {
			return false;
		}
	}

	public boolean exists(String relativePath) {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		return archive.exists(relativePath);
	}

	public void finish() throws IOException {
		try {
			// flush the archvies
			archive.flush();
		} finally {
			if (!shareArchive) {
				archive.close();
			}
		}
	}

	public void flush() throws IOException {
		archive.flush();
	}

	public String getName() {
		return archive.getName();
	}

	public void initialize() throws IOException {
	}

	public void setStreamSorter(IStreamSorter streamSorter) {
	}

	public Object lock(String relativePath) throws IOException {
		if (!relativePath.startsWith(ArchiveUtil.UNIX_SEPERATOR))
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		return archive.lockEntry(relativePath);
	}

	public void unlock(Object locker) {
		try {
			archive.unlockEntry(locker);
		} catch (IOException ex) {
		}
	}

	public List<String> listAllStreams() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(archive.listEntries("/"));
		return list;
	}

	public List<String> listStreams(String namePattern) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
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
}
