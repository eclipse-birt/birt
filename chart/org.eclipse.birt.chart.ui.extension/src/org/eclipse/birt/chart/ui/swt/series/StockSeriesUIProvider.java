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
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.composites.StockSeriesDataDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 *  
 */
public class StockSeriesUIProvider implements ISeriesUIProvider
{
    private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.StockSeriesImpl"; //$NON-NLS-1$

    /**
     *  
     */
    public StockSeriesUIProvider()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesAttributeSheet(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.birt.chart.model.component.Series)
     */
    public Composite getSeriesAttributeSheet(Composite parent, Series series)
    {
        return new StockSeriesAttributeComposite(parent, SWT.NONE, series);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesDataSheet(org.eclipse.swt.widgets.Composite)
     */
    public Composite getSeriesDataSheet(Composite parent, SeriesDefinition seriesdefinition,
        IUIServiceProvider builder, Object oContext)
    {
        return new StockSeriesDataDefinitionComposite(parent, SWT.NONE, seriesdefinition, builder, oContext);
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