/*******************************************************************************
 * Copyright (c) Oct 22, 2004 Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation {ADD
 * SUBSEQUENT AUTHOR & CONTRIBUTION}
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
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
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
public class LineChart implements IChartType
{

    private static final String sType = "Line Chart";

    private transient Image imgIcon = null;

    private transient Image imgStacked = null;

    private transient Image imgStackedWithDepth = null;

    private transient Image imgPercentStacked = null;

    private transient Image imgPercentStackedWithDepth = null;

    private transient Image imgSideBySide = null;

    private transient Image imgSideBySideWithDepth = null;

    private transient Image imgSideBySide3D = null;

    private transient ChartWithAxes newChart = null;

    private static final String[] saDimensions = new String[]
    {
        "2D", "2D With Depth"
    };

    public LineChart()
    {
        imgIcon = UIHelper.getImage("images/LineChartIcon.gif");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChartType#getName()
     */
    public String getName()
    {
        return sType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.IChartType#getImage()
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
            "Line Chart",
            "A line represents each series being rendered. Optional markers may be used to highlight the points for which data is being plotted. Stacked lines show each category point relative to the position of the line series data point below it. Stacked percent lines shows each data value plotted as a percentage of the total for the category.");
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
                imgStacked = UIHelper.getImage("images/StackedLineChartImage.gif");
                imgPercentStacked = UIHelper.getImage("images/PercentStackedLineChartImage.gif");
                imgSideBySide = UIHelper.getImage("images/SideBySideLineChartImage.gif");
            }
            else
            {
                imgStacked = UIHelper.getImage("images/HorizontalStackedLineChartImage.gif");
                imgPercentStacked = UIHelper.getImage("images/HorizontalPercentStackedLineChartImage.gif");
                imgSideBySide = UIHelper.getImage("images/HorizontalSideBySideLineChartImage.gif");
            }

            vSubTypes.add(new ChartSubTypeImpl("Stacked", imgStacked));
            vSubTypes.add(new ChartSubTypeImpl("Percent Stacked", imgPercentStacked));
            vSubTypes.add(new ChartSubTypeImpl("Overlay", imgSideBySide));
        }
        else if (sDimension.equals("2D With Depth"))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgStackedWithDepth = UIHelper.getImage("images/StackedLineChartWithDepthImage.gif");
                imgPercentStackedWithDepth = UIHelper.getImage("images/PercentStackedLineChartWithDepthImage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/SideBySideLineChartWithDepthImage.gif");
            }
            else
            {
                imgStackedWithDepth = UIHelper.getImage("images/HorizontalStackedLineChartWithDepthImage.gif");
                imgPercentStackedWithDepth = UIHelper
                    .getImage("images/HorizontalPercentStackedLineChartWithDepthImage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/HorizontalSideBySideLineChartWithDepthImage.gif");

            }

            vSubTypes.add(new ChartSubTypeImpl("Stacked", imgStackedWithDepth));
            vSubTypes.add(new ChartSubTypeImpl("Percent Stacked", imgPercentStackedWithDepth));
            vSubTypes.add(new ChartSubTypeImpl("Overlay", imgSideBySideWithDepth));
        }
        else if (sDimension.equals("3D"))
        {
            imgSideBySide3D = UIHelper.getImage("images/SideBySideLineChart3DImage.gif");

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

        newChart.getTitle().getLabel().getCaption().setValue("Line Chart Title");

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
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).setStacked(true);
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
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).setStacked(true);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
        }
        else if (sSubType.equalsIgnoreCase("Overlay"))
        {
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getTitle().getCaption().setValue(
                "Orthogonal Axis Title");

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getQuery().setDefinition("MyExpression(Table.Column)");
            sdY.getSeriesPalette().update(0);
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).setStacked(false);
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