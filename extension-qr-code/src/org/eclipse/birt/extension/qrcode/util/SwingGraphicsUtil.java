/*******************************************************************************
 * Copyright (c) 2022 Henning von Bargen
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Henning von Bargen - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.extension.qrcode.util;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * SwingGraphicsUtil
 */
public class SwingGraphicsUtil {

	public static BufferedImage createQRCodeImage(String text, int dotsWidth, int dotsHeight, String encoding,
			String errorCorrectionLevel, int qrVersion) {
		QRCodeWriter qrw = null;
		try {
			if (text == null || text.trim().length() == 0) {
				return null;
			}

			qrw = new QRCodeWriter();
			HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, (encoding != null ? encoding : "utf-8"));
			if ("L".equals(errorCorrectionLevel)) {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			} else if ("H".equals(errorCorrectionLevel)) {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			} else if ("Q".equals(errorCorrectionLevel)) {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
			} else {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			}
			if (qrVersion > 0) {
				hints.put(EncodeHintType.QR_VERSION, qrVersion);
			}
			BitMatrix bm = qrw.encode(text, BarcodeFormat.QR_CODE, dotsWidth, dotsHeight, hints);
			MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0x00FFFFFF);
			// BLACK and transparent instead of white
			BufferedImage im = MatrixToImageWriter.toBufferedImage(bm, config);
			return im;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
