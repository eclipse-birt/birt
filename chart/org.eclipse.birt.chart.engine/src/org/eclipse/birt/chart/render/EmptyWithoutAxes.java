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

import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;

/**
 *  
 */
public final class EmptyWithoutAxes extends BaseRenderer
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.BaseRenderer#renderSeries(org.eclipse.birt.chart.device.IPrimitiveRenderer,
     *      org.eclipse.birt.chart.model.layout.Plot, org.eclipse.birt.chart.render.ISeriesRenderingHints)
     */
    public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws RenderingException
    {
        // NOTE: This method is not used by the Empty renderer
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
            getClass().getName() + ": renderSeries() [" + (iSeriesIndex + 1) + "/" + iSeriesCount + "]");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.model.attribute.Bounds,
     *      org.eclipse.birt.chart.model.layout.Plot, org.eclipse.birt.chart.render.ISeriesRenderingHints)
     */
    public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws GenerationException
    {
        // NOTE: This method is not used by the Empty renderer
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.render.BaseRenderer#renderLegendGraphic(org.eclipse.birt.chart.device.IPrimitiveRenderer,
     *      org.eclipse.birt.chart.model.layout.Legend, org.eclipse.birt.chart.model.attribute.Fill,
     *      org.eclipse.birt.chart.model.attribute.Bounds)
     */
    public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
        throws RenderingException
    {
        // TODO Auto-generated method stub

    }
}