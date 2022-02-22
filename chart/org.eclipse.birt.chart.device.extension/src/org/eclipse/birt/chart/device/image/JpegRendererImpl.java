/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.image.BufferedImage;

import javax.imageio.ImageWriteParam;

/**
 *
 */
public final class JpegRendererImpl extends JavaxImageIOWriter {

	private boolean isQualitySet = false;
	private int jpegQuality;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	@Override
	protected String getFormat() {
		return "jpeg"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	@Override
	protected int getImageType() {
		return BufferedImage.TYPE_3BYTE_BGR; // NO TRANSPARENCY IN JPEG
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return "image/jpeg"; //$NON-NLS-1$
	}

	@Override
	protected void updateWriterParameters(ImageWriteParam iwp) {
		float quality = isQualitySet ? jpegQuality : 0.95f;
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(quality);
	}

	/**
	 * Set the Jpeg compression quality into the renderer. The value must be between
	 * 0 (better compression) and 1 (better quality). The default value is 0.95 (no
	 * visual loss)
	 *
	 * @param jpegQuality
	 */
	public void setCompressionQuality(final int jpegQuality) {
		if (jpegQuality < 0 || jpegQuality > 1) {
			throw new IllegalArgumentException("Jpeg quality must be within the [0-1] range"); //$NON-NLS-1$
		} else {
			isQualitySet = true;
			this.jpegQuality = jpegQuality;
		}
	}

	@Override
	protected boolean supportsTransparency() {
		return false;
	}
}
