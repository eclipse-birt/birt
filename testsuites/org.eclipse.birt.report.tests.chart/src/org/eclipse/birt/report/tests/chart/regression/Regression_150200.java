/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.regression;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Stacked Area Chart: fill.set() ignored in beforeDrawDataPoint()
 * </p>
 * Test description:
 * </p>
 * Create a Stacked Area chart, add script function beforeDrawSeries()
 * </p>
 */

public class Regression_150200 extends ChartTestCase {

	private static String OUTPUT = "Regression_150200.jpg"; //$NON-NLS-1$

	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * The swing rendering device
	 */
	private IDeviceRenderer dRenderer = null;

	private GeneratedChartState gcs = null;

	/**
	 * execute application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new Regression_150200();
	}

	/**
	 * Constructor
	 */
	public Regression_150200() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createAreaChart();
		BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		dRenderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile(OUTPUT));

		Bounds bo = BoundsImpl.create(0, 0, 600, 600);
		bo.scale(72d / dRenderer.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		try {
			gcs = gr.build(dRenderer.getDisplayServer(), cm, bo, null, null, null);
			gr.render(dRenderer, gcs);
		} catch (ChartException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a Area chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createAreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// TODO: research running script under plugin test.

		cwaArea.setScript("function beforeDrawSeries( series, seriesRenderer, context )" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.data);"//$NON-NLS-1$
				+ "importPackage(Packages.org.eclipse.birt.chart.model.attribute.impl);"//$NON-NLS-1$
				+ "seriesText = series.getSeriesIdentifier();"//$NON-NLS-1$
				+ "if (seriesText == \"A\") "//$NON-NLS-1$
				+ "{((SeriesDefinition)(series.eContainer( ))).getSeriesPalette( ).update( ColorDefinitionImpl.create(255, 0, 0) );}"//$NON-NLS-1$
				+ "else if (seriesText == \"B\")"//$NON-NLS-1$
				+ "{((SeriesDefinition)(series.eContainer( ))).getSeriesPalette( ).update( ColorDefinitionImpl.create(0, 0, 255));}" //$NON-NLS-1$
				+ "else if (seriesText == \"C\")"//$NON-NLS-1$
				+ "{((SeriesDefinition)(series.eContainer( ))).getSeriesPalette( ).update( ColorDefinitionImpl.create(0, 255, 0));}" //$NON-NLS-1$
				+ "else if (seriesText == \"D\")"//$NON-NLS-1$
				+ "{((SeriesDefinition)(series.eContainer( ))).getSeriesPalette( ).update( ColorDefinitionImpl.create(0, 255, 255));}}" //$NON-NLS-1$
		);

		// Chart Type
		cwaArea.setType("Area Chart");
		cwaArea.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Area Chart Using beforeDrawSeries"); //$NON-NLS-1$
		cwaArea.getTitle().getLabel().setVisible(true);

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.setVisible(false);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create(new String[] { "Keyboards", "Moritors", "Printers",
				"Mortherboards", "Telephones", "Mouse", "NetCards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create(new double[] { 143.26, 156.55, 95.25, 47.56, 0, 88.9, 93.25 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create(new double[] { 143.26, 0, 95.25, 47.56, 35.8, 0, 123.45 });
		NumberDataSet dsNumericValues3 = NumberDataSetImpl
				.create(new double[] { 143.26, 47.56, 35.8, 0, 95.25, 0, 123.45 });
		NumberDataSet dsNumericValues4 = NumberDataSetImpl
				.create(new double[] { 143.26, 0, 35.8, 0, 95.25, 47.56, 123.45 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create();
		as1.setSeriesIdentifier("A"); //$NON-NLS-1$
		as1.getLabel().setVisible(false);
		as1.setDataSet(dsNumericValues1);
		as1.setStacked(true);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY1.getSeries().add(as1);

		// Y-Series-2
		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create();
		as2.setSeriesIdentifier("B"); //$NON-NLS-1$
		as2.getLabel().setVisible(false);
		as2.setDataSet(dsNumericValues2);
		as2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.RED());
		sdY2.getSeries().add(as2);

		// Y-Series-3
		AreaSeries as3 = (AreaSeries) AreaSeriesImpl.create();
		as3.setSeriesIdentifier("C"); //$NON-NLS-1$
		as3.getLabel().setVisible(false);
		as3.setDataSet(dsNumericValues3);
		as3.setStacked(true);

		SeriesDefinition sdY3 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY3);
		sdY3.getSeriesPalette().update(ColorDefinitionImpl.RED());
		sdY3.getSeries().add(as3);

		// Y-Series-4
		AreaSeries as4 = (AreaSeries) AreaSeriesImpl.create();
		as4.setSeriesIdentifier("D"); //$NON-NLS-1$
		as4.getLabel().setVisible(false);
		as4.setDataSet(dsNumericValues4);
		as4.setStacked(true);

		SeriesDefinition sdY4 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY4);
		sdY4.getSeriesPalette().update(ColorDefinitionImpl.RED());
		sdY4.getSeries().add(as4);

		return cwaArea;

	}
}
