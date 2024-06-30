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
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;

import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class FlattenedTableCellHandler extends AbstractHandler {

	private CellContentHandler contentHandler;

	public FlattenedTableCellHandler(CellContentHandler contentHandler, Logger log, IHandler parent,
			ICellContent cell) {
		super(log, parent, cell);
		this.contentHandler = contentHandler;
	}

	@Override
	public void startCell(HandlerState state, ICellContent cell) throws BirtException {
	}

	@Override
	public void endCell(HandlerState state, ICellContent cell) throws BirtException {
		contentHandler.lastCellContentsWasBlock = false;
		contentHandler.lastCellContentsRequiresSpace = true;
		state.setHandler(parent);
	}

	@Override
	public void startTable(HandlerState state, ITableContent table) throws BirtException {
		state.setHandler(new FlattenedTableHandler(contentHandler, log, this, table));
		state.getHandler().startTable(state, table);
	}

	@Override
	public void emitText(HandlerState state, ITextContent text) throws BirtException {
		contentHandler.emitText(state, text);
	}

	@Override
	public void emitData(HandlerState state, IDataContent data) throws BirtException {
		contentHandler.emitData(state, data);
	}

	@Override
	public void emitLabel(HandlerState state, ILabelContent label) throws BirtException {
		contentHandler.emitLabel(state, label);
	}

	@Override
	public void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException {
		contentHandler.emitAutoText(state, autoText);
	}

	@Override
	public void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException {
		contentHandler.emitForeign(state, foreign);
	}

	@Override
	public void emitImage(HandlerState state, IImageContent image) throws BirtException {
		contentHandler.emitImage(state, image);
	}

}
