/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
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
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
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
 * SVG-Highlight, pie chart can not take effect
 * </p>
 * Test description:
 * <p>
 * Create a pie chart and add interactivity Highlight to its legend series
 * </p>
 */

public class Regression_117865_svg extends Composite implements IUpdateNotifier, SelectionListener {

	private static Display display = null;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	private Map contextMap;

	public static void main(String args[]) {
		display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLocation(display.getClientArea().width / 2 - 110, display.getClientArea().height / 2 - 40);
		shell.setSize(620, 450);
		shell.setLayout(new GridLayout());

		Regression_117865_svg siv = new Regression_117865_svg(shell, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.widthHint = 1;
		gd.heightHint = 1;
		siv.setLayoutData(gd);

		try {
			RunTimeContext rtc = new RunTimeContext();
			rtc.setULocale(ULocale.getDefault());

			IDeviceRenderer idr = null;
			Chart cm = toggleVisibility_PieChart();

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

	Regression_117865_svg(Composite parent, int style) {
		super(parent, style);
		contextMap = new HashMap();

		PluginSettings.instance().registerDevice("dv.SVG", //$NON-NLS-1$
				"org.eclipse.birt.chart.device.svg.SVGRendererImpl"); //$NON-NLS-1$
		cm = toggleVisibility_PieChart();

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

	public static final Chart toggleVisibility_PieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Chart Type
		cwoaPie.setType("Pie Chart");

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Sample Pie Chart");
		cwoaPie.getBlock().setBounds(BoundsImpl.create(0, 0, 252, 288));
		cwoaPie.getBlock().getOutline().setVisible(true);

		// Plot
		cwoaPie.getPlot().getClientArea().getOutline().setVisible(false);
		cwoaPie.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);

		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

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
		ps.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ps.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ps.getLabel().setVisible(true);
		ps.setSeriesIdentifier("Actuate");
		ps.setDataSet(dsNumericValues1);
		ps.setLeaderLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DASH_DOTTED_LITERAL, 3));
		ps.setLeaderLineStyle(LeaderLineStyle.FIXED_LENGTH_LITERAL);
		ps.setExplosion(0);
		ps.setSliceOutline(ColorDefinitionImpl.BLACK());

		SeriesDefinition seGroup1 = SeriesDefinitionImpl.create();
		series.getSeriesPalette().update(-2);
		series.getSeriesDefinitions().add(seGroup1);
		seGroup1.getSeries().add(ps);

		return cwoaPie;

	}
}
