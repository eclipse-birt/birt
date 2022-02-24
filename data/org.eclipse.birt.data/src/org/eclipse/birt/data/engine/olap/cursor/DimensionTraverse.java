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

import java.util.List;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class is to position on dimension cursor.
 *
 */
class DimensionTraverse {
	private DimensionAxis[] dimAxis;
	private EdgeDimensionRelation relationMap;
	private ResultSetFetcher fetcher;
	int[] dimensionCursorPosition;
	private int edgeStart;

	/**
	 *
	 * @param axis
	 * @param relationMap
	 * @param fetcher
	 */
	DimensionTraverse(DimensionAxis[] axis, EdgeDimensionRelation relationMap) {
		this.dimAxis = axis;
		this.relationMap = relationMap;
		this.dimensionCursorPosition = new int[dimAxis.length];
		this.beforeFirst();
		this.fetcher = relationMap.fetcher;
		dimensionCursorPosition = findDimensionPosition(-1);
		this.edgeStart = 0;
	}

	/**
	 *
	 * @param axis
	 * @param relationMap
	 * @param fetcher
	 */
	DimensionTraverse(DimensionAxis[] axis, EdgeDimensionRelation relationMap, int edgeStart, int edgeEnd) {
		this.dimAxis = axis;
		this.relationMap = relationMap;
		this.dimensionCursorPosition = new int[dimAxis.length];
		this.beforeFirst();
		this.fetcher = relationMap.fetcher;
		this.edgeStart = edgeStart;
		dimensionCursorPosition = findDimensionPosition(edgeStart - 1);
	}

	/**
	 *
	 * @param currentPosition
	 * @return
	 */
	private int[] findDimensionPosition(int currentPosition) {
		int position = currentPosition;
		int[] pos = new int[this.dimAxis.length];
		int count = 0;
		for (int i = pos.length - 1; i >= 0; i--) {
			count = 0;
			if (position < 0) {
				pos[i] = -1;
				continue;
			}

			EdgeInfo info = null;
			if (this.relationMap.currentRelation[i].size() > position) {
				info = (EdgeInfo) this.relationMap.currentRelation[i].get(position);
			} else {
				pos[i] = -1;
				continue;
			}
			for (int j = position - 1; j >= 0; j--) {
				EdgeInfo lastInfo = (EdgeInfo) this.relationMap.currentRelation[i].get(j);
				if (info.parent == lastInfo.parent) {
					count++;
				} else {

					break;
				}
			}
			position = info.parent;
			pos[i] = count;

		}
		return pos;
	}

