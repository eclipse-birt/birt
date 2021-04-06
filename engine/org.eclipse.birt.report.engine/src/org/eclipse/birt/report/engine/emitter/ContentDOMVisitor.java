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

package org.eclipse.birt.report.engine.emitter;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
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

/**
 * visit the content and output the content to emitter
 * 
 */
public class ContentDOMVisitor extends ContentVisitorAdapter {

	protected IContentEmitter emitter;

	public ContentDOMVisitor() {
	}

	public void emit(IContent content, IContentEmitter emitter) throws BirtException {
		this.emitter = emitter;
		visit(content, null);
	}

	public Object visitPage(IPageContent page, Object value) throws BirtException {
		emitter.startPage(page);
		visitChildren(page.getPageBody(), value);
		emitter.endPage(page);
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

	public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
		emitter.startTableGroup(group);
		visitChildren(group, value);
		emitter.endTableGroup(group);
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

	public Object visitList(IListContent list, Object value) throws BirtException {
		emitter.startList(list);
		visitChildren(list, value);
		emitter.endList(list);
		return value;
	}

	public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
		emitter.startListGroup(group);
		visitChildren(group, value);
		emitter.endListGroup(group);
		return value;
	}

	public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
		emitter.startListBand(listBand);
		visitChildren(listBand, value);
		emitter.endListBand(listBand);
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

	public Object visitData(IDataContent data, Object value) throws BirtException {
		emitter.startData(data);
		return value;
	}

	public Object visitImage(IImageContent image, Object value) throws BirtException {
		emitter.startImage(image);
		return value;
	}

	public Object visitForeign(IForeignContent content, Object value) throws BirtException {
		emitter.startForeign(content);
		return value;
	}

	protected void visitChildren(IContent container, Object value) throws BirtException {
		Iterator iter = container.getChildren().iterator();
		while (iter.hasNext()) {
			IContent content = (IContent) iter.next();
			visit(content, value);
		}

	}

}
