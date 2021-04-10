package org.eclipse.birt.build.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileBundleEntry extends BundleEntry {

	/**
	 * File for this entry.
	 */
	private final File file;

	public FileBundleEntry(FileBundleFile bundleFile, File file, String name) {
		super(bundleFile, name);
		this.file = file;
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	public int getSize() {
		return (int) file.length();
	}

	public long getTime() {
		return file.lastModified();
	}
}
