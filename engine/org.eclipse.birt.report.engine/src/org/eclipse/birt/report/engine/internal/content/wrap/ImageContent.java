/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.ImageSize;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;

public class ImageContent extends AbstractContentWrapper implements IImageContent {
	IImageContent imageContent;

	public ImageContent(IImageContent content) {
		super(content);
		imageContent = content;
	}

	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitImage(this, value);
	}

	@Override
	public String getAltText() {
		return imageContent.getAltText();
	}

	@Override
	public String getAltTextKey() {
		return imageContent.getAltTextKey();
	}

	@Override
	public void setAltText(String altText) {
		imageContent.setAltText(altText);
	}

	@Override
	public void setAltTextKey(String key) {
		imageContent.setAltTextKey(key);
	}

	@Override
	public void setHelpKey(String key) {
		imageContent.setHelpText(key);
	}

	@Override
	public String getHelpKey() {
		return imageContent.getHelpKey();
	}

	@Override
	public byte[] getData() {
		return imageContent.getData();
	}

	@Override
	public void setData(byte[] data) {
		imageContent.setData(data);
	}

	@Override
	public String getExtension() {
		return imageContent.getExtension();
	}

	@Override
	public void setExtension(String extension) {
		imageContent.setExtension(extension);
	}

	@Override
	public String getURI() {
		return imageContent.getURI();
	}

	@Override
	public void setURI(String uri) {
		imageContent.setURI(uri);
	}

	@Override
	public int getImageSource() {
		return imageContent.getImageSource();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IImageContent#getImageMap()
	 */
	@Override
	public Object getImageMap() {
		return imageContent.getImageMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IImageContent#getMIMEType()
	 */
	@Override
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
	@Override
	public void setImageMap(Object map) {
		imageContent.setImageMap(map);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IImageContent#setImageSource(int)
	 */
	@Override
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
	@Override
	public void setMIMEType(String mimeType) {
		imageContent.setMIMEType(mimeType);
	}

	@Override
	public int getResolution() {
		return imageContent.getResolution();
	}

	@Override
	public void setResolution(int resolution) {
		imageContent.setResolution(resolution);
	}

	@Override
	public void setImageRawSize(ImageSize imageRawSize) {
		imageContent.setImageRawSize(imageRawSize);
	}

	@Override
	public ImageSize getImageRawSize() {
		return imageContent.getImageRawSize();
	}

	@Override
	public void setImageCalculatedSize(ImageSize imageCalcSize) {
		imageContent.setImageCalculatedSize(imageCalcSize);
	}

	@Override
	public ImageSize getImageCalculatedSize() {
		return imageContent.getImageCalculatedSize();
	}
}
