/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

/**
 * Provides information about a grouping level within a query or subquery. A
 * group definition contains group break definition (key column etc.) and a set
 * of transforms defined for the group
 */

public interface IGroupDefinition extends IBaseTransform {
	// Enumeration constants for Interval
	/**
	 * No grouping interval unit specified.
	 */
	public static final int NO_INTERVAL = 0;

	/**
	 * Grouping interval unit is Year.
	 */
	public static final int YEAR_INTERVAL = 1;

	/**
	 * Grouping interval unit is Month.
	 */
	public static final int MONTH_INTERVAL = 2;

	/**
	 * Grouping interval unit is Quarter.
	 */
	public static final int QUARTER_INTERVAL = 3;

	/**
	 * Grouping interval unit is Week.
	 */
	public static final int WEEK_INTERVAL = 4;

	/**
	 * Grouping interval unit is Day.
	 */
	public static final int DAY_INTERVAL = 5;

	/**
	 * Grouping interval unit is Hour.
	 */
	public static final int HOUR_INTERVAL = 6;

	/**
	 * Grouping interval unit is Minute.
	 */
	public static final int MINUTE_INTERVAL = 7;

	/**
	 * Grouping interval unit is Second.
	 */
	public static final int SECOND_INTERVAL = 8;

	/**
	 * Grouping interval unit is the numerical value.
	 */
	public static final int NUMERIC_INTERVAL = 99;

	/**
	 * Grouping interval unit is the length of the string prefix.
	 */
	public static final int STRING_PREFIX_INTERVAL = 100;

	// Enumeration constants for SortDirection
	public static final int NO_SORT = -1; // No sort direction is specified.
	public static final int SORT_ASC = ISortDefinition.SORT_ASC; // Sort asending
	public static final int SORT_DESC = ISortDefinition.SORT_DESC; // Sort descending

	/**
	 * Returns the name of the group
	 * 
	 * @return Name of group. Can be null if group is unnamed.
	 */
	public String getName();

	/**
	 * Returns the interval for grouping on a range of contiguous group key values.
	 * Interval can be year, months, day, etc.
	 * 
	 * @return the grouping interval
	 */

	public int getInterval();

	/**
	 * Returns the sort direction on the group key. Use this to specify a sort in
	 * the common case where the groups are ordered by the group key only. To
	 * specify other types of sort criteria, use the Sorts property. SortDirection
	 * is ignored if Sorts is defined for this group.
	 * 
	 * @return The group key sort direction. If no direction is specified,
	 *         <code>NO_SORT</code> is returned. This means that the data engine can
	 *         choose any sort order, or no sort order at all, for this group level.
	 */
	public int getSortDirection();

	/**
	 * Returns the number of contiguous group intervals that form one single group,
	 * when Interval is used to define group break level. For example, if Interval
	 * is <code>MONTH_INTERVAL</code>, and IntervalRange is 6, each group is defined
	 * to contain a span of 6 months.
	 */
	public double getIntervalRange();

	/**
	 * Returns a start value for grouping by range. Returns null if a start value is
	 * not specified.
	 * <p>
	 * A start value defines the boundary of range based grouping. For numeric
	 * values, the default start value is 0. For date range based grouping, the
	 * default start value is Jan. 1, 2000. It has no meaning for grouping based on
	 * string prefix or distinct values
	 */
	public Object getIntervalStart();

	/**
	 * Returns the name of the column that defines the group key. Either the
	 * KeyColumn or KeyExpression can be used to define the group key.
	 */
	public String getKeyColumn();

	/**
	 * Returns the JavaScript expression that defines the group key. <br>
	 * Note: Presently group key must be a column. If an JavaScript expression is
	 * used to specify the group key, the expression must be in the form of
	 * <code>row.column_name</code>, or <code>row["column_name"]</code>.
	 */
	public String getKeyExpression();
}
