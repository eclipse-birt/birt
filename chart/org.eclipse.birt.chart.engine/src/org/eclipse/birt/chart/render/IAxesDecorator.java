/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.exception.ChartException;

/**
 * This interface defines a decorator renderer for Axes
 */
public interface IAxesDecorator {

	/**
	 * Returns the thickness for use with decoration.
	 *
	 * @param xs The display server.
	 * @param ax The runtime axis model.
	 * @return
	 */
	double[] computeDecorationThickness(IDisplayServer xs, OneAxis ax) throws ChartException;

	/**
	 * Perform decoration.
	 *
	 * @param ipr  The renderer instance.
	 * @param isrh Series rendering hints.
	 * @param ax   The runtime axis model.
	 */
	void decorateAxes(IPrimitiveRenderer ipr, ISeriesRenderingHints isrh, OneAxis ax) throws ChartException;
}