	/**
	 * move the certain index dimension cursor to the next position.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean next(int dimAxisIndex) {
		if (hasNext(dimAxisIndex)) {
			this.dimensionCursorPosition[dimAxisIndex]++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * move the certain dimension cursor to the previous position.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean previous(int dimAxisIndex) {
		int position = this.dimensionCursorPosition[dimAxisIndex];
		position--;

		if (position >= 0) {
			this.dimensionCursorPosition[dimAxisIndex] = position;
			return true;
		} else {
			this.dimensionCursorPosition[dimAxisIndex] = -1;
			return false;
		}
	}

	/**
	 * move the certain dimension cursor to the relative offet.
	 *
	 * @param offset
	 * @param dimAxisIndex
	 * @return
	 */
	boolean relative(int offset, int dimAxisIndex) {
		int index = offset;

		if (offset > 0) {
			for (; index != 0; index--) {
				if (this.next(dimAxisIndex)) {
					continue;
				} else {
					break;
				}
			}
		} else {
			for (; index != 0; index++) {
				if (this.previous(dimAxisIndex)) {
					continue;
				} else {
					break;
				}
			}
		}

		if (index == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * position the certain dimension cursor to the first row.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean first(int dimAxisIndex) {
		if (this.relationMap.traverseLength > 0) {
			this.dimensionCursorPosition[dimAxisIndex] = 0;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * position the certain dimension cursor to the last row.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean last(int dimAxisIndex) {
		if (this.relationMap.traverseLength <= 0) {
			return false;
		}

		int range = findFowardOffsetRange(dimAxisIndex);
		this.dimensionCursorPosition[dimAxisIndex] += range;
		return true;
	}

	/**
	 * indicate whether the certain dimension is before the first row.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean isBeforeFirst(int dimAxisIndex) {
		return this.dimensionCursorPosition[dimAxisIndex] < 0;
	}

	/**
	 * indicate whether the certain dimension is after the last row.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean isAfterLast(int dimAxisIndex) {
		int offset = this.findFowardOffsetRange(dimAxisIndex);
		return offset < 0;
	}

	/**
	 * indicate whether the certain dimension cursor is first position.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean isFirst(int dimAxisIndex) {
		return this.dimensionCursorPosition[dimAxisIndex] == 0;
	}

	/**
	 * indicate whether the certain dimension cursor is last position.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	boolean isLast(int dimAxisIndex) {
		int offset = this.findFowardOffsetRange(dimAxisIndex);
		return offset == 0;
	}

	/**
	 * move the certain dimension cursor after the last position.
	 *
	 * @param dimAxisIndex
	 */
	void afterLast(int dimAxisIndex) {
		int offset = this.findFowardOffsetRange(dimAxisIndex);
		this.dimensionCursorPosition[dimAxisIndex] += offset + 1;
	}

	/**
	 * move the certain dimension cursor before the first position.
	 *
	 * @param dimAxisIndex
	 */
	void beforeFirst(int dimAxisIndex) {
		this.dimensionCursorPosition[dimAxisIndex] = -1;
	}

	/**
	 * set the position for the certain dimension.
	 *
	 * @param dimAxisIndex
	 * @param position
	 */
	void setPosition(int dimAxisIndex, long position) {
		this.dimensionCursorPosition[dimAxisIndex] = (int) position;
	}

	/**
	 * get the position for the certain dimension.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	long getPosition(int dimAxisIndex) {
		return this.dimensionCursorPosition[dimAxisIndex];
	}

	/**
	 * get current row position for the certain dimension according the the position
	 * value of all dimensions.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	int getCurrentRowPosition(int dimAxisIndex) {
		EdgeInfo currentEdgeInfo = this.findCurrentEdgeInfo(dimAxisIndex);
		if (currentEdgeInfo == null) {
			return -1;
		}
		EdgeInfo outerMost = findOuterMostChildEdgeInfo(dimAxisIndex, currentEdgeInfo);

		return outerMost.firstChild;
	}

	/**
	 * get the start row index for the certain dimension.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	int getEdgeStart(int dimAxisIndex) {
		if (this.dimensionCursorPosition[dimAxisIndex] < 0
				|| this.dimensionCursorPosition[dimAxisIndex] > this.relationMap.traverseLength) {
			return -1;
		}

		int outer = this.dimAxis.length;
		EdgeInfo edgeInfo = findCurrentEdgeInfo(dimAxisIndex);
		if (edgeInfo == null) {
			return -1;
//		if( edgeInfo.isNull ==0 )
//			return -1;
		}

		int endPosition = edgeInfo.firstChild, position;
		EdgeInfo info = edgeInfo;
		for (position = dimAxisIndex + 1; position < outer; position++) {
			info = (EdgeInfo) ((List) this.relationMap.currentRelation[position]).get(endPosition);
			endPosition = info.firstChild;
		}
		return info.firstChild;
	}

	/**
	 * get the end row index for the certain dimension cursor.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	int getEdgeEnd(int dimAxisIndex) {
		if (this.dimensionCursorPosition[dimAxisIndex] < 0
				|| this.dimensionCursorPosition[dimAxisIndex] > this.relationMap.traverseLength) {
			return -1;
		}

		EdgeInfo edgeInfo = findCurrentEdgeInfo(dimAxisIndex);
		if (edgeInfo == null) {
			return -1;
//		if( edgeInfo.isNull ==0 )
//			return -1;
		}

		int endPosition = edgeInfo.firstChild;

		int index = this.relationMap.currentRelation[dimAxisIndex].indexOf(edgeInfo);
		if (index < this.relationMap.currentRelation[dimAxisIndex].size() - 1) {
			EdgeInfo nextEdgeInfo = (EdgeInfo) this.relationMap.currentRelation[dimAxisIndex].get(index + 1);
			EdgeInfo nextOuterEdgeInfo = this.findOuterMostChildEdgeInfo(dimAxisIndex, nextEdgeInfo);
			endPosition = nextOuterEdgeInfo.firstChild - 1;
		} else {
			endPosition = this.relationMap.traverseLength - 1;
		}
		return endPosition;
	}

	/**
	 * reset the dimension position to the initial status.
	 */
	void beforeFirst() {
		this.dimensionCursorPosition = this.findDimensionPosition(edgeStart - 1);
	}

	void first() {
		this.dimensionCursorPosition = this.findDimensionPosition(edgeStart);
	}

	/**
	 * indicate whether the dimension cursors is in initial status.
	 *
	 * @return
	 */
	boolean isInitialStatus() {
		for (int i = 0; i < this.dimensionCursorPosition.length; i++) {
			if (this.dimensionCursorPosition[i] >= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param dimAxisIndex
	 * @param attr
	 * @return
	 */
	Object getCurrentMember(int dimAxisIndex, int attr) {
		return this.fetcher.getValue(dimAxis[dimAxisIndex].getLevelIndex(), attr);
	}

	/**
	 *
	 * @param dimAxisIndex
	 * @param attrName
	 * @return
	 * @throws OLAPException
	 */
	Object getCurrentMember(int dimAxisIndex, String attrName) throws OLAPException {
		int attrIndex = fetcher.getAttributeIndex(dimAxis[dimAxisIndex].getLevelIndex(), attrName);
		if (attrIndex == -1) {
			throw new OLAPException(ResourceConstants.INVALID_LEVEL_ATTRIBUTE + attrName);
		}
		return fetcher.getValue(dimAxis[dimAxisIndex].getLevelIndex(), attrIndex);
	}

	/**
	 *
	 * @param dimAxisIndex
	 * @param edgeInfo
	 * @return
	 */
	private EdgeInfo findOuterMostChildEdgeInfo(int dimAxisIndex, EdgeInfo edgeInfo) {
		if (dimAxisIndex < 0 || dimAxisIndex >= this.dimAxis.length || edgeInfo == null) {
			return null;
		}
		int endPosition = edgeInfo.firstChild;
		EdgeInfo info = edgeInfo;
		for (int i = dimAxisIndex + 1; i < this.dimAxis.length; i++) {
			info = (EdgeInfo) ((List) this.relationMap.currentRelation[i]).get(endPosition);
			endPosition = info.firstChild;
		}
		return info;
	}

	/**
	 * Check whether the cursor on dimAxisIndex has next row.
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	private boolean hasNext(int dimAxisIndex) {
		EdgeInfo currentEdgeInfo = findCurrentEdgeInfo(dimAxisIndex);
		// if current EdgeInfo is null, but the cursor is not initial,
		// return true.
		// else if current EdgeInfo is null, return false
		if (currentEdgeInfo == null) {
			if (this.dimensionCursorPosition[dimAxisIndex] < 0 && this.relationMap.traverseLength > 0) {
				return true;
			} else {
				return false;
			}
		}

		int index = this.relationMap.currentRelation[dimAxisIndex].indexOf(currentEdgeInfo);
		EdgeInfo nextEdgeInfo = null;
		if (this.relationMap.currentRelation[dimAxisIndex].size() > index + 1) {
			nextEdgeInfo = (EdgeInfo) this.relationMap.currentRelation[dimAxisIndex].get(index + 1);
		}

		if (nextEdgeInfo == null) {
			return false;
		}

		if (currentEdgeInfo.parent == nextEdgeInfo.parent) {
			return true;
		}
		return false;
	}

	/**
	 * Based on current dimensionCursor position, get current edge info for certain
	 * dimensionAxis.
	 *
	 * @param dimensionAxis
	 * @return
	 */
	EdgeInfo findCurrentEdgeInfo(int dimensionAxis) {
		if (dimensionAxis < 0 || dimensionAxis > this.dimAxis.length) {
			return null;
		}

		EdgeInfo tempEdgeInfo1 = null, tempEdgeInfo2 = null, edgeInfo = null;
		int endPosition = 0;
		for (int index = 0; index <= dimensionAxis; index++) {
			if (dimensionAxis == index) {

				if (this.relationMap.currentRelation[index].size() > endPosition
						&& this.dimensionCursorPosition[index] > -1
						&& this.dimensionCursorPosition[index] + endPosition < this.relationMap.currentRelation[index]
								.size()
						&& (tempEdgeInfo2 == null
								|| this.dimensionCursorPosition[index] + endPosition < tempEdgeInfo2.firstChild)) {
					edgeInfo = (EdgeInfo) this.relationMap.currentRelation[index]
							.get(this.dimensionCursorPosition[index] + endPosition);
				} else if (this.dimensionCursorPosition[index] == -1) {
					return null;
				}
				break;
			} else {
				if (this.dimensionCursorPosition[index] + endPosition < this.relationMap.currentRelation[index].size()
						&& this.dimensionCursorPosition[index] > -1
						&& this.relationMap.currentRelation[index].size() > endPosition) {
					tempEdgeInfo1 = (EdgeInfo) this.relationMap.currentRelation[index]
							.get(this.dimensionCursorPosition[index] + endPosition);
					if (this.dimensionCursorPosition[index] + endPosition + 1 < this.relationMap.currentRelation[index]
							.size()) {
						tempEdgeInfo2 = (EdgeInfo) this.relationMap.currentRelation[index]
								.get(this.dimensionCursorPosition[index] + endPosition + 1);
					}
				} else {
					return null;
				}
				if (tempEdgeInfo1 != null) {
					endPosition = tempEdgeInfo1.firstChild;
				}
			}
		}
		return edgeInfo;
	}

	/**
	 *
	 * @param dimensionAxis
	 * @return
	 */
	private int findFowardOffsetRange(int dimensionAxis) {
		int range = -1;
		if (dimensionAxis < 0 || dimensionAxis > this.dimAxis.length) {
			return range;
		}

		if (dimensionAxis == 0) {
			if (this.dimensionCursorPosition[0] < this.relationMap.currentRelation[0].size()) {
				return this.relationMap.currentRelation[0].size() - this.dimensionCursorPosition[dimensionAxis] - 1;
			} else {
				return range;
			}
		}

		EdgeInfo currentInfo = this.findCurrentEdgeInfo(dimensionAxis);
		if (currentInfo == null) {
			return range;
		} else {
			range = 0;
			while (true) {
				int index = this.relationMap.currentRelation[dimensionAxis].indexOf(currentInfo);
				EdgeInfo nextEdgeInfo = null;
				if (this.relationMap.currentRelation[dimensionAxis].size() > index + 1) {
					nextEdgeInfo = (EdgeInfo) this.relationMap.currentRelation[dimensionAxis].get(index + 1);
				} else {
					break;
				}
				if (nextEdgeInfo != null && currentInfo.parent == nextEdgeInfo.parent) {
					range++;
					currentInfo = nextEdgeInfo;
				} else {
					break;
				}
			}
		}
		return range;
	}

	/**
	 * -1 indicate that this is a drilled member
	 *
	 * @param dimAxisIndex
	 * @return
	 */
	public int getExtend(int dimAxisIndex) {
		EdgeInfo edgeInfo = findCurrentEdgeInfo(dimAxisIndex);
		if (edgeInfo.isNull == 0) {
			return -1;
		} else {
			return 0;
		}
	}
}
