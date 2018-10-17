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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;

import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class FlattenedTableHandler extends AbstractHandler {
	
	private CellContentHandler contentHandler;

	public FlattenedTableHandler(CellContentHandler contentHandler, Logger log, IHandler parent, ITableContent table) {
		super(log, parent, table);
		this.contentHandler = contentHandler;
	}

	
	
	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		state.setHandler(new FlattenedListHandler(contentHandler, log, this, list));
		state.getHandler().startList(state, list);
	}



	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		if( ( state.sheetPassword == null ) || state.sheetPassword.isEmpty() ) {
			String password = EmitterServices.stringOption( state.getRenderOptions(), table, ExcelEmitter.SHEET_PASSWORD, null);
			if( ( password != null ) && ! password.isEmpty() ) {
				state.sheetPassword = password;
			}
		}
	}

	@Override
	public void endTable(HandlerState state, ITableContent table) throws BirtException {
		state.setHandler(parent);
	}

	@Override
	public void startRow(HandlerState state, IRowContent row) throws BirtException {
		state.setHandler(new FlattenedTableRowHandler(contentHandler, log, this, row));
		state.getHandler().startRow(state, row);
	}

	@Override
	public void startTableBand(HandlerState state, ITableBandContent band) throws BirtException {
	}

	@Override
	public void endTableBand(HandlerState state, ITableBandContent band) throws BirtException {
	}

	@Override
	public void startTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
	}

	@Override
	public void endTableGroup(HandlerState state, ITableGroupContent group) throws BirtException {
	}
	
	
	
}
