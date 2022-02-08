/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor.buffermgr;

/**
 * 
 */

public class CellUtil {

	public static int getColId(Cell cell) {
		return cell.colId;
	}

	public static int getStatus(Cell cell) {
		return cell.status;
	}

	public static int getRowId(Cell cell) {
		return cell.rowId;
	}

	public static int getRowSpan(Cell cell) {
		return cell.rowSpan;
	}

	public static int getColSpan(Cell cell) {
		return cell.colSpan;
	}

	public static Object getContent(Cell cell) {
		return cell.content;
	}

	public static Cell getCell(Cell cell) {
		return cell.cell;
	}
}
