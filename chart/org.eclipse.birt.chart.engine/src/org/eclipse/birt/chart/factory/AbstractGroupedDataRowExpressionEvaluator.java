/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.factory;

import java.util.List;

import org.eclipse.birt.chart.internal.datafeed.GroupingLookupHelper;

/**
 * The abstract class is just defined for the grouping integration with BIRT
 * report, Stand-alone mode of chart or chart default grouping mode can't use
 * the class and its implementation.
 * <p>
 * Through the class, it returns appropriate expressions if current has
 * aggregations defined in chart.
 *
 * @since 2.3
 */
public abstract class AbstractGroupedDataRowExpressionEvaluator implements IGroupedDataRowExpressionEvaluator {

	/**
	 * Returns appropriate expressions if current has aggregations defined in chart.
	 *
	 * @param helper
	 * @param isSharingQuery
	 * @return expressions list
	 */
	public List<String> getExpressions(GroupingLookupHelper helper, boolean isSharingQuery) {
		return helper.getExpressions();
	}

	@Override
	public boolean needOptionalGrouping() {
		return false;
	}

	@Override
	public boolean needCategoryGrouping() {
		return false;
	}

	/*
	 * Returns if group is enabled in each group-level.
	 */
	@Override
	public boolean[] getGroupStatus() {
		return new boolean[] { true };
	}
}
