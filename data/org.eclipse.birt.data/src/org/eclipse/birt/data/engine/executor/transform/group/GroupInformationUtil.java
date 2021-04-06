/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.transform.group;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.cache.CachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.OrderingInfo;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * The instance of this class is used by CachedResultSet to deal with
 * group-related data reading operations.
 */
public class GroupInformationUtil {

	/*
	 * groups[level] is an ArrayList of GroupInfo objects at the specified level.
	 * Level is a 0-based group index, with 0 denoting the outermost group, etc.
	 * Example: Row GroupKey1 GroupKey2 GroupKey3 Column4 Column5 0: CHINA BEIJING
	 * 2003 Cola $100 1: CHINA BEIJING 2003 Pizza $320 2: CHINA BEIJING 2004 Cola
	 * $402 3: CHINA SHANGHAI 2003 Cola $553 4: CHINA SHANGHAI 2003 Pizza $223 5:
	 * CHINA SHANGHAI 2004 Cola $226 6: USA CHICAGO 2004 Pizza $133 7: USA NEW YORK
	 * 2004 Cola $339 8: USA NEW YORK 2004 Cola $297
	 * 
	 * groups: (parent, child) LEVEL 0 LEVEL 1 LEVEL 2
	 * ============================================ 0: -,0 0,0 0,0 1: -,2 0,2 0,2 2:
	 * 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
	 */

	private List<GroupInfo>[] groups;
	// index of the current innermost group
	private int leafGroupIdx = -1;

	private GroupCalculationUtil groupCalculationUtil;

	private String tempDir;
	private DataEngineSession session;

	/**
	 * 
	 * @param groupCalculationUtil
	 */
	GroupInformationUtil(GroupCalculationUtil groupCalculationUtil, DataEngineSession session) {
		this.groupCalculationUtil = groupCalculationUtil;
		this.tempDir = this.groupCalculationUtil.getResultSetPopoulator().getSession().getTempDir();
		this.groups = new List[0];
		this.session = session;
	}

	/**
	 * @return An array of <code>GroupInfo</code> ordered by level.
	 *         <p>
	 *         Each level has a list of <code>GroupInfo</code>.
	 */
	public List<GroupInfo>[] getGroups() {
		return groups;
	}

	/**
	 * Set <code>GroupInfo</code>
	 * <p>
	 * Used when the updated <code>GroupInfo</code> should be set to this utility.
	 * 
	 * @param groups An array of <code>GroupInfo</code> ordered by level.
	 */
	public void setGroups(List<GroupInfo>[] groups) {
		for (List<GroupInfo> grp : this.groups)
			grp.clear();
		this.groups = groups;
		leafGroupIdx = 0;
	}

	/**
	 * 
	 * @param index
	 */
	public void setLeaveGroupIndex(int index) {
		this.leafGroupIdx = index;
	}

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the last row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * last row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return The 1-based index of the outermost group in which the current row is
	 *         the last row; (N+1) if the current row is not at the end of any
	 *         group;
	 */
	public int getEndingGroupLevel() throws DataException {
		checkHasCurrentRow();

		// Always return 0 for last row (which ends group 0 - the entire list)
		if (this.groupCalculationUtil.getResultSetCache()
				.getCurrentIndex() == this.groupCalculationUtil.getResultSetCache().getCount() - 1)
			return 0;

		// 1 is returned if no groups are defined
		if (this.groups.length == 0)
			return 1;

		// Find outermost group that current row ends
		int childGroupIdx = this.groupCalculationUtil.getResultSetCache().getCurrentIndex();
		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = this.groups.length - 1; level >= 0; level--) {
			// Current row is known to end child group with index childGroupIdx
			// Does it also end this group?
			GroupInfo nextGroup = GroupUtil.findGroup(level, currentGroupIdx + 1, groups);
			if (nextGroup != null && childGroupIdx == nextGroup.firstChild - 1) {
				// Yes it also ends this group; check if it ends parent as well
				childGroupIdx = currentGroupIdx;
				currentGroupIdx = GroupUtil.findGroup(level, currentGroupIdx, groups).parent;
				continue;
			}
			break;
		}

