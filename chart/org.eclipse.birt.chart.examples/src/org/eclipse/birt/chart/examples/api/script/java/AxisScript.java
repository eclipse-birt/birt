/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public void beforeDrawAxisTitle(Axis axis, Label label, IChartScriptContext icsc) {
		if (axis.getType() == AxisType.TEXT_LITERAL) {
			label.getCaption().getColor().set(140, 198, 62);
		} else {
			label.getCaption().getColor().set(208, 32, 0);
		}
	}
}
