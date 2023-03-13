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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;

import uk.co.spudsoft.birt.emitters.excel.AreaBorders;
import uk.co.spudsoft.birt.emitters.excel.BirtStyle;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class AbstractRealListHandler extends AbstractHandler implements NestedTableContainer {

	protected int startRow;
	protected int startCol;

	private AreaBorders borderDefn;

	private List<NestedTableHandler> nestedTables;

	public AbstractRealListHandler(Logger log, IHandler parent, IListContent list) {
		super(log, parent, list);
	}

	@Override
	public void addNestedTable(NestedTableHandler nestedTableHandler) {
		if (nestedTables == null) {
			nestedTables = new ArrayList<>();
		}
		log.debug("Adding nested table: ", nestedTableHandler);
		nestedTables.add(nestedTableHandler);
	}

	@Override
	public boolean rowHasNestedTable(int rowNum) {
		if (nestedTables != null) {
			for (NestedTableHandler nestedTableHandler : nestedTables) {
				if (nestedTableHandler.includesRow(rowNum)) {
					log.debug("Row ", rowNum, " has nested table ", nestedTableHandler);
					return true;
				}
			}
		}
		log.debug("Row ", rowNum, " has no nested tables");
		return false;
	}

	@Override
	public int extendRowBy(int rowNum) {
		int offset = 1;
		if (nestedTables != null) {
			for (NestedTableHandler nestedTableHandler : nestedTables) {
				int nestedTablesOffset = nestedTableHandler.extendParentsRowBy(rowNum);
				if (nestedTablesOffset > offset) {
					log.debug("Row ", rowNum, " is extended by ", nestedTablesOffset, " thanks to ",
							nestedTableHandler);
					offset = nestedTablesOffset;
				}
			}
		}
		return offset;
	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		startRow = state.rowNum;
		startCol = state.colNum;
		log.debug("List started at [", startRow, ",", startCol, "]");
	}

	@Override
	public void endList(HandlerState state, IListContent list) throws BirtException {
		state.setHandler(parent);

		int endRow = state.rowNum - 1;
		int colStart = 0;
		int colEnd = 0;

		for (int row = startRow; row < endRow; ++row) {
			if (state.currentSheet.getRow(row) != null) {
				int lastColInRow = state.currentSheet.getRow(row).getLastCellNum() - 1;
				if (lastColInRow > colEnd) {
					colEnd = lastColInRow;
				}
			}
		}

		state.getSmu().applyBordersToArea(state.getSm(), state.currentSheet, colStart, colEnd, startRow, endRow,
				new BirtStyle(list));

		if (borderDefn != null) {
			state.removeBorderOverload(borderDefn);
		}

		if (list.getBookmark() != null) {
			createName(state, prepareName(list.getBookmark()), startRow, 0, state.rowNum - 1, 0);
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), list, ExcelEmitter.DISPLAYFORMULAS_PROP, false)) {
			state.currentSheet.setDisplayFormulas(true);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), list, ExcelEmitter.DISPLAYGRIDLINES_PROP, true)) {
			state.currentSheet.setDisplayGridlines(false);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), list, ExcelEmitter.DISPLAYROWCOLHEADINGS_PROP,
				true)) {
			state.currentSheet.setDisplayRowColHeadings(false);
		}
		if (!EmitterServices.booleanOption(state.getRenderOptions(), list, ExcelEmitter.DISPLAYZEROS_PROP, true)) {
			state.currentSheet.setDisplayZeros(false);
		}
	}

	@Override
	public void startListBand(HandlerState state, IListBandContent band) throws BirtException {
		state.colNum = startCol;
		log.debug("startListBand with startCol = ", startCol);
	}

	@Override
	public void endListBand(HandlerState state, IListBandContent band) throws BirtException {
		boolean rowHasNestedTable = rowHasNestedTable(state.rowNum);

		if (rowHasNestedTable) {
			state.rowNum += extendRowBy(state.rowNum);
		}
		state.colNum = startCol;
	}

	@Override
	public void startListGroup(HandlerState state, IListGroupContent group) throws BirtException {
	}

	@Override
	public void endListGroup(HandlerState state, IListGroupContent group) throws BirtException {
	}

}
