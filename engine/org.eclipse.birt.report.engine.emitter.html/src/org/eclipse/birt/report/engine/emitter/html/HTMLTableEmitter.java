package org.eclipse.birt.report.engine.emitter.html;

/**
 * This class is used by the HTML Report Emitter to calcuate the empty cells.
 * 
 * As the HTML doesn't support column_id of a cell, if a row have empty cells,
 * it requires the tags of the empty cell.
 * 
 * If a table is split into two page among the cell drop, the table in the next
 * page may contains empty cells without cell object. We need output empty cells
 * to fix this case.
 *
 */
public class HTMLTableEmitter {
	public HTMLTableEmitter(HTMLReportEmitter emitter) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTable(org.eclipse
	 * .birt.report.engine.content.ITableContent)
	 */
	public void startTable(int columnCount) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endTable(org.eclipse.
	 * birt.report.engine.content.ITableContent)
	 */
	public void endTable() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableHeader(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableBand() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableHeader(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableBand() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startRow(org.eclipse.
	 * birt.report.engine.content.IRowContent)
	 */
	public void startRow() {
	}

	public void endRow() {
	}

	public void startCell(int rowId, int colId, int rowSpan, int colSpan) {
	}
}
