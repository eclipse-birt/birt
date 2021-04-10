/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.render;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.withoutaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;

/**
 * Pie
 */
public final class Pie extends BaseRenderer {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	private PieRenderer pr = null;

	/**
	 * The constructor.
	 */
	public Pie() {
		super();
	}

	/**
	 * 
	 * @param bo
	 * @param p
	 * @param isrh
	 * @throws ChartException
	 */
	public final void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.GENERATION, vex);
		}

		// SCALE VALIDATION
		final ChartWithoutAxes cwoa = (ChartWithoutAxes) getModel();
		final SeriesDefinition sd = getSeriesDefinition();
		final Bounds boCB = getCellBounds();
		try {
			pr = new PieRenderer(cwoa, this, srh.getDataPoints(), srh.asPrimitiveDoubleValues(), sd.getSeriesPalette());
			pr.computeInsets(boCB);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.GENERATION, ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#renderSeries(org.eclipse.birt.
	 * chart.device.IPrimitiveRenderer, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints)
	 */
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		ChartWithoutAxes cwoa = (ChartWithoutAxes) getModel();
		if (cwoa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL
				&& cwoa.getDimension() != ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.pie.dimension", //$NON-NLS-1$
					new Object[] { cwoa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		// UPDATE ALL PIE RENDERER INSETS TO ENSURE THAT THE PIES ARE RENDERED
		// THE SAME SIZE
		if (isFirstPie()) {
			Insets ins = null;
			boolean first = true;

			for (int i = 1; i < iSeriesCount; i++) {
				if (!(getRenderer(i) instanceof Pie)) {
					continue;
				}

				if (first) // SERIES INDEX = 1 CORRESPONDS TO THE FIRST PIE
				{
					ins = ((Pie) getRenderer(i)).getActualRenderer().getFittingInsets();

					first = false;
				} else {
					ins = goFactory.max(ins, ((Pie) getRenderer(i)).getActualRenderer().getFittingInsets());
				}
			}
			try {
				for (int i = 1; i < iSeriesCount; i++) {
					if (!(getRenderer(i) instanceof Pie)) {
						continue;
					}

					((Pie) getRenderer(i)).getActualRenderer().setFittingInsets(ins);
				}
			} catch (IllegalArgumentException uiex) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, uiex);
			}
		}

		final PieSeries ps = (PieSeries) getSeries();

		if (!ps.isVisible()) {
			return;
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				getRunTimeContext().getULocale()) + getClass().getName() + (iSeriesIndex + 1) + iSeriesCount); // i18n_CONCATENATIONS_REMOVED
		final Bounds boCB = getCellBounds(); // TODO: USE PREVIOUSLY CACHED
		// INSTANCE?
		pr.render(getDevice(), boCB);
	}

	final protected boolean isFirstPie() {
		for (int i = 1; i < iSeriesCount; i++) {
			if (getRenderer(i) instanceof Pie) {
				return getRenderer(i) == this;
			}
		}

		return false;
	}

	private final PieRenderer getActualRenderer() {
		return pr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#getFilteredMinSliceEntry(org.
	 * eclipse.birt.chart.computation.DataSetIterator)
	 */
	@Override
	public Collection<Integer> getFilteredMinSliceEntry(DataSetIterator dsi) {
		ChartWithoutAxes cwoa = (ChartWithoutAxes) getModel();

		if (!cwoa.isSetMinSlice()) {
			return Collections.<Integer>emptySet();
		}

		boolean bPercentageMinSlice = cwoa.isSetMinSlicePercent() && cwoa.isMinSlicePercent();
		double dMinSlice = cwoa.getMinSlice();

		dsi.reset();
		if (bPercentageMinSlice) {
			double total = 0;

			while (dsi.hasNext()) {
				Object obj = dsi.next();

				if (obj instanceof Number) {
					total += Math.abs(((Number) obj).doubleValue());
				}
			}

			dMinSlice = total * dMinSlice / 100d;
			dsi.reset();
		}

		double dMinSliceAbs = Math.abs(dMinSlice);
		Set<Integer> setIds = new HashSet<Integer>();

		for (int idx = 0; dsi.hasNext(); idx++) {
			Object obj = dsi.next();

			if (obj instanceof Number) {
				if (Math.abs(((Number) obj).doubleValue()) < dMinSliceAbs) {
					setIds.add(idx);
				}
			}
		}

		return setIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#renderLegendGraphic(org.eclipse.
	 * birt.chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.model.layout.Legend,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public final void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final PieSeries bs = (PieSeries) getSeries();
		final ColorDefinition cd = bs.getSliceOutline();
		final LineAttributes lia = goFactory.createLineAttributes(cd == null ? null : goFactory.copyOf(cd),
				LineStyle.SOLID_LITERAL, 1);

		// COMPUTE THE FRONT FACE AS A SCALENE TRIANGLE
		Location[] loaFrontFace = new Location[3];
		loaFrontFace[0] = goFactory.createLocation(bo.getLeft(), bo.getTop() + bo.getHeight());
		loaFrontFace[1] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight());
		loaFrontFace[2] = goFactory.createLocation(bo.getLeft() + 0.8 * bo.getWidth(), bo.getTop());

		// RENDER THE PLANE (INTERNALLY EXTRUDED IF NECESSARY)
		renderPlane(ipr, StructureSource.createLegend(lg), loaFrontFace, fPaletteEntry, lia, getModel().getDimension(),
				3 * getDeviceScale(), false);
	}
}