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
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 *  
 */
public class PieChart extends DefaultChartTypeImpl
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
        imgIcon = UIHelper.getImage("images/piecharticon.gif");
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
            img2D = UIHelper.getImage("images/piechartimage.gif");

            vSubTypes.add(new DefaultChartSubTypeImpl("Standard Pie Chart", img2D, sStandardDescription));
        }
        else if (sDimension.equals("2D With Depth")
            || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName()))
        {
            img2DWithDepth = UIHelper.getImage("images/piechartwithdepthimage.gif");

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
    public Chart getModel(String sSubType, Orientation orientation, String sDimension, Chart currentChart)
    {
        ChartWithoutAxes newChart = null;
        if(currentChart != null)
        {
            newChart = (ChartWithoutAxes) getConvertedChart(currentChart, sSubType, sDimension);
            if(newChart != null)
            {
                return newChart;
            }
        }
        newChart = ChartWithoutAxesImpl.create();
        newChart.setType(sType);
        newChart.setSubType(sSubType);
        newChart.setDimension(getDimensionFor(sDimension));
        if (newChart.getDimension().equals(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL))
        {
            newChart.setSeriesThickness(15);
        }

        newChart.getTitle().getLabel().getCaption().setValue("Pie Chart Title");

        SeriesDefinition sdX = SeriesDefinitionImpl.create();
        Series categorySeries = SeriesImpl.create();
        sdX.getSeries().add(categorySeries);
        sdX.getQuery().setDefinition("Base Series");

        SeriesDefinition sdY = SeriesDefinitionImpl.create();
        sdY.getSeriesPalette().update(0);
        Series valueSeries = PieSeriesImpl.create();
        valueSeries.getLabel().setVisible(true);
        valueSeries.setSeriesIdentifier("valueSeriesIdentifier");
        ((PieSeries) valueSeries).getTitle().getCaption().setValue("valueSeries");
        ((PieSeries) valueSeries).setStacked(false);
        sdY.getSeries().add(valueSeries);

        sdX.getSeriesDefinitions().add(sdY);

        newChart.getSeriesDefinitions().add(sdX);

        addSampleData(newChart);
        return newChart;
    }

    private void addSampleData(Chart newChart)
    {
        SampleData sd = DataFactory.eINSTANCE.createSampleData();
        sd.getBaseSampleData().clear();
        sd.getOrthogonalSampleData().clear();

        // Create Base Sample Data
        BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
        sdBase.setDataSetRepresentation("A, B, C");
        sd.getBaseSampleData().add(sdBase);

        // Create Orthogonal Sample Data (with simulation count of 2)
        OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
        oSample.setDataSetRepresentation("5,4,12");
        oSample.setSeriesDefinitionIndex(0);
        sd.getOrthogonalSampleData().add(oSample);

        /*
         * OrthogonalSampleData oSample2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
         * oSample2.setDataSetRepresentation("7,22,14"); oSample2.setSeriesDefinitionIndex(0);
         * sd.getOrthogonalSampleData().add(oSample2);
         */
        newChart.setSampleData(sd);
    }

    private Chart getConvertedChart(Chart currentChart, String sNewSubType, String sNewDimension)
    {
        Chart helperModel = (Chart) EcoreUtil.copy(currentChart);
        if((currentChart instanceof ChartWithAxes))	// Chart is ChartWithoutAxes
        {
            // Create a new instance of the correct type and set initial properties
            currentChart = ChartWithoutAxesImpl.create();
            currentChart.setType(sType);
            currentChart.setSubType(sNewSubType);
            currentChart.setDimension(getDimensionFor(sNewDimension));

            // Copy generic chart properties from the old chart
            currentChart.setBlock(helperModel.getBlock());
            currentChart.setDescription(helperModel.getDescription());
            currentChart.setGridColumnCount(helperModel.getGridColumnCount());
            if(!currentChart.getType().equals("Line Chart") && !currentChart.getType().equals("Bar Chart"))
            {
                currentChart.setSampleData(getConvertedSampleData(helperModel.getSampleData()));
            }
            currentChart.setScript(helperModel.getScript());
            if(helperModel.isSetSeriesThickness())
            {
                currentChart.setSeriesThickness(helperModel.getSeriesThickness());
            }
            else
            {
                currentChart.setSeriesThickness(15);
            }
            currentChart.setUnits(helperModel.getUnits());
            if(helperModel.getGridColumnCount() > 0)
            {
                currentChart.setGridColumnCount(helperModel.getGridColumnCount());
            }
            else
            {
                currentChart.setGridColumnCount(1);
            }
            
            // Copy series definitions from old chart
            ((ChartWithoutAxes) currentChart).getSeriesDefinitions().add(((Axis) ((ChartWithAxes) helperModel).getAxes().get(0)).getSeriesDefinitions().get(0));
            Vector vOSD = new Vector();
            EList axesOrthogonal = ((Axis) ((ChartWithAxes) helperModel).getAxes().get(0)).getAssociatedAxes();
            for(int i = 0; i < axesOrthogonal.size(); i++)
            {
                EList osd = ((Axis) axesOrthogonal.get(i)).getSeriesDefinitions();
                for(int j = 0; j < osd.size(); j++)
                {
                    SeriesDefinition sd = (SeriesDefinition) osd.get(j);
                    Series series = sd.getDesignTimeSeries();
                    sd.getSeries().clear();
                    sd.getSeries().add(getConvertedSeries(series));
                    vOSD.add(sd);
                }
            }
            ((SeriesDefinition) ((ChartWithoutAxes) currentChart).getSeriesDefinitions().get(0)).getSeriesDefinitions().clear();
            ((SeriesDefinition) ((ChartWithoutAxes) currentChart).getSeriesDefinitions().get(0)).getSeriesDefinitions().addAll(vOSD);
        }
        else if(currentChart.getType().equals(sType))
        {
        	currentChart.setSubType(sNewSubType);
        	if(!currentChart.isSetSeriesThickness())
        	{
        	    currentChart.setSeriesThickness(15);
        	}
            if(!currentChart.getDimension().equals(getDimensionFor(sNewDimension)))
            {
                currentChart.setDimension(getDimensionFor(sNewDimension));
            }
        }
        else
        {
            return null;
        }
        return currentChart;
    }
    
    private Series getConvertedSeries(Series series)
    {
        // Do not convert base series
        if(series.getClass().getName().equals("org.eclipse.birt.chart.model.component.impl.SeriesImpl"))
        {
            return series;
        }
        PieSeries pieseries = (PieSeries) PieSeriesImpl.create();
        pieseries.setExplosion(10);
        pieseries.getLeaderLineAttributes().setVisible(true);
        pieseries.getLeaderLineAttributes().setColor(ColorDefinitionImpl.BLACK());
        pieseries.setLeaderLineStyle(LeaderLineStyle.STRETCH_TO_SIDE_LITERAL);
        
        // Copy generic series properties
        pieseries.setLabel(series.getLabel());
        if(series.getLabelPosition().equals(Position.INSIDE_LITERAL) || series.getLabelPosition().equals(Position.OUTSIDE_LITERAL))
        {
            pieseries.setLabelPosition(series.getLabelPosition());
        }
        else
        {
            pieseries.setLabelPosition(Position.OUTSIDE_LITERAL);
        }
        pieseries.setVisible(series.isVisible());
        if(series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers()))
        {
            pieseries.getTriggers().addAll(series.getTriggers());
        }
        if(series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint()))
        {
            pieseries.setDataPoint(series.getDataPoint());
        }
        if(series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition()))
        {
            pieseries.getDataDefinition().addAll(series.getDataDefinition());
        }


        // Copy series specific properties
        if(series instanceof LineSeries)
        {
            pieseries.setLeaderLineAttributes(((LineSeries) series).getLineAttributes());
        }
        else if(series instanceof StockSeries)
        {
            pieseries.setLeaderLineAttributes(((StockSeries) series).getLineAttributes());
        }
        return pieseries;
    }
    
    private SampleData getConvertedSampleData(SampleData currentSampleData)
    {
        // Convert base sample data
        EList bsdList = currentSampleData.getBaseSampleData();
        Vector vNewBaseSampleData = new Vector();
        for(int i = 0; i < bsdList.size(); i++)
        {
            BaseSampleData bsd = (BaseSampleData) bsdList.get(i);
            bsd.setDataSetRepresentation(getConvertedBaseSampleDataRepresentation(bsd.getDataSetRepresentation()));
            vNewBaseSampleData.add(bsd);
        }
        currentSampleData.getBaseSampleData().clear();
        currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);
        
        // Convert orthogonal sample data
        EList osdList = currentSampleData.getOrthogonalSampleData();
        Vector vNewOrthogonalSampleData = new Vector();
        for(int i = 0; i < osdList.size(); i++)
        {
            OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get(i);
            osd.setDataSetRepresentation(getConvertedOrthogonalSampleDataRepresentation(osd.getDataSetRepresentation()));
            vNewOrthogonalSampleData.add(osd);
        }
        currentSampleData.getOrthogonalSampleData().clear();
        currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
        return currentSampleData;
    }
    
    private String getConvertedBaseSampleDataRepresentation(String sOldRepresentation)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        StringBuffer sbNewRepresentation = new StringBuffer("");
        while(strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if(!sElement.startsWith("'"))
            {
	            sbNewRepresentation.append("'");
	            sbNewRepresentation.append(sElement);
	            sbNewRepresentation.append("'");
            }
            else
            {
                if(sElement.startsWith("-"))	// Negative Number
                {
                    sElement = sElement.substring(1);	// Convert to positive number since negative values are not supported for pie charts
                }
                sbNewRepresentation.append(sElement);
            }
            sbNewRepresentation.append(",");
        }
        return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
    }
    
    private String getConvertedOrthogonalSampleDataRepresentation(String sOldRepresentation)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        StringBuffer sbNewRepresentation = new StringBuffer("");
        while(strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if(sElement.startsWith("H"))	// Orthogonal sample data is for a stock chart (Orthogonal sample data CANNOT be text
            {
	            sbNewRepresentation.append(sElement.substring(1));
            }
            else
            {
                if(sElement.startsWith("-"))	// Negative Number
                {
                    sElement = sElement.substring(1);	// Convert to positive number since negative values are not supported for pie charts
                }
                sbNewRepresentation.append(sElement);
            }
            sbNewRepresentation.append(",");
        }
        return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
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