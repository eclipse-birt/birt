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

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.Arc3DRenderEvent;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.Area3DRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Image3DRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Oval3DRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;

/**
 * This class implements marker rendering capability used in Line, Area or other
 * series.
 */
public final class MarkerRenderer {

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	private final IDeviceRenderer iRender;

	private final DeferredCache dc;

	private Fill paletteEntry;

	private LineAttributes la;

	private Marker m;

	private final boolean bDeferred;

	private double iSize;

	private int zOrder;

	private final Object oSource;

	private final boolean bRendering3D;

	private final boolean bTransposed;

	private Location lo;

	private Location3D lo3d;

	private PrimitiveRenderEvent preCopy;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param _render
	 * @param _oSource
	 * @param _lo
	 * @param _la
	 * @param _paletteEntry
	 * @param _m
	 * @param _markerSize   Null means auto size
	 * @param _dc
	 * @param _bDeferred
	 * @param _bTransposed
	 */
	public MarkerRenderer(IDeviceRenderer _render, Object _oSource, Location _lo, LineAttributes _la,
			Fill _paletteEntry, Marker _m, Integer _markerSize, DeferredCache _dc, boolean _bDeferred,
			boolean _bTransposed, int _zOrder) {
		this.iRender = _render;
		la = goFactory.copyOf(_la);
		dc = _dc;
		bDeferred = _bDeferred;
		oSource = _oSource;
		m = _m;
		bTransposed = _bTransposed;
		zOrder = _zOrder;

		// Update visible state according to the outline value.
		if (_m.getOutline() != null) {
			la.setVisible(_m.getOutline().isVisible());
		}

		iSize = _markerSize == null ? _m.getSize() : _markerSize.intValue();
		paletteEntry = FillUtil.convertFill(_paletteEntry, iSize, goFactory.TRANSPARENT());
		if (paletteEntry instanceof ColorDefinition && ChartUtil.isColorTransparent((ColorDefinition) paletteEntry)
				&& (!_la.isVisible() || ChartUtil.isColorTransparent(_la.getColor()))) {
			// Avoid marker is invisible if all colors are transparent
			paletteEntry = goFactory.createColorDefinition(0, 0, 0, 15);
		}
		iSize = Math.abs(iSize) * iRender.getDisplayServer().getDpiResolution() / 72d;

		bRendering3D = _lo instanceof Location3D;
		if (bRendering3D) {
			lo3d = (Location3D) _lo;
		} else {
			lo = _lo;
		}
	}

	public MarkerRenderer(IDeviceRenderer _render, Object _oSource, Location _lo, LineAttributes _la,
			Fill _paletteEntry, Marker _m, Integer _markerSize, DeferredCache _dc, boolean _bDeferred,
			boolean _bTransposed) {
		this(_render, _oSource, _lo, _la, _paletteEntry, _m, _markerSize, _dc, _bDeferred, _bTransposed, 1);
	}

	/**
	 * Render the marker with given renderer.
	 * 
	 * @param ipr
	 * @throws ChartException
	 */
	public final void draw(IPrimitiveRenderer ipr) throws ChartException {
		switch (m.getType().getValue()) {
		case MarkerType.CROSSHAIR:
			drawCrosshair(ipr);
			break;
		case MarkerType.TRIANGLE:
			drawTriangle(ipr);
			break;
		case MarkerType.BOX:
			drawBox(ipr);
			break;
		case MarkerType.CIRCLE:
			drawCircle(ipr);
			break;
		case MarkerType.ICON:
			drawIcon(ipr);
			break;
		case MarkerType.NABLA:
			drawNabla(ipr);
			break;
		case MarkerType.DIAMOND:
			drawDiamond(ipr);
			break;
		case MarkerType.FOUR_DIAMONDS:
			drawFourDiamonds(ipr);
			break;
		case MarkerType.ELLIPSE:
			drawEllipse(ipr);
			break;
		case MarkerType.SEMI_CIRCLE:
			drawSemiCircle(ipr);
			break;
		case MarkerType.HEXAGON:
			drawHexagon(ipr);
			break;
		case MarkerType.RECTANGLE:
			drawRectangle(ipr);
			break;
		case MarkerType.STAR:
			drawStar(ipr);
			break;
		case MarkerType.COLUMN:
			drawColumn(ipr);
			break;
		case MarkerType.CROSS:
			drawCross(ipr);
			break;
		}
	}

