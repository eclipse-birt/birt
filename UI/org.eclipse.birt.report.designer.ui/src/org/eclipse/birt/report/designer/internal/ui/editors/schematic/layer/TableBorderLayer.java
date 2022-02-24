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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BorderUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorderHelper;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.TableBorderCollisionArbiter;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;

/**
 * A table layer to draw all the cell borders.
 */

public class TableBorderLayer extends FreeformLayer {

	private AbstractTableEditPart source;

	private int rowCount, colCount;

	/**
	 * Use to store all actual border drawing data, array size:
	 * [2*colCount*rowCount+colCount+rowCount][5], the last dimension arranged as:
	 * [style][width][color][rowIndex][colIndex], index is Zero-based.
	 */
	private int[][] borderData;

	/**
	 * The constructor.
	 * 
	 * @param source
	 */
	public TableBorderLayer(AbstractTableEditPart source) {
		super();
		this.source = source;
		setOpaque(false);
		setRequestFocusEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		IFigure figure = source.getLayer(LayerConstants.PRIMARY_LAYER);

		TableBorderHelper helper = ((TableLayout) figure.getLayoutManager()).getBorderHelper();

		if (helper == null) {
			return;
		}

		borderData = helper.getBorderData();

		rowCount = source.getRowCount();
		colCount = source.getColumnCount();

		if (borderData == null || borderData.length != (2 * colCount * rowCount + colCount + rowCount)) {
			return;
		}
		for (int i = 0; i < rowCount; i++) {
			int y = TableUtil.caleY(source, i + 1);
			int h = caleVisualHeight(i + 1);

			for (int j = 0; j < colCount; j++) {
				int bottomIndex = (i + 1) * (2 * colCount + 1) + j;
				int rightIndex = i * (2 * colCount + 1) + colCount + j + 1;

				int topIndex = i * (2 * colCount + 1) + j;
				int leftIndex = i * (2 * colCount + 1) + colCount + j;

				int x = TableUtil.caleX(source, j + 1);
				int w = caleVisualWidth(j + 1);

				// Only need to draw the right/bottom edge for single line
				// pattern.

				// if ( j < colCount - 1 )
				if (j == 0) {
					drawLeft(graphics, i, j, x, y, w, h, borderData[leftIndex]);
				}

				drawRight(graphics, i, j, x, y, w, h, borderData[rightIndex]);

				// if ( i < rowCount - 1 )
				if (i == 0) {
					drawTop(graphics, i, j, x, y, w, h, borderData[topIndex]);
				}

				drawBottom(graphics, i, j, x, y, w, h, borderData[bottomIndex]);

			}
		}
	}

	private int caleVisualWidth(int columnIndex) {
		IFigure figure = source.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);

		if (data == null) {
			return 0;
		}

		if (columnIndex <= data.columnWidths.length) {
			return data.findColumnData(columnIndex).width;
		}

