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

package org.eclipse.birt.chart.device.swing;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 *
 */
public final class SwingImageCache {

	/**
	 *
	 */
	private final java.awt.Panel p = new java.awt.Panel(); // NEEDED FOR IMAGE

	/**
	 *
	 */
	private final Hashtable<String, Image> htCache;

	/**
	 *
	 */
	private final IDisplayServer idsSWING;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swing"); //$NON-NLS-1$

	/**
	 *
	 */
	SwingImageCache(IDisplayServer idsSWING) {
		this.idsSWING = idsSWING;
		htCache = SecurityUtil.newHashtable();
	}

	/**
	 *
	 * @param url
	 * @return
	 * @throws ChartException
	 */
	Image loadImage(URL url) throws ChartException {
		String sUrl = url.toString();
		Image img = htCache.get(sUrl);
		if (img != null) {
			logger.log(ILogger.INFORMATION, Messages.getString("SwingImageCache.info.using.swing.cached.image", //$NON-NLS-1$
					new Object[] { url }, idsSWING.getULocale()));
		} else {
			logger.log(ILogger.INFORMATION, Messages.getString("SwingImageCache.info.loading.swing.image", //$NON-NLS-1$
					new Object[] { url }, idsSWING.getULocale()));
			try {
				try {
					img = ImageIO.read(url);
				} catch (IllegalArgumentException e) {
					// Some special image formats are not supported by standard
					// sun's JDK, like Microsoft Ico file, it might throw
					// exception, here catch the exception and return null
					// image.
					return null;
				}

				final MediaTracker tracker = new MediaTracker(p);
				tracker.addImage(img, 0);
				tracker.waitForAll();

				if ((tracker.statusAll(true) & MediaTracker.ERRORED) != 0) {
					StringBuilder sb = new StringBuilder();
					Object[] oa = tracker.getErrorsAny();
					sb.append('[');
					for (int i = 0; i < oa.length; i++) {
						sb.append(oa[i]);
						if (i < oa.length - 1) {
							sb.append(", "); //$NON-NLS-1$
						}
					}
					sb.append(']');
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.IMAGE_LOADING,
							"SwingImageCache.exception.media.tracker", //$NON-NLS-1$
							new Object[] { sb.toString() }, Messages.getResourceBundle(idsSWING.getULocale()));
				}
			} catch (InterruptedException | IOException e) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.IMAGE_LOADING, e);
			}
			htCache.put(sUrl, img);
		}
		return img;
	}

	/**
	 *
	 */
	void flush() {
		if (htCache.isEmpty()) {
			return;
		}
		Image img;
		final int n = htCache.size();
		Enumeration<Image> eV = htCache.elements();
		while (eV.hasMoreElements()) {
			img = eV.nextElement();
			img.flush();
		}
		htCache.clear();
		logger.log(ILogger.INFORMATION, Messages.getString("SwingImageCache.info.flushed.swing.images", //$NON-NLS-1$
				new Object[] { Integer.valueOf(n) }, idsSWING.getULocale()));
	}

	/**
	 *
	 * @return
	 */
	Object getObserver() {
		return p;
	}
}
