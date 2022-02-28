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

package org.eclipse.birt.report.engine.presentation;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.XMLWriter;

public class XMLContentWriter extends ContentEmitterAdapter {

	XMLWriter writer;

	@Override
	public String getOutputFormat() {
		return "text/xml";
	}

	@Override
	public void initialize(IEmitterServices service) {
	}

	@Override
	public void start(IReportContent report) {

		writer.openTag("report");

	}

	@Override
	public void end(IReportContent report) {
		writer.closeTag("report");

	}

	@Override
	public void startPage(IPageContent page) {
		writer.openTag("page");
	}

	@Override
	public void endPage(IPageContent page) {
		writer.closeTag("page");
	}

	@Override
	public void startTable(ITableContent table) {
		writer.openTag("table");
	}

	@Override
	public void endTable(ITableContent table) {
		writer.closeTag("table");
	}

	@Override
	public void startRow(IRowContent row) {
		writer.openTag("row");
	}

	@Override
	public void endRow(IRowContent row) {
		writer.closeTag("row");
	}

	@Override
	public void startCell(ICellContent cell) {
		writer.openTag("cell");
	}

	@Override
	public void endCell(ICellContent cell) {
		writer.closeTag("cell");
	}

	@Override
	public void startContainer(IContainerContent container) {
		writer.openTag("container");
	}

	@Override
	public void startText(ITextContent text) {
		writer.openTag("text");
		writer.closeTag("text");
	}

	@Override
	public void startData(IDataContent data) {
		writer.openTag("data");
		writer.closeTag("data");
	}

	@Override
	public void startLabel(ILabelContent label) {
		writer.openTag("label");
		writer.closeTag("label");
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) {
		writer.openTag("auto-text");
		writer.closeTag("auto-text");
	}

	@Override
	public void startForeign(IForeignContent foreign) {
		writer.openTag("foreign");
		writer.closeTag("foreign");
	}

	@Override
	public void startImage(IImageContent image) {
		writer.openTag("image");
		writer.closeTag("image");
	}

	@Override
	public void endList(IListContent list) {
		writer.closeTag("list");
	}

	@Override
	public void endListBand(IListBandContent listBand) {
		writer.closeTag("list-band");
	}

	@Override
	public void endTableBand(ITableBandContent band) {
		writer.closeTag("table-band");
	}

	@Override
	public void startList(IListContent list) {
		writer.openTag("list");
	}

	@Override
	public void startListBand(IListBandContent listBand) {
		writer.openTag("list-band");
	}

	@Override
	public void startTableBand(ITableBandContent band) {
		writer.openTag("table-band");
	}

	@Override
	public void endGroup(IGroupContent group) {
		writer.closeTag("group");
	}

	@Override
	public void startGroup(IGroupContent group) {
		writer.openTag("group");
	}
}
