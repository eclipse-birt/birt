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
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
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
public class ScatterChart implements IChartType
{

    private static final String sType = "Scatter Chart";

    private static final String sStandardDescription = "Scatter charts show the values arranged on the plot using the base and orthogonal values as co-ordinates. Each data value is indicated by a marker.";

    private transient Image imgIcon = null;

    private transient Image img2D = null;

    private transient ChartWithAxes newChart = null;

    private static final String[] saDimensions = new String[]
    {
        "2D"
    };

    public ScatterChart()
    {
        imgIcon = UIHelper.getImage("images/ScatterChartIcon.gif");
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
            "Scatter Chart",
            "Scatter charts show randomly spaced data values associated with non category axes. Each data point is associated with the X and Y axis scale.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
     */
    public Collection getChartSubtypes(String sDimension, Orientation orientation)
    {
        Vector vSubTypes = new Vector();
        if (sDimension.equals("2D") || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName()))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                img2D = UIHelper.getImage("images/ScatterChartImage.gif");
            }
            else
            {
                img2D = UIHelper.getImage("images/HorizontalScatterChartImage.gif");
            }

            vSubTypes.add(new DefaultChartSubTypeImpl("Standard Scatter Chart", img2D, sStandardDescription));
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
        ((Axis) newChart.getAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

        ((Axis) newChart.getAxes().get(0)).getTitle().getCaption().setValue("Base Axis Title");

        SeriesDefinition sdX = SeriesDefinitionImpl.create();
        Series baseSeries = SeriesImpl.create();
        sdX.getSeries().add(baseSeries);
        ((Axis) newChart.getAxes().get(0)).getSeriesDefinitions().add(sdX);

        newChart.getTitle().getLabel().getCaption().setValue("Scatter Chart Title");

        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
            .setOrientation(Orientation.VERTICAL_LITERAL);
        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getTitle().getCaption().setValue(
            "Orthogonal Axis Title");

        SeriesDefinition sdY = SeriesDefinitionImpl.create();
        sdY.getQuery().setDefinition("Expr(\"Column\")");
        sdY.getSeriesPalette().update(0);
        Series orthogonalSeries = ScatterSeriesImpl.create();
        orthogonalSeries.getLabel().setVisible(true);
        ((ScatterSeries) orthogonalSeries).setStacked(true);
        sdY.getSeries().add(orthogonalSeries);
        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);

        if (sSubType.equalsIgnoreCase("Standard Scatter Chart"))
        {
            newChart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
        }
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
        sdBase.setDataSetRepresentation("3,9,-2");
        sd.getBaseSampleData().add(sdBase);

        // Create Orthogonal Sample Data (with simulation count of 2)
        OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
        oSample.setDataSetRepresentation("15,-4,12");
        oSample.setSeriesDefinitionIndex(0);
        sd.getOrthogonalSampleData().add(oSample);

        /*
         * OrthogonalSampleData oSample2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
         * oSample2.setDataSetRepresentation("2,27,35"); oSample2.setSeriesDefinitionIndex(0);
         * sd.getOrthogonalSampleData().add(oSample2);
         */
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
        return true;
    }

    private ChartDimension getDimensionFor(String sDimension)
    {
        return ChartDimension.TWO_DIMENSIONAL_LITERAL;
    }
}