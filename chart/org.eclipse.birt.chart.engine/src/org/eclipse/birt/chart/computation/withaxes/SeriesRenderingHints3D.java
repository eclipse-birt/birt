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

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.render.ISeriesRenderingHints3D;

/**
 * SeriesRenderingHints3D
 */
public final class SeriesRenderingHints3D implements ISeriesRenderingHints3D {

	private int iDataSetStructure = UNDEFINED;

	private final double dXAxisLocation;

	private final double dZAxisLocation;

	private final double dPlotZeroLocation;

	private final double dPlotBaseLocation;

	private final double dPlotHeight;

	private final AxisTickCoordinates daXTickCoordinates;

	private final AxisTickCoordinates daZTickCoordinates;

	private final DataPointHints[] dpa;

	private final AutoScale scBase;

	private final AutoScale scOrthogonal;

	private final AutoScale scAncillary;

	private final PlotWith3DAxes pwa;

	private final DataSetIterator dsiBase;

	private final DataSetIterator dsiOrthogonal;

	private final DataSetIterator dsiAncillary;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * @param _pwa
	 * @param _dXAxisLocation
	 * @param _dZAxisLocation
	 * @param _dPlotBaseLocation
	 * @param _dPlotZeroLocation
	 * @param _daXTickCoordinates
	 * @param _daZTickCoordinates
	 * @param _dpa
	 * @param _scBase
	 * @param _scOrthogonal
	 * @param _scAncillary
	 * @param _dsiBase
	 * @param _dsiOrthogonal
	 * @param _dsiAncillary
	 */
	public SeriesRenderingHints3D(PlotWith3DAxes _pwa, double _dXAxisLocation, double _dZAxisLocation,
			double _dPlotBaseLocation, double _dPlotZeroLocation, double _dPlotHeight,
			AxisTickCoordinates _daXTickCoordinates, AxisTickCoordinates _daZTickCoordinates, DataPointHints[] _dpa,
			AutoScale _scBase, AutoScale _scOrthogonal, AutoScale _scAncillary, DataSetIterator _dsiBase,
			DataSetIterator _dsiOrthogonal, DataSetIterator _dsiAncillary) {
		pwa = _pwa;
		dXAxisLocation = _dXAxisLocation;
		dZAxisLocation = _dZAxisLocation;
		dPlotZeroLocation = _dPlotZeroLocation;
		dPlotBaseLocation = _dPlotBaseLocation;
		dPlotHeight = _dPlotHeight;
		daXTickCoordinates = _daXTickCoordinates;
		daZTickCoordinates = _daZTickCoordinates;
		dpa = _dpa;
		scBase = _scBase;
		scOrthogonal = _scOrthogonal;
		scAncillary = _scAncillary;
		dsiBase = _dsiBase;
		dsiOrthogonal = _dsiOrthogonal;
		dsiAncillary = _dsiAncillary;

		// DEFINE THE DATA SET STRUCTURES
		if (dsiBase.size() != dsiOrthogonal.size()) {
			iDataSetStructure |= BASE_ORTHOGONAL_OUT_OF_SYNC;
		} else {
			iDataSetStructure = BASE_ORTHOGONAL_IN_SYNC;
		}
		if (dsiBase.size() != dsiAncillary.size()) {
			iDataSetStructure |= BASE_ANCILLARY_OUT_OF_SYNC;
		} else {
			iDataSetStructure = BASE_ANCILLARY_IN_SYNC;
		}
		if (dsiBase.isEmpty()) {
			iDataSetStructure |= BASE_EMPTY;
		}
		if (dsiOrthogonal.isEmpty()) {
			iDataSetStructure |= ORTHOGONAL_EMPTY;
		}
		if (dsiAncillary.isEmpty()) {
			iDataSetStructure |= ANCILLARY_EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints#getDataSetStructure()
	 */
	public int getDataSetStructure() {
		return iDataSetStructure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.ISeriesRenderingHints#getBaseDataSet()
	 */
	public DataSetIterator getBaseDataSet() {
		return dsiBase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints#getOrthogonalDataSet()
	 */
	public DataSetIterator getOrthogonalDataSet() {
		return dsiOrthogonal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.ISeriesRenderingHints3D#getSeriesDataSet()
	 */
	public DataSetIterator getSeriesDataSet() {
		return dsiAncillary;
	}

	/**
	 * 
	 * @return The location (if vertical, then horizontal co-ordinate; if
	 *         horizontal, then vertical co-ordinate) of the category axis used in
	 *         the plot
	 */
	public final double getXAxisLocation() {
		return dXAxisLocation;
	}

	/**
	 * @return
	 */
	public final double getZAxisLocation() {
		return dZAxisLocation;
	}

	/**
	 * 
	 * @return The location (if vertical, then horizontal co-ordinate; if
	 *         horizontal, then vertical co-ordinate) of zero along the primary
	 *         orthogonal (value) axis used in the plot
	 */
	public final double getPlotZeroLocation() {
		return dPlotZeroLocation;
	}

	/**
	 * @param oValue The value for which a rendering co-ordinate is being requested
	 *               for
	 * @return The co-ordinate on the scale that corresponds to the requested value
	 * 
	 */
	public final double getLocationOnOrthogonal(Object oValue) throws ChartException, IllegalArgumentException {
		return Methods.getLocation(scOrthogonal, oValue);
	}

	/**
	 * 
	 * @return
	 */
	public final double getPlotBaseLocation() {
		return dPlotBaseLocation;
	}

	/**
	 * @return
	 */
	public final double getPlotHeight() {
		return dPlotHeight;
	}

	/**
	 * 
	 * @return The ticks' co-ordinates specified as a values along a horizontal or
	 *         vertical category axis. The other fixed co-ordinate is obtained via
	 *         the axis location.
	 */
	public final AxisTickCoordinates getXTickCoordinates() {
		return daXTickCoordinates;
	}

	/**
	 * 
	 * @return The ticks' co-ordinates specified as a values along the Z axis. The
	 *         other fixed co-ordinate is obtained via the axis location.
	 */
	public final AxisTickCoordinates getZTickCoordinates() {
		return daZTickCoordinates;
	}

	/**
	 * 
	 * @return Detailed plotting information for the data points represented by the
	 *         series rendering
	 */
	public final DataPointHints[] getDataPoints() {
		return dpa;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isXCategoryScale() {
		return (scBase.getType() == IConstants.TEXT || scBase.isCategoryScale());
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isZCategoryScale() {
		return (scAncillary.getType() == IConstants.TEXT || scAncillary.isCategoryScale());
	}

	/**
	 * 
	 * @param se
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public final Position getLabelPosition(Series se) throws ChartException, IllegalArgumentException {
		final int iTransposed = pwa.transposeLabelPosition(IConstants.ORTHOGONAL,
				Methods.getLabelPosition(se.getLabelPosition()));
		Position p = null;
		switch (iTransposed) {
		case IConstants.LEFT:
			p = Position.LEFT_LITERAL;
			break;
		case IConstants.RIGHT:
			p = Position.RIGHT_LITERAL;
			break;
		case IConstants.ABOVE:
			p = Position.ABOVE_LITERAL;
			break;
		case IConstants.BELOW:
			p = Position.BELOW_LITERAL;
			break;
		case IConstants.OUTSIDE:
			p = Position.OUTSIDE_LITERAL;
			break;
		case IConstants.INSIDE:
			p = Position.INSIDE_LITERAL;
			break;
		}
		return p;
	}

	/**
	 * 
	 * @param se
	 * @return
	 * @throws IllegalArgumentException
	 */
	public final Label getLabelAttributes(Series se) throws IllegalArgumentException {
		return goFactory.copyOf(se.getLabel());
	}

	/**
	 * 
	 * @param bReduceByInsets
	 * @return
	 */
	public final Bounds getClientAreaBounds(boolean bReduceByInsets) {
		final Bounds boClientArea = goFactory.copyOf(pwa.getPlotBounds());
		if (bReduceByInsets) {
			boClientArea.adjust(pwa.getPlotInsets());
		}
		return boClientArea;
	}

}
