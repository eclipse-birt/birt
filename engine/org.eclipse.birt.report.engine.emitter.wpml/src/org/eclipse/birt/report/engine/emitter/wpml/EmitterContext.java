/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.birt.report.engine.content.IStyle;

/**
 * Emitter context class for WordProcessingML
 *
 * @since 3.3
 *
 */
public class EmitterContext {

	private LinkedList<TableInfo> tables = new LinkedList<>();

	private LinkedList<Integer> widthList = new LinkedList<>();

	private Stack<Boolean> cellind = new Stack<>();

	private boolean isFirst = true;

	private boolean isAfterTable = false;

	/**
	 * Set the flag to show if we just finished a table element.
	 *
	 * @param isTable
	 */
	public void setIsAfterTable(boolean isTable) {
		this.isAfterTable = isTable;
	}

	/**
	 * Checks if we just finished a table element
	 *
	 * @return check if the position after a table element
	 */
	public boolean isAfterTable() {
		return this.isAfterTable;
	}

	/**
	 * Set start inline
	 */
	public void startInline() {
		isFirst = false;
	}

	/**
	 * Is first inline
	 *
	 * @return is first inline
	 */
	public boolean isFirstInline() {
		return isFirst;
	}

	/**
	 * Set end inline
	 */
	public void endInline() {
		isFirst = true;
	}

	/**
	 * Set start cell
	 */
	public void startCell() {
		cellind.push(true);
	}

	/**
	 * Remove cell from cell list by end of cell
	 */
	public void endCell() {
		cellind.pop();
	}

	/**
	 * Empty paragraph is needed
	 *
	 * @return is empty paragraph is needed
	 */
	public boolean needEmptyP() {
		return cellind.peek();
	}

	/**
	 * Add container
	 *
	 * @param isContainer is container flag
	 */
	public void addContainer(boolean isContainer) {
		if (!cellind.isEmpty()) {
			cellind.pop();
			cellind.push(isContainer);
		}
	}

	/**
	 * Add width to registry
	 *
	 * @param witdh width
	 */
	public void addWidth(int witdh) {
		widthList.addLast(witdh);
	}

	/**
	 * Reset width registry
	 */
	public void resetWidth() {
		widthList.clear();
	}

	/**
	 * Get current width from registry
	 *
	 * @return current width from registry
	 */
	public int getCurrentWidth() {
		return widthList.getLast();
	}

	/**
	 * Remove width from registry
	 */
	public void removeWidth() {
		widthList.removeLast();
	}

	/**
	 * Get current table columns
	 *
	 * @return the current table columns
	 */
	public int[] getCurrentTableColmns() {
		return tables.getLast().getColumnWidths();
	}

	/**
	 * Add table
	 *
	 * @param cols  table columns
	 * @param style table style
	 */
	public void addTable(int[] cols, IStyle style) {
		tables.addLast(new TableInfo(cols, style));
	}

	/**
	 * Get the table style
	 *
	 * @return the table style
	 */
	public IStyle getTableStyle() {
		return tables.getLast().getTableStyle();
	}

	/**
	 * Create new row
	 */
	public void newRow() {
		tables.getLast().newRow();
	}

	/**
	 * Add span
	 *
	 * @param colmunId   column id
	 * @param columnSpan column span count
	 * @param cellWidth  cell width
	 * @param rowSpan    row span count
	 * @param style      cell style
	 */
	public void addSpan(int colmunId, int columnSpan, int cellWidth, int rowSpan, IStyle style) {
		tables.getLast().addSpan(colmunId, columnSpan, cellWidth, rowSpan, style);
	}

	/**
	 * Remove table
	 */
	public void removeTable() {
		tables.removeLast();
	}

	/**
	 * Get column span
	 *
	 * @param col column
	 * @return span info
	 */
	public List<SpanInfo> getSpans(int col) {
		return tables.getLast().getSpans(col);
	}

	/**
	 * Get the cell width
	 *
	 * @param columnId   column id
	 * @param columnSpan column span
	 * @return the cell width
	 */
	public int getCellWidth(int columnId, int columnSpan) {
		int[] cols = getCurrentTableColmns();

		int width = 0;

		int colNum = Math.min(columnId + columnSpan, DocEmitter.MAX_COLUMN);

		for (int i = columnId; i < colNum; i++) {
			width += cols[i];
		}

		return width;
	}

	static class TableInfo {

		private Hashtable<Integer, List<SpanInfo>> spans = new Hashtable<>();

		private int[] cols;

		private int crow = 0;

		IStyle style = null;

		TableInfo(int[] cols, IStyle style) {
			this.cols = cols;
			this.style = style;
		}

		void newRow() {
			this.crow++;
		}

		void addSpan(int columnId, int columnSpan, int cellWidth, int rowSpan, IStyle style) {
			for (int i = 1; i < rowSpan; i++) {
				Integer key = crow + i;

				if (spans.containsKey(key)) {
					List<SpanInfo> rSpan = spans.get(key);
					rSpan.add(new SpanInfo(columnId, columnSpan, cellWidth, false, style));
					Collections.sort(rSpan, new Comparator<SpanInfo>() {

						@Override
						public int compare(SpanInfo o1, SpanInfo o2) {
							SpanInfo r1 = o1;
							SpanInfo r2 = o2;
							return r1.getColumnId() - r2.getColumnId();
						}
					});
				} else {
					Vector<SpanInfo> rSpan = new Vector<>();
					rSpan.add(new SpanInfo(columnId, columnSpan, cellWidth, false, style));
					spans.put(key, rSpan);
				}
			}
		}

		List<SpanInfo> getSpans(int end) {
			List<SpanInfo> cSpans = spans.get(crow);

			if (cSpans == null) {
				return null;
			}

			Vector<SpanInfo> cList = new Vector<>();

			int pos = -1;

			for (int i = 0; i < cSpans.size(); i++) {
				SpanInfo r = cSpans.get(i);

				if ((r.getColumnId() + r.getColumnSpan() - 1) <= end) {

					cList.add(r);

					pos = i;
				} else {
					break;
				}
			}

			for (int i = 0; i <= pos; i++) {
				cSpans.remove(0);
			}

			if (cSpans.size() == 0) {
				removeSpan();
			}

			return cList.size() == 0 ? null : cList;
		}

		public void removeSpan() {
			spans.remove(crow);
		}

		int[] getColumnWidths() {
			return cols;
		}

		int getRow() {
			return crow;
		}

		IStyle getTableStyle() {
			return this.style;
		}
	}
}
