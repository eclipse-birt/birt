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
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
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
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Error pops up when bind a dataset without any record with Chart
 * </p>
 * Test description:
 * <p>
 * Bind a null data set to a chart, view the exception
 * </p>
 */

public class Regression_121954 extends ChartTestCase {

	private static String OUTPUT = "Reg_121954.jpg"; //$NON-NLS-1$

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
		new Regression_121954();
	}

	/**
	 * Constructor
	 */
	public Regression_121954() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createAreaChart();
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
		}
	}

	public void test_regression_121954() throws Exception {
		new Regression_121954();
	}

	/**
	 * Creates a area chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createAreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");
		cwaArea.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwaArea.setRotation(
				Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.createY(45), Angle3DImpl.createX(-20), }));

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaArea.getPlot().getClientArea().getOutline().setVisible(false);
		cwaArea.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.RIGHT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Z-Axis
		Axis zAxisPrimary = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		zAxisPrimary.getTitle().getCaption().setValue("Z Axis Title"); //$NON-NLS-1$
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setPrimaryAxis(true);
		FontDefinition fd1 = FontDefinitionImpl.create("Arial", (float) 20.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		zAxisPrimary.getLabel().getCaption().setFont(fd1);
		zAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		zAxisPrimary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setType(AxisType.TEXT_LITERAL);
		zAxisPrimary.getMajorGrid().setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 2));
		zAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		zAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		cwaArea.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisPrimary);

		cwaArea.getPrimaryOrthogonalAxis(cwaArea.getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont()
				.setRotation(0);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create(new String[] {});
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {});

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as.getLabel().setVisible(true);
		as.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as.setDataSet(dsNumericValues1);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(as);

		return cwaArea;
	}
}
