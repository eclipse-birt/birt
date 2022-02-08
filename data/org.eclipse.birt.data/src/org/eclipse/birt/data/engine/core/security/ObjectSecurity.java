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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class ObjectSecurity {
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static ObjectInputStream createObjectInputStream(final InputStream is) throws IOException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {

				public ObjectInputStream run() throws IOException {
					return new ObjectInputStream(is);
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

	public static ObjectInputStream createObjectInputStream(final InputStream is, final ClassLoader classLoader)
			throws IOException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {

				public ObjectInputStream run() throws IOException {
					return new ObjectInputStream(is) {

						protected Class resolveClass(ObjectStreamClass desc)
								throws IOException, ClassNotFoundException {
							return Class.forName(desc.getName(), false, classLoader);
						}
					};
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
	 * @param os
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static ObjectOutputStream createObjectOutputStream(final OutputStream os) throws IOException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectOutputStream>() {

				public ObjectOutputStream run() throws IOException {
					return new ObjectOutputStream(os);
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
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(final ObjectInputStream is)
			throws IOException, DataException, ClassNotFoundException {
		try {
			if (is == null)
				return null;
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

				public Object run() throws IOException, ClassNotFoundException {
					return is.readObject();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			} else if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}

			throw new DataException(e.getMessage());
		}
	}
}