		return 0;
	}

	private int caleVisualHeight(int rowIndex) {
		IFigure figure = source.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);

		if (data == null) {
			return 0;
		}

		if (rowIndex <= data.rowHeights.length) {
			return data.findRowData(rowIndex).height;
		}
		return 0;
	}

	/**
	 * @param g
	 * @param rowIndex
	 * @param colIndex
	 * @param data     [style][width][color][rowIndex][colIndex].
	 */
	private void drawBottom(Graphics g, int rowIndex, int colIndex, int x, int y, int w, int h, int[] data) {
		if (data[0] == 0 && data[1] == 0) {
			return;
		}

		int nLeftWidth = 0;
		int nLeftStyle = 0;
		int nLeftX = -1;
		int nLeftY = -1;

		if (colIndex > 0) {
			int[] nLeft = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex - 1];
			nLeftWidth = nLeft[1];
			nLeftStyle = nLeft[0];
			nLeftX = nLeft[3];
			nLeftY = nLeft[4];
		}

		int nRightWidth = 0;
		int nRightStyle = 0;
		int nRightX = -1;
		int nRightY = -1;

		if (colIndex < colCount - 1) {
			int[] nRight = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex + 1];
			nRightWidth = nRight[1];
			nRightStyle = nRight[0];
			nRightX = nRight[3];
			nRightY = nRight[4];
		}

		int nLeftTopWidth = 0;
		int nLeftTopStyle = 0;
		int nLeftTopX = -1;
		int nLeftTopY = -1;
		if (rowIndex >= 0 && colIndex >= 0) {
			int[] nLeftTop = borderData[(rowIndex) * (2 * colCount + 1) + colCount + colIndex];
			nLeftTopWidth = nLeftTop[1];
			nLeftTopStyle = nLeftTop[0];
			nLeftTopX = nLeftTop[3];
			nLeftTopY = nLeftTop[4];
		}

		int nLeftBottomWidth = 0;
		int nLeftBottomStyle = 0;
		int nLeftBottomX = -1;
		int nLeftBottomY = -1;
		if (rowIndex < rowCount - 1 && colIndex >= 0) {
			int[] nLeftBottom = borderData[(rowIndex + 1) * (2 * colCount + 1) + colCount + colIndex];
			nLeftBottomWidth = nLeftBottom[1];
			nLeftBottomStyle = nLeftBottom[0];
			nLeftBottomX = nLeftBottom[3];
			nLeftBottomY = nLeftBottom[4];
		}

		int nRightTopWidth = 0;
		int nRightTopStyle = 0;
		int nRightTopX = -1;
		int nRightTopY = -1;
		if (rowIndex >= 0 && colIndex <= colCount - 1) {
			int[] nRightTop = borderData[(rowIndex) * (2 * colCount + 1) + colCount + colIndex + 1];
			nRightTopWidth = nRightTop[1];
			nRightTopStyle = nRightTop[0];
			nRightTopX = nRightTop[3];
			nRightTopY = nRightTop[4];
		}

		int nRightBottomWidth = 0;
		int nRightBottomStyle = 0;
		int nRightBottomX = -1;
		int nRightBottomY = -1;
		if (rowIndex < rowCount - 1 && colIndex <= colCount - 1) {
			int[] nRightBottom = borderData[(rowIndex + 1) * (2 * colCount + 1) + colCount + colIndex + 1];
			nRightBottomWidth = nRightBottom[1];
			nRightBottomStyle = nRightBottom[0];
			nRightBottomX = nRightBottom[3];
			nRightBottomY = nRightBottom[4];
		}

		int nlexWidth = Math.max(nLeftTopWidth, nLeftBottomWidth);

		boolean exLeft = false;
		boolean exRight = false;

		int rx = (colIndex == 0) ? (x + nlexWidth) : (x + nlexWidth / 2);
		int rw = (colIndex == 0) ? (w - nlexWidth) : (w - nlexWidth / 2);

		if (((TableBorderCollisionArbiter.canExtend(data, nLeftWidth, nLeftStyle, nLeftBottomWidth, nLeftBottomStyle,
				nLeftBottomX, nLeftBottomY, nLeftTopWidth, nLeftTopStyle, nLeftTopX, nLeftTopY, true, false)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nLeftWidth, nLeftStyle, nLeftX, nLeftY,
						nLeftBottomWidth, nLeftBottomStyle, nLeftBottomX, nLeftBottomY, nLeftTopWidth, nLeftTopStyle,
						nLeftTopX, nLeftTopY, true, false))
				&& (nLeftTopX != -2 && nLeftBottomX != -2)) || (data[3] == -2)) {
			rx = (colIndex == 0) ? (x) : (x - nlexWidth / 2 - nlexWidth % 2);
			rw = (colIndex == 0) ? (w) : (w + nlexWidth / 2 + nlexWidth % 2);

			exLeft = true;
		}

		int nrexWidth = Math.max(nRightTopWidth, nRightBottomWidth);

		if (((TableBorderCollisionArbiter.canExtend(data, nRightWidth, nRightStyle, nRightTopWidth, nRightTopStyle,
				nRightTopX, nRightTopY, nRightBottomWidth, nRightBottomStyle, nRightBottomX, nRightBottomY, false,
				false)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nRightWidth, nRightStyle, nRightX, nRightY,
						nRightTopWidth, nRightTopStyle, nRightTopX, nRightTopY, nRightBottomWidth, nRightBottomStyle,
						nRightBottomX, nRightBottomY, false, false))
				&& (nRightTopX != -2 && nRightBottomX != -2)) || (data[3] == -2)) {
			rw += ((colIndex == colCount - 1) ? (0) : (nrexWidth / 2));

			exRight = true;
		} else {
			rw -= ((colIndex == colCount - 1) ? (nrexWidth) : (nrexWidth / 2 + nrexWidth % 2));
		}

		int direction = BorderUtil.BOTTOM;
		int[] widths = new int[] { 0, data[1], 0, 0 };

		if (data[3] == nLeftTopX && data[4] == nLeftTopY && data[3] == nRightTopX && data[4] == nRightTopY) {
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = exLeft ? nLeftTopWidth : 0;
			widths[3] = exRight ? nRightTopWidth : 0;
		} else if (data[3] == nLeftTopX && data[4] == nLeftTopY) {
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = exLeft ? nLeftTopWidth : 0;
			widths[3] = 0;
		} else if (data[3] == nRightTopX && data[4] == nRightTopY) {
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = 0;
			widths[3] = exRight ? nRightTopWidth : 0;
		} else if (data[3] == nLeftBottomX && data[4] == nLeftBottomY && data[3] == nRightBottomX
				&& data[4] == nRightBottomY) {
			direction = BorderUtil.TOP;
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = exLeft ? nLeftBottomWidth : 0;
			widths[3] = exRight ? nRightBottomWidth : 0;
		} else if (data[3] == nLeftBottomX && data[4] == nLeftBottomY) {
			direction = BorderUtil.TOP;
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = exLeft ? nLeftBottomWidth : 0;
			widths[3] = 0;
		} else if (data[3] == nRightBottomX && data[4] == nRightBottomY) {
			direction = BorderUtil.TOP;
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = exRight ? nRightBottomWidth : 0;
		} else if (data[3] != rowIndex || data[4] != colIndex) {
			// default border.
			direction = BorderUtil.TOP;
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = 0;
		}

		Rectangle r = new Rectangle(rx, y + h - data[1] / 2 - data[1] % 2, rw, data[1]);

		if (rowIndex == rowCount - 1) {
			r.y = y + h - data[1];
		}

		if (data[0] != 0) {
			g.setForegroundColor(ColorManager.getColor(data[2]));
			BorderUtil.drawBorderLine(g, direction, data[0], widths, r);
		} else if (data[1] > 0) {
			// draw default border;
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			BorderUtil.drawDefaultLine(g, direction, r);
		}
	}

	/**
	 * @param g
	 * @param rowIndex
	 * @param colIndex
	 * @param data     [style][width][color][rowIndex][colIndex].
	 */
	private void drawRight(Graphics g, int rowIndex, int colIndex, int x, int y, int w, int h, int[] data) {
		if (data[0] == 0 && data[1] == 0) {
			return;
		}

		int nTopWidth = 0;
		int nTopStyle = 0;
		int nTopX = -1;
		int nTopY = -1;

		if (rowIndex > 0) {
			int[] nTop = borderData[(rowIndex - 1) * (2 * colCount + 1) + colCount + colIndex + 1];
			nTopWidth = nTop[1];
			nTopStyle = nTop[0];
			nTopX = nTop[3];
			nTopY = nTop[4];
		}

		int nBottomWidth = 0;
		int nBottomStyle = 0;
		int nBottomX = -1;
		int nBottomY = -1;

		if (rowIndex < rowCount - 1) {
			int[] nBottom = borderData[(rowIndex + 1) * (2 * colCount + 1) + colCount + colIndex + 1];
			nBottomWidth = nBottom[1];
			nBottomStyle = nBottom[0];
			nBottomX = nBottom[3];
			nBottomY = nBottom[4];
		}

		int nTopLeftWidth = 0;
		int nTopLeftStyle = 0;
		int nTopLeftX = -1;
		int nTopLeftY = -1;
		if (colIndex <= colCount - 1 && rowIndex >= 0) {
			int[] nTopLeft = borderData[rowIndex * (2 * colCount + 1) + colIndex];
			nTopLeftWidth = nTopLeft[1];
			nTopLeftStyle = nTopLeft[0];
			nTopLeftX = nTopLeft[3];
			nTopLeftY = nTopLeft[4];
		}

		int nTopRightWidth = 0;
		int nTopRightStyle = 0;
		int nTopRightX = -1;
		int nTopRightY = -1;
		if (colIndex < colCount - 1 && rowIndex >= 0) {
			int[] nTopRight = borderData[rowIndex * (2 * colCount + 1) + colIndex + 1];
			nTopRightWidth = nTopRight[1];
			nTopRightStyle = nTopRight[0];
			nTopRightX = nTopRight[3];
			nTopRightY = nTopRight[4];
		}

		int nBottomLeftWidth = 0;
		int nBottomLeftStyle = 0;
		int nBottomLeftX = -1;
		int nBottomLeftY = -1;
		if (colIndex <= colCount - 1 && rowIndex <= rowCount - 1) {
			int[] nBottomLeft = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex];
			nBottomLeftWidth = nBottomLeft[1];
			nBottomLeftStyle = nBottomLeft[0];
			nBottomLeftX = nBottomLeft[3];
			nBottomLeftY = nBottomLeft[4];
		}

		int nBottomRightWidth = 0;
		int nBottomRightStyle = 0;
		int nBottomRightX = -1;
		int nBottomRightY = -1;
		if (colIndex < colCount - 1 && rowIndex <= rowCount - 1) {
			int[] nBottomRight = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex + 1];
			nBottomRightWidth = nBottomRight[1];
			nBottomRightStyle = nBottomRight[0];
			nBottomRightX = nBottomRight[3];
			nBottomRightY = nBottomRight[4];
		}

		int ntexWidth = Math.max(nTopLeftWidth, nTopRightWidth);

		boolean exTop = false;
		boolean exBottom = false;

		int ry = (rowIndex == 0) ? (y + ntexWidth) : (y + ntexWidth / 2);
		int rh = (rowIndex == 0) ? (h - ntexWidth) : (h - ntexWidth / 2);

		if (((TableBorderCollisionArbiter.canExtend(data, nTopWidth, nTopStyle, nTopLeftWidth, nTopLeftStyle, nTopLeftX,
				nTopLeftY, nTopRightWidth, nTopRightStyle, nTopRightX, nTopRightY, true, true)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nTopWidth, nTopStyle, nTopX, nTopY, nTopLeftWidth,
						nTopLeftStyle, nTopLeftX, nTopLeftY, nTopRightWidth, nTopRightStyle, nTopRightX, nTopRightY,
						true, true))
				&& (nTopLeftX != -2 && nTopRightX != -2)) || (data[3] == -2)) {
			ry = (rowIndex == 0) ? (y) : (y - ntexWidth / 2 - ntexWidth % 2);
			rh = (rowIndex == 0) ? (h) : (h + ntexWidth / 2 + ntexWidth % 2);

			exTop = true;
		}

		int nbexWidth = Math.max(nBottomLeftWidth, nBottomRightWidth);

		if (((TableBorderCollisionArbiter.canExtend(data, nBottomWidth, nBottomStyle, nBottomRightWidth,
				nBottomRightStyle, nBottomRightX, nBottomRightY, nBottomLeftWidth, nBottomLeftStyle, nBottomLeftX,
				nBottomLeftY, false, true)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nBottomWidth, nBottomStyle, nBottomX, nBottomY,
						nBottomRightWidth, nBottomRightStyle, nBottomRightX, nBottomRightY, nBottomLeftWidth,
						nBottomLeftStyle, nBottomLeftX, nBottomLeftY, false, true))
				&& (nBottomLeftX != -2 && nBottomRightX != -2)) || (data[3] == -2)) {
			rh += ((rowIndex == rowCount - 1) ? (0) : (nbexWidth / 2));

			exBottom = true;
		} else {
			rh -= ((rowIndex == rowCount - 1) ? (nbexWidth) : (nbexWidth / 2 + nbexWidth % 2));
		}

		int direction = BorderUtil.RIGHT;
		int[] widths = new int[] { 0, 0, 0, data[1] };

		if (data[3] == nTopLeftX && data[4] == nTopLeftY && data[3] == nBottomLeftX && data[4] == nBottomLeftY) {
			widths[0] = exTop ? nTopLeftWidth : 0;
			widths[1] = exBottom ? nBottomLeftWidth : 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nTopLeftX && data[4] == nTopLeftY) {
			widths[0] = exTop ? nTopLeftWidth : 0;
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nBottomLeftX && data[4] == nBottomLeftY) {
			widths[0] = 0;
			widths[1] = exBottom ? nBottomLeftWidth : 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nTopRightX && data[4] == nTopRightY && data[3] == nBottomRightX
				&& data[4] == nBottomRightY) {
			direction = BorderUtil.LEFT;
			widths[0] = exTop ? nTopRightWidth : 0;
			widths[1] = exBottom ? nBottomRightWidth : 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] == nTopRightX && data[4] == nTopRightY) {
			direction = BorderUtil.LEFT;
			widths[0] = exTop ? nTopRightWidth : 0;
			widths[1] = 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] == nBottomRightX && data[4] == nBottomRightY) {
			direction = BorderUtil.LEFT;
			widths[0] = 0;
			widths[1] = exBottom ? nBottomRightWidth : 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] != rowIndex || data[4] != colIndex) {
			// default border.
			direction = BorderUtil.LEFT;
			widths[0] = 0;
			widths[1] = 0;
			widths[2] = data[1];
			widths[3] = 0;
		}

		Rectangle r = new Rectangle(x + w - data[1] / 2 - data[1] % 2, ry, data[1], rh);

		if (colIndex == colCount - 1) {
			r.x = x + w - data[1];
		}

		if (data[0] != 0) {
			g.setForegroundColor(ColorManager.getColor(data[2]));
			BorderUtil.drawBorderLine(g, direction, data[0], widths, r);
		} else if (data[1] > 0) {
			// draw default border;
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			BorderUtil.drawDefaultLine(g, direction, r);
		}
	}

	/**
	 * @param g
	 * @param rowIndex
	 * @param colIndex
	 * @param data     [style][width][color][rowIndex][colIndex].
	 */
	private void drawTop(Graphics g, int rowIndex, int colIndex, int x, int y, int w, int h, int[] data) {
		if (data[0] == 0 && data[1] == 0) {
			return;
		}

		int nLeftWidth = 0;
		int nLeftStyle = 0;
		int nLeftX = -1;
		int nLeftY = -1;

		if (colIndex > 0) {
			int[] nLeft = borderData[(rowIndex) * (2 * colCount + 1) + colIndex - 1];
			nLeftWidth = nLeft[1];
			nLeftStyle = nLeft[0];
			nLeftX = nLeft[3];
			nLeftY = nLeft[4];
		}

		int nRightWidth = 0;
		int nRightStyle = 0;
		int nRightX = -1;
		int nRightY = -1;

		if (colIndex < colCount - 1) {
			int[] nRight = borderData[(rowIndex) * (2 * colCount + 1) + colIndex + 1];
			nRightWidth = nRight[1];
			nRightStyle = nRight[0];
			nRightX = nRight[3];
			nRightY = nRight[4];
		}

		int nLeftTopWidth = 0;
		int nLeftTopStyle = 0;
		int nLeftTopX = -1;
		int nLeftTopY = -1;
		if (rowIndex > 0 && colIndex >= 0) {
			int[] nLeftTop = borderData[(rowIndex - 1) * (2 * colCount + 1) + colCount + colIndex];
			nLeftTopWidth = nLeftTop[1];
			nLeftTopStyle = nLeftTop[0];
			nLeftTopX = nLeftTop[3];
			nLeftTopY = nLeftTop[4];
		}

		int nLeftBottomWidth = 0;
		int nLeftBottomStyle = 0;
		int nLeftBottomX = -1;
		int nLeftBottomY = -1;
		if (rowIndex <= rowCount - 1 && colIndex >= 0) {
			int[] nLeftBottom = borderData[(rowIndex) * (2 * colCount + 1) + colCount + colIndex];
			nLeftBottomWidth = nLeftBottom[1];
			nLeftBottomStyle = nLeftBottom[0];
			nLeftBottomX = nLeftBottom[3];
			nLeftBottomY = nLeftBottom[4];
		}

		int nRightTopWidth = 0;
		int nRightTopStyle = 0;
		int nRightTopX = -1;
		int nRightTopY = -1;
		if (rowIndex > 0 && colIndex <= colCount - 1) {
			int[] nRightTop = borderData[(rowIndex - 1) * (2 * colCount + 1) + colCount + colIndex + 1];
			nRightTopWidth = nRightTop[1];
			nRightTopStyle = nRightTop[0];
			nRightTopX = nRightTop[3];
			nRightTopY = nRightTop[4];
		}

		int nRightBottomWidth = 0;
		int nRightBottomStyle = 0;
		int nRightBottomX = -1;
		int nRightBottomY = -1;
		if (rowIndex <= rowCount - 1 && colIndex <= colCount - 1) {
			int[] nRightBottom = borderData[(rowIndex) * (2 * colCount + 1) + colCount + colIndex + 1];
			nRightBottomWidth = nRightBottom[1];
			nRightBottomStyle = nRightBottom[0];
			nRightBottomX = nRightBottom[3];
			nRightBottomY = nRightBottom[4];
		}

		int nlexWidth = Math.max(nLeftTopWidth, nLeftBottomWidth);

		boolean exLeft = false;
		boolean exRight = false;

		int rx = (colIndex == 0) ? (x + nlexWidth) : (x + nlexWidth / 2);
		int rw = (colIndex == 0) ? (w - nlexWidth) : (w - nlexWidth / 2);

		if (TableBorderCollisionArbiter.canExtend(data, nLeftWidth, nLeftStyle, nLeftBottomWidth, nLeftBottomStyle,
				nLeftBottomX, nLeftBottomY, nLeftTopWidth, nLeftTopStyle, nLeftTopX, nLeftTopY, true, false)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nLeftWidth, nLeftStyle, nLeftX, nLeftY,
						nLeftBottomWidth, nLeftBottomStyle, nLeftBottomX, nLeftBottomY, nLeftTopWidth, nLeftTopStyle,
						nLeftTopX, nLeftTopY, true, false)
				|| (data[3] == -2)) {
			rx = (colIndex == 0) ? (x) : (x - nlexWidth / 2 - nlexWidth % 2);
			rw = (colIndex == 0) ? (w) : (w + nlexWidth / 2 + nlexWidth % 2);

			exLeft = true;
		}

		int nrexWidth = Math.max(nRightTopWidth, nRightBottomWidth);

		if (TableBorderCollisionArbiter.canExtend(data, nRightWidth, nRightStyle, nRightTopWidth, nRightTopStyle,
				nRightTopX, nRightTopY, nRightBottomWidth, nRightBottomStyle, nRightBottomX, nRightBottomY, false,
				false)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nRightWidth, nRightStyle, nRightX, nRightY,
						nRightTopWidth, nRightTopStyle, nRightTopX, nRightTopY, nRightBottomWidth, nRightBottomStyle,
						nRightBottomX, nRightBottomY, false, false)
				|| (data[3] == -2 && rowIndex == colCount - 1)) {
			rw += ((colIndex == colCount - 1) ? (0) : (nrexWidth / 2));

			exRight = true;
		} else {
			rw -= ((colIndex == colCount - 1) ? (nrexWidth) : (nrexWidth / 2 + nrexWidth % 2));
		}

		int direction = BorderUtil.TOP;
		int[] widths = new int[] { data[1], 0, 0, 0 };

		if (data[3] == nLeftTopX && data[4] == nLeftTopY && data[3] == nRightTopX && data[4] == nRightTopY) {
			direction = BorderUtil.BOTTOM;
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = exLeft ? nLeftTopWidth : 0;
			widths[3] = exRight ? nRightTopWidth : 0;
		} else if (data[3] == nLeftTopX && data[4] == nLeftTopY) {
			direction = BorderUtil.BOTTOM;
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = exLeft ? nLeftTopWidth : 0;
			widths[3] = 0;
		} else if (data[3] == nRightTopX && data[4] == nRightTopY) {
			direction = BorderUtil.BOTTOM;
			widths[0] = 0;
			widths[1] = data[1];
			widths[2] = 0;
			widths[3] = exRight ? nRightTopWidth : 0;
		} else if (data[3] == nLeftBottomX && data[4] == nLeftBottomY && data[3] == nRightBottomX
				&& data[4] == nRightBottomY) {
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = exLeft ? nLeftBottomWidth : 0;
			widths[3] = exRight ? nRightBottomWidth : 0;
		} else if (data[3] == nLeftBottomX && data[4] == nLeftBottomY) {
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = exLeft ? nLeftBottomWidth : 0;
			widths[3] = 0;
		} else if (data[3] == nRightBottomX && data[4] == nRightBottomY) {
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = exRight ? nRightBottomWidth : 0;
		} else if (data[3] != rowIndex || data[4] != colIndex) {
			// default border.
			direction = BorderUtil.TOP;
			widths[0] = data[1];
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = 0;
		}

		Rectangle r = new Rectangle(rx, y + h - data[1] / 2 - data[1] % 2, rw, data[1]);

		if (rowIndex == 0) {
			r.y = y;
		}

		if (data[0] != 0) {
			g.setForegroundColor(ColorManager.getColor(data[2]));
			BorderUtil.drawBorderLine(g, direction, data[0], widths, r);
		} else if (data[1] > 0) {
			// draw default border;
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			BorderUtil.drawDefaultLine(g, direction, r);
		}
	}

	/**
	 * @param g
	 * @param rowIndex
	 * @param colIndex
	 * @param data     [style][width][color][rowIndex][colIndex].
	 */
	private void drawLeft(Graphics g, int rowIndex, int colIndex, int x, int y, int w, int h, int[] data) {
		if (data[0] == 0 && data[1] == 0) {
			return;
		}

		int nTopWidth = 0;
		int nTopStyle = 0;
		int nTopX = -1;
		int nTopY = -1;

		if (rowIndex > 0) {
			int[] nTop = borderData[(rowIndex - 1) * (2 * colCount + 1) + colCount + colIndex];
			nTopWidth = nTop[1];
			nTopStyle = nTop[0];
			nTopX = nTop[3];
			nTopY = nTop[4];
		}

		int nBottomWidth = 0;
		int nBottomStyle = 0;
		int nBottomX = -1;
		int nBottomY = -1;

		if (rowIndex < rowCount - 1) {
			int[] nBottom = borderData[(rowIndex + 1) * (2 * colCount + 1) + colCount + colIndex];
			nBottomWidth = nBottom[1];
			nBottomStyle = nBottom[0];
			nBottomX = nBottom[3];
			nBottomY = nBottom[4];
		}

		int nTopLeftWidth = 0;
		int nTopLeftStyle = 0;
		int nTopLeftX = -1;
		int nTopLeftY = -1;
		if (colIndex > 0 && rowIndex >= 0) {
			int[] nTopLeft = borderData[rowIndex * (2 * colCount + 1) + colIndex - 1];
			nTopLeftWidth = nTopLeft[1];
			nTopLeftStyle = nTopLeft[0];
			nTopLeftX = nTopLeft[3];
			nTopLeftY = nTopLeft[4];
		}

		int nTopRightWidth = 0;
		int nTopRightStyle = 0;
		int nTopRightX = -1;
		int nTopRightY = -1;
		if (colIndex >= 0 && rowIndex >= 0) {
			int[] nTopRight = borderData[rowIndex * (2 * colCount + 1) + colIndex];
			nTopRightWidth = nTopRight[1];
			nTopRightStyle = nTopRight[0];
			nTopRightX = nTopRight[3];
			nTopRightY = nTopRight[4];
		}

		int nBottomLeftWidth = 0;
		int nBottomLeftStyle = 0;
		int nBottomLeftX = -1;
		int nBottomLeftY = -1;
		if (colIndex > 0 && rowIndex <= rowCount - 1) {
			int[] nBottomLeft = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex - 1];
			nBottomLeftWidth = nBottomLeft[1];
			nBottomLeftStyle = nBottomLeft[0];
			nBottomLeftX = nBottomLeft[3];
			nBottomLeftY = nBottomLeft[4];
		}

		int nBottomRightWidth = 0;
		int nBottomRightStyle = 0;
		int nBottomRightX = -1;
		int nBottomRightY = -1;
		if (colIndex >= 0 && rowIndex <= rowCount - 1) {
			int[] nBottomRight = borderData[(rowIndex + 1) * (2 * colCount + 1) + colIndex];
			nBottomRightWidth = nBottomRight[1];
			nBottomRightStyle = nBottomRight[0];
			nBottomRightX = nBottomRight[3];
			nBottomRightY = nBottomRight[4];
		}

		int ntexWidth = Math.max(nTopLeftWidth, nTopRightWidth);

		boolean exTop = false;
		boolean exBottom = false;

		int ry = (rowIndex == 0) ? (y + ntexWidth) : (y + ntexWidth / 2);
		int rh = (rowIndex == 0) ? (h - ntexWidth) : (h - ntexWidth / 2);

		if (TableBorderCollisionArbiter.canExtend(data, nTopWidth, nTopStyle, nTopLeftWidth, nTopLeftStyle, nTopLeftX,
				nTopLeftY, nTopRightWidth, nTopRightStyle, nTopRightX, nTopRightY, true, true)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nTopWidth, nTopStyle, nTopX, nTopY, nTopLeftWidth,
						nTopLeftStyle, nTopLeftX, nTopLeftY, nTopRightWidth, nTopRightStyle, nTopRightX, nTopRightY,
						true, true)
				|| (data[3] == -2)) {
			ry = (rowIndex == 0) ? (y) : (y - ntexWidth / 2 - ntexWidth % 2);
			rh = (rowIndex == 0) ? (h) : (h + ntexWidth / 2 + ntexWidth % 2);

			exTop = true;
		}

		int nbexWidth = Math.max(nBottomLeftWidth, nBottomRightWidth);

		if (TableBorderCollisionArbiter.canExtend(data, nBottomWidth, nBottomStyle, nBottomRightWidth,
				nBottomRightStyle, nBottomRightX, nBottomRightY, nBottomLeftWidth, nBottomLeftStyle, nBottomLeftX,
				nBottomLeftY, false, true)
				|| TableBorderCollisionArbiter.isBrotherWin(data, nBottomWidth, nBottomStyle, nBottomX, nBottomY,
						nBottomRightWidth, nBottomRightStyle, nBottomRightX, nBottomRightY, nBottomLeftWidth,
						nBottomLeftStyle, nBottomLeftX, nBottomLeftY, false, true)
				|| (data[3] == -2 && rowIndex == rowCount - 1)) {
			rh += ((rowIndex == rowCount - 1) ? (0) : (nbexWidth / 2));

			exBottom = true;
		} else {
			rh -= ((rowIndex == rowCount - 1) ? (nbexWidth) : (nbexWidth / 2 + nbexWidth % 2));
		}

		int direction = BorderUtil.LEFT;
		int[] widths = new int[] { 0, 0, data[1], 0 };

		if (data[3] == nTopLeftX && data[4] == nTopLeftY && data[3] == nBottomLeftX && data[4] == nBottomLeftY) {
			direction = BorderUtil.RIGHT;
			widths[0] = exTop ? nTopLeftWidth : 0;
			widths[1] = exBottom ? nBottomLeftWidth : 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nTopLeftX && data[4] == nTopLeftY) {
			direction = BorderUtil.RIGHT;
			widths[0] = exTop ? nTopLeftWidth : 0;
			widths[1] = 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nBottomLeftX && data[4] == nBottomLeftY) {
			direction = BorderUtil.RIGHT;
			widths[0] = 0;
			widths[1] = exBottom ? nBottomLeftWidth : 0;
			widths[2] = 0;
			widths[3] = data[1];
		} else if (data[3] == nTopRightX && data[4] == nTopRightY && data[3] == nBottomRightX
				&& data[4] == nBottomRightY) {
			widths[0] = exTop ? nTopRightWidth : 0;
			widths[1] = exBottom ? nBottomRightWidth : 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] == nTopRightX && data[4] == nTopRightY) {
			widths[0] = exTop ? nTopRightWidth : 0;
			widths[1] = 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] == nBottomRightX && data[4] == nBottomRightY) {
			widths[0] = 0;
			widths[1] = exBottom ? nBottomRightWidth : 0;
			widths[2] = data[1];
			widths[3] = 0;
		} else if (data[3] != rowIndex || data[4] != colIndex) {
			// default border.
			direction = BorderUtil.LEFT;
			widths[0] = 0;
			widths[1] = 0;
			widths[2] = data[1];
			widths[3] = 0;
		}

		Rectangle r = new Rectangle(x - data[1] / 2, ry, data[1], rh);

		if (colIndex == 0) {
			r.x = x;
		}

		if (data[0] != 0) {
			g.setForegroundColor(ColorManager.getColor(data[2]));
			BorderUtil.drawBorderLine(g, direction, data[0], widths, r);
		} else if (data[1] > 0) {
			// draw default border;
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			BorderUtil.drawDefaultLine(g, direction, r);
		}
	}
}
