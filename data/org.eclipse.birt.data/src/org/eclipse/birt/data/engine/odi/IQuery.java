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

package org.eclipse.birt.data.engine.odi;

import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;

/**
 * The IQuery interface allows applications to specify data transformation of
 * result data instances obtained via an external data source or cached data.
 * The IDataSource is the factory for all types of IQuery instances. There may
 * be many IQuery instances associated with an IDataSource. Multiple independent
 * queries might be executed simultaneously by different threads, but the
 * implementation might choose to execute them serially. In either case, the
 * implementation must be thread safe.
 * <p>
 * Interface methods are provided to bind data manipulation specification, such
 * as ordering, aggregates/grouping, filtering. <br>
 * In a later release, this query would also accept specification for computed
 * columns to let an ODI Executor, if capable, to compute the value for such
 * column.
 * <p>
 * The execute methods run a prepared data source query with its parameter
 * values, if applicable, and return the result in a result iterator which the
 * user can iterate to get its IResultObject instances. <br>
 * An implementation might choose to represent its result iterator as a cursored
 * result supporting incremental fetch. <br>
 * The result may be a large set, which could be iterated or passed to another
 * query (ICandidateQuery) for further data manipulation.
 */

public interface IQuery {
	/**
	 * Bind the ordering/sorting specification to the query instance. Specify the
	 * ordering of one or more fields in the query result objects.
	 *
	 * @param sortSpecs An ordered list of IQuery.SortSpec objects.
	 * @throws DataException if given sortSpecs is invalid.
	 */
	void setOrdering(List<SortSpec> sortSpecs) throws DataException;

	/**
	 * Specify the grouping of query results for aggregates.
	 *
	 * @param groupSpecs An ordered list of IQuery.GroupSpec objects.
	 * @throws DataException if given groupSpecs is invalid.
	 */
	void setGrouping(List<GroupSpec> groupSpecs) throws DataException;

	/**
	 * Specifies the maximum number of detail rows that can be retrieved by this
	 * query.
	 *
	 * @param maxRows Maximum number of rows. A value of 0 means no limit on how
	 *                many rows this query can retrieve.
	 */
	void setMaxRows(int maxRows);

	/**
	 * Set up the max number of rows that the data set represent by this
	 * IBaseDataSetDesign instance can fetch from data source. If the input number
	 * is non-positive then unlimited number of rows will be fetched.
	 *
	 * @param limit
	 */
	void setRowFetchLimit(int limit);

	/**
	 * Define a custom event object, which is called after the query retrieves a
	 * result object and before any processing is done. Multiple events can be added
	 * using this method. These event objects are called in the order that they are
	 * added.
	 */
	void addOnFetchEvent(IResultObjectEvent event);

	/**
	 * @param exprProcessor
	 */
	void setExprProcessor(IExpressionProcessor exprProcessor);

	/**
	 * @param distinctValueFlag
	 */
	void setDistinctValueFlag(boolean distinctValueFlag);

	/**
	 * Close all result iterators of execute(...) methods on this Query instance,
	 * and any associated resources. After this method, all associated query results
	 * and their iterators can no longer be used. The Query instance itself is still
	 * valid and can still be used for further execution.
	 */
	void close();

	/**
	 * Return the query definition.
	 *
	 * @return
	 */
	IBaseQueryDefinition getQueryDefinition();

	/**
	 *
	 * @param query
	 */
	void setQueryDefinition(IBaseQueryDefinition query);

	/* Nested data transform spec class definitions */

	/**
	 * Defines a sort criterion on an IQuery. It contains the name or alias of a
	 * result field and a sort direction (ascending or descending)
	 */
	public static class SortSpec {
		private int index = -1;
		private String field;
		private boolean ascendingOrder;
		@SuppressWarnings("rawtypes")
		private Comparator comparator;

		@SuppressWarnings("rawtypes")
		public SortSpec(int index, String field, boolean ascendingOrder, Comparator comparator) {
			this.index = index;
			this.field = field;
			this.ascendingOrder = ascendingOrder;
			this.comparator = comparator;
		}

		public int getIndex() {
			return index;
		}

		public String getField() {
			return field;
		}

		public boolean isAscendingOrder() {
			return ascendingOrder;
		}

		@SuppressWarnings("rawtypes")
		public Comparator getComparator() {
			return this.comparator;
		}
	}

	/**
	 * Defines a grouping criterion on an IQuery.
	 */
	public static class GroupSpec {
		private String keyColumn;
		private int keyIndex;
		private String name;
		private int sortDirection = IGroupDefinition.SORT_ASC;
		private int interval = IGroupDefinition.NO_INTERVAL;
		private boolean isComplexExpression = false;
		private double intervalRange = 0;
		private Object intervalStart;
		private int dataType;
		private List<IFilterDefinition> filters;
		private List<ISortDefinition> sorts;

