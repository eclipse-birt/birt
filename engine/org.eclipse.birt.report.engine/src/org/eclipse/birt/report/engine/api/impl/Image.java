/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.ImageSize;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.util.FileUtil;

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

	} /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.api2.IImage#getID()
		 */

	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IImage#getSource()
	 */
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageData()
	 */
	public byte[] getImageData() throws OutOfMemoryError {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageStream()
	 */
	public InputStream getImageStream() {
		switch (this.source) {
		case IImage.FILE_IMAGE:
			try {
				URL url = new URL(this.id);
				return new BufferedInputStream(url.openStream());
			} catch (MalformedURLException e) {
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
			} catch (MalformedURLException e) {
			} catch (IOException e1) {
			}
			return null;

		case IImage.INVALID_IMAGE:
			return null;

		default:
			return null;
		}
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getImageMap() {
		return imageMap;
	}

	public void setImageMap(String imageMap) {
		this.imageMap = imageMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IImage#writeImage(java.io.File)
	 */
	public void writeImage(File dest) throws IOException {
		if (source == IImage.INVALID_IMAGE) {
			logger.log(Level.SEVERE, "image source {0} is not valid!", id); //$NON-NLS-1$
			return;
		}

		InputStream input = getImageStream();
		if (null == input) {
			logger.log(Level.SEVERE, "image source {0} is not found!", id); //$NON-NLS-1$
			return;
		}
		// if(!dest.exists())
		// {

		String parent = new File(dest.getAbsolutePath()).getParent();
		File parentDir = new File(parent);
		if (!parentDir.exists())
			parentDir.mkdirs();
		OutputStream output = null;
		try {
			output = new BufferedOutputStream(new FileOutputStream(dest));
			copyStream(input, output);
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
		}
		// }

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
	public String getExtension() {
		return extension;
	}

	public void setImageSize(ImageSize size) {
		imageSize = size;
	}

	public ImageSize getImageSize() {
		return imageSize;
	}
}