/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public boolean needOptionalGrouping() {
		return false;
	}

	public boolean needCategoryGrouping() {
		return false;
	}

	/*
	 * Returns if group is enabled in each group-level.
	 */
	public boolean[] getGroupStatus() {
		return new boolean[] { true };
	}
}
