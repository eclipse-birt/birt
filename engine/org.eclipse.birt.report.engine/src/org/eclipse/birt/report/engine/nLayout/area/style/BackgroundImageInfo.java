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
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.StructureRefUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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

	/**
	 * the horizontal and vertical DPI
	 */
	private final static int graphicDpi = 96;

	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int repeatedMode;
	protected String url;
	protected String uri;
	protected String dataUrl;
	protected byte[] imageData;
	protected String sourceType;
	protected String mimeType;
	protected String fileExtension;
	protected int[] dpi = { 96, 96 };

	// width & height in pixel
	protected int width = 0;
	protected int height = 0;

	// width & height in Metric unit
	protected int widthMetricPt = 0;
	protected int heightMetricPt = 0;

	// width & height based on designer properties
	protected String propertyWidth = "auto";
	protected String propertyHeight = "auto";

	/**
	 * URL type of data protocol
	 */
	private final static String DATA_PROTOCOL = "data:";

	/**
	 * Base64 key string of the URL data protocol
	 */
	private final static String DATA_URL_BASE64 = ";base64,";

	/**
	 * default DPI resolution of the background image
	 */
	private final static int BGI_DPI_DEFAULT = 96;

	/**
	 * default metric DPI converting due to different pixel converters
	 */
	private final static int BGI_DPI_METRIC_PT = 72000; // (720 + 28);

	/**
	 * Mapping based on image extension: to MIME-type to default extension
	 */
	private final static String[][] SUPPORTED_MIME_TYPES = { { ".jpg", "image/jpeg", "jpg" },
			{ ".jpe", "image/jpeg", "jpg" }, { ".jpeg", "image/jpeg", "jpg" }, { ".tiff", "image/tiff", "tiff" },
			{ ".svg", "image/svg+xml", "svg" }, { ".png", "image/png", "png" }, { ".gif", "image/gif", "gif" } };

	/**
	 * Default source type URL
	 */
	protected final static String BGI_SRC_TYPE_DEFAULT = BGI_SRC_TYPE_URL;

	/**
	 * Module handle of the background image
	 */
	private Module module = null;

	/**
	 * Image object of the background image himself
	 */
	private Image image = null;

	/**
	 * Resource locator of the background
	 */
	private ResourceLocatorWrapper rl = null;

	/**
	 * constructor 01 of background image
	 *
	 * @param url          URL of the background image
	 * @param repeatedMode repeat mode of the background image
	 * @param xOffset      offset position x of the background image
	 * @param yOffset      offset position y of the background image
	 * @param height       height of the background image
	 * @param width        width of the background image
	 * @param rl           resource locator
	 * @param module       module handle of the background image
	 * @param sourceType   source type of the background image
	 * @param dpi          resolution of the background image
	 *
	 * @since 4.13
	 */
	public BackgroundImageInfo(String url, int repeatedMode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module, String sourceType, int dpi) {
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
		prepareImageByteArray();

		if (dpi <= 0) {
			this.dpi = this.getImageDpi();
		} else {
			this.dpi[0] = dpi;
			this.dpi[1] = dpi;
		}
	}

	/**
	 * constructor 02 of background image
	 *
	 * @param bgi object of background image
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
		this.dpi = bgi.dpi;
		if (bgi.sourceType != null) {
			this.sourceType = bgi.sourceType;
		} else {
			this.sourceType = BGI_SRC_TYPE_DEFAULT;
		}
	}

	/**
	 * constructor 03 of background image
	 *
	 * @param url     URL of the background image
	 * @param mode    repeat mode of the background image
	 * @param xOffset offset position x of the background image
	 * @param yOffset offset position y of the background image
	 * @param height  height of the background image
	 * @param width   width of the background image
	 * @param rl      resource locator
	 * @param module  module handle of the background image
	 */
	public BackgroundImageInfo(String url, CSSValue mode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module) {
		this(url, mode != null ? repeatMap.get(mode) : REPEAT, xOffset, yOffset, height, width, rl, module,
				BGI_SRC_TYPE_DEFAULT, 0);
	}

	/**
	 * constructor 04 of background image
	 *
	 * @param url        URL of the background image
	 * @param mode       repeat mode of the background image
	 * @param xOffset    offset position x of the background image
	 * @param yOffset    offset position y of the background image
	 * @param height     height of the background image
	 * @param width      width of the background image
	 * @param rl         resource locator
	 * @param module     module handle of the background image
	 * @param sourceType source type of the background image
	 */
	public BackgroundImageInfo(String url, CSSValue mode, int xOffset, int yOffset, int height, int width,
			ResourceLocatorWrapper rl, Module module, CSSValue sourceType) {
		this(url, mode != null ? repeatMap.get(mode) : REPEAT, xOffset, yOffset, height, width, rl, module,
				sourceType != null ? bgiSourceTypeMap.get(sourceType)
						: BGI_SRC_TYPE_URL,	0);
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
					this.imageData = SvgFile.transSvgToArray(new ByteArrayInputStream(this.imageData));
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
		return this.height;
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
		return this.width;
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

	/**
	 * Set the URI string
	 *
	 * @param uri representation of the image URI
	 * @since 4.14
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Get the URI string
	 *
	 * @return Return the URI string
	 * @since 4.14
	 */
	public String getUri() {
		if (this.uri == null) {
			return this.getDataUrl();
		}
		return this.uri;
	}

	/**
	 * Get property width of background image based on set designer property size
	 *
	 * @return Return the property height of background image based on designer
	 *         property
	 * @since 4.14
	 */
	public String getPropertyHeight() {
		return this.propertyHeight;
	}

	/**
	 * Get property width of background image based on set designer property size
	 *
	 * @return Return the property width of background image based on designer
	 *         property
	 * @since 4.14
	 */
	public String getPropertyWidth() {
		return this.propertyWidth;
	}

	/**
	 * Get height metric of background image (used e.g. PDF emitter)
	 *
	 * @return Return the height metric of background image
	 * @since 4.14
	 */
	public int getHeightMetricPt() {
		return this.heightMetricPt;
	}

	/**
	 * Set the height metric of background image
	 *
	 * @param height height metric of background image
	 * @since 4.14
	 */
	public void setHeightMetricPt(int height) {
		this.heightMetricPt = height;
	}

	/**
	 * Get width metric of background image (used e.g. PDF emitter)
	 *
	 * @return Return the width metric of background image
	 * @since 4.14
	 */
	public int getWidthMetricPt() {
		return this.widthMetricPt;
	}

	/**
	 * Set the width metric of background image
	 *
	 * @param width width metric of background image
	 * @since 4.14
	 */
	public void setWidthMetricPt(int width) {
		this.widthMetricPt = width;
	}

	/**
	 * Set image dpi with horizontal and vertical resolution
	 *
	 *
	 * @param dpi set the resolution of the image (horizontal & vertical resolution
	 *            in dpi) resolution
	 * @since 4.14
	 */
	public void setDpi(int[] dpi) {
		if (dpi.length != 2) {
			dpi = new int[2];
			dpi[0] = BGI_DPI_DEFAULT;
			dpi[1] = BGI_DPI_DEFAULT;
		}
		this.dpi = dpi;
	}

	/**
	 * Get image dpi with horizontal and vertical resolution
	 *
	 *
	 * @return Return an array of the image dpi for the horizontal and vertical
	 *         resolution
	 * @since 4.14
	 */
	public int[] getDpi() {
		return this.dpi;
	}

	/**
	 * Get image dpi with horizontal and vertical resolution
	 *
	 * @return Return an array of the image dpi for the horizontal and vertical
	 *         resolution
	 */
	private int[] getImageDpi() {
		InputStream in = null;
		URL temp = null;
		DesignElementHandle model = this.module.getModuleHandle();

		try {
			if (org.eclipse.birt.report.model.api.util.URIUtil.isValidResourcePath(this.url)) {
				temp = this.generateURL(model.getModuleHandle(),
						org.eclipse.birt.report.model.api.util.URIUtil.getLocalPath(this.url));
			} else {
				temp = this.generateURL(model.getModuleHandle(), this.url);
			}
			if (temp != null) {
				in = temp.openStream();
			}
		} catch (IOException e) {
			in = null;
		}

		int[] dpi = getImageResolution(in);
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (dpi == null || dpi.length != 2) {
			dpi = new int[2];
			dpi[0] = BGI_DPI_DEFAULT;
			dpi[1] = BGI_DPI_DEFAULT;
		} else {
			if (dpi[0] <= 0) {
				dpi[0] = BGI_DPI_DEFAULT;
			}
			if (dpi[1] <= 0) {
				dpi[1] = BGI_DPI_DEFAULT;
			}
		}
		return dpi;
	}

	/**
	 * Returns the DPI info of given image if applicable.
	 *
	 * @param imageStream
	 *
	 * @return the DPI values in format of {hdpi/widthdpi, vdpi/heightdpi}
	 */
	private int[] getImageResolution(InputStream imageStream) {
		int[] dpi = { BGI_DPI_DEFAULT, BGI_DPI_DEFAULT };

		if (imageStream != null) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(imageStream);
				Iterator<ImageReader> i = ImageIO.getImageReaders(iis);
				ImageReader r = null;
				IIOMetadata meta = null;
				if (i != null && i.hasNext()) {
					r = i.next();
					r.setInput(iis);
					r.read(0);
					meta = r.getImageMetadata(0);
				}

				if (meta != null) {
					double mm2inch = 25.4;

					NodeList lst;
					Element node = (Element) meta.getAsTree("javax_imageio_1.0"); //$NON-NLS-1$
					lst = node.getElementsByTagName("HorizontalPixelSize"); //$NON-NLS-1$
					if (lst != null && lst.getLength() == 1) {
						dpi[0] = (int) (mm2inch / Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))); //$NON-NLS-1$
					}

					lst = node.getElementsByTagName("VerticalPixelSize"); //$NON-NLS-1$
					if (lst != null && lst.getLength() == 1) {
						dpi[1] = (int) (mm2inch / Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))); //$NON-NLS-1$
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dpi;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 *
	 * @param style style of the background image
	 * @since 4.14
	 */
	public void setImageSize(IStyle style) {
		String propertyValue = null;
		int pxBackgroundHeight = 0;
		int pxBackgroundWidth = 0;
		double percentageHeight = 1d;
		double percentageWidth = 1d;

		if (style != null) {

			// calculate the background image height
			propertyValue = style.getPropertyValue(CSSConstants.CSS_BACKGROUND_HEIGHT_PROPERTY);
			CSSValue dimensionValueHeight = style.getPropertyCSSValue(CSSConstants.CSS_BACKGROUND_HEIGHT_PROPERTY);
			this.heightMetricPt = PropertyUtil.getDimensionValue(dimensionValueHeight);
			this.propertyHeight = propertyValue;

			if (propertyValue != null && !DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(propertyValue)
					&& !DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(propertyValue)
					&& !DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(propertyValue)) {
				try {
					if (propertyValue.endsWith("%")) {
						percentageHeight = Double.parseDouble(propertyValue.replace("%", "")) / 100;
					} else {
						DimensionValue propertyBackgroundHeight = StringUtil.parse(propertyValue);

						if (propertyBackgroundHeight.getUnits().equals(DesignChoiceConstants.UNITS_PX)) {
							pxBackgroundHeight = (int) propertyBackgroundHeight.getMeasure();
						} else {
							DimensionValue backgroundHeight = DimensionUtil.convertTo(
									propertyBackgroundHeight.getMeasure(), propertyBackgroundHeight.getUnits(),
									DesignChoiceConstants.UNITS_IN);
							pxBackgroundHeight = (int) BackgroundImageInfo.inchToPixel(backgroundHeight.getMeasure());
						}
					}
				} catch (Exception e) {
				}
			}

			// calculate the background image width
			propertyValue = style.getPropertyValue(CSSConstants.CSS_BACKGROUND_WIDTH_PROPERTY);
			CSSValue dimensionValueWidth = style.getPropertyCSSValue(CSSConstants.CSS_BACKGROUND_WIDTH_PROPERTY);
			this.widthMetricPt = PropertyUtil.getDimensionValue(dimensionValueWidth);
			this.propertyWidth = propertyValue;

			if (propertyValue != null && !DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(propertyValue)
					&& !DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(propertyValue)
					&& !DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(propertyValue)) {
				try {
					if (propertyValue.endsWith("%")) {
						percentageWidth = Double.parseDouble(propertyValue.replace("%", "")) / 100;
					} else {
						DimensionValue propertyBackgroundWidth = StringUtil.parse(propertyValue);

						if (propertyBackgroundWidth.getUnits().equals(DesignChoiceConstants.UNITS_PX)) {
							pxBackgroundWidth = (int) propertyBackgroundWidth.getMeasure();
						} else {
							DimensionValue backgroundWidth = DimensionUtil.convertTo(
									propertyBackgroundWidth.getMeasure(), propertyBackgroundWidth.getUnits(),
									DesignChoiceConstants.UNITS_IN);
							pxBackgroundWidth = (int) BackgroundImageInfo.inchToPixel(backgroundWidth.getMeasure());
						}
					}
				} catch (Exception e) {
				}
			}
		}
		this.height = pxBackgroundHeight;
		this.width = pxBackgroundWidth;
		double scaleFactorHeight = 1d;
		double scaleFactorWidth = 1d;

		if (this.image != null) {
			int dpi = this.dpi[0];
			double imageHeight = this.image.getHeight();
			double imageWidth = this.image.getWidth();

			if (dpi > 0) {
				if (this.height <= 0) {
					double inch = imageHeight / dpi;
					this.height = (int) BackgroundImageInfo.inchToPixel(inch);
					this.heightMetricPt = (int) (inch * BGI_DPI_METRIC_PT);
				} else if (pxBackgroundHeight > 0 && pxBackgroundWidth <= 0) {
					double inch = imageHeight / dpi;
					scaleFactorWidth = this.height / (BackgroundImageInfo.inchToPixel(inch));
				}
			}

			if (dpi > 0) {
				if (this.width <= 0) {
					double inch = imageWidth / dpi;
					this.width = (int) BackgroundImageInfo.inchToPixel(inch);
					this.widthMetricPt = (int) (inch * BGI_DPI_METRIC_PT);
				} else if (pxBackgroundHeight <= 0 && pxBackgroundWidth > 0) {
					double inch = imageWidth / dpi;
					scaleFactorHeight = this.width / (BackgroundImageInfo.inchToPixel(inch));
				}
			}

			if (dpi <= 0 && this.height <= 0 && this.width <= 0) {
				this.heightMetricPt = (int) (this.image.getHeight() * BGI_DPI_METRIC_PT / 100);
				this.heightMetricPt = (int) (this.image.getWidth() * BGI_DPI_METRIC_PT / 100);

				this.height = this.heightMetricPt;
				this.width = this.widthMetricPt;
			}
		}
		// auto scaling of percentage if one percentage is set and the image size is unset
		if (percentageHeight != 1.0 && percentageWidth == 1.0 && pxBackgroundWidth == 0) {
			percentageWidth = percentageHeight;
		} else if (percentageWidth != 1.0 && percentageHeight == 1.0 && pxBackgroundHeight == 0) {
			percentageHeight = percentageWidth;
		}
		this.height = (int) (this.height * percentageHeight * scaleFactorHeight);
		this.width = (int) (this.width * percentageWidth * scaleFactorWidth);
		this.heightMetricPt = (int) (this.heightMetricPt * percentageHeight * scaleFactorHeight);
		this.widthMetricPt = (int) (this.widthMetricPt * percentageWidth * scaleFactorWidth);

	}

	/**
	 * Generate the image URL (based on method ImageManager.getInstance().generateURL)
	 *
	 * @param designHandle handle of the report
	 * @param uri          of the image
	 * @return Return the URL of the image
	 * @throws MalformedURLException
	 */
	private URL generateURL(ModuleHandle designHandle, String uri) throws MalformedURLException {
		try {
			return new URL(uri);
		} catch (MalformedURLException e) {
			String path = URIUtil.getLocalPath(uri);
			if (path != null && designHandle != null) {
				return designHandle.findResource(path, IResourceLocator.IMAGE);
			}
			return URI.create(uri).toURL();
		}
	}

	/**
	 * Transforms the inch to pixel (based on method MetricUtility.inchToPixel)
	 *
	 * @param inch size of inch
	 * @return pixel value
	 */
	private static double inchToPixel(double inch) {
		return (inch * graphicDpi);
	}
}
