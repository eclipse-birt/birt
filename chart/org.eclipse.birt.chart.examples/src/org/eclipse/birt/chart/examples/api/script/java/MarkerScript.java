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

import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class MarkerScript extends ChartEventHandlerAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerLine(
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerLine,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawMarkerLine(Axis axis, MarkerLine mLine, IChartScriptContext icsc) {
		ULocale.setDefault(ULocale.US);
		if (icsc.getULocale().equals(ULocale.US)) {
			mLine.getLabel().getCaption().getColor().set(165, 184, 55);
			mLine.getLineAttributes().getColor().set(165, 184, 55);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerRange(
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.component.MarkerRange,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawMarkerRange(Axis axis, MarkerRange mRange, IChartScriptContext icsc) {
		mRange.getLabel().getCaption().getColor().set(225, 104, 105);
	}

}
