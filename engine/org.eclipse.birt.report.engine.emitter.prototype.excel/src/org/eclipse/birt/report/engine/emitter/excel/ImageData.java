/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;

public class ImageData extends SheetData {

	private String altText, imageUrl;
	private byte[] imageData;
	private int width;
	private int imageHeight;

	public ImageData(IImageContent image, byte[] imageData, int imageWidth, int imageHeight, int styleId, int datatype,
			XlsContainer currentContainer) {
		super();
		this.dataType = datatype;
		this.styleId = styleId;
		height = imageHeight / 1000f;
		this.imageHeight = (int) height;
		width = Math.min(currentContainer.getSizeInfo().getWidth(), imageWidth);
		altText = image.getAltText();
		imageUrl = image.getURI();
		this.imageData = imageData;
		rowSpanInDesign = 0;
	}

	public String getDescription() {
		return altText;
	}

	public void setDescription(String description) {
		this.altText = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setUrl(String url) {
		this.imageUrl = url;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public int getImageWidth() {
		return width / 1000;
	}

	@Override
	public int getEndX() {
		return getStartX() + width;
	}

}
