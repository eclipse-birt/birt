/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * 
 */
public final class BmpRendererImpl extends JavaxImageIOWriter {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/image"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
	 */
	public final String getFormat() {
		return "bmp"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	public final String getMimeType() {
		return "image/bmp"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
	 */
	public final int getImageType() {
		return BufferedImage.TYPE_3BYTE_BGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	public final void after() throws ChartException {
		try {
			super.after();
		} catch (ChartException e) {
			if (isSupportedByJavaxImageIO()) {
				throw e;
			}
			logger.log(ILogger.INFORMATION, Messages.getString("BmpRendererImpl.info.use.custom.image.writer", //$NON-NLS-1$
					new Object[] { getFormat(), BmpWriter.class.getName() }, getULocale()));

			// If not supported by JavaxImageIO, use our own.
			BmpWriter bw = null;

			if (_oOutputIdentifier instanceof OutputStream) {
				bw = new BmpWriter(_img);
				try {
					bw.write((OutputStream) _oOutputIdentifier);
				} catch (Exception ex) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ex);
				}
			} else if (_oOutputIdentifier instanceof String) {
				FileOutputStream fos = null;
				try {
					fos = SecurityUtil.newFileOutputStream((String) _oOutputIdentifier);
					bw = new BmpWriter(_img);
					bw.write(fos);
					fos.close();
				} catch (Exception ex) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ex);
				}
			} else {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"BmpRendererImpl.exception.unable.write.output.identifier", //$NON-NLS-1$
						new Object[] { _oOutputIdentifier }, Messages.getResourceBundle(getULocale()));
			}
		}

	}

	protected boolean supportsTransparency() {
		return false;
	}

}
