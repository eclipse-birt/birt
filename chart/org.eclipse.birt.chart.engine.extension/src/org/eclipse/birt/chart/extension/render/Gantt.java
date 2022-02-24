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
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.GanttDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.IAxesDecorator;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Gantt
 */
public final class Gantt extends AxesRenderer implements IAxesDecorator {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public Gantt() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.
	 * chart.output.IRenderer, Chart.Plot)
	 */
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		if (cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.gantt.dimension", //$NON-NLS-1$
					new Object[] { cwa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale()));

		// OBTAIN AN INSTANCE OF THE SERIES MODEL
		final GanttSeries gs = (GanttSeries) getSeries();

		// TEST VISIBILITY
		if (!gs.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// SETUP VARS USED IN RENDERING
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		final DataPointHints[] dpha = srh.getDataPoints();
		final LineAttributes lia = goFactory.copyOf(gs.getConnectionLine());
		final LineAttributes outlinelia = gs.getOutline();
		final int iLineWidth = lia.getThickness();
		final Marker mStart = gs.getStartMarker();
		final Marker mEnd = gs.getEndMarker();
		final boolean bTransposed = isTransposed();

		float[] faX = new float[dpha.length];
		float[][] faY = new float[dpha.length][2];
		Location lo;
		Location loStart = null, loEnd = null;
		Location[] loaLine = null;
		LineRenderEvent lre;
		PolygonRenderEvent pre;

		// SETUP THE MARKER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { gs }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && gs.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) gs.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(gs);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { gs, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			updateTranslucency(fPaletteEntry, gs);
		}

		Fill fLineFill;
		if (gs.isPaletteLineColor()) {
			fLineFill = fPaletteEntry;
			lia.setColor(FillUtil.getColor(fPaletteEntry));
		} else {
			Fill outlineFill = gs.getOutlineFill();
			fLineFill = lia.getColor() == null ? outlineFill : lia.getColor();
			if (outlineFill != null) {
				lia.setColor(FillUtil.getColor(outlineFill));
			}
		}

		renderClipping(ipr, getPlotBounds());
		final AutoScale scale = getInternalOrthogonalAxis().getScale();

		// RENDER THE LINE FOR EACH DATA ELEMENT IN THE SERIES
		for (int i = 0; i < dpha.length; i++) {
			GanttEntry ge = (GanttEntry) dpha[i].getOrthogonalValue();

			if (!isValidGanttEntry(ge)) {
				continue;
			}

			if (checkEntryInRange(se, scale.getMinimum(), scale.getMaximum()) > 0) {
				dpha[i].markOutside();
			}

			CDateTime geStart = ge.getStart();
			CDateTime geEnd = ge.getEnd();

			double dX = 0, dY = 0;
			double dStart = 0, dEnd = 0;

			// OBTAIN THE CO-ORDINATES OF THE DATA POINT
			lo = dpha[i].getLocation();
			dX = (int) lo.getX();
			dY = (int) lo.getY();

			if (srh.isCategoryScale()) {
				double fSize = dpha[i].getSize();

				if (bTransposed) {
					dY += fSize / 2;
				} else {
					dX += fSize / 2;
				}
			}

			// Set null values. Note that two nulls will be skipped.
			if (geStart != null) {
				dStart = srh.getLocationOnOrthogonal(geStart);
			}
			if (geEnd != null) {
				dEnd = srh.getLocationOnOrthogonal(geEnd);
			} else {
				dEnd = dStart;
			}
			if (geStart == null) {
				dStart = dEnd;
			}

			if (loStart == null) // ONE TIME CREATION
			{
				loStart = goFactory.createLocation(0, 0);
				loEnd = goFactory.createLocation(0, 0);
			}

			// BRANCH OFF HERE IF THE PLOT IS TRANSPOSED (AXES ARE SWAPPED)
			if (cwa.isTransposed()) {
				loStart.set(dStart, dY);
				loEnd.set(dEnd, dY);

				faX[i] = (float) dY;
				faY[i][0] = (float) dStart;
				faY[i][1] = (float) dEnd;

				loaLine = new Location[4];
				loaLine[0] = goFactory.createLocation(dStart, dY + iLineWidth / 2.0);
				loaLine[1] = goFactory.createLocation(dStart, dY - iLineWidth / 2.0);
				loaLine[2] = goFactory.createLocation(dEnd, dY - iLineWidth / 2.0);
				loaLine[3] = goFactory.createLocation(dEnd, dY + iLineWidth / 2.0);
			} else
			// STANDARD PROCESSING FOR REGULAR NON-TRANSPOSED AXES (NOTE:
			// SYMMETRIC CODE)
			{
				loStart.set(dX, dStart);
				loEnd.set(dX, dEnd);

				faX[i] = (float) dX;
				faY[i][0] = (float) dStart;
				faY[i][1] = (float) dEnd;

				loaLine = new Location[4];
				loaLine[0] = goFactory.createLocation(dX + iLineWidth / 2.0, dStart);
				loaLine[1] = goFactory.createLocation(dX - iLineWidth / 2.0, dStart);
				loaLine[2] = goFactory.createLocation(dX - iLineWidth / 2.0, dEnd);
				loaLine[3] = goFactory.createLocation(dX + iLineWidth / 2.0, dEnd);
			}

			if (bPaletteByCategory) {
				fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
			} else {
				fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			}
			updateTranslucency(fPaletteEntry, gs);

			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dpha[i]);

			// DRAW LINE
			if (lia.isVisible() && geStart != null && geEnd != null) {
				if (gs.isPaletteLineColor()) {
					fLineFill = fPaletteEntry;
					lia.setColor(FillUtil.getColor(fPaletteEntry));
				}

				if ((iLineWidth == 1) || (lia.getStyle() != LineStyle.SOLID_LITERAL)) {
					lre = ((EventObjectCache) ipr).getEventObject(
							WrappedStructureSource.createSeriesDataPoint(gs, dpha[i]), LineRenderEvent.class);
					lre.setLineAttributes(lia);
					lre.setStart(loStart);
					lre.setEnd(loEnd);
					ipr.drawLine(lre);
					renderInteractivity(ipr, dpha[i], lre);
				} else {
					pre = ((EventObjectCache) ipr).getEventObject(
							WrappedStructureSource.createSeriesDataPoint(gs, dpha[i]), PolygonRenderEvent.class);
					pre.setPoints(loaLine);
					pre.setBackground(fLineFill);
					ipr.fillPolygon(pre);
					renderInteractivity(ipr, dpha[i], pre);
				}
			}

			// DRAW OUTLINE
			if (outlinelia.isVisible() && geStart != null && geEnd != null) {
				pre = ((EventObjectCache) ipr).getEventObject(WrappedStructureSource.createSeriesDataPoint(gs, dpha[i]),
						PolygonRenderEvent.class);
				pre.setPoints(loaLine);
				pre.setOutline(outlinelia);
				ipr.drawPolygon(pre);
			}

			// Render the start marker
			if (geStart != null && mStart.isVisible() || dpha[i].isOutside()) {
				Location loStartMarker = loStart.copyInstance();
				Position mStartPosition = transposePosition(gs.getStartMarkerPosition());

				switch (mStartPosition.getValue()) {
				case Position.ABOVE:
					if (bTransposed) {
						loStartMarker.set(loStart.getX(), loStart.getY() - mStart.getSize());
					}
					break;
				case Position.BELOW:
					if (bTransposed) {
						loStartMarker.set(loStart.getX(), loStart.getY() + mStart.getSize());
					}
					break;
				// case Position.RIGHT :
				// if ( !bTransposed )
				// {
				// loStartMarker.set( loStart.getX( )
				// + mStart.getSize( ), loStart.getY( ) );
				// }
				// break;
				// case Position.LEFT :
				// if ( !bTransposed )
				// {
				// loStartMarker.set( loStart.getX( )
				// - mStart.getSize( ), loStart.getY( ) );
				// }
				// break;
				}

				Fill startFill = fPaletteEntry;

				if (fPaletteEntry instanceof MultipleFill && ((MultipleFill) fPaletteEntry).getFills().size() > 0) {
					startFill = ((MultipleFill) fPaletteEntry).getFills().get(0);
				}

				renderMarker(gs, ipr, mStart, loStartMarker, outlinelia, startFill, dpha[i], null, true, false);
			}

			// Render the end marker
			if (geEnd != null && mEnd.isVisible() || dpha[i].isOutside()) {
				Location loEndMarker = loEnd.copyInstance();
				Position mEndPosition = transposePosition(gs.getEndMarkerPosition());

				switch (mEndPosition.getValue()) {
				case Position.ABOVE:
					if (bTransposed) {
						loEndMarker.set(loEnd.getX(), loEnd.getY() - mEnd.getSize());
					}
					break;
				case Position.BELOW:
					if (bTransposed) {
						loEndMarker.set(loEnd.getX(), loEnd.getY() + mEnd.getSize());
					}
					break;
				// case Position.RIGHT :
				// if ( !bTransposed )
				// {
				// loEndMarker.set( loEnd.getX( ) + mEnd.getSize( ),
				// loEnd.getY( ) );
				// }
				// break;
				// case Position.LEFT :
				// if ( !bTransposed )
				// {
				// loEndMarker.set( loEnd.getX( ) - mEnd.getSize( ),
				// loEnd.getY( ) );
				// }
				// break;
				}

				Fill endFill = fPaletteEntry;

				if (fPaletteEntry instanceof MultipleFill && ((MultipleFill) fPaletteEntry).getFills().size() > 1) {
					endFill = ((MultipleFill) fPaletteEntry).getFills().get(1);
				}

				renderMarker(gs, ipr, mEnd, loEndMarker, outlinelia, endFill, dpha[i], null, true, false);
			}

			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dpha[i]);
		}

		// DATA POINT RELATED VARIABLES ARE INITIALIZED HERE
		Label laDataPoint = srh.getLabelAttributes(gs);

		if (laDataPoint.isVisible()) {
			Position pDataPoint = srh.getLabelPosition(gs);
			Location loDataPoint = goFactory.createLocation(0, 0);

			final boolean useDecorationLabel = gs.isUseDecorationLabelValue();

			final double dLabelGap = Math.max(
					Math.max(mStart.isVisible() ? mStart.getSize() : 0, mEnd.isVisible() ? mEnd.getSize() : 0),
					(lia.isVisible() ? iLineWidth / 2 : 0) + (outlinelia.isVisible() ? outlinelia.getThickness() : 0));

			for (int i = 0; i < dpha.length; i++) {
				GanttEntry ge = (GanttEntry) dpha[i].getOrthogonalValue();

				if (ge == null || ge.getStart() == null || ge.getEnd() == null) {
					continue;
				}

				if (!isValidGanttEntry(ge) || dpha[i].isOutside()) {
					continue;
				}

				if (useDecorationLabel) {
					laDataPoint.getCaption().setValue(ge.getLabel());
				} else {
					laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());
				}

				switch (pDataPoint.getValue()) {
				case Position.ABOVE:
					if (!cwa.isTransposed()) {
						loDataPoint.set(faX[i], faY[i][1] - mEnd.getSize() - p.getVerticalSpacing());
					} else {
						loDataPoint.set((faY[i][0] + faY[i][1]) / 2, faX[i] - dLabelGap - p.getVerticalSpacing());
					}
					break;
				case Position.BELOW:
					if (!cwa.isTransposed()) {
						loDataPoint.set(faX[i], faY[i][0] + mStart.getSize() + p.getVerticalSpacing());
					} else {
						loDataPoint.set((faY[i][0] + faY[i][1]) / 2, faX[i] + dLabelGap + p.getVerticalSpacing());
					}
					break;
				case Position.LEFT:
					if (!cwa.isTransposed()) {
						loDataPoint.set(faX[i] - dLabelGap - p.getHorizontalSpacing(), (faY[i][0] + faY[i][1]) / 2);
					} else {
						loDataPoint.set(faY[i][0] - dLabelGap - p.getHorizontalSpacing(), faX[i]);
					}
					break;
				case Position.RIGHT:
					if (!cwa.isTransposed()) {
						loDataPoint.set(faX[i] + dLabelGap + p.getHorizontalSpacing(), (faY[i][0] + faY[i][1]) / 2);
					} else {
						loDataPoint.set(faY[i][1] + dLabelGap + p.getHorizontalSpacing(), faX[i]);
					}
					break;
				case Position.INSIDE:
					if (!cwa.isTransposed()) {
						loDataPoint.set(faX[i], (faY[i][0] + faY[i][1]) / 2);
					} else {
						loDataPoint.set((faY[i][0] + faY[i][1]) / 2, faX[i]);
					}
					break;
				default:
					throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
							"exception.illegal.datapoint.position.gantt", //$NON-NLS-1$
							new Object[] { pDataPoint.getName() },
							Messages.getResourceBundle(getRunTimeContext().getULocale()));
				}

				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						laDataPoint);

				if (laDataPoint.isVisible()) {
					renderLabel(WrappedStructureSource.createSeriesDataPoint(gs, dpha[i]),
							TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, pDataPoint, loDataPoint, null);
				}

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
						laDataPoint);
			}
		}

		restoreClipping(ipr);
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
		final ClientArea ca = lg.getClientArea();
		final LineAttributes lia = ca.getOutline();
		final GanttSeries gs = (GanttSeries) getSeries();

		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				RectangleRenderEvent.class);
		rre.setBackground(ca.getBackground());
		rre.setOutline(lia);
		rre.setBounds(bo);
		ipr.fillRectangle(rre);

		LineAttributes liaMarker = gs.getConnectionLine();

		LineAttributes outlinelia = gs.getOutline();

		PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				PolygonRenderEvent.class);
		Location[] points = new Location[] {
				goFactory.createLocation(bo.getLeft() + 1,
						bo.getTop() + bo.getHeight() / 2d + liaMarker.getThickness() / 2d),
				goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1,
						bo.getTop() + bo.getHeight() / 2d + liaMarker.getThickness() / 2d),
				goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1,
						bo.getTop() + bo.getHeight() / 2d - liaMarker.getThickness() / 2d),
				goFactory.createLocation(bo.getLeft() + 1,
						bo.getTop() + bo.getHeight() / 2d - liaMarker.getThickness() / 2d) };

		pre.setPoints(points);
		pre.setOutline(outlinelia);

		// Fill.
		if (liaMarker.isVisible()) {
			if (gs.isPaletteLineColor()) {
				liaMarker = goFactory.copyOf(liaMarker);
				liaMarker.setColor(FillUtil.getColor(fPaletteEntry));
				pre.setBackground(fPaletteEntry);
			} else {
				Fill outlineFill = gs.getOutlineFill();
				Fill fLineFill = liaMarker.getColor() == null ? outlineFill : liaMarker.getColor();
				lia.setColor(FillUtil.getColor(outlineFill));
				pre.setBackground(fLineFill);
			}

			ipr.fillPolygon(pre);
		}

		// DRAW OUTLINE
		if (outlinelia.isVisible()) {
			ipr.drawPolygon(pre);
		}

		double width = bo.getWidth() / getDeviceScale();
		double height = bo.getHeight() / getDeviceScale();
		int markerSize = (int) (((width > height ? height : width) - 2) / 2);
		if (markerSize <= 0) {
			markerSize = 1;
		}

		Marker mStart = gs.getStartMarker();
		DataPointHints dph = createDummyDataPointHintsForLegendItem();
		if (mStart != null && mStart.isVisible()) {
			Location loStartMarker = goFactory.createLocation(bo.getLeft() + 1, bo.getTop() + bo.getHeight() / 2);
			Position mStartPosition = transposePosition(gs.getStartMarkerPosition());

			switch (mStartPosition.getValue()) {
			case Position.ABOVE:
				if (isTransposed()) {
					loStartMarker.set(loStartMarker.getX(), loStartMarker.getY() - markerSize);
				}
				break;
			case Position.BELOW:
				if (isTransposed()) {
					loStartMarker.set(loStartMarker.getX(), loStartMarker.getY() + markerSize);
				}
				break;
			}

			Fill startFill = fPaletteEntry;

			if (fPaletteEntry instanceof MultipleFill && ((MultipleFill) fPaletteEntry).getFills().size() > 0) {
				startFill = ((MultipleFill) fPaletteEntry).getFills().get(0);
			}

			renderMarker(lg, ipr, mStart, loStartMarker, gs.getOutline(), startFill, dph, Integer.valueOf(markerSize),
					false, false);
		}

		Marker mEnd = gs.getEndMarker();

		if (mEnd != null && mEnd.isVisible()) {
			Location loEndMarker = goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1,
					bo.getTop() + bo.getHeight() / 2);
			Position mEndPosition = transposePosition(gs.getEndMarkerPosition());

			switch (mEndPosition.getValue()) {
			case Position.ABOVE:
				if (isTransposed()) {
					loEndMarker.set(loEndMarker.getX(), loEndMarker.getY() - mEnd.getSize());
				}
				break;
			case Position.BELOW:
				if (isTransposed()) {
					loEndMarker.set(loEndMarker.getX(), loEndMarker.getY() + mEnd.getSize());
				}
				break;
			}

			Fill endFill = fPaletteEntry;

			if (fPaletteEntry instanceof MultipleFill && ((MultipleFill) fPaletteEntry).getFills().size() > 1) {
				endFill = ((MultipleFill) fPaletteEntry).getFills().get(1);
			}

			renderMarker(lg, ipr, mEnd, loEndMarker, gs.getOutline(), endFill, dph, Integer.valueOf(markerSize), false,
					false);
		}
	}

	private Position transposePosition(Position p) {
		if (isTransposed()) {
			switch (p.getValue()) {
			case Position.ABOVE:
				return Position.RIGHT_LITERAL;
			case Position.BELOW:
				return Position.LEFT_LITERAL;
			case Position.RIGHT:
				return Position.ABOVE_LITERAL;
			case Position.LEFT:
				return Position.BELOW_LITERAL;
			}
		}
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.
	 * model.attribute.Bounds, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// NOTE: This method is not used by the Gantt Renderer
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.IAxesDecorator#computeDecorationThickness(org.
	 * eclipse.birt.chart.device.IDisplayServer,
	 * org.eclipse.birt.chart.computation.withaxes.OneAxis)
	 */
	public double[] computeDecorationThickness(IDisplayServer xs, OneAxis ax) throws ChartException {
		GanttSeries gs = (GanttSeries) getSeries();
		Label la = gs.getDecorationLabel();

		if (la != null && la.isVisible()) {
			int iOrientation = ax.getOrientation();

			if (iOrientation == IConstants.VERTICAL) {
				double dW, dMaxW = 0;

				GanttDataSet gds = (GanttDataSet) gs.getDataSet();
				DataSetIterator dsi = new DataSetIterator(gds);

				la = goFactory.copyOf(la);
				int pos = transposePosition(gs.getDecorationLabelPosition()).getValue();
				dsi.reset();

				String sValue = null;

				while (dsi.hasNext()) {
					Object o = dsi.next();
					if (o != null) {
						sValue = ((GanttEntry) o).getLabel();
					}

					if (sValue != null && sValue.length() > 0) {
						la.getCaption().setValue(sValue);
						dW = cComp.computeWidth(xs, la);
						if (dW > dMaxW) {
							dMaxW = dW;
						}
					}
				}

				if (pos == Position.LEFT) {
					return new double[] { dMaxW, 0 };
				}
				return new double[] { 0, dMaxW };
			} else if (iOrientation == IConstants.HORIZONTAL) {
				double dH, dMaxH = 0;

				GanttDataSet gds = (GanttDataSet) gs.getDataSet();
				DataSetIterator dsi = new DataSetIterator(gds);

				la = goFactory.copyOf(la);
				int pos = transposePosition(gs.getDecorationLabelPosition()).getValue();
				dsi.reset();

				String sValue = null;

				while (dsi.hasNext()) {
					Object o = dsi.next();

					if (o != null) {
						sValue = ((GanttEntry) o).getLabel();
					}

					if (sValue != null && sValue.length() > 0) {
						la.getCaption().setValue(sValue);
						dH = cComp.computeHeight(xs, la);
						if (dH > dMaxH) {
							dMaxH = dH;
						}
					}
				}

				if (pos == Position.BELOW) {
					return new double[] { 0, dMaxH };
				}
				return new double[] { dMaxH, 0 };
			}
		}

		return new double[] { 0, 0 };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#getAxesDecorator(org.eclipse.birt.
	 * chart.computation.withaxes.OneAxis)
	 */
	public IAxesDecorator getAxesDecorator(OneAxis ax) {
		ChartWithAxes cwa = (ChartWithAxes) getModel();
		Axis baseAxis = cwa.getPrimaryBaseAxes()[0];

		// only decorate base axis
		if (ax.getModelAxis() == baseAxis) {
			GanttSeries gs = (GanttSeries) getSeries();
			Label la = gs.getDecorationLabel();

			if (la != null && la.isVisible()) {
				return this;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.IAxesDecorator#decorateAxes(org.eclipse.birt.
	 * chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints,
	 * org.eclipse.birt.chart.computation.withaxes.OneAxis)
	 */
	public void decorateAxes(IPrimitiveRenderer ipr, ISeriesRenderingHints isrh, OneAxis ax) throws ChartException {
		GanttSeries gs = (GanttSeries) getSeries();
		Label la = gs.getDecorationLabel();
		if (la == null || !la.isVisible()) {
			return;
		}

		// this is the primary base axis.
		final DataPointHints[] dpha = isrh.getDataPoints();
		SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
		int pos = transposePosition(gs.getDecorationLabelPosition()).getValue();
		int iOrientation = ax.getOrientation();

		// translate to runtime constants
		if (iOrientation == IConstants.VERTICAL) {
			if (pos == Position.LEFT) {
				pos = IConstants.LEFT;
			} else {
				pos = IConstants.RIGHT;
			}
		} else {
			if (pos == Position.BELOW) {
				pos = IConstants.BELOW;
			} else {
				pos = IConstants.ABOVE;
			}
		}

		la = goFactory.copyOf(la);
		Location loLabel = goFactory.createLocation(0, 0);
		final double axisCoord = ax.getAxisCoordinate();

		final int iMajorTickStyle = ax.getGrid().getTickStyle(IConstants.MAJOR);
		double dXTick1 = ((iMajorTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT)
				? (axisCoord - IConstants.TICK_SIZE - 1)
				: (axisCoord - 1);
		double dXTick2 = ((iMajorTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT)
				? (axisCoord + IConstants.TICK_SIZE + 1)
				: (axisCoord + 1);

		TextRenderEvent tre;

		if (iOrientation == IConstants.VERTICAL) {
			if (pos == IConstants.LEFT) {
				loLabel.setX(dXTick1);
			} else {
				loLabel.setX(dXTick2);
			}
		} else {
			if (pos == IConstants.ABOVE) {
				loLabel.setY(dXTick1);
			} else {
				loLabel.setY(dXTick2);
			}
		}

		// render decoration label
		for (int i = 0; i < dpha.length; i++) {
			GanttEntry ge = (GanttEntry) dpha[i].getOrthogonalValue();

			if (isValidGanttEntry(ge) && ge.getLabel() != null && ge.getStart() != null && ge.getEnd() != null) {
				la.getCaption().setValue(ge.getLabel());

				double fSize = srh.isCategoryScale() ? dpha[i].getSize() / 2 : 0;

				if (iOrientation == IConstants.VERTICAL) {
					loLabel.setY(dpha[i].getLocation().getY() + fSize);
				} else {
					loLabel.setX(dpha[i].getLocation().getX() + fSize);
				}

				tre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(ax.getModelAxis()),
						TextRenderEvent.class);
				tre.setTextPosition(pos);
				tre.setLocation(loLabel);
				tre.setLabel(la);
				tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
				ipr.drawText(tre);
			}
		}
	}

	private boolean isValidGanttEntry(GanttEntry entry) {
		return entry != null && entry.isValid();
	}

	protected int checkEntryInRange(Object entry, Object min, Object max) {
		if (entry instanceof GanttEntry) {
			GanttEntry ge = (GanttEntry) entry;
			if (ge.getStart().before(Methods.asDateTime(min))) {
				return 1;
			}
			if (ge.getEnd().after(Methods.asDateTime(max))) {
				return 2;
			}
			return 0;
		}
		return super.checkEntryInRange(entry, min, max);
	}
}
