/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.transform.group;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.JSRowObject;

/**
 * Provide two utility functions 1: check whether specified colum name is row id
 * name 2: inteval value determination function.
 */
public final class GroupUtil {

	final static int rowidIndex = 0;
	final static String rowidName = JSRowObject.ROW_POSITION;

	/**
	 * No instance
	 */
	private GroupUtil() {
	}

	/**
	 * Determins whether input colum name is row id name
	 *
	 * @param columnName
	 * @return true, it is rowid column name
	 */
	public static boolean isRowIdColumn(int columnIndex, String columnName) {
		return rowidIndex == columnIndex || columnName.matches("\\Q_{$TEMP_GROUP_\\E.*\\QROWID$}_\\E");
	}

	/**
	 * Determin whether two values are in speficied interval. Please notice, such a
	 * calculation approach only applies to asc order. If desc order needs to be
	 * used too, the intervalValue must be conversed in the start.
	 *
	 * @param startValue
	 * @param intervalValue
	 * @param currValue
	 * @param prevValue
	 * @return true in interval
	 */
	static boolean isWithinInterval(double startValue, double intervalValue, double currValue, double prevValue) {
		assert intervalValue != 0;

		boolean isSame = false;
		double curr = (currValue - startValue) / intervalValue;
		double prev = (prevValue - startValue) / intervalValue;

		// When there is a start value, all the
		// values that less than that start value would enter
		// one group
		if (curr < 0 && prev < 0) {
			return true;
		}

		if (curr < 0 || prev < 0) {
			return false;
		}

		int currDiv = (int) (curr);
		int prevDiv = (int) (prev);
		if (currDiv == prevDiv) {
			isSame = true;
		}
		return isSame;
	}

	/**
	 * Get row index value of specified groupLevel and groupIndex
	 *
	 * @param groupLevel
	 * @param groupIndex
	 * @return rowIndex
	 * @throws DataException
	 */
	public static int getGroupFirstRowIndex(int groupLevel, int groupIndex, List[] groups, int count) {
		int rowIndex;

		if (groupIndex < groups[groupLevel - 1].size()) {
			GroupInfo groupInfo;
			for (int i = groupLevel - 1; i < groups.length; i++) {
				groupInfo = findGroup(i, groupIndex, groups);
				groupIndex = groupInfo.firstChild;
			}
			rowIndex = groupIndex;
		} else {
			rowIndex = count;
		}

		return rowIndex;
	}

	/**
	 * Helper function to find information about a group, given the group level and
	 * the group index at that level. Returns null if groupIndex exceeds max group
	 * index
	 *
	 * @param groupLevel
	 * @param groupIndex
	 * @param groups
	 * @return
	 */
	static GroupInfo findGroup(int groupLevel, int groupIndex, List[] groups) {
		if (groupIndex >= groups[groupLevel].size()) {
			return null;
		} else {
			return (GroupInfo) groups[groupLevel].get(groupIndex);
		}
	}
}
