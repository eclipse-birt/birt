/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.presentation;

public class TableColumnHint {
	protected int start;
	protected int columnCount;
	protected String tableId;

	public TableColumnHint(String tableId, int start, int columnCount) {
		this.tableId = tableId;
		this.start = start;
		this.columnCount = columnCount;
	}

	public int getStart() {
		return start;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public String getTableId() {
		return this.tableId;
	}

	public String toString() {
		int end = start + columnCount;
		return tableId + "-" + start + "-" + end;
	}
}
