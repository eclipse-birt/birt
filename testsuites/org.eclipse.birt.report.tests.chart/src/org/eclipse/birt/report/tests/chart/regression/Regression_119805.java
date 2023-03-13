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
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
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
 * Scatter chart, add multiple Y Axis, has a line
 * </p>
 * Test description:
 * <p>
 * Create a two Y Axis scatter chart, view the generated image
 * </p>
 */

public class Regression_119805 extends ChartTestCase {

	private static String GOLDEN = "Reg_119805.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Reg_119805.jpg"; //$NON-NLS-1$

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
		new Regression_119805();
	}

	/**
	 * Constructor
	 */
	public Regression_119805() {
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
		dRenderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile(OUTPUT)); // $NON-NLS-1$
		Bounds bo = BoundsImpl.create(0, 0, 500, 500);
		bo.scale(72d / dRenderer.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		try {
			gcs = gr.build(dRenderer.getDisplayServer(), cm, bo, null, null, null);
			gr.render(dRenderer, gcs);
		} catch (ChartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test_regression_119805() throws Exception {
		Regression_119805 st = new Regression_119805();
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
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLACK().darker());

		// Y-Axis 1
		Axis yAxisPrimary1 = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary1.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary1.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y Axis 2
		Axis yAxisPrimary2 = AxisImpl.create(Axis.ORTHOGONAL);
		yAxisPrimary2.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary2.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getAssociatedAxes().add(yAxisPrimary2);

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 22.49, 163.55, -65.43, 0.0, -107.0 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { -36.53, 43.9, 8.29, 97.45, 32.0 });
		NumberDataSet dsNumericValues3 = NumberDataSetImpl.create(new double[] { 15.0, -78.34, 4.86, 65.98, 98.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(3);

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series 1
		ScatterSeries ss1 = (ScatterSeries) ScatterSeriesImpl.create();

		DataPoint dp1 = ss1.getDataPoint();
		dp1.getComponents().clear();
		dp1.setPrefix("("); //$NON-NLS-1$
		dp1.setSuffix(")"); //$NON-NLS-1$
		dp1.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp1.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss1.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss1.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss1.getLabel().setVisible(true);
		ss1.setDataSet(dsNumericValues2);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		yAxisPrimary1.getSeriesDefinitions().add(sdY1);
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		ss1.setPaletteLineColor(true);
		sdY1.getSeries().add(ss1);

		// Y-series 2
		ScatterSeries ss2 = (ScatterSeries) ScatterSeriesImpl.create();

		DataPoint dp2 = ss2.getDataPoint();
		dp2.getComponents().clear();
		dp2.setPrefix("("); //$NON-NLS-1$
		dp2.setSuffix(")"); //$NON-NLS-1$
		dp2.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp2.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss2.getLabel().setVisible(true);
		ss2.setDataSet(dsNumericValues3);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary2.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.RED());
		ss2.setPaletteLineColor(true);
		sdY2.getSeries().add(ss2);
		return cwaScatter;

	}
}
