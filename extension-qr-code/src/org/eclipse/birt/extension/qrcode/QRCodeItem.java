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

package org.eclipse.birt.extension.qrcode;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ReportItem;

/**
 * QRCodeItem
 */
public class QRCodeItem extends ReportItem {

	public static final String EXTENSION_NAME = "QRCode"; //$NON-NLS-1$
	public static final String TEXT_PROP = "text"; //$NON-NLS-1$
	public static final String DOTS_WIDTH_PROP = "dotsWidth"; //$NON-NLS-1$
	public static final String ENCODING_PROP = "encoding"; //$NON-NLS-1$
	public static final String ERROR_CORRECTION_LEVEL_PROP = "errorCorrectionLevel"; //$NON-NLS-1$
	public static final String QR_VERSION_PROP = "qrVersion"; //$NON-NLS-1$

	private ExtendedItemHandle modelHandle;

	QRCodeItem(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	public ExtendedItemHandle getModelHandle() {
		return modelHandle;
	}

	public String getText() {
		return modelHandle.getStringProperty(TEXT_PROP);
	}

	public int getDotsWidth() {
		return modelHandle.getIntProperty(DOTS_WIDTH_PROP);
	}

	public String getEncoding() {
		return modelHandle.getStringProperty(ENCODING_PROP);
	}

	public String getErrorCorrectionLevel() {
		return modelHandle.getStringProperty(ERROR_CORRECTION_LEVEL_PROP);
	}

	public int getQrVersion() {
		return modelHandle.getIntProperty(QR_VERSION_PROP);
	}

	public void setText(String value) throws SemanticException {
		modelHandle.setProperty(TEXT_PROP, value);
	}

	public void setDotsWidth(int value) throws SemanticException {
		modelHandle.setProperty(DOTS_WIDTH_PROP, value);
	}

	public void setEncoding(String value) throws SemanticException {
		modelHandle.setProperty(ENCODING_PROP, value);
	}

	public void setErrorLevel(String value) throws SemanticException {
		modelHandle.setProperty(ERROR_CORRECTION_LEVEL_PROP, value);
	}

	public void setQrVersion(String value) throws SemanticException {
		modelHandle.setProperty(QR_VERSION_PROP, value);
	}

}
