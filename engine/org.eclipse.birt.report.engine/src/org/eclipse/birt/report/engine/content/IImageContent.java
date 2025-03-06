/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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

	/** property: image file key */
	int IMAGE_FILE = 0;

	/** property: image name key */
	int IMAGE_NAME = 1;

	/** property: image expression key */
	int IMAGE_EXPRESSION = 2;

	/** property: image URL key */
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

	/**
	 * Set the help text key
	 *
	 * @param key key of the help text
	 */
	void setHelpKey(String key);

	/**
	 * Get the help text key
	 *
	 * @return the help text key
	 */
	String getHelpKey();

	/**
	 * Get the data
	 *
	 * @return the data
	 */
	byte[] getData();

	/**
	 * Set the data
	 *
	 * @param data image data
	 */
	void setData(byte[] data);

	/**
	 * Get the image extension
	 *
	 * @return the image extension
	 */
	String getExtension();

	/**
	 * Set the image extension
	 *
	 * @param extension image extension
	 */
	void setExtension(String extension);

	/**
	 * Get the URI
	 *
	 * @return the URI
	 */
	String getURI();

	/**
	 * Set the URI
	 *
	 * @param uri image URI
	 */
	void setURI(String uri);

	/**
	 * Get the type of image source
	 *
	 * @return the type of image source
	 */
	int getImageSource();

	/**
	 * Set the image source
	 *
	 * @param source image source
	 */
	void setImageSource(int source);

	/**
	 * @return the image map (null means no image map)
	 */
	Object getImageMap();

	/**
	 * Set the image based on full mapping object
	 *
	 * @param map
	 */
	void setImageMap(Object map);

	/**
	 * Get the MIME type
	 *
	 * @return the MIME type
	 */
	String getMIMEType();

	/**
	 * Set the image MIME type
	 *
	 * @param mimeType MIME type of the image
	 */
	void setMIMEType(String mimeType);

	/**
	 * Get the image resolution
	 *
	 * @return the image resolution
	 */
	int getResolution();

	/**
	 * Set the image resolution
	 *
	 * @param resolution image resolution
	 */
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

	/**
	 * Fit the image to the wrapping container
	 *
	 * @return the image is fit to the wrapping container
	 * @since 4.19
	 */
	public default boolean isFitToContainer() {
		return false;
	}
}
