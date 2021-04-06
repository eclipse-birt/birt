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

import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.script.api.component.ICategory;
import org.eclipse.birt.chart.script.api.data.ISeriesGrouping;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;
import org.eclipse.birt.chart.script.internal.data.SeriesGroupingImpl;

/**
 * 
 */

public class CategoryImpl extends SeriesImpl implements ICategory {

	public CategoryImpl(SeriesDefinition sd, Chart cm) {
		super(sd, cm);
	}

	public ISeriesGrouping getGrouping() {
		return new SeriesGroupingImpl(sd.getGrouping());
	}

	public String getSorting() {
		return sd.getSorting().getName();
	}

	public void setSorting(String sorting) {
		sd.setSorting(SortOption.getByName(sorting));
	}

	public String getOptionalValueGroupingExpr() {
		// Bugzilla#188268 Get the option value grouping from the first value
		// series
		List sds = ChartComponentUtil.getOrthogonalSeriesDefinitions(cm, 0);
		SeriesDefinition sdValue0 = (SeriesDefinition) sds.get(0);
		return sdValue0.getQuery().getDefinition();
	}

	public void setOptionalValueGroupingExpr(String expr) {
		Query query = sd.getQuery();
		if (query == null) {
			query = QueryImpl.create(expr);
			sd.setQuery(query);
			query.eAdapters().addAll(sd.eAdapters());
		} else {
			query.setDefinition(expr);
		}
		// Update grouping query to all value series
		updateOptionGrouping(expr);
	}

	private void updateOptionGrouping(String expr) {
		List seriesList = ChartComponentUtil.getOrthogonalSeriesDefinitions(cm, -1);

		// Copy query to all value series
		for (int i = 0; i < seriesList.size(); i++) {
			SeriesDefinition sd = (SeriesDefinition) seriesList.get(i);
			if (sd.getQuery() != null) {
				sd.getQuery().setDefinition(expr);
			} else {
				Query query = QueryImpl.create(expr);
				query.eAdapters().addAll(sd.eAdapters());
				sd.setQuery(query);
			}
		}
	}
}
