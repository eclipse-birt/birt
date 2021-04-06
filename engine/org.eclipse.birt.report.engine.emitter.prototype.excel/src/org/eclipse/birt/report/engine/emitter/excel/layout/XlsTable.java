package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class XlsTable extends XlsContainer {

	private ColumnsInfo columnsInfo;

	public XlsTable(StyleEntry entry, ContainerSizeInfo sizeInfo, XlsContainer parent) {
		super(entry, sizeInfo, parent);
	}

	public XlsTable(ColumnsInfo table, StyleEntry entry, ContainerSizeInfo sizeInfo, XlsContainer parent) {
		this(entry, sizeInfo, parent);
		this.columnsInfo = table;
	}

	public XlsTable(ColumnsInfo table, XlsContainer container) {
		this(table, container.getStyle(), container.getSizeInfo(), container);
	}

	public ContainerSizeInfo getColumnSizeInfo(int column, int span) {
		ContainerSizeInfo sizeInfo = getSizeInfo();
		int startCoordinate = sizeInfo.getStartCoordinate();
		int endCoordinate = sizeInfo.getEndCoordinate();
		int[] columnWidths = columnsInfo.getColumns();

		for (int i = 0; i < column; i++) {
			startCoordinate += columnWidths[i];
		}

		if (startCoordinate >= endCoordinate) {
			return null;
		}

		int width = 0;

		for (int i = column; i < column + span; i++) {
			width += columnWidths[i];
		}
		width = Math.min(width, endCoordinate - startCoordinate);
		return new ContainerSizeInfo(startCoordinate, width);
	}

	public ColumnsInfo getColumnsInfo() {
		return columnsInfo;
	}
}
