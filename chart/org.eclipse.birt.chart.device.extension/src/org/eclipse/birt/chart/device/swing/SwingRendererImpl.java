/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JComponent;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.DeviceAdapter;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
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
import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.util.PluginSettings;

/**
 * Provides a reference implementation of a SWING device renderer. It translates chart primitives into
 * standard J2SDK AWT/SWING rendering primitives.
 */
public class SwingRendererImpl extends DeviceAdapter
{

    /**
     * KEY = TRIGGER_CONDITION VAL = COLLECTION OF SHAPE-ACTION INSTANCES
     */
    private final LinkedHashMap _lhmAllTriggers = new LinkedHashMap();

    /**
     *  
     */
    private final Hashtable _htLineStyles = new Hashtable();

    /**
     *  
     */
    protected Graphics2D _g2d;

    /**
     *  
     */
    private FontRenderContext _frc = null;

    /**
     *  
     */
    protected IDisplayServer _ids;

    /**
     *  
     */
    private IUpdateNotifier _iun = null;

    /**
     *  
     */
    private SwingEventHandler _eh = null;

    /**
     *  
     */
    public SwingRendererImpl()
    {
        final PluginSettings ps = PluginSettings.instance();
        try
        {
            _ids = ps.getDisplayServer("ds.SWING"); //$NON-NLS-1$
        }
        catch (PluginException pex )
        {
            DefaultLoggerImpl.instance().log(pex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String sProperty, Object oValue)
    {
        if (sProperty.equals(IDeviceRenderer.UPDATE_NOTIFIER))
        {
            _iun = (IUpdateNotifier) oValue;
            JComponent jc = (JComponent) _iun.peerInstance();
            _lhmAllTriggers.clear();

            MouseListener[] mla = jc.getMouseListeners();
            for (int i = 0; i < mla.length; i++)
            {
                if (mla[i] instanceof SwingEventHandler)
                {
                    jc.removeMouseListener(mla[i]);
                }
            }

            MouseMotionListener[] mmla = jc.getMouseMotionListeners();
            for (int i = 0; i < mmla.length; i++)
            {
                if (mmla[i] instanceof SwingEventHandler)
                {
                    jc.removeMouseMotionListener(mmla[i]);
                }
            }

            _eh = new SwingEventHandler(_lhmAllTriggers, _iun, getLocale());
            jc.addMouseListener(_eh);
            jc.addMouseMotionListener(_eh);
        }
        else if (sProperty.equals(IDeviceRenderer.GRAPHICS_CONTEXT))
        {
            _g2d = (Graphics2D) oValue;
            _g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            _g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            _g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            _frc = new FontRenderContext(new AffineTransform(), true, false);
            DefaultLoggerImpl.instance().log(
                ILogger.INFORMATION, 
                Messages.getString(
                    "info.using.graphics.context", //$NON-NLS-1$
                    new Object[] { _g2d },
                    getLocale()
                )
            ); // i18n_CONCATENATIONS_REMOVED 
        }
    }

    /**
     * 
     * @param g2d
     */
    public final Object getGraphicsContext()
    {
        return _g2d;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#setClip(org.eclipse.birt.chart.output.ClipRenderEvent)
     */
    public void setClip(ClipRenderEvent pre)
    {
        final Location[] loa = pre.getVertices();
        final int[][] i2a = getCoordinatesAsInts(loa);
        _g2d.setClip(new Polygon(i2a[0], i2a[1], loa.length));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawImage(org.eclipse.birt.chart.output.ImageRenderEvent)
     */
    public void drawImage(ImageRenderEvent pre)
    {
        // TODO: Provide an implementation here
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawLine(org.eclipse.birt.chart.output.LineRenderEvent)
     */
    public void drawLine(LineRenderEvent lre) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = lre.getLineAttributes();
        if (!validateLineAttributes(lre.getSource(), lia) || lia.getColor() == null)
        {
            return;
        }

        // DRAW THE LINE
        final Location loStart = lre.getStart();
        final Location loEnd = lre.getEnd();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
     */
    public void drawRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = rre.getOutline();
        if (!validateLineAttributes(rre.getSource(), lia))
        {
            return;
        }

        // SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
        final Color cFG = (Color) validateEdgeColor(lia.getColor(), rre.getBackground(), _ids);
        if (cFG == null)
        {
            return;
        }

        // RENDER THE RECTANGLE WITH THE APPROPRIATE LINE STYLE
        final Bounds bo = rre.getBounds();
        Stroke sPrevious = null;
        Stroke sCurrent = getCachedStroke(lia);
        if (sCurrent != null) // SOME STROKE DEFINED?
        {
            sPrevious = _g2d.getStroke();
            _g2d.setStroke(sCurrent);
        }
        _g2d.setColor(cFG);
        _g2d.draw(
            new Rectangle2D.Double(
                bo.getLeft(), bo.getTop(), bo.getWidth() - 1, bo.getHeight() - 1
            )
        );
        if (sPrevious != null) // RESTORE PREVIOUS STROKE
        {
            _g2d.setStroke(sPrevious);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillRectangle(org.eclipse.birt.chart.output.RectangleRenderEvent)
     */
    public void fillRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        final Fill flBackground = rre.getBackground();
        final Bounds bo = rre.getBounds();
        final Rectangle2D.Double r2d = new Rectangle2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());
        if (flBackground instanceof ColorDefinition)
        {
            final ColorDefinition cd = (ColorDefinition) flBackground;
            _g2d.setColor((Color) _ids.getColor(cd));
            _g2d.fill(r2d);
        }
        else if (flBackground instanceof Gradient)
        {
            final Gradient g = (Gradient) flBackground;
            final ColorDefinition cdStart = (ColorDefinition) g.getStartColor();
            final ColorDefinition cdEnd = (ColorDefinition) g.getEndColor();
            //boolean bCyclic = g.isCyclic();
            double dAngleInDegrees = g.getDirection();
            final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
            //int iAlpha = g.getTransparency();

            /*if (bCyclic)
            {
            }*/

            if (dAngleInDegrees < -90 || dAngleInDegrees > 90)
            {
                throw new RenderingException(
                    "exception.gradient.angle",//$NON-NLS-1$
                    new Object[] { new Double(dAngleInDegrees) },
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED 
            }

            Point2D.Double p2dStart, p2dEnd;
            if (dAngleInDegrees == 90)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees == -90)
            {
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees > 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight() - bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else if (dAngleInDegrees < 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
            }
            _g2d.setPaint(new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids
                .getColor(cdEnd)));
            _g2d.fill(r2d);
        }
        else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image)
        {
            final String sUrl = ((org.eclipse.birt.chart.model.attribute.Image) flBackground).getURL();
            java.awt.Image img = null;
            try
            {
                img = (java.awt.Image) _ids.loadImage(new URL(sUrl));
            }
            catch (ImageLoadingException ilex )
            {
                throw new RenderingException(ilex);
            }
            catch (MalformedURLException muex )
            {
                throw new RenderingException(muex);
            }

            final Shape shClip = _g2d.getClip();
            _g2d.setClip(r2d);

            final Size szImage = _ids.getSize(img);

            int iXRepeat = (int) (Math.ceil(r2d.width / szImage.getWidth()));
            int iYRepeat = (int) (Math.ceil(r2d.height / szImage.getHeight()));
            ImageObserver io = (ImageObserver) _ids.getObserver();
            for (int i = 0; i < iXRepeat; i++)
            {
                for (int j = 0; j < iYRepeat; j++)
                {
                    _g2d.drawImage(img, (int) (r2d.x + i * szImage.getWidth()),
                        (int) (r2d.y + j * szImage.getHeight()), io);
                }
            }

            //img(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
            _g2d.setClip(shClip); // RESTORE
        }
    }

    /**
     * In SWING, polygons are defined with 'int' co-ordinates. There is no concept of a Polygon2D. As a result, we
     * downgrade high-res 'double' co-ordinates to 'int' co-ordinates.
     * 
     * @param la
     * @return
     */
    static final int[][] getCoordinatesAsInts(Location[] la)
    {
        final int n = la.length;
        final int[] iaX = new int[n];
        final int[] iaY = new int[n];

        for (int i = 0; i < n; i++)
        {
            iaX[i] = (int) la[i].getX();
            iaY[i] = (int) la[i].getY();
        }

        return new int[][]
        {
            iaX, iaY
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawPolygon(org.eclipse.birt.chart.output.PolygonRenderEvent)
     */
    public void drawPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = pre.getOutline();
        if (!validateLineAttributes(pre.getSource(), lia))
        {
            return;
        }

        // SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
        final Color cFG = (Color) validateEdgeColor(lia.getColor(), pre.getBackground(), _ids);
        if (cFG == null) // IF UNDEFINED, EXIT
        {
            return;
        }

        // DRAW THE POLYGON
        final Location[] la = pre.getPoints();
        final int[][] i2a = getCoordinatesAsInts(la);
        Stroke sPrevious = null;
        final Stroke sCurrent = getCachedStroke(lia);
        if (sCurrent != null) // SOME STROKE DEFINED?
        {
            sPrevious = _g2d.getStroke();
            _g2d.setStroke(sCurrent);
        }

        _g2d.setColor(cFG);
        _g2d.draw(new Polygon(i2a[0], i2a[1], la.length));

        if (sPrevious != null) // RESTORE PREVIOUS STROKE
        {
            _g2d.setStroke(sPrevious);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillPolygon(org.eclipse.birt.chart.output.PolygonRenderEvent)
     */
    public void fillPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        final Fill flBackground = pre.getBackground();
        final Location[] loa = pre.getPoints();
        final int[][] i2a = getCoordinatesAsInts(loa);

        if (flBackground instanceof ColorDefinition)
        {
            final ColorDefinition cd = (ColorDefinition) flBackground;
            _g2d.setColor((Color) _ids.getColor(cd));
            _g2d.fill(new Polygon(i2a[0], i2a[1], loa.length));
        }
        else if (flBackground instanceof Gradient)
        {
            final Gradient g = (Gradient) flBackground;
            final ColorDefinition cdStart = (ColorDefinition) g.getStartColor();
            final ColorDefinition cdEnd = (ColorDefinition) g.getEndColor();
            //final boolean bRadial = g.isCyclic();
            final double dAngleInDegrees = g.getDirection();
            final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
            //final int iAlpha = g.getTransparency();

            final double dMinX = BaseRenderer.getX(loa, IConstants.MIN);
            final double dMaxX = BaseRenderer.getX(loa, IConstants.MAX);
            final double dMinY = BaseRenderer.getY(loa, IConstants.MIN);
            final double dMaxY = BaseRenderer.getY(loa, IConstants.MAX);

            /*if (bRadial)
            {
            }*/

            if (dAngleInDegrees < -90 || dAngleInDegrees > 90)
            {
                throw new RenderingException(
                    "exception.gradient.angle",//$NON-NLS-1$
                    new Object[] { new Double(dAngleInDegrees) },
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED 
            }

            Point2D.Double p2dStart, p2dEnd;
            if (dAngleInDegrees == 90)
            {
                p2dStart = new Point2D.Double(dMinX, dMaxY);
                p2dEnd = new Point2D.Double(dMinX, dMinY);
            }
            else if (dAngleInDegrees == -90)
            {
                p2dStart = new Point2D.Double(dMinX, dMinY);
                p2dEnd = new Point2D.Double(dMinX, dMaxY);
            }
            else if (dAngleInDegrees > 0)
            {
                p2dStart = new Point2D.Double(dMinX, dMaxY);
                p2dEnd = new Point2D.Double(dMaxX, dMaxY - (dMaxX - dMinX) * Math.abs(Math.tan(dAngleInRadians)));
            }
            else if (dAngleInDegrees < 0)
            {
                p2dStart = new Point2D.Double(dMinX, dMinY);
                p2dEnd = new Point2D.Double(dMaxX, dMinY + (dMaxX - dMinX) * Math.abs(Math.tan(dAngleInRadians)));
            }
            else
            {
                p2dStart = new Point2D.Double(dMinX, dMinY);
                p2dEnd = new Point2D.Double(dMaxX, dMinY);
            }
            _g2d.setPaint(new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids
                .getColor(cdEnd)));
            _g2d.fill(new Polygon(i2a[0], i2a[1], loa.length));
        }
        else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image)
        {
            final String sUrl = ((org.eclipse.birt.chart.model.attribute.Image) flBackground).getURL();
            java.awt.Image img = null;
            try
            {
                img = (java.awt.Image) _ids.loadImage(new URL(sUrl));
            }
            catch (ImageLoadingException ilex )
            {
                throw new RenderingException(ilex);
            }
            catch (MalformedURLException muex )
            {
                throw new RenderingException(muex);
            }
            final Shape shClip = _g2d.getClip();
            _g2d.setClip(new Polygon(i2a[0], i2a[1], loa.length));

            final double dMinX = BaseRenderer.getX(loa, IConstants.MIN);
            final double dMaxX = BaseRenderer.getX(loa, IConstants.MAX);
            final double dMinY = BaseRenderer.getY(loa, IConstants.MIN);
            final double dMaxY = BaseRenderer.getY(loa, IConstants.MAX);
            final Size szImage = _ids.getSize(img);

            final int iXRepeat = (int) (Math.ceil((dMaxX - dMinX) / szImage.getWidth()));
            final int iYRepeat = (int) (Math.ceil((dMaxY - dMinY) / szImage.getHeight()));
            final ImageObserver io = (ImageObserver) _ids.getObserver();
            for (int i = 0; i < iXRepeat; i++)
            {
                for (int j = 0; j < iYRepeat; j++)
                {
                    _g2d.drawImage(img, (int) (dMinX + i * szImage.getWidth()),
                        (int) (dMinY + j * szImage.getHeight()), io);
                }
            }

            //img(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
            _g2d.setClip(shClip); // RESTORE
        }

        /*if (pre.iObjIndex > 0)
        {
            try
            {
                Bounds bo = pre.getBounds();
                _g2d.setColor(Color.red);
                _g2d.setFont(new Font("Arial", Font.BOLD, 14));
                _g2d.drawString("{0}" + pre.iObjIndex, (int) (bo.getLeft() + bo.getWidth() / 2), (int) (bo.getTop() + bo // i18n_CONCATENATIONS_REMOVED
                    .getHeight() / 2));
            }
            catch (Exception ex )
            {
                ex.printStackTrace();
            }
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawArc(org.eclipse.birt.chart.output.ArcRenderEvent)
     */
    public void drawArc(ArcRenderEvent are) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = are.getOutline();
        if (!validateLineAttributes(are.getSource(), lia))
        {
            return;
        }

        // SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
        final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
        if (cFG == null)
        {
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
        _g2d.draw(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(), are.getHeight(),
            are.getStartAngle(), are.getAngleExtent(), toSwingArcType(are.getStyle())));

        if (sPrevious != null) // RESTORE PREVIOUS STROKE
        {
            _g2d.setStroke(sPrevious);
        }
    }

    /**
     * 
     * @param iArcStyle
     * @return
     */
    private static final int toSwingArcType(int iArcStyle)
    {
        switch (iArcStyle)
        {
            case ArcRenderEvent.OPEN:
                return Arc2D.OPEN;
            case ArcRenderEvent.CLOSED:
                return Arc2D.CHORD;
            case ArcRenderEvent.SECTOR:
                return Arc2D.PIE;
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillArc(org.eclipse.birt.chart.output.ArcRenderEvent)
     */
    public void fillArc(ArcRenderEvent are) throws RenderingException
    {
        final Fill flBackground = are.getBackground();
        if (flBackground instanceof ColorDefinition)
        {
            final Color clrPrevious = _g2d.getColor();
            _g2d.setColor((Color) _ids.getColor((ColorDefinition) flBackground));
            _g2d.fill(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(), are
                .getHeight(), are.getStartAngle(), are.getAngleExtent(), toSwingArcType(are.getStyle())));
            _g2d.setColor(clrPrevious); // RESTORE
        }
        else if (flBackground instanceof Gradient)
        {
            final Gradient g = (Gradient) flBackground;
            final ColorDefinition cdStart = (ColorDefinition) g.getStartColor();
            final ColorDefinition cdEnd = (ColorDefinition) g.getEndColor();
            //boolean bCyclic = g.isCyclic();
            double dAngleInDegrees = g.getDirection();
            final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
            //int iAlpha = g.getTransparency();
            Bounds bo = are.getBounds();

            /*if (bCyclic)
            {
            }*/

            if (dAngleInDegrees < -90 || dAngleInDegrees > 90)
            {
                throw new RenderingException(
                    "exception.gradient.angle",//$NON-NLS-1$
                    new Object[] { new Double(dAngleInDegrees) },
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED
            }

            Point2D.Double p2dStart, p2dEnd;
            if (dAngleInDegrees == 90)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees == -90)
            {
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees > 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight() - bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else if (dAngleInDegrees < 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
            }
            final Paint pPrevious = _g2d.getPaint();
            _g2d.setPaint(new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids
                .getColor(cdEnd)));
            _g2d.fill(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(), are
                .getHeight(), are.getStartAngle(), are.getAngleExtent(), toSwingArcType(are.getStyle())));
            _g2d.setPaint(pPrevious); // RESTORE
        }
        else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image)
        {
            final Bounds bo = are.getBounds();
            final Rectangle2D.Double r2d = new Rectangle2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());
            
            // SETUP THE CLIPPING AREA
            final Shape shArc = new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(), are.getWidth(), are
                .getHeight(), are.getStartAngle(), are.getAngleExtent(), toSwingArcType(are.getStyle()));
            Shape shPreviousClip = _g2d.getClip();
            _g2d.setClip(shArc);

            // LOAD THE IMAGE
            final String sUrl = ((org.eclipse.birt.chart.model.attribute.Image) flBackground).getURL();
            java.awt.Image img = null;
            try
            {
                img = (java.awt.Image) _ids.loadImage(new URL(sUrl));
            }
            catch (ImageLoadingException ilex )
            {
                throw new RenderingException(ilex);
            }
            catch (MalformedURLException muex )
            {
                throw new RenderingException(muex);
            }

            // REPLICATE THE IMAGE AS NEEDED
            final Size szImage = _ids.getSize(img);
            int iXRepeat = (int) (Math.ceil(r2d.width / szImage.getWidth()));
            int iYRepeat = (int) (Math.ceil(r2d.height / szImage.getHeight()));
            ImageObserver io = (ImageObserver) _ids.getObserver();
            for (int i = 0; i < iXRepeat; i++)
            {
                for (int j = 0; j < iYRepeat; j++)
                {
                    _g2d.drawImage(img, (int) (r2d.x + i * szImage.getWidth()),
                        (int) (r2d.y + j * szImage.getHeight()), io);
                }
            }
            //img(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
            _g2d.setClip(shPreviousClip); // RESTORE
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void drawArea(AreaRenderEvent are) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = are.getOutline();
        if (!validateLineAttributes(are.getSource(), lia))
        {
            return;
        }

        // SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
        final Color cFG = (Color) validateEdgeColor(lia.getColor(), are.getBackground(), _ids);
        if (cFG == null) // IF UNDEFINED, EXIT
        {
            return;
        }

        // BUILD THE GENERAL PATH STRUCTURE
        final GeneralPath gp = new GeneralPath();
        PrimitiveRenderEvent pre;
        for (int i = 0; i < are.getElementCount(); i++)
        {
            pre = are.getElement(i);
            if (pre instanceof ArcRenderEvent)
            {
                final ArcRenderEvent acre = (ArcRenderEvent) pre;
                final Arc2D.Double a2d = new Arc2D.Double(acre.getTopLeft().getX(), acre.getTopLeft().getY(), acre
                    .getWidth(), acre.getHeight(), acre.getStartAngle(), acre.getAngleExtent(), toSwingArcType(acre
                    .getStyle()));
                gp.append(a2d, true);
            }
            else if (pre instanceof LineRenderEvent)
            {
                final LineRenderEvent lre = (LineRenderEvent) pre;
                final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(), lre.getEnd()
                    .getX(), lre.getEnd().getY());
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#fillArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void fillArea(AreaRenderEvent are) throws RenderingException
    {
        // SETUP SWING DATA STRUCTURES
        final GeneralPath gp = new GeneralPath();
        PrimitiveRenderEvent pre;
        for (int i = 0; i < are.getElementCount(); i++)
        {
            pre = are.getElement(i);
            if (pre instanceof ArcRenderEvent)
            {
                final ArcRenderEvent acre = (ArcRenderEvent) pre;
                final Arc2D.Double a2d = new Arc2D.Double(acre.getTopLeft().getX(), acre.getTopLeft().getY(), acre
                    .getWidth(), acre.getHeight(), acre.getStartAngle(), acre.getAngleExtent(), toSwingArcType(acre
                    .getStyle()));
                gp.append(a2d, true);
            }
            else if (pre instanceof LineRenderEvent)
            {
                final LineRenderEvent lre = (LineRenderEvent) pre;
                final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(), lre.getEnd()
                    .getX(), lre.getEnd().getY());
                gp.append(l2d, true);
            }
        }

        // BEGIN FILLING
        final Fill flBackground = are.getBackground();
        if (flBackground instanceof ColorDefinition)
        {
            _g2d.setColor((Color) _ids.getColor((ColorDefinition) flBackground));
        }
        else if (flBackground instanceof Gradient)
        {
            final Gradient g = (Gradient) flBackground;
            final ColorDefinition cdStart = (ColorDefinition) g.getStartColor();
            final ColorDefinition cdEnd = (ColorDefinition) g.getEndColor();
            //boolean bCyclic = g.isCyclic();
            double dAngleInDegrees = g.getDirection();
            final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
            //int iAlpha = g.getTransparency();
            Bounds bo = are.getBounds();

            /*if (bCyclic)
            {
            }*/

            if (dAngleInDegrees < -90 || dAngleInDegrees > 90)
            {
                throw new RenderingException(
                    "exception.gradient.angle",//$NON-NLS-1$
                    new Object[] { new Double(dAngleInDegrees) },
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED
            }

            Point2D.Double p2dStart, p2dEnd;
            if (dAngleInDegrees == 90)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees == -90)
            {
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees > 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight() - bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else if (dAngleInDegrees < 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
            }
            _g2d.setPaint(new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids
                .getColor(cdEnd)));
        }
        else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image)
        {
        	// TBD
        }
        _g2d.fill(gp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
     */
    public void drawText(TextRenderEvent tre) throws RenderingException
    {
        SwingTextRenderer tr = SwingTextRenderer.instance((SwingDisplayServer) _ids);
        switch (tre.getAction())
        {
            case TextRenderEvent.UNDEFINED:
                throw new RenderingException(
                    "exception.missing.text.render.action", //$NON-NLS-1$
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); 

            case TextRenderEvent.RENDER_SHADOW_AT_LOCATION:
                tr.renderShadowAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
                break;

            case TextRenderEvent.RENDER_TEXT_AT_LOCATION:
                tr.renderTextAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
                break;

            case TextRenderEvent.RENDER_TEXT_IN_BLOCK:
                tr.renderTextInBlock(this, tre.getBlockBounds(), tre.getBlockAlignment(), tre.getLabel());
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#enableInteraction(org.eclipse.birt.chart.output.InteractionEvent)
     */
    public void enableInteraction(InteractionEvent iev) throws RenderingException
    {
        if (_iun == null)
        {
            DefaultLoggerImpl.instance().log(ILogger.WARNING,
                Messages.getString("exception.missing.component.interaction", getLocale())); //$NON-NLS-1$
            return;
        }

        final Trigger[] tga = iev.getTriggers();
        if (tga == null)
        {
            return;
        }

        // CREATE AND SETUP THE SHAPES FOR INTERACTION
        TriggerCondition tc;
        ArrayList al;
        final PrimitiveRenderEvent pre = iev.getHotSpot();
        if (pre instanceof PolygonRenderEvent)
        {
            final Location[] loa = ((PolygonRenderEvent) pre).getPoints();

            for (int i = 0; i < tga.length; i++)
            {
                tc = tga[i].getCondition();
                al = (ArrayList) _lhmAllTriggers.get(tc);
                if (al == null)
                {
                    al = new ArrayList(4); // UNDER NORMAL CONDITIONS
                    _lhmAllTriggers.put(tc, al);
                }
                al.add(new ShapedAction(iev.getSource(), loa, tga[i].getAction()));
            }
        }
        else if (pre instanceof OvalRenderEvent)
        {
            final Bounds boEllipse = ((OvalRenderEvent) pre).getBounds();

            for (int i = 0; i < tga.length; i++)
            {
                tc = tga[i].getCondition();
                al = (ArrayList) _lhmAllTriggers.get(tc);
                if (al == null)
                {
                    al = new ArrayList(4); // UNDER NORMAL CONDITIONS
                    _lhmAllTriggers.put(tc, al);
                }
                al.add(new ShapedAction(iev.getSource(), boEllipse, tga[i].getAction()));
            }
        }
        else if (pre instanceof ArcRenderEvent)
        {
            final ArcRenderEvent are = (ArcRenderEvent) pre;
            final Bounds boEllipse = are.getEllipseBounds();
            double dStart = are.getStartAngle();
            double dExtent = are.getAngleExtent();
            int iArcType = toSwingArcType(are.getStyle());

            for (int i = 0; i < tga.length; i++)
            {
                tc = tga[i].getCondition();
                al = (ArrayList) _lhmAllTriggers.get(tc);
                if (al == null)
                {
                    al = new ArrayList(4); // UNDER NORMAL CONDITIONS
                    _lhmAllTriggers.put(tc, al);
                }
                al.add(new ShapedAction(iev.getSource(), boEllipse, dStart, dExtent, iArcType, tga[i].getAction()));
            }
        }

    }

    /**
     * Reusable 'strokes' for rendering lines may be obtained from here
     * 
     * @param ls
     * @return
     */
    public final Stroke getCachedStroke(LineAttributes lia)
    {
        if (lia == null)
            return null;

        Stroke s = (Stroke) _htLineStyles.get(lia);
        if (s == null)
        {
            BasicStroke bs = null;
            if (lia.getStyle().getValue() == LineStyle.DASHED)
            {
                float[] faStyle = new float[]
                {
                    6.0f, 4.0f
                };
                bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, faStyle, 0);
            }
            else if (lia.getStyle().getValue() == LineStyle.DOTTED)
            {
                float[] faStyle = new float[]
                {
                    1.0f, 4.0f
                };
                bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, faStyle, 0);
            }
            else if (lia.getStyle().getValue() == LineStyle.SOLID)
            {
                bs = new BasicStroke(lia.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
            if (bs != null)
            {
                _htLineStyles.put(lia, bs);
            }
            return bs;
        }
        return s;
    }

    /**
     * 
     * @param s
     * @param sWordToReplace
     * @param sReplaceWith
     * @return
     */
    public static String csSearchAndReplace(String s, String sWordToReplace, String sReplaceWith)
    {
        int i = 0;
        do
        {
            i = s.indexOf(sWordToReplace, i);
            if (i != -1)
            {
                s = s.substring(0, i) + sReplaceWith + s.substring(i + sWordToReplace.length());
                i += sReplaceWith.length();
            }
        }
        while (i != -1);
        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void drawOval(OvalRenderEvent ore) throws RenderingException
    {
        // CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
        final LineAttributes lia = ore.getOutline();
        if (!validateLineAttributes(ore.getSource(), lia))
        {
            return;
        }

        // SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
        final Color cFG = (Color) validateEdgeColor(lia.getColor(), ore.getBackground(), _ids);
        if (cFG == null)
        {
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#fillOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void fillOval(OvalRenderEvent ore) throws RenderingException
    {
        final Fill flBackground = ore.getBackground();
        final Bounds bo = ore.getBounds();
        final Ellipse2D.Double e2d = new Ellipse2D.Double(bo.getLeft(), bo.getTop(), bo.getWidth(), bo.getHeight());
        if (flBackground instanceof ColorDefinition)
        {
            final ColorDefinition cd = (ColorDefinition) flBackground;
            _g2d.setColor((Color) _ids.getColor(cd));
            _g2d.fill(e2d);
        }
        else if (flBackground instanceof Gradient)
        {
            final Gradient g = (Gradient) flBackground;
            final ColorDefinition cdStart = (ColorDefinition) g.getStartColor();
            final ColorDefinition cdEnd = (ColorDefinition) g.getEndColor();
            //boolean bCyclic = g.isCyclic();
            double dAngleInDegrees = g.getDirection();
            final double dAngleInRadians = ((-dAngleInDegrees * Math.PI) / 180.0);
            //int iAlpha = g.getTransparency();

            /*if (bCyclic)
            {
            }*/

            if (dAngleInDegrees < -90 || dAngleInDegrees > 90)
            {
                throw new RenderingException(
                    "exception.gradient.angle",//$NON-NLS-1$
                    new Object[] { new Double(dAngleInDegrees) },
                    ResourceBundle.getBundle(
                        Messages.DEVICE_EXTENSION, 
                        getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED
            }

            Point2D.Double p2dStart, p2dEnd;
            if (dAngleInDegrees == 90)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees == -90)
            {
                p2dEnd = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
            }
            else if (dAngleInDegrees > 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop() + bo.getHeight());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getHeight() - bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else if (dAngleInDegrees < 0)
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop() + bo.getWidth()
                    * Math.abs(Math.tan(dAngleInRadians)));
            }
            else
            {
                p2dStart = new Point2D.Double(bo.getLeft(), bo.getTop());
                p2dEnd = new Point2D.Double(bo.getLeft() + bo.getWidth(), bo.getTop());
            }
            _g2d.setPaint(new GradientPaint(p2dStart, (Color) _ids.getColor(cdStart), p2dEnd, (Color) _ids
                .getColor(cdEnd)));
            _g2d.fill(e2d);
        }
        else if (flBackground instanceof org.eclipse.birt.chart.model.attribute.Image)
        {
            final String sUrl = ((org.eclipse.birt.chart.model.attribute.Image) flBackground).getURL();
            java.awt.Image img = null;
            try
            {
                img = (java.awt.Image) _ids.loadImage(new URL(sUrl));
            }
            catch (ImageLoadingException ilex )
            {
                throw new RenderingException(ilex);
            }
            catch (MalformedURLException muex )
            {
                throw new RenderingException(muex);
            }

            final Shape shClip = _g2d.getClip();
            _g2d.setClip(e2d);

            final Size szImage = _ids.getSize(img);

            int iXRepeat = (int) (Math.ceil(e2d.width / szImage.getWidth()));
            int iYRepeat = (int) (Math.ceil(e2d.height / szImage.getHeight()));
            ImageObserver io = (ImageObserver) _ids.getObserver();
            for (int i = 0; i < iXRepeat; i++)
            {
                for (int j = 0; j < iYRepeat; j++)
                {
                    _g2d.drawImage(img, (int) (e2d.x + i * szImage.getWidth()),
                        (int) (e2d.y + j * szImage.getHeight()), io);
                }
            }

            //img.flush(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
            _g2d.setClip(shClip); // RESTORE
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.IDeviceRenderer#getXServer()
     */
    public IDisplayServer getDisplayServer()
    {
        return _ids;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.eclipse.birt.chart.event.TransformationEvent)
     */
    public void applyTransformation(TransformationEvent tev) throws RenderingException
    {
        switch (tev.getTransform())
        {
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#start()
     */
    public void before() throws RenderingException
    {
        // NOT YET USED
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#end()
     */
    public void after() throws RenderingException
    {
        // FLUSH ALL IMAGES USED IN RENDERING THE CHART CONTENT
    	((SwingDisplayServer) _ids).getImageCache().flush();
    }
}
