/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This class take a document result iterator and a list of IGroupInstanceInfo
 * as input. It will then wrap the document result iterator so that only the
 * rows as defined in IGroupInstanceInfo list will be retrievable by user.
 */

public class PLSDataPopulator implements IPLSDataPopulator {

	// The target groups. It is sorted and merged from the original
	// user input so that following conditions hold
	// 1.The inner the group level is, the higher the position of its Boundary
	// info appears in the list
	// 2.In case the group level is same, the Boundary will be ordered by the
	// row it specific to.
	protected List<Boundary> targetBoundaries;

	// In case current row is not in a valid boundary, simply ignore the whole
	// group
	// instance it belongs to. The level of group instance is the outest group
	// level
	// in given boundaries.
	protected int jumpGroupLevel = 0;

	// row index.
	protected int rowIndex = -1;

	// The document result iterator.
	protected ResultIterator docIt;

	// The current group boundary.
	protected Boundary currentBoundary;

	// Indicate whether the result set is empty.
	protected boolean isEmpty;

	/**
	 * Constructor.
	 * 
	 * @param targetGroups
	 * @param docIt
	 * @throws DataException
	 */
	PLSDataPopulator(List<IGroupInstanceInfo> targetGroups, ResultIterator docIt) throws DataException {
		this.docIt = docIt;

		this.populateBoundary(targetGroups);

		this.populateEmptyInfo();
	}

	/**
	 * Return the enclosed document iterator.
	 * 
	 * @return
	 */
	public ResultIterator getDocumentIterator() {
		return this.docIt;
	}

	/**
	 * 
	 */
	private void populateEmptyInfo() {
		this.isEmpty = !(this.currentBoundary != null
				&& this.currentBoundary.getStart() < this.docIt.getExprResultSet().getDataSetResultSet().getRowCount());
	}

	/**
	 * Populate the boundaries info.
	 * 
	 * @param targetGroups
	 * @throws DataException
	 */
	private void populateBoundary(List<IGroupInstanceInfo> targetGroups) throws DataException {
		// Make a copy to user input.
		List<IGroupInstanceInfo> groups = new ArrayList<IGroupInstanceInfo>(targetGroups);

		// Sort the copy
		Collections.sort(groups, new Comparator<IGroupInstanceInfo>() {

			public int compare(IGroupInstanceInfo arg0, IGroupInstanceInfo arg1) {
				if (arg0.getGroupLevel() < arg1.getGroupLevel())
					return -1;
				if (arg0.getGroupLevel() > arg1.getGroupLevel())
					return 1;
				if (arg0.getRowId() < arg1.getRowId())
					return -1;
				if (arg0.getRowId() > arg1.getRowId())
					return 1;
				return 0;
			}
		});

		// Here the groups has been sorted.
		List<Boundary> boundaries = new LinkedList<Boundary>();
		tag: for (IGroupInstanceInfo info : groups) {
			int[] groupStartEndingIndex = docIt.getExprResultSet().getGroupStartAndEndIndex(info.getGroupLevel());
			for (int i = 0; i < groupStartEndingIndex.length; i = i + 2) {
				if (groupStartEndingIndex[i] <= info.getRowId() && groupStartEndingIndex[i + 1] > info.getRowId()) {
					Boundary b = new Boundary(info.getGroupLevel(), groupStartEndingIndex[i],
							groupStartEndingIndex[i + 1] - 1);

					// Try to merge the boundaries.
					for (Boundary target : boundaries) {
						if (b.containedBy(target))
							continue tag;
					}

					// If failed to merge, simply add to boundaries list.
					boundaries.add(b);
					continue tag;
				}
			}
		}

		Collections.sort(boundaries, new Comparator<Boundary>() {

			public int compare(Boundary o1, Boundary o2) {
				if (o1.start < o2.start)
					return -1;
				else if (o1.start > o2.start)
					return 1;
				return 0;
			}

		});

		populateStartingEndingGroupLevel(groups, boundaries);

		if (boundaries.size() > 0) {
			this.currentBoundary = boundaries.get(0);

		}

		this.targetBoundaries = boundaries;

	}

