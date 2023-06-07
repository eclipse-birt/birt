/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.model.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Properties;

/**
 * @version 4.14 remove the AccessController due to deprecated usage
 */
public class SecurityUtil {

	/**
	 * Returns the system property with the access controller support.
	 *
	 * @param name the name of the system property.
	 * @param def  a default value.
	 * @return the string value of the system property, or the default value if
	 *         there is no property with that name.
	 */

	public static String getSystemProperty(final String name, final String def) {
		return System.getProperty(name, def);
	}

	/**
	 * Returns the system properties with the access controller support.
	 *
	 * @return the system properties
	 */
	public static Properties getSystemProperties() {
		return System.getProperties();
	}

	/**
	 * Returns the URI schema part with the access controller support.
	 *
	 * @param f the file instance
	 * @return the URI schema part
	 */
	public static String getFiletoURISchemaPart(final File f) {
		URI uri = f.toURI();
		if (uri != null) {
			return uri.getScheme();
		}
		return null;
	}

	/**
	 * Returns the file absolute path with the access controller support.
	 *
	 * @param f the file instance
	 * @return the absolute path
	 */
	public static String getFileAbsolutePath(final File f) {
		return f.getAbsolutePath();
	}

	/**
	 * Returns the file absolute path with the access controller support.
	 *
	 * @param f the file instance
	 * @return the absolute path
	 */

	public static File getAbsoluteFile(final File f) {
		return f.getAbsoluteFile();
	}

	/**
	 * Returns the file canonical path with the access controller support.
	 *
	 * @param f the file instance
	 * @return the canonical path
	 */
	public static File getCanonicalFile(final File f) {
		return f.getAbsoluteFile();
	}

	/**
	 * Returns the file URI.
	 *
	 * @param f the file instance
	 * @return the URI
	 */
	public static URI fileToURI(final File f) {
		return f.toURI();
	}

	/**
	 * Returns the file output stream with the given file instance.
	 *
	 * @param f the file instance
	 * @return the output stream
	 * @throws FileNotFoundException if the file exists but is a directory rather
	 *                               than a regular file, does not exist but cannot
	 *                               be created, or cannot be opened for any other
	 *                               reason
	 */
	public static FileOutputStream createFileOutputStream(final File f) throws FileNotFoundException {
		try {
			return new FileOutputStream(f, false);
		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * Tests whether the file or directory denoted by this abstract pathname exists.
	 *
	 * @param f the file instance
	 * @return true if and only if the file or directory denoted by this abstract
	 *         pathname exists; false otherwise
	 */
	public static boolean exists(final File f) {
		Boolean exists = f.exists();
		return exists.booleanValue();
	}

	/**
	 * Tests whether the file denoted by this abstract pathname is a normal file.
	 *
	 * @param f the file instance
	 * @return true if and only if the file denoted by this abstract pathname exists
	 *         and is a normal file; false otherwise
	 */
	public static boolean isFile(final File f) {
		Boolean exists = f.isFile();
		return exists.booleanValue();
	}

	/**
	 * Tests whether the file denoted by this abstract pathname is a directory.
	 *
	 * @param f the file instance
	 * @return true if and only if the file denoted by this abstract pathname exists
	 *         and is a directory; false otherwise
	 */

	public static boolean isDirectory(final File f) {
		Boolean exists = f.isDirectory();
		return exists.booleanValue();
	}
}
