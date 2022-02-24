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

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.util.FillUtil;

/**
 * CurveRenderer
 */
public final class CurveRenderer {

	private static final double kError = 0.5d;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	private int iNumberOfPoints = 0;

	private Spline spX = null, spY = null;

	private double[] fa, faX, faY, faZ;

	private final double zeroLocation;

	private final Object oSource;

	private LineAttributes lia;

	private final Location loStart, loEnd;

	private final DeferredCache dc;

	private final ChartWithAxes cwa;

	private final boolean bShowAsTape;

	private final boolean bFillArea;

	private final boolean bUseLastState;

	private final boolean bKeepState;

	private final boolean bTranslucent;

	private final boolean bDeferred;

	private final boolean bConnectMissingValue;

	private final Location[] loa;

	private final Location3D[] loa3d;

	private final Location[] loPoints;

	private Location[] tempPoints;

	private final double dTapeWidth;

	private ColorDefinition fillColor, sideColor, tapeColor;

	private final BaseRenderer iRender;

	private final boolean bRendering3D;

	/**
	 * The constructor.
	 * 
	 * @param _render
	 * @param _lia
	 * @param _faX
	 * @param _faY
	 */
	public CurveRenderer(ChartWithAxes _cwa, BaseRenderer _render, LineAttributes _lia, Location[] _lo,
			boolean _bShowAsTape, double _tapeWidth, boolean _bDeferred, boolean _bKeepState, Fill paletteEntry,
			boolean usePaletteLineColor, boolean connectMissingValue) {
		this(_cwa, _render, _lia, _lo, 0, _bShowAsTape, _tapeWidth, false, false, false, _bDeferred, _bKeepState,
				paletteEntry, usePaletteLineColor, connectMissingValue);
	}

	/**
	 * The constructor.
	 * 
	 * @param _cwa
	 * @param _render
	 * @param _lia
	 * @param _faX
	 * @param _faY
	 * @param _bShowAsTape
	 */
	public CurveRenderer(ChartWithAxes _cwa, BaseRenderer _render, LineAttributes _lia, Location[] _lo,
			double _zeroLocation, boolean _bShowAsTape, double _tapeWidth, boolean _bFillArea, boolean _bTranslucent,
			boolean _bUseLastState, boolean _bDeferred, boolean _bKeepState, Fill paletteEntry,
			boolean usePaletteLineColor, boolean connectMissingValue) {
		cwa = _cwa;
		bRendering3D = _lo instanceof Location3D[];

		loPoints = _lo;
		tempPoints = _lo;

		bConnectMissingValue = connectMissingValue;
		bFillArea = _bFillArea;
		bShowAsTape = _bShowAsTape;
		bDeferred = _bDeferred;
		loa = (bShowAsTape || bFillArea) ? new Location[4] : null;
		loa3d = (bRendering3D) ? new Location3D[4] : null;
		if (_tapeWidth == -1) {
			dTapeWidth = _cwa.getSeriesThickness() * _render.getDeviceScale();
		} else {
			dTapeWidth = _tapeWidth;
		}
		if (loa != null) {
			for (int i = 0; i < 4; i++) {
				loa[i] = goFactory.createLocation(0, 0);
			}
		}
		if (loa3d != null) {
			for (int i = 0; i < 4; i++) {
				loa3d[i] = goFactory.createLocation3D(0, 0, 0);
			}
		}

		lia = _lia;
		zeroLocation = _zeroLocation;
		bTranslucent = _bTranslucent;
		oSource = StructureSource.createSeries(_render.getSeries());

		dc = _render.getDeferredCache();
		if (bShowAsTape) {// for 2d+ area with two curve fitting series drawing issue, we need to
							// add comparator here.
			dc.setPlanesComparator(WrappedInstruction.getDefaultComarator());
		}
		this.iRender = _render;
		loStart = goFactory.createLocation(0, 0);
		loEnd = goFactory.createLocation(0, 0);

		bUseLastState = _bUseLastState;
		bKeepState = _bKeepState;

		if (usePaletteLineColor) {
			lia = goFactory.copyOf(lia);
			lia.setColor(FillUtil.getColor(paletteEntry));
		}

		if (bFillArea) {
			fillColor = FillUtil.getColor(paletteEntry);
			tapeColor = fillColor.brighter();
			sideColor = fillColor.darker();
		} else {
			fillColor = lia.getColor();
			tapeColor = lia.getColor().brighter();
			sideColor = lia.getColor().darker();
		}

		if (bTranslucent) {
			fillColor = fillColor.translucent();
			tapeColor = tapeColor.translucent();
			sideColor = sideColor.translucent();
		}
	}

