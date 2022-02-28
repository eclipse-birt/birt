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

package org.eclipse.birt.report.engine.emitter;

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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * used to pass the content object to following process.
 *
 */
public interface IContentEmitter {

	String getOutputFormat();

	void initialize(IEmitterServices service) throws BirtException;

	void start(IReportContent report) throws BirtException;

	void end(IReportContent report) throws BirtException;

	/**
	 * start a page
	 *
	 * @param page
	 */
	void startPage(IPageContent page) throws BirtException;

	/**
	 * page end
	 *
	 * @param page
	 */
	void endPage(IPageContent page) throws BirtException;

	/**
	 * table started
	 *
	 * @param table
	 */
	void startTable(ITableContent table) throws BirtException;

	/**
	 * table end
	 */
	void endTable(ITableContent table) throws BirtException;

	void startTableBand(ITableBandContent band) throws BirtException;

	void endTableBand(ITableBandContent band) throws BirtException;

	void startRow(IRowContent row) throws BirtException;

	void endRow(IRowContent row) throws BirtException;

	void startCell(ICellContent cell) throws BirtException;

	void endCell(ICellContent cell) throws BirtException;

	void startList(IListContent list) throws BirtException;

	void endList(IListContent list) throws BirtException;

	void startListBand(IListBandContent listBand) throws BirtException;

	void endListBand(IListBandContent listBand) throws BirtException;

	void startContainer(IContainerContent container) throws BirtException;

	void endContainer(IContainerContent container) throws BirtException;

	void startText(ITextContent text) throws BirtException;

	void startData(IDataContent data) throws BirtException;

	void startLabel(ILabelContent label) throws BirtException;

	void startAutoText(IAutoTextContent autoText) throws BirtException;

	void startForeign(IForeignContent foreign) throws BirtException;

	void startImage(IImageContent image) throws BirtException;

	void startContent(IContent content) throws BirtException;

	void endContent(IContent content) throws BirtException;

	void startGroup(IGroupContent group) throws BirtException;

	void endGroup(IGroupContent group) throws BirtException;

	void startTableGroup(ITableGroupContent group) throws BirtException;

	void endTableGroup(ITableGroupContent group) throws BirtException;

	void startListGroup(IListGroupContent group) throws BirtException;

	void endListGroup(IListGroupContent group) throws BirtException;
}
