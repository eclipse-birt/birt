package org.eclipse.birt.report.engine.emitter.excel.layout;

public class ColumnsInfo {
	// Each column width
	private int[] columns;
	// the total width
	private int totalWidth;

	public ColumnsInfo(int colcount, int width) {
		columns = new int[colcount];
		int per = (int) width / colcount;

		for (int i = 0; i < columns.length - 1; i++) {
			columns[i] = per;
		}

		columns[colcount - 1] = width - (per * (colcount - 1));
	}

	public ColumnsInfo(int[] columns) {
		this.columns = columns;

		for (int i = 0; i < columns.length; i++) {
			totalWidth += columns[i];
		}
	}

	public int getColumnCount() {
		return columns.length;
	}

	public int getColumnWidth(int index) {
		return columns[index];
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public int[] getColumns() {
		return columns;
	}
}
