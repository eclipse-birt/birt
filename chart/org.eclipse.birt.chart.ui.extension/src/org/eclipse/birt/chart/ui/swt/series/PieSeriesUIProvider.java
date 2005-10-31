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
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.BlankSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 *  
 */
public class PieSeriesUIProvider implements ISeriesUIProvider
{
    private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.PieSeriesImpl"; //$NON-NLS-1$

    /**
     *  
     */
    public PieSeriesUIProvider()
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
        return new PieSeriesAttributeComposite(parent, SWT.NONE, series);
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
        // If container of container is chart, it is a base series
        if (seriesdefinition.eContainer().eContainer() instanceof Chart)
        {
            sPrefix = Messages.getString("PieSeriesUIProvider.Lbl.BasePrefix"); //$NON-NLS-1$
        }
        else
        {
            sPrefix = Messages.getString("PieSeriesUIProvider.Lbl.OrthogonalPrefix"); //$NON-NLS-1$
        }

        String sTitle = query.getDefinition();
        if (sTitle == null || "".equals(sTitle)) //$NON-NLS-1$
        {
            sTitle = sPrefix + Messages.getString("PieSeriesUIProvider.Lbl.SeriesDefinition"); //$NON-NLS-1$
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
    
    public ISelectDataComponent getSeriesDataComponent( int seriesType,
			SeriesDefinition seriesDefn, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		if ( seriesType == ISelectDataCustomizeUI.ORTHOGONAL_SERIES )
		{
			return new BaseDataDefinitionComponent( seriesDefn,
					ChartUIUtil.getDataQuery( seriesDefn, 0 ),
					builder,
					oContext,
					sTitle );
		}
		else if ( seriesType == ISelectDataCustomizeUI.GROUPING_SERIES )
		{
			return new BaseDataDefinitionComponent( seriesDefn,
					seriesDefn.getQuery( ),
					builder,
					oContext,
					sTitle );
		}
		return new BlankSelectDataComponent( );
	}
}