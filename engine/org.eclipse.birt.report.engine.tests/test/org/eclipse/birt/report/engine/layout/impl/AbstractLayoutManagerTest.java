package org.eclipse.birt.report.engine.layout.impl;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;

public abstract class AbstractLayoutManagerTest extends TestCase {

	public AbstractLayoutManagerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected IReportContent createReportContent() {
		return ContentFactory.createReportContent();
	}

	protected ILabelContent createLabelContent(IReportContent report) {
		return createLabelContent(report, "label");
	}

	protected ILabelContent createLabelContent(IReportContent report, String text) {
		ILabelContent label = report.createLabelContent();
		label.setText(text);
		return label;
	}

	protected ICellContent createCellContent(IReportContent report, int colSpan, int rowSpan) {
		ICellContent cell = report.createCellContent();
		cell.setColSpan(colSpan);
		cell.setRowSpan(rowSpan);
		cell.getChildren().add(createLabelContent(report, "colspan:" + colSpan + " rowSpan:" + rowSpan));
		return cell;
	}

	protected ICellContent createCellContent(IReportContent report) {
		return createCellContent(report, 1, 1);
	}

	protected IRowContent createRowContent(IReportContent report, int cellNumber) {
		IRowContent row = report.createRowContent();
		for (int i = 0; i < cellNumber; i++) {
			ICellContent cell = createCellContent(report, 1, 1);
			row.getChildren().add(cell);
			cell.setParent(row);
		}
		return row;
	}

	protected ITableContent createTableContent(IReportContent report, int colNumber, int rowCount) {
		ITableContent table = report.createTableContent();
		for (int i = 0; i < rowCount; i++) {
			ITableBandContent band = report.createTableBandContent();
			band.setBandType(ITableBandContent.BAND_DETAIL);
			IRowContent row = createRowContent(report, colNumber);
			band.getChildren().add(row);
			row.setParent(band);
			table.getChildren().add(band);
			band.setParent(table);
		}
		return table;
	}

}
