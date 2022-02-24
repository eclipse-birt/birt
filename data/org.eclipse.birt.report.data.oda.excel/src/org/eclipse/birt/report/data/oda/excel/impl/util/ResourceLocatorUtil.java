/*************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - added support of relative file path
 *  Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.fs.FileSystemFactory;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

public class ResourceLocatorUtil {
	private static Logger logger = Logger.getLogger(ResourceLocatorUtil.class.getName());

	public static URI resolvePath(Object resourceIdentifiers, String path) throws OdaException {
		URI uri = null;
		File f = null;
		try {
			f = new File(path);
		} catch (Exception ignore) {
		}
		if (f != null && f.isAbsolute() && f.exists()) {
			uri = f.toURI();
			logger.log(Level.FINER, "Excel source folder exists on local file system. Using path: " + uri);
			return uri;
		}

		logger.log(Level.FINER, "Try resolving URI and relative path: " + path);
		try {
			try {
				uri = new URI(path);
			} catch (URISyntaxException ex) {
				uri = new URI(null, null, path, null);
			}

			logger.log(Level.FINER, "Resolved Excel source URI: " + uri);

			if (uri.isAbsolute()) {
				logger.log(Level.FINER, "Excel data source file URI is resolved as the absolute path: " + uri);
				return uri;
			} else if (!uri.isAbsolute() && resourceIdentifiers != null) {
				uri = ResourceIdentifiers.resolveApplResource(resourceIdentifiers, uri);
				logger.log(Level.FINER, "Relative URI resolved as the absolute path: " + uri);
				return uri;
			} else {
				String errMsg = Messages.getString("connection_missingResourceIdentifier") + uri; //$NON-NLS-1$
				logger.log(Level.SEVERE, errMsg);
				throw new OdaException(errMsg);
			}
		} catch (URISyntaxException e1) {
			OdaException odaEx = new OdaException(Messages.getString("connection_invalidSource")); //$NON-NLS-1$
			odaEx.initCause(e1);
			throw odaEx;
		}
	}

	public static void validateFileURI(Object obj) throws Exception {

		InputStream stream = null;
		try {
			stream = getURIStream(obj);
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignore) {
				}
			}

		}
	}

	public static InputStream getURIStream(Object obj) throws IOException {
		if (obj instanceof File) {
			return new BufferedInputStream(new FileInputStream((File) obj));
		}

		else if (obj instanceof URI) {
			return FileSystemFactory.getInstance().getFile((URI) obj).createInputStream();
		}

		return null;
	}

}
