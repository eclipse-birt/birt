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

package org.eclipse.birt.report.engine.ir;

/**
 * Band used in a TableItem.
 *
 */
public class TableBandDesign extends BandDesign {

	/**
	 * get the row number defined in this band.
	 *
	 * @return row number
	 */
	public int getRowCount() {
		return getContentCount();
	}

	/**
	 * add a row definition in this band.
	 *
	 * @param row row to be added.
	 */
	public void addRow(RowDesign row) {
		assert (row != null);
		addContent(row);
	}

	/**
	 * get row in this band.
	 *
	 * @param index row index
	 * @return row.
	 */
	public RowDesign getRow(int index) {
		return (RowDesign) getContent(index);
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitTableBand(this, value);
	}
}
