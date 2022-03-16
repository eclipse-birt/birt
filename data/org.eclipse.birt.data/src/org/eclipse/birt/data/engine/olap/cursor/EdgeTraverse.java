/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.cursor;

/**
 * This class is to position along the edge cursor.
 */
class EdgeTraverse {
	int currentPosition, curPosOnMap;
	private EdgeDimensionRelation relationMap;
	private int edgeStart, edgeEnd, traverseLength;

	EdgeTraverse(EdgeDimensionRelation relationMap) {
		this.currentPosition = -1;
		this.relationMap = relationMap;
		this.edgeStart = 0;
		this.edgeEnd = relationMap.traverseLength - 1;
		this.traverseLength = this.relationMap.traverseLength;
	}

	EdgeTraverse(EdgeDimensionRelation relationMap, int edgeStart, int edgeEnd) {
		this(relationMap);
		this.edgeStart = edgeStart;
		this.edgeEnd = edgeEnd;
		this.traverseLength = edgeEnd - edgeStart + 1;
	}

	/**
	 * Move the cursor to the end of the edge, just after the last row.
	 */
	void afterLast() {
		this.curPosOnMap = edgeEnd + 1;
		this.currentPosition = this.traverseLength;
	}

	/**
	 * Move the cursor before the first row of the edge.
	 */
	void beforeFirst() {
		this.curPosOnMap = edgeStart - 1;
		this.currentPosition = -1;
	}

	/**
	 * Move the cursor to the first row of the edge.
	 *
	 * @return
	 */
	boolean first() {
		if (this.traverseLength > 0) {
			this.currentPosition = 0;
			this.curPosOnMap = edgeStart;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get the current position of edge cursor.
	 *
	 * @return
	 */
	int getEdgePostion() {
		return this.currentPosition;
	}

	/**
	 * indicate whether the cursor move after the last row.
	 *
	 * @return
	 */
	boolean isAfterLast() {
		return this.currentPosition >= this.traverseLength;
	}

	/**
	 * indicate whether the cursor move before the first row
	 *
	 * @return
	 */
	boolean isBeforeFirst() {
		return this.currentPosition < 0;
	}

	/**
	 * indicate whether the cursor move to the first row
	 *
	 * @return
	 */
	boolean isFirst() {
		return this.currentPosition == 0;
	}

	/**
	 * indicate whether the cursor move to the last row
	 *
	 * @return
	 */
	boolean isLast() {
		return this.currentPosition == this.traverseLength - 1;
	}

	/**
	 * move the cursor to the last row.
	 *
	 * @return
	 */
	boolean last() {
		if (this.relationMap.traverseLength > 0) {
			this.currentPosition = this.traverseLength - 1;
			this.curPosOnMap = this.edgeEnd - 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * move the cursor to the next row.
	 *
	 * @return
	 */
	boolean next() {
		this.currentPosition++;
		this.curPosOnMap++;
		if (currentPosition < this.traverseLength) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * move the cursor to the previous row.
	 *
	 * @return
	 */
	boolean previous() {
		this.currentPosition--;
		this.curPosOnMap--;
		if (currentPosition >= 0) {
			return true;
		} else {
			currentPosition = -1;
			return false;
		}
	}

	/**
	 * move the cursor to the relative offset.
	 *
	 * @param arg0
	 * @return
	 */
	boolean relative(int arg0) {
		if (arg0 == 0) {
			return true;
		}
		int position = this.currentPosition + arg0;
		if (position >= this.relationMap.traverseLength) {
			return false;
		} else if (position < 0) {
			this.currentPosition = -1;
			this.curPosOnMap = edgeStart - 1;
			return false;
		} else {
			this.curPosOnMap = this.curPosOnMap + arg0;
			this.currentPosition = position;
			return true;
		}
	}
}
