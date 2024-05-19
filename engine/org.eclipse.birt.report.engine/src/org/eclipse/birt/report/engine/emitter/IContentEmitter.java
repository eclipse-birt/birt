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

	/**
	 * Get the output format
	 *
	 * @return output format
	 */
	String getOutputFormat();

	/**
	 * Initialize the emitter
	 *
	 * @param service emitter service
	 * @throws BirtException
	 */
	void initialize(IEmitterServices service) throws BirtException;

	/**
	 * Start the emitter
	 *
	 * @param report report content
	 * @throws BirtException
	 */
	void start(IReportContent report) throws BirtException;

	/**
	 * End the emitter
	 *
	 * @param report report content
	 * @throws BirtException
	 */
	void end(IReportContent report) throws BirtException;

	/**
	 * Start a page
	 *
	 * @param page page content
	 * @throws BirtException
	 */
	void startPage(IPageContent page) throws BirtException;

	/**
	 * Page end
	 *
	 * @param page oage content
	 * @throws BirtException
	 */
	void endPage(IPageContent page) throws BirtException;

	/**
	 * Table started
	 *
	 * @param table table content
	 * @throws BirtException
	 */
	void startTable(ITableContent table) throws BirtException;

	/**
	 * Table end
	 *
	 * @param table table content
	 * @throws BirtException
	 */
	void endTable(ITableContent table) throws BirtException;

	/**
	 * Start table band
	 *
	 * @param band table band content
	 * @throws BirtException
	 */
	void startTableBand(ITableBandContent band) throws BirtException;

	/**
	 * End table band
	 *
	 * @param band table band content
	 * @throws BirtException
	 */
	void endTableBand(ITableBandContent band) throws BirtException;

	/**
	 * Start row
	 *
	 * @param row row content
	 * @throws BirtException
	 */
	void startRow(IRowContent row) throws BirtException;

	/**
	 * End row
	 *
	 * @param row row content
	 * @throws BirtException
	 */
	void endRow(IRowContent row) throws BirtException;

	/**
	 * Start cell
	 *
	 * @param cell cell content
	 * @throws BirtException
	 */
	void startCell(ICellContent cell) throws BirtException;

	/**
	 * End cell
	 *
	 * @param cell cell content
	 * @throws BirtException
	 */
	void endCell(ICellContent cell) throws BirtException;

	/**
	 * Start list
	 *
	 * @param list list content
	 * @throws BirtException
	 */
	void startList(IListContent list) throws BirtException;

	/**
	 * End list
	 *
	 * @param list list content
	 * @throws BirtException
	 */
	void endList(IListContent list) throws BirtException;

	/**
	 * Start list band
	 *
	 * @param listBand list band content
	 * @throws BirtException
	 */
	void startListBand(IListBandContent listBand) throws BirtException;

	/**
	 * End list band
	 *
	 * @param listBand list band content
	 * @throws BirtException
	 */
	void endListBand(IListBandContent listBand) throws BirtException;

	/**
	 * Start container
	 *
	 * @param container container content
	 * @throws BirtException
	 */
	void startContainer(IContainerContent container) throws BirtException;

	/**
	 * End container
	 *
	 * @param container container content
	 * @throws BirtException
	 */
	void endContainer(IContainerContent container) throws BirtException;

	/**
	 * Start text
	 *
	 * @param text text content
	 * @throws BirtException
	 */
	void startText(ITextContent text) throws BirtException;

	/**
	 * Start data
	 *
	 * @param data data content
	 * @throws BirtException
	 */
	void startData(IDataContent data) throws BirtException;

	/**
	 * Start label
	 *
	 * @param label label content
	 * @throws BirtException
	 */
	void startLabel(ILabelContent label) throws BirtException;

	/**
	 * Start auto text
	 *
	 * @param autoText auto text content
	 * @throws BirtException
	 */
	void startAutoText(IAutoTextContent autoText) throws BirtException;

	/**
	 * Start foreign
	 *
	 * @param foreign foreign content
	 * @throws BirtException
	 */
	void startForeign(IForeignContent foreign) throws BirtException;

	/**
	 * Start image
	 *
	 * @param image image content
	 *
	 * @throws BirtException
	 */
	void startImage(IImageContent image) throws BirtException;

	/**
	 * Start content
	 *
	 * @param content content
	 * @throws BirtException
	 */
	void startContent(IContent content) throws BirtException;

	/**
	 * End content
	 *
	 * @param content content
	 * @throws BirtException
	 */
	void endContent(IContent content) throws BirtException;

	/**
	 * Start group
	 *
	 * @param group group content
	 * @throws BirtException
	 */
	void startGroup(IGroupContent group) throws BirtException;

	/**
	 * End group
	 *
	 * @param group group content
	 * @throws BirtException
	 */
	void endGroup(IGroupContent group) throws BirtException;

	/**
	 * Start table group
	 *
	 * @param group table group content
	 * @throws BirtException
	 */
	void startTableGroup(ITableGroupContent group) throws BirtException;

	/**
	 * End table group
	 *
	 * @param group table group content
	 * @throws BirtException
	 */
	void endTableGroup(ITableGroupContent group) throws BirtException;

	/**
	 * Start list group
	 *
	 * @param group list group content
	 * @throws BirtException
	 */
	void startListGroup(IListGroupContent group) throws BirtException;

	/**
	 * End list group
	 *
	 * @param group list group content
	 * @throws BirtException
	 */
	void endListGroup(IListGroupContent group) throws BirtException;
}
