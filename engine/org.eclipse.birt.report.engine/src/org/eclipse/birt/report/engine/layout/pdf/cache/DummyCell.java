package org.eclipse.birt.report.engine.layout.pdf.cache;

/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;

public class DummyCell extends CellArea {
	protected CellArea cell;

	protected int colSpan;

	/**
	 * For the first dummy cell, delta = refCellHeight - lastRowHeight For the
	 * subsequent dummy cell, delta = upperDummyCellDelta - lastRowHeight
	 */
	protected int delta;

	public DummyCell(CellArea cell) {
		this.cell = cell;
	}

	@Override
	public IContent getContent() {
		return cell.getContent();
	}

	public CellArea getCell() {
		return cell;
	}

	@Override
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	@Override
	public int getRowSpan() {
		return rowSpan;
	}

	@Override
	public int getColumnID() {
		return cell.getColumnID();
	}

	@Override
	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

}
