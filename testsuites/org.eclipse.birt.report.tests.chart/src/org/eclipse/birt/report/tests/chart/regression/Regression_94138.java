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
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Any label(or title) witch contains more than 6 chinese characters in chart
 * can not display correctely.
 * </p>
 * Test description:
 * <p>
 * Set title with more than 6 chinese characters to the chart, generate the
 * image, you can view the image to verify it.
 * </p>
 */

public class Regression_94138 extends ChartTestCase {

	private static String GOLDEN = "Reg_94138.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Reg_94138.jpg"; //$NON-NLS-1$

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
		new Regression_94138();
	}

	/**
	 * Constructor
	 */
	public Regression_94138() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createBarChart();
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

	public void test_regression_94138() throws Exception {
		Regression_94138 st = new Regression_94138();
		assertTrue(st.compareImages(GOLDEN, OUTPUT));
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("测试"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		Plot p = cwaBar.getPlot();

		p.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		p.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		p.getOutline().setVisible(true);

		p.setBackground(ColorDefinitionImpl.CREAM());
		p.setAnchor(Anchor.NORTH_LITERAL);

		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 0, 255),
				ColorDefinitionImpl.create(255, 253, 200), -35, false));
		p.getClientArea().getOutline().setVisible(true);

		// Legend
		Legend lg = cwaBar.getLegend();

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
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getLabel().setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		yAxisPrimary.setPercent(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.YELLOW());
		sdY2.getSeries().add(bs2);

		return cwaBar;

	}
}
