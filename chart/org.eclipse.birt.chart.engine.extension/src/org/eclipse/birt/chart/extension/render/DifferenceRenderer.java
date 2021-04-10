/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.render;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.CurveRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Renderer utility for difference chart
 */

public final class DifferenceRenderer {

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	final static double kError = 0.5d;

	/**
	 * 
	 * Stands for a part of line with the start point and end point
	 * 
	 */
	static class CLine {

		public transient CPoint start;
		public transient CPoint end;

		/**
		 * Constructor
		 * 
		 * @param start       start point of line
		 * @param end         end point of line
		 * @param bTransposed
		 */
		public CLine(CPoint start, CPoint end) {
			this.start = start;
			this.end = end;
		}

		public double getSlope() {
			if (isVertical()) {
				return Double.NaN;
			}
			return (end.y - start.y) / (end.x - start.x);
		}

		public boolean isVertical() {
			return CPoint.bPrecise ? start.x == end.x : ChartUtil.mathEqual(start.x, end.x);
		}

		/**
		 * Checks the point is not out of bound. Needs to ensure the point is in the
		 * Line, otherwise it's useless.
		 * 
		 * @param point point, usually used as an intersection point
		 * @return if the point is in the bound of line
		 */
		boolean checkBoundOfPoint(CPoint point) {
			boolean b;
			// To use non-precise checking policy to ensure the intersection
			// point won't be ignored

			// if ( CPoint.bPrecise )
			// {
			// b = start.x < end.x ? point.x >= start.x && point.x <= end.x
			// : point.x <= start.x && point.x >= end.x;
			// // if ( b )
			// // {
			// // b = start.y < end.y ? point.y >= start.y
			// // && point.y <= end.y : point.y <= start.y
			// // && point.y >= end.y;
			// // }
			// }
			// else
			{
				// Only check one attribute since two lines should be both
				// checked
				b = ChartUtil.mathLT(start.x, end.x)
						? ChartUtil.mathGE(point.x, start.x) && ChartUtil.mathLE(point.x, end.x)
						: ChartUtil.mathLE(point.x, start.x) && ChartUtil.mathGE(point.x, end.x);
			}
			return b;
		}

		/**
		 * Finds intersection point of two lines
		 * 
		 * @param line other line to intersect
		 * @return the intersection point or null
		 */
		public CPoint findIntersection(CLine line) {
			if (this.isVertical() || line.isVertical()) {
				return null;
			}

			final double k1 = this.getSlope();
			final double k2 = line.getSlope();
			if (k1 == k2) {
				if (this.end.equals(line.start)) {
					return this.end;
				} else if (this.start.equals(line.end)) {
					return this.start;
				}
				return null;
			}

			double pointX = (line.start.y - this.start.y + k1 * this.start.x - k2 * line.start.x) / (k1 - k2);
			double pointY = k1 * (pointX - this.start.x) + this.start.y;
			CPoint intersection = null;
			if (this.start.bTransposed) {
				intersection = new CPoint(pointY, pointX, this.start.bTransposed);
			} else {
				intersection = new CPoint(pointX, pointY, this.start.bTransposed);
			}
			if (!this.checkBoundOfPoint(intersection) && !line.checkBoundOfPoint(intersection)) {
				return null;
			}
			// boolean b1 = this.checkBoundOfPoint( intersection );
			// boolean b2 = line.checkBoundOfPoint( intersection );
			// if ( b1 )
			// {
			// System.out.println( "b2:" + b2 + intersection );
			// System.out.println( this );
			// System.out.println( line );
			// }
			// if ( b2 )
			// {
			// System.out.println( "b1:" + b1 + intersection );
			// System.out.println( this );
			// System.out.println( line );
			// }
			// if ( !b1 && !b2 )
			// // || intersection.y == this.start.y
			// // || intersection.y == line.start.y )
			// {
			// return null;
			// }
			return intersection;
		}

