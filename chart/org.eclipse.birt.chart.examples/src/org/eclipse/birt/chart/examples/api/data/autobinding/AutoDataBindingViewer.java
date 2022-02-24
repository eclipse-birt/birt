/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.api.data.autobinding;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.integrate.SimpleDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.util.ULocale;

/**
 * The selector of charts in SWT.
 * 
 */
public final class AutoDataBindingViewer implements PaintListener {
	private IDeviceRenderer idr = null;

	private Chart cm = null;

	private IDataRowExpressionEvaluator dree = null;

	/**
	 * main() method for constructing the layout.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setSize(600, 400);
		shell.setLayout(new GridLayout());

		Canvas cCenter = new Canvas(shell, SWT.NONE);
		AutoDataBindingViewer adbv = new AutoDataBindingViewer();
		cCenter.setLayoutData(new GridData(GridData.FILL_BOTH));
		cCenter.addPaintListener(adbv);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Get the connection with SWT device to render the graphics.
	 */
	AutoDataBindingViewer() {
		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		final PluginSettings ps = PluginSettings.instance(config);
		try {
			idr = ps.getDevice("dv.SWT");//$NON-NLS-1$
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createSimpleChart();
	}

	private static final Chart createSimpleChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getTitle().setVisible(true);

		// X-Series
		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("Items");//$NON-NLS-1$
		seCategory.getDataDefinition().add(query);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		Query query2 = QueryImpl.create("Amounts");//$NON-NLS-1$
		bs.getDataDefinition().add(query2);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
	 * PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, e.gc);
		Composite co = (Composite) e.getSource();
		Rectangle re = co.getClientArea();
		Bounds bo = BoundsImpl.create(0, 0, re.width, re.height);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution());

		RunTimeContext context = new RunTimeContext();
		context.setULocale(ULocale.getDefault());

		String[] set = { "Items", "Amounts" };//$NON-NLS-1$ //$NON-NLS-2$
		Object[][] data = { { "A", "B", "C"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}, { Integer.valueOf(7), Integer.valueOf(2), Integer.valueOf(5) } };
		dree = new SimpleDataRowExpressionEvaluator(set, data);
		Generator gr = Generator.instance();
		try {
			gr.bindData(dree, cm, context);
			gr.render(idr, gr.build(idr.getDisplayServer(), cm, bo, null, context, null));
		} catch (ChartException ce) {
			ce.printStackTrace();
		}
	}

}
