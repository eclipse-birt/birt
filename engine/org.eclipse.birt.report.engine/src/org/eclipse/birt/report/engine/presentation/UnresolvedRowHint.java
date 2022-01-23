/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;

public class UnresolvedRowHint {
	final static short FIELD_TABLE_ID = 0;
	final static short FIELD_ROW_ID = 1;
	final static short FIELD_SPAN_CELL_INFO = 2;
	final static short FIELD_NONE = 100;
	protected String tableId;
	protected String rowId;
	protected ArrayList cells = new ArrayList();

	public UnresolvedRowHint() {
	}

	public UnresolvedRowHint(String tableId, String rowId) {
		this.tableId = tableId;
		this.rowId = rowId;
	}

	public void addUnresolvedCell(String style, int colId, int colSpan, int rowSpan) {
		cells.add(new SpannedCellInfo(style, colId, colSpan, rowSpan));
	}

	public ICellContent initUnresolvedCell(ICellContent cell, InstanceID rowId, int colId) {
		int rowSpan = 1;
		int colSpan = 1;
		SpannedCellInfo cellInfo = getSpannedCellInfo(colId);
		if (cellInfo != null) {
			rowSpan = cellInfo.rowSpan;
			if (rowSpan > 1) {
				if (!equals(rowId, this.rowId)) {
					rowSpan--;
				}
			}
			colSpan = cellInfo.colSpan;
			if (cellInfo.style != null && cellInfo.style.length() > 0) {
				CSSEngine engine = cell.getCSSEngine();
				StyleDeclaration style = new StyleDeclaration(engine);
				style.setCssText(cellInfo.style);
				cell.setInlineStyle(style);
			}
		}
		cell.setColumn(colId);
		cell.setRowSpan(rowSpan);
		cell.setColSpan(colSpan);
		return cell;
	}

	public boolean isDropColumn(int colId) {
		Iterator iter = cells.iterator();
		while (iter.hasNext()) {
			SpannedCellInfo cellInfo = (SpannedCellInfo) iter.next();
			if (cellInfo.colId == colId & cellInfo.rowSpan < 0) {
				return true;
			}
		}
		return false;
	}

	protected SpannedCellInfo getSpannedCellInfo(int colId) {
		Iterator iter = cells.iterator();
		while (iter.hasNext()) {
			SpannedCellInfo cellInfo = (SpannedCellInfo) iter.next();
			if (cellInfo.colId == colId) {
				return cellInfo;
			}
		}
		return null;
	}

	protected boolean equals(InstanceID rowId, String id) {
		if (rowId != null && id != null) {
			return rowId.toUniqueString().equals(id);
		}
		return false;
	}

	public String getTableId() {
		return this.tableId;
	}

	public String getRowId() {
		return this.rowId;
	}

	public void writeObject(DataOutputStream out) throws IOException {
		if (tableId != null) {
			IOUtil.writeShort(out, FIELD_TABLE_ID);
			IOUtil.writeString(out, tableId.toString());
		}
		if (rowId != null) {
			IOUtil.writeShort(out, FIELD_ROW_ID);
			IOUtil.writeString(out, rowId.toString());
		}
		if (cells.size() > 0) {
			Iterator iter = cells.iterator();
			while (iter.hasNext()) {
				SpannedCellInfo cell = (SpannedCellInfo) iter.next();
				IOUtil.writeShort(out, FIELD_SPAN_CELL_INFO);
				cell.writeObject(out);
			}

		}
		IOUtil.writeShort(out, FIELD_NONE);
	}

	public void readObject(DataInputStream in) throws IOException {
		int filedId = 0;
		while (filedId != FIELD_NONE) {
			filedId = IOUtil.readShort(in);
			switch (filedId) {
			case FIELD_TABLE_ID:
				tableId = IOUtil.readString(in);
				break;
			case FIELD_ROW_ID:
				rowId = IOUtil.readString(in);
				break;
			case FIELD_SPAN_CELL_INFO:
				SpannedCellInfo cell = new SpannedCellInfo();
				cell.readObject(in);
				cells.add(cell);
				break;
			default:
				break;

			}
		}
	}

	protected static class SpannedCellInfo {
		final static short FIELD_STYLE = 0;
		final static short FIELD_COLID = 1;
		final static short FIELD_ROWSPAN = 2;
		final static short FIELD_COLSPAN = 3;
		final static short FIELD_NONE = 100;

		protected String style;
		protected int rowSpan;
		protected int colId;
		protected int colSpan;

		public SpannedCellInfo() {
		}

		public SpannedCellInfo(String style, int colId, int colSpan, int rowSpan) {
			this.style = style;
			this.colId = colId;
			this.rowSpan = rowSpan;
			this.colSpan = colSpan;
		}

		public void writeObject(DataOutputStream out) throws IOException {
			if (style != null) {
				IOUtil.writeShort(out, FIELD_STYLE);
				IOUtil.writeString(out, style);
			}
			IOUtil.writeShort(out, FIELD_COLID);
			IOUtil.writeInt(out, colId);

			IOUtil.writeShort(out, FIELD_ROWSPAN);
			IOUtil.writeInt(out, rowSpan);

			IOUtil.writeShort(out, FIELD_COLSPAN);
			IOUtil.writeInt(out, colSpan);

			IOUtil.writeShort(out, FIELD_NONE);
		}

		public void readObject(DataInputStream in) throws IOException {
			int filedId = 0;
			while (filedId != FIELD_NONE) {
				filedId = IOUtil.readShort(in);
				switch (filedId) {
				case FIELD_STYLE:
					style = IOUtil.readString(in);
					break;
				case FIELD_COLID:
					colId = IOUtil.readInt(in);
					break;
				case FIELD_ROWSPAN:
					rowSpan = IOUtil.readInt(in);
					break;
				case FIELD_COLSPAN:
					colSpan = IOUtil.readInt(in);
					break;
				default:
					break;
				}
			}
		}
	}
}
