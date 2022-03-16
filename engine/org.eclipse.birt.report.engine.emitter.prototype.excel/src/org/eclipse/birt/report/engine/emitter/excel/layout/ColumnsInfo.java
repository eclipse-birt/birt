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
