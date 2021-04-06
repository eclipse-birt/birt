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
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.List;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class is to populate the relationship between dimension and its
 * belonging edge cursor. It also provide the method to navigate on
 * edge/dimension cursor. Changing the position of an EdgeCursor affects both
 * the values of the dimension cursor of the edgeCursor and the value of the
 * data of the cubeCursor.
 * 
 */
public class RowDataAccessor implements IRowDataAccessor {

	// result set for this edge
	private IAggregationResultSet rs;
	// the dimension axis on this edge
	protected DimensionAxis[] dimAxis;
	protected DimensionTraverse dimTraverse;
	protected EdgeTraverse edgeTraverse;
	protected EdgeDimensionRelation edgeDimensRelation;
	private RowDataAccessorService service;

	/**
	 * 
	 * @param resultSet
	 * @param axis
	 * @throws IOException
	 */
	public RowDataAccessor(RowDataAccessorService service, IAggregationResultSet rs) {
		if (service.getDimensionAxis().length == 0)
			return;
		this.service = service;
		this.rs = rs;
		this.dimAxis = service.getDimensionAxis();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor#
	 * getAggregationResultSet()
	 */
	public IAggregationResultSet getAggregationResultSet() {
		return this.rs;
	}

	/**
	 * Populate edgeInfo, EdgeInfo represents the startPosition and endPosition to
	 * its children. Here, it will distinguish the non-mirrored level and mirrored
	 * level. Only non-mirrored level will generate its EdgeInfo
	 * 
	 * @param isCalculatedMember
	 * @throws IOException
	 */
	public void initialize(boolean isPage) throws IOException {
		ResultSetFetcher fetcher = new ResultSetFetcher(this.rs);

		edgeDimensRelation = new EdgeDimensionRelation(service, fetcher, isPage);
		dimTraverse = new DimensionTraverse(dimAxis, edgeDimensRelation);
		edgeTraverse = new EdgeTraverse(edgeDimensRelation);
	}

	/**
	 * Move certain dimension cursor to the next row.Return false if the next row
	 * does not exist.
	 * 
	 * @param dimAxisIndex certain dimension cursor
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_next(int dimAxisIndex) throws OLAPException {
		return dimTraverse.next(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_previous(int dimAxisIndex) throws OLAPException {
		return dimTraverse.previous(dimAxisIndex);
	}

	/**
	 * 
	 * @param offset
	 * @param dimAxisIndex
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_relative(int offset, int dimAxisIndex) throws OLAPException {
		return dimTraverse.relative(offset, dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_first(int dimAxisIndex) {
		return dimTraverse.first(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_last(int dimAxisIndex) {
		return dimTraverse.last(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isBeforeFirst(int dimAxisIndex) {
		return dimTraverse.isBeforeFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isAfterLast(int dimAxisIndex) {
		return dimTraverse.isAfterLast(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isFirst(int dimAxisIndex) {
		return dimTraverse.isFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isLast(int dimAxisIndex) {
		return dimTraverse.isLast(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 */
	public void dim_afterLast(int dimAxisIndex) {
		dimTraverse.afterLast(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 */
	public void dim_beforeFirst(int dimAxisIndex) {
		dimTraverse.beforeFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param position
	 */
	public void dim_setPosition(int dimAxisIndex, long position) {
		dimTraverse.setPosition(dimAxisIndex, position);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public long dim_getPosition(int dimAxisIndex) {
		return dimTraverse.getPosition(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param attr
	 * @return
	 * @throws OLAPException
	 */
	public Object dim_getCurrentMember(int dimAxisIndex, int attr) throws OLAPException {
		try {
			int position = dimTraverse.getCurrentRowPosition(dimAxisIndex);
			if (position == -1) {
				throw new OLAPException(ResourceConstants.RD_GET_LEVEL_MEMBER_ERROR);
			}
			rs.seek(position);
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
		return this.dimTraverse.getCurrentMember(dimAxisIndex, attr);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param attrName
	 * @return
	 * @throws OLAPException
	 */
	public Object dim_getCurrentMember(int dimAxisIndex, String attrName) throws OLAPException {

		try {
			int position = dimTraverse.getCurrentRowPosition(dimAxisIndex);
			if (position == -1) {
				throw new OLAPException(ResourceConstants.RD_GET_LEVEL_MEMBER_ERROR);
			}
			rs.seek(position);
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
		return this.dimTraverse.getCurrentMember(dimAxisIndex, attrName);
	}

	/**
	 * 
	 */
	public void edge_afterLast() {
		this.edgeTraverse.afterLast();
		int[] lastDimLength = this.getLastDiemsionLength();
		for (int i = 0; i < this.dimAxis.length; i++) {
			this.dimTraverse.dimensionCursorPosition[i] = lastDimLength[i];
		}
	}

	/**
	 * 
	 */
	public void edge_beforeFirst() {
		this.edgeTraverse.beforeFirst();
		this.dimTraverse.beforeFirst();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_first() {
		if (this.edgeTraverse.first()) {
			this.dimTraverse.first();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public long getEdgePostion() {
		if (this.edgeTraverse == null) {
			return -1;
		}
		return this.edgeTraverse.getEdgePostion();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isAfterLast() {
		return this.edgeTraverse.isAfterLast();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isBeforeFirst() {
		return this.edgeTraverse.isBeforeFirst();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isFirst() {
		return this.edgeTraverse.isFirst();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isLast() {
		return this.edgeTraverse.isLast();
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_last() {
		if (this.edgeTraverse.last()) {
			int[] lastDimLength = getLastDiemsionLength();
			for (int i = 0; i < this.dimAxis.length; i++) {
				this.dimTraverse.dimensionCursorPosition[i] = lastDimLength[i] - 1;
			}
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean edge_next() throws OLAPException {
		if (!this.edgeTraverse.next()) {
			this.edge_afterLast();
			return false;
		}
		if (this.dimTraverse.isInitialStatus()) {
			for (int i = this.dimAxis.length - 1; i >= 0; i--) {
				this.dimTraverse.first(i);
			}
		} else
			for (int i = this.dimAxis.length - 1; i >= 0; i--) {
				if (this.dimTraverse.next(i)) {
					break;
				} else {
					this.dimTraverse.first(i);
				}
			}
		return true;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean edge_previous() throws OLAPException {
		this.edgeTraverse.previous();
		if (this.edgeTraverse.currentPosition >= this.edgeDimensRelation.traverseLength - 1) {
			for (int i = 0; i < this.dimAxis.length; i++) {
				this.dimTraverse.previous(i);
			}
			return true;
		} else if (this.edgeTraverse.currentPosition >= 0) {
			for (int i = this.dimAxis.length - 1; i >= 0; i--) {
				if (this.dimTraverse.previous(i)) {
					for (int k = i + 1; k < this.dimAxis.length; k++) {
						this.dimTraverse.last(k);
					}
					break;
				} else {
					this.dimTraverse.first(i);
				}
			}
			return true;
		} else {
			this.edgeTraverse.currentPosition = -1;
			this.dimTraverse.beforeFirst();
			return false;
		}
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean edge_relative(int arg0) throws OLAPException {
		if (arg0 == 0)
			return true;
		int position = this.edgeTraverse.currentPosition + arg0;
		if (position >= this.edgeDimensRelation.traverseLength) {
			this.edge_afterLast();
			return false;
		} else if (position < 0) {
			this.dimTraverse.beforeFirst();
			this.edgeTraverse.currentPosition = -1;
			return false;
		} else {
			this.edgeTraverse.currentPosition = position;
			adjustDimensionPosition(position);

			return true;
		}
	}

	private void adjustDimensionPosition(int position) {
		int index = position;
		for (int i = dimAxis.length - 1; i >= 0; i--) {
			List edgeInfoList = this.edgeDimensRelation.currentRelation[i];
			EdgeInfo edgeInfo = (EdgeInfo) edgeInfoList.get(index);
			int currentPosition = index;
			int dimPosition = 0;
			while (--currentPosition >= 0) {
				EdgeInfo previousInfo = (EdgeInfo) edgeInfoList.get(currentPosition);
				if (previousInfo.parent == edgeInfo.parent) {
					dimPosition++;
				} else
					break;
			}
			this.dimTraverse.setPosition(i, dimPosition);
			index = edgeInfo.parent;
		}
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException
	 * @throws OLAPException
	 */
	public void edge_setPostion(long position) throws OLAPException {
		if (position < 0) {
			this.dimTraverse.beforeFirst();
			this.edgeTraverse.currentPosition = -1;
			return;
		}
		int offSet = (int) position - this.edgeTraverse.currentPosition;
		this.edge_relative(offSet);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public int getEdgeStart(int dimAxisIndex) {
		return this.dimTraverse.getEdgeStart(dimAxisIndex);
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public int getEdgeEnd(int dimAxisIndex) {
		return this.dimTraverse.getEdgeEnd(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 */
	private int[] getLastDiemsionLength() {
		int[] lastDimensionLength = new int[this.dimAxis.length];
		for (int i = 0; i < this.dimAxis.length; i++) {
			lastDimensionLength[i] = this.getRangeInLastDimension(i);
		}
		return lastDimensionLength;
	}

	/**
	 * 
	 * @param dimIndex
	 * @return
	 */
	private int getRangeInLastDimension(int dimIndex) {
		if (dimIndex == 0)
			return this.edgeDimensRelation.currentRelation[0].size();
		int size = this.edgeDimensRelation.currentRelation[dimIndex].size();
		if (size == 0)
			return -1;
		int count = 1;
		EdgeInfo edgeInfo = (EdgeInfo) this.edgeDimensRelation.currentRelation[dimIndex].get(size - 1);
		EdgeInfo previousInfo;
		for (int i = size - 2; i >= 0; i--) {
			previousInfo = (EdgeInfo) this.edgeDimensRelation.currentRelation[dimIndex].get(i);
			if (previousInfo.parent == edgeInfo.parent)
				count++;
		}
		return count;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor#sychronizedWithPage
	 * (int)
	 */
	public void sychronizedWithPage(int position) {
		this.edgeDimensRelation.synchronizedWithPage(position);
		this.dimTraverse = new DimensionTraverse(this.service.getDimensionAxis(), this.edgeDimensRelation);
		this.edgeTraverse = new EdgeTraverse(this.edgeDimensRelation);
		this.edge_beforeFirst();
	}

	public RowDataAccessorService getRowDataAccessorService() {
		return this.service;
	}

	public int getExtend(int dimAxisIndex) {
		return this.dimTraverse.getExtend(dimAxisIndex);
	}
}
