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

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Responsible for rendering a Bubble series on a plot. Note that it reuses
 * rendering methods available in the Scatter renderer by subclassing the
 * Scatter renderer. The only difference in this implementation is in the
 * positioning of the data point markers which are 'absolute' rather than
 * repositioned in the middle of each unit (as is done for Scatter plots).
 */
public class Bubble extends Scatter {

	/**
	 * The constructor.
	 */
	public Bubble() {
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
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		if (cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL
				&& cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.bubble.dimension", //$NON-NLS-1$
					new Object[] { cwa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale()));

		BubbleSeries bs = (BubbleSeries) getSeries();

		if (!bs.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// COMPUTE THE POINTS THROUGH WHICH THE LINE SERIES IS RENDERED
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
		DataPointHints[] dpha = srh.getDataPoints();
		Location lo;
		LineAttributes lia = bs.getLineAttributes();
		LineAttributes accLia = bs.getAccLineAttributes();
		Orientation accOrientation = bs.getAccOrientation();
		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];

		// Bubble size in measured by points
		Integer[] iSize = new Integer[dpha.length];
		// Bubble size in measured by pixels
		double[] dSizePixel = new double[dpha.length];

		// SETUP THE FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { bs }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && bs.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) bs.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(bs);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { bs, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			updateTranslucency(fPaletteEntry, bs);
		}

		Marker m = null;
		if (bs.getMarkers().size() > 0) {
			m = bs.getMarkers().get(iThisSeriesIndex % bs.getMarkers().size());
		}

		final boolean isCategoryAxis = srh.isCategoryScale();

		// Handles the max size to avoid filling too much space
		final Bounds plotBounds = getPlotBoundsWithMargin();
		double dPointCoefficient = computeBestFit(srh, plotBounds, isCategoryAxis);

		if (dPointCoefficient == 0) {
			// Do not render bubbles, since size is 0

			restoreClipping(ipr);
			return;
		}

		for (int i = 0; i < dpha.length; i++) {
			BubbleEntry be = (BubbleEntry) dpha[i].getOrthogonalValue();
			if (!isValidBubbleEntry(be)) {
				// Use NaN to handle null later
				faX[i] = Double.NaN;
				faY[i] = Double.NaN;
				continue;
			}

			double unitSize = dpha[i].getSize();
			lo = dpha[i].getLocation();
			if (cwa.isTransposed()) {
				faX[i] = srh.getLocationOnOrthogonal(be.getValue());
				faY[i] = lo.getY() + (isCategoryAxis ? (unitSize / 2) : 0);
			} else {
				faX[i] = lo.getX() + (isCategoryAxis ? (unitSize / 2) : 0);
				faY[i] = srh.getLocationOnOrthogonal(be.getValue());
			}

			dSizePixel[i] = be.getSize() * dPointCoefficient;
			// Set bubble size measured by points because of the arguments of
			// MarkerRender
			iSize[i] = Integer.valueOf((int) (dSizePixel[i] / getDeviceScale()));
		}

		handleOutsideDataPoints(ipr, srh, faX, faY, false);

		PlotWith2DAxes pwa = (PlotWith2DAxes) getComputations();
		double dAxisMin;
		if (accOrientation == Orientation.HORIZONTAL_LITERAL) {
			OneAxis oaxaBase = pwa.getAxes().getPrimaryBase();
			AutoScale sc = oaxaBase.getScale();
			dAxisMin = sc.getTickCordinates().getStart();
		} else {
			OneAxis oaxaBase = pwa.getAxes().getPrimaryOrthogonal();
			AutoScale sc = oaxaBase.getScale();
			dAxisMin = sc.getTickCordinates().getStart();
		}

