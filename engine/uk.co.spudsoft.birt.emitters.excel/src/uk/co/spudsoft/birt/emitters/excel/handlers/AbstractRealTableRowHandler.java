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

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

import uk.co.spudsoft.birt.emitters.excel.AreaBorders;
import uk.co.spudsoft.birt.emitters.excel.BirtStyle;
import uk.co.spudsoft.birt.emitters.excel.CellImage;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerUtils;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public abstract class AbstractRealTableRowHandler extends AbstractHandler {

	protected Row currentRow;
	protected int birtRowStartedAtPoiRow;
	protected int birtRowStartedAtPoiCol;
	protected int myRow;
	protected int startCol;

	private BirtStyle rowStyle;
	private AreaBorders borderDefn;

	public AbstractRealTableRowHandler(Logger log, IHandler parent, IRowContent row, int startCol) {
		super(log, parent, row);
		this.startCol = startCol;
	}

	@Override
	public void startRow(HandlerState state, IRowContent row) throws BirtException {
		state.colNum = this.startCol;
		birtRowStartedAtPoiRow = state.rowNum;
		birtRowStartedAtPoiCol = state.colNum;
		resumeRow(state);
	}

	@Override
	public void endRow(HandlerState state, IRowContent row) throws BirtException {
		interruptRow(state);
		if (row.getBookmark() != null) {
			createName(state, prepareName(row.getBookmark()), birtRowStartedAtPoiRow, 0, state.rowNum - 1,
					currentRow.getLastCellNum() - 1);
		}

		if (EmitterServices.booleanOption(state.getRenderOptions(), row, ExcelEmitter.PRINT_BREAK_AFTER, false)) {
			state.currentSheet.setRowBreak(state.rowNum - 1);
		}

		state.setHandler(parent);
	}

	protected abstract boolean isNested();

	public void resumeRow(HandlerState state) {
		log.debug("Resume row at ", state.rowNum);

		myRow = state.rowNum;
		if (state.currentSheet.getRow(state.rowNum) == null) {
			log.debug("Creating row ", state.rowNum);
			currentRow = state.currentSheet.createRow(state.rowNum);
		} else {
			currentRow = state.currentSheet.getRow(state.rowNum);
		}
		state.requiredRowHeightInPoints = 0;

		rowStyle = new BirtStyle((IRowContent) element);
		borderDefn = AreaBorders.create(myRow, 0, ((IRowContent) element).getTable().getColumnCount() - 1, myRow, -1,
				-1,
				rowStyle);
		if (borderDefn != null) {
			state.insertBorderOverload(borderDefn);
		}
	}

	public void interruptRow(HandlerState state) throws BirtException {
		log.debug("Interrupt row at ", state.rowNum);
		currentRow = state.currentSheet.getRow(state.rowNum);

		boolean blankRow = EmitterServices.booleanOption(state.getRenderOptions(), element,
				ExcelEmitter.REMOVE_BLANK_ROWS, true);

		log.debug("currentRow.getRowNum() == ", currentRow.getRowNum(), ", state.rowNum == ", state.rowNum);

		if (state.rowHasMergedCellsWithBorders(state.rowNum)) {
			for (AreaBorders areaBorder : state.areaBorders) {
				if ((areaBorder.isMergedCells) && (areaBorder.top <= state.rowNum)
						&& (areaBorder.bottom >= state.rowNum)) {

					for (int column = areaBorder.left; column <= areaBorder.right; ++column) {
						if (currentRow.getCell(column) == null) {
							BirtStyle birtCellStyle = new BirtStyle(state.getSm().getCssEngine());
							log.debug("Creating cell[", state.rowNum, ",", column, "]");
							Cell cell = state.currentSheet.getRow(state.rowNum).createCell(column);
							state.getSmu().applyAreaBordersToCell(state.areaBorders, cell, birtCellStyle, state.rowNum,
									column);
							CellStyle cellStyle = state.getSm().getStyle(birtCellStyle);
							cell.setCellStyle(cellStyle);
						}
					}
				}
			}
			blankRow = false;
		}

		if (blankRow) {
			for (Iterator<Cell> iter = currentRow.cellIterator(); iter.hasNext();) {
				Cell cell = iter.next();
				if (!StyleManagerUtils.cellIsEmpty(cell)) {
					blankRow = false;
					break;
				}
			}
		}
		if (blankRow) {
			for (CellImage cellImage : state.images) {
				if (cellImage.location.getRow() == state.rowNum) {
					blankRow = false;
					break;
				}
			}
		}
		if (blankRow) {
			if (state.computeNumberSpanBefore(state.rowNum, state.colNum) > 0) {
				// this row is part of a row span. Dont delete it.
				blankRow = false;
			}
		}
		if (blankRow) {
			if (((IRowContent) element).getBookmark() != null) {
				blankRow = false;
			}
		}

		boolean rowHasNestedTable = ((NestedTableContainer) parent).rowHasNestedTable(state.rowNum);

		if (blankRow && rowHasNestedTable) {
			blankRow = false;
		}

		if (blankRow && isNested()) {
			blankRow = false;
		}

		if (blankRow || ((!rowHasNestedTable) && (!isNested()) && (currentRow.getPhysicalNumberOfCells() == 0))) {
			log.debug("Removing row ", currentRow.getRowNum());
			state.currentSheet.removeRow(currentRow);
		} else {
			DimensionType height = ((IRowContent) element).getHeight();
			if (height != null) {
				if (DimensionUtil.isAbsoluteUnit(height.getUnits())) {
					double points = height.convertTo(DimensionType.UNITS_PT);
					currentRow.setHeightInPoints((float) points);
				}
			}
			if (state.requiredRowHeightInPoints > currentRow.getHeightInPoints()) {
				currentRow.setHeightInPoints(state.requiredRowHeightInPoints);
			}

			if (rowHasNestedTable) {
				int increase = ((NestedTableContainer) parent).extendRowBy(state.rowNum);
				log.debug("Incrementing rowNum from ", state.rowNum, " to ", state.rowNum + increase);
				state.rowNum += increase;
				state.getSmu().extendRows(state, birtRowStartedAtPoiRow, birtRowStartedAtPoiCol, state.rowNum,
						state.colNum);
			} else if (currentRow.getPhysicalNumberOfCells() > 0) {
				log.debug("Incrementing rowNum from ", state.rowNum);
				state.rowNum += 1;
			} else {
				log.debug("Not incrementing rowNum from ", state.rowNum, " because there are no cells on row ",
						currentRow.getPhysicalNumberOfCells());
			}
		}

		if (borderDefn != null) {
			state.removeBorderOverload(borderDefn);
			borderDefn = null;
		}
	}
}
