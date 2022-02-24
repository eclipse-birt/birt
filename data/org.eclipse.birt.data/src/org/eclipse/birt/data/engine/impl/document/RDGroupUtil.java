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
package org.eclipse.birt.data.engine.impl.document;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.group.GroupInfo;
import org.eclipse.birt.data.engine.executor.transform.group.GroupUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This class is read-only part of complete GroupUtil. Its group information
 * data will be loaded from external stream, not generated from a SmartCache.
 */
public final class RDGroupUtil implements IRDGroupUtil {
	/*
	 * groups[level] is an CachedList of GroupInfo objects at the specified level.
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
	private List[] groups;

	// index of the current innermost group
	private int leafGroupIdx = 0;

	// provide service of current data cache
	private CacheProvider cacheProvider;

	private Map<Integer, int[]> groupStartEndIndexCache = new HashMap<Integer, int[]>();

	private List<RAInputStream> inputStreams;

	/**
	 * @param inputStream
	 * @param cacheProvider
	 * @throws DataException
	 */
	RDGroupUtil(String tempDir, int groupNumber, List<RAInputStream> inputStreams, CacheProvider cacheProvider)
			throws DataException {
		this.groups = new List[groupNumber];

		for (int i = 0; i < groupNumber; i++) {
			this.groups[i] = new GroupCachedList(inputStreams.get(i));
		}
		this.cacheProvider = cacheProvider;
		this.inputStreams = inputStreams;
	}

	/**
	 * @param inputStream
	 * @param cacheProvider
	 * @throws DataException
	 */
	public RDGroupUtil(String tempDir, int groupNumber, List<RAInputStream> inputStreams) throws DataException {
		this(tempDir, groupNumber, inputStreams, null);
	}

	/**
	 * @param cacheProvider
	 */
	public void setCacheProvider(CacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}

	/**
	 * use if with care
	 *
	 * @param groups
	 */
	public List[] getGroups() {
		return this.groups;
	}

	/**
	 * use if with care
	 *
	 * @param groups
	 */
	public void setGroups(List[] groups) {
		this.groups = groups;
	}

