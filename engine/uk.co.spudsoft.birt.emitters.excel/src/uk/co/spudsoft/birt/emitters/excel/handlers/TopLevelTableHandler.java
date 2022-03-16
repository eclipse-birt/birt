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

import java.util.Stack;

import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class TopLevelTableHandler extends AbstractRealTableHandler {

	private Stack<Integer> groupStarts;

	public TopLevelTableHandler(Logger log, IHandler parent, ITableContent table) {
		super(log, parent, table);
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		state.colNum = 0;
		super.startTable(state, table);
		String name = state.correctSheetName(table.getName());
		if ((name != null) && !name.isEmpty()) {
			state.sheetName = name;
		}

		String password = EmitterServices.stringOption(state.getRenderOptions(), table, ExcelEmitter.SHEET_PASSWORD,
				null);
		if ((password != null) && !password.isEmpty()) {
			state.sheetPassword = password;
		}
		if (EmitterServices.booleanOption(state.getRenderOptions(), table, ExcelEmitter.GROUP_SUMMARY_HEADER, false)) {
			state.currentSheet.setRowSumsBelow(false);
		}
	}

	@Override
	public void endTable(HandlerState state, ITableContent table) throws BirtException {
		super.endTable(state, table);

		boolean autoFilter = EmitterServices.booleanOption(state.getRenderOptions(), table, ExcelEmitter.AUTO_FILTER,
				false);
		if (autoFilter) {
			log.debug("Applying auto filter to [", this.startRow, ",", this.startCol, "] - [", this.endDetailsRow, ",",
					state.colNum - 1, "]");
			CellRangeAddress wholeTable = new CellRangeAddress(startRow, endDetailsRow, startCol, state.colNum - 1);
			state.currentSheet.setAutoFilter(wholeTable);
		}

		boolean blankRowAfterTopLevelTable = EmitterServices.booleanOption(state.getRenderOptions(), table,
				ExcelEmitter.BLANK_ROW_AFTER_TOP_LEVEL_TABLE, false);
		if (blankRowAfterTopLevelTable) {
			++state.rowNum;
		}

		state.setHandler(parent);
	}

	@Override
	public void startRow(HandlerState state, IRowContent row) throws BirtException {
		state.setHandler(new TopLevelTableRowHandler(log, this, row));
		state.getHandler().startRow(state, row);
	}

	@Override
	public void startTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		log.debug("startTableGroup @" + state.rowNum + " called " + group.getBookmark());
		if (groupStarts == null) {
			groupStarts = new Stack<>();
		}
		groupStarts.push(state.rowNum);

		Object groupDesignObject = group.getGenerateBy();
		if (groupDesignObject instanceof TableGroupDesign) {
			TableGroupDesign groupDesign = (TableGroupDesign) groupDesignObject;
			if (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals(groupDesign.getPageBreakBefore())
					|| DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST
							.equals(groupDesign.getPageBreakBefore())
					|| DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals(groupDesign.getPageBreakAfter())
					|| DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST
							.equals(groupDesign.getPageBreakAfter())) {
				if (group.getTOC() != null) {
					state.sheetName = state.correctSheetName(group.getTOC().toString());
				}
			}
		}

	}

	@Override
	public void endTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
		log.debug("endTableGroup @" + state.rowNum + " called " + group.getBookmark());
		int start = groupStarts.pop();
		if (start < state.rowNum - 1) {

			boolean disableGrouping = false;

			// Report user props and context first
			if (EmitterServices.booleanOption(state.getRenderOptions(), group, ExcelEmitter.DISABLE_GROUPING, false)) {
				disableGrouping = true;
			}

			if (!disableGrouping) {
				if (state.currentSheet.getRowSumsBelow()) {
					log.debug("TableGroup of rows below ", start, " - ", state.rowNum - 2);
					state.currentSheet.groupRow(start, state.rowNum - 2);
				} else {
					log.debug("TableGroup of rows above ", start + 1, " - ", state.rowNum - 1);
					state.currentSheet.groupRow(start + 1, state.rowNum - 1);
				}
			}
		}
	}

}
