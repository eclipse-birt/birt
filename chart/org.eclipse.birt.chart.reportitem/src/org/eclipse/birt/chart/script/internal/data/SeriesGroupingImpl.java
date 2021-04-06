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

package org.eclipse.birt.chart.script.internal.data;

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.script.api.data.ISeriesGrouping;

/**
 * 
 */

public class SeriesGroupingImpl implements ISeriesGrouping {

	private SeriesGrouping grouping;

	public SeriesGroupingImpl(SeriesGrouping grouping) {
		this.grouping = grouping;
	}

	public double getGroupInterval() {
		return grouping.getGroupingInterval();
	}

	public String getGroupType() {
		return grouping.getGroupType().getName();
	}

	public String getGroupUnit() {
		return grouping.getGroupingUnit().getName();
	}

	public boolean isEnabled() {
		return grouping.isEnabled();
	}

	public void setEnabled(boolean value) {
		grouping.setEnabled(value);
	}

	public void setGroupInterval(double value) {
		grouping.setGroupingInterval(value);
	}

	public void setGroupType(String type) {
		grouping.setGroupType(DataType.getByName(type));
	}

	public void setGroupUnit(String unit) {
		grouping.setGroupingUnit(GroupingUnitType.getByName(unit));
	}

}
