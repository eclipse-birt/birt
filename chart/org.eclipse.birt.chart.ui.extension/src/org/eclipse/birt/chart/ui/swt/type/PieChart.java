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
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
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

    private static final String sStandardDescription = "Pie charts show values as slices of a pie. The size of each slice is proportional to the value it represents. Pie charts for multiple series are plotted as multiple pies, one for each series.";

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
        if (sDimension.equals("2D") || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName()))
        {
            img2D = UIHelper.getImage("images/PieChartImage.gif");

            vSubTypes.add(new DefaultChartSubTypeImpl("Standard Pie Chart", img2D, sStandardDescription));
        }
        else if (sDimension.equals("2D With Depth")
            || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName()))
        {
            img2DWithDepth = UIHelper.getImage("images/PieChartWithDepthImage.gif");

            vSubTypes.add(new DefaultChartSubTypeImpl("Standard Pie Chart", img2DWithDepth, sStandardDescription));
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
        sdY.getQuery().setDefinition("Expr(\"Column\")");
        sdY.getSeriesPalette().update(0);
        Series valueSeries = PieSeriesImpl.create();
        valueSeries.getLabel().setVisible(true);
        valueSeries.setSeriesIdentifier("valueSeriesIdentifier");
        ((PieSeries) valueSeries).getTitle().getCaption().setValue("valueSeries");
        ((PieSeries) valueSeries).setStacked(false);
        sdY.getSeries().add(valueSeries);

        sdX.getSeriesDefinitions().add(sdY);

        newChart.getSeriesDefinitions().add(sdX);

        addSampleData();
        return newChart;
    }

    private void addSampleData()
    {
        SampleData sd = DataFactory.eINSTANCE.createSampleData();
        sd.getBaseSampleData().clear();
        sd.getOrthogonalSampleData().clear();

        // Create Base Sample Data
        BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
        sdBase.setDataSetRepresentation("New York, Chicago, San Francisco");
        sd.getBaseSampleData().add(sdBase);

        // Create Orthogonal Sample Data (with simulation count of 2)
        OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
        oSample.setDataSetRepresentation("5,4,12");
        oSample.setSeriesDefinitionIndex(0);
        sd.getOrthogonalSampleData().add(oSample);

        OrthogonalSampleData oSample2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
        oSample2.setDataSetRepresentation("7,22,14");
        oSample2.setSeriesDefinitionIndex(0);
        sd.getOrthogonalSampleData().add(oSample2);

        newChart.setSampleData(sd);
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