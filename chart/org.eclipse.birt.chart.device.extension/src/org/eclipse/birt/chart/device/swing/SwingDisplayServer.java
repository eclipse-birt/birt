/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;

import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.g2d.G2dDisplayServerBase;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 *
 */
public class SwingDisplayServer extends G2dDisplayServerBase {

	private transient BufferedImage _bufferedImage = null;

	private transient SwingImageCache _imageCache = null;

	private int userResolution;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swing"); //$NON-NLS-1$

	/**
	 * The constructor.
	 *
	 */
	public SwingDisplayServer() {

		logger.log(ILogger.INFORMATION, Messages.getString("SwingDisplayServer.info.display.server", //$NON-NLS-1$
				new Object[] { SecurityUtil.getSysProp("java.vendor"), SecurityUtil.getSysProp("java.version") }, //$NON-NLS-1$ //$NON-NLS-2$
				getULocale()));
		_imageCache = new SwingImageCache(this);
	}

	@Override
	public void dispose() {
		if (_bufferedImage != null) {
			// This means we have created our own _g2d, so we need to dispose it
			this._g2d.dispose();
			this._g2d = null;
			this._bufferedImage = null;
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.devices.IDisplayServer#getMetrics(org.eclipse.birt.chart.
	 * attribute.FontDefinition, java.lang.Object)
	 */
	public final Object getMetrics(FontDefinition fontDef) {
		return getGraphicsContext().getFontMetrics((Font) createFont(fontDef));
	}

	protected int computeScreenDpi() {
		if (GraphicsEnvironment.isHeadless()) {
			// RETURN OS SPECIFIC DEFAULTS
			return super.getDpiResolution();
		}
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	@Override
	public final int getDpiResolution() {

		if (iDpiResolution == 0) {
			switch (getGraphicsContext().getDeviceConfiguration().getDevice().getType()) {
			case GraphicsDevice.TYPE_RASTER_SCREEN:
				// This is the only reliable dpi for the display, the one in
				// g2d.getTransform()
				// will be 72 dpi for the display, even when the OS has a
				// different dpi set.
				iDpiResolution = computeScreenDpi();
				break;
			case GraphicsDevice.TYPE_PRINTER:
				// In that case the g2d already contains a transform with the right dpi of the
				// printer
				// so we set the dpi to 72, since there is no adjustment needed
				iDpiResolution = 72;
				break;
			case GraphicsDevice.TYPE_IMAGE_BUFFER:
				if (userResolution == 0) {
					// Use value set by user, if none, use screen resolution
					iDpiResolution = computeScreenDpi();
				} else {
					iDpiResolution = userResolution;
				}
				break;
			}

			adjustFractionalMetrics();
		}
		return iDpiResolution;
	}

	/*
	 * set the fractionalmetrics to ON only for high resolution
	 */
	private void adjustFractionalMetrics() {
		if (iDpiResolution == 0 || _g2d == null) {
			return;
		}

		if (iDpiResolution >= 192) {
			_g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		} else {
			_g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		}

	}

	@Override
	public final void setDpiResolution(int dpi) {
		userResolution = dpi;
	}

	@Override
	public Object loadImage(URL url) throws ChartException {
		URL urlFound = findResource(url);
		return _imageCache.loadImage(urlFound);
	}

	@Override
	public final Size getSize(Object oImage) {
		final Image img = (Image) oImage;
		final ImageObserver io = (ImageObserver) _imageCache.getObserver();
		return SizeImpl.create(img.getWidth(io), img.getHeight(io));
	}

	@Override
	public final Object getObserver() {
		return _imageCache.getObserver();
	}

	@Override
	public ITextMetrics getTextMetrics(Label label, boolean autoReuse) {
		return new SwingTextMetrics(this, label, getGraphicsContext(), autoReuse);
	}

	/**
	 * Returns the image cache
	 *
	 * @return
	 */
	final SwingImageCache getImageCache() {
		return _imageCache;
	}

	@Override
	public void setGraphicsContext(Object g2d) {
		// User g2d will replace the one instantiated by the display server if any
		if (g2d != this._g2d && this._bufferedImage != null) {
			this._g2d.dispose();
			// set image as null to indicate it's an external graphic context.
			this._bufferedImage = null;
		}
		this._g2d = (Graphics2D) g2d;
		setAntialiasProperties(_g2d);
	}

	// For internal use only
	private Graphics2D getGraphicsContext() {
		if (_g2d == null) {
			// The user _g2d hasn't been set yet.
			// We create our own _g2d here for computations, and it will be disposed later.

			_bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			_g2d = (Graphics2D) _bufferedImage.getGraphics();

			setAntialiasProperties(_g2d);

		}

		return _g2d;
	}

	private void setAntialiasProperties(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		adjustFractionalMetrics();
	}

}