	public void close() throws DataException {
		try {
			if (this.inputStreams != null) {
				for (int i = 0; i < this.inputStreams.size(); i++) {
					this.inputStreams.get(i).close();
				}
				inputStreams = null;
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	// Helper function to find information about a group, given the group level
	// and the group index at that level. Returns null if groupIndex exceeds
	// max group index
	private GroupInfo findGroup(int groupLevel, int groupIndex) {
		if (groupIndex >= groups[groupLevel].size())
			return null;
		else {
			try {
				return (GroupInfo) groups[groupLevel].get(groupIndex);
			} catch (Exception e) {
				return null;
			}
		}
	}

	private void checkStarted() throws DataException {
		if (cacheProvider == null)
			throw new DataException(ResourceConstants.NO_CURRENT_ROW);
	}

	private void checkHasCurrentRow() throws DataException {
		checkStarted();
		if (cacheProvider.getCurrentIndex() >= cacheProvider.getCount() && cacheProvider.getCount() != -1)
			throw new DataException(ResourceConstants.NO_CURRENT_ROW);
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
		if (cacheProvider.getCurrentIndex() == cacheProvider.getCount() - 1)
			return 0;

		// 1 is returned if no groups are defined
		if (groups.length == 0)
			return 1;

		// Find outermost group that current row ends
		int childGroupIdx = cacheProvider.getCurrentIndex();
		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = groups.length - 1; level >= 0; level--) {
			// Current row is known to end child group with index childGroupIdx
			// Does it also end this group?
			GroupInfo nextGroup = findGroup(level, currentGroupIdx + 1);
			if (nextGroup != null && childGroupIdx == nextGroup.firstChild - 1) {
				// Yes it also ends this group; check if it ends parent as well
				childGroupIdx = currentGroupIdx;
				currentGroupIdx = findGroup(level, currentGroupIdx).parent;
				continue;
			}
			break;
		}

		// current row ends group (level +1 ). Note that the group index we
		// return is 1-based
		return level + 2;
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
		if (cacheProvider.getCurrentIndex() == 0)
			return 0;

		// If no groups defined, return 1
		if (groups.length == 0)
			return 1;

		// Find outermost group that current row starts
		int childGroupIdx = cacheProvider.getCurrentIndex();
		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = groups.length - 1; level >= 0; level--) {
			// Current row is known to start child group with index
			// childGroupIdx
			// Does it also start this group?
			GroupInfo currentGroup = findGroup(level, currentGroupIdx);
			if (currentGroup != null && childGroupIdx == currentGroup.firstChild) {
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

	// Finds index of current group at the specified group level
	private int findCurrentGroup(int groupLevel) {
		// Walk up the group chain from leaf group
		int currentGroupIdx = leafGroupIdx;
		for (int i = groups.length - 1; i > groupLevel; i--)
			currentGroupIdx = findGroup(i, currentGroupIdx).parent;
		return currentGroupIdx;
	}

	/**
	 * Advances row cursor to the last row at the specified group level
	 *
	 * @param groupLevel the specified group level that will be skipped, 1 indicate
	 *                   the highest level. 0 indicates whole list.
	 */
	public void last(int groupLevel) throws DataException {
		if (groupLevel > groups.length || groupLevel < 0)
			throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));

		groupLevel--; // change to 0-based index

		// First find current group at the specified group level
		int currentGroupIdx = -1;
		if (groupLevel >= 0)
			currentGroupIdx = findCurrentGroup(groupLevel);

		if (groupLevel < 0 || // an input of 0 means moving to last row in
		// list
				currentGroupIdx >= groups[groupLevel].size() - 1) {
			// Move to last row in entire list
			// Last row is in the last leaf group
			int currentRowID = cacheProvider.getCount() - 1;

			cacheProvider.moveTo(currentRowID);
			if (groups.length > 0)
				leafGroupIdx = groups[groups.length - 1].size() - 1;
			return;
		}

		// Find first row in the next group
		++currentGroupIdx;

		for (int i = groupLevel + 1; i < groups.length; i++) {
			currentGroupIdx = findGroup(i - 1, currentGroupIdx).firstChild;
		}

		// Move back one row and one leaf group
		int currentRowID = findGroup(groups.length - 1, currentGroupIdx).firstChild - 1;
		cacheProvider.moveTo(currentRowID);
		leafGroupIdx = currentGroupIdx - 1;
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
		if (hasNext == true && groups.length > 0) {
			GroupInfo nextLeafGroup = findGroup(groups.length - 1, leafGroupIdx + 1);
			if (nextLeafGroup != null && cacheProvider.getCurrentIndex() >= nextLeafGroup.firstChild) {
				// Move to next leaf group
				++leafGroupIdx;
			}
		}
	}

	/**
	 * Advance the leaf group with offset.
	 * 
	 * @param offset
	 * @throws DataException
	 */
	public void move() throws DataException {
		if (groups.length > 0) {
			binaryMove();
		}
	}

	/**
	 *
	 * @throws DataException
	 */
	private void binaryMove() throws DataException {
		List<GroupInfo> groupList = this.getGroups()[groups.length - 1];
		int low = leafGroupIdx;
		// Only happen in progressive viewing.
		if (groupList.size() == Integer.MAX_VALUE) {
			int i = 0;
			while (true) {
				GroupInfo info = groupList.get(i);
				// In progressive viewing mode, there exist possibility
				// that group info is not flush to stream yet.Simply do not handle
				// this case.
				if (info == null)
					return;
				if (info.firstChild > cacheProvider.getCurrentIndex()) {
					this.leafGroupIdx = i - 1;
					return;
				}
				i++;
			}
		}
		int high = groupList.size() - 1;
		int mid;

		while (low <= high) {
			mid = (high + low) / 2;
			if (((GroupInfo) groupList.get(mid)).firstChild > cacheProvider.getCurrentIndex()) {
				high = mid - 1;
			} else if (mid == groupList.size() - 1
					|| ((GroupInfo) groupList.get(mid + 1)).firstChild > cacheProvider.getCurrentIndex()) {
				leafGroupIdx = mid;
				return;
			} else {
				low = mid + 1;
			}
		}
	}

	/**
	 * @return
	 */
	public int getGroupLevel() {
		return this.groups.length;
	}

	/**
	 * Gets the index of the current group at the specified group level. The index
	 * starts at 0
	 */
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		checkHasCurrentRow();

		if (groupLevel == 0)
			return 0;

		if (groupLevel < 0 || groupLevel > groups.length)
			throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));

