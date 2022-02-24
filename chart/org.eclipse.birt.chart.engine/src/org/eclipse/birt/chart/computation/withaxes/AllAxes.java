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

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.model.attribute.Insets;

/**
 * AllAxes
 */
public final class AllAxes implements IConstants {

	// PRIMARY AXES INFORMATION
	private OneAxis axPrimaryBase = null;

	private OneAxis axPrimaryOrthogonal = null;

	private OneAxis axAncillaryBase = null;

	private boolean bAxesSwapped = false;

	// OVERLAY AXES INFORMATION
	private OneAxis[] oa = null;

	private double dStart = 0, dLength = 0;

	private int iOverlayOrientation = 0;

	private Insets insClientArea = null;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	AllAxes(Insets _insClientArea) {
		insClientArea = _insClientArea;
		if (insClientArea == null) {
			insClientArea = goFactory.createInsets(0, 0, 0, 0);
		}

	}

	void initOverlays(int _iAxisCount, int _iOverlayOrientation) {
		if (_iAxisCount > 0) {
			oa = new OneAxis[_iAxisCount];
		} else {
			oa = null;
		}
		iOverlayOrientation = _iOverlayOrientation;
	}

	void defineAncillaryBase(OneAxis axBase) {
		axAncillaryBase = axBase;
	}

	void definePrimary(OneAxis axPrimary) {
		int iOrientation = axPrimary.getOrientation();
		if (iOrientation == HORIZONTAL) {
			if (bAxesSwapped) {
				axPrimaryOrthogonal = axPrimary;
			} else {
				axPrimaryBase = axPrimary;
			}
		} else if (iOrientation == VERTICAL) {
			if (bAxesSwapped) {
				axPrimaryBase = axPrimary;
			} else {
				axPrimaryOrthogonal = axPrimary;
			}
		}
	}

	void defineOverlay(int iAxisIndex, OneAxis axOverlay) {
		oa[iAxisIndex] = axOverlay;
	}

	public OneAxis getOverlay(int iAxisIndex) {
		return oa[iAxisIndex];
	}

	double getMaxStartShift() {
		double dMaxSS = 0;
		for (int i = 0; i < getOverlayCount(); i++) {
			if (oa[i].getScale() != null && oa[i].getScale().getStartShift() > dMaxSS) {
				dMaxSS = oa[i].getScale().getStartShift();
			}
		}
		return dMaxSS;
	}

	double getMaxEndShift() {
		double dMaxES = 0;
		for (int i = 0; i < getOverlayCount(); i++) {
			if (oa[i].getScale() != null && oa[i].getScale().getEndShift() > dMaxES) {
				dMaxES = oa[i].getScale().getEndShift();
			}
		}
		return dMaxES;
	}

	public int getOverlayCount() {
		return oa == null ? 0 : oa.length;
	}

	int getOrientation() {
		return iOverlayOrientation;
	}

	void setBlockCordinates(double _dStart, double _dLength) {
		dStart = _dStart;
		dLength = _dLength;
	}

	double getStart() {
		return dStart;
	}

	double getLength() {
		return dLength;
	}

	public OneAxis getPrimaryBase() {
		return axPrimaryBase;
	}

	public OneAxis getPrimaryOrthogonal() {
		return axPrimaryOrthogonal;
	}

	public OneAxis getAncillaryBase() {
		return axAncillaryBase;
	}

	public boolean areAxesSwapped() {
		return bAxesSwapped;
	}

	void swapAxes(boolean _bAxesSwapped) {
		bAxesSwapped = _bAxesSwapped;
		iOverlayOrientation = _bAxesSwapped ? HORIZONTAL : VERTICAL;
	}

	boolean anyOverlayPositionedAt(int iMinOrMax) {
		final int iOC = getOverlayCount();
		for (int i = 0; i < iOC; i++) {
			if (getOverlay(i).getIntersectionValue().getType() == iMinOrMax) {
				return true;
			}
		}
		return false;
	}

	public Insets getInsets() {
		return insClientArea;
	}
}
