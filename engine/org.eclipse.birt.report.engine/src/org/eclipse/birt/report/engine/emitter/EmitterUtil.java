/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.lowagie.text.Image;

/**
 * Utility class for emitter
 *
 * @since 3.3
 *
 */
public class EmitterUtil {

	protected static Logger logger = Logger.getLogger(EmitterUtil.class.getName());

	private static final String URL_IMAGE_TYPE_SVG = "image/svg+xml";
	private static final String URL_PROTOCOL_TYPE_DATA = "data:";
	private static final String URL_PROTOCOL_TYPE_DATA_BASE = ";base64,";
	private static final String URL_PROTOCOL_TYPE_DATA_UTF8 = ";utf8,";

	/**
	 * Get output stream
	 *
	 * @param services          service
	 * @param defaultOutputFile output file
	 * @return output stream
	 * @throws EngineException
	 */
	public static OutputStream getOuputStream(IEmitterServices services, String defaultOutputFile)
			throws EngineException {
		OutputStream out = null;
		Object fd = services.getOption(IRenderOption.OUTPUT_FILE_NAME);
		File file = null;
		try {
			if (fd != null) {
				file = new File(fd.toString());
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				out = new BufferedOutputStream(new FileOutputStream(file));
			}
		} catch (FileNotFoundException e) {
			throw new EngineException(MessageConstants.FAILED_TO_INITIALIZE_EMITTER, e);
		}

		if (out == null) {
			Object value = services.getOption(IRenderOption.OUTPUT_STREAM);
			if (value instanceof OutputStream) {
				Object closeOnExitValue = services.getOption(IRenderOption.CLOSE_OUTPUTSTREAM_ON_EXIT);
				boolean closeOnExit = false;
				if (closeOnExitValue instanceof Boolean) {
					closeOnExit = ((Boolean) closeOnExitValue).booleanValue();
				}
				out = new EmitterOutputStream((OutputStream) value, closeOnExit);
			} else {
				try {
					file = new File(defaultOutputFile);
					out = new BufferedOutputStream(new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					throw new EngineException(MessageConstants.FAILED_TO_INITIALIZE_EMITTER, e);
				}
			}
		}
		return out;
	}

	private static class EmitterOutputStream extends FilterOutputStream {
		private boolean closeOutputStreamOnExit;

		public EmitterOutputStream(OutputStream out, boolean closeOutputStreamOnExit) {
			super(out);
			this.closeOutputStreamOnExit = closeOutputStreamOnExit;
		}

		@Override
		public void write(byte[] b) throws IOException {
			out.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
		}

		@Override
		public void close() throws IOException {
			try {
				flush();
			} catch (IOException ignored) {
			}
			if (closeOutputStreamOnExit) {
				out.close();
			}
		}
	}

	/**
	 * Get the image
	 *
	 * @param content image content
	 * @return the image
	 */
	public static Image getImage(IImageContent content) {
		Image image = null;
		try {
			String uri = content.getURI();
			String mimeType = content.getMIMEType();
			String extension = content.getExtension();
			switch (content.getImageSource()) {
			case IImageContent.IMAGE_FILE:
				ResourceLocatorWrapper rl = null;
				ExecutionContext exeContext = ((ReportContent) content.getReportContent()).getExecutionContext();
				if (exeContext != null) {
					rl = exeContext.getResourceLocator();
				}
				ReportDesignHandle design = content.getReportContent().getDesign().getReportDesign();
				byte[] buffer = null;
				if (rl == null) {
					URL url = design.findResource(uri, IResourceLocator.IMAGE,
							content.getReportContent().getReportContext() == null ? null
									: content.getReportContent().getReportContext().getAppContext());
					buffer = getImageByteArray(url.openStream());
				} else {
					buffer = rl.findResource(design, uri, IResourceLocator.IMAGE,
							content.getReportContent().getReportContext() == null ? null
									: content.getReportContent().getReportContext().getAppContext());
				}

				if (buffer != null) {
					if (SvgFile.isSvg(content.getURI())) {
						buffer = SvgFile.transSvgToArray(new ByteArrayInputStream(buffer));
					}
					image = Image.getInstance(buffer);
				}
				break;
			case IImageContent.IMAGE_NAME:
			case IImageContent.IMAGE_EXPRESSION:
				byte[] data = content.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				if (SvgFile.isSvg(mimeType, uri, extension)) {
					data = SvgFile.transSvgToArray(in);
				}
				in.close();
				image = Image.getInstance(data);
				break;

			case IImageContent.IMAGE_URL:
				if (SvgFile.isSvg(uri)) {
					image = Image.getInstance(SvgFile.transSvgToArray(uri));
				} else {
					image = Image.getInstance(new URL(content.getURI()));
				}

				break;
			default:
				assert (false);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}
		return image;
	}

	private static byte[] getImageByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int size = in.read(buffer);
		while (size != -1) {
			out.write(buffer, 0, size);
			size = in.read(buffer);
		}
		buffer = out.toByteArray();
		out.close();
		return buffer;
	}

	/** property: italic horizontal coefficient */
	public static float ITALIC_HORIZONTAL_COEFFICIENT = (float) Math.tan(15f * Math.PI / 180);

	/**
	 * Get the italic horizontal coefficient
	 *
	 * @return the italic horizontal coefficient
	 */
	public static float getItalicHorizontalCoefficient() {
		return ITALIC_HORIZONTAL_COEFFICIENT;
	}

	/**
	 * Get the color as hex value
	 *
	 * @param color rgb color
	 * @return the hex color value
	 */
	public static String getColorString(Color color) {
		StringBuffer buffer = new StringBuffer();
		appendComponent(buffer, color.getRed());
		appendComponent(buffer, color.getGreen());
		appendComponent(buffer, color.getBlue());
		return buffer.toString();
	}

	/**
	 * Append the component to buffer
	 *
	 * @param buffer    buffer
	 * @param component rgb color value
	 */
	public static void appendComponent(StringBuffer buffer, int component) {
		String hex = Integer.toHexString(component);
		if (hex.length() == 1) {
			buffer.append('0');
		}
		buffer.append(hex);
	}

	/**
	 * Get the image extension
	 *
	 * @param imageURI image URI
	 * @return the image extension
	 */
	public static String getImageExtension(String imageURI) {
		String rectifiedImageURI = imageURI.replace('.', '&');
		String extension = imageURI.substring(rectifiedImageURI.lastIndexOf('&') + 1).toLowerCase();

		if (extension.equals("svg")) {
			extension = "jpg";
		}
		return extension;
	}

	/**
	 * Get the image size
	 *
	 * @param imageURI image URI
	 * @return the image size
	 */
	public static Position getImageSize(String imageURI) {
		InputStream imageStream = null;
		Position imageSize = new Position(0, 0);
		try {
			URL url = new URL(imageURI);
			imageStream = url.openStream();
			java.awt.Image image = ImageIO.read(imageStream);
			ImageIcon imageIcon = new ImageIcon(image);
			imageSize.setX(imageIcon.getIconWidth());
			imageSize.setY(imageIcon.getIconHeight());
		} catch (IOException ioe) {
			logger.log(Level.WARNING, ioe.getMessage(), ioe);
		} finally {
			if (imageStream != null) {
				try {
					imageStream.close();
				} catch (IOException e) {
				}
			}
		}
		return imageSize;
	}

	/**
	 * Get the image data
	 *
	 * @param imageURI image URI
	 * @return the image data
	 * @throws IOException
	 */
	public static byte[] getImageData(String imageURI) throws IOException {
		byte[] imageData = null;
		if (SvgFile.isSvg(imageURI)) {
			try {
				imageData = SvgFile.transSvgToArray(imageURI);
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage());
			}
		} else {
			imageData = getNonSVGImageData(imageURI);
		}
		return imageData;
	}

