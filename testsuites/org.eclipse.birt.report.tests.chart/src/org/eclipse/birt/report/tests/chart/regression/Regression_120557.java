/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
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
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * The property of scatter chart "Show Lines as Curves" exist, but does not work
 * </p>
 * Test description:
 * </p>
 * The property of scatter chart "Show Lines as Curves" works
 * </p>
 */

public class Regression_120557 extends ChartTestCase {

	private static String GOLDEN = "Regression_120557.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_120557.jpg"; //$NON-NLS-1$

	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * The jpg rendering device
	 */
	private IDeviceRenderer dRenderer = null;

	private GeneratedChartState gcs = null;

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Regression_120557();
	}

	/**
	 * Constructor
	 */
	public Regression_120557() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createScatterChart();
		BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		dRenderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile(OUTPUT));

		Bounds bo = BoundsImpl.create(0, 0, 500, 500);
		bo.scale(72d / dRenderer.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		try {
			gcs = gr.build(dRenderer.getDisplayServer(), cm, bo, null, null, null);
			gr.render(dRenderer, gcs);
		} catch (ChartException e) {
			e.printStackTrace();
		}
	}

	public void test_regression_120557() throws Exception {
		Regression_120557 st = new Regression_120557();
		assertTrue(st.compareImages(GOLDEN, OUTPUT));
	}

	/**
	 * Creates a scatter chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Chart Type
		cwaScatter.setType("Scatter Chart");

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Sample Scatter Chart"); //$NON-NLS-1$
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.GREY());

		// Plot
		Plot p = cwaScatter.getPlot();

		p.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		p.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		p.getOutline().setVisible(true);

		p.setBackground(ColorDefinitionImpl.CREAM());
		p.setAnchor(Anchor.NORTH_LITERAL);
		p.getClientArea().getOutline().setVisible(true);

		// Legend
		Legend lg = cwaScatter.getLegend();
		lg.setVisible(false);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLACK().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue(""); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 22.49, 163.55, -65.43, 0.0, -107.0 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { -36.53, 43.9, 8.29, 97.45, 32.0 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();

		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("("); //$NON-NLS-1$
		dp.setSuffix(")"); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss.getLabel().setVisible(true);
		ss.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(117, 137, 223), LineStyle.SOLID_LITERAL, 1));
		ss.setDataSet(dsNumericValues2);
		ss.setCurve(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(ss);

		return cwaScatter;

	}
}
