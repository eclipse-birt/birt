/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitTableBand(this, value);
	}
}
