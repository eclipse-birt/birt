/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Responsible for rendering a scatter series on a plot. Note that it reuses
 * rendering methods available in the Line renderer by subclassing the Line
 * renderer. The only difference in this implementation is in the positioning of
 * the data point markers which are 'absolute' rather than repositioned in the
 * middle of each unit (as is done for Line plots).
 */
public class Scatter extends Line {

	/**
	 * The constructor.
	 */
	public Scatter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.
	 * chart.event.IPrimitiveRenderListener,
	 * org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.computation.axes.SeriesRenderingHints)
	 */
	@Override
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		if (cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.scatter.dimension", //$NON-NLS-1$
					new Object[] { cwa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale()));
		ScatterSeries ss = (ScatterSeries) getSeries();

		if (!ss.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// COMPUTE THE POINTS THROUGH WHICH THE LINE SERIES IS RENDERED
		final AbstractScriptHandler sh = getRunTimeContext().getScriptHandler();
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
		DataPointHints[] dpha = srh.getDataPoints();
		Location lo;
		LineAttributes lia = ss.getLineAttributes();
		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];

		// SETUP THE RISER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { ss }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && ss.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) ss.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(ss);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { ss, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		Marker m = null;
		if (ss.getMarkers().size() > 0) {
			m = ss.getMarkers().get(iThisSeriesIndex % ss.getMarkers().size());
		}

		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
		} else if (iSeriesIndex > 0) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iSeriesIndex - 1);
		}
		updateTranslucency(fPaletteEntry, ss);

		boolean isCategoryAxis = srh.isCategoryScale();

		for (int i = 0; i < dpha.length; i++) {
			double unitSize = dpha[i].getSize();
			lo = dpha[i].getLocation();

			if (cwa.isTransposed()) {
				faX[i] = lo.getX();
				faY[i] = lo.getY() + (isCategoryAxis ? (unitSize / 2) : 0);
			} else {
				faX[i] = lo.getX() + (isCategoryAxis ? (unitSize / 2) : 0);
				faY[i] = lo.getY();
			}
		}

		handleOutsideDataPoints(ipr, srh, faX, faY, false);

		if (ss.isCurve()) {
			CurveRenderer cr = new CurveRenderer(cwa, this, ss.getLineAttributes(), goFactory.createLocations(faX, faY),
					false, -1, false, true, fPaletteEntry, ss.isPaletteLineColor(), true);
			cr.draw(ipr);

			renderShadowAsCurve(ipr, lia, srh, goFactory.createLocations(faX, faY), false, -1);

			// RENDER THE MARKERS NEXT
			if (m != null) {
				for (int i = 0; i < dpha.length; i++) {
					if (dpha[i].isOutside()) {
						continue;
					}

					if (bPaletteByCategory) {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
					} else {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
					}
					updateTranslucency(fPaletteEntry, ss);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(ss, ipr, m, goFactory.createLocation(faX[i], faY[i]), ss.getLineAttributes(),
							fPaletteEntry, dpha[i], null, true, true);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i]);
				}
			}
		} else {
			// COMPUTE THE PLOTTED CO-ORDINATES FOR EACH DATA POINT
			LineRenderEvent lre;

			final Location positionDelta = (cwa.isTransposed()) ? goFactory.createLocation(-3 * getDeviceScale(), 0)
					: goFactory.createLocation(0, 3 * getDeviceScale());

			// RENDER THE SHADOW OF THE LINE IF APPLICABLE
			Location[] loaShadow = null;
			lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createSeries(ss), LineRenderEvent.class);
			final ColorDefinition cLineShadow = ss.getShadowColor();
			boolean bConnectMissing = ss.isConnectMissingValue();

			if (!ChartUtil.isColorTransparent(cLineShadow)) {
				for (int i = 1; i < dpha.length; i++) {
					int pindex = -1;

					if (bConnectMissing) {
						if (isNaN(dpha[i].getOrthogonalValue())) {
							continue;
						}

						pindex = getPreviousNonNullIndex(i, dpha);

						if (pindex == -1) {
							continue;
						}
					} else {
						if (isNaN(dpha[i].getOrthogonalValue())) {
							continue;
						}

						if (i > 0 && isNaN(dpha[i - 1].getOrthogonalValue())) {
							continue;
						}
						pindex = i - 1;
					}

					// TODO: THIS DOES NOT TAKE INTO ACCOUNT LINES RENDERED AT
					// ARBITRARY ANGLES...ONLY STRAIGHT LINES
					// AND TRANSPOSED LINES ARE HANDLED
					if (loaShadow == null) {
						loaShadow = new Location[2];
						loaShadow[0] = goFactory.createLocation(faX[pindex] + positionDelta.getX(),
								faY[pindex] + positionDelta.getY());
						loaShadow[1] = goFactory.createLocation(faX[i] + positionDelta.getX(),
								faY[i] + positionDelta.getY());
					} else {
						loaShadow[0].set(faX[pindex] + positionDelta.getX(), faY[pindex] + positionDelta.getY());
						loaShadow[1].set(faX[i] + positionDelta.getX(), faY[i] + positionDelta.getY());
					}
					lre.setStart(loaShadow[0]);
					lre.setEnd(loaShadow[1]);
					lre.setZOrder(getSeriesDefinition().getZOrder());
					LineAttributes liaShadow = goFactory.copyOf(lia);
					liaShadow.setColor(cLineShadow);
					lre.setLineAttributes(liaShadow);
					dc.addConnectionLine(lre);
				}
			}

			if (lia.isVisible()) {
				Location[] loa = new Location[2];
				for (int i = 1; i < dpha.length; i++) {
					int pindex = -1;

					if (bConnectMissing) {
						if (isNaN(dpha[i].getOrthogonalValue())) {
							continue;
						}

						pindex = getPreviousNonNullIndex(i, dpha);

						if (pindex == -1) {
							continue;
						}
					} else {
						if (isNaN(dpha[i].getOrthogonalValue())) {
							continue;
						}

						if (i > 0 && isNaN(dpha[i - 1].getOrthogonalValue())) {
							continue;
						}
						pindex = i - 1;
					}

					loa[0] = goFactory.createLocation(faX[pindex], faY[pindex]);
					loa[1] = goFactory.createLocation(faX[i], faY[i]);
					// ONLY SUPPORT 2D STYLE.
					// if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL)
					{
						lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createSeries(ss),
								LineRenderEvent.class);
						if (ss.isPaletteLineColor()) {
							LineAttributes newLia = goFactory.copyOf(lia);
							newLia.setColor(FillUtil.getColor(fPaletteEntry));
							lre.setLineAttributes(newLia);
						} else {
							lre.setLineAttributes(lia);
						}

						lre.setStart(loa[0]);
						lre.setEnd(loa[1]);
						lre.setZOrder(getSeriesDefinition().getZOrder());
						dc.addConnectionLine(lre);
					}
					/*
					 * else // TBD: BSP HIDDEN SURFACE REMOVAL FOR INTERSECTING PLANES? {
					 * renderPlane(ipr, p, loa, lia.getColor().brighter(), lia, cd,
					 * dSeriesThickness); }
					 */
				}
			}

			// RENDER THE MARKERS NEXT
			if (m != null) {
				for (int i = 0; i < dpha.length; i++) {
					if (dpha[i].isOutside()) {
						continue;
					}

					if (bPaletteByCategory) {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
					} else {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
					}
					updateTranslucency(fPaletteEntry, ss);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(ss, ipr, m, goFactory.createLocation(faX[i], faY[i]), ss.getLineAttributes(),
							fPaletteEntry, dpha[i], null, true, true);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i]);
				}
			}
		}

		// RENDER THE DATA POINT LABELS IF REQUESTED
		Label laDataPoint = null;
		Position pDataPoint = null;
		Location loDataPoint = null;
		try {
			laDataPoint = srh.getLabelAttributes(ss);
			pDataPoint = srh.getLabelPosition(ss);
			loDataPoint = goFactory.createLocation(0, 0);
		} catch (Exception ex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
		}

		if (laDataPoint.isVisible()) {
			final double dSize = m == null ? 0 : m.getSize();
			for (int i = 0; i < dpha.length; i++) {
				if (isNaN(dpha[i].getOrthogonalValue()) || dpha[i].isOutside()) {
					continue;
				}
				laDataPoint = srh.getLabelAttributes(ss);
				laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());
				switch (pDataPoint.getValue()) {
				case Position.ABOVE:
					loDataPoint.set(faX[i], faY[i] - dSize - p.getVerticalSpacing());
					break;
				case Position.BELOW:
					loDataPoint.set(faX[i], faY[i] + dSize + p.getVerticalSpacing());
					break;
				case Position.LEFT:
					loDataPoint.set(faX[i] - dSize - p.getHorizontalSpacing(), faY[i]);
					break;
				case Position.RIGHT:
					loDataPoint.set(faX[i] + dSize + p.getHorizontalSpacing(), faY[i]);
					break;
				default:
					throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
							"exception.invalid.data.point.position.scatter", //$NON-NLS-1$
							new Object[] { pDataPoint }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
				}
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						dpha[i]);
				if (laDataPoint.isVisible()) {
					renderLabel(WrappedStructureSource.createSeriesDataPoint(ss, dpha[i]),
							TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, pDataPoint, loDataPoint, null);
				}
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
						dpha[i]);
			}
		}

		// Render the fitting curve.
		if (getSeries().getCurveFitting() != null) {
			Location[] larray = new Location[faX.length];
			for (int i = 0; i < larray.length; i++) {
				larray[i] = goFactory.createLocation(faX[i], faY[i]);
			}
			larray = filterNull(larray, isrh.getDataPoints());
			renderFittingCurve(ipr, larray, getSeries().getCurveFitting(), false, true);
		}

		restoreClipping(ipr);

	}
}
