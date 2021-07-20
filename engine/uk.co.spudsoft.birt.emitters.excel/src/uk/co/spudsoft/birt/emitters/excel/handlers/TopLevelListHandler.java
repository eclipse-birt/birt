/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.handlers;

import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import uk.co.spudsoft.birt.emitters.excel.BirtStyle;
import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class TopLevelListHandler extends AbstractRealListHandler {

	private Stack<Integer> groupStarts;

	public TopLevelListHandler(Logger log, IHandler parent, IListContent list) {
		super(log, parent, list);
	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		log.debug("Call startList on ", this);
		super.startList(state, list);
		String name = state.correctSheetName(list.getName());
		if ((name != null) && !name.isEmpty()) {
			state.sheetName = name;
		}

		String password = EmitterServices.stringOption(state.getRenderOptions(), list, ExcelEmitter.SHEET_PASSWORD,
				null);
		if ((password != null) && !password.isEmpty()) {
			state.sheetPassword = password;
		}

		BirtStyle birtStyle = new BirtStyle(list);
		log.debug("List Style: {}", birtStyle);
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		++state.colNum;
		state.setHandler(new NestedTableHandler(log, this, table, 1));
		state.getHandler().startTable(state, table);
	}

	@Override
	public void startListGroup(HandlerState state, IListGroupContent group) throws BirtException {
		if (groupStarts == null) {
			groupStarts = new Stack<Integer>();
		}
		groupStarts.push(state.rowNum);

		Object groupDesignObject = group.getGenerateBy();
		if (groupDesignObject instanceof ListGroupDesign) {
			ListGroupDesign groupDesign = (ListGroupDesign) groupDesignObject;
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
	public void startListBand(HandlerState state, IListBandContent band) throws BirtException {
		super.startListBand(state, band);
	}

	@Override
	public void endListGroup(HandlerState state, IListGroupContent group) throws BirtException {
		int start = groupStarts.pop();
		if (start < state.rowNum - 2) {

			boolean disableGrouping = false;

			// Report user props and context first
			if (EmitterServices.booleanOption(state.getRenderOptions(), group, ExcelEmitter.DISABLE_GROUPING, false)) {
				disableGrouping = true;
			}

			if (!disableGrouping) {
				state.currentSheet.groupRow(start, state.rowNum - 2);
			}
		}
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitText(state, text);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitData(state, data);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitLabel(state, label);
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitAutoText(state, autoText);
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitForeign(state, foreign);
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		state.setHandler(new TopLevelContentHandler(state.getEmitter(), log, this));
		state.getHandler().emitImage(state, image);
	}

}
