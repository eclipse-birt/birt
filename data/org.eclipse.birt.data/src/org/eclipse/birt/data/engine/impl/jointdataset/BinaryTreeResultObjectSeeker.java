/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.jointdataset;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 *
 */
public class BinaryTreeResultObjectSeeker implements IMatchResultObjectSeeker {
	private int joinType;
	private IJoinConditionMatcher matcher;
	private BinaryTreeROSeekerHelper helper;

	private int currentPrimaryIndex;
	private IResultIterator secondaryIterator;

	public BinaryTreeResultObjectSeeker(IJoinConditionMatcher matcher, int joinType) throws DataException {
		this.matcher = matcher;
		this.joinType = joinType;
		currentPrimaryIndex = -1;
	}

	/**
	 * @param array
	 * @return
	 * @throws DataException
	 */
	private List getNodeArray() throws DataException {
		List array = new ArrayList();
		ResultSetCache cache = this.secondaryIterator.getResultSetCache();
		int count = cache.getCount();
		int blockSize = count / 1024;
		int size = 1024;
		if (blockSize == 0) {
			size = count;
			blockSize = 1;
		}
		int startingIndex = 0;
		for (int i = 0; i < size; i++) {
			if (startingIndex >= count) {
				break;
			} else {
				Object[] min = matcher.getCompareValue(this.joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN);
				Object[] max = null;
				int start = startingIndex;

				startingIndex += blockSize;
				if (startingIndex < count) {
					cache.moveTo(startingIndex);
					max = matcher.getCompareValue(this.joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN);

					while (JointDataSetUtil.compare(
							matcher.getCompareValue(this.joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN), max) == 0) {
						startingIndex++;
						if (cache.fetch() == null) {
							break;
						}
					}
					if (startingIndex < count) {
						cache.moveTo(startingIndex);
					} else {
						cache.moveTo(count - 1);
					}
				} else {
					cache.moveTo(count - 1);
					max = matcher.getCompareValue(this.joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN);
				}
				array.add(new SegmentInfo(min, max, start, startingIndex - 1));
			}
		}
		this.secondaryIterator.first(0);
		return array;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.impl.jointdataset.IMatchResultObjectSeeker#
	 * getNextMatchedResultObject(int)
	 */
	@Override
	public IResultObject getNextMatchedResultObject(int primaryIndex) throws DataException {
		Object[] o = matcher.getCompareValue(!(this.joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN));
		SegmentInfo n = helper.search(o);
		if (n == null) {
			return null;
		}
		if (this.currentPrimaryIndex != primaryIndex) {
			this.secondaryIterator.getResultSetCache().moveTo(n.getStartingIndex());
			this.currentPrimaryIndex = primaryIndex;
		} else {
			this.secondaryIterator.next();
		}
		do {
			if (this.secondaryIterator.getCurrentResult() != null && matcher.match()) {
				return this.secondaryIterator.getCurrentResult();
			}
		} while (this.secondaryIterator.getCurrentResultIndex() <= n.getEndingIndex() && this.secondaryIterator.next());
		return null;
	}

	@Override
	public void setResultIterator(IResultIterator ri) throws DataException {
		this.secondaryIterator = ri;
		helper = new BinaryTreeROSeekerHelper(getNodeArray());
	}
}

/**
 * The instance of SegmentInfo give the up and bottom boundary of values being
 * evaluated as join conditions from a set of ResultObjects.It also records the
 * starting and ending index of that set of ResultObjects.
 *
 */
class SegmentInfo {
	//
	private Object[] maxValue;
	private Object[] minValue;
	private int startingIndex;
	private int endingIndex;

	/**
	 * Constructor.
	 *
	 * @param minValue
	 * @param maxValue
	 * @param startingIndex
	 * @param endingIndex
	 */
	SegmentInfo(Object[] minValue, Object[] maxValue, int startingIndex, int endingIndex) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.startingIndex = startingIndex;
		this.endingIndex = endingIndex;
	}

	/**
	 * The starting index.
	 *
	 * @return
	 */
	int getStartingIndex() {
		return this.startingIndex;
	}

	/**
	 * The ending Index(inclusive).
	 *
	 * @return
	 */
	int getEndingIndex() {
		return this.endingIndex;
	}

	/**
	 * The maxium value.
	 *
	 * @return
	 */
	Object[] getMaxValue() {
		return this.maxValue;
	}

	/**
	 * The minimum value.
	 *
	 * @return
	 */
	Object[] getMinValue() {
		return this.minValue;
	}
}
