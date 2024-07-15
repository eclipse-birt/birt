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

package org.eclipse.birt.chart.device.swt;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.chart.device.DeviceAdapter;
import org.eclipse.birt.chart.device.FontUtil;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.ImageSourceType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.render.InteractiveRenderer;
import org.eclipse.birt.chart.util.PatternImageUtil;
import org.eclipse.birt.chart.util.PatternImageUtil.ByteColorModel;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;

/**
 * This class implements the SWT primitive rendering code for each primitive
 * instruction sent out by the chart generation process.
 */
public class SwtRendererImpl extends DeviceAdapter {

	/**
	 * A property name that identifies the double-buffered drawing capability.
	 */
	public static final String DOUBLE_BUFFERED = "device.double.buffered"; //$NON-NLS-1$

	private final LinkedHashMap<TriggerCondition, List<RegionAction>> _lhmAllTriggers = new LinkedHashMap<>();

	private IDisplayServer _ids;

	private SwtTextRenderer _tr;

	private GC _gc = null;

	private IUpdateNotifier _iun = null;

	private SwtEventHandler _eh = null;

	private double dTranslateX = 0;

	private double dTranslateY = 0;

	private double dRotateInDegrees = 0;

	private double dScale = 1;

	private InteractiveRenderer iv;

	static final int CEIL = 1;

	static final int TRUNCATE = 2;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swt"); //$NON-NLS-1$

