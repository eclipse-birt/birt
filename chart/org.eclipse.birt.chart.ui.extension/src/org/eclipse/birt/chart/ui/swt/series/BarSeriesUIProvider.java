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

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.swt.composites.SeriesDataDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionBuilder;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 *  
 */
public class BarSeriesUIProvider implements ISeriesUIProvider
{
    private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.BarSeriesImpl";

    /**
     *  
     */
    public BarSeriesUIProvider()
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
        return new BarSeriesAttributeComposite(parent, SWT.NONE, series);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesDataSheet(org.eclipse.swt.widgets.Composite)
     */
    public Composite getSeriesDataSheet(Composite parent, Series series, IExpressionBuilder builder)
    {
        Query query = null;
        if (series.getDataDefinition().size() > 0)
        {
            query = ((Query) series.getDataDefinition().get(0));
        }
        else
        {
            query = QueryImpl.create("");
            series.getDataDefinition().add(query);
        }
        return new SeriesDataDefinitionComposite(parent, SWT.NONE, query, builder);
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