/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.StructureRefUtil;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Image;

/**
 * Representation of the background image information includes attributes and
 * image data to build the images
 *
 * @since 3.3
 *
 */
public class BackgroundImageInfo extends AreaConstants {
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int repeatedMode;
	protected int width = 0;
	protected int height = 0;
	protected String url;
	protected String dataUrl;
	protected byte[] imageData;
	protected String sourceType;
	protected String mimeType;
	protected String fileExtension;
	protected Integer dpi = null;

	private final static String DATA_PROTOCOL = "data:";

	private final static String DATA_URL_BASE64 = ";base64,";

	// mapping based on image extension: to MIME-type to default extension
	private final static String[][] SUPPORTED_MIME_TYPES = { { ".jpg", "image/jpeg", "jpg" },
			{ ".jpe", "image/jpeg", "jpg" }, { ".jpeg", "image/jpeg", "jpg" }, { ".tiff", "image/tiff", "tiff" },
			{ ".svg", "image/svg+xml", "svg" }, { ".png", "image/png", "png" }, { ".gif", "image/gif", "gif" } };

	protected final static String BGI_SRC_TYPE_DEFAULT = BGI_SRC_TYPE_URL;

	private Module module = null;

	private Image image = null;

	private ResourceLocatorWrapper rl = null;

