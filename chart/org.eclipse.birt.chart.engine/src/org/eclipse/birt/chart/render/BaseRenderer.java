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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withoutaxes.Coordinates;
import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.exception.UnsupportedFeatureException;
import org.eclipse.birt.chart.factory.DeferredCache;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Provides a generic framework that initiates the rendering sequence of the various chart components.
 */
public abstract class BaseRenderer
{

    protected static final String TIMER = "T";

    /**
     *  
     */
    ISeriesRenderingHints srh;

    /**
     *  
     */
    private IDisplayServer xs;

    /**
     *  
     */
    private IDeviceRenderer ir;

    /**
     *  
     */
    private DeferredCache dc;

    /**
     * Needed by the actual renderer
     */
    private Chart cm;

    /**
     *  
     */
    private Object oComputations;

    /**
     * Needed by the actual renderer
     */
    private Series se;

    /**
     *  
     */
    private SeriesDefinition sd;

    /**
     * All renderers associated with the chart provided for convenience and inter-series calculations
     */
    private BaseRenderer[] brna;

    /**
     * Identifies the series sequence # in the list of series renderers
     */
    protected transient int iSeriesIndex = -1;

    /**
     * Identifies the series count in the list of series renderers
     */
    protected transient int iSeriesCount = 1;

    /**
     * Internally used to simulate a translucent shadow
     */
    public static final ColorDefinition SHADOW = ColorDefinitionImpl.create(64, 64, 64, 127);

    /**
     * Internally used to darken a tiled image with a translucent dark grey color
     */
    protected static final ColorDefinition DARK_GLASS = ColorDefinitionImpl.create(64, 64, 64, 127);

    /**
     * Internally used to brighten a tiled image with a translucent light grey color
     */
    protected static final ColorDefinition LIGHT_GLASS = ColorDefinitionImpl.create(196, 196, 196, 127);

    /**
     * The internal constructor that must be defined as public
     * 
     * @param _ir
     * @param _cm
     */
    public BaseRenderer()
    {
    }

    /**
     *  
     */
    public void set(Chart _cm, Object _o, Series _se, Axis _ax, SeriesDefinition _sd)
    {
        cm = _cm;
        oComputations = _o;
        se = _se;
        sd = _sd;
        // CAN'T HOLD 'AXIS' HERE
    }

    public final void set(DeferredCache _dc)
    {
        dc = _dc;
    }

    public final void set(IDeviceRenderer _ir)
    {
        ir = _ir;
    }

    public final void set(IDisplayServer _xs)
    {
        xs = _xs;
    }

    public final void set(ISeriesRenderingHints _srh)
    {
        srh = _srh;
    }

    public final void set(BaseRenderer[] _brna)
    {
        brna = _brna;
    }

    public final ISeriesRenderingHints getSeriesRenderingHints()
    {
        return srh;
    }

    public final IDisplayServer getXServer()
    {
        return xs;
    }

    public final SeriesDefinition getSeriesDefinition()
    {
        return sd;
    }

    public Axis getAxis() // DON'T KNOW ABOUT AXIS HERE!
    {
        return null;
    }

    /**
     * 
     * @return
     */
    public final DeferredCache getDeferredCache()
    {
        return dc;
    }

    /**
     * Provides access to any other renderer in the group that participates in chart rendering
     * 
     * @param iIndex
     * @return
     */
    public final BaseRenderer getRenderer(int iIndex)
    {
        return brna[iIndex];
    }

    /**
     * 
     * @param bo
     * @param p
     * @param isrh
     * @throws GenerationException
     */
    public abstract void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws GenerationException;

    /**
     * Renders all blocks using the appropriate block z-order and the containment hierarchy.
     * 
     * @param bo
     */
    public void render(Map htRenderers, Bounds bo) throws GenerationException, RenderingException
    {
        final boolean bFirstInSequence = (iSeriesIndex == 0);
        final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);
        boolean bStarted = bFirstInSequence;

        final ILogger il = DefaultLoggerImpl.instance();
        long lTimer = System.currentTimeMillis();

        Block bl = cm.getBlock();
        final Enumeration e = bl.children(true);
        final BlockGenerationEvent bge = new BlockGenerationEvent(this);
        final IDeviceRenderer idr = getDevice();
        final ScriptHandler sh = getModel().getScriptHandler();

        if (bFirstInSequence)
        {
            // ALWAYS RENDER THE OUTERMOST BLOCK FIRST
            ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
            bge.updateBlock(bl);
            renderBlock(idr, bl);
            ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
        }

