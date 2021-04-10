/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;

public class ImageContent extends AbstractContentWrapper implements IImageContent {
	IImageContent imageContent;

	public ImageContent(IImageContent content) {
		super(content);
		imageContent = content;
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitImage(this, value);
	}

	public String getAltText() {
		return imageContent.getAltText();
	}

	public String getAltTextKey() {
		return imageContent.getAltTextKey();
	}

	public void setAltText(String altText) {
		imageContent.setAltText(altText);
	}

	public void setAltTextKey(String key) {
		imageContent.setAltTextKey(key);
	}

	public void setHelpKey(String key) {
		imageContent.setHelpText(key);
	}

	public String getHelpKey() {
		return imageContent.getHelpKey();
	}

	public byte[] getData() {
		return imageContent.getData();
	}

	public void setData(byte[] data) {
		imageContent.setData(data);
	}

	public String getExtension() {
		return imageContent.getExtension();
	}

	public void setExtension(String extension) {
		imageContent.setExtension(extension);
	}

	public String getURI() {
		return imageContent.getURI();
	}

	public void setURI(String uri) {
		imageContent.setURI(uri);
	}

	public int getImageSource() {
		return imageContent.getImageSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IImageContent#getImageMap()
	 */
	public Object getImageMap() {
		return imageContent.getImageMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IImageContent#getMIMEType()
	 */
	public String getMIMEType() {
		return imageContent.getMIMEType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IImageContent#setImageMap(java.lang.
	 * Object)
	 */
	public void setImageMap(Object map) {
		imageContent.setImageMap(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IImageContent#setImageSource(int)
	 */
	public void setImageSource(int source) {
		imageContent.setImageSource(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IImageContent#setMIMEType(java.lang.
	 * String)
	 */
	public void setMIMEType(String mimeType) {
		imageContent.setMIMEType(mimeType);
	}

	public int getResolution() {
		return imageContent.getResolution();
	}

	public void setResolution(int resolution) {
		imageContent.setResolution(resolution);
	}

}
