/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.extension.qrcode;

import org.eclipse.birt.extension.qrcode.util.SwtGraphicsUtil;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * QRCodeFigure
 */
public class QRCodeFigure extends Figure {

	private String lastText;
	private int lastDotsWidth;
	private String lastEncoding;
	private String lastErrorCorrectionLevel;
	private int lastQrVersion;

	private Image cachedImage;

	private QRCodeItem qrItem;

	QRCodeFigure(QRCodeItem qrItem) {
		super();

		this.qrItem = qrItem;
	}

	@Override
	public Dimension getMinimumSize(int hint, int hint2) {
		return getPreferredSize(hint, hint2);
	}

	@Override
	public Dimension getPreferredSize(int hint, int hint2) {

		int dotsWidth = qrItem.getDotsWidth();

		if (getBorder() != null) {
			Insets bdInsets = getBorder().getInsets(this);

			return new Dimension(dotsWidth + bdInsets.getWidth(), dotsWidth + bdInsets.getHeight());
		}
		return new Dimension(dotsWidth, dotsWidth);
	}

	@Override
	protected void paintClientArea(Graphics graphics) {
		final Rectangle r = getClientArea().getCopy();

		String text = qrItem.getText();
		int dotsWidth = qrItem.getDotsWidth();
		String encoding = qrItem.getEncoding();
		String errorCorrectionLevel = qrItem.getErrorCorrectionLevel();
		int qrVersion = qrItem.getQrVersion();

		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		if (encoding == null) {
			encoding = ""; //$NON-NLS-1$
		}
		if (errorCorrectionLevel == null) {
			errorCorrectionLevel = ""; //$NON-NLS-1$
		}

		if (!text.equals(lastText) || dotsWidth != lastDotsWidth || encoding != lastEncoding || cachedImage == null
				|| !errorCorrectionLevel.equals(lastErrorCorrectionLevel) || qrVersion != lastQrVersion
				|| cachedImage.isDisposed()) {
			lastText = text;
			lastDotsWidth = dotsWidth;
			lastEncoding = encoding;
			lastErrorCorrectionLevel = errorCorrectionLevel;
			lastQrVersion = qrVersion;

			if (cachedImage != null && !cachedImage.isDisposed()) {
				cachedImage.dispose();
			}

			cachedImage = SwtGraphicsUtil.createQRCodeImage(text, dotsWidth, dotsWidth, encoding, errorCorrectionLevel,
					qrVersion);
		}

		if (cachedImage != null && !cachedImage.isDisposed()) {
			graphics.drawImage(cachedImage, r.x, r.y);
		}
	}

	void setQRCodeItem(QRCodeItem item) {
		this.qrItem = item;
	}

	void dispose() {
		if (cachedImage != null && !cachedImage.isDisposed()) {
			cachedImage.dispose();
		}
	}
}
