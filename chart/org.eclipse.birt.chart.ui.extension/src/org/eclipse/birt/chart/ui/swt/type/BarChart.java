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
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
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
public class BarChart implements IChartType
{

    private static final String sType = "Bar Chart";

    private transient Image imgIcon = null;

    private transient Image imgStacked = null;

    private transient Image imgStackedWithDepth = null;

    private transient Image imgPercentStacked = null;

    private transient Image imgPercentStackedWithDepth = null;

    private transient Image imgSideBySide = null;

    private transient Image imgSideBySideWithDepth = null;

    private transient Image imgSideBySide3D = null;

    private ChartWithAxes newChart = null;

    private static final String[] saDimensions = new String[]
    {
        "2D", "2D With Depth"
    };

    public BarChart()
    {
        imgIcon = UIHelper.getImage("images/BarChartIcon.gif");
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
            "Bar Chart",
            "Bar charts are created for comparing classes or groups of data. Data values for different series in the same category appear adjacent to each other in separate columns or stacked as sections in a single column (depending on the sub type chosen). A percent bar chart would present each bar as a percent of the total for that category.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
     */
    public Collection getChartSubtypes(String sDimension, Orientation orientation)
    {
        Vector vSubTypes = new Vector();
        if (sDimension.equals("2D"))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgStacked = UIHelper.getImage("images/StackedBarChartImage.gif");
                imgPercentStacked = UIHelper.getImage("images/PercentStackedBarChartImage.gif");
                imgSideBySide = UIHelper.getImage("images/SideBySideBarChartImage.gif");
            }
            else
            {
                imgStacked = UIHelper.getImage("images/HorizontalStackedBarChartImage.gif");
                imgPercentStacked = UIHelper.getImage("images/HorizontalPercentStackedBarChartImage.gif");
                imgSideBySide = UIHelper.getImage("images/HorizontalSideBySideBarChartImage.gif");
            }

            vSubTypes.add(new ChartSubTypeImpl("Stacked", imgStacked));
            vSubTypes.add(new ChartSubTypeImpl("Percent Stacked", imgPercentStacked));
            vSubTypes.add(new ChartSubTypeImpl("Side-by-side", imgSideBySide));
        }
        else if (sDimension.equals("2D With Depth"))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgStackedWithDepth = UIHelper.getImage("images/StackedBarChartWithDepthImage.gif");
                imgPercentStackedWithDepth = UIHelper.getImage("images/PercentStackedBarChartWithDepthImage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/SideBySideBarChartWithDepthImage.gif");
            }
            else
            {
                imgStackedWithDepth = UIHelper.getImage("images/HorizontalStackedBarChartWithDepthImage.gif");
                imgPercentStackedWithDepth = UIHelper
                    .getImage("images/HorizontalPercentStackedBarChartWithDepthImage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/HorizontalSideBySideBarChartWithDepthImage.gif");
            }
            vSubTypes.add(new ChartSubTypeImpl("Stacked", imgStackedWithDepth));
            vSubTypes.add(new ChartSubTypeImpl("Percent Stacked", imgPercentStackedWithDepth));
            vSubTypes.add(new ChartSubTypeImpl("Side-by-side", imgSideBySideWithDepth));
        }
        else if (sDimension.equals("3D"))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgSideBySide3D = UIHelper.getImage("images/SideBySideBarChart3DImage.gif");
            }
            else
            {
                imgSideBySide3D = UIHelper.getImage("images/HorizontalSideBySideBarChart3DImage.gif");
            }
            vSubTypes.add(new ChartSubTypeImpl("Side-by-side", imgSideBySide3D));
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
            newChart = ChartWithAxesImpl.create();
            newChart.setType(sType);
            newChart.setSubType(sSubType);
            newChart.setOrientation(orientation);
            newChart.setDimension(getDimensionFor(sDimension));
        }
        else
        {
            return newChart;
        }

        ((Axis) newChart.getAxes().get(0)).setOrientation(Orientation.HORIZONTAL_LITERAL);
        ((Axis) newChart.getAxes().get(0)).setType(AxisType.TEXT_LITERAL);

        ((Axis) newChart.getAxes().get(0)).getTitle().getCaption().setValue("Base Axis Title");

        SeriesDefinition sdX = SeriesDefinitionImpl.create();
        Series categorySeries = SeriesImpl.create();
        sdX.getSeries().add(categorySeries);
        ((Axis) newChart.getAxes().get(0)).getSeriesDefinitions().add(sdX);

        newChart.getTitle().getLabel().getCaption().setValue("Bar Chart Title");

        if (sSubType.equalsIgnoreCase("Stacked"))
        {
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getTitle().getCaption().setValue(
                "Orthogonal Axis Title");

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getQuery().setDefinition("MyExpression(Table.Column)");
            sdY.getSeriesPalette().update(0);
            Series valueSeries = BarSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            valueSeries.setStacked(true);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
        }
        else if (sSubType.equalsIgnoreCase("Percent Stacked"))
        {
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setPercent(true);

            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getTitle().getCaption().setValue(
                "Orthogonal Axis Title");

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getQuery().setDefinition("MyExpression(Table.Column)");
            sdY.getSeriesPalette().update(0);
            Series valueSeries = BarSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            valueSeries.setStacked(true);
            ((BarSeries) valueSeries).setStacked(true);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
        }
        else if (sSubType.equalsIgnoreCase("Side-by-side"))
        {
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getTitle().getCaption().setValue(
                "Orthogonal Axis Title");

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getQuery().setDefinition("MyExpression(Table.Column)");
            sdY.getSeriesPalette().update(0);
            Series valueSeries = BarSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((BarSeries) valueSeries).setStacked(false);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
        }
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
        return true;
    }

    private ChartDimension getDimensionFor(String sDimension)
    {
        if (sDimension == null || sDimension.equals("2D"))
        {
            return ChartDimension.TWO_DIMENSIONAL_LITERAL;
        }
        if (sDimension.equals("3D"))
        {
            return ChartDimension.THREE_DIMENSIONAL_LITERAL;
        }
        else
        {
            return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
        }
    }
}