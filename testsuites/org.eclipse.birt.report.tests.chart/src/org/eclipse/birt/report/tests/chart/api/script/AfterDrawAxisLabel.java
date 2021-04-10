/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.api.script;

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
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
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
 * Description:
 * </p>
 * function afterDrawAxisLabel()
 * </p>
 */

public class AfterDrawAxisLabel extends ChartTestCase {

	private static String OUTPUT = "AfterDrawAxisLabel.jpg"; //$NON-NLS-1$

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
		new AfterDrawAxisLabel();
	}

	/**
	 * Constructor
	 */
	public AfterDrawAxisLabel() {
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
		} catch (ChartException e) {
			e.printStackTrace();
		}
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

		// TODO: research running script under plugin test.

		cwaLine.setScript("function afterDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.java.lang); " //$NON-NLS-1$
				+ "value = label.getCaption().getValue();" //$NON-NLS-1$
				+ "System.out.println(value); " //$NON-NLS-1$
				+ "if (value ==\"Keyboards\"|| value == \"Monitors\"|| value == \"Printers\"|| value == \"Motherboards\") " //$NON-NLS-1$
				+ "{System.out.println(\"X Axis\");} " //$NON-NLS-1$
				+ "else{System.out.println(\"Y Axis\");}}" //$NON-NLS-1$
		);

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.setVisible(false);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(false);
		xAxisPrimary.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 1));

		xAxisPrimary.getLabel().setVisible(true);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.create(32, 208, 0));
		xAxisPrimary.getLabel().getCaption().getFont().setItalic(true);
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(30);
		xAxisPrimary.getLabel().getCaption().getFont().setStrikethrough(true);
		xAxisPrimary.getLabel().getCaption().getFont().setSize(14);
		xAxisPrimary.getLabel().getCaption().getFont().setName("Arial");
		xAxisPrimary.getLabel().getOutline().setVisible(true);
		xAxisPrimary.getLabel().getOutline().setThickness(3);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		yAxisPrimary.getLabel().setVisible(true);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.create(208, 32, 0));
		yAxisPrimary.getLabel().getCaption().getFont().setItalic(true);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(30);
		yAxisPrimary.getLabel().getCaption().getFont().setStrikethrough(true);
		yAxisPrimary.getLabel().getCaption().getFont().setSize(14);
		yAxisPrimary.getLabel().getCaption().getFont().setName("Arial");
		xAxisPrimary.getLabel().getOutline().setVisible(true);
		xAxisPrimary.getLabel().getOutline().setThickness(3);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Monitors", "Printers", "Motherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);
		seBase.getLabel().setVisible(false);

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

		return cwaLine;
	}
}