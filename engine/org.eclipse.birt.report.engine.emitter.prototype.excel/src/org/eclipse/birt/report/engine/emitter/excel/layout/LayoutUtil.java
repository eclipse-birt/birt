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

package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;

public class LayoutUtil {
	private static final int DEFAULT_WIDTH = 40000;
	private static final int MIN_WIDTH = 128;

	private static final Logger log = Logger.getLogger(LayoutUtil.class.getName());

	public static ColumnsInfo createTable(int col, int width) {
		return new ColumnsInfo(col, width);
	}

	public static ColumnsInfo createTable(IListContent list, int width, int dpi) {
		width = getElementWidth(list, width, dpi);
		int[] column = new int[] { width };
		return new ColumnsInfo(column);
	}

	public static ColumnsInfo createChart(IForeignContent content, int width, int dpi) {
		ExtendedItemDesign design = (ExtendedItemDesign) content.getGenerateBy();
		DimensionType value = design.getWidth();
		if (value != null) {
			width = getElementWidth(value, width, dpi);
		}
		int[] column = new int[] { width };
		return new ColumnsInfo(column);
	}

	public static ColumnsInfo createImage(int width) {
		int[] column = new int[] { width };
		return new ColumnsInfo(column);
	}

	public static ColumnsInfo createImage(int width, int parentWidth, boolean isCenterAligned) {
		if (!isCenterAligned || parentWidth < width) {
			return createImage(width);
		}

		int[] column = new int[] { (parentWidth - width) / 2, width, (parentWidth - width) / 2 };
		return new ColumnsInfo(column);
	}

	public static int getImageWidth(DimensionType value, int parentWidth, int imageInfoWidth, int dpi) {
		if (value == null) {
			return imageInfoWidth;
		} else {
			return getElementWidth(value, parentWidth, dpi);
		}
	}

	public static int getImageHeight(DimensionType value, int imageInfoHeight, int dpi) {
		return ExcelUtil.convertDimensionType(value, imageInfoHeight, dpi);
	}

	public static int getElementWidth(IContent content, int width, int dpi) {
		DimensionType value = content.getWidth();
		if (value != null) {
			return getElementWidth(value, width, dpi);
		}
		return width;
	}

	private static int getElementWidth(DimensionType contentWidth, int width, int dpi) {
		try {
			width = Math.min(ExcelUtil.convertDimensionType(contentWidth, width, dpi), width);
		} catch (Exception e) {

		}
		return width;
	}

	public static int[] createFixedTable(ITableContent table, int tableWidth, int dpi) {
		int columnCount = table.getColumnCount();
		int[] columns = new int[columnCount];
		int unassignedCount = 0;
		int totalAssigned = 0;

		for (int i = 0; i < columnCount; i++) {
			DimensionType value = table.getColumn(i).getWidth();
			if (value == null) {
				columns[i] = -1;
				unassignedCount++;
			} else {
				columns[i] = ExcelUtil.convertDimensionType(value, tableWidth, dpi);
				totalAssigned += columns[i];
			}
		}

		if (table.getWidth() == null && unassignedCount == 0) {
			return columns;
		}

		columns = EmitterUtil.resizeTableColumn(tableWidth, columns, unassignedCount, totalAssigned);

		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == 0) {
				columns[i] = MIN_WIDTH;
			}
		}
		return columns;
	}

	public static ColumnsInfo createTable(ITableContent table, int width, int dpi, boolean autoExtend) {
		int tableWidth = getElementWidth(table, width, dpi);

		int columnCount = table.getColumnCount();
		if (columnCount == 0) {
			return null;
		}

		int[] columns = new int[columnCount];
		int unassignedCount = 0;
		int totalAssigned = 0;

		for (int i = 0; i < columnCount; i++) {
			DimensionType value = table.getColumn(i).getWidth();
			if (value == null) {
				columns[i] = -1;
				unassignedCount++;
			} else {
				columns[i] = ExcelUtil.convertDimensionType(value, tableWidth, dpi);
				totalAssigned += columns[i];
			}
		}

		int leftWidth = tableWidth - totalAssigned;
		// If autoExtend, don't adjust column width when the total column width
		// exceed the width of parent table.
		if ((leftWidth > 0 || !autoExtend && leftWidth < 0) && unassignedCount == 0 && table.getWidth() != null) {
			int totalResized = 0;
			for (int i = 0; i < columnCount - 1; i++) {
				columns[i] = resize(columns[i], totalAssigned, leftWidth);
				totalResized += columns[i];
			}
			columns[columnCount - 1] = tableWidth - totalResized;
		} else if (leftWidth < 0 && unassignedCount > 0) {
			int totalResized = 0;
			int lastAssignedIndex = 0;
			for (int i = 0; i < columnCount; i++) {
				if (columns[i] == -1) {
					columns[i] = 0;
				} else if (!autoExtend) {
					columns[i] = resize(columns[i], totalAssigned, leftWidth);
					lastAssignedIndex = i;
					totalResized += columns[i];
				}
			}
			if (!autoExtend) {
				columns[lastAssignedIndex] += tableWidth - totalResized;
			}
		} else if (leftWidth >= 0 && unassignedCount > 0) {
			int per = (int) leftWidth / unassignedCount;
			int index = 0;
			for (int i = 0; i < columns.length; i++) {
				if (columns[i] == -1) {
					columns[i] = per;
					index = i;
				}
			}
			columns[index] = leftWidth - per * (unassignedCount - 1);
		}

		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == 0) {
				if (autoExtend) {
					columns[i] = DEFAULT_WIDTH;
				} else {
					columns[i] = MIN_WIDTH;
				}
			}
		}
		return new ColumnsInfo(columns);
	}

	private static int resize(int width, int total, int left) {
		return Math.round(width + (float) width / (float) total * left);
	}
}
