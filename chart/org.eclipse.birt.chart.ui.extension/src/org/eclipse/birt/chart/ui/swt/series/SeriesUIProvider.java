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
package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.DataDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 *  
 */
public class SeriesUIProvider implements ISeriesUIProvider
{
    private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.component.impl.SeriesImpl"; //$NON-NLS-1$

    /**
     *  
     */
    public SeriesUIProvider()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesAttributeSheet(org.eclipse.swt.widgets.Composite)
     */
    public Composite getSeriesAttributeSheet(Composite parent, Series series)
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesDataSheet(org.eclipse.swt.widgets.Composite)
     */
    public Composite getSeriesDataSheet(Composite parent, SeriesDefinition seriesdefinition,
        IUIServiceProvider builder, Object oContext)
    {
        Query query = null;
        if (seriesdefinition.getDesignTimeSeries().getDataDefinition().size() > 0)
        {
            query = ((Query) seriesdefinition.getDesignTimeSeries().getDataDefinition().get(0));
        }
        else
        {
            query = QueryImpl.create(""); //$NON-NLS-1$
            seriesdefinition.getDesignTimeSeries().getDataDefinition().add(query);
        }

        String sPrefix = ""; //$NON-NLS-1$
        // If container is Axis, chart is of type ChartWithAxes
        if (seriesdefinition.eContainer() instanceof org.eclipse.birt.chart.model.component.impl.AxisImpl)
        {
            // If container of container is Chart, series is Base Series
            if (seriesdefinition.eContainer().eContainer() instanceof Chart)
            {
                sPrefix = "X "; //$NON-NLS-1$
            }
            else
            {
                sPrefix = "Y "; //$NON-NLS-1$
            }
        }
        else
        {
            // If container of container is Chart, series is Base Series
            if (seriesdefinition.eContainer().eContainer() instanceof Chart)
            {
                sPrefix = Messages.getString("SeriesUIProvider.Lbl.BasePrefix"); //$NON-NLS-1$
            }
            else
            {
                sPrefix = Messages.getString("SeriesUIProvider.Lbl.OrthogonalPrefix"); //$NON-NLS-1$
            }
        }

        String sTitle = query.getDefinition();
        if (sTitle == null || "".equals(sTitle)) //$NON-NLS-1$
        {
            sTitle = sPrefix + Messages.getString("SeriesUIProvider.Lbl.SeriesDefinition"); //$NON-NLS-1$
        }
        else
        {
            sTitle = sPrefix + "Series Definition (" + sTitle + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        return new DataDefinitionComposite(parent, SWT.NONE, query, seriesdefinition, builder, oContext, sTitle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesClass()
     */
    public String getSeriesClass()
    {
        return SERIES_CLASS;
    }
}