	private static byte[] getNonSVGImageData(String imageURI) throws IOException {
		InputStream imageStream = null;
		byte[] imageData = null;
		try {
			if (imageURI.startsWith(URL_PROTOCOL_TYPE_DATA) && imageURI.contains(URL_PROTOCOL_TYPE_DATA_BASE)) {
				String base64[] = imageURI.split(URL_PROTOCOL_TYPE_DATA_BASE);
				if (base64.length >= 2) {
					String encodedImg = base64[1];
					imageData = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
				}
			} else {
				URL url = new URL(imageURI);
				imageStream = url.openStream();
				imageData = readData(imageStream);
			}
		} finally {
			if (imageStream != null) {
				try {
					imageStream.close();
				} catch (IOException e) {
				}
			}
		}
		return imageData;
	}

	/**
	 * Read the image data from stream
	 *
	 * @param imageStream image stream
	 * @return the image data
	 * @throws IOException
	 */
	public static byte[] readData(InputStream imageStream) throws IOException {
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		int data = -1;
		while ((data = imageStream.read()) >= 0) {
			byteArrayStream.write(data);
		}
		byteArrayStream.close();
		return byteArrayStream.toByteArray();
	}

	/**
	 * Get the background image URL
	 *
	 * @param style        style of the context
	 * @param reportDesign report design
	 * @param context      context element
	 * @return the background image URI
	 */
	public static String getBackgroundImageUrl(IStyle style, ReportDesignHandle reportDesign, Map<?, ?> context) {
		String imageUri = PropertyUtil.getBackgroundImage(style.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE));
		if (imageUri != null) {
			String url = getImageUrl(imageUri, reportDesign, context);
			if (url != null && url.length() > 0) {
				return url;
			}
		}
		return null;
	}

	/**
	 * Get the image URL
	 *
	 * @param style        style of the context
	 * @param reportDesign report design
	 * @param context      context element
	 * @return the background image URI
	 */
	private static String getImageUrl(String imageUri, ReportDesignHandle reportDesign, Map<?, ?> context) {
		String imageUrl = imageUri;
		if (reportDesign != null) {
			URL url = reportDesign.findResource(imageUri, IResourceLocator.IMAGE, context);
			if (url != null) {
				imageUrl = url.toExternalForm();
			}
		}
		return imageUrl;
	}

	/**
	 * Parse the image
	 *
	 * @param image       image content
	 * @param imageSource image source
	 * @param uri         image URI
	 * @param mimeType    image type
	 * @param extension   image extension
	 * @return the image element
	 * @throws IOException
	 */
	public static org.eclipse.birt.report.engine.layout.emitter.Image parseImage(IImageContent image, int imageSource,
			String uri, String mimeType, String extension) throws IOException {
		org.eclipse.birt.report.engine.layout.emitter.Image imageInfo = null;
		byte[] data = null;
		InputStream in = null;
		try {
			switch (imageSource) {
			case IImageContent.IMAGE_FILE:
			case IImageContent.IMAGE_URL:
				if (uri != null) {
					if (SvgFile.isSvg(uri)) {
						try {
							if (uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
								// url svg stream
								String svgSplitter = "svg\\+xml,";
								if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_UTF8)) {
									svgSplitter = "svg\\+xml;utf8,";
								} else if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_BASE)) {
									svgSplitter = "svg\\+xml;base64,";
								}
								String[] uriParts = uri.split(svgSplitter);
								if (uriParts.length >= 2) {
									String encodedImg = uriParts[1];
									String decodedImg = encodedImg;
									if (uri.contains(URL_PROTOCOL_TYPE_DATA_BASE)) {
										decodedImg = new String(Base64.getDecoder()
												.decode(encodedImg.getBytes(StandardCharsets.UTF_8)));
									}
									decodedImg = java.net.URLDecoder.decode(decodedImg, StandardCharsets.UTF_8);
									data = SvgFile
											.transSvgToArray(new ByteArrayInputStream(decodedImg.getBytes()));
								}
							} else {
								// url svg file load
								data = SvgFile.transSvgToArray(uri);
							}

						} catch (Exception e) {
							logger.log(Level.WARNING, e.getMessage());
						}
					} else {
						data = getNonSVGImageData(uri);
					}
				}
				break;
			case IImageContent.IMAGE_NAME:
			case IImageContent.IMAGE_EXPRESSION:
				data = image.getData();
				if (SvgFile.isSvg(mimeType, uri, extension) && null != data) {
					in = new ByteArrayInputStream(data);
					try {
						data = SvgFile.transSvgToArray(in);
					} catch (Exception e) {
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				break;
			}
			imageInfo = new org.eclipse.birt.report.engine.layout.emitter.Image();
			if (data != null) {
				imageInfo.setInput(data);
				if (!imageInfo.check()) {
					imageInfo.setData(null);
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception t) {
					logger.log(Level.WARNING, t.getMessage());
				}
			}
		}
		return imageInfo;
	}

	/**
	 * Parse the image
	 *
	 * @param imageData image data
	 * @param mimeType  image type
	 * @param extension image extension
	 * @return image element
	 * @throws IOException
	 */
	public static org.eclipse.birt.report.engine.layout.emitter.Image parseImage(byte[] imageData, String mimeType,
			String extension) throws IOException {
		if (imageData != null) {
			org.eclipse.birt.report.engine.layout.emitter.Image imageInfo = new org.eclipse.birt.report.engine.layout.emitter.Image();
			imageInfo.setInput(imageData);
			if (!imageInfo.check()) {
				imageInfo.setData(null);
			}
			return imageInfo;
		}
		return null;
	}

	/**
	 * Get the hyperlink URL
	 *
	 * @param linkAction     link action
	 * @param reportRunnable report
	 * @param actionHandler  action handler
	 * @param reportContext  report context
	 * @return the hyperlink
	 */
	public static String getHyperlinkUrl(IHyperlinkAction linkAction, IReportRunnable reportRunnable,
			IHTMLActionHandler actionHandler, IReportContext reportContext) {
		String systemId = reportRunnable == null ? null : reportRunnable.getReportName();
		Action action = new Action(systemId, linkAction);
		if (actionHandler != null) {
			return actionHandler.getURL(action, reportContext);
		}
		return linkAction.getHyperlink();
	}

	/**
	 * Convert valid color format to "000000"
	 *
	 * @param color color
	 * @return hex color value
	 */
	public static String parseColor(String color) {
		if ("transparent".equalsIgnoreCase(color) || color == null || color.length() == 0) {
			return null;
		}
		if (color.startsWith("#")) {
			return color.substring(1, Math.min(color.length(), 7));
		} else if (color.equalsIgnoreCase("Black")) {
			return "000000";
		} else if (color.equalsIgnoreCase("Gray")) {
			return "121212";
		} else if (color.equalsIgnoreCase("White")) {
			return "ffffff";
		} else if (color.equalsIgnoreCase("Red")) {
			return "ff0000";
		} else if (color.equalsIgnoreCase("Green")) {
			return "00ff00";
		} else if (color.equalsIgnoreCase("Yellow")) {
			return "ffff00";
		} else if (color.equalsIgnoreCase("Blue")) {
			return "0000ff";
		} else if (color.equalsIgnoreCase("Teal")) {
			return "008080";
		} else if (color.equalsIgnoreCase("Aqua")) {
			return "00FFFF";
		} else if (color.equalsIgnoreCase("Silver")) {
			return "C0C0C0";
		} else if (color.equalsIgnoreCase("Navy")) {
			return "000080";
		} else if (color.equalsIgnoreCase("Lime")) {
			return "00FF00";
		} else if (color.equalsIgnoreCase("Olive")) {
			return "808000";
		} else if (color.equalsIgnoreCase("Purple")) {
			return "800080";
		} else if (color.equalsIgnoreCase("Fuchsia")) {
			return "FF00FF";
		} else if (color.equalsIgnoreCase("Maroon")) {
			return "800000";
		} else if (color.equalsIgnoreCase("Orange")) {
			return "FFA500";
		}
		String[] values = color.substring(color.indexOf("(") + 1, color.length() - 1).split(",");
		String value = "";
		for (int i = 0; i < values.length; i++) {
			try {
				String s = Integer.toHexString((Integer.parseInt(values[i].trim())));

				if (s.length() == 1) {
					s = "0" + s;
				}

				value += s;
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				value = null;
			}
		}
		return value;
	}

	/**
	 * Resize table column
	 *
	 * @param tableWidth    table width
	 * @param tblColumns    table columns
	 * @param count         count
	 * @param totalAssigned total assigned
	 * @return resized table columns
	 */
	public static int[] resizeTableColumn(int tableWidth, int[] tblColumns, int count, int totalAssigned) {
		int remainWidth = tableWidth - totalAssigned;
		int average = 0;
		if (remainWidth > 0) {
			if (count > 0) {
				average = remainWidth / count;
				for (int i = 0; i < tblColumns.length; i++) {
					if (tblColumns[i] == -1) {
						tblColumns[i] = average;
						remainWidth -= average;
					}
				}
				// For rounding issue there may still have Width remaining after distributed
				// averagely
				// which will cause column width summed up less than parent width and lead to an
				// extra column
				if (remainWidth > 0) {
					tblColumns[tblColumns.length - 1] += remainWidth;
				}
			} else if (tblColumns.length > 0) {
				average = remainWidth / tblColumns.length;
				for (int i = 0; i < tblColumns.length; i++) {
					tblColumns[i] += average;
				}
			}
		} else if (remainWidth <= 0 && count > 0) {
			for (int i = 0; i < tblColumns.length; i++) {
				if (tblColumns[i] == -1) {
					tblColumns[i] = 0;
				}
			}
		}
		return tblColumns;
	}
}
