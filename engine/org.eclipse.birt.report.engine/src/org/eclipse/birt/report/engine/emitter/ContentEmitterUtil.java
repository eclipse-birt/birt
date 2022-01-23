/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
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

public class ContentEmitterUtil {

	static IContentVisitor starter = new StartContentVisitor();
	static IContentVisitor ender = new EndContentVisitor();

	static public void startContent(IContent content, IContentEmitter emitter) throws BirtException {
		switch (content.getContentType()) {
		case IContent.PAGE_CONTENT:
			emitter.startPage((IPageContent) content);
			break;
		case IContent.TABLE_CONTENT:
			emitter.startTable((ITableContent) content);
			break;
		case IContent.TABLE_BAND_CONTENT:
			emitter.startTableBand((ITableBandContent) content);
			break;
		case IContent.ROW_CONTENT:
			emitter.startRow((IRowContent) content);
			break;
		case IContent.CELL_CONTENT:
			emitter.startCell((ICellContent) content);
			break;
		case IContent.TEXT_CONTENT:
			emitter.startText((ITextContent) content);
			break;
		case IContent.LABEL_CONTENT:
			emitter.startLabel((ILabelContent) content);
			break;
		case IContent.AUTOTEXT_CONTENT:
			emitter.startAutoText((IAutoTextContent) content);
			break;
		case IContent.DATA_CONTENT:
			emitter.startData((IDataContent) content);
			break;
		case IContent.IMAGE_CONTENT:
			emitter.startImage((IImageContent) content);
			break;
		case IContent.FOREIGN_CONTENT:
			emitter.startForeign((IForeignContent) content);
			break;
		case IContent.LIST_CONTENT:
			emitter.startList((IListContent) content);
			break;
		case IContent.LIST_BAND_CONTENT:
			emitter.startListBand((IListBandContent) content);
			break;
		case IContent.LIST_GROUP_CONTENT:
			emitter.startListGroup((IListGroupContent) content);
			break;
		case IContent.TABLE_GROUP_CONTENT:
			emitter.startTableGroup((ITableGroupContent) content);
			break;
		default:
			starter.visit(content, emitter);
		}
	}

	static public void endContent(IContent content, IContentEmitter emitter) throws BirtException {
		switch (content.getContentType()) {
		case IContent.PAGE_CONTENT:
			emitter.endPage((IPageContent) content);
			break;
		case IContent.TABLE_CONTENT:
			emitter.endTable((ITableContent) content);
			break;
		case IContent.TABLE_BAND_CONTENT:
			emitter.endTableBand((ITableBandContent) content);
			break;
		case IContent.ROW_CONTENT:
			emitter.endRow((IRowContent) content);
			break;
		case IContent.CELL_CONTENT:
			emitter.endCell((ICellContent) content);
			break;
		case IContent.TEXT_CONTENT:
		case IContent.LABEL_CONTENT:
		case IContent.AUTOTEXT_CONTENT:
		case IContent.DATA_CONTENT:
		case IContent.IMAGE_CONTENT:
		case IContent.FOREIGN_CONTENT:
			break;
		case IContent.LIST_CONTENT:
			emitter.endList((IListContent) content);
			break;
		case IContent.LIST_BAND_CONTENT:
			emitter.endListBand((IListBandContent) content);
			break;
		case IContent.LIST_GROUP_CONTENT:
			emitter.endListGroup((IListGroupContent) content);
			break;
		case IContent.TABLE_GROUP_CONTENT:
			emitter.endTableGroup((ITableGroupContent) content);
			break;
		default:
			ender.visit(content, emitter);
		}
	}

	private static class StartContentVisitor implements IContentVisitor {

		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContent(content);
			return value;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startPage(page);
			return value;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContainer(container);
			return value;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTable(table);
			return value;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableBand(tableBand);
			return value;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startRow(row);
			return value;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startCell(cell);
			return value;
		}

		public Object visitText(ITextContent text, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startText(text);
			return value;
		}

		public Object visitLabel(ILabelContent label, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startLabel(label);
			return value;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startAutoText(autoText);
			return value;
		}

		public Object visitData(IDataContent data, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startData(data);
			return value;
		}

		public Object visitImage(IImageContent image, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startImage(image);
			return value;
		}

		public Object visitForeign(IForeignContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startForeign(content);
			return value;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startList(list);
			return value;
		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListBand(listBand);
			return value;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startGroup(group);
			return value;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListGroup(group);
			return value;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableGroup(group);
			return value;
		}
	}

	static private class EndContentVisitor implements IContentVisitor {

		public Object visit(IContent content, Object value) throws BirtException {
			return content.accept(this, value);
		}

		public Object visitContent(IContent content, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContent(content);
			return value;
		}

		public Object visitPage(IPageContent page, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endPage(page);
			return value;
		}

		public Object visitContainer(IContainerContent container, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContainer(container);
			return value;
		}

		public Object visitTable(ITableContent table, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTable(table);
			return value;
		}

		public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableBand(tableBand);
			return value;
		}

		public Object visitRow(IRowContent row, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endRow(row);
			return value;
		}

		public Object visitCell(ICellContent cell, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endCell(cell);
			return value;
		}

		public Object visitText(ITextContent text, Object value) {
			return value;
		}

		public Object visitLabel(ILabelContent label, Object value) {
			return value;
		}

		public Object visitAutoText(IAutoTextContent autoText, Object value) {
			return value;
		}

		public Object visitData(IDataContent data, Object value) {
			return value;
		}

		public Object visitImage(IImageContent image, Object value) {
			return value;
		}

		public Object visitForeign(IForeignContent content, Object value) {
			return value;
		}

		public Object visitList(IListContent list, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endList(list);
			return value;
		}

		public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListBand(listBand);
			return value;
		}

		public Object visitGroup(IGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endGroup(group);
			return value;
		}

		public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListGroup(group);
			return value;
		}

		public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableGroup(group);
			return value;
		}
	}
}
