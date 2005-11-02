/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.examples.api.viewer;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.log.impl.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;

public class Chart3DViewer implements PaintListener, SelectionListener {

	private IDeviceRenderer idr = null;
	private Chart cm = null;	
	private Canvas ca = null;
	private Combo cb = null;

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Chart3DViewer c3dViewer = new Chart3DViewer();
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		Display d = Display.getDefault();
		Shell sh = new Shell(d);
		sh.setSize(900, 700);
		sh.setLayout(gl);
		sh.setText(c3dViewer.getClass().getName() + " [device="//$NON-NLS-1$
				+ c3dViewer.idr.getClass().getName() + "]");//$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_BOTH);
		Canvas cCenter = new Canvas(sh, SWT.NONE);
		cCenter.setLayoutData(gd);
		cCenter.addPaintListener(c3dViewer);

		Composite choicePanel = new Composite(sh, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		choicePanel.setLayoutData(gd);
		choicePanel.setLayout(new RowLayout());

		Label la = new Label(choicePanel, SWT.NONE);
		la.setText("Choose: "); //$NON-NLS-1$

		Combo cbType = new Combo(choicePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbType.add("3D Bar Chart"); //$NON-NLS-1$
		cbType.add("3D Line Chart");//$NON-NLS-1$
		cbType.add("3D Area Chart");//$NON-NLS-1$
		cbType.select(0);

		Button btn = new Button(choicePanel, SWT.NONE);
		btn.setText("Update");//$NON-NLS-1$
		btn.addSelectionListener(c3dViewer);

		c3dViewer.cb = cbType;
		c3dViewer.ca = cCenter;
		
		sh.open();

		while (!sh.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

	/**
	 * Constructor
	 */
	Chart3DViewer() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			idr = ps.getDevice("dv.SWT");//$NON-NLS-1$
		} catch (ChartException pex) {
			DefaultLoggerImpl.instance().log(pex);
		}
		cm = PrimitiveCharts.create3DBarChart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public final void paintControl(PaintEvent pe) {
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc);

		Composite co = (Composite) pe.getSource();
		Rectangle re = co.getClientArea();
		Bounds bo = BoundsImpl.create(re.x, re.y, re.width, re.height);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();
		try {
			gr.render(idr, gr.build(
					idr.getDisplayServer(), cm, null, bo, null));
		} catch (ChartException gex) {
			showException(pe.gc, gex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		int iSelection = cb.getSelectionIndex();
		switch (iSelection) {
		case 0:
			cm = PrimitiveCharts.create3DBarChart();
			break;
		case 1:
			cm = PrimitiveCharts.create3DLineChart();
			break;
		case 2:
			cm = PrimitiveCharts.create3DAreaChart();
			break;
		}
		ca.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
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
		Point d = ca.getSize();

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


