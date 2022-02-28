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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.emitter.ImageReader;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ImageAreaLayout implements ILayout {

	private ILayout layout = null;
	private ContainerArea parent;
	private IImageContent content;
	private ImageReader reader;
	private Image imageObject = null;
	private LayoutContext context;

	protected static Logger logger = Logger.getLogger(ImageAreaLayout.class.getName());

	private static Pattern pattern = Pattern.compile(" ([^=]*)=\"([^\"]*)\"");

	public ImageAreaLayout(ContainerArea parent, LayoutContext context, IImageContent content) {
		this.parent = parent;
		this.content = content;
		this.context = context;
	}

	@Override
	public void layout() throws BirtException {
		initialize();
		if (layout != null) {
			layout.layout();
		}
	}

	protected void initialize() throws BirtException {
		// choose the layout manager
		reader = new ImageReader(content, context.getSupportedImageFormats());
		int result = reader.read();
		switch (result) {
		case ImageReader.RESOURCE_UNREACHABLE:
			// display the alt text or prompt object not accessible.
			layout = createAltTextLayout(ImageReader.RESOURCE_UNREACHABLE);
			break;
		case ImageReader.UNSUPPORTED_OBJECTS:
			// display the alt text or prompt unsupported objects.
			layout = createAltTextLayout(ImageReader.UNSUPPORTED_OBJECTS);
			break;
		case ImageReader.OBJECT_LOADED_SUCCESSFULLY:
			// the object is accessible.
			if (reader.getType() == ImageReader.TYPE_IMAGE_OBJECT
					|| reader.getType() == ImageReader.TYPE_CONVERTED_SVG_OBJECT) {
				try {
					imageObject = Image.getInstance(reader.getByteArray());
				} catch (Exception e) {
					logger.log(Level.WARNING, e.getLocalizedMessage());
				}
				// unrecognized image formats.
				if (imageObject == null) {
					layout = createAltTextLayout(ImageReader.UNSUPPORTED_OBJECTS);
					break;
				}
			}
			layout = new ConcreteImageLayout(context, parent, content, reader.getByteArray());
			break;
		}
	}

	private ILayout createAltTextLayout(int altTextType) {
		ITextContent altTextContent = createAltText((IImageContent) content, altTextType);
		if (null == altTextContent) {
			return null;
		}
		BlockTextArea bta = new BlockTextArea(parent, context, altTextContent);
		bta.setHelpText(altTextContent.getText());
		return bta;
	}

	private ITextContent createAltText(IImageContent imageContent, int altTextType) {
		IReportContent report = imageContent.getReportContent();
		if (report == null) {
			return null;
		}
		ITextContent altTextContent = report.createTextContent(imageContent);
		altTextContent.setParent(imageContent.getParent());
		String alt = imageContent.getAltText();
		if (null == alt) {
			ULocale locale = ULocale.forLocale(context.getLocale());
			if (locale == null) {
				locale = ULocale.getDefault();
			}
			EngineResourceHandle resourceHandle = new EngineResourceHandle(locale);
			if (altTextType == ImageReader.UNSUPPORTED_OBJECTS) {
				if (reader.getType() == ImageReader.TYPE_FLASH_OBJECT) {
					alt = resourceHandle.getMessage(MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT);
				} else {
					alt = resourceHandle.getMessage(MessageConstants.REPORT_ITEM_NOT_SUPPORTED_PROMPT);
				}
			}
			if (altTextType == ImageReader.RESOURCE_UNREACHABLE) {
				alt = resourceHandle.getMessage(MessageConstants.RESOURCE_UNREACHABLE_PROMPT);
			}
		}
		altTextContent.setText(alt);
		return altTextContent;
	}

	class ConcreteImageLayout implements ILayout {

		/** The DpiX */
		private int resolutionX = 0;

		/** The DpiY */
		private int resolutionY = 0;

		// private Image imageObject = null;
		private byte[] data;

		private ContainerArea parent;

		protected final static int DEFAULT_WIDHT = 212000;

		protected final static int DEFAULT_HEIGHT = 130000;

		protected IImageContent image;

		protected ContainerArea root;

		private Dimension intrinsic;

		private static final String BOOKMARK_JAVASCRIPT_PREFIX = "javascript:catchBookmark('";

		private static final String BOOKMARK_URL_PREFIX = "__bookmark=";

		private static final String BOOKMARK_ANCHOR_PREFIX = "#";

		private LayoutContext context;

		private boolean fitToContainer = false;

		private BlockTextArea innerText = null;

		public ConcreteImageLayout(LayoutContext context, ContainerArea parent, IImageContent content, byte[] data) {
			this.context = context;
			this.image = content;
			this.parent = parent;
			this.data = data;

			Object reportItemDesign = content.getGenerateBy();
			if (null != reportItemDesign) {
				if (reportItemDesign instanceof ImageItemDesign) {
					fitToContainer = ((ImageItemDesign) reportItemDesign).isFitToContainer();
				}
			}
		}

		/**
		 * get intrinsic dimension of image in pixels. Now only support png, bmp, jpg,
		 * gif.
		 *
		 * @return
		 * @throws IOException
		 * @throws MalformedURLException
		 * @throws BadElementException
		 */
		protected Dimension getIntrinsicDimension(IImageContent content, Image image) {
			if (image != null) {
				return new Dimension((int) (image.getPlainWidth() * 1000 / resolutionX * 72),
						(int) (image.getPlainHeight() * 1000 / resolutionY * 72));
			}
			return null;
		}

		protected Dimension getSpecifiedDimension(IImageContent content, int pWidth, boolean scale) {
			// prepare the DPI for the image.
			int imageFileDpiX = 0;
			int imageFileDpiY = 0;

			if (reader.getType() == ImageReader.TYPE_IMAGE_OBJECT
					|| reader.getType() == ImageReader.TYPE_CONVERTED_SVG_OBJECT) {
				if (imageObject != null) {
					imageFileDpiX = imageObject.getDpiX();
					imageFileDpiY = imageObject.getDpiY();
				}
			}
			resolutionX = PropertyUtil.getImageDpi(content, imageFileDpiX, context.getDpi());
			resolutionY = PropertyUtil.getImageDpi(content, imageFileDpiY, context.getDpi());

			try {
				intrinsic = getIntrinsicDimension(content, imageObject);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getLocalizedMessage());
			}
			int specifiedWidth = PropertyUtil.getImageDimensionValue(content, content.getWidth(), context.getDpi(),
					pWidth);
			int specifiedHeight = PropertyUtil.getImageDimensionValue(content, content.getHeight(), context.getDpi(),
					-1);

			Dimension dim = new Dimension(DEFAULT_WIDHT, DEFAULT_HEIGHT);
			if (intrinsic == null) {
				dim.setDimension(specifiedWidth == -1 ? DEFAULT_WIDHT : specifiedWidth,
						specifiedHeight == -1 ? DEFAULT_HEIGHT : specifiedHeight);
				return dim;
			}
			if (scale) // always does scale.
			{
				double ratio = intrinsic.getRatio();

				if (specifiedWidth >= 0) {
					if (specifiedHeight >= 0) {
						dim.setDimension(specifiedWidth, specifiedHeight);
					} else {
						dim.setDimension(specifiedWidth, (int) (specifiedWidth / ratio));
					}
				} else {
					if (specifiedHeight >= 0) {
						dim.setDimension((int) (specifiedHeight * ratio), specifiedHeight);
					} else {
						dim.setDimension(intrinsic.getWidth(), intrinsic.getHeight());
					}
				}
			} else if (specifiedWidth >= 0) {
				if (specifiedHeight >= 0) {
					dim.setDimension(specifiedWidth, specifiedHeight);
				} else {
					dim.setDimension(specifiedWidth, intrinsic.getHeight());
				}
			} else {
				if (specifiedHeight >= 0) {
					dim.setDimension(intrinsic.getWidth(), specifiedHeight);
				} else {
					dim.setDimension(intrinsic.getWidth(), intrinsic.getHeight());
				}
			}
			return dim;
		}

		@Override
		public void layout() throws BirtException {
			init();

			boolean isEmptyLine = true;
			boolean innerTextInserted = false;
			if ("pdf".equalsIgnoreCase(context.getFormat()) && reader.getType() == ImageReader.TYPE_FLASH_OBJECT) {
				innerTextInserted = true;
				innerText = createInnerTextLayout();
				innerText.content.getStyle().setProperty(IStyle.STYLE_TEXT_ALIGN, IStyle.CENTER_VALUE);
				innerText.setVerticalAlign(IStyle.MIDDLE_VALUE);
				innerText.setIgnoreReordering(true);
				// save current root status
				if (PropertyUtil.isInlineElement(image)) {
					// inline image
					InlineStackingArea lineParent = (InlineStackingArea) parent;
					isEmptyLine = lineParent.isEmptyLine();
				}
				int lastIP = root.currentIP;
				int lastBP = root.currentBP;

				innerText.layout();
				// set the text position manually.
				innerText.setAllocatedPosition(0, 0);
				int rootHeight = root.getContentHeight();
				if (rootHeight < innerText.getHeight()) {
					innerText.setHeight((rootHeight - 1000) > 0 ? (rootHeight - 1000) : 0);
					innerText.setNeedClip(true);
				}
				// restore the root status.
				root.currentIP = lastIP;
				root.currentBP = lastBP;
			}

			// For inline image, the hierarchy is
			// LineArea->InlineContainer->ImageArea.
			// the root is InlineContainer, so we just need to add the root
			// directly to its parent(LineArea).
			// In LineAreaLM, the lineArea will enlarge itself to hold the root.
			// For block image, the hierarchy is BlockContainer->ImageArea
			if (PropertyUtil.isInlineElement(image)) {
				// inline image
				assert (parent instanceof InlineStackingArea);
				InlineStackingArea lineParent = (InlineStackingArea) parent;
				if (root.getAllocatedWidth() > parent.getCurrentMaxContentWidth()) {
					if ((innerTextInserted && !isEmptyLine) || (!innerTextInserted && !lineParent.isEmptyLine())) {
						lineParent.endLine(false);
						layout();
					} else {
						parent.add(root);
						root.finished = true;
						parent.update(root);
					}
				} else {
					parent.add(root);
					root.finished = true;
					parent.update(root);
				}
			} else {
				parent.add(root);
				if (!parent.isInInlineStacking && context.isAutoPageBreak()) {
					int aHeight = root.getAllocatedHeight();
					if (aHeight + parent.getAbsoluteBP() > context.getMaxBP()) {
						parent.autoPageBreak();
					}
				}
				root.finished = true;
				parent.update(root);
			}
			checkDisplayNone();
		}

		protected void checkDisplayNone() {
			if (context != null && context.isDisplayNone()) {
				int aHeight = root.getAllocatedHeight();
				parent.setCurrentBP(parent.currentBP - aHeight);
				root.height = 0;
			}
		}

		protected void init() throws BirtException {

			if (PropertyUtil.isInlineElement(image)) {
				root = new ImageInlineContainer(parent, context, image);
			} else {
				root = new ImageBlockContainer(parent, context, image);
			}

			root.initialize();

			// First, the width of root is set to its parent's max available
			// width.
			root.setAllocatedWidth(parent.getMaxAvaWidth());
			root.setMaxAvaWidth(root.getContentWidth());
			Dimension contentDimension = getSpecifiedDimension(image, root.getContentWidth(), true);
			ImageArea imageArea = createImageArea(image);
			imageArea.setParent(root);
			// implement fitToContainer the maxHeight is the image's max
			// possible height in an empty page.
			int maxHeight = root.getMaxAvaHeight();
			int maxWidth = root.getMaxAvaWidth();
			int cHeight = contentDimension.getHeight();
			int cWidth = contentDimension.getWidth();

			int actualHeight = cHeight;
			int actualWidth = cWidth;

			if (cHeight > maxHeight || cWidth > maxWidth) {
				if (fitToContainer) {
					float rh = ((float) maxHeight) / cHeight;
					float rw = ((float) maxWidth) / cWidth;
					if (rh > rw) {
						actualHeight = (int) ((float) cHeight * maxWidth / cWidth);
						actualWidth = maxWidth;
					} else {
						actualHeight = maxHeight;
						actualWidth = (int) ((float) cWidth * maxHeight / cHeight);
					}
					imageArea.setWidth(actualWidth);
					imageArea.setHeight(actualHeight);
					root.setContentWidth(imageArea.getWidth());
					root.setContentHeight(imageArea.getHeight());
				} else // Fix Bugzilla – Bug 268921 [Automation][Regression]Fit to
				// page does not work in PDF
				if (context.getPageOverflow() == IPDFRenderOption.FIT_TO_PAGE_SIZE
						|| context.getPageOverflow() == IPDFRenderOption.ENLARGE_PAGE_SIZE) {
					imageArea.setWidth(actualWidth);
					imageArea.setHeight(actualHeight);
					root.setContentHeight(actualHeight);
					root.setContentWidth(actualWidth);
				} else {
					imageArea.setWidth(actualWidth);
					imageArea.setHeight(actualHeight);
					root.setNeedClip(true);
					root.setContentHeight(Math.min(maxHeight, cHeight));
					root.setContentWidth(Math.min(maxWidth, cWidth));
					// Fix Bugzilla - Bug 271555 The right and bottom border are still shown even
					// the chart exceeds the page size and got cut in PDF [1200]
					// a temporary solution. root should set the same dimension with imageArea, but
					// currently can not find a solution to avoid empty page when a large image is
					// put into a grid
					if (maxWidth < cWidth) {
						// default box style is unmodified.
						// creates a new instance when style is default style.
						if (root.getBoxStyle() == BoxStyle.DEFAULT) {
							root.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						root.getBoxStyle().setRightBorder(null);
					}
					if (maxHeight < cHeight) {
						if (root.getBoxStyle() == BoxStyle.DEFAULT) {
							root.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						root.getBoxStyle().setBottomBorder(null);
					}
				}
			} else {
				imageArea.setWidth(actualWidth);
				imageArea.setHeight(actualHeight);
				root.setContentWidth(imageArea.getWidth());
				root.setContentHeight(imageArea.getHeight());
			}
			root.addChild(imageArea);
			imageArea.setPosition(root.getContentX(), root.getContentY());

			if (context.getEngineTaskType() != IEngineTask.TASK_RUN) {
				processChartLegend(image, imageArea);
			}
			root.finished = false;
		}

		protected ImageArea createImageArea(IImageContent content) {
			ImageArea area = new ImageArea();
			String mimeType = content.getMIMEType();
			String extension = content.getExtension();
			area.setExtension(extension);
			area.setMIMEType(mimeType);
			switch (content.getImageSource()) {
			case IImageContent.IMAGE_FILE:
			case IImageContent.IMAGE_URL:
				area.setUrl(content.getURI());
				break;
			case IImageContent.IMAGE_NAME:
				area.setUrl("NamedImage_" + content.getURI());
				break;
			case IImageContent.IMAGE_EXPRESSION:
				break;
			}

			area.setData(data);

			if (reader.getType() == ImageReader.TYPE_SVG_OBJECT) {
				area.setMIMEType("image/svg+xml");
				area.setExtension(".svg");
			}
			if (reader.getType() == ImageReader.TYPE_CONVERTED_SVG_OBJECT) {
				// this SVG has been converted into JPEG.
				area.setMIMEType("image/jpeg");
				area.setExtension(".jpg");
			}

			if (content instanceof ObjectContent) {
				ObjectContent object = (ObjectContent) content;
				area.setParameters(object.getParamters());
			}
			area.setAction(content.getHyperlinkAction());
			return area;
		}

		private BlockTextArea createInnerTextLayout() {
			IReportContent report = image.getReportContent();
			if (report == null) {
				return null;
			}
			ITextContent promptTextContent = report.createTextContent(image);
			ULocale locale = ULocale.forLocale(context.getLocale());
			if (locale == null) {
				locale = ULocale.getDefault();
			}
			EngineResourceHandle resourceHandle = new EngineResourceHandle(locale);

			String prompt = resourceHandle.getMessage(MessageConstants.UPDATE_USER_AGENT_PROMPT);

			promptTextContent.setText(prompt);
			return new BlockTextArea(root, context, promptTextContent);
		}

		/**
		 * Creates legend for chart.
		 *
		 * @param imageContent the image content of the chart.
		 * @param imageArea    the imageArea of the chart.
		 */
		private void processChartLegend(IImageContent imageContent, IImageArea imageArea) {
			Object imageMapObject = imageContent.getImageMap();
			boolean hasImageMap = (imageMapObject != null) && (imageMapObject instanceof String)
					&& (((String) imageMapObject).length() > 0);
			if (hasImageMap) {
				createImageMap((String) imageMapObject, imageArea);
			}
		}

		private void createImageMap(String imageMapObject, IImageArea imageArea) {
			if (imageMapObject == null) {
				return;
			}
			String[] maps = imageMapObject.split("/>");

			for (String map : maps) {
				map = map.trim();
				if (map.length() == 0) {
					continue;
				}
				Map<String, String> attributes = new TreeMap<>();
				Matcher matcher = pattern.matcher(map);
				while (matcher.find()) {
					attributes.put(matcher.group(1), matcher.group(2));
				}
				try {
					if (attributes.size() > 0) {
						int[] area = getArea(attributes.get("coords"));
						if (area == null) {
							continue;
						}
						String url = attributes.get("href");
						// does not support javascript in href.
						if (url != null && url.startsWith("javascript:")) {
							url = null;
						}
						String targetWindow = attributes.get("target");
						createImageMap(area, imageArea, url, targetWindow);
					}
				} catch (NumberFormatException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
			}
		}

		private void createImageMap(int[] area, IImageArea imageArea, String url, String targetWindow) {
			if (url == null || url.length() == 0) {
				return;
			}
			url = url.replace("&amp;", "&");
			ActionContent link = new ActionContent();
			String bookmark = getBookmark(url);
			if (bookmark != null) {
				link.setBookmark(bookmark);
			} else {
				link.setHyperlink(url, targetWindow);
			}
			area = getAbsoluteArea(area, imageArea);
			createImageMapContainer(area[0], area[1], area[2], area[3], link);
		}

		/**
		 * Creates an image map container, which is an empty container with an hyper
		 * link.
		 *
		 * @param x      x coordinate of lower left corner of the container.
		 * @param y      y coordinate of lower left corner of the container.
		 * @param width  width of the container.
		 * @param height height of the container.
		 * @param link   destination of the hyperlink.
		 */
		private void createImageMapContainer(int x, int y, int width, int height, IHyperlinkAction link) {
			BlockContainerArea area = new BlockContainerArea();
			area.setAction(link);
			area.setPosition(x, y);
			area.setWidth(width);
			area.setHeight(height);
			root.addChild(area);
		}

		/**
		 * Calculates the absolute positions of image map when given the position of
		 * image. The image map position is relative to the left up corner of the image.
		 *
		 * The argument and returned value are both 4 length integer area, the four
		 * value of which are x, y of up left corner, width and height respectively.
		 *
		 * @param area      rectangle area of a image map.
		 * @param imageArea image area of the image in which the image map is.
		 * @return absolute position of the image map.
		 */
		private int[] getAbsoluteArea(int[] area, IImageArea imageArea) {
			int[] result = new int[4];
			if (intrinsic == null) {
				// this case is for SVG chart.
				// the image map of SVG chart is in Point.
				int imageX = imageArea.getX();
				int imageY = imageArea.getY();
				result[0] = imageX + (int) (area[0] * 1000);
				result[2] = (int) (area[2] * 1000);
				result[1] = imageY + (int) (area[1] * 1000);
				result[3] = (int) (area[3] * 1000);
			} else {
				for (int i = 0; i < 4;) {
					area[i] = getTranslatedLengthX(area[i]);
					i++;
					area[i] = getTranslatedLengthY(area[i]);
					i++;
				}
				int imageX = imageArea.getX();
				int imageY = imageArea.getY();
				int imageHeight = imageArea.getHeight();
				int imageWidth = imageArea.getWidth();
				int intrinsicWidth = intrinsic.getWidth();
				int intrinsicHeight = intrinsic.getHeight();
				float ratio = (float) imageWidth / (float) intrinsicWidth;
				result[0] = imageX + (int) (area[0] * ratio);
				result[2] = (int) (area[2] * ratio);
				ratio = (float) imageHeight / (float) intrinsicHeight;
				result[1] = imageY + (int) (area[1] * ratio);
				result[3] = (int) (area[3] * ratio);
			}
			return result;
		}

		/**
		 * Parse the image map position from a string which is of format "x1, y1, x2,
		 * y2".
		 *
		 * @param string the position string.
		 * @return a array which contains the x, y coordinate of left up corner, width
		 *         and height in sequence.
		 *
		 */
		private int[] getArea(String string) {
			if (string == null) {
				return null;
			}
			String[] rawDatas = string.split(",");
			if (rawDatas.length == 8) {
				int[] area = new int[4];
				area[0] = Integer.parseInt(rawDatas[0]);
				area[1] = Integer.parseInt(rawDatas[1]);
				area[2] = Integer.parseInt(rawDatas[4]) - area[0];
				area[3] = Integer.parseInt(rawDatas[5]) - area[1];
				return area;
			} else if (rawDatas.length >= 6) {
				return generateRectangleByPolygon(rawDatas);
			}
			return null;
		}

		private int[] generateRectangleByPolygon(String[] values) {
			int[] intValues = convertToInt(values);
			int[] xValues = new int[values.length / 2];
			for (int i = 0; i < values.length; i = i + 2) {
				xValues[i / 2] = intValues[i];
			}
			int[] yValues = new int[values.length / 2];
			for (int i = 1; i < values.length; i = i + 2) {
				yValues[i / 2] = intValues[i];
			}
			int maxX = getMax(xValues);
			int maxY = getMax(yValues);
			int minX = getMin(xValues);
			int minY = getMin(yValues);
			int avaX = getAva(xValues);
			int avaY = getAva(yValues);
			return new int[] { avaX - (avaX - minX) / 2, avaY - (avaY - minY) / 2, (maxX - minX) / 2,
					(maxY - minY) / 2 };
		}

		private int getMax(int[] values) {
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < values.length; i++) {
				if (values[i] > max) {
					max = values[i];
				}
			}
			return max;
		}

		private int getAva(int[] values) {
			int total = 0;
			for (int i = 0; i < values.length; i++) {
				total += values[i];
			}
			return total / values.length;
		}

		private int getMin(int[] values) {
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < values.length; i++) {
				if (values[i] < min) {
					min = values[i];
				}
			}
			return min;
		}

		private int[] convertToInt(String[] values) {
			int[] intValues = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				intValues[i] = Integer.parseInt(values[i]);
			}
			return intValues;
		}

		private int getTranslatedLengthX(int length) {
			return length * 1000 / resolutionX * 72;
		}

		private int getTranslatedLengthY(int length) {
			return length * 1000 / resolutionY * 72;
		}

		/**
		 * Parses out bookmark name from a url for interanl bookmark.
		 *
		 * @param url the url string
		 * @return the bookmark name.
		 */
		private String getBookmark(String url) {
			int start = url.indexOf(BOOKMARK_URL_PREFIX);
			int end = -1;
			if (start != -1) {
				start += BOOKMARK_URL_PREFIX.length();
				end = url.indexOf("&", start);
				if (end == -1) {
					end = url.length();
				}
				return url.substring(start, end);
			} else if (url.startsWith(BOOKMARK_ANCHOR_PREFIX)) {
				start = BOOKMARK_ANCHOR_PREFIX.length();
				end = url.length();
				return url.substring(start, end);
			} else if (url.startsWith(BOOKMARK_JAVASCRIPT_PREFIX) && url.endsWith("')")) {
				start = BOOKMARK_JAVASCRIPT_PREFIX.length();
				end = url.length() - 2;
				return url.substring(start, end);
			}
			return null;
		}

		protected void close() {
			// if ( !PropertyUtil.isInlineElement( image ) )
			// We align inline elements (here - inline container parenting the
			// inline image) in LineLayout, but not block-level image.
			// Invoke it here, since it should not be done by ContainerLayout
			// always.
			// TODO: Check if this can be done in a neater way.
			// parent.align( root );
		}

	}
}
