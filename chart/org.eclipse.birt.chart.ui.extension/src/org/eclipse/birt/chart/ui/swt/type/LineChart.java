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
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
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
public class LineChart extends DefaultChartTypeImpl
{

    private static final String sType = "Line Chart";

    private static final String sStackedDescription = "Stacked Line charts show lines stacked one above the other. The positive and negative values are stacked separately above and below the origin.";

    private static final String sPercentStackedDescription = "Percent Stacked Line charts show lines stacked one over the other in such a way that the total height of the stacked lines (from the lowest point to the highest in each unit) is 100.";

    private static final String sOverlayDescription = "Overlay Line charts show lines from each series independent of the others. The lines are shown joining the values for the series.";

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
        imgIcon = UIHelper.getImage("images/linecharticon.gif");
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
        if (sDimension.equals("2D") || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName()))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgStacked = UIHelper.getImage("images/stackedlinechartimage.gif");
                imgPercentStacked = UIHelper.getImage("images/percentstackedlinechartimage.gif");
                imgSideBySide = UIHelper.getImage("images/sidebysidelinechartimage.gif");
            }
            else
            {
                imgStacked = UIHelper.getImage("images/horizontalstackedlinechartimage.gif");
                imgPercentStacked = UIHelper.getImage("images/horizontalpercentstackedlinechartimage.gif");
                imgSideBySide = UIHelper.getImage("images/horizontalsidebysidelinechartimage.gif");
            }

            vSubTypes.add(new DefaultChartSubTypeImpl("Stacked", imgStacked, sStackedDescription));
            vSubTypes
                .add(new DefaultChartSubTypeImpl("Percent Stacked", imgPercentStacked, sPercentStackedDescription));
            vSubTypes.add(new DefaultChartSubTypeImpl("Overlay", imgSideBySide, sOverlayDescription));
        }
        else if (sDimension.equals("2D With Depth")
            || sDimension.equals(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName()))
        {
            if (orientation.equals(Orientation.VERTICAL_LITERAL))
            {
                imgStackedWithDepth = UIHelper.getImage("images/stackedlinechartwithdepthimage.gif");
                imgPercentStackedWithDepth = UIHelper.getImage("images/percentstackedlinechartwithdepthimage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/sidebysidelinechartwithdepthimage.gif");
            }
            else
            {
                imgStackedWithDepth = UIHelper.getImage("images/horizontalstackedlinechartwithdepthimage.gif");
                imgPercentStackedWithDepth = UIHelper
                    .getImage("images/horizontalpercentstackedlinechartwithdepthimage.gif");
                imgSideBySideWithDepth = UIHelper.getImage("images/horizontalsidebysidelinechartwithdepthimage.gif");

            }

            vSubTypes.add(new DefaultChartSubTypeImpl("Stacked", imgStackedWithDepth, sStackedDescription));
            vSubTypes.add(new DefaultChartSubTypeImpl("Percent Stacked", imgPercentStackedWithDepth,
                sPercentStackedDescription));
            vSubTypes.add(new DefaultChartSubTypeImpl("Overlay", imgSideBySideWithDepth, sOverlayDescription));
        }
        else if (sDimension.equals("3D") || sDimension.equals(ChartDimension.THREE_DIMENSIONAL_LITERAL.getName()))
        {
            imgSideBySide3D = UIHelper.getImage("images/sidebysidelinechart3dimage.gif");

            vSubTypes.add(new DefaultChartSubTypeImpl("Side-by-side", imgSideBySide3D, sOverlayDescription));
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

        ((Axis) newChart.getAxes().get(0)).setOrientation(Orientation.HORIZONTAL_LITERAL);
        ((Axis) newChart.getAxes().get(0)).setType(AxisType.TEXT_LITERAL);
        ((Axis) newChart.getAxes().get(0)).setCategoryAxis(true);

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

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getSeriesPalette().update(0);
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).getMarker().setVisible(true);
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

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getSeriesPalette().update(0);
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).getMarker().setVisible(true);
            ((LineSeries) valueSeries).setStacked(true);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
        }
        else if (sSubType.equalsIgnoreCase("Overlay"))
        {
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).setType(AxisType.LINEAR_LITERAL);

            SeriesDefinition sdY = SeriesDefinitionImpl.create();
            sdY.getSeriesPalette().update(0);
            Series valueSeries = LineSeriesImpl.create();
            valueSeries.getLabel().setVisible(true);
            ((LineSeries) valueSeries).getMarker().setVisible(true);
            ((LineSeries) valueSeries).setStacked(false);
            sdY.getSeries().add(valueSeries);
            ((Axis) ((Axis) newChart.getAxes().get(0)).getAssociatedAxes().get(0)).getSeriesDefinitions().add(sdY);
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
        sdBase.setDataSetRepresentation("A, B, C");
        sd.getBaseSampleData().add(sdBase);

        // Create Orthogonal Sample Data (with simulation count of 2)
        OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
        oSample.setDataSetRepresentation("5,-4,12");
        oSample.setSeriesDefinitionIndex(0);
        sd.getOrthogonalSampleData().add(oSample);

        /*
         * OrthogonalSampleData oSample2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
         * oSample2.setDataSetRepresentation("7,22,14"); oSample2.setSeriesDefinitionIndex(0);
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
                        if (sNewSubType.equalsIgnoreCase("Percent Stacked"))
                        {
                            ((Axis) axes.get(i)).setPercent(true);
                        }
                        else
                        {
                            ((Axis) axes.get(i)).setPercent(false);
                        }
                        EList seriesdefinitions = ((Axis) axes.get(i)).getSeriesDefinitions();
                        for (int j = 0; j < seriesdefinitions.size(); j++)
                        {
                            Series series = ((SeriesDefinition) seriesdefinitions.get(j)).getDesignTimeSeries();
                            if ((sNewSubType.equalsIgnoreCase("Stacked") || sNewSubType
                                .equalsIgnoreCase("Percent Stacked")))
                            {
                                series.setStacked(true);
                            }
                            else
                            {
                                series.setStacked(false);
                            }
                        }
                    }
                }
            }
            else if (currentChart.getType().equals("Bar Chart") || currentChart.getType().equals("Stock Chart")
                || currentChart.getType().equals("Scatter Chart"))
            {
                if (!currentChart.getType().equals("Bar Chart"))
                {
                    currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData()));
                }
                currentChart.setType(sType);
                currentChart.setSubType(sNewSubType);
                EList axes = ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes();
                for (int i = 0; i < axes.size(); i++)
                {
                    if (sNewSubType.equalsIgnoreCase("Percent Stacked"))
                    {
                        ((Axis) axes.get(i)).setPercent(true);
                    }
                    else
                    {
                        ((Axis) axes.get(i)).setPercent(false);
                    }
                    EList seriesdefinitions = ((Axis) axes.get(i)).getSeriesDefinitions();
                    for (int j = 0; j < seriesdefinitions.size(); j++)
                    {
                        Series series = ((SeriesDefinition) seriesdefinitions.get(j)).getDesignTimeSeries();
                        series = getConvertedSeries(series);
                        if ((sNewSubType.equalsIgnoreCase("Stacked") || sNewSubType.equalsIgnoreCase("Percent Stacked")))
                        {
                            series.setStacked(true);
                        }
                        else
                        {
                            series.setStacked(false);
                        }
                        ((SeriesDefinition) seriesdefinitions.get(j)).getSeries().set(j, series);
                    }
                }
            }
            else
            {
                return null;
            }
            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setCategoryAxis(true);
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
            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setType(AxisType.TEXT_LITERAL);
            ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).setCategoryAxis(true);

            ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                .setOrientation(Orientation.VERTICAL_LITERAL);
            ((Axis) ((Axis) ((ChartWithAxes) currentChart).getAxes().get(0)).getAssociatedAxes().get(0))
                .setType(AxisType.LINEAR_LITERAL);

            // Copy generic chart properties from the old chart
            currentChart.setBlock(helperModel.getBlock());
            currentChart.setDescription(helperModel.getDescription());
            currentChart.setGridColumnCount(helperModel.getGridColumnCount());
            currentChart.setSampleData(helperModel.getSampleData());
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
                    if ((sNewSubType.equalsIgnoreCase("Stacked") || sNewSubType.equalsIgnoreCase("Percent Stacked")))
                    {
                        series.setStacked(true);
                    }
                    else
                    {
                        series.setStacked(false);
                    }
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
        LineSeries lineseries = (LineSeries) LineSeriesImpl.create();
        lineseries.getLineAttributes().setVisible(true);
        lineseries.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
        if (!(series instanceof ScatterSeries))
        {
            Marker marker = AttributeFactory.eINSTANCE.createMarker();
            marker.setSize(5);
            marker.setType(MarkerType.BOX_LITERAL);
            marker.setVisible(true);
            lineseries.setMarker(marker);
        }
        else
        {
            lineseries.setMarker(((ScatterSeries) series).getMarker());
        }

        // Copy generic series properties
        lineseries.setLabel(series.getLabel());
        if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
            || series.getLabelPosition().equals(Position.OUTSIDE_LITERAL))
        {
            lineseries.setLabelPosition(Position.ABOVE_LITERAL);
        }
        else
        {
            lineseries.setLabelPosition(series.getLabelPosition());
        }

        lineseries.setVisible(series.isVisible());
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers()))
        {
            lineseries.getTriggers().addAll(series.getTriggers());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint()))
        {
            lineseries.setDataPoint(series.getDataPoint());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition()))
        {
            lineseries.getDataDefinition().add(series.getDataDefinition().get(0));
        }

        // Copy series specific properties
        if (series instanceof StockSeries)
        {
            lineseries.getLineAttributes().setColor(((StockSeries) series).getLineAttributes().getColor());
        }
        return lineseries;
    }

    private SampleData getConvertedSampleData(SampleData currentSampleData)
    {
        // Convert base sample data
        EList bsdList = currentSampleData.getBaseSampleData();
        Vector vNewBaseSampleData = new Vector();
        for (int i = 0; i < bsdList.size(); i++)
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
        for (int i = 0; i < osdList.size(); i++)
        {
            OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get(i);
            osd
                .setDataSetRepresentation(getConvertedOrthogonalSampleDataRepresentation(osd.getDataSetRepresentation()));
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
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if (!sElement.startsWith("'"))
            {
                sbNewRepresentation.append("'");
                sbNewRepresentation.append(sElement);
                sbNewRepresentation.append("'");
            }
            else
            {
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
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if (sElement.startsWith("H")) // Orthogonal sample data is for a stock chart (Orthogonal sample data CANNOT
            // be text
            {
                StringTokenizer strStockTokenizer = new StringTokenizer(sElement);
                sbNewRepresentation.append(strStockTokenizer.nextToken().trim().substring(1));
            }
            else
            {
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