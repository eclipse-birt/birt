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

public class ContentEmitterAdapter implements IContentEmitter {

	public String getOutputFormat() {
		return null;
	}

	public void initialize(IEmitterServices service) throws BirtException {
	}

	public void start(IReportContent report) throws BirtException {
	}

	public void end(IReportContent report) throws BirtException {
	}

	public void startContent(IContent content) throws BirtException {
	}

	public void endContent(IContent content) throws BirtException {
	}

	public void startPage(IPageContent page) throws BirtException {
		startContainer(page);
	}

	public void endPage(IPageContent page) throws BirtException {
		endContainer(page);
	}

	public void startTable(ITableContent table) throws BirtException {
		startContainer(table);
	}

	public void endTable(ITableContent table) throws BirtException {
		endContainer(table);
	}

	public void startTableBand(ITableBandContent band) throws BirtException {
		startContainer(band);
	}

	public void endTableBand(ITableBandContent band) throws BirtException {
		endContainer(band);
	}

	public void startList(IListContent list) throws BirtException {
		startContainer(list);
	}

	public void endList(IListContent list) throws BirtException {
		endContainer(list);
	}

	public void startListBand(IListBandContent listBand) throws BirtException {
		startContainer(listBand);
	}

	public void endListBand(IListBandContent listBand) throws BirtException {
		endContainer(listBand);
	}

	public void startRow(IRowContent row) throws BirtException {
		startContainer(row);
	}

	public void endRow(IRowContent row) throws BirtException {
		endContainer(row);
	}

	public void startCell(ICellContent cell) throws BirtException {
		startContainer(cell);
	}

	public void endCell(ICellContent cell) throws BirtException {
		endContainer(cell);

	}

	public void startContainer(IContainerContent container) throws BirtException {
		startContent(container);
	}

	public void endContainer(IContainerContent container) throws BirtException {
		endContent(container);
	}

	public void startText(ITextContent text) throws BirtException {
		startContent(text);
		endContent(text);
	}

	public void startLabel(ILabelContent label) throws BirtException {
		startText(label);
	}

	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		startText(autoText);
	}

	public void startData(IDataContent data) throws BirtException {
		startText(data);
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		startContent(foreign);
		endContent(foreign);
	}

	public void startImage(IImageContent image) throws BirtException {
		startContent(image);
		endContent(image);

	}

	public void endGroup(IGroupContent group) throws BirtException {
		endContainer(group);
	}

	public void startGroup(IGroupContent group) throws BirtException {
		startContainer(group);
	}

	public void endListGroup(IListGroupContent group) throws BirtException {
		endGroup(group);
	}

	public void endTableGroup(ITableGroupContent group) throws BirtException {
		endGroup(group);
	}

	public void startListGroup(IListGroupContent group) throws BirtException {
		startGroup(group);
	}

	public void startTableGroup(ITableGroupContent group) throws BirtException {
		startGroup(group);
	}

	public boolean isMultiplePagesEnabled() {
		return true;
	}
}
