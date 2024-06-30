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
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.HandlerState;

public interface IHandler {

	IHandler getParent();

	<T extends IHandler> T getAncestor(Class<T> clazz);

	CSSValue getBackgroundColour();

	String getPath();

	void notifyHandler(HandlerState state);

	void startPage(HandlerState state, IPageContent page) throws BirtException;

	void endPage(HandlerState state, IPageContent page) throws BirtException;

	void startTable(HandlerState state, ITableContent table) throws BirtException;

	void endTable(HandlerState state, ITableContent table) throws BirtException;

	void startTableBand(HandlerState state, ITableBandContent band) throws BirtException;

	void endTableBand(HandlerState state, ITableBandContent band) throws BirtException;

	void startRow(HandlerState state, IRowContent row) throws BirtException;

	void endRow(HandlerState state, IRowContent row) throws BirtException;

	void startCell(HandlerState state, ICellContent cell) throws BirtException;

	void endCell(HandlerState state, ICellContent cell) throws BirtException;

	void startList(HandlerState state, IListContent list) throws BirtException;

	void endList(HandlerState state, IListContent list) throws BirtException;

	void startListBand(HandlerState state, IListBandContent listBand) throws BirtException;

	void endListBand(HandlerState state, IListBandContent listBand) throws BirtException;

	void startContainer(HandlerState state, IContainerContent container) throws BirtException;

	void endContainer(HandlerState state, IContainerContent container) throws BirtException;

	void startContent(HandlerState state, IContent content) throws BirtException;

	void endContent(HandlerState state, IContent content) throws BirtException;

	void startGroup(HandlerState state, IGroupContent group) throws BirtException;

	void endGroup(HandlerState state, IGroupContent group) throws BirtException;

	void startTableGroup(HandlerState state, ITableGroupContent group) throws BirtException;

	void endTableGroup(HandlerState state, ITableGroupContent group) throws BirtException;

	void startListGroup(HandlerState state, IListGroupContent group) throws BirtException;

	void endListGroup(HandlerState state, IListGroupContent group) throws BirtException;

	void emitText(HandlerState state, ITextContent text) throws BirtException;

	void emitData(HandlerState state, IDataContent data) throws BirtException;

	void emitLabel(HandlerState state, ILabelContent label) throws BirtException;

	void emitAutoText(HandlerState state, IAutoTextContent autoText) throws BirtException;

	void emitForeign(HandlerState state, IForeignContent foreign) throws BirtException;

	void emitImage(HandlerState state, IImageContent image) throws BirtException;

}
