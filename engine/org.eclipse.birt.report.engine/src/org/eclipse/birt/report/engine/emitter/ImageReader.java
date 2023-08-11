/*******************************************************************************
 * Copyright (c)2010 Actuate Corporation.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.internal.util.DataProtocolUtil;
import org.eclipse.birt.report.engine.internal.util.DataProtocolUtil.DataUrlInfo;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class ImageReader {

	public static final int TYPE_IMAGE_OBJECT = 0;
	public static final int TYPE_FLASH_OBJECT = 1;
	public static final int TYPE_SVG_OBJECT = 2;
	public static final int TYPE_CONVERTED_SVG_OBJECT = 3;

	public static final int OBJECT_UNLOADED = -1;
	public static final int RESOURCE_UNREACHABLE = 0;
	public static final int UNSUPPORTED_OBJECTS = 1;
	public static final int OBJECT_LOADED_SUCCESSFULLY = 2;

	private int objectType = TYPE_IMAGE_OBJECT;
	private int status = OBJECT_UNLOADED;

	private IImageContent content;
	private String supportedImageFormats = null;
	private byte[] buffer;
	private ResourceLocatorWrapper rl = null;

	protected static Logger logger = Logger.getLogger(ImageReader.class.getName());

	private static final String URL_PROTOCOL_TYPE_DATA = "data:";
	private static final String URL_PROTOCOL_TYPE_FILE = "file:";

	public ImageReader(IImageContent content, String supportedImageFormats) {
		this.content = content;
		this.supportedImageFormats = supportedImageFormats;
		ExecutionContext exeContext = ((ReportContent) content.getReportContent()).getExecutionContext();
		if (exeContext != null) {
			this.rl = exeContext.getResourceLocator();
		}
	}

	public int read() {
		buffer = null;
		checkObjectType(content);
		String uri = this.verifyURI(content.getURI());
		try {
			switch (content.getImageSource()) {
			case IImageContent.IMAGE_FILE:

				ReportDesignHandle design = content.getReportContent().getDesign().getReportDesign();
				if (rl == null) {
					URL url = design.findResource(uri, IResourceLocator.IMAGE,
							content.getReportContent().getReportContext() == null ? null
									: content.getReportContent().getReportContext().getAppContext());
					readImage(url);
				} else {
					byte[] in = rl.findResource(design, uri, IResourceLocator.IMAGE,
							content.getReportContent().getReportContext() == null ? null
									: content.getReportContent().getReportContext().getAppContext());
					readImage(in);
				}
				break;
			case IImageContent.IMAGE_URL:
				readImage(uri);
				break;
			case IImageContent.IMAGE_NAME:
			case IImageContent.IMAGE_EXPRESSION:
				readImage(content.getData());
				break;
			default:
				assert (false);
			}
		} catch (IOException e) {
			buffer = null;
			status = RESOURCE_UNREACHABLE;
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return status;
	}

	public byte[] getByteArray() {
		return buffer;
	}

	public int getType() {
		return objectType;
	}

	private byte[] getImageByteArray(InputStream in) throws IOException {
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

	private void checkObjectType(IImageContent content) {
		String uri = content.getURI();
		String mimeType = content.getMIMEType();
		String extension = content.getExtension();
		if (FlashFile.isFlash(mimeType, uri, extension)) {
			objectType = TYPE_FLASH_OBJECT;
		} else if (SvgFile.isSvg(mimeType, uri, extension)) {
			objectType = TYPE_SVG_OBJECT;
		} else {
			objectType = TYPE_IMAGE_OBJECT;
		}
	}

	/**
	 * Check if the target output emitter supports the object type.
	 */
	private boolean isOutputSupported() {
		if (objectType == TYPE_IMAGE_OBJECT) {
			if (-1 != supportedImageFormats.indexOf("PNG") || -1 != supportedImageFormats.indexOf("GIF")
					|| -1 != supportedImageFormats.indexOf("BMP") || -1 != supportedImageFormats.indexOf("JPG")) {
				return true;
			}
		} else if (objectType == TYPE_FLASH_OBJECT) {
			if (-1 != supportedImageFormats.indexOf("SWF")) {
				return true;
			}
		} else if (objectType == TYPE_SVG_OBJECT) {
			if (-1 != supportedImageFormats.indexOf("SVG")) {
				return true;
			}
		}
		return false;
	}

	private void readImage(String uri) throws IOException {
		if (uri == null) {
			status = RESOURCE_UNREACHABLE;
			return;
		}

		/*
		 * If the image is using the data protocol, decode the data and read bytes
		 * instead
		 */
		if (uri.startsWith(DataProtocolUtil.DATA_PROTOCOL)) {
			try {
				DataUrlInfo parseDataUrl = DataProtocolUtil.parseDataUrl(uri);

				byte[] bytes = null;
				if (Objects.equals(parseDataUrl.getEncoding(), "base64")) { //$NON-NLS-1$
					bytes = Base64.getDecoder().decode(parseDataUrl.getData());
				} else {
					/* The case of no encoding, the data is a string on the URL */
					bytes = parseDataUrl.getData().getBytes(StandardCharsets.UTF_8); /* Charset of the SVG file */
				}
				if (this.objectType == TYPE_SVG_OBJECT) {
					String decodedImg = java.net.URLDecoder.decode(new String(bytes), StandardCharsets.UTF_8);
					bytes = decodedImg.getBytes(StandardCharsets.UTF_8);
				}

				if (bytes != null) {
					readImage(bytes);
				} else {
					status = RESOURCE_UNREACHABLE;
				}
			} catch (Exception e) {
				status = RESOURCE_UNREACHABLE;
			}
		} else {
			readImage(new URL(uri));
		}
	}

	private void readImage(URL url) throws IOException {
		if (url == null) {
			status = RESOURCE_UNREACHABLE;
			return;
		}
		if (rl != null) {
			byte[] in = rl.findResource(url);
			readImage(in);
		} else {
			InputStream in = url.openStream();
			readImage(in);
		}
	}

	private void readImage(InputStream in) throws IOException {
		if (isOutputSupported()) {
			buffer = getImageByteArray(in);
			status = OBJECT_LOADED_SUCCESSFULLY;
		} else if (objectType == TYPE_SVG_OBJECT) {
			try {
				buffer = SvgFile.transSvgToArray(in);
			} catch (Exception e) {
				buffer = null;
				status = UNSUPPORTED_OBJECTS;
				return;
			}
			objectType = TYPE_CONVERTED_SVG_OBJECT;
			status = OBJECT_LOADED_SUCCESSFULLY;
		} else {
			buffer = null;
			status = UNSUPPORTED_OBJECTS;
		}
	}

	private void readImage(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			buffer = null;
			status = RESOURCE_UNREACHABLE;
			return;
		}
		if (isOutputSupported()) {
			buffer = data;
			status = OBJECT_LOADED_SUCCESSFULLY;
		} else if (objectType == TYPE_SVG_OBJECT) {
			try (InputStream in = new ByteArrayInputStream(data)) {
				buffer = SvgFile.transSvgToArray(in);
			} catch (Exception e) {
				buffer = null;
				status = UNSUPPORTED_OBJECTS;
				return;
			}
			objectType = TYPE_CONVERTED_SVG_OBJECT;
			status = OBJECT_LOADED_SUCCESSFULLY;
		} else {
			buffer = null;
			status = UNSUPPORTED_OBJECTS;
		}
	}

	/**
	 * Check the URL to be valid and fall back try it like file-URL
	 */
	private String verifyURI(String uri) {
		if (uri != null && !uri.toLowerCase().startsWith(URL_PROTOCOL_TYPE_DATA)) {
			try {
				new URL(uri).toURI();
			} catch (MalformedURLException | URISyntaxException excUrl) {
				// invalid URI try it like "file:"
				try {
					String tmpUrl = URL_PROTOCOL_TYPE_FILE + "///" + uri;
					new URL(tmpUrl).toURI();
					uri = tmpUrl;
				} catch (MalformedURLException | URISyntaxException excFile) {
				}
			}
		}
		return uri;
	}
}
