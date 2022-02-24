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
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Script setting label color does not work for pie chart series
 * </p>
 * Test description:
 * </p>
 * Create a pie chart, add script function beforeDrawSeriesTitle()
 * </p>
 */

public class Regression_155185 extends ChartTestCase {

	private static String GOLDEN = "Regression_155185.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_155185.jpg"; //$NON-NLS-1$

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
		new Regression_155185();
	}

	/**
	 * Constructor
	 */
	public Regression_155185() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createPieChart();
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

	public void test_regression_155185() throws Exception {
		Regression_155185 st = new Regression_155185();
		assertTrue(st.compareImages(GOLDEN, OUTPUT));
	}

	/**
	 * Creates a line chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createPieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Chart Type
		cwoaPie.setType("Pie Chart");
		cwoaPie.setScript("function beforeDrawSeriesTitle(series, label, scriptContext)" //$NON-NLS-1$
				+ "{label.setVisible(true);"//$NON-NLS-1$
				+ "label.getCaption().setValue(\"Cities\"); "//$NON-NLS-1$
				+ "label.getCaption().getColor().set(222, 32, 182);"//$NON-NLS-1$
				+ "label.getCaption().getFont().setItalic(true);" //$NON-NLS-1$
				+ "label.getCaption().getFont().setRotation(30);" //$NON-NLS-1$
				+ "label.getCaption().getFont().setStrikethrough(true);" //$NON-NLS-1$
				+ "label.getCaption().getFont().setSize(14);" //$NON-NLS-1$
				+ "label.getCaption().getFont().setName(\"Arial\");" //$NON-NLS-1$
				+ "label.getOutline().setVisible(true);" //$NON-NLS-1$
				+ "label.getOutline().setThickness(3);" //$NON-NLS-1$
				+ "series.getLabel().getCaption().getColor().set(12, 232, 182); "//$NON-NLS-1$ )
				+ "series.getLabel().getCaption().getFont().setItalic(true);"//$NON-NLS-1$
				+ "series.getLabel().getCaption().getFont().setRotation(30);"//$NON-NLS-1$
				+ "series.getLabel().getCaption().getFont().setStrikethrough(true);"//$NON-NLS-1$
				+ "series.getLabel().getCaption().getFont().setSize(14);"//$NON-NLS-1$
				+ "series.getLabel().getCaption().getFont().setName(\"Arial\");"//$NON-NLS-1$
				+ "series.getLabel().getOutline().setVisible(true);" //$NON-NLS-1$
				+ "series.getLabel().getOutline().setThickness(3); }" //$NON-NLS-1$
		);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(dsStringValue);

		SeriesDefinition series = SeriesDefinitionImpl.create();
		series.getSeries().add(seCategory);
		cwoaPie.getSeriesDefinitions().add(series);

		PieSeries ps = (PieSeries) PieSeriesImpl.create();
		ps.setDataSet(dsNumericValues1);

		SeriesDefinition seGroup1 = SeriesDefinitionImpl.create();
		series.getSeriesPalette().update(-2);
		series.getSeriesDefinitions().add(seGroup1);
		seGroup1.getSeries().add(ps);

		return cwoaPie;

	}
}
