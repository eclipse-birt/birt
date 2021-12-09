/*******************************************************************************
 * Copyright (c) 2021 Solme AB and others.
 *
 * All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Claes Rosell - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.engine.util;

import org.eclipse.core.runtime.Assert;

/**
 * @since 3.3
 *
 */
public class DataProtocolUtil {

	/**
	 * The data protocol scheme
	 */
	public static final String DATA_PROTOCOL = "data:"; //$NON-NLS-1$

	private static final char ENCODING_AND_PARAMETER_START_CHAR = ';'; /* Also used for media type parameters */

	private static final char ENCODING_TERMINATION_CHAR = ',';

	private static final String DEFAULT_MEDIA_TYPE = "text/plain;charset=US-ASCII"; //$NON-NLS-1$

	/**
	 * Simple parsing of a URL with a "data protocol" Format is:
	 * data:[<mediatype>][;base64],<data>
	 *
	 * default media type, if not specified is "text/plain;charset=US-ASCII"
	 *
	 * @param url URL to be parsed as a string. Does not except null values
	 *
	 * @throws IllegalArgumentException if url is not a data: URL
	 *
	 * @return Parsed data
	 */
	public static DataUrlInfo parseDataUrl(String url)
	{
		Assert.isNotNull(url);

		if (url != null && url.startsWith(DATA_PROTOCOL)) {
			String nonDataPart = ""; //$NON-NLS-1$
			String mimeTypePart = DEFAULT_MEDIA_TYPE;
			String dataPart = ""; //$NON-NLS-1$
			String encodingPart = null;

			int posOfDataMarker = url.indexOf(ENCODING_TERMINATION_CHAR);

			if (posOfDataMarker > -1) {
				nonDataPart = url.substring(DATA_PROTOCOL.length(), posOfDataMarker);
				dataPart = url.substring(posOfDataMarker + 1);


				int startSearchPos = nonDataPart.length() - 1;
				int mimeTypeEndPos = nonDataPart.lastIndexOf(ENCODING_AND_PARAMETER_START_CHAR, startSearchPos);
				if (mimeTypeEndPos > -1) {
					mimeTypePart = nonDataPart.substring(0, mimeTypeEndPos);
					encodingPart = nonDataPart.substring(mimeTypeEndPos + 1);

					/*
					 * Check if encoding part includes '=', if so, it is a parameter to the mimeType
					 * and not an encoding
					 */
					int equalsPos = encodingPart.indexOf('=');
					if (equalsPos > -1) {
						mimeTypePart = mimeTypePart + ENCODING_AND_PARAMETER_START_CHAR + encodingPart;
						encodingPart = null;
					}
				} else {
					mimeTypePart = nonDataPart;
				}

				return new DataUrlInfo(mimeTypePart, encodingPart, dataPart);
			}
		}

		throw new IllegalArgumentException("URL is not of right format"); //$NON-NLS-1$
	}

	/**
	 * @since 3.3
	 *
	 *        Data structure containing the parts of a parsed data: url.
	 */
	public static class DataUrlInfo {
		private String mediaType;
		private String encoding;
		private String data;

		/**
		 * @param mime
		 * @param encoding
		 * @param data
		 */
		DataUrlInfo(String mediaType, String encoding, String data) {
			this.mediaType = mediaType;
			this.encoding = encoding;
			this.data = data;
		}

		/**
		 * @return The Media type.
		 */
		public String getMediaType() {
			return this.mediaType;
		}

		/**
		 * Returns the encoding used or null if the data is not encoded
		 *
		 * @return The encoding.
		 */
		public String getEncoding() {
			return this.encoding;
		}

		/**
		 * Returns the actual data on the URL. This data is encoded if
		 * {@link #DataProtocolUtil.getEncoding()} is returning something else then null
		 *
		 * @return The data as a string.
		 *
		 */
		public String getData() {
			return this.data;
		}

	}

}
