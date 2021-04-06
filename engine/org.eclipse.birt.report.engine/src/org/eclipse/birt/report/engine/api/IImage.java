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

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Defines an interface to communicate info about an image to image handler
 */
public interface IImage extends IReportPart {

	public final static int DESIGN_IMAGE = 0;
	public final static int REPORTDOC_IMAGE = 1;
	public final static int URL_IMAGE = 2;
	public final static int FILE_IMAGE = 3;
	public final static int CUSTOM_IMAGE = 4;
	public final static int INVALID_IMAGE = -1;

	/**
	 * returns an identifier for the image
	 * 
	 * @return an identifier for the image.
	 */
	public String getID();

	/**
	 * returns the source type of the image. Could be DESIGN_IMAGE, REPORTDOC_IMAGE,
	 * URL_IMAGE or CUSTOM_IMAGE
	 * 
	 * @return the type of the image
	 */
	public int getSource();

	/**
	 * returns binary image data. The function should be called with caution
	 * 
	 * @return the binary image data
	 */
	public byte[] getImageData() throws OutOfMemoryError;

	/**
	 * returns an input stream where the mage can be read
	 * 
	 * @return an input stream where the image can be read
	 */
	public InputStream getImageStream();

	/**
	 * write the image to a destination file
	 * 
	 * @param dest destination file to write the image to
	 */
	public void writeImage(File dest) throws IOException;

	/**
	 * return the image postfix, such as jpg, png
	 */
	public String getExtension();

	/**
	 * @return the mime type of the image
	 */
	public String getMimeType();

	/**
	 * The image map is used in HTML output.
	 * 
	 * @return the image map associate with this image.
	 */
	public String getImageMap();

	/**
	 * @return the size of the image
	 */
	public ImageSize getImageSize();
}
