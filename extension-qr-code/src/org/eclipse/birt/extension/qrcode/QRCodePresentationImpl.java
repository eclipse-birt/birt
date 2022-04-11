/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 *  IBM Corporation  - Bidi direction of text
 *******************************************************************************/

package org.eclipse.birt.extension.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.extension.qrcode.util.SwingGraphicsUtil;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.ReportItemPresentationBase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * QRCodePresentationImpl
 */
public class QRCodePresentationImpl extends ReportItemPresentationBase {

	private QRCodeItem qrItem;

	@Override
	public void setModelObject(ExtendedItemHandle modelHandle) {
		try {
			qrItem = (QRCodeItem) modelHandle.getReportItem();
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getOutputType() {
		return OUTPUT_AS_IMAGE;
	}

	@Override
	public Object onRowSets(IBaseResultSet[] results) throws BirtException {
		if (qrItem == null) {
			return null;
		}

		int dotsWidth = qrItem.getDotsWidth();
		String text = qrItem.getText();
		String encoding = qrItem.getEncoding();
		String errorCorrectionLevel = qrItem.getErrorCorrectionLevel();
		int qrVersion = qrItem.getQrVersion();

		if (results != null && results.length > 0) {
			if (results[0] instanceof IQueryResultSet && ((IQueryResultSet) results[0]).isBeforeFirst()) {
				((IQueryResultSet) results[0]).next();
			}

			text = String.valueOf(results[0].evaluate(text));
		} else {
			text = String.valueOf(context.evaluate(text));
		}

		BufferedImage qrImage = SwingGraphicsUtil.createQRCodeImage(text, dotsWidth, dotsWidth, encoding, errorCorrectionLevel,
				qrVersion);

		ByteArrayInputStream bis = null;

		try {
			ImageIO.setUseCache(false);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

			ImageIO.write(qrImage, "png", ios); //$NON-NLS-1$
			ios.flush();
			ios.close();

			bis = new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bis;

	}

}
