package org.eclipse.birt.build.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipBundleEntry extends BundleEntry {

	protected final ZipEntry zipEntry;

	ZipBundleEntry(ZipBundleFile bundleFile, ZipEntry zipEntry) {
		super(bundleFile, zipEntry.getName());
		this.zipEntry = zipEntry;
	}

	/**
	 * Return an InputStream for the entry.
	 * 
	 * @return InputStream for the entry
	 * @exception java.io.IOException
	 */
	public InputStream getInputStream() throws IOException {
		return ((ZipBundleFile) bundleFile).getZipFile().getInputStream(zipEntry);
	}

	public int getSize() {
		return (int) zipEntry.getSize();
	}

	public long getTime() {
		return zipEntry.getTime();
	}

}
