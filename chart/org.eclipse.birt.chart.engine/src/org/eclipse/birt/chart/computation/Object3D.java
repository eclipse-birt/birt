/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.chart.internal.computations.Matrix;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;

/**
 * This class represents a 3D object and is used to perform all 3d operations.
 */
public class Object3D {

	private Vector[] va;

	private Vector[] viewVa;

	private Vector center;

	private Vector normal;

	private double xMax, xMin;

	private double yMax, yMin;

	private double zMax, zMin;

	private int iZmin;

	private int iZmax;

	private HashMap<Object3D, Boolean> hmSwap = new HashMap<>();

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * Construction by an empty array of coordinates
	 */
	public Object3D(int points) {
		va = new Vector[points];
	}

	/**
	 * Construction by one 3d coordinate
	 */
	public Object3D(Location3D la) {
		this(new Location3D[] { la });
	}

	/**
	 * Construction by an array of 3d coordinates
	 *
	 * @param the points that constitue the 3D object. If there are more than 2
	 *            points the order of points decides the orientation of the surface.
	 *            Only the outside face is painted, unless the object is
	 *            double-sided. Note that polygons making a volume should not be
	 *            double-sided.
	 */
	public Object3D(Location3D[] loa) {
		this(loa, false);
	}

	/**
	 * Construction by an array of 3d coordinates
	 *
	 * @param loa
	 * @param inverted invert the order of points to change the surface orientation
	 */
	public Object3D(Location3D[] loa, boolean inverted) {
		// Remove duplicate points to avoid wrong computation later
		loa = removeDuplicatePoints(loa);
		va = new Vector[loa.length];
		for (int i = 0; i < va.length; i++) {
			if (!inverted) {
				va[i] = new Vector(loa[i]);
				loa[i].linkToVector(va[i]);
			} else {
				va[va.length - 1 - i] = new Vector(loa[i]);
				loa[i].linkToVector(va[va.length - 1 - i]);
			}
		}

	}

	/**
	 * Construction by another Object3D object
	 */
	public Object3D(Object3D original) {
		if (original == null) {
			return;
		}

		this.va = new Vector[original.va.length];
		for (int i = 0; i < original.va.length; i++) {
			this.va[i] = new Vector(original.va[i]);
		}
		center = original.center;
		normal = original.normal;
		zMax = original.zMax;
		zMin = original.zMin;
		yMax = original.yMax;
		yMin = original.yMin;
		xMax = original.xMax;
		xMin = original.xMin;
		iZmin = 0;
		iZmax = 0;
	}

	/**
	 * Returns the 3d coordinates for this object.
	 */
	public Location3D[] getLocation3D() {
		Location3D[] loa3d = new Location3D[va.length];
		for (int i = 0; i < va.length; i++) {
			loa3d[i] = goFactory.createLocation3D(va[i].get(0), va[i].get(1), va[i].get(2));
		}
		return loa3d;
	}

	/**
	 * return the normal (orientation) vector of the plane determined by points pt0,
	 * pt1 and pt2
	 *
	 * @param pt0
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static Vector getPlaneNormal(Vector pt0, Vector pt1, Vector pt2) {
		Vector v1 = new Vector(pt1);
		v1.sub(pt0);
		Vector v2 = new Vector(pt2);
		v2.sub(pt0);
		return v1.crossProduct(v2);
	}

	/**
	 * returns the normal vector (pointing outside the enclosed volume for oriented
	 * polygons.)
	 */
	public Vector getNormal() {
		if (normal == null) {
			if (va == null || va.length < 3) {
				return null;
			}

			// create vectors with first three points and returns cross products
			normal = getPlaneNormal(va[0], va[1], va[2]);
		}

		return normal;
	}

	// public Vector getNormalView( )
	// {
	// if ( normalView == null )
	// {
	// if ( viewVa == null || viewVa.length < 3 )
	// {
	// return null;
	// }
	//
	// // create vectors with first three points and returns cross products
	// normalView = getPlaneNormal( viewVa[0], viewVa[1], viewVa[2] );
	// }
	//
	// return normalView;
	// }

	/**
	 * Returns center of gravity of polygon
	 */
	public Vector getCenter() {
		if (center == null) {
			if (va == null || va.length == 0) {
				return null;
			}

			double m = va.length;

			center = new Vector();

			for (int i = 0; i < m; i++) {
				center.add(va[i]);
			}
			center.scale(1d / m);
		}
		return center;
	}

