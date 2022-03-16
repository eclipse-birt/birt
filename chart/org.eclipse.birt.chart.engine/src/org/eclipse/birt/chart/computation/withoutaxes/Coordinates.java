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

package org.eclipse.birt.chart.computation.withoutaxes;

/**
 * Coordinates
 */
public final class Coordinates {

	private final int iRow;

	private final int iColumn;

	/**
	 * The constructor.
	 *
	 * @param iColumn
	 * @param iRow
	 */
	Coordinates(int iColumn, int iRow) {
		this.iColumn = iColumn;
		this.iRow = iRow;
	}

	/**
	 * @return Returns the column.
	 */
	public int getColumn() {
		return iColumn;
	}

	/**
	 * @return Returns the row.
	 */
	public int getRow() {
		return iRow;
	}
}
