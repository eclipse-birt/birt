/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.core.security.ObjectSecurity;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IIncreCacheDataSetDesign;

/**
 *
 */
public class CacheUtil {
	private static final int MAX_DIR_CREATION_ATTEMPT = 1000;
	private static final String PATH_SEP = File.separator;
	private static final String TEST_MEM_BUFFER_SIZE = "birt.data.engine.test.memcachesize";
	private static final int MAGIC_NUMBER = 2000000000;
	/**
	 * timestamp.data file will be used in incremental cache, while time.data file
	 * will be used in disk cache.
	 */
	private static final String TIME_DATA = "time.data";
	/**
	 * root directory for incremental cache, which should under the user specified
	 * cache directory.
	 */
	private static final String PS_ = "PS_";

	/**
	 * counters for creating unique temporary directory.
	 */
	private static IntegerHolder cacheCounter1 = new IntegerHolder(0);
	private static IntegerHolder cacheCounter2 = new IntegerHolder(0);

	private CacheUtil() {
	}

	// --------------------service for SmartCache----------------------
	/**
	 * @return
	 */
	public static long computeMemoryBufferSize(Map appContext) {

		// here a simple assumption, that 1M memory can accommodate 2000 rows
		if (appContext == null) {
			return 0;
		}
		if (appContext.get(TEST_MEM_BUFFER_SIZE) != null) {
			// For unit test.The unit is 1 byte.
			return populateMemBufferSize(appContext.get(TEST_MEM_BUFFER_SIZE));
		}

		// The unit is 1M.
		return populateMemBufferSize(appContext.get(DataEngine.MEMORY_BUFFER_SIZE)) * 1024 * 1024;
	}

	public static boolean enableSP3CubeQueryChange(Map appContext) {
		if (appContext == null) {
			return false;
		}

		Object propValue = appContext.get(DataEngine.MEMORY_BUFFER_SIZE);

		String targetBufferSize = propValue == null ? "0" : propValue.toString();

		long memoryCacheSize = 0;

		if (targetBufferSize != null) {
			memoryCacheSize = Long.parseLong(targetBufferSize);
		}

		return memoryCacheSize > MAGIC_NUMBER;
	}

	public static int getMaxRows(Map appContext) {
		if (appContext == null) {
			return -1;
		}
		Object maxRows = appContext.get(DataEngine.MAX_DATA_OBJECT_ROWS);
		if (maxRows != null) {
			return Integer.parseInt(maxRows.toString());
		} else {
			return -1;
		}
	}

	/**
	 *
	 * @param propValue
	 * @return
	 */
	private static long populateMemBufferSize(Object propValue) {
		String targetBufferSize = propValue == null ? "0" : propValue.toString();

		long memoryCacheSize = 0;

		if (targetBufferSize != null) {
			memoryCacheSize = Long.parseLong(targetBufferSize);
		}

		return memoryCacheSize > MAGIC_NUMBER ? (memoryCacheSize - MAGIC_NUMBER) : memoryCacheSize;
	}

	// ------------------------service for DiskCache-------------------------

	public static String createTempRootDir(String tempDir) throws DataException {
		String rootDirStr;

		// system default temp dir is used
		File tempDtEDir;
		tempDtEDir = new File(tempDir, "BirtDataTemp" + System.currentTimeMillis() + cacheCounter1.intValue());
		cacheCounter1.add(1);
		int x = 0;
		while (FileSecurity.fileExist(tempDtEDir)) {
			x++;
			tempDtEDir = new File(tempDir,
					"BirtDataTemp" + System.currentTimeMillis() + cacheCounter1.intValue() + "_" + x);
		}
		FileSecurity.fileMakeDirs(tempDtEDir);
//		FileSecurity.fileDeleteOnExit( tempDtEDir );
		rootDirStr = getCanonicalPath(tempDtEDir);
		return rootDirStr;
	}

	/**
	 * @return session temp dir
	 * @throws DataException
	 * @throws IOException
	 */
	public static String createSessionTempDir(String tempRootDir) throws DataException {

		final String prefix = "session_";
		File sessionFile;

		// Here we use complex algorithm so that to avoid the repeating of
		// dir names in 1.same jvm but different threads 2.different jvm.
		String sessionTempDir = tempRootDir + File.separator + prefix + System.currentTimeMillis()
				+ cacheCounter2.intValue();
		cacheCounter2.add(1);
		sessionFile = new File(sessionTempDir);

		int i = 0;
		String tempDir = sessionTempDir;
		while (FileSecurity.fileExist(sessionFile)) {
			i++;
			sessionTempDir = tempDir + "_" + i;
			sessionFile = new File(sessionTempDir);
			if (i > MAX_DIR_CREATION_ATTEMPT) {
				throw new DataException(ResourceConstants.FAIL_TO_CREATE_TEMP_DIR, diagnosticMkdirs(sessionFile));
			}
		}
		if (!FileSecurity.fileMakeDirs(sessionFile)) {
			throw new DataException(ResourceConstants.FAIL_TO_CREATE_TEMP_DIR, diagnosticMkdirs(sessionFile));
		}
//		FileSecurity.fileDeleteOnExit( sessionFile );
		return getCanonicalPath(sessionFile);
	}

