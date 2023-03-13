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
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Description:
 * </p>
 * function beforeDrawMarkerRange()
 * </p>
 */

public class BeforeDrawMarkerRange extends ChartTestCase {

	private static String OUTPUT = "BeforeDrawMarkerRange.jpg"; //$NON-NLS-1$

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
		new BeforeDrawMarkerRange();
	}

	/**
	 * Constructor
	 */
	public BeforeDrawMarkerRange() {
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
	 * Creates a bar chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Bar Chart");

		cwaLine.setScript("function beforeDrawMarkerRange(axis, range, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.attribute); "//$NON-NLS-1$
				+ "if (axis.getType() == AxisType.TEXT_LITERAL)"//$NON-NLS-1$
				+ "{range.getLabel().getCaption().getColor().set(21, 244, 231);" //$NON-NLS-1$ )
				+ "range.getLabel().getCaption().getFont().setItalic(true);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setRotation(30);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setStrikethrough(true);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setSize(14);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setName(\"Arial\");" //$NON-NLS-1$
				+ "range.getLabel().getOutline().setVisible(true);" //$NON-NLS-1$
				+ "range.getLabel().getOutline().setThickness(3);}"//$NON-NLS-1$
				+ "else{range.getLabel().getCaption().getColor().set(244, 21, 231);" //$NON-NLS-1$ )
				+ "range.getLabel().getCaption().getFont().setRotation(45);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setStrikethrough(false);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setSize(14);" //$NON-NLS-1$
				+ "range.getLabel().getCaption().getFont().setName(\"Curse\");" //$NON-NLS-1$
				+ "range.getLabel().getOutline().setVisible(false);}}" //$NON-NLS-1$
		);

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.setVisible(false);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		MarkerRange mrx = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(1),
				NumberDataElementImpl.create(2), null);
		mrx.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		MarkerRange mry = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(40),
				NumberDataElementImpl.create(100), null);
		mry.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY(), LineStyle.DASH_DOTTED_LITERAL, 4));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(true);
		ls.setDataSet(dsNumericValues1);
		ls.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(ls);

		return cwaLine;
	}
}
