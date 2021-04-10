/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a cell
 */
public class CellInstance extends ReportElementInstance implements ICellInstance {

	private ICellContent cell;

	// comment off the variable fromGrid to fix a warning of bug 161814.
	// private boolean fromGrid;

	public CellInstance(ICellContent cell, ExecutionContext context, RunningState runningState, boolean fromGrid) {
		super(cell, context, runningState);
		this.cell = cell;
		// comment off the variable fromGrid to fix a warning of bug 161814.
		// this.fromGrid = fromGrid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#getColSpan()
	 */
	public int getColSpan() {
		return cell.getColSpan();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#setColSpan(
	 * int)
	 */
	public void setColSpan(int colSpan) {
		cell.setColSpan(colSpan);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#getRowSpan()
	 */
	public int getRowSpan() {
		return cell.getRowSpan();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#setRowSpan(
	 * int)
	 */
	public void setRowSpan(int rowSpan) {
		cell.setRowSpan(rowSpan);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#getColumn()
	 */
	public int getColumn() {
		return cell.getColumn();
	}

	public void setHeight(String height) {
		throw new UnsupportedOperationException("Can not set cell height, please set row height instead.");
	}

	public void setWidth(String width) {
		throw new UnsupportedOperationException("Can not set cell width, please set column width instead.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ICellInstance#getData()
	 */
	/*
	 * bug 135790 public Object getData( ) throws ScriptException { if (data ==
	 * null) return null; //TODO bug124760: getColumn both the first cell both in a
	 * grid and in a table return 0 //not the follows: ////TODO: This is beacuse
	 * getColumn from the first cell in a grid returns 0 ////and getColumn in the
	 * first cell in a table returns 1 if ( fromGrid ) return
	 * data.getExpressionValue( getColumn( ) + 1 ); return data.getExpressionValue(
	 * getColumn( ) + 1 ); }
	 */
}
