/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.render;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withoutaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.ChartUtil.CacheDateFormat;
import org.eclipse.birt.chart.util.ChartUtil.CacheDecimalFormat;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Radar
 */
public class Radar extends BaseRenderer {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String STANDARD_SUBTYPE_LITERAL = "Standard Radar Chart"; //$NON-NLS-1$

	public static final String SPIDER_SUBTYPE_LITERAL = "Spider Radar Chart"; //$NON-NLS-1$

	public static final String BULLSEYE_SUBTYPE_LITERAL = "Bullseye Radar Chart"; //$NON-NLS-1$

	public static final String SCRIPT_KEY_WEB = "Web"; //$NON-NLS-1$

	public static final String SCRIPT_KEY_CATEGORY = "Category"; //$NON-NLS-1$

	static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.examples/render"); //$NON-NLS-1$

	private double percentReduce = 0.2;

	private double dSafeSpacing = 10;

	private DataPointHints[] dpha = null;

	private CacheDecimalFormat dfNumericFormatCache = null;
	private CacheDateFormat dfDateFormatCache = null;

	private RadarScaleHelper scaleHelper;

	public Radar() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.ISeriesRenderer#compute(org.eclipse.birt
	 * .chart.model.attribute.Bounds, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		final SeriesRenderingHints srh = (SeriesRenderingHints) isrh;

		// VALIDATE CONSISTENT DATASET COUNT BETWEEN BASE AND ORTHOGONAL
		validateDataSetCount(isrh);

		// SCALE VALIDATION
		dpha = srh.getDataPoints();

		double[] da = srh.asPrimitiveDoubleValues();

