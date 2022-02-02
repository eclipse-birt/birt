/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.birt.core.archive.FolderArchiveFile;
import org.eclipse.birt.core.archive.cache.SystemCacheManager;
import org.eclipse.birt.core.archive.compound.v3.Ext2FileSystem;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;

/**
 * change the default format to ext2.
 */
public class ArchiveFile implements IArchiveFile {

	public static final boolean enableSystemCache = true;
	public static final SystemCacheManager systemCacheManager = new SystemCacheManager(1024);
	public static final boolean enableFileCache = true;
	public static final int FILE_CACHE_SIZE = 1024;

	static final long ARCHIVE_V2_TAG = ArchiveConstants.DOCUMENT_TAG;
	static final long ARCHIVE_V3_TAG = Ext2FileSystem.EXT2_MAGIC_TAG;

	/**
	 * the archive file name.
	 */
	protected String archiveName;

	protected String systemId;

	protected boolean zipOnClose;
	protected String tmpFileName;
	protected static File tmpFileFolder = null;

	protected IArchiveFile af;

	public ArchiveFile(String fileName, String mode) throws IOException {
		// set blank string as the default system id of the archive file.
		this(null, fileName, mode);
	}

	public ArchiveFile(String systemId, String fileName, String mode) throws IOException {
		if (fileName == null || fileName.length() == 0)
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_NAME_IS_NULL));

		File fd = new File(fileName);
		// make sure the file name is an absolute path
		fileName = fd.getCanonicalPath();
		this.archiveName = fileName;
		this.systemId = systemId;
		if ("r".equals(mode)) {
			openArchiveForReading();
		} else if ("rw+".equals(mode)) {
			openArchiveForAppending();
		} else if ("rwz".equals(mode)) {
			// create a zip file
			zipOnClose = true;
			tmpFileName = getTmpFileName();
			ArchiveFileV3 f3 = new ArchiveFileV3(tmpFileName, "rw");
			f3.setSystemId(systemId);
			this.af = f3;
		} else if ("rwf".equals(mode)) {
			FolderArchiveFile f = new FolderArchiveFile(fileName);
			f.setSystemId(systemId);
			this.af = f;
		} else {
			// rwt, rw mode
			ArchiveFileV3 f3 = new ArchiveFileV3(fileName, mode);
			f3.setSystemId(systemId);
			this.af = f3;
		}
	}

	protected void openArchiveForReading() throws IOException {
		// test if we need upgrade the document
		RandomAccessFile rf = new RandomAccessFile(archiveName, "r");
		try {
			long magicTag = rf.readLong();
			if (magicTag == ARCHIVE_V2_TAG) {
				ArchiveFileV2 v2 = new ArchiveFileV2(archiveName, rf, "r");
				upgradeSystemId(v2);
				af = v2;
			} else if (magicTag == ARCHIVE_V3_TAG) {
				ArchiveFileV3 fs = new ArchiveFileV3(archiveName, rf, "r");
				upgradeSystemId(fs);
				af = fs;
			} else if (isZipFile(magicTag)) {
				tmpFileName = getTmpFileName();
				unzip(archiveName, tmpFileName);
				ArchiveFileV3 fs = new ArchiveFileV3(tmpFileName, "r");
				af = fs;
			} else {
				af = new ArchiveFileV1(archiveName, rf);
			}
		} catch (IOException ex) {
			rf.close();
			throw ex;
		}
	}

	protected void openArchiveForAppending() throws IOException {
		// we need upgrade the document
		RandomAccessFile rf = new RandomAccessFile(archiveName, "rw");
		if (rf.length() == 0) {
			// this is a empty file
			af = new ArchiveFileV3(archiveName, rf, "rw");
		} else {
			try {
				long magicTag = rf.readLong();
				if (magicTag == ARCHIVE_V2_TAG) {
					af = new ArchiveFileV2(archiveName, rf, "rw+");
				} else if (magicTag == ARCHIVE_V3_TAG) {
					af = new ArchiveFileV3(archiveName, rf, "rw+");
				} else if (isZipFile(magicTag)) {
					rf.close();
					zipOnClose = true;
					tmpFileName = getTmpFileName();
					unzip(archiveName, tmpFileName);
					ArchiveFileV3 fs = new ArchiveFileV3(tmpFileName, "rw+");
					af = fs;
				} else {
					rf.close();
					upgradeArchiveV1();
					af = new ArchiveFileV3(archiveName, "rw+");
				}
				upgradeSystemId(af);
			} catch (IOException ex) {
				rf.close();
				throw ex;
			}
		}
	}

	/**
	 * get the archive name.
	 * 
	 * the archive name is the file name used to create the archive instance.
	 * 
	 * @return archive name.
	 */
	public String getName() {
		return archiveName;
	}

	public String getDependId() {
		return af.getDependId();
	}

	public String getSystemId() {
		return systemId;
	}

	/**
	 * close the archive.
	 * 
	 * all changed data will be flushed into disk if the file is opened for write.
	 * 
	 * the file will be removed if it is opend as transient.
	 * 
	 * after close, the instance can't be used any more.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (isArchiveFileAvailable(af)) {
			af.close();
			af = null;
			if (tmpFileName != null) {
				if (zipOnClose) {
					zip(tmpFileName, archiveName);
				}
				new File(tmpFileName).delete();
				tmpFileName = null;
			}
		}
	}

	public void setCacheSize(long cacheSize) {
		if (isArchiveFileAvailable(af)) {
			af.setCacheSize(cacheSize);
		}
	}

	public long getUsedCache() {
		if (isArchiveFileAvailable(af)) {
			return af.getUsedCache();
		}
		return 0;
	}

	static public long getTotalUsedCache() {
		return (long) systemCacheManager.getUsedCacheSize() * 4096;
	}

	static public void setTotalCacheSize(long size) {
		long blockCount = (size + 4095) / 4096;
		if (blockCount > Integer.MAX_VALUE) {
			systemCacheManager.setMaxCacheSize(Integer.MAX_VALUE);
		} else {
			systemCacheManager.setMaxCacheSize((int) blockCount);
		}
	}

	public void saveAs(String fileName) throws IOException {
		ArchiveFileV3 file = new ArchiveFileV3(fileName, "rw");
		try {
			file.setSystemId(systemId);
			List<String> entries = listEntries("/");
			for (String name : entries) {
				ArchiveEntry tgt = file.createEntry(name);
				try {
					ArchiveEntry src = openEntry(name);
					try {
						copyEntry(src, tgt);
					} finally {
						src.close();
					}
				} finally {
					tgt.close();
				}
			}
		} finally {
			file.close();
		}
	}

	/**
	 * save the file. If the file is transient file, after saving, it will be
	 * converts to normal file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		if (isArchiveFileAvailable(af)) {
			af.save();
			/*
			 * if ( af instanceof ArchiveFileV2 ) { ( (ArchiveFileV2) af ).save( ); } else {
			 * af.flush( ); }
			 */
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	private void copyEntry(ArchiveEntry src, ArchiveEntry tgt) throws IOException {
		byte[] b = new byte[4096];
		long length = src.getLength();
		long pos = 0;
		while (pos < length) {
			int size = src.read(pos, b, 0, 4096);
			tgt.write(pos, b, 0, size);
			pos += size;
		}
	}

	synchronized public void flush() throws IOException {
		if (isArchiveFileAvailable(af)) {
			af.flush();
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	synchronized public void refresh() throws IOException {
		if (isArchiveFileAvailable(af)) {
			af.refresh();
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	synchronized public boolean exists(String name) {
		if (isArchiveFileAvailable(af)) {
			return af.exists(name);
		}
		return false;
	}

	synchronized public ArchiveEntry openEntry(String name) throws IOException {
		if (isArchiveFileAvailable(af)) {
			return af.openEntry(name);
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	synchronized public List<String> listEntries(String namePattern) {
		if (isArchiveFileAvailable(af)) {
			return af.listEntries(namePattern);
		} else {
			return Collections.emptyList();
		}
	}

	synchronized public ArchiveEntry createEntry(String name) throws IOException {
		if (isArchiveFileAvailable(af)) {
			return af.createEntry(name);
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	synchronized public boolean removeEntry(String name) throws IOException {
		if (isArchiveFileAvailable(af)) {
			return af.removeEntry(name);
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	public Object lockEntry(String name) throws IOException {
		return af.lockEntry(name);
	}

	public void unlockEntry(Object locker) throws IOException {
		if (isArchiveFileAvailable(af)) {
			af.unlockEntry(locker);
		} else {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
	}

	public long getLength() {
		return af.getLength();
	}

	/**
	 * upgrade the archive file to the latest version
	 * 
	 * @throws IOException
	 */
	private void upgradeArchiveV1() throws IOException {
		ArchiveFileV1 reader = new ArchiveFileV1(archiveName);
		try {
			String tempFileName = getTmpFileName();
			ArchiveFile writer = new ArchiveFile(tempFileName, "rwt");
			List<String> streams = reader.listEntries("");
			for (String name : streams) {
				ArchiveEntry src = reader.openEntry(name);
				try {
					ArchiveEntry tgt = writer.createEntry(name);
					try {
						copyEntry(src, tgt);
					} finally {
						tgt.close();
					}
				} finally {
					src.close();
				}
			}
			writer.saveAs(archiveName);
			writer.close();
			new File(tempFileName).delete();
		} finally {
			reader.close();
		}
	}

	/**
	 * upgrade systemId when open/append the current file
	 * 
	 * @param file
	 */
	private void upgradeSystemId(IArchiveFile file) {
		if (systemId == null) {
			systemId = file.getSystemId();
		}
	}

	/**
	 * @param af ArchiveFile
	 * @return whether the ArchiveFile instance is available
	 */
	private boolean isArchiveFileAvailable(IArchiveFile af) {
		return af != null;
	}

	private String getTmpFileName() throws IOException {
		if (tmpFileFolder != null) {
			return File.createTempFile("temp_", ".archive", tmpFileFolder) //$NON-NLS-1$ //$NON-NLS-2$
					.getCanonicalPath();
		}
		return File.createTempFile("temp_", ".archive").getCanonicalPath(); //$NON-NLS-1$//$NON-NLS-2$
	}

	private boolean isZipFile(long magic) {
		byte[] bytes = new byte[8];
		IOUtil.longToBytes(magic, bytes);
		if (bytes[0] == 31 && bytes[1] == -117) {
			return true;
		}
		return false;
	}

	private void zip(String src, String tgt) throws IOException {
		FileInputStream fi = new FileInputStream(src);
		try {
			FileOutputStream fo = new FileOutputStream(tgt);
			try {
				GZIPOutputStream gzip = new GZIPOutputStream(fo);
				byte[] bytes = new byte[4096];
				int size = fi.read(bytes);
				while (size >= 0) {
					gzip.write(bytes, 0, size);
					size = fi.read(bytes);
				}
				gzip.close();
			} finally {
				fo.close();
			}
		} finally {
			fi.close();
		}
	}

	protected void unzip(String src, String tgt) throws IOException {
		FileInputStream fi = new FileInputStream(src);
		try {
			FileOutputStream fo = new FileOutputStream(tgt);
			try {
				GZIPInputStream gzip = new GZIPInputStream(fi);
				byte[] bytes = new byte[4096];
				int size = gzip.read(bytes);
				while (size >= 0) {
					fo.write(bytes, 0, size);
					size = gzip.read(bytes);
				}
				gzip.close();
			} finally {
				fo.close();
			}
		} finally {
			fi.close();
		}
	}

	/**
	 * Sets the temporary file folder to contain temporary files. This folder should
	 * be maintained by caller to clean up. If not set, default temporary file
	 * folder will be used as defined by JDK. See javadoc in
	 * {@link File#createTempFile(String, String, File)}
	 * 
	 * @param folderPath folder path
	 */
	public static void setTempFileFolder(String folderPath) {
		tmpFileFolder = folderPath == null ? null : new File(folderPath);
	}
}