	/**
	 *
	 * @param directory
	 * @return
	 * @throws DataException
	 */
	private static String diagnosticMkdirs(File directory) throws DataException {
		while (true) {
			File canonFile = null;
			try {
				canonFile = FileSecurity.fileGetCanonicalFile(directory);
			} catch (IOException e) {
				return FileSecurity.fileGetAbsolutePath(directory);
			}
			String parent = canonFile.getParent();
			if (parent == null) {
				return FileSecurity.fileGetAbsolutePath(directory);
			}
			directory = new File(parent);
			if (FileSecurity.fileExist(directory) || FileSecurity.fileMakeDirs(directory)) {
				try {
					return FileSecurity.fileGetCanonicalPath(canonFile);
				} catch (IOException e) {
					return FileSecurity.fileGetAbsolutePath(directory);
				}
			}
		}
	}

	/**
	 * get the canonical path without exception.
	 *
	 * @param file
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private static String getCanonicalPath(File file) throws DataException {
		try {
			return FileSecurity.fileGetCanonicalPath(file);
		} catch (IOException e) {
			return FileSecurity.fileGetAbsolutePath(file);
		}
	}

	// ------------------------service for incremental
	// cache-------------------------
	/**
	 *
	 * @param tempDir
	 * @param dataSetDesign
	 * @return
	 * @throws DataException
	 */
	public static String createIncrementalTempDir(DataEngineSession session, IIncreCacheDataSetDesign dataSetDesign)
			throws DataException {
		final String prefix = PS_;
		File cacheDir = new File(session.getTempDir() + PATH_SEP + prefix + PATH_SEP
				+ Md5Util.getMD5(dataSetDesign.getConfigFileUrl().toString()) + PATH_SEP + dataSetDesign.getName());
		if (!FileSecurity.fileExist(cacheDir)) {
			FileSecurity.fileMakeDirs(cacheDir);
		}
		return getCanonicalPath(cacheDir);
	}

	/**
	 * To get the last time doing caching or merging
	 *
	 * @param folder
	 * @return String that contains the last time doing caching or merging
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static String getLastTime(String folder) throws DataException {
		try {
			File file = new File(folder + PATH_SEP + TIME_DATA);
			if (!FileSecurity.fileExist(file)) {
				return null;
			}
			FileInputStream fis = FileSecurity.createFileInputStream(file);
			ObjectInputStream ois = ObjectSecurity.createObjectInputStream(fis);

			String lastTime = (String) ObjectSecurity.readObject(ois);

			fis.close();
			ois.close();

			return lastTime;
		} catch (IOException e) {
			throw new DataException(ResourceConstants.DATASETCACHE_SAVE_ERROR, e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			assert false;
		}
		return null;
	}

	/**
	 * To save the current time doing caching or merging
	 *
	 * @param folder
	 */
	public static void saveCurrentTime(String folder) throws DataException {
		try {
			FileOutputStream fos;

			fos = FileSecurity.createFileOutputStream(new File(folder + PATH_SEP + TIME_DATA));

			ObjectOutputStream oos = ObjectSecurity.createObjectOutputStream(fos);

			Calendar calendar = Calendar.getInstance();

			StringBuilder buffer = new StringBuilder();

			buffer.append(populate2DigitString(calendar.get(Calendar.YEAR)));
			buffer.append(populate2DigitString(calendar.get(Calendar.MONTH) + 1));
			buffer.append(populate2DigitString(calendar.get(Calendar.DATE)));
			if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
				buffer.append(populate2DigitString(calendar.get(Calendar.HOUR) + 12));
			}
			buffer.append(populate2DigitString(calendar.get(Calendar.MINUTE)));
			buffer.append(populate2DigitString(calendar.get(Calendar.SECOND)));

			oos.writeObject(buffer.toString());

			fos.close();
			oos.close();
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage());
		}

	}

	/**
	 *
	 * @param value
	 * @return
	 */
	private static String populate2DigitString(int value) {
		if (value < 10) {
			return "0" + value;
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * To get the last timestamp in incremental cache.
	 *
	 * @param folder
	 * @return String that contains the last time doing caching or merging
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static long getLastTimestamp(String folder) throws DataException {
		try {
			RandomAccessFile raf = FileSecurity
					.createRandomAccessFile(folder + PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "r");
			long timestamp = raf.readLong();
			raf.close();
			return timestamp;
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		}
	}

	/**
	 * To save the current timestamp in incremental cache.
	 *
	 * @param folder
	 */
	public static void saveCurrentTimestamp(String folder) throws DataException {
		try {
			RandomAccessFile raf = FileSecurity
					.createRandomAccessFile(folder + PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "rw");
			Calendar calendar = Calendar.getInstance();
			raf.writeLong(calendar.getTimeInMillis());
			raf.close();
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		}
	}
}

class IntegerHolder {
	private int value;

	IntegerHolder(int value) {
		this.value = value;
	}

	synchronized void add(int i) {
		this.value += i;
	}

	synchronized int intValue() {
		return this.value;
	}
}
