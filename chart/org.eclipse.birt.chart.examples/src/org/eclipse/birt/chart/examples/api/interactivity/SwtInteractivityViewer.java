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

package org.eclipse.birt.chart.examples.api.interactivity;

import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.framework.PlatformConfig;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * The selector of charts in SWT.
 * 
 */
public final class SwtInteractivityViewer extends Composite
		implements PaintListener, ICallBackNotifier, SelectionListener {
	private IDeviceRenderer idr = null;

	private Chart cm = null;

	private static Combo cbType = null;

	private static Button btn = null;

	private GeneratedChartState gcs = null;

	private boolean bNeedsGeneration = true;

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

		SwtInteractivityViewer siv = new SwtInteractivityViewer(shell, SWT.NO_BACKGROUND);
		siv.setLayoutData(new GridData(GridData.FILL_BOTH));
		siv.addPaintListener(siv);

		Composite cBottom = new Composite(shell, SWT.NONE);
		cBottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cBottom.setLayout(new RowLayout());

		Label la = new Label(cBottom, SWT.NONE);

		la.setText("&Choose: ");//$NON-NLS-1$
		cbType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbType.add("Highlight Series");//$NON-NLS-1$
		cbType.add("Show Tooltip");//$NON-NLS-1$
		cbType.add("Toggle Visibility");//$NON-NLS-1$
		cbType.add("URL Redirect");//$NON-NLS-1$
		cbType.add("Multiple URLs Redirect");//$NON-NLS-1$
		cbType.add("Call Back");//$NON-NLS-1$
		cbType.add("Right Mouse Click");//$NON-NLS-1$
		cbType.add("Mouse Cursor");//$NON-NLS-1$
		cbType.select(0);

		btn = new Button(cBottom, SWT.NONE);
		btn.setText("&Update");//$NON-NLS-1$
		btn.setToolTipText("Show");//$NON-NLS-1$
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
	SwtInteractivityViewer(Composite parent, int style) {
		super(parent, style);

		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		final PluginSettings ps = PluginSettings.instance(config);
		try {
			idr = ps.getDevice("dv.SWT");//$NON-NLS-1$
			idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, this);
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
		cm = InteractivityCharts.createHSChart();
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
				cm = InteractivityCharts.createHSChart();
				break;
			case 1:
				cm = InteractivityCharts.createSTChart();
				break;
			case 2:
				cm = InteractivityCharts.createTVChart();
				break;
			case 3:
				cm = InteractivityCharts.createURChart();
				break;
			case 4:
				cm = InteractivityCharts.createMultiURChart();
				break;
			case 5:
				cm = InteractivityCharts.createCBChart();
				break;
			case 6:
				cm = InteractivityCharts.createRCChart();
				break;
			case 7:
				cm = InteractivityCharts.createBarChartWithCursorExample();
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

	public void callback(Object event, Object source, CallBackValue value) {
		MessageBox mb = new MessageBox(this.getShell());
		mb.setMessage(value.getIdentifier());
		mb.open();
	}

}
