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

package org.eclipse.birt.chart.render;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.exception.UnsupportedFeatureException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.CDateTime;

/**
 *  
 */
public abstract class AxesRenderer extends BaseRenderer
{

    /**
     *  
     */
    private Axis ax;

    /**
     *  
     */
    public AxesRenderer()
    {
        super();
    }

    /**
     * Overridden behavior for graphic element series that are plotted along axes
     * 
     * @param bo
     */
    public final void render(Map htRenderers, Bounds bo) throws GenerationException, RenderingException
    {
        final boolean bFirstInSequence = (iSeriesIndex == 0);
        final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);
        final ILogger il = DefaultLoggerImpl.instance();
        long lTimer = System.currentTimeMillis();
        final Chart cm = getModel();
        final Object o = getComputations();
        final IDeviceRenderer idr = getDevice();

        if (bFirstInSequence) // SEQUENCE OF MULTIPLE SERIES RENDERERS
        // (POSSIBLY PARTICIPATING IN A COMBINATION
        // CHART)
        {
            // SETUP A TIMER
            lTimer = System.currentTimeMillis();
            htRenderers.put(TIMER, new Long(lTimer));

            // RENDER THE CHART BY WALKING THROUGH THE RECURSIVE BLOCK STRUCTURE
            Block bl = cm.getBlock();
            final Enumeration e = bl.children(true);
            final BlockGenerationEvent bge = new BlockGenerationEvent(bl);

            // ALWAYS RENDER THE OUTERMOST BLOCK FIRST
            bge.updateBlock(bl);
            renderBlock(idr, bl);

            while (e.hasMoreElements())
            {
                bl = (Block) e.nextElement();

                bge.updateBlock(bl);
                if (bl instanceof Plot)
                {
                    renderPlot(idr, (Plot) bl);
                    if (!bLastInSequence)
                        break; // STOP AT THE PLOT IF NOT ALSO THE LAST IN THE
                    // SEQUENCE
                }
                else if (bl instanceof TitleBlock)
                {
                    renderTitle(idr, bl);
                }
                else if (bl instanceof LabelBlock)
                {
                    renderLabel(idr, bl);
                }
                else if (bl instanceof Legend)
                {
                    renderLegend(idr, (Legend) bl, htRenderers);
                }
                else
                {
                    renderBlock(idr, bl);
                }
            }
        }
        else if (bLastInSequence)
        {
            Block bl = cm.getBlock();
            final Enumeration e = bl.children(true);
            final BlockGenerationEvent bge = new BlockGenerationEvent(this);

            boolean bStarted = false;
            while (e.hasMoreElements())
            {
                bl = (Block) e.nextElement();
                if (!bStarted && !bl.isPlot())
                    continue; // IGNORE ALL BLOCKS UNTIL PLOT IS ENCOUNTERED
                bStarted = true;

                bge.updateBlock(bl);
                if (bl instanceof Plot)
                {
                    renderPlot(idr, (Plot) bl);
                }
                else if (bl instanceof TitleBlock)
                {
                    renderTitle(idr, bl);
                }
                else if (bl instanceof LabelBlock)
                {
                    renderLabel(idr, bl);
                }
                else if (bl instanceof Legend)
                {
                    renderLegend(idr, (Legend) bl, htRenderers);
                }
                else
                {
                    renderBlock(idr, bl);
                }
            }
        }
        else
        // FOR ALL SERIES IN-BETWEEN, ONLY RENDER THE PLOT
        {
            final BlockGenerationEvent bge = new BlockGenerationEvent(this);
            Plot p = cm.getPlot();
            bge.updateBlock(p);
            renderPlot(idr, p);
        }

