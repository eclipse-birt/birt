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

package org.eclipse.birt.data.engine.olap.driver;

import java.io.IOException;

import javax.olap.OLAPException;
import javax.olap.cursor.RowDataMetaData;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor;
import org.eclipse.birt.data.engine.olap.cursor.RowDataMetaDataImpl;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * A DimensionAxis represents an axis based on certain level. It provides
 * methods to point to current position, and get the value at this position.
 * 
 */
public class DimensionAxis {

	private IResultSetMetaData metaData;
	private IAggregationResultSet rs;
	private int dimAxisIndex, levelIndex;
	private IRowDataAccessor accessor;
	private ILevelDefinition levelDefintion;

	/**
	 * 
	 * @param container
	 * @param rs
	 * @param dimAxisIndex
	 * @param levelIndex
	 * @param attrIndex
	 */
	public DimensionAxis(IEdgeAxis container, IAggregationResultSet rs, int dimAxisIndex, int levelIndex) {
		this(container, rs, dimAxisIndex, levelIndex, false);
	}

	/**
	 * 
	 * @param container
	 * @param rs
	 * @param dimAixsIndex
	 * @param levelIndex
	 * @param attrIndex
	 */
	public DimensionAxis(IEdgeAxis container, IAggregationResultSet rs, int dimAixsIndex, int levelIndex,
			boolean isMirrored) {
		this.metaData = new ResultSetMetadata(rs, levelIndex);
		this.rs = rs;
		this.levelIndex = levelIndex;
		this.accessor = container.getRowDataAccessor();
		this.dimAxisIndex = dimAixsIndex;

	}

	/**
	 * 
	 * @return
	 */
	public int getLevelIndex() {
		return this.levelIndex;
	}

	/**
	 * 
	 * @return
	 */
	public IRowDataAccessor getRowDataAccessor() {
		return this.accessor;
	}

	/**
	 * Get dimension's metadata
	 * 
	 * @return
	 */
	public RowDataMetaData getRowDataMetaData() {
		return new RowDataMetaDataImpl(metaData);
	}

	/**
	 * Move cursor to the next row.Return false if the next row does not exist.
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean next() throws OLAPException {
		return this.accessor.dim_next(dimAxisIndex);
	}

	/**
	 * Moves cursor to previous row. Return false if the previous row does not exist
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean previous() throws OLAPException {
		return this.accessor.dim_previous(dimAxisIndex);
	}

	/**
	 * Moves cursor offset positions relative to current. Returns false if the
	 * indicated position does not exist
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean relative(int arg0) throws OLAPException {
		return this.accessor.dim_relative(arg0, dimAxisIndex);
	}

	/**
	 * Moves the cursor to the first row in the result set. Returns false if the
	 * result set is empty.
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean first() throws OLAPException {
		return this.accessor.dim_first(dimAxisIndex);
	}

	/**
	 * Moves cursor to last row. Returns false if the result set is empty
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean last() throws OLAPException {
		return this.accessor.dim_last(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBeforeFirst() {
		return this.accessor.dim_isBeforeFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isAfterLast() throws OLAPException {
		return this.accessor.dim_isAfterLast(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isFirst() throws OLAPException {
		return this.accessor.dim_isFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isLast() throws OLAPException {
		return this.accessor.dim_isLast(dimAxisIndex);
	}

	/**
	 * Moves the cursor to the end of the result set, just after the last row
	 * 
	 * @throws OLAPException
	 */
	public void afterLast() throws OLAPException {
		this.accessor.dim_afterLast(dimAxisIndex);
	}

	/**
	 * Moves the cursor to the front of the result set, just before the first row.
	 * 
	 * @throws OLAPException
	 */
	public void beforeFirst() throws OLAPException {
		this.accessor.dim_beforeFirst(dimAxisIndex);
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException
	 */
	public void setPosition(long position) throws OLAPException {
		this.accessor.dim_setPosition(dimAxisIndex, position);
	}

	/**
	 * Returns the cursor in current position.
	 * 
	 * @return the cursor in current position.
	 * @throws OLAPException
	 */
	public long getPosition() throws OLAPException {
		return this.accessor.dim_getPosition(dimAxisIndex);
	}

	/**
	 * Closes the result set and releases all resources.
	 * 
	 * @throws OLAPException
	 *
	 */
	public void close() throws OLAPException {
		try {
			this.rs.close();
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
	}

	/**
	 * Return the extend of this cursor
	 * 
	 * @return
	 */
	public long getExtend() {
		return this.accessor.getExtend(dimAxisIndex);
	}

	/**
	 * Returns the type of the cursor.
	 * 
	 * @return
	 */
	public int getType() {
		return 0;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public long getEdgeEnd() throws OLAPException {
		return this.accessor.getEdgeEnd(dimAxisIndex);
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public long getEdgeStart() throws OLAPException {
		return this.accessor.getEdgeStart(dimAxisIndex);
	}

	/**
	 * 
	 * @param attr
	 * @return
	 * @throws OLAPException
	 */
	public Object getCurrentMember(int attr) throws OLAPException {
		return this.accessor.dim_getCurrentMember(dimAxisIndex, attr);
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 * @throws OLAPException
	 */
	public Object getCurrentMember(String attrName) throws OLAPException {
		return this.accessor.dim_getCurrentMember(dimAxisIndex, attrName);
	}

	/**
	 * 
	 * @param edgeInfoUtil
	 */
	public void setEdgeInfo(IRowDataAccessor accessor) {
		this.accessor = accessor;
	}

	public void setLevelDefinition(ILevelDefinition level) {
		this.levelDefintion = level;
	}

	public ILevelDefinition getLevelDefinition() {
		return this.levelDefintion;
	}
}
