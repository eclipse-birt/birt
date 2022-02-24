/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.Iterator;

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;

/**
 * Definition of a group
 */
public class GroupAdapter extends GroupDefinition {
	/**
	 * Constructs a group based on Model group definition
	 * 
	 * @throws AdapterException
	 */
	public GroupAdapter(IModelAdapter adapter, GroupHandle modelGroup) throws AdapterException {
		super(modelGroup.getName());
		this.setKeyExpression(adapter.adaptExpression(
				DataAdapterUtil.getExpression(modelGroup.getExpressionProperty(IGroupElementModel.KEY_EXPR_PROP))));
		this.setInterval(intervalFromModel(modelGroup.getInterval()));
		this.setIntervalRange(modelGroup.getIntervalRange());
		this.setIntervalStart(modelGroup.getIntervalBase());

		// sort direction
		String direction = modelGroup.getSortDirection();
		if (direction != null) {
			setSortDirection(SortAdapter.sortDirectionFromModel(direction));
		}

		// Add sorts
		Iterator sortsIt = modelGroup.sortsIterator();
		if (sortsIt != null) {
			while (sortsIt.hasNext()) {
				SortKeyHandle keyHandle = (SortKeyHandle) sortsIt.next();
				this.addSort(adapter.adaptSort(keyHandle));
			}
		}

		// Add filters
		Iterator filtersIt = modelGroup.filtersIterator();
		if (filtersIt != null) {
			while (filtersIt.hasNext()) {
				FilterConditionHandle filterHandle = (FilterConditionHandle) filtersIt.next();
				this.addFilter(adapter.adaptFilter(filterHandle));
			}
		}
	}

	/**
	 * Converts a Model interval string to equivalent DtE constant
	 */
	public static int intervalFromModel(String interval) {
		if (DesignChoiceConstants.INTERVAL_YEAR.equals(interval)) {
			return IGroupDefinition.YEAR_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_MONTH.equals(interval)) {
			return IGroupDefinition.MONTH_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_WEEK.equals(interval)) //
		{
			return IGroupDefinition.WEEK_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_QUARTER.equals(interval)) {
			return IGroupDefinition.QUARTER_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_DAY.equals(interval)) {
			return IGroupDefinition.DAY_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_HOUR.equals(interval)) {
			return IGroupDefinition.HOUR_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_MINUTE.equals(interval)) {
			return IGroupDefinition.MINUTE_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_PREFIX.equals(interval)) {
			return IGroupDefinition.STRING_PREFIX_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_SECOND.equals(interval)) {
			return IGroupDefinition.SECOND_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_INTERVAL.equals(interval)) {
			return IGroupDefinition.NUMERIC_INTERVAL;
		}
		return IGroupDefinition.NO_INTERVAL;
	}
}
