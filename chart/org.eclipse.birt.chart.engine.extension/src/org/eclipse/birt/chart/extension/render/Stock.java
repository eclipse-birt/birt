/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.StackGroup;
import org.eclipse.birt.chart.computation.withaxes.StackedSeriesLookup;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Stock
 */
public final class Stock extends AxesRenderer {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$
	private final static int MIN_HOTSPOT_WIDTH = 12;

	/**
	 * The constructor.
	 */
	public Stock() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.AxesRenderer#renderSeries(org.eclipse.birt.
	 * chart.output.IRenderer, Chart.Plot)
	 */
	@Override
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		if (cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.stock.dimension", //$NON-NLS-1$
					new Object[] { cwa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		// Only vertical orientation is supported for stock series
		if (isTransposed()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.no.transposed.stock.chart", //$NON-NLS-1$
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, vex);
		}

		// SCALE VALIDATION
//		if ( !srh.isCategoryScale( ) )
//		{
//			throw new ChartException( ChartEngineExtensionPlugin.ID,
//					ChartException.RENDERING,
//					"exception.xvalue.scale.stock", //$NON-NLS-1$
//					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
//		}

		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale()));

		// OBTAIN AN INSTANCE OF THE SERIES MODEL
		final StockSeries ss = (StockSeries) getSeries();

		// TEST VISIBILITY
		if (!ss.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// SETUP VARS USED IN RENDERING
		final double dSeriesThickness = srh.getSeriesThickness();
		final DataPointHints[] dpha = srh.getDataPoints();
		// !Already checked done by the stock entry.
		// validateNullDatapoint(dpha);

		final LineAttributes lia = ss.getLineAttributes();
		final double dUnitSpacing = (!cwa.isSetUnitSpacing()) ? 50 : cwa.getUnitSpacing(); // AS A PERCENTAGE OF A
		// QUARTER OF A UNIT
		// SIZE
		double dX = 0, dY = 0, dWidth = 0, dHeight = 0, dSpacing = 0;
		double dLow, dHigh, dOpen, dClose;
		Location lo, loStart = null, loEnd = null, loUpper = goFactory.createLocation(0, 0),
				loLower = goFactory.createLocation(0, 0);
		Location[] loaFrontFace = null;
		LineRenderEvent lre;

		// SETUP THE MARKER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		final SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { ss }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean bPaletteByCategory = isPaletteByCategory();
		int iThisSeriesIndex = -1;
		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			iThisSeriesIndex = sd.getRunTimeSeries().indexOf(ss);
			if (iThisSeriesIndex < 0) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
						"exception.missing.series.for.palette.index", //$NON-NLS-1$
						new Object[] { ss, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
			}
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
		}

		renderClipping(ipr, getPlotBounds());

		StackedSeriesLookup ssl;
		StackGroup sg;

		ssl = srh.getStackedSeriesLookup();
		sg = ssl.getStackGroup(ss);
		// The index of series value in current data point.
		int iSharedUnitIndex = (sg == null) ? 0 : sg.getSharedIndex();
		// The total series count.
		int iSharedUnitCount = (sg == null) ? 1 : sg.getSharedCount();
		if (iSharedUnitCount == 1) {
			iSharedUnitIndex = 0;
		}

		double[] faX = new double[dpha.length];
		double[] faY = new double[dpha.length];
		final AutoScale scale = getInternalOrthogonalAxis().getScale();

		// RENDER THE RISERS (BAR, TRIANGLE, ETC) FOR EACH DATA ELEMENT IN THE
		// SERIES
		for (int i = 0; i < dpha.length; i++) {
			StockEntry se = (StockEntry) dpha[i].getOrthogonalValue();
			if (!isValidEntry(se)) // NULL VALUE HANDLING
			{
				continue;
			}

			correctEntry(se);

			if (checkEntryInRange(se, scale.getMinimum(), scale.getMaximum()) > 0) {
				dpha[i].markOutside();
			}

			if (bPaletteByCategory) {
				fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
			} else {
				fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			}
			updateTranslucency(fPaletteEntry, ss);

			// OBTAIN THE CO-ORDINATES OF THE DATA POINT
			lo = dpha[i].getLocation();
			dX = lo.getX();
			dY = lo.getY();

			try {
				dLow = srh.getLocationOnOrthogonal(new Double(se.getLow()));
				dHigh = srh.getLocationOnOrthogonal(new Double(se.getHigh()));
				dOpen = srh.getLocationOnOrthogonal(new Double(se.getOpen()));
				dClose = srh.getLocationOnOrthogonal(new Double(se.getClose()));
			} catch (Exception ex) {
				logger.log(ex);
				continue;
			}

			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_ELEMENT, dpha[i], fPaletteEntry);
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_ELEMENT, dpha[i]);
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dpha[i]);

			// ADJUST CO-ORDINATES BASED ON EACH UNIT SHARE (DUE TO MULTIPLE
			// SERIES)
			dSpacing = (dpha[i].getSize() * dUnitSpacing) / 200;
			if (loStart == null) // ONE TIME CREATION
			{
				loStart = goFactory.createLocation(0, 0);
				loEnd = goFactory.createLocation(0, 0);
				loaFrontFace = new Location[4];
				for (int j = 0; j < 4; j++) {
					loaFrontFace[j] = goFactory.createLocation(0, 0);
				}
			}

			dWidth = dpha[i].getSize();
			dWidth -= 2 * dSpacing;
			dWidth /= iSharedUnitCount;

			if (cwa.getBaseAxes()[0].isCategoryAxis()) {
				dX += dSpacing + dWidth / 2 + iSharedUnitIndex * dWidth;
			}

			lre = ((EventObjectCache) ipr).getEventObject(WrappedStructureSource.createSeriesDataPoint(ss, dpha[i]),
					LineRenderEvent.class);
			if (ss.isShowAsBarStick()) {
				int stickLength = ss.getStickLength();

				// For stick sub type, the front face is used to create the hotspot
				// of the tooltip. To make it easier for user to show the tooltip, just
				// use topmost, bottommost, rightmost and leftmost point as the hotspot
				// boundaries. Use a cap width in case the stick length is too small.
				// #41900

				int hotspotHalfWidth = Math.min(stickLength, MIN_HOTSPOT_WIDTH / 2);
				double hotspotHigh = Math.max(dHigh, dLow);
				double hotspotLow = Math.min(dHigh, dLow);

				loaFrontFace[0].set(dX - hotspotHalfWidth, hotspotHigh);
				loaFrontFace[1].set(dX - hotspotHalfWidth, hotspotLow);
				loaFrontFace[2].set(dX + hotspotHalfWidth, hotspotLow);
				loaFrontFace[3].set(dX + hotspotHalfWidth, hotspotHigh);

				Location loStart2 = goFactory.createLocation(0, 0), loEnd2 = goFactory.createLocation(0, 0);

				loStart.set(dX - stickLength, dOpen);
				loEnd.set(dX + stickLength, dClose);

				loStart2.set(dX, dOpen);
				loEnd2.set(dX, dClose);

				loUpper.set(dX, dHigh > dLow ? dHigh : dLow);
				loLower.set(dX, dHigh < dLow ? dHigh : dLow);

				// UPPER-LOWER SEGMENT
				lre.setLineAttributes(lia);
				lre.setStart(loUpper);
				lre.setEnd(loLower);
				ipr.drawLine(lre);

				// OPEN SEGMENT
				lre.setStart(loStart);
				lre.setEnd(loStart2);
				ipr.drawLine(lre);

				// CLOSE SEGMENT
				lre.setStart(loEnd);
				lre.setEnd(loEnd2);
				ipr.drawLine(lre);
			} else {
				// STANDARD PROCESSING FOR REGULAR NON-TRANSPOSED AXES (NOTE:
				// SYMMETRIC CODE)
				{

					loStart.set(dX, dLow);
					loEnd.set(dX, dHigh);

					loaFrontFace[0].set(dX - dWidth / 2, dOpen);
					loaFrontFace[1].set(dX - dWidth / 2, dClose);
					loaFrontFace[2].set(dX + dWidth / 2, dClose);
					loaFrontFace[3].set(dX + dWidth / 2, dOpen);

					loUpper.set(dX, dOpen > dClose ? dOpen : dClose);
					loLower.set(dX, dOpen < dClose ? dOpen : dClose);
				}

				// UPPER SEGMENT
				lre.setLineAttributes(lia);
				lre.setStart(loStart);
				lre.setEnd(loUpper);
				ipr.drawLine(lre);

				// LOWER SEGMENT
				lre.setStart(loLower);
				lre.setEnd(loEnd);
				ipr.drawLine(lre);

				// RENDER THE RECTANGLE (EXTRUDED IF > 2D)
				renderPlane(ipr, WrappedStructureSource.createSeriesDataPoint(ss, dpha[i]), loaFrontFace,
						convertFill(fPaletteEntry, se.getClose() > se.getOpen()), lia, cwa.getDimension(),
						dSeriesThickness, true);
			}

			if (cwa.isTransposed()) {
				faX[i] = ((loUpper.getX() + loLower.getX()) / 2);
				faY[i] = loUpper.getY();
			} else {
				faX[i] = loLower.getX();
				faY[i] = ((loUpper.getY() + loLower.getY()) / 2);
			}

			if (isInteractivityEnabled()) {
				final EList<Trigger> elTriggers = ss.getTriggers();
				if (!elTriggers.isEmpty()) {
					final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(ss, dpha[i]);
					final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(iSource,
							InteractionEvent.class);
					iev.setCursor(ss.getCursor());

					Trigger tg;
					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						processTrigger(tg, iSource);
						iev.addTrigger(tg);
					}

					final PolygonRenderEvent pre = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createSeries(ss), PolygonRenderEvent.class);
					pre.setPoints(loaFrontFace);
					iev.setHotSpot(pre);
					ipr.enableInteraction(iev);
				}
			}

			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_ELEMENT, dpha[i], fPaletteEntry);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_ELEMENT, dpha[i]);
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dpha[i]);

			// RENDER DATA POINT LABEL
			renderDataPointLabel(srh.getLabelAttributes(getSeries()), dpha[i], srh.getLabelPosition(getSeries()),
					loaFrontFace, p, sh, dX, dY, dWidth, dHeight, dHigh, dLow, dOpen, dClose);
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
		// final ClientArea ca = lg.getClientArea( );
		final StockSeries ss = (StockSeries) getSeries();
		final LineAttributes lia = ss.getLineAttributes();
		if (!lia.isVisible()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.stock.lineattr.visibility", //$NON-NLS-1$
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				LineRenderEvent.class);
		if (ss.isShowAsBarStick()) {
			int stickLength = ss.getStickLength();

			// UPPER-LOWER SEGMENT
			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop()));
			lre.setEnd(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight()));
			ipr.drawLine(lre);

			// OPEN SEGMENT
			lre.setStart(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2 - stickLength,
					bo.getTop() + bo.getHeight() * 3 / 4));
			lre.setEnd(
					goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight() * 3 / 4));
			ipr.drawLine(lre);

			// CLOSE SEGMENT
			lre.setStart(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight() / 4));
			lre.setEnd(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2 + stickLength,
					bo.getTop() + bo.getHeight() / 4));
			ipr.drawLine(lre);
		} else {
			// DEFINE THE RECTANGLE
			RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
					RectangleRenderEvent.class);
			rre.setBackground(fPaletteEntry);
			rre.setOutline(lia);
			rre.setBounds(goFactory.createBounds(bo.getLeft() + 1, bo.getTop() + bo.getHeight() / 4, bo.getWidth() - 2,
					bo.getHeight() / 2));

			// DEFINE THE LINE
			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight()));
			lre.setEnd(goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop()));

			ipr.drawLine(lre);
			ipr.fillRectangle(rre);
			ipr.drawRectangle(rre);
		}
	}

	private boolean isValidEntry(StockEntry entry) {
		return entry != null && entry.isValid();
	}

	/**
	 * Auto correct the invalid stock entry and log the error.
	 *
	 * @param entry stock entry
	 * @since 2.2
	 */
	private void correctEntry(StockEntry entry) {
		double dHigh, dLow;
		if (entry.getOpen() > entry.getClose()) {
			dHigh = entry.getOpen();
			dLow = entry.getClose();
		} else {
			dHigh = entry.getClose();
			dLow = entry.getOpen();
		}
		if (entry.getHigh() < entry.getLow()) {
			logger.log(new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.stock.entry.invalid.highlow", //$NON-NLS-1$
					new Object[] { new Double(entry.getHigh()), new Double(entry.getLow()) },
					Messages.getResourceBundle(getRunTimeContext().getULocale())));

			entry.setHigh(dHigh);
			entry.setLow(dLow);
		}
		if (entry.getHigh() < dHigh) {
			logger.log(new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.stock.entry.invalid.high", //$NON-NLS-1$
					new Object[] { new Double(entry.getHigh()), new Double(dHigh) },
					Messages.getResourceBundle(getRunTimeContext().getULocale())));

			entry.setHigh(dHigh);
		}
		if (entry.getLow() > dLow) {
			logger.log(new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.stock.entry.invalid.low", //$NON-NLS-1$
					new Object[] { new Double(entry.getLow()), new Double(dLow) },
					Messages.getResourceBundle(getRunTimeContext().getULocale())));

			entry.setLow(dLow);
		}
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
		// NOTE: This method is not used by the Stock Renderer
	}

	protected void renderDataPointLabel(Label laDataPoint, DataPointHints dph, Position pDataPoint,
			Location[] loaFrontFace, Plot p, AbstractScriptHandler<?> sh, double dX, double dY, double dWidth,
			double dHeight, double dHigh, double dLow, double dOpen, double dClose) throws ChartException {
		// Make sure script work so value setter should be first.
		laDataPoint.getCaption().setValue(dph.getDisplayValue());
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dph, laDataPoint,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
				laDataPoint);

		if (laDataPoint.isVisible() && !dph.isOutside()) {
			Location loDataPoint = goFactory.createLocation(0, 0);

			if (((StockSeries) getSeries()).isShowAsBarStick()) {
				// Bar stick is not a rectangle, so width is not like candle's.
				dWidth = ((StockSeries) getSeries()).getStickLength();
			}

			switch (pDataPoint.getValue()) {
			// To compatible with old report file, display as default
			// position: above
			case Position.OUTSIDE:
			case Position.INSIDE:
			case Position.ABOVE:
				loDataPoint.set(dX, dHigh - p.getVerticalSpacing());
				renderLabel(WrappedStructureSource.createSeriesDataPoint(getSeries(), dph),
						TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.ABOVE_LITERAL, loDataPoint,
						null);
				break;
			case Position.BELOW:
				loDataPoint.set(dX, dLow + p.getVerticalSpacing());
				renderLabel(WrappedStructureSource.createSeriesDataPoint(getSeries(), dph),
						TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.BELOW_LITERAL, loDataPoint,
						null);
				break;
			case Position.LEFT:
				loDataPoint.set(dX - dWidth / 2 - p.getHorizontalSpacing(), (dOpen + dClose) / 2);
				renderLabel(WrappedStructureSource.createSeriesDataPoint(getSeries(), dph),
						TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.LEFT_LITERAL, loDataPoint, null);
				break;
			case Position.RIGHT:
				loDataPoint.set(dX + dWidth / 2 + p.getHorizontalSpacing(), (dOpen + dClose) / 2);
				renderLabel(WrappedStructureSource.createSeriesDataPoint(getSeries(), dph),
						TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, Position.RIGHT_LITERAL, loDataPoint,
						null);
				break;
			default:
				// For compatibility with old model, only log the error and
				// do nothing
				logger.log(new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
						"exception.illegal.datapoint.position.stock", //$NON-NLS-1$
						new Object[] { pDataPoint.getName() },
						Messages.getResourceBundle(getRunTimeContext().getULocale())));
			}
		}

		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dph, laDataPoint,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT_LABEL,
				laDataPoint);
	}

	@Override
	protected int checkEntryInRange(Object entry, Object min, Object max) {
		if (entry instanceof StockEntry) {
			StockEntry se = (StockEntry) entry;
			double dMin = Methods.asDouble(min).doubleValue();
			double dMax = Methods.asDouble(max).doubleValue();
			if (se.getLow() < dMin) {
				return 1;
			}
			if (se.getHigh() > dMax) {
				return 2;
			}
			return 0;
		}
		return super.checkEntryInRange(entry, min, max);
	}

	static Fill convertFill(Fill fill, boolean bIncrease) {
		if (bIncrease) {
			// Price increase
			if (fill instanceof MultipleFill) {
				fill = goFactory.copyOf(((MultipleFill) fill).getFills().get(0));
			} else {
				// White color
				fill = ColorDefinitionImpl.WHITE();
			}
		} else // Price decrease
		if (fill instanceof MultipleFill) {
			// Negative color
			fill = goFactory.copyOf(((MultipleFill) fill).getFills().get(1));
		}
		// Palette color
		return fill;
	}

}
