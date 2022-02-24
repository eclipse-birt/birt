/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.device.g2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.DeviceAdapter;
import org.eclipse.birt.chart.device.FontUtil;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextRenderer;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
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
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.InteractiveRenderer;
import org.eclipse.birt.chart.util.PatternImageUtil;
import org.eclipse.birt.chart.util.PatternImageUtil.ByteColorModel;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * The base class of all renderers which bases on java.awt.Graphics2D.
 */

public class G2dRendererBase extends DeviceAdapter {
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/g2d"); //$NON-NLS-1$

	protected IDisplayServer _ids;
	protected Graphics2D _g2d;
	protected InteractiveRenderer iv;
	protected ITextRenderer _tr = null;

	private final Map<LineAttributes, Stroke> _htLineStyles = new HashMap<LineAttributes, Stroke>();

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

	/**
	 * In SWING, polygons are defined with 'int' co-ordinates. There is no concept
	 * of a Polygon2D. As a result, we downgrade high-res 'double' co-ordinates to
	 * 'int' co-ordinates.
	 * 
	 * @param la
	 * @return array of coordinates
	 */
	public static final int[][] getCoordinatesAsInts(Location[] la) {
		final int n = la.length;
		final int[] iaX = new int[n];
		final int[] iaY = new int[n];

		for (int i = 0; i < n; i++) {
			iaX[i] = (int) la[i].getX();
			iaY[i] = (int) la[i].getY();
		}

		return new int[][] { iaX, iaY };
	}

	public static final double[][] getCoordinates(Location[] la) {
		final int n = la.length;
		final double[] iaX = new double[n];
		final double[] iaY = new double[n];

		for (int i = 0; i < n; i++) {
			iaX[i] = la[i].getX();
			iaY[i] = la[i].getY();
		}

		return new double[][] { iaX, iaY };
	}

	public static Shape getPolygon2D(Location[] loa) {
		Path2D path = new Path2D.Double();
		path.moveTo(loa[0].getX(), loa[0].getY());
		for (int i = 1; i < loa.length; ++i) {
			path.lineTo(loa[i].getX(), loa[i].getY());
		}
		path.closePath();
		return path;
	}

	protected Shape getPolygon(Location loa[]) {
		final int[][] i2a = getCoordinatesAsInts(loa);
		return new Polygon(i2a[0], i2a[1], loa.length);
	}

	/**
	 * 
	 * @param iArcStyle
	 * @return
	 */
	protected static final int toG2dArcType(int iArcStyle) {
		switch (iArcStyle) {
		case ArcRenderEvent.OPEN:
			return Arc2D.OPEN;
		case ArcRenderEvent.CLOSED:
			return Arc2D.CHORD;
		case ArcRenderEvent.SECTOR:
			return Arc2D.PIE;
		}
		return -1;
	}

