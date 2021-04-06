package org.eclipse.birt.report.engine.layout.pdf.cache;

/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IContent getContent() {
		return cell.getContent();
	}

	public CellArea getCell() {
		return cell;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public int getColumnID() {
		return cell.getColumnID();
	}

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
