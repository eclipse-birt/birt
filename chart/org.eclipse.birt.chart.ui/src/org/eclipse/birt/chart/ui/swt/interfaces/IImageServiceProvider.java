/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
	List<String> getEmbeddedImageName();

	/**
	 * save image to report
	 *
	 * @param fullPath
	 * @param fileName
	 * @return relative file name
	 * @throws ChartException
	 */
	String saveImage(String fullPath, String fileName) throws ChartException;

	/**
	 * get embedded image by file name
	 *
	 * @param fileName
	 * @return org.eclipse.swt.graphics.Image
	 */
	org.eclipse.swt.graphics.Image getEmbeddedImage(String fileName);

	/**
	 * get resource image by file name
	 *
	 * @param fileName
	 * @return org.eclipse.swt.graphics.Image
	 * @throws ChartException
	 */
	org.eclipse.swt.graphics.Image loadImage(String fileName) throws ChartException;

	/**
	 * get design time image absolute URL
	 *
	 * @param image
	 * @return image URL
	 */
	String getImageAbsoluteURL(Image image);
}
