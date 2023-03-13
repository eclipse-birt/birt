/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

public interface IImageInstance extends IReportItemInstance {

	/**
	 * Get the alt text
	 *
	 */
	String getAltText();

	/**
	 * Set the alt text
	 *
	 * @param altText
	 */
	void setAltText(String altText);

	/**
	 * Get the alt text
	 *
	 */
	String getAltTextKey();

	/**
	 * Set the alt text
	 *
	 * @param altText
	 */
	void setAltTextKey(String altTextKey);

	/**
	 * Get the image URI
	 *
	 */
	String getURI();

	/**
	 * @deprecated Set the image URI
	 *
	 */
	@Deprecated
	void setURI(String uri);

	/**
	 * Returns the type of image source Can be one of the following:
	 * org.eclipse.birt.report.engine.content.IImageContent.IMAGE_FILE
	 * org.eclipse.birt.report.engine.content.IImageContent.IMAGE_NAME
	 * org.eclipse.birt.report.engine.content.IImageContent.IMAGE_EXPRESSION
	 * org.eclipse.birt.report.engine.content.IImageContent.IMAGE_URL
	 */
	int getImageSource();

	/**
	 * Get the image name
	 */
	String getImageName();

	/**
	 * Set the image name
	 */
	void setImageName(String imageName);

	/**
	 * Returns the data for a named image
	 */
	byte[] getData();

	/**
	 * Set the data for a named image
	 */
	void setData(byte[] data);

	/**
	 * Get the MIME Type
	 */
	String getMimeType();

	/**
	 * Set the MIME Type
	 */
	void setMimeType(String type);

	/**
	 * Sets the image url. The source type is <code>IMAGE_REF_TYPE_URL</code>, and
	 * will automatically set in this method.
	 */
	void setURL(String url);

	/**
	 * get the image url, if the source type is not <code>IMAGE_REF_TYPE_URL</code>
	 * return null.
	 */
	String getURL();

	/**
	 * Sets the image file. The source type is <code>IMAGE_REF_TYPE_FILE</code>, and
	 * will automatically set in this method.
	 */
	void setFile(String file);

	/**
	 * get the image url, if the source type is not <code>IMAGE_REF_TYPE_FILE</code>
	 * return null.
	 */
	String getFile();

	/**
	 * Create a new action instance, witch can be bookmark, hyperlink or
	 * drillThrough. The default action instance type is NULL.
	 */
	IActionInstance createAction();

	/**
	 * Get the action instance.
	 */
	IActionInstance getAction();

	/**
	 * set the actionInstance
	 *
	 * @param actionInstance
	 */
	void setAction(IActionInstance actionInstance);

}
