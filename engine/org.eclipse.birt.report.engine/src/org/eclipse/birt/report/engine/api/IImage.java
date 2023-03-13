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

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Defines an interface to communicate info about an image to image handler
 */
public interface IImage extends IReportPart {

	int DESIGN_IMAGE = 0;
	int REPORTDOC_IMAGE = 1;
	int URL_IMAGE = 2;
	int FILE_IMAGE = 3;
	int CUSTOM_IMAGE = 4;
	int INVALID_IMAGE = -1;

	/**
	 * returns an identifier for the image
	 *
	 * @return an identifier for the image.
	 */
	String getID();

	/**
	 * returns the source type of the image. Could be DESIGN_IMAGE, REPORTDOC_IMAGE,
	 * URL_IMAGE or CUSTOM_IMAGE
	 *
	 * @return the type of the image
	 */
	int getSource();

	/**
	 * returns binary image data. The function should be called with caution
	 *
	 * @return the binary image data
	 */
	byte[] getImageData() throws OutOfMemoryError;

	/**
	 * returns an input stream where the mage can be read
	 *
	 * @return an input stream where the image can be read
	 */
	InputStream getImageStream();

	/**
	 * write the image to a destination file
	 *
	 * @param dest destination file to write the image to
	 */
	void writeImage(File dest) throws IOException;

	/**
	 * return the image postfix, such as jpg, png
	 */
	String getExtension();

	/**
	 * @return the mime type of the image
	 */
	String getMimeType();

	/**
	 * The image map is used in HTML output.
	 *
	 * @return the image map associate with this image.
	 */
	String getImageMap();

	/**
	 * @return the size of the image
	 */
	ImageSize getImageSize();
}
