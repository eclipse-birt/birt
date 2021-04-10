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

package org.eclipse.birt.chart.script.internal.component;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.script.api.series.data.ISeriesData;
import org.eclipse.birt.chart.script.internal.series.data.SimpleDataImpl;

/**
 * 
 */

public abstract class SeriesImpl {

	protected SeriesDefinition sd;
	protected Series series;
	protected Chart cm;

	protected SeriesImpl(SeriesDefinition sd, Chart cm) {
		this.sd = sd;
		this.series = sd.getDesignTimeSeries();
		this.cm = cm;
	}

	public ISeriesData getDataExpr() {
		return new SimpleDataImpl(sd);
	}
}
