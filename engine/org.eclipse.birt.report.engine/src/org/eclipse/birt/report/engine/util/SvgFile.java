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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class SvgFile {

	private static Logger logger = Logger.getLogger(SvgFile.class.getName());

	static boolean isSvg = false;

	public static boolean isSvg(String uri) {
		if (uri != null && uri.endsWith(".svg")) {
			isSvg = true;
		} else {
			isSvg = false;
		}
		return isSvg;
	}

	public static boolean isSvg(String mimeType, String uri, String extension) {
		isSvg = ((mimeType != null) && mimeType.equalsIgnoreCase("image/svg+xml")) //$NON-NLS-1$
				|| ((uri != null) && uri.toLowerCase().endsWith(".svg")) //$NON-NLS-1$
				|| ((extension != null) && extension.toLowerCase().endsWith(".svg")); //$NON-NLS-1$
		return isSvg;
	}

	public static byte[] transSvgToArray(String uri) throws Exception {
		InputStream in = new URL(uri).openStream();
		try {
			return transSvgToArray(in);
		} finally {
			in.close();
		}
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
