/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.util.TableBorderCollisionArbiter;
import org.eclipse.draw2d.geometry.Insets;

/**
 * A helper class cooperate to provide cell border calculation.
 */

public class TableBorderHelper {

	private ITableLayoutOwner owner;

	private int[][] heights, widths;

	/**
	 * Use to store all actual border drawing data, array size:
	 * [2*colCount*rowCount+colCount+rowCount][5], the last dimension arranged as:
	 * [style][width][color][rowIndex][colIndex], index is Zero-based.
	 */
	private int[][] borderData;

	/**
	 * The constructor.
	 * 
	 * @param owner
	 */
	public TableBorderHelper(ITableLayoutOwner owner) {
		this.owner = owner;
	}

	/**
	 * Initialize the helper.
	 */
	private void initialize() {
		int rowCount = owner.getRowCount();
		int colCount = owner.getColumnCount();

		heights = new int[colCount][rowCount + 1];
		widths = new int[rowCount][colCount + 1];

		borderData = new int[2 * colCount * rowCount + colCount + rowCount][6];

		// initialize all index data as -1.
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				// top
				borderData[i * (2 * colCount + 1) + j][3] = -1;
				borderData[i * (2 * colCount + 1) + j][4] = -1;
				borderData[i * (2 * colCount + 1) + j][5] = -1;

				// bottom
				borderData[(i + 1) * (2 * colCount + 1) + j][3] = -1;
				borderData[(i + 1) * (2 * colCount + 1) + j][4] = -1;
				borderData[(i + 1) * (2 * colCount + 1) + j][5] = -1;

				// left
				borderData[i * (2 * colCount + 1) + colCount + j][3] = -1;
				borderData[i * (2 * colCount + 1) + colCount + j][4] = -1;
				borderData[i * (2 * colCount + 1) + colCount + j][5] = -1;

				// right
				borderData[i * (2 * colCount + 1) + colCount + j + 1][3] = -1;
				borderData[i * (2 * colCount + 1) + colCount + j + 1][4] = -1;
				borderData[i * (2 * colCount + 1) + colCount + j + 1][5] = -1;
			}
		}

		// use table border data to initialize.
		TableBorder tableBorder = (TableBorder) owner.getFigure().getBorder();
		Insets tableBorderInsets = tableBorder.getTrueBorderInsets();

		for (int i = 0; i < colCount; i++) {
			heights[i][0] = tableBorderInsets.top;
			heights[i][rowCount] = tableBorderInsets.bottom;
		}

		for (int i = 0; i < rowCount; i++) {
			widths[i][0] = tableBorderInsets.left;
			widths[i][colCount] = tableBorderInsets.right;
		}

		// initialize all other border data.
		for (Iterator itr = owner.getChildren().iterator(); itr.hasNext();) {
			ITableLayoutCell cellPart = (ITableLayoutCell) itr.next();

			int rowIndex = cellPart.getRowNumber();
			int colIndex = cellPart.getColumnNumber();
			int rowSpan = cellPart.getRowSpan();
			int colSpan = cellPart.getColSpan();

			CellBorder border = (CellBorder) cellPart.getFigure().getBorder();
			Insets ins = border.getTrueBorderInsets();

			int topStyle = border.getTopBorderStyle();
			int topWidth = border.getTopBorderWidth();
			int topColor = border.getTopBorderColor();
			int topFrom = border.getTopFrom();

			int bottomStyle = border.getBottomBorderStyle();
			int bottomWidth = border.getBottomBorderWidth();
			int bottomColor = border.getBottomBorderColor();
			int bottomFrom = border.getBottomFrom();

			int leftStyle = border.getLeftBorderStyle();
			int leftWidth = border.getLeftBorderWidth();
			int leftColor = border.getLeftBorderColor();

			int rightStyle = border.getRightBorderStyle();
			int rightWidth = border.getRightBorderWidth();
			int rightColor = border.getRightBorderColor();

			for (int i = 0; i < colSpan; i++) {
				// update border data using collision arbiter.
				TableBorderCollisionArbiter.refreshBorderData(
						borderData[(rowIndex - 1) * (2 * colCount + 1) + colIndex - 1 + i], topStyle, topWidth,
						topColor, rowIndex - 1, colIndex - 1 + i, topFrom);

				TableBorderCollisionArbiter.refreshBorderData(
						borderData[(rowIndex + rowSpan - 1) * (2 * colCount + 1) + colIndex - 1 + i], bottomStyle,
						bottomWidth, bottomColor, rowIndex - 1 + rowSpan - 1, colIndex - 1 + i, bottomFrom);

				// update border insets data.
				heights[colIndex - 1 + i][rowIndex - 1] = Math.max(heights[colIndex - 1 + i][rowIndex - 1], ins.top);
				heights[colIndex - 1 + i][rowIndex + rowSpan - 1] = Math
						.max(heights[colIndex - 1 + i][rowIndex + rowSpan - 1], ins.bottom);
			}

			for (int i = 0; i < rowSpan; i++) {
				// update border data using collision arbiter.
				TableBorderCollisionArbiter.refreshBorderData(
						borderData[(rowIndex - 1 + i) * (2 * colCount + 1) + colCount + colIndex - 1], leftStyle,
						leftWidth, leftColor, rowIndex - 1 + i, colIndex - 1);

				TableBorderCollisionArbiter.refreshBorderData(
						borderData[(rowIndex - 1 + i) * (2 * colCount + 1) + colCount + colIndex + colSpan - 1],
						rightStyle, rightWidth, rightColor, rowIndex - 1 + i, colIndex - 1 + colSpan - 1);

				// update border insets data.
				widths[rowIndex - 1 + i][colIndex - 1] = Math.max(widths[rowIndex - 1 + i][colIndex - 1], ins.left);
				widths[rowIndex - 1 + i][colIndex + colSpan - 1] = Math
						.max(widths[rowIndex - 1 + i][colIndex + colSpan - 1], ins.right);
			}
		}

		// if table border has set, use it.
		int tableTopStyle = tableBorder.getTopBorderStyle();
		int tableTopWidth = tableBorder.getTopBorderWidth();
		if (tableTopStyle != 0 && tableTopWidth > 0) {
			int tableTopColor = tableBorder.getTopBorderColor();

			for (int i = 0; i < colCount; i++) {
				borderData[i][0] = tableTopStyle;
				borderData[i][1] = tableTopWidth;
				borderData[i][2] = tableTopColor;
				borderData[i][3] = -2;
				borderData[i][4] = -2;
			}
		}

		int tableBottomStyle = tableBorder.getBottomBorderStyle();
		int tableBottomWidth = tableBorder.getBottomBorderWidth();
		if (tableBottomStyle != 0 && tableBottomWidth > 0) {
			int tableBottomColor = tableBorder.getBottomBorderColor();

			for (int i = 0; i < colCount; i++) {
				borderData[2 * colCount * rowCount + rowCount + i][0] = tableBottomStyle;
				borderData[2 * colCount * rowCount + rowCount + i][1] = tableBottomWidth;
				borderData[2 * colCount * rowCount + rowCount + i][2] = tableBottomColor;
				borderData[2 * colCount * rowCount + rowCount + i][3] = -2;
				borderData[2 * colCount * rowCount + rowCount + i][4] = -2;
			}
		}

		int tableLeftStyle = tableBorder.getLeftBorderStyle();
		int tableLeftWidth = tableBorder.getLeftBorderWidth();
		if (tableLeftStyle != 0 && tableLeftWidth > 0) {
			int tableLeftColor = tableBorder.getLeftBorderColor();

			for (int i = 0; i < rowCount; i++) {
				borderData[(2 * colCount + 1) * i + colCount][0] = tableLeftStyle;
				borderData[(2 * colCount + 1) * i + colCount][1] = tableLeftWidth;
				borderData[(2 * colCount + 1) * i + colCount][2] = tableLeftColor;
				borderData[(2 * colCount + 1) * i + colCount][3] = -2;
				borderData[(2 * colCount + 1) * i + colCount][4] = -2;
			}
		}

		int tableRightStyle = tableBorder.getRightBorderStyle();
		int tableRightWidth = tableBorder.getRightBorderWidth();
		if (tableRightStyle != 0 && tableRightWidth > 0) {
			int tableRightColor = tableBorder.getRightBorderColor();

			for (int i = 0; i < rowCount; i++) {
				borderData[(2 * colCount + 1) * i + 2 * colCount][0] = tableRightStyle;
				borderData[(2 * colCount + 1) * i + 2 * colCount][1] = tableRightWidth;
				borderData[(2 * colCount + 1) * i + 2 * colCount][2] = tableRightColor;
				borderData[(2 * colCount + 1) * i + 2 * colCount][3] = -2;
				borderData[(2 * colCount + 1) * i + 2 * colCount][4] = -2;
			}
		}

	}

	/**
	 * Updates all cell border insets.
	 */
	public void updateCellBorderInsets() {
		if (heights == null || widths == null) {
			initialize();
		}

		int rowCount = owner.getRowCount();
		int colCount = owner.getColumnCount();

		for (Iterator itr = owner.getChildren().iterator(); itr.hasNext();) {
			ITableLayoutCell cellPart = (ITableLayoutCell) itr.next();

			int rowIndex = cellPart.getRowNumber();
			int colIndex = cellPart.getColumnNumber();
			int rowSpan = cellPart.getRowSpan();
			int colSpan = cellPart.getColSpan();

			CellBorder border = (CellBorder) cellPart.getFigure().getBorder();

			Insets borderInsets = new Insets();

			// if it's a toppest and bottomest cell, don't give it the insets,
			// job is handled by Table border.
			if (rowIndex == 1 && (rowIndex + rowSpan - 1) == rowCount) {
				// borderInsets.top = 0;
				// borderInsets.bottom = 0;

				int th = 0;
				int bh = 0;

				for (int i = 0; i < colSpan; i++) {
					th = Math.max(th, heights[colIndex - 1 + i][rowIndex - 1]);
					bh = Math.max(bh, heights[colIndex - 1 + i][rowIndex + rowSpan - 1]);
				}

				borderInsets.top = th;
				borderInsets.bottom = bh;
			} else if (rowIndex == 1) {
				// if it's the toppest cell, don't give the top insets, but set
				// the bottom insets.

				// borderInsets.top = 0;

				int bh = 0;
				int th = 0;

				for (int i = 0; i < colSpan; i++) {
					th = Math.max(th, heights[colIndex - 1 + i][rowIndex - 1]);
					bh = Math.max(bh, heights[colIndex - 1 + i][rowIndex + rowSpan - 1] / 2
							+ heights[colIndex - 1 + i][rowIndex + rowSpan - 1] % 2);
				}

				borderInsets.top = th;
				borderInsets.bottom = bh;
			} else if ((rowIndex + rowSpan - 1) == rowCount) {
				// if it's the bottomest cell, don't give the bottom insets, but
				// set the top insets.

				int th = 0;
				int bh = 0;

				for (int i = 0; i < colSpan; i++) {
					th = Math.max(th, heights[colIndex - 1 + i][rowIndex - 1] / 2);
					bh = Math.max(bh, heights[colIndex - 1 + i][rowIndex + rowSpan - 1]);
				}

				borderInsets.top = th;
				borderInsets.bottom = bh;

				// borderInsets.bottom = 0;
			} else {
				// if neigher the toppest nor the bottomest cell, both set the
				// top and bottom insets.

				int bh = 0;
				int th = 0;

				for (int i = 0; i < colSpan; i++) {
					th = Math.max(th, heights[colIndex - 1 + i][rowIndex - 1] / 2);
					bh = Math.max(bh, heights[colIndex - 1 + i][rowIndex + rowSpan - 1] / 2
							+ heights[colIndex - 1 + i][rowIndex + rowSpan - 1] % 2);
				}

				borderInsets.top = th;
				borderInsets.bottom = bh;
			}

			// if it's a leftest and rightest cell, don't give it the insets,
			// job is handled by Table border.
			if (colIndex == 1 && (colIndex + colSpan - 1) == colCount) {
				// borderInsets.left = 0;
				// borderInsets.right = 0;

				int rw = 0;
				int lw = 0;

				for (int i = 0; i < rowSpan; i++) {
					rw = Math.max(rw, widths[rowIndex - 1 + i][colIndex + colSpan - 1]);
					lw = Math.max(lw, widths[rowIndex - 1 + i][colIndex - 1]);
				}

				borderInsets.left = lw;
				borderInsets.right = rw;

			} else if (colIndex == 1) {
				// if it's the leftest cell, don't give the left insets, but set
				// the right insets.

				// borderInsets.left = 0;

				int rw = 0;
				int lw = 0;

				for (int i = 0; i < rowSpan; i++) {
					lw = Math.max(lw, widths[rowIndex - 1 + i][colIndex - 1]);
					rw = Math.max(rw, widths[rowIndex - 1 + i][colIndex + colSpan - 1] / 2
							+ widths[rowIndex - 1 + i][colIndex + colSpan - 1] % 2);
				}

				borderInsets.left = lw;
				borderInsets.right = rw;
			} else if ((colIndex + colSpan - 1) == colCount) {
				// if it's the rightest cell, don't give the right insets, but
				// set the left insets.

				int lw = 0;
				int rw = 0;

				for (int i = 0; i < rowSpan; i++) {
					lw = Math.max(lw, widths[rowIndex - 1 + i][colIndex - 1] / 2);
					rw = Math.max(rw, widths[rowIndex - 1 + i][colIndex + colSpan - 1]);
				}

				borderInsets.left = lw;
				borderInsets.right = rw;

				// borderInsets.right = 0;
			} else {
				// if neigher the leftest nor the rightest cell, both set the
				// left and right insets.

				int rw = 0;
				int lw = 0;

				for (int i = 0; i < rowSpan; i++) {
					rw = Math.max(rw, widths[rowIndex - 1 + i][colIndex + colSpan - 1] / 2
							+ widths[rowIndex - 1 + i][colIndex + colSpan - 1] % 2);
					lw = Math.max(lw, widths[rowIndex - 1 + i][colIndex - 1] / 2);
				}

				borderInsets.left = lw;
				borderInsets.right = rw;
			}

			border.setBorderInsets(borderInsets);
		}

	}

	/**
	 * Returns the actual border drawing data. especially for TableBorderLayer.
	 * 
	 * @return
	 */
	public int[][] getBorderData() {
		if (borderData == null) {
			initialize();
		}

		return borderData;
	}

}
