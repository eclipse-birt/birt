/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import java.util.Objects;

/**
 * This class ...
 *
 * @author Actuate Corporation
 */
public final class BoundingBox implements Cloneable {

	private double dX, dY;

	private double dWidth, dHeight;

	private double dHotPoint;

	public BoundingBox(int _iLabelLocation, double _dX, double _dY, double _dWidth, double _dHeight,
			double _dHotPoint) {
		dX = _dX;
		dY = _dY;
		dWidth = _dWidth;
		dHeight = _dHeight;
		dHotPoint = _dHotPoint;
	}

	@Override
	public BoundingBox clone() {
		return new BoundingBox(0, dX, dY, dWidth, dHeight, dHotPoint);
	}

	public double getHotPoint() {
		return dHotPoint;
	}

	public double getTop() {
		return dY;
	}

	public double getLeft() {
		return dX;
	}

	public double getWidth() {
		return dWidth;
	}

	public double getHeight() {
		return dHeight;
	}

	public void setLeft(double _dX) {
		dX = _dX;
	}

	public void setTop(double _dY) {
		dY = _dY;
	}

	public void scale(double dScale) {
		dX *= dScale;
		dY *= dScale;
		dWidth *= dScale;
		dHeight *= dScale;
		dHotPoint *= dScale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dHeight, dHotPoint, dWidth, dX, dY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		BoundingBox other = (BoundingBox) obj;
		if (Double.doubleToLongBits(dHeight) != Double.doubleToLongBits(other.dHeight)) {
			return false;
		}
		if (Double.doubleToLongBits(dHotPoint) != Double.doubleToLongBits(other.dHotPoint)) {
			return false;
		}
		if (Double.doubleToLongBits(dWidth) != Double.doubleToLongBits(other.dWidth)) {
			return false;
		}
		if (Double.doubleToLongBits(dX) != Double.doubleToLongBits(other.dX)) {
			return false;
		}
		if (Double.doubleToLongBits(dY) != Double.doubleToLongBits(other.dY)) {
			return false;
		}
		return true;
	}
}
