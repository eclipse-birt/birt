/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.Location;

/**
 * The <code>Location</code> class defines a point specified in
 * <code>double</code> precision.
 */

public class Point {

	/**
	 * The X coordinate of this <code>Location</code>.
	 * 
	 */
	public double x;

	/**
	 * The Y coordinate of this <code>Location</code>.
	 * 
	 */
	public double y;

	/**
	 * Constructs and initializes a <code>Location</code> with coordinates
	 * (0,&nbsp;0).
	 * 
	 */
	public Point() {
	}

	public Point(Location lo) {
		this.x = lo.getX();
		this.y = lo.getY();
	}

	/**
	 * Constructs and initializes a <code>Location</code> with the specified
	 * coordinates.
	 * 
	 * @param x,&nbsp;y the coordinates to which to set the newly constructed
	 *                  <code>Location</code>
	 * 
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the X coordinate of this <code>Location</code> in <code>double</code>
	 * precision.
	 * 
	 * @return the X coordinate of this <code>Location</code>.
	 * 
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y coordinate of this <code>Location</code> in <code>double</code>
	 * precision.
	 * 
	 * @return the Y coordinate of this <code>Location</code>.
	 * 
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the location of this <code>Location</code> to the specified
	 * <code>double</code> coordinates.
	 * 
	 * @param x,&nbsp;y the coordinates to which to set this <code>Location</code>
	 * 
	 */
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns a <code>String</code> that represents the value of this
	 * <code>Location</code>.
	 * 
	 * @return a string representation of this <code>Location</code>.
	 * 
	 */
	public String toString() {
		return "Location[" + x + ", " + y + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void translate(double dTranslateX, double dTranslateY) {
		setX(getX() + dTranslateX);
		setY(getY() + dTranslateY);
	}

}
