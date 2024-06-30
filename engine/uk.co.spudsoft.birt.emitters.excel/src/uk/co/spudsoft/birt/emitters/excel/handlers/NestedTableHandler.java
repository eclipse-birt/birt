/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.handlers;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;

import uk.co.spudsoft.birt.emitters.excel.Coordinate;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class NestedTableHandler extends AbstractRealTableHandler {

	boolean inserted = false;

	private Coordinate topLeft;
	private Coordinate bottomRight;
	private int parentRowSpan;

	@Override
	public String toString() {
		return "NestedTableHandler [topLeft=" + topLeft + ", bottomRight=" + bottomRight + ", parentRowSpan="
				+ parentRowSpan + "]";
	}

	public NestedTableHandler(Logger log, IHandler parent, ITableContent table, int parentRowSpan) {
		super(log, parent, table);
		this.parentRowSpan = parentRowSpan;
	}

	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}

	public boolean includesRow(int rowNum) {
		return ((topLeft.getRow() <= rowNum) && (bottomRight.getRow() >= rowNum));
	}

	public int extendParentsRowBy(int rowNum) {
		if (rowNum == topLeft.getRow() + parentRowSpan - 1) {
			if (bottomRight.getRow() - topLeft.getRow() >= parentRowSpan - 1) {
				int extension = 2 + bottomRight.getRow() - topLeft.getRow() - parentRowSpan;
				log.debug("Nested table ", this, " extends row ", rowNum, " by ", extension);
				return extension;
			}
		}
		return 1;
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		--state.colNum;
		topLeft = new Coordinate(state.rowNum, state.colNum);
		log.debug("startTable called with topLeft = [", topLeft.getRow(), ", ", topLeft.getCol(), "]");
		super.startTable(state, table);
		if ((state.sheetName == null) || state.sheetName.isEmpty()) {
			String name = state.correctSheetName(table.getName());
			if ((name != null) && !name.isEmpty()) {
				state.sheetName = name;
			}
		}
		if ((state.sheetPassword == null) || state.sheetPassword.isEmpty()) {
			String password = EmitterServices.stringOption(state.getRenderOptions(), table, ExcelEmitter.SHEET_PASSWORD,
					null);
			if ((password != null) && !password.isEmpty()) {
				state.sheetPassword = password;
			}
		}
	}

	@Override
	public void startRow(HandlerState state, IRowContent row) throws BirtException {
		log.debug("startRow called with topLeft = [", topLeft.getRow(), ", ", topLeft.getCol(), "]");
		NestedTableRowHandler rowHandler = new NestedTableRowHandler(log, this, row, topLeft.getCol());

		state.setHandler(rowHandler);
		state.getHandler().startRow(state, row);
	}

	@Override
	public void endTable(HandlerState state, ITableContent table) throws BirtException {

		bottomRight = new Coordinate(state.rowNum - 1, state.colNum);

		super.endTable(state, table);

		// Parent could be a ListHandler (all derive from TopLevelListHandler) or a
		// CellHandler
		// If it's a cell handler we need to undo the rowNum increment from the last
		// nested table row
		if (!(parent instanceof TopLevelListHandler)) {
			log.debug("Decrementing rowNum from ", state.rowNum);
			--state.rowNum;
			--state.colNum;
		}

		state.rowNum = topLeft.getRow();

		NestedTableContainer parentTableHandler = this.getAncestor(NestedTableContainer.class);
		parentTableHandler.addNestedTable(this);

		if (bottomRight.getRow() < topLeft.getRow() + parentRowSpan - 1) {
			state.getSmu().extendRows(state, topLeft.getRow(), topLeft.getCol(), topLeft.getRow() + parentRowSpan,
					bottomRight.getCol());
		}

		state.setHandler(parent);
	}

}
