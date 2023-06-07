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

import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */
public class FileSecurity {
	/**
	 * Create a new file
	 *
	 * @param file
	 * @return Return the status of file creation
	 * @throws IOException
	 */
	public static boolean createNewFile(final File file) throws IOException {
		if (file == null) {
			return false;
		}
		try {
			return file.createNewFile();
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			return false;
		}
	}

	/**
	 * Create a random access file
	 *
	 * @param path
	 * @param type
	 * @return Return the created file
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static RandomAccessFile createRandomAccessFile(final String path, final String type)
			throws FileNotFoundException, DataException {
		try {
			return new RandomAccessFile(path, type);
		} catch (Exception typedException) {
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(typedException.getLocalizedMessage());
		}
	}

	/**
	 * Create a random access file
	 *
	 * @param file
	 * @param type
	 * @return Return the created file
	 * @throws FileNotFoundException
	 */
	public static RandomAccessFile createRandomAccessFile(final File file, final String type)
			throws FileNotFoundException {
		try {
			return new RandomAccessFile(file, type);
		} catch (Exception typedException) {
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			return null;
		}
	}

	/**
	 * Create a file reader
	 *
	 * @param file
	 * @return Return the file reader
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileReader createFileReader(final File file) throws FileNotFoundException, DataException {
		try {
			return new FileReader(file);
		} catch (Exception typedException) {
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(typedException.getLocalizedMessage());
		}
	}

	/**
	 * Create the file output stream
	 *
	 * @param file
	 * @return Return the file output stream
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileOutputStream createFileOutputStream(final File file) throws FileNotFoundException, DataException {
		return createFileOutputStream(file, false);
	}

	/**
	 * Create the file output stream
	 *
	 * @param file
	 * @param append
	 * @return Return the file output stream
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileOutputStream createFileOutputStream(final File file, final boolean append)
			throws FileNotFoundException, DataException {
		try {
			return new FileOutputStream(file, append);
		} catch (Exception typedException) {
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(typedException.getMessage());
		}
	}

	/**
	 * Create the file input stream
	 *
	 * @param file
	 * @return Return the file input stream
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	public static FileInputStream createFileInputStream(final File file) throws FileNotFoundException, DataException {
		try {
					return new FileInputStream(file);
				} catch (Exception typedException) {
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
			throw new DataException(typedException.getMessage());
		}
	}

	/**
	 * Check whether file exists
	 *
	 * @param file
	 * @return Return the check result
	 */
	public static boolean fileExist(final File file) {
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	/**
	 * CHeck whether the file is an existing file
	 *
	 * @param file
	 * @return Return the check result
	 */
	public static boolean fileIsFile(final File file) {
		if (file == null) {
			return false;
		}
		return file.isFile();
	}

	/**
	 * Create a list of files
	 *
	 * @param file
	 * @return Return a file array
	 */
	public static File[] fileListFiles(final File file) {
		if (file == null) {
			return new File[0];
		}
		return file.listFiles();
	}

	/**
	 * CHeck if the file is a directory
	 *
	 * @param file
	 * @return Return the check result
	 */
	public static boolean fileIsDirectory(final File file) {
		if (file == null) {
			return false;
		}
		return file.isDirectory();
	}

	/**
	 * Get the absolute file path
	 *
	 * @param file
	 * @return Return the absolute file path
	 */
	public static String fileGetAbsolutePath(final File file) {
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}

	/**
	 * Get the canonical file path
	 *
	 * @param file
	 * @return Return the canonical file path
	 * @throws IOException
	 * @throws DataException
	 */
	public static String fileGetCanonicalPath(final File file) throws IOException, DataException {
		if (file == null) {
			return null;
		}
		try {
			return file.getCanonicalPath();
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			throw new DataException(typedException.getMessage());
		}
	}

	/**
	 * Delete file
	 *
	 * @param file
	 * @return Return the delete status
	 */
	public static boolean fileDelete(final File file) {
		if (file == null) {
			return true;
		}
		return file.delete();
	}

	/**
	 * Delete file and exit
	 *
	 * @param file
	 */
	public static void fileDeleteOnExit(final File file) {
		if (file == null) {
			return;
		}
		file.deleteOnExit();
	}

	/**
	 * Create file directory
	 *
	 * @param file
	 * @return Return the directory creation status
	 */
	public static boolean fileMakeDirs(final File file) {
		if (file == null) {
			return false;
		}
		return file.mkdirs();
	}

	/**
	 * Get canonical file
	 *
	 * @param file
	 * @return Return the canonical file
	 * @throws IOException
	 * @throws DataException
	 */
	public static File fileGetCanonicalFile(final File file) throws IOException, DataException {
		if (file == null) {
			return null;
		}
		try {
			return file.getCanonicalFile();
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			throw new DataException(typedException.getMessage());
		}
	}
}
