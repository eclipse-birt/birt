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

package org.eclipse.birt.chart.computation.withoutaxes;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;

/**
 * SeriesRenderingHints
 */
public class SeriesRenderingHints implements ISeriesRenderingHints {

	private int iDataSetStructure = UNDEFINED;

	private final DataSetIterator dsiBase;

	private final DataSetIterator dsiOrthogonal;

	private final DataPointHints[] dpha;

	private final PlotWithoutAxes pwoa;

	private final static IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The constructor.
	 *
	 * @param pwoa
	 * @param dpha
	 * @param dsiBase
	 * @param dsiOrthogonal
	 */
	SeriesRenderingHints(PlotWithoutAxes pwoa, DataPointHints[] dpha, DataSetIterator dsiBase,
			DataSetIterator dsiOrthogonal) {
		this.pwoa = pwoa;
		this.dpha = dpha;
		this.dsiBase = dsiBase;
		this.dsiOrthogonal = dsiOrthogonal;

		// DEFINE THE DATA SET STRUCTURES
		if (dsiBase.size() != dsiOrthogonal.size()) {
			iDataSetStructure |= BASE_ORTHOGONAL_OUT_OF_SYNC;
		} else {
			iDataSetStructure = BASE_ORTHOGONAL_IN_SYNC;
		}
		if (dsiBase.isEmpty()) {
			iDataSetStructure |= BASE_EMPTY;
		}
		if (dsiOrthogonal.isEmpty()) {
			iDataSetStructure |= ORTHOGONAL_EMPTY;
		}
	}

	@Override
	public final DataPointHints[] getDataPoints() {
		return dpha;
	}

	public final Double[] asDoubleValues() throws ChartException {
		final int iCount = dpha.length;
		final Double[] doa = new Double[iCount];
		NumberDataElement nde;
		Object o;

		for (int i = 0; i < iCount; i++) {
			o = dpha[i].getOrthogonalValue();
			if (o instanceof NumberDataElement) {
				nde = (NumberDataElement) o;
				doa[i] = new Double(nde.getValue());
			} else if (o == null) {
				doa[i] = null;
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
						"exception.dataset.non.numerical.to.numerical", //$NON-NLS-1$
						new Object[] { o }, Messages.getResourceBundle(pwoa.getRunTimeContext().getULocale()));
			}
		}
		return doa;
	}

	public final double[] asPrimitiveDoubleValues() throws ChartException {
		final int iCount = dpha.length;
		final double[] doa = new double[iCount];
		Object o;

		for (int i = 0; i < iCount; i++) {
			o = dpha[i].getOrthogonalValue();
			if (o instanceof Number) {
				doa[i] = ((Number) o).doubleValue();
			} else if (o == null) {
				doa[i] = Double.NaN;
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
						"exception.dataset.non.numerical.to.numerical", //$NON-NLS-1$
						new Object[] { o }, Messages.getResourceBundle(pwoa.getRunTimeContext().getULocale()));
			}
		}
		return doa;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints#getDataSetStructure()
	 */
	@Override
	public int getDataSetStructure() {
		return iDataSetStructure;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.render.ISeriesRenderingHints#getBaseDataSet()
	 */
	@Override
	public DataSetIterator getBaseDataSet() {
		return dsiBase;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints#getOrthogonalDataSet()
	 */
	@Override
	public DataSetIterator getOrthogonalDataSet() {
		return dsiOrthogonal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.render.ISeriesRenderingHints#getClientAreaBounds(
	 * boolean)
	 */
	@Override
	public Bounds getClientAreaBounds(boolean bReduceByInsets) {
		final Bounds boClientArea = goFactory.copyOf(pwoa.getPlotBounds());
		if (bReduceByInsets) {
			boClientArea.adjust(pwoa.getPlotInsets());
		}
		return boClientArea;
	}
}