		int currentGroupIdx = leafGroupIdx;
		int level;
		for (level = groups.length - 1; level > groupLevel - 1; level--) {
			GroupInfo currentGroup = findGroup(level, currentGroupIdx);
			currentGroupIdx = currentGroup.parent;
		}
		return currentGroupIdx;
	}

	/**
	 * For a particual group level, it might consists of several group units. For
	 * each group unit, it has its start row index and end row index + 1, and then
	 * the total index will be the group unit number*2.
	 *
	 * @param groupLevel
	 * @return int[]
	 */
	public int[] getGroupStartAndEndIndex(int groupLevel) {
		if (this.groupStartEndIndexCache.containsKey(groupLevel))
			return this.groupStartEndIndexCache.get(groupLevel);
		int max = -1;
		if (this.cacheProvider != null)
			max = this.cacheProvider.getCount();

		if (groupLevel == 0) {
			this.groupStartEndIndexCache.put(groupLevel, new int[] { 0, max });
			return this.groupStartEndIndexCache.get(groupLevel);
		}

		int unitCountInOneGroup = this.groups[groupLevel - 1].size();
		if (unitCountInOneGroup == 1) {
			this.groupStartEndIndexCache.put(groupLevel, new int[] { 0, max });
			return this.groupStartEndIndexCache.get(groupLevel);
		} else {
			int[] unitInfo = new int[unitCountInOneGroup * 2];
			for (int i = 0; i < unitCountInOneGroup; i++) {
				int startIndex = i;
				int endIndex = startIndex + 1;

				startIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, startIndex, this.groups, max);
				endIndex = GroupUtil.getGroupFirstRowIndex(groupLevel, endIndex, this.groups, max);

				unitInfo[i * 2] = startIndex;
				unitInfo[i * 2 + 1] = endIndex;
			}
			this.groupStartEndIndexCache.put(groupLevel, unitInfo);
			return this.groupStartEndIndexCache.get(groupLevel);
		}
	}

	private static class GroupCachedList implements List<GroupInfo> {
		private int size;
		private RAInputStream dataSource;
		private long initOffset;

		public GroupCachedList(RAInputStream input) throws DataException {
			// We need not buffer stream here, for all the RA input stream is already
			// buffered.
			this.dataSource = input;

			try {
				initOffset = dataSource.getOffset();
				size = IOUtil.readInt(dataSource);
			} catch (IOException e) {
				throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Group Info");
			}
		}

		/**
		 *
		 * @return
		 */
		public int size() {
			return this.size;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.List#get(int)
		 */
		public GroupInfo get(int index) {
			GroupInfo groupInfo = new GroupInfo();
			try {
				this.dataSource.seek((index * 2 + 1) * IOUtil.INT_LENGTH + this.initOffset);
				groupInfo.parent = IOUtil.readInt(this.dataSource);
				groupInfo.firstChild = IOUtil.readInt(this.dataSource);
			} catch (IOException e) {
				try {
					// In progressive viewing mode, it is possible that the next group info is not
					// available yet.
					// See TED 42519
					if (this.dataSource.length() == ((index * 2 + 1) * IOUtil.INT_LENGTH + this.initOffset)) {
						return null;
					}
				} catch (IOException e1) {
				}
				throw new RuntimeException(e);
			}

			return groupInfo;
		}

		public boolean add(GroupInfo o) {
			throw new UnsupportedOperationException();

		}

		public void add(int index, GroupInfo element) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(int index, Collection c) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
//			throw new UnsupportedOperationException( );
		}

		public boolean contains(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean containsAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean isEmpty() {
			return this.size == 0;
		}

		public Iterator iterator() {
			throw new UnsupportedOperationException();
		}

		public int lastIndexOf(Object o) {
			throw new UnsupportedOperationException();
		}

		public ListIterator listIterator() {
			throw new UnsupportedOperationException();
		}

		public ListIterator listIterator(int index) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public GroupInfo remove(int index) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public GroupInfo set(int index, GroupInfo element) {
			throw new UnsupportedOperationException();
		}

		public List subList(int fromIndex, int toIndex) {
			throw new UnsupportedOperationException();
		}

		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		public Object[] toArray(Object[] a) {
			throw new UnsupportedOperationException();
		}
	}
}
