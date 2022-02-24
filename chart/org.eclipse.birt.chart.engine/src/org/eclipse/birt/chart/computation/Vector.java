/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.internal.computations.Matrix;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * A Vector class used to perform 3D computation.
 */
public class Vector {

	/**
	 * array makes computations easier, v[3]==1 means it's a point. v[3]==0 means
	 * it's a vector.
	 */
	private double v[] = new double[4];

	/**
	 * The default constructor. This makes an origin point.
	 */
	public Vector() {
		this.v[0] = 0;
		this.v[1] = 0;
		this.v[2] = 0;
		this.v[3] = 1;
	}

	/**
	 * @param start
	 * @param end
	 */
	public Vector(Location3D start, Location3D end) {
		this.v[0] = end.getX() - start.getX();
		this.v[1] = end.getY() - start.getY();
		this.v[2] = end.getZ() - start.getZ();
		this.v[3] = 0;
	}

	/**
	 * @param v
	 */
	public Vector(Vector v) {
		this.v[0] = v.v[0];
		this.v[1] = v.v[1];
		this.v[2] = v.v[2];
		this.v[3] = v.v[3];
	}

	/**
	 * @param loc
	 */
	public Vector(Location3D loc) {
		this.v[0] = loc.getX();
		this.v[1] = loc.getY();
		this.v[2] = loc.getZ();
		this.v[3] = 1;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(double x, double y, double z) {
		this(x, y, z, true);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param isPoint
	 */
	public Vector(double x, double y, double z, boolean isPoint) {
		this.v[0] = x;
		this.v[1] = y;
		this.v[2] = z;
		this.v[3] = isPoint ? 1 : 0;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param isPoint
	 */
	public void set(double x, double y, double z, boolean isPoint) {
		this.v[0] = x;
		this.v[1] = y;
		this.v[2] = z;
		this.v[3] = isPoint ? 1 : 0;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(double x, double y, double z) {
		this.v[0] = x;
		this.v[1] = y;
		this.v[2] = z;
	}

	/**
	 * @param i
	 * @return
	 */
	public double get(int i) {
		return v[i];
	}

	/**
	 * @param v
	 */
	public void add(Vector v) {
		this.v[0] += v.v[0];
		this.v[1] += v.v[1];
		this.v[2] += v.v[2];

	}

	public Vector getAdd(Vector v) {
		return new Vector(this.v[0] + v.v[0], this.v[1] + v.v[1], this.v[2] + v.v[2], false);
	}

	/**
	 * @param v
	 */
	public void sub(Vector v) {
		this.v[0] -= v.v[0];
		this.v[1] -= v.v[1];
		this.v[2] -= v.v[2];

	}

	public Vector getSub(Vector v) {
		return new Vector(this.v[0] - v.v[0], this.v[1] - v.v[1], this.v[2] - v.v[2], false);
	}

	/**
	 * @param f
	 */
	public void scale(double f) {
		this.v[0] *= f;
		this.v[1] *= f;
		this.v[2] *= f;
	}

	/**
	 * @param distance
	 */
	public void perspective(double distance) {
		v[0] = (v[0] / v[2]) * distance;
		v[1] = (v[1] / v[2]) * distance;
		v[2] = -1 / v[2];
	}

	/**
	 * @return
	 */
	public boolean isPoint() {
		return v[3] > 0;
	}

	/**
	 * @param v
	 * @return
	 */
	public Vector crossProduct(Vector v) {
		return new Vector(this.v[1] * v.v[2] - this.v[2] * v.v[1], this.v[2] * v.v[0] - this.v[0] * v.v[2],
				this.v[0] * v.v[1] - this.v[1] * v.v[0], false);
	}

	/**
	 * @param v
	 * @return
	 */
	public double scalarProduct(Vector v) {
		return this.v[0] * v.v[0] + this.v[1] * v.v[1] + this.v[2] * v.v[2];
	}

	/**
	 * @param v
	 * @return
	 */
	public double cosineValue(Vector v) {
		return scalarProduct(v) / (Math.sqrt(this.scalarProduct(this)) * Math.sqrt(v.scalarProduct(v)));
	}

	/**
	 * @param angle
	 */
	public void rotate(Angle3D angle) {
		double xr = Math.toRadians(angle.getXAngle());
		double yr = Math.toRadians(angle.getYAngle());
		double zr = Math.toRadians(angle.getZAngle());

		Matrix xm = new Matrix(new double[][] { { Math.cos(xr), -Math.sin(xr), 0, 0 },
				{ Math.sin(xr), Math.cos(xr), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 }, }, 4, 4);

		Matrix ym = new Matrix(new double[][] { { Math.cos(yr), 0, -Math.sin(yr), 0 }, { 0, 1, 0, 0 },
				{ Math.sin(yr), 0, Math.cos(yr), 0 }, { 0, 0, 0, 1 }, }, 4, 4);

		Matrix zm = new Matrix(new double[][] { { 1, 0, 0, 0 }, { 0, Math.cos(zr), -Math.sin(zr), 0 },
				{ 0, Math.sin(zr), Math.cos(zr), 0 }, { 0, 0, 0, 1 }, }, 4, 4);

		Matrix t = new Matrix(v, 1);

		t = t.times(xm).times(ym).times(zm);

		this.v[0] = t.get(0, 0);
		this.v[1] = t.get(0, 1);
		this.v[2] = t.get(0, 2);
	}

	/**
	 * @param dist
	 */
	public void project(int dist) {
		v[0] = (v[0] / v[2]) * dist;
		v[1] = (v[0] / v[2]) * dist;
		v[2] = -1 / v[2];
	}

	/**
	 * Returns the inverse direction of this vector.
	 */
	public void inverse() {
		v[0] = -v[0];
		v[1] = -v[1];
		v[2] = -v[2];
	}

	/**
	 * @param m
	 */
	public void multiply(Matrix m) {
		Matrix t = new Matrix(v, 1);

		t = t.times(m);

		this.v[0] = t.get(0, 0);
		this.v[1] = t.get(0, 1);
		this.v[2] = t.get(0, 2);
	}

	/**
	 * @param m
	 * @return
	 */
	public Vector getMultiply(Matrix m) {
		Matrix t = new Matrix(v, 1);

		t = t.times(m);

		return new Vector(t.get(0, 0), t.get(0, 1), t.get(0, 2), v[3] > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (v != null) {
			return "X:" + v[0] + ",Y:" + v[1] + ",Z:" + v[2] + ",PV:" + v[3]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return super.toString();
	}

	public double getNorm() {
		return Math.sqrt(this.scalarProduct(this));
	}

	public Vector getNormalized() {
		double norm = getNorm();
		if (ChartUtil.mathEqual(norm, 0)) {
			return new Vector(0, 0, 0, false);
		} else {
			return new Vector(v[0] / norm, v[1] / norm, v[2] / norm, false);
		}
	}

	public void normalize() {
		double norm = getNorm();
		if (!ChartUtil.mathEqual(norm, 0)) {
			for (int i = 0; i < 3; i++) {
				v[i] /= norm;
			}
		}

	}

	public boolean equals(Object other) {
		if (other instanceof Vector) {
			Vector ot = (Vector) other;
			return v[0] == ot.v[0] && v[1] == ot.v[1] && v[2] == ot.v[2];
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return Double.valueOf(v[0]).hashCode() ^ Double.valueOf(v[1]).hashCode()
				^ (31 * Double.valueOf(v[2]).hashCode());
	}

}
