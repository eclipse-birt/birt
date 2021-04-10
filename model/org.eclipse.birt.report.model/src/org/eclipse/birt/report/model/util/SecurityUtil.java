/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;

/**
 *
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
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.getProperty(name, def);
			}
		});
	}

	/**
	 * Returns the system properties with the access controller support.
	 * 
	 * @return the system properties
	 */

	public static Properties getSystemProperties() {
		return AccessController.doPrivileged(new PrivilegedAction<Properties>() {

			public Properties run() {
				return System.getProperties();
			}
		});
	}

	/**
	 * Returns the URI schema part with the access controller support.
	 * 
	 * @param f the file instance
	 * @return the URI schema part
	 */

	public static String getFiletoURISchemaPart(final File f) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				URI uri = f.toURI();
				if (uri != null)
					return uri.getScheme();
				return null;
			}
		});
	}

	/**
	 * Returns the file absolute path with the access controller support.
	 * 
	 * @param f the file instance
	 * @return the absolute path
	 */

	public static String getFileAbsolutePath(final File f) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return f.getAbsolutePath();
			}
		});
	}

	/**
	 * Returns the file absolute path with the access controller support.
	 * 
	 * @param f the file instance
	 * @return the absolute path
	 */

	public static File getAbsoluteFile(final File f) {

		return AccessController.doPrivileged(new PrivilegedAction<File>() {

			public File run() {
				return f.getAbsoluteFile();
			}
		});

	}

	/**
	 * Returns the file canonical path with the access controller support.
	 * 
	 * @param f the file instance
	 * @return the canonical path
	 */

	public static File getCanonicalFile(final File f) {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<File>() {

				public File run() throws IOException {
					return f.getAbsoluteFile();
				}
			});
		} catch (PrivilegedActionException e) {
			return null;
		}
	}

	/**
	 * Returns the file URI.
	 * 
	 * @param f the file instance
	 * @return the URI
	 */

	public static URI fileToURI(final File f) {
		return AccessController.doPrivileged(new PrivilegedAction<URI>() {

			public URI run() {
				return f.toURI();
			}
		});
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

			return AccessController.doPrivileged(new PrivilegedExceptionAction<FileOutputStream>() {

				public FileOutputStream run() throws FileNotFoundException {
					return new FileOutputStream(f, false);
				}
			});
		} catch (PrivilegedActionException e) {
			throw (FileNotFoundException) e.getException();
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
		Boolean exists = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				boolean exists = f.exists();
				return Boolean.valueOf(exists);
			}
		});
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
		Boolean exists = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				boolean exists = f.isFile();
				return Boolean.valueOf(exists);
			}
		});
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
		Boolean exists = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			public Boolean run() {
				boolean exists = f.isDirectory();
				return Boolean.valueOf(exists);
			}
		});
		return exists.booleanValue();
	}
}
