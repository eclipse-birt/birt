/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class SvgFile {

	private static Logger logger = Logger.getLogger(SvgFile.class.getName());

	private static final String URL_IMAGE_TYPE_SVG = "image/svg+xml";
	private static final String URL_PROTOCOL_TYPE_DATA = "data:";
	private static final String URL_PROTOCOL_TYPE_DATA_BASE = ";base64,";
	private static final String URL_PROTOCOL_TYPE_DATA_UTF8 = ";utf8,";

	static boolean isSvg = false;

	public static boolean isSvg(String uri) {
		if (uri != null && (uri.endsWith(".svg") || uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG))) {
			isSvg = true;
		} else {
			isSvg = false;
		}
		return isSvg;
	}

	public static boolean isSvg(String mimeType, String uri, String extension) {
		isSvg = ((mimeType != null)
				&& mimeType.equalsIgnoreCase(URL_IMAGE_TYPE_SVG)) // $NON-NLS-1$
				|| ((uri != null)
						&& (uri.toLowerCase().endsWith(".svg") || uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG))) //$NON-NLS-1$
				|| ((extension != null) && extension.toLowerCase().endsWith(".svg")); //$NON-NLS-1$
		return isSvg;
	}

	public static byte[] transSvgToArray(String uri) throws Exception {
		byte[] data = null;

		if (uri != null && uri.toLowerCase().contains(URL_PROTOCOL_TYPE_DATA)
				&& uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
			String encodedImg = null;
			String decodedImg = null;
			if (uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
				String svgSplitter = "svg\\+xml,";
				if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_UTF8)) {
					svgSplitter = "svg\\+xml;utf8,";
				} else if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_BASE)) {
					svgSplitter = "svg\\+xml;base64,";
				}
				String[] uriParts = uri.split(svgSplitter);
				if (uriParts.length >= 2) {
					encodedImg = uriParts[1];
					decodedImg = encodedImg;
					if (uri.contains(URL_PROTOCOL_TYPE_DATA_BASE)) {
						decodedImg = new String(
								Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8)));
					}
				}
			}
			try {
				decodedImg = java.net.URLDecoder.decode(decodedImg, StandardCharsets.UTF_8);
			} catch (IllegalArgumentException iae) {
				// do nothing
			}
			data = SvgFile.transSvgToArray(new ByteArrayInputStream(decodedImg.getBytes()));
		} else {
			InputStream in = new URL(uri).openStream();
			try (in) {
				data = transSvgToArray(in);
			}
		}
		return data;
	}

	public static byte[] transSvgToArray(InputStream inputStream) throws Exception {
		PNGTranscoder transcoder = new PNGTranscoder();
		// create the transcoder input
		TranscoderInput input = new TranscoderInput(inputStream);
		// create the transcoder output
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(ostream);
		transcoder.transcode(input, output);
		// flush the stream
		ostream.flush();
		// use the output stream as Image input stream.
		return ostream.toByteArray();
	}
}
