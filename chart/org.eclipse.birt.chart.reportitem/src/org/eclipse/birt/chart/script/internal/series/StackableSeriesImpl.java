/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
