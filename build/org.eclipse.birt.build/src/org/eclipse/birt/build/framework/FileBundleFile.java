package org.eclipse.birt.build.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBundleFile extends BundleFile {

	public FileBundleFile(Bundle bundle, File basefile) throws IOException {
		super(bundle, basefile);
	}

	public BundleEntry getEntry(String path) {
		path = normalizeFile(path);
		File file = new File(this.basefile, path);
		if (!file.exists()) {
			return null;
		}
		return new FileBundleEntry(this, file, path);
	}

	public boolean isDirectory(String path) {
		path = normalizeFolder(path);
		File dirPath = new File(this.basefile, path);
		if (dirPath.exists() && dirPath.isDirectory()) {
			return true;
		}
		return false;
	}

	public List<String> getEntryPaths(String path) {
		path = normalizeFolder(path);
		File pathFile = new File(basefile, path);
		if (pathFile.exists() && pathFile.isDirectory()) {
			String[] fileList = pathFile.list();
			if (fileList == null || fileList.length == 0)
				return null;

			ArrayList<String> entries = new ArrayList<String>();
			for (String file : fileList) {
				File childFile = new File(pathFile, file);
				StringBuffer sb = new StringBuffer(path).append(file);
				if (childFile.isDirectory()) {
					sb.append("/");
				}
				entries.add(sb.toString());
			}
			return entries;
		}
		return null;
	}

	public void close() {
	}
}