	/**
	 * Reusable 'strokes' for rendering lines may be obtained from here
	 * 
	 * @param ls
	 * @return stroke
	 */
	public final Stroke getCachedStroke(LineAttributes lia) {
		if (lia == null)
			return null;

		Stroke s = _htLineStyles.get(lia);
		if (s == null) {
			BasicStroke bs = null;
			int thickness = lia.getThickness();
			if (thickness == 0) {
				// Thickness can be zero, but dashed pattern can not be.
				thickness = 1;
			}
			if (lia.getStyle().getValue() == LineStyle.DASHED) {
				float[] faStyle = new float[] { 6 * thickness, 4 * thickness };
				bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, faStyle, 0);
			} else if (lia.getStyle().getValue() == LineStyle.DOTTED) {
				float[] faStyle = new float[] { thickness, 4 * thickness };
				bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, faStyle, 0);
			} else if (lia.getStyle().getValue() == LineStyle.DASH_DOTTED) {
				float[] faStyle = new float[] { 6 * thickness, 4 * thickness, thickness, 4 * thickness };
				bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, faStyle, 0);
			} else if (lia.getStyle().getValue() == LineStyle.SOLID) {
				bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
			}
			if (bs != null) {
				_htLineStyles.put(lia, bs);
			}
			return bs;
		}
		return s;
	}

	private static Set<String> sLocalFontFamilyNamesSet = new HashSet<String>();

	static {
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		sLocalFontFamilyNamesSet = new HashSet<String>(Arrays.asList(fonts));
	}

	private static Map<String, String> sLogicFontNameMap = new HashMap<String, String>();

	static {
		sLogicFontNameMap.put(FontUtil.LOGIC_FONT_FAMILY_SERIF, "Serif"); //$NON-NLS-1$
		sLogicFontNameMap.put(FontUtil.LOGIC_FONT_FAMILY_SANS_SERIF, "SansSerif"); //$NON-NLS-1$
		sLogicFontNameMap.put(FontUtil.LOGIC_FONT_FAMILY_MONOSPACE, "Monospaced"); //$NON-NLS-1$
	}

	@Override
	protected String convertFont(String fontFamily) {
		String localFont = sLogicFontNameMap.get(fontFamily);
		if (localFont == null) {
			localFont = fontFamily;
		}
		if (sLocalFontFamilyNamesSet.contains(localFont)) {
			return localFont.toLowerCase();
		}

		return FontUtil.getFontFamily(fontFamily);
	}

	protected void prepareGraphicsContext() {
		_g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		_g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		getDisplayServer().setGraphicsContext(_g2d);
	}

	@Override
	public void setProperty(String sProperty, Object oValue) {
		if (sProperty.equals(IDeviceRenderer.GRAPHICS_CONTEXT)) {
			_g2d = (Graphics2D) oValue;
			prepareGraphicsContext();
		} else if (sProperty.equals(IDeviceRenderer.DPI_RESOLUTION)) {
			getDisplayServer().setDpiResolution(((Integer) oValue).intValue());
		}
	}

	@Override
	public Object getGraphicsContext() {
		return _g2d;
	}

	@Override
	public IDisplayServer getDisplayServer() {
		return _ids;
	}

	@Override
	public void setClip(ClipRenderEvent cre) {
		final Location[] loa = cre.getVertices();

		if (loa == null) {
			_g2d.setClip(null);
		} else {
			_g2d.setClip(getPolygon(loa));
		}
	}

	@Override
	public void drawImage(ImageRenderEvent ire) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(ire);
		}
		if (ire.getImage() == null || ire.getLocation() == null) {
			return;
		}

		java.awt.Image img = null;

		if (ire.getImage() instanceof EmbeddedImage) {
			try {
				byte[] data = Base64.decodeBase64(((EmbeddedImage) ire.getImage()).getData().getBytes());

				img = createImage(data);
			} catch (Exception ilex) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ilex);
			}
		} else if (ire.getImage().getSource() != ImageSourceType.FILE
				&& ire.getImage().getSource() != ImageSourceType.REPORT) {
			try {
				final String sUrl = ire.getImage().getURL();
				img = (java.awt.Image) _ids.loadImage(SecurityUtil.newURL(sUrl));
			} catch (ChartException ilex) {
				// Ignore the invalid path, and log it only
				logger.log(new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ilex));
			} catch (MalformedURLException muex) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, muex);
			}
		}

		if (img == null) {
			return;
		}

		Location loc = ire.getLocation();
		Position pos = ire.getPosition();
		if (pos == null) {
			pos = Position.INSIDE_LITERAL;
		}

		ImageObserver io = (ImageObserver) _ids.getObserver();

		final boolean bSizeSet = ire.getWidth() * ire.getHeight() > 0;
		int width = bSizeSet ? ire.getWidth() : img.getWidth(io);
		int height = bSizeSet ? ire.getHeight() : img.getHeight(io);
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

		_g2d.drawImage(img, x, y, width, height, io);
	}

	@Override
	public void drawLine(LineRenderEvent lre) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(lre);
		}
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = lre.getLineAttributes();
		if (!validateLineAttributes(lre.getSource(), lia) || lia.getColor() == null) {
			return;
		}

		if (lia.getColor().isSetTransparency() && lia.getColor().getTransparency() == 0) {
			return;
		}

		// DRAW THE LINE
		final Location loStart = lre.getStart();
		final Location loEnd = lre.getEnd();
		// Location may be beyond integer range and need long time to
		// render
		if (!checkValidLocation(loStart) || !checkValidLocation(loEnd)) {
			return;
		}
		Stroke sPrevious = null, sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}

		_g2d.setColor((Color) _ids.getColor(lia.getColor()));
		_g2d.draw(new Line2D.Double(loStart.getX(), loStart.getY(), loEnd.getX(), loEnd.getY()));

		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	protected boolean checkValidLocation(Location lo) {
		// If the location is out of integer range, it needs endless time to
		// render
		return lo.getX() < Integer.MAX_VALUE && lo.getX() > Integer.MIN_VALUE && lo.getY() < Integer.MAX_VALUE
				&& lo.getY() > Integer.MIN_VALUE;
	}

	@Override
	public void drawRectangle(RectangleRenderEvent rre) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(rre);
		}
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = rre.getOutline();
		if (!validateLineAttributes(rre.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), rre.getBackground(), _ids);
		if (cFG == null || cFG.getAlpha() == 0) {
			return;
		}

		// RENDER THE RECTANGLE WITH THE APPROPRIATE LINE STYLE
		final Bounds bo = normalizeBounds(rre.getBounds());
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}
		_g2d.setColor(cFG);
		_g2d.draw(new Rectangle2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth() - 1, bo.getHeight() - 1));
		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	@Override
	public void fillRectangle(RectangleRenderEvent rre) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(rre);
		}
		final Fill flBackground = validateMultipleFill(rre.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Bounds bo = normalizeBounds(rre.getBounds());
		final Rectangle2D.Double r2d = new Rectangle2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());
		if (flBackground instanceof ColorDefinition) {
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor((Color) _ids.getColor(cd));
			_g2d.fill(r2d);
		} else if (flBackground instanceof Gradient) {
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor();
			final ColorDefinition cdEnd = g.getEndColor();
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection();
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			// int iAlpha = g.getTransparency();

			if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"SwingRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
			}

			Point2D.Double p2dStart, p2dEnd;
			if (dAngleInDegrees == 90) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees == -90) {
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees > 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getHeight() - bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else if (dAngleInDegrees < 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
			}
			_g2d.setPaint(
					new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids.getColor(cdEnd)));
			_g2d.fill(r2d);
		} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
			if (flBackground instanceof PatternImage) {
				fillWithPatternImage(new Area(r2d), flBackground);
				return;
			}
			java.awt.Image img = createImageFromModel(flBackground);

			if (img != null) {
				final Shape shClip = _g2d.getClip();
				Area ar2 = new Area(r2d);
				if (shClip != null) {
					Area ar1 = new Area(shClip);
					ar2.intersect(ar1);
				}
				_g2d.setClip(ar2);

				img = scaleImage(img);
				final Size szImage = _ids.getSize(img);

				int iXRepeat = (int) (Math.ceil(r2d.width / szImage.getWidth()));
				int iYRepeat = (int) (Math.ceil(r2d.height / szImage.getHeight()));
				ImageObserver io = (ImageObserver) _ids.getObserver();
				for (int i = 0; i < iXRepeat; i++) {
					for (int j = 0; j < iYRepeat; j++) {
						_g2d.drawImage(img, (int) (r2d.x + i * szImage.getWidth()),
								(int) (r2d.y + j * szImage.getHeight()), io);
					}
				}

				_g2d.setClip(shClip); // RESTORE
			}
		}
	}

	@Override
	public void drawPolygon(PolygonRenderEvent pre) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(pre);
		}
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = pre.getOutline();
		if (!validateLineAttributes(pre.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), pre.getBackground(), _ids);
		if (cFG == null || cFG.getAlpha() == 0) {
			return;
		}

		// DRAW THE POLYGON
		final Location[] la = pre.getPoints();
		Stroke sPrevious = null;
		final Stroke sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}

		_g2d.setColor(cFG);
		_g2d.draw(getPolygon(la));
		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	@Override
	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(pre);
		}
		final Fill flBackground = validateMultipleFill(pre.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Location[] loa = pre.getPoints();
		final int[][] i2a = getCoordinatesAsInts(loa);

		if (flBackground instanceof ColorDefinition) {
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor((Color) _ids.getColor(cd));
			_g2d.fill(getPolygon(loa));
		} else if (flBackground instanceof Gradient) {
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor();
			final ColorDefinition cdEnd = g.getEndColor();
			// final boolean bRadial = g.isCyclic();
			final double dAngleInDegrees = g.getDirection();
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			// final int iAlpha = g.getTransparency();

			final double dMinX = BaseRenderer.getX(loa, IConstants.MIN);
			final double dMaxX = BaseRenderer.getX(loa, IConstants.MAX);
			final double dMinY = BaseRenderer.getY(loa, IConstants.MIN);
			final double dMaxY = BaseRenderer.getY(loa, IConstants.MAX);

			if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"SwingRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
			}

			Point2D.Double p2dStart, p2dEnd;
			if (dAngleInDegrees == 90) {
				p2dStart = new Point2D.Double(dMinX, dMaxY);
				p2dEnd = new Point2D.Double(dMinX, dMinY);
			} else if (dAngleInDegrees == -90) {
				p2dStart = new Point2D.Double(dMinX, dMinY);
				p2dEnd = new Point2D.Double(dMinX, dMaxY);
			} else if (dAngleInDegrees > 0) {
				p2dStart = new Point2D.Double(dMinX, dMaxY);
				p2dEnd = new Point2D.Double(dMaxX, dMaxY - (dMaxX - dMinX) * Math.abs(Math.tan(dAngleInRadians)));
			} else if (dAngleInDegrees < 0) {
				p2dStart = new Point2D.Double(dMinX, dMinY);
				p2dEnd = new Point2D.Double(dMaxX, dMinY + (dMaxX - dMinX) * Math.abs(Math.tan(dAngleInRadians)));
			} else {
				p2dStart = new Point2D.Double(dMinX, dMinY);
				p2dEnd = new Point2D.Double(dMaxX, dMinY);
			}
			_g2d.setPaint(
					new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids.getColor(cdEnd)));
			_g2d.fill(getPolygon(loa));
		} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
			Area ar2 = new Area(new Polygon(i2a[0], i2a[1], loa.length));
			if (flBackground instanceof PatternImage) {
				fillWithPatternImage(ar2, flBackground);
				return;
			}
			java.awt.Image img = createImageFromModel(flBackground);

			if (img != null) {
				final Shape shClip = _g2d.getClip();
				if (shClip != null) {
					Area ar1 = new Area(shClip);
					ar2.intersect(ar1);
				}
				_g2d.setClip(ar2);

				final double dMinX = BaseRenderer.getX(loa, IConstants.MIN);
				final double dMaxX = BaseRenderer.getX(loa, IConstants.MAX);
				final double dMinY = BaseRenderer.getY(loa, IConstants.MIN);
				final double dMaxY = BaseRenderer.getY(loa, IConstants.MAX);
				final Size szImage = _ids.getSize(img);

				final int iXRepeat = (int) (Math.ceil((dMaxX - dMinX) / szImage.getWidth()));
				final int iYRepeat = (int) (Math.ceil((dMaxY - dMinY) / szImage.getHeight()));
				final ImageObserver io = (ImageObserver) _ids.getObserver();
				for (int i = 0; i < iXRepeat; i++) {
					for (int j = 0; j < iYRepeat; j++) {
						_g2d.drawImage(img, (int) (dMinX + i * szImage.getWidth()),
								(int) (dMinY + j * szImage.getHeight()), io);
					}
				}

				_g2d.setClip(shClip); // RESTORE
			}
		}
	}

	@Override
	public void drawArc(ArcRenderEvent are) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(are);
		}

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline();
		if (!validateLineAttributes(are.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
		if (cFG == null || cFG.getAlpha() == 0) {
			return;
		}

		// DRAW THE ARC
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}
		_g2d.setColor(cFG);

		if ((are.getInnerRadius() >= 0 && are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
				|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
			Bounds rctOuter = getOuterRectangle(are);
			Bounds rctInner = getInnerRectangle(are);

			Shape outerArc = new Arc2D.Double(rctOuter.getLeft(), rctOuter.getTop(), rctOuter.getWidth(),
					rctOuter.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.OPEN);
			Shape innerArc = new Arc2D.Double(rctInner.getLeft(), rctInner.getTop(), rctInner.getWidth(),
					rctInner.getHeight(), are.getStartAngle() + are.getAngleExtent(), -are.getAngleExtent(),
					Arc2D.OPEN);

			double startAngle = Math.toRadians(-are.getStartAngle());
			double stopAngle = Math.toRadians(-are.getStartAngle() - are.getAngleExtent());

			double xsOuter = (rctOuter.getLeft() + (Math.cos(startAngle) * 0.5 + 0.5) * rctOuter.getWidth());
			double ysOuter = (rctOuter.getTop() + (Math.sin(startAngle) * 0.5 + 0.5) * rctOuter.getHeight());

			double xeInner = (rctInner.getLeft() + (Math.cos(stopAngle) * 0.5 + 0.5) * rctInner.getWidth());
			double yeInner = (rctInner.getTop() + (Math.sin(stopAngle) * 0.5 + 0.5) * rctInner.getHeight());

			GeneralPath gp = new GeneralPath();
			gp.append(outerArc, false);
			gp.lineTo((float) xeInner, (float) yeInner);
			gp.append(innerArc, false);
			gp.lineTo((float) xsOuter, (float) ysOuter);

			Area area = new Area(gp);
			Shape prevClip = _g2d.getClip();
			Area ar2 = new Area(area);
			if (prevClip != null) {
				Area ar1 = new Area(prevClip);
				ar2.intersect(ar1);
			}
			_g2d.setClip(ar2);
			_g2d.draw(area);
			_g2d.setClip(prevClip);

		} else {
			_g2d.draw(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
					are.getHeight(), are.getStartAngle(), are.getAngleExtent(), toG2dArcType(are.getStyle())));
		}

		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	protected Bounds getOuterRectangle(ArcRenderEvent are) {
		Bounds rctOuter;
		if (are.getOuterRadius() > 0) {
			double radio = are.getHeight() / are.getWidth();
			rctOuter = BoundsImpl.create(are.getTopLeft().getX() + (are.getWidth() - 2 * are.getOuterRadius()) / 2,
					are.getTopLeft().getY() + (are.getHeight() - 2 * are.getOuterRadius() * radio) / 2,
					2 * are.getOuterRadius(), 2 * are.getOuterRadius() * radio);
		} else {
			rctOuter = BoundsImpl.create(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
					are.getHeight());
		}
		return rctOuter;
	}

	protected Bounds getInnerRectangle(ArcRenderEvent are) {
		Bounds rctInner;
		if (are.getInnerRadius() > 0) {
			double radio = are.getHeight() / are.getWidth();
			rctInner = BoundsImpl.create(are.getTopLeft().getX() + (are.getWidth() - 2 * are.getInnerRadius()) / 2,
					are.getTopLeft().getY() + (are.getHeight() - 2 * are.getInnerRadius() * radio) / 2,
					2 * are.getInnerRadius(), 2 * are.getInnerRadius() * radio);
		} else {
			rctInner = BoundsImpl.create(are.getTopLeft().getX() + (are.getWidth() - 2 * are.getInnerRadius()) / 2,
					are.getTopLeft().getY() + (are.getHeight() - 2 * are.getInnerRadius()) / 2,
					2 * are.getInnerRadius(), 2 * are.getInnerRadius());
		}
		return rctInner;
	}

	@Override
	public void fillArc(ArcRenderEvent are) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(are);
		}

		final Fill flBackground = validateMultipleFill(are.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		if (flBackground instanceof ColorDefinition) {
			final ColorDefinition cl = (ColorDefinition) flBackground;
			final Color clrPrevious = _g2d.getColor();
			final Color currentColor = (Color) _ids.getColor(cl);
			_g2d.setColor(currentColor);

			if ((are.getInnerRadius() >= 0 && are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
					|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
				Bounds rctOuter = getOuterRectangle(are);
				Bounds rctInner = getInnerRectangle(are);

				Shape outerArc = new Arc2D.Double(rctOuter.getLeft(), rctOuter.getTop(), rctOuter.getWidth(),
						rctOuter.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Shape innerArc = new Arc2D.Double(rctInner.getLeft(), rctInner.getTop(), rctInner.getWidth(),
						rctInner.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Area fArea = new Area(outerArc);
				fArea.exclusiveOr(new Area(innerArc));

				Shape prevClip = _g2d.getClip();
				Area ar2 = new Area(fArea);
				if (prevClip != null) {
					Area ar1 = new Area(prevClip);
					ar2.intersect(ar1);
				}
				_g2d.setClip(ar2);
				_g2d.fill(fArea);
				_g2d.setClip(prevClip);
			} else {
				_g2d.fill(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
						are.getHeight(), are.getStartAngle(), are.getAngleExtent(), toG2dArcType(are.getStyle())));
			}

			_g2d.setColor(clrPrevious); // RESTORE
		} else if (flBackground instanceof Gradient) {
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor();
			final ColorDefinition cdEnd = g.getEndColor();
			double dAngleInDegrees = g.getDirection();
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			Bounds bo = are.getBounds();

			if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"SwingRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
			}

			Point2D.Double p2dStart, p2dEnd;
			if (dAngleInDegrees == 90) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees == -90) {
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees > 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getHeight() - bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else if (dAngleInDegrees < 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
			}

			final Paint pPrevious = _g2d.getPaint();
			_g2d.setPaint(
					new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids.getColor(cdEnd)));

			if ((are.getInnerRadius() >= 0 && are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
					|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
				Bounds rctOuter = getOuterRectangle(are);
				Bounds rctInner = getInnerRectangle(are);

				Shape outerArc = new Arc2D.Double(rctOuter.getLeft(), rctOuter.getTop(), rctOuter.getWidth(),
						rctOuter.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Shape innerArc = new Arc2D.Double(rctInner.getLeft(), rctInner.getTop(), rctInner.getWidth(),
						rctInner.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Area fArea = new Area(outerArc);
				fArea.exclusiveOr(new Area(innerArc));

				Shape prevClip = _g2d.getClip();
				Area ar2 = new Area(fArea);
				if (prevClip != null) {
					Area ar1 = new Area(prevClip);
					ar2.intersect(ar1);
				}
				_g2d.setClip(ar2);
				_g2d.fill(fArea);
				_g2d.setClip(prevClip);
			} else {
				_g2d.fill(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
						are.getHeight(), are.getStartAngle(), are.getAngleExtent(), toG2dArcType(are.getStyle())));
			}

			_g2d.setPaint(pPrevious); // RESTORE
		} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
			final Bounds bo = are.getBounds();
			final Rectangle2D.Double r2d = new Rectangle2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(),
					bo.getHeight());

			Shape shPreviousClip = _g2d.getClip();
			Area ar = null;

			if ((are.getInnerRadius() >= 0 && are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius())
					|| (are.getInnerRadius() > 0 && are.getOuterRadius() <= 0)) {
				Bounds rctOuter = getOuterRectangle(are);
				Bounds rctInner = getInnerRectangle(are);

				Shape outerArc = new Arc2D.Double(rctOuter.getLeft(), rctOuter.getTop(), rctOuter.getWidth(),
						rctOuter.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Shape innerArc = new Arc2D.Double(rctInner.getLeft(), rctInner.getTop(), rctInner.getWidth(),
						rctInner.getHeight(), are.getStartAngle(), are.getAngleExtent(), Arc2D.PIE);

				Area fArea = new Area(outerArc);
				fArea.exclusiveOr(new Area(innerArc));

				if (shPreviousClip != null) {
					Area ar1 = new Area(shPreviousClip);
					fArea.intersect(ar1);
				}
				// _g2d.setClip( fArea );
				ar = fArea;
			} else {
				// SETUP THE CLIPPING AREA
				final Shape shArc = new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(),
						are.getHeight(), are.getStartAngle(), are.getAngleExtent(), toG2dArcType(are.getStyle()));

				Area ar2 = new Area(shArc);
				if (shPreviousClip != null) {
					Area ar1 = new Area(shPreviousClip);
					ar2.intersect(ar1);
				}
				// _g2d.setClip( ar2 );
				ar = ar2;
			}

			if (flBackground instanceof PatternImage) {
				fillWithPatternImage(new Area(ar), flBackground);
				return;
			}

			_g2d.setClip(ar);

			// LOAD THE IMAGE
			java.awt.Image img = createImageFromModel(flBackground);
			if (img != null) {
				// REPLICATE THE IMAGE AS NEEDED
				final Size szImage = _ids.getSize(img);
				int iXRepeat = (int) (Math.ceil(r2d.width / szImage.getWidth()));
				int iYRepeat = (int) (Math.ceil(r2d.height / szImage.getHeight()));
				ImageObserver io = (ImageObserver) _ids.getObserver();
				for (int i = 0; i < iXRepeat; i++) {
					for (int j = 0; j < iYRepeat; j++) {
						_g2d.drawImage(img, (int) (r2d.x + i * szImage.getWidth()),
								(int) (r2d.y + j * szImage.getHeight()), io);
					}
				}
			}

			_g2d.setClip(shPreviousClip); // RESTORE
		}
	}

	protected Image createImage(byte[] data) {
		try {
			return ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException e) {
			logger.log(e);
		}
		return null;
	}

	protected java.awt.Image createImageFromModel(Fill imageModel) throws ChartException {

		java.awt.Image img = null;
		if (imageModel instanceof EmbeddedImage) {
			try {
				byte[] data = Base64.decodeBase64(((EmbeddedImage) imageModel).getData().getBytes());

				img = createImage(data);
			} catch (Exception ilex) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ilex);
			}
		} else if (imageModel instanceof PatternImage) {
			PatternImage pi = (PatternImage) imageModel;
			byte[] data = PatternImageUtil.createImageData(pi, ByteColorModel.RGBA);
			BufferedImage bimg = new BufferedImage(8, 8, BufferedImage.TYPE_4BYTE_ABGR);
			img = bimg;
			WritableRaster raster = bimg.getRaster();
			raster.setDataElements(0, 0, 8, 8, data);
			bimg.flush();
		} else if (imageModel instanceof org.eclipse.birt.chart.model.attribute.Image) {
			if (((org.eclipse.birt.chart.model.attribute.Image) imageModel).getSource() == ImageSourceType.STATIC) {
				try {
					final String sUrl = ((org.eclipse.birt.chart.model.attribute.Image) imageModel).getURL();
					img = (java.awt.Image) _ids.loadImage(SecurityUtil.newURL(sUrl));
				} catch (ChartException ilex) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, ilex);
				} catch (MalformedURLException muex) {
					throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING, muex);
				}
			}
		}

		return img;

	}

	/**
	 * Scales image according to output DPI. If 96, do not need to scale
	 * 
	 * @param img
	 * @return
	 */
	private java.awt.Image scaleImage(java.awt.Image img) {
		if (this._ids.getDpiResolution() == 96) {
			// Do not scale in normal dpi
			return img;
		}
		double scale = this._ids.getDpiResolution() / 96d;
		int newWidth = (int) (img.getWidth((ImageObserver) getDisplayServer().getObserver()) * scale);
		int newHeight = (int) (img.getHeight((ImageObserver) getDisplayServer().getObserver()) * scale);
		return img.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
	}

	protected BufferedImage convertPatternImage(java.awt.Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		return null;
	}

	protected void fillWithPatternImage(Shape shape, Fill fill) throws ChartException {
		java.awt.Image img = createImageFromModel(fill);
		BufferedImage bimg = convertPatternImage(img);

		if (bimg != null) {
			_g2d.setPaint(new TexturePaint(bimg, new Rectangle(0, 0, bimg.getWidth(), bimg.getHeight())));
			_g2d.fill(shape);
		}
	}

	@Override
	public void drawArea(AreaRenderEvent are) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(are);
		}

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline();
		if (!validateLineAttributes(are.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
		// IF UNDEFINED OR TOTALLY TRANSPARENT, EXIT
		if (cFG == null || cFG.getAlpha() == 0) {
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final GeneralPath gp = new GeneralPath();
		PrimitiveRenderEvent pre;
		for (int i = 0; i < are.getElementCount(); i++) {
			pre = are.getElement(i);
			if (pre instanceof ArcRenderEvent) {
				final ArcRenderEvent acre = (ArcRenderEvent) pre;
				final Arc2D.Double a2d = new Arc2D.Double(acre.getTopLeft().getX(), acre.getTopLeft().getY(),
						acre.getWidth(), acre.getHeight(), acre.getStartAngle(), acre.getAngleExtent(),
						toG2dArcType(acre.getStyle()));
				gp.append(a2d, true);
			} else if (pre instanceof LineRenderEvent) {
				final LineRenderEvent lre = (LineRenderEvent) pre;
				final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(),
						lre.getEnd().getX(), lre.getEnd().getY());
				gp.append(l2d, true);
			}
		}

		// DRAW THE GENERAL PATH
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}

		_g2d.setColor(cFG);
		_g2d.draw(gp);

		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	@Override
	public void fillArea(AreaRenderEvent are) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(are);
		}

		final Fill flBackground = validateMultipleFill(are.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		// SETUP SWING DATA STRUCTURES
		final GeneralPath gp = new GeneralPath();
		PrimitiveRenderEvent pre;
		for (int i = 0; i < are.getElementCount(); i++) {
			pre = are.getElement(i);
			if (pre instanceof ArcRenderEvent) {
				final ArcRenderEvent acre = (ArcRenderEvent) pre;
				final Arc2D.Double a2d = new Arc2D.Double(acre.getTopLeft().getX(), acre.getTopLeft().getY(),
						acre.getWidth(), acre.getHeight(), acre.getStartAngle(), acre.getAngleExtent(),
						toG2dArcType(acre.getStyle()));
				gp.append(a2d, true);
			} else if (pre instanceof LineRenderEvent) {
				final LineRenderEvent lre = (LineRenderEvent) pre;
				final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(),
						lre.getEnd().getX(), lre.getEnd().getY());
				gp.append(l2d, true);
			}
		}

		// BEGIN FILLING
		if (flBackground instanceof ColorDefinition) {
			_g2d.setColor((Color) _ids.getColor((ColorDefinition) flBackground));
		} else if (flBackground instanceof Gradient) {
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor();
			final ColorDefinition cdEnd = g.getEndColor();
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection();
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
			// int iAlpha = g.getTransparency();
			Bounds bo = are.getBounds();

			/*
			 * if (bCyclic) { }
			 */

			if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"SwingRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
			}

			Point2D.Double p2dStart, p2dEnd;
			if (dAngleInDegrees == 90) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees == -90) {
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees > 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getHeight() - bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else if (dAngleInDegrees < 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
			}
			_g2d.setPaint(
					new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids.getColor(cdEnd)));
		} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
			// TODO TBD
		}
		_g2d.fill(gp);
	}

	@Override
	public void drawOval(OvalRenderEvent ore) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(ore);
		}

		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = ore.getOutline();
		if (!validateLineAttributes(ore.getSource(), lia)) {
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor(lia.getColor(), ore.getBackground(), _ids);
		if (cFG == null || cFG.getAlpha() == 0) {
			return;
		}

		// RENDER THE ELLIPSE WITH THE APPROPRIATE LINE STYLE
		final Bounds bo = ore.getBounds();
		final Ellipse2D.Double e2d = new Ellipse2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());

		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke(lia);
		if (sCurrent != null) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke();
			_g2d.setStroke(sCurrent);
		}

		_g2d.setColor(cFG);
		_g2d.draw(e2d);

		if (sPrevious != null) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke(sPrevious);
		}
	}

	@Override
	public void fillOval(OvalRenderEvent ore) throws ChartException {
		if (iv != null) {
			iv.modifyEvent(ore);
		}

		final Fill flBackground = validateMultipleFill(ore.getBackground());

		if (isFullTransparent(flBackground)) {
			return;
		}

		final Bounds bo = ore.getBounds();
		final Ellipse2D.Double e2d = new Ellipse2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());
		if (flBackground instanceof ColorDefinition) {
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor((Color) _ids.getColor(cd));
			_g2d.fill(e2d);
		} else if (flBackground instanceof Gradient) {
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor();
			final ColorDefinition cdEnd = g.getEndColor();
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection();
			final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);

			if (dAngleInDegrees < -90 || dAngleInDegrees > 90) {
				throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
						"SwingRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[] { new Double(dAngleInDegrees) }, Messages.getResourceBundle(getULocale()));
			}

			Point2D.Double p2dStart, p2dEnd;
			if (dAngleInDegrees == 90) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees == -90) {
				p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
			} else if (dAngleInDegrees > 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getHeight() - bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else if (dAngleInDegrees < 0) {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(),
						bo.getTop() + bo.getWidth() * Math.abs(Math.tan(dAngleInRadians)));
			} else {
				p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
				p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
			}
			_g2d.setPaint(
					new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids.getColor(cdEnd)));
			_g2d.fill(e2d);
		} else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image) {
			Area ar2 = new Area(e2d);
			if (flBackground instanceof PatternImage) {
				fillWithPatternImage(ar2, flBackground);
				return;
			}
			java.awt.Image img = createImageFromModel(flBackground);

			if (img != null) {

				final Shape shClip = _g2d.getClip();
				if (shClip != null) {
					Area ar1 = new Area(shClip);
					ar2.intersect(ar1);
				}
				_g2d.setClip(ar2);

				final Size szImage = _ids.getSize(img);

				int iXRepeat = (int) (Math.ceil(e2d.width / szImage.getWidth()));
				int iYRepeat = (int) (Math.ceil(e2d.height / szImage.getHeight()));
				ImageObserver io = (ImageObserver) _ids.getObserver();
				for (int i = 0; i < iXRepeat; i++) {
					for (int j = 0; j < iYRepeat; j++) {
						_g2d.drawImage(img, (int) (e2d.x + i * szImage.getWidth()),
								(int) (e2d.y + j * szImage.getHeight()), io);
					}
				}

				_g2d.setClip(shClip); // RESTORE
			}
		}
	}

	@Override
	public void drawText(TextRenderEvent tre) throws ChartException {
		String fontName = convertFont(tre.getLabel().getCaption().getFont().getName());
		if (fontName != null) {
			tre.getLabel().getCaption().getFont().setName(fontName);
		}

		if (iv != null) {
			iv.modifyEvent(tre);
		}

		if (!tre.getLabel().isVisible())
			return;

		switch (tre.getAction()) {
		case TextRenderEvent.UNDEFINED:
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"SwingRendererImpl.exception.missing.text.render.action", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));

		case TextRenderEvent.RENDER_SHADOW_AT_LOCATION:
			_tr.renderShadowAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_AT_LOCATION:
			_tr.renderTextAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_IN_BLOCK:
			_tr.renderTextInBlock(this, tre.getBlockBounds(), tre.getBlockAlignment(), tre.getLabel());
			break;
		}
	}

	@Override
	public void applyTransformation(TransformationEvent tev) throws ChartException {
		switch (tev.getTransform()) {
		case TransformationEvent.TRANSLATE:
			_g2d.translate(tev.getTranslateX(), tev.getTranslateY());
			break;

		case TransformationEvent.ROTATE:
			_g2d.rotate((tev.getRotation() * Math.PI) / 180d);
			break;

		case TransformationEvent.SCALE:
			_g2d.scale(tev.getScale(), tev.getScale());
			break;
		}
	}

	@Override
	public void dispose() {
		if (_ids != null) {
			_ids.dispose();
			_ids = null;
		}

		if (_g2d != null) {
			_g2d.dispose();
			_g2d = null;
		}
	}

}
