/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * The <code>Rectangle</code> class defines a rectangle specified in Rectangle
 * coordinates.
 */

public class Rectangle {

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The bitmask that indicates that a point lies to the left of this
	 * <code>Rectangle2D</code>.
	 */
	public static final int OUT_LEFT = 1;

	/**
	 * The bitmask that indicates that a point lies above this
	 * <code>Rectangle2D</code>.
	 */
	public static final int OUT_TOP = 2;

	/**
	 * The bitmask that indicates that a point lies to the right of this
	 * <code>Rectangle2D</code>.
	 *
	 */
	public static final int OUT_RIGHT = 4;

	/**
	 * The bitmask that indicates that a point lies below this
	 * <code>Rectangle2D</code>.
	 *
	 */
	public static final int OUT_BOTTOM = 8;

	/**
	 * The x coordinate of this <code>Rectangle</code>.
	 */
	public double x;

	/**
	 * The y coordinate of this <code>Rectangle</code>.
	 *
	 */
	public double y;

	/**
	 * The width of this <code>Rectangle</code>.
	 *
	 */
	public double width;

	/**
	 * The height of this <code>Rectangle</code>.
	 *
	 */
	public double height;

	/**
	 * Constructs a new <code>Rectangle</code>, initialized to location (0,&nbsp;0)
	 * and size (0,&nbsp;0).
	 *
	 */
	public Rectangle() {
	}

	public Rectangle(BoundingBox bb) {
		this.x = bb.getLeft();
		this.y = bb.getTop();
		this.height = bb.getHeight();
		this.width = bb.getWidth();
	}

	public Bounds getBounds() {
		return goFactory.createBounds(x, y, width, height);
	}

	/**
	 * Constructs and initializes a <code>Rectangle</code> from the specified
	 * Rectangle coordinates.
	 *
	 * @param x,&nbsp;y the coordinates of the upper left corner of the newly
	 *                  constructed <code>Rectangle</code>
	 * @param w         the width of the newly constructed <code>Rectangle</code>
	 * @param h         the height of the newly constructed <code>Rectangle</code>
	 *
	 */
	public Rectangle(double x, double y, double w, double h) {
		setRect(x, y, w, h);
	}

	/**
	 * Returns the X coordinate of this <code>Rectangle</code> in Rectangle
	 * precision.
	 *
	 * @return the X coordinate of this <code>Rectangle</code>.
	 *
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y coordinate of this <code>Rectangle</code> in Rectangle
	 * precision.
	 *
	 * @return the Y coordinate of this <code>Rectangle</code>.
	 *
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the width of this <code>Rectangle</code> in Rectangle precision.
	 *
	 * @return the width of this <code>Rectangle</code>.
	 *
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Returns the height of this <code>Rectangle</code> in Rectangle precision.
	 *
	 * @return the height of this <code>Rectangle</code>.
	 *
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Determines whether or not this <code>Rectangle</code> is empty.
	 *
	 * @return <code>true</code> if this <code>Rectangle</code> is empty;
	 *         <code>false</code> otherwise.
	 *
	 */
	public boolean isEmpty() {
		return (width <= 0.0) || (height <= 0.0);
	}

