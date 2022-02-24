/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.script.IChartScriptContext;

/**
 * Script event handler interface for charts. This event handler provides
 * control on the chart databinding, generation and rendering. Please use the
 * adapter implementation ChartItem
 * 
 * @see IChartScriptContext
 */
public interface IChartEventHandler {

	/**
	 * Called before populating the series dataset using the DataSetProcessor.
	 * 
	 * @param series Series
	 * @param idsp   IDataSetProcessor
	 * @param icsc   IChartScriptContext
	 */
	public void beforeDataSetFilled(Series series, IDataSetProcessor idsp, IChartScriptContext icsc);

	/**
	 * Called after populating the series dataset.
	 * 
	 * @param series  Series
	 * @param dataSet DataSet
	 * @param icsc    IChartScriptContext
	 */
	public void afterDataSetFilled(Series series, DataSet dataSet, IChartScriptContext icsc);

	/**
	 * Called before generation of chart model to GeneratedChartState.
	 * 
	 * @param cm   Chart
	 * @param icsc IChartScriptContext
	 */
	public void beforeGeneration(Chart cm, IChartScriptContext icsc);

	/**
	 * Called after generation of chart model to GeneratedChartState.
	 * 
	 * @param gcs  GeneratedChartState
	 * @param icsc IChartScriptContext
	 */
	public void afterGeneration(GeneratedChartState gcs, IChartScriptContext icsc);

	/**
	 * Called before computations of chart model.
	 * 
	 * @param cm            Chart
	 * @param oComputations PlotComputation
	 * @since 2.5
	 */
	public void beforeComputations(Chart cm, PlotComputation oComputations);

	/**
	 * Called after computations of chart model.
	 * 
	 * @param cm            Chart
	 * @param oComputations PlotComputation
	 * @since 2.5
	 */
	public void afterComputations(Chart cm, PlotComputation oComputations);

	/**
	 * Called before the chart is rendered.
	 * 
	 * @param gcs  GeneratedChartState
	 * @param icsc IChartScriptContext
	 */
	public void beforeRendering(GeneratedChartState gcs, IChartScriptContext icsc);

	/**
	 * Called after the chart is rendered.
	 * 
	 * @param gcs  GeneratedChartState
	 * @param icsc IChartScriptContext
	 */
	public void afterRendering(GeneratedChartState gcs, IChartScriptContext icsc);

	/**
	 * Called before drawing each block.
	 * 
	 * @param block Block
	 * @param icsc  IChartScriptContext
	 */
	public void beforeDrawBlock(Block block, IChartScriptContext icsc);

	/**
	 * Called after drawing each block.
	 * 
	 * @param block Block
	 * @param icsc  IChartScriptContext
	 */
	public void afterDrawBlock(Block block, IChartScriptContext icsc);

	/**
	 * Called before drawing each entry in the legend.
	 * 
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 * @deprecated Since 2.2.0 use beforeDrawLegendItem( ) instead
	 */
	public void beforeDrawLegendEntry(Label label, IChartScriptContext icsc);

	/**
	 * Called after drawing each entry in the legend.
	 * 
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 * @deprecated Since 2.2.0 use afterDrawLegendItem( ) instead
	 */
	public void afterDrawLegendEntry(Label label, IChartScriptContext icsc);

	/**
	 * Called before drawing the legend item.
	 * 
	 * @param lerh LegendEntryRenderingHints
	 * @param bo   Bounds
	 * @param icsc IChartScriptContext
	 * @since Version 2.2.0
	 */
	public void beforeDrawLegendItem(LegendEntryRenderingHints lerh, Bounds bo, IChartScriptContext icsc);

	/**
	 * Called after drawing the legend item.
	 * 
	 * @param lerh LegendEntryRenderingHints
	 * @param bo   Bounds
	 * @param icsc IChartScriptContext
	 * @since Version 2.2.0
	 */
	public void afterDrawLegendItem(LegendEntryRenderingHints lerh, Bounds bo, IChartScriptContext icsc);

	/**
	 * Called before rendering Series.
	 * 
	 * @param series Series
	 * @param isr    ISeriesRenderer
	 * @param icsc   IChartScriptContext
	 */
	public void beforeDrawSeries(Series series, ISeriesRenderer isr, IChartScriptContext icsc);

	/**
	 * Called after rendering Series.
	 * 
	 * @param series Series
	 * @param isr    ISeriesRenderer
	 * @param icsc   IChartScriptContext
	 */
	public void afterDrawSeries(Series series, ISeriesRenderer isr, IChartScriptContext icsc);