	private void drawCrosshair(IPrimitiveRenderer ipr) throws ChartException {
		paletteEntry = FillUtil.getColor(paletteEntry);
		if (ChartUtil.isColorTransparent((ColorDefinition) paletteEntry)) {
			// Avoid marker is invisible
			paletteEntry = goFactory.createColorDefinition(0, 0, 0, 15);
		}
		final ColorDefinition cd = (ColorDefinition) paletteEntry;
		final LineAttributes lia = goFactory.createLineAttributes(goFactory.copyOf(cd), LineStyle.SOLID_LITERAL,
				la.getThickness());

		final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(oSource, LineRenderEvent.class);
		final Line3DRenderEvent lre3d = bRendering3D
				? (Line3DRenderEvent) ((EventObjectCache) ipr).getEventObject(oSource, Line3DRenderEvent.class)
				: null;

		if (bRendering3D) {
			lre3d.setLineAttributes(lia);
			lre3d.setStart3D(goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - iSize, lo3d.getZ()));
			lre3d.setEnd3D(goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + iSize, lo3d.getZ()));
		} else {
			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(lo.getX(), lo.getY() - iSize));
			lre.setEnd(goFactory.createLocation(lo.getX(), lo.getY() + iSize));
		}

		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize - 1, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize - 1, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize - 1, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize - 1, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			preCopy = pre;
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + iSize);
			loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + iSize);
			loa[2] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - iSize);
			loa[3] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - iSize);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			preCopy = pre;
		}

		if (bRendering3D) {
			dc.addLine(lre3d);

			lre3d.setLineAttributes(lia);
			lre3d.setStart3D(goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY(), lo3d.getZ()));
			lre3d.setEnd3D(goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY(), lo3d.getZ()));

			dc.addLine(lre3d);
		} else {
			if (bDeferred) {
				dc.addMarker(lre, PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.drawLine(lre);
			}

			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(lo.getX() - iSize, lo.getY()));
			lre.setEnd(goFactory.createLocation(lo.getX() + iSize, lo.getY()));

			if (bDeferred) {
				dc.addMarker(lre, PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.drawLine(lre);
			}
		}
	}

	private void drawTriangle(IPrimitiveRenderer ipr) throws ChartException {
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[3];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize - 1, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize - 1, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + iSize - 1, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			pre.setDoubleSided(true);

			preCopy = pre;
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[3];
			if (bTransposed) {
				loa[0] = goFactory.createLocation(lo.getX() - iSize - 1, lo.getY() - iSize);
				loa[1] = goFactory.createLocation(lo.getX() - iSize - 1, lo.getY() + iSize);
				loa[2] = goFactory.createLocation(lo.getX() + iSize - 1, lo.getY());
			} else {
				loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + iSize - 1);
				loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + iSize - 1);
				loa[2] = goFactory.createLocation(lo.getX(), lo.getY() - iSize - 1);
			}

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre;

			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawBox(IPrimitiveRenderer ipr) throws ChartException {
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + iSize);
			loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + iSize);
			loa[2] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - iSize);
			loa[3] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - iSize);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawCircle(IPrimitiveRenderer ipr) throws ChartException {
		if (bRendering3D) {
			final Oval3DRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, Oval3DRenderEvent.class);
			ore.setBackground(paletteEntry);
			ore.setOutline(la);
			ore.setLocation3D(new Location3D[] {
					goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize, lo3d.getZ()) });

			preCopy = ore.copy();
			dc.addPlane(ore, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, OvalRenderEvent.class);
			ore.setBackground(paletteEntry);
			ore.setBounds(goFactory.createBounds(lo.getX() - iSize, lo.getY() - iSize, iSize * 2, iSize * 2));
			ore.setOutline(la);
			preCopy = ore.copy();

			if (bDeferred) {
				dc.addMarker(ore, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillOval(ore);
				ipr.drawOval(ore);
			}

		}
	}

	private void drawIcon(IPrimitiveRenderer ipr) throws ChartException {
		Fill fil = m.getFill();

		if (!(fil instanceof Image)) {
			logger.log(ILogger.WARNING, Messages.getString("exception.illegal.icon.palette.type.for.marker", //$NON-NLS-1$
					new Object[] { fil }, iRender.getULocale()));
			return;
		}
		Image icon = (Image) fil;

		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			preCopy = pre.copy();

			final Image3DRenderEvent ire = ((EventObjectCache) ipr).getEventObject(oSource, Image3DRenderEvent.class);
			ire.setLocation3D(lo3d);
			ire.setWidth((int) (2 * iSize + .5));
			ire.setHeight((int) (2 * iSize + .5));
			ire.setImage(icon);

			dc.addPlane(ire, PrimitiveRenderEvent.FILL);
		} else {
			final ImageRenderEvent ire = ((EventObjectCache) ipr).getEventObject(oSource, ImageRenderEvent.class);
			ire.setLocation(lo);
			ire.setWidth((int) (2 * iSize + .5));
			ire.setHeight((int) (2 * iSize + .5));
			ire.setImage(icon);

			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + iSize);
			loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + iSize);
			loa[2] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - iSize);
			loa[3] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - iSize);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			if (bDeferred) {
				dc.addMarker(ire, PrimitiveRenderEvent.FILL, iSize, zOrder);
			} else {
				ipr.drawImage(ire);
			}
		}
	}

	private void drawNabla(IPrimitiveRenderer ipr) throws ChartException {
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[3];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize - 1, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize - 1, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - iSize - 1, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			pre.setDoubleSided(true);
			preCopy = pre.copy();

			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[3];
			if (bTransposed) {
				loa[0] = goFactory.createLocation(lo.getX() + iSize - 1, lo.getY() - iSize);
				loa[1] = goFactory.createLocation(lo.getX() + iSize - 1, lo.getY() + iSize);
				loa[2] = goFactory.createLocation(lo.getX() - iSize - 1, lo.getY());
			} else {
				loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - iSize + 1);
				loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - iSize + 1);
				loa[2] = goFactory.createLocation(lo.getX(), lo.getY() + iSize + 1);
			}

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawDiamond(IPrimitiveRenderer ipr) throws ChartException {
		double offset = Math.floor(iSize);

		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY(), lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + offset, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY(), lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - offset, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - offset, lo.getY());
			loa[1] = goFactory.createLocation(lo.getX(), lo.getY() + offset);
			loa[2] = goFactory.createLocation(lo.getX() + offset, lo.getY());
			loa[3] = goFactory.createLocation(lo.getX(), lo.getY() - offset);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawFourDiamonds(IPrimitiveRenderer ipr) throws ChartException {
		double offset = Math.floor(0.5 * iSize) + 1;
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - 2 * offset, lo3d.getY(), lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + 2 * offset, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + 2 * offset, lo3d.getY(), lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - 2 * offset, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			preCopy = pre.copy();

			final Location3D[] loa1 = new Location3D[4];
			loa1[0] = goFactory.createLocation3D(lo3d.getX() - offset + 1, lo3d.getY() - offset, lo3d.getZ());
			loa1[1] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - 1, lo3d.getZ());
			loa1[2] = goFactory.createLocation3D(lo3d.getX() + offset - 1, lo3d.getY() - offset, lo3d.getZ());
			loa1[3] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - 2 * offset + 1, lo3d.getZ());

			final Polygon3DRenderEvent pre1 = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre1.setPoints3D(loa1);
			pre1.setDoubleSided(true);
			pre1.setBackground(paletteEntry);
			pre1.setOutline(la);

			final Location3D[] loa2 = new Location3D[4];
			loa2[0] = goFactory.createLocation3D(lo3d.getX() + 1, lo3d.getY(), lo3d.getZ());
			loa2[1] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() + offset - 1, lo3d.getZ());
			loa2[2] = goFactory.createLocation3D(lo3d.getX() + 2 * offset - 1, lo3d.getY(), lo3d.getZ());
			loa2[3] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() - offset + 1, lo3d.getZ());

			final Location3D[] loa3 = new Location3D[4];
			loa3[0] = goFactory.createLocation3D(lo3d.getX() - offset + 1, lo3d.getY() + offset, lo3d.getZ());
			loa3[1] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + 2 * offset - 1, lo3d.getZ());
			loa3[2] = goFactory.createLocation3D(lo3d.getX() + offset - 1, lo3d.getY() + offset, lo3d.getZ());
			loa3[3] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + 1, lo3d.getZ());

			final Location3D[] loa4 = new Location3D[4];
			loa4[0] = goFactory.createLocation3D(lo3d.getX() - 2 * offset + 1, lo3d.getY(), lo3d.getZ());
			loa4[1] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() + offset - 1, lo3d.getZ());
			loa4[2] = goFactory.createLocation3D(lo3d.getX() - 1, lo3d.getY(), lo3d.getZ());
			loa4[3] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() - offset + 1, lo3d.getZ());

			dc.addPlane(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
			pre1.setPoints3D(loa2);
			dc.addPlane(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
			pre1.setPoints3D(loa3);
			dc.addPlane(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
			pre1.setPoints3D(loa4);
			dc.addPlane(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - 2 * offset, lo.getY());
			loa[1] = goFactory.createLocation(lo.getX(), lo.getY() + 2 * offset);
			loa[2] = goFactory.createLocation(lo.getX() + 2 * offset, lo.getY());
			loa[3] = goFactory.createLocation(lo.getX(), lo.getY() - 2 * offset);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			preCopy = pre.copy();

			final Location[] loa1 = new Location[4];
			loa1[0] = goFactory.createLocation(lo.getX() - offset + 1, lo.getY() - offset);
			loa1[1] = goFactory.createLocation(lo.getX(), lo.getY() - 1);
			loa1[2] = goFactory.createLocation(lo.getX() + offset - 1, lo.getY() - offset);
			loa1[3] = goFactory.createLocation(lo.getX(), lo.getY() - 2 * offset + 1);

			final PolygonRenderEvent pre1 = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre1.setPoints(loa1);
			pre1.setBackground(paletteEntry);
			pre1.setOutline(la);

			final Location[] loa2 = new Location[4];
			loa2[0] = goFactory.createLocation(lo.getX() + 1, lo.getY());
			loa2[1] = goFactory.createLocation(lo.getX() + offset, lo.getY() + offset - 1);
			loa2[2] = goFactory.createLocation(lo.getX() + 2 * offset - 1, lo.getY());
			loa2[3] = goFactory.createLocation(lo.getX() + offset, lo.getY() - offset + 1);

			final Location[] loa3 = new Location[4];
			loa3[0] = goFactory.createLocation(lo.getX() - offset + 1, lo.getY() + offset);
			loa3[1] = goFactory.createLocation(lo.getX(), lo.getY() + 2 * offset - 1);
			loa3[2] = goFactory.createLocation(lo.getX() + offset - 1, lo.getY() + offset);
			loa3[3] = goFactory.createLocation(lo.getX(), lo.getY() + 1);

			final Location[] loa4 = new Location[4];
			loa4[0] = goFactory.createLocation(lo.getX() - 2 * offset + 1, lo.getY());
			loa4[1] = goFactory.createLocation(lo.getX() - offset, lo.getY() + offset - 1);
			loa4[2] = goFactory.createLocation(lo.getX() - 1, lo.getY());
			loa4[3] = goFactory.createLocation(lo.getX() - offset, lo.getY() - offset + 1);

			if (bDeferred) {
				dc.addMarker(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
				pre1.setPoints(loa2);
				dc.addMarker(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
				pre1.setPoints(loa3);
				dc.addMarker(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
				pre1.setPoints(loa4);
				dc.addMarker(pre1, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);

			} else {
				ipr.fillPolygon(pre1);
				ipr.drawPolygon(pre1);

				pre1.setPoints(loa2);
				ipr.fillPolygon(pre1);
				ipr.drawPolygon(pre1);

				pre1.setPoints(loa3);
				ipr.fillPolygon(pre1);
				ipr.drawPolygon(pre1);

				pre1.setPoints(loa4);
				ipr.fillPolygon(pre1);
				ipr.drawPolygon(pre1);
			}
		}
	}

	private void drawCross(IPrimitiveRenderer ipr) throws ChartException {
		double offset = Math.floor(0.5 * iSize);
		if (bRendering3D) {
			final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			final Location3D[] loa = new Location3D[12];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + offset, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY(), lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - offset, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() - iSize, lo3d.getZ());
			loa[4] = goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - offset, lo3d.getZ());
			loa[5] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() - iSize, lo3d.getZ());
			loa[6] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - offset, lo3d.getZ());
			loa[7] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY(), lo3d.getZ());
			loa[8] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + offset, lo3d.getZ());
			loa[9] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() + iSize, lo3d.getZ());
			loa[10] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY(), lo3d.getZ());
			loa[11] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() + iSize, lo3d.getZ());
			pre3d.setPoints3D(loa);
			pre3d.setDoubleSided(true);
			preCopy = pre3d.copy();

			pre3d.setBackground(paletteEntry);
			pre3d.setOutline(la);
			dc.addPlane(pre3d, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			final Location[] loa = new Location[12];
			loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + offset);
			loa[1] = goFactory.createLocation(lo.getX() - offset, lo.getY());
			loa[2] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - offset);
			loa[3] = goFactory.createLocation(lo.getX() - offset, lo.getY() - iSize);
			loa[4] = goFactory.createLocation(lo.getX(), lo.getY() - offset);
			loa[5] = goFactory.createLocation(lo.getX() + offset, lo.getY() - iSize);
			loa[6] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - offset);
			loa[7] = goFactory.createLocation(lo.getX() + offset, lo.getY());
			loa[8] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + offset);
			loa[9] = goFactory.createLocation(lo.getX() + offset, lo.getY() + iSize);
			loa[10] = goFactory.createLocation(lo.getX(), lo.getY() + offset);
			loa[11] = goFactory.createLocation(lo.getX() - offset, lo.getY() + iSize);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);
			preCopy = pre.copy();

			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawEllipse(IPrimitiveRenderer ipr) throws ChartException {
		if (bRendering3D) {
			final Oval3DRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, Oval3DRenderEvent.class);
			ore.setBackground(paletteEntry);
			ore.setOutline(la);
			ore.setLocation3D(new Location3D[] {
					goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize / 2, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize / 2, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize / 2, lo3d.getZ()),
					goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize / 2, lo3d.getZ()) });

			preCopy = ore.copy();
			dc.addPlane(ore, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, OvalRenderEvent.class);
			ore.setBackground(paletteEntry);
			ore.setBounds(goFactory.createBounds(lo.getX() - iSize, lo.getY() - iSize / 2, iSize * 2, iSize));
			ore.setOutline(la);
			preCopy = ore.copy();

			if (bDeferred) {
				dc.addMarker(ore, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillOval(ore);
				ipr.drawOval(ore);
			}

		}
	}

	private void drawSemiCircle(IPrimitiveRenderer ipr) throws ChartException {
		double offset = 0.5 * iSize;
		if (bRendering3D) {
			final Arc3DRenderEvent are3d = ((EventObjectCache) ipr).getEventObject(oSource, Arc3DRenderEvent.class);
			are3d.setBackground(paletteEntry);
			are3d.setOutline(la);
			are3d.setStartAngle(-90.0);
			are3d.setAngleExtent(180.0);
			are3d.setTopLeft3D(
					goFactory.createLocation3D(lo3d.getX() - iSize - offset, lo3d.getY() + iSize, lo3d.getZ()));
			are3d.setHeight(iSize);
			are3d.setWidth(iSize);

			preCopy = are3d.copy();
			dc.addPlane(are3d, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final ArcRenderEvent are = ((EventObjectCache) ipr).getEventObject(oSource, ArcRenderEvent.class);
			if (bTransposed) {
				are.setStartAngle(0);
				are.setAngleExtent(180.0);
				are.setBounds(goFactory.createBounds(lo.getX() - iSize, lo.getY() - offset, 2 * iSize, 2 * iSize));
			} else {
				are.setStartAngle(-90.0);
				are.setAngleExtent(180.0);
				are.setBounds(
						goFactory.createBounds(lo.getX() - iSize - offset, lo.getY() - iSize, 2 * iSize, 2 * iSize));
			}
			are.setBackground(paletteEntry);
			are.setOutline(la);

			preCopy = are.copy();
			if (bDeferred) {
				dc.addMarker(are, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillArc(are);
				ipr.drawArc(are);
			}
		}
	}

	private void drawHexagon(IPrimitiveRenderer ipr) throws ChartException {
		double offset1 = Math.floor(iSize * Math.sqrt(3) / 2);
		double offset2 = Math.floor(0.5 * iSize);
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[6];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - offset2, lo3d.getY() - offset1, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + offset2, lo3d.getY() - offset1, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY(), lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() + offset2, lo3d.getY() + offset1, lo3d.getZ());
			loa[4] = goFactory.createLocation3D(lo3d.getX() - offset2, lo3d.getY() + offset1, lo3d.getZ());
			loa[5] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY(), lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[6];
			loa[0] = goFactory.createLocation(lo.getX() - offset2, lo.getY() - offset1);
			loa[1] = goFactory.createLocation(lo.getX() + offset2, lo.getY() - offset1);
			loa[2] = goFactory.createLocation(lo.getX() + iSize, lo.getY());
			loa[3] = goFactory.createLocation(lo.getX() + offset2, lo.getY() + offset1);
			loa[4] = goFactory.createLocation(lo.getX() - offset2, lo.getY() + offset1);
			loa[5] = goFactory.createLocation(lo.getX() - iSize, lo.getY());

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawRectangle(IPrimitiveRenderer ipr) throws ChartException {
		double offset = 0.5 * iSize;
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - offset, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - offset, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + offset, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + offset, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - iSize, lo.getY() + offset);
			loa[1] = goFactory.createLocation(lo.getX() + iSize, lo.getY() + offset);
			loa[2] = goFactory.createLocation(lo.getX() + iSize, lo.getY() - offset);
			loa[3] = goFactory.createLocation(lo.getX() - iSize, lo.getY() - offset);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	private void drawStar(IPrimitiveRenderer ipr) throws ChartException {
		double offsety = (iSize * Math.tan(Math.toRadians(18)))
				/ (Math.tan(Math.toRadians(18)) + Math.tan(Math.toRadians(36)));
		double offsetx = offsety * Math.tan(Math.toRadians(36));
		double offset1 = offsetx + offsetx / Math.sin(Math.toRadians(18));
		double offset2 = offsetx + 2 * offsetx * Math.sin(Math.toRadians(18));
		double offset3 = 2 * offsetx * Math.cos(Math.toRadians(18)) - offsety;
		double offset4 = 2 * offsetx * (1 + Math.sin(Math.toRadians(18)));
		double offset5 = offset4 / Math.tan(Math.toRadians(18)) - iSize;
		if (bRendering3D) {
			final Line3DRenderEvent lre0 = new Line3DRenderEvent(oSource);
			lre0.setStart3D(goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + iSize, lo3d.getZ()));
			lre0.setEnd3D(goFactory.createLocation3D(lo3d.getX() + offsetx, lo3d.getY() + offsety, lo3d.getZ()));

			final Line3DRenderEvent lre1 = new Line3DRenderEvent(oSource);
			lre1.setStart3D(goFactory.createLocation3D(lo3d.getX() + offsetx, lo3d.getY() + offsety, lo3d.getZ()));
			lre1.setEnd3D(goFactory.createLocation3D(lo3d.getX() + offset1, lo3d.getY() + offsety, lo3d.getZ()));

			final Line3DRenderEvent lre2 = new Line3DRenderEvent(oSource);
			lre2.setStart3D(goFactory.createLocation3D(lo3d.getX() + offset1, lo3d.getY() + offsety, lo3d.getZ()));
			lre2.setEnd3D(goFactory.createLocation3D(lo3d.getX() + offset2, lo3d.getY() - offset3, lo3d.getZ()));

			final Line3DRenderEvent lre3 = new Line3DRenderEvent(oSource);
			lre3.setStart3D(goFactory.createLocation3D(lo3d.getX() + offset2, lo3d.getY() - offset3, lo3d.getZ()));
			lre3.setEnd3D(goFactory.createLocation3D(lo3d.getX() + offset4, lo3d.getY() - offset5, lo3d.getZ()));

			final Line3DRenderEvent lre4 = new Line3DRenderEvent(oSource);
			lre4.setStart3D(goFactory.createLocation3D(lo3d.getX() + offset4, lo3d.getY() - offset5, lo3d.getZ()));
			lre4.setEnd3D(goFactory.createLocation3D(lo3d.getX(), lo3d.getY() - offsety / Math.cos(Math.toRadians(18)),
					lo3d.getZ()));

			final Line3DRenderEvent lre5 = new Line3DRenderEvent(oSource);
			lre5.setStart3D(goFactory.createLocation3D(lo3d.getX(),
					lo3d.getY() - offsety / Math.cos(Math.toRadians(18)), lo3d.getZ()));
			lre5.setEnd3D(goFactory.createLocation3D(lo3d.getX() - offset4, lo3d.getY() - offset5, lo3d.getZ()));

			final Line3DRenderEvent lre6 = new Line3DRenderEvent(oSource);
			lre6.setStart3D(goFactory.createLocation3D(lo3d.getX() - offset4, lo3d.getY() - offset5, lo3d.getZ()));
			lre6.setEnd3D(goFactory.createLocation3D(lo3d.getX() - offset2, lo3d.getY() - offset3, lo3d.getZ()));

			final Line3DRenderEvent lre7 = new Line3DRenderEvent(oSource);
			lre7.setStart3D(goFactory.createLocation3D(lo3d.getX() - offset2, lo3d.getY() - offset3, lo3d.getZ()));
			lre7.setEnd3D(goFactory.createLocation3D(lo3d.getX() - offset1, lo3d.getY() + offsety, lo3d.getZ()));

			final Line3DRenderEvent lre8 = new Line3DRenderEvent(oSource);
			lre8.setStart3D(goFactory.createLocation3D(lo3d.getX() - offset1, lo3d.getY() + offsety, lo3d.getZ()));
			lre8.setEnd3D(goFactory.createLocation3D(lo3d.getX() - offsetx, lo3d.getY() + offsety, lo3d.getZ()));

			final Line3DRenderEvent lre9 = new Line3DRenderEvent(oSource);
			lre9.setStart3D(goFactory.createLocation3D(lo3d.getX() - offsetx, lo3d.getY() + offsety, lo3d.getZ()));
			lre9.setEnd3D(goFactory.createLocation3D(lo3d.getX(), lo3d.getY() + iSize, lo3d.getZ()));

			final Area3DRenderEvent area3d = new Area3DRenderEvent(oSource);
			area3d.add(lre0);
			area3d.add(lre1);
			area3d.add(lre2);
			area3d.add(lre3);
			area3d.add(lre4);
			area3d.add(lre5);
			area3d.add(lre6);
			area3d.add(lre7);
			area3d.add(lre8);
			area3d.add(lre9);
			area3d.setBackground(paletteEntry);
			area3d.setOutline(la);

			preCopy = area3d.copy();
			dc.addPlane(area3d, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final LineRenderEvent lre0 = new LineRenderEvent(oSource);
			lre0.setStart(goFactory.createLocation(lo.getX(), lo.getY() - iSize));
			lre0.setEnd(goFactory.createLocation(lo.getX() + offsetx, lo.getY() - offsety));

			final LineRenderEvent lre1 = new LineRenderEvent(oSource);
			lre1.setStart(goFactory.createLocation(lo.getX() + offsetx, lo.getY() - offsety));
			lre1.setEnd(goFactory.createLocation(lo.getX() + offset1, lo.getY() - offsety));

			final LineRenderEvent lre2 = new LineRenderEvent(oSource);
			lre2.setStart(goFactory.createLocation(lo.getX() + offset1, lo.getY() - offsety));
			lre2.setEnd(goFactory.createLocation(lo.getX() + offset2, lo.getY() + offset3));

			final LineRenderEvent lre3 = new LineRenderEvent(oSource);
			lre3.setStart(goFactory.createLocation(lo.getX() + offset2, lo.getY() + offset3));
			lre3.setEnd(goFactory.createLocation(lo.getX() + offset4, lo.getY() + offset5));

			final LineRenderEvent lre4 = new LineRenderEvent(oSource);
			lre4.setStart(goFactory.createLocation(lo.getX() + offset4, lo.getY() + offset5));
			lre4.setEnd(goFactory.createLocation(lo.getX(), lo.getY() + offsety / Math.cos(Math.toRadians(18))));

			final LineRenderEvent lre5 = new LineRenderEvent(oSource);
			lre5.setStart(goFactory.createLocation(lo.getX(), lo.getY() + offsety / Math.cos(Math.toRadians(18))));
			lre5.setEnd(goFactory.createLocation(lo.getX() - offset4, lo.getY() + offset5));

			final LineRenderEvent lre6 = new LineRenderEvent(oSource);
			lre6.setStart(goFactory.createLocation(lo.getX() - offset4, lo.getY() + offset5));
			lre6.setEnd(goFactory.createLocation(lo.getX() - offset2, lo.getY() + offset3));

			final LineRenderEvent lre7 = new LineRenderEvent(oSource);
			lre7.setStart(goFactory.createLocation(lo.getX() - offset2, lo.getY() + offset3));
			lre7.setEnd(goFactory.createLocation(lo.getX() - offset1, lo.getY() - offsety));

			final LineRenderEvent lre8 = new LineRenderEvent(oSource);
			lre8.setStart(goFactory.createLocation(lo.getX() - offset1, lo.getY() - offsety));
			lre8.setEnd(goFactory.createLocation(lo.getX() - offsetx, lo.getY() - offsety));

			final LineRenderEvent lre9 = new LineRenderEvent(oSource);
			lre9.setStart(goFactory.createLocation(lo.getX() - offsetx, lo.getY() - offsety));
			lre9.setEnd(goFactory.createLocation(lo.getX(), lo.getY() - iSize));

			final AreaRenderEvent area = new AreaRenderEvent(oSource);
			area.add(lre0);
			area.add(lre1);
			area.add(lre2);
			area.add(lre3);
			area.add(lre4);
			area.add(lre5);
			area.add(lre6);
			area.add(lre7);
			area.add(lre8);
			area.add(lre9);
			area.setBackground(paletteEntry);
			area.setOutline(la);
			preCopy = area.copy();

			if (bDeferred) {
				dc.addMarker(area, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillArea(area);
				ipr.drawArea(area);
			}
		}
	}

	private void drawColumn(IPrimitiveRenderer ipr) throws ChartException {
		double offset = 0.5 * iSize;
		if (bRendering3D) {
			final Location3D[] loa = new Location3D[4];
			loa[0] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() - iSize, lo3d.getZ());
			loa[1] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() - iSize, lo3d.getZ());
			loa[2] = goFactory.createLocation3D(lo3d.getX() + offset, lo3d.getY() + iSize, lo3d.getZ());
			loa[3] = goFactory.createLocation3D(lo3d.getX() - offset, lo3d.getY() + iSize, lo3d.getZ());

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource,
					Polygon3DRenderEvent.class);
			pre.setPoints3D(loa);
			pre.setDoubleSided(true);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		} else {
			final Location[] loa = new Location[4];
			loa[0] = goFactory.createLocation(lo.getX() - offset, lo.getY() + iSize);
			loa[1] = goFactory.createLocation(lo.getX() + offset, lo.getY() + iSize);
			loa[2] = goFactory.createLocation(lo.getX() + offset, lo.getY() - iSize);
			loa[3] = goFactory.createLocation(lo.getX() - offset, lo.getY() - iSize);

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loa);
			pre.setBackground(paletteEntry);
			pre.setOutline(la);

			preCopy = pre.copy();
			if (bDeferred) {
				dc.addMarker(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW, iSize, zOrder);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		}
	}

	/**
	 * @return Returns the rendering event containing the area affected by last
	 *         drawing operation.
	 */
	public PrimitiveRenderEvent getRenderArea() {
		return preCopy;
	}
}
