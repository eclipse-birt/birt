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

package org.eclipse.birt.chart.examples.api.script.java;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class DataPointsScript extends ChartEventHandlerAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#
	 * beforeDrawDataPointLabel(org.eclipse.birt.chart.computation.DataPointHints,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawDataPointLabel(DataPointHints dph, Label label, IChartScriptContext icsc) {
		double value = ((Double) dph.getOrthogonalValue()).doubleValue();
		if (value < -10.0) {
			label.getCaption().getColor().set(32, 168, 255);
		} else if ((value >= -10.0) & (value <= 10.0)) {
			label.getCaption().getColor().set(168, 0, 208);
		} else if (value > 10.0) {
			label.getCaption().getColor().set(0, 208, 32);
		}
	}

}
