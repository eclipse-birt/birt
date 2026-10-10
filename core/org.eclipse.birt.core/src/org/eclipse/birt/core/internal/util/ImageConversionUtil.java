/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.core.internal.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts images in formats that some image consumers of BIRT cannot read
 * (SWT, OpenPDF, Apache POI) to PNG. The format is recognized by the content,
 * since URL and expression images often come without a MIME type.
 *
 * The only such format is WebP, which is recognized by its RIFF header and
 * decoded with the TwelveMonkeys ImageIO plugin. The plugin is an optional
 * dependency. This class never names its types, so it stays usable without it:
 * {@link #convertUnsupportedFormat(byte[])} then returns its input unchanged and
 * the caller fails on the WebP bytes as it did before.
 *
 * @since 4.26
 */
public final class ImageConversionUtil {

	private static final Logger logger = Logger.getLogger(ImageConversionUtil.class.getName());

	private static final int WEBP_HEADER_LENGTH = 12;

	private static final String WEBP_DECODE_ERROR = "Cannot decode the WebP image"; //$NON-NLS-1$

	private ImageConversionUtil() {
	}

	/**
	 * Converts image data in a format that is not supported everywhere, such as
	 * WebP, to PNG with the same pixel size, and returns any other data unchanged.
	 * Only the first frame of an animated image is kept.
	 *
	 * @param data the image data, may be <code>null</code>
	 * @return the PNG image data if the data is in such a format and could be
	 *         decoded, otherwise the data itself
	 */
	public static byte[] convertUnsupportedFormat(byte[] data) {
		if (!isWebp(data)) {
			return data;
		}
		try {
			return WebpDecoder.toPng(data);
		} catch (IOException | RuntimeException | LinkageError e) {
			logger.log(Level.WARNING, WEBP_DECODE_ERROR, e);
			return data;
		}
	}

	/**
	 * Reads the pixel size of an image in one of the formats that
	 * {@link #convertUnsupportedFormat(byte[])} converts, without converting it.
	 *
	 * @param data the image data, may be <code>null</code>
	 * @return the width and the height, or <code>null</code> if the data is not in
	 *         such a format or could not be read
	 */
	public static int[] getSize(byte[] data) {
		if (!isWebp(data)) {
			return null;
		}
		try {
			return WebpDecoder.getSize(data);
		} catch (IOException | RuntimeException | LinkageError e) {
			logger.log(Level.WARNING, WEBP_DECODE_ERROR, e);
			return null;
		}
	}

	/**
	 * Checks whether the data is a WebP image, which is a RIFF container whose form
	 * type is "WEBP".
	 */
	private static boolean isWebp(byte[] data) {
		return data != null && data.length >= WEBP_HEADER_LENGTH && data[0] == 'R' && data[1] == 'I'
				&& data[2] == 'F' && data[3] == 'F' && data[8] == 'W' && data[9] == 'E' && data[10] == 'B'
				&& data[11] == 'P';
	}
}