	/**
	 * Resets all values to defaults.
	 */
	public void reset() {
		this.center = null;
		this.normal = null;
		this.va = null;
		this.viewVa = null;
		this.zMax = 0;
		this.zMin = 0;
		this.yMax = 0;
		this.yMin = 0;
		this.xMax = 0;
		this.xMin = 0;
		this.iZmin = 0;
		this.iZmax = 0;
	}

	/*
	 * Returns the point with the fursthest z
	 */
	public Vector getZMaxPoint() {
		return va[iZmax];
	}
	/*
	 * Returns the point with the nearest z
	 */

	public Vector getZMinPoint() {
		return va[iZmin];
	}

	/**
	 * Returns maximum X value for this object
	 */
	public double getXMax() {
		return xMax;
	}

	/**
	 * Returns minimum X value for this object
	 */
	public double getXMin() {
		return xMin;
	}

	/**
	 * Returns maximum Y value for this object
	 */
	public double getYMax() {
		return yMax;
	}

	/**
	 * Returns minimum Y value for this object
	 */
	public double getYMin() {
		return yMin;
	}

	/**
	 * Returns maximum Z value for this object
	 */
	public double getZMax() {
		return zMax;
	}

	/**
	 * Returns minimum Z value for this object
	 */
	public double getZMin() {
		return zMin;
	}

	/**
	 * Performs transformation by given matrix
	 */
	public void transform(Matrix m) {
		for (int i = 0; i < va.length; i++) {
			va[i].multiply(m);
		}
		if (center != null) {
			// center.multiply( m );
		}
		if (normal != null) {
			// normal.multiply( m );
		}
	}

	private void computeExtremums() {
		xMin = Double.MAX_VALUE;
		xMax = -Double.MAX_VALUE;

		yMin = Double.MAX_VALUE;
		yMax = -Double.MAX_VALUE;

		zMin = Double.MAX_VALUE;
		zMax = -Double.MAX_VALUE;

		for (int i = 0; i < va.length; i++) {
			xMin = Math.min(xMin, va[i].get(0));
			xMax = Math.max(xMax, va[i].get(0));

			yMin = Math.min(yMin, va[i].get(1));
			yMax = Math.max(yMax, va[i].get(1));

			if (zMin > va[i].get(2)) {
				zMin = va[i].get(2);
				iZmin = i;
			}
			if (zMax < va[i].get(2)) {
				zMax = va[i].get(2);
				iZmax = i;
			}

		}
	}

	/**
	 * Check and clip vectors by given engine.
	 */
	public void clip(Engine3D engine) {
		byte retval;

		List<Vector> lst = new ArrayList<>();

		switch (va.length) {
		case 0:
			break;
		case 1: {
			Vector start = new Vector(va[0]);
			Vector end = new Vector(va[0]);

			retval = engine.checkClipping(start, end);

			if (retval != Engine3D.OUT_OF_RANGE_BOTH) {
				lst.add(start);
			}
		}
			break;
		case 2: {
			Vector start = new Vector(va[0]);
			Vector end = new Vector(va[1]);

			retval = engine.checkClipping(start, end);

			if (retval != Engine3D.OUT_OF_RANGE_BOTH) {
				lst.add(start);
				lst.add(end);
			}
		}
			break;

		default: {
			boolean endClipped = false;

			for (int i = 0; i < va.length; i++) {
				Vector start = null;
				Vector end = null;

				if (i == va.length - 1) {
					start = new Vector(va[i]);
					end = new Vector(va[0]);
				} else {
					start = new Vector(va[i]);
					end = new Vector(va[i + 1]);
				}

				retval = engine.checkClipping(start, end);

				if (retval != Engine3D.OUT_OF_RANGE_BOTH) {
					if (i == 0 || (retval & Engine3D.OUT_OF_RANGE_START) != 0 || endClipped) {
						lst.add(start);
					}

					endClipped = false;

					if ((retval & Engine3D.OUT_OF_RANGE_END) != 0) {
						endClipped = true;
					}

					if (i != va.length - 1 || endClipped) {
						lst.add(end);
					}

				}
			}
		}
			break;
		}
		va = lst.toArray(new Vector[lst.size()]);
	}

