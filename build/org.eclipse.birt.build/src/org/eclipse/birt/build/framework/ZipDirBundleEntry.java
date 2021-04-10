package org.eclipse.birt.build.framework;

import java.io.IOException;
import java.io.InputStream;

public class ZipDirBundleEntry extends BundleEntry {

	protected ZipBundleFile zipBundleFile;

	protected ZipDirBundleEntry(ZipBundleFile bundleFile, String name) {
		super(bundleFile, name);
		this.zipBundleFile = bundleFile;
	}

	public InputStream getInputStream() throws IOException {
		return null;
	}

	public int getSize() {
		return 0;
	}

	public long getTime() {
		return 0;
	}
}
