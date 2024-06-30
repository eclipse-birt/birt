/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ooxml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;

public class ImageManager {
	private static ImageManager instance;

	private Map<String, ImagePart> images;

	private int imageCount = 0;

	public static ImageManager getInstance() {
		if (instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}

	public ImageManager() {
		this.images = new HashMap<>();
	}

	public boolean hasImage(String imageId) {
		return images.containsKey(imageId);
	}

	public ImagePart getImagePart(IPart parent, String imageId, Image imageInfo) throws IOException {
		if (imageId == null) {
			imageId = Integer.toString(imageCount++);
		}
		ImagePart imagePart = images.get(imageId);
		if (imagePart == null) {
			String format = imageInfo.getFormatName();
			String uri = "/media/" + "image" + (images.size() + 1) + "." + format;
			MimeType mimeType = MimeType.valueOf(format.toUpperCase());
			IPart part = parent.getPart(uri, mimeType, RelationshipTypes.IMAGE);
			OutputStream partOut = part.getOutputStream();
			partOut.write(imageInfo.getData());
			imagePart = new ImagePart(imageInfo, part);
			images.put(imageId, imagePart);
			partOut.close();
			return imagePart;
		} else {
			IPart part = imagePart.getPart();
			String parentUri = parent.getAbsoluteUri();
			String childUri = part.getAbsoluteUri();
			String relativeUri = OOXmlUtil.getRelativeUri(parentUri, childUri);
			IPart existPart = parent.getPart(relativeUri);
			if (existPart != null) {
				if (imagePart.getPart() == existPart) {
					return imagePart;
				}
				return new ImagePart(imagePart, existPart);
			}
			return new ImagePart(imagePart, parent.createPartReference(part));
		}
	}

	public ImagePart getImagePart(IPart parent, String imageId, byte imageData[]) throws IOException {
		Image imageInfo = OOXmlUtil.getImageInfo(imageData);
		return getImagePart(parent, imageId, imageInfo);
	}

	public static class ImagePart {

		private Image imageInfo;
		private IPart part;

		public ImagePart(Image imageInfo, IPart part) {
			this.imageInfo = imageInfo;
			this.part = part;
		}

		public ImagePart(ImagePart imagePart, IPart part) {
			this.imageInfo = imagePart.getImageInfo();
			this.part = part;
		}

		public Image getImageInfo() {
			return imageInfo;
		}

		public IPart getPart() {
			return part;
		}
	}

	/**
	 * @param uri
	 * @return
	 */
	public ImagePart getImagePart(String uri) {
		return images.get(uri);
	}

}
