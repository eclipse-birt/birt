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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.api.ImageSize;

/**
 * Image content in the report.
 *
 */
public interface IImageContent extends IContent {

	int IMAGE_FILE = 0;
	int IMAGE_NAME = 1;
	int IMAGE_EXPRESSION = 2;
	int IMAGE_URL = 3;
	/**
	 * @deprecated replaced by IMAGE_URL
	 */
	@Deprecated
	int IMAGE_URI = 3;

	/**
	 * @return Returns the altText.
	 */
	@Override
	String getAltText();

	@Override
	String getAltTextKey();

	@Override
	void setAltText(String altText);

	@Override
	void setAltTextKey(String key);

	void setHelpKey(String key);

	String getHelpKey();

	/**
	 * @return Returns the data.
	 */
	byte[] getData();

	void setData(byte[] data);

	/**
	 * @return Returns the extension.
	 */
	String getExtension();

	void setExtension(String extension);

	/**
	 * @return Returns the URI.
	 */
	String getURI();

	void setURI(String uri);

	/**
	 * Returns the type of image source
	 */
	int getImageSource();

	void setImageSource(int source);

	/**
	 * @return the image map (null means no image map)
	 */
	Object getImageMap();

	void setImageMap(Object map);

	/**
	 * get the MIMEType
	 */
	String getMIMEType();

	void setMIMEType(String mimeType);

	int getResolution();

	void setResolution(int resolution);

	/**
	 * Set the image raw size
	 *
	 * @param imageRawSize image raw size
	 */
	public void setImageRawSize(ImageSize imageRawSize);

	/**
	 * Get the image raw size
	 *
	 * @return Return the image raw size
	 */
	public ImageSize getImageRawSize();

	/**
	 * Set the calculated image size
	 *
	 * @param imageCalcSize calculated image size
	 */
	public void setImageCalculatedSize(ImageSize imageCalcSize);

	/**
	 * Get the calculated image size
	 *
	 * @return Return the calculated image size
	 */
	public ImageSize getImageCalculatedSize();
}
