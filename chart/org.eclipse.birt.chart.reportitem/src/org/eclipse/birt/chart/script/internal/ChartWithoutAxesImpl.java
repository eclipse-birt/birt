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

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.script.api.IChartWithoutAxes;
import org.eclipse.birt.chart.script.api.component.ICategory;
import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.internal.component.CategoryImpl;
import org.eclipse.birt.chart.script.internal.component.ValueSeriesImpl;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.emf.common.util.EList;

/**
 * 
 */

public class ChartWithoutAxesImpl extends ChartImpl implements IChartWithoutAxes {

	public ChartWithoutAxesImpl(ExtendedItemHandle eih, ChartWithoutAxes cm) {
		super(eih, cm);
	}

	public IValueSeries[] getValueSeries() {
		SeriesDefinition bsd = (SeriesDefinition) getChartWithoutAxes().getSeriesDefinitions().get(0);
		EList osds = bsd.getSeriesDefinitions();
		IValueSeries[] valueSeries = new IValueSeries[osds.size()];
		for (int i = 0; i < valueSeries.length; i++) {
			SeriesDefinition osd = (SeriesDefinition) osds.get(i);
			valueSeries[i] = ValueSeriesImpl.createValueSeries(osd, cm);
		}
		return valueSeries;
	}

	public ICategory getCategory() {
		SeriesDefinition bsd = (SeriesDefinition) getChartWithoutAxes().getSeriesDefinitions().get(0);
		return new CategoryImpl(bsd, cm);
	}

	private ChartWithoutAxes getChartWithoutAxes() {
		return (ChartWithoutAxes) cm;
	}

	public void setDimension(String dimensionName) {
		if (ChartDimension.THREE_DIMENSIONAL_LITERAL.getName().equals(dimensionName)) {
			throw new IllegalArgumentException(Messages.getString("ChartWithoutAxesImpl.exception.3DNotSupported")); //$NON-NLS-1$
		}
		super.setDimension(dimensionName);
	}
}