	/**
	 * Prepars for Z-sorting
	 */
	public void prepareZSort() {
		computeExtremums();
		getNormal();
		getCenter();

		viewVa = new Vector[va.length];
		for (int i = 0; i < va.length; i++) {
			viewVa[i] = new Vector(va[i]);
		}

	}

	/**
	 * Perspective transformation of the vectors.
	 */
	public void perspective(double distance) {
		for (int i = 0; i < va.length; i++) {
			va[i].perspective(distance);
		}
		if (center != null) {
			// center.perspective( distance );
		}
		// computeExtremums( );
	}

	/**
	 * Returns vectors in model frame for this object
	 */
	public Vector[] getVectors() {
		return va;
	}

	/**
	 * Returns vectors in viewer frame for this object
	 */
	public Vector[] getViewerVectors() {
		return viewVa;
	}

	/**
	 * Returns the projected 2D coordinates for this object
	 */
	public Location[] getPoints2D(double xOffset, double yOffset) {
		Location[] locations = new Location[va.length];
		for (int i = 0; i < va.length; i++) {
			locations[i] = goFactory.createLocation(va[i].get(0) + xOffset, va[i].get(1) + yOffset);
		}
		return locations;
	}

	/**
	 * Returns if comparedObj is totally in front of /behind the current object.
	 */
	protected boolean testAside(Object3D comparedObj, boolean bFront, Engine3D engine) {
		int thisPointsNumber = viewVa.length;
		int comparedPointsNumber = comparedObj.getViewerVectors().length;

		if (thisPointsNumber == 0 || comparedPointsNumber == 0 || (thisPointsNumber < 3 && comparedPointsNumber < 3)) {
			// test two lines or point in a line.
			return true;
		}

		Vector normal = null;
		Vector ov = viewVa[0];

		double d = 0;
		if (thisPointsNumber < 3 || comparedPointsNumber < 3) {
			// We do not actully support compare of a line segment and a plane.
			// The result may be incorrect.
			// The solution is to set a plane, which contains this line, to the
			// parent of a line, so that instead of comparing the line itself,
			// its
			// parent will be compared
			return true;
		} else {
			// case: polygon obscures polygon
			// normal = getNormalView( );
			normal = getNormal();

			// necessary for the plan equation ax+by+cz+d=0
			d = -normal.scalarProduct(ov);

			Vector vVRP = engine.getViewReferencePoint();

			boolean bViewerOutside = isOutSide(normal, d, vVRP);

			if (bFront) {
				return testPolygon(normal, d, comparedObj, bViewerOutside);
			} else {
				return testPolygon(normal, d, comparedObj, !bViewerOutside);
			}
		}
	}

	/**
	 *
	 * @return true if the point vPoint is on the outer sider of the plane
	 *         determined by the vNorm and d
	 */
	private boolean isOutSide(Vector vNormal, double d, Vector vPoint) {
		double p = vNormal.scalarProduct(vPoint) + d;
		return (p > -1E-7);
	}

	public boolean isBehind(Vector point) {
		// double d = -getNormalView( ).scalarProduct( viewVa[0] );
		// return isOutSide( getNormalView( ), d, point );
		double d = -getNormal().scalarProduct(viewVa[0]);
		return isOutSide(getNormal(), d, point);
	}

