/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

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
 * Adapter of the content emitter
 *
 * @since 3.3
 *
 */
public class ContentEmitterAdapter implements IContentEmitter {

	@Override
	public String getOutputFormat() {
		return null;
	}

	@Override
	public void initialize(IEmitterServices service) throws BirtException {
	}

	@Override
	public void start(IReportContent report) throws BirtException {
	}

	@Override
	public void end(IReportContent report) throws BirtException {
	}

	@Override
	public void startContent(IContent content) throws BirtException {
	}

	@Override
	public void endContent(IContent content) throws BirtException {
	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		startContainer(page);
	}

	@Override
	public void endPage(IPageContent page) throws BirtException {
		endContainer(page);
	}

	@Override
	public void startTable(ITableContent table) throws BirtException {
		startContainer(table);
	}

	@Override
	public void endTable(ITableContent table) throws BirtException {
		endContainer(table);
	}

	@Override
	public void startTableBand(ITableBandContent band) throws BirtException {
		startContainer(band);
	}

	@Override
	public void endTableBand(ITableBandContent band) throws BirtException {
		endContainer(band);
	}

	@Override
	public void startList(IListContent list) throws BirtException {
		startContainer(list);
	}

	@Override
	public void endList(IListContent list) throws BirtException {
		endContainer(list);
	}

	@Override
	public void startListBand(IListBandContent listBand) throws BirtException {
		startContainer(listBand);
	}

	@Override
	public void endListBand(IListBandContent listBand) throws BirtException {
		endContainer(listBand);
	}

	@Override
	public void startRow(IRowContent row) throws BirtException {
		startContainer(row);
	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		endContainer(row);
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		startContainer(cell);
	}

	@Override
	public void endCell(ICellContent cell) throws BirtException {
		endContainer(cell);

	}

	@Override
	public void startContainer(IContainerContent container) throws BirtException {
		startContent(container);
	}

	@Override
	public void endContainer(IContainerContent container) throws BirtException {
		endContent(container);
	}

	@Override
	public void startText(ITextContent text) throws BirtException {
		startContent(text);
		endContent(text);
	}

	@Override
	public void startLabel(ILabelContent label) throws BirtException {
		startText(label);
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		startText(autoText);
	}

	@Override
	public void startData(IDataContent data) throws BirtException {
		startText(data);
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		startContent(foreign);
		endContent(foreign);
	}

	@Override
	public void startImage(IImageContent image) throws BirtException {
		startContent(image);
		endContent(image);

	}

	@Override
	public void endGroup(IGroupContent group) throws BirtException {
		endContainer(group);
	}

	@Override
	public void startGroup(IGroupContent group) throws BirtException {
		startContainer(group);
	}

	@Override
	public void endListGroup(IListGroupContent group) throws BirtException {
		endGroup(group);
	}

	@Override
	public void endTableGroup(ITableGroupContent group) throws BirtException {
		endGroup(group);
	}

	@Override
	public void startListGroup(IListGroupContent group) throws BirtException {
		startGroup(group);
	}

	@Override
	public void startTableGroup(ITableGroupContent group) throws BirtException {
		startGroup(group);
	}

	/**
	 * Is multiple pages enabled
	 *
	 * @return is multiple pages enabled
	 */
	public boolean isMultiplePagesEnabled() {
		return true;
	}
}