		@Override
		public String toString() {
			return "Start point:" + start + " End point:" + end; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	static class CPointContainer {

		private final HashSet<String> set = new HashSet<String>();
		private final LinkedList<CPoint> list = new LinkedList<CPoint>();

		public int size() {
			return list.size();
		}

		public void clear() {
			set.clear();
			list.clear();
		}

		public boolean contains(CPoint o) {
			return set.contains(o.toString());
		}

		public void add(CPoint o) {
			set.add(o.toString());
			list.add(o);
		}

		public void addFirst(CPoint o) {
			set.add(o.toString());
			list.addFirst(o);
		}

		public List<CPoint> getPoints() {
			return list;
		}
	}

	static class CPoint {

		/**
		 * Main coordinate. If transposed, the value is Y coordinate.
		 */
		public transient double x;

		/**
		 * Sub coordinate. If transposed, the value is X coordinate.
		 */
		public transient double y;
		public transient boolean bTransposed = false;

		public static transient boolean bPrecise = false;

		public CPoint(double x, double y, boolean bTransposed) {
			this.bTransposed = bTransposed;
			if (bTransposed) {
				this.y = x;
				this.x = y;
			} else {
				this.x = x;
				this.y = y;
			}
		}

		public CPoint(double[] point, double offset, boolean bTransposed) {
			this(point[0] + offset, point[1] + offset, bTransposed);
		}

		public Location toLocation() {
			return bTransposed ? goFactory.createLocation(y, x) : goFactory.createLocation(x, y);
		}

		public void addToListBottom(CPointContainer points) {
			if (!points.contains(this)) {
				points.addFirst(this);
			}
		}

		public void addToList(CPointContainer points) {
			if (!points.contains(this)) {
				points.add(this);
			}
		}

		@Override
		public String toString() {
			return "b=" + bTransposed + " x=" + ChartUtil.formatDouble(x) + " y=" + ChartUtil.formatDouble(y); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof CPoint) {
				CPoint p = (CPoint) obj;
				if (bPrecise) {
					return this.x == p.x && this.y == p.y && this.bTransposed == p.bTransposed;
				}
				return ChartUtil.mathEqual(this.x, p.x) && ChartUtil.mathEqual(this.y, p.y)
						&& this.bTransposed == p.bTransposed;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Double.valueOf(this.x).hashCode() ^ (37 * Double.valueOf(this.y).hashCode())
					^ Boolean.valueOf(this.bTransposed).hashCode();
		}

		/**
		 * Compares X coordinate of two points
		 * 
		 * @param p other point
		 * @return the positive number when current point is located right or above
		 */
		public double compareX(CPoint p) {
			return bTransposed ? p.x - this.x : this.x - p.x;
		}

		/**
		 * Compares Y coordinate of two points
		 * 
		 * @param p other point
		 * @return the positive number when current point is located right or above
		 */
		public double compareY(CPoint p) {
			return bTransposed ? this.y - p.y : p.y - this.y;
		}
	}

	/**
	 * Creates fills for difference chart
	 * 
	 * @param paletteEntry fill from palette
	 * @param isColor      to determine output fill is ColorDefinition instance if
	 *                     possible
	 * @return Fill array
	 */
	public static Fill[] createDifferenceFillFromPalette(Fill paletteEntry, boolean isColor) {
		Fill fillColorP = null;
		Fill fillColorN = null;
		if (paletteEntry instanceof ColorDefinition) {
			fillColorP = goFactory.copyOf((ColorDefinition) paletteEntry);
			fillColorN = goFactory.copyOf((ColorDefinition) paletteEntry);
		} else if (paletteEntry instanceof MultipleFill) {
			EList<Fill> fills = ((MultipleFill) paletteEntry).getFills();
			// Copy instance to avoid the element been removed from
			// EObjectContainmentList
			fillColorP = goFactory.copyOf(fills.get(0));
			fillColorN = goFactory.copyOf(fills.get(1));
		} else if (isColor && paletteEntry instanceof Gradient) {
			fillColorP = goFactory.copyOf(((Gradient) paletteEntry).getStartColor());
			fillColorN = goFactory.copyOf(((Gradient) paletteEntry).getEndColor());
		} else {
			// Not clone since image or gradient can't be changed directly
			fillColorP = paletteEntry;
			fillColorN = paletteEntry;
		}
		return new Fill[] { fillColorP, fillColorN };
	}

	private static Fill[] createUpdateFills(AxesRenderer renderer, Fill paletteEntry) {
		final Fill seriesPalette = getSeriesPaletteEntry(renderer);
		if (seriesPalette != null) {
			paletteEntry = seriesPalette;
		}

		Fill[] fills = createDifferenceFillFromPalette(paletteEntry, false);

		if (paletteEntry instanceof ColorDefinition) {
			if (renderer.getSeries().isSetTranslucent() && renderer.getSeries().isTranslucent()) {
				fills[0] = goFactory.translucent((ColorDefinition) paletteEntry);
			} else {
				fills[0] = goFactory.copyOf((ColorDefinition) paletteEntry);
			}
			fills[1] = goFactory.copyOf((ColorDefinition) paletteEntry);
		}
		return fills;
	}

	public static void renderDifferencePolygon(AxesRenderer renderer, IPrimitiveRenderer ipr, DataPointHints[] dpha,
			Location[] loaP, Location[] loaN, LineAttributes liaP, LineAttributes liaN, Fill paletteEntry) {
		CPoint.bPrecise = false;

		if (loaP == null || loaN == null || loaP.length < 2 || loaN.length < 2) {
			return;
		}

		final boolean isTransposed = ((ChartWithAxes) renderer.getModel()).isTransposed();
		final LineRenderEvent lre = ((EventObjectCache) ipr)
				.getEventObject(StructureSource.createSeries(renderer.getSeries()), LineRenderEvent.class);
		final PolygonRenderEvent pre = ((EventObjectCache) ipr)
				.getEventObject(StructureSource.createSeries(renderer.getSeries()), PolygonRenderEvent.class);
		final DeferredCache dc = renderer.getDeferredCache();

		int findex = getFirstNonNullIndex(dpha);
		int lindex = getLastNonNullIndex(dpha);

		if (findex < 0 || lindex < 0) {
			// Returns since no valid entry
			return;
		}

		final Fill[] fills = createUpdateFills(renderer, paletteEntry);
		final Fill fillColorP = fills[0];
		final Fill fillColorN = fills[1];

		// Points to render polygon
		CPointContainer points = new CPointContainer();

		CPoint pStart = new CPoint(loaP[findex].getX(), loaP[findex].getY(), isTransposed);
		CPoint nStart = new CPoint(loaN[findex].getX(), loaN[findex].getY(), isTransposed);
		CPoint pEnd, nEnd;

		for (int i = findex + 1; i <= lindex; i++) {
			if (!isValidDifferenceEntry(dpha[i].getOrthogonalValue())) {
				// Skip the invalid entry
				continue;
			}

			// always add negative point in the bottom, positive point
			// in the top
			nStart.addToListBottom(points);
			pStart.addToList(points);

			pEnd = new CPoint(loaP[i].getX(), loaP[i].getY(), isTransposed);
			nEnd = new CPoint(loaN[i].getX(), loaN[i].getY(), isTransposed);

			CLine lp = new CLine(pStart, pEnd);
			CLine ln = new CLine(nStart, nEnd);

			// Render two lines
			renderLine(lp, lre, dc, liaP);
			renderLine(ln, lre, dc, liaN);

			// To find the intersection of two lines
			CPoint intersection = lp.findIntersection(ln);

			// If no intersection or the point may be counted in before
			if (intersection != null && !intersection.equals(lp.start) && !intersection.equals(ln.start)) {
				intersection.addToList(points);
				if (points.size() > 1) {
					renderPolygon(points.getPoints(), pre, dc, pStart.compareY(nStart) >= 0 ? fillColorP : fillColorN,
							renderer.getSeries());

					// Start the next polygon
					points.clear();
					intersection.addToList(points);
				}
			}

			// Since it's the last point, no need to find the intersection
			if (i == lindex || i == dpha.length - 1) {
				nEnd.addToListBottom(points);
				pEnd.addToList(points);
				if (points.size() > 1 && !locationEquals(loaP, loaN)) {
					renderPolygon(points.getPoints(), pre, dc, pEnd.compareY(nEnd) >= 0 ? fillColorP : fillColorN,
							renderer.getSeries());
				}
				points.clear();
				break;
			}

			pStart = pEnd;
			nStart = nEnd;
		}

	}

	static void renderPolygon(List<CPoint> points, PolygonRenderEvent pre, DeferredCache dc, Fill fillColor,
			Series as) {
		Location[] pa = new Location[points.size()];
		Iterator<CPoint> it = points.iterator();
		for (int i = 0; it.hasNext(); i++) {
			pa[i] = it.next().toLocation();
		}
		pre.setOutline(null);
		pre.setPoints(pa);
		pre.setBackground(fillColor);
		pre.setSourceObject(StructureSource.createSeries(as));
		dc.addPlane(pre, PrimitiveRenderEvent.FILL);
	}

	static void renderLine(CLine line, LineRenderEvent lre, DeferredCache dc, LineAttributes lia) {
		if (lia.isVisible()) {
			lre.setStart(line.start.toLocation());
			lre.setEnd(line.end.toLocation());
			lre.setLineAttributes(lia);
			// Use deferred cache to ensure Line rendering above polygon
			dc.addLine(lre);
		}
	}

	static Fill getSeriesPaletteEntry(AxesRenderer renderer) {
		Fill fPaletteEntry = null;
		SeriesDefinition sd = null;

		Series se = renderer.getSeries();

		if (se.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) se.eContainer();
		}

		if (sd != null) {
			int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
			if (iThisSeriesIndex >= 0) {
				EList<Fill> ePalette = sd.getSeriesPalette().getEntries();
				fPaletteEntry = FillUtil.getPaletteFill(ePalette, iThisSeriesIndex);

				renderer.updateTranslucency(fPaletteEntry, se);
			}
		}

		return fPaletteEntry;
	}