	/**
	 * return whether all point of the obj is outside/inside the plane determinded
	 * by normal and d
	 *
	 * @param normal
	 * @param d
	 * @param obj
	 * @param outside
	 * @return
	 */
	protected boolean testPolygon(Vector normal, double d, Object3D obj, boolean outside) {
		Vector[] tva = obj.getViewerVectors();

		for (int i = 0; i < tva.length; i++) {
			double p = tva[i].scalarProduct(normal) + d;

			if (outside) {
				if (p < -1E-7) {
					return false;
				}
			} else if (p > 1E-7) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Tests if two objects intersects.
	 */
	protected boolean testIntersect(Object3D near, Engine3D engine) {
		Vector[] va1 = this.getVectors();
		Vector[] va2 = near.getVectors();

		Polygon p1 = new Polygon();
		for (int i = 0; i < va1.length; i++) {
			p1.add(va1[i].get(0), va1[i].get(1));
		}

		Polygon p2 = new Polygon();
		for (int i = 0; i < va2.length; i++) {
			p2.add(va2[i].get(0), va2[i].get(1));
		}

		return p1.intersects(p2);
	}

	/**
	 * Tests if two objects overlap in X direction.
	 */
	protected boolean testXOverlap(Object3D near) {
		return !(this.getXMin() >= near.getXMax() || near.getXMin() >= this.getXMax());
	}

	/**
	 * Tests if two objects overlap in Y direction.
	 */
	protected boolean testYOverlap(Object3D near) {
		return !(this.getYMin() >= near.getYMax() || near.getYMin() >= this.getYMax());
	}

	/**
	 * Tests if two objects need swapping.
	 */
	public boolean testSwap(Object3D near, Engine3D engine) {
		Boolean bCached = hmSwap.get(near);
		if (bCached != null) {
			return bCached;
		}

		Object3D far = this;

		boolean swap = false;
		if (far.testXOverlap(near) && far.testYOverlap(near)) {
			if (!(far.testAside(near, true, engine))) {
				if (!(near.testAside(far, false, engine))) {
					if (far.testIntersect(near, engine)) {
						swap = true;
					}
				}
			}
		}

		hmSwap.put(near, swap);

		return swap;
	}

	/**
	 * Tests if two objects overlap in Z direction.
	 */
	public boolean testZOverlap(Object3D near) {
		return !((this.getZMin() >= near.getZMax()) || (near.getZMin() >= this.getZMax()));
	}

	/**
	 * Removed duplicate points to avoid wrong computations later.
	 *
	 * @param loa
	 * @return
	 */
	private Location3D[] removeDuplicatePoints(Location3D[] loa) {
		if (loa.length > 3) {
			int iWrong = -1;
			for (int i = 1; i < loa.length; i++) {
				if (loa[i].getX() == loa[i - 1].getX() && loa[i].getY() == loa[i - 1].getY()
						&& loa[i].getZ() == loa[i - 1].getZ()) {
					// Find one duplicate point
					iWrong = i;
					break;
				}
			}
			if (iWrong >= 0) {
				// Handle one duplicate point
				Location3D[] newLoa = new Location3D[loa.length - 1];
				for (int i = 0, j = 0; i < loa.length; i++) {
					if (i != iWrong) {
						newLoa[j++] = loa[i];
					}
				}
				// check for next duplicate point
				return removeDuplicatePoints(newLoa);
			}
		}
		return loa;
	}

	protected int getFollowingIndex(int index, boolean next) {

		if (next) {
			if (index + 1 > va.length - 1) {
				return 0;
			} else {
				return index + 1;
			}

		} else if (index - 1 < 0) {
			return va.length - 1;
		} else {
			return index - 1;
		}
	}

	public Object3D getSharedEdge(Object3D other) {
		for (int i = 0; i < va.length; i++) {
			for (int j = 0; j < other.va.length; j++) {
				if (va[i].equals(other.va[j])) {
					int adjacentIndex = 0;
					int otherAdjacentIndex = 0;
					boolean next = true;
					boolean otherNext = true;
					for (int k = 0; k < 4; k++) {
						switch (k) {
						case 0:
							next = true;
							otherNext = true;
							break;
						case 1:
							next = true;
							otherNext = false;
							break;
						case 2:
							next = false;
							otherNext = true;
							break;
						case 3:
							next = false;
							otherNext = false;
							break;
						}
						adjacentIndex = getFollowingIndex(i, next);
						otherAdjacentIndex = other.getFollowingIndex(j, otherNext);
						if (va[adjacentIndex].equals(other.va[otherAdjacentIndex])) {
							// Polygon overlapping the two polygons with a shared edge.
							Object3D sharedEdge = new Object3D(6);

							sharedEdge.va[0] = va[i];
							sharedEdge.va[1] = computeNextEdgePoint(i, !next);
							sharedEdge.va[2] = computeNextEdgePoint(adjacentIndex, next);
							sharedEdge.va[3] = va[adjacentIndex];
							sharedEdge.va[4] = other.computeNextEdgePoint(otherAdjacentIndex, otherNext);
							sharedEdge.va[5] = other.computeNextEdgePoint(j, !otherNext);
							return sharedEdge;
						}
					}

				}
			}
		}

		return null;

	}

	// Computea point for the shared edge polygon. Near the edge, towards the next
	// point
	protected Vector computeNextEdgePoint(int i, boolean next) {
		Vector point = va[i];
		Vector nextPoint = va[getFollowingIndex(i, next)];
		Vector dir = nextPoint.getSub(point);
		dir.normalize();

		return point.getAdd(dir);
	}
}
