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

import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class LegendScript extends ChartEventHandlerAdapter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawLegendEntry(
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawLegendEntry(Label label, IChartScriptContext icsc) {
		label.getCaption().getColor().set(35, 184, 245);
		label.getCaption().getFont().setBold(true);
		label.getCaption().getFont().setItalic(true);
		label.getOutline().setVisible(true);
		label.getOutline().getColor().set(177, 12, 187);
	}

}
