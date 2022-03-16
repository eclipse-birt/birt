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

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.IGenerator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;

public class SwtChartPrinter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Chart chart = PrimitiveCharts.createBarChart();

			Printer printer = createPrinter();
			printChart(chart, printer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Prints the chart on the default printer

	private static void printChart(Chart chart, Printer printer) throws ChartException {
		// create graphics context for printer
		GC gc = new GC(printer);

		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		IDeviceRenderer render = ChartEngine.instance(config).getRenderer("dv.SWT"); //$NON-NLS-1$

		render.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);

		// The input size is in points (1 inch = 72 points)

		Bounds bo = BoundsImpl.create(0, 0, 300, 300);

		// builds and computes preferred sizes of various chart components
		IGenerator generator = ChartEngine.instance().getGenerator();
		GeneratedChartState state = generator.build(render.getDisplayServer(), chart, bo, null, null, null);

		printer.startJob("BIRT Sample Chart");//$NON-NLS-1$
		printer.startPage();

		// set render properties
		generator.render(render, state);

		printer.endPage();
		printer.endJob();
	}

	private static Printer createPrinter() {
		return new Printer();// this is the default printer
	}

}
