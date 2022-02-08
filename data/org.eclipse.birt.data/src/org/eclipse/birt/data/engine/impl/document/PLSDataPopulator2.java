/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This class take a document result iterator and a list of IGroupInstanceInfo
 * as input. It will then wrap the document result iterator so that only the
 * rows as defined in IGroupInstanceInfo list will be retrievable by user.
 * 
 * This class is used to get the PLS Data for summary table.
 */

public class PLSDataPopulator2 implements IPLSDataPopulator {

	// The target groups. It is sorted and merged from the original
	// user input so that following conditions hold
	// 1.The inner the group level is, the higher the position of its Boundary
	// info appears in the list
	// 2.In case the group level is same, the Boundary will be ordered by the
	// row it specific to.
	private List<Boundary2> targetBoundaries;

	// row index.
	private int rowIndex = -1;

	// The document result iterator.
	private ResultIterator docIt;

	// The current group boundary.
	private Boundary2 currentBoundary;

	// Indicate whether the result set is empty.
	private boolean isEmpty;

	/**
	 * Constructor.
	 * 
	 * @param targetGroups
	 * @param docIt
	 * @throws DataException
	 */
	PLSDataPopulator2(List<IGroupInstanceInfo> targetGroups, ResultIterator docIt) throws DataException {
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
	 */
	private void populateBoundary(List<IGroupInstanceInfo> targetGroups) {
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
		List<Boundary2> boundaries = new LinkedList<Boundary2>();
		tag: for (IGroupInstanceInfo info : groups) {

			Boundary2 b = new Boundary2(info.getGroupLevel(), info.getRowId());

			// Try to merge the boundaries.
			for (Boundary2 target : boundaries) {
				if (b.containedBy(target))
					continue tag;
			}

			// If failed to merge, simply add to boundaries list.
			boundaries.add(b);
			continue tag;
		}

		Collections.sort(boundaries, new Comparator<Boundary2>() {

			public int compare(Boundary2 o1, Boundary2 o2) {
				if (o1.start < o2.start)
					return -1;
				else if (o1.start > o2.start)
					return 1;
				return 0;
			}

		});

		if (boundaries.size() > 0) {
			this.currentBoundary = boundaries.get(0);

		}

		this.targetBoundaries = boundaries;

	}

	/**
	 * 
	 * @throws BirtException
	 */
	public void close() throws DataException {
		try {
			this.docIt.close();
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
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
			if (this.currentBoundary == null) {
				return false;
			}
			while (docIt.next()) {
				this.getDocumentIterator().getExprResultSet().getDataSetResultSet().next();
				if (docIt.getRowIndex() < this.currentBoundary.start) {
					docIt.moveTo(this.currentBoundary.start);
					this.getDocumentIterator().getExprResultSet().getDataSetResultSet()
							.skipTo(this.currentBoundary.start);
				}

				if (docIt.getEndingGroupLevel() == this.currentBoundary.endGroupLevel) {
					this.targetBoundaries.remove(this.currentBoundary);
					if (this.targetBoundaries.size() == 0) {
						this.rowIndex++;
						this.currentBoundary = null;
						return true;
					}
					this.currentBoundary = this.targetBoundaries.get(0);
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
	protected static class Boundary2 {
		private int start;
		int startGroupLevel;
		int endGroupLevel;

		/**
		 * 
		 * @param groupLevel
		 * @param start
		 * @param end
		 */
		Boundary2(int groupLevel, int start) {
			this.start = start;
			this.startGroupLevel = groupLevel;
			this.endGroupLevel = groupLevel;
		}

		/**
		 * 
		 * @param target
		 * @return
		 */
		public boolean containedBy(Boundary2 target) {
			return target.start == this.start;
		}

		public int getStart() {
			return this.start;
		}
	}
}