	/**
	 * Sets the location and size of this <code>Rectangle</code> to the specified
	 * Rectangle values.
	 *
	 * @param x,&nbsp;y the coordinates to which to set the upper left corner of
	 *                  this <code>Rectangle</code>
	 * @param w         the value to use to set the width of this
	 *                  <code>double</code>
	 * @param h         the value to use to set the height of this
	 *                  <code>double</code>
	 *
	 */
	public void setRect(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	/**
	 * Sets this <code>Rectangle</code> to be the same as the specified
	 * <code>Rectangle</code>.
	 *
	 * @param r the specified <code>Rectangle</code>
	 *
	 */
	public void setRect(Rectangle r) {
		this.x = r.getX();
		this.y = r.getY();
		this.width = r.getWidth();
		this.height = r.getHeight();
	}

	/**
	 * Determines where the specified Rectangle coordinates lie with respect to this
	 * <code>Rectangle</code>. This method computes a binary OR of the appropriate
	 * mask values indicating, for each side of this <code>Rectangle</code>, whether
	 * or not the specified coordinates are on the same side of the edge as the rest
	 * of this <code>Rectangle</code>.
	 *
	 * @param x,&nbsp;y the specified coordinates
	 * @return the logical OR of all appropriate out codes.
	 * @see Rectangle#OUT_LEFT
	 * @see Rectangle#OUT_TOP
	 * @see Rectangle#OUT_RIGHT
	 * @see Rectangle#OUT_BOTTOM
	 *
	 */
	public int outcode(double x, double y) {
		int out = 0;
		if (this.width <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < this.x) {
			out |= OUT_LEFT;
		} else if (x > this.x + this.width) {
			out |= OUT_RIGHT;
		}
		if (this.height <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < this.y) {
			out |= OUT_TOP;
		} else if (y > this.y + this.height) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	/**
	 * Returns the high precision bounding box of this <code>Rectangle</code>.
	 *
	 * @return the bounding box of this <code>Rectangle</code>.
	 *
	 */
	public Rectangle getBounds2D() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Returns a new <code>Rectangle</code> object representing the intersection of
	 * this <code>Rectangle</code> with the specified <code>Rectangle</code>.
	 *
	 * @param r the <code>Rectangle</code> to be intersected with this
	 *          <code>Rectangle</code>
	 * @return the largest <code>Rectangle</code> contained in both the specified
	 *         <code>Rectangle</code> and in this <code>Rectangle</code>.
	 *
	 */
	public Rectangle createIntersection(Rectangle r) {
		Rectangle dest = new Rectangle();
		Rectangle.intersect(this, r, dest);
		return dest;
	}

	private static void intersect(Rectangle rectangle, Rectangle r, Rectangle dest) {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns a new <code>Rectangle</code> object representing the union of this
	 * <code>Rectangle</code> with the specified <code>Rectangle</code>.
	 *
	 * @param r the <code>Rectangle</code> to be combined with this
	 *          <code>Rectangle</code>
	 * @return the smallest <code>Rectangle</code> containing both the specified
	 *         <code>Rectangle</code> and this <code>Rectangle</code>.
	 *
	 */
	public Rectangle createUnion(Rectangle r) {
		Rectangle dest = new Rectangle();
		Rectangle.union(this, r, dest);
		return dest;
	}

	private static void union(Rectangle rectangle, Rectangle r, Rectangle dest) {
		double right = Math.max(rectangle.x + rectangle.width, r.x + r.width);
		double bottom = Math.max(rectangle.y + rectangle.height, r.y + r.height);
		dest.x = Math.min(rectangle.x, r.x);
		dest.y = Math.min(rectangle.y, r.y);
		dest.width = right - dest.x;
		dest.height = bottom - dest.y;
	}

	public static Rectangle union(Rectangle rect1, Rectangle rect2) {
		Rectangle rect = null;
		if (rect1 != null || rect2 != null) {
			rect = new Rectangle();

			if (rect1 != null) {
				rect.setRect(rect1);
				rect.union(rect2);
			} else {
				rect.setRect(rect2);
			}

		}

		return rect;
	}

	public void union(Rectangle rect) {
		if (rect != null) {
			union(this, rect, this);
		}
	}

	/**
	 * Returns the <code>String</code> representation of this
	 * <code>Rectangle</code>.
	 *
	 * @return a <code>String</code> representing this <code>Rectangle</code>.
	 *
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + ",w=" + width //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ",h=" + height + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns the smallest X coordinate of the framing rectangle of the
	 * <code>Rectangle</code> in <code>double</code> precision.
	 *
	 * @return the smallest x coordinate of the framing rectangle of the
	 *         <code>Rectangle</code>.
	 */
	public double getMinX() {
		return getX();
	}

	/**
	 * Returns the smallest Y coordinate of the framing rectangle of the
	 * <code>Rectangle</code> in <code>double</code> precision.
	 *
	 * @return the smallest y coordinate of the framing rectangle of the
	 *         <code>Rectangle</code>.
	 */
	public double getMinY() {
		return getY();
	}

	/**
	 * Returns the largest X coordinate of the framing rectangle of the
	 * <code>Rectangle</code> in <code>double</code> precision.
	 *
	 * @return the largest x coordinate of the framing rectangle of the
	 *         <code>Rectangle</code>.
	 */
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * Returns the largest Y coordinate of the framing rectangle of the
	 * <code>Rectangle</code> in <code>double</code> precision.
	 *
	 * @return the largest y coordinate of the framing rectangle of the
	 *         <code>Rectangle</code>.
	 */
	public double getMaxY() {
		return getY() + getHeight();
	}

	public boolean contains(Point lo) {
		double w = getWidth();
		double h = getHeight();
		if (w < 0 || h < 0) {
			// At least one of the dimensions is negative...
			return false;
		}

		// Note: if either dimension is zero, tests below must return false...
		double x = getMinX();
		double y = getMinY();
		if (lo.getX() < x || lo.getY() < y) {
			return false;
		}

		w += x;
		h += y;
		// overflow || intersect
		return ((w < x || w > lo.getX()) && (h < y || h > lo.getY()));
	}

}
