/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.ui.swt.ChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 *  
 */
public class PieChart implements IChartType
{

    private static final String sType = "Pie Chart";

    private transient Image imgIcon = null;

    private transient Image img2D = null;

    private transient Image img2DWithDepth = null;

    private transient ChartWithoutAxes newChart = null;

    private static final String[] saDimensions = new String[]
    {
        "2D", "2D With Depth"
    };

    public PieChart()
    {
        imgIcon = UIHelper.getImage("images/PieChartIcon.gif");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
     */
    public String getName()
    {
        return sType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
     */
    public Image getImage()
    {
        return imgIcon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
     */
    public IHelpContent getHelp()
    {
        return new HelpContentImpl(
            "Pie Chart",
            "Pie charts show data values for a series or category as a slice percentage of the entire pie. Multiple pie series are presented in a grid.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
     */
    public Collection getChartSubtypes(String sDimension, Orientation orientation)
    {
        Vector vSubTypes = new Vector();
        // Do not respond to requests for unknown orientations
        if (!orientation.equals(Orientation.VERTICAL_LITERAL))
        {
            return vSubTypes;
        }
        if (sDimension.equals("2D"))
        {
            img2D = UIHelper.getImage("images/PieChartImage.gif");

            vSubTypes.add(new ChartSubTypeImpl("Standard Pie Chart", img2D));
        }
        else if (sDimension.equals("2D With Depth"))
        {
            img2DWithDepth = UIHelper.getImage("images/PieChartWithDepthImage.gif");

            vSubTypes.add(new ChartSubTypeImpl("Standard Pie Chart", img2DWithDepth));
        }
        return vSubTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public Chart getModel(String sSubType, Orientation orientation, String sDimension)
    {
        if (newChart == null)
        {
            newChart = ChartWithoutAxesImpl.create();
            newChart.setType(sType);
            newChart.setSubType(sSubType);
            newChart.setDimension(getDimensionFor(sDimension));
        }
        else
        {
            return newChart;
        }

        newChart.getTitle().getLabel().getCaption().setValue("Pie Chart Title");

        SeriesDefinition sdX = SeriesDefinitionImpl.create();
        Series categorySeries = SeriesImpl.create();
        sdX.getSeries().add(categorySeries);
        sdX.getQuery().setDefinition("Base Series");

        SeriesDefinition sdY = SeriesDefinitionImpl.create();
        sdY.getQuery().setDefinition("MyExpression(Table.Column)");
        sdY.getSeriesPalette().update(0);
        Series valueSeries = PieSeriesImpl.create();
        valueSeries.getLabel().setVisible(true);
        valueSeries.setSeriesIdentifier("valueSeriesIdentifier");
        ((PieSeries) valueSeries).getTitle().getCaption().setValue("valueSeries");
        ((PieSeries) valueSeries).setStacked(false);
        sdY.getSeries().add(valueSeries);

        sdX.getSeriesDefinitions().add(sdY);

        newChart.getSeriesDefinitions().add(sdX);

        return newChart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
     */
    public String[] getSupportedDimensions()
    {
        return saDimensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
     */
    public String getDefaultDimension()
    {
        return saDimensions[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
     */
    public boolean supportsTransposition()
    {
        return false;
    }

    private ChartDimension getDimensionFor(String sDimension)
    {
        if (sDimension == null || sDimension.equals("2D"))
        {
            return ChartDimension.TWO_DIMENSIONAL_LITERAL;
        }
        else
        {
            return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
        }
    }
}