/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.presentation;

import java.util.Collection;
import java.util.Iterator;

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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class ContentEmitterVisitor implements IContentVisitor {

	IContentEmitter emitter;

	public ContentEmitterVisitor(IContentEmitter emitter) {
		this.emitter = emitter;
	}

	public Object visit(IContent content, Object value) throws BirtException {
		return content.accept(this, value);
	}

	public Object visitContent(IContent content, Object value) throws BirtException {
		emitter.startContent(content);
		return value;
	}

	public Object visitPage(IPageContent page, Object value) throws BirtException {
		// emitter.startPage( page );
		// emitter.endPage( page );
		return value;
	}

	public Object visitContainer(IContainerContent container, Object value) throws BirtException {
		emitter.startContainer(container);
		visitChildren(container, value);
		emitter.endContainer(container);
		return value;
	}

	public Object visitTable(ITableContent table, Object value) throws BirtException {
		emitter.startTable(table);
		visitChildren(table, value);
		emitter.endTable(table);
		return value;
	}

	public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
		emitter.startTableBand(tableBand);
		visitChildren(tableBand, value);
		emitter.endTableBand(tableBand);
		return value;
	}

	public Object visitRow(IRowContent row, Object value) throws BirtException {
		emitter.startRow(row);
		visitChildren(row, value);
		emitter.endRow(row);
		return value;
	}

	public Object visitCell(ICellContent cell, Object value) throws BirtException {
		emitter.startCell(cell);
		visitChildren(cell, value);
		emitter.endCell(cell);
		return value;
	}

	public Object visitText(ITextContent text, Object value) throws BirtException {
		emitter.startText(text);
		return value;
	}

	public Object visitLabel(ILabelContent label, Object value) throws BirtException {
		emitter.startLabel(label);
		return value;
	}

	public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
		emitter.startAutoText(autoText);
		return value;
	}

	public Object visitData(IDataContent data, Object value) throws BirtException {
		emitter.startData(data);
		return value;
	}

	public Object visitImage(IImageContent image, Object value) throws BirtException {
		emitter.startImage(image);
		return value;
	}

	public Object visitForeign(IForeignContent foreign, Object value) throws BirtException {
		emitter.startForeign(foreign);
		return value;
	}

	public Object visitChildren(IContent content, Object value) throws BirtException {
		Collection list = content.getChildren();
		if (list == null) {
			return value;
		}

		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Object child = iter.next();
			if (child instanceof IContent) {
				visit((IContent) child, value);
			}
		}
		return value;
	}

	public Object visitList(IListContent list, Object value) throws BirtException {
		emitter.startList(list);
		visitChildren(list, value);
		emitter.endList(list);
		return value;
	}

	public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
		emitter.startListBand(listBand);
		visitChildren(listBand, value);
		emitter.endListBand(listBand);
		return value;
	}

	public Object visitGroup(IGroupContent group, Object value) throws BirtException {
		emitter.startGroup(group);
		visitChildren(group, value);
		emitter.endGroup(group);
		return null;
	}

	public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
		emitter.startListGroup(group);
		visitChildren(group, value);
		emitter.endListGroup(group);
		return null;
	}

	public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
		emitter.startTableGroup(group);
		visitChildren(group, value);
		emitter.endTableGroup(group);
		return null;
	}
}
