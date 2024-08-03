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
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
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
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;
import org.junit.Ignore;

/**
 * Regression description:
 * </p>
 * Chart title alignment error
 * </p>
 * Test description:
 * <p>
 * Set chart title alignment to top right, view the jpg
 * </p>
 */

public class Regression_133237 extends ChartTestCase {

	private static String GOLDEN = "Regression_133237.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_133237.jpg"; //$NON-NLS-1$

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
		new Regression_133237();
	}

	/**
	 * Constructor
	 */
	public Regression_133237() {
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

	@Ignore("https://github.com/eclipse-birt/birt/issues/1828")
	public void test_regression_133237() throws Exception {
		if (Boolean.TRUE) {
			return;
		}
		Regression_133237 st = new Regression_133237();
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
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getTitle().setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 3));
		cwaBar.getTitle().setInsets(InsetsImpl.create(13, 13, 13, 13));
		cwaBar.getTitle().getLabel().setInsets(InsetsImpl.create(10, 13, 10, 13));
		cwaBar.getTitle().getLabel().setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 3));
		// cwaBar.getTitle().setAnchor( Anchor.WEST_LITERAL );
		TextAlignment ta = TextAlignmentImpl.create();
		ta.setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
		ta.setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
		cwaBar.getTitle().getLabel().getCaption().getFont().setAlignment(ta);
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

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
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		return cwaBar;

	}
}
