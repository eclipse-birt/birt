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

package org.eclipse.birt.chart.factory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.withaxes.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.OverlapException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.exception.ScriptException;
import org.eclipse.birt.chart.exception.UnsupportedFeatureException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutManager;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.mozilla.javascript.Scriptable;

/**
 * Provides an entry point into building a chart for a given model. It is implemented as a singleton
 * and does not maintain any state information hence allowing multi-threaded requests for a single
 * generator instance.
 */
public final class Generator
{

    /**
     * Internally used. 
     */
    private static final int UNDEFINED = IConstants.UNDEFINED;

    /**
     * Internally used.
     */
    static final int WITH_AXES = 1;

    /**
     * Internally used to maintain type information.
     */
    static final int WITHOUT_AXES = 2;

    /**
     * Internally used.
     */
    private static final LegendItemRenderingHints[] EMPTY_LIRHA = new LegendItemRenderingHints[0];

    /**
     * The internal singleton Generator reference created lazily.
     */
    private static Generator g = null;

    /**
     * A private constructor.
     */
    private Generator()
    {

    }

    /**
     * Returns a singleton instance of the chart generator.
     * 
     * @return A singleton instance for the chart generator.
     */
    public static synchronized final Generator instance()
    {
        if (g == null)
        {
            g = new Generator();
        }
        return g;
    }

    /**
     * Builds and computes preferred sizes of various chart components offscreen
     * using the provided display server.
     *  
     * @param ids           A display server using which the chart may be built.
     * @param cmDesignTime  The design time chart model (bound to a dataset).
     * @param scParent      A parent script handler that may be attached to the existing chart model script handler.
     * @param bo            The bounds associated with the chart being built.
     * @param rtc           Encapsulates the runtime environment for the build process.
     * 
     * @return  An instance of a generated chart state that encapsulates built chart information that may be subsequently rendered.
     * 
     * @throws GenerationException
     */
    public final GeneratedChartState build(IDisplayServer ids, Chart cmDesignTime,
        Scriptable scParent, Bounds bo, RunTimeContext rtc) throws GenerationException
    {
        if (ids == null || cmDesignTime == null || bo == null)
        {
            throw new GenerationException("Illegal 'null' value passed as an argument to build a chart");
        }
        if (rtc == null)
        {
            rtc = new RunTimeContext();
        }

        if (cmDesignTime.getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL)
        {
            throw new GenerationException(new UnsupportedFeatureException("3D charts are not yet supported"));
        }

        final Chart cmRunTime = (Chart) EcoreUtil.copy(cmDesignTime);
        if (rtc.getLocale() == null)
        {
            rtc.setLocale(Locale.getDefault());
        }

        final String sScriptContent = cmRunTime.getScript();
        ScriptHandler sh = rtc.getScriptHandler();
        if (sh == null && sScriptContent != null) // NOT PREVIOUSLY DEFINED BY REPORTITEM ADAPTER
        {
            sh = new ScriptHandler();
            try
            {
                sh.init(scParent);
                sh.setRunTimeModel(cmRunTime);
                rtc.setScriptHandler(sh);
                sh.register(sScriptContent);
            }
            catch (ScriptException sx )
            {
                throw new GenerationException(sx);
            }
        }
        else if (sh != null) // COPY SCRIPTS FROM DESIGNTIME TO RUNTIME INSTANCE
        {
            rtc.setScriptHandler(sh);
        }
        rtc.setScriptHandler(sh);

        // SETUP THE COMPUTATIONS
        ScriptHandler.callFunction(sh, ScriptHandler.START_GENERATION, cmRunTime);
        int iChartType = UNDEFINED;
        Object oComputations = null;
        if (cmRunTime instanceof ChartWithAxes)
        {
            iChartType = WITH_AXES;
            oComputations = new PlotWith2DAxes(ids, (ChartWithAxes) cmRunTime, rtc);
        }
        else if (cmRunTime instanceof ChartWithoutAxes)
        {
            iChartType = WITHOUT_AXES;
            oComputations = new PlotWithoutAxes(ids, (ChartWithoutAxes) cmRunTime, rtc);
        }

        if (oComputations == null)
        {
            throw new GenerationException("Unable to compute chart defined by model [{0}]" + cmRunTime ); // i18n_CONCATENATIONS_REMOVED
        }

        // OBTAIN THE RENDERERS
        final ILogger il = DefaultLoggerImpl.instance();
        final LinkedHashMap lhmRenderers = new LinkedHashMap();
        BaseRenderer[] brna = null;
        try
        {
            brna = BaseRenderer.instances(cmRunTime, rtc, oComputations);
            for (int i = 0; i < brna.length; i++)
            {
                lhmRenderers.put(brna[i].getSeries(), new LegendItemRenderingHints(brna[i], BoundsImpl.create(0, 0, 0,
                    0)));
            }
        }
        catch (Exception ex )
        {
            ex.printStackTrace();
            throw new GenerationException(ex);
        }

        // PERFORM THE BLOCKS' LAYOUT
        Block bl = cmRunTime.getBlock();
        final LayoutManager lm = new LayoutManager(bl);
        ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_LAYOUT, cmRunTime);
        try
        {
            lm.doLayout_tmp(ids, cmRunTime, bo, rtc);
        }
        catch (OverlapException oex )
        {
            throw new GenerationException(oex);
        }
        ScriptHandler.callFunction(sh, ScriptHandler.AFTER_LAYOUT, cmRunTime);