	/**
	 * 
	 * @param ipr
	 * @throws ChartException
	 */
	public final void draw(IPrimitiveRenderer ipr) throws ChartException {
		if (!bFillArea && !lia.isVisible()) {
			return;
		}

		if (!bConnectMissingValue) {
			for (int i = 0; i < loPoints.length; i++) {
				if (Double.isNaN(loPoints[i].getX()) || Double.isNaN(loPoints[i].getY())) {
					continue;
				}

				List<Location> al = new ArrayList<Location>();
				while ((i < loPoints.length)
						&& !(Double.isNaN(loPoints[i].getX()) || Double.isNaN(loPoints[i].getY()))) {
					al.add(loPoints[i]);
					i += 1;
				}
				i -= 1;

				if (loPoints instanceof Location3D[]) {
					tempPoints = al.toArray(new Location3D[al.size()]);
				} else {
					tempPoints = al.toArray(new Location[al.size()]);
				}
				faX = LocationImpl.getXArray(tempPoints);
				faY = LocationImpl.getYArray(tempPoints);
				if (bRendering3D) {
					faZ = Location3DImpl.getZArray((Location3D[]) tempPoints);
				} else {
					faZ = null;
				}
				iNumberOfPoints = faX.length;

				if (iNumberOfPoints < 1) {
					return;
				} else if (iNumberOfPoints == 1) {
					double iSize = lia.getThickness();

					if (bRendering3D) {
						Line3DRenderEvent lre3dValue = ((EventObjectCache) ipr).getEventObject(oSource,
								Line3DRenderEvent.class);
						Location3D[] loa3dValue = new Location3D[2];
						loa3dValue[0] = goFactory.createLocation3D(faX[0], faY[0], faZ[0]);
						loa3dValue[1] = goFactory.createLocation3D(faX[0], faY[0], faZ[0] - dTapeWidth);
						lre3dValue.setStart3D(loa3dValue[0]);
						lre3dValue.setEnd3D(loa3dValue[1]);
						lre3dValue.setLineAttributes(lia);

						dc.addLine(lre3dValue);
					} else {
						final OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource,
								OvalRenderEvent.class);
						ore.setBounds(goFactory.createBounds(faX[0] - iSize, faY[0] - iSize, 2 * iSize, 2 * iSize));
						ore.setOutline(lia);
						ipr.drawOval(ore);
					}
				} else {
					// X-COORDINATES
					spX = new Spline(faX); // X-SPLINE

					// Y-COORDINATES
					spY = new Spline(faY); // Y-SPLINE

					fa = new double[iNumberOfPoints];
					for (int j = 0; j < iNumberOfPoints; j++) {
						fa[j] = j;
					}

					renderCurve(ipr, 0, 0); // ACTUAL CURVE
				}
			}
		} else {
			// Fix null values
			tempPoints = this.iRender.filterNull(loPoints);
			faX = LocationImpl.getXArray(tempPoints);
			faY = LocationImpl.getYArray(tempPoints);
			if (bRendering3D) {
				faZ = Location3DImpl.getZArray((Location3D[]) tempPoints);
			} else {
				faZ = null;
			}
			iNumberOfPoints = faX.length;

			if (iNumberOfPoints <= 1) {
				return;
			}

			// X-COORDINATES
			spX = new Spline(faX); // X-SPLINE

			// Y-COORDINATES
			spY = new Spline(faY); // Y-SPLINE

			fa = new double[iNumberOfPoints];
			for (int i = 0; i < iNumberOfPoints; i++) {
				fa[i] = i;
			}

			renderCurve(ipr, 0, 0); // ACTUAL CURVE
		}

	}

	/**
	 * 
	 * @param ipr
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @throws ChartException
	 */
	private final void plotPlane(IPrimitiveRenderer ipr, double x1, double y1, double x2, double y2, double z1,
			double z2, boolean drawSide, boolean leftSide) throws ChartException {
		if (bRendering3D) {
			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setOutline(null);
			pre.setDoubleSided(true);
			pre.setBackground(tapeColor);

			if (!leftSide) {
				loa3d[0].set(x1 + kError, y1 + kError, z1);
				loa3d[1].set(x2 + kError, y2 + kError, z2);
				loa3d[2].set(x2 + kError, y2 + kError, z2 - dTapeWidth);
				loa3d[3].set(x1 + kError, y1 + kError, z1 - dTapeWidth);
				pre.setPoints3D(loa3d);

				dc.addPlane(pre, PrimitiveRenderEvent.FILL);
			}

			if (drawSide) {
				pre.setBackground(sideColor);

				if (leftSide) {
					if (y1 + kError > zeroLocation) {
						loa3d[0].set(x1 + kError, y1 + kError, z1);
						loa3d[1].set(x1 + kError, y1 + kError, z1 - dTapeWidth);
						loa3d[2].set(x1 + kError, zeroLocation, z1 - dTapeWidth);
						loa3d[3].set(x1 + kError, zeroLocation, z1);
					} else {
						loa3d[0].set(x1 + kError, y1 + kError, z1);
						loa3d[1].set(x1 + kError, zeroLocation, z1);
						loa3d[2].set(x1 + kError, zeroLocation, z1 - dTapeWidth);
						loa3d[3].set(x1 + kError, y1 + kError, z1 - dTapeWidth);
					}
				} else {
					if (y2 + kError > zeroLocation) {
						loa3d[0].set(x2 + kError, y2 + kError, z2);
						loa3d[1].set(x2 + kError, zeroLocation, z2);
						loa3d[2].set(x2 + kError, zeroLocation, z2 - dTapeWidth);
						loa3d[3].set(x2 + kError, y2 + kError, z2 - dTapeWidth);
					} else {
						loa3d[0].set(x2 + kError, y2 + kError, z2);
						loa3d[1].set(x2 + kError, y2 + kError, z2 - dTapeWidth);
						loa3d[2].set(x2 + kError, zeroLocation, z2 - dTapeWidth);
						loa3d[3].set(x2 + kError, zeroLocation, z2);
					}
				}
				pre.setPoints3D(loa3d);

				dc.addPlane(pre, PrimitiveRenderEvent.FILL);
			}
		} else {
			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setOutline(null);
			pre.setBackground(tapeColor);
			loa[0].set(x1 + kError, y1 + kError);
			loa[1].set(x2 + kError, y2 + kError);
			loa[2].set(x2 + kError + dTapeWidth, y2 + kError - dTapeWidth);
			loa[3].set(x1 + kError + dTapeWidth, y1 + kError - dTapeWidth);
			pre.setPoints(loa);

			if (bDeferred) {
				dc.addPlane(pre, PrimitiveRenderEvent.FILL);
			} else {
				ipr.fillPolygon(pre);
			}

			if (drawSide) {
				pre.setBackground(sideColor);

				if (leftSide) {
					loa[0].set(x1 + kError, y1 + kError);
					loa[1].set(x1 + kError + dTapeWidth, y1 + kError - dTapeWidth);
					if (cwa.isTransposed()) {
						loa[2].set(zeroLocation + dTapeWidth, y1 + kError - dTapeWidth);
						loa[3].set(zeroLocation, y1 + kError);
					} else {
						loa[2].set(x1 + kError + dTapeWidth, zeroLocation - dTapeWidth);
						loa[3].set(x1 + kError, zeroLocation);
					}
				} else {
					// get the last point location of last series
					Object obj = iRender.getRunTimeContext().getState(BaseRenderer.STACKED_SERIES_LOCATION_KEY);
					double[] last = new double[] { zeroLocation, zeroLocation };
					if (obj instanceof List) {
						List lst = (List) obj;
						int index = lst.size() - 1;
						if (index > 0) {
							obj = lst.get(index);
						}
						if (obj instanceof double[]) {
							last = (double[]) obj;
						}
					}
					loa[0].set(x2 + kError, y2 + kError);
					loa[1].set(x2 + kError + dTapeWidth, y2 + kError - dTapeWidth);
					if (cwa.isTransposed()) {
						loa[2].set(last[0] + dTapeWidth, y2 + kError - dTapeWidth);
						loa[3].set(last[0], y2 + kError);
					} else {
						loa[2].set(x2 + kError + dTapeWidth, last[1] - dTapeWidth);
						loa[3].set(x2 + kError, last[1]);
					}
				}
				pre.setPoints(loa);

				if (bDeferred) {
					dc.addPlane(pre, PrimitiveRenderEvent.FILL);
				} else {
					ipr.fillPolygon(pre);
				}
			}
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @throws ChartException
	 */
	private final void plotLine(IPrimitiveRenderer ipr, double x1, double y1, double x2, double y2, double z1,
			double z2) throws ChartException {
		if (bRendering3D) {
			final Line3DRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, Line3DRenderEvent.class);
			lre.setLineAttributes(lia);
			lre.setStart3D(goFactory.createLocation3D(x1 + kError, y1 + kError, z1));
			lre.setEnd3D(goFactory.createLocation3D(x2 + kError, y2 + kError, z2));

			dc.addLine(lre);
		} else {
			final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, LineRenderEvent.class);
			lre.setLineAttributes(lia);
			loStart.set(x1 + kError, y1 + kError);
			loEnd.set(x2 + kError, y2 + kError);
			lre.setStart(loStart);
			lre.setEnd(loEnd);

			if (bDeferred) {
				dc.addLine(lre);
			} else {
				ipr.drawLine(lre);
			}
		}
	}

	/**
	 * 
	 * @param t
	 * @param faXY
	 * @return
	 */
	private final boolean computeSpline(double t, double[] faXY) {
		if (spX == null || spY == null) {
			return false;
		}
		faXY[0] = spX.computeValue(t);
		faXY[1] = spY.computeValue(t);
		return true;
	}

	/**
	 * @param ipr
	 * @param points
	 * @throws ChartException
	 */
	private final void plotArea(IPrimitiveRenderer ipr, List<double[]> points) throws ChartException {
		if (points == null || points.size() < 1) {
			return;
		}

		final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, LineRenderEvent.class);
		lre.setLineAttributes(lia);
		final Line3DRenderEvent lre3d = ((EventObjectCache) ipr).getEventObject(oSource, Line3DRenderEvent.class);
		lre3d.setLineAttributes(lia);

		final PolygonRenderEvent pre = bRendering3D ? null
				: (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
		final Polygon3DRenderEvent pre3d = bRendering3D
				? (Polygon3DRenderEvent) ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class)
				: null;

		if (bUseLastState) {
			Object obj = iRender.getRunTimeContext().getState(BaseRenderer.FIXED_STACKED_SERIES_INDEX_KEY);

			double[] lastFixedX = null;
			double[] lastFixedY = null;

			int lastVisbileSeriesIndex = iRender.getSeriesIndex() - 1;
			// TODO this is just a temporary fix for stacked area rendering
			if (iRender instanceof AxesRenderer) {
				lastVisbileSeriesIndex = ((AxesRenderer) iRender)
						.getPrevVisibleSiblingSeriesIndex(iRender.getSeriesIndex());
			}

			if (obj instanceof Integer && ((Integer) obj).intValue() == lastVisbileSeriesIndex) {
				// only search nearest previous values.
				obj = iRender.getRunTimeContext().getState(BaseRenderer.FIXED_STACKED_SERIES_LOCATION_KEY);

				if (obj instanceof List && ((List) obj).size() > 0) {
					List lst = (List) obj;

					for (int i = 0; i < lst.size(); i++) {
						Object o = lst.get(i);

						if (o instanceof double[]) {
							if (lastFixedX == null) {
								lastFixedX = new double[lst.size()];
								lastFixedY = new double[lastFixedX.length];
							}

							lastFixedX[i] = ((double[]) o)[0];
							lastFixedY[i] = ((double[]) o)[1];
						} else {
							lastFixedX = null;
							lastFixedY = null;
							break;
						}
					}
				}
			}

			obj = iRender.getRunTimeContext().getState(BaseRenderer.STACKED_SERIES_LOCATION_KEY);
			double[] lastX = null;
			double[] lastY = null;

			if (obj instanceof List && ((List) obj).size() > 0) {
				List lst = (List) obj;

				for (int i = 0; i < lst.size(); i++) {
					Object o = lst.get(i);

					if (o instanceof double[]) {
						if (lastX == null) {
							lastX = new double[lst.size()];
							lastY = new double[lastX.length];
						}

						lastX[i] = ((double[]) o)[0];
						lastY[i] = ((double[]) o)[1];
					} else {
						lastX = null;
						lastY = null;
						break;
					}
				}
			}

			if (lastX != null) {

				List<Location> lst = new ArrayList<Location>();

				for (int i = 0; i < points.size(); i++) {
					double[] pt = points.get(i);
					lst.add(goFactory.createLocation(pt[0], pt[1]));
				}

				if (lastFixedX != null) {
					for (int i = lastFixedX.length - 1; i >= 0; i--) {
						lst.add(goFactory.createLocation(lastFixedX[i], lastFixedY[i]));
					}
				} else {
					for (int i = lastX.length - 1; i >= 0; i--) {
						lst.add(goFactory.createLocation(lastX[i], lastY[i]));
					}
				}

				Location[] pa = lst.toArray(new Location[lst.size()]);

				pre.setOutline(null);
				pre.setPoints(pa);
				pre.setBackground(fillColor);

				if (bDeferred) {
					dc.addPlane(pre, PrimitiveRenderEvent.FILL);
				} else {
					ipr.fillPolygon(pre);
				}

				if (lia.isVisible()) {
					for (int i = 0; i < points.size() - 1; i++) {
						lre.setStart(pa[i]);
						lre.setEnd(pa[i + 1]);
						if (bDeferred) {
							dc.addLine(lre);
						} else {
							ipr.drawLine(lre);
						}
					}
				}

				return;
			}
		}

		if (bRendering3D) {
			Location3D[] pa = new Location3D[points.size() + 2];

			double[] pt0 = points.get(0);

			if (pt0[1] > zeroLocation) {
				for (int i = 1; i < points.size(); i++) {
					double[] pt = points.get(i);
					pa[pa.length - i] = goFactory.createLocation3D(pt[0], pt[1], pt[2]);
				}
				pa[0] = goFactory.createLocation3D(pt0[0], pt0[1], pt0[2]);
				pa[1] = goFactory.createLocation3D(pt0[0], zeroLocation, pt0[2]);
				pa[2] = goFactory.createLocation3D(pa[3].getX(), zeroLocation, pa[3].getZ());
			} else {
				for (int i = 0; i < points.size(); i++) {
					double[] pt = points.get(i);
					pa[i + 2] = goFactory.createLocation3D(pt[0], pt[1], pt[2]);
				}
				pa[0] = goFactory.createLocation3D(pa[pa.length - 1].getX(), zeroLocation, pa[pa.length - 1].getZ());
				pa[1] = goFactory.createLocation3D(pt0[0], zeroLocation, pt0[2]);
			}

			pre3d.setOutline(null);
			pre3d.setPoints3D(pa);
			pre3d.setBackground(fillColor);
			dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);

			for (int i = 0; i < pa.length; i++) {
				pa[i].setZ(pa[i].getZ() - dTapeWidth);
			}
			dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);

			double[] pte = points.get(points.size() - 1);
			loa3d[0].set(pt0[0], zeroLocation, pt0[2]);
			loa3d[1].set(pt0[0], zeroLocation, pt0[2] - dTapeWidth);
			loa3d[2].set(pte[0], zeroLocation, pte[2] - dTapeWidth);
			loa3d[3].set(pte[0], zeroLocation, pte[2]);
			pre3d.setPoints3D(loa3d);
			dc.addPlane(pre3d, PrimitiveRenderEvent.FILL);

			if (lia.isVisible() && points.size() > 1) {
				double[] ptp = points.get(0);

				for (int i = 1; i < points.size(); i++) {
					double[] pta = points.get(i);
					lre3d.setStart3D(ptp[0], ptp[1], ptp[2]);
					lre3d.setEnd3D(pta[0], pta[1], pta[2]);
					ptp = pta;

					dc.addLine(lre3d);
				}
			}

		} else {
			Location[] pa = new Location[points.size() + 2];

			for (int i = 0; i < points.size(); i++) {
				double[] pt = points.get(i);
				pa[i] = goFactory.createLocation(pt[0], pt[1]);
			}

			if (cwa.isTransposed()) {
				pa[pa.length - 2] = goFactory.createLocation(zeroLocation, pa[pa.length - 3].getY());
				pa[pa.length - 1] = goFactory.createLocation(zeroLocation, pa[0].getY());
			} else {
				pa[pa.length - 2] = goFactory.createLocation(pa[pa.length - 3].getX(), zeroLocation);
				pa[pa.length - 1] = goFactory.createLocation(pa[0].getX(), zeroLocation);
			}

			pre.setOutline(null);
			pre.setPoints(pa);
			pre.setBackground(fillColor);

			if (bDeferred) {
				dc.addPlane(pre, PrimitiveRenderEvent.FILL);
			} else {
				ipr.fillPolygon(pre);
			}

			if (lia.isVisible()) {
				for (int i = 0; i < points.size() - 1; i++) {
					lre.setStart(pa[i]);
					lre.setEnd(pa[i + 1]);
					if (bDeferred) {
						dc.addLine(lre);
					} else {
						ipr.drawLine(lre);
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param ipr
	 * @param fXOffset
	 * @param fYOffset
	 * @throws ChartException
	 */
	private final void renderCurve(IPrimitiveRenderer ipr, double fXOffset, double fYOffset) throws ChartException {
		final double[] faKnotXY1 = new double[2];
		final double[] faKnotXY2 = new double[2];
		if (!computeSpline(fa[0], faKnotXY1)) {
			return;
		}

		int iNumberOfDivisions;
		double fX, fY;
		double[] faXY1, faXY2;
		double fT;

		final List<double[]> stateList = new ArrayList<double[]>();

		for (int i = 0; i < iNumberOfPoints - 1; i++) {
			if (!computeSpline(fa[i + 1], faKnotXY2)) {
				continue;
			}
			fX = faKnotXY2[0] - faKnotXY1[0];
			fY = faKnotXY2[1] - faKnotXY1[1];
			iNumberOfDivisions = (int) (Math.sqrt(fX * fX + fY * fY) / 5.0f) + 1;

			faXY1 = new double[2];
			faXY2 = new double[2];
			if (!computeSpline(fa[i], faXY1)) {
				continue;
			}

			for (int j = 0; j < iNumberOfDivisions; j++) {
				fT = fa[i] + (fa[i + 1] - fa[i]) * (j + 1) / iNumberOfDivisions;
				if (!computeSpline(fT, faXY2)) {
					continue;
				}
				if (bShowAsTape) {
					boolean drawLeftSide = (i == 0) && (j == 0)
					// && bKeepState
							&& bRendering3D && bFillArea;

					if (drawLeftSide) {
						plotPlane(ipr, faXY1[0] + fXOffset, faXY1[1] + fYOffset, faXY2[0] + fXOffset,
								faXY2[1] + fYOffset, bRendering3D ? faZ[i] : 0, bRendering3D ? faZ[i] : 0, true, true);
					}

					// TODO user a single surface to draw the tape.
					boolean drawRightSide = bFillArea
							&& ((!iRender.isRightToLeft() && i == iNumberOfPoints - 2 && j == iNumberOfDivisions - 1)
									|| (iRender.isRightToLeft() && i == 0 && j == 0));

					plotPlane(ipr, faXY1[0] + fXOffset, faXY1[1] + fYOffset, faXY2[0] + fXOffset, faXY2[1] + fYOffset,
							bRendering3D ? faZ[i] : 0, bRendering3D ? faZ[i] : 0, drawRightSide,
							iRender.isRightToLeft());
				}

				if (!bFillArea) {
					// if fill area, defer to the loop end.
					plotLine(ipr, faXY1[0] + fXOffset, faXY1[1] + fYOffset, faXY2[0] + fXOffset, faXY2[1] + fYOffset,
							bRendering3D ? faZ[i] : 0, bRendering3D ? faZ[i] : 0);
				}

				// TODO remove the duplicate points.
				if (bRendering3D) {
					stateList.add(new double[] { faXY1[0] + fXOffset, faXY1[1] + fYOffset, faZ[i] });
					stateList.add(new double[] { faXY2[0] + fXOffset, faXY2[1] + fYOffset, faZ[i] });
				} else {
					stateList.add(new double[] { faXY1[0] + fXOffset, faXY1[1] + fYOffset });
					stateList.add(new double[] { faXY2[0] + fXOffset, faXY2[1] + fYOffset });
				}

				faXY1[0] = faXY2[0];
				faXY1[1] = faXY2[1];
			}

			faKnotXY1[0] = faKnotXY2[0];
			faKnotXY1[1] = faKnotXY2[1];
		}

		if (bFillArea) {
			plotArea(ipr, stateList);
		}

		if (bKeepState) {
			if (iRender instanceof AxesRenderer && ((AxesRenderer) iRender).isLastRuntimeSeriesInAxis()) {
				// clean up last state.
				iRender.getRunTimeContext().putState(BaseRenderer.STACKED_SERIES_LOCATION_KEY, null);
			} else {
				iRender.getRunTimeContext().putState(BaseRenderer.STACKED_SERIES_LOCATION_KEY, stateList);
			}
		}
	}

	/**
	 * Spline
	 */
	private static class Spline {

		private final int iNumberOfPoints;

		private final double[] fa;

		private final double[] faA;

		private final double[] faB;

		private final double[] faC;

		/**
		 * 
		 * @param _fa
		 */
		public Spline(double[] _fa) {
			iNumberOfPoints = _fa.length;
			fa = new double[iNumberOfPoints];

			faA = new double[iNumberOfPoints - 1];
			faB = new double[iNumberOfPoints - 1];
			faC = new double[iNumberOfPoints - 1];

			for (int i = 0; i < iNumberOfPoints; i++) {
				fa[i] = _fa[i];
			}
			computeCoefficients();
		}

		private final void computeCoefficients() {
			double p, dy1, dy2;
			dy1 = fa[1] - fa[0];
			for (int i = 1; i < iNumberOfPoints - 1; i++) {
				dy2 = fa[i + 1] - fa[i];
				faC[i] = 0.5f;
				faB[i] = 1.0f - faC[i];
				faA[i] = 3.0f * (dy2 - dy1);
				dy1 = dy2;
			}

			faC[0] = 0.0f;
			faB[0] = 0.0f;
			faA[0] = 0.0f;

			for (int i = 1; i < iNumberOfPoints - 1; i++) {
				p = faB[i] * faC[i - 1] + 2.0f;
				faC[i] = -faC[i] / p;
				faB[i] = (faA[i] - faB[i] * faB[i - 1]) / p;
			}

			dy1 = 0;
			for (int i = iNumberOfPoints - 2; i >= 0; i--) {
				dy2 = faC[i] * dy1 + faB[i];
				faA[i] = (dy1 - dy2) / 6.0f;
				faB[i] = dy2 / 2.0f;
				faC[i] = (fa[i + 1] - fa[i]) - 1 * (faB[i] + faA[i]);
				dy1 = dy2;
			}
		}

		/**
		 * 
		 * @param x
		 * @return
		 */
		private final double computeValue(double x) {
			if (iNumberOfPoints < 2) {
				return 0.0f;
			}

			int i = 0, iMiddle;
			int iRight = iNumberOfPoints - 1;

			while (i + 1 < iRight) {
				iMiddle = (i + iRight) / 2;
				if (iMiddle <= x) {
					i = iMiddle;
				} else {
					iRight = iMiddle;
				}
			}
			final double t = (x - i);
			return faA[i] * t * t * t + faB[i] * t * t + faC[i] * t + fa[i];
		}
	}

	private static boolean computeSpline(double t, double[] faXY, Spline spx, Spline spy) {
		if (spx == null || spy == null) {
			return false;
		}
		faXY[0] = spx.computeValue(t);
		faXY[1] = spy.computeValue(t);
		return true;
	}

	/**
	 * Generates the points of curve line.
	 * 
	 * @param loPoints
	 * @param connectMissingValue
	 * @param fXOffset
	 * @param fYOffset
	 * @return points list in the form of double array
	 */
	public static List<double[]> generateCurvePoints(BaseRenderer _render, Location[] loPoints,
			boolean connectMissingValue, double fXOffset, double fYOffset) {
		final double[] faKnotXY1 = new double[2];
		final double[] faKnotXY2 = new double[2];

		int iNumberOfPoints = 0;
		Location[] tempPoints;
		double[] fa = null, faX, faY;
		Spline spX = null, spY = null;

		if (!connectMissingValue) {
			for (int i = 0; i < loPoints.length; i++) {
				if (Double.isNaN(loPoints[i].getX()) || Double.isNaN(loPoints[i].getY())) {
					continue;
				}

				List<Location> al = new ArrayList<Location>();
				while ((i < loPoints.length)
						&& !(Double.isNaN(loPoints[i].getX()) || Double.isNaN(loPoints[i].getY()))) {
					al.add(loPoints[i]);
					i += 1;
				}
				i -= 1;

				// if ( loPoints instanceof Location3D[] )
				// {
				// tempPoints = (Location3D[]) al.toArray( new
				// Location3D[al.size( )] );
				// }
				// else
				{
					tempPoints = al.toArray(new Location[al.size()]);
				}
				faX = LocationImpl.getXArray(tempPoints);
				faY = LocationImpl.getYArray(tempPoints);

				iNumberOfPoints = faX.length;

				if (iNumberOfPoints > 1) {
					// X-CORDINATES
					spX = new Spline(faX); // X-SPLINE

					// Y-CORDINATES
					spY = new Spline(faY); // Y-SPLINE

					fa = new double[iNumberOfPoints];
					for (int j = 0; j < iNumberOfPoints; j++) {
						fa[j] = j;
					}
				} else {
					return null;
				}
			}
		} else {
			// Fix null values
			tempPoints = _render.filterNull(loPoints);
			faX = LocationImpl.getXArray(tempPoints);
			faY = LocationImpl.getYArray(tempPoints);

			iNumberOfPoints = faX.length;

			if (iNumberOfPoints <= 1) {
				return null;
			}

			// X-CORDINATES
			spX = new Spline(faX); // X-SPLINE

			// Y-CORDINATES
			spY = new Spline(faY); // Y-SPLINE

			fa = new double[iNumberOfPoints];
			for (int i = 0; i < iNumberOfPoints; i++) {
				fa[i] = i;
			}
		}

		if (!computeSpline(fa[0], faKnotXY1, spX, spY)) {
			return null;
		}

		int iNumberOfDivisions;
		double fX, fY;
		double[] faXY1, faXY2;
		double fT;

		final List<double[]> stateList = new ArrayList<double[]>();

		for (int i = 0; i < iNumberOfPoints - 1; i++) {
			if (!computeSpline(fa[i + 1], faKnotXY2, spX, spY)) {
				continue;
			}
			fX = faKnotXY2[0] - faKnotXY1[0];
			fY = faKnotXY2[1] - faKnotXY1[1];
			iNumberOfDivisions = (int) (Math.sqrt(fX * fX + fY * fY) / 5.0f) + 1;

			faXY1 = new double[2];
			faXY2 = new double[2];
			if (!computeSpline(fa[i], faXY1, spX, spY)) {
				continue;
			}

			for (int j = 0; j < iNumberOfDivisions; j++) {
				fT = fa[i] + (fa[i + 1] - fa[i]) * (j + 1) / iNumberOfDivisions;
				if (!computeSpline(fT, faXY2, spX, spY)) {
					continue;
				}

				// TODO remove the duplicate points.
				// if ( bRendering3D )
				// {
				// stateList.add( new double[]{
				// faXY1[0] + fXOffset, faXY1[1] + fYOffset, faZ[i]
				// } );
				// stateList.add( new double[]{
				// faXY2[0] + fXOffset, faXY2[1] + fYOffset, faZ[i]
				// } );
				// }
				// else
				{
					stateList.add(new double[] { faXY1[0] + fXOffset, faXY1[1] + fYOffset });
					stateList.add(new double[] { faXY2[0] + fXOffset, faXY2[1] + fYOffset });
				}

				faXY1[0] = faXY2[0];
				faXY1[1] = faXY2[1];
			}

			faKnotXY1[0] = faKnotXY2[0];
			faKnotXY1[1] = faKnotXY2[1];
		}

		return stateList;
	}
}
