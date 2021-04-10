/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.pkg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IImageContent;

/**
 * Image manager.
 *
 */
public class ImageManager {
	private static final String IMAGE_PREFIX = "Pictures/"; //$NON-NLS-1$
	private static final Map<String, String> MIME_TO_EXTENSION;
	private static final Map<String, String> EXTENSION_TO_MIME;

	private Package pkg;
	private int lastId;

	/**
	 * Map of original image URI to image entry.
	 */
	private Map<String, ImageEntry> images;

	public ImageManager(Package pkg) {
		this.pkg = pkg;
		images = new HashMap<String, ImageEntry>();
		lastId = 0;
	}

	public ImageEntry addImage(byte[] data, String extension) throws IOException {
		String fileName = generateFileName(extension, null);
		String contentType = null;
		if (extension != null) {
			contentType = EXTENSION_TO_MIME.get(extension.substring(1));
		} else {
			// There has to be a contentType (MIMEtype) as required field for manifest.xml
			contentType = EXTENSION_TO_MIME.get(fileName.substring(fileName.lastIndexOf('.') + 1));
		}
		ImageEntry entry = new ImageEntry(pkg, fileName, contentType, null, extension);
		processEntry(entry, data);
		return entry;
	}

	public ImageEntry addImage(String uri, String contentType, String imageExtension) throws IOException {
		ImageEntry entry = images.get(uri);
		if (entry != null) {
			return entry;
		}

		if (imageExtension == null) {
			imageExtension = "." + getImageExtension(uri); //$NON-NLS-1$
		}

		String fileName = generateFileName(imageExtension, contentType);
		entry = new ImageEntry(pkg, fileName, contentType, uri, imageExtension);
		if (processEntry(entry, entry.getImage().getData())) {
			images.put(uri, entry);
		}
		return entry;
	}

	public ImageEntry addImage(IImageContent image) throws IOException {
		ImageEntry entry = images.get(image.getURI());
		if (entry != null) {
			return entry;
		}

		String fileName = generateFileName(image.getExtension(), image.getMIMEType());
		entry = new ImageEntry(pkg, fileName, image.getMIMEType(), image);
		if (processEntry(entry, entry.getImage().getData())) {
			images.put(image.getURI(), entry);
		}
		return entry;
	}

	private boolean processEntry(ImageEntry entry, byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return false;
		}
		OutputStream out = null;
		try {
			out = entry.getOutputStream();
			out.write(data);
			pkg.addEntry(entry);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return false;
	}

	private String generateFileName(String imageExtension, String contentType) {
		if (imageExtension == null && contentType != null) {
			imageExtension = "." + MIME_TO_EXTENSION.get(contentType); //$NON-NLS-1$
		}

		if (imageExtension == null) {
			imageExtension = ".jpg"; //$NON-NLS-1$
		}

		String id = generateId() + imageExtension;
		return IMAGE_PREFIX + id;
	}

	private String generateId() {
		lastId++;
		return "Image" + lastId; //$NON-NLS-1$
	}

	public static String getImageExtension(String imageURI) {
		String rectifiedImageURI = imageURI.replace('.', '&');
		String extension = imageURI.substring(rectifiedImageURI.lastIndexOf('&') + 1).toLowerCase();

		if (extension.equals("svg")) {
			extension = "jpg";
		}
		return extension;
	}

	static {
		Map<String, String> mimeToExtension = new HashMap<String, String>();
		mimeToExtension.put("image/gif", "gif"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/x-png", "png"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/png", "png"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("application/jpeg", "jpg"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/jpeg", "jpeg"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/tiff", "tif"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/x-ms-bmp", "bmp"); //$NON-NLS-1$//$NON-NLS-2$
		mimeToExtension.put("image/svg+xml", "svg"); //$NON-NLS-1$//$NON-NLS-2$

		Map<String, String> extensionToMime = new HashMap<String, String>();
		for (Map.Entry<String, String> ext : mimeToExtension.entrySet()) {
			extensionToMime.put(ext.getValue(), ext.getKey());
		}

		MIME_TO_EXTENSION = Collections.unmodifiableMap(mimeToExtension);
		EXTENSION_TO_MIME = Collections.unmodifiableMap(extensionToMime);
	}

}
