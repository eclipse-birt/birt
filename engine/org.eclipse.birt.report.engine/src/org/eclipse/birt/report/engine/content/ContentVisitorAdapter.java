/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.core.exception.BirtException;

public class ContentVisitorAdapter implements IContentVisitor {

	@Override
	public Object visit(IContent content, Object value) throws BirtException {
		return content.accept(this, value);
	}

	@Override
	public Object visitContent(IContent content, Object value) throws BirtException {
		return value;
	}

	@Override
	public Object visitPage(IPageContent page, Object value) throws BirtException {
		return visitContent(page, value);
	}

	@Override
	public Object visitContainer(IContainerContent container, Object value) throws BirtException {
		return visitContent(container, value);
	}

	@Override
	public Object visitTable(ITableContent table, Object value) throws BirtException {
		return visitContent(table, value);
	}

	@Override
	public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
		return visitContent(tableBand, value);
	}

	@Override
	public Object visitList(IListContent list, Object value) throws BirtException {
		return visitContainer(list, value);
	}

	@Override
	public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
		return visitContainer(listBand, value);
	}

	@Override
	public Object visitRow(IRowContent row, Object value) throws BirtException {
		return visitContent(row, value);
	}

	@Override
	public Object visitCell(ICellContent cell, Object value) throws BirtException {
		return visitContainer(cell, value);
	}

	@Override
	public Object visitText(ITextContent text, Object value) throws BirtException {
		return visitContent(text, value);
	}

	@Override
	public Object visitLabel(ILabelContent label, Object value) throws BirtException {
		return visitText(label, value);
	}

	@Override
	public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
		return visitText(autoText, value);
	}

	@Override
	public Object visitData(IDataContent data, Object value) throws BirtException {
		return visitText(data, value);
	}

	@Override
	public Object visitImage(IImageContent image, Object value) throws BirtException {
		return visitContent(image, value);
	}

	@Override
	public Object visitForeign(IForeignContent content, Object value) throws BirtException {
		return visitContent(content, value);
	}

	@Override
	public Object visitGroup(IGroupContent group, Object value) throws BirtException {
		return visitContent(group, value);
	}

	@Override
	public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
		return visitGroup(group, value);
	}

	@Override
	public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
		return visitGroup(group, value);
	}

}
