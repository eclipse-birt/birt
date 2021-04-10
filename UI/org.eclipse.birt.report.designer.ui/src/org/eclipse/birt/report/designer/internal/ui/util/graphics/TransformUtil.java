/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Utility class for transform operation.
 * 
 */
public class TransformUtil {

	/**
	 * Returns the rectangle transformed from the given rectangle.
	 * 
	 * @param af
	 * @param rect
	 * @return
	 */
	public static Rectangle transformRect(AffineTransform af, Rectangle rect) {
		Rectangle rt = new Rectangle(0, 0, 0, 0);
		rect = redressRect(rect);
		Point p1 = new Point(rect.x, rect.y);
		p1 = transformPoint(af, p1);
		rt.x = p1.x;
		rt.y = p1.y;
		rt.width = (int) (rect.width * af.getScaleX());
		rt.height = (int) (rect.height * af.getScaleY());
		return rt;
	}

	/**
	 * Returns the rectangle which is the inverse transform of the given rectangle.
	 * 
	 * @param af
	 * @param rect
	 * @return
	 */
	public static Rectangle inverseTransformRect(AffineTransform af, Rectangle rect) {
		Rectangle rt = new Rectangle(0, 0, 0, 0);
		rect = redressRect(rect);
		Point p1 = new Point(rect.x, rect.y);
		p1 = inverseTransformPoint(af, p1);
		rt.x = p1.x;
		rt.y = p1.y;
		rt.width = (int) (rect.width / af.getScaleX());
		rt.height = (int) (rect.height / af.getScaleY());
		return rt;
	}

	/**
	 * Returns the point transformed from the given point.
	 * 
	 * @param af
	 * @param pt
	 * @return
	 */
	public static Point transformPoint(AffineTransform af, Point pt) {
		Point2D src = new Point2D.Float(pt.x, pt.y);
		Point2D dest = af.transform(src, null);
		Point point = new Point((int) Math.floor(dest.getX()), (int) Math.floor(dest.getY()));
		return point;
	}

	/**
	 * Returns the point which is the inverse transform of the given point.
	 * 
	 * @param af
	 * @param pt
	 * @return
	 */
	public static Point inverseTransformPoint(AffineTransform af, Point pt) {
		Point2D src = new Point2D.Float(pt.x, pt.y);

		try {
			Point2D dest = af.inverseTransform(src, null);
			return new Point((int) Math.floor(dest.getX()), (int) Math.floor(dest.getY()));
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}

	/**
	 * Redress the rectangle orientation, make it start with upper-left and positive
	 * width and height.
	 * 
	 * @param rect
	 * @return result
	 */
	public static Rectangle redressRect(Rectangle rect) {
		Rectangle rt = new Rectangle(0, 0, 0, 0);

		if (rect.width < 0) {
			rt.x = rect.x + rect.width + 1;
			rt.width = -rect.width;
		} else {
			rt.x = rect.x;
			rt.width = rect.width;
		}

		if (rect.height < 0) {
			rt.y = rect.y + rect.height + 1;
			rt.height = -rect.height;
		} else {
			rt.y = rect.y;
			rt.height = rect.height;
		}

		return rt;
	}
}