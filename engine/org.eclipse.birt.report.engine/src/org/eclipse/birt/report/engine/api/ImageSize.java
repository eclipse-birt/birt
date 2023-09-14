/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

/**
 * Class of image size
 *
 * @since 3.3
 *
 */
public class ImageSize {

	protected String unit;
	protected float width;
	protected float height;

	/**
	 * Constructor
	 *
	 * @param u unit of image size
	 * @param w width of image
	 * @param h height of image
	 */
	public ImageSize(String u, float w, float h) {
		unit = u;
		width = w;
		height = h;
	}

	/**
	 * Get the image size unit
	 *
	 * @return Return the image size unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Get the image size width
	 *
	 * @return Return the image size width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Get the image size height
	 *
	 * @return Return the image size height
	 */
	public float getHeight() {
		return height;
	}
}
