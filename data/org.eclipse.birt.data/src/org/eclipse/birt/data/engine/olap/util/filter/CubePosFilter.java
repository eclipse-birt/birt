
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.util.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * 
 */

public abstract class CubePosFilter implements ICubePosFilter {
	protected String[] dimensionNames;
	protected List cubePosRangeFilter = null;

	/**
	 * 
	 * @param measureNames
	 */
	public CubePosFilter(String[] dimensionNames) {
		this.dimensionNames = dimensionNames;
		this.cubePosRangeFilter = new ArrayList();
	}

	/**
	 * 
	 * @param dimPositions
	 * @throws IOException
	 */
	public void addDimPositions(IDiskArray[] dimPositions) throws IOException {
		CubePositionRangeFilter cubePositionRangeFilter = new CubePositionRangeFilter(dimPositions);
		cubePosRangeFilter.add(cubePositionRangeFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter#
	 * getFilterDimensionNames()
	 */
	public String[] getFilterDimensionNames() {
		return dimensionNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter#getFilterResult(
	 * int[])
	 */
	public abstract boolean getFilterResult(int[] dimPositions);

}

/**
 * 
 * @author Administrator
 *
 */
class CubePositionRangeFilter {
	private Range[][] dimPosRanges;

	CubePositionRangeFilter(IDiskArray[] invalidDimPosArray) throws IOException {
		dimPosRanges = new Range[invalidDimPosArray.length][];
		for (int i = 0; i < invalidDimPosArray.length; i++) {
			dimPosRanges[i] = convertToRanges(invalidDimPosArray[i]);
		}
	}

	/**
	 * 
	 * @param dimPosArray
	 * @return
	 * @throws IOException
	 */
	private Range[] convertToRanges(IDiskArray dimPosArray) throws IOException {
		List rangeList = new ArrayList();
		int start;
		int last;
		Range range;

		start = ((Integer) dimPosArray.get(0)).intValue();
		last = start;
		for (int i = 1; i < dimPosArray.size(); i++) {
			int currentPos = ((Integer) dimPosArray.get(i)).intValue();
			if (currentPos != last + 1) {
				range = new Range(start, last);
				rangeList.add(range);
				start = currentPos;
			}
			last = currentPos;
		}
		range = new Range(start, last);
		rangeList.add(range);

		Range[] result = new Range[rangeList.size()];
		for (int i = 0; i < rangeList.size(); i++) {
			result[i] = (Range) rangeList.get(i);
		}
		return result;
	}

	/**
	 * 
	 * @param dimPositions
	 * @return
	 */
	boolean match(int[] dimPositions) {
		for (int i = 0; i < dimPosRanges.length; i++) {
			boolean match = false;
			for (int j = 0; j < dimPosRanges[i].length; j++) {
				if (dimPosRanges[i][j].match(dimPositions[i])) {
					match = true;
					break;
				}
			}
			if (!match)
				return false;
		}
		return true;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class Range {
	private int start;
	private int end;

	/**
	 * 
	 * @param start
	 * @param end
	 */
	Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * 
	 * @param iValue
	 * @return
	 */
	boolean match(int iValue) {
		return iValue <= end && iValue >= start;
	}
}