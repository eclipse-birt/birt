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

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.emitter.Image;

/**
 * Image entry inside a package.
 */
public class ImageEntry extends PackageEntry {
	private IImageContent imageContent;
	private Image imageInfo;
	private String originalUri;
	private String imageExtension;
	private int imageSource;

	ImageEntry(Package pkg, String uri, String contentType, String originalUri, String extension) {
		super(pkg, uri, contentType, true);
		this.imageContent = null;
		this.imageInfo = null;
		this.originalUri = originalUri;
		this.imageExtension = extension;
		this.imageSource = IImageContent.IMAGE_URL;

	}

	ImageEntry(Package pkg, String uri, String contentType, IImageContent imageContent) {
		super(pkg, uri, contentType, true);
		this.imageContent = imageContent;
		this.imageInfo = null;
		this.originalUri = imageContent.getURI();
		this.imageExtension = imageContent.getExtension();
		this.imageSource = imageContent.getImageSource();
	}

	public Image getImage() throws IOException {
		if (imageInfo == null) {
			imageInfo = EmitterUtil.parseImage(imageContent, imageSource, originalUri, contentType, imageExtension);
		}
		return imageInfo;
	}

}
