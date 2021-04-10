
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

	public String getOutputFormat() {
		return "text/xml";
	}

	public void initialize(IEmitterServices service) {
	}

	public void start(IReportContent report) {

		writer.openTag("report");

	}

	public void end(IReportContent report) {
		writer.closeTag("report");

	}

	public void startPage(IPageContent page) {
		writer.openTag("page");
	}

	public void endPage(IPageContent page) {
		writer.closeTag("page");
	}

	public void startTable(ITableContent table) {
		writer.openTag("table");
	}

	public void endTable(ITableContent table) {
		writer.closeTag("table");
	}

	public void startRow(IRowContent row) {
		writer.openTag("row");
	}

	public void endRow(IRowContent row) {
		writer.closeTag("row");
	}

	public void startCell(ICellContent cell) {
		writer.openTag("cell");
	}

	public void endCell(ICellContent cell) {
		writer.closeTag("cell");
	}

	public void startContainer(IContainerContent container) {
		writer.openTag("container");
	}

	public void startText(ITextContent text) {
		writer.openTag("text");
		writer.closeTag("text");
	}

	public void startData(IDataContent data) {
		writer.openTag("data");
		writer.closeTag("data");
	}

	public void startLabel(ILabelContent label) {
		writer.openTag("label");
		writer.closeTag("label");
	}

	public void startAutoText(IAutoTextContent autoText) {
		writer.openTag("auto-text");
		writer.closeTag("auto-text");
	}

	public void startForeign(IForeignContent foreign) {
		writer.openTag("foreign");
		writer.closeTag("foreign");
	}

	public void startImage(IImageContent image) {
		writer.openTag("image");
		writer.closeTag("image");
	}

	public void endList(IListContent list) {
		writer.closeTag("list");
	}

	public void endListBand(IListBandContent listBand) {
		writer.closeTag("list-band");
	}

	public void endTableBand(ITableBandContent band) {
		writer.closeTag("table-band");
	}

	public void startList(IListContent list) {
		writer.openTag("list");
	}

	public void startListBand(IListBandContent listBand) {
		writer.openTag("list-band");
	}

	public void startTableBand(ITableBandContent band) {
		writer.openTag("table-band");
	}

	public void endGroup(IGroupContent group) {
		writer.closeTag("group");
	}

	public void startGroup(IGroupContent group) {
		writer.openTag("group");
	}
}
