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

import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
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
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
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

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Interactivity does not work for Area charts
 * </p>
 * Test description:
 * <p>
 * Create an area chart with two series, set interactivity showTooltip to its
 * series
 * </p>
 */

public class Regression_145712_svg extends Composite implements IUpdateNotifier, SelectionListener {

	private static Display display = null;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	private Map contextMap;

	Regression_145712_svg(Composite parent, int style) {
		super(parent, style);
		contextMap = new HashMap();

		PluginSettings.instance().registerDevice("dv.SVG", //$NON-NLS-1$
				"org.eclipse.birt.chart.device.svg.SVGRendererImpl"); //$NON-NLS-1$
		cm = showTooltip_AreaChart();

	}

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

			IDeviceRenderer idr;
			Chart cm = showTooltip_AreaChart();

			idr = PluginSettings.instance().getDevice("dv.SVG"); //$NON-NLS-1$
			Generator gr = Generator.instance();
			GeneratedChartState gcs;
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
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
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
	@Override
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
	@Override
	public Chart getDesignTimeModel() {
		return cm;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#getRunTimeModel()
	 */
	@Override
	public Chart getRunTimeModel() {
		return gcs.getChartModel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#peerInstance()
	 */
	@Override
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
	@Override
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
	@Override
	public void repaintChart() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates a bar chart model as a reference implementation
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_AreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create(new String[] { "Keyboards", "Moritors", "Printers",
				"Mortherboards", "Telephones", "Mouse", "NetCards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create(new double[] { 143.26, 156.55, 95.25, 47.56, 0, 88.9, 93.25 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create(new double[] { 143.26, 0, 95.25, 47.56, 35.8, 0, 123.45 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series-1
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create();
		as1.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as1.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN());
		as1.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as1.getLabel().setVisible(true);
		as1.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as1.setDataSet(dsNumericValues2);

		as1.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		as1.setStacked(true);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY1.getSeries().add(as1);

		// Y-Series-2
		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create();
		as2.setSeriesIdentifier("Microsoft"); //$NON-NLS-1$
		as2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as2.getLabel().setVisible(true);
		as2.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as2.setDataSet(dsNumericValues1);

		as2.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		as2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.RED());
		sdY2.getSeries().add(as2);

		return cwaArea;
	}
}
