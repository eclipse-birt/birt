
/*******************************************************************************
 * Copyright (c) 2004,  2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.util.logging.Logger;

/**
 * A dimension can be divided to several sub dimensions by a DimensionDivision.
 * An instance of DimensionDivision contains a range array to indicate the start
 * and end of sub dimensions.
 */

public class DimensionDivision {
	private IntRange[] ranges = null;
	private static Logger logger = Logger.getLogger(DimensionDivision.class.getName());

	/**
	 * 
	 * @param dimensionMemberCount
	 * @param subDimensionCount
	 */
	public DimensionDivision(int dimensionMemberCount, int subDimensionCount) {
		Object[] params = { Integer.valueOf(dimensionMemberCount), Integer.valueOf(subDimensionCount) };
		logger.entering(DimensionDivision.class.getName(), "DimensionDivision", params);
		if (dimensionMemberCount <= subDimensionCount) {
			setRanges(new IntRange[dimensionMemberCount]);
			for (int i = 0; i < dimensionMemberCount; i++) {
				getRanges()[i] = new IntRange(i, i);
			}
			return;
		}
		int[] subDimensionMemberCount = new int[subDimensionCount];
		int baseSize = dimensionMemberCount / subDimensionCount;
		for (int i = 0; i < dimensionMemberCount % subDimensionCount; i++) {
			subDimensionMemberCount[i] = baseSize + 1;
		}
		for (int i = dimensionMemberCount % subDimensionCount; i < subDimensionCount; i++) {
			subDimensionMemberCount[i] = baseSize;
		}
		setRanges(new IntRange[subDimensionCount]);
		getRanges()[0] = new IntRange(0, subDimensionMemberCount[0] - 1);

		for (int i = 1; i < getRanges().length; i++) {
			getRanges()[i] = new IntRange();
			getRanges()[i].start = getRanges()[i - 1].end + 1;
			getRanges()[i].end = getRanges()[i].start + subDimensionMemberCount[i] - 1;
		}

		assert getRanges()[getRanges().length - 1].end == dimensionMemberCount - 1;
		logger.exiting(DimensionDivision.class.getName(), "DimensionDivision");
	}

	int getSubDimensionIndex(int dimensionIndex) {
		for (int i = 0; i < getRanges().length; i++) {
			if (getRanges()[i].contains(dimensionIndex)) {
				return i;
			}
		}

		return -1;
	}

	void setRanges(IntRange[] ranges) {
		this.ranges = ranges;
	}

	public IntRange[] getRanges() {
		return ranges;
	}

	public static class IntRange {
		int start;
		int end;

		IntRange() {

		}

		IntRange(int start, int end) {
			this.start = start;
			this.end = end;
		}

		boolean contains(int i) {
			return i >= start && i <= end;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}
	}
}
