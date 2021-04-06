/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.transform.group;

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;

/**
 * To every column which has group property, a GroupBy instance will be
 * generated to do group judgment which supports regarding distinction as
 * standard or not within an interval range as another standard.
 */

public abstract class GroupBy {

	private int columnIndex;
	private String columnName;
	private GroupSpec groupSpec;

	/**
	 * Static method to create and instance of subclass of GroupBy, based on the
	 * group definition
	 * 
	 * @param groupDefn
	 * @param columnIndex
	 * @param columnType
	 * @return GroupBy
	 * @throws DataException
	 */
	public static GroupBy newInstance(GroupSpec groupDefn, int columnIndex, String columnName, Class columnType)
			throws DataException {
		assert groupDefn != null;

		GroupBy groupBy = null;
		if (groupDefn.getInterval() == IGroupDefinition.NO_INTERVAL && Math.round(groupDefn.getIntervalRange()) > 1) {
			groupBy = new GroupByRowKeyCount((int) (Math.round(groupDefn.getIntervalRange())));
		} else {
			groupBy = new GroupByDistinctValue();
		}

		groupBy.groupSpec = groupDefn;
		groupBy.columnIndex = columnIndex;
		groupBy.columnName = columnName;

		return groupBy;
	}

	/**
	 * Determines if the current group key is in the same group as the key value
	 * provided in the last call
	 * 
	 * @param currentGroupKey
	 * @param previousGroupKey
	 * @return boolean
	 */
	public abstract boolean isInSameGroup(Object currentGroupKey, Object previousGroupKey);

	/**
	 * reset for grouping on another list of data
	 */
	public void reset() {

	}

	/**
	 * Gets the index of the column to group by
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	String getColumnName() {
		return columnName;
	}

	/**
	 * Gets the GroupSpec associated with this group by
	 */
	GroupSpec getGroupSpec() {
		return groupSpec;
	}
}