	/**
	 * Called before rendering the title of a Series.
	 * 
	 * @param series Series
	 * @param label  Label
	 * @param icsc   IChartScriptContext
	 */
	public void beforeDrawSeriesTitle(Series series, Label label, IChartScriptContext icsc);

	/**
	 * Called after rendering the title of a Series .
	 * 
	 * @param series Series
	 * @param label  Label
	 * @param icsc   IChartScriptContext
	 */
	public void afterDrawSeriesTitle(Series series, Label label, IChartScriptContext icsc);

	/**
	 * Called before drawing each marker.
	 * 
	 * @param marker Marker
	 * @param dph    DataPointHints
	 * @param icsc   IChartScriptContext
	 */
	public void beforeDrawMarker(Marker marker, DataPointHints dph, IChartScriptContext icsc);

	/**
	 * Called after drawing each marker.
	 * 
	 * @param marker Marker
	 * @param dph    DataPointHints
	 * @param icsc   IChartScriptContext
	 */
	public void afterDrawMarker(Marker marker, DataPointHints dph, IChartScriptContext icsc);

	/**
	 * Called before drawing each marker line in an Axis.
	 * 
	 * @param axis  Axis
	 * @param mLine MarkerLine
	 * @param icsc  IChartScriptContext
	 */
	public void beforeDrawMarkerLine(Axis axis, MarkerLine mLine, IChartScriptContext icsc);

	/**
	 * Called after drawing each marker line in an Axis.
	 * 
	 * @param axis  Axis
	 * @param mLine MarkerLine
	 * @param icsc  IChartScriptContext
	 */
	public void afterDrawMarkerLine(Axis axis, MarkerLine mLine, IChartScriptContext icsc);

	/**
	 * Called before drawing each marker range in an Axis.
	 * 
	 * @param axis   Axis
	 * @param mRange MarkerRange
	 * @param icsc   IChartScriptContext
	 */
	public void beforeDrawMarkerRange(Axis axis, MarkerRange mRange, IChartScriptContext icsc);

	/**
	 * Called after drawing each marker range in an Axis.
	 * 
	 * @param axis   Axis
	 * @param mRange MarkerRange
	 * @param icsc   IChartScriptContext
	 */
	public void afterDrawMarkerRange(Axis axis, MarkerRange mRange, IChartScriptContext icsc);

	/**
	 * Called before drawing each datapoint graphical representation or marker.
	 * 
	 * @param dph  DataPointHints
	 * @param fill Fill
	 * @param icsc IChartScriptContext
	 */
	public void beforeDrawDataPoint(DataPointHints dph, Fill fill, IChartScriptContext icsc);

	/**
	 * Called after drawing each datapoint graphical representation or marker.
	 * 
	 * @param dph  DataPointHints
	 * @param fill Fill
	 * @param icsc IChartScriptContext
	 */
	public void afterDrawDataPoint(DataPointHints dph, Fill fill, IChartScriptContext icsc);

	/**
	 * Called before rendering the label for each datapoint.
	 * 
	 * @param dph   DataPointHints
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void beforeDrawDataPointLabel(DataPointHints dph, Label label, IChartScriptContext icsc);

	/**
	 * Called after rendering the label for each datapoint.
	 * 
	 * @param dph   DataPointHints
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void afterDrawDataPointLabel(DataPointHints dph, Label label, IChartScriptContext icsc);

	/**
	 * Called before rendering curve fitting.
	 * 
	 * @param cf   CurveFitting
	 * @param icsc IChartScriptContext
	 */
	public void beforeDrawFittingCurve(CurveFitting cf, IChartScriptContext icsc);

	/**
	 * Called after rendering curve fitting.
	 * 
	 * @param cf   CurveFitting
	 * @param icsc IChartScriptContext
	 */
	public void afterDrawFittingCurve(CurveFitting cf, IChartScriptContext icsc);

	/**
	 * Called before rendering each label on a given Axis.
	 * 
	 * @param axis  Axis
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void beforeDrawAxisLabel(Axis axis, Label label, IChartScriptContext icsc);

	/**
	 * Called after rendering each label on a given Axis.
	 * 
	 * @param axis  Axis
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void afterDrawAxisLabel(Axis axis, Label label, IChartScriptContext icsc);

	/**
	 * Called before rendering the Title of an Axis.
	 * 
	 * @param axis  Axis
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void beforeDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc);

	/**
	 * Called after rendering the Title of an Axis.
	 * 
	 * @param axis  Axis
	 * @param label Label
	 * @param icsc  IChartScriptContext
	 */
	public void afterDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc);
}
