package org.eclipse.birt.build.framework;

import java.io.File;
import java.io.IOException;
import java.util.List;

abstract public class BundleFile {

	/**
	 * The File object for this BundleFile.
	 */
	protected File basefile;

	protected Bundle bundle;

	/**
	 * BundleFile constructor
	 * 
	 * @param basefile The File object where this BundleFile is persistently stored.
	 */
	public BundleFile(Bundle bundle, File basefile) {
		this.bundle = bundle;
		this.basefile = basefile;
	}

	public Bundle getBundle() {
		return bundle;
	}

	abstract public BundleEntry getEntry(String path);

	abstract public List<String> getEntryPaths(String path);

	abstract public void close() throws IOException;

	abstract public boolean isDirectory(String dir);

	/**
	 * Returns the base file for this BundleFile
	 * 
	 * @return the base file for this BundleFile
	 */
	public File getBaseFile() {
		return basefile;
	}

	public String toString() {
		return String.valueOf(basefile);
	}

	protected String normalizeFile(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		if (path.charAt(0) == '/') {
			return path.substring(1);
		}
		return path;
	}

	protected String normalizeFolder(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		// test if it starts with /
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}
		// test if it ends with '/'
		if (path.length() > 0 && path.charAt(path.length() - 1) != '/') {
			path = path + "/";
		}
		return path;
	}
}
