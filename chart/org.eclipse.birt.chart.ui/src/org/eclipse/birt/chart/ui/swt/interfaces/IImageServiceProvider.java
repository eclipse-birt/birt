/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Image;

/**
 * IImageServiceProvider
 */

public interface IImageServiceProvider {

	/**
	 * get All available embedded image names in report
	 * 
	 * @return list of image names
	 */
	public List<String> getEmbeddedImageName();

	/**
	 * save image to report
	 * 
	 * @param fullPath
	 * @param fileName
	 * @return relative file name
	 * @throws ChartException
	 */
	public String saveImage(String fullPath, String fileName) throws ChartException;

	/**
	 * get embedded image by file name
	 * 
	 * @param fileName
	 * @return org.eclipse.swt.graphics.Image
	 */
	public org.eclipse.swt.graphics.Image getEmbeddedImage(String fileName);

	/**
	 * get resource image by file name
	 * 
	 * @param fileName
	 * @return org.eclipse.swt.graphics.Image
	 * @throws ChartException
	 */
	public org.eclipse.swt.graphics.Image loadImage(String fileName) throws ChartException;

	/**
	 * get design time image absolute URL
	 * 
	 * @param image
	 * @return image URL
	 */
	public String getImageAbsoluteURL(Image image);
}
