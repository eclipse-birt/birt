/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	private boolean isQualitySet = false;;
	private int jpegQuality;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	protected final String getFormat() {
		return "jpeg"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	protected final int getImageType() {
		return BufferedImage.TYPE_3BYTE_BGR; // NO TRANSPARENCY IN JPEG
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	public String getMimeType() {
		return "image/jpeg"; //$NON-NLS-1$
	}

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

	protected boolean supportsTransparency() {
		return false;
	}
}