	public static boolean isValidDifferenceEntry(Object obj) {
		if (obj instanceof DifferenceEntry) {
			DifferenceEntry entry = (DifferenceEntry) obj;
			if (!Double.isNaN(entry.getPositiveValue()) && !Double.isNaN(entry.getNegativeValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the index of first non-null value.
	 * 
	 * @param dpha
	 */
	static int getFirstNonNullIndex(DataPointHints[] dpha) {
		for (int i = 0; i < dpha.length; i++) {
			if (isValidDifferenceEntry(dpha[i].getOrthogonalValue())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of last non-null value.
	 * 
	 * @param dpha
	 */
	static int getLastNonNullIndex(DataPointHints[] dpha) {
		for (int i = dpha.length - 1; i >= 0; i--) {
			if (isValidDifferenceEntry(dpha[i].getOrthogonalValue())) {
				return i;
			}
		}
		return -1;
	}

	public static void renderDifferenceCurve(AxesRenderer renderer, IPrimitiveRenderer ipr, DataPointHints[] dpha,
			Location[] loaP, Location[] loaN, LineAttributes liaP, LineAttributes liaN, Fill paletteEntry) {
		CPoint.bPrecise = true;

		boolean bConnectingMissing = ((LineSeries) renderer.getSeries()).isConnectMissingValue();
		List<double[]> listP = CurveRenderer.generateCurvePoints(renderer, loaP, bConnectingMissing, 0, 0);
		List<double[]> listN = CurveRenderer.generateCurvePoints(renderer, loaN, bConnectingMissing, 0, 0);

		if (listP == null || listN == null || listP.size() < 2 || listN.size() < 2) {
			return;
		}
		final int lengthP = listP.size();
		final int lengthN = listN.size();

		final boolean bTransposed = ((ChartWithAxes) renderer.getModel()).isTransposed();
		final LineRenderEvent lre = ((EventObjectCache) ipr)
				.getEventObject(StructureSource.createSeries(renderer.getSeries()), LineRenderEvent.class);
		final PolygonRenderEvent pre = ((EventObjectCache) ipr)
				.getEventObject(StructureSource.createSeries(renderer.getSeries()), PolygonRenderEvent.class);
		final DeferredCache dc = renderer.getDeferredCache();

		final Fill[] fills = createUpdateFills(renderer, paletteEntry);
		final Fill fillColorP = fills[0];
		final Fill fillColorN = fills[1];

		// Points to render polygon
		CPointContainer points = new CPointContainer();

		CPoint pStart = new CPoint(listP.get(0), kError, bTransposed);
		CPoint nStart = new CPoint(listN.get(0), kError, bTransposed);
		CPoint pEnd, nEnd;
		int i = 1;
		int j = 1;
		// Since the NaN points have been filtered out during Curve generation,
		// no need more null check in loop
		while (i < lengthP && j < lengthN) {
			pEnd = new CPoint(listP.get(i), kError, bTransposed);
			nEnd = new CPoint(listN.get(j), kError, bTransposed);
			final CLine lineP = new CLine(pStart, pEnd);
			final CLine lineN = new CLine(nStart, nEnd);

			if (nStart.compareX(pEnd) >= 0 || lineP.isVertical()) {
				// Use next positive line
				i++;
				pStart = pEnd;

				lineP.start.addToList(points);
				renderLine(lineP, lre, dc, liaP);
				continue;
			} else if (pStart.compareX(nEnd) >= 0 || lineN.isVertical()) {
				// Use next negative line
				j++;
				nStart = nEnd;

				lineN.start.addToListBottom(points);
				renderLine(lineN, lre, dc, liaN);
				continue;
			}

			CPoint intersection = lineP.findIntersection(lineN);

			// If no intersection or the point may be counted in before
			if (intersection == null
			// || intersection.equals( lineP.start )
			// || intersection.equals( lineN.start )
			) {
				// always add negative point in the bottom, positive point
				// in the top
				lineN.start.addToListBottom(points);
				lineP.start.addToList(points);
			} else {
				intersection.addToList(points);
				// pIndex++;
				renderPolygon(points.getPoints(), pre, dc, pStart.compareY(nStart) >= 0 ? fillColorP : fillColorN,
						renderer.getSeries());

				// Start the next polygon
				points.clear();
				intersection.addToList(points);
			}

			i++;
			j++;
			renderLine(lineP, lre, dc, liaP);
			renderLine(lineN, lre, dc, liaN);
			pStart = pEnd;
			nStart = nEnd;
		}

		while (i <= lengthP) {
			pEnd = new CPoint(listP.get(i - 1), kError, bTransposed);
			pEnd.addToList(points);
			renderLine(new CLine(pStart, pEnd), lre, dc, liaP);
			i++;
			pStart = pEnd;
		}
		while (j <= lengthN) {
			nEnd = new CPoint(listN.get(j - 1), kError, bTransposed);
			nEnd.addToListBottom(points);
			renderLine(new CLine(nStart, nEnd), lre, dc, liaN);
			j++;
			nStart = nEnd;
		}

		if (points.size() > 2) {
			// Fix Bugzilla bug 188846.
			if (!locationEquals(loaP, loaN)) {
				renderPolygon(points.getPoints(), pre, dc, pStart.compareY(nStart) >= 0 ? fillColorP : fillColorN,
						renderer.getSeries());
			}
			points.clear();
		}
	}

	/**
	 * Check if specified locations are equal.
	 * 
	 * @param loaP specified location array.
	 * @param loaN specified location array.
	 * @return <code>true</code> if specified location arrays are equal, then
	 *         <code>false</code>.
	 */
	private static boolean locationEquals(Location[] loaP, Location[] loaN) {
		if (loaP.length != loaN.length) {
			return false;
		}

		for (int i = 0; i < loaP.length; i++) {
			if (loaP[i].getX() != loaN[i].getX() || loaP[i].getY() != loaN[i].getY()) {
				return false;
			}
		}

		return true;
	}
}
