/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.ods.layout;

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
