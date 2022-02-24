/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

import org.eclipse.birt.data.engine.cache.BasicCachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

public class GroupInfoUpdator {
	private int level;
	protected AggrValuesUpdator[] aggrUpdator;
	protected List<GroupInfo> originGroups;
	protected List<GroupInfo> newGroups;
	protected int parentIdxAdj;
	protected int childIdxAdj;
	protected RuntimeGroupInfo currentGroup;
	protected int lastIndex;

	@SuppressWarnings("unchecked")
	public GroupInfoUpdator(int level, String tempDir, List<GroupInfo> groups, int last,
			AggrValuesUpdator[] aggrValues) {
		this.level = level;
		this.originGroups = groups;
		this.lastIndex = last;
		this.newGroups = new BasicCachedList(tempDir, DataEngineSession.getCurrentClassLoader());
		this.aggrUpdator = aggrValues;
	}

	/**
	 * Try to mark the given group can be accepted and accept current processed
	 * group.
	 * 
	 * @param groupIndex Group index
	 * @throws DataException
	 */
	public void onGroup(int groupIndex) throws DataException {
		acceptPrevious(groupIndex);
		if (currentGroup == null)
			currentGroup = getRumtimeGroupInfo(groupIndex);
	}

	protected void acceptPrevious(int groupIndex) throws DataException {
		if (currentGroup == null) {
			return;
		} else if (currentGroup.groupId < groupIndex) {
			if (!currentGroup.isRemoved()) {
				acceptGroup(currentGroup);
			}
			currentGroup = null;
		} else if (currentGroup.groupId > groupIndex) {
			throw new DataException(ResourceConstants.GROUPUPDATE_ILLEGAL_GROUP_ORDER_STATE,
					new Object[] { currentGroup.groupId, groupIndex });
		}
		// Same group do nothing.
	}

	/**
	 * Try to mark the given group can be filtered out and accept current processed
	 * group.
	 * 
	 * @param groupIndex
	 * @return Removed group index or -1 while no group is removed.
	 * @throws DataException
	 */
	public int notOnGroup(int groupIndex) throws DataException {
		acceptPrevious(groupIndex);
		if (currentGroup == null)
			currentGroup = getRumtimeGroupInfo(groupIndex);

		int rIdx = -1;
		currentGroup.removed++;
		childIdxAdj++;

		if (currentGroup.isRemoved())
			rIdx = groupIndex;

		return rIdx;
	}

	protected RuntimeGroupInfo getRumtimeGroupInfo(int index) {
		GroupInfo current = getGroupInfo(index);
		GroupInfo next = getGroupInfo(index + 1);
		int count;
		if (next != null)
			count = next.firstChild - current.firstChild;
		else
			count = lastIndex - current.firstChild + 1;

		return new RuntimeGroupInfo(current, index, count, parentIdxAdj, childIdxAdj);
	}

	protected GroupInfo getGroupInfo(int index) {
		if (index < originGroups.size())
			return originGroups.get(index);
		else
			return null;
	}

	protected void acceptGroup(RuntimeGroupInfo groupInfo) {
		GroupInfo grpInfo = new GroupInfo();
		grpInfo.parent = groupInfo.group.parent - groupInfo.parentIdxAdj;
		grpInfo.firstChild = groupInfo.group.firstChild - groupInfo.childIdxAdj;
		newGroups.add(grpInfo);
		acceptAggr(groupInfo.groupId);
	}

	protected void acceptAggr(int groupIndex) {
		for (int i = 0; aggrUpdator != null && i < aggrUpdator.length; i++)
			aggrUpdator[i].onGroup(level + 1, groupIndex);
	}

	public void increaseParentIndex() {
		parentIdxAdj++;
	}

	public List<GroupInfo> getGroups() {
		return newGroups;
	}

	public void close() {
		if (currentGroup != null && !currentGroup.isRemoved())
			acceptGroup(currentGroup);
	}

	public int filterGroup(int groupIndex) throws DataException {
		acceptPrevious(groupIndex);
		if (currentGroup == null)
			currentGroup = getRumtimeGroupInfo(groupIndex);

		currentGroup.removed += currentGroup.count;
		childIdxAdj += currentGroup.count;
		assert currentGroup.isRemoved();

		return currentGroup.count;
	}

	/**
	 * Filter a chunk of groups.
	 * 
	 * @param range The group range to be filtered
	 * @return A <code>GroupRange</code> to be filtered at next level
	 */
	public GroupRange filterGroupRange(GroupRange range) throws DataException {
		GroupRange childRange = new GroupRange();
		for (int i = range.first; i < range.length + range.first; i++) {
			RuntimeGroupInfo currentGroup = getRumtimeGroupInfo(i);
			if (i == range.first) {
				acceptPrevious(i);
				childRange.first = currentGroup.group.firstChild;
			}
			childRange.length += currentGroup.count;
		}
		childIdxAdj += childRange.length;
		return childRange;
	}

	/**
	 * Accept a chunk of groups.
	 * 
	 * @param range The group range to be accepted
	 * @return A <code>GroupRange</code> to be accepted at the next level
	 * @throws DataException
	 */
	public GroupRange acceptGroupRange(GroupRange range) throws DataException {
		GroupRange childRange = new GroupRange();
		for (int i = range.first; i < range.length + range.first; i++) {
			RuntimeGroupInfo currentGroup = getRumtimeGroupInfo(i);
			if (i == range.first) {
				acceptPrevious(i);
				childRange.first = currentGroup.group.firstChild;
			}
			childRange.length += currentGroup.count;
			acceptGroup(currentGroup);
		}

		return childRange;
	}

	/**
	 * @return The current group's <code>GroupRange</code> at the next level
	 */
	public GroupRange getChildRange() {
		if (currentGroup == null)
			return null;

		return new GroupRange(currentGroup.group.firstChild, currentGroup.count);
	}

	public void increaseChildIndex(int childs) {
		this.childIdxAdj += childs;
		if (currentGroup != null)
			currentGroup.removed += childs;
	}

	public void increaseParentIndex(int parents) {
		parentIdxAdj += parents;
	}

	public int getChildCount(int groupIdx) {
		int count = 0;
		GroupInfo i = getGroupInfo(groupIdx);
		if (i == null)
			return count;

		GroupInfo next = getGroupInfo(groupIdx + 1);
		if (next != null)
			count = next.firstChild - i.firstChild;
		else
			count = lastIndex - i.firstChild + 1;
		return count;
	}
}