		if (dpha == null || da == null || dpha.length < 1 || da.length < 1) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.invalid.datapoint.dial", //$NON-NLS-1$
					org.eclipse.birt.chart.engine.extension.i18n.Messages
							.getResourceBundle(getRunTimeContext().getULocale()));
		}

		if (scaleHelper == null) {
			scaleHelper = new RadarScaleHelper(getFirstSeries(), (ChartWithoutAxes) getModel());
		}

		scaleHelper.compute();

		// Set on Plot dialog
		double cvr = ((ChartWithoutAxes) getModel()).getCoverage();
		if (cvr <= 0) {
			cvr = 0.8;
		}
		percentReduce = 1 - cvr;

		dSafeSpacing *= getDeviceScale();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.ISeriesRenderer#renderSeries(org.eclipse
	 * .birt.chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		SeriesDefinition sd = getSeriesDefinition();
		ChartWithoutAxes cwoa = (ChartWithoutAxes) getModel();
		if (cwoa.getDimension() != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.dial.dimension", //$NON-NLS-1$
					new Object[] { cwoa.getDimension().getName() },
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
				getRunTimeContext().getULocale()) + getClass().getName() + (iSeriesIndex + 1) + iSeriesCount);

		render(getDevice(), srh.getClientAreaBounds(true), (RadarSeries) getSeries(), sd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.ISeriesRenderer#renderLegendGraphic(org
	 * .eclipse.birt.chart.device.IPrimitiveRenderer,
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
		final RadarSeries ls = (RadarSeries) getSeries();
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
		if (!liaMarker.isSetVisible()) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.unspecified.marker.linestyle.visibility", //$NON-NLS-1$
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}
		if (liaMarker.isVisible()) {
			final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
					LineRenderEvent.class);

			if (fPaletteEntry instanceof ColorDefinition && (ls.isSetPaletteLineColor() && ls.isPaletteLineColor())) {
				liaMarker = goFactory.copyOf(liaMarker);
				liaMarker.setColor(goFactory.copyOf(FillUtil.getColor(fPaletteEntry)));
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

		// need
		m = ls.getMarker();
		double width = bo.getWidth() / getDeviceScale();
		double height = bo.getHeight() / getDeviceScale();
		int markerSize = (int) (((width > height ? height : width) - 2) / 2);
		if (markerSize <= 0) {
			markerSize = 1;
		}

		if (m != null && m.isVisible()) {
			renderMarker(lg, ipr, m,
					goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + bo.getHeight() / 2),
					ls.getLineAttributes(), fPaletteEntry, null, Integer.valueOf(markerSize), false, false);
		}
	}

	private void renderPolys(IDeviceRenderer idr, Location[] prelo, RadarSeries se, SeriesDefinition sd)
			throws ChartException {

		int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
		if (iThisSeriesIndex == -1)
			iThisSeriesIndex = getSeriesIndex();
		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		Fill fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);

		LineAttributes llia = se.getLineAttributes();

		final PolygonRenderEvent pre = ((EventObjectCache) idr).getEventObject(StructureSource.createSeries(se),
				PolygonRenderEvent.class);
		final LineRenderEvent lre = ((EventObjectCache) idr).getEventObject(StructureSource.createSeries(se),
				LineRenderEvent.class);

		if (se.isSetTranslucent() && se.isTranslucent()) {
			if (fPaletteEntry instanceof ColorDefinition) {
				fPaletteEntry = goFactory.translucent((ColorDefinition) fPaletteEntry);
			}
		}
		// Disconnected Lines
		if (!se.isFillPolys() || !se.isConnectEndpoints()) {
			lre.setLineAttributes(llia);
			for (int i = 0; i < (prelo.length - 1); i++) {
				if (prelo[i] == null || prelo[i + 1] == null) {
					continue;
				}

				lre.setStart(prelo[i]);
				lre.setEnd(prelo[i + 1]);

				idr.drawLine(lre);
			}
			// Connect the first and last point
			if (se.isConnectEndpoints()) {
				if (prelo[0] != null && prelo[prelo.length - 1] != null) {
					lre.setStart(prelo[0]);
					lre.setEnd(prelo[prelo.length - 1]);

					idr.drawLine(lre);
				}
			}
		} else {
			pre.setBackground(fPaletteEntry);
			pre.setPoints(prelo);
			pre.setOutline(llia);

			idr.drawPolygon(pre);
			idr.fillPolygon(pre);
		}
	}

	private void renderAxes(IDeviceRenderer idr, PolarCoordinate pc, double magnitude) throws ChartException {
		Location center = pc.getCenter();
		// int iSeriesCount = getSeriesCount( ) - 1;
		int iCount = pc.getCount();// * iSeriesCount;
		pc = new PolarCoordinate(center, iCount, 1, 0);
		Location lo = center.copyInstance();

		LineAttributes lia = null;
		LineAttributes wlia = null;
		RadarSeries rsd = getFirstSeries();
		wlia = rsd.getWebLineAttributes();
		if (wlia == null) {
			lia = goFactory.createLineAttributes(goFactory.GREY(), LineStyle.SOLID_LITERAL, 1);
		} else {
			lia = wlia;
		}

		StructureSource ss = StructureSource.createPlot(getModel().getPlot());

		final LineRenderEvent lre = ((EventObjectCache) idr).getEventObject(ss, LineRenderEvent.class);
		final OvalRenderEvent ore = ((EventObjectCache) idr).getEventObject(ss, OvalRenderEvent.class);

		lre.setLineAttributes(lia);

		// Radials
		lre.setStart(center);

		for (int i = 0; i < iCount; i++) {
			pc.computeLocation(lo, i, magnitude);
			lre.setEnd(lo);
			idr.drawLine(lre);

			DataPointHints dph = dpha[i];
			if ((rsd.isSetShowCatLabels() && rsd.isShowCatLabels()) || (!rsd.isSetShowCatLabels())) {

				drawAxisRadialLabel(idr, pc, lo, i, dph.getBaseValue());
			}
		}

		String subType = getModel().getSubType();
		if (STANDARD_SUBTYPE_LITERAL.equals(subType) || BULLSEYE_SUBTYPE_LITERAL.equals(subType)) {
			ore.setBackground(lia.getColor());
			ore.setOutline(lia);
			Bounds bo = goFactory.createBounds(0, 0, 0, 0);
			for (int sc = 1; sc <= scaleHelper.getScaleCount(); sc++) {
				double spiderMag = magnitude * sc / scaleHelper.getScaleCount();
				ore.setBounds(pc.computeBounds(bo, spiderMag));
				idr.drawOval(ore);
			}

		} else if (SPIDER_SUBTYPE_LITERAL.equals(subType)) {
			Location lo1 = lo.copyInstance();
			for (int sc = 1; sc < scaleHelper.getScaleCount() + 1; sc++) {
				double spiderMag = magnitude * sc / scaleHelper.getScaleCount();
				pc.computeLocation(lo1, 0, spiderMag);
				for (int index = 1; index < iCount + 1; index++) {
					lo.set(lo1.getX(), lo1.getY());
					pc.computeLocation(lo1, index, spiderMag);
					lre.setStart(lo);
					lre.setEnd(lo1);
					idr.drawLine(lre);
				}
			}
		}

	}

	private void renderOvalBackgrounds(IDeviceRenderer idr, Location center, Series se, double magnitude)
			throws ChartException {

		final OvalRenderEvent ore = ((EventObjectCache) idr).getEventObject(StructureSource.createPlot(cm.getPlot()),
				OvalRenderEvent.class);

		LineAttributes lia = null;
		LineAttributes wlia = ((RadarSeries) se).getWebLineAttributes();
		if (wlia == null) {
			lia = goFactory.createLineAttributes(goFactory.GREY(), LineStyle.SOLID_LITERAL, 1);
		} else {
			lia = wlia;
		}
		ore.setBackground(lia.getColor());
		ore.setOutline(lia);

		for (int sc = scaleHelper.getScaleCount(); sc >= 1; sc--) {
			double spiderMag = magnitude * sc / scaleHelper.getScaleCount();
			ore.setBounds(goFactory.createBounds(center.getX() - spiderMag, center.getY() - spiderMag, spiderMag * 2,
					spiderMag * 2));

			Fill wPaletteEntry = null;
			Palette pa = sd.getSeriesPalette();
			int ps = pa.getEntries().size();
			int tscnt = getSeriesCount();
			int palcnt = ps + tscnt + sc;
			if (palcnt > ps)
				palcnt = 1;

			wPaletteEntry = FillUtil.getPaletteFill(pa.getEntries(), sc + 1);
			if (wPaletteEntry instanceof ColorDefinition) {
				RadarSeries rsd = getFirstSeries();
				if (rsd.isBackgroundOvalTransparent()) {
					wPaletteEntry = goFactory.translucent((ColorDefinition) wPaletteEntry);
				}
			}
			// ore.setBounds( goFactory.copyOf( bo ) );
			ore.setBackground(wPaletteEntry);
			idr.fillOval(ore);
		}

	}

	private static class PolarCoordinate {

		private final Location center;
		// private final int iSeriesCount;
		// private final int iSeriesIndex;
		private final int iCount;
		private final double delta;

		public PolarCoordinate(Location center, int iCount, int iSeriesCount, int iSeriesIndex) {
			this.center = center;
			// this.iSeriesCount = iSeriesCount;
			// this.iSeriesIndex = iSeriesIndex;
			this.iCount = iCount;
			delta = 2 * Math.PI / iCount;
		}

		public Location getCenter() {
			return center;
		}

		public int getCount() {
			return iCount;
		}

		public double getAngle(int index, int iSeriesIndex) {
			return (index * delta) * (-1.0) + Math.PI / 2; // + iSeriesIndex
															// * delta /
															// iSeriesCount;
		}

		public double getAngle(int index) {
			return getAngle(index, 0/* iSeriesIndex */);
		}

		public int getDegree(int index) {
			double degree = Math.toDegrees(getAngle(index)) % 360;
			if (degree < 0) {
				degree += 360;
			}
			if (degree > 180) {
				degree -= 360;
			}
			return (int) Math.round(degree);
		}

		public Location computeLocation(Location lo, int index, double magnitude) {
			double angle = getAngle(index, 0/* iSeriesIndex */);
			double x = Math.cos(angle) * magnitude;
			double y = Math.sin(angle) * magnitude;
			lo.set(center.getX() + x, center.getY() - y);
			return lo;
		}

		public Location createLocation(int index, double magnitude) {
			double angle = getAngle(index);
			double x = Math.cos(angle) * magnitude;
			double y = Math.sin(angle) * magnitude;
			return goFactory.createLocation(center.getX() + x, center.getY() - y);
		}

		public Bounds computeBounds(Bounds bo, double magnitude) {
			bo.set(center.getX() - magnitude, center.getY() - magnitude, magnitude * 2, magnitude * 2);
			return bo;
		}
	}

	private Object getWebLabelDefaultFormat(Object value) {
		if (value instanceof Number) {
			if (dfNumericFormatCache == null) {
				dfNumericFormatCache = new ChartUtil.CacheDecimalFormat(rtc.getULocale());
			}
			return dfNumericFormatCache.get(ValueFormatter.getNumericPattern((Number) value));
		}

		return null;
	}

	private Object getCategoryDefaultFormat(Object value) throws ChartException {
		if (value instanceof Number) {
			if (dfNumericFormatCache == null) {
				dfNumericFormatCache = new ChartUtil.CacheDecimalFormat(rtc.getULocale());
			}
			return dfNumericFormatCache.get(ValueFormatter.getNumericPattern((Number) value));
		}

		DataSetIterator categoryDSI = new DataSetIterator(
				((ChartWithoutAxes) getModel()).getSeriesDefinitions().get(0).getRunTimeSeries().get(0).getDataSet());
		int iDateTimeUnit = ChartUtil.computeDateTimeCategoryUnit(getModel(), categoryDSI);

		if (iDateTimeUnit != IConstants.UNDEFINED) {
			if (dfDateFormatCache == null) {
				dfDateFormatCache = new ChartUtil.CacheDateFormat(rtc.getULocale());
			}
			return dfDateFormatCache.get(iDateTimeUnit);
		}

		return null;
	}

	private final void drawAxisRadialLabel(IDeviceRenderer idr, PolarCoordinate pc, Location lo, int cindex, Object lab)
			throws ChartException {
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		double space = dSafeSpacing / 2;
		Label la = null;
		if (getFirstSeries().getCatLabel() != null) {
			la = goFactory.copyOf(getFirstSeries().getCatLabel());
		} else {
			la = goFactory.copyOf(getFirstSeries().getLabel());
			la.setVisible(true);
		}
		if (lab == null) {
			lab = "null"; //$NON-NLS-1$
		}

		FormatSpecifier fs = getFirstSeries().getCatLabelFormatSpecifier();
		Object defaultFormat = getCategoryDefaultFormat(lab);
		String catlabel = ValueFormatter.format(lab, fs, rtc.getULocale(), defaultFormat);
		la.getCaption().setValue(catlabel);

		Location loLabel = lo.copyInstance();

		final TextRenderEvent tre = ((EventObjectCache) idr)
				.getEventObject(WrappedStructureSource.createAxisLabel(null, la), TextRenderEvent.class);
		tre.setLabel(la);

		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_LABEL, SCRIPT_KEY_CATEGORY, la,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL, la);

		tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);

		int degree = pc.getDegree(cindex);

		if (Math.abs(degree) > 90) {
			tre.setTextPosition(TextRenderEvent.LEFT);
		} else {
			tre.setTextPosition(TextRenderEvent.RIGHT);
		}
		if (Math.abs(degree) == 90) {
			tre.setTextPosition(TextRenderEvent.ABOVE);
		}
		if (degree == -90) {
			tre.setTextPosition(TextRenderEvent.BELOW);
		}
		double dX = -Math.signum(Math.abs(degree) - 90) * space;
		double dY = -Math.signum(degree) * space;
		if (degree == 0 || degree == 180 || degree == -180) {
			dY = 0;
		}
		if (degree == 90 || degree == -90 || degree == 270 || degree == -270) {
			dX = 0;
		}

		loLabel.translate(dX, dY);

		tre.setLocation(loLabel);

		// Text render event must be either cached or copied here for correct
		// interactivity.
		dc.addLabel(tre);
		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_LABEL, SCRIPT_KEY_CATEGORY, la,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL, la);

	}

	private final void drawSeriesLabel(IDeviceRenderer idr, PolarCoordinate pc, DataPointHints dph, Location lopt)
			throws ChartException {
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		if (se.getLabel().isVisible()) {
			double space = dSafeSpacing / 2;
			Label la = goFactory.copyOf(se.getLabel());
			la.getCaption().setValue(dph.getDisplayValue());
			Location loLabel = lopt.copyInstance();

			final TextRenderEvent tre = ((EventObjectCache) idr)
					.getEventObject(WrappedStructureSource.createSeriesDataPoint(se, dph), TextRenderEvent.class);
			tre.setLabel(la);

			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL, dph, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL, la);

			tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);

			int degree = pc.getDegree(dph.getIndex());

			if (Math.abs(degree) > 90) {
				tre.setTextPosition(TextRenderEvent.LEFT);
			} else {
				tre.setTextPosition(TextRenderEvent.RIGHT);
			}
			if (Math.abs(degree) == 90) {
				tre.setTextPosition(TextRenderEvent.ABOVE);
			}
			if (degree == -90) {
				tre.setTextPosition(TextRenderEvent.BELOW);
			}
			double dX = -Math.signum(Math.abs(degree) - 90) * space;
			double dY = -Math.signum(degree) * space;
			loLabel.translate(dX, dY);
			tre.setLocation(loLabel);

			// Text render event must be either cached or copied here for
			// correct interactivity.
			dc.addLabel(tre);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL, dph, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT_LABEL, la);
		}

	}

	/**
	 * @param idr
	 * @param bo
	 * @throws ChartException
	 */
	private final void render(IDeviceRenderer idr, Bounds bo, RadarSeries se, SeriesDefinition sd)
			throws ChartException {
		List<Series> rts = sd.getRunTimeSeries();
		int iThisSeriesIndex = rts.indexOf(se);

		if (iThisSeriesIndex == -1)
			iThisSeriesIndex = getSeriesIndex();

		int totalSeriesCnt = getSeriesCount();
		int currSeriesIdx = getSeriesIndex();

		final EList<Fill> elPalette = sd.getSeriesPalette().getEntries();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		dc = getDeferredCache();

		// render polys biggest to least
		// render axes
		// render labels
		// render markers

		Bounds boCA = goFactory.copyOf(bo);

		double dh = boCA.getHeight() - (boCA.getHeight() * percentReduce);
		double dl = boCA.getLeft() + (boCA.getWidth() * percentReduce) / 2;
		double dt = boCA.getTop() + (boCA.getHeight() * percentReduce) / 2;
		double dw = boCA.getWidth() - (boCA.getWidth() * percentReduce);
		double centrePointX = Math.round(dl + dw / 2);
		double centrePointY = Math.round(dt + dh / 2);
		double mag = dh / 2;

		Location cntpt = goFactory.createLocation(centrePointX, centrePointY);

		if (currSeriesIdx == 1) {
			if (BULLSEYE_SUBTYPE_LITERAL.equals(getModel().getSubType())) {
				renderOvalBackgrounds(idr, cntpt, se, mag);
			}
		}

		PolarCoordinate pc = new PolarCoordinate(cntpt, dpha.length, getSeriesCount() - 1, getSeriesIndex() - 1);

		final boolean bPaletteByCategory = isPaletteByCategory();
		Fill fPaletteEntry = null;
		if (!bPaletteByCategory) {
			fPaletteEntry = FillUtil.getPaletteFill(elPalette, iThisSeriesIndex);
			updateTranslucency(fPaletteEntry, se);
		}

		Location loAxis = goFactory.createLocation(centrePointX, centrePointY);
		List<Location> loList = new LinkedList<Location>();

		for (int index = 0; index < dpha.length; index++) {
			DataPointHints dph = dpha[index];

			if (isNaN(dph.getOrthogonalValue())) {
				if (!se.isFillPolys()) {
					loList.add(null);
				}
				continue;
			}

			double currval = ((Number) dph.getOrthogonalValue()).doubleValue();
			if (!scaleHelper.getAutoScale()) {
				// Do not render points out of bounds.
				if (currval < scaleHelper.getAxisMin()) {
					currval = scaleHelper.getAxisMin();
				}
				if (currval > scaleHelper.getAxisMax()) {
					currval = scaleHelper.getAxisMax();
				}
			}
			// Need to do something to give some space at top and center
			pc.computeLocation(loAxis, index, mag);
			Location lo = pc.createLocation(index, mag * (1
					- ((scaleHelper.getAxisMax() - currval) / (scaleHelper.getAxisMax() - scaleHelper.getAxisMin()))));
			loList.add(lo);

			if (bPaletteByCategory) {
				fPaletteEntry = FillUtil.getPaletteFill(elPalette, index);
				updateTranslucency(fPaletteEntry, se);
			}

			LineAttributes llia = se.getLineAttributes();
			if (se.isPaletteLineColor() && index == 0) {
				llia.setColor(goFactory.copyOf(FillUtil.getColor(fPaletteEntry)));
			}

			Marker m = se.getMarker();
			if (m != null) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dph, fPaletteEntry,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dph);
				renderMarker(se, idr, m, lo, llia, fPaletteEntry, dph, null, true, false);
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dph, fPaletteEntry,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dph);
			}

			drawSeriesLabel(idr, pc, dph, lo);
		}

		renderPolys(idr, loList.toArray(new Location[loList.size()]), se, sd);

		// last for rendering
		if (currSeriesIdx == (totalSeriesCnt - 1)) {
			if (se.getWebLineAttributes() != null && se.getWebLineAttributes().isVisible()) {
				renderAxes(idr, pc, mag);
			}

			RadarSeries rsd = getFirstSeries();
			if (rsd.isShowWebLabels()) {
				Location loLabel = goFactory.createLocation(0, 0);
				for (int sc = 0; sc <= scaleHelper.getScaleCount(); sc++) {
					// Use chart plot as structure source instead of series so
					// as to web labels interactivity won't be affected by data
					// point labels.
					final TextRenderEvent stre = ((EventObjectCache) idr)
							.getEventObject(StructureSource.createPlot(cm.getPlot()), TextRenderEvent.class);

					Label la = null;
					if (rsd.getWebLabel() != null) {
						la = goFactory.copyOf(rsd.getWebLabel());
					} else {
						la = goFactory.copyOf(rsd.getLabel());
					}

					la.setVisible(true);
					stre.setTextPosition(TextRenderEvent.RIGHT);
					// use this to set the direction rsd.getLabelPosition();
					double lblperc;
					if (sc == 0) {
						lblperc = scaleHelper.getAxisMin();
					} else if (sc == (scaleHelper.getScaleCount())) {
						lblperc = scaleHelper.getAxisMax();
					} else {
						lblperc = (((double) sc / scaleHelper.getScaleCount())
								* (scaleHelper.getAxisMax() - scaleHelper.getAxisMin())) + scaleHelper.getAxisMin();
					}

					String weblabel = ValueFormatter.format(lblperc, rsd.getWebLabelFormatSpecifier(), rtc.getULocale(),
							getWebLabelDefaultFormat(lblperc));
					la.getCaption().setValue(weblabel);

					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_LABEL, SCRIPT_KEY_WEB, la,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL, la);

					stre.setLabel(la);
					stre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
					double ycord = mag * sc / scaleHelper.getScaleCount();
					ycord = Math.round(centrePointY - ycord);
					double xcord = Math.round(centrePointX - (mag * 0.25));
					loLabel.set(xcord, ycord);
					stre.setLocation(loLabel);
					dc.addLabel(stre);

					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_LABEL, SCRIPT_KEY_WEB, la,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL, la);

				}
			}

		}
	}

	private RadarSeries getFirstSeries() {
		return (RadarSeries) ((ChartWithoutAxes) getModel()).getSeriesDefinitions().get(0).getSeriesDefinitions().get(0)
				.getSeries().get(0);
	}

}
