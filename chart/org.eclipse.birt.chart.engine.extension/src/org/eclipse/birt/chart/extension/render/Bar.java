/***********************************************************************
 * Copyright (c) 2004, 2005, 2008 Actuate Corporation.
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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AxisSubUnit;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints3D;
import org.eclipse.birt.chart.computation.withaxes.StackGroup;
import org.eclipse.birt.chart.computation.withaxes.StackedSeriesLookup;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Responsible for rendering the graphic elements associated with a bar graph.
 * It is also responsible for rendering the graphic element in the legend for
 * the associated series entry.
 */
public final class Bar extends AxesRenderer {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	/**
	 * Min height of Bar, especially for zero value.
	 */
	private static final int MIN_HEIGHT = 10;

	DeferredCache subDeferredCache = null;

	/**
	 * The default zero-arg constructor (required for initialization via the
	 * extension framework)
	 */
	public Bar() {
		super();
	}

	/**
	 * Computes the start and end values for stacked bars
	 */
	protected double[] computeStackPosition(AxisSubUnit au, double dValue, Axis ax) {
		if (ax.isPercent()) {
			dValue = au.valuePercentage(dValue);
		}
		double dStart = au.getStackedValue(dValue);
		double dEnd = au.stackValue(dValue);
		return new double[] { dStart, dEnd };
	}

	private static class BarAxisIdLookup {

		private final int barAxisId;
		private final int barAxisCount;

		public BarAxisIdLookup(ChartWithAxes cwa, Axis curAxis) {
			Axis[] axs = cwa.getOrthogonalAxes(cwa.getBaseAxes()[0], true);
			int count = 0, id = 0;
			for (Axis axis : axs) {
				if (axis.isSideBySide() && axis.getSeriesDefinitions().size() > 0) {
					SeriesDefinition sd = axis.getSeriesDefinitions().get(0);
					if (sd.getDesignTimeSeries() instanceof BarSeries) {
						if (curAxis == axis) {
							id = count;
						}
						count++;
					}
				}
			}

			if (count > 1 && curAxis.isSideBySide()) {
				barAxisId = id;
				barAxisCount = count;
			} else {
				barAxisId = 0;
				barAxisCount = 1;
			}
		}

		public int getCount() {
			return barAxisCount;
		}

