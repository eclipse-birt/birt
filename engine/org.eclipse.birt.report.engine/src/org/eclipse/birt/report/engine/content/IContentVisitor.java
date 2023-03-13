/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Defines the visitor interface used mainly by a buffered emitter
 *
 */
public interface IContentVisitor {

	Object visit(IContent content, Object value) throws BirtException;

	Object visitContent(IContent content, Object value) throws BirtException;

	Object visitPage(IPageContent page, Object value) throws BirtException;

	/**
	 * visit content( free-form and list band)
	 *
	 * @param content
	 */
	Object visitContainer(IContainerContent container, Object value) throws BirtException;

	/**
	 * visit table content object
	 *
	 * @param table the table object
	 */
	Object visitTable(ITableContent table, Object value) throws BirtException;

	/**
	 * visit table band
	 *
	 * @param tableBand
	 * @throws BirtException
	 */
	Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException;

	/**
	 * visit list content
	 *
	 * @param list
	 * @param value
	 */
	Object visitList(IListContent list, Object value) throws BirtException;

	/**
	 * visit list band content
	 *
	 * @param listBand
	 * @param value
	 */
	Object visitListBand(IListBandContent listBand, Object value) throws BirtException;

	/**
	 * visit the row content object
	 *
	 * @param row the row object
	 */
	Object visitRow(IRowContent row, Object value) throws BirtException;

	/**
	 * visit cell content object
	 *
	 * @param cell the cell object
	 */
	Object visitCell(ICellContent cell, Object value) throws BirtException;

	/**
	 * visit the text content object
	 *
	 * @param text the text object
	 */
	Object visitText(ITextContent text, Object value) throws BirtException;

	Object visitLabel(ILabelContent label, Object value) throws BirtException;

	Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException;

	Object visitData(IDataContent data, Object value) throws BirtException;

	/**
	 * visit image content
	 *
	 * @param image
	 */
	Object visitImage(IImageContent image, Object value) throws BirtException;

	/**
	 * visit exteded item
	 *
	 * @param content
	 */
	Object visitForeign(IForeignContent foreign, Object value) throws BirtException;

	Object visitGroup(IGroupContent group, Object value) throws BirtException;

	Object visitListGroup(IListGroupContent group, Object value) throws BirtException;

	Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException;
}
