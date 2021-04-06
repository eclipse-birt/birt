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
package org.eclipse.birt.report.engine.emitter.ods;

import org.eclipse.birt.report.engine.emitter.ods.layout.OdsContainer;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class ImageData extends SheetData {

	private String altText, imageUrl;
	private float width;
	private float imageHeight;

	public ImageData(String uri, String altText, int imageWidth, int imageHeight, StyleEntry styleId, int datatype,
			OdsContainer currentContainer) {
		super();
		this.dataType = datatype;
		this.styleId = styleId;
		height = imageHeight / 1000f;
		this.imageHeight = (int) height;
		width = Math.min(currentContainer.getSizeInfo().getWidth(), imageWidth);
		this.altText = altText;
		imageUrl = uri;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getImageHeight() {
		return imageHeight;
	}

	public float getImageWidth() {
		return width / 1000;
	}

	@Override
	public int getEndX() {
		return (int) (getStartX() + width);
	}

}
