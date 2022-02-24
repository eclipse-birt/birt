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
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class SeriesTitleScript extends ChartEventHandlerAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawSeriesTitle(
	 * org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.component.Label,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawSeriesTitle(Series series, Label label, IChartScriptContext icsc) {
		label.setVisible(true);
		label.getCaption().setValue("Cities"); //$NON-NLS-1$
		label.getCaption().getColor().set(222, 32, 182);
		series.getLabel().getCaption().getColor().set(12, 232, 182);
	}

}
