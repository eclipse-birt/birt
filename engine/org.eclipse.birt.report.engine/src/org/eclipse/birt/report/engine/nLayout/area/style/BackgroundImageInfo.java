/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Image;

public class BackgroundImageInfo extends AreaConstants {
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int repeatedMode;
	protected int width = 0;
	protected int height = 0;
	protected String url;
	protected byte[] imageData;

	private Image image;

	private ResourceLocatorWrapper rl = null;

	public BackgroundImageInfo(String url, int repeatedMode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.repeatedMode = repeatedMode;
		this.width = width;
		this.height = height;
		this.url = url;
		this.rl = rl;
		prepareImageByteArray();
	}

	public BackgroundImageInfo(BackgroundImageInfo bgi) {
		this.xOffset = bgi.xOffset;
		this.yOffset = bgi.yOffset;
		this.repeatedMode = bgi.repeatedMode;
		this.width = bgi.width;
		this.height = bgi.height;
		this.url = bgi.url;
		this.imageData = bgi.imageData;
		this.image = bgi.image;
		this.rl = bgi.rl;
	}

	public BackgroundImageInfo(String url, CSSValue mode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl) {
		this(url, mode != null ? repeatMap.get(mode) : REPEAT, xOffset, yOffset, height, width, rl);
	}

	public BackgroundImageInfo(String url, int height, int width, ResourceLocatorWrapper rl) {
		this(url, 0, 0, 0, height, width, rl);
	}

	public void setResourceLocator(ResourceLocatorWrapper rl) {
		this.rl = rl;
	}

	private void prepareImageByteArray() {
		if (rl == null) {
			InputStream in = null;
			try {
				in = new URL(url).openStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int size = in.read(buffer);
				while (size != -1) {
					out.write(buffer, 0, size);
					size = in.read(buffer);
				}
				imageData = out.toByteArray();
				out.close();
			} catch (IOException ioe) {
				imageData = null;
				image = null;
				return;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			try {
				imageData = rl.findResource(new URL(url));
			} catch (MalformedURLException mue) {
				imageData = null;
				image = null;
				return;
			}
		}

		try {

			image = Image.getInstance(imageData);
		} catch (Exception e) {
			try {
				imageData = SvgFile.transSvgToArray(new ByteArrayInputStream(imageData));
				image = Image.getInstance(imageData);
			} catch (Exception te) {
				imageData = null;
				image = null;
			}
		}

	}

	public Image getImageInstance() {
		return image;
	}

	public int getXOffset() {
		return xOffset;
	}

	public void setYOffset(int y) {
		this.yOffset = y;
	}

	public void setXOffset(int x) {
		this.xOffset = x;
	}

	public int getYOffset() {
		return yOffset;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getRepeatedMode() {
		return repeatedMode;
	}

	public String getUrl() {
		return url;
	}

	public byte[] getImageData() {
		return imageData;
	}

}
