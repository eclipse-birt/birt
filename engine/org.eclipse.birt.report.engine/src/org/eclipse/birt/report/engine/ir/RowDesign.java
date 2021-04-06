/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Row used in GridItem and TableItem.
 * 
 * @see GridItemDesign
 * @see TableItemDesign
 */
///TODO: RowDesign is not a realy styled element. It only has a style, but has
// no other attributes.
public class RowDesign extends ReportItemDesign {
	/**
	 * cells in this row.
	 */
	protected ArrayList<CellDesign> cells = new ArrayList<CellDesign>();

	// TODO: this field should be removed
	protected boolean isStartOfGroup = false;

	protected boolean isRepeatable = true;

	/**
	 * @return the isStartOfGroup
	 */
	public boolean isStartOfGroup() {
		return isStartOfGroup;
	}

	/**
	 * @param isStartOfGroup the isStartOfGroup to set
	 */
	public void setStartOfGroup(boolean isStartOfGroup) {
		this.isStartOfGroup = isStartOfGroup;
	}

	public Collection<CellDesign> getCells() {
		return cells;
	}

	/**
	 * get cell count
	 * 
	 * @return cell count
	 */
	public int getCellCount() {
		return this.cells.size();
	}

	/**
	 * get Cell
	 * 
	 * @param index cell index
	 * @return cell
	 */
	public CellDesign getCell(int index) {
		return (CellDesign) this.cells.get(index);
	}

	/**
	 * append cell into the row.
	 * 
	 * @param cell cell to be added.
	 */
	public void addCell(CellDesign cell) {
		assert (cell != null);
		this.cells.add(cell);
		/*
		 * if (cell.getColumn() != -1) { for (int i = cells.size(); i <
		 * cell.getColumn(); i++) { this.cells.add(null); }
		 * this.cells.set(cell.getColumn()-1, cell); return; } else {
		 * this.cells.add(cell); }
		 */
	}

	public void removeCells() {
		this.cells.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.
	 * report.engine.ir.IReportItemVisitor)
	 */
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitRow(this, value);
	}

	public void setRepeatable(boolean repeatable) {
		isRepeatable = repeatable;
	}

	public boolean getRepeatable() {
		return isRepeatable;
	}
}
