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

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

/**
 *
 */

public class AxisScript extends ChartEventHandlerAdapter {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawAxisLabel(org
	 * .eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawAxisLabel(Axis axis, Label label, IChartScriptContext icsc) {
		if (axis.getType() == AxisType.TEXT_LITERAL) {
			label.getCaption().getColor().set(140, 198, 62);
		} else {
			label.getCaption().getColor().set(208, 32, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawAxisTitle(org
	 * .eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc) {
		if (axis.getType() == AxisType.TEXT_LITERAL) {
			label.getCaption().getColor().set(140, 198, 62);
		} else {
			label.getCaption().getColor().set(208, 32, 0);
		}
	}
}
