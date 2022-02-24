/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * This is the renderer implementation used for difference chart.
 */
public class Difference extends Line {

	/**
	 * The constructor.
	 */
	public Difference() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.Line#renderSeries(org.eclipse.birt.chart.device
	 * .IPrimitiveRenderer, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {

		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		if (cwa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.difference.dimension", //$NON-NLS-1$
					new Object[] { cwa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		try {
			validateDataSetCount(isrh);
		} catch (ChartException vex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, vex);
		}

		// SCALE VALIDATION
		SeriesRenderingHints srh = (SeriesRenderingHints) isrh;

//		if ( !srh.isCategoryScale( ) )
//		{
//			throw new ChartException( ChartEngineExtensionPlugin.ID,
//					ChartException.RENDERING,
//					"exception.xvalue.scale.difference", //$NON-NLS-1$
//					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
//		}

		// OBTAIN AN INSTANCE OF THE SERIES MODEL (AND SOME VALIDATION)
		DifferenceSeries ds = (DifferenceSeries) getSeries();

		if (!ds.isVisible()) {
			restoreClipping(ipr);
			return;
		}

		// OBTAIN AN INSTANCE OF THE CHART (TO RETRIEVE GENERAL CHART PROPERTIES
		// IF ANY)
		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1), Integer.valueOf(iSeriesCount) },
				getRunTimeContext().getULocale())); // i18n_CONCATENATIONS_REMOVED

		// SETUP VARIABLES NEEDED TO RENDER THE SERIES
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		final DataPointHints[] dpha = isrh.getDataPoints();
		validateNullDatapoint(dpha);

		// SETUP VARIABLES NEEDED TO COMPUTE CO-ORDINATES
		double fX, fY, fUnit, fPos, fNeg;
		double[] faX = new double[dpha.length];
		double[][] faY = new double[2][dpha.length];

		// SETUP THE MARKER FILL COLOR FROM THE SERIES DEFINITION PALETTE (BY
		// CATEGORIES OR BY SERIES)
		SeriesDefinition sd = getSeriesDefinition();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		if (elPalette.isEmpty()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING, "exception.empty.palette", //$NON-NLS-1$
					new Object[] { ds }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		final boolean isTransposed = isTransposed();
		final boolean bPaletteByCategory = isPaletteByCategory();

		if (bPaletteByCategory && ds.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) ds.eContainer();
		}

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(ds);
		if (iThisSeriesIndex < 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.series.for.palette.index", //$NON-NLS-1$
					new Object[] { ds, sd }, Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		Marker posMarker = null;
		if (ds.getMarkers().size() > 0) {
			posMarker = ds.getMarkers().get(iThisSeriesIndex % ds.getMarkers().size());
		}

		Marker negMarker = null;
		if (ds.getNegativeMarkers().size() > 0) {
			negMarker = ds.getNegativeMarkers().get(iThisSeriesIndex % ds.getNegativeMarkers().size());
		}

		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
		} else if (iSeriesIndex > 0) {
			// Here eliminate the position for one base series.
			// NOTE only one base series allowed now.
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iSeriesIndex - 1);
		}
		updateTranslucency(fPaletteEntry, ds);

		// Update Line color
		LineAttributes liaPos = ds.getLineAttributes();
		LineAttributes liaNeg = ds.getNegativeLineAttributes();
		if (((LineSeries) getSeries()).isPaletteLineColor()) {
			final Fill[] fills = DifferenceRenderer.createDifferenceFillFromPalette(fPaletteEntry, true);
			liaPos.setColor(FillUtil.getColor(fills[0]));
			liaNeg.setColor(FillUtil.getColor(fills[1]));
		}

		// THE MAIN LOOP THAT WALKS THROUGH THE DATA POINT HINTS ARRAY 'dpha'
		for (int i = 0; i < dpha.length; i++) {
			DifferenceEntry de = (DifferenceEntry) dpha[i].getOrthogonalValue();

			faX[i] = Double.NaN;
			faY[0][i] = Double.NaN;
			faY[1][i] = Double.NaN;

			if (!DifferenceRenderer.isValidDifferenceEntry(de)) {
				continue;
			}

			Location lo = dpha[i].getLocation();

			fPos = srh.getLocationOnOrthogonal(new Double(de.getPositiveValue()));
			fNeg = srh.getLocationOnOrthogonal(new Double(de.getNegativeValue()));

			if (isTransposed) {
				fUnit = dpha[i].getSize();
				fY = (lo.getY() + fUnit / 2.0);
				// for fixing the position change in refreshing
				// lo.setY( fY );

				faX[i] = fY;
				faY[0][i] = fPos;
				faY[1][i] = fNeg;
			} else {
				fUnit = dpha[i].getSize();
				fX = (lo.getX() + fUnit / 2.0);
				// for fixing the position change in refreshing
				// lo.setX( fX );

				faX[i] = fX;
				faY[0][i] = fPos;
				faY[1][i] = fNeg;
			}
		}

		if (isTransposed) {
			handleOutsideDataPoints(ipr, srh, faY[0], faX, false);
			handleOutsideDataPoints(ipr, srh, faY[1], faX, false);
		} else {
			handleOutsideDataPoints(ipr, srh, faX, faY[0], false);
			handleOutsideDataPoints(ipr, srh, faX, faY[1], false);
		}

		if (ds.isCurve()) {
			DifferenceRenderer.renderDifferenceCurve(this, ipr, dpha,
					isTransposed ? goFactory.createLocations(faY[0], faX) : goFactory.createLocations(faX, faY[0]),
					isTransposed ? goFactory.createLocations(faY[1], faX) : goFactory.createLocations(faX, faY[1]),
					liaPos, liaNeg, fPaletteEntry);
		} else {
			DifferenceRenderer.renderDifferencePolygon(this, ipr, dpha,
					isTransposed ? goFactory.createLocations(faY[0], faX) : goFactory.createLocations(faX, faY[0]),
					isTransposed ? goFactory.createLocations(faY[1], faX) : goFactory.createLocations(faX, faY[1]),
					liaPos, liaNeg, fPaletteEntry);
		}

		// RENDER THE MARKERS NEXT
		if (negMarker != null || posMarker != null) {
			for (int i = 0; i < dpha.length; i++) {
				DifferenceEntry de = (DifferenceEntry) dpha[i].getOrthogonalValue();
				if (!DifferenceRenderer.isValidDifferenceEntry(de) || dpha[i].isOutside()) {
					continue;
				}

				if (bPaletteByCategory) {
					fPaletteEntry = FillUtil.getPaletteFill(elPalette, i);
				} else {
					fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
				}
				updateTranslucency(fPaletteEntry, ds);

				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dpha[i]);

				if (negMarker != null) {
					Fill negFill = FillUtil.convertFill(fPaletteEntry, -1, null);

					renderMarker(ds, ipr, negMarker,
							isTransposed ? goFactory.createLocation(faY[1][i], faX[i])
									: goFactory.createLocation(faX[i], faY[1][i]),
							liaNeg, negFill, dpha[i], null, true, true);
				}

				if (posMarker != null) {
					Fill posFill = FillUtil.convertFill(fPaletteEntry, 1, null);

					renderMarker(ds, ipr, posMarker,
							isTransposed ? goFactory.createLocation(faY[0][i], faX[i])
									: goFactory.createLocation(faX[i], faY[0][i]),
							liaPos, posFill, dpha[i], null, true, true);
				}

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dpha[i], fPaletteEntry,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dpha[i]);
			}
		}

		// DATA POINT RELATED VARIABLES ARE INITIALIZED HERE
		Label laDataPoint = srh.getLabelAttributes(ds);

		if (laDataPoint.isVisible()) // ONLY COMPUTE IF NECESSARY
		{
			Position posDataPoint = srh.getLabelPosition(ds);
			Location loDataPoint = goFactory.createLocation(0, 0);

			final double dSize = (posMarker == null) ? 0 : posMarker.getSize();

			for (int i = 0; i < dpha.length; i++) {
				DifferenceEntry de = (DifferenceEntry) dpha[i].getOrthogonalValue();
				if (!DifferenceRenderer.isValidDifferenceEntry(de) || dpha[i].isOutside()) {
					continue;
				}

				laDataPoint.getCaption().setValue(dpha[i].getDisplayValue());

				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dpha[i], laDataPoint,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL,
						laDataPoint);

				if (laDataPoint.isVisible()) {
					fX = faX[i];
					fY = faY[1][i];

					if (isTransposed) {
						fX = faY[1][i];
						fY = faX[i];
					}

					switch (posDataPoint.getValue()) {
					case Position.ABOVE:
						loDataPoint.set(fX, fY - dSize - p.getVerticalSpacing());
						break;
					case Position.BELOW:
						loDataPoint.set(fX, fY + dSize + p.getVerticalSpacing());
						break;
					case Position.LEFT:
						loDataPoint.set(fX - dSize - p.getHorizontalSpacing(), fY);
						break;
					case Position.RIGHT:
						loDataPoint.set(fX + dSize + p.getHorizontalSpacing(), fY);
						break;
					default:
						throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
								"exception.illegal.datapoint.position.line", //$NON-NLS-1$
								new Object[] { posDataPoint.getName() },
								Messages.getResourceBundle(getRunTimeContext().getULocale()));
					}
					renderLabel(WrappedStructureSource.createSeriesDataPoint(ds, dpha[i]),
							TextRenderEvent.RENDER_TEXT_AT_LOCATION, laDataPoint, posDataPoint, loDataPoint, null);
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
	 * org.eclipse.birt.chart.render.Line#renderLegendGraphic(org.eclipse.birt.chart
	 * .device.IPrimitiveRenderer, org.eclipse.birt.chart.model.layout.Legend,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final DifferenceSeries ds = (DifferenceSeries) getSeries();
		if (fPaletteEntry == null) {
			fPaletteEntry = goFactory.RED();
		}

		// Fix Bugzilla bug 188846.
		Location[] loa1 = new Location[2];
		loa1[0] = goFactory.createLocation(bo.getLeft() + 1 * getDeviceScale(), bo.getTop() + 2 * getDeviceScale());
		loa1[1] = goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1 * getDeviceScale(),
				bo.getTop() + 2 * getDeviceScale());

		Location[] loa2 = new Location[2];
		loa2[0] = goFactory.createLocation(bo.getLeft() + 1 * getDeviceScale(),
				bo.getTop() + bo.getHeight() - 2 * getDeviceScale());
		loa2[1] = goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1 * getDeviceScale(),
				bo.getTop() + bo.getHeight() - 2 * getDeviceScale());

		// Always render non-transposed curve line for difference legend
		DifferenceRenderer.renderDifferenceCurve(this, ipr, null, loa1, loa2, ds.getLineAttributes(),
				ds.getNegativeLineAttributes(), fPaletteEntry);
		dc.flush();

	}

	protected int checkEntryInRange(Object entry, Object min, Object max) {
		if (entry instanceof DifferenceEntry) {
			double vP = ((DifferenceEntry) entry).getPositiveValue();
			double vN = ((DifferenceEntry) entry).getNegativeValue();
			double dMin = Methods.asDouble(min).doubleValue();
			double dMax = Methods.asDouble(max).doubleValue();
			if (vP < dMin || vN < dMin) {
				return 1;
			}
			if (vP > dMax || vN > dMax) {
				return 2;
			}
			return 0;
		}
		return super.checkEntryInRange(entry, min, max);
	}

}
