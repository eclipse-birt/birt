
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

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public class URLSecurity {
	/**
	 *
	 * @param protocol
	 * @param host
	 * @param file
	 * @return
	 * @throws MalformedURLException
	 * @throws DataException
	 */
	public static URL getURL(final String protocol, final String host, final String file)
			throws MalformedURLException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<URL>() {

				@Override
				public URL run() throws MalformedURLException {
					return new URL(protocol, host, file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
			throw new DataException(e.getLocalizedMessage());
		}

	}

	/**
	 *
	 * @param spec
	 * @return
	 * @throws MalformedURLException
	 * @throws DataException
	 */
	public static URL getURL(final String spec) throws MalformedURLException, DataException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<URL>() {

				@Override
				public URL run() throws MalformedURLException {
					return new URL(spec);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
			throw new DataException(e.getLocalizedMessage());
		}

	}
}