	/**
	 * 
	 * @param groups
	 * @param boundaries
	 * @throws DataException
	 */
	private void populateStartingEndingGroupLevel(List<IGroupInstanceInfo> groups, List<Boundary> boundaries)
			throws DataException {
		int matteredGroupLevel = groups.get(groups.size() - 1).getGroupLevel();

		for (int i = 0; i <= matteredGroupLevel; i++) {
			int[] starEndGroupIndex = this.docIt.getExprResultSet().getGroupStartAndEndIndex(i);
			List<Boundary> temp = new ArrayList<Boundary>(boundaries);
			for (int j = 0; j < starEndGroupIndex.length; j = j + 2) {
				// If all the boundaries are processed, then continue to process next group
				// level.
				if (temp.isEmpty())
					break;

				// Feed all the boundaries of current starting/ending group index to this list
				List<Boundary> contained = new ArrayList<Boundary>();

				Iterator<Boundary> it = temp.iterator();
				while (it.hasNext()) {
					Boundary b = it.next();
					// Need not process the boundary that of same or above the current group
					// that under check.
					if (b.groupLevel <= i) {
						it.remove();
						continue;
					}
					if (b.start >= starEndGroupIndex[j] && b.end <= starEndGroupIndex[j + 1] - 1) {
						contained.add(b);
						it.remove();
						continue;
					}

					// if b is not in current starting ending group index boundary.
					if (b.start > starEndGroupIndex[j + 1]) {
						break;
					}
				}
				if (!contained.isEmpty()) {
					contained.get(0).startGroupLevel = contained.get(0).startGroupLevel < i
							? contained.get(0).startGroupLevel
							: i;
					contained.get(
							contained.size() - 1).endGroupLevel = contained.get(contained.size() - 1).endGroupLevel < i
									? contained.get(contained.size() - 1).endGroupLevel
									: i;
				}
			}
		}
	}

	/**
	 * 
	 * @throws BirtException
	 */
	public void close() throws BirtException {
		this.docIt.close();
	}

	/**
	 * Move to next qualified row.
	 * 
	 * @return
	 * @throws DataException
	 */
	public boolean next() throws DataException {
		if (this.isEmpty)
			return false;
		try {
			while (docIt.next()) {
				if (docIt.getExprResultSet().getCurrentIndex() < this.currentBoundary.start) {
					docIt.moveTo(this.currentBoundary.start);
				} else if (docIt.getExprResultSet().getCurrentIndex() > this.currentBoundary.end) {
					this.targetBoundaries.remove(this.currentBoundary);

					if (this.targetBoundaries.size() == 0) {
						break;
					}

					this.currentBoundary = this.targetBoundaries.get(0);

					docIt.moveTo(this.currentBoundary.start);

				}

				this.rowIndex++;
				return true;
			}
		} catch (BirtException e1) {
			throw DataException.wrap(e1);
		}
		return false;
	}

	/**
	 * 
	 * @author Work
	 * 
	 */
	protected static class Boundary {

		//
		private int start;
		private int end;
		private int groupLevel;

		// By default the startGroupLevel should be 1.
		// For after the merge all the standalone boundary should start and end
		// group level 1
		// The first and last boundaries make exception.
		int startGroupLevel;
		int endGroupLevel;

		/**
		 * 
		 * @param groupLevel
		 * @param start
		 * @param end
		 */
		Boundary(int groupLevel, int start, int end) {
			this.start = start;
			this.end = end;
			this.startGroupLevel = groupLevel;
			this.endGroupLevel = groupLevel;
			this.groupLevel = groupLevel;
		}

		/**
		 * 
		 * @param target
		 * @return
		 */
		public boolean containedBy(Boundary target) {
			return target.start <= this.start && target.end >= this.end;
		}

		public int getStart() {
			return this.start;
		}

		public int getEnd() {
			return this.end;
		}
	}
}
