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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints3D;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.render.AxesRenderHelper;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Area
 */
public class Area extends Line {

	private final static String AREA_ENVELOPS = "Area.Envelops"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public Area() {
		super();
	}

	private Fill getSeriesPaletteEntry() {
		Fill fPaletteEntry = null;
		SeriesDefinition sd = null;

		Series se = getSeries();

		if (se.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) se.eContainer();
		}

		if (sd != null) {
			int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
			if (iThisSeriesIndex >= 0) {
				EList<Fill> ePalette = sd.getSeriesPalette().getEntries();
				fPaletteEntry = FillUtil.getPaletteFill(ePalette, iThisSeriesIndex);
				updateTranslucency(fPaletteEntry, se);
			}
		}

		return fPaletteEntry;
	}

	@Override
	protected boolean validateShowAsTape() {
		ChartWithAxes cwa = (ChartWithAxes) getModel();
		AreaSeries as = (AreaSeries) getSeries();

		if (!as.isStacked()) // NOT STACKED
		{
			if (getSeriesCount() > 2 && !isDimension3D()) {
				return false;
			}
		} else {
			final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(cwa.getBaseAxes()[0], true);
			if (axaOrthogonal.length > 1) {
				// If it is study layout, it should just check if it has another
				// series type in the same axis.
				if (ChartUtil.isStudyLayout(cwa)) {
					Axis axis = ChartUtil.getAxisFromSeries(as);
					if (axis != null) {
						for (Iterator<SeriesDefinition> itr = axis.getSeriesDefinitions().iterator(); itr.hasNext();) {
							SeriesDefinition sd = itr.next();
							for (Iterator<Series> sitr = sd.getRunTimeSeries().iterator(); sitr.hasNext();) {
								Series se = sitr.next();

								if (!(se instanceof AreaSeries) || !se.isStacked()) {
									return false;
								}
							}
						}

						return true;
					}
				}

				return false;
			}
			if (getSeriesCount() > 2 && !isDimension3D()) {
				for (Iterator<SeriesDefinition> itr = axaOrthogonal[0].getSeriesDefinitions().iterator(); itr
						.hasNext();) {
					SeriesDefinition sd = itr.next();
					for (Iterator<Series> sitr = sd.getRunTimeSeries().iterator(); sitr.hasNext();) {
						Series se = sitr.next();

						if (!(se instanceof AreaSeries) || !se.isStacked()) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	protected void renderAsCurve(IPrimitiveRenderer ipr, LineAttributes lia, ISeriesRenderingHints isrh, Location[] loa,
			boolean bShowAsTape, double tapeWidth, Fill paletteEntry, boolean usePaletteLineColor)
			throws ChartException {
		Fill seriesPalette = getSeriesPaletteEntry();

		double zeroLocation = 0;
		if (isDimension3D()) {
			zeroLocation = ((SeriesRenderingHints3D) isrh).getPlotBaseLocation();
		} else {
			SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
			zeroLocation = srh.getZeroLocation();

			final Bounds boClientArea = srh.getClientAreaBounds(true);
			final double dSeriesThickness = srh.getSeriesThickness();
			if (((ChartWithAxes) getModel()).getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
				boClientArea.delta(-dSeriesThickness, dSeriesThickness, 0, 0);
			}

			if (((ChartWithAxes) getModel()).isTransposed()) {
				if (zeroLocation < boClientArea.getLeft()) {
					zeroLocation = boClientArea.getLeft();
				}

				if (zeroLocation > boClientArea.getLeft() + boClientArea.getWidth()) {
					zeroLocation = boClientArea.getLeft() + boClientArea.getWidth();
				}
			} else {
				if (zeroLocation < boClientArea.getTop()) {
					zeroLocation = boClientArea.getTop();
				}

				if (zeroLocation > boClientArea.getTop() + boClientArea.getHeight()) {
					zeroLocation = boClientArea.getTop() + boClientArea.getHeight();
				}
			}
		}

		boolean bStacked = getSeries().isStacked();
		AreaSeries as = (AreaSeries) getSeries();
		if (!bStacked && as.isConnectMissingValue()) {
			DataPointsSeeker seeker = DataPointsSeeker.create(isrh.getDataPoints(), (AreaSeries) getSeries(), bStacked);
			List<Location> lst = new ArrayList<>();

			// #44030 Keep the invalid point before the first valid point if it is not the
			// first of loa list.
			// Set invalid point value to the minimum value of the Y-axis.
			if (seeker.next()) {
				if (seeker.getIndex() > 0) {
					lst.add(loa[seeker.getIndex() - 1]);
				}
				lst.add(loa[seeker.getIndex()]);
			}

			while (seeker.next()) {
				lst.add(loa[seeker.getIndex()]);
			}
			// Keep the invalid point after the last valid point if it is not the last of
			// the loa list.
			// Set invalid point value to the minimum value of the Y-axis.
			if (seeker.getIndex() < seeker.size() - 1) {
				lst.add(loa[seeker.getIndex() + 1]);
			}

			loa = isDimension3D() ? new Location3D[lst.size()] : new Location[lst.size()];
			loa = lst.toArray(loa);
		}

		final CurveRenderer cr = new CurveRenderer(((ChartWithAxes) getModel()), this, lia, loa, zeroLocation,
				bShowAsTape, tapeWidth, true, getSeries().isTranslucent(),
				getSeries().isStacked() || getAxis().isPercent(), true, true,
				seriesPalette != null ? seriesPalette : paletteEntry, usePaletteLineColor, true);
		cr.draw(ipr);
	}

	@Override
	protected void renderDataPoints(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh, DataPointHints[] dpha,
			LineAttributes lia, Location[] loa, boolean bShowAsTape, double dTapeWidth, Fill paletteEntry,
			boolean usePaletteLineColor) throws ChartException {
		Fill seriesPalette = getSeriesPaletteEntry();
		if (seriesPalette != null) {
			paletteEntry = seriesPalette;
		}

		AreaDataPointsRenderer dpRender = AreaDataPointsRenderer.create(this, ipr, isrh, dpha, loa, bShowAsTape,
				dTapeWidth, paletteEntry);

		dpRender.render();
	}

	protected void renderShadow(IPrimitiveRenderer ipr, Plot p, LineAttributes lia, Location[] loa,
			boolean bShowAsTape) {
		// AREA DONT RENDER A SHADOW
	}

	@Override
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

		final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
				PolygonRenderEvent.class);

		Location[] loa = new Location[5];
		loa[0] = goFactory.createLocation(bo.getLeft() + 1 * getDeviceScale(),
				bo.getTop() + bo.getHeight() - 2 * getDeviceScale());
		loa[1] = goFactory.createLocation(bo.getLeft() + bo.getWidth() - 1 * getDeviceScale(),
				bo.getTop() + bo.getHeight() - 2 * getDeviceScale());
		loa[2] = goFactory.createLocation(bo.getLeft() + bo.getWidth() * 5 / 6, bo.getTop() + bo.getHeight() / 3);
		loa[3] = goFactory.createLocation(bo.getLeft() + bo.getWidth() * 2 / 3, bo.getTop() + bo.getHeight() / 2);
		loa[4] = goFactory.createLocation(bo.getLeft() + bo.getWidth() / 2, bo.getTop() + 1 * getDeviceScale());

		pre.setBackground(fPaletteEntry);
		pre.setPoints(loa);
		ipr.fillPolygon(pre);

		// render outline
		LineAttributes liaMarker = ls.getLineAttributes();
		if (liaMarker.isVisible()) {
			if (ls.isPaletteLineColor()) {
				liaMarker = goFactory.copyOf(liaMarker);
				liaMarker.setColor(FillUtil.getColor(fPaletteEntry));
			}

			pre.setOutline(liaMarker);
			ipr.drawPolygon(pre);
		}

	}

	@Override
	protected Location[] filterNull(Location[] ll) {
		// Fix null values to use base line instead
		final Bounds boClientArea = getPlotBounds();
		List<Location> al = new ArrayList<>();
		for (int i = 0; i < ll.length; i++) {
			if (Double.isNaN(ll[i].getX())) {
				ll[i].setX(boClientArea.getLeft());
			}
			if (Double.isNaN(ll[i].getY())) {
				ll[i].setY(boClientArea.getTop() + boClientArea.getHeight());
			}

			al.add(ll[i]);
		}

		if (ll instanceof Location3D[]) {
			return al.toArray(new Location3D[al.size()]);
		}
		return al.toArray(new Location[al.size()]);
	}

	/**
	 * DataPointsRenderer is used to render the data points of an area series.
	 */
	private abstract static class AreaDataPointsRenderer extends DataPointsRenderer {

		protected final Fill fillColor;
		protected final double zeroLocation;

		AreaDataPointsRenderer(Context context) throws ChartException {
			super(context);
			dc.setPlaneShadowsComparator(WrappedInstruction.getDefaultComarator());
			dc.setPlanesComparator(WrappedInstruction.getDefaultComarator());
			this.zeroLocation = initZeroLocation(context.line, context.isrh);

			Fill fillColor = context.paletteEntry;

			if (ls.isTranslucent()) {
				if (fillColor instanceof ColorDefinition) {
					fillColor = goFactory.translucent((ColorDefinition) fillColor);
				}
			}
			this.fillColor = fillColor;
		}

		/**
		 * Computes the zero location.
		 */
		protected abstract double initZeroLocation(Line line, ISeriesRenderingHints isrh) throws ChartException;

		public static AreaDataPointsRenderer create(Area area, IPrimitiveRenderer ipr, ISeriesRenderingHints isrh,
				DataPointHints[] dpha, Location[] loa, boolean bShowAsTape, double dTapeWidth, Fill paletteEntry)
				throws ChartException {
			AreaDataPointsRenderer.Context context = new AreaDataPointsRenderer.Context(area, ipr, isrh, dpha,
					paletteEntry);

			if (area.isDimension3D()) {
				return new AreaDataPointsRenderer3D(context, loa, dTapeWidth);
			} else if (bShowAsTape) {
				return new AreaDataPointsRenderer2Dplus(context, loa);
			} else if (context.bStacked) {
				return new AreaDataPointsRenderer2DStacked(context, loa);
			} else {
				return new AreaDataPointsRenderer2D(context, loa);
			}

		}
	}

	/**
	 * DataPointsRenderer implementation for 2D not-stacked case.
	 */
	private static class AreaDataPointsRenderer2D extends AreaDataPointsRenderer {

		protected final SeriesRenderingHints srh;
		protected final Location[] loa;
		protected final Transposition trans;
		protected final LineRenderEvent lre;
		protected final PolygonRenderEvent pre;
		protected final List<Location> lstPolygon = new ArrayList<>();

		AreaDataPointsRenderer2D(Context context, Location[] loa) throws ChartException {
			super(context);

			this.srh = (SeriesRenderingHints) context.isrh;
			this.loa = loa;
			this.trans = ((ChartWithAxes) context.line.getModel()).isTransposed() ? Transposition.TRANSPOSED
					: Transposition.NOT_TRANSPOSED;

			StructureSource sourceObj = createSeriesSource();
			this.pre = ((EventObjectCache) context.ipr).getEventObject(sourceObj, PolygonRenderEvent.class);
			this.lre = ((EventObjectCache) context.ipr).getEventObject(sourceObj, LineRenderEvent.class);
			this.lre.setLineAttributes(lia);
		}

		@Override
		protected double initZeroLocation(Line line, ISeriesRenderingHints isrh) throws ChartException {
			SeriesRenderingHints srh = (SeriesRenderingHints) isrh;
			double zeroLocation = srh.getZeroLocation();

			// Adjusts zero location for study layout case.
			if (ChartUtil.isStudyLayout(cwa)) {
				zeroLocation = srh.getLocationOnOrthogonal(srh.getOrthogonalScale().getMinimum());
			}

			final Bounds boClientArea = srh.getClientAreaBounds(true);
			final double dSeriesThickness = srh.getSeriesThickness();
			if (cwa.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
				boClientArea.delta(-dSeriesThickness, dSeriesThickness, 0, 0);
			}

			if (cwa.isTransposed()) {
				if (zeroLocation < boClientArea.getLeft()) {
					zeroLocation = boClientArea.getLeft();
				}

				if (zeroLocation > boClientArea.getLeft() + boClientArea.getWidth()) {
					zeroLocation = boClientArea.getLeft() + boClientArea.getWidth();
				}
			} else {
				if (zeroLocation < boClientArea.getTop()) {
					zeroLocation = boClientArea.getTop();
				}

				if (zeroLocation > boClientArea.getTop() + boClientArea.getHeight()) {
					zeroLocation = boClientArea.getTop() + boClientArea.getHeight();
				}
			}

			return zeroLocation;
		}

		protected void drawFrontLine(int pindex, int index) {
			lre.setStart(loa[pindex]);
			lre.setEnd(loa[index]);
			lre.setSourceObject(createDataPointSource(pindex));
			dc.addLine(lre);
		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) {
			int lindex = seeker.getIndex();
			if (lindex < seeker.size() - 1) {
				trans.setY(loa[lindex + 1], zeroLocation);
				addPolygonPoint(loa[lindex + 1]);
				drawFrontLine(lindex, lindex + 1);
			} else {
				Location pt1 = loa[lindex].copyInstance();
				trans.setY(pt1, zeroLocation);
				addPolygonPoint(pt1);
			}

			fillPolygon();
		}

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) {
			int findex = seeker.getIndex();

			if (findex > 0) {
				trans.setY(loa[findex - 1], zeroLocation);
				drawFrontLine(findex - 1, findex);
				addPolygonPoint(loa[findex - 1]);
			} else {
				Location pt0 = loa[findex].copyInstance();
				trans.setY(pt0, zeroLocation);
				addPolygonPoint(pt0);
			}
			addPolygonPoint(loa[findex]);
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) {
			int index = seeker.getIndex();
			int pindex = seeker.getPrevIndex();

			if (seeker.isNull()) {
				trans.setY(loa[index], zeroLocation);
			}

			addPolygonPoint(loa[index]);
			drawFrontLine(pindex, index);
		}

		protected void addPolygonPoint(Location lo) {
			lstPolygon.add(lo);
		}

		protected void fillPolygon() {
			if (lstPolygon.size() > 0) {
				Location[] pa = lstPolygon.toArray(new Location[lstPolygon.size()]);

				pre.setOutline(null);
				pre.setPoints(pa);
				pre.setBackground(fillColor);
				pre.setSourceObject(createSeriesSource());
				dc.addPlane(pre, PrimitiveRenderEvent.FILL);
			}
		}
	}

	/**
	 * DataPointsRenderer implementation for 2D stacked case.
	 */
	private static class AreaDataPointsRenderer2DStacked extends AreaDataPointsRenderer2D {

		protected Location[] loaLast;

		AreaDataPointsRenderer2DStacked(Context context, Location[] loa) throws ChartException {
			super(context, loa);
		}

		/*
		 * Fixes a point with null value. It's assumed that the value of the given point
		 * is null.
		 */
		protected void fixPoint(int index) {
			if (loaLast == null) {
				trans.setY(loa[index], zeroLocation);
			} else {
				trans.setY(loa[index], trans.getY(loaLast[index]));
			}
		}

		protected void loadLastStates() {
			Object obj = context.line.getRunTimeContext().getState(STACKED_SERIES_LOCATION_KEY);

			if (obj instanceof Location[]) {
				loaLast = (Location[]) obj;
			} else if (obj instanceof List) {
				@SuppressWarnings("rawtypes")
				List lst = (List) obj;
				Location[] last = new Location[lst.size()];
				for (int i = 0; i < lst.size(); i++) {
					Object o = lst.get(i);
					if (o instanceof double[]) {
						last[i] = goFactory.createLocation(((double[]) o)[0], ((double[]) o)[1]);
					}
				}
				loaLast = last;
			}
		}

		protected void saveStates() {
			if (context.line.isLastRuntimeSeriesInAxis()) {
				// clean stack state.
				context.line.getRunTimeContext().putState(STACKED_SERIES_LOCATION_KEY, null);
			} else {
				List<double[]> list = new ArrayList<>();
				for (Location lo : loa) {
					double[] l = { lo.getX(), lo.getY() };
					list.add(l);
				}
				context.line.getRunTimeContext().putState(STACKED_SERIES_LOCATION_KEY, list);
			}
		}

		// protected double fixNaN( double baseValue, double currentValue,
		// int currentIndex, double[] lastValues )
		// {
		// // only fix NaN values
		// if ( !Double.isNaN( currentValue ) )
		// {
		// return currentValue;
		// }
		//
		// // use baseValue if this is the first series
		// if ( lastValues == null )
		// {
		// return baseValue;
		// }
		//
		// return lastValues[currentIndex];
		// }

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) {
			loadLastStates();
			int findex = seeker.getIndex();
			if (seeker.isNull()) {
				fixPoint(findex);
			}
			addPolygonPoint(loa[findex]);
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) {
			int index = seeker.getIndex();
			if (seeker.isNull()) {
				fixPoint(index);
			}
			addPolygonPoint(loa[index]);
			drawFrontLine(index - 1, index);
		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) {
			if (loaLast != null) {
				for (int i = loaLast.length - 1; i >= 0; i--) {
					addPolygonPoint(loaLast[i]);
				}
			} else {
				Location pt = lstPolygon.get(lstPolygon.size() - 1).copyInstance();
				trans.setY(pt, zeroLocation);
				addPolygonPoint(pt);
				pt = lstPolygon.get(0).copyInstance();
				trans.setY(pt, zeroLocation);
				addPolygonPoint(pt);
			}

			fillPolygon();
			saveStates();
		}

	}

	/**
	 * DataPointsRenderer implementation for 2D+ case.
	 */
	private static class AreaDataPointsRenderer2Dplus extends AreaDataPointsRenderer2DStacked {

		private static class Envelop {

			private static class IndexedPoint {

				public int index;
				public Point pt;

				public IndexedPoint(int index, double x, double y) {
					this.index = index;
					this.pt = new Point(x, y);
				}

				public IndexedPoint(int index, Point pt) {
					this.index = index;
					this.pt = pt;
				}

				public IndexedPoint copy() {
					return new IndexedPoint(index, pt.x, pt.y);
				}

				public double getX() {
					return pt.getX();
				}

				public double getY() {
					return pt.getY();
				}

				public void setY(double y) {
					pt.setY(y);
				}

				@Override
				public String toString() {
					StringBuilder sb = new StringBuilder("["); //$NON-NLS-1$
					sb.append(index);
					sb.append(", "); //$NON-NLS-1$
					sb.append(pt.x);
					sb.append(", "); //$NON-NLS-1$
					sb.append(pt.y);
					sb.append("]"); //$NON-NLS-1$
					return sb.toString();
				}
			}

			protected final static IGObjectFactory _goFactory = GObjectFactory.instance();
			private List<IndexedPoint> top = new ArrayList<>();
			private List<IndexedPoint> bottom = new ArrayList<>();
			private double baseStart;
			private double baseEnd;
			private boolean bTransposed;

			public Envelop(double baseStart, double baseEnd, double zeroLocation, boolean bTransposed) {
				this.bTransposed = bTransposed;
				this.baseStart = baseStart;
				this.baseEnd = baseEnd;
				IndexedPoint ipt0 = new IndexedPoint(0, baseStart, zeroLocation);
				IndexedPoint ipt1 = new IndexedPoint(0, baseEnd, zeroLocation);
				top.add(ipt0);
				top.add(ipt1);
				IndexedPoint ipt2 = ipt0.copy();
				IndexedPoint ipt3 = ipt1.copy();
				bottom.add(ipt2);
				bottom.add(ipt3);
			}

			private List<IndexedPoint> merge(List<IndexedPoint> list, int index, StraightLine sl, boolean bLessThan) {
				int len = list.size();
				if (len < 2) {
					return list;
				}

				List<IndexedPoint> list_new = new ArrayList<>();

				IndexedPoint ipt0 = list.get(0);

				for (int i = 1; i < len; i++) {
					IndexedPoint ipt1 = list.get(i);

					double y0 = sl.getYfromX(ipt0.getX());
					double y1 = sl.getYfromX(ipt1.getX());

					boolean bUpdate0 = bLessThan ? (y0 < ipt0.getY()) : (y0 > ipt0.getY());
					boolean bUpdate1 = bLessThan ? (y1 < ipt1.getY()) : (y1 > ipt1.getY());

					Point pt = sl.getCrossPoint(ipt0.pt, ipt1.pt);
					IndexedPoint ipn = new IndexedPoint(bUpdate0 ? ipt0.index : index, pt);

					if (i == 1) {
						if (bUpdate0) {
							ipt0.index = index;
							ipt0.setY(y0);
						}
						list_new.add(ipt0);
					} else if (!bUpdate0) {
						list_new.add(ipt0);
					}

					if (pt != null) {
						list_new.add(ipn);
					}

					if (i == len - 1) {
						if (bUpdate1) {
							ipt1.setY(y1);
						}
						list_new.add(ipt1);
					}

					ipt0 = ipt1;
				}

				return list_new;
			}

			private void mergeTop(int index, StraightLine sl) {
				this.top = merge(this.top, index, sl, !bTransposed);
			}

			private void mergeBottom(int index, StraightLine sl) {
				this.bottom = merge(this.bottom, index, sl, bTransposed);
			}

			public static Location[] createPolygonFromLine(double x0, double y0, double x1, double y1,
					double dTapeWidth) {
				Location[] loa = new Location[4];
				loa[0] = _goFactory.createLocation(x0, y0);
				loa[1] = _goFactory.createLocation(x1, y1);
				loa[2] = _goFactory.createLocation(x1 + dTapeWidth, y1 - dTapeWidth);
				loa[3] = _goFactory.createLocation(x0 + dTapeWidth, y0 - dTapeWidth);
				return loa;
			}

			public List<Location[]> getTopChanges(int index, double dTapeWidth) {
				List<Location[]> list = new ArrayList<>();
				int len = top.size();
				IndexedPoint[] top_a = new IndexedPoint[len];
				top_a = top.toArray(top_a);

				if (len > 0) {
					for (int i = 1; i < len; i++) {
						IndexedPoint ipt0 = top_a[i - 1];
						IndexedPoint ipt1 = top_a[i];

						if (ipt0.index == index - 1 || ipt0.index == index) {
							if (!bTransposed) {
								Location[] loa = createPolygonFromLine(ipt0.getX(), ipt0.getY(), ipt1.getX(),
										ipt1.getY(), dTapeWidth);
								list.add(loa);
							} else {
								Location[] loa = createPolygonFromLine(ipt0.getY(), ipt0.getX(), ipt1.getY(),
										ipt1.getX(), dTapeWidth);
								list.add(loa);
							}

						}
					}
				}
				return list;
			}

			public List<Location[]> getBottomChanges(int index, double dTapeWidth) {
				List<Location[]> list = new ArrayList<>();
				int len = bottom.size();
				IndexedPoint[] bottom_a = new IndexedPoint[len];
				bottom_a = bottom.toArray(bottom_a);

				if (len > 0) {
					for (int i = 1; i < len; i++) {
						IndexedPoint ipt0 = bottom_a[i - 1];
						IndexedPoint ipt1 = bottom_a[i];

						if (ipt0.index == index - 1 || ipt0.index == index) {
							if (!bTransposed) {
								Location[] loa = createPolygonFromLine(ipt0.getX(), ipt0.getY(), ipt1.getX(),
										ipt1.getY(), dTapeWidth);

								list.add(loa);
							} else {
								Location[] loa = createPolygonFromLine(ipt0.getY(), ipt0.getX(), ipt1.getY(),
										ipt1.getX(), dTapeWidth);

								list.add(loa);
							}
						}
					}
				}

				return list;
			}

			public void addLine(int index, double valueStart, double valueEnd) {
				StraightLine sl = new StraightLine(baseStart, valueStart, baseEnd, valueEnd);

				mergeTop(index, sl);
				mergeBottom(index, sl);
			}
		}

		private static class StraightLine {

			private double x0, y0;
			private double k;

			public StraightLine(double x0, double y0, double x1, double y1) {
				this.x0 = x0;
				this.y0 = y0;
				k = (y1 - y0) / (x1 - x0);
			}

			/*
			 * return the cross point of this line and linesegment(pt0-pt1) if there is any,
			 * null otherwise
			 */
			public Point getCrossPoint(Point pt0, Point pt1) {
				Point pt = null;

				double xst = pt0.x;
				double yst0 = pt0.y;
				double yst1 = getYfromX(xst);

				double xed = pt1.x;
				double yed0 = pt1.y;
				double yed1 = getYfromX(xed);

				if (xed != xst) {
					if ((yst0 - yst1) * (yed0 - yed1) < 0) {
						double x, y;

						if (yed1 != yst1) {
							double rate = (yed0 - yst0) / (yed1 - yst1);
							y = (yst0 - rate * yst1) / (1 - rate);
							x = getXfromY(y);
						} else {
							y = yst1;
							double rate = (xed - xst) / (yed0 - yst0);
							x = xst + rate * (y - yst0);
						}

						pt = new Point(x, y);
					} else if (yst0 == yst1) {
						pt = new Point(xst, yst0);
					} else if (yed0 == yed1) {
						pt = new Point(xed, yed0);
					} else if (yst0 == yed0) {
						if (k != 0) {
							double y = yst0;
							double x = getXfromY(y);
							pt = new Point(x, y);
						}
					}
				}

				if (pt != null) {
					if (pt.x < Math.min(pt0.x, pt1.x) || pt.x > Math.max(pt0.x, pt1.x)) {
						pt = null;
					}
				}

				return pt;
			}

			public double getYfromX(double x) {
				return y0 + k * (x - x0);
			}

			public double getXfromY(double y) {
				return x0 + (y - y0) / k;
			}
		}

		protected final int iSeriesIndex;
		private final double dTapeWidth;
		private final ColorDefinition tapeColor;
		private final ColorDefinition sideColor;
		private final Location[] loaPlane = createLocationArray(4);

		private Envelop[] envelops;

		AreaDataPointsRenderer2Dplus(Context context, Location[] loa) throws ChartException {
			super(context, loa);
			this.iSeriesIndex = context.line.getSeriesIndex();
			this.dTapeWidth = srh.getSeriesThickness();

			Fill paletteEntry = context.paletteEntry;
			ColorDefinition tapeColor = FillUtil.getBrighterColor(paletteEntry);
			ColorDefinition sideColor = FillUtil.getDarkerColor(paletteEntry);

			if (ls.isTranslucent()) {
				tapeColor = tapeColor.translucent();
				sideColor = sideColor.translucent();
			}
			this.tapeColor = tapeColor;
			this.sideColor = sideColor;
		}

		/*
		 * fill the left most side plane
		 */
		private void fillLeftSide(int findex) {
			int index = findex;
			double x = loa[index].getX();
			double y = loa[index].getY();

			loaPlane[0].set(x, y);
			loaPlane[1].set(x + dTapeWidth, y - dTapeWidth);

			double lastLocation = loaLast == null ? zeroLocation : trans.getY(loaLast[index]);

			if (trans == Transposition.TRANSPOSED) {
				loaPlane[2].set(lastLocation + dTapeWidth, y - dTapeWidth);
				loaPlane[3].set(lastLocation, y);
			} else {
				loaPlane[2].set(x + dTapeWidth, lastLocation - dTapeWidth);
				loaPlane[3].set(x, lastLocation);
			}

			pre.setOutline(null);
			pre.setBackground(sideColor);
			pre.setPoints(loaPlane);
			dc.addPlaneShadow(pre, PrimitiveRenderEvent.FILL);
		}

		private void fillRightSide(int lindex) {
			int index = lindex;
			double x = loa[index].getX();
			double y = loa[index].getY();
			loaPlane[0].set(x, y);
			loaPlane[1].set(x + dTapeWidth, y - dTapeWidth);

			// get corresponding index considering last series may be curve line
			double lastLocation = loaLast == null ? zeroLocation
					: trans.getY(loaLast[loaLast.length - (loa.length - index)]);

			if (trans == Transposition.TRANSPOSED) {
				loaPlane[2].set(lastLocation + dTapeWidth, y - dTapeWidth);
				loaPlane[3].set(lastLocation, y);
			} else {
				loaPlane[2].set(x + dTapeWidth, lastLocation - dTapeWidth);
				loaPlane[3].set(x, lastLocation);
			}
			pre.setOutline(null);
			pre.setBackground(sideColor);
			pre.setPoints(loaPlane);
			pre.setSourceObject(createDataPointSource(index));
			// Rightmost shadow must be in the top of z-order, so
			// it's not addPlaneShadow
			dc.addPlane(pre, PrimitiveRenderEvent.FILL);
		}

		// private void fillPlaneShadow( Fill fill, Location[] loa,
		// Object sourceObject, int zorder_hint )
		// {
		// pre.setOutline( null );
		// pre.setBackground( tapeColor );
		// pre.setPoints( loa );
		// pre.setSourceObject( sourceObject );
		// dc.addPlaneShadow( pre, PrimitiveRenderEvent.FILL, zorder_hint );
		// }

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) {
			super.beforeLoop(seeker);

			if (context.line.isRightToLeft() && context.dpha[0].getOrthogonalValue() != null) {
				fillLeftSide(seeker.getIndex());
			}

		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) {
			super.processDataPoint(seeker);
			int index = seeker.getIndex();
			int pindex = seeker.getPrevIndex();

			double loX = loa[index].getX();
			double loY = loa[index].getY();
			double loXp = loa[pindex].getX();
			double loYp = loa[pindex].getY();

			if (envelops == null) {
				envelops = new Envelop[seeker.size()];
			}

			boolean isTransposed = trans == Transposition.TRANSPOSED;

			if (trans == Transposition.TRANSPOSED) {
				if (envelops[index] == null) {
					envelops[index] = new Envelop(loYp, loY, zeroLocation, isTransposed);
				}

				envelops[index].addLine(iSeriesIndex, loXp, loX);

			} else {
				if (envelops[index] == null) {
					envelops[index] = new Envelop(loXp, loX, zeroLocation, isTransposed);
				}

				envelops[index].addLine(iSeriesIndex, loYp, loY);
			}

			// ------ Render the top tape.
			List<Location[]> tops = envelops[index].getTopChanges(iSeriesIndex, dTapeWidth);
			for (Location[] polygon : tops) {
				pre.setOutline(null);
				pre.setBackground(tapeColor);
				pre.setPoints(polygon);
				pre.setSourceObject(createDataPointSource(index));
				dc.addPlaneShadow(pre, PrimitiveRenderEvent.FILL, context.dpha.length + index);
			}

			// Render the bottom tape.
			// Only if the current or previous value is negative
			List<Location[]> bottoms = envelops[index].getBottomChanges(iSeriesIndex, dTapeWidth);
			for (Location[] polygon : bottoms) {
				pre.setOutline(null);
				pre.setBackground(tapeColor);
				pre.setPoints(polygon);
				pre.setSourceObject(createDataPointSource(index));
				dc.addPlaneShadow(pre, PrimitiveRenderEvent.FILL, index);
			}

		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) {
			if (!context.line.isRightToLeft() && context.dpha[seeker.size() - 1].getOrthogonalValue() != null) {
				fillRightSide(seeker.getIndex());
			}
			super.afterLoop(seeker);
		}

		@Override
		protected void loadLastStates() {
			super.loadLastStates();
			envelops = (Envelop[]) context.line.getRunTimeContext().getState(AREA_ENVELOPS);
		}

		@Override
		protected void saveStates() {
			RunTimeContext rtc = context.line.getRunTimeContext();
			if (context.line.isLastRuntimeSeriesInAxis()) {
				// clean stack state.
				rtc.putState(STACKED_SERIES_LOCATION_KEY, null);
				rtc.putState(AREA_ENVELOPS, null);
			} else {
				List<double[]> list = new ArrayList<>();
				for (Location lo : loa) {
					double[] l = { lo.getX(), lo.getY() };
					list.add(l);
				}
				rtc.putState(STACKED_SERIES_LOCATION_KEY, list);
				rtc.putState(AREA_ENVELOPS, envelops);
			}
		}

	}

	/*
	 * DataPointsRenderer implementation for 3D.
	 */
	private static class AreaDataPointsRenderer3D extends AreaDataPointsRenderer {

		private final SeriesRenderingHints3D srh3d;
		private final Location3D[] loa3d;
		private final Location3D loStart = goFactory.createLocation3D(0, 0, 0);
		private final Location3D loEnd = goFactory.createLocation3D(0, 0, 0);

		private final Location3D[] loaPlane3d = createLocation3DArray(4);
		private final Polygon3DRenderEvent pre3d;
		private final Line3DRenderEvent lre3d;

		private final double dTapeWidth;

		private final ColorDefinition tapeColor;
		private final ColorDefinition sideColor;

		private final double plotBaseLocation;
		private final double plotHeight;

		private int findex;

		AreaDataPointsRenderer3D(Context context, Location[] loa, double dTapeWidth) throws ChartException {
			super(context);

			Object sourceObj = createSeriesSource();
			this.pre3d = ((EventObjectCache) context.ipr).getEventObject(sourceObj, Polygon3DRenderEvent.class);
			this.lre3d = ((EventObjectCache) context.ipr).getEventObject(sourceObj, Line3DRenderEvent.class);
			lre3d.setLineAttributes(lia);

			this.srh3d = (SeriesRenderingHints3D) context.isrh;
			this.plotBaseLocation = srh3d.getPlotBaseLocation();
			this.plotHeight = srh3d.getPlotHeight();
			this.loa3d = (Location3D[]) loa;
			this.dTapeWidth = dTapeWidth;

			Fill paletteEntry = context.paletteEntry;
			ColorDefinition tapeColor = FillUtil.getBrighterColor(paletteEntry);
			ColorDefinition sideColor = FillUtil.getDarkerColor(paletteEntry);

			if (ls.isTranslucent()) {
				tapeColor = tapeColor.translucent();
				sideColor = sideColor.translucent();
			}
			this.tapeColor = tapeColor;
			this.sideColor = sideColor;
		}

		private static double shear(double plotBase, double plotHeight, double y) {
			if (y < plotBase) {
				y = plotBase;
			}
			if (y > plotBase + plotHeight) {
				y = plotBase + plotHeight;
			}
			return y;
		}

		private void fillBackPlane(int pindex, int index, DataPointsSeeker seeker) throws ChartException {
			// Because chart use Painter's algorithm to compute the polygons
			// Z-order, if the polygon is not convex hull, it needs to separate
			// the polygon to multiple convex polygon.
			double x0 = loa3d[pindex].getX();
			double y0 = shear(plotBaseLocation, plotHeight, loa3d[pindex].getY());
			double z0 = loa3d[pindex].getZ();

			double x1 = loa3d[index].getX();
			double y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
			double z1 = loa3d[index].getZ();

			Double pValue = Methods.asDouble(seeker.getDataPointHints(pindex).getOrthogonalValue());
			Double value = Methods.asDouble(seeker.getDataPointHints(index).getOrthogonalValue());
			Object source = createDataPointSource(index);
			if (zeroLocation <= y0) {
				if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
					double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
					double midX = x0 + (x1 - x0) * rate;
					double midY = y0 + (y1 - y0) * rate;

					Location3D[] loc = createLocation3DArray(3);
					loc[0].set(x0, y0, z0 - dTapeWidth);
					loc[1].set(midX, midY, z0 - dTapeWidth);
					loc[2].set(x0, midY, z0 - dTapeWidth);
					fill3DPlane(fillColor, source, loc, false);

					loc[0].set(midX, midY, z1 - dTapeWidth);
					loc[1].set(x1, midY, z1 - dTapeWidth);
					loc[2].set(x1, y1, z1 - dTapeWidth);
					fill3DPlane(fillColor, source, loc, false);
				} else {
					loaPlane3d[0].set(x0, y0, z0 - dTapeWidth);
					loaPlane3d[1].set(x1, y1, z1 - dTapeWidth);
					loaPlane3d[2].set(x1, zeroLocation, z1 - dTapeWidth);
					loaPlane3d[3].set(x0, zeroLocation, z0 - dTapeWidth);
					fill3DPlane(fillColor, source, false);
				}
			} else if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
				double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
				double midX = x0 + (x1 - x0) * rate;
				double midY = y0 + (y1 - y0) * rate;

				Location3D[] loc = createLocation3DArray(3);
				loc[0].set(x0, midY, z0 - dTapeWidth);
				loc[1].set(midX, midY, z0 - dTapeWidth);
				loc[2].set(x0, y0, z0 - dTapeWidth);
				fill3DPlane(fillColor, source, loc, false);

				loc[0].set(midX, midY, z1 - dTapeWidth);
				loc[1].set(x1, y1, z1 - dTapeWidth);
				loc[2].set(x1, midY, z1 - dTapeWidth);
				fill3DPlane(fillColor, source, loc, false);
			} else {
				loaPlane3d[0].set(x0, zeroLocation, z0 - dTapeWidth);
				loaPlane3d[1].set(x1, zeroLocation, z1 - dTapeWidth);
				loaPlane3d[2].set(x1, y1, z1 - dTapeWidth);
				loaPlane3d[3].set(x0, y0, z0 - dTapeWidth);
				fill3DPlane(fillColor, source, false);
			}
		}

		private Object fillFrontPlane(int pindex, int index, DataPointsSeeker seeker) throws ChartException {
			// Because chart use Painter's algorithm to compute the polygons
			// Z-order, if the polygon is not convex hull, it needs to separate
			// the polygon to multiple convex polygon.
			double x0 = loa3d[pindex].getX();
			double y0 = shear(plotBaseLocation, plotHeight, loa3d[pindex].getY());
			double z0 = loa3d[pindex].getZ();

			double x1 = loa3d[index].getX();
			double y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
			double z1 = loa3d[index].getZ();

			Double pValue = Methods.asDouble(seeker.getDataPointHints(pindex).getOrthogonalValue());
			Double value = Methods.asDouble(seeker.getDataPointHints(index).getOrthogonalValue());
			Object source = createDataPointSource(index);
			if (zeroLocation <= y0) {
				if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
					double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
					double midX = x0 + (x1 - x0) * rate;
					double midY = y0 + (y1 - y0) * rate;

					Location3D[] loc = createLocation3DArray(3);
					loc[0].set(x0, y0, z0);
					loc[1].set(midX + 1, midY, z0);
					loc[2].set(x0, midY, z0);
					fill3DPlane(fillColor, source, loc, true);

					loc[0].set(midX, midY, z1);
					loc[1].set(x1, midY, z1);
					loc[2].set(x1, y1, z1);
					return fill3DPlane(fillColor, source, loc, true);
				} else {
					loaPlane3d[0].set(x0, y0, z0);
					loaPlane3d[1].set(x0, zeroLocation, z0);
					loaPlane3d[2].set(x1, zeroLocation, z1);
					loaPlane3d[3].set(x1, y1, z1);
					return fill3DPlane(fillColor, source, false);
				}
			} else if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
				double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
				double midX = x0 + (x1 - x0) * rate;
				double midY = y0 + (y1 - y0) * rate;

				Location3D[] loc = createLocation3DArray(3);
				loc[0].set(x0, midY, z0);
				loc[1].set(midX + 1, midY, z0);
				loc[2].set(x0, y0, z0);
				fill3DPlane(fillColor, source, loc, true);

				loc[0].set(midX, midY, z1);
				loc[1].set(x1, y1, z1);
				loc[2].set(x1, midY, z1);
				return fill3DPlane(fillColor, source, loc, true);
			} else {
				loaPlane3d[0].set(x0, zeroLocation, z0);
				loaPlane3d[1].set(x0, y0, z0);
				loaPlane3d[2].set(x1, y1, z1);
				loaPlane3d[3].set(x1, zeroLocation, z1);
				return fill3DPlane(fillColor, source, false);
			}
		}

		private void fillLeftSidePlane(int findex) throws ChartException {
			double x = loa3d[findex].getX();
			double y = shear(plotBaseLocation, plotHeight, loa3d[findex].getY());
			double z = loa3d[findex].getZ();

			if (zeroLocation < loa3d[findex].getY()) {
				loaPlane3d[0].set(x, y, z);
				loaPlane3d[1].set(x, y, z - dTapeWidth);
				loaPlane3d[2].set(x, zeroLocation, z - dTapeWidth);
				loaPlane3d[3].set(x, zeroLocation, z);
			} else {
				loaPlane3d[0].set(x, y, z);
				loaPlane3d[1].set(x, zeroLocation, z);
				loaPlane3d[2].set(x, zeroLocation, z - dTapeWidth);
				loaPlane3d[3].set(x, y, z - dTapeWidth);
			}

			fill3DPlane(tapeColor, createSeriesSource(), false);
		}

		private void fillRightSidePlane(int lindex) throws ChartException {
			double x = loa3d[lindex].getX();
			double y = shear(plotBaseLocation, plotHeight, loa3d[lindex].getY());
			double z = loa3d[lindex].getZ();

			if (shear(plotBaseLocation, plotHeight, loa3d[lindex].getY()) > zeroLocation) {
				loaPlane3d[0].set(x, y, z);
				loaPlane3d[1].set(x, zeroLocation, z);
				loaPlane3d[2].set(x, zeroLocation, z - dTapeWidth);
				loaPlane3d[3].set(x, y, z - dTapeWidth);
			} else {
				loaPlane3d[0].set(x, y, z);
				loaPlane3d[1].set(x, y, z - dTapeWidth);
				loaPlane3d[2].set(x, zeroLocation, z - dTapeWidth);
				loaPlane3d[3].set(x, zeroLocation, z);
			}

			fill3DPlane(sideColor, createDataPointSource(lindex), false);

		}

		private void fillBottomPlane(int findex, int lindex, DataPointsSeeker seeker) throws ChartException {
			// Different polygons of chart might be crossing with each other, in order to
			// avoid
			// polygon overlay, here must divide bottom polygon to multiple polygons
			// according to every data point on series.
			Double pValue = Methods.asDouble(seeker.getDataPointHints(findex).getOrthogonalValue());
			Double value = Methods.asDouble(seeker.getDataPointHints(lindex).getOrthogonalValue());

			if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
				double x0 = loa3d[findex].getX();
				double y0 = shear(plotBaseLocation, plotHeight, loa3d[findex].getY());
				double z0 = loa3d[findex].getZ();

				double x1 = loa3d[lindex].getX();
				double y1 = shear(plotBaseLocation, plotHeight, loa3d[lindex].getY());
				double z1 = loa3d[lindex].getZ();
				boolean up = (y0 < y1);
				double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
				x1 = x0 + (x1 - x0) * rate;
				y1 = y0 + (y1 - y0) * rate;

				fillBottomPlane(x0, z0, x1, z1, up);

				x0 = x1;
				y0 = y1;
				z0 = loa3d[findex].getZ();

				x1 = loa3d[lindex].getX();
				y1 = shear(plotBaseLocation, plotHeight, loa3d[lindex].getY());
				z1 = loa3d[lindex].getZ();

				fillBottomPlane(x0, z0, x1, z1, !up);
			} else {
				double x0 = loa3d[findex].getX();
				double z0 = loa3d[findex].getZ();

				double x1 = loa3d[lindex].getX();
				double z1 = loa3d[lindex].getZ();

				fillBottomPlane(x0, z0, x1, z1, true);
			}
		}

		private void fillBottomPlane(double x0, double z0, double x1, double z1, boolean bDoubleSided)
				throws ChartException {
			loaPlane3d[0].set(x0, zeroLocation, z0);
			loaPlane3d[1].set(x0, zeroLocation, z0 - dTapeWidth);
			loaPlane3d[2].set(x1, zeroLocation, z1 - dTapeWidth);
			loaPlane3d[3].set(x1, zeroLocation, z1);

			fill3DPlane(fillColor, createSeriesSource(), bDoubleSided);
		}

		private void fillTopPlane(int pindex, int index, DataPointsSeeker seeker) throws ChartException {
			// Different polygons of chart might be crossing with each other, in
			// order to avoid
			// polygon overlay, here must divide bottom polygon to multiple
			// polygons
			// according to every data point on series.
			Double pValue = Methods.asDouble(seeker.getDataPointHints(pindex).getOrthogonalValue());
			Double value = Methods.asDouble(seeker.getDataPointHints(index).getOrthogonalValue());

			if (pValue != null && value != null && (pValue.doubleValue() * value.doubleValue()) < 0) {
				double x0 = loa3d[pindex].getX();
				double y0 = shear(plotBaseLocation, plotHeight, loa3d[pindex].getY());
				double z0 = loa3d[pindex].getZ();

				double x1 = loa3d[index].getX();
				double y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
				double z1 = loa3d[index].getZ();

				double rate = (0 - pValue.doubleValue()) / (value.doubleValue() - pValue.doubleValue());
				x1 = x0 + (x1 - x0) * rate;
				y1 = y0 + (y1 - y0) * rate;

				loaPlane3d[0].set(x0, y0, z0);
				loaPlane3d[1].set(x1, y1, z1);
				loaPlane3d[2].set(x1, y1, z1 - dTapeWidth);
				loaPlane3d[3].set(x0, y0, z0 - dTapeWidth);
				fill3DPlane(tapeColor, createDataPointSource(index), true);

				x0 = x1;
				y0 = y1;
				z0 = loa3d[findex].getZ();

				x1 = loa3d[index].getX();
				y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
				z1 = loa3d[index].getZ();

				loaPlane3d[0].set(x0, y0, z0);
				loaPlane3d[1].set(x1, y1, z1);
				loaPlane3d[2].set(x1, y1, z1 - dTapeWidth);
				loaPlane3d[3].set(x0, y0, z0 - dTapeWidth);
				fill3DPlane(tapeColor, createDataPointSource(index), true);
			} else {
				double x0 = loa3d[pindex].getX();
				double y0 = shear(plotBaseLocation, plotHeight, loa3d[pindex].getY());
				double z0 = loa3d[pindex].getZ();

				double x1 = loa3d[index].getX();
				double y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
				double z1 = loa3d[index].getZ();

				loaPlane3d[0].set(x0, y0, z0);
				loaPlane3d[1].set(x1, y1, z1);
				loaPlane3d[2].set(x1, y1, z1 - dTapeWidth);
				loaPlane3d[3].set(x0, y0, z0 - dTapeWidth);

				fill3DPlane(tapeColor, createDataPointSource(index), false);
			}
		}

		private Object fill3DPlane(Fill fillColor, Object sourceObj, boolean bDoubleSided) throws ChartException {
			pre3d.setDoubleSided(bDoubleSided);
			pre3d.setOutline(null);
			pre3d.setPoints3D(loaPlane3d);
			pre3d.setBackground(fillColor);
			pre3d.setSourceObject(sourceObj);
			Object event = dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);
			pre3d.setDoubleSided(false);
			return event;
		}

		private Object fill3DPlane(Fill fillColor, Object sourceObj, Location3D[] loc, boolean bDoubleSided)
				throws ChartException {
			pre3d.setDoubleSided(bDoubleSided);
			pre3d.setOutline(null);
			pre3d.setPoints3D(loc);
			pre3d.setBackground(fillColor);
			pre3d.setSourceObject(sourceObj);
			Object event = dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);
			pre3d.setDoubleSided(false);
			return event;
		}

		private void drawFrontLine(int pindex, int index, Object eventFront) {
			if (!lia.isVisible()) {
				return;
			}

			double x0 = loa3d[pindex].getX();
			double y0 = shear(plotBaseLocation, plotHeight, loa3d[pindex].getY());
			double z0 = loa3d[pindex].getZ();

			double x1 = loa3d[index].getX();
			double y1 = shear(plotBaseLocation, plotHeight, loa3d[index].getY());
			double z1 = loa3d[index].getZ();

			loStart.set(x0, y0, z0);
			loEnd.set(x1, y1, z1);

			lre3d.setStart3D(loStart);
			lre3d.setEnd3D(loEnd);
			AxesRenderHelper.addLine3DEvent(lre3d, eventFront, dc);
		}

		@Override
		protected double initZeroLocation(Line line, ISeriesRenderingHints isrh) throws ChartException {
			SeriesRenderingHints3D srh3d = (SeriesRenderingHints3D) isrh;
			return shear(srh3d.getPlotBaseLocation(), srh3d.getPlotHeight(), srh3d.getPlotZeroLocation());
		}

		@Override
		protected void afterLoop(DataPointsSeeker seeker) throws ChartException {
			int lindex = seeker.getIndex();
			fillRightSidePlane(lindex);
		}

		@Override
		protected void beforeLoop(DataPointsSeeker seeker) throws ChartException {
			findex = seeker.getIndex();
			if (findex >= 0) {
				fillLeftSidePlane(findex);
			}
		}

		@Override
		protected void processDataPoint(DataPointsSeeker seeker) throws ChartException {
			int index = seeker.getIndex();
			int pindex = seeker.getPrevIndex();
			fillBackPlane(pindex, index, seeker);
			fillTopPlane(pindex, index, seeker);
			fillBottomPlane(pindex, index, seeker);
			Object eventFront = fillFrontPlane(pindex, index, seeker);
			drawFrontLine(pindex, index, eventFront);
		}

	}

}