		// current row ends group (level +1 ). Note that the group index we
		// return is 1-based
		return level + 2;
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void checkStarted() throws DataException {
		if (this.groupCalculationUtil.getResultSetCache() == null)
			throw new DataException(ResourceConstants.NO_CURRENT_ROW);
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void checkHasCurrentRow() throws DataException {
		checkStarted();
		if (this.groupCalculationUtil.getResultSetCache().getCurrentResult() == null)
			throw new DataException(ResourceConstants.NO_CURRENT_ROW);
	}

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the first row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * first row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return The 1-based index of the outermost group in which the current row is
	 *         the first row; (N+1) if the current row is not at the start of any
	 *         group;
	 */
	public int getStartingGroupLevel() throws DataException {
		checkHasCurrentRow();

		// Always return 0 for first row, which starts group 0 - the entire list
		if (this.groupCalculationUtil.getResultSetCache().getCurrentIndex() == 0)
			return 0;

		// If no groups defined, return 1
		if (this.groups.length == 0)
			return 1;

		// Find outermost group that current row starts
		int childGroupIdx = this.groupCalculationUtil.getResultSetCache().getCurrentIndex();
		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = this.groups.length - 1; level >= 0; level--) {
			// Current row is known to start child group with index
			// childGroupIdx
			// Does it also start this group?
			GroupInfo currentGroup = GroupUtil.findGroup(level, currentGroupIdx, this.groups);
			if (childGroupIdx == currentGroup.firstChild) {
				// Yes it also starts this group; check if it starts parent as
				// well
				childGroupIdx = currentGroupIdx;
				currentGroupIdx = currentGroup.parent;
				continue;
			}
			break;
		}
		// current row starts group (level +1 ). Note that the group index we
		// return is 1-based
		return level + 2;
	}

	/**
	 * Finds index of current group at the specified group level
	 */
	private int findCurrentGroup(int groupLevel) {
		// Walk up the group chain from leaf group
		int currentGroupIdx = leafGroupIdx;
		for (int i = this.groups.length - 1; i > groupLevel; i--)
			currentGroupIdx = GroupUtil.findGroup(i, currentGroupIdx, this.groups).parent;
		return currentGroupIdx;
	}

