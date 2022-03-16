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

package org.eclipse.birt.chart.device.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.device.ImageWriterFactory;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * JavaxImageIOWriter
 */
public abstract class JavaxImageIOWriter extends SwingRendererImpl
		implements IIOWriteWarningListener, IImageMapEmitter {

	private boolean _bAltEnabled = false;

	protected Image _img = null;

	protected Object _oOutputIdentifier = null;

	private Bounds _bo = null;

	private boolean _bImageExternallySpecified = false;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/image"); //$NON-NLS-1$

	private String outputFormat;

	/**
	 * Returns the output format string for this writer.
	 *
	 * @return
	 */
	protected abstract String getFormat();

	/**
	 * Returns the output image type for this writer.
	 *
	 * @see java.awt.image.BufferedImage#TYPE_INT_RGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB_PRE
	 * @see java.awt.image.BufferedImage#TYPE_INT_BGR
	 * @see java.awt.image.BufferedImage#TYPE_3BYTE_BGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_BINARY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_INDEXED
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_565_RGB
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_555_RGB
	 *
	 * @return
	 */
	protected abstract int getImageType();

	/**
	 * Returns true if the image type supports transparency false otherwise
	 *
	 * @return
	 */
	protected boolean supportsTransparency() {
		return true;
	}

	JavaxImageIOWriter() {
		// By default do not cache images on disk
		ImageIO.setUseCache(false);
	}

	/**
	 * Updates the writer's parameters.
	 *
	 * @param iwp
	 */
	protected void updateWriterParameters(ImageWriteParam iwp) {
		// OPTIONALLY IMPLEMENTED BY SUBCLASS
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getImageMap()
	 */
	@Override
	public String getImageMap() {
		return new ImageMapEmitter(getShapeActions(), _bAltEnabled, getULocale(), getDisplayServer().getDpiResolution())
				.getImageMap();
	}

	/**
	 * Returns if the given format type or MIME type is supported by the registered
	 * JavaxImageIO writers.
	 *
	 * @return
	 */
	protected boolean isSupportedByJavaxImageIO() {
		boolean supported = false;

		// Search for writers using format type.
		String s = getFormat();
		if (s != null) {
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(s);
			if (it.hasNext()) {
				supported = true;
			}
		}

		// Search for writers using MIME type.
		if (!supported) {
			s = getMimeType();
			if (s != null) {
				Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(s);
				if (it.hasNext()) {
					supported = true;
				}
			}
		}

		return supported;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#before()
	 */
	@Override
	public void before() throws ChartException {
		super.before();

		_bImageExternallySpecified = (_img != null);

		// IF A CACHED IMAGE STRATEGY IS NOT USED, CREATE A NEW INSTANCE
		// EVERYTIME
		if (!_bImageExternallySpecified) {
			if (_bo == null) // BOUNDS MUST BE SPECIFIED BEFORE RENDERING
			// BEGINS
			{
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"JavaxImageIOWriter.exception.no.bounds", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}

			if ((int) _bo.getWidth() < 0 || (int) _bo.getHeight() < 0) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.INVALID_IMAGE_SIZE,
						"JavaxImageIOWriter.exception.invalid.image.size", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}

			if ((int) _bo.getWidth() == 0 || (int) _bo.getHeight() == 0) {
				// Zero size is forbidden in BufferedImage, so replace the size
				// with 1 to make it seem invisible
				_bo.setWidth(1);
				_bo.setHeight(1);
			}

			// CREATE THE IMAGE INSTANCE
			_img = new BufferedImage((int) Math.round(_bo.getWidth()), (int) Math.round(_bo.getHeight()),
					getImageType());
		}
		super.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, _img.getGraphics());

		if (!supportsTransparency()) {
			// Paint image white to avoid black background
			_g2d.setPaint(Color.WHITE);
			_g2d.fillRect(0, 0, _img.getWidth(null), _img.getHeight(null));
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	@Override
	public void after() throws ChartException {
		super.after();

		if (_oOutputIdentifier != null) {

			// SEARCH FOR WRITER USING FORMAT
			ImageWriter iw = ImageWriterFactory.instance().createImageWriter(getFormat(), outputFormat);

			// SEARCH FOR WRITER USING MIME TYPE
			if (iw == null) {
				String s = getMimeType();

				if (s == null) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
							"JavaxImageIOWriter.exception.no.imagewriter.mimetype.and.format", //$NON-NLS-1$
							new Object[] { getMimeType(), getFormat(), getClass().getName() },
							Messages.getResourceBundle(getULocale()));
				}
				Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(s);
				if (!it.hasNext()) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
							"JavaxImageIOWriter.exception.no.imagewriter.mimetype", //$NON-NLS-1$
							new Object[] { getMimeType() }, Messages.getResourceBundle(getULocale()));
				}

				iw = it.next();
			}

			logger.log(ILogger.INFORMATION,
					Messages.getString("JavaxImageIOWriter.info.using.imagewriter", getULocale()) //$NON-NLS-1$
							+ getFormat() + iw.getClass().getName());

			// WRITE TO SPECIFIC FILE FORMAT
			final Object o = (_oOutputIdentifier instanceof String) ? new File((String) _oOutputIdentifier)
					: _oOutputIdentifier;
			try {
				final ImageOutputStream ios = SecurityUtil.newImageOutputStream(o);
				ImageWriteParam iwp = iw.getDefaultWriteParam();
				updateWriterParameters(iwp);
				iw.setOutput(ios);
				iw.write((IIOMetadata) null, new IIOImage((BufferedImage) _img, null, null), iwp);
				ios.close();
			} catch (Exception ex) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ex);
			} finally {
				iw.dispose();
			}
		}

		// FLUSH AND RESTORE STATE OF INTERNALLY CREATED IMAGE
		if (!_bImageExternallySpecified) {
			_img.flush();
			_img = null;
		}

		// ALWAYS DISPOSE THE GRAPHICS CONTEXT THAT WAS CREATED FROM THE IMAGE
		_g2d.dispose();
		_g2d = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setProperty(String sProperty, Object oValue) {
		super.setProperty(sProperty, oValue);
		if (sProperty.equals(IDeviceRenderer.EXPECTED_BOUNDS)) {
			_bo = (Bounds) oValue;
		} else if (sProperty.equals(IDeviceRenderer.CACHED_IMAGE)) {
			_img = (Image) oValue;
		} else if (sProperty.equals(IDeviceRenderer.FILE_IDENTIFIER)) {
			_oOutputIdentifier = oValue;
		} else if (sProperty.equals(IDeviceRenderer.CACHE_ON_DISK)) {
			ImageIO.setUseCache(((Boolean) oValue).booleanValue());
		} else if (sProperty.equals(IDeviceRenderer.AREA_ALT_ENABLED)) {
			_bAltEnabled = ((Boolean) oValue).booleanValue();
		} else if (sProperty.equals("output.format")) //$NON-NLS-1$
		{
			outputFormat = (String) oValue;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.imageio.event.IIOWriteWarningListener#warningOccurred(javax.imageio.
	 * ImageWriter, int, java.lang.String)
	 */
	@Override
	public void warningOccurred(ImageWriter source, int imageIndex, String warning) {
		logger.log(ILogger.WARNING, warning);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDeviceRenderer#presentException(java.lang.
	 * Exception)
	 */
	@Override
	public void presentException(Exception cexp) {
		if (_bo == null) {
			_bo = BoundsImpl.create(0, 0, 400, 300);
		}
		String sWrappedException = cexp.getClass().getName();
		while (cexp.getCause() != null) {
			cexp = (Exception) cexp.getCause();
		}
		String sException = cexp.getClass().getName();
		if (sWrappedException.equals(sException)) {
			sWrappedException = null;
		}
		String sMessage = cexp.getMessage();
		StackTraceElement[] stea = cexp.getStackTrace();
		Dimension d = new Dimension((int) _bo.getWidth(), (int) _bo.getHeight());

		Font fo = new Font("Monospaced", Font.BOLD, 14); //$NON-NLS-1$
		_g2d.setFont(fo);
		FontMetrics fm = _g2d.getFontMetrics();
		_g2d.setColor(Color.WHITE);
		_g2d.fillRect(20, 20, d.width - 40, d.height - 40);
		_g2d.setColor(Color.BLACK);
		_g2d.drawRect(20, 20, d.width - 40, d.height - 40);
		_g2d.setClip(20, 20, d.width - 40, d.height - 40);
		int x = 25, y = 20 + fm.getHeight();
		_g2d.drawString(Messages.getString("JavaxImageIOWriter.exception.caption", getULocale()), x, y); //$NON-NLS-1$
		x += fm.stringWidth(Messages.getString("JavaxImageIOWriter.exception.caption", //$NON-NLS-1$
				getULocale())) + 5;
		_g2d.setColor(Color.RED);
		_g2d.drawString(sException, x, y);
		x = 25;
		y += fm.getHeight();
		if (sWrappedException != null) {
			_g2d.setColor(Color.BLACK);
			_g2d.drawString(Messages.getString("JavaxImageIOWriter.wrapped.caption", getULocale()), x, y); //$NON-NLS-1$
			x += fm.stringWidth(Messages.getString("JavaxImageIOWriter.wrapped.caption", //$NON-NLS-1$
					getULocale())) + 5;
			_g2d.setColor(Color.RED);
			_g2d.drawString(sWrappedException, x, y);
			x = 25;
			y += fm.getHeight();
		}
		_g2d.setColor(Color.BLACK);
		y += 10;
		_g2d.drawString(Messages.getString("JavaxImageIOWriter.message.caption", getULocale()), x, y); //$NON-NLS-1$
		x += fm.stringWidth(Messages.getString("JavaxImageIOWriter.message.caption", getULocale())) + 5; //$NON-NLS-1$
		_g2d.setColor(Color.BLUE);
		_g2d.drawString(sMessage, x, y);
		x = 25;
		y += fm.getHeight();
		_g2d.setColor(Color.BLACK);
		y += 10;
		_g2d.drawString(Messages.getString("JavaxImageIOWriter.trace.caption", getULocale()), x, y); //$NON-NLS-1$
		x = 40;
		y += fm.getHeight();
		_g2d.setColor(Color.GREEN.darker());
		for (int i = 0; i < stea.length; i++) {
			_g2d.drawString(Messages.getString("JavaxImageIOWriter.trace.detail", //$NON-NLS-1$
					new Object[] { stea[i].getClassName(), stea[i].getMethodName(),
							String.valueOf(stea[i].getLineNumber()) },
					getULocale()), x, y);
			x = 40;
			y += fm.getHeight();
		}

	}

}
