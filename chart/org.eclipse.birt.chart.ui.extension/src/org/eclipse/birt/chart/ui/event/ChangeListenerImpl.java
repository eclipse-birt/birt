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

package org.eclipse.birt.chart.ui.event;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.data.SeriesDataSetSheetImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIManager;
import org.eclipse.emf.common.util.EList;

/**
 * @author Actuate Corporation
 *  
 */
public class ChangeListenerImpl implements IChangeListener
{

    int iSeriesCount = 0;

    public void initialize(Chart cModel, IUIManager uiManager)
    {
        EList series = getSeries(cModel);
        if (series == null)
            return;
        iSeriesCount = series.size();
        for (int iS = 0; iS < series.size(); iS++)
        {
            uiManager.addSeriesDataSheet(new SeriesDataSetSheetImpl());
        }

        if (cModel instanceof ChartWithAxes)
        {
            // TODO: Handle addition of default sheets for ChartWithAxes model
        }
        else if (cModel instanceof ChartWithoutAxes)
        {
            // TODO: Handle addition of default sheets for ChartWithoutAxes
            // model
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChangeListener#chartModified(org.eclipse.emf.common.notify.Notification,
     *      org.eclipse.birt.chart.ui.swt.IUIManager)
     */
    public void chartModified(IUIManager uiManager)
    {
        int iNewSeriesCount = 0;
        EList series = getSeries(uiManager.getCurrentModelState());

        iNewSeriesCount = series.size();
        if (iNewSeriesCount >= iSeriesCount)
        {
            for (int iNC = iSeriesCount; iNC > iNewSeriesCount; iNC++)
            {
                uiManager.addSeriesDataSheet(new SeriesDataSetSheetImpl());
            }
        }
        else
        {
            for (int iNC = iSeriesCount; iNC == iNewSeriesCount; iNC--)
            {
                uiManager.removeSeriesDataSheet();
            }
        }
        iSeriesCount = iNewSeriesCount;
    }

    /**
     * @param chart
     * @return
     */
    private EList getSeries(Chart chart)
    {
        EList series = null;
        if (chart instanceof ChartWithAxes)
        {
            EList axes = ((ChartWithAxes) chart).getAxes();
            for (int iA = 0; iA < axes.size(); iA++)
            {
                if (series == null)
                {
                    series = ((SeriesDefinition) ((Axis) axes.get(iA)).getSeriesDefinitions().get(0)).getSeries();
                }
                else
                {
                    series.addAll(((SeriesDefinition) ((Axis) axes.get(iA)).getSeriesDefinitions().get(0)).getSeries());
                }
            }
        }
        else if (chart instanceof ChartWithoutAxes)
        {
            series = ((SeriesDefinition) ((ChartWithoutAxes) chart).getSeriesDefinitions().get(0)).getSeries();
        }
        return series;
    }
}