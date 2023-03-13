/*******************************************************************************
 * Copyright (c) 2004 , 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Grid Item.
 *
 * Grid Item is static table, which contains a
 *
 * column define, serveral rows. and each row contains several cells(maximum to
 * column count defined in column define).
 *
 */
public class GridItemDesign extends ReportItemDesign {

	/**
	 * grid caption
	 */
	protected String caption;

	/*
	 * grid caption resource key
	 */
	protected String captionKey;

	/*
	 * grid summary
	 */
	protected String summary;

	/**
	 * column defines. the item type is Column.
	 */
	protected ArrayList<ColumnDesign> columns = new ArrayList<>();
	/**
	 * rows. the item type is Row.
	 *
	 * @see RowDesign
	 */
	protected ArrayList<RowDesign> rows = new ArrayList<>();

	/**
	 * add column into the column define.
	 *
	 * @param column column to be added.
	 */
	public void addColumn(ColumnDesign column) {
		assert (column != null);
		this.columns.add(column);
	}

	public Collection<ColumnDesign> getColumns() {
		return columns;
	}

	/**
	 * get column count.
	 *
	 * @return count of the column.
	 */
	public int getColumnCount() {
		return this.columns.size();
	}

	/**
	 * get column defines. the index is not the order of addColumn. It is the actual
	 * column defines(repeated by colum.repeat).
	 *
	 * @param index index of the column.
	 * @return column define.
	 */
	public ColumnDesign getColumn(int index) {
		assert (index >= 0 && index < this.columns.size());
		return (ColumnDesign) this.columns.get(index);
	}

	/**
	 * add a row into the grid.
	 *
	 * @param row
	 */
	public void addRow(RowDesign row) {
		assert (row != null);
		this.rows.add(row);
	}

	public Collection<RowDesign> getRows() {
		return rows;
	}

	/**
	 * get the row number.
	 *
	 * @return row number
	 */
	public int getRowCount() {
		return this.rows.size();
	}

	/**
	 * get the row.
	 *
	 * @param index index of the row.
	 * @return row.
	 */
	public RowDesign getRow(int index) {
		assert (index >= 0 && index < rows.size());
		return (RowDesign) this.rows.get(index);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.ir.ReportItem#accept(org.eclipse.birt.report.
	 * engine.ir.ReportItemVisitor)
	 */
	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitGridItem(this, value);
	}

	/**
	 * set grid caption
	 *
	 * @param captionKey resource key
	 * @param caption    caption
	 */
	public void setCaption(String captionKey, String caption) {
		this.captionKey = captionKey;
		this.caption = caption;

	}

	/**
	 * @returns Return grid caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @returns Return grid captionKey
	 */
	public String getCaptionKey() {
		return captionKey;
	}

	/**
	 * set grid summary
	 *
	 * @param summary summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * get grid summary
	 *
	 * @returns Return grid summary
	 */
	public String getSummary() {
		return summary;
	}
}
