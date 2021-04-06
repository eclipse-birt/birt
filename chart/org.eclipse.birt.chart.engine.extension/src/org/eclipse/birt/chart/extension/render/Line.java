/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.render;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AxisSubUnit;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints3D;
import org.eclipse.birt.chart.computation.withaxes.StackedSeriesLookup;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.render.Line.DataPointsRenderer.Context;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Line
 */
public class Line extends AxesRenderer {

	protected static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	protected DeferredCache subDeferredCache;

	/**
	 * The constructor.
	 */
	public Line() {
		super();
	}

	/**
	 * Computes the end values for stacked lines
	 */
	protected double computeStackPosition(AxisSubUnit au, double dValue, Axis ax) {
		if (ax.isPercent()) {
			dValue = au.valuePercentage(dValue);
		}

		return au.stackValue(dValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.
	 * chart.output.IRenderer, Chart.Plot)
	 */
	@SuppressWarnings("deprecation")
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, vex);
		}

		boolean bRendering3D = isDimension3D();

		// SCALE VALIDATION
		SeriesRenderingHints srh = null;
		SeriesRenderingHints3D srh3d = null;

		if (bRendering3D) {
			srh3d = (SeriesRenderingHints3D) isrh;
		} else {
			srh = (SeriesRenderingHints) isrh;
		}

		// SCALE VALIDATION
		// if ( ( !bRendering3D && !srh.isCategoryScale( ) )
		// || ( bRendering3D && !srh3d.isXCategoryScale( ) ) )
		// {
		// throw new ChartException( ChartEngineExtensionPlugin.ID,
		// ChartException.RENDERING,
		// "exception.xvalue.scale.lines", //$NON-NLS-1$
		// Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		// }

		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		ChartWithAxes cwa = (ChartWithAxes) getModel();
		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale())); // i18n_CONCATENATIONS_REMOVED

		final Bounds boClientArea = isrh.getClientAreaBounds(true);
		final double dSeriesThickness = bRendering3D ? 0 : srh.getSeriesThickness();
		if (cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
			boClientArea.delta(-dSeriesThickness, dSeriesThickness, 0, 0);
		}

		// OBTAIN AN INSTANCE OF THE SERIES MODEL (AND SOME VALIDATION)
		LineSeries ls = (LineSeries) getSeries();
		if (!ls.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// SETUP VARIABLES NEEDED TO RENDER THE LINES/CURVE
		ChartDimension cd = cwa.getDimension();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		DataPointHints[] dpha = isrh.getDataPoints();
		validateNullDatapoint(dpha);

		double fX = 0, fY = 0, fZ = 0, fWidth = 0, fWidthZ = 0, fHeight = 0;
		Location lo = null;
		Location3D lo3d = null;

		// DETERMINE IF THE LINES SHOULD BE SHOWN AS TAPES OR 2D LINES
		// Line chart has no 2D+, so just 3D is shown as tape.
		boolean isAreaSeries = (getSeries() instanceof AreaSeries && !(getSeries() instanceof DifferenceSeries));
		boolean bShowAsTape = (cd.getValue() == ChartDimension.THREE_DIMENSIONAL)
				|| (isAreaSeries && cd.getValue() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH);

		if (bShowAsTape) {
			bShowAsTape = validateShowAsTape();
		}

		// SETUP VARIABLES NEEDED IN STACKED COMPUTATIONS AND GROUPING
		AxisSubUnit au;
		Axis ax = getAxis();
		double dValue, dEnd;

		StackedSeriesLookup ssl = null;
		if (!bRendering3D) {
			ssl = srh.getStackedSeriesLookup();
		}

		// SETUP VARIABLES NEEDED TO COMPUTE CO-ORDINATES
		LineAttributes lia = ls.getLineAttributes();

		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];
		double[] faZ = new double[dpha.length];

		// SETUP THE MARKER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { ls }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && ls.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) ls.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(ls);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { ls, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}
		Marker m = null;
		if (ls.getMarkers().size() > 0) {
			m = ls.getMarkers().get(iThisSeriesIndex % ls.getMarkers().size());
		}

		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
		} else if (iSeriesIndex > 0) {
			// Here eliminate the position for one base series.
			// NOTE only one base series allowed now.
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iSeriesIndex - 1);
		}

		updateTranslucency(fPaletteEntry, ls);

		double dTapeWidth = -1;
		double dUnitSpacingZ = 0;

		// THE MAIN LOOP THAT WALKS THROUGH THE DATA POINT HINTS ARRAY 'dpha'
		for (int i = 0; i < dpha.length; i++) {
			if (bRendering3D) {
				lo3d = dpha[i].getLocation3D();

				if (ChartUtil.mathEqual(dTapeWidth, -1)) {
					final double dUnitSpacing = (!cwa.isSetUnitSpacing()) ? 50 : cwa.getUnitSpacing(); // AS A
																										// PERCENTAGE OF
					// ONE
					dTapeWidth = dpha[i].getSize2D().getHeight() * (100 - dUnitSpacing) / 100;

					dUnitSpacingZ = dpha[i].getSize2D().getHeight() * dUnitSpacing / 200;
				}
			} else {
				lo = dpha[i].getLocation(); // TBD: CHECK FOR NULL VALUES
			}

			if (cwa.isTransposed()) {
				if (srh.isCategoryScale()) {
					fHeight = dpha[i].getSize();
				}
				fY = (lo.getY() + fHeight / 2.0);
				// shouldn't update DataPointHints that may affect the next
				// rendering without re-computation
				// lo.setY( fY );
				faY[i] = fY;

				if (ls.isStacked() || ax.isPercent()) // SPECIAL
				// PROCESSING
				// FOR STACKED OR
				// PERCENT SERIES
				{
					au = ssl.getUnit(ls, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN(dpha[i].getOrthogonalValue()) ? 0
							: ((Double) dpha[i].getOrthogonalValue()).doubleValue();
					dEnd = computeStackPosition(au, dValue, ax);

					try {
						// NOTE: FLOORS DONE TO FIX ROUNDING ERRORS IN GFX
						// CONTEXT (DOUBLE EDGES)
						faX[i] = Math.floor(srh.getLocationOnOrthogonal(new Double(dEnd)));
						// Add following statement to correct painting the stacked flip chart when
						// negative value exists.
						dpha[i].setStackOrthogonalValue(new Double(dEnd));

						if (faX[i] < srh.getPlotBaseLocation()) {
							faX[i] = srh.getPlotBaseLocation();
						}

						au.setLastPosition(dValue, faX[i], 0);
					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}

				} else {
					faX[i] = lo.getX();
				}

			} else {
				if (bRendering3D) {
					fWidth = dpha[i].getSize2D().getWidth();
					fWidthZ = dpha[i].getSize2D().getHeight();
					fX = lo3d.getX() + fWidth / 2;
					fZ = lo3d.getZ() + fWidthZ - dUnitSpacingZ;
					// shouldn't update DataPointHints that may affect the next
					// rendering without re-computation
					// lo3d.setX( fX );
					// lo3d.setZ( fZ );
					faX[i] = fX;
					faZ[i] = fZ;
				} else {
					if (srh.isCategoryScale()) {
						fWidth = dpha[i].getSize();
					}
					fX = (lo.getX() + fWidth / 2.0);
					// shouldn't update DataPointHints that may affect the next
					// rendering without re-computation
					// lo.setX( fX );
					faX[i] = fX;
				}

				if (ls.isStacked() || ax.isPercent()) // SPECIAL
				// PROCESSING
				// FOR STACKED OR
				// PERCENT SERIES
				{
					if (bRendering3D) {
						// Not support stack/percent for 3D chart.
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.COMPUTATION,
								"exception.no.stack.percent.3D.chart", //$NON-NLS-1$
								Messages.getResourceBundle(getRunTimeContext().getULocale()));
					}

					au = ssl.getUnit(ls, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN(dpha[i].getOrthogonalValue()) ? 0
							: ((Number) dpha[i].getOrthogonalValue()).doubleValue();
					dEnd = computeStackPosition(au, dValue, ax);

					try {
						// NOTE: FLOORS DONE TO FIX ROUNDING ERRORS IN GFX
						// CONTEXT (DOUBLE EDGES)
						faY[i] = Math.floor(srh.getLocationOnOrthogonal(new Double(dEnd)));
						dpha[i].setStackOrthogonalValue(new Double(dEnd));

						au.setLastPosition(dValue, faY[i], 0);
					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}
				} else {
					if (bRendering3D) {
						faY[i] = lo3d.getY();
					} else {
						faY[i] = lo.getY();
					}

				}

				// Range check.
				if (bRendering3D) {
					double plotBaseLocation = srh3d.getPlotBaseLocation();

					// RANGE CHECK (WITHOUT CLIPPING)
					if (faY[i] < plotBaseLocation) // TOP EDGE
					{
						faY[i] = plotBaseLocation; // - This causes
						// clipping in output
					}

					if (faY[i] > plotBaseLocation + srh3d.getPlotHeight()) // BOTTOM
					// EDGE
					{
						faY[i] = plotBaseLocation + srh3d.getPlotHeight();
					}
				}

			}
		}

		if (!bRendering3D) {
			// Area does not support show outside
			handleOutsideDataPoints(ipr, srh, faX, faY, bShowAsTape);
		}

		// In order to simplify the computation of rendering order of
		// planes in 3D space, here we create a plane and add it into
		// current deferred cache, it is used as a delegate of all planes of
		// current series to take part in the sorting of planes in 3D space,
		// the order of this plane will determine the rendering order of
		// current series in 3D space. Also we create a sub deferred cache
		// instead of current deferred cache to store all planes of current
		// series, and add the sub-deferred cache as a child of this plane
		// event, all planes in sub-deferred cache will be processed and
		// rendered after this plane is processed.
		if (bRendering3D) {
			addComparsionPolygon(ipr, goFactory.createLocation3Ds(faX, faY, faZ), dpha);
		}

		if (ls.isCurve()) {
			// RENDER AS CURVE
			renderAsCurve(ipr, ls.getLineAttributes(), bRendering3D ? (ISeriesRenderingHints) srh3d : srh,
					bRendering3D ? goFactory.createLocation3Ds(faX, faY, faZ) : goFactory.createLocations(faX, faY),
					bShowAsTape, dTapeWidth, fPaletteEntry, ls.isPaletteLineColor());

			renderShadowAsCurve(ipr, lia, bRendering3D ? (ISeriesRenderingHints) srh3d : srh,
					bRendering3D ? goFactory.createLocation3Ds(faX, faY, faZ) : goFactory.createLocations(faX, faY),
					bShowAsTape, dTapeWidth);

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
					updateTranslucency(fPaletteEntry, ls);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(ls, ipr, m,
							bRendering3D ? goFactory.createLocation3D(faX[i], faY[i], faZ[i])
									: goFactory.createLocation(faX[i], faY[i]),
							ls.getLineAttributes(), fPaletteEntry, dpha[i], null, true, true);

					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i]);
				}
			}
		} else {
			// RENDER THE SHADOW OF THE LINE IF APPLICABLE
			renderShadow(ipr, p, lia,
					bRendering3D ? goFactory.createLocation3Ds(faX, faY, faZ) : goFactory.createLocations(faX, faY),
					bShowAsTape, dpha);

			// RENDER THE SERIES DATA POINTS
			renderDataPoints(ipr, p, bRendering3D ? (ISeriesRenderingHints) srh3d : srh, dpha, lia,
					bRendering3D ? goFactory.createLocation3Ds(faX, faY, faZ) : goFactory.createLocations(faX, faY),
					bShowAsTape, dTapeWidth, fPaletteEntry, ls.isPaletteLineColor());

			// RENDER THE MARKERS NEXT
			if (m != null) {
				for (int i = 0; i < dpha.length; i++) {
					if (bPaletteByCategory) {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
					} else {
						fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
					}
					updateTranslucency(fPaletteEntry, ls);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT,
							dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT,
							dpha[i]);
					renderMarker(ls, ipr, m,
							bRendering3D ? goFactory.createLocation3D(faX[i], faY[i], faZ[i])
									: goFactory.createLocation(faX[i], faY[i]),
							ls.getLineAttributes(), fPaletteEntry, dpha[i], null, true, true);

					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fPaletteEntry);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT,
							dpha[i]);
				}
			}
		}

		// DATA POINT RELATED VARIABLES ARE INITIALIZED HERE
		Label laDataPoint = null;
		Position pDataPoint = null;
		Location loDataPoint = null;
		Location3D loDataPoint3d = null;
		try {
			if (bRendering3D) {
				laDataPoint = srh3d.getLabelAttributes(ls);
				if (laDataPoint.isVisible()) // ONLY COMPUTE IF NECESSARY
				{
					pDataPoint = srh3d.getLabelPosition(ls);
					loDataPoint3d = goFactory.createLocation3D(0, 0, 0);
				}
			} else {
				laDataPoint = srh.getLabelAttributes(ls);
				if (laDataPoint.isVisible()) // ONLY COMPUTE IF NECESSARY
				{
					pDataPoint = srh.getLabelPosition(ls);
					loDataPoint = goFactory.createLocation(0, 0);
				}
			}
		} catch (Exception ex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
		}

		if (laDataPoint.isVisible()) {
			final double dSize = m == null ? 0 : m.getSize();
			for (int i = 0; i < dpha.length; i++) {
				if (isNaN(dpha[i].getOrthogonalValue()) || dpha[i].isOutside()) {
					continue;
				}
				laDataPoint = bRendering3D ? srh3d.getLabelAttributes(ls) : srh.getLabelAttributes(ls);
				laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());

				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						laDataPoint);

				if (laDataPoint.isVisible()) {
					if (bRendering3D) {
						switch (pDataPoint.getValue()) {
						case Position.ABOVE:
							loDataPoint3d.set(faX[i], faY[i] + dSize + p.getVerticalSpacing(), faZ[i] + 1);
							break;
						case Position.BELOW:
							loDataPoint3d.set(faX[i], faY[i] - dSize - p.getVerticalSpacing(), faZ[i] + 1);
							break;
						case Position.LEFT:
							loDataPoint3d.set(faX[i] - dSize - p.getHorizontalSpacing(), faY[i], faZ[i] + 1);
							break;
						case Position.RIGHT:
							loDataPoint3d.set(faX[i] + dSize + p.getHorizontalSpacing(), faY[i], faZ[i] + 1);
							break;
						default:
							throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
									"exception.illegal.datapoint.position.line", //$NON-NLS-1$
									new Object[] { pDataPoint.getName() },
									Messages.getResourceBundle(getRunTimeContext().getULocale()));
						}

						final Text3DRenderEvent tre3d = ((EventObjectCache) ipr).getEventObject(
								WrappedStructureSource.createSeriesDataPoint(ls, dpha[i]), Text3DRenderEvent.class);
						tre3d.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						tre3d.setLabel(laDataPoint);
						tre3d.setTextPosition(Methods.getLabelPosition(pDataPoint));
						tre3d.setLocation3D(loDataPoint3d);

						getDeferredCache().addLabel(tre3d);
					} else {
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
									"exception.illegal.datapoint.position.line", //$NON-NLS-1$
									new Object[] { pDataPoint.getName() },
									Messages.getResourceBundle(getRunTimeContext().getULocale()));
						}
						renderLabel(WrappedStructureSource.createSeriesDataPoint(ls, dpha[i]),
								TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, pDataPoint, loDataPoint, null);
					}
				}

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
						laDataPoint);
			}
		}

		// Render the fitting curve.
		if (!bRendering3D && getSeries().getCurveFitting() != null) {
			Location[] larray = new Location[faX.length];
			for (int i = 0; i < larray.length; i++) {
				larray[i] = goFactory.createLocation(faX[i], faY[i]);
			}
			larray = filterNull(larray, isrh.getDataPoints());
			renderFittingCurve(ipr, larray, getSeries().getCurveFitting(), false, true);
		}

		if (!bRendering3D) {
			restoreClipping(ipr);
		}
	}

	/**
	 * Check if to show as tape.
	 * 
	 * @return
	 */
	protected boolean validateShowAsTape() {
		ChartWithAxes cwa = (ChartWithAxes) getModel();
		LineSeries ls = (LineSeries) getSeries();

		if (!ls.isStacked()) // NOT STACKED
		{
			if (getSeriesCount() > 2 && !isDimension3D()) // (2 = BASE + 1
			// LINE SERIES);
			// OVERLAY OF MULTIPLE SERIES COULD
			// CAUSE TAPE INTERSECTIONS
			{
				return false;
			}
		} else {
			final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(cwa.getBaseAxes()[0], true);
			if (axaOrthogonal.length > 1) // MULTIPLE Y-AXES CAN'T SHOW
			// TAPES DUE TO POSSIBLE TAPE
			// INTERSECTIONS
			{
				return false;
			} else {
				if (getSeriesCount() > 2 && !isDimension3D()) // (2 = BASE
				// + 1 LINE
				// SERIES);
				// OVERLAY OF MULTIPLE
				// 'STACKED' SERIES COULD ALSO
				// CAUSE TAPE INTERSECTIONS
				{
					return false;
				}
			}
		}

		return true;
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
	public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final ClientArea ca = lg.getClientArea();
		final LineAttributes lia = ca.getOutline();
		final LineSeries ls = (LineSeries) getSeries();
		if (fPaletteEntry == null) // TEMPORARY PATCH: WILL BE REMOVED SOON
		{
			fPaletteEntry = goFactory.RED();
		}

		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				RectangleRenderEvent.class);
		rre.setBackground(ca.getBackground());
		rre.setOutline(lia);
		rre.setBounds(bo);
		ipr.fillRectangle(rre);

		LineAttributes liaMarker = ls.getLineAttributes();
		if (liaMarker.isVisible()) {
			final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
					LineRenderEvent.class);

			if (ls.isPaletteLineColor()) {
				liaMarker = goFactory.copyOf(liaMarker);
				liaMarker.setColor(FillUtil.getColor(fPaletteEntry));
			}

			lre.setLineAttributes(liaMarker);
			lre.setStart(goFactory.createLocation(bo.getLeft() + 1, bo.getTop() + bo.getHeight() / 2));
			lre.setEnd(goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1, bo.getTop() + bo.getHeight() / 2));
			ipr.drawLine(lre);
		}

		SeriesDefinition sd = getSeriesDefinition();

		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && ls.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) ls.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(ls);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { ls, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		Marker m = null;
		if (ls.getMarkers().size() > 0) {
			m = ls.getMarkers().get(iThisSeriesIndex % ls.getMarkers().size());
		}

		double width = bo.getWidth() / getDeviceScale();
		double height = bo.getHeight() / getDeviceScale();
		int markerSize = (int) (((width > height ? height : width) - 2) / 2);
		if (markerSize <= 0) {
			markerSize = 1;
		}

		if (m != null) {
			DataPointHints dph = createDummyDataPointHintsForLegendItem();
			renderMarker(lg, ipr, m,
					goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight() / 2),
					ls.getLineAttributes(), fPaletteEntry, dph, Integer.valueOf(markerSize), false, false);
		}
	}

	/**
	 * render series as curve.
	 */
	protected void renderAsCurve(IPrimitiveRenderer ipr, LineAttributes lia, ISeriesRenderingHints srh, Location[] loa,
			boolean bShowAsTape, double tapeWidth, Fill paletteEntry, boolean usePaletteLineColor)
			throws ChartException {
		DataPointHints[] dpha = srh.getDataPoints();
		boolean bStacked = getSeries().isStacked() || getAxis().isPercent();
		LineSeries ls = (LineSeries) getSeries();
		DataPointsSeeker dpSeeker = DataPointsSeeker.create(dpha, ls, bStacked);
		List<Location> list = new LinkedList<Location>();

		while (dpSeeker.next()) {
			list.add(loa[dpSeeker.getIndex()]);
		}

		Location[] newLoa = isDimension3D() ? list.toArray(new Location3D[list.size()])
				: list.toArray(new Location[list.size()]);
		final CurveRenderer cr = new CurveRenderer(((ChartWithAxes) getModel()), this, lia, newLoa, bShowAsTape,
				tapeWidth, true, !isDimension3D(), paletteEntry, usePaletteLineColor,
				((LineSeries) this.getSeries()).isConnectMissingValue());
		cr.draw(ipr);
	}

	/**
	 * render the shadow as curve.
	 */
	protected void renderShadowAsCurve(IPrimitiveRenderer ipr, LineAttributes lia, ISeriesRenderingHints srh,
			Location[] loa, boolean bShowAsTape, double tapeWidth) throws ChartException {
		final ColorDefinition cLineShadow = ((LineSeries) getSeries()).getShadowColor();

		if (!bShowAsTape && cLineShadow != null
				&& cLineShadow.getTransparency() != goFactory.TRANSPARENT().getTransparency() && lia.isVisible()) {
			final Location positionDelta = (((ChartWithAxes) getModel()).isTransposed())
					? goFactory.createLocation(-2 * getDeviceScale(), 0)
					: goFactory.createLocation(0, 2 * getDeviceScale());

			double[] shX = new double[loa.length];
			double[] shY = new double[loa.length];
			for (int i = 0; i < loa.length; i++) {
				shX[i] = loa[i].getX() + positionDelta.getX();
				shY[i] = loa[i].getY() + positionDelta.getY();
			}

			LineAttributes liaShadow = goFactory.copyOf(lia);
			liaShadow.setColor(cLineShadow);

			renderAsCurve(ipr, liaShadow, srh, goFactory.createLocations(shX, shY), bShowAsTape, tapeWidth,
					liaShadow.getColor(), false);
		}
	}

	/**
	 * @param ipr
	 * @param p
	 * @param srh
	 * @param dpha
	 * @param lia
	 * @param faX
	 * @param faY
	 * @param bShowAsTape
	 * @param paletteEntry
	 * @throws ChartException
	 */
	protected void renderDataPoints(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints srh, DataPointHints[] dpha,
			LineAttributes lia, Location[] loa, boolean bShowAsTape, double dTapeWidth, Fill paletteEntry,
			boolean usePaletteLineColor) throws ChartException {
		if (!lia.isVisible()) {
			return;
		}

		Context context = new Context(this, ipr, srh, dpha, paletteEntry);

		DataPointsRenderer dpRenderer = isDimension3D() ? new LineDataPointsRenderer3D(context, loa, dTapeWidth)
				: new LineDataPointsRenderer2D(context, loa);

		dpRenderer.render();
	}

	/**
	 * Render series shadow if applicable.
	 * 
	 * @param ipr
	 * @param p
	 * @param lia
	 * @param faX
	 * @param faY
	 * @param bShowAsTape
	 * @param dpha
	 */
	protected void renderShadow(IPrimitiveRenderer ipr, Plot p, LineAttributes lia, Location[] loa, boolean bShowAsTape,
			DataPointHints[] dpha) throws ChartException {
		final ColorDefinition cLineShadow = ((LineSeries) getSeries()).getShadowColor();

		if (!bShowAsTape && cLineShadow != null
				&& cLineShadow.getTransparency() != goFactory.TRANSPARENT().getTransparency() && lia.isVisible()) {
			Context context = new Context(this, ipr, null, dpha, null);
			LineDataPointsRenderer2DShadow dpRenderer = new LineDataPointsRenderer2DShadow(context, loa);
			dpRenderer.render();
		}
	}

	protected int getPreviousNonNullIndex(int currentIndex, DataPointHints[] dpha) {
		for (int i = currentIndex - 1; i >= 0; i--) {
			if (dpha[i].getOrthogonalValue() == null || isNaN(dpha[i].getOrthogonalValue())) {
				continue;
			}
			return i;
		}

		return -1;
	}

	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// NOTE: This method is not used by the Line Renderer
	}

	protected static Location3D[] createLocation3DArray(int iSize) {
		Location3D[] loa3d = new Location3D[iSize];
		for (int i = 0; i < iSize; i++) {
			loa3d[i] = goFactory.createLocation3D(0, 0, 0);
		}
		return loa3d;
	}

	protected static Location[] createLocationArray(int iSize) {
		Location[] loa = new Location[iSize];
		for (int i = 0; i < iSize; i++) {
			loa[i] = goFactory.createLocation(0, 0);
		}

		return loa;
	}

	/**
	 * DataPointsSeeker, the data points will be accessed one by one.
	 */
	protected static class DataPointsSeeker {

		protected final DataPointHints[] dpha;
		protected final int iSize;
		protected int index;

		protected DataPointsSeeker(DataPointHints[] dpha) {
			this.dpha = dpha;
			if (dpha != null) {
				iSize = dpha.length;
			} else {
				iSize = 0;
			}

			reset();
		}

		public void reset() {
			index = -1;
		}

		/**
		 * Seeks to the next datapoint.
		 * 
		 * @return True if the seeking succeeds.
		 */
		public boolean next() {
			if (index + 1 < iSize) {
				index++;
				return true;
			}
			return false;
		}

		public int getIndex() {
			return index;
		}

		public int getPrevIndex() {
			return index - 1;
		}

		public boolean isSingle() {
			return false;
		}

		public DataPointHints getDataPointHints(int index) {
			return dpha[index];
		}

		public DataPointHints getDataPointHints() {
			return dpha[index];
		}

		/**
		 * Returns the count of all datapoints.
		 * 
		 * @return The count of all datapoints.
		 */
		public int size() {
			return iSize;
		}

		protected boolean isNull(int index) {
			return isNaN(dpha[index].getOrthogonalValue());
		}

		public boolean isNull() {
			return isNull(index);
		}

		public static DataPointsSeeker create(DataPointHints[] dpha, LineSeries ls, boolean bStacked) {
			if (ls instanceof AreaSeries) {
				if (bStacked) {
					return new DataPointsSeeker(dpha);
				} else if (ls.isConnectMissingValue()) {
					return new DataPointsSeekerConnectNull(dpha);
				} else {
					return new DataPointsSeekerTrimmed(dpha);
				}
			}

			if (ls.isConnectMissingValue()) {
				return new DataPointsSeekerConnectNull(dpha);
			} else {
				return new DataPointsSeekerSkipNullDS(dpha);
			}
		}
	}

	/**
	 * This implement of DataPointsSeeker will seek the data points one by one, but
	 * the nulls at the start and end will be trimmed.
	 */
	protected static class DataPointsSeekerTrimmed extends DataPointsSeeker {

		protected final int findex;
		protected final int lindex;

		protected DataPointsSeekerTrimmed(DataPointHints[] dpha) {
			super(dpha);
			findex = initFirstIndex();
			lindex = initLastIndex(findex);
			index = findex - 1;
		}

		@Override
		public boolean next() {
			if (index + 1 <= lindex) {
				index++;
				return true;
			}
			return false;
		}

		protected int initFirstIndex() {
			for (int i = 0; i < iSize; i++) {
				if (!isNull(i)) {
					return i;
				}
			}

			return -1;
		}

		protected int initLastIndex(int findex) {
			if (findex >= 0) {
				for (int i = iSize - 1; i > -1; i--) {
					if (!isNull(i)) {
						return i;
					}
				}
			}
			return -2;
		}

		@Override
		public void reset() {
			index = -1;
		}

	}

	/**
	 * DataPointsSeeker implement for case of connecting missing value.
	 * DataPointsSeekerConnectNull
	 */
	protected static class DataPointsSeekerConnectNull extends DataPointsSeeker {

		private int idPrevNonNull;

		DataPointsSeekerConnectNull(DataPointHints[] dpha) {
			super(dpha);
		}

		@Override
		public void reset() {
			index = -1;
			idPrevNonNull = -1;
		}

		/**
		 * Seeks to the next non-null datapoint.
		 * 
		 * @return true if success.
		 */
		@Override
		public boolean next() {
			for (int newIndex = index + 1; newIndex < dpha.length; newIndex++) {
				if (!isNull(newIndex)) {
					idPrevNonNull = index;
					index = newIndex;
					return true;
				}
			}
			return false;
		}

		@Override
		public int getPrevIndex() {
			return idPrevNonNull;
		}

		@Override
		public boolean isNull() {
			return false;
		}

	}

	/**
	 * DataPointsSeekerSkipNullDS is an implement of DataPointsSeeker for case of
	 * not connecting missing value, but null value will also be skipped, and single
	 * points will also be detected.
	 */
	protected static class DataPointsSeekerSkipNullDS extends DataPointsSeeker {

		private boolean bNextIsNull;
		private boolean bIsNull;
		private boolean bPrevIsNull;

		DataPointsSeekerSkipNullDS(DataPointHints[] dpha) {
			super(dpha);
		}

		@Override
		public void reset() {
			index = -2;
			bNextIsNull = true;
			bIsNull = true;
		}

		/**
		 * Seeks to the next non-null datapoint.
		 * 
		 * @return true if success.
		 */
		public boolean next() {
			boolean bPrevIsNull;
			boolean bIsNull = this.bIsNull;
			boolean bNextIsNull = this.bNextIsNull;

			for (int newIndex = index + 1; newIndex < dpha.length; newIndex++) {
				bPrevIsNull = bIsNull;
				bIsNull = bNextIsNull;
				bNextIsNull = newIndex + 1 < dpha.length ? isNull(newIndex + 1) : true;
				if (!bIsNull) {
					this.bPrevIsNull = bPrevIsNull;
					this.bIsNull = bIsNull;
					this.bNextIsNull = bNextIsNull;
					index = newIndex;
					return true;
				}

			}
			return false;
		}

		@Override
		public int getPrevIndex() {
			return bPrevIsNull ? -1 : index - 1;
		}

		@Override
		public boolean isSingle() {
			return !bIsNull && bNextIsNull && bPrevIsNull;
		}

		@Override
		public boolean isNull() {
			return false;
		}

	}

	protected static enum Transposition {
		TRANSPOSED {

			@Override
			public double getX(Location lo) {
				return lo.getY();
			}

			@Override
			public double getY(Location lo) {
				return lo.getX();
			}

			@Override
			public void setX(Location lo, double value) {
				lo.setY(value);
			}

			@Override
			public void setY(Location lo, double value) {
				lo.setX(value);
			}

			@Override
			public void set(Location lo, double x, double y) {
				lo.set(y, x);
			}

		},
		NOT_TRANSPOSED {

			@Override
			public double getX(Location lo) {
				return lo.getX();
			}

			@Override
			public double getY(Location lo) {
				return lo.getY();
			}

			@Override
			public void setX(Location lo, double value) {
				lo.setX(value);
			}

			@Override
			public void setY(Location lo, double value) {
				lo.setY(value);
			}

			@Override
			public void set(Location lo, double x, double y) {
				lo.set(x, y);
			}

		};

		public abstract double getX(Location lo);

		public abstract double getY(Location lo);

		public abstract void setX(Location lo, double value);

		public abstract void setY(Location lo, double value);

		public abstract void set(Location lo, double x, double y);
	}

	/**
	 * DataPointsRenderer is used to render the data points of a line series.
	 */
	protected abstract static class DataPointsRenderer {

		/**
		 * Immutable class to hold common info to instantiate a DataPointsRenderer.
		 */
		public static class Context {

			protected final Line line;
			protected final IPrimitiveRenderer ipr;
			protected final ISeriesRenderingHints isrh;
			protected final DataPointHints[] dpha;
			protected final Fill paletteEntry;
			protected final boolean bStacked;

			public Context(Line line, IPrimitiveRenderer ipr, ISeriesRenderingHints isrh, DataPointHints[] dpha,
					Fill paletteEntry) {
				this.line = line;
				this.ipr = ipr;
				this.isrh = isrh;
				this.dpha = dpha;
				this.paletteEntry = paletteEntry;
				this.bStacked = line.getSeries().isStacked() || line.getAxis().isPercent();
			}
		}

		protected final Context context;
		protected final ChartWithAxes cwa;
		protected final DeferredCache dc;
		protected final LineSeries ls;
		protected final LineAttributes lia;
		protected final EventObjectCache eventObjCache;

		protected DataPointsRenderer(Context context) throws ChartException {
			this.context = context;
			this.dc = context.line.getDeferredCache();
			this.ls = (LineSeries) context.line.getSeries();
			this.cwa = (ChartWithAxes) context.line.getModel();

			if (ls.isPaletteLineColor()) {
				this.lia = ls.getLineAttributes().copyInstance();
				lia.setColor(FillUtil.getColor(context.paletteEntry));
			} else {
				this.lia = ls.getLineAttributes();
			}
			this.eventObjCache = (EventObjectCache) context.ipr;
		}

		/**
		 * Renders all data points.
		 */
		public void render() throws ChartException {
			DataPointsSeeker seeker = DataPointsSeeker.create(context.dpha, ls, context.bStacked);

			if (seeker.next()) {
				beforeLoop(seeker);
				while (seeker.next()) {
					processDataPoint(seeker);
				}
				afterLoop(seeker);
			}
		}

		/**
		 * Creates source object for the data point with a certain index.
		 */
		protected StructureSource createDataPointSource(int index) {
			return WrappedStructureSource.createSeriesDataPoint(ls, context.dpha[index]);
		}

		/**
		 * Creates source object for the whole series.
		 */
		protected StructureSource createSeriesSource() {
			return StructureSource.createSeries(ls);
		}

		protected void addInteractivity(DataPointHints dph, PrimitiveRenderEvent event) throws ChartException {
			context.line.addInteractivity(context.ipr, dph, event);
		}

		/**
		 * A part of the rendering process, and will be invoked before the loop though
		 * all data points.
		 */
		protected abstract void beforeLoop(DataPointsSeeker seeker) throws ChartException;

		/**
		 * A part of the rendering process, and will be invoked for each data point.
		 */
		protected abstract void processDataPoint(DataPointsSeeker seeker) throws ChartException;

		/**
		 * A part of the rendering process, and will be invoked before the loop though
		 * all data points.
		 */
		protected abstract void afterLoop(DataPointsSeeker seeker) throws ChartException;

	}

	protected static class LineDataPointsRenderer2D extends DataPointsRenderer {

		protected final Location[] loa;
		protected final LineRenderEvent lre;
		protected final Bounds boSingle = goFactory.createBounds(0, 0, 0, 0);

		protected LineDataPointsRenderer2D(Context context, Location[] loa) throws ChartException {
			super(context);
			this.loa = loa;
			this.lre = eventObjCache.getEventObject(createSeriesSource(), LineRenderEvent.class);
			lre.setLineAttributes(lia);
		}

		protected void drawSinglePoint(DataPointsSeeker seeker) throws ChartException {
			if (seeker.isSingle()) {
				int index = seeker.getIndex();
				double iSize = lia.getThickness() * 2.0;
				final OvalRenderEvent ore = eventObjCache.getEventObject(createDataPointSource(index),
						OvalRenderEvent.class);
				boSingle.set(loa[index].getX() - iSize / 2, loa[index].getY() - iSize / 2, iSize, iSize);
				ore.setBounds(boSingle);
				ore.setOutline(lia);
				context.ipr.drawOval(ore);
				addInteractivity(seeker.getDataPointHints(), ore);
			}
		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) throws ChartException {
			// do nothing
		}

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) throws ChartException {
			drawSinglePoint(seeker);
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) throws ChartException {
			drawSinglePoint(seeker);
			int pindex = seeker.getPrevIndex();

			if (pindex > -1) {
				int index = seeker.getIndex();
				DataPointHints dph = seeker.getDataPointHints();

				lre.setStart(loa[pindex]);
				lre.setEnd(loa[index]);
				addInteractivity(dph, lre);
				dc.addLine(lre);
			}
		}

	}

	protected static class LineDataPointsRenderer2DShadow extends LineDataPointsRenderer2D {

		private final LineAttributes liaShadow;
		private static final double deltaY = 3;
		private final Location loStart = goFactory.createLocation(0, 0);
		private final Location loEnd = goFactory.createLocation(0, 0);

		protected LineDataPointsRenderer2DShadow(Context context, Location[] loa) throws ChartException {
			super(context, loa);
			ColorDefinition cLineShadow = ls.getShadowColor();
			this.liaShadow = lia.copyInstance();
			liaShadow.setColor(cLineShadow);
			lre.setLineAttributes(liaShadow);
			lre.setStart(loStart);
			lre.setEnd(loEnd);
		}

		@Override
		protected void drawSinglePoint(DataPointsSeeker seeker) throws ChartException {
			if (seeker.isSingle()) {
				double iSize = lia.getThickness();
				int index = seeker.getIndex();

				final OvalRenderEvent ore = eventObjCache.getEventObject(createDataPointSource(index),
						OvalRenderEvent.class);
				ore.setOutline(liaShadow);
				ore.setBackground(liaShadow.getColor());
				boSingle.set(loa[index].getX(), loa[index].getY() + deltaY, iSize, iSize);
				ore.setBounds(boSingle);
				context.ipr.drawOval(ore);
				context.ipr.fillOval(ore);
			}
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) throws ChartException {
			drawSinglePoint(seeker);
			int pindex = seeker.getPrevIndex();

			if (pindex > -1) {
				int index = seeker.getIndex();
				DataPointHints dph = seeker.getDataPointHints();
				loStart.set(loa[pindex].getX(), loa[pindex].getY() + deltaY);
				loEnd.set(loa[index].getX(), loa[index].getY() + deltaY);
				addInteractivity(dph, lre);
				dc.addLine(lre);
			}
		}

	}

	protected static class LineDataPointsRenderer3D extends DataPointsRenderer {

		private final Location3D[] loa3d;
		private final Location3D[] loaPlane3d = createLocation3DArray(4);
		private final Line3DRenderEvent lre3d;
		private final Polygon3DRenderEvent pre3d;
		private final double dTapeWidth;

		LineDataPointsRenderer3D(Context context, Location[] loa, double dTapeWidth) throws ChartException {
			super(context);
			this.loa3d = (Location3D[]) loa;

			Object sourceObj = createSeriesSource();
			this.lre3d = eventObjCache.getEventObject(sourceObj, Line3DRenderEvent.class);
			lre3d.setLineAttributes(lia);

			this.pre3d = eventObjCache.getEventObject(sourceObj, Polygon3DRenderEvent.class);
			pre3d.setDoubleSided(true);
			pre3d.setOutline(null);
			pre3d.setBackground(goFactory.brighter(lia.getColor()));

			this.dTapeWidth = dTapeWidth;
		}

		private void drawSinglePoint(DataPointsSeeker seeker) throws ChartException {
			if (seeker.isSingle()) {
				int index = seeker.getIndex();
				Line3DRenderEvent lre3dValue = eventObjCache.getEventObject(createDataPointSource(index),
						Line3DRenderEvent.class);
				loaPlane3d[0] = goFactory.createLocation3D(loa3d[index].getX(), loa3d[index].getY(),
						loa3d[index].getZ());
				loaPlane3d[1] = goFactory.createLocation3D(loa3d[index].getX(), loa3d[index].getY(),
						loa3d[index].getZ() - dTapeWidth);
				lre3dValue.setStart3D(loaPlane3d[0]);
				lre3dValue.setEnd3D(loaPlane3d[1]);
				lre3dValue.setLineAttributes(lia);

				dc.addLine(lre3dValue);
				addInteractivity(seeker.getDataPointHints(), lre3dValue);
			}
		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) throws ChartException {
			// do nothing
		}

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) throws ChartException {
			drawSinglePoint(seeker);
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) throws ChartException {
			drawSinglePoint(seeker);

			int pindex = seeker.getPrevIndex();
			if (pindex > -1) {
				int index = seeker.getIndex();
				DataPointHints dph = seeker.getDataPointHints();

				lre3d.setStart3D(loa3d[pindex]);
				lre3d.setEnd3D(loa3d[index]);
				addInteractivity(dph, lre3d);
				dc.addLine(lre3d);

				loaPlane3d[0].set(loa3d[pindex].getX(), loa3d[pindex].getY(), loa3d[pindex].getZ());
				loaPlane3d[1].set(loa3d[index].getX(), loa3d[index].getY(), loa3d[index].getZ());
				loaPlane3d[2].set(loa3d[index].getX(), loa3d[index].getY(), loa3d[index].getZ() - dTapeWidth);
				loaPlane3d[3].set(loa3d[pindex].getX(), loa3d[pindex].getY(), loa3d[pindex].getZ() - dTapeWidth);
				pre3d.setPoints3D(loaPlane3d);

				addInteractivity(dph, pre3d);
				dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);
			}

		}

	}

	@Override
	protected void flushClipping() throws ChartException {
		// Do not clip markers
		getDeferredCacheManager()
				.flushOptions(DeferredCache.FLUSH_LINE | DeferredCache.FLUSH_PLANE | DeferredCache.FLUSH_PLANE_SHADOW);
	}

	/**
	 * 
	 * @param ipr
	 * @param loa3d
	 * @param dpha
	 * @throws ChartException
	 */
	private void addComparsionPolygon(IPrimitiveRenderer ipr, Location3D[] loa3d, DataPointHints[] dpha)
			throws ChartException {
		// Create location 3d of a plane.
		Location3D[] l3d = createLocation3DArray(4);
		double x0 = loa3d[0].getX();
		double x1 = loa3d[loa3d.length - 1].getX();
		double minY = loa3d[0].getY();
		double maxY = loa3d[0].getY();
		double maxZ = loa3d[0].getZ();
		for (int i = 0; i < loa3d.length; i++) {
			if (minY > loa3d[i].getY())
				minY = loa3d[i].getY();
			if (maxY < loa3d[i].getY())
				maxY = loa3d[i].getY();
			if (maxZ < loa3d[i].getZ())
				maxZ = loa3d[i].getZ();
		}
		l3d[0].set(x0, maxY, maxZ);
		l3d[1].set(x1, maxY, maxZ);
		l3d[2].set(x1, minY, maxZ);
		l3d[3].set(x0, minY, maxZ);

		Object sourceObj = WrappedStructureSource.createSeriesDataPoint(getSeries(), dpha[0]);
		Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr).getEventObject(sourceObj, Polygon3DRenderEvent.class);

		pre3d.setEnable(false);
		pre3d.setDoubleSided(true);
		pre3d.setOutline(null);
		pre3d.setPoints3D(l3d);
		pre3d.setSourceObject(sourceObj);
		Object event = dc.getParentDeferredCache().addPlane(pre3d, PrimitiveRenderEvent.FILL);
		if (event instanceof WrappedInstruction) {
			((WrappedInstruction) event).setSubDeferredCache(dc);
		}
		// Restore the default value.
		pre3d.setDoubleSided(false);
		pre3d.setEnable(true);
	}

	@Override
	public void set(DeferredCache _dc) {
		super.set(_dc);
		// In order to simplify the polygon rendering order computation, here
		// creates a sub deferred cache instead of default deferred
		// cache to stores all shape events of current series for 3D chart, just
		// a polygon of current series is stored in default deferred cache to
		// take part in the computation of series rendering order.
		if (isDimension3D()) {
			subDeferredCache = dc.deriveNewDeferredCache();
			dc = subDeferredCache;
			// Line 3D chart needs antialiasing.
			subDeferredCache.setAntialiasing(true);
		}
	}
}