        // RENDER ALL BLOCKS EXCEPT FOR THE LEGEND IN THIS ITERATIVE LOOP
        while (e.hasMoreElements())
        {
            bl = (Block) e.nextElement();
            bge.updateBlock(bl);

            if (bl instanceof Plot)
            {
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
                renderPlot(ir, (Plot) bl);
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
                if (bFirstInSequence && !bLastInSequence)
                {
                    break;
                }

                if (!bStarted)
                {
                    bStarted = true;
                }
            }
            else if (bl instanceof TitleBlock && bStarted)
            {
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
                renderTitle(ir, bl);
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
            }
            else if (bl instanceof LabelBlock && bStarted)
            {
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
                renderLabel(ir, bl);
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
            }
            else if (bl instanceof Legend && bStarted)
            {
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
                renderLegend(idr, (Legend) bl, htRenderers);
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
            }
            else if (bStarted)
            {
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl);
                renderBlock(ir, bl);
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl);
            }
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
            try
            {
                dc.flush(); // FLUSH DEFERRED CACHE
            }
            catch (UnsupportedFeatureException ex ) // NOTE: RENDERING
            // EXCEPTION ALREADY BEING
            // THROWN
            {
                throw new RenderingException(ex);
            }
            il.log(ILogger.INFORMATION, "Time to render everything = " + lTimer + " ms");
            htRenderers.remove(TIMER);
        }
    }

    /**
     * Each of the individual series renderers will have to implement their own graphic element rendering routines
     * w.r.t. the plot background and axes that are drawn at different Z-indices.
     * 
     * This is rendered with Z-order=1
     * 
     * @param ipr
     * @param p
     * @param isrh
     */
    public abstract void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh)
        throws RenderingException;

    /**
     * Renders the legend block based on the legend rendering rules.
     * 
     * @param ipr
     * @param lg
     * @param htRenderers
     * 
     * @throws RenderingException
     */
    public void renderLegend(IPrimitiveRenderer ipr, Legend lg, Map htRenderers) throws RenderingException
    {
        renderBlock(ipr, lg);
        IDisplayServer xs = getDevice().getDisplayServer();
        final double dScale = xs.getDpiResolution() / 72d;

        Bounds bo = lg.getBounds().scaledInstance(dScale);

        Size sz = null;
        double dX, dY;
        if (lg.getPosition() != Position.INSIDE_LITERAL)
        {
            try
            {
                sz = lg.getPreferredSize(xs, cm);
            }
            catch (Exception ex )
            {
                throw new RenderingException(ex);
            }
            sz.scale(dScale);

            // USE ANCHOR IN POSITIONING THE LEGEND CLIENT AREA WITHIN THE BLOCK
            // SLACK SPACE
            dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
            dY = 0;
            if (lg.isSetAnchor())
            {
                final int iAnchor = lg.getAnchor().getValue();
                switch (iAnchor)
                {
                    case Anchor.NORTH:
                    case Anchor.NORTH_EAST:
                    case Anchor.NORTH_WEST:
                        dY = bo.getTop();
                        break;

                    case Anchor.SOUTH:
                    case Anchor.SOUTH_EAST:
                    case Anchor.SOUTH_WEST:
                        dY = bo.getTop() + bo.getHeight() - sz.getHeight();
                        break;

                    default: // CENTERED
                        dY = bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2;
                        break;
                }

                switch (iAnchor)
                {
                    case Anchor.WEST:
                    case Anchor.NORTH_WEST:
                        dX = bo.getLeft();
                        break;

                    case Anchor.EAST:
                    case Anchor.SOUTH_EAST:
                        dX = bo.getLeft() + bo.getWidth() - sz.getWidth();
                        break;

                    default: // CENTERED
                        dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
                        break;
                }
            }
            else
            {
                dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
                dY = bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2;
            }
        }
        else
        // USE PREVIOUSLY COMPUTED POSITION IN THE GENERATOR FOR LEGEND 'INSIDE' PLOT
        {
            dX = bo.getLeft();
            dY = bo.getTop();
            sz = SizeImpl.create(bo.getWidth(), bo.getHeight()); // 'bo' ALREADY SCALED
        }

        // RENDER THE LEGEND CLIENT AREA
        final ClientArea ca = lg.getClientArea();
        LineAttributes lia = ca.getOutline();
        bo = BoundsImpl.create(dX, dY, sz.getWidth(), sz.getHeight());
        bo = bo.adjustedInstance(lg.getInsets().scaledInstance(dScale)); // SHRINK BY INSETS
        dX = bo.getLeft();
        dY = bo.getTop();

        final RectangleRenderEvent rre = (RectangleRenderEvent) ((EventObjectCache) ir).getEventObject(ca,
            RectangleRenderEvent.class);
        rre.setBounds(bo);
        rre.setOutline(lia);
        rre.setBackground(ca.getBackground());
        ipr.fillRectangle(rre);
        ipr.drawRectangle(rre);
        lia = (LineAttributes) EcoreUtil.copy(lia);
        lia.setVisible(true); // SEPARATOR LINES MUST BE VISIBLE

        final SeriesDefinition[] seda = cm.getSeriesForLegend();

        // INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
        final Orientation o = lg.getOrientation();
        final Direction d = lg.getDirection();
        final Label la = LabelImpl.create();
        la.setCaption((Text) EcoreUtil.copy(lg.getText()));
        la.getCaption().setValue("X");
        ITextMetrics itm = xs.getTextMetrics(la);

        double dWidth = 0, dHeight = 0;
        double dItemHeight = itm.getFullHeight();
        final double dHorizontalSpacing = 4;
        final double dVerticalSpacing = 4;
        double dSeparatorThickness = lia.getThickness();
        final LinkedHashMap htSeriesGroups = new LinkedHashMap(); // ORDER OF KEYS IS IMPORTANT
        Insets insCA = ca.getInsets().scaledInstance(xs.getDpiResolution() / 72d);

        String sRC, sSI;
        Series seBase;
        ArrayList al;
        LegendItemRenderingHints lirh;
        Palette pa;
        int iPaletteCount;
        EList elPaletteEntries;
        Fill fPaletteEntry;
        final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

        // COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
        if (o.getValue() == Orientation.VERTICAL)
        {
            if (bPaletteByCategory)
            {
                SeriesDefinition sdBase = null;
                if (cm instanceof ChartWithAxes)
                {
                    // ONLY SUPPORT 1 BASE AXIS FOR NOW
                    final Axis axPrimaryBase = ((ChartWithAxes) cm).getBaseAxes()[0];
                    if (axPrimaryBase.getSeriesDefinitions().isEmpty())
                    {
                        return; // NOTHING TO RENDER
                        //throw new RenderingException("The primary base axis
                        // does not contain any series definitions");
                    }
                    // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
                    sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions().get(0);
                }
                else if (cm instanceof ChartWithoutAxes)
                {
                    if (((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty())
                    {
                        return; // NOTHING TO RENDER
                        //throw new RenderingException("The primary base axis
                        // does not contain any series definitions");
                    }
                    // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
                    sdBase = (SeriesDefinition) ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0);
                }
                
                // OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
                seBase = (Series) sdBase.getRunTimeSeries().get(0);
                pa = sdBase.getSeriesPalette();
                elPaletteEntries = pa.getEntries();
                iPaletteCount = elPaletteEntries.size();

                DataSetIterator dsiBase = null;
                try
                {
                    dsiBase = new DataSetIterator(seBase.getDataSet());
                }
                catch (Exception ex )
                {
                    throw new RenderingException(ex);
                }

                int i = 0;
                while (dsiBase.hasNext())
                {
                    dY += insCA.getTop();
                    la.getCaption().setValue(String.valueOf(dsiBase.next()));
                    itm.reuse(la);
                    fPaletteEntry = (Fill) elPaletteEntries.get(i++ % iPaletteCount); // CYCLE THROUGH THE PALETTE
                    lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                    renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(), insCA
                        .getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);
                    dY += itm.getFullHeight() + insCA.getBottom();
                }
            }
            else if (d.getValue() == Direction.TOP_BOTTOM)
            {
                dSeparatorThickness += dVerticalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    al = seda[j].getRunTimeSeries();
                    pa = seda[j].getSeriesPalette();
                    elPaletteEntries = pa.getEntries();
                    iPaletteCount = elPaletteEntries.size();

                    for (int i = 0; i < al.size(); i++)
                    {
                        dY += insCA.getTop();
                        seBase = (Series) al.get(i);
                        lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                        la.getCaption().setValue(String.valueOf(seBase.getSeriesIdentifier())); // TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        fPaletteEntry = (Fill) elPaletteEntries.get(i % iPaletteCount); // CYCLE THROUGH THE PALETTE
                        renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(),
                            insCA.getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);
                        dY += dItemHeight + insCA.getBottom();
                    }
                    if (j < seda.length - 1)
                    {
                        renderSeparator(ipr, lg, lia, dX, dY + dSeparatorThickness / 2, bo.getWidth(),
                            Orientation.HORIZONTAL_LITERAL);
                        dY += dSeparatorThickness;
                    }
                }
            }
            else if (d.getValue() == Direction.LEFT_RIGHT)
            {
                double dMaxW;
                dSeparatorThickness += dHorizontalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    dMaxW = 0;
                    al = seda[j].getRunTimeSeries();
                    pa = seda[j].getSeriesPalette();
                    elPaletteEntries = pa.getEntries();
                    iPaletteCount = elPaletteEntries.size();

                    for (int i = 0; i < al.size(); i++)
                    {
                        dY += insCA.getTop();
                        seBase = (Series) al.get(i);
                        lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                        la.getCaption().setValue(String.valueOf(seBase.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        dMaxW = Math.max(dMaxW, itm.getFullWidth());
                        fPaletteEntry = (Fill) elPaletteEntries.get(i % iPaletteCount); // CYCLE THROUGH THE PALETTE
                        renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(),
                            insCA.getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);
                        dY += dItemHeight + insCA.getBottom();
                    }

                    // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING +
                    // MAX ITEM WIDTH + RIGHT INSETS
                    dX += insCA.getLeft() + (3 * dItemHeight / 2) + dHorizontalSpacing + dMaxW + insCA.getRight();
                    dY = bo.getTop();

                    // SETUP VERTICAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        renderSeparator(ipr, lg, lia, dX + dSeparatorThickness / 2, dY, bo.getHeight(),
                            Orientation.VERTICAL_LITERAL);
                        dX += dSeparatorThickness;
                    }
                }
            }
            else
            {
                throw new RenderingException("Invalid argument specified for legend rendering direction = " + d);
            }
        }
        else if (o.getValue() == Orientation.HORIZONTAL)
        {
            if (bPaletteByCategory)
            {
                SeriesDefinition sdBase = null;
                if (cm instanceof ChartWithAxes)
                {
                    final Axis axPrimaryBase = ((ChartWithAxes) cm).getBaseAxes()[0]; // ONLY SUPPORT 1 BASE AXIS FOR
                    // NOW
                    if (axPrimaryBase.getSeriesDefinitions().isEmpty())
                    {
                        return; // NOTHING TO RENDER
                        //throw new RenderingException("The primary base axis
                        // does not contain any series definitions");
                    }
                    // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
                    sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions().get(0);
                }
                else if (cm instanceof ChartWithoutAxes)
                {
                    if (((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty())
                    {
                        return; // NOTHING TO RENDER
                        //throw new RenderingException("The primary base axis
                        // does not contain any series definitions");
                    }
                    // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
                    sdBase = (SeriesDefinition) ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0); 
                }
                // OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
                seBase = (Series) sdBase.getRunTimeSeries().get(0);
                pa = sdBase.getSeriesPalette();
                elPaletteEntries = pa.getEntries();
                iPaletteCount = elPaletteEntries.size();

                DataSetIterator dsiBase = null;
                try
                {
                    dsiBase = new DataSetIterator(seBase.getDataSet());
                }
                catch (Exception ex )
                {
                    throw new RenderingException(ex);
                }

                int i = 0;
                dY += insCA.getTop();
                while (dsiBase.hasNext())
                {
                    dX += insCA.getLeft();
                    la.getCaption().setValue(String.valueOf(dsiBase.next()));
                    itm.reuse(la);
                    fPaletteEntry = (Fill) elPaletteEntries.get(i++ % iPaletteCount); // CYCLE THROUGH THE PALETTE
                    lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                    renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(), insCA
                        .getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);
                    dX += itm.getFullWidth() + (3 * dItemHeight) / 2 + dHorizontalSpacing + insCA.getRight();
                }
            }
            else if (d.getValue() == Direction.TOP_BOTTOM)
            {
                double dMaxW = 0;
                dSeparatorThickness += dVerticalSpacing;
                for (int j = 0; j < seda.length; j++)
                {
                    dWidth = 0;
                    dY += insCA.getTop();
                    al = seda[j].getRunTimeSeries();
                    pa = seda[j].getSeriesPalette();
                    elPaletteEntries = pa.getEntries();
                    iPaletteCount = elPaletteEntries.size();

                    for (int i = 0; i < al.size(); i++)
                    {
                        seBase = (Series) al.get(i);
                        lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                        la.getCaption().setValue(String.valueOf(seBase.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        fPaletteEntry = (Fill) elPaletteEntries.get(i % iPaletteCount); // CYCLE THROUGH THE PALETTE
                        renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(),
                            insCA.getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);

                        // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING
                        // + MAX ITEM WIDTH + RIGHT INSETS
                        dX += insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + itm.getFullWidth()
                            + insCA.getRight();
                    }
                    dY += insCA.getTop() + dItemHeight + insCA.getRight(); // LINE
                    // FEED

                    // SETUP HORIZONTAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        dX = bo.getLeft(); // CARRIAGE RETURN
                        renderSeparator(ipr, lg, lia, dX, dY + dSeparatorThickness / 2, bo.getWidth(),
                            Orientation.HORIZONTAL_LITERAL);
                        dY += dSeparatorThickness;
                    }
                }
            }
            else if (d.getValue() == Direction.LEFT_RIGHT)
            {
                dSeparatorThickness += dHorizontalSpacing;
                dY += insCA.getTop();
                for (int j = 0; j < seda.length; j++)
                {
                    al = seda[j].getRunTimeSeries();
                    pa = seda[j].getSeriesPalette();
                    elPaletteEntries = pa.getEntries();
                    iPaletteCount = elPaletteEntries.size();

                    for (int i = 0; i < al.size(); i++)
                    {
                        seBase = (Series) al.get(i);
                        lirh = (LegendItemRenderingHints) htRenderers.get(seBase);
                        la.getCaption().setValue(String.valueOf(seBase.getSeriesIdentifier()));// TBD: APPLY FORMAT
                        // SPECIFIER
                        itm.reuse(la);
                        fPaletteEntry = (Fill) elPaletteEntries.get(i % iPaletteCount); // CYCLE THROUGH THE PALETTE
                        renderLegendItem(ipr, lg, la, dX, dY, itm.getFullWidth(), dItemHeight, itm.getFullHeight(),
                            insCA.getLeft(), dHorizontalSpacing, seBase, fPaletteEntry, lirh);

                        // LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING
                        // + MAX ITEM WIDTH + RIGHT INSETS
                        dX += insCA.getLeft() + (3 * dItemHeight) / 2 + dHorizontalSpacing + itm.getFullWidth()
                            + insCA.getRight();
                    }

                    // SETUP VERTICAL SEPARATOR SPACING
                    if (j < seda.length - 1)
                    {
                        renderSeparator(ipr, lg, lia, dX + dSeparatorThickness / 2, dY, bo.getHeight(),
                            Orientation.VERTICAL_LITERAL);
                        dX += dSeparatorThickness;
                    }
                }
            }
            else
            {
                throw new RenderingException("Invalid argument specified for legend rendering direction = " + d);
            }
        }
        else
        {
            throw new RenderingException("Invalid argument specified for legend rendering orientation = " + o);
        }
    }

    /**
     * Internally used to render a legend item separator
     * 
     * @param ipr
     * @param lg
     * @param dX
     * @param dY
     * @param dLength
     * @param o
     */
    protected static final void renderSeparator(IPrimitiveRenderer ipr, Legend lg, LineAttributes lia, double dX,
        double dY, double dLength, Orientation o) throws RenderingException
    {
        if (o.getValue() == Orientation.HORIZONTAL)
        {
            final LineRenderEvent lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(lg,
                LineRenderEvent.class);
            lre.setLineAttributes(lia);
            lre.setStart(LocationImpl.create(dX, dY));
            lre.setEnd(LocationImpl.create(dX + dLength, dY));
            ipr.drawLine(lre);
        }
        else if (o.getValue() == Orientation.VERTICAL)
        {
            final LineRenderEvent lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(lg,
                LineRenderEvent.class);
            lre.setLineAttributes(lia);
            lre.setStart(LocationImpl.create(dX, dY));
            lre.setEnd(LocationImpl.create(dX, dY + dLength));
            ipr.drawLine(lre);
        }
    }

    /**
     * Internally provided to render a single legend entry
     * 
     * @param ipr
     * @param lg
     * @param la
     * @param dX
     * @param dY
     * @param dW
     * @param dItemHeight
     * @param dLeftInset
     * @param dHorizontalSpacing
     * @param se
     * @param fPaletteEntry
     * @param lirh
     * @throws RenderingException
     */
    protected final void renderLegendItem(IPrimitiveRenderer ipr, Legend lg, Label la, double dX, double dY, double dW,
        double dItemHeight, double dFullHeight, double dLeftInset, double dHorizontalSpacing, Series se,
        Fill fPaletteEntry, LegendItemRenderingHints lirh) throws RenderingException
    {
        ScriptHandler sh = getModel().getScriptHandler();
        ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY, la);
        final Bounds bo = lirh.getLegendGraphicBounds();
        bo.setLeft(dX + dLeftInset + 1);
        bo.setTop(dY + 1);
        bo.setWidth(3 * dItemHeight / 2);
        bo.setHeight(dItemHeight - 2);

        final BaseRenderer br = lirh.getRenderer();
        br.renderLegendGraphic(ipr, lg, fPaletteEntry, bo);

        final ColorDefinition cdTransparent = ColorDefinitionImpl.TRANSPARENT();
        final TextRenderEvent tre = (TextRenderEvent) ((EventObjectCache) ir).getEventObject(lg, TextRenderEvent.class);
        tre.setLocation(LocationImpl.create(dX + dLeftInset + (3 * dItemHeight / 2) + dHorizontalSpacing, dY
            + dFullHeight / 2 - 1));
        tre.setTextPosition(TextRenderEvent.RIGHT);
        tre.setLabel(la);
        tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
        ipr.drawText(tre);

        // PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
        Action ac;
        Trigger tg;
        EList elTriggers = lg.getTriggers();
        Location[] loaHotspot = new Location[4];
        double dTextStartX = tre.getLocation().getX() - 1;
        loaHotspot[0] = LocationImpl.create(dTextStartX, dY);
        loaHotspot[1] = LocationImpl.create(dTextStartX + dW, dY + 1);
        loaHotspot[2] = LocationImpl.create(dTextStartX + dW, dY + dItemHeight);
        loaHotspot[3] = LocationImpl.create(dTextStartX, dY + dItemHeight);
        if (!elTriggers.isEmpty())
        {
            final InteractionEvent iev = (InteractionEvent) ((EventObjectCache) ipr).getEventObject(se,
                InteractionEvent.class);
            iev.reuse(se);
            for (int t = 0; t < elTriggers.size(); t++)
            {
                tg = (Trigger) EcoreUtil.copy((Trigger) elTriggers.get(t));
                if (tg.getCondition() == TriggerCondition.MOUSE_CLICK_LITERAL)
                {
                    iev.addTrigger(tg);
                }
            }
            final PolygonRenderEvent pre = (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(lg,
                PolygonRenderEvent.class);
            pre.setPoints(loaHotspot);
            iev.setHotSpot(pre);
            ipr.enableInteraction(iev);
        }
        ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_LEGEND_ENTRY, la);
    }

    /**
     * 
     * @param ipr
     * @param p
     * 
     * @throws RenderingException
     */
    public void renderPlot(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        final boolean bFirstInSequence = (iSeriesIndex == 0);
        final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);

        if (bFirstInSequence)
        {
            renderBackground(ipr, p);
        }

        ScriptHandler.callFunction(getModel().getScriptHandler(), ScriptHandler.BEFORE_DRAW_SERIES, getSeries(), this);
        renderSeries(ipr, p, srh); // CALLS THE APPROPRIATE SUBCLASS FOR
        ScriptHandler.callFunction(getModel().getScriptHandler(), ScriptHandler.AFTER_DRAW_SERIES, getSeries(), this);

        if (bLastInSequence)
        {
            // RENDER OVERLAYS HERE IF ANY
        }
    }

    /**
     * 
     * @param ipr
     * @param p
     * 
     * @throws RenderingException
     */
    protected void renderBackground(IPrimitiveRenderer ipr, Plot p) throws RenderingException
    {
        final double dScale = getDevice().getDisplayServer().getDpiResolution() / 72d;
        final RectangleRenderEvent rre = (RectangleRenderEvent) ((EventObjectCache) ipr).getEventObject(p,
            RectangleRenderEvent.class);
        rre.updateFrom(p, dScale); // POINTS => PIXELS
        ipr.fillRectangle(rre);
        ipr.drawRectangle(rre);

        Object oComputations = getComputations();
        if (oComputations instanceof PlotWithoutAxes)
        {
            final ClientArea ca = p.getClientArea();
            rre.setBackground(ca.getBackground());
            ipr.fillRectangle(rre);
            ipr.drawRectangle(rre);

            if (!ca.getOutline().isSetVisible())
            {
                throw new RenderingException("Client area outline visibility was not specified");
            }
            if (ca.getOutline().isVisible())
            {
                final Bounds bo = p.getBounds().scaledInstance(dScale); // POINTS
                // =>
                // PIXELS
                final PlotWithoutAxes pwoa = (PlotWithoutAxes) oComputations;
                final Size sz = SizeImpl.create(bo.getWidth() / pwoa.getColumnCount(), bo.getHeight()
                    / pwoa.getRowCount());
                final LineRenderEvent lre = (LineRenderEvent) ((EventObjectCache) ipr).getEventObject(ca,
                    LineRenderEvent.class);
                lre.setLineAttributes(ca.getOutline());
                for (int i = 1; i < pwoa.getColumnCount(); i++)
                {
                    lre.setStart(LocationImpl.create(bo.getLeft() + i * sz.getWidth(), bo.getTop()));
                    lre.setEnd(LocationImpl.create(bo.getLeft() + i * sz.getWidth(), bo.getTop() + bo.getHeight()));
                    ipr.drawLine(lre);
                }
                for (int j = 1; j < pwoa.getRowCount(); j++)
                {
                    lre.setStart(LocationImpl.create(bo.getLeft(), bo.getTop() + j * sz.getHeight()));
                    lre.setEnd(LocationImpl.create(bo.getLeft() + bo.getWidth(), bo.getTop() + j * sz.getHeight()));
                    ipr.drawLine(lre);
                }
            }
        }

    }

    /**
     * 
     * @param ipr
     * @param b
     * 
     * @throws RenderingException
     */
    protected void renderBlock(IPrimitiveRenderer ipr, Block b) throws RenderingException
    {
        final double dScale = getDevice().getDisplayServer().getDpiResolution() / 72d;
        final RectangleRenderEvent rre = (RectangleRenderEvent) ((EventObjectCache) ipr).getEventObject(b,
            RectangleRenderEvent.class);
        rre.updateFrom(b, dScale);
        ipr.fillRectangle(rre);
        ipr.drawRectangle(rre);
    }

    /**
     * 
     * @param ipr
     * @param b
     * 
     * @throws RenderingException
     */
    public void renderLabel(IPrimitiveRenderer ipr, Block b) throws RenderingException
    {
        renderBlock(ipr, b);
        final double dScale = getDevice().getDisplayServer().getDpiResolution() / 72d;
        final LabelBlock lb = (LabelBlock) b;
        final TextRenderEvent tre = (TextRenderEvent) ((EventObjectCache) ipr)
            .getEventObject(lb, TextRenderEvent.class);
        tre.updateFrom(lb, dScale);
        ipr.drawText(tre);
    }

    /**
     * 
     * @param ipr
     * @param b
     * 
     * @throws RenderingException
     */
    public void renderTitle(IPrimitiveRenderer ipr, Block b) throws RenderingException
    {
        renderLabel(ipr, b);
    }

    private static final BaseRenderer[] createEmptyInstance(Chart cm, Object oComputations)
    {
        final BaseRenderer[] brna = new BaseRenderer[1];
        final AxesRenderer ar = new EmptyWithAxes();
        ar.iSeriesIndex = 0;
        ar.set(cm, oComputations, null, null, null);
        brna[0] = ar;
        return brna;
    }

    /**
     * This method returns appropriate renderers for the given chart model. It uses extension points to identify a
     * renderer corresponding to a custom series.
     * 
     * @param ir
     * @param cm
     * @param oComputations
     * 
     * @return
     * @throws PluginException
     */
    public static final BaseRenderer[] instances(Chart cm, Object oComputations) throws PluginException
    {
        final PluginSettings ps = PluginSettings.instance();
        BaseRenderer[] brna = null;
        final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

        if (cm instanceof ChartWithAxes)
        {
            final ChartWithAxes cwa = (ChartWithAxes) cm;
            final Axis[] axa = cwa.getPrimaryBaseAxes();
            Axis axPrimaryBase = axa[0], ax;
            final Axis axaPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis(axPrimaryBase);
            final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, false);
            int iOverlayOrthogonalCount = axaOverlayOrthogonal.length;
            EList elSeries;
            Series se;
            AxesRenderer ar = null;
            ArrayList al = new ArrayList(), alRunTimeSeries;
            AllAxes aaxX = ((PlotWith2DAxes) oComputations).getAxes();
            EList elBase, elOrthogonal;
            SeriesDefinition sd = null;
            //final int iSeriesCount =
            // ((ChartWithAxes)cm).getSeries(IConstants.ORTHOGONAL).length;

            Boolean bShared;
            int iSI = 0; // SERIES INDEX COUNTER

            elBase = axPrimaryBase.getSeriesDefinitions();
            if (elBase.isEmpty()) // NO SERIES DEFINITIONS
            {
                return createEmptyInstance(cm, oComputations);
            }
            else
            {
                final SeriesDefinition sdBase = (SeriesDefinition) elBase.get(0); // ONLY 1 SERIES DEFINITION MAY BE
                // ASSOCIATED
                // WITH THE BASE AXIS
                alRunTimeSeries = sdBase.getRunTimeSeries();
                if (alRunTimeSeries.isEmpty())
                {
                    return createEmptyInstance(cm, oComputations);
                }
                se = (Series) alRunTimeSeries.get(0); // ONLY 1 SERIES MAY BE
                // ASSOCIATED WITH THE
                // BASE SERIES
                // DEFINITION
                ar = (se.getClass() == SeriesImpl.class) ? new EmptyWithAxes() : (AxesRenderer) ps.getRenderer(se
                    .getClass());
                ar.set(cm, oComputations, se, axPrimaryBase, sdBase); // INITIALIZE
                // THE
                // RENDERER
                ar.iSeriesIndex = iSI++;
                al.add(ar);

                final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
                for (int i = 0; i < axaOrthogonal.length; i++)
                {
                    elOrthogonal = axaOrthogonal[i].getSeriesDefinitions();
                    for (int j = 0; j < elOrthogonal.size(); j++)
                    {
                        sd = (SeriesDefinition) elOrthogonal.get(j);
                        alRunTimeSeries = sd.getRunTimeSeries();
                        for (int k = 0; k < alRunTimeSeries.size(); k++)
                        {
                            se = (Series) alRunTimeSeries.get(k);
                            ar = (se.getClass() == SeriesImpl.class) ? new EmptyWithAxes() : (AxesRenderer) ps
                                .getRenderer(se.getClass());
                            ar.set(cm, oComputations, se, axaOrthogonal[i], bPaletteByCategory ? sdBase : sd); // INITIALIZE
                            // THE
                            // RENDERER
                            ar.iSeriesIndex = iSI++;
                            al.add(ar);
                        }
                    }
                }

                // CONVERT INTO AN ARRAY AS REQUESTED
                brna = new BaseRenderer[iSI];
                for (int i = 0; i < iSI; i++)
                {
                    ar = (AxesRenderer) al.get(i);
                    ar.iSeriesCount = iSI;
                    brna[i] = ar;
                }
            }
        }
        else if (cm instanceof ChartWithoutAxes)
        {
            final ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
            EList elBase = cwoa.getSeriesDefinitions();
            EList elOrthogonal;
            SeriesDefinition sd, sdBase;
            ArrayList alRuntimeSeries;

            final Series[] sea = cwoa.getRunTimeSeries();

            BaseRenderer br;
            Class cRenderer;
            Series se;
            final int iSeriesCount = sea.length;
            brna = new BaseRenderer[iSeriesCount];
            int iSI = 0; // SERIES INDEX COUNTER

            for (int i = 0; i < elBase.size(); i++)
            {
                sdBase = (SeriesDefinition) elBase.get(i);
                alRuntimeSeries = sdBase.getRunTimeSeries();
                if (alRuntimeSeries.size() != 1) // CHECK FOR A SINGLE BASE
                // SERIES ONLY
                {
                    throw new PluginException("Base runtime series count (here " + alRuntimeSeries.size()
                        + ") for a chart without axes must 1");
                }
                se = (Series) alRuntimeSeries.get(0);
                brna[iSI] = (se.getClass() == SeriesImpl.class) ? new EmptyWithoutAxes() : ps
                    .getRenderer(se.getClass());
                brna[iSI].set(cm, oComputations, se, null, sdBase); // INITIALIZE
                // THE
                // RENDERER
                brna[iSI].iSeriesIndex = iSI++;

                elOrthogonal = ((SeriesDefinition) elBase.get(i)).getSeriesDefinitions();
                for (int j = 0; j < elOrthogonal.size(); j++)
                {
                    sd = (SeriesDefinition) elOrthogonal.get(j);
                    alRuntimeSeries = sd.getRunTimeSeries();
                    for (int k = 0; k < alRuntimeSeries.size(); k++)
                    {
                        se = (Series) alRuntimeSeries.get(k);
                        brna[iSI] = (se.getClass() == SeriesImpl.class) ? new EmptyWithoutAxes() : ps.getRenderer(se
                            .getClass());
                        brna[iSI].set(cm, oComputations, se, null, bPaletteByCategory ? sdBase : sd); // INITIALIZE
                        // THE
                        // RENDERER
                        brna[iSI].iSeriesIndex = iSI++;
                    }
                }
            }

            for (int k = 0; k < iSI; k++)
            {
                brna[k].iSeriesCount = iSI;
            }
        }

        return brna;
    }

    /**
     * The graphic element for a legend entry should be rendered by the series type implementer
     * 
     * @param ipr
     * @param lg
     * @param bo
     * 
     * @throws RenderingException
     */
    public abstract void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
        throws RenderingException;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.IModelAccess#getSeries()
     */
    public final Series getSeries()
    {
        return se;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.IModelAccess#getModel()
     */
    public final Chart getModel()
    {
        return cm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.IModelAccess#getComputations()
     */
    public final Object getComputations()
    {
        return oComputations;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.IModelAccess#getDevice()
     */
    public final IDeviceRenderer getDevice()
    {
        return ir;
    }

    /**
     * Renders a 2D or extruded 2D plane as necessary for a given front surface polygon. Takes into account the correct
     * z-ordering of each plane and applies basic lighting. This convenience method may be used by series type rendering
     * extensions if needed.
     * 
     * @param ipr
     *            A handle to the primitive rendering device
     * @param oSource
     *            The object wrapped in the polygon rendering event
     * @param loaFront
     *            The co-ordinates of the front face polygon
     * @param f
     *            The fill color for the front face
     * @param lia
     *            The edge color for the polygon
     * @param dSeriesThickness
     *            The thickness or the extrusion level (for 2.5D or 3D)
     * 
     * @throws RenderingException
     */
    protected final void renderPlane(IPrimitiveRenderer ipr, Object oSource, Location[] loaFront, Fill f,
        LineAttributes lia, ChartDimension cd, double dSeriesThickness, boolean bDeferred) throws RenderingException
    {
        PolygonRenderEvent pre;
        if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL)
        {
            // RENDER THE POLYGON
            pre = (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
            pre.setPoints(loaFront);
            pre.setBackground(f);
            pre.setOutline(lia);
            ipr.fillPolygon(pre);
            ipr.drawPolygon(pre);
            return;
        }

        final boolean bSolidColor = f instanceof ColorDefinition;
        Fill fDarker = null, fBrighter = null;
        if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH)
        {
            fDarker = f;
            if (fDarker instanceof ColorDefinition)
            {
                fDarker = ((ColorDefinition) fDarker).darker();
                /*
                 * ColorDefinition cdD = (ColorDefinition) fDarker; System.out.println("darker Creating color " +
                 * cdD.getRed() + ", " + cdD.getGreen() + ", " + cdD.getBlue());
                 */
            }
            fBrighter = f;
            if (fBrighter instanceof ColorDefinition)
            {
                fBrighter = ((ColorDefinition) fBrighter).brighter();
            }
        }

        final int nSides = loaFront.length;
        final Location[][] loaa = new Location[nSides + 1][];
        Location[] loa;
        double dY, dSmallestY = 0;
        for (int j, i = 0; i < nSides; i++)
        {
            j = i + 1;
            if (j >= loaFront.length)
                j = 0;
            loa = new Location[4];
            loa[0] = LocationImpl.create(loaFront[i].getX(), loaFront[i].getY());
            loa[1] = LocationImpl.create(loaFront[j].getX(), loaFront[j].getY());
            loa[2] = LocationImpl.create(loaFront[j].getX() + dSeriesThickness, loaFront[j].getY() - dSeriesThickness);
            loa[3] = LocationImpl.create(loaFront[i].getX() + dSeriesThickness, loaFront[i].getY() - dSeriesThickness);
            loaa[i] = loa;
        }
        loaa[nSides] = loaFront;

        // SORT ON MULTIPLE KEYS (GREATEST Y, SMALLEST X)
        double dI, dJ;
        Location[] loaI, loaJ, loaSwap;
        for (int i = 0; i < nSides - 1; i++)
        {
            loaI = loaa[i];
            for (int j = i + 1; j < nSides; j++)
            {
                loaJ = loaa[j];

                dI = getY(loaI, IConstants.AVERAGE);
                dJ = getY(loaJ, IConstants.AVERAGE);
                if (dJ > dI) // SWAP
                {
                    loaa[i] = loaJ;
                    loaa[j] = loaI;
                    loaI = loaJ;
                }
                else if (dJ == dI)
                {
                    dI = getX(loaI, IConstants.AVERAGE);
                    dJ = getX(loaJ, IConstants.AVERAGE);
                    if (dI > dJ)
                    {
                        loaa[i] = loaJ;
                        loaa[j] = loaI;
                        loaI = loaJ;
                    }
                }
            }
        }

        int iSmallestYIndex = 0;
        for (int i = 0; i < nSides; i++)
        {
            dY = getY(loaa[i], IConstants.AVERAGE);
            if (i == 0)
            {
                dSmallestY = dY;
            }
            else if (dSmallestY > dY)
            {
                dSmallestY = dY;
                iSmallestYIndex = i;
            }
        }

        ArrayList alModel = new ArrayList(nSides + 1);
        Fill fP;
        for (int i = 0; i <= nSides; i++)
        {
            pre = (PolygonRenderEvent) ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
            pre.setOutline(lia);
            pre.setPoints(loaa[i]);
            if (i < nSides) // OTHER SIDES (UNKNOWN ORDER) ARE DEEP
            {
                pre.setDepth(-dSeriesThickness);
            }
            else
            // FRONT FACE IS NOT DEEP
            {
                pre.setDepth(0);
            }
            if (i == nSides)
            {
                fP = f;
            }
            else if (i == iSmallestYIndex)
            {
                fP = fBrighter;
            }
            else
            {
                fP = fDarker;
            }
            pre.setBackground(fP);
            if (bDeferred)
            {
                alModel.add(pre.copy());
            }
            else
            {
                ipr.fillPolygon(pre);
            }

            if (i == nSides)
            {
            }
            else if (i == iSmallestYIndex)
            {
                // DRAW A TRANSLUCENT LIGHT GLASS PANE OVER THE BRIGHTER SURFACE (IF NOT A SOLID COLOR)
                if (!bSolidColor)
                {
                    pre.setBackground(LIGHT_GLASS);
                }
                if (bDeferred)
                {
                    alModel.add(pre.copy());
                }
                else
                {
                    ipr.fillPolygon(pre);
                }
            }
            else
            {
                // DRAW A TRANSLUCENT DARK GLASS PANE OVER THE DARKER SURFACE (IF NOT A SOLID COLOR)
                if (!bSolidColor)
                {
                    pre.setBackground(DARK_GLASS);
                }
                if (bDeferred)
                {
                    alModel.add(pre.copy());
                }
                else
                {
                    ipr.fillPolygon(pre);
                }
            }
            if (!bDeferred)
            {
                ipr.drawPolygon(pre);
            }
        }
        if (!alModel.isEmpty())
        {
            dc.addModel(new WrappedInstruction(getDeferredCache(), alModel, PrimitiveRenderEvent.FILL));
        }
    }

    /**
     * 
     * @param loa
     * @param iProperty
     * 
     * @return
     */
    public static final double getY(Location[] loa, int iProperty)
    {
        int iCount = loa.length;
        double dY = 0;
        if (iProperty == IConstants.MIN)
        {
            dY = loa[0].getY();
            for (int i = 1; i < iCount; i++)
            {
                dY = Math.min(dY, loa[i].getY());
            }
        }
        else if (iProperty == IConstants.MAX)
        {
            dY = loa[0].getY();
            for (int i = 1; i < iCount; i++)
            {
                dY = Math.max(dY, loa[i].getY());
            }
        }
        else if (iProperty == IConstants.AVERAGE)
        {
            for (int i = 0; i < iCount; i++)
            {
                dY += loa[i].getY();
            }
            dY /= iCount;
        }
        return dY;
    }

    /**
     * 
     * @param loa
     * @param iProperty
     * 
     * @return
     */
    public static final double getX(Location[] loa, int iProperty)
    {
        int iCount = loa.length;
        double dX = 0;
        if (iProperty == IConstants.MIN)
        {
            dX = loa[0].getX();
            for (int i = 1; i < iCount; i++)
            {
                dX = Math.min(dX, loa[i].getX());
            }
        }
        else if (iProperty == IConstants.MAX)
        {
            dX = loa[0].getX();
            for (int i = 1; i < iCount; i++)
            {
                dX = Math.max(dX, loa[i].getX());
            }
        }
        else if (iProperty == IConstants.AVERAGE)
        {
            for (int i = 0; i < iCount; i++)
            {
                dX += loa[i].getX();
            }
            dX /= iCount;
        }
        return dX;
    }

    /**
     * 
     * @param tg
     * @param dph
     */
    public void processTrigger(Trigger tg, DataPointHints dph)
    {
        if (tg.getAction().getType() == ActionType.SHOW_TOOLTIP_LITERAL) // BUILD THE VALUE
        {
            ((TooltipValue) tg.getAction().getValue()).setText(dph.getDisplayValue());
        }
        else if (tg.getAction().getType() == ActionType.URL_REDIRECT_LITERAL) // BUILD A URI
        {
            final URLValue uv = (URLValue) tg.getAction().getValue();
            final String sBaseURL = uv.getBaseUrl();
            final StringBuffer sb = new StringBuffer(sBaseURL);
            char c = '?';
            if (sBaseURL.indexOf(c) != -1)
            {
                c = '&';
            }
            if (uv.getBaseParameterName() != null)
            {
                sb.append(c);
                c = '&';
                sb.append(URLValueImpl.encode(uv.getBaseParameterName()));
                sb.append('=');
                sb.append(URLValueImpl.encode(dph.getBaseDisplayValue()));
            }

            if (uv.getValueParameterName() != null)
            {
                sb.append(c);
                c = '&';
                sb.append(URLValueImpl.encode(uv.getValueParameterName()));
                sb.append('=');
                sb.append(URLValueImpl.encode(dph.getOrthogonalDisplayValue()));
            }
            uv.setBaseUrl(sb.toString());
        }
    }

    /**
     * This method may ONLY be called in an event that the model instance is of type ChartWithoutAxes. It returns the
     * bounds of an individual cell (if the rendered plot is to be split into a grid).
     * 
     * @return
     */
    protected final Bounds getCellBounds()
    {
        final PlotWithoutAxes pwoa = (PlotWithoutAxes) getComputations();
        final Coordinates co = pwoa.getCellCoordinates(iSeriesIndex - 1);
        final int iColumnCount = pwoa.getColumnCount();
        final int iRowCount = pwoa.getRowCount();
        final Size sz = pwoa.getCellSize();

        Bounds bo = (Bounds) EcoreUtil.copy(pwoa.getBounds());
        bo.setLeft(bo.getLeft() + co.getColumn() * sz.getWidth());
        bo.setTop(bo.getTop() + co.getRow() * sz.getHeight());
        bo.setWidth(sz.getWidth());
        bo.setHeight(sz.getHeight());
        bo = bo.adjustedInstance(pwoa.getCellInsets());

        return bo;
    }

    /**
     * This convenience method renders the data point label alongwith the shadow If there's a need to render the data
     * point label and the shadow separately, each call should be made separately by calling into the primitive
     * rendering interface directly.
     */
    public final void renderLabel(Object oSource, int iTextRenderType, Label laDataPoint, Position lp, Location lo,
        Bounds bo) throws RenderingException
    {
        final IDeviceRenderer idr = getDevice();
        TextRenderEvent tre = (TextRenderEvent) ((EventObjectCache) idr).getEventObject(oSource, TextRenderEvent.class);
        if (iTextRenderType != TextRenderEvent.RENDER_TEXT_IN_BLOCK)
        {
            tre.setTextPosition(Methods.getLabelPosition(lp));
            tre.setLocation(lo);
        }
        else
        {
            tre.setBlockBounds(bo);
            tre.setBlockAlignment(null);
        }
        tre.setLabel(laDataPoint);
        tre.setAction(iTextRenderType);
        dc.addLabel(tre);
    }
}