/*******************************************************************************
 * Copyright (c) 2018 Actuate Corporation.
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

package org.eclipse.birt.core.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * File interface is used to define common methods to access file in various
 * file system.
 */

public interface IFile {

	/**
	 * Creates input stream for current file.
	 *
	 * @return input stream.
	 * @throws IOException
	 */
	InputStream createInputStream() throws IOException;

	/**
	 * Creates output stream for current file.
	 *
	 * @return output stream.
	 * @throws IOException
	 */
	OutputStream createOutputStream() throws IOException;

	/**
	 * Returns file name of current file.
	 *
	 * @return file name
	 */
	String getName();

	/**
	 * Returns file path of current file.
	 *
	 * @return file path
	 */
	String getPath();

	/**
	 * Checks if current file exists.
	 *
	 * @return true if file exists, otherwise false.
	 * @throws IOException
	 */
	boolean exists() throws IOException;

	/**
	 * Deletes current file.
	 *
	 * @return true if deletion is successful, otherwise false.
	 * @throws IOException
	 */
	boolean delete() throws IOException;

	/**
	 * Creates folder for current file. If it exists, do nothing.
	 *
	 * @return true if creation is successful, otherwise false.
	 * @throws IOException
	 */
	boolean mkdirs() throws IOException;

	/**
	 * Checks if current file object is a directory.
	 *
	 * @return true if current file object is directory, otherwise false.
	 * @throws IOException
	 */
	boolean isDirectory() throws IOException;

	/**
	 * Checks if current file path is absolute.
	 *
	 * @return true if file path is absolute, otherwise false.
	 */
	boolean isAbsolute();

	/**
	 * Returns the parent folder.
	 *
	 * @return parent folder
	 */
	IFile getParent();

	/**
	 * Returns all files under current folder.
	 *
	 * @return array of files in current folder.
	 * @throws IOException
	 */
	IFile[] listFiles() throws IOException;

	/**
	 * Converts file path to URL.
	 *
	 * @return URL of current file.
	 * @throws IOException
	 */
	URL toURL() throws IOException;
}
