/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * The utility class to handle Unicode encoding and signatures.
 *
 */

public class UnicodeUtil {
	/**
	 * The signature for the UTF-8 files.
	 */

	public final static String SIGNATURE_UTF_8 = "UTF-8"; //$NON-NLS-1$

	/**
	 * The signature for the Unicode Big Endian files.
	 */

	public final static String SIGNATURE_UNICODE_BIG = "UnicodeBig"; //$NON-NLS-1$

	/**
	 * The signature for the Unicode Little Endian files.
	 */

	public final static String SIGNATURE_UNICODE_LITTLE = "UnicodeLittle"; //$NON-NLS-1$

	/**
	 * The signature for the UCS4 Big Endian files.
	 */

	public final static String SIGNATURE_UCS4_BIG = "UCS-4Big"; //$NON-NLS-1$

	/**
	 * The signature for the UCS4 Little Endian files.
	 */

	public final static String SIGNATURE_UCS4_LITTLE = "UCS-4Little"; //$NON-NLS-1$

	/**
	 * Reads a signature if it applies. Currently BIRT can recognize the following
	 * signatures:
	 *
	 * <ul>
	 * <li>SIGNATURE_UTF_8</li>
	 * <li>SIGNATURE_UNICODE_BIG</li>
	 * <li>SIGNATURE_UNICODE_LITTLE</li>
	 * <li>SIGNATURE_UCS4_BIG_8</li>
	 * <li>SIGNATURE_UCS4_LITTLE</li>
	 * </ul>
	 *
	 * @param inputStream the input stream of the unicode file
	 *
	 * @return the encoding signature from the unicode file.
	 *
	 * @throws IOException if errors occur when reading bytes from the input stream
	 */

	public static String checkUTFSignature(InputStream inputStream) throws IOException {

		inputStream.mark(1024);

		int byte1 = inputStream.read();
		int byte2 = inputStream.read();
		if (byte1 == 0xFE && byte2 == 0xFF) {
			return SIGNATURE_UNICODE_BIG;
		} else if (byte1 == 0xFF && byte2 == 0xFE) {

			return SIGNATURE_UNICODE_LITTLE;
		}

		int byte3 = inputStream.read();

		// check for UTF-8 byte order mark

		if (byte1 == 0xEF && byte2 == 0xBB && byte3 == 0xBF) {
			return SIGNATURE_UTF_8;
		}

		int byte4 = inputStream.read();
		if (byte1 == 0x00 && byte2 == 0x00 && byte3 == 0xFE && byte4 == 0xFF) {
			return SIGNATURE_UCS4_BIG;
		} else if (byte1 == 0x00 && byte2 == 0x00 && byte3 == 0xFF && byte4 == 0xFE) {
			return SIGNATURE_UCS4_LITTLE;
		}

		inputStream.reset();
		return null;
	}

}
