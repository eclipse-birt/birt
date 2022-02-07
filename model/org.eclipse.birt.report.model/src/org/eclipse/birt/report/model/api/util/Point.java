/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.util;

/**
 * Represents a point in application units. Can also represent a rectangle size.
 * In this case, the point represents the position of the bottom right corner
 * relative to the top left corner.
 *
 */

public class Point {
	/**
	 * The x position or width.
	 */

	public double x;

	/**
	 * The y position or height.
	 */

	public double y;

	/**
	 * Default constructor.
	 */

	public Point() {
		x = 0;
		y = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @param x the x position or width
	 * @param y the y position or width
	 */

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns whether the point is empty.
	 * 
	 * @return true if the point is (0,0) or the size is empty.
	 */

	public boolean isEmpty() {
		return x == 0 && y == 0;
	}
}
