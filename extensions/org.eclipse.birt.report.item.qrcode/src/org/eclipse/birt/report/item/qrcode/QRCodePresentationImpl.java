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

package org.eclipse.birt.report.item.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.qrcode.util.SwingGraphicsUtil;
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
		String expression = qrItem.getText();
		String encoding = qrItem.getEncoding();
		String errorCorrectionLevel = qrItem.getErrorCorrectionLevel();
		int qrVersion = qrItem.getQrVersion();

		Object obj = null;
		if (results != null && results.length > 0) {
			if (results[0] instanceof IQueryResultSet && ((IQueryResultSet) results[0]).isBeforeFirst()) {
				((IQueryResultSet) results[0]).next();
			}
			obj = (results[0].evaluate(expression));
		} else {
			obj = context.evaluate(expression);
		}
		if (obj == null) {
			return null;
		}
		String text = String.valueOf(obj);

		BufferedImage qrImage = SwingGraphicsUtil.createQRCodeImage(text, dotsWidth, dotsWidth, encoding, errorCorrectionLevel,
				qrVersion);
		if (qrImage == null) {
			return null;
		}

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