	/**
	 * The required zero-argument constructor
	 */
	public SwtRendererImpl() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			_ids = ps.getDisplayServer("ds.SWT"); //$NON-NLS-1$
			_tr = new SwtTextRenderer(_ids);
			iv = new InteractiveRenderer();
		} catch (ChartException pex) {
			logger.log(pex);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.IDeviceRenderer#getGraphicsContext()
	 */
	@Override
	public Object getGraphicsContext() {
		return _gc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.IDeviceRenderer#getDisplayServer()
	 */
	@Override
	public IDisplayServer getDisplayServer() {
		return _ids;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#setClip(org.eclipse.birt.
	 * chart.event.ClipRenderEvent)
	 */
	@Override
	public void setClip(ClipRenderEvent cre) {
		final Location[] loa = cre.getVertices();

		if (loa == null) {
			_gc.setClipping((Region) null);
		} else {
			Region rgClipping = new Region();
			rgClipping.add(getCoordinatesAsInts(loa, TRUNCATE, dTranslateX, dTranslateY, dScale));
			_gc.setClipping(rgClipping);
			rgClipping.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawImage(org.eclipse.birt.
	 * chart.event.ImageRenderEvent)
	 */
	@Override
	public void drawImage(ImageRenderEvent pre) throws ChartException {
		if (pre.getImage() == null || pre.getLocation() == null) {
			return;
		}

		Image img = null;

		if (pre.getImage() instanceof EmbeddedImage) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(
						Base64.getDecoder().decode(((EmbeddedImage) pre.getImage()).getData().getBytes()));

				img = new org.eclipse.swt.graphics.Image(((SwtDisplayServer) _ids).getDevice(), bis);
			} catch (Exception ilex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, ilex);
			}
		} else if (pre.getImage().getSource() != ImageSourceType.FILE
				&& pre.getImage().getSource() != ImageSourceType.REPORT) {
			try {
				final String sUrl = pre.getImage().getURL();
				img = (Image) _ids.loadImage(SecurityUtil.newURL(sUrl));
			} catch (ChartException ilex) {
				// Ignore the invalid path, and log it only
				logger.log(new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, ilex));
			} catch (MalformedURLException muex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, muex);
			}
		}

		if (img == null) {
			return;
		}

		Location loc = pre.getLocation();
		Position pos = pre.getPosition();
		if (pos == null) {
			pos = Position.INSIDE_LITERAL;
		}

		final boolean bSizeSet = pre.getWidth() * pre.getHeight() > 0;
		int width = bSizeSet ? pre.getWidth() : img.getBounds().width;
		int height = bSizeSet ? pre.getHeight() : img.getBounds().height;
		int x = (int) loc.getX();
		int y = (int) loc.getY();

		switch (pos.getValue()) {
		case Position.INSIDE:
		case Position.OUTSIDE:
			x -= width / 2;
			y -= height / 2;
			break;
		case Position.LEFT:
			x -= width;
			y -= height / 2;
			break;
		case Position.RIGHT:
			y -= height / 2;
			break;
		case Position.ABOVE:
			x -= width / 2;
			y -= height;
			break;
		case Position.BELOW:
			x -= width / 2;
			break;
		}

		// Reset alpha
		R31Enhance.setAlpha(_gc, (ColorDefinition) null);
		if (bSizeSet) {
			_gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, x, y, pre.getWidth(),
					pre.getHeight());
		} else {
			_gc.drawImage(img, x, y);
		}

		img.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawLine(org.eclipse.birt.
	 * chart.event.LineRenderEvent)
	 */
	@Override
	public void drawLine(LineRenderEvent lre) throws ChartException {
		iv.modifyEvent(lre);
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = lre.getLineAttributes();
		if (!validateLineAttributes(lre.getSource(), lia) || lia.getColor() == null) {
			return;
		}

		// DRAW THE LINE
		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();
		final Color cFG = (Color) _ids.getColor(lia.getColor());
		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case (LineStyle.DOTTED):
			iLineStyle = SWT.LINE_DOT;
			break;
		case (LineStyle.DASH_DOTTED):
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case (LineStyle.DASHED):
			iLineStyle = SWT.LINE_DASH;
			break;
		}
		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		final Location lo1 = lre.getStart();
		final Location lo2 = lre.getEnd();
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		_gc.drawLine((int) ((lo1.getX() + dTranslateX) * dScale), (int) ((lo1.getY() + dTranslateY) * dScale),
				(int) ((lo2.getX() + dTranslateX) * dScale), (int) ((lo2.getY() + dTranslateY) * dScale));

		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawRectangle(org.eclipse.
	 * birt.chart.event.RectangleRenderEvent)
	 */
	@Override
	public void drawRectangle(RectangleRenderEvent rre) throws ChartException {
		iv.modifyEvent(rre);
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = rre.getOutline();
		if (!validateLineAttributes(rre.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), rre.getBackground(), _ids);
		if (cFG == null) {
			return;
		}

		// DRAW THE RECTANGLE WITH THE APPROPRIATE LINE STYLE
		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();
		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case (LineStyle.DOTTED):
			iLineStyle = SWT.LINE_DOT;
			break;
		case (LineStyle.DASH_DOTTED):
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case (LineStyle.DASHED):
			iLineStyle = SWT.LINE_DASH;
			break;
		}
		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		final Bounds bo = normalizeBounds(rre.getBounds());
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		_gc.drawRectangle((int) ((bo.getLeft() + dTranslateX) * dScale), (int) ((bo.getTop() + dTranslateY) * dScale),
				(int) (bo.getWidth() * dScale) - 1, (int) (bo.getHeight() * dScale) - 1);

		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IPrimitiveRenderer#fillRectangle(org.eclipse.
	 * birt.chart.event.RectangleRenderEvent)
	 */
	@Override
	public void fillRectangle(RectangleRenderEvent rre) throws ChartException {
		iv.modifyEvent(rre);
		final Fill flBackground = validateMultipleFill(rre.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Bounds bo = normalizeBounds(rre.getBounds());
		final Rectangle r = new Rectangle((int) ((bo.getLeft() + dTranslateX) * dScale),
				(int) ((bo.getTop() + dTranslateY) * dScale), (int) Math.ceil(bo.getWidth() * dScale),
				(int) Math.ceil(bo.getHeight() * dScale));

		final Path pt = new Path(((SwtDisplayServer) _ids).getDevice());
		pt.moveTo(r.x, r.y);
		pt.lineTo(r.x, r.y + r.height);
		pt.lineTo(r.x + r.width, r.y + r.height);
		pt.lineTo(r.x + r.width, r.y);

		try {
			if (flBackground instanceof ColorDefinition) {
				fillPathColor(pt, (ColorDefinition) flBackground);
			}
			if (flBackground instanceof Gradient) {
				fillPathGradient(pt, (Gradient) flBackground, r);
			} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
				fillPathImage(pt, (org.eclipse.birt.chart.model.attribute.Image) flBackground);
			}
		} finally {
			pt.dispose();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawPolygon(org.eclipse.birt.
	 * chart.event.PolygonRenderEvent)
	 */
	@Override
	public void drawPolygon(PolygonRenderEvent pre) throws ChartException {
		iv.modifyEvent(pre);
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = pre.getOutline();
		if (!validateLineAttributes(pre.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), pre.getBackground(), _ids);
		if (cFG == null) {
			return;
		}

		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();

		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case (LineStyle.DOTTED):
			iLineStyle = SWT.LINE_DOT;
			break;
		case (LineStyle.DASH_DOTTED):
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case (LineStyle.DASHED):
			iLineStyle = SWT.LINE_DASH;
			break;
		}

		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		_gc.drawPolygon(getCoordinatesAsInts(pre.getPoints(), TRUNCATE, dTranslateX, dTranslateY, dScale));

		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#fillPolygon(org.eclipse.birt.
	 * chart.event.PolygonRenderEvent)
	 */
	@Override
	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		iv.modifyEvent(pre);

		// DUE TO RESTRICTIVE SWT API, WE SET A CLIPPED POLYGON REGION
		// AND RENDER THE POLYGON BY RENDERING A CONTAINING RECTANGLE WHERE
		// THE RECTANGLE BOUNDS CORRESPOND TO THE POLYGON BOUNDS
		// NOTE: SOME INCOMPLETE PAINTING ERRORS SEEM TO EXIST FOR GRADIENT POLY
		// FILLS

		final Fill flBackground = validateMultipleFill(pre.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Bounds bo = normalizeBounds(pre.getBounds());
		final Rectangle r = new Rectangle((int) ((bo.getLeft() + dTranslateX) * dScale),
				(int) ((bo.getTop() + dTranslateY) * dScale), (int) Math.ceil(bo.getWidth() * dScale),
				(int) Math.ceil(bo.getHeight() * dScale));

		float[] points = convertDoubleToFloat(
				getDoubleCoordinatesAsInts(pre.getPoints(), TRUNCATE, dTranslateX, dTranslateY, dScale));
		if (points.length < 1) {
			return;
		}
		final Path pt = new Path(((SwtDisplayServer) _ids).getDevice());
		pt.moveTo(points[0], points[1]);
		for (int i = 1; i < points.length / 2; i++) {
			pt.lineTo(points[2 * i], points[2 * i + 1]);
		}

		try {
			if (flBackground instanceof ColorDefinition) {
				fillPathColor(pt, (ColorDefinition) flBackground);
			} else if (flBackground instanceof Gradient) {
				fillPathGradient(pt, (Gradient) flBackground, r);
			} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
				fillPathImage(pt, (org.eclipse.birt.chart.model.attribute.Image) flBackground);
			}
		} finally {
			pt.dispose();
		}

	}

	/**
	 * Extra fix due to SWT arc rendering limitation.
	 *
	 * @param _gc
	 * @param are
	 * @param dTranslateX
	 * @param dTranslateY
	 * @param dScale
	 */
	protected void drawArc(GC _gc, Device _dv, ArcRenderEvent are, double dTranslateX, double dTranslateY,
			double dScale) {

		if (are.getInnerRadius() >= 0 && (are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
				|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
			Bounds bo = goFactory.createBounds(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
					are.getHeight());

			Bounds rctOuter, rctInner;

			rctOuter = getOuterRectangle(are, dTranslateX, dTranslateY, dScale, bo);

			rctInner = getInnerRectangle(are, dTranslateX, dTranslateY, dScale, bo);

			double startAngle = Math.toRadians(-are.getStartAngle());
			double stopAngle = Math.toRadians(-are.getStartAngle() - are.getAngleExtent());

			double xsOuter = (rctOuter.getLeft() + (Math.cos(startAngle) * 0.5 + 0.5) * rctOuter.getWidth());
			double ysOuter = (rctOuter.getTop() + (Math.sin(startAngle) * 0.5 + 0.5) * rctOuter.getHeight());

			// double xeOuter = ( rctOuter.getLeft( ) + ( Math.cos( stopAngle )
			// * 0.5 + 0.5 )
			// * rctOuter.getWidth( ) );
			// double yeOuter = ( rctOuter.getTop( ) + ( Math.sin( stopAngle ) *
			// 0.5 + 0.5 )
			// * rctOuter.getHeight( ) );
			//
			// double xsInner = ( rctInner.getLeft( ) + ( Math.cos( startAngle )
			// * 0.5 + 0.5 )
			// * rctInner.getWidth( ) );
			// double ysInner = ( rctInner.getTop( ) + ( Math.sin( startAngle )
			// * 0.5 + 0.5 )
			// * rctInner.getHeight( ) );

			double xeInner = (rctInner.getLeft() + (Math.cos(stopAngle) * 0.5 + 0.5) * rctInner.getWidth());
			double yeInner = (rctInner.getTop() + (Math.sin(stopAngle) * 0.5 + 0.5) * rctInner.getHeight());

			Path pt = new Path(_dv);
			pt.addArc((float) rctOuter.getLeft(), (float) rctOuter.getTop(), (float) rctOuter.getWidth(),
					(float) rctOuter.getHeight(), (float) are.getStartAngle(), (float) are.getAngleExtent());

			pt.lineTo((float) xeInner, (float) yeInner);

			pt.addArc((float) rctInner.getLeft(), (float) rctInner.getTop(), (float) rctInner.getWidth(),
					(float) rctInner.getHeight(), (float) (are.getStartAngle() + are.getAngleExtent()),
					(float) -are.getAngleExtent());

			pt.lineTo((float) xsOuter, (float) ysOuter);

			_gc.drawPath(pt);

			pt.dispose();
		} else if (are.getStyle() == ArcRenderEvent.OPEN) {
			_gc.drawArc((int) ((are.getTopLeft().getX() + dTranslateX) * dScale),
					(int) ((are.getTopLeft().getY() + dTranslateY) * dScale), (int) (are.getWidth() * dScale),
					(int) (are.getHeight() * dScale), (int) are.getStartAngle(), (int) are.getAngleExtent());
		} else {
			double xc = ((are.getTopLeft().getX() + dTranslateX + are.getWidth() / 2d) * dScale);
			double yc = ((are.getTopLeft().getY() + dTranslateY + are.getHeight() / 2d) * dScale);

			double xs = 0, ys = 0, xe = 0, ye = 0;

			double angle = Math.toRadians(-are.getStartAngle());

			xs = ((are.getTopLeft().getX() + dTranslateX + (Math.cos(angle) * 0.5 + 0.5) * are.getWidth()) * dScale);
			ys = ((are.getTopLeft().getY() + dTranslateY + (Math.sin(angle) * 0.5 + 0.5) * are.getHeight()) * dScale);

			angle = Math.toRadians(-are.getStartAngle() - are.getAngleExtent());

			xe = ((are.getTopLeft().getX() + dTranslateX + (Math.cos(angle) * 0.5 + 0.5) * are.getWidth()) * dScale);
			ye = ((are.getTopLeft().getY() + dTranslateY + (Math.sin(angle) * 0.5 + 0.5) * are.getHeight()) * dScale);

			Path pt = new Path(_dv);
			if (are.getStyle() == ArcRenderEvent.CLOSED) {
				pt.addArc((float) ((are.getTopLeft().getX() + dTranslateX) * dScale),
						(float) ((are.getTopLeft().getY() + dTranslateY) * dScale), (float) (are.getWidth() * dScale),
						(float) (are.getHeight() * dScale), (float) are.getStartAngle(), (float) are.getAngleExtent());
				// fix in case angle extent is zero.
				pt.moveTo((float) xe, (float) ye);
				pt.lineTo((float) xs, (float) ys);

				_gc.drawPath(pt);
			} else if (are.getStyle() == ArcRenderEvent.SECTOR) {
				pt.addArc((float) ((are.getTopLeft().getX() + dTranslateX) * dScale),
						(float) ((are.getTopLeft().getY() + dTranslateY) * dScale), (float) (are.getWidth() * dScale),
						(float) (are.getHeight() * dScale), (float) are.getStartAngle(), (float) are.getAngleExtent());
				// fix in case angle extent is zero.
				pt.moveTo((float) xe, (float) ye);
				pt.lineTo((float) xc, (float) yc);
				pt.lineTo((float) xs, (float) ys);

				_gc.drawPath(pt);
			}
			pt.dispose();
		}

	}

	protected Bounds getOuterRectangle(ArcRenderEvent are, double dTranslateX, double dTranslateY, double dScale,
			Bounds bo) {
		Bounds rctOuter;
		if (are.getOuterRadius() > 0) {
			double radio = bo.getHeight() / bo.getWidth();
			rctOuter = goFactory.createBounds(
					((bo.getLeft() + dTranslateX + (bo.getWidth() / 2d - are.getOuterRadius())) * dScale),
					((bo.getTop() + dTranslateY + (bo.getHeight() / 2d - are.getOuterRadius() * radio)) * dScale),
					(2 * are.getOuterRadius() * dScale), (2 * are.getOuterRadius() * dScale) * radio);
		} else {
			rctOuter = goFactory.createBounds(((bo.getLeft() + dTranslateX) * dScale),
					((bo.getTop() + dTranslateY) * dScale), (bo.getWidth() * dScale), (bo.getHeight() * dScale));
		}
		return rctOuter;
	}

	protected Bounds getInnerRectangle(ArcRenderEvent are, double dTranslateX, double dTranslateY, double dScale,
			Bounds bo) {
		Bounds rctInner;
		if (are.getInnerRadius() > 0) {
			double radio = bo.getHeight() / bo.getWidth();
			rctInner = goFactory.createBounds(
					((bo.getLeft() + dTranslateX + (bo.getWidth() / 2d - are.getInnerRadius())) * dScale),
					((bo.getTop() + dTranslateY + (bo.getHeight() / 2d - are.getInnerRadius() * radio)) * dScale),
					(2 * are.getInnerRadius() * dScale), (2 * are.getInnerRadius() * dScale) * radio);
		} else {
			rctInner = goFactory.createBounds(((bo.getLeft() + dTranslateX + bo.getWidth() / 2d) * dScale),
					((bo.getTop() + dTranslateY + bo.getHeight() / 2d) * dScale), 0, 0);
		}
		return rctInner;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawArc(org.eclipse.birt.
	 * chart.event.ArcRenderEvent)
	 */
	@Override
	public void drawArc(ArcRenderEvent are) throws ChartException {
		iv.modifyEvent(are);

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline();
		if (!validateLineAttributes(are.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
		if (cFG == null) {
			return;
		}

		// DRAW THE ARC WITH THE SPECIFIED LINE ATTRIBUTES
		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();
		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case LineStyle.DOTTED:
			iLineStyle = SWT.LINE_DOT;
			break;
		case LineStyle.DASH_DOTTED:
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case LineStyle.DASHED:
			iLineStyle = SWT.LINE_DASH;
			break;
		}
		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		drawArc(_gc, ((SwtDisplayServer) _ids).getDevice(), are, dTranslateX, dTranslateY, dScale);

		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#fillArc(org.eclipse.birt.
	 * chart.event.ArcRenderEvent)
	 */
	@Override
	public void fillArc(ArcRenderEvent are) throws ChartException {
		iv.modifyEvent(are);

		Fill flBackground = validateMultipleFill(are.getBackground());

		if (isFullTransparent(flBackground) || are.getAngleExtent() == 0) {
			return;
		}

		Bounds bo = goFactory.createBounds(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
				are.getHeight());
		final Rectangle r = new Rectangle((int) ((bo.getLeft() + dTranslateX) * dScale),
				(int) ((bo.getTop() + dTranslateY) * dScale), (int) Math.ceil(bo.getWidth() * dScale),
				(int) Math.ceil(bo.getHeight() * dScale));

		Path pt = new Path(((SwtDisplayServer) _ids).getDevice());

		if (are.getInnerRadius() >= 0 && (are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
				|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
			Bounds rctOuter, rctInner;

			rctOuter = getOuterRectangle(are, dTranslateX, dTranslateY, dScale, bo);

			rctInner = getInnerRectangle(are, dTranslateX, dTranslateY, dScale, bo);

			double startAngle = Math.toRadians(-are.getStartAngle());
			double stopAngle = Math.toRadians(-are.getStartAngle() - are.getAngleExtent());

			double xsOuter = (rctOuter.getLeft() + (Math.cos(startAngle) * 0.5 + 0.5) * rctOuter.getWidth());
			double ysOuter = (rctOuter.getTop() + (Math.sin(startAngle) * 0.5 + 0.5) * rctOuter.getHeight());

			double xeInner = (rctInner.getLeft() + (Math.cos(stopAngle) * 0.5 + 0.5) * rctInner.getWidth());
			double yeInner = (rctInner.getTop() + (Math.sin(stopAngle) * 0.5 + 0.5) * rctInner.getHeight());

			pt.addArc((float) rctOuter.getLeft(), (float) rctOuter.getTop(), (float) rctOuter.getWidth(),
					(float) rctOuter.getHeight(), (float) are.getStartAngle(), (float) are.getAngleExtent());

			pt.lineTo((float) xeInner, (float) yeInner);

			pt.addArc((float) rctInner.getLeft(), (float) rctInner.getTop(), (float) rctInner.getWidth(),
					(float) rctInner.getHeight(), (float) (are.getStartAngle() + are.getAngleExtent()),
					(float) -are.getAngleExtent());

			pt.lineTo((float) xsOuter, (float) ysOuter);
		} else if (are.getStyle() == ArcRenderEvent.SECTOR
				|| (are.getStyle() == ArcRenderEvent.CLOSED && Math.abs(are.getAngleExtent()) >= 360)) {
			double xc = ((are.getTopLeft().getX() + dTranslateX + are.getWidth() / 2d) * dScale);
			double yc = ((are.getTopLeft().getY() + dTranslateY + are.getHeight() / 2d) * dScale);

			double xs = 0, ys = 0;
			double angle = Math.toRadians(-are.getStartAngle());

			xs = ((are.getTopLeft().getX() + dTranslateX + (Math.cos(angle) * 0.5 + 0.5) * are.getWidth()) * dScale);
			ys = ((are.getTopLeft().getY() + dTranslateY + (Math.sin(angle) * 0.5 + 0.5) * are.getHeight()) * dScale);

			if (are.getStyle() == ArcRenderEvent.CLOSED) {
				pt.addArc((float) ((are.getTopLeft().getX() + dTranslateX) * dScale),
						(float) ((are.getTopLeft().getY() + dTranslateY) * dScale), (float) (are.getWidth() * dScale),
						(float) (are.getHeight() * dScale), (float) are.getStartAngle(), (float) are.getAngleExtent());
				pt.lineTo((float) xs, (float) ys);
			} else if (are.getStyle() == ArcRenderEvent.SECTOR) {
				pt.addArc((float) ((are.getTopLeft().getX() + dTranslateX) * dScale),
						(float) ((are.getTopLeft().getY() + dTranslateY) * dScale), (float) (are.getWidth() * dScale),
						(float) (are.getHeight() * dScale), (float) are.getStartAngle(), (float) are.getAngleExtent());
				pt.lineTo((float) xc, (float) yc);
				pt.lineTo((float) xs, (float) ys);
			}
		}

		// Extra fix due to SWT arc rendering limitation.
		else if (are.getStyle() == ArcRenderEvent.OPEN || are.getStyle() == ArcRenderEvent.CLOSED) {
			double angle = Math.toRadians(-are.getStartAngle());

			double xs = ((are.getTopLeft().getX() + dTranslateX + (Math.cos(angle) * 0.5 + 0.5) * are.getWidth())
					* dScale);
			double ys = ((are.getTopLeft().getY() + dTranslateY + (Math.sin(angle) * 0.5 + 0.5) * are.getHeight())
					* dScale);

			pt.addArc((float) ((are.getTopLeft().getX() + dTranslateX) * dScale),
					(float) ((are.getTopLeft().getY() + dTranslateY) * dScale), (float) (are.getWidth() * dScale),
					(float) (are.getHeight() * dScale), (float) are.getStartAngle(), (float) are.getAngleExtent());

			pt.lineTo((float) xs, (float) ys);
		}

		try {
			if (flBackground instanceof ColorDefinition) {
				fillPathColor(pt, (ColorDefinition) flBackground);
			} else if (flBackground instanceof Gradient) {
				fillPathGradient(pt, (Gradient) flBackground, r);
			} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
				fillPathImage(pt, (org.eclipse.birt.chart.model.attribute.Image) flBackground);
			}
		} finally {
			pt.dispose();
		}

	}

	private final void fillPathColor(Path path, ColorDefinition g) throws ChartException {
		// skip full transparency for optimization.
		if (!(g.isSetTransparency() && g.getTransparency() == 0)) {
			final Color cBG = (Color) _ids.getColor(g);
			final Color cPreviousBG = _gc.getBackground();
			_gc.setBackground(cBG);

			R31Enhance.setAlpha(_gc, g);

			_gc.fillPath(path);

			cBG.dispose();
			_gc.setBackground(cPreviousBG);
		}
	}

	private final void fillPathGradient(Path path, Gradient g, Rectangle r) throws ChartException {
		final ColorDefinition cdStart = g.getStartColor();
		final ColorDefinition cdEnd = g.getEndColor();
		double dAngleInDegrees = g.getDirection();

		if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
			throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING,
					"SwtRendererImpl.exception.gradient.angle", //$NON-NLS-1$
					new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
		}

		final Color cPreviousFG = _gc.getForeground();
		final Color cPreviousBG = _gc.getBackground();
		Color cFG = (Color) _ids.getColor(cdStart);
		Color cBG = (Color) _ids.getColor(cdEnd);

		float x1, y1, x2, y2;

		// #232647
		// The maximal round-off error to calculate x2 and y2 here can be 2.
		// And if the pattern value is less than the forground, it is very obvious.
		// So we add 2 here to overcome this error.
		final int iMaxError = 2;

		if (dAngleInDegrees == 0) {
			x1 = r.x;
			x2 = r.x + r.width + iMaxError;
			y1 = y2 = r.y;
		} else if (dAngleInDegrees == 90) {
			x1 = x2 = r.x;
			y1 = r.y + r.height + iMaxError;
			y2 = r.y;
		} else if (dAngleInDegrees == -90) {
			x1 = x2 = r.x;
			y1 = r.y;
			y2 = r.y + r.height + iMaxError;
		} else if (dAngleInDegrees > 0) {
			x1 = r.x;
			y1 = r.y + r.height;
			x2 = r.x + r.width + iMaxError;
			y2 = r.y;
		} else {
			x1 = r.x;
			y1 = r.y;
			x2 = r.x + r.width;
			y2 = r.y + r.height;
		}

		_gc.setForeground(cFG);
		_gc.setBackground(cBG);

		R31Enhance.setAlpha(_gc, g);

		Pattern pattern = new Pattern(_gc.getDevice(), x1, y1, x2, y2, cFG, cdStart.getTransparency(), cBG,
				cdEnd.getTransparency());
		_gc.setBackgroundPattern(pattern);
		_gc.fillPath(path);

		_gc.setForeground(cPreviousFG);
		_gc.setBackground(cPreviousBG);
		cFG.dispose();
		cBG.dispose();
		pattern.dispose();
	}

	private final void fillPathImage(Path path, org.eclipse.birt.chart.model.attribute.Image g) throws ChartException {
		org.eclipse.swt.graphics.Image img = null;
		if (g instanceof EmbeddedImage) {
			try {
				String imageData = ((EmbeddedImage) g).getData();
				if (imageData != null) {
					ByteArrayInputStream bis = new ByteArrayInputStream(
							Base64.getDecoder().decode(((EmbeddedImage) g).getData().getBytes()));

					img = new org.eclipse.swt.graphics.Image(((SwtDisplayServer) _ids).getDevice(), bis);
				} else {
					img = createEmptyImage();
				}
			} catch (Exception ilex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, ilex);
			}
		} else if (g instanceof PatternImage) {
			PatternImage patternImage = (PatternImage) g;
			img = createImageFromPattern(patternImage);
		} else if (g.getSource() == ImageSourceType.STATIC) {
			final String sUrl = g.getURL();
			try {
				img = (org.eclipse.swt.graphics.Image) _ids.loadImage(SecurityUtil.newURL(sUrl));
			} catch (MalformedURLException muex) {
				throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING, muex);
			}
		} else {
			img = createEmptyImage();
		}

		Pattern pattern = new Pattern(_gc.getDevice(), img);
		_gc.setBackgroundPattern(pattern);
		_gc.fillPath(path);

		pattern.dispose();
		img.dispose();
	}

	private Image createEmptyImage() {
		// To render a blank image for null embedded data
		return new org.eclipse.swt.graphics.Image(((SwtDisplayServer) _ids).getDevice(), 10, 10);
	}

	private Image createImageFromPattern(PatternImage patternImage) {
		Device device = ((SwtDisplayServer) _ids).getDevice();

		PaletteData paletteData = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		byte[] data = PatternImageUtil.createImageData(patternImage, ByteColorModel.BGRA);

		ImageData imageData = new ImageData(8, 8, 32, paletteData, 4, data);

		return new Image(device, imageData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#enableInteraction(org.eclipse
	 * .birt.chart.event.InteractionEvent)
	 */
	@Override
	public void enableInteraction(InteractionEvent iev) throws ChartException {
		if (_iun == null) {
			logger.log(ILogger.INFORMATION,
					Messages.getString("SwtRendererImpl.exception.missing.component.interaction", getULocale())); //$NON-NLS-1$
			return;
		}

		final Trigger[] tga = iev.getTriggers();
		if (tga == null) {
			return;
		}

		Region clipping = new Region();
		_gc.getClipping(clipping);

		// CREATE AND SETUP THE SHAPES FOR INTERACTION
		TriggerCondition tc;
		List<RegionAction> al;
		final PrimitiveRenderEvent pre = iev.getHotSpot();
		if (pre instanceof PolygonRenderEvent) {
			final Location[] loa = ((PolygonRenderEvent) pre).getPoints();

			for (int i = 0; i < tga.length; i++) {
				tc = tga[i].getCondition();
				al = _lhmAllTriggers.get(tc);
				if (al == null) {
					al = new ArrayList<>(4); // UNDER NORMAL
															// CONDITIONS
					_lhmAllTriggers.put(tc, al);
				}
				RegionAction ra = new RegionAction(iev.getStructureSource(), loa, tga[i].getAction(), dTranslateX,
						dTranslateY, dScale, clipping);
				ra.setCursor(iev.getCursor());
				al.add(0, ra);
			}
		} else if (pre instanceof RectangleRenderEvent) {
			final Bounds bo = ((RectangleRenderEvent) pre).getBounds();

			for (int i = 0; i < tga.length; i++) {
				tc = tga[i].getCondition();
				al = _lhmAllTriggers.get(tc);
				if (al == null) {
					al = new ArrayList<>(4); // UNDER NORMAL
															// CONDITIONS
					_lhmAllTriggers.put(tc, al);
				}
				RegionAction ra = new RegionAction(iev.getStructureSource(), bo, tga[i].getAction(), dTranslateX,
						dTranslateY, dScale, clipping);
				ra.setCursor(iev.getCursor());
				al.add(0, ra);
			}
		} else if (pre instanceof OvalRenderEvent) {
			final Bounds boEllipse = ((OvalRenderEvent) pre).getBounds();

			for (int i = 0; i < tga.length; i++) {
				tc = tga[i].getCondition();
				al = _lhmAllTriggers.get(tc);
				if (al == null) {
					al = new ArrayList<>(4); // UNDER NORMAL
															// CONDITIONS
					_lhmAllTriggers.put(tc, al);
				}

				// using rectangle to simulate the oval due to swt limitation.
				RegionAction ra = new RegionAction(iev.getStructureSource(), boEllipse, tga[i].getAction(), dTranslateX,
						dTranslateY, dScale, clipping);
				ra.setCursor(iev.getCursor());
				al.add(0, ra);
			}
		} else if (pre instanceof ArcRenderEvent) {
			final ArcRenderEvent are = (ArcRenderEvent) pre;
			final Bounds boEllipse = are.getEllipseBounds();
			double dStart = are.getStartAngle();
			double dExtent = are.getAngleExtent();
			int iArcType = are.getStyle();

			for (int i = 0; i < tga.length; i++) {
				tc = tga[i].getCondition();
				al = _lhmAllTriggers.get(tc);
				if (al == null) {
					al = new ArrayList<>(4); // UNDER NORMAL
															// CONDITIONS
					_lhmAllTriggers.put(tc, al);
				}

				// using rectangle to simulate the arc due to swt limitation.
				RegionAction ra = new RegionAction(iev.getStructureSource(), boEllipse, dStart, dExtent, iArcType,
						tga[i].getAction(), dTranslateX, dTranslateY, dScale, clipping);
				ra.setCursor(iev.getCursor());
				al.add(0, ra);
			}
		} else if (pre instanceof AreaRenderEvent) {
			final Bounds bo = ((AreaRenderEvent) pre).getBounds();

			for (int i = 0; i < tga.length; i++) {
				tc = tga[i].getCondition();
				al = _lhmAllTriggers.get(tc);
				if (al == null) {
					al = new ArrayList<>(4); // UNDER NORMAL
															// CONDITIONS
					_lhmAllTriggers.put(tc, al);
				}
				RegionAction ra = new RegionAction(iev.getStructureSource(), bo, tga[i].getAction(), dTranslateX,
						dTranslateY, dScale, clipping);
				ra.setCursor(iev.getCursor());
				al.add(0, ra);
			}
		}

		// free the clip region resource.
		clipping.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawArea(org.eclipse.birt.
	 * chart.event.AreaRenderEvent)
	 */
	@Override
	public void drawArea(AreaRenderEvent are) throws ChartException {
		iv.modifyEvent(are);

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline();
		if (!validateLineAttributes(are.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
		if (cFG == null) // IF UNDEFINED, EXIT
		{
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final Path gp = new Path(((SwtDisplayServer) _ids).getDevice());
		PrimitiveRenderEvent pre;
		for (int i = 0; i < are.getElementCount(); i++) {
			pre = are.getElement(i);
			if (pre instanceof ArcRenderEvent) {
				final ArcRenderEvent acre = (ArcRenderEvent) pre;

				gp.addArc((float) acre.getTopLeft().getX(), (float) acre.getTopLeft().getY(), (float) acre.getWidth(),
						(float) acre.getHeight(), (float) acre.getStartAngle(), (float) acre.getAngleExtent());
			} else if (pre instanceof LineRenderEvent) {
				final LineRenderEvent lre = (LineRenderEvent) pre;
				gp.moveTo((float) lre.getStart().getX(), (float) lre.getStart().getY());
				gp.lineTo((float) lre.getEnd().getX(), (float) lre.getEnd().getY());
			}
		}

		// DRAW THE PATH
		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();
		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case (LineStyle.DOTTED):
			iLineStyle = SWT.LINE_DOT;
			break;
		case (LineStyle.DASH_DOTTED):
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case (LineStyle.DASHED):
			iLineStyle = SWT.LINE_DASH;
			break;
		}
		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		_gc.drawPath(gp);

		// Restore state
		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);

		// Free resource
		gp.dispose();
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#fillArea(org.eclipse.birt.
	 * chart.event.AreaRenderEvent)
	 */
	@Override
	public void fillArea(AreaRenderEvent are) throws ChartException {
		iv.modifyEvent(are);

		Fill flBackground = validateMultipleFill(are.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final Path pt = new Path(((SwtDisplayServer) _ids).getDevice());
		PrimitiveRenderEvent pre;
		for (int i = 0; i < are.getElementCount(); i++) {
			pre = are.getElement(i);
			if (pre instanceof ArcRenderEvent) {
				final ArcRenderEvent acre = (ArcRenderEvent) pre;

				pt.addArc((float) acre.getTopLeft().getX(), (float) acre.getTopLeft().getY(), (float) acre.getWidth(),
						(float) acre.getHeight(), (float) acre.getStartAngle(), (float) acre.getAngleExtent());
			} else if (pre instanceof LineRenderEvent) {
				final LineRenderEvent lre = (LineRenderEvent) pre;
				if (i == 0) {
					pt.moveTo((float) lre.getStart().getX(), (float) lre.getStart().getY());
				}
				pt.lineTo((float) lre.getEnd().getX(), (float) lre.getEnd().getY());
			}
		}

		try {
			if (flBackground instanceof ColorDefinition) {
				fillPathColor(pt, (ColorDefinition) flBackground);
			} else if (flBackground instanceof Gradient) {
				final Bounds bo = are.getBounds();
				final Rectangle r = new Rectangle((int) ((bo.getLeft() + dTranslateX) * dScale),
						(int) ((bo.getTop() + dTranslateY) * dScale), (int) (bo.getWidth() * dScale),
						(int) (bo.getHeight() * dScale));
				fillPathGradient(pt, (Gradient) flBackground, r);
			} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
				fillPathImage(pt, (org.eclipse.birt.chart.model.attribute.Image) flBackground);
			}
		} finally {
			pt.dispose();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawOval(org.eclipse.birt.
	 * chart.event.OvalRenderEvent)
	 */
	@Override
	public void drawOval(OvalRenderEvent ore) throws ChartException {
		iv.modifyEvent(ore);

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = ore.getOutline();
		if (!validateLineAttributes(ore.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), ore.getBackground(), _ids);
		if (cFG == null) {
			return;
		}

		// DRAW THE OVAL WITH THE SPECIFIED LINE ATTRIBUTES
		final int iOldLineStyle = _gc.getLineStyle();
		final int iOldLineWidth = _gc.getLineWidth();
		int iLineStyle = SWT.LINE_SOLID;
		switch (lia.getStyle().getValue()) {
		case (LineStyle.DOTTED):
			iLineStyle = SWT.LINE_DOT;
			break;
		case (LineStyle.DASH_DOTTED):
			iLineStyle = SWT.LINE_DASHDOT;
			break;
		case (LineStyle.DASHED):
			iLineStyle = SWT.LINE_DASH;
			break;
		}
		_gc.setLineStyle(iLineStyle);
		_gc.setLineWidth(lia.getThickness());
		final Bounds bo = ore.getBounds();
		_gc.setForeground(cFG);

		R31Enhance.setAlpha(_gc, lia.getColor());

		_gc.drawOval((int) ((bo.getLeft() + dTranslateX) * dScale), (int) ((bo.getTop() + dTranslateY) * dScale),
				(int) (bo.getWidth() * dScale), (int) (bo.getHeight() * dScale));

		_gc.setLineStyle(iOldLineStyle);
		_gc.setLineWidth(iOldLineWidth);
		cFG.dispose();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#fillOval(org.eclipse.birt.
	 * chart.event.OvalRenderEvent)
	 */
	@Override
	public void fillOval(OvalRenderEvent ore) throws ChartException {
		iv.modifyEvent(ore);

		final Fill flBackground = validateMultipleFill(ore.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Bounds bo = ore.getBounds();
		final Rectangle r = new Rectangle((int) ((bo.getLeft() + dTranslateX) * dScale),
				(int) ((bo.getTop() + dTranslateY) * dScale), (int) (bo.getWidth() * dScale),
				(int) (bo.getHeight() * dScale));
		Path pt = new Path(((SwtDisplayServer) _ids).getDevice());
		pt.addArc(r.x, r.y, r.width, r.height, 0, 360);

		try {
			if (flBackground instanceof ColorDefinition) {
				fillPathColor(pt, (ColorDefinition) flBackground);
			} else if (flBackground instanceof Gradient) {
				fillPathGradient(pt, (Gradient) flBackground, r);
			} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
				fillPathImage(pt, (org.eclipse.birt.chart.model.attribute.Image) flBackground);
			}
		} finally {
			pt.dispose();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderer#drawText(org.eclipse.birt.
	 * chart.event.TextRenderEvent)
	 */
	@Override
	public void drawText(TextRenderEvent tre) throws ChartException {
		String fontName = convertFont(tre.getLabel().getCaption().getFont().getName());
		if (fontName != null) {
			tre.getLabel().getCaption().getFont().setName(fontName);
		}

		iv.modifyEvent(tre);
		if (!tre.getLabel().isVisible()) {
			return;
		}

		switch (tre.getAction()) {
		case TextRenderEvent.UNDEFINED:
			throw new ChartException(ChartDeviceSwtActivator.ID, ChartException.RENDERING,
					"SwtRendererImpl.exception.unspecified.text.rendering.action", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));

		case TextRenderEvent.RENDER_SHADOW_AT_LOCATION:
			Location lo = tre.getLocation().copyInstance();
			lo.translate(dTranslateX, dTranslateY);
			lo.scale(dScale);
			_tr.renderShadowAtLocation(this, tre.getTextPosition(), lo, tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_AT_LOCATION:
			lo = tre.getLocation().copyInstance();
			lo.translate(dTranslateX, dTranslateY);
			lo.scale(dScale);

			_tr.renderTextAtLocation(this, tre.getTextPosition(), lo, tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_IN_BLOCK:
			final Bounds bo = goFactory.copyOf(tre.getBlockBounds());
			bo.translate(dTranslateX, dTranslateY);
			bo.scale(dScale);

			_tr.renderTextInBlock(this, bo, tre.getBlockAlignment(), tre.getLabel());
			break;
		}

	}

	/**
	 * Converts an array of high-res co-ordinates into a single dimensional integer
	 * array that represents consecutive X/Y co-ordinates associated with a
	 * polygon's vertices as required in SWT.
	 *
	 * @param la
	 * @return int array
	 */
	static final int[] getCoordinatesAsInts(Location[] la, int iRoundingStyle, double dTranslateX, double dTranslateY,
			double dScale) {
		return convertDoubleToInt(getDoubleCoordinatesAsInts(la, iRoundingStyle, dTranslateX, dTranslateY, dScale));
	}

	/**
	 * Converts an array of high-res co-ordinates into a single dimensional double
	 * array that represents consecutive X/Y co-ordinates associated with a
	 * polygon's vertices as required in SWT.
	 *
	 * @param la
	 * @return double array
	 */
	static final double[] getDoubleCoordinatesAsInts(Location[] la, int iRoundingStyle, double dTranslateX,
			double dTranslateY, double dScale) {
		final int n = la.length * 2;
		final double[] iaXY = new double[n];

		if (iRoundingStyle == CEIL) {
			for (int i = 0; i < n / 2; i++) {
				iaXY[2 * i] = Math.ceil((la[i].getX() + dTranslateX) * dScale);
				iaXY[2 * i + 1] = Math.ceil((la[i].getY() + dTranslateY) * dScale);
			}
		} else if (iRoundingStyle == TRUNCATE) {
			for (int i = 0; i < n / 2; i++) {
				iaXY[2 * i] = ((la[i].getX() + dTranslateX) * dScale);
				iaXY[2 * i + 1] = ((la[i].getY() + dTranslateY) * dScale);
			}
		}

		return iaXY;
	}

	static final float[] convertDoubleToFloat(double[] da) {
		if (da == null) {
			return null;
		}
		final float[] fa = new float[da.length];
		for (int i = 0; i < fa.length; i++) {
			fa[i] = (float) da[i];
		}
		return fa;
	}

	static final int[] convertDoubleToInt(double[] da) {
		if (da == null) {
			return null;
		}
		final int[] fa = new int[da.length];
		for (int i = 0; i < fa.length; i++) {
			fa[i] = (int) da[i];
		}
		return fa;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.
	 * eclipse.birt.chart.event.TransformationEvent)
	 */
	@Override
	public void applyTransformation(TransformationEvent tev) throws ChartException {
		// NOTE: Transformations are accumulated
		switch (tev.getTransform()) {
		case TransformationEvent.TRANSLATE:
			dTranslateX += tev.getTranslateX();
			dTranslateY += tev.getTranslateY();
			break;

		case TransformationEvent.ROTATE:
			dRotateInDegrees += tev.getRotation();
			break;

		// Currently not used
		case TransformationEvent.SCALE:
			dScale *= tev.getScale();
			((SwtDisplayServer) _ids).setScale(dScale);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#start()
	 */
	@Override
	public void before() throws ChartException {
		// Clean previous status.
		cleanUpTriggers();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#end()
	 */
	@Override
	public void after() throws ChartException {
		// USED BY SUBCLASSES IF NEEDED
	}

	private void cleanUpTriggers() {
		for (Iterator<List<RegionAction>> itr = _lhmAllTriggers.values().iterator(); itr.hasNext();) {
			List<RegionAction> ralist = itr.next();

			if (ralist != null) {
				for (Iterator<RegionAction> sitr = ralist.iterator(); sitr.hasNext();) {
					RegionAction ra = sitr.next();
					ra.dispose();
				}
			}
		}

		_lhmAllTriggers.clear();
	}

	/**
	 * Free all allocated system resources.
	 */
	@Override
	public void dispose() {
		cleanUpTriggers();

		if (_iun != null) {
			Object obj = _iun.peerInstance();

			if (obj instanceof Composite) {
				Composite jc = (Composite) obj;

				if (_eh != null) {
					if (!jc.isDisposed()) {
						// We can't promise to remove all the old
						// swtEventHandler
						// due to SWT limitation here, so be sure to just attach
						// the
						// update_notifier only to one renderer.

						jc.removeMouseListener(_eh);
						jc.removeMouseMoveListener(_eh);
						jc.removeMouseTrackListener(_eh);
						jc.removeKeyListener(_eh);
						jc.removeFocusListener(_eh);
					}

					_eh.dispose();
					_eh = null;
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public final void setProperty(final String sProperty, final Object oValue) {
		if (sProperty.equals(IDeviceRenderer.UPDATE_NOTIFIER)) {
			_iun = (IUpdateNotifier) oValue;
			iv.reset();
			iv.setUpdateNotifier(_iun);

			cleanUpTriggers();
			Object obj = _iun.peerInstance();

			if (obj instanceof Composite) {
				Composite jc = (Composite) obj;

				if (_eh != null) {
					// We can't promise to remove all the old swtEventHandler
					// due to SWT limitation here, so be sure to just attach the
					// update_notifier only to one renderer.

					jc.removeMouseListener(_eh);
					jc.removeMouseMoveListener(_eh);
					jc.removeMouseTrackListener(_eh);
					jc.removeKeyListener(_eh);
					jc.removeFocusListener(_eh);

					_eh.dispose();
				}

				_eh = new SwtEventHandler(iv, _lhmAllTriggers, _iun, getULocale());

				jc.addMouseListener(_eh);
				jc.addMouseMoveListener(_eh);
				jc.addMouseTrackListener(_eh);
				jc.addKeyListener(_eh);
				jc.addFocusListener(_eh);
			}
		} else if (sProperty.equals(IDeviceRenderer.GRAPHICS_CONTEXT)) {
			_gc = (GC) oValue;

			if (R31Enhance.isR31Available()) {
				Region rg = new Region();
				_gc.getClipping(rg);

				R31Enhance.setAdvanced(_gc, true, rg);
				R31Enhance.setAntialias(_gc, SWT.ON);
				R31Enhance.setTextAntialias(_gc, SWT.ON);

				rg.dispose();
			}
			_ids.setGraphicsContext(_gc);

			logger.log(ILogger.INFORMATION, Messages.getString("SwtRendererImpl.info.graphics.context", //$NON-NLS-1$
					new Object[] { _gc.getClass().getName(), _gc }, getULocale()));
		} else if (sProperty.equals(IDeviceRenderer.DPI_RESOLUTION)) {
			getDisplayServer().setDpiResolution(((Integer) oValue).intValue());
		} else if (sProperty.equals(IDeviceRenderer.EXPECTED_BOUNDS)) {
			Bounds bo = (Bounds) oValue;
			int x = (int) Math.round(bo.getLeft());
			int y = (int) Math.round(bo.getTop());
			int width = (int) Math.round(bo.getWidth());
			int height = (int) Math.round(bo.getHeight());
			this._gc.setClipping(x, y, width, height);
		}
	}

	/**
	 * Make bounds height/width always positive.
	 *
	 * @param bo
	 * @return
	 */
	protected static final Bounds normalizeBounds(Bounds bo) {
		if (bo.getHeight() < 0) {
			bo.setTop(bo.getTop() + bo.getHeight());
			bo.setHeight(-bo.getHeight());
		}

		if (bo.getWidth() < 0) {
			bo.setLeft(bo.getLeft() + bo.getWidth());
			bo.setWidth(-bo.getWidth());
		}

		return bo;
	}

	@Override
	protected String convertFont(String fontFamily) {
		return FontUtil.getFontFamily(fontFamily);
	}

}
