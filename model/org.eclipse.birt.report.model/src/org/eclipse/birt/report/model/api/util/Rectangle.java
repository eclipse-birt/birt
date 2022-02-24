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
 * Represents the position and size of a rectangle in application units.
 *
 */

public class Rectangle {
	/**
	 * The left edge.
	 */

	public double x;

	/**
	 * The top edge.
	 */

	public double y;

	/**
	 * The width of the rectangle.
	 */

	public double width;

	/**
	 * The height of the rectangle.
	 */

	public double height;

	/**
	 * Default constructor.
	 */

	public Rectangle() {
		x = 0;
		y = 0;
		height = 0;
		width = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @param x      left edge
	 * @param y      top edge
	 * @param height rectangle height
	 * @param width  rectangle width
	 */

	public Rectangle(double x, double y, double height, double width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	/**
	 * Constructor.
	 * 
	 * @param posn position of top left corner
	 * @param size size of the rectangle
	 */

	public Rectangle(Point posn, Point size) {
		x = posn.x;
		y = posn.y;
		width = size.x;
		height = size.y;
	}

	/**
	 * Returns the position of the top left corner.
	 * 
	 * @return position of the top left corner
	 */

	public Point getPosition() {
		return new Point(x, y);
	}

	/**
	 * Returns the rectangle size.
	 * 
	 * @return rectangle size
	 */

	public Point getSize() {
		return new Point(width, height);
	}
}
