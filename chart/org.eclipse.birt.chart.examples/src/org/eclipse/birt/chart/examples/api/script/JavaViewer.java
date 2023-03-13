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

package org.eclipse.birt.chart.examples.api.script;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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

public class JavaViewer extends Composite implements PaintListener, SelectionListener {

	private IDeviceRenderer idr = null;

	private Chart cm = null;

	private transient static Label description = null;

	private static Combo cbType = null;

	private static Button btn = null;

	private GeneratedChartState gcs = null;

	private boolean bNeedsGeneration = true;

	private static ILogger logger = Logger.getLogger(JavaScriptViewer.class.getName());

	/**
	 * Execute application
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setSize(600, 400);
		shell.setLayout(new GridLayout());

		JavaViewer jViewer = new JavaViewer(shell, SWT.NO_BACKGROUND);
		jViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
		jViewer.addPaintListener(jViewer);

		Composite cBottom = new Composite(shell, SWT.NONE);
		cBottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cBottom.setLayout(new RowLayout());

		description = new Label(shell, SWT.NONE);
		description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		description.setText("beforeDrawAxisLabel( Axis, Label, IChartScriptContext )" //$NON-NLS-1$
				+ "\nbeforeDrawAxisTitle( Axis, Label, IChartScriptContext )"); //$NON-NLS-1$

		Label la = new Label(cBottom, SWT.NONE);

		la.setText("&Choose: ");//$NON-NLS-1$
		cbType = new Combo(cBottom, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbType.add("Axis");//$NON-NLS-1$
		cbType.add("DataPoints");//$NON-NLS-1$
		cbType.add("Marker");//$NON-NLS-1$
		cbType.add("Series");//$NON-NLS-1$
		cbType.add("Series Title"); //$NON-NLS-1$
		cbType.add("Block");//$NON-NLS-1$
		cbType.add("Legend"); //$NON-NLS-1$
		cbType.select(0);

		btn = new Button(cBottom, SWT.NONE);
		btn.setText("&Update");//$NON-NLS-1$
		btn.setToolTipText("&Update");//$NON-NLS-1$
		btn.addSelectionListener(jViewer);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

	/**
	 * Constructor
	 */
	JavaViewer(Composite parent, int style) {
		super(parent, style);
		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		final PluginSettings ps = PluginSettings.instance(config);
		try {
			idr = ps.getDevice("dv.SWT");//$NON-NLS-1$
		} catch (ChartException pex) {
			logger.log(pex);
		}
		cm = ScriptCharts.createJChart_Axis();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
	 * PaintEvent)
	 */
	@Override
	public final void paintControl(PaintEvent e) {
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
		} catch (ChartException gex) {
			showException(e.gc, gex);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.widget.equals(btn)) {
			int iSelection = cbType.getSelectionIndex();
			switch (iSelection) {
			case 0:
				cm = ScriptCharts.createJChart_Axis();
				description.setText("beforeDrawAxisLabel( Axis, Label, IChartScriptContext )" //$NON-NLS-1$
						+ "\nbeforeDrawAxisTitle( Axis, Label, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 1:
				cm = ScriptCharts.createJChart_DataPoints();
				description.setText("beforeDrawDataPointLabel( DataPointHint, Label, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 2:
				cm = ScriptCharts.createJChart_Marker();
				description.setText("beforeDrawMarkerLine( Axis, MarkerLine, IChartScriptContext )" //$NON-NLS-1$
						+ "\nbeforeDrawMarkerRange( Axis, MarkerRange, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 3:
				cm = ScriptCharts.createJChart_Series();
				description.setText("beforeDrawSeries( Series, ISeriesRenderer, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 4:
				cm = ScriptCharts.createJChart_SeriesTitle();
				description.setText("beforeDrawSeriesTitle( Series, Label, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 5:
				cm = ScriptCharts.createJChart_Block();
				description.setText("beforeDrawBlock( Block, IChartScriptContext )"); //$NON-NLS-1$
				break;
			case 6:
				cm = ScriptCharts.createJChart_Legend();
				description.setText("beforeDrawLegendEntry( Label, IChartScriptContext )"); //$NON-NLS-1$
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
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

	private final void showException(GC g2d, Exception ex) {
		String sWrappedException = ex.getClass().getName();
		Throwable th = ex;
		while (ex.getCause() != null) {
			ex = (Exception) ex.getCause();
		}
		String sException = ex.getClass().getName();
		if (sWrappedException.equals(sException)) {
			sWrappedException = null;
		}

		String sMessage = null;
		if (th instanceof BirtException) {
			sMessage = ((BirtException) th).getLocalizedMessage();
		} else {
			sMessage = ex.getMessage();
		}

		if (sMessage == null) {
			sMessage = "<null>";//$NON-NLS-1$
		}
		StackTraceElement[] stea = ex.getStackTrace();
		Point d = this.getSize();

		Device dv = Display.getCurrent();
		Font fo = new Font(dv, "Courier", SWT.BOLD, 16);//$NON-NLS-1$
		g2d.setFont(fo);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setBackground(dv.getSystemColor(SWT.COLOR_WHITE));
		g2d.fillRectangle(20, 20, d.x - 40, d.y - 40);
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
		g2d.drawRectangle(20, 20, d.x - 40, d.y - 40);
		g2d.setClipping(20, 20, d.x - 40, d.y - 40);
		int x = 25, y = 20 + fm.getHeight();
		g2d.drawString("Exception:", x, y);//$NON-NLS-1$
		x += g2d.textExtent("Exception:").x + 5;//$NON-NLS-1$
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_RED));
		g2d.drawString(sException, x, y);
		x = 25;
		y += fm.getHeight();
		if (sWrappedException != null) {
			g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
			g2d.drawString("Wrapped In:", x, y);//$NON-NLS-1$
			x += g2d.textExtent("Wrapped In:").x + 5;//$NON-NLS-1$
			g2d.setForeground(dv.getSystemColor(SWT.COLOR_RED));
			g2d.drawString(sWrappedException, x, y);
			x = 25;
			y += fm.getHeight();
		}
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
		y += 10;
		g2d.drawString("Message:", x, y);//$NON-NLS-1$
		x += g2d.textExtent("Message:").x + 5;//$NON-NLS-1$
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLUE));
		g2d.drawString(sMessage, x, y);
		x = 25;
		y += fm.getHeight();
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_BLACK));
		y += 10;
		g2d.drawString("Trace:", x, y);//$NON-NLS-1$
		x = 40;
		y += fm.getHeight();
		g2d.setForeground(dv.getSystemColor(SWT.COLOR_DARK_GREEN));
		for (int i = 0; i < stea.length; i++) {
			g2d.drawString(stea[i].getClassName() + ":"//$NON-NLS-1$
					+ stea[i].getMethodName() + "(...):"//$NON-NLS-1$
					+ stea[i].getLineNumber(), x, y);
			x = 40;
			y += fm.getHeight();
		}
		fo.dispose();
	}
}
