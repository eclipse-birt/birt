/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.eclipse.birt.core.internal.util.ImageConversionUtil;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.ImageSize;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.engine.util.SvgFile;

/**
 * Defines an image object that provides services for passing or writing image
 * content out.
 */
public class Image extends ReportPart implements IImage {

	protected static Logger logger = Logger.getLogger(Image.class.getName());

	/**
	 * image ID
	 */
	protected String id = null;

	/**
	 * postfix of image
	 */
	protected String extension = null;

	/**
	 * image source
	 */
	protected int source = IImage.INVALID_IMAGE;

	/**
	 * Comment for <code>data</code>
	 */
	protected byte[] data = null;

	protected String mimeType;

	protected String imageMap;

	protected ImageSize imageSize;

	protected ImageSize imageRawSize;

	/**
	 * Constructor with an image uri
	 *
	 * @param uri
	 */
	public Image(String uri) {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("null or empty uri");
		}

		this.id = uri;
		if (!FileUtil.isLocalResource(uri)) {
			this.source = IImage.URL_IMAGE;
		} else {
			this.source = IImage.FILE_IMAGE;
		}
		this.extension = FileUtil.getExtFromFileName(uri);
	}

	/**
	 *
	 * @param data
	 * @param name
	 */
	public Image(byte[] data, String name) {
		if (data == null) {
			return;
		}

		id = name;
		this.data = data;
		this.source = IImage.CUSTOM_IMAGE;
	}

	/**
	 *
	 * @param data
	 * @param name
	 * @param postfix
	 */
	public Image(byte[] data, String name, String postfix) {
		if (data == null) {
			return;
		}

		id = name;
		this.data = data;
		this.source = IImage.CUSTOM_IMAGE;
		extension = postfix;
		mimeType = FileUtil.getTypeFromExt(extension);
	}

	/**
	 * Constructor
	 *
	 * @param content content environment of the image
	 */
	public Image(IImageContent content) {
		String imgUri = content.getURI();
		byte[] imgData = content.getData();
		this.imageMap = (String) content.getImageMap();
		this.mimeType = content.getMIMEType();
		this.extension = content.getExtension();
		if (extension == null) {
			if (mimeType != null) {
				extension = FileUtil.getExtFromType(mimeType);
			}
		}
		if (mimeType == null) {
			if (extension != null) {
				mimeType = FileUtil.getTypeFromExt(extension);
			}
		}
		switch (content.getImageSource()) {
		case IImageContent.IMAGE_FILE:
			if (imgUri != null) {
				this.id = imgUri;
				this.source = IImage.FILE_IMAGE;
			}
			break;
		case IImageContent.IMAGE_NAME:
			if (imgData != null) {
				this.data = imgData;
				this.source = IImage.DESIGN_IMAGE;
				this.id = imgUri;
			}
			break;
		case IImageContent.IMAGE_EXPRESSION:
			if (imgData != null) {
				this.data = imgData;
				this.source = IImage.CUSTOM_IMAGE;
				this.id = content.getInstanceID().toUniqueString();
			}
			break;
		case IImageContent.IMAGE_URL:
			if (imgUri != null) {
				this.id = imgUri;
				this.source = IImage.URL_IMAGE;
			}
			break;
		default:
			assert (false);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IImage#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IImage#getSource()
	 */
	@Override
	public int getSource() {
		return source;
	}

	/**
	 * Set the image source
	 *
	 * @param source image source
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageData()
	 */
	@Override
	public byte[] getImageData() throws OutOfMemoryError {
		return data;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageStream()
	 */
	@Override
	public InputStream getImageStream() {
		switch (this.source) {
		case IImage.FILE_IMAGE:
			try {
				URL url = new URL(this.id);
				return new BufferedInputStream(url.openStream());
			} catch (IOException e1) {
			}

			try {
				return new BufferedInputStream(new FileInputStream(new File(this.id)));
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			return null;

		case IImage.CUSTOM_IMAGE:
			return new ByteArrayInputStream(this.data);

		case IImage.DESIGN_IMAGE:
			return new ByteArrayInputStream(this.data);

		case IImage.URL_IMAGE:
			try {
				URL url = new URL(this.id);
				return new BufferedInputStream(url.openStream());
			} catch (IOException e1) {
			}
			return null;

		case IImage.INVALID_IMAGE:
			return null;

		default:
			return null;
		}
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Set mime type
	 *
	 * @param mimeType mime type of the image
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String getImageMap() {
		return imageMap;
	}

	/**
	 * Set the image mape
	 *
	 * @param imageMap image map
	 */
	public void setImageMap(String imageMap) {
		this.imageMap = imageMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IImage#writeImage(java.io.File)
	 */
	@Override
	public void writeImage(File dest) {
		if (source == IImage.INVALID_IMAGE) {
			logger.log(Level.SEVERE, "image source {0} is not valid!", id); //$NON-NLS-1$
			return;
		}

		InputStream input = getImageStream();
		if (null == input) {
			logger.log(Level.SEVERE, "image source {0} is not found!", id); //$NON-NLS-1$
			return;
		}

		String parent = new File(dest.getAbsolutePath()).getParent();
		File parentDir = new File(parent);
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		OutputStream output = null;
		boolean copied = false;
		try {
			output = new BufferedOutputStream(new FileOutputStream(dest));
			copyStream(input, output);
			copied = true;
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			// a failed copy has been logged already
			this.imageRawSize = copied ? readRawSize(dest) : new ImageSize("px", 0, 0); //$NON-NLS-1$
		}
	}

	/**
	 * Reads the pixel size of the written image. ImageIO cannot read every format,
	 * such as WebP, so those get their size from {@link ImageConversionUtil}.
	 *
	 * @param file the image file
	 * @return the size in pixels, or 0x0 if the image cannot be read
	 */
	private ImageSize readRawSize(File file) {
		try {
			BufferedImage bImg = ImageIO.read(file);
			if (bImg != null) {
				return new ImageSize("px", bImg.getWidth(), bImg.getHeight()); //$NON-NLS-1$
			}
			byte[] imageData = Files.readAllBytes(file.toPath());
			int[] size = ImageConversionUtil.getSize(imageData);
			if (size != null) {
				return new ImageSize("px", size[0], size[1]); //$NON-NLS-1$
			}
			if (isReadableType(imageData)) {
				logger.log(Level.SEVERE, "image {0} has data in an unknown format", id); //$NON-NLS-1$
			}
		} catch (IOException | RuntimeException ex) {
			logger.log(Level.SEVERE, "image " + id + " cannot be read", ex); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return new ImageSize("px", 0, 0); //$NON-NLS-1$
	}

	/**
	 * Checks whether ImageIO should be able to read the image. Types that it has no
	 * reader for, such as SVG or ICO, are passed to the browser as they are, so
	 * their size is unknown without that being an error.
	 */
	private boolean isReadableType(byte[] imageData) {
		if (mimeType != null) {
			return ImageIO.getImageReadersByMIMEType(mimeType.toLowerCase(Locale.ROOT)).hasNext();
		}
		if (extension != null) {
			String suffix = extension.startsWith(".") ? extension.substring(1) : extension; //$NON-NLS-1$
			return ImageIO.getImageReadersBySuffix(suffix.toLowerCase(Locale.ROOT)).hasNext();
		}
		return !SvgFile.isSvg(null, id, null, imageData);
	}

	/**
	 * Copies the stream from the source to the target
	 *
	 * @param src the source stream
	 * @param tgt the target stream
	 * @throws IOException
	 */
	protected void copyStream(InputStream src, OutputStream tgt) throws IOException {
		// copy the file content
		byte[] buffer = new byte[1024];
		int size = 0;
		do {
			size = src.read(buffer);
			if (size > 0) {
				tgt.write(buffer, 0, size);
			}
		} while (size > 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IImage#getExtension()
	 */
	@Override
	public String getExtension() {
		return extension;
	}

	/**
	 * Set image size
	 *
	 * @param size image size
	 */
	public void setImageSize(ImageSize size) {
		imageSize = size;
	}

	@Override
	public ImageSize getImageSize() {
		return imageSize;
	}

	/**
	 * Set the raw size of the image
	 *
	 * @param rawSize The raw image size
	 */
	public void setImageRawSize(ImageSize rawSize) {
		this.imageRawSize = rawSize;
	}

	/**
	 * Get the raw size of the image
	 *
	 * @return Return the raw size of the image
	 */
	public ImageSize getImageRawSize() {
		return this.imageRawSize;
	}
}
