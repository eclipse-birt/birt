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

import java.util.LinkedHashMap;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;

/**
 *  
 */
public final class GeneratedChartState
{

    /**
     *  
     */
    private final LinkedHashMap _lhmRenderers;

    /**
     *  
     */
    private final Object _oComputations;

    /**
     *  
     */
    private final IDisplayServer _ids;

    /**
     *  
     */
    private final Chart _cm;

    /**
     *  
     */
    private final int _iType;
    
    /**
     * 
     */
    private final RunTimeContext _rtc;

    /**
     * 
     * @param xs
     * @param cm
     * @param lhmRenderers
     * @param oComputations
     */
    GeneratedChartState(IDisplayServer xs, Chart cm, LinkedHashMap lhmRenderers, RunTimeContext rtc, Object oComputations)
    {
        _lhmRenderers = lhmRenderers;
        _oComputations = oComputations;
        _ids = xs;
        _cm = cm;
        _rtc = rtc;
        if (cm instanceof ChartWithAxes)
        {
            _iType = Generator.WITH_AXES;
        }
        else if (cm instanceof ChartWithoutAxes)
        {
            _iType = Generator.WITHOUT_AXES;
        }
        else
        {
            _iType = IConstants.UNDEFINED;
        }
    }

    /**
     * 
     * @return
     */
    public final LinkedHashMap getRenderers()
    {
        return _lhmRenderers;
    }

    /**
     * 
     * @return
     */
    public final Object getComputations()
    {
        return _oComputations;
    }

    /**
     * 
     * @return
     */
    public final IDisplayServer getDisplayServer()
    {
        return _ids;
    }

    /**
     * 
     * @return
     */
    public final Chart getChartModel()
    {
        return _cm;
    }

    /**
     * 
     * @return
     */
    public final RunTimeContext getRunTimeContext()
    {
        return _rtc;
    }
    
    /**
     * 
     * @return
     */
    public final int getType()
    {
        return _iType;
    }
}