		/**
		 * Instantiates a groupSpec defining a column name as its required group key.
		 *
		 * @param groupKeyColumn The column name as the group key.
		 */
		public GroupSpec(String groupKeyColumn) {
			keyIndex = -1;
			keyColumn = groupKeyColumn;
		}

		/**
		 * Instantiates a groupSpec defining a column name as its required group key.
		 *
		 * @param groupKeyColumn The column name as the group key.
		 */
		public GroupSpec(int groupKeyIndex, String groupKeyColumn) {
			keyIndex = groupKeyIndex;
			keyColumn = groupKeyColumn;
		}

		/**
		 * Gets the index of the column that defines the group key.
		 *
		 * @return The column name of the group key.
		 */
		public int getKeyIndex() {
			return keyIndex;
		}

		/**
		 * Gets the name of the column that defines the group key.
		 *
		 * @return The column name of the group key.
		 */
		public String getKeyColumn() {
			return keyColumn;
		}

		/**
		 * Specifies the group name. A name is optional, i.e. a group could be unnamed.
		 *
		 * @param groupName The name of the group.
		 */
		public void setName(String groupName) {
			name = groupName;
		}

		/**
		 * Returns the name of the group.
		 *
		 * @return Name of group. Can be null if group is unnamed.
		 */
		public String getName() {
			return name;
		}

		public void setDataType(int type) {
			this.dataType = type;
		}

		public int getDataType() {
			return this.dataType;
		}

		/**
		 * Specifies the sort direction on the group key. Use this method to specify a
		 * sort in the common case where the groups are ordered by the group key only.
		 * To specify other types of sort criteria, use the query's setOrdering method
		 * to apply directly on the query.
		 *
		 * @param groupSortDirection The group key sortDirection to set. Valid values
		 *                           are those defined as the SortDirection enumeration
		 *                           constants in birt.data.engine.api.IGroupDefn.
		 */
		public void setSortDirection(int groupSortDirection) {
			sortDirection = groupSortDirection;
		}

		/**
		 * Gets the sort direction on the group key.
		 *
		 * @return The group key sort direction. If no direction is specified,
		 *         IGroupDefn.NO_SORT is returned. This means the data engine can choose
		 *         any sort order, or no sort order at all, for this group level.
		 */
		public int getSortDirection() {
			return sortDirection;
		}

		/**
		 * Specifies the interval for grouping on a range of contiguous group key
		 * values. Interval can be year, months, day, etc.
		 *
		 * @param interval The interval to set, as an integer value defined in
		 *                 birt.data.engine.api.IGroupDefn.
		 */
		public void setInterval(int groupInterval) {
			interval = groupInterval;
		}

		/**
		 * Returns the interval for grouping on a range of contiguous group key values.
		 *
		 * @return The grouping interval
		 */
		public int getInterval() {
			return interval;
		}

		/**
		 * Specifies the number of contiguous group intervals that form one single
		 * group, when Interval is used to define group break level. <br>
		 * For example, if Interval is MONTH_INTERVAL, and IntervalRange is 6, each
		 * group is defined to contain a span of 6 months.
		 *
		 * @param intervalRange The intervalRange to set.
		 */
		public void setIntervalRange(double groupIntervalRange) {
			intervalRange = groupIntervalRange;
		}

		/**
		 * Returns the number of contiguous group intervals that form one single group,
		 * when Interval is used to define group break level.
		 *
		 * @return The grouping intervalRange.
		 */
		public double getIntervalRange() {
			return intervalRange;
		}

		/**
		 * Gets the start value of a group range
		 */
		public Object getIntervalStart() {
			return intervalStart;
		}

		/**
		 * Sets the start value of a group range
		 */
		public void setIntervalStart(Object intervalStart) {
			this.intervalStart = intervalStart;
		}

		public void setIsComplexExpression(boolean isComplexExpr) {
			this.isComplexExpression = isComplexExpr;
		}

		public boolean isCompleteExpression() {
			return this.isComplexExpression;
		}

		public void setSorts(List<ISortDefinition> sorts) {
			if (sorts != null) {
				this.sorts = sorts;
			}
		}

		public List<ISortDefinition> getSorts() {
			return this.sorts;
		}

		public void setFilters(List<IFilterDefinition> filters) {
			if (filters != null) {
				this.filters = FilterUtil.sortFilters(filters);
			}
		}

		public List<IFilterDefinition> getFilters() {
			return this.filters;
		}

	}

}
