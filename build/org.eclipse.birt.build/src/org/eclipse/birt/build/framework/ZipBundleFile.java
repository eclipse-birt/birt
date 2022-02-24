/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.build.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipBundleFile extends BundleFile {

	protected ZipFile zipFile;

	public ZipBundleFile(Bundle bundle, File basefile) throws IOException {
		super(bundle, basefile);
		zipFile = new ZipFile(basefile);
	}

	protected ZipFile getZipFile() {
		return zipFile;
	}

	protected ZipEntry getZipEntry(String path) {
		ZipEntry entry = zipFile.getEntry(path);
		if (entry != null && entry.getSize() == 0 && !entry.isDirectory()) {
			ZipEntry dirEntry = zipFile.getEntry(path + '/');
			if (dirEntry != null)
				entry = dirEntry;
		}
		return entry;
	}

	public synchronized boolean isDirectory(String dir) {
		dir = normalizeFolder(dir);

		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry zipEntry = zipEntries.nextElement();
			String entryPath = zipEntry.getName();
			if (entryPath.startsWith(dir)) {
				return true;
			}
		}
		return false;
	}

	public BundleEntry getEntry(String path) {
		path = normalizeFile(path);

		ZipEntry zipEntry = getZipEntry(path);
		if (zipEntry == null) {
			if (path.charAt(path.length() - 1) == '/') {
				// this is a directory request lets see if any entries exist in
				// this directory
				if (isDirectory(path))
					return new ZipDirBundleEntry(this, path);
			}
			return null;
		}

		return new ZipBundleEntry(this, zipEntry);
	}

	public List<String> getEntryPaths(String path) {
		if (zipFile == null) {
			return null;
		}

		path = normalizeFile(path);

		ArrayList<String> entries = new ArrayList<String>();
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry zipEntry = zipEntries.nextElement();
			String entryPath = zipEntry.getName();
			if (entryPath.startsWith(path)) {
				if (path.length() < entryPath.length()) {
					if (entryPath.lastIndexOf('/') < path.length()) {
						entries.add(entryPath);
					} else {
						entryPath = entryPath.substring(path.length());
						int slash = entryPath.indexOf('/');
						entryPath = path + entryPath.substring(0, slash + 1);
						if (!entries.contains(entryPath))
							entries.add(entryPath);
					}
				}
			}
		}
		return entries;
	}

	public synchronized void close() throws IOException {
		if (zipFile != null) {
			zipFile.close();
			zipFile = null;
		}
	}
}
