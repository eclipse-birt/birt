package org.eclipse.birt.core.archive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFileV3;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;

public class FolderArchiveFile implements IArchiveFile {

	private static final String METEDATA = ".metadata";
	private static Logger logger = Logger.getLogger(FolderArchiveFile.class.getName());
	protected String folderName;
	protected String systemId;
	protected String dependId;
	private HashSet<RAFolderInputStream> inputStreams = new HashSet<>();
	private HashSet<RAFolderOutputStream> outputStreams = new HashSet<>();

	protected Map<String, String> properties = new HashMap<>();

	public FolderArchiveFile(String name) throws IOException {
		if (name == null || name.length() == 0) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FOLDER_NAME_IS_NULL));
		}

		File file = new File(name);
		file.mkdirs();
		this.folderName = file.getCanonicalPath();
		readMetaData();
	}

	@Override
	public String getName() {
		return folderName;
	}

	private void readMetaData() throws IOException {
		// serialize meta data into .metedata file
		String meta = ArchiveUtil.getFullPath(folderName, METEDATA);
		File file = new File(meta);
		if (file.exists() && file.isFile()) {
			DataInputStream data = new DataInputStream(new FileInputStream(file));
			try (data) {
				properties = IOUtil.readMap(data);
			}
		}
	}

	private void saveMetaData() throws IOException {
		// serialize meta data into .metedata file
		String meta = ArchiveUtil.getFullPath(folderName, METEDATA);
		File file = new File(meta);
		DataOutputStream data = new DataOutputStream(new FileOutputStream(file));
		try (data) {
			IOUtil.writeMap(data, this.properties);
		}
	}

	@Override
	public void close() throws IOException {
		saveMetaData();

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
		// ArchiveUtil.archive( folderName, null, fileName );

	}

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

	@Override
	public void refresh() throws IOException {
	}

	@Override
	public boolean exists(String name) {
		String path = getFilePath(name);
		File fd = new File(path);
		return fd.exists();
	}

	@Override
	public void setCacheSize(long cacheSize) {
	}

	@Override
	public long getUsedCache() {
		return 0;
	}

	@Override
	public ArchiveEntry openEntry(String name) throws IOException {
		String fullPath = getFilePath(name);
		File fd = new File(fullPath);
		if (fd.exists()) {
			return new FolderArchiveEntry(name, fd, inputStreams, outputStreams);
		}
		throw new FileNotFoundException(fullPath);
	}

	@Override
	public List<String> listEntries(String namePattern) {
		ArrayList<String> streamList = new ArrayList<>();
		String storagePath = getFolderPath(namePattern);

		ArrayList<File> files = new ArrayList<>();
		ArchiveUtil.listAllFiles(new File(storagePath), files);
		for (File file : files) {
			String relativePath = ArchiveUtil.getRelativePath(folderName, file.getPath());
			if (!ArchiveUtil.needSkip(relativePath)) {
				String entryName = ArchiveUtil.getEntryName(folderName, file.getPath());
				streamList.add(entryName);
			}
		}
		return streamList;
	}

	@Override
	public ArchiveEntry createEntry(String name) throws IOException {
		String path = getFilePath(name);
		File fd = new File(path);

		ArchiveUtil.createParentFolder(fd);

		FolderArchiveEntry out = new FolderArchiveEntry(name, fd, inputStreams, outputStreams);
		return out;
	}

	@Override
	public boolean removeEntry(String name) throws IOException {
		String path = getFilePath(name);
		try {
			File fd = new File(path);
			return ArchiveUtil.removeFileAndFolder(fd);
		} finally {
			synchronized (outputStreams) {
				ArrayList<RAFolderOutputStream> outputs = new ArrayList<>(outputStreams);
				for (RAFolderOutputStream output : outputs) {
					try {
						if (name.equals(output.getName())) {
							output.close();
						}
					} catch (IOException ex) {
						logger.log(Level.SEVERE, ex.getMessage(), ex);
						throw ex;
					}
				}
			}
		}

	}

	@Override
	public Object lockEntry(String entry) throws IOException {
		String path = getFilePath(entry) + ".lck";
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance();
		return lockManager.lock(path);
	}

	@Override
	public void unlockEntry(Object locker) throws IOException {
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance();
		lockManager.unlock(locker);

	}

	@Override
	public String getSystemId() {
		if (properties.containsKey(ArchiveFileV3.PROPERTY_SYSTEM_ID)) {
			return properties.get(ArchiveFileV3.PROPERTY_SYSTEM_ID).toString();
		}
		return null;
	}

	@Override
	public String getDependId() {
		if (properties.containsKey(ArchiveFileV3.PROPERTY_DEPEND_ID)) {
			return properties.get(ArchiveFileV3.PROPERTY_DEPEND_ID).toString();
		}
		return null;
	}

	public void setSystemId(String systemId) {
		if (systemId != null) {
			this.properties.put(ArchiveFileV3.PROPERTY_SYSTEM_ID, systemId);
		}
	}

	public void setDependId(String dependId) {
		if (dependId != null) {
			this.properties.put(ArchiveFileV3.PROPERTY_DEPEND_ID, dependId);
		}
	}

	@Override
	public void save() throws IOException {
		flush();
	}

	@Override
	public long getLength() {
		long result = 0;
		List<String> entries = listEntries(null);
		for (String entry : entries) {
			try {
				result += openEntry(entry).getLength();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String getFilePath(String entryName) {
		return ArchiveUtil.getFilePath(folderName, entryName);
	}

	private String getFolderPath(String entryName) {
		return ArchiveUtil.getFolderPath(folderName, entryName);
	}
}
