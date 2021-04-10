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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Regression description:
 * </p>
 * Data sorting of X Axis in chart builder doesn't work.
 * </p>
 * Test description:
 * <p>
 * set data soring of X Axis, render a chart to swt device
 * </p>
 */

public final class Regression_78699_swt extends Composite implements PaintListener, IUpdateNotifier, SelectionListener {

	private IDeviceRenderer idr = null;

	private Chart cm = null;

	private static Combo cbType = null;

	private static Button btn = null;

	private GeneratedChartState gcs = null;

	private boolean bNeedsGeneration = true;

	private Map contextMap;

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

		Regression_78699_swt siv = new Regression_78699_swt(shell, SWT.NO_BACKGROUND);
		siv.setLayoutData(new GridData(GridData.FILL_BOTH));
		siv.addPaintListener(siv);

		Composite cBottom = new Composite(shell, SWT.NONE);
		cBottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cBottom.setLayout(new RowLayout());

		Label la = new Label(cBottom, SWT.NONE);

		la.setText("Choose: ");//$NON-NLS-1$
		cbType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbType.add("Bar Chart");
		cbType.select(0);

		btn = new Button(cBottom, SWT.NONE);
		btn.setText("Update");//$NON-NLS-1$
		btn.addSelectionListener(siv);

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
	Regression_78699_swt(Composite parent, int style) {
		super(parent, style);

		contextMap = new HashMap();

		final PluginSettings ps = PluginSettings.instance();
		try {
			idr = ps.getDevice("dv.SWT");//$NON-NLS-1$
			idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, this);
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = createChart();
		bindGroupingData(cm);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
	 * PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		Rectangle d = this.getClientArea();
		Image imgChart = new Image(this.getDisplay(), d);
		GC gcImage = new GC(imgChart);
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gcImage);

		Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();
		if (bNeedsGeneration) {
			bNeedsGeneration = false;
			try {
				gcs = gr.build(idr.getDisplayServer(), cm, bo, null, null, null);
			} catch (ChartException ce) {
				ce.printStackTrace();
			}
		}

		try {
			gr.render(idr, gcs);
			GC gc = e.gc;
			gc.drawImage(imgChart, d.x, d.y);
		} catch (ChartException ce) {
			ce.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.widget.equals(btn)) {
			int iSelection = cbType.getSelectionIndex();
			switch (iSelection) {
			case 0:
				cm = Title.BarChart();
				break;
			}
			bNeedsGeneration = true;
			this.redraw();
		}
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
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel()
	 */
	public Chart getDesignTimeModel() {
		return cm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
	 */
	public Chart getRunTimeModel() {
		return gcs.getChartModel();
	}

	public Object peerInstance() {
		return this;
	}

	public void regenerateChart() {
		bNeedsGeneration = true;
		redraw();
	}

	public void repaintChart() {
		redraw();
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
	 * @see
	 * org.eclipse.birt.chart.device.IUpdateNotifier#removeContext(java.lang.Object)
	 */
	public Object removeContext(Object key) {
		return contextMap.remove(key);
	}

	private void bindGroupingData(Chart chart)

	{
		// Data Set
		final Object[][] data = new Object[][] { { "x1", new Integer(1), "g1" }, { "x2", new Integer(2), "g2" },
				{ "x3", new Integer(3), "g1" }, { "x4", new Integer(4), "g3" }, { "x5", new Integer(5), "g2" },
				{ "x6", new Integer(6), "g1" }, { "x7", new Integer(7), "g3" }, { "x8", new Integer(8), "g2" },
				{ "x9", new Integer(9), "g2" }, { "x0", new Integer(0), "g2" }, };

		try {
			Generator gr = Generator.instance();
			gr.bindData(new IDataRowExpressionEvaluator() {

				int idx = 0;

				public void close() {
				}

				public Object evaluate(String expression) {
					if ("X".equals(expression)) {
						return data[idx][0];
					} else if ("Y".equals(expression)) {
						return data[idx][1];
					} else if ("G".equals(expression)) {
						return data[idx][2];
					}
					return null;
				}

				public Object evaluateGlobal(String expression) {
					return evaluate(expression);
				}

				public boolean first() {
					idx = 0;
					return true;
				}

				public boolean next() {
					idx++;
					return (idx < 9);
				}
			}, chart, new RunTimeContext());
		} catch (ChartException e) {
			e.printStackTrace();
		}
	}

	private Chart createChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// X-Series
		Series seCategory = SeriesImpl.create();
		Query xQ = QueryImpl.create("G");
		seCategory.getDataDefinition().add(xQ);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// -------------------------------------------------------------

		sdX.setSorting(SortOption.DESCENDING_LITERAL);
		sdX.getGrouping().setEnabled(true);
		sdX.getGrouping().setGroupType(DataType.TEXT_LITERAL);
		sdX.getGrouping().setAggregateExpression("Sum");
		sdX.getGrouping().setGroupingInterval(0);

		// -------------------------------------------------------------

		// Y-Series
		LineSeries bs = (LineSeries) LineSeriesImpl.create();
		bs.getLabel().setVisible(false);
		Query yQ = QueryImpl.create("Y");
		bs.getDataDefinition().add(yQ);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(0);
		sdY.getSeries().add(bs);

		return cwaBar;

	}

}
