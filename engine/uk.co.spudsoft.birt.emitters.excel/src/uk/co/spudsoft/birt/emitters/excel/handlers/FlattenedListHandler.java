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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class FlattenedListHandler extends AbstractHandler {

	private CellContentHandler contentHandler;

	public FlattenedListHandler(CellContentHandler contentHandler, Logger log, IHandler parent, IContent element) {
		super(log, parent, element);
		this.contentHandler = contentHandler;
	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
	}

	@Override
	public void endList(HandlerState state, IListContent list) throws BirtException {
		contentHandler.lastCellContentsWasBlock = true;
		contentHandler.lastCellContentsRequiresSpace = false;
		state.setHandler(parent);
	}

	@Override
	public void startListBand(HandlerState state, IListBandContent listBand) throws BirtException {
	}

	@Override
	public void endListBand(HandlerState state, IListBandContent listBand) throws BirtException {
	}

	@Override
	public void startListGroup(HandlerState state, IListGroupContent group) throws BirtException {
	}

	@Override
	public void endListGroup(HandlerState state, IListGroupContent group) throws BirtException {
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
