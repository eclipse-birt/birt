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

/**
 *
 */
public class PngRendererImpl extends JavaxImageIOWriter {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	@Override
	public final String getFormat() {
		return "png"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return "image/png"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	@Override
	public final int getImageType() {
		return BufferedImage.TYPE_4BYTE_ABGR; // SUPPORT ALPHA
	}

	@Override
	protected boolean supportsTransparency() {
		return true;
	}

}