        lTimer = System.currentTimeMillis() - lTimer;
        if (htRenderers.containsKey(TIMER))
        {
            final Long l = (Long) htRenderers.get(TIMER);
            htRenderers.put(TIMER, new Long(l.longValue() + lTimer));
        }
        else
        {
            htRenderers.put(TIMER, new Long(lTimer));
        }
        if (bLastInSequence)
        {
            il.log(ILogger.INFORMATION, "Time to render everything = " + lTimer + " ms");
            htRenderers.remove(TIMER);
        }
    }

    /**
     * Ths background is the first component rendered within the plot block. This is rendered with Z-order=0
     */
    protected void renderBackground(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        // PLOT BLOCK STUFF
        super.renderBackground(ipr, p);

        final ChartWithAxes cwa = (ChartWithAxes) getModel();
        final PlotWith2DAxes pwa = (PlotWith2DAxes) getComputations();

        // PLOT CLIENT AREA
        final ClientArea ca = p.getClientArea();
        Bounds bo = pwa.getPlotBounds();
        final RectangleRenderEvent rre = (RectangleRenderEvent) ((EventObjectCache) ipr).getEventObject(p,
            RectangleRenderEvent.class);
        rre.setBounds(bo);
        rre.setOutline(ca.getOutline());
        rre.setBackground(ca.getBackground());
        ipr.fillRectangle(rre);

        // NOW THAT THE AXES HAVE BEEN COMPUTED, FILL THE INTERNAL PLOT AREA
        double dSeriesThickness = pwa.getSeriesThickness();
        double[] daX =
        {
            bo.getLeft() - dSeriesThickness, bo.getLeft() + bo.getWidth() - dSeriesThickness
        };
        double[] daY =
        {
            bo.getTop() + bo.getHeight() + dSeriesThickness, bo.getTop() + dSeriesThickness
        };
        if (pwa.getDimension() == IConstants.TWO_5_D)
        {
            Fill fDarker = ca.getBackground();
            if (fDarker instanceof ColorDefinition)
            {
                fDarker = ((ColorDefinition) fDarker).darker();
            }

            // LEFT WALL
            renderPlane(ipr, p, new Location[]
            {
                LocationImpl.create(daX[0], daY[0]), LocationImpl.create(daX[0], daY[1])
            }, ca.getBackground(), ca.getOutline(), cwa.getDimension(), dSeriesThickness, false);

            // FLOOR
            renderPlane(ipr, p, new Location[]
            {
                LocationImpl.create(daX[0], daY[0]), LocationImpl.create(daX[1], daY[0])
            }, ca.getBackground(), ca.getOutline(), cwa.getDimension(), dSeriesThickness, false);
        }

        // RENDER GRID LINES (MAJOR=DONE; MINOR=PENDING)
        final AllAxes aax = pwa.getAxes();
        final OneAxis[] oaxa = new OneAxis[2 + aax.getOverlayCount()];
        oaxa[0] = aax.getPrimaryBase();
        oaxa[1] = aax.getPrimaryOrthogonal();
        for (int i = 0; i < aax.getOverlayCount(); i++)
        {
            oaxa[2 + i] = aax.getOverlay(i);
        }

        double x = 0, y = 0;
        LineAttributes lia;
        LineRenderEvent lre;
        final Insets insCA = aax.getInsets();

        // RENDER MINOR GRID LINES FIRST
        int iCount;
        Grid g;
        double[] doaMinor = null;
        for (int i = 0; i < oaxa.length; i++)
        {
            g = oaxa[i].getGrid();
            iCount = g.getMinorCountPerMajor();

            lia = oaxa[i].getGrid().getLineAttributes(IConstants.MINOR);
            if (lia == null || !lia.isSetStyle() || !lia.isVisible())
            {
                continue;
            }

            if (iCount <= 0)
            {
                throw new RenderingException("Cannot split up a major unit into " + iCount + " minor unit(s).");
            }

            AutoScale sc = oaxa[i].getScale();
            doaMinor = sc.getMinorCoordinates(iCount);

            if (oaxa[i].getOrientation() == IConstants.HORIZONTAL)
            {
                double[] da = sc.getTickCordinates();
                double dY2 = bo.getTop() + 1, dY1 = bo.getTop() + bo.getHeight() - 2;
                if (pwa.getDimension() == IConstants.TWO_5_D)
                {
                    for (int j = 0; j < da.length - 1; j++)
                    {
                        x = da[j];
                        for (int k = 0; k < doaMinor.length; k++)
                        {
                            lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                            lre.setLineAttributes(lia);
                            lre.setStart(LocationImpl.create(x + doaMinor[k], dY1 + pwa.getSeriesThickness()));
                            lre.setEnd(LocationImpl.create(x + doaMinor[k] + pwa.getSeriesThickness(), dY1));
                            ipr.drawLine(lre);
                        }
                    }
                }

                for (int j = 0; j < da.length - 1; j++)
                {
                    x = da[j];
                    if (pwa.getDimension() == IConstants.TWO_5_D)
                    {
                        x += pwa.getSeriesThickness();
                    }
                    for (int k = 0; k < doaMinor.length; k++)
                    {
                        lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                        lre.setLineAttributes(lia);
                        lre.setStart(LocationImpl.create(x + doaMinor[k], dY1));
                        lre.setEnd(LocationImpl.create(x + doaMinor[k], dY2));
                        ipr.drawLine(lre);
                    }
                }
            }
            else if (oaxa[i].getOrientation() == IConstants.VERTICAL)
            {
                double[] da = sc.getTickCordinates();
                double dX1 = bo.getLeft() + 1, dX2 = bo.getLeft() + bo.getWidth() - 2;
                if (pwa.getDimension() == IConstants.TWO_5_D)
                {
                    for (int j = 0; j < da.length - 1; j++)
                    {
                        y = (da[j] - pwa.getSeriesThickness());
                        for (int k = 0; k < doaMinor.length; k++)
                        {
                            lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                            lre.setLineAttributes(lia);
                            lre.setStart(LocationImpl.create(dX1, y - doaMinor[k]));
                            lre.setEnd(LocationImpl.create(dX1 - pwa.getSeriesThickness(), y - doaMinor[k]
                                + pwa.getSeriesThickness()));
                            ipr.drawLine(lre);
                        }
                    }
                }
                for (int j = 0; j < da.length - 1; j++)
                {
                    y = da[j];
                    if (pwa.getDimension() == IConstants.TWO_5_D)
                    {
                        y -= pwa.getSeriesThickness();
                    }
                    for (int k = 0; k < doaMinor.length; k++)
                    {
                        lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                        lre.setLineAttributes(lia);
                        lre.setStart(LocationImpl.create(dX1, y - doaMinor[k]));
                        lre.setEnd(LocationImpl.create(dX2, y - doaMinor[k]));
                        ipr.drawLine(lre);
                    }
                }
            }
        }

        // RENDER MAJOR GRID LINES NEXT
        for (int i = 0; i < oaxa.length; i++)
        {
            lia = oaxa[i].getGrid().getLineAttributes(IConstants.MAJOR);
            if (lia == null || !lia.isSetStyle() || !lia.isVisible()) // GRID
            // LINE
            // UNDEFINED
            {
                continue;
            }

            AutoScale sc = oaxa[i].getScale();
            if (oaxa[i].getOrientation() == IConstants.HORIZONTAL)
            {
                double[] da = sc.getTickCordinates();
                double dY2 = bo.getTop() + 1, dY1 = bo.getTop() + bo.getHeight() - 2;
                if (pwa.getDimension() == IConstants.TWO_5_D)
                {
                    for (int j = 0; j < da.length; j++)
                    {
                        if (j == 0 && insCA.getBottom() < lia.getThickness())
                            continue;
                        if (j == da.length - 1 && insCA.getTop() < lia.getThickness())
                            continue;

                        x = da[j];
                        lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                        lre.setLineAttributes(lia);
                        lre.setStart(LocationImpl.create(x, dY1 + pwa.getSeriesThickness()));
                        lre.setEnd(LocationImpl.create(x + pwa.getSeriesThickness(), dY1));
                        ipr.drawLine(lre);
                    }
                }
                for (int j = 0; j < da.length; j++)
                {
                    if (j == 0 && insCA.getBottom() < lia.getThickness())
                        continue;
                    if (j == da.length - 1 && insCA.getTop() < lia.getThickness())
                        continue;

                    x = da[j];
                    if (pwa.getDimension() == IConstants.TWO_5_D)
                    {
                        x += pwa.getSeriesThickness();
                    }
                    lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                    lre.setLineAttributes(lia);
                    lre.setStart(LocationImpl.create(x, dY1));
                    lre.setEnd(LocationImpl.create(x, dY2));
                    ipr.drawLine(lre);
                }
            }
            else if (oaxa[i].getOrientation() == IConstants.VERTICAL)
            {
                double[] da = sc.getTickCordinates();
                double dX1 = bo.getLeft() + 1, dX2 = bo.getLeft() + bo.getWidth() - 2;
                if (pwa.getDimension() == IConstants.TWO_5_D)
                {
                    for (int j = 0; j < da.length; j++)
                    {
                        if (j == 0 && insCA.getLeft() < lia.getThickness())
                            continue;
                        if (j == da.length - 1 && insCA.getRight() < lia.getThickness())
                            continue;

                        y = (int) (da[j] - pwa.getSeriesThickness());
                        lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                        lre.setLineAttributes(lia);
                        lre.setStart(LocationImpl.create(dX1, y));
                        lre.setEnd(LocationImpl.create(dX1 - pwa.getSeriesThickness(), y + pwa.getSeriesThickness()));
                        ipr.drawLine(lre);
                    }
                }
                for (int j = 0; j < da.length; j++)
                {
                    if (j == 0 && insCA.getLeft() < lia.getThickness())
                        continue;
                    if (j == da.length - 1 && insCA.getRight() < lia.getThickness())
                        continue;

                    y = (int) da[j];
                    if (pwa.getDimension() == IConstants.TWO_5_D)
                    {
                        y -= pwa.getSeriesThickness();
                    }
                    lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(p, LineRenderEvent.class);
                    lre.setLineAttributes(lia);
                    lre.setStart(LocationImpl.create(dX1, y));
                    lre.setEnd(LocationImpl.create(dX2, y));
                    ipr.drawLine(lre);
                }
            }
        }

        if (p.getClientArea().getOutline().isVisible())
        {
            ipr.drawRectangle(rre);
        }
    }

    /**
     * The axes correspond to the lines/planes being rendered within the plot block. This is rendered with Z-order=2
     */
    private final void renderAxesStructure(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        final PlotWith2DAxes pwa = (PlotWith2DAxes) getComputations();
        final AllAxes aax = pwa.getAxes();

        final int iCount = aax.getOverlayCount() + 2;
        final OneAxis[] oaxa = new OneAxis[iCount];
        oaxa[0] = aax.getPrimaryBase();
        oaxa[1] = aax.getPrimaryOrthogonal();
        for (int i = 0; i < iCount - 2; i++)
        {
            oaxa[i + 2] = aax.getOverlay(i);
        }

        // RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
        for (int i = 0; i < iCount; i++)
        {
            renderEachAxis(ipr, p, oaxa[i], IConstants.AXIS);
        }
    }

    /**
     * The axes correspond to the lines/planes being rendered within the plot block. This is rendered with Z-order=2
     */
    private final void renderAxesLabels(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        PlotWith2DAxes pwa = (PlotWith2DAxes) getComputations();
        AllAxes aax = pwa.getAxes();

        int iCount = aax.getOverlayCount() + 2;
        OneAxis[] oaxa = new OneAxis[iCount];
        oaxa[0] = aax.getPrimaryBase();
        oaxa[1] = aax.getPrimaryOrthogonal();
        for (int i = 0; i < iCount - 2; i++)
        {
            oaxa[i + 2] = aax.getOverlay(i);
        }

        // RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
        for (int i = 0; i < iCount; i++)
        {
            renderEachAxis(ipr, p, oaxa[i], IConstants.LABELS);
        }
    }

    /**
     * This method renders the bar graphic elements superimposed over the plot background and any previously rendered
     * series' graphic elements.
     */
    public final void renderPlot(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        final boolean bFirstInSequence = (iSeriesIndex == 0);
        final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);

        if (bFirstInSequence)
        {
            renderBackground(ipr, p);
            renderAxesStructure(ipr, p);
        }

        SeriesRenderingHints srh = null;
        try
        {
            srh = ((PlotWith2DAxes) getComputations()).getSeriesRenderingHints(getSeries());
        }
        catch (Exception ex )
        {
            throw new RenderingException(ex);
        }
        renderSeries(ipr, p, srh); // CALLS THE APPROPRIATE SUBCLASS FOR
        // GRAPHIC ELEMENT RENDERING

        if (bLastInSequence)
        {
            try
            {
                getDeferredCache().flush(); // FLUSH DEFERRED CACHE
            }
            catch (UnsupportedFeatureException ex ) // NOTE: RENDERING
            // EXCEPTION ALREADY BEING
            // THROWN
            {
                throw new RenderingException(ex);
            }
            renderAxesLabels(ipr, p);
        }
    }

    /**
     * 
     * 
     * @param ipr
     * @param pl
     * @param ax
     * @param iWhatToDraw
     * 
     * @throws RenderingException
     */
    public final void renderEachAxis(IPrimitiveRenderer ipr, Plot pl, OneAxis ax, int iWhatToDraw)
        throws RenderingException
    {
        final PlotWith2DAxes pwa = (PlotWith2DAxes) getComputations();
        final Insets insCA = pwa.getAxes().getInsets();
        double dLocation = ax.getAxisCoordinate();
        double dAngleInDegrees = ax.getLabel().getCaption().getFont().getRotation();
        AutoScale sc = ax.getScale();
        IntersectionValue iv = ax.getIntersectionValue();
        int iTickStyle = ax.getGrid().getTickStyle(IConstants.MAJOR);
        int iLabelLocation = ax.getLabelPosition();
        int iOrientation = ax.getOrientation();
        IDisplayServer xs = this.getDevice().getDisplayServer();

        double[] daEndPoints = sc.getEndPoints();
        double[] da = sc.getTickCordinates();
        String sText;
        boolean bLabelShadowEnabled = (ax.getLabel().getShadowColor() != null);
        int iDimension = pwa.getDimension();
        double dSeriesThickness = pwa.getSeriesThickness();
        final NumberDataElement nde = NumberDataElementImpl.create(0);
        final FormatSpecifier fs = ax.getFormatSpecifier();
        DecimalFormat df = null;

        LineAttributes lia = ax.getLineAttributes();
        if (!lia.isSetVisible())
        {
            throw new RenderingException("The axis visibility is not set and the results will be ambiguous");
        }
        Label la = ax.getLabel();
        Location lo = LocationImpl.create(0, 0);

        final TransformationEvent trae = (TransformationEvent) ((EventObjectCache) ipr).getEventObject(pl,
            TransformationEvent.class);
        final TextRenderEvent tre = (TextRenderEvent) ((EventObjectCache) ipr)
            .getEventObject(pl, TextRenderEvent.class);
        tre.setLabel(la);
        tre.setTextPosition(iLabelLocation);
        tre.setLocation(lo);

        final LineRenderEvent lre = (LineRenderEvent) ((EventObjectCache) ipr)
            .getEventObject(pl, LineRenderEvent.class);
        lre.setLineAttributes(lia);
        lre.setStart(LocationImpl.create(0, 0));
        lre.setEnd(LocationImpl.create(0, 0));

        if (iOrientation == IConstants.VERTICAL)
        {
            int y;
            double dX = dLocation;

            if (iv != null && iv.getType() == IntersectionValue.MAX && iDimension == IConstants.TWO_5_D)
            {
                trae.setTransform(TransformationEvent.TRANSLATE);
                trae.setTranslation(dSeriesThickness, -dSeriesThickness);
                ipr.applyTransformation(trae);
            }

            double dXTick1 = ((iTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT) ? (dX - IConstants.TICK_SIZE)
                : dX, dXTick2 = ((iTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT) ? dX
                + IConstants.TICK_SIZE : dX;

            if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS && lia.isVisible())
            {
                final double dStart = daEndPoints[0] + insCA.getBottom(), dEnd = daEndPoints[1] - insCA.getTop();
                if (iv != null && iv.getType() == IntersectionValue.VALUE && iDimension == IConstants.TWO_5_D)
                {
                    final Location[] loa = new Location[4];
                    loa[0] = LocationImpl.create(dX, dStart);
                    loa[1] = LocationImpl.create(dX + dSeriesThickness, dStart - dSeriesThickness);
                    loa[2] = LocationImpl.create(dX + dSeriesThickness, dEnd - dSeriesThickness);
                    loa[3] = LocationImpl.create(dX, dEnd);

                    final PolygonRenderEvent pre = (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(pl,
                        PolygonRenderEvent.class);
                    pre.setPoints(loa);
                    pre.setBackground(ColorDefinitionImpl.create(255, 255, 255, 127));
                    pre.setOutline(lia);
                    ipr.fillPolygon(pre);
                }
                lre.setLineAttributes(lia);
                lre.getStart().set(dX, dStart);
                lre.getEnd().set(dX, dEnd);
                ipr.drawLine(lre);
            }

            if ((sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale())
            {
                double dAngleInRadians = (-dAngleInDegrees * Math.PI) / 180;
                double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
                double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
                double dOffset = 0, dW, dH, dHCosTheta;
                double dUnitSize = sc.getUnitSize();
                DataSetIterator dsi = sc.getData();
                final int iDateTimeUnit = (sc.getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
                    : IConstants.UNDEFINED;
                ITextMetrics tl = null;

                if (dAngleInDegrees == 90 || dAngleInDegrees == 0)
                {
                    dOffset = -dUnitSize / 2;
                }

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                    dsi.reset();
                    for (int i = 0; i < da.length - 1; i++)
                    {
                        la.getCaption().setValue(sc.formatCategoryValue(sc.getType(), dsi.next(), iDateTimeUnit));
                        tl = xs.getTextMetrics(la);
                        dH = tl.getFullHeight();
                        dW = tl.getFullWidth();
                        dHCosTheta = dH * dCosTheta;
                        if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
                        {
                            if (iLabelLocation == IConstants.LEFT)
                            {
                                dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dW * dSineTheta;
                            }
                            else if (iLabelLocation == IConstants.RIGHT)
                            {
                                dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dHCosTheta;
                            }
                        }
                        else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
                        {
                            if (iLabelLocation == IConstants.LEFT)
                            {
                                dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dHCosTheta;
                            }
                            else if (iLabelLocation == IConstants.RIGHT)
                            {
                                dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dW * dSineTheta;
                            }
                        }
                        else if (dAngleInDegrees == 0 || dAngleInDegrees == 90 || dAngleInDegrees == -90)
                        {
                            dOffset = -dUnitSize / 2;
                        }
                        y = (int) da[i];
                        if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                        {
                            lo.set(x, y + dOffset);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                        }
                    }
                }

                double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                dsi.reset();
                for (int i = 0; i < da.length - 1; i++)
                {
                    la.getCaption().setValue(sc.formatCategoryValue(sc.getType(), dsi.next(), iDateTimeUnit));
                    tl = xs.getTextMetrics(la);
                    dH = tl.getFullHeight();
                    dW = tl.getFullWidth();
                    dHCosTheta = dH * dCosTheta;
                    if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
                    {
                        if (iLabelLocation == IConstants.LEFT)
                        {
                            dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dW * dSineTheta;
                        }
                        else if (iLabelLocation == IConstants.RIGHT)
                        {
                            dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dHCosTheta;
                        }
                    }
                    else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
                    {
                        if (iLabelLocation == IConstants.LEFT)
                        {
                            dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dHCosTheta;
                        }
                        else if (iLabelLocation == IConstants.RIGHT)
                        {
                            dOffset = (dHCosTheta + dW * dSineTheta - dUnitSize) / 2 - dW * dSineTheta;
                        }
                    }
                    else if (dAngleInDegrees == 0 || dAngleInDegrees == 90 || dAngleInDegrees == -90)
                    {
                        dOffset = -dUnitSize / 2;
                    }

                    y = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dXTick1 != dXTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(dXTick1, y);
                            lre.getEnd().set(dXTick2, y);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.setStart(LocationImpl.create(dX, y));
                                lre.setEnd(LocationImpl.create(dX + dSeriesThickness, y - dSeriesThickness));
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y + dOffset);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                }
                y = (int) da[da.length - 1];
                if (dXTick1 != dXTick2)
                {
                    lre.setLineAttributes(lia);
                    lre.getStart().set(dXTick1, y);
                    lre.getEnd().set(dXTick2, y);
                    ipr.drawLine(lre);

                    if (iv != null && iDimension == IConstants.TWO_5_D && iv.getType() == IntersectionValue.VALUE)
                    {
                        lre.setStart(LocationImpl.create(dX, y));
                        lre.setEnd(LocationImpl.create(dX + dSeriesThickness, y - dSeriesThickness));
                        ipr.drawLine(lre);
                    }
                }
            }
            else if ((sc.getType() & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC)
            {
                double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
                final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;

                        for (int i = 0; i < da.length; i++)
                        {
                            if (fs == null)
                            {
                                df = new DecimalFormat(sc.getNumericPattern(dAxisValue));
                            }
                            nde.setValue(dAxisValue);
                            try
                            {
                                sText = ValueFormatter.format(nde, ax.getFormatSpecifier(), ax.getLocale(), df);
                            }
                            catch (DataFormatException dfex )
                            {
                                DefaultLoggerImpl.instance().log(dfex);
                                sText = IConstants.NULL_STRING;
                            }
                            y = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);

                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            dAxisValue *= dAxisStep;
                        }
                    }
                }

                dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue(); // RESET
                double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    if (fs == null)
                    {
                        df = new DecimalFormat(sc.getNumericPattern(dAxisValue));
                    }
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, null, df); // TBD:
                        // SET
                        // LOCALE
                        // CORRECTLY
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    y = (int) da[i];

                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dXTick1 != dXTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(dXTick1, y);
                            lre.getEnd().set(dXTick2, y);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.setLineAttributes(lia);
                                lre.setStart(LocationImpl.create(dX, y));
                                lre.setEnd(LocationImpl.create(dX + dSeriesThickness, y - dSeriesThickness));
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);

                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                    dAxisValue *= dAxisStep;
                }
            }
            else if ((sc.getType() & IConstants.LINEAR) == IConstants.LINEAR)
            {
                double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
                final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();
                df = new DecimalFormat(sc.getNumericPattern());

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                        for (int i = 0; i < da.length; i++)
                        {
                            nde.setValue(dAxisValue);
                            try
                            {
                                sText = ValueFormatter.format(nde, fs, null, df); // TBD: SET LOCALE CORRECTLY
                            }
                            catch (DataFormatException dfex )
                            {
                                DefaultLoggerImpl.instance().log(dfex);
                                sText = IConstants.NULL_STRING;
                            }

                            y = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);

                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            dAxisValue += dAxisStep;
                        }
                    }
                }

                dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue(); // RESET
                double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, fs, null, df); // TBD:
                        // SET
                        // LOCALE
                        // CORRECTLY
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    y = (int) da[i];

                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dXTick1 != dXTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(dXTick1, y);
                            lre.getEnd().set(dXTick2, y);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.setLineAttributes(lia);
                                lre.setStart(LocationImpl.create(dX, y));
                                lre.setEnd(LocationImpl.create(dX + dSeriesThickness, y - dSeriesThickness));
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);

                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                    dAxisValue += dAxisStep;
                }
            }
            else if ((sc.getType() & IConstants.DATE_TIME) == IConstants.DATE_TIME)
            {
                CDateTime cdt, cdtAxisValue = Methods.asDateTime(sc.getMinimum());
                final int iUnit = Methods.asInteger(sc.getUnit());
                final int iStep = Methods.asInteger(sc.getStep());
                final SimpleDateFormat sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));
                cdt = cdtAxisValue;

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                        for (int i = 0; i < da.length; i++)
                        {
                            sText = sdf.format(cdt.getTime());
                            y = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
                            // W.R.T
                            // START
                            // VALUE
                        }
                    }
                }

                double x = (iLabelLocation == IConstants.LEFT) ? dXTick1 - 1 : dXTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    sText = sdf.format(cdt.getTime());
                    y = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dXTick1 != dXTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(dXTick1, y);
                            lre.getEnd().set(dXTick2, y);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.setStart(LocationImpl.create(dX, y));
                                lre.setEnd(LocationImpl.create(dX + dSeriesThickness, y - dSeriesThickness));
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }

                    cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
                    // W.R.T
                    // START
                    // VALUE
                }
            }

            if (ax.getTitle().isVisible())
            {
                final BoundingBox bb = Methods.computeBox(xs, ax.getTitlePosition(), ax.getTitle(), 0, 0);
                final Bounds boPlot = pl.getBounds();

                final Bounds bo = BoundsImpl.create(ax.getTitleCoordinate(), daEndPoints[1], bb.getWidth(),
                    daEndPoints[0] - daEndPoints[1]);

                tre.setBlockBounds(bo);
                tre.setLabel(ax.getTitle());
                tre.setBlockAlignment(null);
                tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
                ipr.drawText(tre);
            }

            if (iv != null && iv.getType() == IntersectionValue.MAX && iDimension == IConstants.TWO_5_D)
            {
                trae.setTranslation(-dSeriesThickness, dSeriesThickness);
                ipr.applyTransformation(trae);
            }
        }
        else if (iOrientation == IConstants.HORIZONTAL)
        {
            int x;
            double dY = dLocation;

            double dYTick1 = ((iTickStyle & IConstants.TICK_ABOVE) == IConstants.TICK_ABOVE) ? (dY - IConstants.TICK_SIZE)
                : dY, dYTick2 = ((iTickStyle & IConstants.TICK_BELOW) == IConstants.TICK_BELOW) ? dY
                + IConstants.TICK_SIZE : dY;

            if (iv != null && iv.getType() == IntersectionValue.MAX && iDimension == IConstants.TWO_5_D)
            {
                trae.setTransform(TransformationEvent.TRANSLATE);
                trae.setTranslation(dSeriesThickness, -dSeriesThickness);
                ipr.applyTransformation(trae);
            }

            if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS && lia.isVisible())
            {
                final double dStart = daEndPoints[0] - insCA.getLeft(), dEnd = daEndPoints[1] + insCA.getRight();
                if (iv != null && iv.getType() == IntersectionValue.VALUE && iDimension == IConstants.TWO_5_D)
                {
                    final Location[] loa = new Location[4];
                    loa[0] = LocationImpl.create(dStart, dY);
                    loa[1] = LocationImpl.create(dStart + dSeriesThickness, dY - dSeriesThickness);
                    loa[2] = LocationImpl.create(dEnd + dSeriesThickness, dY - dSeriesThickness);
                    loa[3] = LocationImpl.create(dEnd, dY);

                    final PolygonRenderEvent pre = (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(pl,
                        PolygonRenderEvent.class);
                    pre.setPoints(loa);
                    pre.setBackground(ColorDefinitionImpl.create(255, 255, 255, 127));
                    pre.setOutline(lia);
                    ipr.fillPolygon(pre);
                }
                lre.setLineAttributes(lia);
                lre.getStart().set(dStart, dY);
                lre.getEnd().set(dEnd, dY);
                ipr.drawLine(lre);
            }

            if ((sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale())
            {
                double dAngleInRadians = (-dAngleInDegrees * Math.PI) / 180;
                double dSineTheta = Math.abs(Math.sin(dAngleInRadians));
                double dCosTheta = Math.abs(Math.cos(dAngleInRadians));
                double dOffset = 0, dW, dH, dHSineTheta;
                double dUnitSize = sc.getUnitSize();
                DataSetIterator dsi = sc.getData();
                final int iDateTimeUnit = (sc.getType() == IConstants.DATE_TIME) ? CDateTime.computeUnit(dsi)
                    : IConstants.UNDEFINED;
                ITextMetrics tl = null;

                if (dAngleInDegrees == 90 || dAngleInDegrees == 0)
                {
                    dOffset = dUnitSize / 2;
                }

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS)
                    {
                        double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                        dsi.reset();
                        for (int i = 0; i < da.length - 1; i++)
                        {
                            la.getCaption().setValue(sc.formatCategoryValue(sc.getType(), dsi.next(), iDateTimeUnit));
                            tl = xs.getTextMetrics(la);
                            dH = tl.getFullHeight();
                            dW = tl.getFullWidth();
                            dHSineTheta = dH * dSineTheta;
                            if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
                            {
                                if (iLabelLocation == IConstants.ABOVE)
                                {
                                    dOffset = dUnitSize / 2 - (dW * dCosTheta + dHSineTheta) / 2 + dHSineTheta;
                                }
                                else if (iLabelLocation == IConstants.BELOW)
                                {
                                    dOffset = dUnitSize + dHSineTheta - (dUnitSize - dW * dCosTheta + dHSineTheta) / 2
                                        - dHSineTheta;
                                }
                            }
                            else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
                            {
                                if (iLabelLocation == IConstants.ABOVE)
                                {
                                    dOffset = dUnitSize / 2 - dHSineTheta / 2 + (dW * dCosTheta + dHSineTheta) / 2;
                                }
                                else if (iLabelLocation == IConstants.BELOW)
                                {
                                    dOffset = (dUnitSize - dW * dCosTheta + dHSineTheta) / 2;
                                }
                            }
                            else if (dAngleInDegrees == 0 || dAngleInDegrees == 90 || dAngleInDegrees == -90)
                            {
                                dOffset = dUnitSize / 2;
                            }
                            x = (int) da[i];
                            lo.set(x + dOffset, y);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                        }
                    }
                }

                double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                dsi.reset();
                for (int i = 0; i < da.length - 1; i++)
                {
                    la.getCaption().setValue(sc.formatCategoryValue(sc.getType(), dsi.next(), iDateTimeUnit));
                    tl = xs.getTextMetrics(la);
                    dH = tl.getFullHeight();
                    dW = tl.getFullWidth();
                    dHSineTheta = dH * dSineTheta;
                    if (dAngleInDegrees > 0 && dAngleInDegrees < 90)
                    {
                        if (iLabelLocation == IConstants.ABOVE)
                        {
                            dOffset = dUnitSize / 2 - (dW * dCosTheta + dHSineTheta) / 2 + dHSineTheta;
                        }
                        else if (iLabelLocation == IConstants.BELOW)
                        {
                            dOffset = dUnitSize + dHSineTheta - (dUnitSize - dW * dCosTheta + dHSineTheta) / 2
                                - dHSineTheta;
                        }
                    }
                    else if (dAngleInDegrees < 0 && dAngleInDegrees > -90)
                    {
                        if (iLabelLocation == IConstants.ABOVE)
                        {
                            dOffset = dUnitSize / 2 - dHSineTheta / 2 + (dW * dCosTheta + dHSineTheta) / 2;
                        }
                        else if (iLabelLocation == IConstants.BELOW)
                        {
                            dOffset = (dUnitSize - dW * dCosTheta + dHSineTheta) / 2;
                        }
                    }
                    else if (dAngleInDegrees == 0 || dAngleInDegrees == 90 || dAngleInDegrees == -90)
                    {
                        dOffset = dUnitSize / 2;
                    }

                    x = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dYTick1 != dYTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(x, dYTick1);
                            lre.getEnd().set(x, dYTick2);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.getStart().set(x, dY);
                                lre.getEnd().set(x + dSeriesThickness, dY - dSeriesThickness);
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x + dOffset, y);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                }
                x = (int) da[da.length - 1];
                if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                {
                    if (dYTick1 != dYTick2)
                    {
                        lre.setLineAttributes(lia);
                        lre.getStart().set(x, dYTick1);
                        lre.getEnd().set(x, dYTick2);
                        ipr.drawLine(lre);
                        if (iv != null && iDimension == IConstants.TWO_5_D && iv.getType() == IntersectionValue.VALUE)
                        {
                            lre.getStart().set(x, dY);
                            lre.getEnd().set(x + dSeriesThickness, dY - dSeriesThickness);
                            ipr.drawLine(lre);
                        }
                    }
                }
            }
            else if ((sc.getType() & IConstants.LINEAR) == IConstants.LINEAR)
            {
                double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
                final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();
                if (ax.getFormatSpecifier() == null)
                {
                    df = new DecimalFormat(sc.getNumericPattern());
                }

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                        for (int i = 0; i < da.length; i++)
                        {
                            nde.setValue(dAxisValue);
                            try
                            {
                                sText = ValueFormatter.format(nde, ax.getFormatSpecifier(), ax.getLocale(), df);
                            }
                            catch (DataFormatException dfex )
                            {
                                DefaultLoggerImpl.instance().log(dfex);
                                sText = IConstants.NULL_STRING;
                            }
                            x = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            dAxisValue += dAxisStep;
                        }
                    }
                }

                dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue(); // RESET
                double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, ax.getFormatSpecifier(), ax.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    x = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dYTick1 != dYTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(x, dYTick1);
                            lre.getEnd().set(x, dYTick2);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.getStart().set(x, dY);
                                lre.getEnd().set(x + dSeriesThickness, dY - dSeriesThickness);
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                    dAxisValue += dAxisStep;
                }
            }
            else if ((sc.getType() & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC)
            {
                double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
                final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                        for (int i = 0; i < da.length; i++)
                        {
                            if (ax.getFormatSpecifier() == null)
                            {
                                df = new DecimalFormat(sc.getNumericPattern(dAxisValue));
                            }
                            nde.setValue(dAxisValue);
                            try
                            {
                                sText = ValueFormatter.format(nde, ax.getFormatSpecifier(), ax.getLocale(), df);
                            }
                            catch (DataFormatException dfex )
                            {
                                DefaultLoggerImpl.instance().log(dfex);
                                sText = IConstants.NULL_STRING;
                            }
                            x = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            dAxisValue *= dAxisStep;
                        }
                    }
                }

                dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue(); // RESET
                double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    if (ax.getFormatSpecifier() == null)
                    {
                        df = new DecimalFormat(sc.getNumericPattern(dAxisValue));
                    }
                    nde.setValue(dAxisValue);
                    try
                    {
                        sText = ValueFormatter.format(nde, ax.getFormatSpecifier(), ax.getLocale(), df);
                    }
                    catch (DataFormatException dfex )
                    {
                        DefaultLoggerImpl.instance().log(dfex);
                        sText = IConstants.NULL_STRING;
                    }
                    x = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dYTick1 != dYTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(x, dYTick1);
                            lre.getEnd().set(x, dYTick2);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.getStart().set(x, dY);
                                lre.getEnd().set(x + dSeriesThickness, dY - dSeriesThickness);
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }
                    dAxisValue *= dAxisStep;
                }
            }
            else if ((sc.getType() & IConstants.DATE_TIME) == IConstants.DATE_TIME)
            {
                CDateTime cdt, cdtAxisValue = Methods.asDateTime(sc.getMinimum());
                final int iUnit = Methods.asInteger(sc.getUnit());
                final int iStep = Methods.asInteger(sc.getStep());
                SimpleDateFormat sdf = null;

                if (ax.getFormatSpecifier() == null)
                {
                    sdf = new SimpleDateFormat(CDateTime.getPreferredFormat(iUnit));
                }
                cdt = cdtAxisValue;

                // TODO: CACHE COMPUTED VALUES FOR REUSE IN 2ND LOOP
                if (bLabelShadowEnabled)
                {
                    // MUST RENDER SHADOWS IN A PREVIOUS LOOP TO ENABLE CORRECT
                    // Z-ORDERING
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                        for (int i = 0; i < da.length; i++)
                        {
                            try
                            {
                                sText = ValueFormatter.format(cdt, ax.getFormatSpecifier(), ax.getLocale(), sdf);
                            }
                            catch (DataFormatException dfex )
                            {
                                DefaultLoggerImpl.instance().log(dfex);
                                sText = IConstants.NULL_STRING;
                            }
                            sText = sdf.format(cdt.getTime());
                            x = (int) da[i];
                            lo.set(x, y);
                            la.getCaption().setValue(sText);
                            tre.setAction(TextRenderEvent.RENDER_SHADOW_AT_LOCATION);
                            ipr.drawText(tre);
                            cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
                            // W.R.T
                            // START
                            // VALUE
                        }
                    }
                }

                double y = (iLabelLocation == IConstants.ABOVE) ? dYTick1 - 1 : dYTick2 + 1;
                for (int i = 0; i < da.length; i++)
                {
                    sText = sdf.format(cdt.getTime());
                    x = (int) da[i];
                    if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS)
                    {
                        if (dYTick1 != dYTick2)
                        {
                            lre.setLineAttributes(lia);
                            lre.getStart().set(x, dYTick1);
                            lre.getEnd().set(x, dYTick2);
                            ipr.drawLine(lre);

                            if (iv != null && iDimension == IConstants.TWO_5_D
                                && iv.getType() == IntersectionValue.VALUE)
                            {
                                lre.getStart().set(x, dY);
                                lre.getEnd().set(x + dSeriesThickness, dY - dSeriesThickness);
                                ipr.drawLine(lre);
                            }
                        }
                    }
                    if ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible())
                    {
                        lo.set(x, y);
                        la.getCaption().setValue(sText);
                        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
                        ipr.drawText(tre);
                    }

                    cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1)); // ALWAYS
                    // W.R.T
                    // START
                    // VALUE
                }
            }

            if (ax.getTitle().isVisible())
            {
                final BoundingBox bb = Methods.computeBox(xs, ax.getTitlePosition(), ax.getTitle(), 0, 0);
                final Bounds boPlot = pl.getBounds();

                final Bounds bo = BoundsImpl.create(daEndPoints[0], ax.getTitleCoordinate(), daEndPoints[1]
                    - daEndPoints[0], bb.getHeight());

                tre.setBlockBounds(bo);
                tre.setLabel(ax.getTitle());
                tre.setBlockAlignment(null);
                tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
                ipr.drawText(tre);
            }

            if (iv != null && iv.getType() == IntersectionValue.MAX && iDimension == IConstants.TWO_5_D)
            {
                trae.setTranslation(-dSeriesThickness, dSeriesThickness);
                ipr.applyTransformation(trae);
            }
        }
    }

    /**
     *  
     */
    public final void set(Chart _cm, Object _o, Series _se, Axis _ax, SeriesDefinition _sd)
    {
        super.set(_cm, _o, _se, _ax, _sd);
        ax = _ax; // HOLD AXIS HERE
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.IModelAccess#getAxis()
     */
    public final Axis getAxis()
    {
        return ax;
    }
}