/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.core.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class FileSecurity {
	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean createNewFile(final File file) throws IOException {
		if (file == null)
			return false;

		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {

				public Boolean run() throws IOException {
					return file.createNewFile();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			return false;
		}
	}

	/**
	 * 
	 * @param path
	 * @param type
	 * @return
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static RandomAccessFile createRandomAccessFile(final String path, final String type)
			throws FileNotFoundException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<RandomAccessFile>() {

				public RandomAccessFile run() throws FileNotFoundException {
					return new RandomAccessFile(path, type);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @param type
	 * @return
	 * @throws FileNotFoundException
	 */
	public static RandomAccessFile createRandomAccessFile(final File file, final String type)
			throws FileNotFoundException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<RandomAccessFile>() {

				public RandomAccessFile run() throws FileNotFoundException {
					return new RandomAccessFile(file, type);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			return null;
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileReader createFileReader(final File file) throws FileNotFoundException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<FileReader>() {

				public FileReader run() throws FileNotFoundException {
					return new FileReader(file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileOutputStream createFileOutputStream(final File file) throws FileNotFoundException, DataException {
		return createFileOutputStream(file, false);
	}

	/**
	 * 
	 * @param file
	 * @param append
	 * @return
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileOutputStream createFileOutputStream(final File file, final boolean append)
			throws FileNotFoundException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<FileOutputStream>() {

				public FileOutputStream run() throws FileNotFoundException {
					return new FileOutputStream(file, append);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileInputStream createFileInputStream(final File file) throws FileNotFoundException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() {

				public FileInputStream run() throws FileNotFoundException {
					return new FileInputStream(file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileExist(final File file) {
		if (file == null)
			return false;
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return file.exists();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileIsFile(final File file) {
		if (file == null)
			return false;
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return file.isFile();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static File[] fileListFiles(final File file) {
		if (file == null)
			return new File[0];
		return AccessController.doPrivileged(new PrivilegedAction<File[]>() {

			public File[] run() {
				return file.listFiles();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileIsDirectory(final File file) {
		if (file == null)
			return false;
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return file.isDirectory();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String fileGetAbsolutePath(final File file) {
		if (file == null)
			return null;
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return file.getAbsolutePath();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static String fileGetCanonicalPath(final File file) throws IOException, DataException {
		if (file == null)
			return null;
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

				public String run() throws IOException {
					return file.getCanonicalPath();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			throw new DataException(e.getMessage());

		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileDelete(final File file) {
		if (file == null)
			return true;
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return file.delete();
			}
		});
	}

	/**
	 * 
	 * @param file
	 */
	public static void fileDeleteOnExit(final File file) {
		if (file == null)
			return;
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			public Object run() {
				file.deleteOnExit();
				return null;
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileMakeDirs(final File file) {
		if (file == null)
			return false;
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				return file.mkdirs();
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static File fileGetCanonicalFile(final File file) throws IOException, DataException {
		if (file == null)
			return null;
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<File>() {

				public File run() throws IOException {
					return file.getCanonicalFile();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			throw new DataException(e.getMessage());

		}
	}
}
