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

package org.eclipse.birt.report.tests.engine.emitter.html;

import java.util.HashMap;

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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

/**
 * <b>HTMLEmitter is an extended emitter for test</b>
 * <p>
 * Format: emitter_html
 */
public class HTMLEmitter implements IContentEmitter {

	protected IContentEmitter emitter;

	@Override
	public void initialize(IEmitterServices service) {
		emitter = (IContentEmitter) ((HashMap) service.getRenderContext()).get("emitter_class");
	}

	@Override
	public void end(IReportContent report) throws BirtException {
		emitter.end(report);
	}

	@Override
	public void endCell(ICellContent cell) throws BirtException {
		emitter.endCell(cell);
	}

	@Override
	public void endContainer(IContainerContent container) throws BirtException {
		emitter.endContainer(container);
	}

	@Override
	public void endContent(IContent content) throws BirtException {
		emitter.endContent(content);
	}

	@Override
	public void endGroup(IGroupContent group) throws BirtException {
		emitter.endGroup(group);
	}

	@Override
	public void endList(IListContent list) throws BirtException {
		emitter.endList(list);
	}

	@Override
	public void endListBand(IListBandContent listBand) throws BirtException {
		emitter.endListBand(listBand);
	}

	@Override
	public void endListGroup(IListGroupContent group) throws BirtException {
		emitter.endListGroup(group);
	}

	@Override
	public void endPage(IPageContent page) throws BirtException {
		emitter.endPage(page);
	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		emitter.endRow(row);
	}

	@Override
	public void endTable(ITableContent table) throws BirtException {
		emitter.endTable(table);
	}

	@Override
	public void endTableBand(ITableBandContent band) throws BirtException {
		emitter.endTableBand(band);
	}

	@Override
	public void endTableGroup(ITableGroupContent group) throws BirtException {
		emitter.endTableGroup(group);
	}

	@Override
	public String getOutputFormat() {
		return emitter.getOutputFormat();
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		emitter.start(report);
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		emitter.startAutoText(autoText);
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		emitter.startCell(cell);
	}

	@Override
	public void startContainer(IContainerContent container) throws BirtException {
		emitter.startContainer(container);
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		emitter.startContent(content);

	}

	@Override
	public void startData(IDataContent data) throws BirtException {
		emitter.startData(data);
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		emitter.startForeign(foreign);
	}

	@Override
	public void startGroup(IGroupContent group) throws BirtException {
		emitter.startGroup(group);
	}

	@Override
	public void startImage(IImageContent image) throws BirtException {
		emitter.startImage(image);
	}

	@Override
	public void startLabel(ILabelContent label) throws BirtException {
		emitter.startLabel(label);
	}

	@Override
	public void startList(IListContent list) throws BirtException {
		emitter.startList(list);
	}

	@Override
	public void startListBand(IListBandContent listBand) throws BirtException {
		emitter.startListBand(listBand);
	}

	@Override
	public void startListGroup(IListGroupContent group) throws BirtException {
		emitter.startListGroup(group);
	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		emitter.startPage(page);
	}

	@Override
	public void startRow(IRowContent row) throws BirtException {
		emitter.startRow(row);
	}

	@Override
	public void startTable(ITableContent table) throws BirtException {
		emitter.startTable(table);
	}

	@Override
	public void startTableBand(ITableBandContent band) throws BirtException {
		emitter.startTableBand(band);
	}

	@Override
	public void startTableGroup(ITableGroupContent group) throws BirtException {
		emitter.startTableGroup(group);
	}

	@Override
	public void startText(ITextContent text) throws BirtException {
		emitter.startText(text);
	}

}
