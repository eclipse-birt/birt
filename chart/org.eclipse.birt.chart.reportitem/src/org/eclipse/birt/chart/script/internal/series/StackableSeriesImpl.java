/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.internal.series;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.script.api.series.IStackableSeries;
import org.eclipse.birt.chart.script.internal.component.ValueSeriesImpl;

/**
 * 
 */

public abstract class StackableSeriesImpl extends ValueSeriesImpl implements IStackableSeries {

	public StackableSeriesImpl(SeriesDefinition sd, Chart cm) {
		super(sd, cm);
	}

	public boolean isStacked() {
		return series.isStacked();
	}

	public void setStacked(boolean stacked) {
		series.setStacked(stacked);
	}

	public void setPercent(boolean percent) {
		getAxis().setPercent(percent);
	}
}
