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

package org.eclipse.birt.chart.examples.api.viewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;

public class SwingChartPrinter implements Printable {

	private Chart chart;

	public SwingChartPrinter(Chart chart) {
		this.chart = chart;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Chart chart = PrimitiveCharts.createBarChart();

			PrinterJob job = createPrinter();
			job.setPrintable(new SwingChartPrinter(chart));
			boolean ok = job.printDialog();
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					/* The job did not successfully complete */
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// returns a printerjob
	private static PrinterJob createPrinter() {
		return PrinterJob.getPrinterJob();// this is the default printer
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

		if (pageIndex == 0) {

			Graphics2D g2d = (Graphics2D) graphics;

			try {
				printChart(g2d);
			} catch (ChartException e) {
				e.printStackTrace();
			}
			return Printable.PAGE_EXISTS;
		} else
			return Printable.NO_SUCH_PAGE;

	}

	private void printChart(Graphics2D g2d) throws ChartException {
		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		IDeviceRenderer render = ChartEngine.instance(config).getRenderer("dv.SWING"); //$NON-NLS-1$
		render.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);

		// The input size is in points (1 inch = 72 points)

		Bounds bo = BoundsImpl.create(0, 0, 300, 300);

		Generator generator = Generator.instance();

		GeneratedChartState state = generator.build(render.getDisplayServer(), chart, bo, null, null, null);

		// set render properties
		generator.render(render, state);

	}

}
