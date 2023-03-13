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
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Reder a meter chart clockwise
 * </p>
 * Test description:
 * <p>
 * RuntimeContext.setRightToLeft(true), independent of locale (Arabic), create a
 * meter chart, verify if it can be redered anti-clockwise
 * </p>
 */

public class Regression_132783 extends ChartTestCase {

	private static String GOLDEN = "Regression_132783.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_132783.jpg"; //$NON-NLS-1$

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
		new Regression_132783();
	}

	/**
	 * Constructor
	 */
	public Regression_132783() {
		final PluginSettings ps = PluginSettings.instance();
		try {

			// ULocale.setDefault( new ULocale( "ar_YE" ) );

			dRenderer = ps.getDevice("dv.JPG");//$NON-NLS-1$

		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createMeterChart();
		BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
		dRenderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile(OUTPUT)); // $NON-NLS-1$
		Bounds bo = BoundsImpl.create(0, 0, 500, 500);
		bo.scale(72d / dRenderer.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		try {
			RunTimeContext rtc = new RunTimeContext();
			rtc.setRightToLeft(true);
			gcs = gr.build(dRenderer.getDisplayServer(), cm, bo, null, rtc, null);
			gr.render(dRenderer, gcs);
		} catch (ChartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ULocale.setDefault(new ULocale("en_US"));
	}

	public void test_regression_132783() throws Exception {
		Regression_132783 st = new Regression_132783();
		assertTrue(st.compareImages(GOLDEN, OUTPUT));
	}

	/**
	 * Creates a meter chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createMeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		dChart.getTitle().getLabel().getCaption().setValue("City Temperature");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		lg.setVisible(false);

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "London", "Madrid", "Rome", "Moscow" });
		NumberDataSet seriesValues = NumberDataSetImpl.create(new double[] { 21.0, 39.0, 30.0, 10.0 });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		final Fill[] fiaBase = { ColorDefinitionImpl.ORANGE(),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false),
				ColorDefinitionImpl.CREAM(), ColorDefinitionImpl.RED(), ColorDefinitionImpl.GREEN(),
				ColorDefinitionImpl.BLUE().brighter(), ColorDefinitionImpl.CYAN().darker(), };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		// Dial
		DialSeries seDial = (DialSeries) DialSeriesImpl.create();
		seDial.setDataSet(seriesValues);
		seDial.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		NumberFormatSpecifier nfs = NumberFormatSpecifierImpl.create();
		nfs.setSuffix("`C");
		nfs.setFractionDigits(0);
		seDial.getDial().setFormatSpecifier(nfs);
		seDial.setSeriesIdentifier("Temperature");
		seDial.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial.getDial().setStartAngle(-45);
		seDial.getDial().setStopAngle(225);
		seDial.getDial().getMajorGrid().getTickAttributes().setVisible(true);
		seDial.getDial().getMajorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial.getDial().getScale().setMax(NumberDataElementImpl.create(100));
		seDial.getDial().getScale().setStep(30);
		seDial.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial);

		return dChart;

	}
}
