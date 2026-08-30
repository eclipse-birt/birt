/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.render;

import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;

/**
 * Builds the charts that {@link LineStepRenderTest} renders. One code path
 * builds the step chart and the reference chart, which differ only in the
 * interpolation, so their data point coordinates compare exactly.
 */
public final class StepChartFixture {

	/** The category labels. A chart uses the first {@code values.length} of them. */
	private static final String[] CATEGORIES = { "A", "B", "C", "D", "E" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/** The colour of the first value series, <code>#2a78d6</code>. */
	private static final int[] SERIES_BLUE = { 42, 120, 214 };

	/** The colour of the second value series, <code>#d95926</code>. */
	private static final int[] SERIES_ORANGE = { 217, 89, 38 };

	private StepChartFixture() {
	}

	/**
	 * Builds a chart with a category axis and a value axis. A {@code null} mode
	 * builds the reference chart; a {@code null} entry in {@code values} is a
	 * missing value.
	 *
	 * @param mode
	 * @param values
	 * @param o
	 * @throws IllegalArgumentException
	 */
	public static ChartWithAxes chart(LineInterpolation mode, Double[] values, Options o) {
		if (values.length > CATEGORIES.length) {
			throw new IllegalArgumentException(
					"at most " + CATEGORIES.length + " values are supported, got " + values.length); //$NON-NLS-1$ //$NON-NLS-2$
		}

		ChartWithAxes chart = ChartWithAxesImpl.create();

		Axis xAxis = chart.getPrimaryBaseAxes()[0];
		xAxis.setType(AxisType.TEXT_LITERAL);
		xAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxis.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		Axis yAxis = chart.getPrimaryOrthogonalAxis(xAxis);

		// Write an optional feature only if it differs from the model default.
		if (o.transposed) {
			chart.setTransposed(true);
		}
		if (o.dimension != ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			chart.setDimension(o.dimension);
		}
		if (o.percent) {
			yAxis.setPercent(true);
		}
		if (o.dimension == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
			addAncillaryAxis(xAxis);
		}

		TextDataSet categoryData = TextDataSetImpl.create(Arrays.copyOf(CATEGORIES, values.length));

		Series baseSeries = SeriesImpl.create();
		baseSeries.setDataSet(categoryData);
		SeriesDefinition baseDefinition = SeriesDefinitionImpl.create();
		xAxis.getSeriesDefinitions().add(baseDefinition);
		baseDefinition.getSeries().add(baseSeries);

		SeriesDefinition valueDefinition = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add(valueDefinition);

		valueDefinition.getSeries().add(configure(newValueSeries(o), "series1", values, o, mode)); //$NON-NLS-1$
		if (o.secondValues != null) {
			valueDefinition.getSeries().add(configure(newValueSeries(o), "series2", o.secondValues, o, mode)); //$NON-NLS-1$
		}

		applyPalette(valueDefinition);

		return chart;
	}

	private static LineSeries newValueSeries(Options o) {
		return o.area ? (LineSeries) AreaSeriesImpl.create() : (LineSeries) LineSeriesImpl.create();
	}

	/**
	 * Puts the data, the identifier and the options on one value series
	 */
	private static LineSeries configure(LineSeries series, String identifier, Double[] values, Options o,
			LineInterpolation mode) {
		NumberDataSet valueData = NumberDataSetImpl.create(values);
		series.setDataSet(valueData);
		series.setSeriesIdentifier(identifier);
		if (o.stacked) {
			series.setStacked(true);
		}
		if (o.curve) {
			series.setCurve(true);
		}
		if (!o.connectMissingValue) {
			series.setConnectMissingValue(false);
		}
		if (o.labelsVisible) {
			series.getLabel().setVisible(true);
		}
		if (o.shadowColor != null) {
			series.setShadowColor(o.shadowColor);
		}
		if (mode != null) {
			series.setInterpolation(mode);
		}
		// The BOX marker of the series initializer never produces an oval.
		series.getMarkers().get(0).setVisible(o.markersVisible);
		return series;
	}

	/**
	 * Sets the series colours. A new series definition has an empty palette,
	 * which {@code Line.renderSeries} rejects.
	 */
	private static void applyPalette(SeriesDefinition valueDefinition) {
		List<Fill> entries = valueDefinition.getSeriesPalette().getEntries();
		entries.clear();
		entries.add(color(SERIES_BLUE));
		entries.add(color(SERIES_ORANGE));
	}

	/** Creates a colour, a new instance per call for EMF containment. */
	private static ColorDefinition color(int[] rgb) {
		return ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Adds the ancillary (depth) axis that a three dimensional chart needs
	 */
	private static void addAncillaryAxis(Axis xAxis) {
		Axis zAxis = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxis.setType(AxisType.TEXT_LITERAL);
		zAxis.setLabelPosition(Position.BELOW_LITERAL);
		zAxis.setTitlePosition(Position.BELOW_LITERAL);
		zAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		zAxis.setOrientation(Orientation.HORIZONTAL_LITERAL);
		xAxis.getAncillaryAxes().add(zAxis);
		zAxis.getSeriesDefinitions().add(SeriesDefinitionImpl.create());
	}

	/** The options that the render tests set. */
	public static final class Options {

		private boolean transposed;

		private ChartDimension dimension = ChartDimension.TWO_DIMENSIONAL_LITERAL;

		private boolean curve;

		private boolean stacked;

		private boolean percent;

		/** The model default of {@code LineSeries.connectMissingValue} is true. */
		private boolean connectMissingValue = true;

		private boolean markersVisible = true;

		private boolean labelsVisible;

		private boolean area;

		private ColorDefinition shadowColor;

		private Double[] secondValues;

		public Options transposed(boolean value) {
			this.transposed = value;
			return this;
		}

		public boolean isTransposed() {
			return transposed;
		}

		public Options dimension(ChartDimension value) {
			this.dimension = value;
			return this;
		}

		public Options curve(boolean value) {
			this.curve = value;
			return this;
		}

		public Options stacked(boolean value) {
			this.stacked = value;
			return this;
		}

		public Options percent(boolean value) {
			this.percent = value;
			return this;
		}

		public Options connectMissingValue(boolean value) {
			this.connectMissingValue = value;
			return this;
		}

		public Options markersVisible(boolean value) {
			this.markersVisible = value;
			return this;
		}

		public Options labelsVisible(boolean value) {
			this.labelsVisible = value;
			return this;
		}

		public Options area(boolean value) {
			this.area = value;
			return this;
		}

		public Options shadowColor(ColorDefinition value) {
			this.shadowColor = value;
			return this;
		}

		public Options secondValues(Double[] value) {
			this.secondValues = value == null ? null : value.clone();
			return this;
		}
	}
}