        // COMPUTE THE PLOT AREA
        Bounds boPlot = cmRunTime.getPlot().getBounds();
        Insets insPlot = cmRunTime.getPlot().getInsets();
        boPlot = boPlot.adjustedInstance(insPlot);

        ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_COMPUTATIONS, cmRunTime, oComputations);
        long lTimer = System.currentTimeMillis();
        if (iChartType == WITH_AXES)
        {
            PlotWith2DAxes pwa = (PlotWith2DAxes) oComputations;
            try
            {
                pwa.compute(boPlot);
            }
            catch (Exception ex )
            {
                ex.printStackTrace();
                throw new GenerationException(ex);
            }
        }
        else if (iChartType == WITHOUT_AXES)
        {
            PlotWithoutAxes pwoa = (PlotWithoutAxes) oComputations;
            try
            {
                pwoa.compute(boPlot);
            }
            catch (Exception ex )
            {
                throw new GenerationException(ex);
            }
        }
        ScriptHandler.callFunction(sh, ScriptHandler.AFTER_COMPUTATIONS, cmRunTime, oComputations);

        final Collection co = lhmRenderers.values();
        final LegendItemRenderingHints[] lirha = (LegendItemRenderingHints[]) co.toArray(EMPTY_LIRHA);
        final int iSize = lhmRenderers.size();
        BaseRenderer br;

        for (int i = 0; i < iSize; i++)
        {
            br = lirha[i].getRenderer();
            br.set(brna);
            br.set(ids);
            br.set(rtc);
            try
            {
                if (br.getComputations() instanceof PlotWithoutAxes)
                {
                    br.set(((PlotWithoutAxes) br.getComputations()).getSeriesRenderingHints(br.getSeries()));
                }
                else
                {
                    br.set(((PlotWith2DAxes) br.getComputations()).getSeriesRenderingHints(br.getSeriesDefinition(), br.getSeries()));
                }
                ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_COMPUTE_SERIES, br.getSeries());
                br.compute(bo, cmRunTime.getPlot(), br.getSeriesRenderingHints());
                ScriptHandler.callFunction(sh, ScriptHandler.AFTER_COMPUTE_SERIES, br.getSeries());
            }
            catch (Exception ex )
            {
                throw new GenerationException(ex);
            }
        }
        il.log(ILogger.INFORMATION, "Time to compute plot (without axes) = {0} ms" + (System.currentTimeMillis() - lTimer) ); // i18n_CONCATENATIONS_REMOVED
        final GeneratedChartState gcs = new GeneratedChartState(ids, cmRunTime, lhmRenderers, rtc, oComputations);
        if (sh != null)
        {
            sh.setGeneratedChartState(gcs);
            ScriptHandler.callFunction(sh, ScriptHandler.FINISH_GENERATION, gcs);
        }
        return gcs;
    }

    /**
     * Performs a minimal rebuild of the chart if non-sizing attributes are altered or the dataset for any series has changed. 
     * However, if sizing attribute changes occur that affects the relative position of the various chart subcomponents,
     * a re-build is required.
     * 
     * @param   gcs   A previously built chart encapsulated in a transient structure.
     * 
     * @throws GenerationException
     */
    public final void refresh(GeneratedChartState gcs) throws GenerationException
    {
        Chart cm = gcs.getChartModel();
        ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_COMPUTATIONS, gcs);

        // COMPUTE THE PLOT AREA
        long lTimer = System.currentTimeMillis();
        int iChartType = gcs.getType();
        Bounds boPlot = cm.getPlot().getBounds();
        Insets insPlot = cm.getPlot().getInsets();
        boPlot = boPlot.adjustedInstance(insPlot);

        final ILogger il = DefaultLoggerImpl.instance();
        if (iChartType == WITH_AXES)
        {
            PlotWith2DAxes pwa = (PlotWith2DAxes) gcs.getComputations();
            try
            {
                pwa.compute(boPlot);
            }
            catch (Exception ex )
            {
                throw new GenerationException(ex);
            }
            il.log(ILogger.INFORMATION, "Time to compute plot (with axes) = {0} ms" + (System.currentTimeMillis() - lTimer) ); // i18n_CONCATENATIONS_REMOVED
        }
        else if (iChartType == WITHOUT_AXES)
        {
            PlotWithoutAxes pwoa = (PlotWithoutAxes) gcs.getComputations();
            try
            {
                pwoa.compute(boPlot);
            }
            catch (Exception ex )
            {
                throw new GenerationException(ex);
            }
            il.log(ILogger.INFORMATION, "Time to compute plot (without axes) = {0} ms" + (System.currentTimeMillis() - lTimer) ); // i18n_CONCATENATIONS_REMOVED
        }
        ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_COMPUTATIONS, gcs);
    }

    /**
     * Draws a previously built chart using the specified device renderer into a target output device.
     * 
     * @param   idr     A device renderer that determines the target context on which the chart will be rendered.
     * @param   gcs     A previously built chart that needs to be rendered.
     * 
     * @throws GenerationException
     */
    public final void render(IDeviceRenderer idr, GeneratedChartState gcs) throws RenderingException
    {
        final Chart cm = gcs.getChartModel();
        ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.START_RENDERING, gcs);

        Legend lg = cm.getLegend();
        lg.updateLayout(cm); // RE-ORGANIZE BLOCKS IF REQUIRED
        if (lg.getPosition() == Position.INSIDE_LITERAL)
        {
            int iType = gcs.getType();
            if (iType == WITH_AXES)
            {
                Bounds bo = ((PlotWith2DAxes) gcs.getComputations()).getPlotBounds();
                try
                {
                    updateLegendInside(bo, lg, idr.getDisplayServer(), cm, gcs.getRunTimeContext());
                }
                catch (GenerationException gex )
                {
                    throw new RenderingException(gex);
                }
            }
        }

        final LinkedHashMap lhm = (LinkedHashMap) gcs.getRenderers();
        final int iSize = lhm.size();

        BaseRenderer br;
        final Collection co = lhm.values();
        final LegendItemRenderingHints[] lirha = (LegendItemRenderingHints[]) co.toArray(EMPTY_LIRHA);
        final DeferredCache dc = new DeferredCache(idr, cm); // USED IN RENDERING ELEMENTS WITH THE CORRECT Z-ORDER

        // USE SAME BOUNDS FOR RENDERING AS THOSE USED TO PREVIOUSLY COMPUTE THE CHART OFFSCREEN
        final Bounds bo = gcs.getChartModel().getBlock().getBounds();
        idr.setProperty(IDeviceRenderer.EXPECTED_BOUNDS, bo.scaledInstance(idr.getDisplayServer().getDpiResolution() / 72d));
        
        // UPDATE THE STRUCTURE DEFINITION LISTENER MAINTAINED BY THE RUNTIME CONTEXT
        gcs.getRunTimeContext().setStructureDefinitionListener(idr.needsStructureDefinition() ? idr : null);
        
        idr.before(); // INITIALIZATION BEFORE RENDERING BEGINS
        for (int i = 0; i < iSize; i++)
        {
            br = lirha[i].getRenderer();
            br.set(dc);
            br.set(idr);
            br.set(gcs.getRunTimeContext());
            try
            {
                br.render(lhm, bo); // 'bo' MUST BE CLIENT AREA WITHIN ANY
                // 'shell' OR 'frame'
            }
            catch (Exception ex )
            {
                ex.printStackTrace();
                throw new RenderingException(ex);
            }
        }
        idr.after(); // ANY CLEANUP AFTER THE CHART HAS BEEN RENDERED
        ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.FINISH_RENDERING, gcs);
    }

    /**
     * Internally updates a legend position by altering the legend block
     * hierarchy as needed.
     * 
     * @param   boContainer     The internal bounds of the client area contained within the plot.
     * @param   lg              An instance of the legend for which the position is being updated.
     * @param   ids             An instance of the display server associated with building the chart.
     * @param   cm              An instance of the chart model for which the legend position is updated.
     * 
     * @throws GenerationException
     */
    private static final void updateLegendInside(Bounds boContainer, Legend lg, IDisplayServer ids, Chart cm, RunTimeContext rtc)
        throws GenerationException
    {
        final double dScale = ids.getDpiResolution() / 72d;

        double dX, dY;
        final Size sz = lg.getPreferredSize(ids, cm, rtc);
        boContainer = boContainer.scaledInstance(1d / dScale);

        // USE ANCHOR IN POSITIONING THE LEGEND CLIENT AREA WITHIN THE BLOCK
        // SLACK SPACE
        if (lg.isSetAnchor())
        {
            final int iAnchor = lg.getAnchor().getValue();
            switch (iAnchor)
            {
                case Anchor.NORTH:
                case Anchor.NORTH_EAST:
                case Anchor.NORTH_WEST:
                    dY = boContainer.getTop();
                    break;

                case Anchor.SOUTH:
                case Anchor.SOUTH_EAST:
                case Anchor.SOUTH_WEST:
                    dY = boContainer.getTop() + boContainer.getHeight() - sz.getHeight();
                    break;

                default: // CENTERED
                    dY = boContainer.getTop() + (boContainer.getHeight() - sz.getHeight()) / 2;
                    break;
            }

            switch (iAnchor)
            {
                case Anchor.WEST:
                case Anchor.SOUTH_WEST:
                case Anchor.NORTH_WEST:
                    dX = boContainer.getLeft();
                    break;

                case Anchor.NORTH_EAST:
                case Anchor.EAST:
                case Anchor.SOUTH_EAST:
                    dX = boContainer.getLeft() + boContainer.getWidth() - sz.getWidth();
                    break;

                default: // CENTERED
                    dX = boContainer.getLeft() + (boContainer.getWidth() - sz.getWidth()) / 2;
                    break;
            }
        }
        else
        {
            dX = boContainer.getLeft() + (boContainer.getWidth() - sz.getWidth()) / 2;
            dY = boContainer.getTop() + (boContainer.getHeight() - sz.getHeight()) / 2;
        }

        lg.getBounds().set(dX, dY, sz.getWidth(), sz.getHeight());
    }
}