	/**
	 * constructor 01 of background image
	 *
	 * @param url
	 * @param repeatedMode
	 * @param xOffset
	 * @param yOffset
	 * @param height
	 * @param width
	 * @param rl
	 * @param module
	 * @param sourceType
	 *
	 * @since 4.13
	 */
	public BackgroundImageInfo(String url, int repeatedMode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module, String sourceType, Integer dpi) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.repeatedMode = repeatedMode;
		this.width = width;
		this.height = height;
		this.url = url;
		this.rl = rl;
		this.module = module;
		if (sourceType != null) {
			this.sourceType = sourceType;
		} else {
			this.sourceType = BGI_SRC_TYPE_DEFAULT;
		}
		this.dpi = dpi;
		prepareImageByteArray();
	}

	/**
	 * constructor 02 of background image
	 *
	 * @param bgi
	 */
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
		if (bgi.sourceType != null) {
			this.sourceType = bgi.sourceType;
		} else {
			this.sourceType = BGI_SRC_TYPE_DEFAULT;
		}
		this.dpi = bgi.dpi;
	}

	/**
	 * constructor 03 of background image
	 *
	 * @param url
	 * @param mode
	 * @param xOffset
	 * @param yOffset
	 * @param height
	 * @param width
	 * @param rl
	 * @param module
	 */
	public BackgroundImageInfo(String url, CSSValue mode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module, Integer dpi) {
		this(url, mode != null ? repeatMap.get(mode) : REPEAT, xOffset, yOffset, height, width, rl, module,
				BGI_SRC_TYPE_DEFAULT, dpi);
	}

	/**
	 * constructor 04 of background image
	 *
	 * @param url
	 * @param mode
	 * @param xOffset
	 * @param yOffset
	 * @param height
	 * @param width
	 * @param rl
	 * @param module
	 * @param sourceType
	 */
	public BackgroundImageInfo(String url, CSSValue mode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module, CSSValue sourceType, Integer dpi) {
		this(url, mode != null ? repeatMap.get(mode) : REPEAT, xOffset, yOffset, height, width, rl, module,
				sourceType != null ? bgiSourceTypeMap.get(sourceType)
						: BGI_SRC_TYPE_URL,
				dpi);
	}

	/**
	 * Set the resource locator
	 *
	 * @param rl set the resource locator
	 */
	public void setResourceLocator(ResourceLocatorWrapper rl) {
		this.rl = rl;
	}

	/**
	 * Create the data URL of the image
	 *
	 * @since 4.13
	 */
	private void createDataUrl() {

		if (this.url != null && this.url.contains(DATA_PROTOCOL)) {
			this.dataUrl = this.url;

		} else if (this.imageData != null) {
			Encoder encoder = java.util.Base64.getEncoder();
			this.dataUrl = DATA_PROTOCOL + this.mimeType + DATA_URL_BASE64
					+ (new String(encoder.encode(this.imageData), StandardCharsets.UTF_8));
		}
	}

	/**
	 * Get the data URL of the image
	 *
	 * @return Return the data URL of the image
	 */
	public String getDataUrl() {
		return this.dataUrl;
	}

	/**
	 * Set the image mime type
	 *
	 * @param mimeType
	 */
	private void setMimeType(String mimeType) {

		if (mimeType != null) {
			this.mimeType = mimeType;

		} else if (this.url.contains(DATA_PROTOCOL)) {
			try {
				if (url.contains(";") && url.contains(DATA_PROTOCOL)) {
					String partMimeType = url.split(";")[1];
					this.mimeType = partMimeType.split(DATA_PROTOCOL)[0];
				}
			} catch (IndexOutOfBoundsException ioobe) {
				this.mimeType = null;
			}
		} else {
			for (int index = 0; index < SUPPORTED_MIME_TYPES.length; index++) {
				if (this.url.toLowerCase().contains(SUPPORTED_MIME_TYPES[index][0])) {
					this.mimeType = SUPPORTED_MIME_TYPES[index][1];
					this.fileExtension = SUPPORTED_MIME_TYPES[index][2];
					break;
				}
			}
		}
	}

	/**
	 * Create the image byte data of the image
	 */
	private void prepareImageByteArray() {
		String mimeType = null;

		// get image URL based or from data-URL
		if (this.sourceType.equals(BGI_SRC_TYPE_URL)) {

			if (this.url.contains(DATA_PROTOCOL)) {
				String[] imageDataArray = this.url.split(DATA_URL_BASE64);
				if (imageDataArray.length == 2 && this.url.contains(DATA_PROTOCOL)) {
					try {
						String imageDataBase64 = imageDataArray[1];
						Decoder decoder = java.util.Base64.getDecoder();
						this.imageData = decoder.decode(imageDataBase64);
						this.mimeType = imageDataArray[0].split(DATA_PROTOCOL)[1];
					} catch (IndexOutOfBoundsException ioobe) {
						this.imageData = null;
						this.image = null;
						this.mimeType = null;
					}
				} else {
					this.imageData = null;
					this.image = null;
					this.mimeType = null;
				}
			} else {
				if (this.rl == null) {
					InputStream in = null;
					try {
						in = new URL(this.url).openStream();
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int size = in.read(buffer);
						while (size != -1) {
							out.write(buffer, 0, size);
							size = in.read(buffer);
						}
						this.imageData = out.toByteArray();
						out.close();
					} catch (IOException ioe) {
						this.imageData = null;
						this.image = null;
						this.mimeType = null;
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
						this.imageData = this.rl.findResource(new URL(this.url));
					} catch (MalformedURLException mue) {
						this.imageData = null;
						this.image = null;
						this.mimeType = null;
					}
				}
			}
		}

		// get embedded image from report
		if (this.sourceType.equals(BGI_SRC_TYPE_EMBED) || this.imageData == null) {
			StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance()
					.getStructure(EmbeddedImage.EMBEDDED_IMAGE_STRUCT);

			byte[] imageData = null;
			try {
				EmbeddedImage ei = (EmbeddedImage) StructureRefUtil.findStructure(this.module, defn, this.url);
				imageData = ei.getData(this.module);
				mimeType = ei.getType(this.module);
				if (this.sourceType.equals(BGI_SRC_TYPE_URL))
					this.sourceType = BGI_SRC_TYPE_EMBED;
			} catch (Exception te) {
				this.imageData = null;
				this.image = null;
				this.mimeType = null;
			}
			this.imageData = imageData;
		}

		if (this.imageData != null) {
			try {
				this.image = Image.getInstance(this.imageData);
			} catch (Exception e) {
				try {
					this.imageData = SvgFile.transSvgToArray(new ByteArrayInputStream(this.imageData), dpi);
					this.image = Image.getInstance(this.imageData);
				} catch (Exception te) {
					this.imageData = null;
					this.image = null;
					this.mimeType = null;
				}
			}
		}
		this.setMimeType(mimeType);
		this.createDataUrl();
	}

	/**
	 * Get the image instance
	 *
	 * @return Return the image instance
	 */
	public Image getImageInstance() {
		return image;
	}

	/**
	 * Get the background image offset position X
	 *
	 * @return Return the background image offset position X
	 */
	public int getXOffset() {
		return xOffset;
	}

	/**
	 * Set the background image offset position X
	 *
	 * @param x set the background image offset position X
	 */
	public void setXOffset(int x) {
		this.xOffset = x;
	}

	/**
	 * Get the background image offset position Y
	 *
	 * @return Return the background image offset position Y
	 */
	public int getYOffset() {
		return yOffset;
	}

	/**
	 * Set the background image offset position Y
	 *
	 * @param y set the background image offset position Y
	 */
	public void setYOffset(int y) {
		this.yOffset = y;
	}

	/**
	 * Get the background image height
	 *
	 * @return Return the background image height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the background image height
	 *
	 * @param height height of the background image
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the background image width
	 *
	 * @return Return the background image width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the background image width
	 *
	 * @param width width of the background image
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the background image repeat mode
	 *
	 * @return Return the background image repeat mode
	 */
	public int getRepeatedMode() {
		return repeatedMode;
	}

	/**
	 * Set the source type of the background image
	 *
	 * @param sourceType String of the image source type
	 * @since 4.13
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * Get the source type of the background image
	 *
	 * @return Returned the source type of the image
	 * @since 4.13
	 */
	public String getSourceType() {
		return this.sourceType;
	}

	/**
	 * Get the extension of the image file
	 *
	 * @since 4.13
	 * @return Returned the extension of the image file
	 */
	public String getFileExtension() {
		return this.fileExtension;
	}

	/**
	 * Get the image mime type
	 *
	 * @return Returned the image mime type
	 * @since 4.13
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * Get the url string
	 *
	 * @return Return the url string
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Get the image byte array
	 *
	 * @return Return the image data ByteArray
	 */
	public byte[] getImageData() {
		return this.imageData;
	}

}
