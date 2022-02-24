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

import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.util.ULocale;

public class SvgInteractivityViewer extends Composite implements IUpdateNotifier, SelectionListener {

	private IDeviceRenderer idr = null;

	private static Combo cbType = null;

	private static Button btn = null;

	private static Display display = null;

	private GeneratedChartState gcs = null;

	private Chart cm = null;

	SvgInteractivityViewer(Composite parent, int style) {
		super(parent, style);

		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		PluginSettings.instance(config).registerDevice("dv.SVG", //$NON-NLS-1$
				"org.eclipse.birt.chart.device.svg.SVGRendererImpl"); //$NON-NLS-1$
		cm = InteractivityCharts.createHSChart();

	}

	public static void main(String args[]) {
		display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setSize(220, 80);
		shell.setLocation(display.getClientArea().width / 2 - 110, display.getClientArea().height / 2 - 40);
		shell.setLayout(new GridLayout());

		SvgInteractivityViewer siv = new SvgInteractivityViewer(shell, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.widthHint = 1;
		gd.heightHint = 1;
		siv.setLayoutData(gd);

		Composite cBottom = new Composite(shell, SWT.NONE);
		cBottom.setLayoutData(new GridData(GridData.CENTER));
		cBottom.setLayout(new RowLayout());

		Label la = new Label(cBottom, SWT.NONE);
		la.setText("&Choose: ");//$NON-NLS-1$

		cbType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbType.add("Highlight Series");//$NON-NLS-1$
		cbType.add("Show Tooltip");//$NON-NLS-1$
		cbType.add("Toggle Visibility");//$NON-NLS-1$
		cbType.add("URL Redirect");//$NON-NLS-1$
		cbType.select(0);

		btn = new Button(cBottom, SWT.NONE);
		btn.setText("&Show");//$NON-NLS-1$
		btn.addSelectionListener(siv);
		btn.setToolTipText("Show");//$NON-NLS-1$

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
		if (e.widget == btn) {
			int i = cbType.getSelectionIndex();
			switch (i) {
			case 0:
				cm = InteractivityCharts.createSVGHSChart();
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
			}

			try {

				RunTimeContext rtc = new RunTimeContext();
				rtc.setULocale(ULocale.getDefault());

				idr = PluginSettings.instance().getDevice("dv.SVG"); //$NON-NLS-1$
				Generator gr = Generator.instance();
				Bounds bo = BoundsImpl.create(0, 0, 450, 300);
				gcs = gr.build(idr.getDisplayServer(), cm, bo, null, rtc, null);

				idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, "c:/test.svg"); //$NON-NLS-1$
				idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new EmptyUpdateNotifier(cm, gcs.getChartModel()));

				gr.render(idr, gcs);
			} catch (ChartException ce) {
				ce.printStackTrace();
			}

			Shell shell = new Shell(display);
			shell.setSize(620, 450);
			shell.setLayout(new GridLayout());

			Browser br = new Browser(shell, SWT.NONE);
			br.setLayoutData(new GridData(GridData.FILL_BOTH));
			br.setUrl("c:/test.svg");//$NON-NLS-1$
			br.setVisible(true);

			shell.open();
		}
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
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#regenerateChart()
	 */
	@Override
	public void regenerateChart() {
		// TODO Auto-generated method stub

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
}
