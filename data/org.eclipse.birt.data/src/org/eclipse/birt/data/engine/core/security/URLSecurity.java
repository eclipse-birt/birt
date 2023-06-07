
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
	 * @return Return the URL
	 * @throws MalformedURLException
	 * @throws DataException
	 */
	public static URL getURL(final String protocol, final String host, final String file)
			throws MalformedURLException, DataException {
		try {
			return new URL(protocol, host, file);
		} catch (Exception typedException) {
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
			throw new DataException(typedException.getLocalizedMessage());
		}

	}

	/**
	 *
	 * @param spec
	 * @return Return the URL
	 * @throws MalformedURLException
	 * @throws DataException
	 */
	public static URL getURL(final String spec) throws MalformedURLException, DataException {
		try {
			return new URL(spec);
		} catch (Exception typedException) {
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
			throw new DataException(typedException.getLocalizedMessage());
		}

	}
}