		// render the acceleration lines.
		if (accLia.isVisible()) {
			Location[] loa = new Location[2];
			for (int i = 0; i < dpha.length; i++) {
				if (!isValidBubbleEntry((BubbleEntry) dpha[i].getOrthogonalValue())) {
					continue;
				}

				LineRenderEvent lre;

				if (accOrientation == Orientation.HORIZONTAL_LITERAL) {
					if (cwa.isTransposed()) {
						loa[0] = goFactory.createLocation(faX[i], dAxisMin);
					} else {
						loa[0] = goFactory.createLocation(dAxisMin, faY[i]);
					}
				} else {
					if (cwa.isTransposed()) {
						loa[0] = goFactory.createLocation(dAxisMin, faY[i]);
					} else {
						loa[0] = goFactory.createLocation(faX[i], dAxisMin);
					}
				}
				loa[1] = goFactory.createLocation(faX[i], faY[i]);

				lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createSeries(bs), LineRenderEvent.class);
				lre.setLineAttributes(accLia);
				lre.setStart(loa[0]);
				lre.setEnd(loa[1]);
				ipr.drawLine(lre);
			}
		}

		if (bs.isCurve()) {
			CurveRenderer cr = new CurveRenderer(cwa, this, lia, goFactory.createLocations(faX, faY), false, -1, true,
					true, fPaletteEntry, bs.isPaletteLineColor(), true);
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
					updateTranslucency(fPaletteEntry, bs);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(bs, ipr, m, goFactory.createLocation(faX[i], faY[i]), bs.getLineAttributes(),
							fPaletteEntry, dpha[i], iSize[i], true, true);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i]);
				}
			}
		} else {
			// COMPUTE THE PLOTTED CO-ORDINATES FOR EACH DATA POINT
			LineRenderEvent lre;

			// DRAW THE LINE SEGMENTS

			final Location positionDelta = (cwa.isTransposed()) ? goFactory.createLocation(-3 * getDeviceScale(), 0)
					: goFactory.createLocation(0, 3 * getDeviceScale());

			// RENDER THE SHADOW OF THE LINE IF APPLICABLE
			Location[] loaShadow = null;
			lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createSeries(bs), LineRenderEvent.class);
			final ColorDefinition cLineShadow = bs.getShadowColor();

			if (cLineShadow != null && cLineShadow.getTransparency() != goFactory.TRANSPARENT().getTransparency()) {
				for (int i = 1; i < dpha.length; i++) {
					if (!isValidBubbleEntry((BubbleEntry) dpha[i].getOrthogonalValue())) {
						continue;
					}

					int pindex = getPreviousNonNullIndex(i, dpha);
					if (pindex == -1) {
						continue;
					}

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
					LineAttributes liaShadow = goFactory.copyOf(lia);
					liaShadow.setColor(cLineShadow);
					lre.setLineAttributes(liaShadow);
					ipr.drawLine(lre);
				}
			}

			if (lia.isVisible()) {
				Location[] loa = new Location[2];
				for (int i = 1; i < dpha.length; i++) {
					if (!isValidBubbleEntry((BubbleEntry) dpha[i].getOrthogonalValue())) {
						continue;
					}

					int pindex = getPreviousNonNullIndex(i, dpha);
					if (pindex == -1) {
						continue;
					}

					loa[0] = goFactory.createLocation(faX[pindex], faY[pindex]);
					loa[1] = goFactory.createLocation(faX[i], faY[i]);

					lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createSeries(bs),
							LineRenderEvent.class);
					if (bs.isPaletteLineColor()) {
						LineAttributes newLia = goFactory.copyOf(lia);
						newLia.setColor(FillUtil.getColor(fPaletteEntry));
						lre.setLineAttributes(newLia);
					} else {
						lre.setLineAttributes(lia);
					}
					lre.setStart(loa[0]);
					lre.setEnd(loa[1]);
					ipr.drawLine(lre);
				}
			}

			// RENDER THE MARKERS NEXT
			if (m != null) {
				for (int i = 0; i < dpha.length; i++) {
					if (!isValidBubbleEntry((BubbleEntry) dpha[i].getOrthogonalValue()) || dpha[i].isOutside()) {
						continue;
					}
					if (bPaletteByCategory) {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
					} else {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
					}
					updateTranslucency(fPaletteEntry, bs);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(bs, ipr, m, goFactory.createLocation(faX[i], faY[i]), bs.getLineAttributes(),
							fPaletteEntry, dpha[i], iSize[i], true, true);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
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
			laDataPoint = srh.getLabelAttributes(bs);
			pDataPoint = srh.getLabelPosition(bs);
			loDataPoint = goFactory.createLocation(0, 0);
		} catch (Exception ex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
		}

		if (laDataPoint.isVisible()) {
			for (int i = 0; i < dpha.length; i++) {
				if (!isValidBubbleEntry(((BubbleEntry) dpha[i].getOrthogonalValue())) || dpha[i].isOutside()) {
					continue;
				}

				laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());
				final double dSize = dSizePixel[i];
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
				case Position.INSIDE:
					loDataPoint.set(faX[i], faY[i]);
					break;
				default:
					throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
							"exception.invalid.data.point.position.bubble", //$NON-NLS-1$
							new Object[] { pDataPoint }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
				}
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						dpha[i]);
				if (laDataPoint.isVisible()) {
					renderLabel(StructureSource.createSeries(bs), TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
							pDataPoint, loDataPoint, null);
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
			larray = filterNull(larray);
			renderFittingCurve(ipr, larray, getSeries().getCurveFitting(), false, true);
		}

		restoreClipping(ipr);
	}

	private boolean isValidBubbleEntry(BubbleEntry entry) {
		return (entry != null && entry.isValid());
	}

	/**
	 * Computes the max bubble size that fits in all bubble series
	 * 
	 * @param srh
	 * @param plotBounds
	 * @param isCategoryAxis
	 * @throws ChartException
	 */
	private double computeBestFit(final SeriesRenderingHints srh, final Bounds plotBounds,
			final boolean isCategoryAxis) {
		double dCoeff = 1;
		double dLastCoeff = -1;
		boolean isFirstFit = false;
		for (int i = 0; i < 50; i++) {
			try {
				if (checkAllInbound(srh, plotBounds, dCoeff, isCategoryAxis)) {
					dLastCoeff = dCoeff;
					dCoeff *= 2;

					if (i == 0) {
						isFirstFit = true;
					}

					if (!isFirstFit) {
						// Fitting first time means best fit
						return dLastCoeff;
					}
				} else {
					if (dLastCoeff > 0) {
						// Fitting last time means best fit
						return dLastCoeff;
					}
					dCoeff /= 2;
				}
			} catch (RuntimeException ex) {
				// No bubbles in range, just return 0
			} catch (ChartException ex) {
				logger.log(ex);
			}
		}
		return 0;
	}

	/**
	 * Checks if all bubbles are inside of the plot
	 * 
	 * @param srh
	 * @param plotBounds
	 * @param dCoeff
	 * @param isCategoryAxis
	 * @throws ChartException
	 */
	private boolean checkAllInbound(final SeriesRenderingHints srh, final Bounds plotBounds, final double dCoeff,
			final boolean isCategoryAxis) throws ChartException {
		// This flag is to avoid the case that no valid bubbles are checked
		boolean hasEntryChecked = false;
		final AutoScale scaleOrth = getInternalOrthogonalAxis().getScale();
		final AutoScale scaleBase = getInternalBaseAxis().getScale();
		for (int i = 1; i < iSeriesCount; i++) {
			BaseRenderer renderer = getRenderer(i);
			if (renderer instanceof Bubble) {
				// #233885
				// only series of the same axis should be calculated.
				if (((Bubble) renderer).getAxis() != this.getAxis()) {
					continue;
				}
				DataPointHints[] dpha = renderer.getSeriesRenderingHints().getDataPoints();
				for (int j = 0; j < dpha.length; j++) {
					final BubbleEntry be = (BubbleEntry) dpha[j].getOrthogonalValue();
					if (!isValidBubbleEntry(be)) {
						continue;
					}

					// Check if the value of bubble center is in the scale
					// bound.
					if (checkEntryByType(scaleOrth, dpha[j].getOrthogonalValue()) == 0
							&& checkEntryByType(scaleBase, dpha[j].getBaseValue()) == 0) {
						final Location lo = dpha[j].getLocation();
						final double unitSize = dpha[j].getSize();
						double faX, faY;
						if (isTransposed()) {
							faX = srh.getLocationOnOrthogonal(be.getValue());
							faY = lo.getY() + (isCategoryAxis ? (unitSize / 2) : 0);
						} else {
							faX = lo.getX() + (isCategoryAxis ? (unitSize / 2) : 0);
							faY = srh.getLocationOnOrthogonal(be.getValue());
						}

						// Indicates at least one valid entry is checked
						hasEntryChecked = true;

						// Check if the bubble is inbound
						// Check if position of the bubble center is outbound.
						// It seems a bug when the value is inbound but the
						// position is outbound
						if (checkInbound(plotBounds, faX, faY, 0)) {
							if (!checkInbound(plotBounds, faX, faY, Math.abs(be.getSize()) * dCoeff)) {
								return false;
							}
						}
					}
				}
			}
		}

		if (!hasEntryChecked) {
			throw new RuntimeException(Messages.getString("exception.invalid.data.point.outbound.bubble")); //$NON-NLS-1$
		}

		return hasEntryChecked;
	}

	/**
	 * Checks if a bubbles is inside of the plot
	 * 
	 * @param plotBounds
	 * @param faX
	 * @param faY
	 * @param size
	 */
	private boolean checkInbound(Bounds plotBounds, double faX, double faY, double size) {
		return faX - size > plotBounds.getLeft() && faX + size < plotBounds.getLeft() + plotBounds.getWidth()
				&& faY - size > plotBounds.getTop() && faY + size < plotBounds.getTop() + plotBounds.getHeight();
	}

	protected int checkEntryInRange(Object entry, Object min, Object max) {
		if (entry instanceof BubbleEntry) {
			int iOutside = 0;
			Object oValue = ((BubbleEntry) entry).getValue();
			if (oValue instanceof Number) {
				// Double entry
				double value = ((Number) oValue).doubleValue();
				double dMin = Methods.asDouble(min).doubleValue();
				double dMax = Methods.asDouble(max).doubleValue();
				if (value < dMin) {
					iOutside = 1;
				} else if (value > dMax) {
					iOutside = 2;
				}
			} else if (oValue instanceof CDateTime) {
				// Datetime entry
				CDateTime value = (CDateTime) oValue;
				CDateTime cMin = Methods.asDateTime(min);
				CDateTime cMax = Methods.asDateTime(max);
				if (value.before(cMin)) {
					iOutside = 1;
				} else if (value.after(cMax)) {
					iOutside = 2;
				}
			} else {
				iOutside = 1;
			}
			return iOutside;
		}
		// If not bubble, it's outside to ignore the size check.
		return 1;
	}

	protected int getPreviousNonNullIndex(int currentIndex, DataPointHints[] dpha) {
		for (int i = currentIndex - 1; i >= 0; i--) {
			if (isValidBubbleEntry((BubbleEntry) dpha[i].getOrthogonalValue())) {
				return i;
			}
		}
		return -1;
	}

	public void set(Chart _cm, PlotComputation _o, Series _se, Axis _ax, SeriesDefinition _sd) {
		super.set(_cm, _o, _se, _ax, _sd);

		if (_o instanceof PlotWith2DAxes) {
			PlotWith2DAxes pwa = (PlotWith2DAxes) _o;
			// Add 20% of client area for bubbles
			pwa.addMargin(20);
		}
	}

	protected boolean isShowOutside() {
		// Always clip the bubbles
		return false;
	}

	protected void flushClipping() throws ChartException {
		// Flush markers before clipping
		getDeferredCache().flushOptions(DeferredCache.FLUSH_MARKER);
	}

	protected final Bounds getPlotBoundsWithMargin() {
		Object obj = getComputations();

		Bounds bo = null;

		if (obj instanceof PlotWith2DAxes) {
			PlotWith2DAxes pwa = (PlotWith2DAxes) obj;

			bo = goFactory.adjusteBounds(pwa.getPlotBoundsWithMargin(), pwa.getPlotInsets());
		}

		return bo;
	}
}