		public int getId() {
			return barAxisId;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.
	 * chart.output.IRenderer, Chart.Plot,
	 * org.eclipse.birt.chart.render.axes.SeriesRenderingHints)
	 */
	@Override
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, vex);
		}

		boolean bRendering3D = isDimension3D();
		boolean hasAddedComparsionPolygon = false;

		// SCALE VALIDATION
		SeriesRenderingHints srh = null;
		SeriesRenderingHints3D srh3d = null;

		if (bRendering3D) {
			srh3d = (SeriesRenderingHints3D) isrh;
		} else {
			srh = (SeriesRenderingHints) isrh;
		}

		// CANNOT PLOT BARS ON A VALUE X-SCALE or Z-SCALE
		// if ( ( !bRendering3D && !srh.isCategoryScale( ) )
		// || ( bRendering3D && !srh3d.isXCategoryScale( ) ) )
		// {
		// throw new ChartException( ChartEngineExtensionPlugin.ID,
		// ChartException.RENDERING,
		// "exception.xvalue.scale.bars", //$NON-NLS-1$
		// Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		// }

		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		final Bounds boClientArea = isrh.getClientAreaBounds(true);
		final Bounds boClientAreaWithoutInsets = isrh.getClientAreaBounds(false);

		final AbstractScriptHandler sh = getRunTimeContext().getScriptHandler();
		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale()));

		// OBTAIN AN INSTANCE OF THE SERIES MODEL (AND SOME VALIDATION)
		final BarSeries bs = (BarSeries) getSeries();
		if (!bs.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// SETUP VARS USED IN RENDERING
		final RiserType rt = bs.getRiser();
		final double dSeriesThickness = bRendering3D ? 0 : srh.getSeriesThickness();
		// always be Zero for 3D rendering.
		final double dZeroLocation = bRendering3D ? srh3d.getPlotZeroLocation() : srh.getZeroLocation();
		// NOT TO BE
		// USED
		// FOR STACKED
		// CHARTS
		double dBaseLocation = -1;
		final DataPointHints[] dpha = isrh.getDataPoints();
		validateNullDatapoint(dpha);

		double sizeForNonCategory = -1;
		if (!bRendering3D && !((SeriesRenderingHints) isrh).isCategoryScale() && dpha.length != 0) {
			sizeForNonCategory = computeSizeForNonCategoryBar(cwa.isTransposed(), dpha);
		}

		final ColorDefinition cd = bs.getRiserOutline();
		final LineAttributes lia = goFactory.createLineAttributes(cd == null ? null : goFactory.copyOf(cd),
				LineStyle.SOLID_LITERAL, 1);
		double dX = 0, dY = 0, dZ = 0;
		double dWidth = 0, dHeight = 0, dSpacing = 0, dValue = 0;
		double dWidthZ = 0, dSpacingZ = 0;
		Location lo;
		Location3D lo3d;
		Location[] loaFrontFace = null;
		List<Location3D[]> loa3dFace = null;
		boolean bInverted = false;
		final double dUnitSpacing = (!cwa.isSetUnitSpacing()) ? 50 : cwa.getUnitSpacing(); // AS A PERCENTAGE OF ONE

		// Clipping area is not the same with client area.
		final Bounds clipArea = goFactory.copyOf(boClientAreaWithoutInsets);
		if (cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
			boClientArea.delta(-dSeriesThickness, dSeriesThickness, 0, 0);
			clipArea.delta(-dSeriesThickness, 0, 2 * dSeriesThickness, dSeriesThickness);
		}
		// Clip Plot area if needed. Bar renderer won't invoke
		// handleOutsideDataPoints method like other renderers to clip all data
		// points as a whole. On the contrary, clip every data point one by one.
		// Hence, invoke this method to clip directly.
		renderClipping(ipr, clipArea);

		// STRUCTURES NEEDED TO RENDER STACKED BARS
		AxisSubUnit au = null;
		Axis ax = getAxis();

		StackedSeriesLookup ssl = null;
		StackGroup sg = null;

		if (!bRendering3D) {
			ssl = srh.getStackedSeriesLookup();
			sg = ssl.getStackGroup(bs);
		}
		int iSharedUnitIndex = (sg == null) ? 0 : sg.getSharedIndex();
		int iSharedUnitCount = (sg == null) ? 1 : sg.getSharedCount();

		double dStart, dEnd;

		// DATA POINT RELATED VARIABLES ARE INITIALIZED HERE
		Label laDataPoint = null;
		try {
			laDataPoint = bRendering3D ? srh3d.getLabelAttributes(bs) : srh.getLabelAttributes(bs);
		} catch (Exception ex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
		}

		// SETUP THE RISER FILL COLOR FROM THE SERIES DEFINITION PALETTE
		// (BY CATEGORIES OR BY SERIES)
		final SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { bs }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}
		final boolean bPaletteByCategory = (cwa.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);
		int iThisSeriesIndex = -1;

		if (!bPaletteByCategory) {
			iThisSeriesIndex = sd.getRunTimeSeries().indexOf(bs);
			if (iThisSeriesIndex < 0) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
						"exception.missing.series.for.palette.index", //$NON-NLS-1$
						new Object[] { bs, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
			}
		}

		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];

		boolean bShowOutside = isShowOutside();

		// Iterate each data point of series, compute coordinates/size/bounds
		// and do renderer.
		// THE MAIN LOOP THAT WALKS THROUGH THE DATA POINT HINTS ARRAY 'dpha'
		for (int i = 0; i < dpha.length; i++) {
			// Initialize the coordinates with special value that can be handled
			// later by filterNull()
			faX[i] = Double.NaN;
			faY[i] = Double.NaN;

			// Check if category value is outside
			// TODO do we need to check orthogonal value?
			int iOutside = checkEntryByType(getInternalBaseAxis().getScale(), dpha[i].getBaseValue());
			if (iOutside != 0) {
				// Set outside label is invisible
				dpha[i].markOutside();
				continue;
			}

			laDataPoint = bRendering3D ? srh3d.getLabelAttributes(bs) : srh.getLabelAttributes(bs);

			Fill f = null;
			if (bPaletteByCategory) {
				f = FillUtil.getPaletteFill(elPalette, i);
			} else if (iThisSeriesIndex >= 0) {
				f = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			}

			updateTranslucency(f, bs);

			// Convert Fill for negative value
			Fill fixedFill;
			if (dpha[i] != null && dpha[i].getOrthogonalValue() instanceof Double) {
				fixedFill = FillUtil.convertFill(f, ((Double) dpha[i].getOrthogonalValue()).doubleValue(), null);
			} else {
				fixedFill = FillUtil.copyOf(f);
			}

			// OBTAIN THE CO-ORDINATES OF THE DATA POINT
			if (bRendering3D) {
				lo3d = dpha[i].getLocation3D();
				dX = lo3d.getX();
				dY = lo3d.getY();
				dZ = lo3d.getZ();

				// ADJUST CO-ORDINATES BASED ON EACH UNIT SHARE (DUE TO MULTIPLE
				// SERIES)
				dSpacing = ((dpha[i].getSize2D().getWidth()) * dUnitSpacing) / 200;
				dSpacingZ = ((dpha[i].getSize2D().getHeight()) * dUnitSpacing) / 200;
			} else {
				lo = dpha[i].getLocation();
				dX = lo.getX();
				dY = lo.getY();

				// ADJUST CO-ORDINATES BASED ON EACH UNIT SHARE (DUE TO MULTIPLE
				// SERIES)
				dSpacing = ((dpha[i].getSize()) * dUnitSpacing) / 200;
			}

			// BRANCH OFF HERE IF THE PLOT IS TRANSPOSED (AXES ARE SWAPPED)
			if (cwa.isTransposed()) {
				BarAxisIdLookup basLookup = new BarAxisIdLookup((ChartWithAxes) cm, this.getAxis());

				if (((SeriesRenderingHints) isrh).isCategoryScale()) {
					dHeight = dpha[i].getSize();
					dHeight -= 2 * dSpacing;

					dHeight /= basLookup.getCount();
					dY += dHeight * basLookup.getId();

					dHeight /= iSharedUnitCount;
					dY += iSharedUnitIndex * dHeight + dSpacing;
				} else {
					dHeight = Math.min(sizeForNonCategory, (dpha[i].getSize() - 2 * dSpacing) * .8);
					dSpacing = (dpha[i].getSize() - dHeight) / 2.0;
					dY -= dpha[i].getSize() * 0.5;

					dHeight /= basLookup.getCount();
					dY += dHeight * basLookup.getId();

					dHeight /= iSharedUnitCount;
					dY += iSharedUnitIndex * dHeight + dSpacing;
				}

				if (isStackedOrPercent(bs)) // SPECIAL
				// PROCESSING
				// FOR STACKED SERIES
				{
					au = ssl.getUnit(bs, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN(dpha[i].getOrthogonalValue()) ? 0
							: ((Double) dpha[i].getOrthogonalValue()).doubleValue();
					double[] values = computeStackPosition(au, dValue, ax);
					dStart = values[0];
					dEnd = values[1];

					try {
						// Bugzilla bug 182279: Calculate orthogonal X position
						// and save it as next base location. - Henry
						double dMargin = srh.getLocationOnOrthogonal(dEnd) - srh.getLocationOnOrthogonal(dStart);
						double lastPosition = au.getLastPosition(dValue);
						if (Double.isNaN(lastPosition)) {
							dBaseLocation = srh.getLocationOnOrthogonal(dStart);
						} else {
							dBaseLocation = au.getLastPosition(dValue);
						}
						au.setLastPosition(dValue, dBaseLocation, dMargin);
						dX = au.getLastPosition(dValue);
					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}
				} else if (!ChartUtil.isStudyLayout(cwa)) {
					dBaseLocation = dZeroLocation;
				} else {
					// Adjusts last position for the study layout.
					au = ssl.getUnit(bs, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = Methods.asDouble(dpha[i].getOrthogonalValue());
					try {
						// Calculate orthogonal X position
						// and save it as next base location.
						double dMargin = srh.getLocationOnOrthogonal(dpha[i].getOrthogonalValue())
								- srh.getLocationOnOrthogonal(srh.getOrthogonalScale().getMinimum());
						double lastPosition = au.getLastPosition(dValue);
						double precisionDelta = 0.00000001d;
						if (Double.isNaN(lastPosition)) {
							dBaseLocation = srh.getLocationOnOrthogonal(srh.getOrthogonalScale().getMinimum())
									- precisionDelta;
						} else {
							dBaseLocation = au.getLastPosition(dValue) - precisionDelta;
						}
						au.setLastPosition(dValue, dBaseLocation, dMargin);
					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}

					dX = au.getLastPosition(dValue);
				}

				// RANGE CHECK (WITHOUT CLIPPING)
				// =================================
				// NOTE: use a wider precision check here to fix some incorrect
				// rendering case.
				// ==================================
				if (ChartUtil.mathLT(dX, boClientArea.getLeft())) // LEFT
				// EDGE
				{
					if (ChartUtil.mathLT(dBaseLocation, boClientArea.getLeft())) {
						// BOTH ARE OUT OF RANGE
						if (!bShowOutside) {
							continue;
						}
					}
					dX = boClientArea.getLeft();
				} else if (ChartUtil.mathLT(dBaseLocation, boClientArea.getLeft())) {
					dBaseLocation = boClientArea.getLeft();
				}

				if (ChartUtil.mathGT(dX, boClientArea.getLeft() + boClientArea.getWidth())) // RIGHT
				// EDGE
				{
					if (ChartUtil.mathGT(dBaseLocation, boClientArea.getLeft() + boClientArea.getWidth())) {
						// BOTH ARE OUT OF RANGE
						continue;
					}
					dX = boClientArea.getLeft() + boClientArea.getWidth();
				} else if (ChartUtil.mathGT(dBaseLocation, boClientArea.getLeft() + boClientArea.getWidth())) {
					dBaseLocation = boClientArea.getLeft() + boClientArea.getWidth();
				}

				// HANDLE INVERTED RISER DIRECTION
				dWidth = dBaseLocation - dX;
				// <= is needed for the label to be in the right place when the
				// width is null
				// this is due to the difference in rounding (ceil vs floor) for
				// transposed axes.
				bInverted = dWidth <= 0;
				if (bInverted) {
					dX = dBaseLocation;
					dWidth = -dWidth;
				}
			} else
			// STANDARD PROCESSING FOR REGULAR NON-TRANSPOSED AXES (NOTE:
			// SYMMETRIC CODE)
			{
				if (bRendering3D) {
					dWidth = dpha[i].getSize2D().getWidth();
					dWidth -= 2 * dSpacing;

					dWidthZ = dpha[i].getSize2D().getHeight();
					dWidthZ -= 2 * dSpacingZ;

					dX += dSpacing;
					dZ += dSpacingZ;
				} else {
					BarAxisIdLookup basLookup = new BarAxisIdLookup((ChartWithAxes) cm, this.getAxis());

					if (((SeriesRenderingHints) isrh).isCategoryScale()) {
						dWidth = dpha[i].getSize();
						dWidth -= 2 * dSpacing;

						dWidth /= basLookup.getCount();
						dX += dWidth * basLookup.getId();

						dWidth /= iSharedUnitCount;
						dX += iSharedUnitIndex * dWidth + dSpacing;
					} else {
						dWidth = Math.min(sizeForNonCategory, (dpha[i].getSize() - 2 * dSpacing) * .8);
						dSpacing = (dpha[i].getSize() - dWidth) / 2.0;
						dX -= dpha[i].getSize() / 2;

						dWidth /= basLookup.getCount();
						dX += dWidth * basLookup.getId();

						dWidth /= iSharedUnitCount;
						dX += iSharedUnitIndex * dWidth + dSpacing;
					}
				}

				if (isStackedOrPercent(bs))
				// SPECIAL PROCESSING FOR STACKED OR PERCENT SERIES
				{
					if (bRendering3D) {
						// Not support stack/percent for 3D chart.
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.COMPUTATION,
								"exception.no.stack.percent.3D.chart", //$NON-NLS-1$
								Messages.getResourceBundle(getRunTimeContext().getULocale()));
					}

					au = ssl.getUnit(bs, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					dValue = isNaN(dpha[i].getOrthogonalValue()) ? 0
							: ((Number) dpha[i].getOrthogonalValue()).doubleValue();
					double[] values = computeStackPosition(au, dValue, ax);
					dStart = values[0];
					dEnd = values[1];

					try {
						// Calculate orthogonal Y position and save it as next
						// base location.
						double dMargin = srh.getLocationOnOrthogonal(dEnd) - srh.getLocationOnOrthogonal(dStart);
						double lastPosition = au.getLastPosition(dValue);
						if (Double.isNaN(lastPosition)) {
							dBaseLocation = srh.getLocationOnOrthogonal(dStart);
						} else {
							dBaseLocation = au.getLastPosition(dValue);
						}
						au.setLastPosition(dValue, dBaseLocation, dMargin);
						dY = au.getLastPosition(dValue);
					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}
				} else if (!ChartUtil.isStudyLayout(cwa)) {
					dBaseLocation = dZeroLocation;
				} else {
					// Adjusts last position for the study layout.
					au = ssl.getUnit(bs, i); // UNIT POSITIONS (MAX, MIN) FOR
					// INDEX = 'i'
					Object oValue = dpha[i].getOrthogonalValue();
					dValue = oValue == null ? 0 : Methods.asDouble(oValue);
					try {
						// Calculate orthogonal Y position and save it as next
						// base location.
						double dMargin = srh.getLocationOnOrthogonal(oValue == null ? 0 : oValue)
								- srh.getLocationOnOrthogonal(srh.getOrthogonalScale().getMinimum());
						double lastPosition = au.getLastPosition(dValue);
						if (Double.isNaN(lastPosition)) {
							dBaseLocation = srh.getLocationOnOrthogonal(srh.getOrthogonalScale().getMinimum());
						} else {
							dBaseLocation = au.getLastPosition(dValue);
						}
						au.setLastPosition(dValue, dBaseLocation, dMargin);

					} catch (Exception ex) {
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
					}
					dY = au.getLastPosition(dValue);
				}

				// Range check.
				if (bRendering3D) {
					double plotBaseLocation = srh3d.getPlotBaseLocation();

					// RANGE CHECK (WITHOUT CLIPPING)
					if (dY < plotBaseLocation) // TOP EDGE
					{
						if (bShowOutside) {
							dBaseLocation = plotBaseLocation;
							dY = plotBaseLocation;
						} else {
							if (dBaseLocation < plotBaseLocation) {
								// BOTH ARE OUT OF RANGE
								continue;
							}
							dY = plotBaseLocation; // - This causes
						}
						// clipping in output
					} else if (dBaseLocation < plotBaseLocation) {
						dBaseLocation = plotBaseLocation;
					}

					if (dY > plotBaseLocation + srh3d.getPlotHeight()) // BOTTOM
					// EDGE
					{
						if (dBaseLocation > plotBaseLocation + srh3d.getPlotHeight()) {
							// BOTH ARE OUT OF RANGE
							continue;
						}
						dY = plotBaseLocation + srh3d.getPlotHeight();
					} else if (dBaseLocation > plotBaseLocation + srh3d.getPlotHeight()) {
						dBaseLocation = plotBaseLocation + srh3d.getPlotHeight();
					}
				} else {
					// RANGE CHECK (WITHOUT CLIPPING)
					if (dY < boClientArea.getTop()) // TOP EDGE
					{
						if (dBaseLocation < boClientArea.getTop()) {
							// BOTH ARE OUT OF RANGE
							continue;
						}
						dY = boClientArea.getTop(); // - This causes
						// clipping in output
					} else if (dBaseLocation < boClientArea.getTop()) {
						dBaseLocation = boClientArea.getTop();
					}

					if (dY > boClientArea.getTop() + boClientArea.getHeight()) // BOTTOM
					// EDGE
					{
						if (dBaseLocation > boClientArea.getTop() + boClientArea.getHeight()) {
							// BOTH ARE OUT OF RANGE
							if (!bShowOutside) {
								continue;
							}
						}
						dY = boClientArea.getTop() + boClientArea.getHeight();
					} else if (dBaseLocation > boClientArea.getTop() + boClientArea.getHeight()) {
						dBaseLocation = boClientArea.getTop() + boClientArea.getHeight();
					}
				}

				// HANDLE INVERTED RISER DIRECTION
				dHeight = dBaseLocation - dY;
				// 3d true upward, 2d false upward, 0 always be treated as
				// upward
				bInverted = bRendering3D ? dHeight <= 0 : dHeight < 0;
				if (bInverted) {
					dY = dBaseLocation;
					dHeight = -dHeight;
				}
			}

			// Generate a compare bounds for 2D bar(including tube, cone,
			// triangle)chart, this compare bounds will be used instead of
			// actual bound of polygon for adjusting order of polygons.
			Bounds compareBounds = null;
			if (getModel().getDimension() == ChartDimension.TWO_DIMENSIONAL_LITERAL
					|| getModel().getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
				compareBounds = BoundsImpl.create(dX, dY, dWidth, dHeight);
			}

			// COMPUTE EACH RECTANGLE FACE
			if (rt.getValue() == RiserType.RECTANGLE) {
				if (bRendering3D) {
					loa3dFace = computeRiserRectangle3D(bInverted, dX, dY, dZ, dHeight, dWidth, dWidthZ);
				} else {
					loaFrontFace = computeRiserRectangle2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth);
				}
			}
			// COMPUTE EACH TRIANGULAR FACE
			else if (rt.getValue() == RiserType.TRIANGLE) {
				if (bRendering3D) {
					loa3dFace = computeRiserTriangle3D(bInverted, dX, dY, dZ, dHeight, dWidth, dWidthZ);
				} else // Bugzilla 187473. - Henry
				if (isStackedOrPercent(bs)) {
					StackedSizeHints slh = getCurrentStackedSizeHints(i);
					double[] size = null;
					// Compute bottom and top size for series painting.
					if (isTransposed()) {
						size = computeStacked2DTopNBottomSize(slh, au, dValue, dHeight);
					} else {
						size = computeStacked2DTopNBottomSize(slh, au, dValue, dWidth);
					}

					// Compute four locations of triangle.
					loaFrontFace = computeStackedRiserTriangle2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth,
							size[0], size[1], getCurrentStackedSizeHints(i));
				} else {
					loaFrontFace = computeRiserTriangle2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth);
				}
			}
			// COMPUTE EACH TUBE FACE
			else if (rt.getValue() == RiserType.TUBE) {
				if (bRendering3D) {
					loa3dFace = computeRiserTube3D(dX, dY, dZ, dHeight, dWidth, dWidthZ);
				} else {
					loaFrontFace = computeRiserTube2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth);
				}
			} else if (rt.getValue() == RiserType.CONE) {
				if (bRendering3D) {
					loa3dFace = computeRiserCone3D(bInverted, dX, dY, dZ, dHeight, dWidth, dWidthZ);
				} else // Bugzilla 187473. - Henry
				if (isStackedOrPercent(bs)) {
					StackedSizeHints slh = getCurrentStackedSizeHints(i);
					double[] size = null;
					// Compute bottom and top size of cone for series
					// painting.
					if (isTransposed()) {
						size = computeStacked2DTopNBottomSize(slh, au, dValue, dHeight);
					} else {
						size = computeStacked2DTopNBottomSize(slh, au, dValue, dWidth);
					}

					// Compute four locations of cone.
					loaFrontFace = computeStackedRiserCone2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth, size[0],
							size[1]);
				} else {
					loaFrontFace = computeRiserCone2D(bInverted, i, faX, faY, dX, dY, dHeight, dWidth);
				}
			} else {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
						"exception.unspecified.riser.type", //$NON-NLS-1$
						new Object[] { rt.getName() }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
			}

			// Skip rendering.
			if (isNaN(dpha[i].getOrthogonalValue())) {
				faX[i] = Double.NaN;
				faY[i] = Double.NaN;
				continue;
			}

			if (isInteractivityEnabled()) {
				// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
				final EList<Trigger> elTriggers = bs.getTriggers();
				if (!elTriggers.isEmpty()) {
					final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]);

					if (bRendering3D) {
						for (int j = 0; j < loa3dFace.size(); j++) {
							Location3D[] points = loa3dFace.get(j);
							if (points.length <= 2) {
								continue;
							}

							final InteractionEvent iev = createEvent(iSource, elTriggers, ipr);
							iev.setCursor(bs.getCursor());

							final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
									.getEventObject(StructureSource.createSeries(bs), Polygon3DRenderEvent.class);
							pre3d.setPoints3D(points);
							final Location panningOffset = getPanningOffset();

							if (get3DEngine().processEvent(pre3d, panningOffset.getX(), panningOffset.getY()) != null) {
								iev.setHotSpot(pre3d);
								ipr.enableInteraction(iev);
							}
						}
					} else {
						boolean isConeOrTriangle = (rt.getValue() == RiserType.TRIANGLE
								|| rt.getValue() == RiserType.CONE);
						final InteractionEvent iev = createEvent(iSource, elTriggers, ipr);
						iev.setCursor(bs.getCursor());

						final PolygonRenderEvent pre = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createSeries(bs), PolygonRenderEvent.class);

						Location[] hotspotLoa = new Location[loaFrontFace.length];
						for (int a = 0; a < hotspotLoa.length; a++) {
							hotspotLoa[a] = goFactory.createLocation(loaFrontFace[a].getX(), loaFrontFace[a].getY());
						}
						if (hotspotLoa.length == 4) {
							if (isTransposed()) {
								if (hotspotLoa[2].getX() - hotspotLoa[1].getX() < MIN_HEIGHT) {
									hotspotLoa[2].setX(hotspotLoa[1].getX() + MIN_HEIGHT);
									hotspotLoa[3].setX(hotspotLoa[0].getX() + MIN_HEIGHT);
								}
							} else if (isConeOrTriangle) {
								if (hotspotLoa[0].getY() - hotspotLoa[1].getY() < MIN_HEIGHT) {
									hotspotLoa[1].setY(hotspotLoa[0].getY() - MIN_HEIGHT);
									hotspotLoa[2].setY(hotspotLoa[3].getY() - MIN_HEIGHT);
								}
							} else {
								if (hotspotLoa[1].getY() - hotspotLoa[0].getY() < MIN_HEIGHT) {
									hotspotLoa[0].setY(hotspotLoa[1].getY() - MIN_HEIGHT);
									hotspotLoa[3].setY(hotspotLoa[2].getY() - MIN_HEIGHT);
								}
							}
						} else if (hotspotLoa.length == 3) {
							if (isTransposed()) {
								if (hotspotLoa[1].getX() - hotspotLoa[0].getX() < MIN_HEIGHT) {
									hotspotLoa[1].setX(hotspotLoa[0].getX() + MIN_HEIGHT);
								}
							} else if (hotspotLoa[0].getY() - hotspotLoa[1].getY() < MIN_HEIGHT) {
								hotspotLoa[1].setY(hotspotLoa[0].getY() - MIN_HEIGHT);
							}
						}
						pre.setPoints(hotspotLoa);
						iev.setHotSpot(pre);
						ipr.enableInteraction(iev);
					}
				}
			}

			// RENDER THE POLYGON (EXTRUDED IF > 2D)
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fixedFill);
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fixedFill,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT, dpha[i]);
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dpha[i]);

			if (isTransposed() ? dWidth != 0 : dHeight != 0 || bShowOutside) {
				// Do not render the bar when height is 0. Still keep the label.
				if (bRendering3D) {
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
					if (!hasAddedComparsionPolygon) {
						hasAddedComparsionPolygon = true;
						Location3D[] l3d = new Location3D[4];
						for (int k = 0; k < 4; k++) {
							l3d[k] = goFactory.createLocation3D(0, 0, 0);
						}
						double x0 = dpha[0].getLocation3D().getX();
						double x1 = dpha[dpha.length - 1].getLocation3D().getX() + dSpacing * 2;
						double z = dZ + dWidthZ;
						l3d[0].set(x0, dY, z);
						l3d[1].set(x0, dY + boClientArea.getHeight(), z);
						l3d[2].set(x1, dY + boClientArea.getHeight(), z);
						l3d[3].set(x1, dY, z);

						Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr).getEventObject(dpha[i],
								Polygon3DRenderEvent.class);
						pre3d.setEnable(false);
						pre3d.setDoubleSided(false);
						pre3d.setOutline(null);
						pre3d.setPoints3D(l3d);
						pre3d.setBackground(fixedFill);
						Object event = dc.getParentDeferredCache().addPlane(pre3d, PrimitiveRenderEvent.FILL);
						if (event instanceof WrappedInstruction) {
							((WrappedInstruction) event).setSubDeferredCache(subDeferredCache);
						}
						// Restore the default value.
						pre3d.setDoubleSided(false);
						pre3d.setEnable(true);
					}

					if (rt.getValue() == RiserType.TUBE) {
						renderRiserTube3D(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), loa3dFace,
								fixedFill, lia, dpha[i]);
					} else if (rt.getValue() == RiserType.CONE) {
						renderRiserCone3D(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), loa3dFace,
								fixedFill, lia, dpha[i]);
					} else {
						render3DPlane(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), loa3dFace,
								fixedFill, lia);
					}
				} else if (rt.getValue() == RiserType.TUBE) {
					renderRiserTube2D(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), dpha[i],
							loaFrontFace, fixedFill, lia, cwa.getDimension(),
							cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL
									? dSeriesThickness / 2
									: dSeriesThickness / 4,
							cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL, isTransposed(),
							true, bInverted, isStackedOrPercent(bs), 0, compareBounds);
				} else if (rt.getValue() == RiserType.CONE) {
					boolean isStacked = isStackedOrPercent(bs);
					double coneThickness = cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL
							? dSeriesThickness / 2
							: dSeriesThickness / 4;
					// Bugzilla 187473
					// Compute related height of bottom oval by related width.
					double coneBottomHeight = computeBottomOvalHeightOfCone(i, coneThickness, loaFrontFace, dValue,
							isStacked);

					renderRiserCone2D(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), dpha[i],
							loaFrontFace, fixedFill, lia, cwa.getDimension(), coneThickness,
							cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL, isTransposed(),
							true, bInverted, isStackedOrPercent(bs), coneBottomHeight, 0, compareBounds);
				} else if (rt.getValue() == RiserType.TRIANGLE) {
					// The method invoking is to paint fliped triangle
					// correctly.
					double[] thicknesses = computeThicknessesWithTriangle2D(loaFrontFace, dWidth, dHeight,
							dSeriesThickness);
					if (cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
						adjustLocationsWithTriangle2D(loaFrontFace, thicknesses[0], thicknesses[1], dSeriesThickness);
					}
					renderRiserTriangle2D(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), loaFrontFace,
							fixedFill, lia, cwa.getDimension(), thicknesses[0], thicknesses[1], true, 0, compareBounds);
				} else {
					renderPlane(ipr, WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]), loaFrontFace, fixedFill,
							lia, cwa.getDimension(), dSeriesThickness, true, 0, compareBounds);
				}
			}
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fixedFill);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fixedFill,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dpha[i]);

			// RENDER DATA POINTS
			// Make sure script work so value setter should be first.
			laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
					laDataPoint);

			// Computes data point info.
			Position pDataPoint = null;
			Location loDataPoint = null;
			Location3D loDataPoint3d = null;
			Bounds boDataPoint = null;
			try {
				if (laDataPoint.isVisible()) // ONLY COMPUTE IF NECESSARY
				{
					pDataPoint = bRendering3D ? srh3d.getLabelPosition(bs) : srh.getLabelPosition(bs);
					loDataPoint = goFactory.createLocation(0, 0);
					loDataPoint3d = goFactory.createLocation3D(0, 0, 0);
					boDataPoint = goFactory.createBounds(0, 0, 0, 0);
				}
			} catch (Exception ex) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, ex);
			}

			boolean zeroBarLength = ChartUtil.mathEqual(dHeight, 0);
			boolean zeroBarWidth = ChartUtil.mathEqual(dWidth, 0);

			if (cwa.isTransposed()) {
				boolean tmpBoolean = zeroBarLength;
				zeroBarLength = zeroBarWidth;
				zeroBarWidth = tmpBoolean;
			}

			if (laDataPoint.isVisible() && (!zeroBarLength || bShowOutside) && !zeroBarWidth) {
				// Only render the label that is inside
				if (!dpha[i].isOutside()) {
					if (!cwa.isTransposed()) {
						if (bRendering3D) {
							if (pDataPoint.getValue() == Position.OUTSIDE) {
								if (!bInverted) {
									loDataPoint3d.set(dX + dWidth / 2, dY - p.getVerticalSpacing(), dZ + dWidthZ / 2);

									Text3DRenderEvent tre = ((EventObjectCache) ipr).getEventObject(
											WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											Text3DRenderEvent.class);
									tre.setLabel(laDataPoint);
									tre.setTextPosition(TextRenderEvent.BELOW);
									tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);

									Location3D[] loa3d = new Location3D[5];
									loa3d[0] = loDataPoint3d;
									loa3d[1] = goFactory.createLocation3D(dX, dY - p.getVerticalSpacing(),
											dZ + dWidthZ / 2);
									loa3d[2] = goFactory.createLocation3D(dX, dY - p.getVerticalSpacing() - 16,
											dZ + dWidthZ / 2);
									loa3d[3] = goFactory.createLocation3D(dX + dWidth, dY - p.getVerticalSpacing() - 16,
											dZ + dWidthZ / 2);
									loa3d[4] = goFactory.createLocation3D(dX + dWidth, dY - p.getVerticalSpacing(),
											dZ + dWidthZ / 2);
									tre.setBlockBounds3D(loa3d);

									getDeferredCache().addLabel(tre);
								} else {
									loDataPoint3d.set(dX + dWidth / 2, dY + dHeight + p.getVerticalSpacing(),
											dZ + dWidthZ / 2);

									Text3DRenderEvent tre = ((EventObjectCache) ipr).getEventObject(
											WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											Text3DRenderEvent.class);
									tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
									tre.setLabel(laDataPoint);
									tre.setTextPosition(TextRenderEvent.ABOVE);

									Location3D[] loa3d = new Location3D[5];
									loa3d[0] = loDataPoint3d;
									loa3d[1] = goFactory.createLocation3D(dX + dWidth,
											dY + dHeight + p.getVerticalSpacing(), dZ + dWidthZ / 2);
									loa3d[2] = goFactory.createLocation3D(dX + dWidth,
											dY + dHeight + 16 + p.getVerticalSpacing(), dZ + dWidthZ / 2);
									loa3d[3] = goFactory.createLocation3D(dX,
											dY + dHeight + 16 + p.getVerticalSpacing(), dZ + dWidthZ / 2);
									loa3d[4] = goFactory.createLocation3D(dX, dY + dHeight + p.getVerticalSpacing(),
											dZ + dWidthZ / 2);
									tre.setBlockBounds3D(loa3d);

									getDeferredCache().addLabel(tre);
								}
							} else {
								throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
										"exception.illegal.datapoint.position.bar3d", //$NON-NLS-1$
										new Object[] { pDataPoint.getName() },
										Messages.getResourceBundle(getRunTimeContext().getULocale()));
							}
						} else {
							switch (pDataPoint.getValue()) {
							case Position.OUTSIDE:
								if (!bInverted) {
									loDataPoint.set(dX + dWidth / 2, dY - p.getVerticalSpacing());
									renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
											Position.ABOVE_LITERAL, loDataPoint, null);
								} else {
									loDataPoint.set(dX + dWidth / 2, dY + dHeight + p.getVerticalSpacing());
									renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
											Position.BELOW_LITERAL, loDataPoint, null);
								}
								break;
							case Position.INSIDE:
								if (rt.getValue() == RiserType.CONE || rt.getValue() == RiserType.TRIANGLE) {
									if (!bInverted) {
										loDataPoint.set(dX + dWidth / 2, dY + dHeight);
										renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
												TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
												Position.ABOVE_LITERAL, loDataPoint, null);
									} else {
										loDataPoint.set(dX + dWidth / 2, dY);
										renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
												TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
												Position.BELOW_LITERAL, loDataPoint, null);
									}
								} else {
									boDataPoint.updateFrom(loaFrontFace);
									renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											TextRenderEvent.RENDER_TEXT_IN_BLOCK, laDataPoint, null, null, boDataPoint);
								}
								break;
							default:
								throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
										"exception.illegal.datapoint.position.bar", //$NON-NLS-1$
										new Object[] { pDataPoint.getName() },
										Messages.getResourceBundle(getRunTimeContext().getULocale()));
							}
						}
					} else {
						switch (pDataPoint.getValue()) {
						case Position.OUTSIDE:
							if (!bInverted) {
								loDataPoint.set(dX - p.getHorizontalSpacing(), dY + dHeight / 2);
								renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
										TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.LEFT_LITERAL,
										loDataPoint, null);
							} else {
								loDataPoint.set(dX + dWidth + p.getHorizontalSpacing(), dY + dHeight / 2);
								renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
										TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.RIGHT_LITERAL,
										loDataPoint, null);
							}
							break;
						case Position.INSIDE:
							if (rt.getValue() == RiserType.CONE || rt.getValue() == RiserType.TRIANGLE) {
								if (!bInverted) {
									loDataPoint.set(dX + dWidth, dY + dHeight / 2);
									renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.LEFT_LITERAL,
											loDataPoint, null);
								} else {
									loDataPoint.set(dX, dY + dHeight / 2);
									renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
											TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint,
											Position.RIGHT_LITERAL, loDataPoint, null);
								}
							} else {
								boDataPoint.updateFrom(loaFrontFace);
								renderLabel(WrappedStructureSource.createSeriesDataPoint(bs, dpha[i]),
										TextRenderEvent.RENDER_TEXT_IN_BLOCK, laDataPoint, null, null, boDataPoint);
							}
							break;
						default:
							throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
									"exception.illegal.datapoint.position.bar", //$NON-NLS-1$
									new Object[] { pDataPoint.getName() },
									Messages.getResourceBundle(getRunTimeContext().getULocale()));
						}
					}
				}
			}

			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
					laDataPoint);
		}

		if (!bRendering3D) {
			List<double[]> points = new ArrayList<>();
			for (int i = 0; i < faX.length; i++) {
				points.add(new double[] { faX[i], faY[i] });
			}

			points = filterNull(points);

			if (isLastRuntimeSeriesInAxis()) {
				// clean stack state.
				getRunTimeContext().putState(STACKED_SERIES_LOCATION_KEY, null);
			} else {
				getRunTimeContext().putState(STACKED_SERIES_LOCATION_KEY, points);
			}

			// Render the fitting curve.
			// Fixed bugzilla bug 189607, always display curve fitting as a line for 2D and
			// 2D+.
			if (getSeries().getCurveFitting() != null) {
				Location[] larray = createLocationArray(points);
				renderFittingCurve(ipr, larray, getSeries().getCurveFitting(), false, true);
			}
		}

		resetAllStackedSizeHints();

		// Close clipping
		if (!bRendering3D) {
			restoreClipping(ipr);
		}
	}

	/**
	 * Compute the size for non category bar. The minimum size is 30% of tick size
	 */
	private double computeSizeForNonCategoryBar(boolean isTransposed, DataPointHints[] dpha) {
		double sizeForNonCategory;
		sizeForNonCategory = dpha[0].getSize();
		double maxSize = 0;
		for (int i = 0; i < dpha.length - 1; i++) {
			if (dpha[i].getSize() > maxSize) {
				maxSize = dpha[i].getSize();
			}

			for (int j = i + 1; j < dpha.length; j++) {
				double space = 0;
				if (isTransposed) {
					space = dpha[j].getLocation().getY() - dpha[i].getLocation().getY();
				} else {
					space = dpha[j].getLocation().getX() - dpha[i].getLocation().getX();
				}
				space = Math.abs(space);
				if (sizeForNonCategory > space) {
					sizeForNonCategory = space;
				}
			}
		}
		sizeForNonCategory = Math.max(sizeForNonCategory * .8, maxSize * .3);
		return sizeForNonCategory;
	}

	/**
	 * Returns specified instance of StackedSizeHints.
	 *
	 * @param i
	 * @return
	 */
	private StackedSizeHints getCurrentStackedSizeHints(int i) {
		Object obj = getRunTimeContext().getState(STACKED_SERIES_SIZE_KEY);
		if (obj == null) {
			getRunTimeContext().putState(STACKED_SERIES_SIZE_KEY, new ArrayList());
			obj = getRunTimeContext().getState(STACKED_SERIES_SIZE_KEY);
		}

		List stackedSizes = (List) obj;
		while ((stackedSizes.size() - 1) <= i) {
			stackedSizes.add(new StackedSizeHints());
		}

		StackedSizeHints slh = (StackedSizeHints) stackedSizes.get(i);
		return slh;
	}

	/**
	 * Clear stored StackedSizeHints info.
	 */
	private void resetAllStackedSizeHints() {
		if (isLastRuntimeSeriesInGroup()) {
			getRunTimeContext().putState(STACKED_SERIES_SIZE_KEY, null);
		}
	}

	/**
	 * Compute top and bottom size when it is stacked 2D case.
	 *
	 * @param stackedSizeHints
	 * @param au
	 * @param dValue
	 * @param size
	 * @return
	 */
	private static double[] computeStacked2DTopNBottomSize(StackedSizeHints stackedSizeHints, AxisSubUnit au,
			double dValue, double size) {
		double stackedSizePercent = computeStacked2DTopSizePercent(stackedSizeHints, au, dValue);

		double topSize;
		double bottomSize = 0d;

		if (dValue >= 0) {
			if (Double.isNaN(stackedSizeHints.getLastPositiveBottom()) && !Double.isNaN(size)) {
				stackedSizeHints.setLastPositiveBottom(size);
			}

			bottomSize = stackedSizeHints.getLastPositiveBottom();

			stackedSizeHints.setLastPositiveBottom(bottomSize * stackedSizePercent);
		} else {
			if (Double.isNaN(stackedSizeHints.getLastNegativeBottom()) && !Double.isNaN(size)) {
				stackedSizeHints.setLastNegativeBottom(size);
			}

			bottomSize = stackedSizeHints.getLastNegativeBottom();

			stackedSizeHints.setLastNegativeBottom(bottomSize * stackedSizePercent);
		}

		topSize = bottomSize * stackedSizePercent;

		return new double[] { topSize, bottomSize };
	}

	/**
	 *
	 * @param bs
	 * @return
	 */
	private boolean isStackedOrPercent(final BarSeries bs) {
		return bs.isStacked();
	}

	/**
	 * Compute top size percent of stacked 2D series.
	 *
	 * @param stackedSizeHints
	 * @param au
	 * @param dValue
	 * @return
	 */
	private static double computeStacked2DTopSizePercent(StackedSizeHints stackedSizeHints, AxisSubUnit au,
			double dValue) {
		double dTopSizePercent;
		double dCurrentTotal = 0;
		if (dValue >= 0) {
			if (Double.isNaN(stackedSizeHints.getRemainedPositiveTotal())) {
				dCurrentTotal = au.getPositiveTotal();
			} else {
				dCurrentTotal = stackedSizeHints.getRemainedPositiveTotal();
			}

			stackedSizeHints.setRemainedPositiveTotal(dCurrentTotal - dValue);
		} else {
			if (Double.isNaN(stackedSizeHints.getRemainedNegativeTotal())) {
				dCurrentTotal = au.getNegativeTotal();
			} else {
				dCurrentTotal = stackedSizeHints.getRemainedNegativeTotal();
			}

			stackedSizeHints.setRemainedNegativeTotal(dCurrentTotal - dValue);
		}

		if (!ChartUtil.mathEqual(dCurrentTotal, 0d)) {
			dTopSizePercent = (dCurrentTotal - dValue) / dCurrentTotal;
		} else {
			return 0d;
		}

		return dTopSizePercent;
	}

	private Location[] computeRiserRectangle2D(boolean bInverted, int i, double[] faX, double[] faY, double dX,
			double dY, double dHeight, double dWidth) {
		Location[] loaFrontFace = new Location[4];
		// NEW INSTANCE CREATED PER DATA POINT
		loaFrontFace[0] = goFactory.createLocation(dX, dY);
		loaFrontFace[1] = goFactory.createLocation(dX, dY + dHeight);
		loaFrontFace[2] = goFactory.createLocation(dX + dWidth, dY + dHeight);
		loaFrontFace[3] = goFactory.createLocation(dX + dWidth, dY);

		if (isTransposed()) {
			faX[i] = bInverted ? dX + dWidth : dX;
			faY[i] = (dY + dHeight / 2);
		} else {
			faX[i] = (dX + dWidth / 2);
			faY[i] = bInverted ? dY + dHeight : dY;
		}
		return loaFrontFace;
	}

	private List<Location3D[]> computeRiserRectangle3D(boolean bInverted, double dX, double dY, double dZ,
			double dHeight, double dWidth, double dWidthZ) {
		List<Location3D[]> loa3dFace = new ArrayList<>();
		Location3D[] a3dFace;
		if (!bInverted) {
			// downward

			// back
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ);
			loa3dFace.add(a3dFace);

			// left
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			loa3dFace.add(a3dFace);

			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			loa3dFace.add(a3dFace);

			// top
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);

			// right
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);

			// front
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			loa3dFace.add(a3dFace);
		} else {
			// back
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			loa3dFace.add(a3dFace);

			// left
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);

			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			loa3dFace.add(a3dFace);

			// top
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);

			// right
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			loa3dFace.add(a3dFace);

			// front
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			loa3dFace.add(a3dFace);
		}
		return loa3dFace;
	}

	/**
	 * Compute locations of stacked triangle.
	 *
	 * @param bInverted
	 * @param i
	 * @param faX
	 * @param faY
	 * @param dX
	 * @param dY
	 * @param dHeight
	 * @param dWidth
	 * @param dTopSize
	 * @param dBottomSize
	 * @return
	 */
	private Location[] computeStackedRiserTriangle2D(boolean bInverted, int i, double[] faX, double[] faY, double dX,
			double dY, double dHeight, double dWidth, double dTopSize, double dBottomSize, StackedSizeHints ssh) {

		if (ssh.getInitialBounds() == null) {
			Bounds b = goFactory.createBounds(dX, dY, dWidth, dHeight);
			ssh.setInitialBounds(b);
		}

		Location[] loaFrontFace = new Location[4];
		// NEW INSTANCE CREATED PER DATA POINT
		if (isTransposed())
		// TRIANGLE IS ROTATED BY 90 DEGREES
		{
			final double dX1 = bInverted ? dX : dX + dWidth;
			final double dX2 = bInverted ? dX + dWidth : dX;

			final double dTopDelta = (dHeight - dTopSize) / 2;
			final double dBottomDelta = (dHeight - dBottomSize) / 2;

			loaFrontFace[0] = goFactory.createLocation(dX1, dY + dBottomDelta + dBottomSize);
			loaFrontFace[1] = goFactory.createLocation(dX2, dY + dTopDelta + dTopSize);
			loaFrontFace[2] = goFactory.createLocation(dX2, dY + dTopDelta);
			loaFrontFace[3] = goFactory.createLocation(dX1, dY + dBottomDelta);

			faX[i] = dX2;
			faY[i] = (dY + dHeight / 2);
		} else
		// TRIANGLE IS UPRIGHT OR INVERTED DEPENDING ON VALUE BEING
		// PLOTTED
		{
			final double dY1 = bInverted ? dY : dY + dHeight;
			final double dY2 = bInverted ? dY + dHeight : dY;

			final double dTopDelta = (dWidth - dTopSize) / 2;
			final double dBottomDelta = (dWidth - dBottomSize) / 2;

			loaFrontFace[0] = goFactory.createLocation(dX + dBottomDelta, dY1);
			loaFrontFace[1] = goFactory.createLocation(dX + dTopDelta, dY2);
			loaFrontFace[2] = goFactory.createLocation(dX + dTopDelta + dTopSize, dY2);
			loaFrontFace[3] = goFactory.createLocation(dX + dBottomDelta + dBottomSize, dY1);

			faX[i] = (dX + dWidth / 2);
			faY[i] = (dY2);
		}
		return loaFrontFace;
	}

	private Location[] computeRiserTriangle2D(boolean bInverted, int i, double[] faX, double[] faY, double dX,
			double dY, double dHeight, double dWidth) {
		Location[] loaFrontFace = new Location[3];
		// NEW INSTANCE CREATED PER DATA POINT
		if (isTransposed())
		// TRIANGLE IS ROTATED BY 90 DEGREES
		{
			final double dX1 = bInverted ? dX : dX + dWidth;
			final double dX2 = bInverted ? dX + dWidth : dX;
			loaFrontFace[0] = goFactory.createLocation(dX1, dY + dHeight);
			loaFrontFace[1] = goFactory.createLocation(dX2, dY + dHeight / 2);
			loaFrontFace[2] = goFactory.createLocation(dX1, dY);

			faX[i] = dX2;
			faY[i] = (dY + dHeight / 2);
		} else
		// TRIANGLE IS UPRIGHT OR INVERTED DEPENDING ON VALUE BEING
		// PLOTTED
		{
			final double dY1 = bInverted ? dY : dY + dHeight;
			final double dY2 = bInverted ? dY + dHeight : dY;
			loaFrontFace[0] = goFactory.createLocation(dX, dY1);
			loaFrontFace[1] = goFactory.createLocation(dX + dWidth / 2, dY2);
			loaFrontFace[2] = goFactory.createLocation(dX + dWidth, dY1);

			faX[i] = (dX + dWidth / 2);
			faY[i] = (dY2);
		}
		return loaFrontFace;
	}

	private List<Location3D[]> computeRiserTriangle3D(boolean bInverted, double dX, double dY, double dZ,
			double dHeight, double dWidth, double dWidthZ) {
		List<Location3D[]> loa3dFace = new ArrayList<>();
		Location3D[] a3dFace;
		if (!bInverted) {
			// front
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// back
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// left
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// right
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);
		} else {
			// front
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY + dHeight, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// back
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY + dHeight, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// left
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY + dHeight, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// right
			a3dFace = new Location3D[3];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth / 2, dY + dHeight, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);

			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			loa3dFace.add(a3dFace);
		}
		return loa3dFace;
	}

	private Location[] computeRiserTube2D(boolean bInverted, int i, double[] faX, double[] faY, double dX, double dY,
			double dHeight, double dWidth) {
		Location[] loaFrontFace = new Location[4];
		// NEW INSTANCE CREATED PER DATA POINT
		loaFrontFace[0] = goFactory.createLocation(dX, dY);
		loaFrontFace[1] = goFactory.createLocation(dX, dY + dHeight);
		loaFrontFace[2] = goFactory.createLocation(dX + dWidth, dY + dHeight);
		loaFrontFace[3] = goFactory.createLocation(dX + dWidth, dY);

		if (isTransposed()) {
			faX[i] = bInverted ? (dX + dWidth) : dX;
			faY[i] = (dY + dHeight / 2);
		} else {
			faX[i] = (dX + dWidth / 2);
			faY[i] = bInverted ? dY + dHeight : dY;
		}
		return loaFrontFace;
	}

	private List<Location3D[]> computeRiserTube3D(double dX, double dY, double dZ, double dHeight, double dWidth,
			double dWidthZ) {
		List<Location3D[]> loa3dFace = new ArrayList<>();
		Location3D[] a3dFace = new Location3D[4];

		// top
		a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
		a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
		a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
		a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
		loa3dFace.add(a3dFace);

		// bottom
		a3dFace = new Location3D[4];
		a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
		a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
		a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
		a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
		loa3dFace.add(a3dFace);

		return loa3dFace;
	}

	/**
	 * Compute locations of stacked cone.
	 *
	 * @param bInverted
	 * @param i
	 * @param faX
	 * @param faY
	 * @param dX
	 * @param dY
	 * @param dHeight
	 * @param dWidth
	 * @param dTopSize
	 * @param dBottomSize
	 * @return
	 */
	private Location[] computeStackedRiserCone2D(boolean bInverted, int i, double[] faX, double[] faY, double dX,
			double dY, double dHeight, double dWidth, double dTopSize, double dBottomSize) {
		Location[] loaFrontFace = new Location[4];
		// NEW INSTANCE CREATED PER DATA POINT
		if (isTransposed()) // TRIANGLE IS ROTATED BY 90
		// DEGREES
		{
			final double dX1 = bInverted ? dX : dX + dWidth;
			final double dX2 = bInverted ? dX + dWidth : dX;

			final double dTopDelta = (dHeight - dTopSize) / 2;
			final double dBottomDelta = (dHeight - dBottomSize) / 2;

			loaFrontFace[0] = goFactory.createLocation(dX1, dY + dBottomDelta + dBottomSize);
			loaFrontFace[1] = goFactory.createLocation(dX2, dY + dTopDelta + dTopSize);
			loaFrontFace[2] = goFactory.createLocation(dX2, dY + dTopDelta);
			loaFrontFace[3] = goFactory.createLocation(dX1, dY + dBottomDelta);

			faX[i] = dX2;
			faY[i] = (dY + dHeight / 2);
		} else
		// TRIANGLE IS UPRIGHT OR INVERTED DEPENDING ON VALUE BEING
		// PLOTTED
		{
			final double dY1 = bInverted ? dY : dY + dHeight;
			final double dY2 = bInverted ? dY + dHeight : dY;

			final double dTopDelta = (dWidth - dTopSize) / 2;
			final double dBottomDelta = (dWidth - dBottomSize) / 2;

			loaFrontFace[0] = goFactory.createLocation(dX + dBottomDelta, dY1);
			loaFrontFace[1] = goFactory.createLocation(dX + dTopDelta, dY2);
			loaFrontFace[2] = goFactory.createLocation(dX + dTopDelta + dTopSize, dY2);
			loaFrontFace[3] = goFactory.createLocation(dX + dBottomDelta + dBottomSize, dY1);

			faX[i] = (dX + dWidth / 2);
			faY[i] = (dY2);

		}
		return loaFrontFace;
	}

	private Location[] computeRiserCone2D(boolean bInverted, int i, double[] faX, double[] faY, double dX, double dY,
			double dHeight, double dWidth) {
		Location[] loaFrontFace = new Location[3];
		// NEW INSTANCE CREATED PER DATA POINT
		if (isTransposed()) // TRIANGLE IS ROTATED BY 90
		// DEGREES
		{
			final double dX1 = bInverted ? dX : dX + dWidth;
			final double dX2 = bInverted ? dX + dWidth : dX;
			loaFrontFace[0] = goFactory.createLocation(dX1, dY);
			loaFrontFace[1] = goFactory.createLocation(dX2, dY + dHeight / 2);
			loaFrontFace[2] = goFactory.createLocation(dX1, dY + dHeight);

			faX[i] = dX2;
			faY[i] = (dY + dHeight / 2);
		} else
		// TRIANGLE IS UPRIGHT OR INVERTED DEPENDING ON VALUE BEING
		// PLOTTED
		{
			final double dY1 = bInverted ? dY : dY + dHeight;
			final double dY2 = bInverted ? dY + dHeight : dY;
			loaFrontFace[0] = goFactory.createLocation(dX, dY1);
			loaFrontFace[1] = goFactory.createLocation(dX + dWidth / 2, dY2);
			loaFrontFace[2] = goFactory.createLocation(dX + dWidth, dY1);

			faX[i] = (dX + dWidth / 2);
			faY[i] = (dY2);
		}
		return loaFrontFace;
	}

	private List<Location3D[]> computeRiserCone3D(boolean bInverted, double dX, double dY, double dZ, double dHeight,
			double dWidth, double dWidthZ) {
		List<Location3D[]> loa3dFace = new ArrayList<>();
		Location3D[] a3dFace;
		if (!bInverted) {
			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY + dHeight, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX, dY + dHeight, dZ + dWidthZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX + dWidth, dY + dHeight, dZ);
			loa3dFace.add(a3dFace);

			// the top point
			a3dFace = new Location3D[1];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth / 2, dY, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);
		} else {
			// bottom
			a3dFace = new Location3D[4];
			a3dFace[0] = goFactory.createLocation3D(dX, dY, dZ);
			a3dFace[1] = goFactory.createLocation3D(dX + dWidth, dY, dZ);
			a3dFace[2] = goFactory.createLocation3D(dX + dWidth, dY, dZ + dWidthZ);
			a3dFace[3] = goFactory.createLocation3D(dX, dY, dZ + dWidthZ);
			loa3dFace.add(a3dFace);

			// the top point
			a3dFace = new Location3D[1];
			a3dFace[0] = goFactory.createLocation3D(dX + dWidth / 2, dY + dHeight, dZ + dWidthZ / 2);
			loa3dFace.add(a3dFace);
		}
		return loa3dFace;
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
	@Override
	public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final BarSeries bs = (BarSeries) getSeries();
		final ColorDefinition cd = bs.getRiserOutline();
		final LineAttributes lia = goFactory.createLineAttributes(cd == null ? null : goFactory.copyOf(cd),
				LineStyle.SOLID_LITERAL, 1);
		if (fPaletteEntry == null) // TEMPORARY PATCH: WILL BE REMOVED SOON
		{
			fPaletteEntry = goFactory.RED();
		} else if (fPaletteEntry instanceof MultipleFill) {
			fPaletteEntry = ((MultipleFill) fPaletteEntry).getFills().get(0);
		}

		// COMPUTE THE FRONT FACE ONLY
		Location[] loaFrontFace = null;
		double dSeriesThickness = 0;
		switch (bs.getRiser().getValue()) {
		case RiserType.RECTANGLE:
		case RiserType.TUBE:
			if (bs.getRiser().getValue() == RiserType.RECTANGLE
					&& getModel().getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
				dSeriesThickness = 3 * getDeviceScale();
			}
			if (bs.getRiser().getValue() == RiserType.TUBE) {
				dSeriesThickness = 2 * getDeviceScale();
			}
			loaFrontFace = new Location[4];
			loaFrontFace[0] = goFactory.createLocation(bo.getLeft(), bo.getTop() + dSeriesThickness);
			loaFrontFace[1] = goFactory.createLocation(bo.getLeft(), bo.getTop() + bo.getHeight());
			loaFrontFace[2] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight());
			loaFrontFace[3] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + dSeriesThickness);
			break;
		case RiserType.TRIANGLE:
		case RiserType.CONE:
			loaFrontFace = new Location[3];
			loaFrontFace[0] = goFactory.createLocation(bo.getLeft(), bo.getTop() + bo.getHeight());
			loaFrontFace[1] = goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop());
			loaFrontFace[2] = goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight());
			break;
		default:
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.legend.graphic.unknown.riser", //$NON-NLS-1$
					new Object[] { bs.getRiser().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		if (bs.getRiser().getValue() == RiserType.TUBE) {
			renderRiserTube2D(ipr, StructureSource.createLegend(lg), null, loaFrontFace, fPaletteEntry, lia,
					getModel().getDimension(), 2 * getDeviceScale(), false, false, // Always non-transposed in legend
					false, false, false, 0, null);
		} else if (bs.getRiser().getValue() == RiserType.CONE) {
			renderRiserCone2D(ipr, StructureSource.createLegend(lg), null, loaFrontFace, fPaletteEntry, lia,
					getModel().getDimension(), 2 * getDeviceScale(), false, false, // Always non-transposed in legend
					false, false, // Always upward
					false, 2 * getDeviceScale(), 0, null);
		} else if (bs.getRiser().getValue() == RiserType.TRIANGLE) {
			ChartDimension cdim = getModel().getDimension();
			if (cdim != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
				adjustLocationsWithTriangle2D(loaFrontFace, 0, 2 * getDeviceScale(), 2 * getDeviceScale());
			}

			renderRiserTriangle2D(ipr, StructureSource.createLegend(lg), loaFrontFace, fPaletteEntry, lia,
					cdim == ChartDimension.THREE_DIMENSIONAL_LITERAL ? ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL
							: cdim,
					0d, 2 * getDeviceScale(), false, 0, null);

		} else {
			// RENDER THE PLANE (INTERNALLY EXTRUDED IF NECESSARY)
			renderPlane(ipr, StructureSource.createLegend(lg), loaFrontFace, fPaletteEntry, lia,
					getModel().getDimension(), 3 * getDeviceScale(), false);
		}
	}

	/**
	 * Returns a Location array from given list, each entry in the list should be a
	 * double[2] array object.
	 *
	 * @param ll
	 */
	private Location[] createLocationArray(List ll) {
		Location[] loa = new Location[ll.size()];
		for (int i = 0; i < loa.length; i++) {
			double[] obj = (double[]) ll.get(i);
			loa[i] = goFactory.createLocation(obj[0], obj[1]);
		}
		return loa;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.
	 * model.attribute.Bounds, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	@Override
	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// NOTE: This method is not used by the BAR renderer
	}

	/**
	 * @param ipr
	 * @param oSource
	 * @param dpha
	 * @param loaFront
	 * @param f
	 * @param lia
	 * @param cd
	 * @param dSeriesThickness
	 * @param bOffset
	 * @param bTransposed
	 * @param bDeferred
	 * @param bInverted
	 * @param bStacked
	 * @param zorder_hint
	 * @param compareBounds    this bounds is used to adjust the order of polygon,
	 *                         if this bound isn't null, chart will use this bounds
	 *                         instead of actual bounds of polygon for order.
	 *
	 * @throws ChartException
	 */
	private void renderRiserTube2D(IPrimitiveRenderer ipr, Object oSource, DataPointHints dpha, Location[] loaFront,
			Fill f, LineAttributes lia, ChartDimension cd, double dSeriesThickness, boolean bOffset,
			boolean bTransposed, boolean bDeferred, boolean bInverted, boolean bStacked, int zorder_hint,
			Bounds compareBounds) throws ChartException {
		ArrayList alModel = new ArrayList();

		// 1. Get correct fill color and line attributes for outline.
		Fill fBrighter;

		if (!isDimension3D()) {
			f = FillUtil.convertFillToGradient(f, bTransposed);
			fBrighter = FillUtil.getBrighterFill(f);
		} else {
			// drawing the legend graphic
			fBrighter = FillUtil.changeBrightness(f, 0.89);
			f = FillUtil.convertFillToGradient3D(f, bTransposed);
		}

		LineAttributes liaBorder = goFactory.copyOf(lia);
		if (liaBorder.getColor() == null) {
			// Prevent line invisible in Auto color
			liaBorder.setColor(FillUtil.getDarkerColor(f));
		}

		// 2. Create rendering event for data renderer.
		PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
		LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, LineRenderEvent.class);
		OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, OvalRenderEvent.class);
		ArcRenderEvent are = ((EventObjectCache) ipr).getEventObject(oSource, ArcRenderEvent.class);

		// 3. Compute bounds of top and bottom ovals.
		double dWidth = bTransposed ? loaFront[1].getY() - loaFront[0].getY() : loaFront[2].getX() - loaFront[1].getX();
		if (bOffset) {
			for (int i = 0; i < loaFront.length; i++) {

				if (bTransposed) {
					loaFront[i].setX(loaFront[i].getX() + dSeriesThickness);
				} else {
					loaFront[i].setY(loaFront[i].getY() - dSeriesThickness);
				}
			}
		}

		Bounds bottomBounds = null;
		if (bTransposed) {
			// Bugzilla 188058. - Henry
			// Compute correct coordinate and avoid negative value.
			bottomBounds = goFactory.createBounds(loaFront[0].getX() - dSeriesThickness, loaFront[0].getY() + dWidth,
					dSeriesThickness * 2, Math.abs(dWidth));
		} else {
			bottomBounds = goFactory.createBounds(loaFront[0].getX(), loaFront[1].getY() - dSeriesThickness, dWidth,
					dSeriesThickness * 2);
		}

		Bounds topBounds = null;
		if (bTransposed) {
			topBounds = goFactory.createBounds(loaFront[3].getX() - dSeriesThickness, loaFront[3].getY() + dWidth,
					dSeriesThickness * 2, Math.abs(dWidth));
		} else {
			topBounds = goFactory.createBounds(loaFront[0].getX(), loaFront[0].getY() - dSeriesThickness, dWidth,
					dSeriesThickness * 2);
		}

		// 4. Do renderer.
		// 4.1 - Render the bottom
		if (bottomBounds != null) {
			ore.setBounds(bottomBounds);
			ore.setBackground(f);
			ore.setOutline(liaBorder);
			if (bDeferred) {
				alModel.add(ore.copy());
			} else {
				ipr.fillOval(ore);
			}

			// 1.1 Render the bottom border
			are.setBackground(null);
			are.setOutline(liaBorder);
			are.setBounds(ore.getBounds());
			are.setAngleExtent(180);
			are.setStyle(ArcRenderEvent.OPEN);
			if (bTransposed) {
				are.setStartAngle(90);
			} else {
				are.setStartAngle(180);
			}
			if (bDeferred) {
				alModel.add(are.copy());
			} else {
				ipr.drawArc(are);
			}
		}

		// 4.2 - RENDER THE body without border
		pre.setPoints(loaFront);
		pre.setBackground(f);
		pre.setOutline(null);
		if (bDeferred) {
			alModel.add(pre.copy());
		} else {
			ipr.fillPolygon(pre);
		}

		// Render the border of the body
		if (bTransposed) {
			lre.setStart(loaFront[1]);
			lre.setEnd(loaFront[2]);
		} else {
			lre.setStart(loaFront[0]);
			lre.setEnd(loaFront[1]);
		}
		lre.setLineAttributes(liaBorder);
		if (bDeferred) {
			alModel.add(lre.copy());
		} else {
			ipr.drawLine(lre);
		}

		if (bTransposed) {
			lre.setStart(loaFront[0]);
			lre.setEnd(loaFront[3]);
		} else {
			lre.setStart(loaFront[2]);
			lre.setEnd(loaFront[3]);
		}
		lre.setLineAttributes(liaBorder);
		if (bDeferred) {
			alModel.add(lre.copy());
		} else {
			ipr.drawLine(lre);
		}

		// 4.3 - Render the top.
		if (topBounds != null) {
			ore.setBounds(topBounds);
			ore.setBackground(fBrighter);
			ore.setOutline(liaBorder);

			if (bDeferred) {
				alModel.add(ore.copy());
			} else {
				ipr.fillOval(ore);
				ipr.drawOval(ore);
			}

			// Render the top border
			are.setBackground(null);
			are.setOutline(liaBorder);
			are.setBounds(ore.getBounds());
			are.setAngleExtent(180);
			are.setStyle(ArcRenderEvent.OPEN);
			if (bTransposed) {
				are.setStartAngle(90);
			} else {
				are.setStartAngle(180);
			}
			if (bDeferred) {
				alModel.add(are.copy());
			} else {
				ipr.drawArc(are);
			}
		}

		// 5. Add interactivity support for bottom face. Support for body has been
		// added in main renderSeries method.
		renderInteractivity(ipr, dpha, ore);

		// 6. Add deferred rendering to cache.
		if (!alModel.isEmpty()) {
			WrappedInstruction wi = new WrappedInstruction(getDeferredCache(), alModel, PrimitiveRenderEvent.FILL,
					zorder_hint);
			wi.setCompareBounds(compareBounds);
			dc.addModel(wi);
		}
	}

	/**
	 *
	 * @param ipr
	 * @param oSource
	 * @param dpha
	 * @param loaFront
	 * @param f
	 * @param lia
	 * @param cd
	 * @param dSeriesThickness
	 * @param bOffset
	 * @param bTransposed
	 * @param bDeferred
	 * @param bInverted        true: downward, false: upward
	 * @param bIsStacked
	 * @param compareBounds    this bounds is used to adjust the order of polygon,
	 *                         if this bound isn't null, chart will use this bounds
	 *                         instead of actual bounds of polygon for order.
	 *
	 * @throws ChartException
	 */
	private void renderRiserCone2D(IPrimitiveRenderer ipr, Object oSource, DataPointHints dpha, Location[] loaFront,
			Fill f, LineAttributes lia, ChartDimension cd, double dSeriesThickness, boolean bOffset,
			boolean bTransposed, boolean bDeferred, boolean bInverted, boolean bIsStacked, double ovalHeight,
			int zorder_hint, Bounds compareBounds) throws ChartException {
		ArrayList alModel = new ArrayList();

		// 1. Compute fill color.

		if (!isDimension3D()) {
			f = FillUtil.convertFillToGradient(f, bTransposed);
		} else {
			f = FillUtil.convertFillToGradient3D(f, bTransposed);
		}

		// 2. Create renderer events.
		PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
		LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, LineRenderEvent.class);
		ArcRenderEvent are = ((EventObjectCache) ipr).getEventObject(oSource, ArcRenderEvent.class);

		// 3. Compute bounds of top and bottom ovals.
		double dBottomWidth = 0d;
		if (bIsStacked) {
			dBottomWidth = bTransposed ? loaFront[0].getY() - loaFront[3].getY()
					: loaFront[3].getX() - loaFront[0].getX();
		} else {
			dBottomWidth = bTransposed ? loaFront[2].getY() - loaFront[0].getY()
					: loaFront[2].getX() - loaFront[0].getX();
		}

		double dTopWidth = 0d;
		if (bIsStacked) {
			dTopWidth = bTransposed ? loaFront[1].getY() - loaFront[2].getY() : loaFront[2].getX() - loaFront[1].getX();
		} else {
			dTopWidth = Double.NaN;
		}

		if (bOffset) {
			for (int i = 0; i < loaFront.length; i++) {

				if (bTransposed) {
					loaFront[i].setX(loaFront[i].getX() + dSeriesThickness);
				} else {
					loaFront[i].setY(loaFront[i].getY() - dSeriesThickness);
				}
			}
		}

		// Render the bottom
		LineAttributes liaBorder = goFactory.copyOf(lia);
		if (liaBorder.getColor() == null) {
			// Prevent line invisible in Auto color
			liaBorder.setColor(FillUtil.getDarkerColor(f));
		}

		Bounds bottomBounds;
		if (bTransposed) {
			if (bIsStacked) {
				// Bugzilla 188058. - Henry
				// Compute correct coordinate and avoid negative value.
				bottomBounds = goFactory.createBounds(loaFront[3].getX() - ovalHeight,
						loaFront[3].getY() + dBottomWidth, ovalHeight * 2, Math.abs(dBottomWidth));
			} else {
				bottomBounds = goFactory.createBounds(loaFront[0].getX() - ovalHeight,
						loaFront[0].getY() + dBottomWidth, ovalHeight * 2, Math.abs(dBottomWidth));
			}
		} else {
			bottomBounds = goFactory.createBounds(loaFront[0].getX(), loaFront[0].getY() - ovalHeight, dBottomWidth,
					ovalHeight * 2);
		}

		Bounds topBounds;
		double topHeight = ovalHeight * dTopWidth / dBottomWidth;
		if (bTransposed) {
			if (bIsStacked) {
				// Bugzilla 188058. - Henry
				// Compute correct coordinate and avoid negative value.
				topBounds = goFactory.createBounds(loaFront[2].getX() - topHeight, loaFront[2].getY() + dTopWidth,
						topHeight * 2, Math.abs(dTopWidth));
			} else {
				topBounds = null;
			}
		} else {
			topBounds = goFactory.createBounds(loaFront[1].getX(), loaFront[1].getY() - topHeight, dTopWidth,
					topHeight * 2);
		}

		// Switch top and bottom for inverted case.
		if (!bTransposed) {
			if (bInverted) {
				Bounds tmpBounds = topBounds;
				topBounds = bottomBounds;
				bottomBounds = tmpBounds;
			}
		} else if (!bInverted) {
			Bounds tmpBounds = topBounds;
			topBounds = bottomBounds;
			bottomBounds = tmpBounds;
		}

		// 4. Do renderer.
		// 4.1 - Render bottom.
		if (bottomBounds != null) {
			are.setOutline(liaBorder);
			are.setBounds(bottomBounds);
			are.setBackground(f);
			are.setAngleExtent(360);
			are.setStyle(ArcRenderEvent.OPEN);
			if (bTransposed) {
				are.setStartAngle(90);
			} else {
				are.setStartAngle(180);
			}

			if (bDeferred) {
				alModel.add(are.copy());
			} else {
				ipr.drawArc(are);
				ipr.fillArc(are);
			}
		}

		// 4.2 - RENDER THE triangle
		pre.setPoints(loaFront);
		pre.setBackground(f);
		pre.setOutline(null);
		if (bDeferred) {
			alModel.add(pre.copy());
		} else {
			ipr.fillPolygon(pre);
		}

		// Render the border line of triangle
		lre.setLineAttributes(liaBorder);
		lre.setStart(loaFront[0]);
		lre.setEnd(loaFront[1]);
		if (bDeferred) {
			alModel.add(lre.copy());
		} else {
			ipr.drawLine(lre);
		}
		lre.setLineAttributes(liaBorder);
		if (bIsStacked) {
			lre.setStart(loaFront[3]);
			lre.setEnd(loaFront[2]);
		} else {
			lre.setStart(loaFront[2]);
			lre.setEnd(loaFront[1]);
		}

		if (bDeferred) {
			alModel.add(lre.copy());
		} else {
			ipr.drawLine(lre);
		}

		// 4.3 - Render top.
		if (topBounds != null) {
			are.setOutline(liaBorder);
			are.setBounds(topBounds);
			are.setBackground(f);
			are.setAngleExtent(360);
			are.setStyle(ArcRenderEvent.OPEN);
			if (bTransposed) {
				are.setStartAngle(90);
			} else {
				are.setStartAngle(180);
			}

			if (bDeferred) {
				alModel.add(are.copy());
			} else {
				ipr.drawArc(are);
				ipr.fillArc(are);
			}
		}

		// 5. Add interactivity support for top face. Support for body has been
		// added in main renderSeries method.
		renderInteractivity(ipr, dpha, are);

		// 6. Add deferred renderer to cache.
		if (!alModel.isEmpty()) {
			WrappedInstruction wi = new WrappedInstruction(getDeferredCache(), alModel,
					PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, zorder_hint);
			wi.setCompareBounds(compareBounds);
			dc.addModel(wi);
		}
	}

	/**
	 * Render triangle.
	 *
	 * @param ipr              A handle to the primitive rendering device
	 * @param oSource          The object wrapped in the polygon rendering event
	 * @param loaFront         The co-ordinates of the front face polygon
	 * @param f                The fill color for the front face
	 * @param lia              The edge color for the polygon
	 * @param dSeriesThickness The thickness or the extrusion level (for 2.5D or 3D)
	 * @param compareBounds    this bounds is used to adjust the order of polygon,
	 *                         if this bound isn't null, chart will use this bounds
	 *                         instead of actual bounds of polygon for order.
	 *
	 * @throws ChartException
	 */
	private void renderRiserTriangle2D(IPrimitiveRenderer ipr, Object oSource, Location[] loaFront, Fill f,
			LineAttributes lia, ChartDimension cd, double dTopThickness, double dBottomThickness, boolean bDeferred,
			int zorder_hint, Bounds compareBounds) throws ChartException {
		// Process 2D case.
		PolygonRenderEvent pre;
		if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL) {
			// RENDER THE POLYGON
			pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loaFront);
			pre.setBackground(f);
			pre.setOutline(lia);
			if (bDeferred) {
				dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
			return;
		}

		// Process 2D+ case.
		// 1. Compute fill color.
		final boolean bSolidColor = f instanceof ColorDefinition;
		Fill fDarker = null, fBrighter = null;
		if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH) {
			fDarker = FillUtil.getDarkerFill(f);
			fBrighter = FillUtil.getBrighterFill(f);
		}

		// #192368
		// case of drawing legend graphics in 3d mode
		// readjusts the brightness to give a more consistenter appearance
		if (isDimension3D()) {
			fBrighter = FillUtil.changeBrightness(f, 0.89);
			fDarker = FillUtil.changeBrightness(f, 0.65);
			f = FillUtil.changeBrightness(f, 0.89);
		}

		// 2. Compute all sides of triangle for 2D+.
		final int nSides = loaFront.length;
		final Location[][] loaa = new Location[nSides + 1][];
		Location[] loa;
		double dY, dSmallestY = 0;
		for (int j, i = 0; i < nSides; i++) {
			j = i + 1;
			if (j >= loaFront.length) {
				j = 0;
			}

			double[] correctThicknesses = computeCorrectThicknessesWithTriangle2D(nSides, i, dTopThickness,
					dBottomThickness);

			loa = new Location[4];
			loa[0] = goFactory.createLocation(loaFront[i].getX(), loaFront[i].getY());
			loa[1] = goFactory.createLocation(loaFront[j].getX(), loaFront[j].getY());
			loa[2] = goFactory.createLocation(loaFront[j].getX() + correctThicknesses[0],
					loaFront[j].getY() - correctThicknesses[0]);
			loa[3] = goFactory.createLocation(loaFront[i].getX() + correctThicknesses[1],
					loaFront[i].getY() - correctThicknesses[1]);
			loaa[i] = loa;
		}
		loaa[nSides] = loaFront;

		// SORT ON MULTIPLE KEYS (GREATEST Y, SMALLEST X)
		double dI, dJ;
		Location[] loaI, loaJ;
		for (int i = 0; i < nSides - 1; i++) {
			loaI = loaa[i];
			for (int j = i + 1; j < nSides; j++) {
				loaJ = loaa[j];

				dI = getY(loaI, IConstants.AVERAGE);
				dJ = getY(loaJ, IConstants.AVERAGE);

				// Use fuzzy comparison here due to possible precision loss
				// during computation.
				if (ChartUtil.mathGT(dJ, dI)) // SWAP
				{
					loaa[i] = loaJ;
					loaa[j] = loaI;
					loaI = loaJ;
				} else if (ChartUtil.mathEqual(dJ, dI)) {
					dI = getX(loaI, IConstants.AVERAGE);
					dJ = getX(loaJ, IConstants.AVERAGE);
					if (ChartUtil.mathGT(dI, dJ)) {
						loaa[i] = loaJ;
						loaa[j] = loaI;
						loaI = loaJ;
					}
				}
			}
		}

		int iSmallestYIndex = 0;
		for (int i = 0; i < nSides; i++) {
			dY = getY(loaa[i], IConstants.AVERAGE);
			if (i == 0) {
				dSmallestY = dY;
			}
			// #192797: Use fuzzy comparison here due to possible precision
			// loss during computation.
			else if (ChartUtil.mathGT(dSmallestY, dY)) {
				dSmallestY = dY;
				iSmallestYIndex = i;
			}
		}

		// 3. Do renderer.
		ArrayList alModel = new ArrayList(nSides + 1);
		Fill fP;
		for (int i = 0; i < (nSides + 1); i++) {
			pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);

			pre.setOutline(lia);
			pre.setPoints(loaa[i]);

			if (i == nSides) {
				fP = f;
			} else if (i == iSmallestYIndex) {
				fP = fBrighter;
			} else {
				fP = fDarker;
			}
			pre.setBackground(fP);

			if (i == nSides) {
				if (bDeferred) {
					alModel.add(pre.copy());
				} else {
					ipr.fillPolygon(pre);
				}
			} else if (i == iSmallestYIndex) {
				// DRAW A TRANSLUCENT LIGHT GLASS PANE OVER THE BRIGHTER SURFACE
				// (IF NOT A SOLID COLOR)
				if (!bSolidColor) {
					pre.setBackground(LIGHT_GLASS);
				}
				if (bDeferred) {
					alModel.add(pre.copy());
				} else {
					ipr.fillPolygon(pre);
				}
			} else {
				// DRAW A TRANSLUCENT DARK GLASS PANE OVER THE DARKER SURFACE
				// (IF NOT A SOLID COLOR)
				if (!bSolidColor) {
					pre.setBackground(DARK_GLASS);
				}
				if (bDeferred) {
					alModel.add(pre.copy());
				} else {
					ipr.fillPolygon(pre);
				}
			}
			if (!bDeferred) {
				ipr.drawPolygon(pre);
			}
		}

		// 4. Add deferred rendering to cache.
		if (!alModel.isEmpty()) {
			WrappedInstruction wi = new WrappedInstruction(getDeferredCache(), alModel, PrimitiveRenderEvent.FILL,
					zorder_hint);
			wi.setCompareBounds(compareBounds);
			dc.addModel(wi);
		}
	}

	/**
	 * Renders Tube as 3D presentation.
	 *
	 * @param ipr
	 * @param oSource
	 * @param loaFace
	 * @param f
	 * @param lia
	 * @throws ChartException
	 */
	private void renderRiserTube3D(IPrimitiveRenderer ipr, Object oSource, List loaFace, Fill f, LineAttributes lia,
			DataPointHints dpha) throws ChartException {
		if (loaFace == null || loaFace.size() != 2) {
			throw new IllegalArgumentException();
		}

		// Create a sub rendering cache to save all polygons of this data point.
		DeferredCache subCache = createSubDeferreceCache4Tube3D(ipr, oSource, loaFace, f);

		Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class);
		Point2D.Double[] ovalsTop = null, ovalsBottom = null;
		double yTop = 0, yBottom = 0;
		final int size = 30; // TODO define constant
		for (int i = 0; i < loaFace.size(); i++) {
			Location3D[] locations = (Location3D[]) loaFace.get(i);

			Point2D.Double pointA = new Point2D.Double(locations[0].getX(), locations[0].getZ());
			Point2D.Double pointB = new Point2D.Double(locations[2].getX(), locations[2].getZ());
			if (i == 0) {
				ovalsTop = computeOvalPoints(size, pointA, pointB);
				yTop = locations[0].getY();
			} else {
				ovalsBottom = computeOvalPoints(size, pointA, pointB);
				yBottom = locations[0].getY();
			}
		}

		Location3D[] topPoints = new Location3D[size];
		Location3D[] bottomPoints = new Location3D[size];
		// Fill the body
		for (int i = 0; i < size; i++) {
			// Depending on the tube orientation, the top and bottom surface
			// have different orientations (only one side is painted).
			topPoints[i] = goFactory.createLocation3D(ovalsTop[i].getX(), yTop, ovalsTop[i].getY());
			bottomPoints[i] = goFactory.createLocation3D(ovalsBottom[i].getX(), yBottom, ovalsBottom[i].getY());
			int preSize = i - 1;
			if (i == 0) {
				preSize = size - 1;
				topPoints[preSize] = goFactory.createLocation3D(ovalsTop[preSize].getX(), yTop,
						ovalsTop[preSize].getY());
				bottomPoints[preSize] = goFactory.createLocation3D(ovalsBottom[preSize].getX(), yBottom,
						ovalsBottom[preSize].getY());
			}
			pre.setPoints3D(new Location3D[] { topPoints[i], bottomPoints[i], bottomPoints[preSize], topPoints[preSize]

			});
			pre.setBackground(f);
			pre.setDoubleSided(false);
			subCache.addPlane(pre, PrimitiveRenderEvent.FILL);
		}

		// Fill the top face
		pre.setDoubleSided(false);
		pre.setBrightness(10);
		pre.setPoints3D(topPoints, yBottom < yTop);
		pre.setOutline(lia);
		pre.setBackground(f);
		subCache.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);

		// Fill the bottom face
		pre.setDoubleSided(false);
		pre.setPoints3D(bottomPoints, yBottom > yTop);
		pre.setOutline(lia);
		pre.setBackground(f);
		subCache.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);

		// Add interactivity support for Tube body. Support for Tube top/bottom
		// face have been added in main renderSeries method
		if (isInteractivityEnabled()) {
			// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
			final EList<Trigger> elTriggers = getSeries().getTriggers();
			if (!elTriggers.isEmpty()) {
				final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(getSeries(), dpha);
				for (int i = 0; i < topPoints.length; i++) {
					final InteractionEvent iev = createEvent(iSource, elTriggers, ipr);
					iev.setCursor(getSeries().getCursor());

					final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createSeries(getSeries()), Polygon3DRenderEvent.class);
					int preSize = i == 0 ? size - 1 : i - 1;
					pre3d.setPoints3D(new Location3D[] { topPoints[preSize], bottomPoints[preSize], bottomPoints[i],
							topPoints[i] });
					final Location panningOffset = getPanningOffset();

					if (get3DEngine().processEvent(pre3d, panningOffset.getX(), panningOffset.getY()) != null) {
						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}
				}
			}
		}
	}

	private DeferredCache createSubDeferreceCache4Tube3D(IPrimitiveRenderer ipr, Object oSource, List loaFace, Fill f)
			throws ChartException {
		DeferredCache subCache = dc.deriveNewDeferredCache();
		subCache.setAntialiasing(true);
		Location3D[] l3d = new Location3D[4];
		for (int k = 0; k < 4; k++) {
			l3d[k] = goFactory.createLocation3D(0, 0, 0);
		}
		Location3D[] l0 = (Location3D[]) loaFace.get(0);
		Location3D[] l1 = (Location3D[]) loaFace.get(1);
		l3d[0].set(l0[0].getX(), l0[0].getY(), l0[0].getZ());
		l3d[1].set(l0[2].getX(), l0[2].getY(), l0[2].getZ());
		l3d[2].set(l1[2].getX(), l1[2].getY(), l1[2].getZ());
		l3d[3].set(l1[0].getX(), l1[0].getY(), l1[0].getZ());

		Polygon3DRenderEvent pre3dEvent = ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class);
		pre3dEvent.setEnable(false);
		pre3dEvent.setDoubleSided(false);
		pre3dEvent.setOutline(null);
		pre3dEvent.setPoints3D(l3d);
		pre3dEvent.setBackground(f);

		Object event = dc.addPlane(pre3dEvent, PrimitiveRenderEvent.FILL);
		if (event instanceof WrappedInstruction) {
			((WrappedInstruction) event).setSubDeferredCache(subCache);
		}
		// Restore the default value.
		pre3dEvent.reset();
		return subCache;
	}

	/**
	 * Renders Cone as 3D presentation.
	 *
	 * @param ipr
	 * @param oSource
	 * @param loaFace
	 * @param f
	 * @param lia
	 * @throws ChartException
	 */
	private void renderRiserCone3D(IPrimitiveRenderer ipr, Object oSource, List loaFace, Fill f, LineAttributes lia,
			DataPointHints dpha) throws ChartException {
		if (loaFace == null || loaFace.size() != 2) {
			throw new IllegalArgumentException();
		}

		// Create a sub rendering cache to save all polygons of this data point.
		DeferredCache subCache = createSubDeferredCache4Cone3D(ipr, oSource, loaFace, f);

		Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class);
		final int size = 30;

		Location3D[] locations = (Location3D[]) loaFace.get(0);
		Point2D.Double pointA = new Point2D.Double(locations[0].getX(), locations[0].getZ());
		Point2D.Double pointB = new Point2D.Double(locations[2].getX(), locations[2].getZ());
		Point2D.Double[] ovals = computeOvalPoints(size, pointA, pointB);
		final double yBottom = locations[0].getY();
		final Location3D pTop = ((Location3D[]) loaFace.get(1))[0];

		Location3D[] bottomPoints = new Location3D[size];
		// Fill the body
		for (int i = 0; i < size; i++) {
			bottomPoints[i] = goFactory.createLocation3D(ovals[i].getX(), yBottom, ovals[i].getY());
			int preSize = i - 1;
			if (i == 0) {
				preSize = size - 1;
				bottomPoints[preSize] = goFactory.createLocation3D(ovals[preSize].getX(), yBottom,
						ovals[preSize].getY());
			}
			if (pTop.getY() - yBottom > 0) {
				pre.setPoints3D(new Location3D[] { bottomPoints[i], bottomPoints[preSize], pTop, });
			} else {
				pre.setPoints3D(new Location3D[] { bottomPoints[i], pTop, bottomPoints[preSize] });
			}
			pre.setBackground(f);
			pre.setDoubleSided(false);
			subCache.addPlane(pre, PrimitiveRenderEvent.FILL);
		}

		// Fill the bottom face
		pre.setDoubleSided(false);
		pre.setPoints3D(bottomPoints, pTop.getY() - yBottom < 0);
		pre.setOutline(lia);
		pre.setBackground(f);
		subCache.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);

		// Add interactivity support for Core body. Support for Tube top/bottom
		// face have been added in main renderSeries method
		if (isInteractivityEnabled()) {
			// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
			final EList<Trigger> elTriggers = getSeries().getTriggers();
			if (!elTriggers.isEmpty()) {
				final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(getSeries(), dpha);
				for (int i = 0; i < bottomPoints.length; i++) {
					final InteractionEvent iev = createEvent(iSource, elTriggers, ipr);
					iev.setCursor(getSeries().getCursor());
					final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createSeries(getSeries()), Polygon3DRenderEvent.class);
					int preSize = i == 0 ? size - 1 : i - 1;
					pre3d.setPoints3D(new Location3D[] { bottomPoints[preSize], bottomPoints[i], pTop });
					final Location panningOffset = getPanningOffset();

					if (get3DEngine().processEvent(pre3d, panningOffset.getX(), panningOffset.getY()) != null) {
						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}
				}
			}
		}
	}

	private DeferredCache createSubDeferredCache4Cone3D(IPrimitiveRenderer ipr, Object oSource, List loaFace, Fill f)
			throws ChartException {
		DeferredCache subCache = dc.deriveNewDeferredCache();
		subCache.setAntialiasing(true);
		Location3D[] l3d = new Location3D[3];
		for (int k = 0; k < 3; k++) {
			l3d[k] = goFactory.createLocation3D(0, 0, 0);
		}
		Location3D[] l0 = (Location3D[]) loaFace.get(0);
		Location3D[] l1 = (Location3D[]) loaFace.get(1);
		l3d[0].set(l0[0].getX(), l0[0].getY(), l0[0].getZ());
		l3d[1].set(l0[2].getX(), l0[2].getY(), l0[2].getZ());
		l3d[2].set(l1[0].getX(), l1[0].getY(), l1[0].getZ());

		Polygon3DRenderEvent pre3dEvent = ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class);
		pre3dEvent.setEnable(false);
		pre3dEvent.setDoubleSided(false);
		pre3dEvent.setOutline(null);
		pre3dEvent.setPoints3D(l3d);
		pre3dEvent.setBackground(f);

		Object event = dc.addPlane(pre3dEvent, PrimitiveRenderEvent.FILL);
		if (event instanceof WrappedInstruction) {
			((WrappedInstruction) event).setSubDeferredCache(subCache);
		}
		// Restore the default value.
		pre3dEvent.reset();
		return subCache;
	}

	/**
	 * Computes the points array to simulate an oval
	 *
	 * @param size   the points size, to determine the smoothness
	 * @param pointA the first point of the oval bounds
	 * @param pointB the diagonal point of the first point
	 * @return points array
	 */
	private static Point2D.Double[] computeOvalPoints(int size, Point2D.Double pointA, Point2D.Double pointB) {
		Point2D.Double original = new Point2D.Double();
		original.x = (pointA.x + pointB.x) / 2;
		original.y = (pointA.y + pointB.y) / 2;
		double width = Math.abs(pointA.x - pointB.x);
		double height = Math.abs(pointA.y - pointB.y);
		return computeOvalPoints(size, width, height, original);
	}

	private static Point2D.Double[] computeOvalPoints(int size, double width, double height, Point2D.Double original) {
		if (size <= 0 || width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		final Point2D.Double[] points = new Point2D.Double[size];
		final double interval = Math.PI * 2 / size;
		final double dWidth = width / 2;
		final double dHeight = height / 2;
		double degree = 0;
		for (int i = 0; i < size; i++, degree += interval) {
			points[i] = new Point2D.Double(dWidth * Math.cos(degree) + original.x,
					dHeight * Math.sin(degree) + original.y);
		}
		return points;
	}

	/**
	 * Compute the height of bottom oval for cone renderer.
	 *
	 * @param iSeriesIndex     the index of which series.
	 * @param defaultThickness the specified thickness.
	 * @param loaFrontFace     locations points of cone.
	 * @param dValue           width of height of series.
	 * @param isStacked        stacked flag.
	 * @return ralted height of bottom oval for cone.
	 */
	private double computeBottomOvalHeightOfCone(int iSeriesIndex, double defaultThickness, Location[] loaFrontFace,
			double dValue, boolean isStacked) {
		double width = 0d;
		double initialWidth = 0d;
		StackedSizeHints ssh = getCurrentStackedSizeHints(iSeriesIndex);
		if (isStacked) {
			width = isTransposed() ? loaFrontFace[0].getY() - loaFrontFace[3].getY()
					: loaFrontFace[3].getX() - loaFrontFace[0].getX();
		} else {
			width = isTransposed() ? loaFrontFace[2].getY() - loaFrontFace[0].getY()
					: loaFrontFace[2].getX() - loaFrontFace[0].getX();
		}

		if (dValue >= 0) {
			if (Double.isNaN(ssh.getInitialPositiveConeBottomWidth())) {
				ssh.setInitialPositiveConeBottomWidth(width);
			}
			initialWidth = ssh.getInitialPositiveConeBottomWidth();
		} else {
			if (Double.isNaN(ssh.getInitialNegativeConeBottomWidth())) {
				ssh.setInitialNegativeConeBottomWidth(width);
			}
			initialWidth = ssh.getInitialNegativeConeBottomWidth();
		}

		double value = defaultThickness * width / initialWidth;

		// Make it has min value 2.
		return value < 2 ? 2 : value;
	}

	/**
	 * The class stores top and bottom size of series for stacked cases.
	 *
	 */
	public static class StackedSizeHints {

		/** The last positive bottom size of series. */
		public double fdLastPositiveBottom = Double.NaN;

		/**
		 * The remained positive size whose value equals positive total size subtract
		 * size of processed series.
		 */
		public double fdRemainedPositiveTotal = Double.NaN;

		/** The last negative bottom size of seires. */
		public double fdLastNegativeBottom = Double.NaN;

		/**
		 * The remained negative size whose value equals negative total size subtract
		 * size of processed series.
		 */
		public double fdRemainedNegativeTotal = Double.NaN;

		/** The initial positive bottom width of cone. */
		private double fdInitialPositiveConeBottomWidth = Double.NaN;

		/** The initial negative bottom width of cone. */
		private double fdInitialNegativeConeBottomWidth = Double.NaN;

		/** The initial bouds of triangle. */
		private Bounds fbInitialTriangleBounds = null;

		public double getLastPositiveBottom() {
			return fdLastPositiveBottom;
		}

		public void setLastPositiveBottom(double fdPositiveDelta) {
			this.fdLastPositiveBottom = fdPositiveDelta;
		}

		public double getRemainedPositiveTotal() {
			return fdRemainedPositiveTotal;
		}

		public void setRemainedPositiveTotal(double fdRemainedPositiveTotal) {
			this.fdRemainedPositiveTotal = fdRemainedPositiveTotal;
		}

		public double getLastNegativeBottom() {
			return fdLastNegativeBottom;
		}

		public void setLastNegativeBottom(double fdNegativeDelta) {
			this.fdLastNegativeBottom = fdNegativeDelta;
		}

		public double getRemainedNegativeTotal() {
			return fdRemainedNegativeTotal;
		}

		public void setRemainedNegativeTotal(double fdRemainedNegativeTotal) {
			this.fdRemainedNegativeTotal = fdRemainedNegativeTotal;
		}

		public double getInitialPositiveConeBottomWidth() {
			return fdInitialPositiveConeBottomWidth;
		}

		public void setInitialPositiveConeBottomWidth(double dInitialPositiveConeBottomWidth) {
			this.fdInitialPositiveConeBottomWidth = dInitialPositiveConeBottomWidth;
		}

		public double getInitialNegativeConeBottomWidth() {
			return fdInitialNegativeConeBottomWidth;
		}

		public void setInitialNegativeConeBottomWidth(double dInitialNegativeConeBottomWidth) {
			this.fdInitialNegativeConeBottomWidth = dInitialNegativeConeBottomWidth;
		}

		public Bounds getInitialBounds() {
			return fbInitialTriangleBounds;
		}

		public void setInitialBounds(Bounds initialBounds) {
			this.fbInitialTriangleBounds = initialBounds;
		}
	}

	/**
	 * Compute thickness of top and bottom with specified triangle 2D locations.
	 *
	 * @param loaFrontFace     specified locations.
	 * @param dWidth           largest width.
	 * @param dHeight          largest height.
	 * @param dSeriesThickness standard series thickness.
	 * @return array of thickness, first element is top side thickness, second
	 *         element is bottom side thickness.
	 */
	private double[] computeThicknessesWithTriangle2D(Location[] loaFrontFace, double dWidth, double dHeight,
			double dSeriesThickness) {

		double topWidth = 0d;
		double bottomWidth = 0d;
		if (!isTransposed()) {
			if (loaFrontFace.length == 4) {
				topWidth = loaFrontFace[2].getX() - loaFrontFace[1].getX();
				bottomWidth = loaFrontFace[3].getX() - loaFrontFace[0].getX();
			} else {
				bottomWidth = loaFrontFace[2].getX() - loaFrontFace[0].getX();
			}
		} else if (loaFrontFace.length == 4) {
			topWidth = loaFrontFace[1].getY() - loaFrontFace[2].getY();
			bottomWidth = loaFrontFace[0].getY() - loaFrontFace[3].getY();
		} else {
			bottomWidth = loaFrontFace[0].getY() - loaFrontFace[2].getY();
		}
		double width = dWidth;
		if (isTransposed()) {
			width = dHeight;
		}
		double topThickness = (topWidth / width) * dSeriesThickness;
		double bottomThickness = (bottomWidth / width) * dSeriesThickness;

		return new double[] { topThickness, bottomThickness };
	}

	/**
	 * Adjust locations with triangle 2D.
	 *
	 * @param loaFrontFace     specified locations.
	 * @param dTopThickness    the thickness of top side.
	 * @param dBottomThickness the thickness of bottom side.
	 * @param dSeriesThickness standard thickness.
	 */
	private void adjustLocationsWithTriangle2D(Location[] loaFrontFace, double dTopThickness, double dBottomThickness,
			double dSeriesThickness) {
		double topOffset = (dSeriesThickness - dTopThickness) / 2;
		double bottomOffset = (dSeriesThickness - dBottomThickness) / 2;

		if (loaFrontFace.length == 4) {
			loaFrontFace[0].setX(loaFrontFace[0].getX() + bottomOffset);
			loaFrontFace[0].setY(loaFrontFace[0].getY() - bottomOffset);
			loaFrontFace[1].setX(loaFrontFace[1].getX() + topOffset);
			loaFrontFace[1].setY(loaFrontFace[1].getY() - topOffset);
			loaFrontFace[2].setX(loaFrontFace[2].getX() + topOffset);
			loaFrontFace[2].setY(loaFrontFace[2].getY() - topOffset);
			loaFrontFace[3].setX(loaFrontFace[3].getX() + bottomOffset);
			loaFrontFace[3].setY(loaFrontFace[3].getY() - bottomOffset);
		} else {
			loaFrontFace[0].setX(loaFrontFace[0].getX() + bottomOffset);
			loaFrontFace[0].setY(loaFrontFace[0].getY() - bottomOffset);
			loaFrontFace[1].setX(loaFrontFace[1].getX() + topOffset);
			loaFrontFace[1].setY(loaFrontFace[1].getY() - topOffset);
			loaFrontFace[2].setX(loaFrontFace[2].getX() + bottomOffset);
			loaFrontFace[2].setY(loaFrontFace[2].getY() - bottomOffset);
		}
	}

	/**
	 * Compute correct top side and bottom side thickness with triangle 2D.
	 *
	 * @param nSides           computed sides of triangle.
	 * @param nCurrentSide     current side.
	 * @param dTopThickness    top side thickness.
	 * @param dBottomThickness bottom side thickness.
	 * @return
	 */
	private double[] computeCorrectThicknessesWithTriangle2D(int nSides, int nCurrentSide, double dTopThickness,
			double dBottomThickness) {
		double[] thicknesses = new double[2];
		thicknesses[0] = dTopThickness;
		thicknesses[1] = dBottomThickness;

		if (nSides == 3) {
			switch (nCurrentSide) {
			case 0:
				// Use default value.
				break;
			case 1:
				thicknesses[0] = dBottomThickness;
				thicknesses[1] = dTopThickness;
				break;
			case 2:
				thicknesses[0] = dBottomThickness;
				thicknesses[1] = dBottomThickness;
				break;
			}
		} else if (nSides == 4) {
			switch (nCurrentSide) {
			case 0:
				// Use default value.
				break;
			case 1:
				thicknesses[0] = dTopThickness;
				thicknesses[1] = dTopThickness;
				break;
			case 2:
				thicknesses[0] = dBottomThickness;
				thicknesses[1] = dTopThickness;
				break;
			case 3:
				thicknesses[0] = dBottomThickness;
				thicknesses[1] = dBottomThickness;
				break;
			}
		}

		return thicknesses;
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
		}
	}
}
