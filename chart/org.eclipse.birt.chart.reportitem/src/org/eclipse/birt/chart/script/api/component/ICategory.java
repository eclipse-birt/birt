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

package org.eclipse.birt.chart.script.api.component;

import org.eclipse.birt.chart.script.api.data.ISeriesGrouping;
import org.eclipse.birt.chart.script.api.series.data.ISeriesData;

/**
 * Represents the category(X) Series of a Chart in the scripting environment
 */

public interface ICategory {

	/**
	 * Gets query expression in the Category
	 *
	 * @return query expression object
	 */
	ISeriesData getDataExpr();

	/**
	 * Gets grouping related information
	 *
	 * @return series grouping object
	 */
	ISeriesGrouping getGrouping();

	/**
	 * Gets the name of SortOption. Return values are an enumeration including
	 * "Ascending" and "Descending". Default value is "Ascending"
	 *
	 * @return the name of SortOption
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 */
	String getSorting();

	/**
	 * Sets SortOption by name. Sorting names are an enumeration including
	 * "Ascending" and "Descending". Default value is "Ascending". If sorting name
	 * is invalid, will set the default value.
	 *
	 * @param sorting the name of SortOption
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 */
	void setSorting(String sorting);

	/**
	 * Gets the query expression for optional grouping for value series.
	 *
	 * @return query expression
	 */
	String getOptionalValueGroupingExpr();

	/**
	 * Sets the query expression for optional grouping for value series.
	 *
	 * @param expr query expression
	 */
	void setOptionalValueGroupingExpr(String expr);
}
