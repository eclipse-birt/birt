/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;

public interface IPDFTableLayoutManager {

	void startRow(IRowContent content);

	void updateRow(RowArea area, int specifiedHeight);

	int getXPos(int columnID);

	void startCell(ICellContent cellContent);

	int getCellWidth(int startColumn, int endColumn);

	void resolveBorderConflict(CellArea area);

	TableLayoutInfo getLayoutInfo();

	public class TableLayoutInfo {

		public TableLayoutInfo(int[] colWidth) {
			this.colWidth = colWidth;
			this.columnNumber = colWidth.length;
			this.xPositions = new int[columnNumber];
			this.tableWidth = 0;
			for (int i = 0; i < columnNumber; i++) {
				xPositions[i] = tableWidth;
				tableWidth += colWidth[i];
			}

		}

		public int getTableWidth() {
			return this.tableWidth;
		}

		public int getXPosition(int index) {
			return xPositions[index];
		}

		/**
		 * get cell width
		 *
		 * @param startColumn
		 * @param endColumn
		 * @return
		 */
		public int getCellWidth(int startColumn, int endColumn) {
			assert (startColumn < endColumn);
			assert (colWidth != null);
			int sum = 0;
			for (int i = startColumn; i < endColumn; i++) {
				sum += colWidth[i];
			}
			return sum;
		}

		protected int columnNumber;

		protected int tableWidth;
		/**
		 * Array of column width
		 */
		protected int[] colWidth = null;

		/**
		 * array of position for each column
		 */
		protected int[] xPositions = null;

	}

}
