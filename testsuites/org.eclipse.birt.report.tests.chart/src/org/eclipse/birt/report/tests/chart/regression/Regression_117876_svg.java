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

import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Regression description:
 * </p>
 * SVG-ToggleVisibility, Meter Chart has problem
 * </p>
 * Test description:
 * <p>
 * Create a meter chart and add interactivity Toggle Visibility to its value
 * series
 * </p>
 */

public class Regression_117876_svg extends Composite implements IUpdateNotifier, SelectionListener {

	private static Display display = null;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	private Map contextMap;

	Regression_117876_svg(Composite parent, int style) {
		super(parent, style);
		contextMap = new HashMap();

		PluginSettings.instance().registerDevice("dv.SVG", //$NON-NLS-1$
				"org.eclipse.birt.chart.device.svg.SVGRendererImpl"); //$NON-NLS-1$
		cm = toggleVisibility_MeterChart();

	}

	public static void main(String args[]) {
		display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLocation(display.getClientArea().width / 2 - 110, display.getClientArea().height / 2 - 40);
		shell.setSize(620, 450);
		shell.setLayout(new GridLayout());

		Regression_117876_svg siv = new Regression_117876_svg(shell, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.widthHint = 1;
		gd.heightHint = 1;
		siv.setLayoutData(gd);

		try {
			RunTimeContext rtc = new RunTimeContext();
			rtc.setULocale(ULocale.getDefault());

			IDeviceRenderer idr = null;
			Chart cm = toggleVisibility_MeterChart();

			idr = PluginSettings.instance().getDevice("dv.SVG"); //$NON-NLS-1$
			Generator gr = Generator.instance();
			GeneratedChartState gcs = null;
			Bounds bo = BoundsImpl.create(0, 0, 450, 300);
			gcs = gr.build(idr.getDisplayServer(), cm, bo, null, rtc, null);

			idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, "c:/test.svg"); //$NON-NLS-1$
			idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new EmptyUpdateNotifier(cm, gcs.getChartModel()));

			gr.render(idr, gcs);
		}

		catch (ChartException ce) {
			ce.printStackTrace();
		}

		Browser br = new Browser(shell, SWT.NONE);
		br.setLayoutData(new GridData(GridData.FILL_BOTH));
		br.setUrl("c:/test.svg");//$NON-NLS-1$
		br.setVisible(true);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#getContext(java.lang.Object)
	 */
	public Object getContext(Object key) {
		return contextMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#getDesignTimeModel()
	 */
	public Chart getDesignTimeModel() {
		return cm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#getRunTimeModel()
	 */
	public Chart getRunTimeModel() {
		return gcs.getChartModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#peerInstance()
	 */
	public Object peerInstance() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#putContext(java.lang.Object,
	 * java.lang.Object)
	 */
	public Object putContext(Object key, Object value) {
		return contextMap.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#regenerateChart()
	 */
	public void regenerateChart() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#removeContext(java.lang.Object)
	 */
	public Object removeContext(Object key) {
		return contextMap.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#repaintChart()
	 */
	public void repaintChart() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart toggleVisibility_MeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = dChart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.CREAM());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(8);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(false);
		lg.setShowValue(true);
		lg.getClientArea().setBackground(ColorDefinitionImpl.PINK());

		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().getCaption().getFont().setSize(20);
		lg.getTitle().setInsets(InsetsImpl.create(10, 10, 10, 10));
		lg.setTitlePosition(Position.ABOVE_LITERAL);
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;

	}
}
