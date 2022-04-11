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

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * SwtGraphicsUtil
 */
public class SwtGraphicsUtil {

	public static Image createQRCodeImage(String text, int dotsWidth, int dotsHeight, String encoding,
			String errorCorrectionLevel, int qrVersion) {
		try {
			if (text == null || text.trim().length() == 0) {
				return null;
			}

			return renderQRObject(text, dotsWidth, dotsHeight, encoding, errorCorrectionLevel, qrVersion);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	private static byte[] toByteArray(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();

		int bytesPerRow = (width + 7) / 8;
		byte[] out = new byte[height * bytesPerRow];
		int offset = 0;
		BitArray row = new BitArray(width);
		for (int y = 0; y < height; y++) {
			row = matrix.getRow(y, row);
			row.toBytes(0, out, offset, bytesPerRow);
			offset += bytesPerRow;
		}
		return out;
	}

	private static Image renderQRObject(String text, int width, int height, String encoding, String errorCorrectionLevel,
			int qrVersion) {
		Display display = Display.getCurrent();
		QRCodeWriter qrw = null;

		Image dest = null;
		GC gc = null;

		try {
			dest = new Image(display, width, height);
			gc = new GC(dest);

			gc.setAdvanced(true);
			gc.setAntialias(SWT.OFF);

			qrw = new QRCodeWriter();
			HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, encoding != null ? encoding : "utf-8");
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
			BitMatrix bm = qrw.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

			byte[] rawData = toByteArray(bm);
			org.eclipse.swt.graphics.PaletteData swtPalette = new PaletteData(new RGB(0xff, 0xff, 0xff),
					new RGB(0, 0, 0));
			int depth = 1;
			org.eclipse.swt.graphics.ImageData swtImageData = new ImageData(width, height, depth, swtPalette, 1,
					rawData);
			swtImageData.transparentPixel = 0;
			dest = new Image(Display.getDefault(), swtImageData);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (gc != null && !gc.isDisposed()) {
				gc.dispose();
			}

		}

		return dest;
	}
}