	/**
	 * Rewinds row cursor to the first row at the specified group level
	 * 
	 * @param groupLevel the specified group level that will be skipped, 1 indicate
	 *                   the highest level. 0 indicates whole list.
	 */
	public void first(int groupLevel) throws DataException {
		if (groupLevel > this.groups.length || groupLevel < 0)
			throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));

		if (groupLevel == 0) {
			// Special case: move to first row in entire list
			leafGroupIdx = 0;
			this.groupCalculationUtil.getResultSetCache().reset();
			this.groupCalculationUtil.getResultSetCache().next();
			return;
		}
		groupLevel--; // change to 0-based index

		// First find current group at the specified group level
		int currentGroupIdx = findCurrentGroup(groupLevel);

		// Find first child group at each level
		for (int i = groupLevel + 1; i < this.groups.length; i++) {
			currentGroupIdx = GroupUtil.findGroup(i - 1, currentGroupIdx, this.groups).firstChild;
		}

		leafGroupIdx = currentGroupIdx;
		int currentRowID = GroupUtil.findGroup(this.groups.length - 1, leafGroupIdx, this.groups).firstChild;
		this.groupCalculationUtil.getResultSetCache().moveTo(currentRowID);
	}

	/**
	 * Advances row cursor to the last row at the specified group level
	 * 
	 * @param groupLevel the specified group level that will be skipped, 1 indicate
	 *                   the highest level. 0 indicates whole list.
	 */
	public void last(int groupLevel) throws DataException {
		if (groupLevel > this.groups.length || groupLevel < 0)
			throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));

		groupLevel--; // change to 0-based index

		// First find current group at the specified group level
		int currentGroupIdx = -1;
		if (groupLevel >= 0)
			currentGroupIdx = findCurrentGroup(groupLevel);

		if (groupLevel < 0 || // an input of 0 means moving to last row in
		// list
				currentGroupIdx >= this.groups[groupLevel].size() - 1) {
			// Move to last row in entire list
			// Last row is in the last leaf group
			int currentRowID = this.groupCalculationUtil.getResultSetCache().getCount() - 1;
			this.groupCalculationUtil.getResultSetCache().moveTo(currentRowID);
			if (this.groups.length > 0)
				leafGroupIdx = this.groups[this.groups.length - 1].size() - 1;
			return;
		}

		// Find first row in the next group
		++currentGroupIdx;

		for (int i = groupLevel + 1; i < this.groups.length; i++) {
			currentGroupIdx = GroupUtil.findGroup(i - 1, currentGroupIdx, this.groups).firstChild;
		}

		// Move back one row and one leaf group
		int currentRowID = GroupUtil.findGroup(this.groups.length - 1, currentGroupIdx, this.groups).firstChild - 1;
		this.groupCalculationUtil.getResultSetCache().moveTo(currentRowID);
		leafGroupIdx = currentGroupIdx - 1;
	}

	/**
	 * Gets the index of the current group at the specified group level. The index
	 * starts at 0
	 */
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		if (groupLevel == 0)
			return 0;

		checkHasCurrentRow();
		if (groupLevel < 0 || groupLevel > this.groups.length)
			throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));

		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = this.groups.length - 1; level > groupLevel - 1; level--) {
			GroupInfo currentGroup = GroupUtil.findGroup(level, currentGroupIdx, this.groups);
			currentGroupIdx = currentGroup.parent;
		}
		return currentGroupIdx;
	}

	/**
	 * When the smartCache is proceed (IResultIterator.next() is called), the
	 * leafGroupIdx should be re-calculated.
	 * 
	 * @param hasNext
	 * @throws DataException
	 */
	public void next(boolean hasNext) throws DataException {
		// Adjust leaf group index
		// Have we advanced into the next leaf group?
		if (hasNext == true && this.groups.length > 0) {
			GroupInfo nextLeafGroup = GroupUtil.findGroup(this.groups.length - 1, leafGroupIdx + 1, this.groups);
			if (nextLeafGroup != null
					&& this.groupCalculationUtil.getResultSetCache().getCurrentIndex() >= nextLeafGroup.firstChild) {
				// Move to next leaft group
				++leafGroupIdx;
			}
		}
	}

	/**
	 * For a particual group level, it might consists of several group units. For
	 * each group unit, it has its start row index and end row index, and then the
	 * total index will be the group unit number*2.
	 * 
	 * @param groupLevel
	 * @return int[]
	 * @throws DataException
	 */
	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		if (groupLevel == 0) {
			return new int[] { 0, this.groupCalculationUtil.getResultSetCache().getCount() };
		}

		int unitCountInOneGroup = this.groups[groupLevel - 1].size();
		if (unitCountInOneGroup == 1) {
			return new int[] { 0, this.groupCalculationUtil.getResultSetCache().getCount() };
		} else {
			int[] unitInfo = new int[unitCountInOneGroup * 2];
			for (int i = 0; i < unitCountInOneGroup; i++) {
				int startIndex = i;
				int endIndex = startIndex + 1;

				startIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, startIndex, this.groups,
						this.groupCalculationUtil.getResultSetCache().getCount());
				endIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, endIndex, this.groups,
						this.groupCalculationUtil.getResultSetCache().getCount());

				unitInfo[i * 2] = startIndex;
				unitInfo[i * 2 + 1] = endIndex;
			}
			return unitInfo;
		}
	}

	/**
	 * Do grouping, and fill group indexes
	 * 
	 * @param stopsign
	 * @throws DataException
	 */
	public void doGrouping() throws DataException {
		assert this.groupCalculationUtil.getResultSetCache() != null;
		// Pass through sorted data set to process group indexes
		groups = new CachedList[this.groupCalculationUtil.getGroupDefn().length];

		if (groups.length == 0)
			return;

		for (int i = 0; i < this.groupCalculationUtil.getGroupDefn().length; i++) {
			groups[i] = new CachedList(tempDir, DataEngineSession.getCurrentClassLoader(), GroupInfo.getCreator());
		}

		IResultObject prevRow = null;
		this.groupCalculationUtil.getResultSetCache().reset();
		// reset groupBys for grouping
		for (int i = 0; i < groupCalculationUtil.getGroupDefn().length; i++) {
			groupCalculationUtil.getGroupDefn()[i].reset();
		}
		for (int rowID = 0; rowID < this.groupCalculationUtil.getResultSetCache().getCount(); rowID++) {
			if (session.getStopSign().isStopped())
				break;
			IResultObject currRow = this.groupCalculationUtil.getResultSetCache().fetch();

			// breakLevel is the outermost group number to differentiate row
			// data
			int breakLevel;
			if (rowID == 0)
				breakLevel = 0; // Special case for first row
			else
				breakLevel = getBreakLevel(currRow, prevRow);

			// Create a new group in each group level between
			// [ breakLevel ... groupDefs.length - 1]
			for (int level = breakLevel; level < groups.length; level++) {
				GroupInfo group = new GroupInfo();

				if (level != 0)
					group.parent = groups[level - 1].size() - 1;
				if (level == groups.length - 1) {
					// at leaf group level, first child is the first row, which
					// is current row
					group.firstChild = rowID;

				} else {
					// Our first child is the group to be created at the next
					// level
					// in the next loop
					group.firstChild = groups[level + 1].size();
				}
				groups[level].add(group);
			}

			prevRow = currRow;
		}

		// this method must be called, since after doing group,
		// the current row index needs to adjusted the start value of data.
		this.groupCalculationUtil.getResultSetCache().reset();
		this.setLeaveGroupIndex(0);
	}

	/**
	 * Helper method to get the group break level between 2 rows
	 * 
	 * @param currRow
	 * @param prevRow
	 * @return
	 * @throws DataException
	 */
	private int getBreakLevel(IResultObject currRow, IResultObject prevRow) throws DataException {
		assert currRow != null;
		assert prevRow != null;

		int breakLevel = 0;
		for (; breakLevel < this.groupCalculationUtil.getGroupDefn().length; breakLevel++) {
			int colIndex = this.groupCalculationUtil.getGroupDefn()[breakLevel].getColumnIndex();

			Object currObjectValue = null;
			Object prevObjectValue = null;
			if (colIndex >= 0) {
				currObjectValue = currRow.getFieldValue(colIndex);
				prevObjectValue = prevRow.getFieldValue(colIndex);
			}

			GroupBy groupBy = this.groupCalculationUtil.getGroupDefn()[breakLevel];
			if (!groupBy.isInSameGroup(currObjectValue, prevObjectValue)) {
				// current group is the break level
				// reset the groupBys of the inner groups within current group for the following
				// compare
				for (int i = breakLevel + 1; i < this.groupCalculationUtil.getGroupDefn().length; i++) {
					this.groupCalculationUtil.getGroupDefn()[i].reset();
				}
				break;
			}
		}
		return breakLevel;
	}

	/**
	 * Returns all rows in the current group at the specified group level, as an
	 * array of ResultObject objects.
	 * 
	 * @param groupLevel
	 * @return int[], group star index and end index
	 * @throws DataException
	 */
	public int[] getCurrentGroupInfo(int groupLevel) throws DataException {
		if (groupLevel == 0) {
			return new int[] { 0, this.groupCalculationUtil.getResultSetCache().getCount() };
		}

		// temporary value
		GroupInfo groupInfo;

		// first get the index value in specified group level
		int groupLevelIndex = this.leafGroupIdx;
		for (int i = groups.length - 1; i > groupLevel - 1; i--) {
			List parentGroupInfo = groups[i];
			groupInfo = (GroupInfo) parentGroupInfo.get(groupLevelIndex);
			groupLevelIndex = groupInfo.parent;
		}

		// start index value in specified group level
		// we need it to get the index value of start row id
		int startIndex = groupLevelIndex;

		// next index value in specified group level
		// we need it to get the index value of end row id
		int endIndex = startIndex + 1;

		startIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, startIndex, groups,
				this.groupCalculationUtil.getResultSetCache().getCount());
		endIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, endIndex, groups,
				this.groupCalculationUtil.getResultSetCache().getCount());

		// finally we get data between startIndex to endIndex - 1 in dataRows
		return new int[] { startIndex, endIndex };
	}

	void readGroupsFromStream(InputStream inputStream) throws IOException {
		int size = IOUtil.readInt(inputStream);
		this.groups = new CachedList[size];

		for (int i = 0; i < size; i++) {
			List list = new CachedList(tempDir, DataEngineSession.getCurrentClassLoader(), GroupInfo.getCreator());
			;
			int asize = IOUtil.readInt(inputStream);
			for (int j = 0; j < asize; j++) {

				GroupInfo groupInfo = new GroupInfo();
				groupInfo.parent = IOUtil.readInt(inputStream);
				groupInfo.firstChild = IOUtil.readInt(inputStream);
				list.add(groupInfo);
			}
			this.groups[i] = list;
		}
	}

	void saveGroupsToStream(OutputStream outputStream) throws IOException {
		int size = groups.length;
		IOUtil.writeInt(outputStream, size);
		for (int i = 0; i < size; i++) {
			List list = groups[i];

			int asize = list.size();
			IOUtil.writeInt(outputStream, asize);

			for (int j = 0; j < asize; j++) {
				GroupInfo groupInfo = (GroupInfo) list.get(j);
				IOUtil.writeInt(outputStream, groupInfo.parent);
				IOUtil.writeInt(outputStream, groupInfo.firstChild);
			}
		}
	}

	/**
	 * The structue of a groupBoundaryInfoArray is exactly same as that of
	 * GroupCalculationUtil.groups,except that all GroupInfo instances are replaced
	 * by GroupBoundaryInfo instances.
	 * 
	 * @return
	 * @throws DataException
	 */
	List[] getGroupBoundaryInfos() throws DataException {
		List[] groupBoundaryInfos = new List[groups.length];

		for (int i = 1; i <= groups.length; i++) {
			groupBoundaryInfos[i - 1] = new CachedList(tempDir, DataEngineSession.getCurrentClassLoader(),
					GroupBoundaryInfo.getCreator());
			// i is the group level, is 1-based
			for (int j = 0; j < groups[i - 1].size(); j++) {
				groupBoundaryInfos[i - 1].add(getGroupBoundaryInfo(i, j));
			}
		}
		return groupBoundaryInfos;
	}

	/**
	 * Generate a GroupBoundaryInfo instance from certain groupLevel and groupIndex.
	 * 
	 * @param groupLevel 1-based group level
	 * @param groupIndex 0-based group index
	 * @return
	 * @throws DataException
	 */
	private GroupBoundaryInfo getGroupBoundaryInfo(int groupLevel, int groupIndex) throws DataException {
		// j is the group index, is 0-based
		int startIdx = GroupUtil.getGroupFirstRowIndex(groupLevel, groupIndex, groups,
				this.groupCalculationUtil.getResultSetCache().getCount());
		int endIdx = this.groupCalculationUtil.getResultSetCache().getCount() - 1;
		if (groupIndex < groups[groupLevel - 1].size() - 1) {
			endIdx = GroupUtil.getGroupFirstRowIndex(groupLevel, groupIndex + 1, groups,
					this.groupCalculationUtil.getResultSetCache().getCount()) - 1;
		}
		assert startIdx >= 0;
		assert endIdx >= 0;

		GroupBoundaryInfo result = new GroupBoundaryInfo(startIdx, endIdx);
		return result;
	}

	/**
	 * Re-sort the GroupBoundaryInfo instances of a lower group level according the
	 * ordering of GroupBoundaryInfo instances of a higher group level .
	 * 
	 * @param higherGroup
	 * @param lowerGroup
	 * @return
	 * @throws IOException
	 */
	private List mergeTwoGroupBoundaryInfoGroups(List higherGroup, List lowerGroup) throws IOException {
		List result = new CachedList(tempDir, DataEngineSession.getCurrentClassLoader(),
				GroupBoundaryInfo.getCreator());
		GroupInfoWithIndex groupInfoWithIndex;
		DiskSortedStack higherSortedStack = new DiskSortedStack(0, false,
				new GroupInfoWithIndexComparator(GroupInfoWithIndexComparator.START_INDEX_KEY),
				GroupInfoWithIndex.getCreator());
		DiskSortedStack lowerSortedStack = new DiskSortedStack(0, false,
				new GroupInfoWithIndexComparator(GroupInfoWithIndexComparator.START_INDEX_KEY),
				GroupInfoWithIndex.getCreator());
		// sort higher group boundary info objects
		for (int i = 0; i < higherGroup.size(); i++) {
			groupInfoWithIndex = new GroupInfoWithIndex();
			groupInfoWithIndex.groupIndex = i;
			groupInfoWithIndex.groupBoundaryInfo = (GroupBoundaryInfo) higherGroup.get(i);
			higherSortedStack.push(groupInfoWithIndex);
		}
		// sort lower group boundary info objects
		for (int i = 0; i < lowerGroup.size(); i++) {
			groupInfoWithIndex = new GroupInfoWithIndex();
			groupInfoWithIndex.groupIndex = i;
			groupInfoWithIndex.groupBoundaryInfo = (GroupBoundaryInfo) lowerGroup.get(i);
			lowerSortedStack.push(groupInfoWithIndex);
		}

		DiskSortedStack resultSortedStack = new DiskSortedStack(0, false,
				new GroupInfoWithIndexComparator(GroupInfoWithIndexComparator.PARENT_GROUP_INDEX_KEY),
				GroupInfoWithIndex.getCreator());

		GroupInfoWithIndex gbiH;
		GroupInfoWithIndex gbiL = (GroupInfoWithIndex) lowerSortedStack.pop();
		for (int i = 0; i < higherSortedStack.size(); i++) {
			gbiH = (GroupInfoWithIndex) higherSortedStack.pop();
			while (gbiL != null && gbiL.groupBoundaryInfo.getStartIndex() <= gbiH.groupBoundaryInfo.getEndIndex()) {
				if (gbiH.groupBoundaryInfo.isInBoundary(gbiL.groupBoundaryInfo)) {
					gbiL.parentGroupIndex = gbiH.groupIndex;
					resultSortedStack.push(gbiL);
				}
				gbiL = (GroupInfoWithIndex) lowerSortedStack.pop();
			}
		}
		for (int i = 0; i < resultSortedStack.size(); i++) {
			result.add(((GroupInfoWithIndex) resultSortedStack.pop()).groupBoundaryInfo);
		}

		return result;
	}

	/**
	 * This method return the OrderingInfo that will be finally used to re-generate
	 * the smartcache.
	 * 
	 * @param groups
	 * @return
	 */
	OrderingInfo getOrderingInfo(List[] groups) {
		// First merge all GroupBoundaryInfos groups
		for (int i = 1; i < groups.length; i++) {
			try {
				groups[i] = mergeTwoGroupBoundaryInfoGroups(groups[i - 1], groups[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Then populate the OrderingInfo
		OrderingInfo odInfo = new OrderingInfo();
		for (int i = 0; i < groups[groups.length - 1].size(); i++) {
			odInfo.add(((GroupBoundaryInfo) groups[groups.length - 1].get(i)).getStartIndex(),
					((GroupBoundaryInfo) groups[groups.length - 1].get(i)).getEndIndex());
		}
		return odInfo;
	}
}

class GroupInfoWithIndex implements IStructure {
	int parentGroupIndex;
	int groupIndex;
	GroupBoundaryInfo groupBoundaryInfo;

	public Object[] getFieldValues() {
		Object[] groupFields = groupBoundaryInfo.getFieldValues();
		Object[] fields = new Object[groupFields.length + 2];
		System.arraycopy(groupFields, 0, fields, 0, groupFields.length);
		fields[fields.length - 2] = Integer.valueOf(parentGroupIndex);
		fields[fields.length - 1] = Integer.valueOf(groupIndex);
		return fields;
	}

	public static IStructureCreator getCreator() {
		return new GroupInfoWithIndexCreator();
	}
}

class GroupInfoWithIndexCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		GroupInfoWithIndex groupInfoWithIndex = new GroupInfoWithIndex();
		groupInfoWithIndex.parentGroupIndex = ((Integer) fields[fields.length - 2]).intValue();
		groupInfoWithIndex.groupIndex = ((Integer) fields[fields.length - 1]).intValue();
		Object[] groupFields = new Object[fields.length - 2];
		System.arraycopy(fields, 0, groupFields, 0, fields.length - 2);
		groupInfoWithIndex.groupBoundaryInfo = (GroupBoundaryInfo) GroupBoundaryInfo.getCreator()
				.createInstance(groupFields);
		return groupInfoWithIndex;
	}
}

final class GroupInfoWithIndexComparator implements Comparator {
	final static int PARENT_GROUP_INDEX_KEY = 1;
	final static int START_INDEX_KEY = 2;
	private int keyType;

	GroupInfoWithIndexComparator(int keyType) {
		this.keyType = keyType;
	}

	/**
	 * 
	 */

	public int compare(Object o1, Object o2) {
		if (keyType == START_INDEX_KEY) {
			return compare(((GroupInfoWithIndex) o1).groupBoundaryInfo.getStartIndex(),
					((GroupInfoWithIndex) o2).groupBoundaryInfo.getStartIndex());
		} else if (keyType == PARENT_GROUP_INDEX_KEY) {
			int result = compare(((GroupInfoWithIndex) o1).parentGroupIndex,
					((GroupInfoWithIndex) o2).parentGroupIndex);
			if (result != 0)
				return result;
			return compare(((GroupInfoWithIndex) o1).groupIndex, ((GroupInfoWithIndex) o2).groupIndex);
		}
		return 0;
	}

	private int compare(int i1, int i2) {
		return (i1 < i2 ? -1 : (i1 == i2 ? 0 : 1));
	}
}