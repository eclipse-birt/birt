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

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withoutaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;

/**
 * Dial
 */
public class Dial extends BaseRenderer {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	/**
	 * 
	 */
	private DialRenderer dr = null;

	/**
	 * 
	 */
	public Dial() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderer#compute(org.eclipse.birt.chart.
	 * model.attribute.Bounds, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.GENERATION, vex);
		}

		// SCALE VALIDATION
		final ChartWithoutAxes dct = (ChartWithoutAxes) getModel();
		final SeriesDefinition sd = getSeriesDefinition();

		try {
			dr = new DialRenderer(dct, this, srh, sd.getSeriesPalette());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.GENERATION, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderer#renderSeries(org.eclipse.birt.
	 * chart.device.IPrimitiveRenderer, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		ChartWithoutAxes cwoa = (ChartWithoutAxes) getModel();
		if (cwoa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.dial.dimension", //$NON-NLS-1$
					new Object[] { cwoa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final DialSeries ds = (DialSeries) getSeries();

		if (!ds.isVisible()) {
			return;
		}

		final Bounds boCB = isDialSuperimposed() ? getPlotBounds() : getCellBounds();

		// UPDATE ALL DIAL RENDERER INSETS TO ENSURE THAT ALL DIALS ARE RENDERED
		// USING THE SAME NECESSARY SETTINGS.
		if (isDialSuperimposed()) {
			double radius = 0;
			double startAngle = 0;
			double stopAngle = 0;
			Scale sc = null;
			AutoScale asc = null;
			double extraSpacing = 0;
			boolean inverseScale = false;
			boolean first = true;

			for (int i = 1; i < iSeriesCount; i++) {
				BaseRenderer br = getRenderer(i);
				Series se = br.getSeries();

				if (!(br instanceof Dial) || !se.isVisible()) {
					continue;
				}

				if (first) {
					radius = ((Dial) br).getActualRenderer().getDialRadius();
					startAngle = ((Dial) br).getActualRenderer().getDialStartAngle();
					stopAngle = ((Dial) br).getActualRenderer().getDialStopAngle();
					sc = ((Dial) br).getActualRenderer().getDialScale();
					inverseScale = ((Dial) br).getActualRenderer().isInverseScale();

					first = false;

				} else {
					radius = Math.max(radius, ((Dial) br).getActualRenderer().getDialRadius());
				}
			}

			for (int i = 1; i < iSeriesCount; i++) {
				BaseRenderer br = getRenderer(i);
				Series se = br.getSeries();
				DataSetIterator dsi = new DataSetIterator(se.getDataSet());
				BigDecimal divisor = null;
				dsi.reset();
				while (dsi.hasNext()) {
					Object o = dsi.next();
					if (NumberUtil.isBigNumber(o)) {
						divisor = ((BigNumber) o).getDivisor();
						break;
					}
				}

				if (!(br instanceof Dial) || !se.isVisible()) {
					continue;
				}

				// update the radius first
				((Dial) br).getActualRenderer().updateRadius(radius);

				asc = ((Dial) br).getActualRenderer().getAutoScale(startAngle, stopAngle, sc, boCB, divisor);

				// fix bugzilla: 122343
				if (sc.getMin() == null || sc.getMax() == null) {
					// This is an auto-scale case, walk through all actual
					// dials to auto-compute a proper scale.

					double min = Double.MAX_VALUE;
					double max = -Double.MAX_VALUE;

					for (int j = 1; j < iSeriesCount; j++) {
						BaseRenderer innerbr = getRenderer(j);
						Series innerse = innerbr.getSeries();

						if (!(innerbr instanceof Dial) || !innerse.isVisible()) {
							continue;
						}

						min = Math.min(min, ((Dial) innerbr).getActualRenderer().getValue());
						max = Math.max(max, ((Dial) innerbr).getActualRenderer().getValue());
					}

					Scale nsc = null;
					boolean needChange = false;

					if (sc.getMin() == null && Methods.getLocation(asc, min) < asc.getStart()) {
						nsc = sc.copyInstance();
						nsc.setMin(NumberDataElementImpl.create(min));
						needChange = true;
					}
					if (sc.getMax() == null && Methods.getLocation(asc, max) > asc.getEnd()) {
						if (nsc == null) {
							nsc = sc.copyInstance();
						}
						nsc.setMax(NumberDataElementImpl.create(max));
						needChange = true;
					}

					if (needChange) {
						sc = nsc;
						asc = ((Dial) br).getActualRenderer().getAutoScale(startAngle, stopAngle, nsc, boCB);
					}
				}

				// Compute the spacing using the final scale.
				extraSpacing = ((Dial) br).getActualRenderer().getDialExtraSpacing(asc);

				break;
			}

			for (int i = 1; i < iSeriesCount; i++) {
				BaseRenderer br = getRenderer(i);
				Series se = br.getSeries();

				if (!(br instanceof Dial) || !se.isVisible()) {
					continue;
				}

				((Dial) br).getActualRenderer().updateRadius(radius);
				((Dial) br).getActualRenderer().updateStartAngle(startAngle);
				((Dial) br).getActualRenderer().updateStopAngle(stopAngle);
				((Dial) br).getActualRenderer().updateScale(sc);
				((Dial) br).getActualRenderer().updateAutoScale(asc);
				((Dial) br).getActualRenderer().updateExtraSpacing(extraSpacing);
				((Dial) br).getActualRenderer().updateInverseScale(inverseScale);
			}
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				getRunTimeContext().getULocale()) + getClass().getName() + (iSeriesIndex + 1) + iSeriesCount);
		dr.render(getDevice(), boCB);
	}

	final protected Bounds getFirstDialBounds() {
		for (int i = 1; i < iSeriesCount; i++) {
			if (getRenderer(i) instanceof Dial) {
				return getCellBounds(i);
			}
		}

		return getCellBounds();
	}

	final protected boolean isFirstDial() {
		for (int i = 1; i < iSeriesCount; i++) {
			BaseRenderer br = getRenderer(i);

			if (br instanceof Dial) {
				Series se = br.getSeries();

				if (se.isVisible()) {
					return br == this;
				}
			}
		}

		return false;
	}

	/**
	 * @return
	 */
	final protected boolean isDialSuperimposed() {
		if (getModel() instanceof DialChart) {
			DialChart dct = (DialChart) getModel();

			return dct.isDialSuperimposition();
		}

		return false;
	}

	/**
	 * @return
	 */
	private final DialRenderer getActualRenderer() {
		return dr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.ISeriesRenderer#renderLegendGraphic(org.eclipse
	 * .birt.chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.model.layout.Legend,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final DialSeries ds = (DialSeries) getSeries();
		final ColorDefinition cd = ds.getDial().getLineAttributes().getColor();
		final LineAttributes lia = goFactory.createLineAttributes(cd == null ? null : goFactory.copyOf(cd),
				LineStyle.SOLID_LITERAL, 1);

		OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				OvalRenderEvent.class);
		ore.setBackground(fPaletteEntry);
		ore.setOutline(lia);
		ore.setBounds(goFactory.copyOf(bo));
		ipr.fillOval(ore);
		ipr.drawOval(ore);
	}

}
