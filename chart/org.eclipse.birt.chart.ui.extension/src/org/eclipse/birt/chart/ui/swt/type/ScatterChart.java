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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
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
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
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
public class ScatterChart extends DefaultChartTypeImpl
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
        imgIcon = UIHelper.getImage("images/scattercharticon.gif");
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
                img2D = UIHelper.getImage("images/scatterchartimage.gif");
            }
            else
            {
                img2D = UIHelper.getImage("images/horizontalscatterchartimage.gif");
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
    public Chart getModel(String sSubType, Orientation orientation, String sDimension, Chart currentChart)
    {
        ChartWithAxes newChart = null;
        if (currentChart != null)
        {
            newChart = (ChartWithAxes) getConvertedChart(currentChart, sSubType, orientation, sDimension);
            if (newChart != null)
            {
                return newChart;
            }
        }
        newChart = ChartWithAxesImpl.create();
        newChart.setType(sType);
        newChart.setSubType(sSubType);
        newChart.setOrientation(orientation);
        newChart.setDimension(getDimensionFor(sDimension));
        newChart.setUnits("Points");

        ((Axis) newChart.getAxes().get(0)).setOrientation(Orientation.HORIZONTAL_LITERAL);
        ((Axis) newChart.getAxes().get(0)).setType(AxisType.LINEAR_LITERAL);
        ((Axis) newChart.getAxes().get(0)).setCategoryAxis(false);

        SeriesDefinition sdX = SeriesDefinitionImpl.create();
        Series baseSeries = SeriesImpl.create();
        sdX.getSeries().add(baseSeries);
        ((Axis) newChart.getAxes().get(0)).getSeriesDefinitions().add(sdX);

        newChart.getTitle().getLabel().getCaption().setValue("Scatter Chart Title");

        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
            .setOrientation(Orientation.VERTICAL_LITERAL);
        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

        SeriesDefinition sdY = SeriesDefinitionImpl.create();
        sdY.getSeriesPalette().update(0);
        Series orthogonalSeries = ScatterSeriesImpl.create();
        orthogonalSeries.getLabel().setVisible(true);
        ((ScatterSeries) orthogonalSeries).setStacked(false);
        sdY.getSeries().add(orthogonalSeries);
        ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);

        if (sSubType.equalsIgnoreCase("Standard Scatter Chart"))
        {
            newChart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
        }
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

    private Chart getConvertedChart(Chart currentChart, String sNewSubType, Orientation newOrientation,
        String sNewDimension)
    {
        Chart helperModel = (Chart) EcoreUtil.copy(currentChart);
        if ((currentChart instanceof ChartWithAxes)) // Chart is ChartWithAxes
        {
            if (currentChart.getType().equals(sType)) // Original chart is of this type (LineChart)
            {
                if (!currentChart.getSubType().equals(sNewSubType)) // Original chart is of the required subtype
                {
                    currentChart.setSubType(sNewSubType);
                    EList axes = ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes();
                    for (int i = 0; i < axes.size(); i++)
                    {
                        ((Axis) axes.get(i)).setPercent(false);
                        EList seriesdefinitions = ((Axis) axes.get(i)).getSeriesDefinitions();
                        for (int j = 0; j < seriesdefinitions.size(); j++)
                        {
                            Series series = ((SeriesDefinition) seriesdefinitions.get(j)).getDesignTimeSeries();
                            series.setStacked(false);
                        }
                    }
                }
            }
            else if (currentChart.getType().equals("Bar Chart") || currentChart.getType().equals("Stock Chart")
                || currentChart.getType().equals("Line Chart"))
            {
                currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData(), currentChart.getType()
                    .equals("Stock Chart")));
                currentChart.setType(sType);
                ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setType(AxisType.LINEAR_LITERAL);
                ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setCategoryAxis(false);

                currentChart.setSubType(sNewSubType);
                EList axes = ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes();
                for (int i = 0; i < axes.size(); i++)
                {
                    ((Axis) axes.get(i)).setPercent(false);
                    EList seriesdefinitions = ((Axis) axes.get(i)).getSeriesDefinitions();
                    for (int j = 0; j < seriesdefinitions.size(); j++)
                    {
                        Series series = ((SeriesDefinition) seriesdefinitions.get(j)).getDesignTimeSeries();
                        series = getConvertedSeries(series);
                        series.setStacked(false);
                        ((SeriesDefinition) seriesdefinitions.get(j)).getSeries().clear();
                        ((SeriesDefinition) seriesdefinitions.get(j)).getSeries().add(series);
                    }
                }
                ((ChartWithAxes) currentChart).setOrientation(newOrientation);
                currentChart.setDimension(getDimensionFor(sNewDimension));
            }
            else
            {
                return null;
            }
        }
        else
        {
            // Create a new instance of the correct type and set initial properties
            currentChart = ChartWithAxesImpl.create();
            currentChart.setType(sType);
            currentChart.setSubType(sNewSubType);
            ((ChartWithAxes) currentChart).setOrientation(newOrientation);
            currentChart.setDimension(getDimensionFor(sNewDimension));

            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setOrientation(Orientation.HORIZONTAL_LITERAL);
            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setType(AxisType.LINEAR_LITERAL);
            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setCategoryAxis(false);

            ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                .setType(AxisType.LINEAR_LITERAL);

            // Copy generic chart properties from the old chart
            currentChart.setBlock(helperModel.getBlock());
            currentChart.setDescription(helperModel.getDescription());
            currentChart.setGridColumnCount(helperModel.getGridColumnCount());
            currentChart.setSampleData(getConvertedSampleData(helperModel.getSampleData(), false));
            currentChart.setScript(helperModel.getScript());
            currentChart.setSeriesThickness(helperModel.getSeriesThickness());
            currentChart.setUnits(helperModel.getUnits());

            if (helperModel.getType().equals("Pie Chart"))
            {
                // Clear existing series definitions
                ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getSeriesDefinitions().clear();

                // Copy base series definitions
                ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getSeriesDefinitions().add(
                    ((ChartWithoutAxes) helperModel).getSeriesDefinitions().get(0));

                // Clear existing series definitions
                ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                    .getSeriesDefinitions().clear();

                // Copy orthogonal series definitions
                ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                    .getSeriesDefinitions().addAll(
                        ((SeriesDefinition) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0))
                            .getSeriesDefinitions().get(0)).getSeriesDefinitions());

                // Update the base series
                Series series = ((SeriesDefinition) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0))
                    .getSeriesDefinitions().get(0)).getDesignTimeSeries();
                series = getConvertedSeries(series);

                // Clear existing series
                ((SeriesDefinition) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getSeriesDefinitions()
                    .get(0)).getSeries().clear();

                // Add converted series
                ((SeriesDefinition) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getSeriesDefinitions()
                    .get(0)).getSeries().add(series);

                // Update the orthogonal series
                EList seriesdefinitions = ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0))
                    .getAssociatedAxes().get(0)).getSeriesDefinitions();
                for (int j = 0; j < seriesdefinitions.size(); j++)
                {
                    series = ((SeriesDefinition) seriesdefinitions.get(j)).getDesignTimeSeries();
                    series = getConvertedSeries(series);
                    series.setStacked(false);
                    // Clear any existing series
                    ((SeriesDefinition) seriesdefinitions.get(j)).getSeries().clear();
                    // Add the new series
                    ((SeriesDefinition) seriesdefinitions.get(j)).getSeries().add(series);
                }
            }
            else
            {
                return null;
            }
        }
        if (currentChart instanceof ChartWithAxes
            && !((ChartWithAxes) currentChart).getOrientation().equals(newOrientation))
        {
            ((ChartWithAxes) currentChart).setOrientation(newOrientation);
        }
        if (!currentChart.getDimension().equals(getDimensionFor(sNewDimension)))
        {
            currentChart.setDimension(getDimensionFor(sNewDimension));
        }
        return currentChart;
    }

    private Series getConvertedSeries(Series series)
    {
        // Do not convert base series
        if (series.getClass().getName().equals("org.eclipse.birt.chart.model.component.impl.SeriesImpl"))
        {
            return series;
        }
        ScatterSeries scatterseries = (ScatterSeries) ScatterSeriesImpl.create();
        scatterseries.getLineAttributes().setVisible(false);
        if (!(series instanceof LineSeries))
        {
            Marker marker = AttributeFactory.eINSTANCE.createMarker();
            marker.setSize(5);
            marker.setType(MarkerType.BOX_LITERAL);
            marker.setVisible(true);
            scatterseries.setMarker(marker);
        }
        else
        {
            scatterseries.setMarker(((LineSeries) series).getMarker());
        }

        // Copy generic series properties
        scatterseries.setLabel(series.getLabel());
        if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
            || series.getLabelPosition().equals(Position.OUTSIDE_LITERAL))
        {
            scatterseries.setLabelPosition(Position.ABOVE_LITERAL);
        }
        else
        {
            scatterseries.setLabelPosition(series.getLabelPosition());
        }

        scatterseries.setVisible(series.isVisible());
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers()))
        {
            scatterseries.getTriggers().addAll(series.getTriggers());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint()))
        {
            scatterseries.setDataPoint(series.getDataPoint());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition()))
        {
            scatterseries.getDataDefinition().add(series.getDataDefinition().get(0));
        }

        // Copy series specific properties
        if (series instanceof BarSeries)
        {
            scatterseries.getLineAttributes().setColor(((BarSeries) series).getRiserOutline());
        }
        else if (series instanceof PieSeries)
        {
            scatterseries.getLineAttributes().setColor(((PieSeries) series).getSliceOutline());
        }
        else if (series instanceof StockSeries)
        {
            scatterseries.getLineAttributes().setColor(((StockSeries) series).getLineAttributes().getColor());
        }
        return scatterseries;
    }

    private SampleData getConvertedSampleData(SampleData currentSampleData, boolean bStockSeries)
    {
        // Convert base sample data
        EList bsdList = currentSampleData.getBaseSampleData();
        Vector vNewBaseSampleData = new Vector();
        for (int i = 0; i < bsdList.size(); i++)
        {
            BaseSampleData bsd = (BaseSampleData) bsdList.get(i);
            bsd.setDataSetRepresentation(getConvertedSampleDataRepresentation(bsd.getDataSetRepresentation(), false)); // Special
            // handling
            // for
            // stock
            // series
            // only
            // needed
            // for
            // orthogonal
            // values
            vNewBaseSampleData.add(bsd);
        }
        currentSampleData.getBaseSampleData().clear();
        currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);

        // Convert orthogonal sample data
        EList osdList = currentSampleData.getOrthogonalSampleData();
        Vector vNewOrthogonalSampleData = new Vector();
        for (int i = 0; i < osdList.size(); i++)
        {
            OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get(i);
            osd.setDataSetRepresentation(getConvertedSampleDataRepresentation(osd.getDataSetRepresentation(),
                bStockSeries));
            vNewOrthogonalSampleData.add(osd);
        }
        currentSampleData.getOrthogonalSampleData().clear();
        currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
        return currentSampleData;
    }

    private String getConvertedSampleDataRepresentation(String sOldRepresentation, boolean bStockSeries)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        NumberFormat nf = NumberFormat.getNumberInstance();
        StringBuffer sbNewRepresentation = new StringBuffer("");
        int iValueCount = 0;
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if (bStockSeries)
            {
                StringTokenizer strStockTokenizer = new StringTokenizer(sElement);
                sElement = strStockTokenizer.nextToken().trim().substring(1);
            }
            try
            {
                double dbl = nf.parse(sElement).doubleValue();
                if (dbl < 0)
                {
                    // If the value is negative, use an arbitrary positive value
                    sElement = String.valueOf(4.0 + iValueCount);
                    iValueCount++;
                }
                else
                {
                    sElement = String.valueOf(dbl);
                }
            }
            catch (ParseException e )
            {
                sElement = String.valueOf(6.0 + iValueCount);
                iValueCount++;
            }
            sbNewRepresentation.append(sElement);
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
        return true;
    }

    private ChartDimension getDimensionFor(String sDimension)
    {
        return ChartDimension.TWO_DIMENSIONAL_LITERAL;
    }
}