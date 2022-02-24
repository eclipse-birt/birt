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

	final void initOverlays(int _iAxisCount, int _iOverlayOrientation) {
		if (_iAxisCount > 0) {
			oa = new OneAxis[_iAxisCount];
		} else {
			oa = null;
		}
		iOverlayOrientation = _iOverlayOrientation;
	}

	final void defineAncillaryBase(OneAxis axBase) {
		axAncillaryBase = axBase;
	}

	final void definePrimary(OneAxis axPrimary) {
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

	final void defineOverlay(int iAxisIndex, OneAxis axOverlay) {
		oa[iAxisIndex] = axOverlay;
	}

	public final OneAxis getOverlay(int iAxisIndex) {
		return oa[iAxisIndex];
	}

	final double getMaxStartShift() {
		double dMaxSS = 0;
		for (int i = 0; i < getOverlayCount(); i++) {
			if (oa[i].getScale() != null && oa[i].getScale().getStartShift() > dMaxSS) {
				dMaxSS = oa[i].getScale().getStartShift();
			}
		}
		return dMaxSS;
	}

	final double getMaxEndShift() {
		double dMaxES = 0;
		for (int i = 0; i < getOverlayCount(); i++) {
			if (oa[i].getScale() != null && oa[i].getScale().getEndShift() > dMaxES) {
				dMaxES = oa[i].getScale().getEndShift();
			}
		}
		return dMaxES;
	}

	public final int getOverlayCount() {
		return oa == null ? 0 : oa.length;
	}

	final int getOrientation() {
		return iOverlayOrientation;
	}

	final void setBlockCordinates(double _dStart, double _dLength) {
		dStart = _dStart;
		dLength = _dLength;
	}

	final double getStart() {
		return dStart;
	}

	final double getLength() {
		return dLength;
	}

	public final OneAxis getPrimaryBase() {
		return axPrimaryBase;
	}

	public final OneAxis getPrimaryOrthogonal() {
		return axPrimaryOrthogonal;
	}

	public final OneAxis getAncillaryBase() {
		return axAncillaryBase;
	}

	public boolean areAxesSwapped() {
		return bAxesSwapped;
	}

	final void swapAxes(boolean _bAxesSwapped) {
		bAxesSwapped = _bAxesSwapped;
		iOverlayOrientation = _bAxesSwapped ? HORIZONTAL : VERTICAL;
	}

	final boolean anyOverlayPositionedAt(int iMinOrMax) {
		final int iOC = getOverlayCount();
		for (int i = 0; i < iOC; i++) {
			if (getOverlay(i).getIntersectionValue().getType() == iMinOrMax) {
				return true;
			}
		}
		return false;
	}

	public final Insets getInsets() {
		return insClientArea;
	}
}
