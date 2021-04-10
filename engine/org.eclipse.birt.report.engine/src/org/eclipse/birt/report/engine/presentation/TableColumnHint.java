/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
