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
import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * <p>
 * <b>Regression description:</b>
 * </p>
 * Different chart, color by categories, Y series, tick Use Palette As Line
 * Color, can not take effect
 * <p>
 * Different chart, color by categories, Y series, tick Use Palette As Line
 * Color, can not take effect Steps:
 * <ol>
 * <li>Create a different chart
 * <li>Open chart builder, go to "Format Chart" tab, select Series, set Color By
 * Categories
 * <li>Select Value(Y) Series, tick "Use Palette As Line Color"
 * <li>Preview
 * </ol>
 * <p>
 * Actual Rerulst: Can not take effect
 * <p>
 * Expected Results: Can be take effect
 * </p>
 * <b>Test description:</b>
 * <p>
 * Create a Different chart, call setPaletteLineColor( true ). On legend,
 * setItemType( LegendItemType.CATEGORIES_LITERAL ); compare chart which is
 * generated in output folder with golden file.
 * </p>
 */

public class Regression_160432 extends ChartTestCase {

	private static String OUTPUT = "Difference_AfterDrawLegendItem.jpg"; //$NON-NLS-1$
	private static String GOLDEN = "Difference_AfterDrawLegendItem.jpg"; //$NON-NLS-1$

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
		new Regression_160432();
	}

	/**
	 * Constructor
	 */
	public Regression_160432() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createDifferenceChart();
		BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		dRenderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile(OUTPUT)); // $NON-NLS-1$
		Bounds bo = BoundsImpl.create(0, 0, 600, 600);
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

	public void test_regression_160432() throws Exception {
		Regression_160432 st = new Regression_160432();
		assertTrue(st.compareImages(GOLDEN, OUTPUT));
	}

	/**
	 * Creates a Difference chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createDifferenceChart() {
		ChartWithAxes cwaDifference = ChartWithAxesImpl.create();

		// Chart Type
		cwaDifference.setType("Difference Chart");

		// Title
		cwaDifference.getTitle().getLabel().getCaption().setValue("Difference Chart Using beforeDrawSeries"); //$NON-NLS-1$
		cwaDifference.getTitle().getLabel().setVisible(true);

		// Legend
		Legend lg = cwaDifference.getLegend();
		lg.setVisible(true);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaDifference).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaDifference).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });

		DifferenceDataSet DifferenceValue = DifferenceDataSetImpl
				.create(new DifferenceEntry[] { new DifferenceEntry(143.26, 43.26), new DifferenceEntry(156.55, 56.55),
						new DifferenceEntry(92.25, 195.25), new DifferenceEntry(47.56, 147.56) });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);
		sdX.getSeriesPalette().update(-2);

		// Y-Series
		DifferenceSeries ds = (DifferenceSeries) DifferenceSeriesImpl.create();
		ds.setDataSet(DifferenceValue);
		ds.setPaletteLineColor(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.setSeriesPalette(PaletteImpl.create(ColorDefinitionImpl.BLUE()));
		sdY.getSeries().add(ds);

		return cwaDifference;

	}

}
