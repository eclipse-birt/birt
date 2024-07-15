/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.device.ImageWriterFactory;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * This class represents an SVG image.
 */
public class SVGImage extends Image {

	protected Image image;
	protected URL url;
	protected byte[] data;
	public static final String BASE64 = "data:;base64,"; //$NON-NLS-1$

	public byte[] getData() {
		return data;
	}

	/**
	 * @param image
	 */
	public SVGImage(Image image, URL url) {
		this(image, url, null);
	}

	public SVGImage(Image image, URL url, byte[] data) {
		super();
		this.image = image;
		this.url = url;
		this.data = data;
		if (url == null && data == null && image instanceof BufferedImage) {
			ImageWriter iw = ImageWriterFactory.instance().createImageWriter("png", "html"); //$NON-NLS-1$ //$NON-NLS-2$

			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
				ImageOutputStream ios = SecurityUtil.newImageOutputStream(baos);
				ImageWriteParam iwp = iw.getDefaultWriteParam();
				iw.setOutput(ios);
				iw.write((IIOMetadata) null, new IIOImage((BufferedImage) image, null, null), iwp);
				ios.close();
				this.data = baos.toByteArray();
				baos.close();
			} catch (IOException e) {
				// do nothing
			} finally {
				iw.dispose();
			}

		}
	}

	/**
	 *
	 */
	@Override
	public void flush() {
		image.flush();
	}

	@Override
	public Graphics getGraphics() {
		return image.getGraphics();
	}

	@Override
	public int getHeight(ImageObserver arg0) {
		return image.getHeight(arg0);
	}

	@Override
	public Object getProperty(String arg0, ImageObserver arg1) {
		return image.getProperty(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Image#getScaledInstance(int, int, int)
	 */
	@Override
	public Image getScaledInstance(int arg0, int arg1, int arg2) {
		return new SVGImage(image.getScaledInstance(arg0, arg1, arg2), url, data);
	}

	@Override
	public ImageProducer getSource() {
		return image.getSource();
	}

	@Override
	public int getWidth(ImageObserver arg0) {
		return image.getWidth(arg0);
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		if (url == null) {
			if (data != null) {
				return BASE64 + new String(Base64.getEncoder().encode(data));

			}
			return ""; //$NON-NLS-1$
		}
		return url.toExternalForm();
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
}
