/*******************************************************************************
 * Copyright (c) 2005,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.ModuleHandle;

public class ResourceLocatorWrapper {

	private HashMap<URL, byte[]> cache;
	private static final byte[] DUMMY_BYTES = {};
	private static final String RESOURCE_BUNDLE = "org.eclipse.birt.report.engine.i18n.Messages";
	protected static Logger logger = Logger.getLogger(ResourceLocatorWrapper.class.getName(), RESOURCE_BUNDLE);

	public ResourceLocatorWrapper() {
		cache = new HashMap<>();
	}

	public void dispose() {
		synchronized (cache) {
			cache.clear();
			cache = null;
		}
	}

	/**
	 *
	 * @param fileName
	 * @param fileType
	 * @param appContext
	 * @return
	 */
	public byte[] findResource(ModuleHandle design, String fileName, int fileType, Map appContext) {
		URL url = design.findResource(fileName, fileType, appContext);
		if (url == null) {
			logger.log(Level.WARNING, MessageConstants.RESOURCE_NOT_ACCESSIBLE, fileName);
			return DUMMY_BYTES;
		}
		return findResource(url);
	}

	/**
	 * Finds a resource from the given URL. If the URL is not accessible, it will
	 * return a 0-size byte array.
	 */
	public byte[] findResource(URL url) {
		if (url == null) {
			return DUMMY_BYTES;
		}
		synchronized (cache) {
			if (cache == null) {
				return DUMMY_BYTES;
			}
			byte[] inBytes = cache.get(url);
			if (inBytes == null) {
				URLConnection connection = null;
				try {
					connection = url.openConnection();
					InputStream in = connection.getInputStream();
					inBytes = getByteArrayFromInputStream(in);
					in.close();
					cache.put(url, inBytes);
				} catch (IOException e) {
					LogRecord record = new LogRecord(Level.WARNING, MessageConstants.RESOURCE_NOT_ACCESSIBLE);
					record.setParameters(new Object[] { url.toExternalForm() });
					record.setLoggerName(logger.getName());
					if (connection instanceof HttpURLConnection httpConn) {
						String errorBody = extractErrorBody(httpConn);
						if (!errorBody.isEmpty()) {
							record.setThrown(new IOException(errorBody));
						}
					}
					logger.log(record);
					cache.put(url, DUMMY_BYTES);
					return DUMMY_BYTES;
				} finally {
					if (connection instanceof HttpURLConnection httpConn) {
						httpConn.disconnect();
					}
				}
			}
			return inBytes;
		}
	}

	private byte[] getByteArrayFromInputStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int size = in.read(buffer);
		while (size != -1) {
			out.write(buffer, 0, size);
			size = in.read(buffer);
		}
		buffer = out.toByteArray();
		out.close();
		return buffer;
	}

	private String extractErrorBody(HttpURLConnection httpConn) {
		try {
			InputStream errorStream = httpConn.getErrorStream();
			if (errorStream == null) {
				return "No Error-Body";
			}

			try (errorStream) {
				String body = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
				return body.length() > 1024 ? body.substring(0, 1024) + "..." : body;
			}
		} catch (Exception ex) {
			return "Can't read Error-Body: " + ex.getMessage();
		}
	}

}
