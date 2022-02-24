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
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Drag a line chart to editor.Set the X-axis/Y-axis type to be text. Exception
 * message show on preview dialog.
 * </p>
 * Test description:
 * <p>
 * Create a line chart, set the X-axis/Y-axis type to be text, check if it
 * throws exception
 * </p>
 */

public class Regression_76914 extends ChartTestCase {

	private static String OUTPUT = "Regression_76914.jpg"; //$NON-NLS-1$

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
		new Regression_76914();
	}

	/**
	 * Constructor
	 */
	public Regression_76914() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createLineChart();
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
			fail();
		} catch (ChartException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// success

		}
	}

	public void test_regression_76914() throws Exception {
		new Regression_76914();
	}

	/**
	 * Creates a line chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart");

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.TEXT_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

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
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(220, 50, 227), LineStyle.DOTTED_LITERAL, 3));
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(true);
		ls.setDataSet(dsNumericValues1);
		ls.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls2.getLabel().setVisible(true);
		ls2.setDataSet(dsNumericValues2);
		ls2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(ls2);

		return cwaLine;
	}
}
