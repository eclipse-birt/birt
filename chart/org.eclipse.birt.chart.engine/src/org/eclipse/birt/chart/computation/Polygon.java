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

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.util.ChartUtil;

/**
 * A 2D Polygon class
 */
public class Polygon {

	private List<Point> points;

	public Polygon() {
		points = new ArrayList<Point>();
	}

	public void add(double x, double y) {
		add(new Point(x, y));
	}

	public void add(Point p) {
		points.add(p);
	}

	public Point getPoint(int index) {
		return points.get(index);
	}

	public List<Point> getPoints() {
		return points;
	}

	public Rectangle getBounds() {
		double xmin = Double.MAX_VALUE;
		double ymin = Double.MAX_VALUE;
		double xmax = -Double.MAX_VALUE;
		double ymax = -Double.MAX_VALUE;

		for (int i = 0; i < points.size(); i++) {
			double x = getPoint(i).getX();
			double y = getPoint(i).getY();
			if (x < xmin) {
				xmin = x;
			}
			if (x > xmax) {
				xmax = x;
			}
			if (y < ymin) {
				ymin = y;
			}
			if (y > ymax) {
				ymax = y;
			}
		}

		return new Rectangle(xmin, ymin, (xmax - xmin), (ymax - ymin));
	}

	/**
	 * Tests the point is inside or outside of the polygon. This method doesn't
	 * contain the case if the point being queries lies exactly on a vertex.
	 * 
	 * @param count the length of the polygon vertex
	 * @param xa    X coordinates of the polygon
	 * @param ya    Y coordinates of the polygon
	 * @param x     X coordinates of the point
	 * @param y     Y coordinates of the point
	 * @return true: inside; false: outside
	 */
	private boolean testInside(int count, double[] xa, double[] ya, double x, double y) {
		boolean c = false;
		for (int i = 0, j = count - 1; i < count; j = i++) {
			if ((((ya[i] <= y) && (y < ya[j])) || ((ya[j] <= y) && (y < ya[i])))
					&& (x < (xa[j] - xa[i]) * (y - ya[i]) / (ya[j] - ya[i]) + xa[i])) {
				c = !c;
			}
		}
		return c;
	}

	/**
	 * Tests if the point is in the lines of the polygon
	 * 
	 * @param p the point
	 */
	private boolean testWithinLine(Point p) {
		boolean c = false;
		for (int i = 0, j = points.size() - 1; !c && i < points.size(); j = i++) {
			c = testWithinLine(points.get(i), points.get(j), p);
		}
		return c;
	}

	private boolean testWithinLine(Point pStart, Point pEnd, Point p) {
		// Check if the point is in the rectangle of the line
		boolean c = (p.x >= pStart.x && p.x <= pEnd.x || p.x <= pStart.x && p.x >= pEnd.x)
				&& (p.y >= pStart.y && p.y <= pEnd.y || p.y <= pStart.y && p.y >= pEnd.y);
		if (c) {
			// Check if the point is in the vertical line
			if (pEnd.x == pStart.x && pEnd.x == p.x) {
				return true;
			} else if ((pEnd.x - pStart.x) * (pEnd.x - p.x) == 0) {
				return false;
			}
			// Check the slope
			c = ChartUtil.mathEqual((pEnd.y - pStart.y) / (pEnd.x - pStart.x), (pEnd.y - p.y) / (pEnd.x - p.x));
		}
		return c;
	}

	private boolean testLineIntersect(Point p1, Point p2, Point q1, Point q2) {
		// Uses Sedgewick algorithm
		return ((ccw(p1, p2, q1) != ccw(p1, p2, q2)) && (ccw(q1, q2, p1) != ccw(q1, q2, p2)));
	}

	// Return true if a,b,c in counter-clockwise order
	private boolean ccw(Point a, Point b, Point c) {

		double dx1 = b.x - a.x;
		double dx2 = c.x - b.x;
		double dy1 = b.y - a.y;
		double dy2 = c.y - b.y;
		double gradient1 = dy2 * dx1;
		double gradient2 = dy1 * dx2;

		if (ChartUtil.mathEqual(gradient1, gradient2)) {
			// colinear case
			return (dx1 * dx2 >= 0 && dy1 * dy2 >= 0);
		} else {
			// generic case
			return (gradient1 > gradient2);
		}

	}

	public boolean intersects(Polygon poly) {
		if (poly == null) {
			return false;
		}

		if (points.size() > 2 && poly.points.size() > 2) {

			// TODO this is just a simple implementation for clip test.
			// Currently
			// only
			// works for convex polygon.

			boolean started = false;
			boolean diff = false, oldDiff = false;

			int count = points.size();
			double[] xa = new double[count];
			double[] ya = new double[count];

			for (int i = 0; i < count; i++) {
				Point pt = getPoint(i);
				xa[i] = pt.getX();
				ya[i] = pt.getY();
			}

			// first round test
			for (Iterator<Point> itr = poly.getPoints().iterator(); itr.hasNext();) {
				Point pt = itr.next();

				diff = testInside(count, xa, ya, pt.getX(), pt.getY());

				if (!started) {
					started = true;
					oldDiff = diff;
				}

				if (diff != oldDiff) {
					return true;
				}

				oldDiff = diff;
			}

			if (diff) {
				return true;
			}

			// second round test
			if (getPoints().size() > 0) {
				count = poly.getPoints().size();
				xa = new double[count];
				ya = new double[count];

				for (int i = 0; i < count; i++) {
					Point pt = poly.getPoint(i);
					xa[i] = pt.getX();
					ya[i] = pt.getY();
				}

				for (Iterator<Point> itr = points.iterator(); itr.hasNext();) {
					Point pt = itr.next();

					if (testInside(count, xa, ya, pt.getX(), pt.getY())) {
						return true;
					}
				}
			}
		} else
		// check line cases
		if (points.size() == 2 || poly.points.size() == 2) {
			List<Point> line = points;
			List<Point> pg = poly.points;

			if (line.size() > 2) {
				line = poly.points;
				pg = points;
			}

			Point lp1 = line.get(0);
			Point lp2 = line.get(1);
			for (int i = 0; i < pg.size(); i++) {
				if (i == pg.size() - 1) {
					if (testLineIntersect(lp1, lp2, pg.get(i), pg.get(0))) {
						return true;
					}
				} else {
					if (testLineIntersect(lp1, lp2, pg.get(i), pg.get(i + 1))) {
						return true;
					}
				}
			}
		}
		return false;

	}

	public boolean contains(Point p) {
		int count = points.size();
		double[] xa = new double[count];
		double[] ya = new double[count];

		for (int i = 0; i < count; i++) {
			Point pt = getPoint(i);
			xa[i] = pt.getX();
			ya[i] = pt.getY();
		}

		return testInside(count, xa, ya, p.getX(), p.getY()) || testWithinLine(p);
	}

}
