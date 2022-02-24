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
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
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
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
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
 * SVG: Tooltip of chart didn't show completely
 * </p>
 * Test description:
 * </p>
 * Create a bar chart and add interactivity Show Tooltip with text
 * "abcdef\ndeeeeee" to its chart area
 * </p>
 */

public class Regression_128590_svg extends Composite implements IUpdateNotifier, SelectionListener {

	private static Display display = null;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	private Map contextMap;

	Regression_128590_svg(Composite parent, int style) {
		super(parent, style);
		contextMap = new HashMap();

		PluginSettings.instance().registerDevice("dv.SVG", //$NON-NLS-1$
				"org.eclipse.birt.chart.device.svg.SVGRendererImpl"); //$NON-NLS-1$
		cm = showTooltip_BarChart();

	}

	public static void main(String args[]) {
		display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLocation(display.getClientArea().width / 2 - 110, display.getClientArea().height / 2 - 40);
		shell.setSize(620, 450);
		shell.setLayout(new GridLayout());

		Regression_128590_svg siv = new Regression_128590_svg(shell, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.widthHint = 1;
		gd.heightHint = 1;
		siv.setLayoutData(gd);

		try {
			RunTimeContext rtc = new RunTimeContext();
			rtc.setULocale(ULocale.getDefault());

			IDeviceRenderer idr = null;
			Chart cm = showTooltip_BarChart();

			idr = PluginSettings.instance().getDevice("dv.SVG"); //$NON-NLS-1$
			Generator gr = Generator.instance();
			GeneratedChartState gcs = null;
			Bounds bo = BoundsImpl.create(0, 0, 450, 300);
			gcs = gr.build(idr.getDisplayServer(), cm, bo, null, rtc, null);

			idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, "c:/test.svg"); //$NON-NLS-1$
			idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new EmptyUpdateNotifier(cm, gcs.getChartModel()));

			gr.render(idr, gcs);
		} catch (ChartException ce) {
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

	public static final Chart showTooltip_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");
		cwaBar.getBlock().getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL, ActionImpl
				.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(0, new String("abcdef\ndeeeeee")))));

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$

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
