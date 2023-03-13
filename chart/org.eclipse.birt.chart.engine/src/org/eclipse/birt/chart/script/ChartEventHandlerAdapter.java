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
import org.eclipse.birt.chart.factory.GeneratedChartState;
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
import org.eclipse.birt.chart.render.ISeriesRenderer;

/**
 * An adapter (empty implementation of IChartEventHandler) makes the interface
 * easier for the user. This is required to provide backward compatibility when
 * new methods are added to the interface in future versions.
 */
public class ChartEventHandlerAdapter implements IChartEventHandler {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartEventHandler#afterDrawLegendItem(org.
	 * eclipse.birt.chart.computation.LegendEntryRenderingHints,
	 * org.eclipse.birt.chart.model.attribute.Bounds,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawLegendItem(LegendEntryRenderingHints lerh, Bounds bo, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartEventHandler#beforeDrawLegendItem(org.
	 * eclipse.birt.chart.computation.LegendEntryRenderingHints,
	 * org.eclipse.birt.chart.model.attribute.Bounds,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawLegendItem(LegendEntryRenderingHints lerh, Bounds bo, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDataSetFilled(org.
	 * eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.data.DataSet,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDataSetFilled(Series series, DataSet dataSet, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDataSetFilled(org
	 * .eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDataSetFilled(Series series, IDataSetProcessor idsp, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawAxisLabel(org
	 * .eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawAxisLabel(Axis axis, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawAxisTitle(org
	 * .eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawBlock(org.
	 * eclipse.birt.chart.model.layout.Block,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawBlock(Block block, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawDataPoint(org
	 * .eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawDataPoint(DataPointHints dph, Fill fill, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#
	 * beforeDrawDataPointLabel(org.eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawDataPointLabel(DataPointHints dph, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawFittingCurve(
	 * org.eclipse.birt.chart.model.component.CurveFitting,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawFittingCurve(CurveFitting cf, IChartScriptContext icsc) {
	}

	@Override
	@SuppressWarnings("deprecation")
	public void beforeDrawLegendEntry(Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarker(org.
	 * eclipse.birt.chart.model.attribute.Marker,
	 * org.eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawMarker(Marker marker, DataPointHints dph, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarker(org.
	 * eclipse.birt.chart.model.attribute.Marker,
	 * org.eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawMarker(Marker marker, DataPointHints dph, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerLine(
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerLine,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawMarkerLine(Axis axis, MarkerLine mLine, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerRange(
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerRange,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawMarkerRange(Axis axis, MarkerRange mRange, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawSeries(org.
	 * eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.render.ISeriesRenderer,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawSeries(Series series, ISeriesRenderer isr, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawSeriesTitle(
	 * org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawSeriesTitle(Series series, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeGeneration(org.
	 * eclipse.birt.chart.model.Chart,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeGeneration(Chart cm, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeRendering(org.
	 * eclipse.birt.chart.factory.GeneratedChartState,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeRendering(GeneratedChartState gcs, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterGeneration(org.
	 * eclipse.birt.chart.factory.GeneratedChartState,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterGeneration(GeneratedChartState gcs, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterRendering(org.
	 * eclipse.birt.chart.factory.GeneratedChartState,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterRendering(GeneratedChartState gcs, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawBlock(org.
	 * eclipse.birt.chart.model.layout.Block,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawBlock(Block block, IChartScriptContext icsc) {
	}

	@Override
	@SuppressWarnings("deprecation")
	public void afterDrawLegendEntry(Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawSeries(org.
	 * eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.render.ISeriesRenderer,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawSeries(Series series, ISeriesRenderer isr, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawSeriesTitle(
	 * org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawSeriesTitle(Series series, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawMarkerLine(org
	 * .eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerLine,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawMarkerLine(Axis axis, MarkerLine mLine, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawMarkerRange(
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerRange,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawMarkerRange(Axis axis, MarkerRange mRange, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawDataPoint(org.
	 * eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawDataPoint(DataPointHints dph, Fill fill, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawDataPointLabel
	 * (org.eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawDataPointLabel(DataPointHints dph, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawFittingCurve(
	 * org.eclipse.birt.chart.model.component.CurveFitting,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawFittingCurve(CurveFitting cf, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawAxisLabel(org.
	 * eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawAxisLabel(Axis axis, Label label, IChartScriptContext icsc) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#afterDrawAxisTitle(org.
	 * eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void afterDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc) {
	}

	@Override
	public void afterComputations(Chart cm, PlotComputation oComputations) {
	}

	@Override
	public void beforeComputations(Chart cm, PlotComputation oComputations) {
	}

}
